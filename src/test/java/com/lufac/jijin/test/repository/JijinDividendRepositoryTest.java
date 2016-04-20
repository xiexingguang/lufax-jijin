package com.lufac.jijin.test.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.lufax.jijin.fundation.dto.JijinDividendDTO;
import com.lufax.jijin.fundation.dto.JijinDividendDTO.Status;
import com.lufax.jijin.fundation.repository.JijinDividendRepository;
import com.lufax.jijin.fundation.service.domain.Dividend;
import com.lufax.mq.client.util.MapUtils;

@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class JijinDividendRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests{
	
	@Autowired
	JijinDividendRepository repository;
	
	SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");

	@Test@Ignore
	public void testInsertJijinDividend() {
		JijinDividendDTO dto = new JijinDividendDTO();
		dto.setInstId("yfd");
		dto.setAppSheetNo("app_sheet_no_insert");
		dto.setChargeType("A");
		dto.setDividendAmount(BigDecimal.valueOf(0.25));
		dto.setDividendDate(format.format(new Date()));
		dto.setDividendMode("0");
		dto.setDividendShare(BigDecimal.valueOf(1.36));
		dto.setDividendType(Dividend.Type.SWITCH_DIVIDEND.getName());
		dto.setFee(BigDecimal.valueOf(0.50));
		dto.setFileId(10001);
		dto.setFundCode("003201");
		dto.setRightDate(format.format(new Date()));
		dto.setResCode("0");
		dto.setResMsg("success");
		dto.setStatus(Status.NEW);
		dto.setTrxDate(format.format(new Date()));
		dto.setUserId(1001);
		repository.insertJijinDividend(dto);
		assertTrue(dto.getId()>0);
	}

	@Test@Ignore
	public void testFindJijinDividend() {
		JijinDividendDTO dto = repository.findJijinDividend(MapUtils.buildKeyValueMap("id","181","status",Status.NEW.name()));
		assertEquals(dto.getAppSheetNo(), "app_sheet_no_insert");
	}

	@Test@Ignore
	public void testFindJijinDividends() {
		List<JijinDividendDTO> dtos = repository.findJijinDividends(MapUtils.buildKeyValueMap("id","181","status",Status.NEW.name()));
		assertTrue(dtos.size()>0);
	}

	@Test@Ignore
	public void testFindBatchNewDividendsByType(){
		List<JijinDividendDTO> dtos = repository.findBatchNewDividendsByType(Dividend.Type.SWITCH_DIVIDEND.getName(), 3);
		assertEquals(1, dtos.size());
	}
	
	@Test@Ignore
	public void testFind(){
		JijinDividendDTO dto = repository.findJijinDividendByAppno("app_sheet_no_insert", "yfd");
		assertTrue(dto!=null);
	}
	
	@Test@Ignore
	public void testUpdateJijinDividend() {
		JijinDividendDTO dto = new JijinDividendDTO();
		dto.setInstId("yfd");
		dto.setAppSheetNo("app_sheet_no_update");
		dto.setChargeType("A");
		dto.setDividendAmount(BigDecimal.valueOf(0.25));
		dto.setDividendDate(format.format(new Date()));
		dto.setDividendMode("0");
		dto.setDividendShare(BigDecimal.valueOf(1.36));
		dto.setDividendType(Dividend.Type.CASH.getName());
		dto.setFee(BigDecimal.valueOf(0.50));
		dto.setFileId(10001);
		dto.setFundCode("003201");
		dto.setRightDate(format.format(new Date()));
		dto.setResCode("0");
		dto.setResMsg("success");
		dto.setStatus(Status.NEW);
		dto.setTrxDate(format.format(new Date()));
		dto.setUserId(1001);
		repository.insertJijinDividend(dto);
		int result = repository.updateJijinDividend(MapUtils.buildKeyValueMap("id",dto.getId(),"status",Status.DISPATCHED));
		assertEquals(result, 1);
	}

}
