package com.lufax.jijin.ylx.request.service;


import com.lufax.jijin.base.service.TradeDayService;
import com.lufax.jijin.base.utils.DateUtils;
import com.lufax.jijin.base.utils.JijinAppProperties;
import com.lufax.jijin.user.service.UserService;
import com.lufax.jijin.ylx.batch.domain.*;
import com.lufax.jijin.ylx.batch.dto.BatchRunStatus;
import com.lufax.jijin.ylx.batch.dto.YLXBatchDTO;
import com.lufax.jijin.ylx.batch.repository.YLXBatchFileRepository;
import com.lufax.jijin.ylx.batch.repository.YLXBatchRepository;
import com.lufax.jijin.ylx.dto.repository.YLXBuyRequestDetailRepository;
import com.lufax.jijin.ylx.dto.repository.YLXOpenRequestDetailRepository;
import com.lufax.jijin.ylx.dto.repository.YLXSellRequestDetailRepository;
import com.lufax.jijin.ylx.util.RequestTimer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
public class BaseRequestService {
	
	@Autowired
    protected YLXOpenRequestDetailRepository ylxOpenRequestDetailRepository;
	@Autowired
	protected YLXBuyRequestDetailRepository ylxBuyRequestDetailRepository;
	@Autowired
	protected YLXSellRequestDetailRepository ylxSellRequestDetailRepository;

    @Autowired
    protected YLXBatchRepository ylxBatchRepository;
    @Autowired
    protected YLXBatchFileRepository ylxBatchFileRepository;
    @Autowired
    protected UserService userService;
    @Autowired
    protected JijinAppProperties jijinAppProperties;
    @Autowired
    protected TradeDayService tradeDayService;

    /*protected IYLXRequestWriter writer;*/

	/*public void setWriter(IYLXRequestWriter writer) {
		this.writer = writer;
	}*/

	protected static final int ROWNUM = 500; // batchSize
    protected int FILE_SIZE; // file size 30000
    protected long TIMEOUT_LIMIT;
	protected String OPEN_BUY_REQUEST_TIMEOUT;
	protected String SELL_REQUEST_TIMEOUT;
	
    @PostConstruct
    public void setFileMaxSize(){
    	FILE_SIZE = Integer.valueOf(jijinAppProperties.getYlxFileMaxSize());
    	TIMEOUT_LIMIT = jijinAppProperties.getMaxBatchHangLimit();
    	OPEN_BUY_REQUEST_TIMEOUT = jijinAppProperties.getOpenBuyRequestTimeout();
    	SELL_REQUEST_TIMEOUT = jijinAppProperties.getSellRequestTimeout();
    }
    /*protected void createFiles(YLXBatchDTO batch){
    	
    	List<YLXBatchFileDTO> YLXBatchFileDTOs=ylxBatchFileRepository.getYLXBatchFileDTOsByBatchId(batch.getId());
    	int FileTotalNum = YLXBatchFileDTOs.size();

    	if(FileTotalNum!=0){
    		long totalRecords=writer.getTotalRecords(batch.getId());
         	
        	for(YLXBatchFileDTO file :YLXBatchFileDTOs){
        		String rootDir = jijinAppProperties.getYlxRequestRootDir();
        		if(YLXBatchFileStatus.init.name().equals(file.getStatus())){
         			long id = file.id();
        			int seq = BatchFileContentUtil.getIntSeq(file.getFileName());
        			long startRow =FILE_SIZE*(seq-1)+1;
        			long endRow=0;
        			long total =0;
        			try{
        				File newFile = FileUtils.createEmptyFile(file.getFileName(), rootDir);
        			    
            			if(seq==FileTotalNum){ // means this is file is at last
             				endRow = totalRecords;
             				total = endRow-startRow+1;
            			}else{// means file is in middle 
              				endRow = FILE_SIZE*seq;
              				total = FILE_SIZE;
            			}
            			BigDecimal amount = writer.writeRequestContent(newFile, file, startRow, endRow, total, batch,FILE_SIZE);
            			ylxBatchFileRepository.updateYLXBatchFileDTOSuccessById(id, YLXBatchFileStatus.created.name(), amount, file.getTrxDate(), total);
        			}catch(Exception e){
        				Logger.warn(this, "Creating file fail, retry！", e);
        				ylxBatchFileRepository.updateYLXBatchFileDTOStatusById(id, YLXBatchFileStatus.init.name(), file.getRetryTimes()+1,null,null);
            			throw new RuntimeException(e);
        			}
        		}
        	}

    	}
  
    	//update heart beat and next sub step
    	ylxBatchRepository.updateYLXBatchDTOSuccessById(batch.getId(), 
    			YLXBatchStatus.request_data_preparing.name(), 
    			YLXBatchSubStep.sub_step_1_5.name());
    	batch.setSubNextStep(YLXBatchSubStep.sub_step_1_5.name());
    	
    }*/
    
