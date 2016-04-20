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
import com.lufax.jijin.base.utils.DateUtils;
import com.lufax.jijin.fundation.dto.JijinInfoDTO;
import com.lufax.jijin.fundation.dto.JijinInfoToListDTO;
import com.lufax.jijin.fundation.gson.BaseGson;
import com.lufax.jijin.fundation.gson.CreateJijinInfoResultGson;
import com.lufax.jijin.fundation.service.builder.JunitMockModelBuilder;
import com.lufax.jijin.sysFacade.gson.result.ListCreateProductResultGson;
import com.lufax.jijin.sysFacade.service.ProdOprSvcInterfaceService;

@TransactionConfiguration(defaultRollback = true)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class JijinInfoServiceTest extends AbstractTransactionalJUnit4SpringContextTests{

	@Autowired
	private JijinInfoService jijinInfoService;
	@Autowired
	private ProdOprSvcInterfaceService prodOprSvcInterfaceService;
	@Autowired
	private JunitMockModelBuilder mockBuilder;
	
	@Before
	public void setUp() {
		
		prodOprSvcInterfaceService = mock(ProdOprSvcInterfaceService.class);
		jijinInfoService.setProdOprSvcInterfaceService(prodOprSvcInterfaceService);
	}
	
	@Test
	public void testAddJijinInfo(){

		JijinInfoDTO dto = new JijinInfoDTO();
		dto.setAppliedAmount("999999999999");
		dto.setBuyDailyLimit(new BigDecimal("0"));
		dto.setBuyFeeRateDesc("0");
		dto.setBuyFeeDiscountDesc("0");
		dto.setBuyStatus("PUR_OPEN");
		dto.setChargeType("A");
		dto.setCollectionMode("6");
		dto.setDividendType("0");
		dto.setEstablishedDate(	DateUtils.parseDate("2013-12-03", DateUtils.DATE_FORMAT));
		dto.setForeignId("11111");
		dto.setFundBrand("平安大华");
		dto.setFundCode("000379");
		dto.setFundName("平安大华日增利货币市场基金");
		dto.setFundOpeningType("1");
		dto.setFundType("CURRENCY");
		dto.setInstId("dh103");
		dto.setIsBuyDailyLimit("0");
		dto.setIsFirstPublish(0);
		dto.setMinInvestAmount(new BigDecimal("0.01"));
		dto.setProductCategory("802");
		dto.setProductCode("150045825");
		dto.setRedemptionArrivalDay(1);
		dto.setRedemptionFeeRateDesc("0");
		dto.setRedemptionStatus("RED_OPEN");
		dto.setRiskLevel("1");
		dto.setSourceType("8");
		dto.setTrustee("平安银行");

//		ListCreateProductResultGson res  = new ListCreateProductResultGson();
//		res.setRetCode("SUCCESS");
//		when( prodOprSvcInterfaceService.addJijinInfo(Mockito.any(JijinInfoToListDTO.class))).thenReturn(res);
//
//		CreateJijinInfoResultGson gson = jijinInfoService.addJiJinInfo(dto);
//		assertTrue("00".equals(gson.getRetCode()));
	}

	
    @Test
	public void testUpdateJijinInfo(){
		
//		JijinInfoDTO dto =mockBuilder.buildJijinInfo("47009", "yfd101", "stock");
//		dto.setSourceType("test");
//
//		ListCreateProductResultGson listResult = new ListCreateProductResultGson();
//		listResult.setRetCode("000");
//		when(prodOprSvcInterfaceService.updateJijinInfo(Mockito.anyString())).thenReturn(listResult);
//
//		BaseGson res = jijinInfoService.modifyJiJinInfo(dto);
//		assertTrue("00".equals(res.getRetCode()));
		
     }

}
