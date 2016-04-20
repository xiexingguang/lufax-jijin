package com.lufax.jijin.trade.service;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.lufax.jersey.utils.Logger;
import com.lufax.jijin.base.service.TradeDayService;
import com.lufax.jijin.base.utils.DateUtils;
import com.lufax.jijin.dao.FundLoanRequestDAO;
import com.lufax.jijin.dto.FundLoanRequestDTO;
import com.lufax.jijin.ylx.batch.domain.BatchFileContentUtil;
import com.lufax.jijin.ylx.batch.domain.YLXBatchFileStatus;
import com.lufax.jijin.ylx.batch.domain.YLXBatchStatus2;
import com.lufax.jijin.ylx.batch.domain.YLXBatchType;
import com.lufax.jijin.ylx.batch.dto.BatchRunStatus;
import com.lufax.jijin.ylx.batch.dto.YLXBatchDTO;
import com.lufax.jijin.ylx.batch.dto.YLXBatchFileDTO;
import com.lufax.jijin.ylx.batch.repository.YLXBatchFileRepository;
import com.lufax.jijin.ylx.batch.repository.YLXBatchRepository;
import com.lufax.jijin.ylx.confirm.service.BaseConfirmService;
import com.lufax.jijin.ylx.dto.YLXFundBalanceDTO;
import com.lufax.jijin.ylx.dto.YlxFundInvestorProfitDTO;
import com.lufax.jijin.ylx.dto.YlxFundProdProfitDTO;
import com.lufax.jijin.ylx.dto.repository.YLXFundBalanceRepository;
import com.lufax.jijin.ylx.exception.YlxBatchErrorCode;
import com.lufax.jijin.ylx.exception.YlxBatchException;
import com.lufax.jijin.ylx.remote.YLXSmsService;
import com.lufax.jijin.ylx.util.YlxConstants;

@Service
@Scope
public class YLXProfitService{
	@Autowired
    private YLXBatchRepository ylxBatchRepository;
    @Autowired
    private YLXBatchFileRepository ylxBatchFileRepository;
    @Autowired
    private BaseConfirmService baseConfirmService;
    @Autowired
    protected TradeDayService tradeDayService;
    @Autowired
    private YLXProfitTransactionalService ylxProfitTransactionalService;
    @Autowired
    private FundLoanRequestDAO fundLoanRequestDAO;
    @Autowired
    private YLXFundBalanceRepository ylxFundBalanceRepository;
    public void prepareProfit(YLXBatchDTO batch){
    	try {
        	List<YLXBatchFileDTO> ylxBatchFileDTOs = ylxBatchFileRepository.getYLXBatchFileDTOsByBatchIdAndStatus(batch.getId(), YLXBatchFileStatus.created.name());
        	for(YLXBatchFileDTO batchFile : ylxBatchFileDTOs){
        		if(YLXBatchType.YLX_SLP_INVESTOR_PROFIT.name().equals(batch.getType())){
        			prepareInvestorProfitData(batch , batchFile);
        		}else if(YLXBatchType.YLX_SLP_PROD_PROFIT.name().equals(batch.getType())){
        			prepareProdProfitData(batch , batchFile);
        		}
        	}
            ylxBatchRepository.updateBatchStatusById(batch.getId(), YLXBatchStatus2.COMPLETE.name());
        } catch (Exception e) {
        	handleException(batch , e);
        	ylxBatchRepository.updateBatchStatusById(batch.getId(), YLXBatchStatus2.CONFIRM_RECEIVE_FAIL.name());
        	Logger.error(this, e.getMessage());
        }
    }
    
    
    
