package com.lufax.jijin.fundation.schedular;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lufax.jijin.fundation.dto.JijinUserBalanceAuditDTO;
import com.lufax.jijin.fundation.dto.JijinUserBalanceAuditDTO.Status;
import com.lufax.jijin.fundation.repository.JijinUserBalanceAuditRepository;
import com.lufax.jijin.fundation.service.JijinIncomeDispatchService;
import com.lufax.mq.client.util.Logger;

/**
 * 处理货基收益文件
 * @author xuneng
 *
 */
@Service
public class HandleIncomeDispatchJob extends BaseBatchWithTimeLimitJob<JijinUserBalanceAuditDTO, Long> {

    @Autowired
    private JijinIncomeDispatchService jijinIncomeDispatchService;

    
    @Autowired
    private JijinUserBalanceAuditRepository jijinUserBalanceAuditRepository;
    

    @Override
    protected Long getKey(JijinUserBalanceAuditDTO item) {
        return item.getId();
    }

    @Override
    protected List<JijinUserBalanceAuditDTO> fetchList(int batchAmount) {
        
       return  jijinUserBalanceAuditRepository.getUnDispatchedAuditsByType("1",Status.NEW,batchAmount);
        
    }

    @Override
    protected void process(JijinUserBalanceAuditDTO auditDto) {

    	try{
    		jijinIncomeDispatchService.dispatchIncome(auditDto);
    	}catch(Exception e){
    		Logger.warn(this, String.format("Sync daily income failed. user balance audit id:[%s]", auditDto.getId()),e);
    	}
    }
}
