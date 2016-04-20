package com.lufac.jijin.test.repository;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.lufax.jijin.fundation.dto.JijinRedeemBalDTO;
import com.lufax.jijin.fundation.repository.JijinRedeemBalRepository;

@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class JijinRedeemBalRespositoryTest extends AbstractTransactionalJUnit4SpringContextTests{

	@Autowired
	protected JijinRedeemBalRepository repository;
	
	@Test@Rollback(true)
	public void test() {
		
		//insert
		JijinRedeemBalDTO dto = new JijinRedeemBalDTO();
		dto.setAmount(new BigDecimal("100000.00"));
		dto.setFundCode("00379");
		dto.setSnapshotTime("20160201151210");
		dto.setVersion(0l);
		
		dto= repository.insertBusJijinRedeemBal(dto);

		assertTrue(dto.getId()>0);
		
		// update
		int result = repository.updateBusJijinRedeemBalById(dto.getId(), new BigDecimal("90.00"), dto.getVersion(), "20160201151211");
		assertTrue(result>0);
	}

}
