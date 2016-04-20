package com.lufax.jijin.fundation.service;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.lufax.jijin.base.utils.JsonHelper;
import com.lufax.jijin.fundation.constant.FundSaleCode;
import com.lufax.jijin.fundation.constant.SyncFileBizType;
import com.lufax.jijin.fundation.constant.TradeRecordStatus;
import com.lufax.jijin.fundation.constant.TradeRecordType;
import com.lufax.jijin.fundation.dto.JijinTradeRecordDTO;
import com.lufax.jijin.fundation.gson.RedeemResultGson;
import com.lufax.jijin.fundation.remote.JijinGatewayRemoteService;
import com.lufax.jijin.fundation.remote.gson.response.GWRedeemResponseGson;
import com.lufax.jijin.fundation.remote.gson.response.GWResponseCode;
import com.lufax.jijin.fundation.remote.gson.response.GWResponseGson;
import com.lufax.jijin.fundation.repository.JijinTradeRecordRepository;
import com.lufax.jijin.fundation.service.builder.JunitMockModelBuilder;
import com.lufax.jijin.fundation.service.domain.FileHolder;
import com.lufax.jijin.user.domain.CheckTradePasswordGson;
import com.lufax.jijin.user.service.UserService;
import com.lufax.mq.client.util.MapUtils;


