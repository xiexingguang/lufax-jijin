package com.lufax.jijin.fundation.schedular;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.lufax.jijin.fundation.constant.SyncFileBizType;
import com.lufax.utility.time.DateUtils;

@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class SendBillFileToLfexJobTest extends AbstractJUnit4SpringContextTests {

	@Autowired
	private SendBillFileToLfexJob job;
	
	@Test
	public void test(){
		job.execute(SyncFileBizType.JIJIN_LFX_REGISTER_RESULT.name(),DateUtils.parseDate("2015-08-07"));
		//job.execute(SyncFileBizType.JIJIN_LFX_DIVIDEND_RESULT.name(),DateUtils.parseDate("2015-06-15"));
	}
}
