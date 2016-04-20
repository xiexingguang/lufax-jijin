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

import com.lufax.jijin.fundation.dto.JijinUserBalanceAuditDTO;
import com.lufax.jijin.fundation.dto.JijinUserBalanceAuditDTO.Status;
import com.lufax.jijin.fundation.repository.JijinUserBalanceAuditRepository;
import com.lufax.mq.client.util.MapUtils;


@Ignore
@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class JijinUserBalanceAuditRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests{

	@Autowired
	protected JijinUserBalanceAuditRepository repository;
	
	@Test@Ignore
	public void testInsert() {
		
		JijinUserBalanceAuditDTO dto = new JijinUserBalanceAuditDTO();
		
		dto.setAvailableShare(new BigDecimal("12.21"));
		dto.setBizDate("20150916");
		dto.setContractNo("contractNo");
		dto.setDailyIncome(new BigDecimal("122"));
		dto.setDividendType("0");
		dto.setFeeType("A");
		dto.setFileId(1234l);
		dto.setFrozenShare(new BigDecimal("2.2"));
		dto.setFundCode("fundCode");
		dto.setInstId("lfx201");
		dto.setTotalIncome(new BigDecimal("232.33"));
		dto.setTotalShare(new BigDecimal("22223"));
		dto.setUnpaiedIncome(null);
		dto.setFundType("1");
		dto.setStatus(Status.NEW.name());
		
		dto.setUserId(12334l);
	   

		repository.insertBusJijinUserBalanceAudit(dto);
	}
	
	@Test@Ignore
	public void testFindActive(){
		
		JijinUserBalanceAuditDTO dto = repository.findBusJijinUserBalanceAudit(MapUtils.buildKeyValueMap("id",1));
		assertNotNull(dto);
		
	}


	@Test@Ignore
	public void testUpdate() {
		
		JijinUserBalanceAuditDTO dto = repository.findBusJijinUserBalanceAudit(MapUtils.buildKeyValueMap("id",1));
		
		int i = repository.updateBusJijinUserBalanceAudit(dto);
		assertTrue(i==1);
	}
	
	@Test@Ignore
	public void testGetUnDispatchedAudit() {

		List<JijinUserBalanceAuditDTO> dtos = repository.getUnDispatchedAuditsByType("1", Status.NEW, 100);
		for(JijinUserBalanceAuditDTO dto:dtos){
			System.out.println(dto.getFundType());
		}
	}

}
