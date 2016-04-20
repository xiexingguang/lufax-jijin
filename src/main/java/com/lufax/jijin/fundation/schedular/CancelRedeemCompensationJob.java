package com.lufax.jijin.fundation.schedular;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lufax.jijin.fundation.constant.TradeRecordType;
import com.lufax.jijin.fundation.dto.JijinTradeRecordDTO;
import com.lufax.jijin.fundation.repository.JijinTradeRecordRepository;
import com.lufax.jijin.fundation.service.CancelOrderService;

@Service
public class CancelRedeemCompensationJob extends BaseBatchJob<JijinTradeRecordDTO, Long>{

	@Autowired
	private JijinTradeRecordRepository jijinTradeRecordRepository;
	
    @Autowired
    private CancelOrderService cancelOrderService;



    @Override
    protected Long getKey(JijinTradeRecordDTO item) {
        return item.getId();
    }

    @Override
    protected List<JijinTradeRecordDTO> fetchList(int batchAmount) {

        return jijinTradeRecordRepository.getRecallingTradeRecords(TradeRecordType.REDEEM.name(), batchAmount);
    }

    @Override
    protected void process(JijinTradeRecordDTO jijinTradeRecordDTO) {
    	cancelOrderService.cancelRedeem(jijinTradeRecordDTO.getId(),true,jijinTradeRecordDTO.getUserId());
    }
}
