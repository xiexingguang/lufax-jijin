package com.lufax.jijin.ylx.request.service;

import com.google.gson.Gson;
import com.lufax.jijin.base.service.TradeDayService;
import com.lufax.jijin.base.utils.JijinAppProperties;
import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.product.repository.ProductRepository;
import com.lufax.jijin.sysFacade.gson.YLXResponse;
import com.lufax.jijin.user.service.UserService;
import com.lufax.jijin.ylx.batch.domain.YLXBatchFileStatus;
import com.lufax.jijin.ylx.batch.domain.YLXBatchStatus2;
import com.lufax.jijin.ylx.batch.domain.YLXBatchType;
import com.lufax.jijin.ylx.batch.dto.BatchRunStatus;
import com.lufax.jijin.ylx.batch.dto.YLXBatchDTO;
import com.lufax.jijin.ylx.batch.dto.YLXBatchFileDTO;
import com.lufax.jijin.ylx.batch.repository.YLXBatchFileRepository;
import com.lufax.jijin.ylx.batch.repository.YLXBatchRepository;
import com.lufax.jijin.ylx.dto.repository.YLXBuyRequestDetailRepository;
import com.lufax.jijin.ylx.dto.repository.YLXOpenRequestDetailRepository;
import com.lufax.jijin.ylx.dto.repository.YLXSellRequestDetailRepository;
import com.lufax.jijin.ylx.remote.YLXSmsService;
import com.lufax.jijin.ylx.remote.YlxGatewayRemoteService;
import com.lufax.jijin.ylx.util.RequestTimer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
public class YLXSendFileService{
	
	@Autowired
	protected YlxGatewayRemoteService gwService;
	@Autowired
	protected ProductRepository productRepository;
	@Autowired
	protected YLXSmsService smsService;
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
	
	private static final String SUCCESS = "0000";


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
	
	/*public void sendFiles(YLXBatchType type){
		
		List<YLXBatchDTO> batchDTOs = null; //getBatchSendFileRecords(type);
		
		for(YLXBatchDTO batch:batchDTOs){
			
			try{
				// sending request file to ylx
				if(YLXBatchSubStep.sub_step_2_1.name().equals(batch.getSubNextStep())){
					
					List<YLXBatchFileDTO> files = ylxBatchFileRepository.getYLXBatchFileDTOsByBatchId(batch.getId());
					
					//call gw to send file
					for(YLXBatchFileDTO file:files){
						if(YLXBatchFileStatus.created.name().equals(file.getStatus())){
							YLXResponse res = gwService.sendFile(file.getFileName());
							Logger.info(this, "GW response:" + new Gson().toJson(res));
							if(SUCCESS.equals(res.getReturnCode())){
								ylxBatchFileRepository.updateYLXBatchFileDTOStatusById(file.getId(), YLXBatchFileStatus.sent.name(), file.getRetryTimes(),res.getReturnCode(),res.getFileId());						
							}else{
								ylxBatchFileRepository.updateYLXBatchFileDTOStatusById(file.getId(), YLXBatchFileStatus.created.name(), file.getRetryTimes()+1,res.getReturnCode(),null);
								throw new YlxBatchException("send file fail:"+res.getReturnMsg());// let it retry
							}
						}							
					}
					
					ylxBatchRepository.updateYLXBatchDTOSuccessById(batch.getId(), 
			    			YLXBatchStatus.request_sending.name(),
			    			YLXBatchSubStep.sub_step_2_2.name());
			    	batch.setSubNextStep(YLXBatchSubStep.sub_step_2_2.name());
				}
				// update batch status and insert confirm record
				if(YLXBatchSubStep.sub_step_2_2.name().equals(batch.getSubNextStep())){
					Logger.info(this, "send file successfully - batchId:"+batch.getId()+" type:"+batch.getType());
					updateStatusAndInsertConfirm(batch,false,null);		
				}
			}catch(Exception e){
    			// any runtime exception, try to rerun
    			int times = batch.getRetryTimes()+1;
    			String msg = e.getMessage();
    			if(msg.length()>=4000)
    				msg = msg.substring(0,3999);
    			if(isFinalFail(batch.getType())){
    				Logger.error(this, "send Request file Failed ! retry times:"+times+" batchId:"+batch.getId(),e);
    				updateStatusAndInsertConfirm(batch,true,msg);	
    			}else{//retry
    				Logger.warn(this, "send Request file retry - times:"+times+" batchId:"+batch.getId(),e);
    				ylxBatchRepository.updateYLXBatchDTOFailById(batch.id(), 
	    					YLXBatchStatus.request_send.name(), msg, String.valueOf(batch.id()), times);			
    			}	
    		}	
		}
		
	}*/

