package com.lufax.jijin.daixiao.repository;

import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.daixiao.dto.JijinExSellDayCountDTO;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by NiuZhanJun on 7/23/15.
 */
@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class JijinExSellDayCountRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {

    private Long batchId = 0l;
    private String status = "";
    private String fundCode = "B00001";
    private String bizCode = "20";

    @Before
    public void setUp() throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        batchId = Long.valueOf(simpleDateFormat.format(new Date()));
        status = "NEW";
    }

    @Autowired
    private JijinExSellDayCountRepository jijinExSellDayCountRepository;

    @Test@Ignore
    public void testInsertJijinExSellDayCount() throws Exception {

        JijinExSellDayCountDTO jijinExSellDayCountDTO = new JijinExSellDayCountDTO();
        jijinExSellDayCountDTO.setFundCode(fundCode);
        jijinExSellDayCountDTO.setSellConfirmDayCount(10l);
        jijinExSellDayCountDTO.setBatchId(batchId);
        jijinExSellDayCountDTO.setStatus(status);
        JijinExSellDayCountDTO jijinExSellDayCountDTO1 = jijinExSellDayCountRepository.insertJijinExSellDayCount(jijinExSellDayCountDTO);

        assertTrue(jijinExSellDayCountDTO1.getId() > 0);
    }

    @Test@Ignore
    public void testUpdateJijinExSellDayCount() throws Exception {
        Long testId = 1l;
        String updStatus = "INIT";
        List<JijinExSellDayCountDTO> list = jijinExSellDayCountRepository.getJijinExSellDayCount(MapUtils.buildKeyValueMap("id", testId));
        JijinExSellDayCountDTO jijinExSellDayCountDTO = list.get(0);
        if (jijinExSellDayCountDTO.getStatus().equals("INIT")) {
            updStatus = "COMPLETE";
        }
        Map map = new HashMap();
        map.put("id", testId);
        map.put("status", updStatus);
        int i = jijinExSellDayCountRepository.updateJijinExSellDayCount(map);
        assertTrue(i == 1);
    }

    @Test@Ignore
    public void testGetJijinExSellDayCountByFundCodeAndBizCode() throws Exception {
        List<JijinExSellDayCountDTO> list = jijinExSellDayCountRepository.getJijinExSellDayCountByFundCode(fundCode);
        assertTrue(list.size() > 0);
    }

    @Test@Ignore
    public void testGetUnDispachedJijinExSellDayCount(){
        List<JijinExSellDayCountDTO> unDispachedJijinExSellDayCount = jijinExSellDayCountRepository.getUnDispachedJijinExSellDayCount(3);
        System.out.println("========="+unDispachedJijinExSellDayCount.size());

        unDispachedJijinExSellDayCount = jijinExSellDayCountRepository.getUnDispachedJijinExSellDayCount(100);
        System.out.println("========="+unDispachedJijinExSellDayCount.size());
        assertTrue(unDispachedJijinExSellDayCount.size() > 0);
    }

    @Test@Ignore
    public void testGetLatestBatchIdByFundCode(){
        Long latestBatchId = jijinExSellDayCountRepository.getLatestBatchIdByFundCode("B00001");
        System.out.println(latestBatchId);
        assertTrue(latestBatchId>0);
    }
}