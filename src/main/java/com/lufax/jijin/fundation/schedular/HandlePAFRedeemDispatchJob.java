package com.lufax.jijin.fundation.schedular;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lufax.jijin.base.utils.DateUtils;
import com.lufax.jijin.fundation.dto.JijinThirdPaySyncDTO;
import com.lufax.jijin.fundation.repository.JijinThirdPaySyncRepository;
import com.lufax.jijin.fundation.service.JijinThirdPaySyncService;

/**
 * 处理平安付代付结果 - 赎回代付
 * @author xuneng
 *
 */
@Service
public class HandlePAFRedeemDispatchJob extends BaseBatchWithTimeLimitJob<JijinThirdPaySyncDTO, Long> {

    @Autowired
    private JijinThirdPaySyncService jijinThirdPaySyncService;
    @Autowired
    private JijinThirdPaySyncRepository jijinThirdPaySyncRepository;
  

    @Override
    protected Long getKey(JijinThirdPaySyncDTO item) {
        return item.getId();
    }

    @Override
    protected List<JijinThirdPaySyncDTO> fetchList(int batchAmount) {
    	
    	List<JijinThirdPaySyncDTO> targetList = new ArrayList<JijinThirdPaySyncDTO>();
    	
    	targetList.addAll(jijinThirdPaySyncRepository.getUnDispatchRecords(JijinThirdPaySyncDTO.Status.NEW, 
        		"PAF", "04", batchAmount));
    	
        return targetList;
    }

    @Override
    protected void process(JijinThirdPaySyncDTO jijinThirdPaySyncDTO) {
       	jijinThirdPaySyncService.dispatchPAFRedeemSync(jijinThirdPaySyncDTO);
    }
}
