package com.lufax.jijin.daixiao.schedular.Jobs;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lufax.jijin.daixiao.constant.RecordStatus;
import com.lufax.jijin.daixiao.dto.JijinExHotSubjectDTO;
import com.lufax.jijin.daixiao.repository.JijinExHotSubjectRepository;
import com.lufax.jijin.daixiao.service.JijinExHotSubjectService;
import com.lufax.jijin.fundation.schedular.BaseBatchWithLimitJob;

@Service
public class HandleJijinExHotSubjectJob extends BaseBatchWithLimitJob<JijinExHotSubjectDTO, Long> {

	@Autowired
	private JijinExHotSubjectRepository jijinExHotSubjectRepository;
	@Autowired
	private JijinExHotSubjectService jijinExHotSubjectService;

	@Override
	protected Long getKey(JijinExHotSubjectDTO item) {
		return item.getId();
	}

	@Override
	protected List<JijinExHotSubjectDTO> fetchList(int batchAmount) {
		return jijinExHotSubjectRepository.getUndispachedHotSubject(batchAmount);
	}

	@Override
	public void process(JijinExHotSubjectDTO dto){
		
		try {
			jijinExHotSubjectService.handleJijinExHotSubject(dto);
		} catch (Exception e) {
			jijinExHotSubjectRepository.batchUpdateHotSubjectStatusByBatchIdAndFundCode(dto.getBatchId(), dto.getFundCode(),RecordStatus.DISPACHED.name(),e.getMessage());
		}
		
	}
}
