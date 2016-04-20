package com.lufax.jijin.fundation.schedular;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lufax.jijin.fundation.constant.TradeRecordType;
import com.lufax.jijin.fundation.dto.JijinTradeRecordDTO;
import com.lufax.jijin.fundation.repository.JijinTradeRecordRepository;
import com.lufax.jijin.fundation.service.RedeemService;

@Service
public class RedeemCompensationJob extends BaseBatchJob<JijinTradeRecordDTO, Long>{

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

        return jijinTradeRecordRepository.getUnknownTradeRecords(TradeRecordType.REDEEM.name(), batchAmount);
    }

    @Override
    protected void process(JijinTradeRecordDTO jijinTradeRecordDTO) {
    	redeemService.redeemCompensation(jijinTradeRecordDTO);
    }
}
