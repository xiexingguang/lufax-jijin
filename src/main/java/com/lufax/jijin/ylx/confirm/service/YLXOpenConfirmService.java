package com.lufax.jijin.ylx.confirm.service;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.lufax.jersey.utils.Logger;
import com.lufax.jijin.base.constant.FundName;
import com.lufax.jijin.base.utils.JijinAppProperties;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.dto.FundAccountDTO;
import com.lufax.jijin.repository.FundAccountRepository;
import com.lufax.jijin.ylx.batch.domain.YLXBatchFileStatus;
import com.lufax.jijin.ylx.batch.domain.YLXBatchStatus2;
import com.lufax.jijin.ylx.batch.dto.YLXBatchDTO;
import com.lufax.jijin.ylx.batch.dto.YLXBatchFileDTO;
import com.lufax.jijin.ylx.batch.repository.YLXBatchFileRepository;
import com.lufax.jijin.ylx.batch.repository.YLXBatchRepository;
import com.lufax.jijin.ylx.dto.OpenConfirmDetailDTO;
import com.lufax.jijin.ylx.dto.YLXOpenRequestDetailDTO;
import com.lufax.jijin.ylx.dto.repository.OpenConfirmDetailRepository;
import com.lufax.jijin.ylx.dto.repository.YLXOpenRequestDetailRepository;
import com.lufax.jijin.ylx.exception.YlxBatchErrorCode;
import com.lufax.jijin.ylx.exception.YlxBatchException;
import com.lufax.jijin.ylx.enums.YLXOpenRequestStatus;
import com.site.lookup.util.StringUtils;

@Service
@Scope
public class YLXOpenConfirmService {

    @Autowired
    private YLXBatchRepository ylxBatchRepository;
    @Autowired
    private YLXBatchFileRepository ylxBatchFileRepository;
    @Autowired
    private JijinAppProperties jijinAppProperties;
    @Autowired
    private OpenConfirmDetailRepository openConfirmDetailDAO;
    @Autowired
    private BaseConfirmService baseConfirmService;
    @Autowired
    private YLXOpenRequestDetailRepository ylxOpenRequestDetailRepository;
    @Autowired
    private FundAccountRepository fundAccountRepository;
    @Autowired
    private OpenConfirmHandler openConfirmHandler;
    
    private static final long ROWNUM = 500l;//每批次处理这么多行
    
    /**
     * 解析BatchFile,将相应的确认记录读入ylx_open_confirm_detail,更新Batch状态
     * @param ylxBatch
     */
    public void handleOpenConfirmData(YLXBatchDTO ylxBatch) {
        try {
            List<YLXBatchFileDTO> ylxBatchFileDTOs = ylxBatchFileRepository.getYLXBatchFileDTOsByBatchIdAndStatus(ylxBatch.getId(), YLXBatchFileStatus.created.name());
            //解析BatchFile,将相应的确认记录读入ylx_open_confirm
            for (YLXBatchFileDTO batchFile : ylxBatchFileDTOs) {
                dealFileWithBatchSize(batchFile.getCurrentLine(), ROWNUM, batchFile, ylxBatch);
                batchFile.setStatus(YLXBatchFileStatus.received.name());
                ylxBatchFileRepository.updateByIdSelective(batchFile);
            }
            //处理完更新主状态为received已接收文件
            ylxBatchRepository.updateBatchStatusById(ylxBatch.getId(), YLXBatchStatus2.RECEIVED.name());
        } catch (Exception e) {
            //发送失败超时短信
            baseConfirmService.sendHandleConfirmFailSMSIfTimeout(ylxBatch, "10:00","10:30");
            // any runtime exception, try to rerun
            long times = ylxBatch.getRetryTimes() + 1;
            String msg = e.getMessage();
            if (!StringUtils.isEmpty(msg) && msg.length() >= 4000)
                msg = msg.substring(0, 3999);
            //同步文件失败，保持主状态不变，下次job 运行仍然继续处理“received_prepare”状态Batch
            ylxBatchRepository.updateYLXBatchRetryTimes(ylxBatch.getId(), msg, String.valueOf(ylxBatch.getId()));
            Logger.error(this, "sync open confirm file Failed ! retry times:" + times + " batchId:" + ylxBatch.getId(), e);

        }

    }
    
