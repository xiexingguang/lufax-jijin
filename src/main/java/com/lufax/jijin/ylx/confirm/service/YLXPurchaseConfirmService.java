package com.lufax.jijin.ylx.confirm.service;

import com.lufax.jersey.utils.Logger;
import com.lufax.jijin.base.utils.DateUtils;
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
import com.lufax.jijin.ylx.dto.YlxFundProdProfitDTO;
import com.lufax.jijin.ylx.dto.repository.BuyConfirmDetailRepository;
import com.lufax.jijin.ylx.dto.repository.YLXBuyRequestDetailRepository;
import com.lufax.jijin.ylx.enums.YLXBuyRequestStatus;
import com.lufax.jijin.ylx.enums.YLXTransactionHistoryType;
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
public class YLXPurchaseConfirmService {

    @Autowired
    private YLXBatchRepository ylxBatchRepository;
    @Autowired
    private YLXBatchFileRepository ylxBatchFileRepository;
    @Autowired
    private BaseConfirmService baseConfirmService;
    @Autowired
    private BuyConfirmDetailRepository buyConfirmDetailDAO;
    @Autowired
    private PurchaseConfirmHandler purchaseConfirmHandler;
    @Autowired
    private PurchaseConfirmReader purchaseConfirmReader;
    @Autowired
    private YLXBuyRequestDetailRepository ylxBuyRequestDetailRepository;
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
     * 解析BatchFile,将相应的确认记录读入ylx_buy_confirm_detail,更新Batch状态为received
     * @param ylxBatch
     */
    public void handlePurchaseConfirmData(YLXBatchDTO ylxBatch) {
            try {
            	YLXBatchDTO openConfirmBatch = ylxBatchRepository.getYLXBatchDTOByTypeAndTargetDate(YLXBatchType.YLX_SLP_OPEN_CONFIRM.name(), ylxBatch.getTargetDate());
                //首先判断开户文件是否已经被confirm过,如果没有,申购确认不处理
                if (openConfirmBatch == null || !openConfirmBatch.getStatus().equalsIgnoreCase(YLXBatchStatus2.COMPLETE.name())) {
                    Logger.info(this, String.format("purchase confirm need after open confirm targetDate [%s]", ylxBatch.getTargetDate()));
                    return;
                }
            	List<YLXBatchFileDTO> ylxBatchFileDTOs = ylxBatchFileRepository.getYLXBatchFileDTOsByBatchIdAndStatus(ylxBatch.getId(), YLXBatchFileStatus.created.name());
            	//解析BatchFile,将相应的确认记录读入ylx_buy_confirm_detail
            	Logger.info(this, "purchase confirm files ! " +ylxBatchFileDTOs.size());
            	for(YLXBatchFileDTO batchFile:ylxBatchFileDTOs){
            		Logger.info(this, String.format("purchase confirm file name %s,current line %s",batchFile.getFileName(),batchFile.getCurrentLine()));
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
                Logger.error(this, "sync purchase confirm file Failed ! retry times:" + times + " batchId:" + ylxBatch.getId(), e);
            }
    }
    
    
    /**
     * 解析BatchFile,将相应的确认记录读入ylx_buy_confirm_detail
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
            List<BuyConfirmDetailDTO> dtos = new ArrayList<BuyConfirmDetailDTO>();
            do {
                s = reader.readLine();
                if (StringUtils.isEmpty(s)) {// 文件读取完毕,插入最后一批数据
                    Logger.info(this, String.format("purchase confirm insert last batch records into DB endline:%s]", reader.getLineNumber() - 1));
                    if (dtos.size() > 0)
                    	purchaseConfirmHandler.batchInsertOpenConfirm(dtos, reader.getLineNumber(), batchFile);
                    break;
                }
                if (reader.getLineNumber() >= startLine && reader.getLineNumber() < startLine + rownum) {
                    if (reader.getLineNumber() == 3) {// header validation, any error throw exception
                        if (!purchaseConfirmReader.validateFileHeaderContent(s)){
                        	 throw new YlxBatchException(YlxBatchErrorCode.FILE_HEADER_ERROR.getCode(), "purchase confirm file header has error:" + s);
                        }
                    }
                    // 解析行，转换成DTO
                    BuyConfirmDetailDTO dto = purchaseConfirmReader.readLine(s, reader.getLineNumber(),ylxBatchDTO);
                    if (dto != null)
                        dtos.add(dto);
                }
                if (reader.getLineNumber() == startLine + rownum - 1) {// 已达到批次记录集，进行插数据库，清空缓存，并set下一个startLine
                    Logger.info(this, String.format("purchase confirm insert into DB endline:%s]", reader.getLineNumber()));
                    startLine = reader.getLineNumber() + 1;// set new startLine
                    purchaseConfirmHandler.batchInsertOpenConfirm(dtos, startLine, batchFile);
                    dtos.clear();
                }
            } while (s != null);
        } finally {
            reader.close();
            in.close();
        }
    }
    
    /**
     * 1.申购文件对账
     * 2.对账成功的buy request save 
     *
     * @param ylxBatchDTO
     */
    public void confirmAndUpdateFundBalance(YLXBatchDTO ylxBatchDTO) {
        List<YLXBuyRequestDetailDTO> buyRequests = null;
        do {
            buyRequests = ylxBuyRequestDetailRepository.getYLXBuyRequestDTOsByBatchIdandStatus(ylxBatchDTO.getReqBatchId(), ROWNUM, YLXBuyRequestStatus.BUYING.getCode());

            if (CollectionUtils.isNotEmpty(buyRequests)) {

                List<Long> requestIds = new ArrayList<Long>();
                List<Long> userIds = new ArrayList<Long>();
                for (int i = 0; i < buyRequests.size(); i++) {
                	YLXBuyRequestDetailDTO dto = buyRequests.get(i);
                    requestIds.add(dto.getId());
                    userIds.add(Long.valueOf(dto.getBankAccount()));
                }
                List<BuyConfirmDetailDTO> ylxBuyConfirmDTOs = buyConfirmDetailDAO.getYlxBuyConfirmsByInternalTrxId(requestIds);

                Map<Long, BuyConfirmDetailDTO> buyConfirmMap = new HashMap<Long, BuyConfirmDetailDTO>();
                for (BuyConfirmDetailDTO BuyConfirmDTO : ylxBuyConfirmDTOs) {
                    buyConfirmMap.put(BuyConfirmDTO.getInternalTrxId(), BuyConfirmDTO);
                }

                List<YLXBuyRequestDetailDTO> needSendSmsBuyRequests = new ArrayList<YLXBuyRequestDetailDTO>();
                for (YLXBuyRequestDetailDTO ylxBuyRequestDTO : buyRequests) {
                	if(purchaseConfirmHandler.handleBuyConfirmData(ylxBuyRequestDTO, buyConfirmMap)){
                		needSendSmsBuyRequests.add(ylxBuyRequestDTO);
                		//成功记录发送成功短信
                		sendSuccessMessage(ylxBuyRequestDTO,buyConfirmMap.get(ylxBuyRequestDTO.getId()));
                	} else {//失败记录发送失败短信
                		sendFailMessage(ylxBuyRequestDTO);
                	}
                }
                //send sms
                if (!needSendSmsBuyRequests.isEmpty()) {
                    for (YLXBuyRequestDetailDTO fundBuyRequestDTO : needSendSmsBuyRequests) {
                    	mqService.sendYLXBuyConfirmMsg(
                    			String.format("%s-%s",fundBuyRequestDTO.getBatchId(),fundBuyRequestDTO.id()) ,
                    			fundBuyRequestDTO.getInternalTrxId(), 
                    			YLXTransactionHistoryType.PURCHASE.name(),
                    			true);
                    }
                }
            }
        } while (CollectionUtils.isNotEmpty(buyRequests));
      //更新batch 为complete status
    	Logger.debug(this, "update batch status to complete.");
    	ylxBatchRepository.update("updateBatchStatusById",
    			MapUtils.buildKeyValueMap("batchId", ylxBatchDTO.getId(), "status", YLXBatchStatus2.COMPLETE.name()));
    }
    
    /**
     * 拼数据 发失败消息
     * @param userId
     * @param ylxBuyRequestDTO
     * @param productId
     * @param ylxBuyConfirmDTO
     */
    private void sendFailMessage(YLXBuyRequestDetailDTO ylxBuyRequestDTO){
    	UserDTO user = userRepository.getUserById(Long.valueOf(ylxBuyRequestDTO.getBankAccount()));
    	ProductDTO product = productRepository.getById(ylxBuyRequestDTO.getProductId());
    	Date refundDate = DateUtils.add(ylxBuyRequestDTO.getTrxTime(), 3, Calendar.DAY_OF_YEAR);
    	ylxSmsService.sendPurchaseFailMessageToCustomer(user.getMobileNo(), user.getName(), ylxBuyRequestDTO.getTrxTime(), product.getDisplayName(), ylxBuyRequestDTO.getAmount(), refundDate);
    }
    
    /**
     * 拼数据 发成功消息
     * @param userId
     * @param ylxBuyRequestDTO
     * @param productId
     * @param ylxBuyConfirmDTO
     */
    private void sendSuccessMessage(YLXBuyRequestDetailDTO ylxBuyRequestDTO,BuyConfirmDetailDTO BuyConfirmDTO){
    	UserDTO user = userRepository.getUserById(Long.valueOf(ylxBuyRequestDTO.getBankAccount()));
    	ProductDTO product = productRepository.getById(ylxBuyRequestDTO.getProductId());
    	ylxSmsService.sendPurchaseSuccessMessageToCustomer(user.getMobileNo(), user.getName(), ylxBuyRequestDTO.getTrxTime(), product.getDisplayName(), ylxBuyRequestDTO.getAmount(), BuyConfirmDTO.getConfirmFundShare(), BuyConfirmDTO.getConfirmUnitPrice(), BuyConfirmDTO.getPurchaseFee());
    }
}
