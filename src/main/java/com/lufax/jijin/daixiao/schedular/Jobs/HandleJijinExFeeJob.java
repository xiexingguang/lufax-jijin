package com.lufax.jijin.daixiao.schedular.Jobs;

import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.daixiao.constant.RecordStatus;
import com.lufax.jijin.daixiao.dto.JijinExFeeDTO;
import com.lufax.jijin.daixiao.repository.JijinExFeeRepository;
import com.lufax.jijin.daixiao.service.JijinExFeeService;
import com.lufax.jijin.fundation.schedular.BaseBatchWithLimitJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HandleJijinExFeeJob extends BaseBatchWithLimitJob<JijinExFeeDTO, Long> {

	@Autowired
	private JijinExFeeRepository jijinExFeeRepository;
	@Autowired
	private JijinExFeeService jijinExFeeService;

	@Override
	protected Long getKey(JijinExFeeDTO item) {
		return item.getId();
	}

	@Override
	protected List<JijinExFeeDTO> fetchList(int batchAmount) {
		return jijinExFeeRepository.getUnDispachedJijinExFee(batchAmount);
	}
	
	@Override
	public void process(JijinExFeeDTO item){
		try{
			jijinExFeeService.handleFee(item);
		}catch(Exception e){
			jijinExFeeRepository.updateJijinExFee(MapUtils.buildKeyValueMap("id", item.getId(), "status", RecordStatus.FAILED.name()));
		}
	}

}
