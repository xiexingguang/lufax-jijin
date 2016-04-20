package com.lufax.jijin.ylx.request.service;
import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.product.constant.ProductCategory;
import com.lufax.jijin.ylx.batch.domain.*;
import com.lufax.jijin.ylx.batch.dto.YLXBatchDTO;
import com.lufax.jijin.ylx.request.domain.YLXBuyRequestBatchFileCreator;
import com.lufax.jijin.ylx.request.domain.YLXBuyRequestBatchFileRecordCreator;
import com.lufax.jijin.ylx.request.domain.YLXBuyRequestRecordHandler;
import com.lufax.jijin.ylx.request.domain.YLXBuyRequestWriter;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;

@Service
public class YLXBuyRequestService extends BaseRequestService{
	
	@Autowired
    YLXBuyRequestWriter buyWriter;
    @Autowired
    YLXBuyRequestBatchFileRecordCreator ylxBuyRequestBatchFileRecordCreator;
    @Autowired
    YLXBuyRequestRecordHandler ylxBuyRequestRecordHandler;
    @Autowired
    YLXBuyRequestBatchFileCreator ylxBuyRequestBatchFileCreator;
	
	/*@PostConstruct
	public void setWriter(){
		super.setWriter(purchaseWriter);
	}*/

    public void prepareBuyRequestData(){/*

    	List<YLXBatchDTO> batchDTOs = null;//getBatchRequestRecords(YLXBatchType.YLX_SLP_BUY_REQUEST);
    	
    	if (CollectionUtils.isNotEmpty(batchDTOs)){
    		for(YLXBatchDTO batch: batchDTOs){
	    		try{
	    			Date targetDate = batch.getTargetDate();
	    			if(prodCodeMapping.isEmpty()){// no third company code mapping, complete directly
	    				Logger.info(this, "buyRequestDataPrepare, no prod code mapping,skip! - cutOffId:" + cutOffId);
	    				ylxBatchRepository.updateYLXBatchDTOFailById(batch.id(), 
		    					YLXBatchStatus.complete.name(), "no prod code mapping,skip", String.valueOf(batch.id()), batch.getRetryTimes());
	    				continue;
	    			}
	    			Logger.info(this, "start buy request data prepare - batchId:"+batch.getId());
                    if(YLXBatchSubStep.sub_step_1_1.name().equals(batch.getSubNextStep())){
                        Logger.info(this, String.format("buyRequestDataPrepare sub step1: update to current batchId:%s",batch.getId()));
                        ylxPurchaseRequestRecordHandler.updateBuyRequestWithCurrentBatchId(batch);
                    }

                    // 3.insert batch file records
	    			if(YLXBatchSubStep.sub_step_1_3.name().equals(batch.getSubNextStep())){
	    				Logger.info(this, String.format("buyRequestDataPrepare sub step3: insertBatchFile - batchId:%s",batch.getId()));
                        ylxPurchaseRequestBatchFileRecordCreator.insertBatchFile(batch);
	    			} 
	    			// 4.create files
	    			if(YLXBatchSubStep.sub_step_1_4.name().equals(batch.getSubNextStep())){
	    				Logger.info(this, String.format("buyRequestDataPrepare sub step4: createFiles - batchId:%s",batch.getId()));
                        ylxPurchaseRequestBatchFileCreator.createFiles(batch);
	    			}
	    			// 5.update batch record status
	    			if(YLXBatchSubStep.sub_step_1_5.name().equals(batch.getSubNextStep())){
	    				Logger.info(this, String.format("buyRequestDataPrepare sub step5: updateFinalStatus - batchId:%s",batch.getId()));
	     		    	ylxBatchRepository.updateYLXBatchDTOSuccessById(batch.getId(), 
	    		    			YLXBatchStatus.request_send.name(), 
	    		    			YLXBatchSubStep.sub_step_2_1.name()); 
	    			}
	    		}catch(Exception e){
	    			// any runtime exception, try to rerun
	    			int times = batch.getRetryTimes()+1;
	    			String msg = e.getMessage();
	    			if(msg.length()>=4000)
	    				msg =msg.substring(0,3999);
	    			if(isFinalFail(batch.getType())){
	    				Logger.error(this, "buyRequestDataPrepare Failed ! retry times:"+times+" batchId:"+batch.getId(),e);
	    				ylxBatchRepository.updateYLXBatchDTOFailById(batch.id(), 
		    					YLXBatchStatus.request_data_prepare_fail.name(), msg, String.valueOf(batch.id()), times);
	    			}else{//retry
	    				Logger.warn(this, "buyRequestDataPrepare retry - times:"+times+" batchId:"+batch.getId(),e);
	    				ylxBatchRepository.updateYLXBatchDTOFailById(batch.id(), 
		    					YLXBatchStatus.request_data_prepare.name(), msg, null, times);
	    			}
	    		}
    		}
    	}	
    */}

    public void prepareData(YLXBatchDTO batch){
        Logger.info(this, String.format("buyRequestDataPrepare begin, batchId:%s",batch.getId()));

        ylxBuyRequestRecordHandler.updateBuyRequestWithCurrentBatchId(batch);

        ylxBuyRequestBatchFileRecordCreator.insertBatchFile(batch);

        Logger.info(this, String.format("buyRequestDataPrepare end, batchId:%s",batch.getId()));
    }

    public void createFiles(YLXBatchDTO batch){
        Logger.info(this, String.format("buyRequestFileCreation begin, batchId:%s",batch.getId()));
        ylxBuyRequestBatchFileCreator.createFiles(batch);
        Logger.info(this, String.format("buyRequestDataPrepare end, batchId:%s",batch.getId()));
    }

    /**
     * sub step 1-1
     * update batch id
     */

   /*private void updateBuyRequestWithCurrentBatchId(YLXBatchDTO batch){
       ylxSellRequestDetailRepository.updateBatchId(-1L, batch.getId(), batch.getTargetDate(), ProductCategory.SLP);
       ylxBatchRepository.updateYLXBatchDTOSuccessById(batch.getId(),
               YLXBatchStatus.request_data_preparing.name(),
               YLXBatchSubStep.sub_step_1_3.name());
       batch.setSubNextStep(YLXBatchSubStep.sub_step_1_3.name());
   }*/

    /**
     * sub step 1-3
     * insert YLX_BATCH_FILE
     *//*
    @Transactional
    private void insertBatchFile(YLXBatchDTO batch,Date targetDate){
		
		//create file name and insert batch file	
		Long count = ylxSellRequestDetailRepository.countBuyRequestBybatchId(batch.getId());
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
				String target = format.format(targetDate);
				String num = BatchFileContentUtil.getStrSeq(i);
				String fileName = "prod_buy_ljs_op_"+target+"_"+num+".txt";
				YLXBatchFileDTO file = new YLXBatchFileDTO();
				file.setBatchId(batch.getId());
				file.setFileName(fileName);
				file.setStatus(YLXBatchFileStatus.init.name());
				file.setOrgCode(YlxConstants.SELL_ORG_CODE);
				file.setTrxDate(targetDate);//上传日期，文件名里的交易日期
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
	    	ylxBatchRepository.updateYLXBatchDTOSuccessById(batch.getId(), 
	    			YLXBatchStatus.complete.name(), 
	    			YLXBatchSubStep.sub_step_complete.name());
	    	batch.setSubNextStep(YLXBatchSubStep.sub_step_complete.name());
		}
    	
    }*/
}
