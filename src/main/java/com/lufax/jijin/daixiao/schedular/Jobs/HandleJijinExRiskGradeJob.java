package com.lufax.jijin.daixiao.schedular.Jobs;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lufax.jijin.daixiao.constant.RecordStatus;
import com.lufax.jijin.daixiao.dto.JijinExRiskGradeDTO;
import com.lufax.jijin.daixiao.repository.JijinExRiskGradeRepository;
import com.lufax.jijin.daixiao.service.JijinExRiskGradeService;
import com.lufax.jijin.fundation.schedular.BaseBatchWithLimitJob;
/**
 * 处理风险等级的job
 * @author chenqunhui
 *
 */
@Service
public class HandleJijinExRiskGradeJob extends BaseBatchWithLimitJob<JijinExRiskGradeDTO, Long> {

	@Autowired
	private JijinExRiskGradeService jijinExRiskGradeService;
	@Autowired
	private JijinExRiskGradeRepository jijinExRiskGradeRepository;

	@Override
	protected Long getKey(JijinExRiskGradeDTO item) {
		return item.getId();
	}

	@Override
	protected List<JijinExRiskGradeDTO> fetchList(int batchAmount) {
		return jijinExRiskGradeRepository.getUndispachedRiskGrade(batchAmount);
	}

	@Override
	public void process(JijinExRiskGradeDTO dto){
		try {
			jijinExRiskGradeService.handleJijinExRiskGrade(dto);
		} catch (Exception e) {
			jijinExRiskGradeRepository.updateJijinExRiskGradeStatus(dto.getId(), RecordStatus.FAILED.name(),e.getMessage());
		}
	}
}
