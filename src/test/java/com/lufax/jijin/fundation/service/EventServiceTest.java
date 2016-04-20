package com.lufax.jijin.fundation.service;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.lufax.jijin.base.utils.JijinAppProperties;
import com.lufax.jijin.base.utils.JsonHelper;
import com.lufax.jijin.fundation.constant.JijinConstants;
import com.lufax.jijin.fundation.constant.TradeRecordStatus;
import com.lufax.jijin.fundation.constant.TradeRecordType;
import com.lufax.jijin.fundation.dto.JijinTradeRecordDTO;
import com.lufax.jijin.fundation.gson.AccountChangeMsgGson;
import com.lufax.jijin.fundation.gson.JijinPaymentResultGson;
import com.lufax.jijin.fundation.gson.JijinStatusChangeMsgGson;
import com.lufax.jijin.fundation.gson.PurchaseOrderResultGson;
import com.lufax.jijin.fundation.remote.FundAppCallerService;
import com.lufax.jijin.fundation.remote.JijinGatewayRemoteService;
import com.lufax.jijin.fundation.remote.LjbGateWayRemoteService;
import com.lufax.jijin.fundation.remote.gson.paf.response.PAFPayResponseGson;
import com.lufax.jijin.fundation.remote.gson.request.PaymentRequestGson;
import com.lufax.jijin.fundation.remote.gson.response.FundResponseGson;
import com.lufax.jijin.fundation.remote.gson.response.GWBuyApplyNotifyResponseGson;
import com.lufax.jijin.fundation.remote.gson.response.GWResponseGson;
import com.lufax.jijin.fundation.repository.JijinInfoRepository;
import com.lufax.jijin.fundation.repository.JijinTradeRecordRepository;
import com.lufax.jijin.fundation.repository.JijinUserBalanceRepository;
import com.lufax.jijin.fundation.service.builder.JunitMockModelBuilder;
import com.lufax.jijin.service.MqService;

@Ignore
@TransactionConfiguration(defaultRollback = true)
@ContextConfiguration(locations = {	"file:src/main/webapp/WEB-INF/applicationContext.xml",	"file:src/main/resources/dataSource.xml" })
public class EventServiceTest extends	AbstractTransactionalJUnit4SpringContextTests {
	@Autowired 
	private EventService eventService; // target test class
	
	@Autowired
	private JunitMockModelBuilder mockBuilder;
	@Autowired
	private JijinAppProperties jijinAppProperties;
	@Autowired
	private JijinTradeRecordRepository jijinTradeRecordRepository;
	@Autowired
	private JijinGatewayRemoteService jijinGatewayRemoteService;
    @Autowired
    private LjbGateWayRemoteService pafGateWayRemoteService;
    @Autowired
    private RedeemService redeemService;
    @Autowired
    private JijinUserBalanceRepository jijinUserBalanceRepository;
    @Autowired
    private JijinInfoRepository jijinInfoRepository;
    @Autowired
    private MqService mqService;
    @Autowired
    private FundAppCallerService fundAppCallerService;


	@Before
	public void setUp() {
		// set mock
		jijinGatewayRemoteService = mock(JijinGatewayRemoteService.class);
		eventService.setJijinGatewayRemoteService(jijinGatewayRemoteService);
		pafGateWayRemoteService= mock(LjbGateWayRemoteService.class);
		eventService.setPafGateWayRemoteService(pafGateWayRemoteService);
		mqService =  mock(MqService.class);
		eventService.setMqService(mqService);
		fundAppCallerService = mock(FundAppCallerService.class);
		eventService.setFundAppCallerService(fundAppCallerService);
		
		mockBuilder.buildJijinUserBalance(1l,"470009");
		mockBuilder.buildJijinInfo("470009","yfd101","stock"); 
		mockBuilder.buildJijinUserAccount(1l, "yfd101");
	}
	
