package com.lufax.jijin.fundation.schedular;


import com.lufax.jijin.base.utils.DateUtils;
import com.lufax.jijin.fundation.constant.JijinBizType;
import com.lufax.jijin.fundation.dto.JijinTradeSyncDTO;
import com.lufax.jijin.fundation.repository.JijinTradeSyncRepository;
import com.lufax.jijin.fundation.service.JijinTradeSyncService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 处理异常数据,基金公司主动发起的操作
 */
@Service
public class HandleJijinSpecialDispatchJob extends BaseBatchWithLimitJob<JijinTradeSyncDTO, Long> {

    @Autowired
    private JijinTradeSyncService jijinTradeSyncService;
    @Autowired
    private JijinTradeSyncRepository jijinTradeSyncRepository;

    @Override
    protected Long getKey(JijinTradeSyncDTO item) {
        return item.getId();
    }

    @Override
    protected List<JijinTradeSyncDTO> fetchList(int batchAmount) {
        List<String> businessCodes = new ArrayList<String>();
        businessCodes.add(JijinBizType.FORCE_INCREASE.getCode());
        businessCodes.add(JijinBizType.FORCE_REDUCED.getCode());
        businessCodes.add(JijinBizType.FORCE_REDEEM.getCode());

        List<String> businessCodes2 = new ArrayList<String>();
        businessCodes2.add(JijinBizType.FORCE_REDEEM.getCode()); 
        
        List<JijinTradeSyncDTO> targetList = new ArrayList<JijinTradeSyncDTO>();
        
        targetList.addAll(jijinTradeSyncRepository.getUnDispatchRecords("REDO", batchAmount, businessCodes2));
        targetList.addAll(jijinTradeSyncRepository.getUnDispatchRecords("NEW", batchAmount, businessCodes));
         
         return targetList;

    }

    @Override
    protected void process(JijinTradeSyncDTO jijinTradeSyncDTO) {
        if (jijinTradeSyncDTO.getBusinessCode().equals(JijinBizType.FORCE_INCREASE.getCode())) {
            jijinTradeSyncService.dispatchForceIncrease(jijinTradeSyncDTO);
        } else if (jijinTradeSyncDTO.getBusinessCode().equals(JijinBizType.FORCE_REDUCED.getCode())) {
            jijinTradeSyncService.dispatchForceReduced(jijinTradeSyncDTO);
        } else if (JijinBizType.FORCE_REDEEM.getCode().equals(jijinTradeSyncDTO.getBusinessCode())){
        	jijinTradeSyncService.dispatchForceRedeem(jijinTradeSyncDTO);
        }
    }
}
