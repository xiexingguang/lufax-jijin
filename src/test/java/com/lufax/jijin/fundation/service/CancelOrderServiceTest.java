package com.lufax.jijin.fundation.service;


import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.lufax.jijin.base.utils.JijinAppProperties;
import com.lufax.jijin.base.utils.JsonHelper;
import com.lufax.jijin.fundation.constant.ResourceResponseCode;
import com.lufax.jijin.fundation.constant.TradeRecordStatus;
import com.lufax.jijin.fundation.constant.TradeRecordType;
import com.lufax.jijin.fundation.dto.JijinTradeRecordDTO;
import com.lufax.jijin.fundation.gson.BaseGson;
import com.lufax.jijin.fundation.remote.JijinGatewayRemoteService;
import com.lufax.jijin.fundation.remote.LjbGateWayRemoteService;
import com.lufax.jijin.fundation.remote.gson.paf.response.PAFCancelResponseGson;
import com.lufax.jijin.fundation.remote.gson.request.PaymentRequestGson;
import com.lufax.jijin.fundation.remote.gson.response.GWCancelOrderResponseGson;
import com.lufax.jijin.fundation.remote.gson.response.GWRedeemResponseGson;
import com.lufax.jijin.fundation.remote.gson.response.GWResponseGson;
import com.lufax.jijin.fundation.repository.JijinInfoRepository;
import com.lufax.jijin.fundation.repository.JijinTradeRecordRepository;
import com.lufax.jijin.fundation.repository.JijinUserBalanceRepository;
import com.lufax.jijin.fundation.service.builder.JunitMockModelBuilder;

//@TransactionConfiguration(defaultRollback = true)
@Ignore
@ContextConfiguration(locations = {	"file:src/main/webapp/WEB-INF/applicationContext.xml",	"file:src/main/resources/dataSource.xml" })
public class CancelOrderServiceTest extends	AbstractTransactionalJUnit4SpringContextTests {

	@Autowired 
	private CancelOrderService service; // target test class
	
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
    private EventService eventService;

	@Before
	public void setUp() {
		// set mock
		jijinGatewayRemoteService = mock(JijinGatewayRemoteService.class);
		service.setJijinGateway(jijinGatewayRemoteService);
		pafGateWayRemoteService= mock(LjbGateWayRemoteService.class);
		service.setJijinPafGateway(pafGateWayRemoteService);
		eventService = mock(EventService.class);
		service.setEventService(eventService);
		mockBuilder.buildJijinUserBalance(1l,"470009");
		mockBuilder.buildJijinInfo("470009","yfd101","stock"); 
	}

	@Test@Rollback(true)
	public void testCancelPurchase() {

		JijinTradeRecordDTO record =mockBuilder.buildTradeRecord(TradeRecordStatus.NOTIFY_SUCCESS,TradeRecordType.PURCHASE,"470009","yfd101","20990101", 0l);

		// jijin gw response
		GWResponseGson res = new GWResponseGson();
		res.setRetCode("000");
		GWCancelOrderResponseGson gs = new GWCancelOrderResponseGson();
		gs.setAppSheetSerialNo("appSheetSerialNotest");
		gs.setErrorCode("0000");
		res.setContent(JsonHelper.toJson(gs));
		when(jijinGatewayRemoteService.cancel(anyString(), anyString())).thenReturn(res);
		
		//paf gw response
		GWResponseGson pafGwres = new GWResponseGson();
		pafGwres.setRetCode("000");
		PAFCancelResponseGson pafCancelResponseGson = new    PAFCancelResponseGson();
		pafCancelResponseGson.setRespCode("0000");
		pafGwres.setContent(JsonHelper.toJson(pafCancelResponseGson));
		when(pafGateWayRemoteService.cancel(anyString())).thenReturn(pafGwres);
		
		//audit remote call
	    when(eventService.callFundAuditPayment(new PaymentRequestGson("channelId", "instructionNo", null),record)).thenReturn(true);

		String result = service.cancelPurchase(record.getId(),  record.getUserId());
		BaseGson gsonResult = JsonHelper.fromJson(result, BaseGson.class);
		assertTrue(ResourceResponseCode.SUCCESS.equals(gsonResult.getRetCode())
				|| "01".equals(gsonResult	.getRetCode()));
	}

	@Test@Rollback(true)
	public void testCancelRedeem() {

		JijinTradeRecordDTO record = mockBuilder.buildTradeRecord(TradeRecordStatus.SUBMIT_SUCCESS,TradeRecordType.REDEEM,"470009","yfd101","20990101", 0l);

		GWResponseGson res = new GWResponseGson();
		res.setRetCode("000");
		GWRedeemResponseGson gs = new GWRedeemResponseGson();
		gs.setAppSheetSerialNo("appSheetSerialNotest");
		gs.setErrorCode("0000");
		res.setContent(JsonHelper.toJson(gs));
		when(jijinGatewayRemoteService.cancel(anyString(), anyString())).thenReturn(res);
		
		String result = service.cancelRedeem(record.getId(), false, record.getUserId());
		BaseGson gsonResult = JsonHelper.fromJson(result, BaseGson.class);
		assertTrue(ResourceResponseCode.SUCCESS.equals(gsonResult.getRetCode())
				|| ResourceResponseCode.CANCEL_REDEEM_FAIL.equals(gsonResult	.getRetCode()));
	}

	@Test
	public void testCancelRedeemToPurchase(){
		GWResponseGson res = new GWResponseGson();
		res.setRetCode("000");
		GWRedeemResponseGson gs = new GWRedeemResponseGson();
		gs.setAppSheetSerialNo("appSheetSerialNotest");
		gs.setErrorCode("0000");
		res.setContent(JsonHelper.toJson(gs));
		when(jijinGatewayRemoteService.cancel(anyString(),anyString())).thenReturn(res);

		service.cancelRedeemToPurchase(61l,112l);
	}

}
