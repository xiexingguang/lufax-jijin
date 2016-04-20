package com.lufac.jijin.test.repository;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.lufax.jijin.fundation.dto.JijinBuyAuditDTO;
import com.lufax.jijin.fundation.dto.JijinRedeemAuditDTO;
import com.lufax.jijin.fundation.repository.JijinRedeemAuditRepository;
import com.lufax.mq.client.util.MapUtils;

@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class JijinRedeemAuditRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests{

	@Autowired
	protected JijinRedeemAuditRepository repository;
	
	@Test@Ignore
	public void testInsert() {
		JijinRedeemAuditDTO dto = new JijinRedeemAuditDTO();
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
		dto.setTxnMode("1");

		repository.insertBusJijinRedeemAudit(dto);
	}
	
	@Test@Ignore
	public void testFindActive(){
		
		JijinRedeemAuditDTO dto = repository.findBusJijinRedeemAudit(MapUtils.buildKeyValueMap("id",1));
		assertNotNull(dto);
		
	}

	@Test@Ignore
	public void testUpdate() {
		int i = repository.updateBusJijinRedeemAudit(MapUtils.buildKeyValueMap("id",1,"customerId","NEW"));
		assertTrue(i==1);
	}

}
