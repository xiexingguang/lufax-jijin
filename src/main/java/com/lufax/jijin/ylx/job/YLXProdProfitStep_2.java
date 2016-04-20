package com.lufax.jijin.ylx.job;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
public class YLXProdProfitStep_2 extends BaseJob<YLXBatchDTO,Long> {
    @Autowired
    private BaseConfirmService baseConfirmServie;
    @Autowired
    private YLXProfitService profitService;
    @Autowired
    private YLXBatchRepository ylxBatchRepository;
    @Override
    protected List<YLXBatchDTO> fetchList(int batchAmount) {
        return baseConfirmServie.getAfterTriggerDateBatchRecords(YLXBatchType.YLX_SLP_PROD_PROFIT, asList(YLXBatchStatus2.RECEIVED_PREPARE), BatchRunStatus.IDLE);
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
                profitService.prepareProfit(batchDTO);
            }
    	}finally{
    		ylxBatchRepository.updateBatchRunStatusByIds(batchIds, BatchRunStatus.IDLE);
    	}
    }
}