	@Test
	public void testOrderPurchase(){
		Mockito.doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        }).when(mqService).sendJijinRecordSuccessMsg(Mockito.anyLong());  
		
		//when(  mqService.sendJijinRecordSuccessMsg(anyLong())).thenReturn();
		
		eventService.orderPurchase("470009", 1l, BigDecimal.TEN,"PURCHASE", 1234l, "345678", "NORMAL", "1","businessMode","");
		
		JijinTradeRecordDTO record = jijinTradeRecordRepository.getRecordByTrxIdAndTypeChannel(1234l, "PURCHASE","PAF");
		assertTrue(null!=record);
		
	}
	
	@Test
	public void testDoPurchaseSuccess(){
		JijinTradeRecordDTO tradeRecord = mockBuilder.buildTradeRecord(TradeRecordStatus.INIT, TradeRecordType.PURCHASE,"470009","yfd101","20990101", 0l);
		
        GWResponseGson gwResponseGson = new GWResponseGson();
        PurchaseOrderResultGson purchaseOrderResultGson = new PurchaseOrderResultGson();
        purchaseOrderResultGson.setErrorCode("0000");
        purchaseOrderResultGson.setAppSheetSerialNo("testAppSheetNo");
        gwResponseGson.setRetCode("000");
        gwResponseGson.setContent(JsonHelper.toJson(purchaseOrderResultGson));
        when(jijinGatewayRemoteService.buy(Mockito.anyString(),Mockito.anyString())).thenReturn(gwResponseGson);
        
		Mockito.doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        }).when(mqService).sendPurchaseResultMsg(Mockito.anyLong(),Mockito.anyString(),Mockito.any(BigDecimal.class));  

		eventService.doPurchase(tradeRecord.getId());
		
		JijinTradeRecordDTO record = jijinTradeRecordRepository.getRecordById(tradeRecord.getId());
		
		assertTrue("SUBMIT_SUCCESS".equals(record.getStatus()));
		
	}
	
	@Test
	public void testDoPurchaseFail(){
		JijinTradeRecordDTO tradeRecord = mockBuilder.buildTradeRecord(TradeRecordStatus.INIT, TradeRecordType.PURCHASE,"470009","yfd101","20990101", 0l);
		
        GWResponseGson gwResponseGson = new GWResponseGson();
        PurchaseOrderResultGson purchaseOrderResultGson = new PurchaseOrderResultGson();
        purchaseOrderResultGson.setErrorCode("0001");
        purchaseOrderResultGson.setAppSheetSerialNo("testAppSheetNo");
        gwResponseGson.setRetCode("000");
        gwResponseGson.setContent(JsonHelper.toJson(purchaseOrderResultGson));
        when(jijinGatewayRemoteService.buy(Mockito.anyString(),Mockito.anyString())).thenReturn(gwResponseGson);
        
		Mockito.doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        }).when(mqService).sendPurchaseResultMsg(Mockito.anyLong(),Mockito.anyString(),Mockito.any(BigDecimal.class));  

		eventService.doPurchase(tradeRecord.getId());
		
		JijinTradeRecordDTO record = jijinTradeRecordRepository.getRecordById(tradeRecord.getId());
		
		assertTrue("FAIL".equals(record.getStatus()));
		
	}
	
	@Test
	public void testDoWithdrawByPAF(){
		
		JijinTradeRecordDTO tradeRecord = mockBuilder.buildTradeRecord(TradeRecordStatus.SUBMIT_SUCCESS, TradeRecordType.PURCHASE,"470009","yfd101","20990101", 0l);
		
        GWResponseGson gwResponseGson = new GWResponseGson();
        PAFPayResponseGson pafPayResponseGson = new PAFPayResponseGson();
        pafPayResponseGson.setRespCode("0000");
        pafPayResponseGson.setOrderCompleteTime("20161212121212");
        gwResponseGson.setContent(JsonHelper.toJson(pafPayResponseGson));
        gwResponseGson.setRetCode("000");
                		   
        when(pafGateWayRemoteService.pay(Mockito.anyString())).thenReturn(gwResponseGson);
		
		FundResponseGson fundResponseGson = new FundResponseGson();

		fundResponseGson.setRetCode("00");
		fundResponseGson.setResultStatus("SUCCESS");
		when(fundAppCallerService.auditPaymentRequest(Mockito.any(PaymentRequestGson.class)))	.thenReturn(fundResponseGson);
        
		Mockito.doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        }).when(mqService).sendJijinWithdrawSuccessMsg(Mockito.anyLong());  
		
		eventService.doWithdrawByPAF(tradeRecord.getId());
		
		JijinTradeRecordDTO record = jijinTradeRecordRepository.getRecordById(tradeRecord.getId());
		assertTrue("WITHDRAW_SUCCESS".equals(record.getStatus()));
	}
	
	@Test
	public void testDoWithdrawByPAFFailLargerThen5(){
		
		JijinTradeRecordDTO tradeRecord = mockBuilder.buildTradeRecord(TradeRecordStatus.SUBMIT_SUCCESS, TradeRecordType.PURCHASE,"470009","yfd101","20990101", 0l);
		mockBuilder.updateTradeRecordRetryTimes(tradeRecord.getId(), 10l);
		
        GWResponseGson gwResponseGson = new GWResponseGson();
        PAFPayResponseGson pafPayResponseGson = new PAFPayResponseGson();
        pafPayResponseGson.setRespCode("0001");
        pafPayResponseGson.setOrderCompleteTime("20161212121212");
        gwResponseGson.setContent(JsonHelper.toJson(pafPayResponseGson));
        gwResponseGson.setRetCode("000");
                		   
        when(pafGateWayRemoteService.pay(Mockito.anyString())).thenReturn(gwResponseGson);
		
		FundResponseGson fundResponseGson = new FundResponseGson();

		fundResponseGson.setRetCode("00");
		fundResponseGson.setResultStatus("SUCCESS");
		when(fundAppCallerService.auditPaymentRequest(Mockito.any(PaymentRequestGson.class)))	.thenReturn(fundResponseGson);
        
		Mockito.doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        }).when(mqService).sendJijinWithdrawSuccessMsg(Mockito.anyLong());  
		
		eventService.doWithdrawByPAF(tradeRecord.getId());
		
		JijinTradeRecordDTO record = jijinTradeRecordRepository.getRecordById(tradeRecord.getId());
		assertTrue("FAIL".equals(record.getStatus()));
	}
	
	@Test
	public void testDoWithdrawByPAFFailSmallerThen5(){
		
		JijinTradeRecordDTO tradeRecord = mockBuilder.buildTradeRecord(TradeRecordStatus.SUBMIT_SUCCESS, TradeRecordType.PURCHASE,"470009","yfd101","20990101", 0l);
		
        GWResponseGson gwResponseGson = new GWResponseGson();
        PAFPayResponseGson pafPayResponseGson = new PAFPayResponseGson();
        pafPayResponseGson.setRespCode("0001");
        pafPayResponseGson.setOrderCompleteTime("20161212121212");
        gwResponseGson.setContent(JsonHelper.toJson(pafPayResponseGson));
        gwResponseGson.setRetCode("000");
                		   
        when(pafGateWayRemoteService.pay(Mockito.anyString())).thenReturn(gwResponseGson);
		
		FundResponseGson fundResponseGson = new FundResponseGson();

		fundResponseGson.setRetCode("00");
		fundResponseGson.setResultStatus("SUCCESS");
		when(fundAppCallerService.auditPaymentRequest(Mockito.any(PaymentRequestGson.class)))	.thenReturn(fundResponseGson);
        
		Mockito.doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        }).when(mqService).sendJijinWithdrawSuccessMsg(Mockito.anyLong());  
		
		eventService.doWithdrawByPAF(tradeRecord.getId());
		
		JijinTradeRecordDTO record = jijinTradeRecordRepository.getRecordById(tradeRecord.getId());
		assertTrue("SUBMIT_SUCCESS".equals(record.getStatus()));
	}
	
	@Test
	public void testBuyApplyNotify(){
		
		JijinTradeRecordDTO tradeRecord = mockBuilder.buildTradeRecord(TradeRecordStatus.WITHDRAW_SUCCESS, TradeRecordType.PURCHASE,"470009","yfd101","20990101", 0l);
		
	    GWResponseGson gwResponseGson = new GWResponseGson();
	    GWBuyApplyNotifyResponseGson gwBuyApplyNotifyResponseGson = new GWBuyApplyNotifyResponseGson();
	    gwBuyApplyNotifyResponseGson.setErrorCode("0000");
	    gwResponseGson.setRetCode("000");
	    gwResponseGson.setContent(JsonHelper.toJson(gwBuyApplyNotifyResponseGson));
	    when(jijinGatewayRemoteService.buyNotify(Mockito.anyString(),Mockito.anyString() )).thenReturn(gwResponseGson);
		
		eventService.buyApplyNotify(tradeRecord.getId());
		
		JijinTradeRecordDTO record = jijinTradeRecordRepository.getRecordById(tradeRecord.getId());
		assertTrue("NOTIFY_SUCCESS".equals(record.getStatus()));
	}
	
	@Test
	public void testHandlePaymentResult(){
		JijinPaymentResultGson jijinPaymentResultGson = new JijinPaymentResultGson();
		jijinPaymentResultGson.setActualAmount(BigDecimal.TEN);
		jijinPaymentResultGson.setAmount(BigDecimal.TEN);
		jijinPaymentResultGson.setType(JijinConstants.RECHARGE_NAME);
		jijinPaymentResultGson.setChannelId(JijinConstants.RECHARGE_CHANNEL_ID);
		jijinPaymentResultGson.setInstructionId("instructionId");
		jijinPaymentResultGson.setStatus("success");
		jijinPaymentResultGson.setRetCode("00");
		
		Mockito.doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        }).when(mqService).sendDahuaAccountBalanceChange(Mockito.anyString(),Mockito.anyString());  

		mockBuilder.buildWithdrawRecord("instructionId","INIT");
		eventService.handlePaymentResult(jijinPaymentResultGson);
	}
	
	@Test
	public void testHandleAccountChangeMsg(){
		AccountChangeMsgGson msg = new AccountChangeMsgGson();
		msg.setFundCode(JijinConstants.DAHUA_FUND_CODE);
		eventService.handleAccountChangeMsg(msg);
	}
	
	@Test
	public void testUpdateJijinStatus(){
		
		JijinStatusChangeMsgGson msg = new JijinStatusChangeMsgGson();
		msg.setCode("1234");
		msg.setBeOperationType("0");
		msg.setBeNewStatus("RED_OPEN");
		msg.setBeOldStatus("CLOSE");
		try{
			eventService.updateJijinStatus(msg);
		}catch(Exception e){}
		msg.setBeOperationType("1");
		msg.setBeNewStatus("CLOSE");
		msg.setBeOldStatus("RED_OPEN");
		try{
			eventService.updateJijinStatus(msg);
		}catch(Exception e){}
	}
}
