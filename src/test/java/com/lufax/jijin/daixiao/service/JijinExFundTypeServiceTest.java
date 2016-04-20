package com.lufax.jijin.daixiao.service;

import java.io.IOException;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.lufax.jijin.daixiao.dto.JijinExFundTypeDTO;
import com.lufax.jijin.daixiao.repository.JijinExFundTypeRepository;
import com.lufax.jijin.fundation.dto.JijinSyncFileDTO;

@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class JijinExFundTypeServiceTest  extends AbstractTransactionalJUnit4SpringContextTests{

	@Autowired
	private JijinExFundTypeService jijinExFundTypeService;
	@Autowired
	private JijinExFundTypeRepository jijinExFundTypeRepository;
	
	@Test@Ignore
	public void testRecordFileSync(){
		JijinSyncFileDTO jijinSyncFileDTO = new JijinSyncFileDTO();
	    jijinSyncFileDTO.setId(271);
	    jijinSyncFileDTO.setFileName("/home/chenqunhui/Documents/wind_20150729230000_11.txt");
	    jijinSyncFileDTO.setCurrentLine(1l);
	    try {
			jijinExFundTypeService.recordFileSync(jijinSyncFileDTO);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test@Ignore
	public void testHandleJijinExFundType(){
		List<JijinExFundTypeDTO> list = jijinExFundTypeRepository.getUnDispachedJijinExFundType(10);
		for(JijinExFundTypeDTO dto :list){
			try {
				jijinExFundTypeService.handleFundType(dto);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
}
