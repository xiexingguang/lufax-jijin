package com.lufac.jijin.test.repository;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.lufax.jijin.fundation.dto.JijinRedeemThresholdDTO;
import com.lufax.jijin.fundation.repository.JijinRedeemThresholdRepository;

import static org.junit.Assert.*;

@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class JijinRedeemThresholdRepositoryTest extends AbstractJUnit4SpringContextTests{

	@Autowired
	private JijinRedeemThresholdRepository repostory;
	
	@Test@Ignore
	public void testGetRedeemThresholdByFundCode(){
		JijinRedeemThresholdDTO dto = repostory.getRedeemThresholdByFundCode("000379");
		assertNotNull(dto);
	}
	

	@Test@Ignore
	public void testUpdateJijinRedeemStatusByFundCode(){
		int i = repostory.updateJijinRedeemStatusByFundCode("000379", "CLOSE");
		assertEquals(i,1);
		
	}
}
