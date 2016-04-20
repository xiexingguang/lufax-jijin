package com.lufax.jijin.daixiao.schedular.Jobs;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lufax.jijin.daixiao.constant.RecordStatus;
import com.lufax.jijin.daixiao.dto.JijinExAnnounceDTO;
import com.lufax.jijin.daixiao.repository.JijinExAnnounceRepository;
import com.lufax.jijin.fundation.schedular.BaseBatchWithTimeLimitJob;

@Service
public class HandleJijinExAnnounceJob extends BaseBatchWithTimeLimitJob<JijinExAnnounceDTO, Long>{

	@Autowired
	private JijinExAnnounceRepository repository;
	
	@Override
	protected Long getKey(JijinExAnnounceDTO item) {
		return item.getId();
	}

	@Override
	protected List<JijinExAnnounceDTO> fetchList(int batchAmount) {
		return repository.findUnDispatchedAnnounce(batchAmount);
	}
	
	@Transactional
	public void process(JijinExAnnounceDTO item){
		if(!RecordStatus.NEW.name().equals(item.getStatus())){
			return;
		}
		int count = repository.countBatchIdIsBiggerThanThis(item.getObjectId(), item.getBatchId());
		if(count>0){
			//存在batchid更大的数据，本条数据置为无效
			repository.updateStatusAndIsValidById(item.getId(), 0, RecordStatus.NO_USED.name());
		}else{
			//不存在这样的数据
			repository.updateIsValidByObjectIdAndMaxBatchId(item.getObjectId(), item.getBatchId());
			repository.updateStatusAndIsValidById(item.getId(), 1, RecordStatus.DISPACHED.name());
		}
	}

}