    public List<YLXBatchDTO> getBatchRequestRecords(YLXBatchType type,YLXBatchStatus2 ylxBatchStatus2,BatchRunStatus runStatus){

        //如果是交易日,插入一OPEN_REQEUST/BUY_REQUEST/PURCHASE_REQUEST/SELL_REQUEST条记录
        Date today = DateUtils.startOfDay(new Date());
        if(tradeDayService.isTradeDay(today)&& YLXBatchStatus2.REQUEST_INIT.name().equals(ylxBatchStatus2.name())){
            if(null ==ylxBatchRepository.getYLXBatchDTOByTypeAndTargetDate(type.name(), today)){
                YLXBatchDTO openRecord = new YLXBatchDTO();
                openRecord = new YLXBatchDTO();
                openRecord.setStatus(YLXBatchStatus2.REQUEST_INIT.name());
                openRecord.setTargetDate(today);
                openRecord.setCutOffId(-1L);
                openRecord.setType(type.name());
                openRecord.setRunStatus(runStatus);
                ylxBatchRepository.insertYLXBatchDTO(openRecord);
            }
        }

        List<YLXBatchDTO> targetList = new ArrayList<YLXBatchDTO>();
    	
    	/*YLXBatchStatus timeOutStatus = YLXBatchStatus.request_data_preparing;
    	YLXBatchStatus normalStatus = YLXBatchStatus.request_data_prepare;
    	YLXBatchSubStep subStep = YLXBatchSubStep.sub_step_1_1;*/
    	

	
		// first to pickup timeout case
		/*List<YLXBatchDTO> timeoutRecords = ylxBatchRepository.getYLXBatchDTOByStatusAndType(timeOutStatus.name(),type.name());*/

		List<YLXBatchDTO> normalRecords = ylxBatchRepository.getYLXBatchDTOByTypeAndStatusAndRunStatus(ylxBatchStatus2.name(), type.name(), BatchRunStatus.IDLE);
		
		/*//如果是认购申请发送，先要判断对应的开户申请是否已发送
		if(YLXBatchType.YLX_SLP_BUY_REQUEST==type && isSendFile){
			Iterator<YLXBatchDTO> it = normalRecords.iterator();
			while(it.hasNext()){
				YLXBatchDTO dto = it.next();
				YLXBatchDTO openRequest = ylxBatchRepository.getYLXBatchDTOByTypeAndTargetDate(YLXBatchType.YLX_SLP_OPEN_REQUEST.name(), dto.getTargetDate());
		    	if(!YLXBatchStatus.complete.name().equals(openRequest.getStatus())){
		    		it.remove();
		    	}
			}
		}*/

		// change status to 'ing' status
	    /*for(YLXBatchDTO dto :normalRecords){
	    	ylxBatchRepository.updateYLXBatchDTOSuccessById(dto.getId(), timeOutStatus.name(), dto.getSubNextStep());
	    }*/

		/*Iterator<YLXBatchDTO> it = timeoutRecords.iterator();

        while (it.hasNext()) {
			YLXBatchDTO dto = it.next();
			Date updateAt = dto.getUpdatedAt();
			Date currentTime = new Date();
            long diffMin = DateUtils.diffMinute(updateAt,currentTime);
		    long timeout_limit_mins = TIMEOUT_LIMIT;
			
			if (diffMin < timeout_limit_mins) {// consider it is still alive
				it.remove();
			}
		}*/
	
		targetList.addAll(normalRecords);
		/*targetList.addAll(timeoutRecords);*/
		return targetList;
	}


    
    protected boolean isFinalFail(String type){
    	Calendar c = Calendar.getInstance();
    	c.setTime(new Date());
    	int hour = c.get(Calendar.HOUR_OF_DAY);
    	int minute = c.get(Calendar.MINUTE);
    	
    	RequestTimer oepnBuy = parseTime(OPEN_BUY_REQUEST_TIMEOUT);
    	RequestTimer sell = parseTime(SELL_REQUEST_TIMEOUT);
    	
    	if(YLXBatchType.YLX_SLP_OPEN_REQUEST.name().equals(type)||YLXBatchType.YLX_SLP_BUY_REQUEST.name().equals(type)){
    		if(hour>oepnBuy.getHour() ||(hour==oepnBuy.getHour() && minute>oepnBuy.getMinute())){
    			return true;	
    		}
    			
    	}else if(YLXBatchType.YLX_SLP_REDEEM_REQUEST.name().equals(type)){
    		
    		if(hour>= sell.getHour()){
        		return true;
    		}
    			
    	}
    	return false;
    }

    private RequestTimer parseTime(String timeout){
    	List<String> holder = Arrays.asList(timeout.split(":"));
    	int hour = Integer.valueOf(holder.get(0));
    	int minute = Integer.valueOf(holder.get(1));
    	
    	RequestTimer timer = new RequestTimer(hour,minute);
    	
    	return timer;
    	
    }

}
