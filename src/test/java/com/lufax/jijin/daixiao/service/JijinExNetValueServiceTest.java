package com.lufax.jijin.daixiao.service;

import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.lufax.jijin.fundation.dto.JijinSyncFileDTO;

@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class JijinExNetValueServiceTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private JijinExNetValueService jijinExNetValueService;

    @Test@Ignore
    public void testRecordFileSync() throws Exception {

        JijinSyncFileDTO jijinSyncFileDTO = new JijinSyncFileDTO();
        jijinSyncFileDTO.setId(444);
        jijinSyncFileDTO.setFileName("/tmp/wind/wind_20150729230000_06.txt");
        jijinSyncFileDTO.setCurrentLine(1l);
        jijinExNetValueService.recordFileSync(jijinSyncFileDTO);
        assertTrue(1 == 1);
    }
}
