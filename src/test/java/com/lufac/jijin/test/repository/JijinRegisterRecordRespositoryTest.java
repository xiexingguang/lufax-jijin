package com.lufac.jijin.test.repository;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.lufax.jijin.fundation.dto.JijinRegisteRecordDTO;
import com.lufax.jijin.fundation.repository.JijinRegisteRecordRepository;
import com.lufax.mq.client.util.MapUtils;

@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class JijinRegisterRecordRespositoryTest extends AbstractTransactionalJUnit4SpringContextTests{

	@Autowired
	protected JijinRegisteRecordRepository repository;
	
	@Test@Ignore
	public void testInsertBJijinRegisteRecord() {
		JijinRegisteRecordDTO dto = new JijinRegisteRecordDTO();
		dto.setAppNo("3216549879");
		dto.setContractNo("contract_no");
		dto.setCustNo("customer");
		dto.setErrorCode("0");
		dto.setErrorMsg("success");
		dto.setInstId("inst_test");
		dto.setPayNo("pay_no");
		dto.setChannel("PAF");
		dto.setStatus(JijinRegisteRecordDTO.RegisteStatus.SUCCESS);
		dto.setUserId(1111);
		repository.insertBJijinRegisteRecord(dto);
		assertTrue(dto.getId()>0);
	}

	@Test@Ignore
	public void testFindJijinRegisteRecord() {
		JijinRegisteRecordDTO dto = repository.findJijinRegisteRecord(MapUtils.buildKeyValueMap("id","101"));
		assertNotNull(dto);
	}

	@Test@Ignore
	public void testFindJijinRegisteRecords() {
		List<JijinRegisteRecordDTO> dtos = repository.findJijinRegisteRecords(MapUtils.buildKeyValueMap("id","101"));
		assertTrue(dtos.size()>0);
	}

}
