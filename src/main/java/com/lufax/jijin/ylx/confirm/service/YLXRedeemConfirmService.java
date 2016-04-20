package com.lufax.jijin.ylx.confirm.service;

import com.lufax.jersey.utils.Logger;
import com.lufax.jijin.base.utils.DateUtils;
import com.lufax.jijin.base.utils.EmptyChecker;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.product.dto.ProductDTO;
import com.lufax.jijin.product.repository.ProductRepository;
import com.lufax.jijin.service.MqService;
import com.lufax.jijin.user.dto.UserDTO;
import com.lufax.jijin.user.repository.UserRepository;
import com.lufax.jijin.ylx.batch.domain.YLXBatchFileStatus;
import com.lufax.jijin.ylx.batch.domain.YLXBatchStatus2;
import com.lufax.jijin.ylx.batch.domain.YLXBatchType;
import com.lufax.jijin.ylx.batch.dto.YLXBatchDTO;
import com.lufax.jijin.ylx.batch.dto.YLXBatchFileDTO;
import com.lufax.jijin.ylx.batch.repository.YLXBatchFileRepository;
import com.lufax.jijin.ylx.batch.repository.YLXBatchRepository;
import com.lufax.jijin.ylx.dto.BuyConfirmDetailDTO;
import com.lufax.jijin.ylx.dto.YLXBuyRequestDetailDTO;
import com.lufax.jijin.ylx.dto.YLXSellConfirmDetailDTO;
import com.lufax.jijin.ylx.dto.YLXSellRequestDetailDTO;
import com.lufax.jijin.ylx.dto.repository.YLXSellConfirmDetailRepository;
import com.lufax.jijin.ylx.dto.repository.YLXSellRequestDetailRepository;
import com.lufax.jijin.ylx.enums.YLXSellRequestStatus;
import com.lufax.jijin.ylx.exception.YlxBatchErrorCode;
import com.lufax.jijin.ylx.exception.YlxBatchException;
import com.lufax.jijin.ylx.remote.YLXSmsService;
import com.site.lookup.util.StringUtils;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class YLXRedeemConfirmService {

    @Autowired
    private YLXBatchRepository ylxBatchRepository;
    @Autowired
    private YLXBatchFileRepository ylxBatchFileRepository;
    @Autowired
    private BaseConfirmService baseConfirmService;
    @Autowired
    private YLXSellConfirmDetailRepository sellConfirmDetailDAO;
    @Autowired
    private RedeemConfirmHandler redeemConfirmHandler;
    @Autowired
    private RedeemConfirmReader redeemConfirmReader;
    @Autowired
    private YLXSellRequestDetailRepository ylxSellRequestDetailRepository;
    @Autowired
    private MqService mqService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private YLXSmsService ylxSmsService;

    protected static final int ROWNUM = 500; // batchSize

    /**
     * 解析BatchFile,将相应的确认记录读入ylx_sell_confirm_detail,更新Batch状态为received
     * @param ylxBatch
     */
    public void handleRedeemConfirmData(YLXBatchDTO ylxBatch) {
        try {
           /* YLXBatchDTO investorProfitBatch = ylxBatchRepository.getYLXBatchDTOByTypeAndTargetDate(YLXBatchType.YLX_SLP_INVESTOR_PROFIT.name(), ylxBatch.getTargetDate());
            //首先判断投资人收益是否已经被处理过,如果没有,赎回确认不处理
            if (investorProfitBatch == null || !investorProfitBatch.getStatus().equalsIgnoreCase(YLXBatchStatus2.COMPLETE.name())) {
                Logger.info(this, String.format("sell confirm need after investor profit targetDate [%s]", ylxBatch.getTargetDate()));
                return;
            }*/
            List<YLXBatchFileDTO> ylxBatchFileDTOs = ylxBatchFileRepository.getYLXBatchFileDTOsByBatchIdAndStatus(ylxBatch.getId(), YLXBatchFileStatus.created.name());
            //解析BatchFile,将相应的确认记录读入ylx_sell_confirm_detail
            Logger.info(this, "sell confirm files ! " +ylxBatchFileDTOs.size());
            for(YLXBatchFileDTO batchFile:ylxBatchFileDTOs){
                Logger.info(this, String.format("sell confirm file name %s,current line %s",batchFile.getFileName(),batchFile.getCurrentLine()));
                dealFileWithBatchSize(batchFile.getCurrentLine(), ROWNUM, batchFile, ylxBatch);
                batchFile.setStatus(YLXBatchFileStatus.received.name());
                ylxBatchFileRepository.updateByIdSelective(batchFile);
            }
            //处理完更新主状态为received已接收文件
            ylxBatchRepository.update("updateBatchStatusById",
                    MapUtils.buildKeyValueMap("batchId", ylxBatch.getId(), "status", YLXBatchStatus2.RECEIVED.name()));
        } catch (Exception e) {
            //发送失败超时短信
            baseConfirmService.sendHandleConfirmFailSMSIfTimeout(ylxBatch, "15:00","15:30");
            // any runtime exception, try to rerun
            long times = ylxBatch.getRetryTimes() + 1;
            String msg = e.getMessage();
            if (!StringUtils.isEmpty(msg) && msg.length() >= 4000)
                msg = msg.substring(0, 3999);
            //同步文件失败，保持主状态不变，下次job 运行仍然继续处理“received_prepare”状态Batch
            ylxBatchRepository.updateYLXBatchRetryTimes(ylxBatch.getId(), msg, String.valueOf(ylxBatch.getId()));
            Logger.error(this, "sync sell confirm file Failed ! retry times:" + times + " batchId:" + ylxBatch.getId(), e);
        }
    }


    /**
     * 解析BatchFile,将相应的确认记录读入ylx_sell_confirm_detail
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
            List<YLXSellConfirmDetailDTO> dtos = new ArrayList<YLXSellConfirmDetailDTO>();
            for(String s=reader.readLine(); StringUtils.isNotEmpty(s);s=reader.readLine()){
            	
                if(reader.getLineNumber()<=startLine||reader.getLineNumber()<3)
                    continue;
                else if(reader.getLineNumber() == 3){
                    if (!redeemConfirmReader.validateFileHeaderContent(s)){
                        throw new YlxBatchException(YlxBatchErrorCode.FILE_HEADER_ERROR.getCode(), "redeem confirm file header has error:" + s);
                    }
                }else{
                    YLXSellConfirmDetailDTO dto = redeemConfirmReader.readLine(s, reader.getLineNumber(),ylxBatchDTO);
                    if (dto != null)
                        dtos.add(dto);
                    if(dtos.size() >= ROWNUM){
                        redeemConfirmHandler.batchInsertRedeemConfirm(dtos, reader.getLineNumber(), batchFile);
                        dtos.clear();
                    }
                }
            }
            if(!dtos.isEmpty()){
                redeemConfirmHandler.batchInsertRedeemConfirm(dtos, reader.getLineNumber(), batchFile);
            }
        } finally {
            reader.close();
            in.close();
        }
    }

    /**
     * 1.赎回文件对账
     * 2.对账成功的sell request save
     *
     * @param ylxBatchDTO
     */
    public void confirmAndUpdateFundBalance(YLXBatchDTO ylxBatchDTO) {
        List<YLXSellRequestDetailDTO> sellRequests = null;
        do {
            sellRequests = ylxSellRequestDetailRepository.getYLXSellRequestDTOsByBatchIdandStatus(ylxBatchDTO.getReqBatchId(), ROWNUM, YLXSellRequestStatus.SELLING.getCode());

            if (CollectionUtils.isNotEmpty(sellRequests)) {

                List<Long> requestIds = new ArrayList<Long>();
                List<Long> userIds = new ArrayList<Long>();
                for (int i = 0; i < sellRequests.size(); i++) {
                    YLXSellRequestDetailDTO dto = sellRequests.get(i);
                    requestIds.add(dto.getId());
                    userIds.add(Long.valueOf(dto.getBankAccount()));
                }
                List<YLXSellConfirmDetailDTO> ylxSellConfirmDTOs = sellConfirmDetailDAO.getYlxSellConfirmsByInternalTrxId(requestIds);

                Map<Long, YLXSellConfirmDetailDTO> sellConfirmMap = new HashMap<Long, YLXSellConfirmDetailDTO>();
                for (YLXSellConfirmDetailDTO sellConfirmDTO : ylxSellConfirmDTOs) {
                    sellConfirmMap.put(sellConfirmDTO.getInternalTrxId(), sellConfirmDTO);
                }

                List<YLXSellRequestDetailDTO> needSendSmsSellRequests = new ArrayList<YLXSellRequestDetailDTO>();
                for (YLXSellRequestDetailDTO ylxSellRequestDTO : sellRequests) {
                    if(redeemConfirmHandler.handleRedeemConfirmData(ylxSellRequestDTO, sellConfirmMap)){
                        needSendSmsSellRequests.add(ylxSellRequestDTO);
                        //sendSuccessMessage(ylxSellRequestDTO,sellConfirmMap.get(ylxSellRequestDTO.getId()));//sell成功 发送短信
                    } else{//sell失败 发送短信
                    	sendFailMessage(ylxSellRequestDTO);
                    }
                }
                //send sms
                if (!needSendSmsSellRequests.isEmpty()) {
                    for (YLXSellRequestDetailDTO sellRequestDTO : needSendSmsSellRequests) {
                        //mqService.sendYLXBuyConfirmMsg(String.format("%s-%s", sellRequestDTO.getBatchId(), sellRequestDTO.id()), sellRequestDTO.getInternalTrxId(), YLXTransactionHistoryType.BUY.name(), true);
                    }
                }
            }
        } while (CollectionUtils.isNotEmpty(sellRequests));
        //更新batch到CONFIRMED status,接下来准备待扣
        Logger.debug(this, "update batch status to confirmed.");
        ylxBatchRepository.update("updateBatchStatusById", MapUtils.buildKeyValueMap("batchId", ylxBatchDTO.getId(), "status", YLXBatchStatus2.CONFIRMED.name()));
        ylxBatchRepository.update("updateBatchStatusById", MapUtils.buildKeyValueMap("batchId", ylxBatchDTO.getReqBatchId(), "status", YLXBatchStatus2.CONFIRMED.name()));
    }
    
    /**
     * 拼数据 发失败消息
     * @param userId
     * @param ylxBuyRequestDTO
     * @param productId
     * @param ylxBuyConfirmDTO
     */
    private void sendFailMessage(YLXSellRequestDetailDTO ylxSellRequestDTO){
    	UserDTO user = userRepository.getUserById(Long.valueOf(ylxSellRequestDTO.getUserId()));
    	ProductDTO product = productRepository.getById(ylxSellRequestDTO.getProductId());
    	ylxSmsService.sendRedeemFailMessageToCustomer(user.getMobileNo(), user.getName(), ylxSellRequestDTO.getTrxTime(), ylxSellRequestDTO.getFundShare(), product.getDisplayName());
    }
    
    /**
     * 拼数据 发成功消息
     * @param userId
     * @param ylxBuyRequestDTO
     * @param productId
     * @param ylxBuyConfirmDTO
     */
    public void sendSuccessMessage(YLXSellRequestDetailDTO ylxSellRequestDTO,YLXSellConfirmDetailDTO sellConfirmDTO){
    	UserDTO user = userRepository.getUserById(Long.valueOf(ylxSellRequestDTO.getUserId()));
    	ProductDTO product = productRepository.getById(ylxSellRequestDTO.getProductId());
    	ylxSmsService.sendRedeemSuccessMessageToCustomer(user.getMobileNo(), 
    			user.getName(), 
    			ylxSellRequestDTO.getTrxTime(), 
    			product.getDisplayName(),
    			ylxSellRequestDTO.getConfirmFundShare(), 
    			sellConfirmDTO.getConfirmUnitPrice(), 
    			sellConfirmDTO.getAmount(), 
    			sellConfirmDTO.getCommissionFee());
    }
}