    public void prepareProdProfitData(YLXBatchDTO batch,YLXBatchFileDTO batchFile) throws Exception{
		InputStreamReader in = new InputStreamReader(new FileInputStream(baseConfirmService.getBatchFile(batchFile)), "UTF-8");
        LineNumberReader reader = new LineNumberReader(in);
        try {
        	for(long index = batchFile.getCurrentLineNotNull() + 3; index > 0; index -- ){
        		reader.readLine();
        	}
        	List<YlxFundProdProfitDTO> batchList = new ArrayList<YlxFundProdProfitDTO>();
        	
        	List<FundLoanRequestDTO> floans = new ArrayList<FundLoanRequestDTO>();
        	
            for(String line = reader.readLine(); StringUtils.isNotEmpty(line); line = reader.readLine()){
            	YlxFundProdProfitDTO profit = new YlxFundProdProfitDTO(line);
            	FundLoanRequestDTO floan = fundLoanRequestDAO.getFundLoanRequestByThirdCompanyCode(profit.getProdCode());
            	if(floan != null){
            		floans.add(floan);
            		profit.setProductCode(floan.getProductCode());
                	profit.setProductId(0L);
                	profit.setProductCategory(floan.getProductCategory());
            	}else{
            		profit.setProductCode("未知");
                	profit.setProductId(0L);
                	profit.setProductCategory("未知");
            	}
            	
            	batchList.add(profit );
            }
            if(!batchList.isEmpty()){
            	ylxProfitTransactionalService.productProfitBatchInsert(batchList, floans);
        	}
            
        } finally {
            reader.close();
            in.close();
        }
        batchFile.setStatus(YLXBatchFileStatus.received.name());
		ylxBatchFileRepository.updateByIdSelective(batchFile);
    }
    
    
    public void initBatchForProfit(YLXBatchType type){

        Date today = DateUtils.startOfDay(new Date());
        if(!tradeDayService.isTradeDay(today))
            return;
    	YLXBatchDTO batch = ylxBatchRepository.getYLXBatchDTOByTypeAndTargetDate(type.name(), today);
    	
    	if(batch == null){
    		Logger.info(this, String.format("initBatchForProfit batch == null targetDate : %s", today.toString()));
    		YLXBatchDTO confirmBatch = new YLXBatchDTO();
            confirmBatch.setType(type.name());
            confirmBatch.setStatus(YLXBatchStatus2.CONFIRM_RECEIVE.name());
            confirmBatch.setCutOffId(0);
            confirmBatch.setCreatedBy("SYS");
            confirmBatch.setRunStatus(BatchRunStatus.IDLE);
            confirmBatch.setTargetDate(today);
            confirmBatch.setTriggerDate(tradeDayService.getNextTradeDayAsDate(confirmBatch.getTargetDate()));
            
            List<YLXBatchFileDTO> confirmFiles = new ArrayList<YLXBatchFileDTO>();
            String target = new SimpleDateFormat("yyyyMMdd").format(confirmBatch.getTargetDate());
            YLXBatchFileDTO file = new YLXBatchFileDTO();
            file.setBatchId(confirmBatch.getId());
            file.setFileName(String.format("%s_%s_op_%s_%s.txt", type.getFileType() , YlxConstants.SELL_ORG_NAME , target , BatchFileContentUtil.getStrSeq(1)));
            file.setStatus(YLXBatchFileStatus.init.name());
            file.setCurrentLine(0L);
            file.setOrgCode(YlxConstants.SELL_ORG_CODE);
            file.setTrxDate(confirmBatch.getTargetDate());//上传日期，文件名里的交易日期
            file.setVersion(YlxConstants.VERSION);
            confirmFiles.add(file);
            
            ylxProfitTransactionalService.initBatchDB(confirmBatch, confirmFiles);
    	}
    }
    
