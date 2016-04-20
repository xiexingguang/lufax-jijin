package com.lufax.jijin.daixiao.schedular.Jobs;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lufax.jijin.daixiao.constant.RecordStatus;
import com.lufax.jijin.daixiao.service.JijinNetValueService;
import com.lufax.jijin.fundation.dto.JijinNetValueDTO;
import com.lufax.jijin.fundation.repository.JijinNetValueRepository;
import com.lufax.jijin.fundation.schedular.BaseBatchWithLimitJob;
import org.springframework.stereotype.Service;

/**
 * 处理净值文件里的基金状态数据
 * 
 * @author chenqunhui
 *
 */
@Service
public class HandleJijinNetValueJob extends BaseBatchWithLimitJob<JijinNetValueDTO, Long> {

	@Autowired
	private JijinNetValueRepository jijinNetValueRepository;
	@Autowired
	private JijinNetValueService jijinNetValueService;
	
	@Override
	protected Long getKey(JijinNetValueDTO item) {
		return item.getId();
	}

	@Override
	protected List<JijinNetValueDTO> fetchList(int batchAmount) {
		return jijinNetValueRepository.getUnDispachedNetValue(batchAmount);
	}
	
	@Override
	public void process(JijinNetValueDTO item){
		try{
			jijinNetValueService.handleJijinNetValue(item);
		}catch(Exception e){
			jijinNetValueRepository.updateJijinNetValueStatus(item.getId(), RecordStatus.FAILED.name());
		}
		
	}

}
