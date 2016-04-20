package com.lufax.jijin.daixiao.schedular.Jobs;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lufax.jijin.daixiao.constant.RecordStatus;
import com.lufax.jijin.daixiao.dto.JijinExFundTypeDTO;
import com.lufax.jijin.daixiao.repository.JijinExFundTypeRepository;
import com.lufax.jijin.daixiao.service.JijinExFundTypeService;
import com.lufax.jijin.fundation.schedular.BaseBatchWithLimitJob;
@Service
public class HandleJijinExFundTypeJob extends BaseBatchWithLimitJob<JijinExFundTypeDTO, Long> {

	@Autowired
	private JijinExFundTypeRepository jijinExFundTypeRepository;
	@Autowired
	private JijinExFundTypeService jijinExFundTypeService;

	@Override
	protected Long getKey(JijinExFundTypeDTO item) {
		return item.getId();
	}

	@Override
	protected List<JijinExFundTypeDTO> fetchList(int batchAmount) {
		return jijinExFundTypeRepository.getUnDispachedJijinExFundType(batchAmount);
	}
	
	@Override
	public void process(JijinExFundTypeDTO item){
		try{
			jijinExFundTypeService.handleFundType(item);
		}catch(Exception e){
			jijinExFundTypeRepository.updateJijinExFundTypeStatus(item.getId(), RecordStatus.FAILED.name(),e.getMessage());
		}
	}

}
