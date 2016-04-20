package com.lufax.jijin.fundation.service;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.lufax.jijin.fundation.repository.JijinDividendRepository;
import com.lufax.jijin.fundation.service.builder.JunitMockModelBuilder;
import com.lufax.jijin.fundation.service.domain.Dividend;

//@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class DividendServiceTest extends AbstractTransactionalJUnit4SpringContextTests{
	
	@Autowired
	private DividendService service;
	@Autowired
	private JunitMockModelBuilder mockBuilder;
    @Autowired
    private JijinDividendRepository dividendRepo;
    
//	@Before
//	public void setUp() {
//		// set mock
//		mockBuilder.builddividendDTO();
//		mockBuilder.buildJijinUserBalance(1l,"470009");
//		mockBuilder.buildJijinInfo("470009","yfd101","stock");
//
//	}
	
	@Test@Rollback(true)
	public void testIncreaseDividend() {
//		List<Dividend> dividends = service.getNewDividendsByType(Dividend.Type.SWITCH_DIVIDEND, 1);
//        if(!dividends.isEmpty()){
//        	assertTrue(service.increaseDividend(dividends.get(0)));
//        }
	}
	
	@Test@Rollback(true)
	public void testIncreaseDividendCash(){
//		List<Dividend> dividends = service.getNewDividendsByType(Dividend.Type.CASH, 1);
//        if(!dividends.isEmpty()){
//        	assertTrue(service.increaseDividend(dividends.get(0)));
//        }
	}
	
	@Test@Rollback(true)
	public void testIncreaseDividendCashFail(){
//		List<Dividend> dividends = service.getNewDividendsByType(Dividend.Type.CASH, 1);
//        assertTrue(dividends.isEmpty());
	}

}
