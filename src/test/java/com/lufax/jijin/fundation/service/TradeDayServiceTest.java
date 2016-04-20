package com.lufax.jijin.fundation.service;


import static org.junit.Assert.assertTrue;
import java.util.Date;
import java.util.Map;

import com.lufax.jijin.fundation.constant.TradeRecordType;
import com.lufax.jijin.service.MqService;
import com.lufax.mq.client.util.MapUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.lufax.jijin.base.service.TradeDayService;

@TransactionConfiguration(defaultRollback = true)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class TradeDayServiceTest extends AbstractTransactionalJUnit4SpringContextTests{
	
	@Autowired
	private TradeDayService service;
	@Autowired
	private MqService mqService;
	
	
	@Test
	public void testGetLastTradeDay() {
		
		String estimate = service.getLastTradeDay(new Date());
		assertTrue(null!=estimate);
	}

	@Test
	public void testGetNextTradeDay(){
		Date date = service.getNextTradeDayAsDate(new Date());
	}

	@Test
	public void testGetNextTradeDayAsString(){
		String nextDay = service.getNextTradeDayAsString(new Date());
		
	}
	
	@Test
	public void testGetRedeemEstimateDate(){
		service.getRedeemEstimateDate(new Date(), 3);
	}
	
	@Test
	public void testGetTargetTradeDay(){
		service.getTargetTradeDay(new Date());
	}
	
	
}