@TransactionConfiguration(defaultRollback = true)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml","file:src/main/resources/dataSource.xml" })
public class RedeemServiceTest extends	AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private RedeemService service;
	@Autowired
	private JijinGatewayRemoteService jijinGatewayRemoteService;
	@Autowired
	private JunitMockModelBuilder mockBuilder;
	@Autowired
	private UserService userService;
	@Autowired
	private JijinTradeRecordRepository jijinTradeRecordRepository;
	
	@Before
	public void setUp() {
		
		mockBuilder.buildJijinUserBalance(1l, "470009");
		mockBuilder.buildJijinInfo("470009", "yfd101", "stock");
		mockBuilder.buildJijinUserAccount(1l, "yfd101");
		
		jijinGatewayRemoteService = mock(JijinGatewayRemoteService.class);
		service.setJijinGatewayService(jijinGatewayRemoteService);
		userService = mock(UserService.class);
		service.setUserService(userService);
		CheckTradePasswordGson value = new CheckTradePasswordGson();
		value.setResultId("00");
		when(userService.checkTradePwd(Mockito.anyLong(),Mockito.anyString(),Mockito.anyString())).thenReturn(value);
	}

	@Test
	public void testUnfreeze() {
		assertTrue(service.unFrezee(new BigDecimal("10.00"), 1l, "470009", false));
	}

	@Test
	public void testRedeemSuccess() {
		long userId = 1l;
		String fundCode = "470009";
		String tradePassword = "test";
		String amount = "1.00";

		GWResponseGson res = new GWResponseGson();
		res.setRetCode("000");
		GWRedeemResponseGson gs = new GWRedeemResponseGson();
		gs.setAppSheetSerialNo("appSheetSerialNotest");
		gs.setErrorCode("0000");
		gs.setTransactionDate("20160108");
		res.setContent(JsonHelper.toJson(gs));
		when(jijinGatewayRemoteService.redeem(anyString(), anyString())).thenReturn(res);

		

		String result =service.redeem(userId, fundCode, tradePassword, amount,null,12345l,"0","prodCode","virtual","remark");
		RedeemResultGson resultGson = JsonHelper.fromJson(result, RedeemResultGson.class);
		assertTrue("00".equals(resultGson.getRetCode()));
	}

	@Test
	@Ignore
	public void testRedeemFail() {

		long userId = 1l;
		String fundCode = "470009";
		String tradePassword = "test";
		String amount = "1.00";

		GWResponseGson res = new GWResponseGson();
		res.setRetCode("000");
		GWRedeemResponseGson gs = new GWRedeemResponseGson();
		gs.setAppSheetSerialNo("appSheetSerialNotest");
		gs.setErrorCode("0001");
		res.setContent(JsonHelper.toJson(gs));
		when(jijinGatewayRemoteService.redeem(anyString(), anyString()))
				.thenReturn(res);


		String result =service.redeem(userId, fundCode, tradePassword, amount,null,12345l,"0","prodCode","virtual","remark");
		RedeemResultGson resultGson = JsonHelper.fromJson(result, RedeemResultGson.class);
		assertTrue(!"00".equals(resultGson.getRetCode()));
	}

	@Test
	public void testRedeemRunTimeFail() {

		long userId =1l;
		String fundCode = "470009";
		String tradePassword = "test";
		String amount = "1.00";

		GWResponseGson res = new GWResponseGson();
		res.setRetCode(GWResponseCode.RUNTIME_ERROR);
		GWRedeemResponseGson gs = new GWRedeemResponseGson();
		gs.setAppSheetSerialNo("appSheetSerialNotest");
		gs.setErrorCode("1212");
		res.setContent(JsonHelper.toJson(gs));
		when(jijinGatewayRemoteService.redeem(anyString(), anyString())).thenReturn(res);


		String result =service.redeem(userId, fundCode, tradePassword, amount,null,12345l,"0","prodCode","virtual","remark");
		RedeemResultGson resultGson = JsonHelper.fromJson(result, RedeemResultGson.class);
		assertTrue("00".equals(resultGson.getRetCode()));
	}

	@Test
	public void testCompensationSuccess() {
		
		JijinTradeRecordDTO record = mockBuilder.buildTradeRecord(TradeRecordStatus.UNKNOWN, TradeRecordType.REDEEM,"470009","yfd101","20990101", 0l);
		List<JijinTradeRecordDTO> records = jijinTradeRecordRepository.getUnknownTradeRecords(TradeRecordType.REDEEM.name(), 100);

		GWResponseGson res = new GWResponseGson();
		res.setRetCode("000");
		GWRedeemResponseGson gs = new GWRedeemResponseGson();
		gs.setAppSheetSerialNo("appSheetSerialNotest");
		gs.setErrorCode("0000");
		gs.setTransactionDate("20160108");
		res.setContent(JsonHelper.toJson(gs));
		when(jijinGatewayRemoteService.redeem(anyString(), anyString())).thenReturn(res);

		service.redeemCompensation(records.get(0));
		record = mockBuilder.getJijinTradeRecord(record.getId());
		assertTrue("SUBMIT_SUCCESS".equals(record.getStatus()));
	}

	@Test
	public void testCompensationFail() {
		
		JijinTradeRecordDTO record = mockBuilder.buildTradeRecord(TradeRecordStatus.UNKNOWN, TradeRecordType.REDEEM,"470009","yfd101","20990101", 0l);
		List<JijinTradeRecordDTO> records = jijinTradeRecordRepository.getUnknownTradeRecords(TradeRecordType.REDEEM.name(), 100);

		GWResponseGson res = new GWResponseGson();
		res.setRetCode("000");
		GWRedeemResponseGson gs = new GWRedeemResponseGson();
		gs.setAppSheetSerialNo("appSheetSerialNotest");
		gs.setErrorCode("0001");
		res.setContent(JsonHelper.toJson(gs));
		when(jijinGatewayRemoteService.redeem(anyString(), anyString())).thenReturn(res);

		service.redeemCompensation(records.get(0));
		record = mockBuilder.getJijinTradeRecord(record.getId());
		assertTrue("SUBMIT_FAIL".equals(record.getStatus()));
	}

	@Test
	@Ignore
	public void testHandelRedeemApply() {

		JijinTradeRecordDTO jijinTradeRecordDTO = new JijinTradeRecordDTO();
		jijinTradeRecordDTO.setId(89l);

		service.handelT0RedeemApply(jijinTradeRecordDTO);
		assertTrue(1 == 1);
	}

	@Test
	@Ignore
	public void testUpd() {
		String a = "dh103";
		JijinTradeRecordDTO tradeRecord = jijinTradeRecordRepository
				.getRecordById(2997l);
		Map<Object, Object> map = MapUtils.buildKeyValueMap("id",
				tradeRecord.getId(), "status",
				TradeRecordStatus.SUBMIT_SUCCESS.name(), "appSheetNo",
				"2323232323", "trxDate", "20151106", "trxTime",
				"20151106173727", "errorCode", "0001", "errorMsg", "999999");
		if (FundSaleCode.DHC.getInstId().equals(a)) {
			map.put("isControversial", "-1");
		}
		jijinTradeRecordRepository.updateBusJijinTradeRecord(map);
	}

	@Test
	@Ignore
	public void testCreate() {
		String dateStr = "20151125";
		FileHolder fileHolder = new FileHolder();
		fileHolder.setFileName("lfx201" + "_" + dateStr + "_29.txt");
		fileHolder.setFileAbsolutePath("/home/chenzhiqiang288/Downloads/");
		service.generateRedeemFile(fileHolder, "20151125",
				SyncFileBizType.JIJIN_LFX_REDEEM_RESULT.name());
	}


	@Test
	@Ignore
	public void testT0Redeem(){
		JijinTradeRecordDTO dto = jijinTradeRecordRepository.getRecordById(271l);
		service.handelT0RedeemApply(dto);
	}
	
}