    public boolean sendFiles(YLXBatchDTO batch){

        /*try {
            List<YLXBatchFileDTO> files = ylxBatchFileRepository.getYLXBatchFileDTOsByBatchId(batch.getId());
            //call gw to send file
            for (YLXBatchFileDTO file : files) {
                if (YLXBatchFileStatus.created.name().equals(file.getStatus())) {
                    YLXResponse res = gwService.sendFile(file.getFileName());
                    Logger.info(this, "GW response:" + new Gson().toJson(res));
                    if (SUCCESS.equals(res.getReturnCode())) {
                        ylxBatchFileRepository.updateYLXBatchFileDTOStatusById(file.getId(), YLXBatchFileStatus.sent.name(), file.getRetryTimes(), res.getReturnCode(), res.getFileId());
                    } else {
                        ylxBatchFileRepository.updateYLXBatchFileDTOStatusById(file.getId(), YLXBatchFileStatus.created.name(), file.getRetryTimes() + 1, res.getReturnCode(), null);
                        //throw new YlxBatchException("send file fail:" + res.getReturnMsg());// let it retry
                        return false;
                    }
                }
            }
            return true;

            // update batch status and insert confirm record
            updateStatusAndInsertConfirm(batch, false, null);

        } catch (Exception e) {
            // any runtime exception, try to rerun
            String msg = e.getMessage();
            if (msg.length() >= 4000)
                msg = msg.substring(0, 3999);
            if (isFinalFail(batch.getType())) {
                Logger.error(this, "send Request file Failed - batchId:" + batch.getId(), e);
                updateStatusAndInsertConfirm(batch, true, msg);
            } else {//retry
                Logger.warn(this, "send Request file retry - batchId:" + batch.getId(), e);
                ylxBatchRepository.updateYLXBatchRetryTimes(batch.id(), msg, String.valueOf(batch.id()));
            }
        }*/
        List<YLXBatchFileDTO> files = ylxBatchFileRepository.getYLXBatchFileDTOsByBatchId(batch.getId());
        //call gw to send file
        for (YLXBatchFileDTO file : files) {
            if (YLXBatchFileStatus.created.name().equals(file.getStatus())) {
                YLXResponse res = gwService.sendFile(file.getFileName());
                Logger.info(this, "GW response:" + new Gson().toJson(res));
                if (SUCCESS.equals(res.getReturnCode())) {
                    ylxBatchFileRepository.updateYLXBatchFileDTOStatusById(file.getId(), YLXBatchFileStatus.sent.name(), file.getRetryTimes(), res.getReturnCode(), res.getFileId());
                } else {
                    ylxBatchFileRepository.updateYLXBatchFileDTOStatusById(file.getId(), YLXBatchFileStatus.created.name(), file.getRetryTimes() + 1, res.getReturnCode(), null);
                    //throw new YlxBatchException("send file fail:" + res.getReturnMsg());// let it retry
                    return false;
                }
            }
        }
        return true;
    }

