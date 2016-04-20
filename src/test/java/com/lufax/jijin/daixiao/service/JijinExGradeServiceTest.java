package com.lufax.jijin.daixiao.service;

import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.lufax.jijin.daixiao.dto.JijinExGradeDTO;
import com.lufax.jijin.daixiao.repository.JijinExGradeRepository;
import com.lufax.jijin.fundation.dto.JijinSyncFileDTO;

@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class JijinExGradeServiceTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private JijinExGradeService jijinExGradeService;
    @Autowired
    private JijinExGradeRepository jijinExGradeRepository;


    @Test@Ignore
    public void testRecordFileSync() throws Exception {

        JijinSyncFileDTO jijinSyncFileDTO = new JijinSyncFileDTO();
        jijinSyncFileDTO.setId(446);
        jijinSyncFileDTO.setFileName("/tmp/wind/wind_20150729230000_08.txt");
        jijinSyncFileDTO.setCurrentLine(1l);
        jijinExGradeService.recordFileSync(jijinSyncFileDTO);
        assertTrue(1 == 1);
    }

    @Test@Ignore
    public void testHandleJijinExGrade() {
        JijinExGradeDTO jijinExGradeDTO = new JijinExGradeDTO();
        jijinExGradeDTO.setFundCode("110020");
        jijinExGradeDTO.setId(4L);
        jijinExGradeDTO.setBatchId(3L);
        jijinExGradeService.handleJijinExGrade(jijinExGradeDTO);
    }

}
