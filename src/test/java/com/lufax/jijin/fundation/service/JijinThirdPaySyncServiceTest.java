package com.lufax.jijin.fundation.service;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyList;
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

import com.lufax.jijin.fundation.constant.TradeConfirmStatus;
import com.lufax.jijin.fundation.constant.TradeRecordStatus;
import com.lufax.jijin.fundation.constant.TradeRecordType;
import com.lufax.jijin.fundation.dto.JijinThirdPaySyncDTO;
import com.lufax.jijin.fundation.dto.JijinTradeRecordDTO;
import com.lufax.jijin.fundation.dto.JijinTradeSyncDTO;
import com.lufax.jijin.fundation.gson.BaseResponseGson;
import com.lufax.jijin.fundation.remote.AccountAppCallerService;
import com.lufax.jijin.fundation.remote.FundAppCallerService;
import com.lufax.jijin.fundation.remote.gson.request.UploadResultGson;
import com.lufax.jijin.fundation.remote.gson.response.FundResponseGson;
import com.lufax.jijin.fundation.service.builder.JunitMockModelBuilder;
import com.lufax.jijin.sysFacade.service.ExtInterfaceService;
import com.lufax.jijin.user.domain.UserInfoGson;
import com.lufax.jijin.user.service.UserService;


@TransactionConfiguration(defaultRollback = true)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class JijinThirdPaySyncServiceTest extends AbstractTransactionalJUnit4SpringContextTests{
	
	@Autowired
	private JijinThirdPaySyncService jijinThirdPaySyncService;
    @Autowired
    private AccountAppCallerService accountAppCallerService;
    @Autowired
    private FundAppCallerService fundAppCallerService;
    @Autowired
    private UserService userService;
	@Autowired
    private ExtInterfaceService extInterfaceService;
    @Autowired
    private JunitMockModelBuilder junitMockModelBuilder;
	
//	@Before
	public void setUp() {
//		accountAppCallerService = mock(AccountAppCallerService.class);
//		jijinThirdPaySyncService.setAccountAppCallerService(accountAppCallerService);
//		fundAppCallerService = mock(FundAppCallerService.class);
//		jijinThirdPaySyncService.setFundAppCallerService(fundAppCallerService);
//		userService = mock(UserService.class);
//		jijinThirdPaySyncService.setUserService(userService);
//		extInterfaceService = mock(ExtInterfaceService.class);
//		jijinThirdPaySyncService.setExtInterfaceService(extInterfaceService);
//		
//		
//		junitMockModelBuilder.buildJijinInfo("470009","yfd101","stock");
//		junitMockModelBuilder.buildJijinUserBalance(1l,"470009");
//		junitMockModelBuilder.buildJijinUserAccount(1l,"yfd101");
	}
	
	@Test
	public void testJijinRedeemSync() {
//		JijinTradeRecordDTO tradeRecord =junitMockModelBuilder.buildTradeRecord(TradeRecordStatus.WAITING_MONEY,TradeRecordType.REDEEM,"470009","yfd101","20990101", 0l);
//		JijinThirdPaySyncDTO paySync = junitMockModelBuilder.buildJijinThirdPayDTO("04");
//		JijinTradeSyncDTO tradeSync = junitMockModelBuilder.buildTradeSync("appSheetNo","124",TradeConfirmStatus.DISPATCH);
//		
//		FundResponseGson res  = new FundResponseGson();
//		res.setRetCode("000");
//		res.setResultStatus("SUCCESS");
//		when( fundAppCallerService.auditUploadResult(Mockito.any(UploadResultGson.class))).thenReturn(res);
//		
//		UserInfoGson userInfoGson = new UserInfoGson();
//		userInfoGson.setMobileNo("1300000000");
//		userInfoGson.setName("JUNIT USER");
//		when(userService.getUserInfo(1l)).thenReturn(userInfoGson);
//		when(extInterfaceService.sendSms(anyList())).thenReturn(null);
//		
//		jijinThirdPaySyncService.dispatchPAFRedeemSync(paySync);
//		
//		paySync = junitMockModelBuilder.getThirdPayRecord("appSheetNo");
//		
//		assertTrue("DISPATCHED".equals(paySync.getStatus().name()));

	}
	
	@Test
	public void testJijinPurchaseRefund() {
//		JijinTradeRecordDTO tradeRecord =junitMockModelBuilder.buildTradeRecord(TradeRecordStatus.NOTIFY_SUCCESS,TradeRecordType.PURCHASE,"470009","yfd101","20990101", 0l);
//		JijinThirdPaySyncDTO paySync = junitMockModelBuilder.buildJijinThirdPayDTO("05");
//		JijinTradeSyncDTO tradeSync = junitMockModelBuilder.buildTradeSync("appSheetNo","122",TradeConfirmStatus.DISPATCH);
//		
//		FundResponseGson res  = new FundResponseGson();
//		res.setRetCode("000");
//		res.setResultStatus("SUCCESS");
//		when( fundAppCallerService.auditUploadResult(Mockito.any(UploadResultGson.class))).thenReturn(res);
//		
//		UserInfoGson userInfoGson = new UserInfoGson();
//		userInfoGson.setMobileNo("1300000000");
//		userInfoGson.setName("JUNIT USER");
//		when(userService.getUserInfo(1l)).thenReturn(userInfoGson);
//		when(extInterfaceService.sendSms(anyList())).thenReturn(null);
//		
//		BaseResponseGson baseResponseGson = new BaseResponseGson();
//		baseResponseGson.setRetCode("000");
//		
//		
//		when(accountAppCallerService.plusMoney(Mockito.any(BigDecimal.class),Mockito.any(String.class), Mockito.any(Long.class), Mockito.any(String.class), Mockito.any(String.class), Mockito.any(String.class), Mockito.any(String.class))).thenReturn(baseResponseGson);
//		
//		jijinThirdPaySyncService.dispatchPurchaseRefund(paySync);
//		
//		paySync = junitMockModelBuilder.getThirdPayRecord("appSheetNo");
//		
//		assertTrue("DISPATCHED".equals(paySync.getStatus().name()));

	}
	
	@Test
	public void testForceRedeemPay() {
		
//		JijinTradeRecordDTO tradeRecord =junitMockModelBuilder.buildTradeRecord(TradeRecordStatus.WAITING_MONEY,TradeRecordType.FORCE_REDEEM,"470009","yfd101","20990101", 0l);
//		JijinThirdPaySyncDTO paySync = junitMockModelBuilder.buildJijinThirdPayDTO("04");
//		JijinTradeSyncDTO tradeSync = junitMockModelBuilder.buildTradeSync("appSheetNo","143",TradeConfirmStatus.DISPATCH);
//		FundResponseGson res  = new FundResponseGson();
//		res.setRetCode("000");
//		res.setResultStatus("SUCCESS");
//		when( fundAppCallerService.auditUploadResult(Mockito.any(UploadResultGson.class))).thenReturn(res);
//		UserInfoGson userInfoGson = new UserInfoGson();
//		userInfoGson.setMobileNo("1300000000");
//		userInfoGson.setName("JUNIT USER");
//		when(userService.getUserInfo(1l)).thenReturn(userInfoGson);
//		when(extInterfaceService.sendSms(anyList())).thenReturn(null);
//		
//		jijinThirdPaySyncService.handForceRedeemPay(tradeRecord.getId());
//		tradeRecord = junitMockModelBuilder.getJijinTradeRecord(tradeRecord.getId());
//		assertTrue(TradeRecordStatus.SUCCESS.name().equals(tradeRecord.getStatus()));

	}
	

}
