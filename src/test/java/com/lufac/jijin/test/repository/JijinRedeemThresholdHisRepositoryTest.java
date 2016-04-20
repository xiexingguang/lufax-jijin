package com.lufac.jijin.test.repository;

import java.math.BigDecimal;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.lufax.jijin.fundation.dto.JijinRedeemThresholdHisDTO;
import com.lufax.jijin.fundation.repository.JijinRedeemThresholdHisRepository;

@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class JijinRedeemThresholdHisRepositoryTest extends AbstractJUnit4SpringContextTests{

	@Autowired
	private JijinRedeemThresholdHisRepository repostory;
	
	@Test@Ignore
	public void testInsertJijinRedeemThresholdHis(){
		JijinRedeemThresholdHisDTO dto = new JijinRedeemThresholdHisDTO();
		dto.setCurrentAmount(new BigDecimal("10000.555"));
		dto.setFundCode("000379");
		dto.setNewStatus("CLOSE");
		dto.setOldStatus("OPEN");
		repostory.insertJijinRedeemThresholdHis(dto);
	}
}
