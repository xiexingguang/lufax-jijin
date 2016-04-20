package com.lufax.jijin.ylx.job.purchase;

import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.scheduler.basejob.BaseJob;
import com.lufax.jijin.ylx.batch.domain.YLXBatchStatus2;
import com.lufax.jijin.ylx.batch.domain.YLXBatchType;
import com.lufax.jijin.ylx.batch.dto.BatchRunStatus;
import com.lufax.jijin.ylx.batch.dto.YLXBatchDTO;
import com.lufax.jijin.ylx.batch.repository.YLXBatchRepository;
import com.lufax.jijin.ylx.request.service.YLXBuyRequestService;
import com.lufax.jijin.ylx.request.service.YLXPurchaseRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class YLXPurchaseRequestDataPrepareJob extends BaseJob<YLXBatchDTO,Long> {
    @Autowired
    YLXPurchaseRequestService ylxPurchaseRequestService;
    @Autowired
    private YLXBatchRepository ylxBatchRepository;
    @Override
    protected List<YLXBatchDTO> fetchList(int batchAmount) {
        return ylxPurchaseRequestService.getBatchRequestRecords(YLXBatchType.YLX_SLP_PURCHASE_REQUEST, YLXBatchStatus2.REQUEST_INIT, BatchRunStatus.IDLE);
    }

    @Override
    protected int getBatchAmount() {
        return 0;
    }

    @Override
    protected void processList(List<YLXBatchDTO> list) {
        for(YLXBatchDTO batchDTO : list){
            try {
                ylxBatchRepository.updateBatchRunStatusById(batchDTO.getId(),BatchRunStatus.ONGOING);
                ylxPurchaseRequestService.prepareData(batchDTO);
            }catch (Exception e){
                Logger.error(this,String.format("YLXPurchaseRequestDataPrepareJob failed,batchId:%s",batchDTO.getId()),e);
            }finally {
                ylxBatchRepository.updateBatchRunStatusById(batchDTO.getId(),BatchRunStatus.IDLE);
            }
        }

    }
}
