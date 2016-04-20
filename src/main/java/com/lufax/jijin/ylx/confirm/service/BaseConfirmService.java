package com.lufax.jijin.ylx.confirm.service;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.lufax.jersey.utils.Logger;
import com.lufax.jijin.base.service.TradeDayService;
import com.lufax.jijin.base.utils.JijinAppProperties;
import com.lufax.jijin.sysFacade.gson.YLXResponse;
import com.lufax.jijin.ylx.batch.domain.YLXBatchFileStatus;
import com.lufax.jijin.ylx.batch.domain.YLXBatchStatus2;
import com.lufax.jijin.ylx.batch.domain.YLXBatchType;
import com.lufax.jijin.ylx.batch.dto.BatchRunStatus;
import com.lufax.jijin.ylx.batch.dto.YLXBatchDTO;
import com.lufax.jijin.ylx.batch.dto.YLXBatchFileDTO;
import com.lufax.jijin.ylx.batch.repository.YLXBatchFileRepository;
import com.lufax.jijin.ylx.batch.repository.YLXBatchRepository;
import com.lufax.jijin.ylx.exception.YlxBatchException;
import com.lufax.jijin.ylx.remote.YLXSmsService;
import com.lufax.jijin.ylx.remote.YlxGatewayRemoteService;
import com.lufax.jijin.ylx.util.TimeoutUtils;
import com.lufax.jijin.ylx.util.YlxConstants;

@Service
@Scope
public class BaseConfirmService {

    @Autowired
    private YLXBatchRepository ylxBatchRepository;
    @Autowired
    private YLXBatchFileRepository ylxBatchFileRepository;
    @Autowired
    private JijinAppProperties jijinAppProperties;
    @Autowired
    private YlxGatewayRemoteService ylxGwRmtSrv;
    @Autowired
    private TradeDayService tradeDayService;
    @Autowired
    private OpenConfirmHandler openConfirmHandler;
    @Autowired
    private YLXSmsService ylxSmsService;

    protected static final int ROWNUM = 500; // batchSize
    /**
     * 返回符合条件的YLXBatch
     * 
     * @param type
     * @param status
     * @return
     */
    public List<YLXBatchDTO> getAfterTriggerDateBatchRecords(YLXBatchType type, List<YLXBatchStatus2> status,BatchRunStatus runStatus){
    	List<YLXBatchDTO> batchDTOs = ylxBatchRepository.getYLXBatchDTOByTriggerDateAndTypeAndRunStatus(new Date(), type, status, runStatus);
    	Iterator<YLXBatchDTO> it = batchDTOs.iterator();
		while(it.hasNext()){
			it.next();
			if(!tradeDayService.isTradeDay(new Date())){
				it.remove();
			}
		}
    	return batchDTOs;
    }
    
    /**
     * 根据Batch信息从Response目录得到BatchFile
     * @param ylxBatchFile
     * @return File
     * @throws Exception 
     */
    public File getBatchFile(YLXBatchFileDTO ylxBatchFile) throws Exception{
		YLXBatchFileDTO batchFile = ylxBatchFileRepository.getYLXBatchFileDTOsById(ylxBatchFile.getId());
		try {
			File file = new File(jijinAppProperties.getYlxResponseRootDir() + batchFile.getFileName());
			return file;
		} catch (Exception e) {
			 Logger.error(this, String.format("YLXConfirm failed,batchFileId:%s", batchFile.getId()));
			 throw e;
		}
    }
    /**
     * pull confirm file and update batch file record
     * @param batch
     */
    public void receiveFile(YLXBatchDTO batch) {
    	List<YLXBatchFileDTO> batchFiles = ylxBatchFileRepository.getYLXBatchFileDTOsByBatchIdAndStatus(batch.getId(), YLXBatchFileStatus.init.name());
    	for(YLXBatchFileDTO batchFile:batchFiles){
    		YLXResponse resp = ylxGwRmtSrv.pullFile(batchFile.getFileName());
            if (!YlxConstants.GENERAL_SUCCESS_CODE.equals(resp.getReturnCode())) {
            	batchFile.setReturnCode(resp.getReturnCode());
            	ylxBatchFileRepository.updateByIdSelective(batchFile);
            	throw new YlxBatchException("Failed to pull ylx slp confirm file, code:" + resp.getReturnCode() + " msg:" + resp.getReturnMsg());
            }
        	batchFile.setStatus(YLXBatchFileStatus.created.toString());
        	batchFile.setCurrentLine(0l);
        	ylxBatchFileRepository.updateByIdSelective(batchFile);
            Logger.info(this, "Pull confirm file success. file name - "+batchFile.getFileName());
        	
    	}
    	//更新batch主状态
    	ylxBatchRepository.updateBatchStatusById(batch.getId(),YLXBatchStatus2.RECEIVED_PREPARE.name());
    }
    
