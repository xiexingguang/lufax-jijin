package com.lufax.jijin.ylx.request.service;

import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.ylx.batch.domain.*;
import com.lufax.jijin.ylx.batch.dto.YLXBatchDTO;
import com.lufax.jijin.ylx.batch.dto.YLXBatchFileDTO;
import com.lufax.jijin.ylx.request.domain.*;
import com.lufax.jijin.ylx.util.YlxConstants;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


@Service
public class YLXOpenRequestService extends BaseRequestService{
    

	@Autowired
    YLXOpenRequestWriter openWriter;

    @Autowired
    YLXOpenRequestBatchFileRecordCreator ylxOpenRequestBatchFileRecordCreator;
    @Autowired
    YLXOpenRequestRecordHandler ylxOpenRequestRecordHandler;
    @Autowired
    YLXOpenRequestBatchFileCreator ylxOpenRequestBatchFileCreator;
	
	/*@PostConstruct
	public void setWriter(){
		super.setWriter(openWriter);
	}*/
	
    public void prepareOpenRequestData(){/*
    	
    	List<YLXBatchDTO> batchDTOs = null;//getBatchRequestRecords(YLXBatchType.YLX_SLP_OPEN_REQUEST);
    	
    	if (CollectionUtils.isNotEmpty(batchDTOs)){
    		for(YLXBatchDTO batch: batchDTOs){
	    		try{
	    			Logger.info(this, String.format("start open request data prepare - batchId:%s",batch.getId()));
	    			// 1.insert request detail placeholder
	    			if(YLXBatchSubStep.sub_step_1_1.name().equals(batch.getSubNextStep())){
	    				Logger.info(this, String.format("openRequestDataPrepare sub step1: updateOpenRequestWithCurrentBatchId - batchId:%s",batch.getId()));
                        ylxOpenRequestRecordHandler.updateOpenRequestWithCurrentBatchId(batch);
	    			}
	    			// 3.insert batch file records
	    			if(YLXBatchSubStep.sub_step_1_3.name().equals(batch.getSubNextStep())){
	    				Logger.info(this, String.format("openRequestDataPrepare sub step3: insertBatchFile - batchId:%s",batch.getId()));
                        ylxOpenRequestBatchFileRecordCreator.insertBatchFile(batch);
	    			} 
	    			// 4.create files
	    			if(YLXBatchSubStep.sub_step_1_4.name().equals(batch.getSubNextStep())){
	    				Logger.info(this, String.format("openRequestDataPrepare sub step4: createFiles - batchId:%s",batch.getId()));
                        ylxOpenRequestBatchFileCreator.createFiles(batch);
	    			}
	    			// 5.update batch record status
	    			if(YLXBatchSubStep.sub_step_1_5.name().equals(batch.getSubNextStep())){
	    				Logger.info(this, String.format("openRequestDataPrepare sub step5: updateFinalStatus - batchId:%s",batch.getId()));
	     		    	ylxBatchRepository.updateYLXBatchDTOSuccessById(batch.getId(), 
	    		    			YLXBatchStatus.request_send.name(), 
	    		    			YLXBatchSubStep.sub_step_2_1.name()); 
	    			}
	    		}catch(Exception e){
	    			// any runtime exception, try to rerun
	    			int times = batch.getRetryTimes()+1;
	    			String msg = e.getMessage();
	    			if(msg.length()>=4000)
	    				msg = msg.substring(0,3999);
	    			if(isFinalFail(batch.getType())){
	    				Logger.error(this, "openRequestDataPrepare Failed ! retry times:"+times+" batchId:"+batch.getId(),e);
	    				ylxBatchRepository.updateYLXBatchDTOFailById(batch.id(), 
		    					YLXBatchStatus.request_data_prepare_fail.name(), msg, String.valueOf(batch.id()), times);
	    			}else{//retry
	    				Logger.warn(this, "openRequestDataPrepare retry - times:"+times+" batchId:"+batch.getId(),e);
	    				ylxBatchRepository.updateYLXBatchDTOFailById(batch.id(), 
		    					YLXBatchStatus.request_data_prepare.name(), msg, null, times);
	    			}
	    		}	
    		}
    	}	
    */}


