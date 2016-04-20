package com.lufax.jijin.daixiao.repository;

import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.daixiao.constant.RecordStatus;
import com.lufax.jijin.daixiao.dto.JijinExInfoDTO;

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
public class JijinExInfoRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {


    @Autowired
    protected JijinExInfoRepository repository;

    @Test@Ignore
    public void testInsert() {
        JijinExInfoDTO jijinExInfoDTO = new JijinExInfoDTO();
        jijinExInfoDTO.setFundCode("110020");
        jijinExInfoDTO.setBatchId(3L);
        jijinExInfoDTO.setStatus(RecordStatus.NEW.name());
        jijinExInfoDTO.setBenchMark("benchMark");
        jijinExInfoDTO.setCompanyCode("comCode");
        jijinExInfoDTO.setCompanyFullName("comFullName");
        jijinExInfoDTO.setCustodianBank("bank");
        jijinExInfoDTO.setFullName("fullName");
        jijinExInfoDTO.setInvestType("investType");
        jijinExInfoDTO.setManagementComp("mamCom");
        jijinExInfoDTO.setManagementFee(new BigDecimal(1));
        jijinExInfoDTO.setName("name");
        jijinExInfoDTO.setTrusteeFee(new BigDecimal(2));
        jijinExInfoDTO.setSetupDate("20150723");
        repository.insertJijinExInfo(jijinExInfoDTO);
    }


    @Test@Ignore
    public void testGetJijinExInfoByFundCode() {
        List<JijinExInfoDTO> list = repository.getJijinExInfoByFundCode("110020");
        Assert.assertTrue(list.size() > 0);
    }


    @Test@Ignore
    public void testUpdateJijinExInfo() {
        int a = repository.updateJijinExInfo(MapUtils.buildKeyValueMap("id", 5, "status", RecordStatus.DISPACHED.name()));
        Assert.assertTrue(a == 1);
    }

    @Test@Ignore
    public void testGetJijinExInfosByStatus() {
        List<JijinExInfoDTO> list = repository.getJijinExInfoByStatus(RecordStatus.DISPACHED.name(), 100);
        Assert.assertTrue(list.size() > 0);
        JijinExInfoDTO jijinExInfoDTO = repository.getJijinExInfoById(list.get(0).getId());
        Assert.assertTrue(jijinExInfoDTO != null);
    }
}
