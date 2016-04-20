package com.lufax.jijin.fundation.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.google.common.collect.Maps;
import com.lufax.jijin.fundation.dto.JijinNetValueDTO;
import com.lufax.jijin.fundation.service.domain.NetValueDay;

//@Ignore
@TransactionConfiguration(defaultRollback = true)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class NetValueServiceTest extends AbstractTransactionalJUnit4SpringContextTests{
	
	@Autowired
	private IncreaseService service;

	@Test
	public void testCalIncreaseDayZero() {
		TreeMap<String, BigDecimal> netValueMap = Maps.newTreeMap();
		netValueMap.put("20150521", new BigDecimal(1.25));
		netValueMap.put("20150522", new BigDecimal(1.25));
		Map<NetValueDay, String> targetDates = Maps.newHashMap();
		targetDates.put(NetValueDay.TODAY, "20150522");
		targetDates.put(NetValueDay.LAST_TRADE_DAY, "20150521");
		
		BigDecimal result = service.calDayIncrease(
				targetDates.get(NetValueDay.LAST_TRADE_DAY), 
				targetDates.get(NetValueDay.TODAY),
				netValueMap);
		assertEquals("0.0000", result.toString());
	}
	
	@Test
	public void testCalIncreaseDay() {
		TreeMap<String, BigDecimal> netValueMap = Maps.newTreeMap();
		netValueMap.put("20150521", new BigDecimal(1.14));
		netValueMap.put("20150522", new BigDecimal(1.146));
		TreeMap<String, BigDecimal> dividends = Maps.newTreeMap();
		dividends.put("20150521", new BigDecimal(0.03));
		
		Map<NetValueDay, String> targetDates = Maps.newHashMap();
		targetDates.put(NetValueDay.TODAY, "20150522");
		targetDates.put(NetValueDay.LAST_TRADE_DAY, "20150521");
		
		BigDecimal result = service.calDayIncrease(				
				targetDates.get(NetValueDay.LAST_TRADE_DAY), 
				targetDates.get(NetValueDay.TODAY),  
				netValueMap);
		assertEquals("0.0053", result.toString());
	}
	
	@Test
	public void testCalIncreaseDays(){
		TreeMap<String, BigDecimal> netValueMap = Maps.newTreeMap();
		netValueMap.put("20150101", new BigDecimal(1.5));
		netValueMap.put("20150701", new BigDecimal(1.5));
		netValueMap.put("20150303", new BigDecimal(2.0));
		netValueMap.put("20150530", new BigDecimal(2.5));
		TreeMap<String, BigDecimal> dividends = Maps.newTreeMap();
		dividends.put("20150303", new BigDecimal(0.5));
		dividends.put("20150530", new BigDecimal(0.3));
		
		BigDecimal result = service.calIncrease("320010", "20150101", "20150701", "20150630", netValueMap, dividends);
		assertEquals("0.7647", result.toString());
	}
	
	@Test
	public void testCalIn(){
		JijinNetValueDTO netValue = new JijinNetValueDTO();
		netValue.setBenefitPerTenthousand(new BigDecimal(0));
		netValue.setFundCode("519068");
		netValue.setFundStatus("0");
		netValue.setInterestratePerSevenday(new BigDecimal(0));
		netValue.setNetValue(new BigDecimal(2.7788));
		netValue.setNetValueDate("20150531");
		netValue.setTotalNetValue(new BigDecimal(3.1968));
		try {
			long t = System.currentTimeMillis();
			service.calculateFundIncrease(netValue);
			System.out.println("Spend " + (System.currentTimeMillis()-t) + "ms to cal.");
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void testCalIn2(){
		TreeMap<String, BigDecimal> netValueMap = Maps.newTreeMap();
		netValueMap.put("20150625", new BigDecimal(1.118000));
		netValueMap.put("20150325", new BigDecimal(1.634100));
		netValueMap.put("20150529", new BigDecimal(1.034300));
//		netValueMap.put("20150530", new BigDecimal(2.5));
		TreeMap<String, BigDecimal> dividends = Maps.newTreeMap();
		dividends.put("20150601", new BigDecimal(1.0));
//		dividends.put("20150530", new BigDecimal(0.3));
		
		BigDecimal result = service.calIncrease("320010", "20150325", "20150625", "20150529", netValueMap, dividends);
		assertEquals("19.6308", result.toString());
	}
}
