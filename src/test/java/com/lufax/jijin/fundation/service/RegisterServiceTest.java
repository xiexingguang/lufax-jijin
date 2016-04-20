package com.lufax.jijin.fundation.service;

import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.google.common.collect.Maps;
import com.lufax.jijin.base.utils.JsonHelper;
import com.lufax.jijin.fundation.dto.JijinRegisteRecordDTO;


@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class RegisterServiceTest extends AbstractTransactionalJUnit4SpringContextTests{
	
	@Autowired
	private AccountService service;

	@Test@Ignore
	public void testSaveRegisterAccountSuccess() {
		JijinRegisteRecordDTO request = new JijinRegisteRecordDTO();
		request.setAppNo("registerservice_test");
		request.setContractNo("contract_no");
		request.setCustNo("customer");
		request.setErrorCode("0");
		request.setErrorMsg("success");
		request.setInstId("inst_test");
		request.setPayNo("paf");
		request.setChannel("PAF");
		request.setStatus(JijinRegisteRecordDTO.RegisteStatus.SUCCESS);
		request.setUserId(1112);
		assertTrue(service.saveRegisterAccount(request));
	}
	
	@Test@Ignore
	public void testSaveRegisterAccountFail(){
		JijinRegisteRecordDTO request = new JijinRegisteRecordDTO();
		request.setAppNo("registerservice_test");
		request.setContractNo("contract_no");
		request.setCustNo("customer");
		request.setErrorCode("0");
		request.setErrorMsg("success");
		request.setInstId("inst_test");
		request.setPayNo("paf");
		request.setChannel("PAF");
		request.setStatus(JijinRegisteRecordDTO.RegisteStatus.FAIL);
		request.setUserId(1112);
		assertTrue(service.saveRegisterAccount(request));
	}
	
	@Test@Ignore
	public void testmq(){
		Map<String, String> paras = Maps.newHashMap();
		paras.put("fundCode", "111111");
		paras.put("increaseDate", "20150615");
		paras.put("dayIncrease", "0.0014");
		paras.put("monthIncrease", null);
		paras.put("sixMonthIncrease", null);
		paras.put("thisYearIncrease", null);
		paras.put("threeMonthIncrease", null);
		paras.put("totalIncrease", null);
		paras.put("yearIncrease", null);
		System.out.println(JsonHelper.toJson(paras));
	}

}
