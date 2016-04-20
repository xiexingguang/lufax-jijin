package com.lufax.jijin.daixiao.repository;

import com.lufax.jijin.daixiao.dto.JijinExFundTypeDTO;

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
public class JijinExFundTypeRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    JijinExFundTypeRepository re;

    @Test@Ignore
    public void testInsertJijinExFundType() {
        JijinExFundTypeDTO dto = new JijinExFundTypeDTO();
        dto.setBatchId(1111l);
        dto.setFundCode("B00001");
        dto.setFundType("股票型");
        dto.setStatus("NEW");
        re.inserJijinExFundType(dto);
    }

    @Test@Ignore
    public void testGetJijinExFundTypeByFundCode() {
        List<JijinExFundTypeDTO> list = re.getJijinExFundTypeByFundCode("470008");
        Assert.assertTrue(list.size() > 0);
    }
}
