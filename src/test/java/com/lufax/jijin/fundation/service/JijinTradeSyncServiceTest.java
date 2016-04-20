package com.lufax.jijin.fundation.service;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.lufax.jijin.fundation.constant.JijinBizType;
import com.lufax.jijin.fundation.constant.TradeConfirmStatus;
import com.lufax.jijin.fundation.constant.TradeRecordStatus;
import com.lufax.jijin.fundation.constant.TradeRecordType;
import com.lufax.jijin.fundation.dto.JijinTradeRecordDTO;
import com.lufax.jijin.fundation.dto.JijinTradeSyncDTO;
import com.lufax.jijin.fundation.gson.BaseResponseGson;
import com.lufax.jijin.fundation.remote.AccountAppCallerService;
import com.lufax.jijin.fundation.remote.gson.request.RedeemAuditRequestGson;
import com.lufax.jijin.fundation.service.builder.JunitMockModelBuilder;
import com.lufax.jijin.sysFacade.service.ExtInterfaceService;
import com.lufax.jijin.user.domain.UserInfoGson;
import com.lufax.jijin.user.service.UserService;

@Ignore
@TransactionConfiguration(defaultRollback = true)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class JijinTradeSyncServiceTest extends AbstractTransactionalJUnit4SpringContextTests{
	
	@Autowired
	private JijinTradeSyncService jijinTradeSyncService;
    @Autowired
    private AccountAppCallerService accountAppCallerService;
	@Autowired
	private JunitMockModelBuilder junitMockModelBuilder;
    @Autowired
    private UserService userService;
	@Autowired
    private ExtInterfaceService extInterfaceService;
	
	@Before
	public void setUp() {
		accountAppCallerService = mock(AccountAppCallerService.class);
		jijinTradeSyncService.setAccountAppCallerService(accountAppCallerService);
		userService = mock(UserService.class);
		jijinTradeSyncService.setUserService(userService);
		extInterfaceService = mock(ExtInterfaceService.class);
		jijinTradeSyncService.setExtInterfaceService(extInterfaceService);
		UserInfoGson userInfoGson = new UserInfoGson();
		userInfoGson.setMobileNo("1300000000");
		userInfoGson.setName("JUNIT USER");
		when(userService.getUserInfo(1l)).thenReturn(userInfoGson);
		when(extInterfaceService.sendSms(anyList())).thenReturn(null);
		
		
		junitMockModelBuilder.buildJijinInfo("470009","yfd101","stock");
		junitMockModelBuilder.buildJijinUserBalance(1l,"470009");
		junitMockModelBuilder.buildJijinUserAccount(1l,"yfd101");
	}
	
	@Test
	public void testDispatchRedeemTradeSync() {
		
		JijinTradeRecordDTO tradeRecord =junitMockModelBuilder.buildTradeRecord(TradeRecordStatus.SUBMIT_SUCCESS,TradeRecordType.REDEEM,"470009","yfd101","20990101", 0l);
		JijinTradeSyncDTO tradeSync = junitMockModelBuilder.buildTradeSync("appSheetNo",JijinBizType.REDEEM_CONFIRM.getCode(),TradeConfirmStatus.NEW);

		BaseResponseGson response = new BaseResponseGson();
		response.setRetCode("000");
	    when(accountAppCallerService.redemmAudit(Mockito.any(RedeemAuditRequestGson.class))).thenReturn(response);
		
		jijinTradeSyncService.dispatchRedeemTradeSync(tradeSync);
		tradeSync = junitMockModelBuilder.getTradeSyncRecord(tradeRecord.getAppSheetNo(), tradeRecord.getFundCode());
		assertTrue("DISPATCHED".equals(tradeSync.getStatus()));
	}
	
	
	@Test
	public void testDispatchPurchaseTradeSyncNotifySuccess() {
		
		JijinTradeRecordDTO tradeRecord =junitMockModelBuilder.buildTradeRecord(TradeRecordStatus.NOTIFY_SUCCESS,TradeRecordType.PURCHASE,"470009","yfd101","20990101", 0l);
		JijinTradeSyncDTO tradeSync = junitMockModelBuilder.buildTradeSync("appSheetNo",JijinBizType.PURCHASE_CONFIRM.getCode(),TradeConfirmStatus.NEW);

		jijinTradeSyncService.dispatchPurchaseTradeSync(tradeSync);
		tradeSync = junitMockModelBuilder.getTradeSyncRecord(tradeRecord.getAppSheetNo(), tradeRecord.getFundCode());
		assertTrue("DISPATCHED".equals(tradeSync.getStatus()));
	}
	
	@Test
	public void testDispatchPurchaseTradeSyncSubmitSuccess() {
		
		JijinTradeRecordDTO tradeRecord =junitMockModelBuilder.buildTradeRecord(TradeRecordStatus.SUBMIT_SUCCESS,TradeRecordType.PURCHASE,"470009","yfd101","20990101", 0l);
		JijinTradeSyncDTO tradeSync = junitMockModelBuilder.buildTradeSync("appSheetNo",JijinBizType.PURCHASE_CONFIRM.getCode(),TradeConfirmStatus.NEW);

		jijinTradeSyncService.dispatchPurchaseTradeSync(tradeSync);
		tradeSync = junitMockModelBuilder.getTradeSyncRecord(tradeRecord.getAppSheetNo(), tradeRecord.getFundCode());
		assertTrue("ERROR".equals(tradeSync.getStatus()));
	}
	
	@Test
	public void testDispatchForceIncrease() {
		
		//JijinTradeRecordDTO tradeRecord =junitMockModelBuilder.buildTradeRecord(TradeRecordStatus.SUBMIT_SUCCESS,TradeRecordType.PURCHASE);
		JijinTradeSyncDTO tradeSync = junitMockModelBuilder.buildTradeSync("appSheetNo",JijinBizType.FORCE_INCREASE.getCode(),TradeConfirmStatus.NEW);

		jijinTradeSyncService.dispatchForceIncrease(tradeSync);
		tradeSync = junitMockModelBuilder.getTradeSyncRecord(tradeSync.getAppSheetNo(), tradeSync.getFundCode());
		assertTrue("DISPATCHED".equals(tradeSync.getStatus()));
	}
	
	@Test
	public void testDispatchForceReduced() {
		
		//JijinTradeRecordDTO tradeRecord =junitMockModelBuilder.buildTradeRecord(TradeRecordStatus.SUBMIT_SUCCESS,TradeRecordType.PURCHASE);
		JijinTradeSyncDTO tradeSync = junitMockModelBuilder.buildTradeSync("appSheetNo",JijinBizType.FORCE_REDUCED.getCode(),TradeConfirmStatus.NEW);

		jijinTradeSyncService.dispatchForceReduced(tradeSync);
		tradeSync = junitMockModelBuilder.getTradeSyncRecord(tradeSync.getAppSheetNo(), tradeSync.getFundCode());
		assertTrue("DISPATCHED".equals(tradeSync.getStatus()));
	}
	
	@Test
	public void testDispatchForceRedeem() {
		
		//JijinTradeRecordDTO tradeRecord =junitMockModelBuilder.buildTradeRecord(TradeRecordStatus.SUBMIT_SUCCESS,TradeRecordType.PURCHASE);
		JijinTradeSyncDTO tradeSync = junitMockModelBuilder.buildTradeSync("appSheetNo",JijinBizType.FORCE_REDEEM.getCode(),TradeConfirmStatus.NEW);

		BaseResponseGson response = new BaseResponseGson();
		response.setRetCode("000");
	    when(accountAppCallerService.redemmAudit(Mockito.any(RedeemAuditRequestGson.class))).thenReturn(response);
		
		jijinTradeSyncService.dispatchForceRedeem(tradeSync);
		tradeSync = junitMockModelBuilder.getTradeSyncRecord(tradeSync.getAppSheetNo(), tradeSync.getFundCode());
		assertTrue("DISPATCHED".equals(tradeSync.getStatus()));
	}


}
