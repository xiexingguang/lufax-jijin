package com.lufax.jijin.daixiao.repository;

import com.lufax.jijin.daixiao.dto.JijinExNetValueDTO;

import com.lufax.jijin.fundation.constant.FundSaleCode;
import junit.framework.Assert;

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
public class JijinExNetValueRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    protected JijinExNetValueRepository repository;

    @Test@Ignore
    public void testInsert() {
        JijinExNetValueDTO jijinExNetValueDTO = new JijinExNetValueDTO();
        jijinExNetValueDTO.setNetValue(BigDecimal.ONE);
        jijinExNetValueDTO.setBatchId(1L);
        jijinExNetValueDTO.setEndDate("20150723");
        jijinExNetValueDTO.setFundCode("110020");
        jijinExNetValueDTO.setTotalNetValue(new BigDecimal(100));
        repository.insertJijinExNetValue(jijinExNetValueDTO);
    }

    @Test@Ignore
    public void testGetJijinExNetValueByFundCode() {
        List<JijinExNetValueDTO> list = repository.getJijinExNetValueByFundCodeAndEndDate("110020","20111111");
        Assert.assertTrue(list.size() > 0);
    }

    @Test
    public void test() {
        String instId=null;
        boolean equals = FundSaleCode.DHC.getInstId().equals(instId);
        System.out.println(equals);
        assertTrue(1==1);
    }
}