    @Transactional
    public void prepareData(YLXBatchDTO batch){
        Logger.info(this, String.format("OpenRequestDataPrepare begin, batchId:%s",batch.getId()));

        ylxOpenRequestRecordHandler.updateOpenRequestWithCurrentBatchId(batch);

        ylxOpenRequestBatchFileRecordCreator.insertBatchFile(batch);

        Logger.info(this, String.format("OpenRequestDataPrepare end, batchId:%s",batch.getId()));
    }

    public void createFiles(YLXBatchDTO batch){
        Logger.info(this, String.format("OpenRequestFileCreation begin, batchId:%s",batch.getId()));
        ylxOpenRequestBatchFileCreator.createFiles(batch);
        Logger.info(this, String.format("OpenRequestDataPrepare end, batchId:%s",batch.getId()));
    }


    /**
     * sub step 1-1
     * update batch id
     */

    /*private void updateOpenRequestWithCurrentBatchId(YLXBatchDTO batch){
        ylxOpenRequestDetailRepository.updateBatchId(-1L, batch.getId(), batch.getTargetDate());
        ylxBatchRepository.updateYLXBatchDTOSuccessById(batch.getId(),
                YLXBatchStatus.request_data_preparing.name(),
                YLXBatchSubStep.sub_step_1_3.name());
        batch.setSubNextStep(YLXBatchSubStep.sub_step_1_3.name());
    }*/

    
    /**
     * sub step 1-3
     * gennerate file name and  insert them into YLX_BATCH_FILE
     */
    /*@Transactional
    private void insertBatchFile(YLXBatchDTO batch){
		
		//create file name and insert batch file	
		Long count = ylxOpenRequestDetailRepository.countOpenRequestBybatchId(batch.getId());
		if(count!=0){
			long fileNumLoop =0l;// real file num is count/file_size +1
			if(count%FILE_SIZE==0){
				fileNumLoop = count/FILE_SIZE+1;
			}else if(count%FILE_SIZE>0){
				fileNumLoop = count/FILE_SIZE+2;
			}
			
			List<YLXBatchFileDTO> files = new ArrayList<YLXBatchFileDTO>();
			
			for(int i=1;i<fileNumLoop;i++){
				SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
				String target = format.format(batch.getTargetDate());
//				Date today = new Date();
//		    	today = DateUtils.startOfDay(today);
				String num = BatchFileContentUtil.getStrSeq(i);
				String fileName = "account_open_ljs_ta_"+target+"_"+num+".txt";
				YLXBatchFileDTO file = new YLXBatchFileDTO();
				file.setBatchId(batch.getId());
				file.setFileName(fileName);
				file.setStatus(YLXBatchFileStatus.init.name());
				file.setOrgCode(YlxConstants.SELL_ORG_CODE);
				file.setTrxDate(batch.getTargetDate());
				file.setVersion(YlxConstants.VERSION);
				files.add(file);	
			}
			
			ylxBatchFileRepository.batchInsert(files);
			//update heart beat and next sub step
	    	ylxBatchRepository.updateYLXBatchDTOSuccessById(batch.getId(), 
	    			YLXBatchStatus.request_data_preparing.name(), 
	    			YLXBatchSubStep.sub_step_1_4.name());
	    	batch.setSubNextStep(YLXBatchSubStep.sub_step_1_4.name());
		}else{
			//set job complete directly
	    	ylxBatchRepository.updateYLXBatchDTOFailById(batch.getId(), YLXBatchStatus.complete.name(),
	    			"no records need to handle today",String.valueOf(batch.getId()), batch.getRetryTimes());
	    	batch.setSubNextStep(YLXBatchSubStep.sub_step_complete.name());
		}
    	
    }*/

    /**
     * usually the input lufaxRiskLevel should be 3 or 4
     * 3 mapping to 2
     * 4 mapping to 3
     * 
     * consider some exception value, use 2 as default value to unblock biz
     * @param lufaxRiskLevel
     * @return
     */
    /*private String getYLXRiskLevel(String lufaxRiskLevel){
    	
    	String YLXRiskLevel = "2"; // set as default
    	if("4".equals(lufaxRiskLevel)){
    		YLXRiskLevel="3";
    	}
    	
    	return YLXRiskLevel;   	
    }*/
    
}
