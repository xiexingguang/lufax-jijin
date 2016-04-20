package com.lufax.jijin.daixiao.schedular.Jobs;

import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.daixiao.constant.RecordStatus;
import com.lufax.jijin.daixiao.dto.JijinExSellDayCountDTO;
import com.lufax.jijin.daixiao.repository.JijinExSellDayCountRepository;
import com.lufax.jijin.daixiao.service.JijinExSellDayCountService;
import com.lufax.jijin.fundation.schedular.BaseBatchWithLimitJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HandleJijinExSellDayCountJob extends BaseBatchWithLimitJob<JijinExSellDayCountDTO, Long> {

	@Autowired
	private JijinExSellDayCountRepository jijinExSellDayCountRepository;
	@Autowired
	private JijinExSellDayCountService jijinExSellDayCountService;

	@Override
	protected Long getKey(JijinExSellDayCountDTO item) {
		return item.getId();
	}

	@Override
	protected List<JijinExSellDayCountDTO> fetchList(int batchAmount) {
		return jijinExSellDayCountRepository.getUnDispachedJijinExSellDayCount(batchAmount);
	}
	
	@Override
	public void process(JijinExSellDayCountDTO item){
		try{
			jijinExSellDayCountService.handleSellDayCount(item);
		}catch(Exception e){
			jijinExSellDayCountRepository.updateJijinExSellDayCount(MapUtils.buildKeyValueMap("id", item.getId(), "status", RecordStatus.FAILED.name()));
		}
	}

}
