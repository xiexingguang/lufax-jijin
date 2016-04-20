package com.lufax.jijin.fundation.schedular;

import com.lufax.jijin.fundation.constant.DHBizType;
import com.lufax.jijin.fundation.dto.JijinTradeConfirmDTO;
import com.lufax.jijin.fundation.repository.JijinTradeConfirmRepository;
import com.lufax.jijin.fundation.service.JijinTradeConfirmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HandleDHTradeDispatchJob extends BaseBatchWithLimitJob<JijinTradeConfirmDTO, Long> {

    @Autowired
    private JijinTradeConfirmService jijinTradeConfirmService;
    @Autowired
    private JijinTradeConfirmRepository jijinTradeConfirmRepository;

    @Override
    protected Long getKey(JijinTradeConfirmDTO item) {
        return item.getId();
    }

    @Override
    protected List<JijinTradeConfirmDTO> fetchList(int maxNum) {
        List<String> bizTypes = new ArrayList<String>();
        bizTypes.add(DHBizType.REDEEM.getCode());
        bizTypes.add(DHBizType.PURCHASE.getCode());
        bizTypes.add(DHBizType.REDEEM_T1.getCode());
        return jijinTradeConfirmRepository.getUnDispatchConfirms("NEW", maxNum, bizTypes);
    }

    @Override
    protected void process(JijinTradeConfirmDTO jijinTradeConfirmDTO) {
        jijinTradeConfirmService.dispatchConfirm(jijinTradeConfirmDTO);
    }
}
