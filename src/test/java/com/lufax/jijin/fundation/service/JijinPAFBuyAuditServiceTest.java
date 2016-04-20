package com.lufax.jijin.fundation.service;

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

import com.lufax.jijin.base.service.TradeDayService;
import com.lufax.jijin.base.utils.JijinAppProperties;
import com.lufax.jijin.base.utils.JijinInstIdPlatMerchantIdMapHolder;
import com.lufax.jijin.fundation.constant.TradeRecordStatus;
import com.lufax.jijin.fundation.constant.TradeRecordType;
import com.lufax.jijin.fundation.dto.JijinPAFBuyAuditDTO;
import com.lufax.jijin.fundation.dto.JijinTradeRecordDTO;
import com.lufax.jijin.fundation.remote.FundAppCallerService;
import com.lufax.jijin.fundation.remote.gson.request.PaymentRequestGson;
import com.lufax.jijin.fundation.remote.gson.request.UploadResultGson;
import com.lufax.jijin.fundation.remote.gson.response.FundResponseGson;
import com.lufax.jijin.fundation.repository.JijinInfoRepository;
import com.lufax.jijin.fundation.service.builder.JunitMockModelBuilder;

@Ignore
@TransactionConfiguration(defaultRollback = true)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class JijinPAFBuyAuditServiceTest extends AbstractTransactionalJUnit4SpringContextTests{
	
	@Autowired
	private JijinPAFBuyAuditService jijinPAFBuyAuditService;
    @Autowired
    private EventService eventService;
    @Autowired
    private JunitMockModelBuilder junitMockModelBuilder;
    @Autowired
    private FundAppCallerService fundAppCallerService;
	
	@Autowired
	private JijinAppProperties jijinAppProperties;
    @Autowired
    private TradeDayService tradeDayService;
    @Autowired
    private JijinInfoRepository jijinInfoRepository;
    @Autowired
    private JijinInstIdPlatMerchantIdMapHolder jijinInstIdPlatMerchantIdMapHolder;

	@Before
	public void setUp() {
		eventService = mock(EventService.class);
		jijinPAFBuyAuditService.setEventService(eventService);
		fundAppCallerService = mock(FundAppCallerService.class);
		jijinPAFBuyAuditService.setFundAppCallerService(fundAppCallerService);
		junitMockModelBuilder.buildJijinInfo("470009","yfd101","stock");
		junitMockModelBuilder.buildJijinUserBalance(1l, "470009");
		
	}
    
	@Test
	public void testServiceWithNotifySuccess() {
		
		JijinTradeRecordDTO record =junitMockModelBuilder.buildTradeRecord(TradeRecordStatus.NOTIFY_SUCCESS,TradeRecordType.PURCHASE,"470009","yfd101","20990101", 0l);
		
		JijinPAFBuyAuditDTO audit = new JijinPAFBuyAuditDTO();
		audit.setTxnAmount(BigDecimal.ONE);
		audit.setFundSeqId(record.getAppSheetNo());
		audit.setDistributorCode("211"); //yfd
		
		FundResponseGson res  = new FundResponseGson();
		res.setRetCode("000");
		res.setResultStatus("SUCCESS");
		when( fundAppCallerService.auditUploadResult(Mockito.any(UploadResultGson.class))).thenReturn(res);
		 
		jijinPAFBuyAuditService.dispatchPAFBuyAudit(audit);
		assertTrue(audit.getStatus().equals("DISPATCHED"));		
	}
	
	@Test
	public void testServiceWithSubmitSuccess() {
		
		JijinTradeRecordDTO record =junitMockModelBuilder.buildTradeRecord(TradeRecordStatus.SUBMIT_SUCCESS,TradeRecordType.PURCHASE,"470009","dh103","20990101", 0l);
		
		JijinPAFBuyAuditDTO audit = new JijinPAFBuyAuditDTO();
		audit.setTxnAmount(BigDecimal.ONE);
		audit.setFundSeqId(record.getAppSheetNo());
		audit.setDistributorCode("270A"); //dh103
		
		FundResponseGson res  = new FundResponseGson();
		res.setRetCode("000");
		res.setResultStatus("SUCCESS");
		when( fundAppCallerService.auditUploadResult(Mockito.any(UploadResultGson.class))).thenReturn(res);
		when(eventService.callFundAuditPayment(Mockito.any(PaymentRequestGson.class),record)).thenReturn(true);
		
		jijinPAFBuyAuditService.dispatchPAFBuyAudit(audit);
		assertTrue(audit.getStatus().equals("DISPATCHED"));		
	}
	


}
