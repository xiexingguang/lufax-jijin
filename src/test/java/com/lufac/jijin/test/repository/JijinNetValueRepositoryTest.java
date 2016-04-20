package com.lufac.jijin.test.repository;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.google.common.collect.Lists;
import com.lufax.jijin.fundation.dto.JijinNetValueDTO;
import com.lufax.jijin.fundation.repository.JijinNetValueRepository;
import com.lufax.mq.client.util.MapUtils;

@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class JijinNetValueRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests{
	
	@Autowired
	JijinNetValueRepository repository;
	
	SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");

	@Test@Ignore
	public void testInsertJijinNetValue() {
		JijinNetValueDTO dto = new JijinNetValueDTO();
		dto.setBenefitPerTenthousand(BigDecimal.valueOf(0.0067));
		dto.setFundCode("003201");
		dto.setFundStatus("0");
		dto.setInterestratePerSevenday(BigDecimal.valueOf(0.015));
		dto.setNetValue(BigDecimal.valueOf(0.18));
		dto.setNetValueDate(format.format(new Date()));
		dto.setTotalNetValue(BigDecimal.valueOf(0.51));
		repository.insertJijinNetValue(dto);
		assertTrue(dto.getId()>0);
		
	}

	@Test@Ignore
	public void testFindBusJijinNetValue() {
		JijinNetValueDTO dto = repository.findBusJijinNetValue(MapUtils.buildKeyValueMap("fundCode","003201","netValueDate","20150506"));
		assertEquals(dto.getNetValue(), BigDecimal.valueOf(0.10));
	}

	@Test@Ignore
	public void testFindBusJijinNetValues() {
		List<JijinNetValueDTO> dtos = repository.findBusJijinNetValues(MapUtils.buildKeyValueMap("fundCode","003201","netValueDate","20150506"));
		assertTrue(dtos.size()>0);
	}
	
	@Test@Ignore
	public void testFindLatestBusJijinNetValues() {
		JijinNetValueDTO dto = repository.findLatestBusJijinNetValueByFundCode("003201");
		assertNotNull(dto);
	}
	
	@Test@Ignore
	public void testFindByDates(){
		String fundCode = "003201";
		List<String> dates = Lists.newArrayList("20150506","20150507","20150508","20150509");
		List<JijinNetValueDTO> dtos = repository.findBusJijinNetValuesByFundAndDate(fundCode, dates);
		assertEquals(4, dtos.size());
	}
	
	@Test@Ignore
	public void testFindByDates2(){
		String fundCode = "003201";
		List<String> dates = Lists.newArrayList("20150506");
		List<JijinNetValueDTO> dtos = repository.findBusJijinNetValuesByFundAndDate(fundCode, dates);
		assertEquals(1, dtos.size());
	}
	
	@Test@Ignore
	public void testFindBetweenDates(){
		String fundCode = "519068";
		String startDate = "20150506";
		String endDate = "20150508";
		List<JijinNetValueDTO> dtos = repository.findBusJijinNetValuesByFundAndDate(fundCode, startDate, endDate);
		assertEquals(3, dtos.size());
	}
}
