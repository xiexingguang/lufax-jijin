package com.lufax.jijin.fundation.schedular;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lufax.jijin.fundation.dto.JijinPAFBuyAuditDTO;
import com.lufax.jijin.fundation.repository.JijinPAFBuyAuditRepository;
import com.lufax.jijin.fundation.service.JijinPAFBuyAuditService;

/**
 * 处理平安付认申购文件
 * @author xuneng
 *
 */
@Service
public class HandlePAFBuyAuditDispatchJob extends BaseBatchWithTimeLimitJob<JijinPAFBuyAuditDTO, Long> {

    @Autowired
    private JijinPAFBuyAuditService jijinPAFBuyAuditService;
    @Autowired
    private JijinPAFBuyAuditRepository jijinPAFBuyAuditRepository;
  

    @Override
    protected Long getKey(JijinPAFBuyAuditDTO item) {
        return item.getId();
    }

    @Override
    protected List<JijinPAFBuyAuditDTO> fetchList(int batchAmount) {
        return jijinPAFBuyAuditRepository.getUnDispatchRecords(batchAmount);
    }

    @Override
    protected void process(JijinPAFBuyAuditDTO jijinPAFBuyAuditDTO) {
    	jijinPAFBuyAuditService.dispatchPAFBuyAudit(jijinPAFBuyAuditDTO);
    }
}
