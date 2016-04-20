package com.lufax.jijin.daixiao.service;

import java.io.IOException;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.lufax.jijin.daixiao.dto.JijinExYieldRateDTO;
import com.lufax.jijin.daixiao.repository.JijinExYieldRateRepository;
import com.lufax.jijin.fundation.dto.JijinSyncFileDTO;

@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class JijinExYieldRateServiceTest extends AbstractJUnit4SpringContextTests {

	@Autowired
	private JijinExYieldRateService jijinExYieldRateService;
	@Autowired
	private JijinExYieldRateRepository jijinExYieldRateRepository;

    @Test@Ignore
	public void test(){
		JijinSyncFileDTO dto = new JijinSyncFileDTO();
		dto.setFileName("/home/chenqunhui/Documents/yieldRate.txt");
		try {
			jijinExYieldRateService.recordFileSync(dto);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test@Ignore
	public void testHandleJijinExYieldRate(){
		List<JijinExYieldRateDTO> dtolist = jijinExYieldRateRepository.getUnDispatchedJijinExYieldRate(1);
		jijinExYieldRateService.handleJijinExYieldRate(dtolist.get(0));
	}
}
