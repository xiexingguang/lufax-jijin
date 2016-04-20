package com.lufax.jijin.fundation.service;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.lufax.jijin.fundation.constant.JijinConstants;
import com.lufax.jijin.fundation.dto.JijinRedeemBalDTO;
import com.lufax.jijin.fundation.remote.AccountrAppCallerService;
import com.lufax.jijin.fundation.repository.JijinRedeemBalRepository;

@TransactionConfiguration(defaultRollback = true)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class JijinDahuaRedisServiceTest extends AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private JijinDahuaRedisService JijinDahuRedisService;
	@Autowired
	private JijinRedeemBalRepository jijinRedeemBalRepository;
	@Autowired
	private AccountrAppCallerService accountrAppCallerService;
	
	@Before
	public void setUp(){
		accountrAppCallerService=mock(AccountrAppCallerService.class);
		JijinDahuRedisService.setAccountrAppCallerService(accountrAppCallerService);
	}
	
	@Test
	@Ignore
	public void testRedis(){
		
		BigDecimal balance = BigDecimal.TEN;
		when( accountrAppCallerService.getCanBeUsedBalanceByUserId(Mockito.anyLong())).thenReturn(balance);
		JijinDahuRedisService.initRedis(JijinConstants.DAHUA_FUND_CODE);
		
		JijinRedeemBalDTO dto = 	jijinRedeemBalRepository.findBusJijinRedeemBalByFundCode(JijinConstants.DAHUA_FUND_CODE);
		assertTrue(null!=dto);

		JijinDahuRedisService.putRedisValue(balance);
		BigDecimal value = JijinDahuRedisService.getRedisValue();
		assertTrue(BigDecimal.TEN.equals(value));	
		
		int result = JijinDahuRedisService.genRedisSnapshot();
		assertTrue(result==1);	
	}
}
