package com.lufax.jijin.fundation.schedular;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class DividendTypeModifyJobTest extends AbstractTransactionalJUnit4SpringContextTests {
	
	@Autowired
	private DividendTypeModifyJob job;
	
	@Test@Ignore
	public void testExcute() {
		job.execute();
	}

}
