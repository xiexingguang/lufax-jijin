package com.lufax.jijin.daixiao.service;

import java.io.IOException;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.lufax.jijin.daixiao.dto.JijinExGoodSubjectDTO;
import com.lufax.jijin.daixiao.repository.JijinExGoodSubjectRepository;
import com.lufax.jijin.fundation.dto.JijinSyncFileDTO;

@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class JijinExGoodSubjectServiceTest extends AbstractJUnit4SpringContextTests{

	@Autowired
	private JijinExGoodSubjectService jijinExGoodSubjectService;
	@Autowired
	private JijinExGoodSubjectRepository jijinExGoodSubjectRepository;
	@Test@Ignore
	public void testRecordFileSync(){
		JijinSyncFileDTO jijinSyncFileDTO = new JijinSyncFileDTO();
	    jijinSyncFileDTO.setId(271);
	    jijinSyncFileDTO.setFileName("/home/chenqunhui/Documents/wind_20150729230000_16.txt");
	    jijinSyncFileDTO.setCurrentLine(1l);
	    try {
	    	jijinExGoodSubjectService.recordFileSync(jijinSyncFileDTO);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test@Ignore
	public void testHandJijinExGoodSubject(){
		List<JijinExGoodSubjectDTO> list = jijinExGoodSubjectRepository.getUnDispachedJijinExGoodSubjectList(20);
		//jijinExGoodSubjectRepository.getJijinExGoodSubjectsByBatchIdAndFundCode(batchId, fundCode)
		for(JijinExGoodSubjectDTO dto :list){
			try {
				jijinExGoodSubjectService.handleJijinExGoodSubject(dto);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
