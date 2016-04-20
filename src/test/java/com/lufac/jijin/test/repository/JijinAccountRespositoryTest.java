package com.lufac.jijin.test.repository;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.lufax.jijin.fundation.dto.JijinAccountDTO;
import com.lufax.jijin.fundation.repository.JijinAccountRepository;
import com.lufax.mq.client.util.MapUtils;

@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class JijinAccountRespositoryTest extends AbstractTransactionalJUnit4SpringContextTests{

	@Autowired
	protected JijinAccountRepository repository;
	
	@Test@Rollback(true)
	public void testInsert() {
		
		//insert
		JijinAccountDTO dto = new JijinAccountDTO();
		dto.setUserId(4l);
		dto.setInstId("htf102");
		dto.setPayNo("pay_test_max");
		dto.setChannel("PAF");
		dto.setContractNo("contract_no");
		dto.setCustNo("customer");
		dto.setDeleted(false);
		JijinAccountDTO JijinAccountDTO = repository.insertBusJijinAccount(dto);
		Long dtoId = JijinAccountDTO.getId();
		assertTrue(dtoId>0);
		
		// find
		JijinAccountDTO dto1 = repository.findActiveAccount(4l, "htf102", "PAF");
		assertNotNull(dto1);
		
		//update
		int result = repository.updateBusJijinAccountById(MapUtils.buildKeyValueMap("id",dtoId,"contractNo","contract_no_2"));
		assertTrue(result==1);
	}

}
