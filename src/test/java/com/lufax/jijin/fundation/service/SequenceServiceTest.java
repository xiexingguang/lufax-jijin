package com.lufax.jijin.fundation.service;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.lufax.jijin.fundation.constant.JijinBizType;

@TransactionConfiguration(defaultRollback = true)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml","file:src/main/resources/dataSource.xml" })
public class SequenceServiceTest extends	AbstractTransactionalJUnit4SpringContextTests {
	
	@Autowired
	private  SequenceService sequenceService;
	
	@Test
	public void testService(){
		String serialNum = sequenceService.getSerialNumber(JijinBizType.APPLY.name());
		assertTrue(serialNum.contains(JijinBizType.APPLY.name()));
	}
	
}
