package com.lufax.jijin.fundation.schedular;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.fundation.dto.JijinSyncFileDTO;
import com.lufax.jijin.fundation.repository.JijinSyncFileRepository;

@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class ReadePafSyncJobTest extends AbstractTransactionalJUnit4SpringContextTests {
	
	@Autowired
	private ReadPAFSyncFileJob job;
	
	@Autowired
	private JijinSyncFileRepository jijinSyncFielRepository;
	
	@Test@Ignore
	public void testExcute() {
		
		
	JijinSyncFileDTO dto = jijinSyncFielRepository.findBusJijinSyncFile(MapUtils.buildKeyValueMap("id",42)); 
	
	job.process(dto);
			
		
	}

}
