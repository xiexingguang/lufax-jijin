package com.lufax.jijin.daixiao.service;

import com.lufax.jijin.daixiao.dto.JijinExInfoDTO;
import com.lufax.jijin.fundation.dto.JijinSyncFileDTO;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import static org.junit.Assert.assertTrue;

@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class JijinExInfoServiceTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private JijinExInfoService jijinExInfoService;


    @Test@Ignore
    public void testRecordFileSync() throws Exception {

        JijinSyncFileDTO jijinSyncFileDTO = new JijinSyncFileDTO();
        jijinSyncFileDTO.setId(445);
        jijinSyncFileDTO.setFileName("/tmp/wind/wind_20150729230000_09.txt");
        jijinSyncFileDTO.setCurrentLine(1l);
        jijinExInfoService.recordFileSync(jijinSyncFileDTO);
        assertTrue(1 == 1);
    }

    @Test@Ignore
    public void testHandle(){
        JijinExInfoDTO jijinExInfoDTO = new JijinExInfoDTO();
        jijinExInfoDTO.setFundCode("110020");
        jijinExInfoDTO.setId(2368L);
        jijinExInfoDTO.setBatchId(445L);
        jijinExInfoService.dispatchJijinExInfo(jijinExInfoDTO);
    }
}
