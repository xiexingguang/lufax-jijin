package com.lufax.jijin.daixiao.schedular.Jobs;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.lufax.jijin.daixiao.dto.JijinExBuyLimitDTO;

@Ignore
@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class HandleJijinExBuyLimitJobTest extends AbstractJUnit4SpringContextTests{

	@Autowired
	HandleJijinExBuyLimitJob handleJijinExBuyLimitJob;
	
	@Test@Ignore
	public void test(){
		List<JijinExBuyLimitDTO> list = handleJijinExBuyLimitJob.fetchList(10);
		handleJijinExBuyLimitJob.process(list.get(0));
	}
	
	
}
