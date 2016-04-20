package com.lufac.jijin.test.repository;

import java.math.BigDecimal;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.lufax.jijin.yeb.dto.YebJJZLIncomeSyncDTO;
import com.lufax.jijin.yeb.repository.IncomeSyncStatus;
import com.lufax.jijin.yeb.repository.YebJJZLIncomeSyncRepository;

@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class YebIncomeSyncRespositoryTest extends AbstractTransactionalJUnit4SpringContextTests{

	@Autowired
	protected YebJJZLIncomeSyncRepository repository;
	
	@Test@Ignore
	public void testInsert() {
		YebJJZLIncomeSyncDTO dto = new YebJJZLIncomeSyncDTO();
		dto.setAmount(new BigDecimal("100.00"));
		dto.setFundCode("testFundCode");
		dto.setIncomeDate("2010924");
		dto.setStatus(IncomeSyncStatus.NEW.getCode());
		dto.setTotalYield(new BigDecimal("123.33"));
		dto.setUnpayYield(new BigDecimal("0.00"));
		dto.setUserId(1234l);
		dto.setYesterdayYield(new BigDecimal("7.32"));
		
		
		repository.insertYebIncomeSync(dto);
	}

}

