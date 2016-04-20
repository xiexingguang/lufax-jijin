package com.lufax.jijin.daixiao.schedular.Jobs;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lufax.jersey.utils.Logger;
import com.lufax.jijin.base.utils.StringUtils;
import com.lufax.jijin.daixiao.constant.RecordStatus;
import com.lufax.jijin.daixiao.dto.JijinExMfPerformDTO;
import com.lufax.jijin.daixiao.repository.JijinExMfPerformRepository;
import com.lufax.jijin.daixiao.service.JijinExMfPerformService;
import com.lufax.jijin.fundation.schedular.BaseBatchWithLimitJob;

@Service
public class HandleJijinExMfPerformJob extends BaseBatchWithLimitJob<JijinExMfPerformDTO, Long> {

	@Autowired
	private JijinExMfPerformService jijinExMfPerformService;
	@Autowired
	private JijinExMfPerformRepository jijinExMfPerformRepository;
	
	@Override
	protected Long getKey(JijinExMfPerformDTO item) {
		return item.getId();
	}

	@Override
    protected List<JijinExMfPerformDTO> fetchList(int batchAmount) {
        return jijinExMfPerformRepository.getUnDispatchedJijinExMfPerform(batchAmount);
    }
	
	@Override
    public void process(JijinExMfPerformDTO dto) {
		try {
			 jijinExMfPerformService.handleJijinExMfPerform(dto);
		    } catch (Exception e) {
		    	Logger.warn(this, String.format("handleJijinExMfPerform error id is[%s]",dto.getId()));
		    	
		    	String errorMsg = e.getMessage();
	            if (StringUtils.isNotBlank(errorMsg) && errorMsg.length() >= 1000) {
	            	jijinExMfPerformRepository.updateJijinExMfPerformStatus(dto.getId(), RecordStatus.FAILED.name(),0, errorMsg.substring(0, 1000));
	            } else {
	            	jijinExMfPerformRepository.updateJijinExMfPerformStatus(dto.getId(), RecordStatus.FAILED.name(),0, errorMsg);
	            }
		    }
    }
}
