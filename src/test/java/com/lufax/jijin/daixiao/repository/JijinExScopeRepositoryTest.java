package com.lufax.jijin.daixiao.repository;

import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.daixiao.constant.RecordStatus;
import com.lufax.jijin.daixiao.dto.JijinExScopeDTO;

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
public class JijinExScopeRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    protected JijinExScopeRepository repository;

    @Test@Ignore
    public void testInsert() {
        JijinExScopeDTO jijinExScopeDTO = new JijinExScopeDTO();
        jijinExScopeDTO.setStatus(RecordStatus.NEW.name());
        jijinExScopeDTO.setFundCode("110020");
        jijinExScopeDTO.setBatchId(2L);
        jijinExScopeDTO.setPurchaseShare(new BigDecimal(1000));
        jijinExScopeDTO.setRedeemShare(new BigDecimal(2000));
        jijinExScopeDTO.setReportDate("20150723");
        jijinExScopeDTO.setStartDate("20150702");
        jijinExScopeDTO.setFundShare(new BigDecimal(10000000));
        repository.insertJijinExScope(jijinExScopeDTO);

    }

    @Test@Ignore
    public void testGetJijinExScopeByFundCode() {
        List<JijinExScopeDTO> list = repository.getJijinExScopeByFundCode("110020",1);
        Assert.assertTrue(list.size() > 0);
    }

    @Test@Ignore
    public void testUpdateJijinExScope() {
        int a = repository.updateJijinExScope(MapUtils.buildKeyValueMap("id", 1, "status", RecordStatus.DISPACHED.name()));
        Assert.assertTrue(a == 1);
        List<JijinExScopeDTO> list = repository.getJijinExScopeByStatusAndMaxNum(RecordStatus.DISPACHED.name(), 100);
        Assert.assertTrue(list.size() > 0);
    }

    @Test@Ignore
    public void testCount(){
        Integer a =  repository.countNumberOfAfterReportDate("110020","20150811");
        System.out.println(a);
        int b= repository.updateSameFundCodeReportDateRecordToNotVaild(4701L,"100002","20150811");
        System.out.println(b);
    }
}
