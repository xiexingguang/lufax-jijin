package com.lufac.jijin.test.repository;

import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.lufax.jijin.fundation.dto.JijinUserBalanceHistoryDTO;
import com.lufax.jijin.fundation.repository.JijinUserBalanceHistoryRepository;

@Ignore
@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class JijinUserBalanceHistoryRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests{
	
	 @Autowired
	protected JijinUserBalanceHistoryRepository jijinUserBalanceHistoryRepository;
	 

    @Test@Ignore
    public void testInsert(){
    	
    	JijinUserBalanceHistoryDTO dto= new JijinUserBalanceHistoryDTO();
    	    dto.setUserId(1001L);
    	    dto.setFundCode("470009");
    	    dto.setShareBalance(new BigDecimal("122.23"));
    	    dto.setFrozenShare(new BigDecimal("0.00"));
    	    dto.setDividendType("FUND");
    	    dto.setApplyingAmount(new BigDecimal("100.00"));
    	    dto.setBalance(new BigDecimal("100.00"));
    	    dto.setFrozenAmount(new BigDecimal("100.00"));
    	    dto.setShareBalance(new BigDecimal("100.00"));
    	    dto.setFrozenShare(new BigDecimal("100.00"));
    	    dto.setBizDate("20150902");
//    	    dto.setIncrease(new BigDecimal("100.00"));
//    	    dto.setDecrease(new BigDecimal("100.00"));
    	    dto.setRemark("测试");
    	    dto.setDividendStatus("DONE");
    
    	    jijinUserBalanceHistoryRepository.insertBusJijinUserBalanceHistory(dto);
    }
    

    @Test@Ignore
    public void testFind(){
    	 List<JijinUserBalanceHistoryDTO> dtoList = jijinUserBalanceHistoryRepository.findBusJijinUserBalanceHistoryList(1001l, "470009","20150902");
    	 assertNotNull(dtoList);
    	 
    }
   
}
