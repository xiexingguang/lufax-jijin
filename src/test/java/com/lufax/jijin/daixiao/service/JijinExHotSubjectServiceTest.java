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

import com.lufax.jijin.daixiao.dto.JijinExHotSubjectDTO;
import com.lufax.jijin.daixiao.repository.JijinExHotSubjectRepository;
import com.lufax.jijin.fundation.dto.JijinSyncFileDTO;

@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class JijinExHotSubjectServiceTest extends AbstractJUnit4SpringContextTests{

	@Autowired
	private JijinExHotSubjectService jijinExHotSubjectService;
	@Autowired
	private JijinExHotSubjectRepository jijinExHotSubjectRepository;
	@Test@Ignore
	public void testRecordFileSync(){
		JijinSyncFileDTO jijinSyncFileDTO = new JijinSyncFileDTO();
	    jijinSyncFileDTO.setId(278);
	    jijinSyncFileDTO.setFileName("/home/chenqunhui/Documents/wind_20150729230000_17.txt");
	    jijinSyncFileDTO.setCurrentLine(1l);
	    try {
	    	jijinExHotSubjectService.recordFileSync(jijinSyncFileDTO);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test@Ignore
	public void  testHandJijinExHotSubject(){
		List<JijinExHotSubjectDTO>  list = jijinExHotSubjectRepository.getUndispachedHotSubject(100);//getJijinExHotSubjectsByBatchIdAndFundCode(278L, "000505");
		for(JijinExHotSubjectDTO dto :list){
			try {
				jijinExHotSubjectService.handleJijinExHotSubject(dto);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}