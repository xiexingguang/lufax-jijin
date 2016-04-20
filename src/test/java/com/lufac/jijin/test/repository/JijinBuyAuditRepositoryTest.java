package com.lufac.jijin.test.repository;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.lufax.jijin.fundation.dto.JijinBuyAuditDTO;
import com.lufax.jijin.fundation.dto.JijinPAFBuyAuditDTO;
import com.lufax.jijin.fundation.repository.JijinBuyAuditRepository;
import com.lufax.mq.client.util.MapUtils;

@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class JijinBuyAuditRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests{

	@Autowired
	protected JijinBuyAuditRepository repository;
	
	@Test@Ignore
	public void testInsert() {
		JijinBuyAuditDTO dto = new JijinBuyAuditDTO();
		dto.setCurrency("CNY");
		dto.setCustomerId("customerId");
		dto.setDistributorCode("distributorCode");
		dto.setFileId(123l);
		dto.setFundDate("fundDate");
		dto.setFundSeqId("fundSeqId");
		dto.setFundTime("fundTime");
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
		dto.setTxnAmount(new BigDecimal("100.00"));
		dto.setTxnDate("txnDate");
		dto.setTxnType("txnType");

		repository.insertBusJijinBuyAudit(dto);
	}
	
	@Test@Ignore
	public void testFindActive(){
		
		JijinBuyAuditDTO dto = repository.findBusJijinBuyAudit(MapUtils.buildKeyValueMap("id",1));
		assertNotNull(dto);
		
	}

	@Test@Ignore
	public void testUpdate() {
		int i = repository.updateBusJijinBuyAudit(MapUtils.buildKeyValueMap("id",1,"customerId","NEW"));
		assertTrue(i==1);
	}

}
