package com.lufax.jijin.daixiao.repository;

import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.lufax.jijin.daixiao.dto.JijinExDictDTO;

/**
 * Created by NiuZhanJun on 8/20/15.
 */
@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class JijinExDictRepositoryTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    private JijinExDictRepository jijinExDictRepository;

    @Test@Ignore
    public void testInsertJijinExDict() throws Exception {

        JijinExDictDTO jijinExDictDTO=new JijinExDictDTO();
        jijinExDictDTO.setFundCode("xxxxxxx");
        jijinExDictRepository.insertJijinExDict(jijinExDictDTO);
        assertTrue(1==1);
    }


}