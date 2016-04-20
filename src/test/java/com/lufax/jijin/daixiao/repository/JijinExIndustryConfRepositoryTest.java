package com.lufax.jijin.daixiao.repository;

import com.lufax.jijin.daixiao.dto.JijinExAssetConfDTO;
import com.lufax.jijin.daixiao.dto.JijinExIndustryConfDTO;

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
public class JijinExIndustryConfRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {

    private Long batchId = 0l;
    private String status = "";
    private String fundCode = "B00001";
    private String bizCode = "20";

    @Autowired
    private JijinExIndustryConfRepository repository;

    //@Before
    public void setUp() throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        batchId = Long.valueOf(simpleDateFormat.format(new Date()));
        status = "NEW";
    }

    @Test@Ignore
    public void testInsert(){
        JijinExIndustryConfDTO dto = new JijinExIndustryConfDTO();
        dto.setFundCode(fundCode);
        dto.setStatus(status);
        dto.setIsValid(1L);
        dto.setBatchId(batchId);
        dto.setEndDate("20160126");

        dto.setIndustryCode("0001");
        dto.setIndustryName("test industry");
        dto.setIndustryPer("0.75");

        repository.insert(dto);
        assertTrue(dto.getId()>0);
    }


}
