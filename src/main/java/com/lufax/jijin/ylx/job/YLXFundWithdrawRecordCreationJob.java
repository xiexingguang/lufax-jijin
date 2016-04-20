package com.lufax.jijin.ylx.job;

import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.scheduler.basejob.BaseJob;
import com.lufax.jijin.ylx.batch.domain.YLXBatchStatus2;
import com.lufax.jijin.ylx.batch.domain.YLXBatchType;
import com.lufax.jijin.ylx.batch.dto.BatchRunStatus;
import com.lufax.jijin.ylx.batch.dto.YLXBatchDTO;
import com.lufax.jijin.ylx.batch.repository.YLXBatchRepository;
import com.lufax.jijin.ylx.dto.repository.YLXBuyRequestDetailRepository;
import com.lufax.jijin.ylx.request.service.YLXFundWithdrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class YLXFundWithdrawRecordCreationJob  extends BaseJob<YLXBatchDTO,Long> {
    @Autowired
    private YLXBatchRepository ylxBatchRepository;
    @Autowired
    private YLXBuyRequestDetailRepository ylxBuyRequestDetailRepository;
    @Autowired
    private YLXFundWithdrawService ylxFundWithdrawService;

    @Override
    protected List<YLXBatchDTO> fetchList(int batchAmount) {
        List<YLXBatchDTO> ylxBatchDTOs = new ArrayList<YLXBatchDTO>();
        ylxBatchDTOs.addAll(ylxBatchRepository.getYLXBatchDTOByTypeAndStatusAndRunStatus(YLXBatchStatus2.REQUEST_FILE_SEND.name(), YLXBatchType.YLX_SLP_BUY_REQUEST.name(), BatchRunStatus.IDLE));
        ylxBatchDTOs.addAll(ylxBatchRepository.getYLXBatchDTOByTypeAndStatusAndRunStatus(YLXBatchStatus2.REQUEST_FILE_SEND.name(), YLXBatchType.YLX_SLP_PURCHASE_REQUEST.name(), BatchRunStatus.IDLE));
        return ylxBatchDTOs;
    }

    @Override
    protected int getBatchAmount() {
        return 0;
    }

    @Override
    protected void processList(List<YLXBatchDTO> list) {
        for(YLXBatchDTO batchDTO : list){
            try {
                ylxBatchRepository.updateBatchRunStatusById(batchDTO.getId(), BatchRunStatus.ONGOING);
                ylxFundWithdrawService.createFundRecord(ylxBuyRequestDetailRepository.getByBatchId(batchDTO.id()),batchDTO);
            }catch (Exception e){
                Logger.error(this, String.format("buyRequestDataPrepare failed,batchId:%s", batchDTO.getId()),e);
            }finally {
                ylxBatchRepository.updateBatchRunStatusById(batchDTO.getId(), BatchRunStatus.IDLE);
            }
        }
    }
}
