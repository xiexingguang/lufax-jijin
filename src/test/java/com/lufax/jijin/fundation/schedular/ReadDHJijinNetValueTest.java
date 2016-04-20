package com.lufax.jijin.fundation.schedular;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.lufax.jijin.fundation.dto.JijinSyncFileDTO;

@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class ReadDHJijinNetValueTest extends AbstractJUnit4SpringContextTests{
	
	@Autowired
	private ReadJijinDHNetValueJob job;
	
	@Test
	public void test(){
		JijinSyncFileDTO dto = new JijinSyncFileDTO();
		dto.setCurrentLine(1L);
		dto.setFileName("/home/chenqunhui/dh103_20151028_24.txt");
		job.process(dto);
	}
}