    private void handleException(YLXBatchDTO batch, Exception e){
    	//发送失败超时短信
        baseConfirmService.sendHandleConfirmFailSMSIfTimeout(batch, "09:00","15:30");
        long times = batch.getRetryTimes() + 1;
        String msg = e.getMessage();
        if (!StringUtils.isEmpty(msg) && msg.length() >= 4000){
            msg = msg.substring(0, 3999);
        }
        //同步文件失败，保持主状态不变，下次job 运行仍然继续处理“received_prepare”状态Batch
        if (e instanceof YlxBatchException) { // header validation fail, set job fail directly
            if (((YlxBatchException) e).getErrorCode() == YlxBatchErrorCode.FILE_HEADER_ERROR.getCode()) {
                Logger.error(this, "sync open confirm file Failed ! retry times:" + times + " batchId:" + batch.getId(), e);
            }
        }
    }
    
	public void prepareInvestorProfitData(YLXBatchDTO batch , YLXBatchFileDTO batchFile) throws Exception {
		InputStreamReader in = new InputStreamReader(new FileInputStream(baseConfirmService.getBatchFile(batchFile)), "UTF-8");
        LineNumberReader reader = new LineNumberReader(in);
        try {
        	for(long index = batchFile.getCurrentLineNotNull() + 3; index > 0; index -- ){
        		reader.readLine();
        	}
        	
        	List<YlxFundInvestorProfitDTO> batchList = new ArrayList<YlxFundInvestorProfitDTO>(500);
            for(String line = reader.readLine(); StringUtils.isNotEmpty(line); line = reader.readLine()){
            	
            	batchList.add(new YlxFundInvestorProfitDTO(line));
            	
            	if(batchList.size() >= YlxConstants.ROW_LIMIT){
            		ylxProfitTransactionalService.investorProfitBatchInsert(batchList, batchFile.getId(), reader.getLineNumber() - 3);
            		//checkFundBalance(batchList);
            		batchList.clear();
            	}
            }
            if(!batchList.isEmpty()){
            	ylxProfitTransactionalService.investorProfitBatchInsert(batchList, batchFile.getId(), reader.getLineNumber() - 3);
            	//checkFundBalance(batchList);
            }
        } finally {
            reader.close();
            in.close();
        }
        batchFile.setStatus(YLXBatchFileStatus.received.name());
		ylxBatchFileRepository.updateByIdSelective(batchFile);
    }
	
	@Autowired
    private YLXSmsService ylxSmsService;
	/*
	 * 用户份额和养老险份额对账
	 */
	private void checkFundBalance(List<YlxFundInvestorProfitDTO> batchList) {
		List<String> thirdAccounts = new ArrayList<String>();
		for(YlxFundInvestorProfitDTO y : batchList){
			thirdAccounts.add(y.getInvestorFundAccount());
		}
		List<YLXFundBalanceDTO> fbs = ylxFundBalanceRepository.getByThirdAccounts(thirdAccounts);
		
		List<String> codes = new ArrayList<String>();
		for(YLXFundBalanceDTO fb : fbs){
			if(!codes.contains(fb.getProductCode())){
				codes.add(fb.getProductCode());
			}
		}
		List<FundLoanRequestDTO> floans = fundLoanRequestDAO.getFundLoanRequestByProductCodes(codes);
		
		for(YLXFundBalanceDTO fb : fbs){
			for(YlxFundInvestorProfitDTO y : batchList){
				for(FundLoanRequestDTO floan : floans){
					if(fb.getThirdCustomerAccount().equals(y.getInvestorFundAccount()) && floan.getThirdCompanyCode().equals(y.getProdCode()) && floan.getProductCode().equals(fb.getProductCode())){
						BigDecimal b1 = fb.getFundShare().add(fb.getFrozenFundShare()).setScale(1, BigDecimal.ROUND_UP);
						BigDecimal b2 = y.getTotalShare().setScale(1, BigDecimal.ROUND_UP);
						if(b1.compareTo(b2) != 0){
							ylxSmsService.sendYlxProfitError(fb.getFundName() + fb.getProductCode(), fb.getThirdCustomerAccount(),b1 ,b2);
							//X月X号XXX（产品名称），用户号XXX的陆-养总份额不符。养老险总份额为XXX，陆金所总份额为XXX。
						}
						break;
					}
				}
			}
		}
	}
}
