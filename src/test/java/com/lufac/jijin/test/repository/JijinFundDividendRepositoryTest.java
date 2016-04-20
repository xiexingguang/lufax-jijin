package com.lufac.jijin.test.repository;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.lufax.jijin.fundation.dto.JijinFundDividendDTO;
import com.lufax.jijin.fundation.repository.JijinFundDividendRepository;

@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class JijinFundDividendRepositoryTest  extends AbstractTransactionalJUnit4SpringContextTests{
	
	@Autowired
	JijinFundDividendRepository repo;

	@Test@Ignore
	public void testFindBusJijinFundDividend() {
		List<JijinFundDividendDTO> dtos = repo.findBusJijinFundDividendsByFund("320010");
		assertTrue(dtos.size()>0);
	}

}