    public List<YLXBatchDTO> getBatchSendFileRecords(YLXBatchType type,YLXBatchStatus2 ylxBatchStatus2,BatchRunStatus runStatus){

        List<YLXBatchDTO> targetList = new ArrayList<YLXBatchDTO>();

       /* YLXBatchStatus    timeOutStatus = YLXBatchStatus.request_sending;
        YLXBatchStatus    normalStatus = YLXBatchStatus.request_send;
        YLXBatchSubStep   subStep = YLXBatchSubStep.sub_step_2_1;*/

        // first to pickup timeout case
        /*List<YLXBatchDTO> timeoutRecords = ylxBatchRepository.getYLXBatchDTOByStatusAndType(timeOutStatus.name(),type.name());*/

        List<YLXBatchDTO> normalRecords = ylxBatchRepository.getYLXBatchDTOByTypeAndStatusAndRunStatus(ylxBatchStatus2.name(), type.name(),runStatus);

        //如果是认购申请发送，先要判断对应的开户申请是否已发送
        if(YLXBatchType.YLX_SLP_BUY_REQUEST.equals(type)||YLXBatchType.YLX_SLP_PURCHASE_REQUEST.equals(type)){
            Iterator<YLXBatchDTO> it = normalRecords.iterator();
            while(it.hasNext()){
                YLXBatchDTO dto = it.next();
                YLXBatchDTO openRequest = ylxBatchRepository.getYLXBatchDTOByTypeAndTargetDate(YLXBatchType.YLX_SLP_OPEN_REQUEST.name(), dto.getTargetDate());
                if(!YLXBatchStatus2.COMPLETE.name().equals(openRequest.getStatus())){
                    it.remove();
                }
            }
        }

        // change status to 'ing' status
       /* for(YLXBatchDTO dto :normalRecords){
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
	
	@Transactional
	protected void updateStatusAndInsertConfirm(YLXBatchDTO batch, boolean isFail,String msg){
			
		/*if(isFail){
			ylxBatchRepository.updateYLXBatchDTOFailById(batch.id(), 
					YLXBatchStatus2.REQUEST_FILE_SEND_FAIL.name(), msg, String.valueOf(batch.id()), batch.getRetryTimes());
		}else{
            //如果是BUY_REQUEST更新状态REQUEST_FILE_SEND，下一步会进行对公待付
           if(YLXBatchType.YLX_SLP_BUY_REQUEST.name().equals(batch.getType())) {
               ylxBatchRepository.updateYLXBatchDTOSuccessById(batch.getId(),
                       YLXBatchStatus2.REQUEST_FILE_SEND.name(),
                       null);
           }
            //如果是OPEN_REQUEST更新状态为COMPLETE
           if(YLXBatchType.YLX_SLP_OPEN_REQUEST.name().equals(batch.getType())){
               ylxBatchRepository.updateYLXBatchDTOSuccessById(batch.getId(),
                       YLXBatchStatus2.COMPLETE.name(),
                       null);
           }
		}
		*//*
		 * if buy request fail, send sms and do not insert confirm record
		 * this batch will be handled by manual totally.
		 *//*
		if(YLXBatchType.YLX_SLP_BUY_REQUEST.name().equals(batch.getType())&&isFail){
			*//*List<String> codes = faLoanRepository.getDistinctProdCodeByBatchId(batch.getId());
			
			for(String code: codes){
				ProductDTO dto = productRepository.getByFaRequestCode(code);
				smsService.sendYlxRequestFilePushFailedMessage(batch.getTargetDate(), dto.getDisplayName());
			}*//*
		}else{
			
			YLXBatchDTO dto = new YLXBatchDTO();
	    	
	    	Date targetDate = batch.getTargetDate();    	
	    	dto.setStatus(YLXBatchStatus.confirm_receive.name());
	    	dto.setTargetDate(targetDate);
	 		dto.setTriggerDate(tradeDayService.getNextTradeDayAsDate(targetDate));
	     	dto.setCutOffId(batch.getCutOffId());
	    	dto.setType(getBatchType(batch.getType()));
	    	dto.setReqBatchId(batch.getId());
	    	ylxBatchRepository.insertYLXBatchDTO(dto);
		}*/
	}
    public boolean isFinalFail(String type){
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

	protected String getBatchType(String type){
		
		if(YLXBatchType.YLX_SLP_OPEN_REQUEST.name().equals(type)){
			return YLXBatchType.YLX_SLP_OPEN_CONFIRM.name();
		}else if(YLXBatchType.YLX_SLP_BUY_REQUEST.name().equals(type)){
			return YLXBatchType.YLX_SLP_BUY_CONFIRM.name();
		}else{
			return YLXBatchType.YLX_SLP_REDEEM_CONFIRM.name();
		}
	}

}
