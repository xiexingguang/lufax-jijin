package com.lufax.jijin.fundation.schedular;

import com.lufax.jijin.fundation.constant.FundSaleCode;
import com.lufax.jijin.fundation.constant.TradeRecordStatus;
import com.lufax.jijin.fundation.constant.TradeRecordType;
import com.lufax.jijin.fundation.dto.JijinTradeRecordDTO;
import com.lufax.jijin.fundation.repository.JijinTradeRecordRepository;
import com.lufax.jijin.fundation.service.RedeemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NiuZhanJun on 9/24/15.
 */
@Service

public class HandleTZeroRedeemJob extends BaseBatchWithTimeLimitJob<JijinTradeRecordDTO, Long> {

    @Autowired
    private JijinTradeRecordRepository jijinTradeRecordRepository;

    @Autowired
    private RedeemService redeemService;

    @Override
    protected Long getKey(JijinTradeRecordDTO item) {
        return item.getId();
    }

    @Override
    protected List<JijinTradeRecordDTO> fetchList(int batchAmount) {
        List<JijinTradeRecordDTO> list = new ArrayList<JijinTradeRecordDTO>();

        //获取type="REDEEM" and status="SUBMIT_SUCCESS" or "PAY_IN_ADVANCE" and instId="dh103" and redeemType="0"的记录
        //已提交的T+0赎回申请的记录
        list = jijinTradeRecordRepository.getUndoT0RedeemRecords(batchAmount);

        return list;
    }

    @Override
    protected void process(JijinTradeRecordDTO jijinTradeRecordDTO) {
        redeemService.handelT0RedeemApply(jijinTradeRecordDTO);
    }
}
