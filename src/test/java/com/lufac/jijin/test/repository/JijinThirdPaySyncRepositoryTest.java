package com.lufac.jijin.test.repository;

import java.math.BigDecimal;
import java.util.List;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.fundation.dto.JijinThirdPaySyncDTO;
import com.lufax.jijin.fundation.repository.JijinThirdPaySyncRepository;

@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class JijinThirdPaySyncRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests{
	
	 @Autowired
	protected JijinThirdPaySyncRepository jijinPafSyncRepository;
	 
	 
    @Test@Ignore
    public void testInsert(){
    	
    	JijinThirdPaySyncDTO dto= new JijinThirdPaySyncDTO();
       	    dto.setFileId(1L);
    	    dto.setPayNo("pafNo");
    	    dto.setPaySerialNo("pafSerialNo");
    	    dto.setAppSheetNo("appSheetNo");
    	    dto.setAmount(new BigDecimal("18.88"));
    	    dto.setCurrency("CNY");
    	    dto.setPayType("04");
    	    dto.setTrxTime("20150514231512");
    	    dto.setTrxResult("S");
    	    dto.setStatus(JijinThirdPaySyncDTO.Status.NEW);
    	    dto.setChannel("PAF");

    	    
    	    jijinPafSyncRepository.insertBusJijinThirdPaySync(dto);
    }
    
    @Test@Ignore
    public void testFind(){
    	// List<JijinThirdPaySyncDTO> dtos = jijinPafSyncRepository.findBusJijinThirdPaySync(MapUtils.buildKeyValueMap("status","Test","channel","PAF"));
    	 //assertTrue(dtos.size()>0);

    	 List<JijinThirdPaySyncDTO> dtos = jijinPafSyncRepository.getUnDispatchRecords(JijinThirdPaySyncDTO.Status.NEW,"PAF","04",200);
    	 assertTrue(dtos.size()>0);
    	 
    }
	    
}
