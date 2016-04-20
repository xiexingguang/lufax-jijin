package com.lufax.jijin.daixiao.repository;

import com.lufax.jijin.daixiao.dto.JijinExIndustryConfDTO;
import com.lufax.jijin.daixiao.dto.JijinExStockConfDTO;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static junit.framework.Assert.assertTrue;

/**
 * Created by chenguang451 on 2016/1/6.
 */
@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class JijinExStockConfRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {

    private Long batchId = 0l;
    private String status = "";
    private String fundCode = "B00001";
    private String bizCode = "20";

    @Autowired
    private JijinExStockConfRepository repository;

    //@Before
    public void setUp() throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        batchId = Long.valueOf(simpleDateFormat.format(new Date()));
        status = "NEW";
    }

    @Test@Ignore
    public void testInsert(){
        JijinExStockConfDTO dto = new JijinExStockConfDTO();
        dto.setFundCode(fundCode);
        dto.setStatus(status);
        dto.setIsValid(1L);
        dto.setBatchId(batchId);
        dto.setEndDate("20160126");

        dto.setStockPer("35.55");
        dto.setAnnounceDate("52000000");
        dto.setAnnounceDate("20160126");
        dto.setCapitalStockPer("20.53");
        dto.setEndDate("20160115");
        dto.setStockCode("000025");
        dto.setStockName("test_stock");
        dto.setStockShare("220000");
        dto.setStockValue("12.45");
        dto.setStockValuePer("1.5");

        repository.insert(dto);
        assertTrue(dto.getId()>0);
    }

}
