package com.lufax.jijin.fundation.schedular;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.lufax.jijin.fundation.dto.JijinSyncFileDTO;

@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class HandleJijinTradeReconJobTest extends AbstractTransactionalJUnit4SpringContextTests {
	
	@Autowired
	private HandleJijinTradeReconJob job;
	
	@Test
	public void testExcute() {
		
		List<JijinSyncFileDTO> dtos = job.fetchList(200);
		
		job.processList(dtos);
		
	}

}
