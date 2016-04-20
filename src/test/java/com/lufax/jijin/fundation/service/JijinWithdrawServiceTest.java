package com.lufax.jijin.fundation.service;

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
import com.lufax.jijin.base.utils.JsonHelper;
import com.lufax.jijin.fundation.constant.DahuaAccountTypeEnum;
import com.lufax.jijin.fundation.remote.FundAppCallerService;
import com.lufax.jijin.fundation.remote.JijinGatewayRemoteService;
import com.lufax.jijin.fundation.remote.gson.response.GWQueryDahuaBalanceResponseGson;
import com.lufax.jijin.fundation.remote.gson.response.GWResponseGson;
import com.lufax.jijin.fundation.service.builder.JunitMockModelBuilder;
import com.lufax.jijin.service.MqService;
import com.lufax.jijin.sysFacade.gson.result.BaseResultDTO;
import com.lufax.jijin.user.domain.BankAccountDetailGson;
import com.lufax.jijin.user.domain.UserInfoGson;
import com.lufax.jijin.user.service.UserService;

/**
 * 
 * @author XUNENG311
 */
@TransactionConfiguration(defaultRollback = true)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class JijinWithdrawServiceTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private JijinWithdrawService jijinWithdrawService;
	@Autowired
	private JunitMockModelBuilder junitMockModelBuilder;
    @Autowired
    private JijinGatewayRemoteService jijinGatewayRemoteService;
    @Autowired
    private MqService mqService;
    @Autowired
    private FundAppCallerService fundAppCallerService;
    @Autowired
    private UserService userService;
    
//	@Before
	public void setUp() {

		fundAppCallerService = mock(FundAppCallerService.class);
		jijinWithdrawService.setFundAppCallerService(fundAppCallerService);
		userService = mock(UserService.class);
		jijinWithdrawService.setUserService(userService);
		mqService = mock(MqService.class);
		jijinWithdrawService.setMqService(mqService);
		jijinGatewayRemoteService = mock(JijinGatewayRemoteService.class);
		jijinWithdrawService.setJijinGatewayRemoteService(jijinGatewayRemoteService);

		UserInfoGson userInfoGson = new UserInfoGson();
		userInfoGson.setMobileNo("1300000000");
		userInfoGson.setName("JUNIT USER");

		when(userService.getUserInfo(1l)).thenReturn(userInfoGson);
		BankAccountDetailGson bankAccountDetailGson = new BankAccountDetailGson();
        when(userService.bankAccountDetail(Mockito.anyLong())).thenReturn(bankAccountDetailGson);

		junitMockModelBuilder.buildJijinInfo("000999","dh103","currency");
		junitMockModelBuilder.buildJijinUserBalance(1l,"000999");
		junitMockModelBuilder.buildJijinUserAccount(1l,"dh103");
	}
    
//	@Ignore
    @Test
    public void testCreateWithdrawRecord() throws Exception {
    	
    	GWResponseGson gwResponseGson = new GWResponseGson();
    	gwResponseGson.setRetCode("000");
    	GWQueryDahuaBalanceResponseGson remoteResponseGson = new GWQueryDahuaBalanceResponseGson();
    	remoteResponseGson.setBalance("1000.00");
    	gwResponseGson.setContent(JsonHelper.toJson(remoteResponseGson));
//        when(jijinGatewayRemoteService.getDahuaBalance(DahuaAccountTypeEnum.中间户.getAccountType())).thenReturn(gwResponseGson);
//        when(jijinGatewayRemoteService.getDahuaBalance(DahuaAccountTypeEnum.垫资户.getAccountType())).thenReturn(gwResponseGson);
//
//        BaseResultDTO baseResultDTO = new BaseResultDTO();
//
//        when( fundAppCallerService.rechargeDahua(
//                Mockito.anyLong(),
//                Mockito.anyString(),
//                Mockito.any(BigDecimal.class),
//                Mockito.anyString())).thenReturn(baseResultDTO);
//
//        jijinWithdrawService.createWithdrawRecord();
//        jijinWithdrawService.handleDaHuaAccountStatus();
        //jijinWithdrawService.handlePaymentResult(String.valueOf(recordId), JijinConstants.RECHARGE_NAME, "success", new BigDecimal("100.00"), new BigDecimal("100.00"), JijinConstants.RECHARGE_CHANNEL_ID);

    }
}