package com.lufax.jijin.daixiao.repository;

import static junit.framework.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.lufax.jijin.daixiao.dto.JijinExMaPerfDTO;
import com.lufax.jijin.fundation.schedular.SyncFileRepository;
import com.lufax.jijin.fundation.util.ApplicationContextUtils;

/**
 * Created by chenguang451 on 2016/1/6.
 */
@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class JijinExMaPerfRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {

    private Long batchId = 0l;
    private String status = "";
    private String managerId = "B00001";
    private String bizCode = "20";

    @Autowired
    private JijinExMaPerfRepository repository;

    //@Before
    public void setUp() throws Exception {
    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        batchId = Long.valueOf(simpleDateFormat.format(new Date()));
        status = "NEW";
    }

    @Test@Ignore
    public void testInsert(){
        JijinExMaPerfDTO dto = new JijinExMaPerfDTO();
        dto.setStatus(status);
        dto.setIsValid(1L);
        dto.setBatchId(batchId);

        dto.setMaId(managerId);
        dto.setPubDate("20160122");
        dto.setBenefitFiveYear("1.68");
        dto.setBenefitFromBegin("2.36");

        repository.insert(dto);
        assertTrue(dto.getId()>0);
    }

/*    @Test
    public void testSelectLatest(){
        Long batchId = repository.getMaxBatchIdByManagerId(managerId);
        JijinExMaPerfDTO dto = repository.queryLatestRecordByManagerId(managerId, batchId);
        System.out.println(dtos.size());
        assertTrue(dtos.size()>0);
    }*/
}
