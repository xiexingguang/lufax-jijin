package com.lufax.jijin.daixiao.service;

import java.io.IOException;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.lufax.jijin.daixiao.dto.JijinExMfPerformDTO;
import com.lufax.jijin.daixiao.repository.JijinExMfPerformRepository;
import com.lufax.jijin.fundation.dto.JijinSyncFileDTO;

@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class JijinExMfPerformServiceTest extends AbstractTransactionalJUnit4SpringContextTests{

	@Autowired
	private JijinExMfPerformService jijinExMfPerformService;
	@Autowired
	private JijinExMfPerformRepository repository;
	@Test
	@Ignore
	public void testRecordFileSync(){
		JijinSyncFileDTO jijinSyncFileDTO = new JijinSyncFileDTO();
	    jijinSyncFileDTO.setId(271);
	    jijinSyncFileDTO.setFileName("/home/chenqunhui/Documents/wind_20150729230000_13.txt");
	    jijinSyncFileDTO.setCurrentLine(1l);
	    try {
	    	jijinExMfPerformService.recordFileSync(jijinSyncFileDTO);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test@Ignore
	public void testHandle(){
		List<JijinExMfPerformDTO> list = repository.getUnDispatchedJijinExMfPerform(3000);
		for(JijinExMfPerformDTO dto: list){
			jijinExMfPerformService.handleJijinExMfPerform(dto);
		}
		
	}
}
