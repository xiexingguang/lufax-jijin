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

import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.fundation.constant.DHBizType;
import com.lufax.jijin.fundation.constant.TradeConfirmStatus;
import com.lufax.jijin.fundation.constant.TradeRecordStatus;
import com.lufax.jijin.fundation.constant.TradeRecordType;
import com.lufax.jijin.fundation.dto.JijinTradeConfirmDTO;
import com.lufax.jijin.fundation.dto.JijinTradeRecordDTO;
import com.lufax.jijin.fundation.service.builder.JunitMockModelBuilder;


@TransactionConfiguration(defaultRollback = true)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
@Ignore
public class JijinTradeConfirmServiceTest extends AbstractTransactionalJUnit4SpringContextTests{

	@Autowired
	JijinTradeConfirmService jijinTradeConfirmService;

    @Autowired
    private JunitMockModelBuilder junitMockModelBuilder;
    
    
	@Before
	public void setUp() {
		
		junitMockModelBuilder.buildJijinInfo("300749","dh103","stock");
		junitMockModelBuilder.buildJijinUserBalance(1l,"300749");
		junitMockModelBuilder.buildJijinUserAccount(1l,"dh103");
	}
	
	@Test
	public void testPurchaseConfirm(){
		JijinTradeConfirmDTO dto = junitMockModelBuilder.buildJijinTradeConfirmDTO("20980101",
				"20980102",DHBizType.PURCHASE.getCode(),"300749",BigDecimal.TEN, BigDecimal.TEN, "0000", "NEW");
		
		JijinTradeRecordDTO record = junitMockModelBuilder.buildTradeRecord(TradeRecordStatus.SUCCESS, 
				TradeRecordType.PURCHASE, "300749", "dh103", "20980101", 0l);
		
		jijinTradeConfirmService.dispatchConfirm(dto);
		
		record = junitMockModelBuilder.getJijinTradeRecord(record.getId());
		
		assertTrue("0".equals(record.getIsControversial()));
		
	}
	
	@Test
	public void testRedeemConfirm(){
		JijinTradeConfirmDTO dto = junitMockModelBuilder.buildJijinTradeConfirmDTO("20980101",
				"20980102",DHBizType.REDEEM.getCode(),"300749",BigDecimal.TEN, BigDecimal.TEN, "0000", "NEW");
		
		JijinTradeRecordDTO record = junitMockModelBuilder.buildTradeRecord(TradeRecordStatus.SUCCESS, 
				TradeRecordType.REDEEM, "300749", "dh103", "20980101", 0l);
		
		jijinTradeConfirmService.dispatchConfirm(dto);
		
		record = junitMockModelBuilder.getJijinTradeRecord(record.getId());
		
		assertTrue("0".equals(record.getIsControversial()));
		
	}

	
	@Test
	public void testLackTradeRecord(){
		
		JijinTradeConfirmDTO dto = junitMockModelBuilder.buildJijinTradeConfirmDTO("20980101",
				"20980102",DHBizType.REDEEM.getCode(),"300749",BigDecimal.TEN, BigDecimal.TEN, "0000", "NEW");
		
		jijinTradeConfirmService.dispatchConfirm(dto);

	}
	
	@Test
	public void testTradeConfirmFaillRes(){
		
		JijinTradeConfirmDTO dto = junitMockModelBuilder.buildJijinTradeConfirmDTO("20980101",
				"20980102",DHBizType.REDEEM.getCode(),"300749",BigDecimal.TEN, BigDecimal.TEN, "0001", "NEW");
		
		JijinTradeRecordDTO record = junitMockModelBuilder.buildTradeRecord(TradeRecordStatus.SUCCESS, 
				TradeRecordType.REDEEM, "300749", "dh103", "20980101", 0l);
		
		jijinTradeConfirmService.dispatchConfirm(dto);

	}
	
	@Test
	public void testAmountNotEqual(){
		JijinTradeConfirmDTO dto = junitMockModelBuilder.buildJijinTradeConfirmDTO("20980101",
				"20980102",DHBizType.REDEEM.getCode(),"300749", new BigDecimal("9"),BigDecimal.TEN, "0000", "NEW");
		
		JijinTradeRecordDTO record = junitMockModelBuilder.buildTradeRecord(TradeRecordStatus.SUCCESS, 
				TradeRecordType.REDEEM, "300749", "dh103", "20980101", 0l);
		
		jijinTradeConfirmService.dispatchConfirm(dto);
		
		record = junitMockModelBuilder.getJijinTradeRecord(record.getId());
		
		assertTrue("1".equals(record.getIsControversial()));
		
	}
	
	@Test
	public void testTradeRecordNotSuccess(){
		JijinTradeConfirmDTO dto = junitMockModelBuilder.buildJijinTradeConfirmDTO("20980101",
				"20980102",DHBizType.REDEEM.getCode(),"300749",BigDecimal.TEN, BigDecimal.TEN, "0000", "NEW");
		
		JijinTradeRecordDTO record = junitMockModelBuilder.buildTradeRecord(TradeRecordStatus.FAIL, 
				TradeRecordType.REDEEM, "300749", "dh103", "20980101", 0l);
		
		jijinTradeConfirmService.dispatchConfirm(dto);
		
		record = junitMockModelBuilder.getJijinTradeRecord(record.getId());
		
		assertTrue("1".equals(record.getIsControversial()));
		
	}
	
	@Test
	public void testAmoutnNotEq(){
		JijinTradeConfirmDTO dto = junitMockModelBuilder.buildJijinTradeConfirmDTO("20980101",
				"20980102",DHBizType.REDEEM.getCode(),"300749",new BigDecimal("9"), new BigDecimal("9"), "0000", "NEW");
		
		JijinTradeRecordDTO record = junitMockModelBuilder.buildTradeRecord(TradeRecordStatus.SUCCESS, 
				TradeRecordType.REDEEM, "300749", "dh103", "20980101", 0l);
		
		jijinTradeConfirmService.dispatchConfirm(dto);
		
		record = junitMockModelBuilder.getJijinTradeRecord(record.getId());
		
		assertTrue("1".equals(record.getIsControversial()));
		
	}
	
	@Test
	public void test(){
		
		JijinTradeConfirmDTO dto = junitMockModelBuilder.buildJijinTradeConfirmDTO("20980101",
				"20980102",DHBizType.PURCHASE.getCode(),"300749",new BigDecimal("9"), new BigDecimal("9"), "0000", "NEW");
		
		JijinTradeRecordDTO record = junitMockModelBuilder.buildTradeRecord(TradeRecordStatus.SUCCESS, 
				TradeRecordType.PURCHASE, "300749", "dh103", "20980101", 0l);
		
		jijinTradeConfirmService.dispatchConfirm(dto);

	}

}
