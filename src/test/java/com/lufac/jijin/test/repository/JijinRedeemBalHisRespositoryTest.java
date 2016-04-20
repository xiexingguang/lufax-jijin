package com.lufac.jijin.test.repository;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.lufax.jijin.fundation.dto.JijinRedeemBalHisDTO;
import com.lufax.jijin.fundation.repository.JijinRedeemBalHisRepository;

@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class JijinRedeemBalHisRespositoryTest extends AbstractTransactionalJUnit4SpringContextTests{

	@Autowired
	protected JijinRedeemBalHisRepository repository;
	
	@Test@Rollback(true)
	public void test() {
		
		//insert
		JijinRedeemBalHisDTO dto = new JijinRedeemBalHisDTO();
		dto.setAmount(new BigDecimal("100000.00"));
		dto.setRemark("test");
		
		dto= repository.insertBusJijinRedeemBalHis(dto);
		
		JijinRedeemBalHisDTO dto2 = new JijinRedeemBalHisDTO();
		dto2.setAmount(new BigDecimal("-100000.00"));
		dto2.setRemark("test");
		
		dto2= repository.insertBusJijinRedeemBalHis(dto2);
		

		assertTrue(dto.getId()>0);
		
		// update
		List<JijinRedeemBalHisDTO> dtos = repository.findBusJijinRedeemBalHisAfterTime("20160101121212");
		assertTrue(dtos.size()>0);
	}

}
