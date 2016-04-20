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
import com.lufax.jijin.fundation.constant.TradeRecordStatus;
import com.lufax.jijin.fundation.constant.TradeRecordType;
import com.lufax.jijin.fundation.dto.JijinTradeRecordDTO;
import com.lufax.jijin.fundation.gson.BaseResponseGson;
import com.lufax.jijin.fundation.gson.PurchaseOrderResultGson;
import com.lufax.jijin.fundation.remote.AccountAppCallerService;
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
import com.lufax.jijin.user.domain.UserInfoGson;
import com.lufax.jijin.user.service.UserService;
@Ignore
@TransactionConfiguration(defaultRollback = true)
@ContextConfiguration(locations = {	"file:src/main/webapp/WEB-INF/applicationContext.xml",	"file:src/main/resources/dataSource.xml" })
public class DhEventServiceTest extends	AbstractTransactionalJUnit4SpringContextTests  {

	@Autowired
	private DhEventService dhEventService;
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
    @Autowired
    private UserService userService;
    @Autowired
    private AccountAppCallerService accountAppCallerService;
    
	@Before
	public void setUp() {
		// set mock
		jijinGatewayRemoteService = mock(JijinGatewayRemoteService.class);
		dhEventService.setJijinGatewayRemoteService(jijinGatewayRemoteService);
		pafGateWayRemoteService= mock(LjbGateWayRemoteService.class);
		dhEventService.setPafGateWayRemoteService(pafGateWayRemoteService);
		mqService =  mock(MqService.class);
		dhEventService.setMqService(mqService);
		fundAppCallerService = mock(FundAppCallerService.class);
		dhEventService.setFundAppCallerService(fundAppCallerService);
		userService = mock(UserService.class);
		dhEventService.setUserService(userService);
		accountAppCallerService = mock(AccountAppCallerService.class);
		dhEventService.setAccountAppCallerService(accountAppCallerService);
		mockBuilder.buildJijinUserBalance(1l,"000379");
		mockBuilder.buildJijinInfo("000379","dh103","currency");
		mockBuilder.buildJijinUserAccount(1l, "dh103");
		
		Mockito.doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        }).when(mqService).sendPurchaseResultMsg(Mockito.anyLong(),Mockito.anyString(),Mockito.any(BigDecimal.class));  

		Mockito.doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        }).when(mqService).sendJijinRecordSuccessMsg(Mockito.anyLong());  

		Mockito.doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        }).when(mqService).sendDhTradeResultMsg(Mockito.anyLong(),Mockito.anyString());  
	
		Mockito.doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        }).when(mqService).sendJijinWithdrawSuccessMsg(Mockito.anyLong());  
	}
	
	@Test
	public void testSubmitSuccess(){
		
		JijinTradeRecordDTO record = mockBuilder.buildTradeRecord(TradeRecordStatus.INIT, TradeRecordType.PURCHASE, "000379", "dh103", "201500302",0l);
		
		JijinTradeRecordDTO record2 = mockBuilder.buildTradeRecord(TradeRecordStatus.INIT, TradeRecordType.APPLY, "000379", "dh103", "201500302",0l);
		
        GWResponseGson gwResponseGson = new GWResponseGson();
        PurchaseOrderResultGson purchaseOrderResultGson = new PurchaseOrderResultGson();
        purchaseOrderResultGson.setErrorCode("0000");
        purchaseOrderResultGson.setAppSheetSerialNo("testAppSheetNo");
        gwResponseGson.setRetCode("000");
        gwResponseGson.setContent(JsonHelper.toJson(purchaseOrderResultGson));
        when(jijinGatewayRemoteService.buy(Mockito.anyString(),Mockito.anyString())).thenReturn(gwResponseGson);
        when(jijinGatewayRemoteService.apply(Mockito.anyString(),Mockito.anyString())).thenReturn(gwResponseGson);

	    UserInfoGson userInfoGson = new UserInfoGson();
	    userInfoGson.setName("name");
	    userInfoGson.setIdNo("idNo");
	    when(userService.getUserInfo(Mockito.anyLong())).thenReturn(userInfoGson);
		
	    dhEventService.doPurchase(record.getId());
		
		record = jijinTradeRecordRepository.getRecordById(record.getId());
		
		assertTrue("SUBMIT_SUCCESS".equals(record.getStatus()));
		
		dhEventService.doPurchase(record2.getId());
		
		record2 = jijinTradeRecordRepository.getRecordById(record2.getId());
		
		assertTrue("SUBMIT_SUCCESS".equals(record2.getStatus()));

	} 

	
	@Test
	public void testSubmit0099(){
		JijinTradeRecordDTO record = mockBuilder.buildTradeRecord(TradeRecordStatus.INIT, TradeRecordType.APPLY, "000379", "dh103", "201500302",0l);
		
        GWResponseGson gwResponseGson = new GWResponseGson();
        PurchaseOrderResultGson purchaseOrderResultGson = new PurchaseOrderResultGson();
        purchaseOrderResultGson.setErrorCode("0099");
        purchaseOrderResultGson.setAppSheetSerialNo("testAppSheetNo");
        gwResponseGson.setRetCode("000");
        gwResponseGson.setContent(JsonHelper.toJson(purchaseOrderResultGson));
        when(jijinGatewayRemoteService.buy(Mockito.anyString(),Mockito.anyString())).thenReturn(gwResponseGson);
        when(jijinGatewayRemoteService.apply(Mockito.anyString(),Mockito.anyString())).thenReturn(gwResponseGson);

	    UserInfoGson userInfoGson = new UserInfoGson();
	    userInfoGson.setName("name");
	    userInfoGson.setIdNo("idNo");
	    when(userService.getUserInfo(Mockito.anyLong())).thenReturn(userInfoGson);
		
	    dhEventService.doPurchase(record.getId());
		
		record = jijinTradeRecordRepository.getRecordById(record.getId());
		
		assertTrue("INIT".equals(record.getStatus()));
	}
	
	@Test
	public void testSubmitFailwithUnfreezeSuccess(){
		JijinTradeRecordDTO record = mockBuilder.buildTradeRecord(TradeRecordStatus.INIT, TradeRecordType.APPLY, "000379", "dh103", "201500302",0l);
		
        GWResponseGson gwResponseGson = new GWResponseGson();
        PurchaseOrderResultGson purchaseOrderResultGson = new PurchaseOrderResultGson();
        purchaseOrderResultGson.setErrorCode("0009");
        purchaseOrderResultGson.setAppSheetSerialNo("testAppSheetNo");
        gwResponseGson.setRetCode("000");
        gwResponseGson.setContent(JsonHelper.toJson(purchaseOrderResultGson));
        when(jijinGatewayRemoteService.buy(Mockito.anyString(),Mockito.anyString())).thenReturn(gwResponseGson);
        when(jijinGatewayRemoteService.apply(Mockito.anyString(),Mockito.anyString())).thenReturn(gwResponseGson);

	    UserInfoGson userInfoGson = new UserInfoGson();
	    userInfoGson.setName("name");
	    userInfoGson.setIdNo("idNo");
	    when(userService.getUserInfo(Mockito.anyLong())).thenReturn(userInfoGson);
		
	    BaseResponseGson baseResponseGson = new BaseResponseGson();
	    baseResponseGson.setRetCode("000");		
	    when(accountAppCallerService.unfreezeFund(Mockito.anyLong(), Mockito.anyLong(),Mockito.anyLong(), Mockito.anyString())).thenReturn(baseResponseGson);
	    
	    dhEventService.doPurchase(record.getId());
		record = jijinTradeRecordRepository.getRecordById(record.getId());	
		assertTrue("FAIL".equals(record.getStatus()));
	}
	
	@Test
	public void testSubmitFailwithUnfreezeFail(){
		JijinTradeRecordDTO record = mockBuilder.buildTradeRecord(TradeRecordStatus.INIT, TradeRecordType.APPLY, "000379", "dh103", "201500302", 0l);
		
        GWResponseGson gwResponseGson = new GWResponseGson();
        PurchaseOrderResultGson purchaseOrderResultGson = new PurchaseOrderResultGson();
        purchaseOrderResultGson.setErrorCode("0009");
        purchaseOrderResultGson.setAppSheetSerialNo("testAppSheetNo");
        gwResponseGson.setRetCode("000");
        gwResponseGson.setContent(JsonHelper.toJson(purchaseOrderResultGson));
        when(jijinGatewayRemoteService.buy(Mockito.anyString(),Mockito.anyString())).thenReturn(gwResponseGson);
        when(jijinGatewayRemoteService.apply(Mockito.anyString(),Mockito.anyString())).thenReturn(gwResponseGson);

	    UserInfoGson userInfoGson = new UserInfoGson();
	    userInfoGson.setName("name");
	    userInfoGson.setIdNo("idNo");
	    when(userService.getUserInfo(Mockito.anyLong())).thenReturn(userInfoGson);
		
	    BaseResponseGson baseResponseGson = new BaseResponseGson();
	    baseResponseGson.setRetCode("001");		
	    when(accountAppCallerService.unfreezeFund(Mockito.anyLong(), Mockito.anyLong(),Mockito.anyLong(), Mockito.anyString())).thenReturn(baseResponseGson);
	    
	    dhEventService.doPurchase(record.getId());
		record = jijinTradeRecordRepository.getRecordById(record.getId());	
		assertTrue("FAIL".equals(record.getStatus()));
	}
	
	@Test
	public void testWithdrawByPAFSuccess(){
		
		JijinTradeRecordDTO record = mockBuilder.buildTradeRecord(TradeRecordStatus.SUBMIT_SUCCESS, TradeRecordType.PURCHASE, "000379", "dh103", "201500302", 0l);

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
		
		dhEventService.doWithdrawByPAF(record.getId());
		record = jijinTradeRecordRepository.getRecordById(record.getId());	
		assertTrue("WITHDRAW_SUCCESS".equals(record.getStatus()));
	}
	
	@Test
	public void testWithdrawByPAFFailLargerThan5(){
		
		JijinTradeRecordDTO record = mockBuilder.buildTradeRecord(TradeRecordStatus.SUBMIT_SUCCESS, TradeRecordType.PURCHASE, "000379", "dh103", "201500302", 10l);
		
		mockBuilder.updateTradeRecordRetryTimes(record.getId(), 10l);
		
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
		
		 BaseResponseGson baseResponseGson = new BaseResponseGson();
		 baseResponseGson.setRetCode("000");
		when(accountAppCallerService.unfreezeFund(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyLong(), Mockito.anyString())).thenReturn(baseResponseGson);;
		
		dhEventService.doWithdrawByPAF(record.getId());
		record = jijinTradeRecordRepository.getRecordById(record.getId());	
		assertTrue("FAIL".equals(record.getStatus()));
	}
	
	
	@Test
	public void testWithdrawByPAFFailSmallerThan5(){
		
		JijinTradeRecordDTO record = mockBuilder.buildTradeRecord(TradeRecordStatus.SUBMIT_SUCCESS, TradeRecordType.PURCHASE, "000379", "dh103", "201500302", 2l);

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
		
		dhEventService.doWithdrawByPAF(record.getId());
		record = jijinTradeRecordRepository.getRecordById(record.getId());	
		assertTrue("SUBMIT_SUCCESS".equals(record.getStatus()));
	}
	
	@Test
	public void testNotifySuccess(){
		
		JijinTradeRecordDTO record = mockBuilder.buildTradeRecord(TradeRecordStatus.WITHDRAW_SUCCESS, TradeRecordType.PURCHASE, "000379", "dh103", "201500302", 2l);
		
		
	    GWResponseGson gwResponseGson = new GWResponseGson();
	    GWBuyApplyNotifyResponseGson gwBuyApplyNotifyResponseGson = new GWBuyApplyNotifyResponseGson();
	    gwBuyApplyNotifyResponseGson.setErrorCode("0000");
	    gwResponseGson.setRetCode("000");
	    gwResponseGson.setContent(JsonHelper.toJson(gwBuyApplyNotifyResponseGson));
	    when(jijinGatewayRemoteService.buyNotify(Mockito.anyString(),Mockito.anyString() )).thenReturn(gwResponseGson);
		
	    dhEventService.buyApplyNotify(record.getId());
		
		record = jijinTradeRecordRepository.getRecordById(record.getId());
		assertTrue("SUCCESS".equals(record.getStatus()));
		
	}
	  
}
