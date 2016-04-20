package com.lufax.jijin.daixiao.schedular.Jobs;

import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.daixiao.constant.RecordStatus;
import com.lufax.jijin.daixiao.dto.JijinExBuyLimitDTO;
import com.lufax.jijin.daixiao.repository.JijinExBuyLimitRepository;
import com.lufax.jijin.daixiao.service.JijinExBuyLimitService;
import com.lufax.jijin.fundation.schedular.BaseBatchWithLimitJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HandleJijinExBuyLimitJob extends BaseBatchWithLimitJob<JijinExBuyLimitDTO, Long> {

	@Autowired
	private JijinExBuyLimitRepository jijinExBuyLimitRepository;
	@Autowired
	private JijinExBuyLimitService jijinExBuyLimitService;

	@Override
	protected Long getKey(JijinExBuyLimitDTO item) {
		return item.getId();
	}

	@Override
	protected List<JijinExBuyLimitDTO> fetchList(int batchAmount) {
		return jijinExBuyLimitRepository.getUnDispachedJijinExBuyLimit(batchAmount);
	}
	
	@Override
	public void process(JijinExBuyLimitDTO item){
		try{
			jijinExBuyLimitService.handleBuyLimit(item);
		}catch(Exception e){
			Logger.warn(this, String.format("handle jijinExBuyLimit info occured exception ,id [%s]", item.getId()));
			jijinExBuyLimitRepository.updateJijinExBuyLimit(MapUtils.buildKeyValueMap("id", item.getId(), "status", RecordStatus.FAILED.name()));
		}
	}

}
