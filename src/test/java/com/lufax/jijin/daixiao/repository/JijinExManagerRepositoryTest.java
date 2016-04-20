package com.lufax.jijin.daixiao.repository;

import com.lufax.jijin.daixiao.constant.RecordStatus;
import com.lufax.jijin.daixiao.dto.JijinExManagerDTO;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import java.util.List;

@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class JijinExManagerRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    protected JijinExManagerRepository repository;

    @Test@Ignore
    public void testInsert() {
        JijinExManagerDTO jijinManagerDTO = new JijinExManagerDTO();
        jijinManagerDTO.setBatchId(2L);
        jijinManagerDTO.setFundCode("110020");
        jijinManagerDTO.setManager("chen");
        jijinManagerDTO.setResume("ttttttttttt");
        jijinManagerDTO.setStatus(RecordStatus.NEW.name());
        repository.insertJijinExManager(jijinManagerDTO);
    }


    @Test@Ignore
    public void testGetJijinExManagersByStatus() {
        List<JijinExManagerDTO> list = repository.getJijinExManagersByStatus(RecordStatus.NEW.name(), 100);
        Assert.assertTrue(list.size() > 0);
        list = repository.getJijinExManagersByBatchIdAndFundCode(2L,"110020");
        Assert.assertTrue(list.size() > 0);
        JijinExManagerDTO jijinManagerDTO =  repository.getJijinExManagerById(list.get(0).getId());
        Assert.assertTrue(!jijinManagerDTO.getManager().equals(""));
    }

}
