package com.lufax.jijin.fundation.schedular;


import com.lufax.jijin.fundation.constant.JijinBizType;
import com.lufax.jijin.fundation.dto.JijinTradeSyncDTO;
import com.lufax.jijin.fundation.repository.JijinTradeSyncRepository;
import com.lufax.jijin.fundation.service.JijinTradeSyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 处理基金公司交易确认文件数据 - 赎回交易
 * @author xuneng
 *
 */
@Service
public class HandleRedeemDispatchJob extends BaseBatchWithTimeLimitJob<JijinTradeSyncDTO, Long> {

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
        businessCodes.add(JijinBizType.REDEEM_CONFIRM.getCode());
        return jijinTradeSyncRepository.getUnDispatchRecords("NEW", batchAmount, businessCodes);
    }

    @Override
    protected void process(JijinTradeSyncDTO jijinTradeSyncDTO) {

    	jijinTradeSyncService.dispatchRedeemTradeSync(jijinTradeSyncDTO);
    }
}
