package com.lufax.jijin.daixiao.repository;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.daixiao.dto.JijinExSellLimitDTO;

/**
 * Created by NiuZhanJun on 7/23/15.
 */
@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class JijinExSellLimitRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {

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
    private JijinExSellLimitRepository jijinExSellLimitRepository;

    @Test@Ignore
    public void testInsertJijinExSellLimit() throws Exception {

        JijinExSellLimitDTO jijinExSellLimitDTO = new JijinExSellLimitDTO();
        jijinExSellLimitDTO.setFundCode(fundCode);
        jijinExSellLimitDTO.setBizCode(bizCode);
        jijinExSellLimitDTO.setSingleSellMaxAmount(new BigDecimal(100));
        jijinExSellLimitDTO.setSingleSellMinAmount(new BigDecimal(99));
        jijinExSellLimitDTO.setBatchId(batchId);
        jijinExSellLimitDTO.setStatus(status);
        JijinExSellLimitDTO jijinExSellLimitDTO1 = jijinExSellLimitRepository.insertJijinExSellLimit(jijinExSellLimitDTO);

        assertTrue(jijinExSellLimitDTO1.getId() > 0);
    }

    @Test@Ignore
    public void testUpdateJijinExSellLimit() throws Exception {
        Long testId = 1l;
        String updStatus = "INIT";
        List<JijinExSellLimitDTO> list = jijinExSellLimitRepository.getJijinExSellLimit(MapUtils.buildKeyValueMap("id", testId));
        JijinExSellLimitDTO jijinExSellLimitDTO = list.get(0);
        if (jijinExSellLimitDTO.getStatus().equals("INIT")) {
            updStatus = "COMPLETE";
        }
        Map map = new HashMap();
        map.put("id", testId);
        map.put("status", updStatus);
        int i = jijinExSellLimitRepository.updateJijinExSellLimit(map);
        assertTrue(i == 1);
    }

    @Test@Ignore
    public void testGetJijinExSellLimitByFundCodeAndBizCode() throws Exception {
        List<JijinExSellLimitDTO> list = jijinExSellLimitRepository.getJijinExSellLimitByFundCodeAndBizCode(fundCode, bizCode);
        assertTrue(list.size() > 0);
    }

    @Test@Ignore
    public void testGetUnDispachedJijinExSellLimit(){
        List<JijinExSellLimitDTO> unDispachedJijinExSellLimit = jijinExSellLimitRepository.getUnDispachedJijinExSellLimit(3);
        System.out.println("========="+unDispachedJijinExSellLimit.size());

        unDispachedJijinExSellLimit = jijinExSellLimitRepository.getUnDispachedJijinExSellLimit(100);
        System.out.println("========="+unDispachedJijinExSellLimit.size());
        assertTrue(unDispachedJijinExSellLimit.size() > 0);
    }


    @Test@Ignore
    public void testGetLatestBatchIdByFundCode(){
        Long latestBatchId = jijinExSellLimitRepository.getLatestBatchIdByFundAndBizCode("B00001", "24");
        System.out.println(latestBatchId);
        assertTrue(latestBatchId>0);
    }

}