package com.lufax.jijin.daixiao.service;

import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.lufax.jijin.daixiao.dto.JijinExScopeDTO;
import com.lufax.jijin.fundation.dto.JijinSyncFileDTO;

@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class JijinExScopeServiceTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private JijinExScopeService jijinExScopeService;


    @Test@Ignore
    public void testRecordFileSync() throws Exception {

        JijinSyncFileDTO jijinSyncFileDTO = new JijinSyncFileDTO();
        jijinSyncFileDTO.setId(447);
        jijinSyncFileDTO.setFileName("/tmp/wind/wind_20150729230000_07.txt");
        jijinSyncFileDTO.setCurrentLine(1l);
        jijinExScopeService.recordFileSync(jijinSyncFileDTO);
        assertTrue(1 == 1);
    }


    @Test@Ignore
    public void testHandleJijinExScope(){
        JijinExScopeDTO jijinExScopeDTO =  new JijinExScopeDTO();
        jijinExScopeDTO.setId(3156L);
        jijinExScopeDTO.setBatchId(447L);
        jijinExScopeDTO.setFundCode("110020");
        jijinExScopeService.handleJijinExScope(jijinExScopeDTO);
    }
}
