package com.lufax.jijin.daixiao.service;

import com.lufax.jijin.base.utils.DateUtils;
import com.lufax.jijin.fundation.dto.JijinSyncFileDTO;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import java.util.Date;

import static org.junit.Assert.assertTrue;

@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class JijinExDIvidendServiceTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    private JijinExDividendService jijinExDividendService;


    @Test@Ignore
    public void testRecordFileSync() throws Exception {

        Date dateS = new Date();
        JijinSyncFileDTO jijinSyncFileDTO = new JijinSyncFileDTO();
        jijinSyncFileDTO.setId(448);
        jijinSyncFileDTO.setFileName("/home/NiuZhanJun/WorkLufax/Projects/30_jijindaixiao/sampleFiles/wind_20150806131449_10.txt");
        jijinSyncFileDTO.setCurrentLine(1l);
        jijinExDividendService.recordFileSync(jijinSyncFileDTO);
        Date datee = new Date();
        Long all=datee.getTime()-dateS.getTime();
        System.out.println(DateUtils.formatDate(dateS,"yyyy-MM-dd HH:mm:ss"));
        System.out.println(DateUtils.formatDate(datee,"yyyy-MM-dd HH:mm:ss"));
        System.out.println(dateS.getTime() + "--------------" + datee.getTime());
        System.out.println(all);
        assertTrue(1 == 1);
    }

}
