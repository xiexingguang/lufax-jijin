package com.lufax.jijin.daixiao.service;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.lufax.jijin.daixiao.dto.JijinExDictDTO;
import com.lufax.jijin.daixiao.repository.JijinExDictRepository;

@Ignore
@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class JijinExDictServiceTest extends AbstractJUnit4SpringContextTests{

	@Autowired
	private JijinExDictService jijinExDictService;
	@Autowired
	private JijinExDictRepository jijinExDictRepository;
	
	@Test@Ignore
	public void testHandleJijinExDict(){
		List<JijinExDictDTO> dto = jijinExDictRepository.getUnDispachedJijinExDictList(20);
		//JijinExDictDTO dot = new JijinExDictDTO();
		//dot.setId(1112L);
		//dot.setFundCode("000555");
		jijinExDictService.handExDict(dto.get(0));
	}
	
}
