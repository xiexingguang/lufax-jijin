package com.lufax.jijin.fundation.schedular;

import static org.junit.Assert.*;

import org.apache.commons.lang.StringUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class BatchJobTemplateTest extends AbstractTransactionalJUnit4SpringContextTests{
	
	@Autowired
	private BatchJobTemplate worker;

	@Test@Ignore
	public void testExecuteString() {
		int result = worker.execute("schedular/netvalue.avsc");
		assertEquals(1, result);
	}
	
	@Test@Ignore
	public void testDividend(){
		int result = worker.execute("schedular/dividend.avsc");
		assertEquals(1, result);
	}
	
	@Test@Ignore
	public void testParse(){
		String fileName = "/nfsc/sftp_user/pafsftp/yfd101/upload/20150616/yfd101_20150616_05.txt";
		String head = "/nfsc/sftp_user/pafsftp/";
		String info = StringUtils.removeStart(fileName, head);
		String instId = info.split("/")[0];
		assertEquals("yfd101", instId);
	}


}
