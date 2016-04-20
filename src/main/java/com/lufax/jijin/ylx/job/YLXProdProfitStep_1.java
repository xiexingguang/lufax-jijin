package com.lufax.jijin.ylx.job;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.scheduler.basejob.BaseJob;
import com.lufax.jijin.trade.service.YLXProfitService;
import com.lufax.jijin.ylx.batch.domain.YLXBatchStatus2;
import com.lufax.jijin.ylx.batch.domain.YLXBatchType;
import com.lufax.jijin.ylx.batch.dto.BatchRunStatus;
import com.lufax.jijin.ylx.batch.dto.YLXBatchDTO;
import com.lufax.jijin.ylx.batch.repository.YLXBatchRepository;
import com.lufax.jijin.ylx.confirm.service.BaseConfirmService;

@Component
public class YLXProdProfitStep_1 extends BaseJob<YLXBatchDTO,Long> {
    @Autowired
    private BaseConfirmService baseConfirmServie;
    @Autowired
    private YLXBatchRepository ylxBatchRepository;
    @Autowired 
    private YLXProfitService profitService;
    
    @Override
    protected void initJob(){
    	Logger.info(this, String.format("YLXPullProdProfitFileJob initJob"));
    	profitService.initBatchForProfit(YLXBatchType.YLX_SLP_PROD_PROFIT);
    };
    
    @Override
    protected List<YLXBatchDTO> fetchList(int batchAmount) {
    	List<YLXBatchDTO> batchList = baseConfirmServie.getAfterTriggerDateBatchRecords(YLXBatchType.YLX_SLP_PROD_PROFIT, asList(YLXBatchStatus2.CONFIRM_RECEIVE), BatchRunStatus.IDLE);
    	Logger.info(this, String.format("YLXPullProdProfitFileJob fetchList batchList : %s", new Gson().toJson(batchList)));
    	return batchList;
    }
    @Override
    protected void processList(List<YLXBatchDTO> list) {
    	List<Long> batchIds = new ArrayList<Long>(list.size());
    	for(YLXBatchDTO batchDTO : list){
    		batchIds.add(batchDTO.getId());
    	}
    	try{
    		ylxBatchRepository.updateBatchRunStatusByIds(batchIds, BatchRunStatus.ONGOING);
    		
    		for(YLXBatchDTO batchDTO : list){
                try {
                	baseConfirmServie.receiveFile(batchDTO);
                }catch (Exception e){
                    baseConfirmServie.sendPullConfirmFailSMSIfTimeout(batchDTO, "09:00" , "09:30");
                    Logger.error(this, String.format("YLXPullProdProfitFileJob failed,batchId:%s", batchDTO.getId()),e);
                }
            }
    	}finally{
    		ylxBatchRepository.updateBatchRunStatusByIds(batchIds, BatchRunStatus.IDLE);
    	}
    	
        
    }
}
