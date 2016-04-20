package com.lufax.jijin.daixiao.service;

import java.io.IOException;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.lufax.jijin.daixiao.dto.JijinExRiskGradeDTO;
import com.lufax.jijin.daixiao.repository.JijinExRiskGradeRepository;
import com.lufax.jijin.fundation.dto.JijinSyncFileDTO;

@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class JijinExRiskGradeServiceTest extends AbstractTransactionalJUnit4SpringContextTests{

	@Autowired
	private JijinExRiskGradeService jijinExRiskGradeService;
	@Autowired
	private JijinExRiskGradeRepository jijinExRiskGradeRepository;
	
	@Test@Ignore
	public void testRecordFileSync(){
		JijinSyncFileDTO jijinSyncFileDTO = new JijinSyncFileDTO();
	    jijinSyncFileDTO.setId(278);
	    jijinSyncFileDTO.setFileName("/home/chenqunhui/Documents/wind_20150729230000_12.txt");
	    jijinSyncFileDTO.setCurrentLine(1l);
	    try {
	    	jijinExRiskGradeService.recordFileSync(jijinSyncFileDTO);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test@Ignore
	public void testHandle(){
		List<JijinExRiskGradeDTO> dtolist =  jijinExRiskGradeRepository.getUndispachedRiskGrade(20);
		for(JijinExRiskGradeDTO dto :dtolist){
			try {
				jijinExRiskGradeService.handleJijinExRiskGrade(dto);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}