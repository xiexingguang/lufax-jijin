package com.lufax.jijin.daixiao.repository;

import com.lufax.jijin.daixiao.dto.JijinExAnnounceDTO;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import java.text.SimpleDateFormat;
import java.util.Date;

import static junit.framework.Assert.assertTrue;

/**
 * Created by chenguang451 on 2016/1/6.
 */
@Ignore
@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class JijinExAnnounceRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {

    private Long batchId = 0l;
    private String status = "";
    private String fundCode = "B00001";
    private String bizCode = "20";

    @Autowired
    private JijinExAnnounceRepository repository;

    //@Before
    public void setUp() throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        batchId = Long.valueOf(simpleDateFormat.format(new Date()));
        status = "NEW";
    }

    @Test
    @Ignore
    public void testInsert(){
        JijinExAnnounceDTO dto = new JijinExAnnounceDTO();
        dto.setFundCode(fundCode);
        dto.setStatus(status);
        dto.setIsValid(1L);
        dto.setBatchId(batchId);

        dto.setAbstractInfo("test abstract info2.");
        dto.setColCode("test column code");
        dto.setCompanyId("54432");
        dto.setPubDate("20160118");
        dto.setPubLink("htt test");
        dto.setSecurityId("abcdefg");
        dto.setTitle("announce title");

        repository.insert(dto);
        assertTrue(dto.getId()>0);
    }

    @Test
    @Ignore
    public void testSelectLatest(){
        JijinExAnnounceDTO dto = repository.queryLatestRecordByFundCode(fundCode);
        System.out.println(dto);
        assertTrue(dto.getId()>0);
    }
}
