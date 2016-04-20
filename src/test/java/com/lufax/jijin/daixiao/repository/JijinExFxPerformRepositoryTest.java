package com.lufax.jijin.daixiao.repository;

import com.lufax.jijin.daixiao.dto.JijinExFxPerformDTO;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertTrue;


@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class JijinExFxPerformRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests{

    @Autowired
    private JijinExFxPerformRepository jijinExFxPerformRepository;

    @Test@Ignore
    public void testGetJijinExFxPerformByFindexCode(){
        List<JijinExFxPerformDTO> lst = jijinExFxPerformRepository.getJijinExFxPerformByFindexCode("000300");
        System.out.println(lst.size());
        assertTrue(1 == 1);
    }

    @Test@Ignore
    public void test(){
        assertTrue(1 == 1);
        String isBuyDailyLimit = "0";
        BigDecimal buydailylimit = BigDecimal.ZERO;
        BigDecimal testBD = null;
        //JijinInfo/Products：购买单日限额
        if (testBD != null && testBD.compareTo(BigDecimal.ZERO) > 0) {
            isBuyDailyLimit = "1";
            buydailylimit = testBD;
        }else{
            buydailylimit= new BigDecimal("999999999999");
        }
        System.out.println("11111="+isBuyDailyLimit+"|"+buydailylimit);
    }
}
