package com.lufax.jijin.fundation.schedular;

import com.lufax.jijin.fundation.dto.JijinThirdPaySyncDTO;
import com.lufax.jijin.fundation.repository.JijinThirdPaySyncRepository;
import com.lufax.jijin.fundation.service.JijinThirdPaySyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class PurchaseRefundJob extends BaseBatchWithTimeLimitJob<JijinThirdPaySyncDTO, Long> {

    @Autowired
    private JijinThirdPaySyncRepository jijinThirdPaySyncRepository;
    @Autowired
    private JijinThirdPaySyncService jijinThirdPaySyncService;


    @Override
    protected Long getKey(JijinThirdPaySyncDTO item) {
        return item.getId();
    }

    @Override
    protected List<JijinThirdPaySyncDTO> fetchList(int batchAmount) {
        return jijinThirdPaySyncRepository.getUnDispatchRecords(JijinThirdPaySyncDTO.Status.NEW, 
        		"PAF", "05", batchAmount);
    }

    @Override
    protected void process(JijinThirdPaySyncDTO jijinThirdPaySyncDTO) {
        jijinThirdPaySyncService.dispatchPurchaseRefund(jijinThirdPaySyncDTO);
    }
}
