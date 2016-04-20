package com.lufax.jijin.daixiao.schedular.Jobs;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lufax.jersey.utils.Logger;
import com.lufax.jijin.daixiao.dto.JijinExDictDTO;
import com.lufax.jijin.daixiao.repository.JijinExDictRepository;
import com.lufax.jijin.daixiao.service.JijinExDictService;
import com.lufax.jijin.fundation.schedular.BaseBatchWithLimitJob;
/**
 * 处理代销目录的job
 * @author chenqunhui
 *
 */
@Service
public class HandleJijinExDictJob extends BaseBatchWithLimitJob<JijinExDictDTO, Long>{

	@Autowired
	private JijinExDictRepository jijinExDictRepository;
	@Autowired
	private JijinExDictService jijinExDictService;
	
	@Override
	protected Long getKey(JijinExDictDTO item) {
		return item.getId();
	}

	@Override
	protected List<JijinExDictDTO> fetchList(int batchAmount) {
		return jijinExDictRepository.getUnDispachedJijinExDictList(batchAmount);
	}

	//新增代销基金信息
	protected void process(JijinExDictDTO item) {
		Logger.info(HandleJijinExDictJob.class, String.format("handle dict fund code=[%s]",item.getFundCode()));
		jijinExDictService.handExDict(item);
    }
	
	
	

}
