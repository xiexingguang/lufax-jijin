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
public class ReadJijinRedeemAuditJobTest extends AbstractTransactionalJUnit4SpringContextTests {
	
	@Autowired
	private ReadJijinRedeemAuditFileJob job;
	
	@Autowired
	private JijinSyncFileRepository jijinSyncFielRepository;
	
	@Test@Ignore
	public void testExcute() {
		
		
		List<JijinSyncFileDTO> files = job.fetchList(200);
		
		for(JijinSyncFileDTO file: files){
			job.process(file);
		}

	}

}
