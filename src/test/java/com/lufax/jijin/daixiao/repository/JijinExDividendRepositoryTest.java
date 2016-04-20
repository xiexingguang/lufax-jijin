package com.lufax.jijin.daixiao.repository;

import com.lufax.jijin.daixiao.dto.JijinExDividendDTO;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import java.math.BigDecimal;
import java.util.List;

@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class JijinExDividendRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    protected JijinExDividendRepository repository;

    @Test@Ignore
    public void testInsert() {
        JijinExDividendDTO jijinExDividend = new JijinExDividendDTO();
        jijinExDividend.setFundCode("110020");
        jijinExDividend.setBatchId(1L);
        jijinExDividend.setAnnDate("20150721");
        jijinExDividend.setCurrencyCode("CNY");
        jijinExDividend.setDivDate("20150722");
        jijinExDividend.setDivEdexDate("20150723");
        jijinExDividend.setDividendDate("20150724");
        jijinExDividend.setExDate("20150720");
        jijinExDividend.setPerDividend(new BigDecimal(100));
        jijinExDividend.setRecordDate("20150719");
        repository.insertJijinExDividend(jijinExDividend);
    }

    @Test@Ignore
    public void testGetJijinExDividendByFundCode() {
        List<JijinExDividendDTO> list = repository.getJijinExDividendByFundCode("110020");
        Assert.assertTrue(list.size() > 0);
    }
}
