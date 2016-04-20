package com.lufax.jijin.fundation.schedular;


import com.lufax.jijin.fundation.constant.JijinBizType;
import com.lufax.jijin.fundation.dto.JijinTradeSyncDTO;
import com.lufax.jijin.fundation.repository.JijinTradeSyncRepository;
import com.lufax.jijin.fundation.service.JijinTradeSyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HandlePurchaseDispatchJob extends BaseBatchWithTimeLimitJob<JijinTradeSyncDTO, Long> {

    @Autowired
    private JijinTradeSyncService jijinTradeSyncService;
    @Autowired
    private JijinTradeSyncRepository jijinTradeSyncRepository;

    @Override
    protected Long getKey(JijinTradeSyncDTO item) {
        return item.getId();
    }

    @Override
    protected List<JijinTradeSyncDTO> fetchList(int batchAmount) {
        List<String> businessCodes = new ArrayList<String>();
        businessCodes.add(JijinBizType.APPLY_CONFIRM.getCode());
        businessCodes.add(JijinBizType.APPLY_CONFIRM_TMP.getCode());
        businessCodes.add(JijinBizType.PURCHASE_CONFIRM.getCode());
        businessCodes.add(JijinBizType.CREATE_FAIL.getCode());
        return jijinTradeSyncRepository.getUnDispatchRecords("NEW", batchAmount, businessCodes);
    }

    @Override
    protected void process(JijinTradeSyncDTO jijinTradeSyncDTO) {
        jijinTradeSyncService.dispatchPurchaseTradeSync(jijinTradeSyncDTO);
    }
}
