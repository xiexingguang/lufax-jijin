package com.lufax.jijin.fundation.schedular;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.lufax.jijin.fundation.dto.JijinTradeRecordDTO;
import com.lufax.jijin.fundation.repository.JijinSyncFileRepository;

@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class RedeemCompensationJobTest extends AbstractTransactionalJUnit4SpringContextTests {
	
	@Autowired
	private RedeemCompensationJob job;
	
	@Autowired
	private JijinSyncFileRepository jijinSyncFielRepository;
	
	@Test@Ignore
	public void testExcute() {
		
		List<JijinTradeRecordDTO> dtos = job.fetchList(200);
		
		job.processList(dtos);
		
	}

}
