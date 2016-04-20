package com.lufax.jijin.daixiao.schedular.Jobs;

import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.daixiao.constant.RecordStatus;
import com.lufax.jijin.daixiao.dto.JijinExDiscountDTO;
import com.lufax.jijin.daixiao.repository.JijinExDiscountRepository;
import com.lufax.jijin.daixiao.service.JijinExDiscountService;
import com.lufax.jijin.fundation.schedular.BaseBatchWithLimitJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HandleJijinExDiscountJob extends BaseBatchWithLimitJob<JijinExDiscountDTO, Long> {

	@Autowired
	private JijinExDiscountRepository jijinExDiscountRepository;
	@Autowired
	private JijinExDiscountService jijinExDiscountService;

	@Override
	protected Long getKey(JijinExDiscountDTO item) {
		return item.getId();
	}

	@Override
	protected List<JijinExDiscountDTO> fetchList(int batchAmount) {
		return jijinExDiscountRepository.getUnDispachedJijinExDiscount(batchAmount);
	}
	
	@Override
	public void process(JijinExDiscountDTO item){
		try{
			jijinExDiscountService.handleDiscount(item);
		}catch(Exception e){
			jijinExDiscountRepository.updateJijinExDiscount(MapUtils.buildKeyValueMap("id", item.getId(), "status", RecordStatus.FAILED.name()));
		}
	}

}
