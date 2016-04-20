package com.lufac.jijin.test.repository;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.lufax.jijin.fundation.repository.JijinSequenceRepository;

@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class JijinSequenceRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests{
	
	 @Autowired
	protected JijinSequenceRepository jijinSequenceRepository;
	 
   
    @Test@Ignore
    public void testFind(){
    	 long num = jijinSequenceRepository.findJijinSequence();
    	 System.out.println(num);
    	 
    }
	
	  
	    
}
