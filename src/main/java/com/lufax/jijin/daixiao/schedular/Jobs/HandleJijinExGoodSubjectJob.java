package com.lufax.jijin.daixiao.schedular.Jobs;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lufax.jijin.daixiao.constant.RecordStatus;
import com.lufax.jijin.daixiao.dto.JijinExGoodSubjectDTO;
import com.lufax.jijin.daixiao.repository.JijinExGoodSubjectRepository;
import com.lufax.jijin.daixiao.service.JijinExGoodSubjectService;
import com.lufax.jijin.fundation.schedular.BaseBatchWithLimitJob;

@Service
public class HandleJijinExGoodSubjectJob extends BaseBatchWithLimitJob<JijinExGoodSubjectDTO, Long> {


	@Autowired
	private JijinExGoodSubjectService jijinExGoodSubjectService;
	@Autowired
	private JijinExGoodSubjectRepository jijinExGoodSubjectRepository;
	
	@Override
	protected Long getKey(JijinExGoodSubjectDTO item) {
		return item.getId();
	}

	@Override
	protected List<JijinExGoodSubjectDTO> fetchList(int batchAmount) {
		return jijinExGoodSubjectRepository.getUnDispachedJijinExGoodSubjectList(batchAmount);
	}

	@Override
	public void process(JijinExGoodSubjectDTO dto){
		try {
			jijinExGoodSubjectService.handleJijinExGoodSubject(dto);
		} catch (Exception e) {
			jijinExGoodSubjectRepository.batchUpdateJijinExGoodSubjectStatus(dto.getBatchId(), dto.getFundCode(), RecordStatus.FAILED.name(),e.getMessage());
		}
	}
}
