package com.lufax.jijin.daixiao.repository;

import com.lufax.jijin.base.utils.DateUtils;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.daixiao.dto.JijinExFeeDTO;

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
public class JijinExFeeRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {

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
    private JijinExFeeRepository jijinExFeeRepository;

    @Test@Ignore
    public void testInsertJijinExFee() throws Exception {

        JijinExFeeDTO jijinExFeeDTO = new JijinExFeeDTO();
        jijinExFeeDTO.setFundCode(fundCode);
        jijinExFeeDTO.setFeeType("1");
        jijinExFeeDTO.setChargeType("1");
        jijinExFeeDTO.setUpperLimitAmount(new BigDecimal(100));
        jijinExFeeDTO.setLowerLimitAmount(new BigDecimal(101));
        jijinExFeeDTO.setLowerLimitHoldYear(new BigDecimal(1));
        jijinExFeeDTO.setUpperLimitHoldYear(new BigDecimal(2));
        jijinExFeeDTO.setFee(new BigDecimal(102));
        jijinExFeeDTO.setChangeDate("20150707");
        jijinExFeeDTO.setBatchId(batchId);
        jijinExFeeDTO.setStatus(status);
        JijinExFeeDTO jijinExFeeDTO1 = jijinExFeeRepository.insertJijinExFee(jijinExFeeDTO);

        assertTrue(jijinExFeeDTO1.getId() > 0);
    }

    @Test@Ignore
    public void testUpdateJijinExFee() throws Exception {
        Long testId = 1l;
        String updStatus = "INIT";
        List<JijinExFeeDTO> list = jijinExFeeRepository.getJijinExFee(MapUtils.buildKeyValueMap("id", testId));
        JijinExFeeDTO jijinExFeeDTO = list.get(0);
        if (jijinExFeeDTO.getStatus().equals("INIT")) {
            updStatus = "COMPLETE";
        }
        Map map = new HashMap();
        map.put("id", testId);
        map.put("status", updStatus);
        int i = jijinExFeeRepository.updateJijinExFee(map);
        assertTrue(i == 1);
    }

    @Test@Ignore
    public void testGetJijinExFeeByFundCodeAndBizCode() throws Exception {
        List<JijinExFeeDTO> list = jijinExFeeRepository.getJijinExFeeByFundCode(fundCode);
        assertTrue(list.size() > 0);
    }

    @Test@Ignore
    public void testGetLatestBatchIdByDate() {
        fundCode = "B00001";
        bizCode = "2";
        String currentDate = "20150706";
        String result = "";

        Long exFeeBatchId = jijinExFeeRepository.getLatestBatchIdByDate(fundCode, bizCode, currentDate);
        System.out.println("exFeeBatchId=" + exFeeBatchId);
        //List<JijinExFeeDTO> list = jijinExFeeRepository.getJijinExFeeByBatchIdAndFundCodeAndBizCode(exFeeBatchId,fundCode,bizCode);
        result += exFeeBatchId.compareTo(20150731165905l) == 0 ? "1" : "0";

        exFeeBatchId = jijinExFeeRepository.getLatestBatchIdByDate(fundCode, "", "");
        System.out.println("exFeeBatchId=" + exFeeBatchId);
        result += exFeeBatchId.compareTo(20150803145010l) == 0 ? "1" : "0";

        exFeeBatchId = jijinExFeeRepository.getLatestBatchIdByDate(fundCode, bizCode, "");
        System.out.println("exFeeBatchId=" + exFeeBatchId);
        result += exFeeBatchId.compareTo(20150801180212l) == 0 ? "1" : "0";

        System.out.println(result);
        assertTrue("111".equals(result));

    }

    @Test@Ignore
    public void testGetUnDispachedJijinExFee() {
        List<JijinExFeeDTO> unDispachedJijinExFee = jijinExFeeRepository.getUnDispachedJijinExFee(3);
        System.out.println("=========" + unDispachedJijinExFee.size());

        unDispachedJijinExFee = jijinExFeeRepository.getUnDispachedJijinExFee(100);
        System.out.println("=========" + unDispachedJijinExFee.size());
        assertTrue(unDispachedJijinExFee.size() > 0);
    }
}