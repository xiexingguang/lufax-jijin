package com.lufax.jijin.daixiao.schedular.Jobs;

import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.daixiao.constant.RecordStatus;
import com.lufax.jijin.daixiao.dto.JijinExIncomeModeDTO;
import com.lufax.jijin.daixiao.repository.JijinExIncomeModeRepository;
import com.lufax.jijin.daixiao.service.JijinExIncomeModeService;
import com.lufax.jijin.fundation.schedular.BaseBatchWithLimitJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HandleJijinExIncomeModeJob extends BaseBatchWithLimitJob<JijinExIncomeModeDTO, Long> {

	@Autowired
	private JijinExIncomeModeRepository jijinExIncomeModeRepository;
	@Autowired
	private JijinExIncomeModeService jijinExIncomeModeService;

	@Override
	protected Long getKey(JijinExIncomeModeDTO item) {
		return item.getId();
	}

	@Override
	protected List<JijinExIncomeModeDTO> fetchList(int batchAmount) {
		return jijinExIncomeModeRepository.getUnDispachedJijinExIncomeMode(batchAmount);
	}
	
	@Override
	public void process(JijinExIncomeModeDTO item){
		try{
			jijinExIncomeModeService.handleIncomeMode(item);
		}catch(Exception e){
			jijinExIncomeModeRepository.updateJijinExIncomeMode(MapUtils.buildKeyValueMap("id", item.getId(), "status", RecordStatus.FAILED.name()));
		}
	}

}
