package com.lufax.jijin.daixiao.schedular.Jobs;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lufax.jijin.daixiao.dto.JijinExFxPerformDTO;
import com.lufax.jijin.daixiao.repository.JijinExFxPerformRepository;
import com.lufax.jijin.daixiao.service.JijinExFxPerformService;
import com.lufax.jijin.fundation.schedular.BaseBatchWithLimitJob;

@Service
public class HandleJijinExFxPerformJob  extends BaseBatchWithLimitJob<JijinExFxPerformDTO, Long> {

	@Autowired
	private JijinExFxPerformRepository jijinExFxPerformRepositroy;
	@Autowired
	private JijinExFxPerformService jijinExFxPerformService;
	
	@Override
	protected Long getKey(JijinExFxPerformDTO item) {
		return item.getId();
	}

	@Override
	protected List<JijinExFxPerformDTO> fetchList(int batchAmount) {
		return jijinExFxPerformRepositroy.getUnDispachedJijinExFxPerform(batchAmount);
	}
	
	public void process(JijinExFxPerformDTO dto){
		jijinExFxPerformService.handleJijinExPerform(dto);
	}

}
