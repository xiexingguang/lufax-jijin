package com.lufax.jijin.fundation.service;


import static org.junit.Assert.assertTrue;
import java.math.BigDecimal;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import com.lufax.jijin.fundation.dto.JijinUserBalanceAuditDTO;
import com.lufax.jijin.fundation.dto.JijinDividendDTO.Status;
import com.lufax.jijin.fundation.service.builder.JunitMockModelBuilder;

@TransactionConfiguration(defaultRollback = true)
@ContextConfiguration(locations = {	"file:src/main/webapp/WEB-INF/applicationContext.xml",	"file:src/main/resources/dataSource.xml" })
@Ignore
public class JijinIncomeDispatchServiceTest extends	AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private JijinIncomeDispatchService jijinIncomeDispatchService;
	@Autowired
	private JunitMockModelBuilder mockBuilder;
	
	@Before
	public void setUp() {
		// set mock
//		mockBuilder.buildJijinUserBalance(1l,"470009");
//		mockBuilder.buildJijinInfo("470009","yfd101","stock"); 
//		
//		mockBuilder.buildJijinUserBalance(1l,"300749");
//		mockBuilder.buildJijinInfo("300749","dh103","currency"); 
	}
	
	@Test
	public void testDispatch(){

		mockBuilder.buildJijinUserBalance(1l,"480009");
		mockBuilder.buildJijinInfo("480009","yfd101","stock");

		JijinUserBalanceAuditDTO yesterDay = mockBuilder.buildJijinUserBalanceAuditDto(1l, "480009", "yfd101", "20990202", Status.DISPATCHED, BigDecimal.TEN);
		JijinUserBalanceAuditDTO today = mockBuilder.buildJijinUserBalanceAuditDto(1l, "480009", "yfd101", "20990203", Status.NEW, new BigDecimal("11"));

		jijinIncomeDispatchService.dispatchIncome(today);

		today =  mockBuilder.getJijinUserBalanceAuditDTO(today.getId());

		assertTrue("DISPATCHED".equals(today.getStatus()));

		
	}
	
	@Ignore
	@Test
	public void testCurrencyDispatch(){
		

		mockBuilder.buildJijinUserBalance(1l,"000999");
		mockBuilder.buildJijinInfo("000999","dh103","currency");

		JijinUserBalanceAuditDTO yesterDay = mockBuilder.buildJijinUserBalanceAuditDto(1l, "000999", "dh103", "20990202", Status.DISPATCHED, BigDecimal.TEN);
		JijinUserBalanceAuditDTO today = mockBuilder.buildJijinUserBalanceAuditDto(1l, "000999", "dh103", "20990203", Status.NEW, new BigDecimal("11"));

		jijinIncomeDispatchService.dispatchIncome(today);

		today =  mockBuilder.getJijinUserBalanceAuditDTO(today.getId());

		assertTrue("DISPATCHED".equals(today.getStatus()));

	}
	
}
