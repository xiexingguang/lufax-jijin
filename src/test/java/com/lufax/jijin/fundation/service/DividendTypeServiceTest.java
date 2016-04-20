package com.lufax.jijin.fundation.service;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.lufax.jijin.base.utils.JsonHelper;
import com.lufax.jijin.fundation.gson.BaseGson;
import com.lufax.jijin.fundation.remote.JijinGatewayRemoteService;
import com.lufax.jijin.fundation.remote.gson.response.GWDividendModifyResponseGson;
import com.lufax.jijin.fundation.remote.gson.response.GWResponseGson;
import com.lufax.jijin.fundation.service.builder.JunitMockModelBuilder;

//@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class DividendTypeServiceTest extends AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private  DividendTypeService service;
	@Autowired
	private JijinGatewayRemoteService jijinGatewayRemoteService;
	@Autowired
	private JunitMockModelBuilder junitMockModelBuilder;

//	@Before
	public void setUp() {
		jijinGatewayRemoteService = mock(JijinGatewayRemoteService.class);
		service.setJijinGatewayRemoteService(jijinGatewayRemoteService);
		junitMockModelBuilder.buildJijinUserBalance(1l,"470009");
		junitMockModelBuilder.buildJijinInfo("470009","yfd101","stock");
		junitMockModelBuilder.buildJijinUserAccount(1l,"yfd101");
	}
	
	@Test@Rollback(true)
	public void testApplyModifyDividendTypeRequest(){
		
//		GWResponseGson res = new GWResponseGson();
//		GWDividendModifyResponseGson modifyRes = new GWDividendModifyResponseGson();
//
//		res.setRetCode("000");
//		modifyRes.setAppSheetSerialNo("appSheetSerialNotest");
//		modifyRes.setErrorCode("0000");
//		res.setContent(JsonHelper.toJson(modifyRes));
//		when(jijinGatewayRemoteService.applyModifyDividend(anyString(),anyString())).thenReturn(res);
//		BaseGson result = service.applyModifyDividendTypeRequest(1l,"470009","1");
//
//		assertTrue(result.getRetCode().equals("00"));
	}
	
}
