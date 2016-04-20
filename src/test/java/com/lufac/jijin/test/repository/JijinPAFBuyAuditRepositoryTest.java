package com.lufac.jijin.test.repository;

import com.google.gson.Gson;
import com.lufax.jijin.fundation.dto.JijinPAFBuyAuditDTO;
import com.lufax.jijin.fundation.dto.PafBuyAuditCountDTO;
import com.lufax.jijin.fundation.repository.JijinPAFBuyAuditRepository;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertNotNull;

@Ignore
@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class JijinPAFBuyAuditRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    protected JijinPAFBuyAuditRepository repository;

    @Test
    public void testInsert() {
        JijinPAFBuyAuditDTO dto = new JijinPAFBuyAuditDTO();
        dto.setCurrency("CNY");
        dto.setCustomerId("1001");
        dto.setDistributorCode("211");
        dto.setFileId(1234l);
        dto.setFundDate("20150505");
        dto.setFundSeqId("20150508003721");
        dto.setFundTime("20150505010101");
        dto.setFundType("fundType");
        dto.setPayAcct("payAcct");
        dto.setPayAcctName("payAcctName");
        dto.setPayBankCode("payBankCode");
        dto.setPayBankName("payBankName");
        dto.setPayOrgId("payOrgId");
        dto.setRecBankCode("recBankCode");
        dto.setRecBankName("recBankName");
        dto.setReceiveAcct("receiveAcct");
        dto.setReceiveAcctName("receiveAcctName");
        dto.setStatus("status");
        dto.setTxnAmount(new BigDecimal("1000.00"));
        dto.setTxnDate("20150505");
        dto.setTxnType("txnType");
        dto.setVersion(0l);

        repository.insertBusJijinPAFBuyAudit(dto);
    }

    @Test
    @Ignore
    public void testGetFundSeqIdsByFileId() {

        List<String> seqs = repository.getFundSeqIdsByFileId(1234l);
        assertNotNull(seqs);


    }

    @Test@Ignore
    public void testCount(){
        PafBuyAuditCountDTO countDto = repository.countByFileIdAndStatus(357L, "DISPATCHED");
        System.out.println(new Gson().toJson(countDto));
    }

}
