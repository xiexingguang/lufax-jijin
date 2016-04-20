package com.lufac.jijin.test.repository;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.lufax.jijin.fundation.dto.JijinTradeReconDTO;
import com.lufax.jijin.fundation.repository.JijinTradeReconRepository;

@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class JijinTradeReconRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    protected JijinTradeReconRepository repository;

    @Test@Ignore
    public void testInsert() {

            JijinTradeReconDTO dto = new JijinTradeReconDTO();
            dto.setBuyAuditId("buyAuditId");
            dto.setRecordId("recordId");
            dto.setRemark("remark");
            dto.setStatus("LACK_BUY_AUDIT");
            repository.insertBusJijinBuyAudit(dto);

    }


}
