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

import com.lufax.jijin.fundation.dto.JijinIncreaseDTO;
import com.lufax.jijin.fundation.repository.JijinIncreaseRepository;
import com.lufax.mq.client.util.MapUtils;

@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class JijinIncreaseRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests{
	
	@Autowired
	JijinIncreaseRepository repository;
	
	SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");

	@Test@Ignore
	public void testInsertBusJijinIncrease() {
		JijinIncreaseDTO dto = new JijinIncreaseDTO();
		dto.setDayIncrease(BigDecimal.valueOf(0.0002));
		dto.setFundCode("320013");
		dto.setIncreaseDate(format.format(new Date()));
		dto.setMonthIncrease(BigDecimal.valueOf(0.0011));
		dto.setSixMonthIncrease(BigDecimal.valueOf(0.0018));
		dto.setThisYearIncrease(BigDecimal.valueOf(0.0004));
		dto.setThreeMonthIncrease(BigDecimal.valueOf(0.0015));
		dto.setTotalIncrease(BigDecimal.valueOf(0.0032));
		dto.setYearIncrease(BigDecimal.valueOf(0.0028));
		repository.insertBusJijinIncrease(dto);
		assertTrue(dto.getId()>0);
	}

	@Test@Ignore
	public void testFindBusJijinIncrease() {
		JijinIncreaseDTO dto = repository.findBusJijinIncrease(MapUtils.buildKeyValueMap("fundCode","320013","increaseDate","20150506"));
		assertEquals(dto.getDayIncrease().toString(), "0.0002");
	}

	@Test@Ignore
	public void testFindBusJijinIncreases() {
		List<JijinIncreaseDTO> dtos = repository.findBusJijinIncreases(MapUtils.buildKeyValueMap("fundCode","320013","increaseDate","20150506"));
		assertTrue(dtos.size()>0);
	}
}