    /**
     * 第一步 对ylx_open_confirm_detail 和 ylx_open_request_detail 对账
     * 第二步 对于返回状态成功的记录save到bus_fund_account
     * @param ylxBatch
     */
    public void saveConfirmDataToAccount(YLXBatchDTO ylxBatch){
    	List<YLXOpenRequestDetailDTO> openRequests = new ArrayList<YLXOpenRequestDetailDTO>();
    	do {
            openRequests = ylxOpenRequestDetailRepository.getOpeningOpenRequests(ylxBatch.getReqBatchId(), YLXOpenRequestStatus.OPENING.name(), ROWNUM);
            Logger.info(this, String.format("open Requests size : %s.", openRequests.size()));
            if (CollectionUtils.isNotEmpty(openRequests)) {

                List<Long> requestIds = new ArrayList<Long>();
                List<Long> userIds = new ArrayList<Long>();

                for (int i = 0; i < openRequests.size(); i++) {
                	YLXOpenRequestDetailDTO dto = openRequests.get(i);
                    requestIds.add(dto.getId());
                    userIds.add(Long.valueOf(dto.getBankAccount()));
                }
                List<OpenConfirmDetailDTO> ylxOpenConfirmDTOs = openConfirmDetailDAO.getYlxOpenConfirmsByInternalTrxId(requestIds);
                List<FundAccountDTO> openAccountDTOs = fundAccountRepository.getOpenUserDTOsByIds(userIds);
                List<Long> alreadyOpenAccountUserIds = new ArrayList<Long>();
                for (FundAccountDTO fundAccountDTO : openAccountDTOs) {
                    alreadyOpenAccountUserIds.add(fundAccountDTO.getUserId());
                }

                List<YLXOpenRequestDetailDTO> needUpdateOpenRequests = new ArrayList<YLXOpenRequestDetailDTO>();
                Map<Long, OpenConfirmDetailDTO> openConfirmMap = new HashMap<Long, OpenConfirmDetailDTO>();
                for (OpenConfirmDetailDTO openConfirmDTO : ylxOpenConfirmDTOs) {
                    openConfirmMap.put(openConfirmDTO.getInternalTrxId(), openConfirmDTO);
                }
                for (YLXOpenRequestDetailDTO ylxOpenRequestDetailDTO : openRequests) {
                	ylxOpenRequestDetailDTO.setStatus(YLXOpenRequestStatus.UN_CONFIRM.getCode());
                    if (openConfirmMap.containsKey(ylxOpenRequestDetailDTO.getId())) {
                    	OpenConfirmDetailDTO ylxOpenConfirmDTO = openConfirmMap.get(ylxOpenRequestDetailDTO.getId());
                        if (ylxOpenConfirmDTO.getResultCode().equals("ETS-B0000")) {
                        	ylxOpenRequestDetailDTO.setStatus(YLXOpenRequestStatus.SUCCESS.name());
                            if (!alreadyOpenAccountUserIds.contains(Long.valueOf(ylxOpenRequestDetailDTO.getBankAccount()))) {
                                List<FundAccountDTO> accounts = fundAccountRepository.findBusFundAccount(MapUtils.buildKeyValueMap("userId",ylxOpenRequestDetailDTO.getBankAccount(),"fundName",FundName.YLX.name()));
                                if(null==accounts||(null!=accounts&&accounts.size()<=0)){
                                	Logger.info(this, String.format("insert fund account into bus_fund_account user id:%s", ylxOpenRequestDetailDTO.getBankAccount()));
                                	fundAccountRepository.insertBusFundAccount(new FundAccountDTO(Long.valueOf(ylxOpenRequestDetailDTO.getBankAccount()), ylxOpenConfirmDTO.getThirdCustomerAccount(), ylxOpenConfirmDTO.getThirdAccount()));
                                }
                            }
                        } else {
                        	ylxOpenRequestDetailDTO.setStatus(YLXOpenRequestStatus.FAIL.name());
                        }
                    }
                    needUpdateOpenRequests.add(ylxOpenRequestDetailDTO);
                }
                Logger.info(this, String.format("need Update Open Requests size : %s.", openRequests.size()));
                //fundAccountRepository.batchInsert(fundAccountDTOs);
                Logger.debug(this, "batch insert open confirm account to bus_fund_account.");
                ylxOpenRequestDetailRepository.batchUpdateOpenRequest(needUpdateOpenRequests);
                Logger.debug(this, "batch update open request status");
            }
        } while (CollectionUtils.isNotEmpty(openRequests));
    	//更新batch 为complete status
    	Logger.debug(this, "update batch status to complete.");
    	ylxBatchRepository.update("updateBatchStatusById",
    			MapUtils.buildKeyValueMap("batchId", ylxBatch.getId(), "status", YLXBatchStatus2.COMPLETE.name()));
    }
	
    /**
     * 解析BatchFile,将相应的确认记录读入ylx_open_confirm
     * @param startLine
     * @param rownum
     * @param batchFile
     * @param ylxBatchDTO
     * @throws Exception
     */
    private void dealFileWithBatchSize(long startLine, long rownum, YLXBatchFileDTO batchFile, YLXBatchDTO ylxBatchDTO) throws Exception {
        InputStreamReader in = new InputStreamReader(new FileInputStream(baseConfirmService.getBatchFile(batchFile)), "UTF-8");
        LineNumberReader reader = new LineNumberReader(in);
        try {
            String s = null;
            List<OpenConfirmDetailDTO> dtos = new ArrayList<OpenConfirmDetailDTO>();
            OpenConfirmReader contentReader = new OpenConfirmReader();
            do {
                s = reader.readLine();
                if (StringUtils.isEmpty(s)) {// 文件读取完毕,插入最后一批数据
                    Logger.info(this, String.format("open confirm insert last batch records into DB endline:%s]", reader.getLineNumber() - 1));
                    if (dtos.size() > 0)
                    	openConfirmHandler.batchInsertOpenConfirm(dtos, reader.getLineNumber(), batchFile);
                    break;
                }
                if (reader.getLineNumber() >= startLine && reader.getLineNumber() < startLine + rownum) {
                    if (reader.getLineNumber() == 3) {// header validation, any error throw exception
                        if (!contentReader.validateFileHeaderContent(s)){
                        	 throw new YlxBatchException(YlxBatchErrorCode.FILE_HEADER_ERROR.getCode(), "open confirm file header has error:" + s);
                        }
                    }
                    // 解析行，转换成DTO
                    OpenConfirmDetailDTO dto = contentReader.readLine(s, reader.getLineNumber(),ylxBatchDTO);
                    if (dto != null)
                        dtos.add(dto);
                }
                if (reader.getLineNumber() == startLine + rownum - 1) {// 已达到批次记录集，进行插数据库，清空缓存，并set下一个startLine
                    Logger.info(this, String.format("open confirm insert into DB endline:%s]", reader.getLineNumber()));
                    startLine = reader.getLineNumber() + 1;// set new startLine
                    openConfirmHandler.batchInsertOpenConfirm(dtos, startLine, batchFile);
                    dtos.clear();
                }
            } while (s != null);
        } finally {
            reader.close();
            in.close();
        }
    }
}