    /**
     * 判断YLXBatch 是否超出确认限制时间
     * @param ylxBatch
     * @return
     */
    @SuppressWarnings("unused")
	private boolean isExceedConfirmLimitDays(YLXBatchDTO ylxBatch){
    	boolean result = false;
		if (isExeceedMaxJobTimeLimit(ylxBatch.getCreatedAt())) {
			String status = ylxBatch.getStatus() + "_FAIL";
			ylxBatchRepository.updateYLXBatchDTOFailById(ylxBatch.getId(), status, "Execeed max confirm days",
					ylxBatch.getId() + "", ylxBatch.getRetryTimes());
			Logger.info(this, "update ylx batch id " + ylxBatch.getId() + " status:" + status);
			result = true;
		}
		Logger.info(this, "isExceedConfirmLimitDays result:" + result);
		return result;
    }
    
/*    /**
     * 判断是否准备好run
     * @param ylxBatch
     * @return
     *//*
    private boolean isReadytoRun(YLXBatchDTO ylxBatch){
		boolean result = true;
		if (isJobDead(ylxBatch)) {
			result = true;
		} else if (BatchRunStatus.IDLE != ylxBatch.getRunStatus()) {
			result = false;
		} else {
			result = true;
		}
		Logger.info(this, "IsReadyToRun result:" + result);
		return result;
	
    }*/
    
	/**
	 * 判断YLXBatch 是否超时
	 * @param createdTime
	 * @return
	 */
	private boolean isExeceedMaxJobTimeLimit(Date createdTime) {
		Date now = new Date();
		long maxDays = jijinAppProperties.getYlxMaxConfirmJobRunningDays();
		if ((now.getTime() - createdTime.getTime()) / YlxConstants.DAY_TIME_MILLS < maxDays) {
			return false;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(createdTime);
		for (int i = 0; i < maxDays;) {
			cal.add(Calendar.DATE, 1);
			if (tradeDayService.isTradeDay(cal.getTime())) {
				i = i + 1;
			}
		}
		return cal.getTime().before(now);
	}
	
    public void sendPullConfirmFailSMSIfTimeout(YLXBatchDTO batchDTO,String fromtimeout,String toTimeout){
        try{
            if(TimeoutUtils.isTimeout(fromtimeout) && !TimeoutUtils.isTimeout(toTimeout))
                ylxSmsService.sendYlxPullConfirmFileFailed(batchDTO);
        }catch (Exception e){
            Logger.error(this,"sendPullConfirmFailSMSIfTimeout fail",e);
        }

    }
    
    public void sendHandleConfirmFailSMSIfTimeout(YLXBatchDTO batchDTO, String fromtimeout,String toTimeout){
        try {
            if(TimeoutUtils.isTimeout(fromtimeout) && !TimeoutUtils.isTimeout(toTimeout))
                ylxSmsService.sendYlxHandleConfirmFileFailed(batchDTO);
        }catch (Exception e){
            Logger.error(this,"sendHandleConfirmFailSMSIfTimeout fail",e);
        }
    }
	
	/**
	 * 判断当前的ylxBatch 是否正常进行完
	 * @param ylxBatchDTO
	 * @return
	 */
/*	private boolean isJobDead(YLXBatchDTO ylxBatchDTO) {
		BatchRunStatus runStatus = ylxBatchDTO.getRunStatus();
		Date updatedAt = ylxBatchDTO.getUpdatedAt();
		if (BatchRunStatus.ONGOING == runStatus
				&& System.currentTimeMillis() > (updatedAt.getTime() + jijinAppProperties.getMaxBatchHangLimit())) {
			return true;
		}
		return false;
	}*/
}
