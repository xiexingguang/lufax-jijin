package com.lufax.jijin.daixiao.repository;

import com.lufax.jijin.base.utils.DateUtils;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.daixiao.dto.JijinExDiscountDTO;

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

import static org.junit.Assert.assertTrue;

/**
 * Created by NiuZhanJun on 7/23/15.
 */
@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class JijinExDiscountRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {

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
    private JijinExDiscountRepository jijinExDiscountRepository;

    @Test@Ignore
    public void testInsertJijinExDiscount() throws Exception {

        JijinExDiscountDTO jijinExDiscountDTO = new JijinExDiscountDTO();
        jijinExDiscountDTO.setFundCode(fundCode);
        jijinExDiscountDTO.setBizCode(bizCode);
        jijinExDiscountDTO.setValidDate("20150831");
        jijinExDiscountDTO.setDiscountMode("0");
        jijinExDiscountDTO.setFeePloy("1");
        jijinExDiscountDTO.setFixedMaxAmount(new BigDecimal(100));
        jijinExDiscountDTO.setFixedMinAmount(new BigDecimal(100));
        jijinExDiscountDTO.setFixedRate(new BigDecimal(0.8));
        jijinExDiscountDTO.setSetType("1");
        jijinExDiscountDTO.setSetValue(new BigDecimal(102));
        jijinExDiscountDTO.setBatchId(batchId);
        jijinExDiscountDTO.setStatus(status);
        JijinExDiscountDTO jijinExDiscountDTO1 = jijinExDiscountRepository.insertJijinExDiscount(jijinExDiscountDTO);

        assertTrue(jijinExDiscountDTO1.getId() > 0);
    }

    @Test@Ignore
    public void testUpdateJijinExDiscount() throws Exception {
        Long testId = 1l;
        String updStatus = "INIT";
        List<JijinExDiscountDTO> list = jijinExDiscountRepository.getJijinExDiscount(MapUtils.buildKeyValueMap("id", testId));
        JijinExDiscountDTO jijinExDiscountDTO = list.get(0);
        if (jijinExDiscountDTO.getStatus().equals("INIT")) {
            updStatus = "COMPLETE";
        }
        Map map = new HashMap();
        map.put("id", testId);
        map.put("status", updStatus);
        int i = jijinExDiscountRepository.updateJijinExDiscount(map);
        assertTrue(i == 1);
    }

    @Test@Ignore
    public void testGetJijinExDiscountByFundCodeAndBizCode() throws Exception {
        List<JijinExDiscountDTO> list = jijinExDiscountRepository.getJijinExDiscountByFundCodeAndBizCode(fundCode, bizCode);
        assertTrue(list.size() > 0);
    }

    @Test@Ignore
    public void testGetLatestBatchIdByDate() {
        fundCode = "B00001";
        bizCode = "22";
        String dateStr = "20150830";
        String result = "";
        Long batchId = jijinExDiscountRepository.getLatestBatchIdByDate(fundCode, bizCode, dateStr);
        System.out.println("batchId=" + batchId);
        result += batchId.compareTo(20150801120811l) == 0 ? "1" : "0";
        //List<JijinExDiscountDTO> list = jijinExDiscountRepository.getJijinExDiscountsByBatchId(batchId);

        batchId = jijinExDiscountRepository.getLatestBatchIdByDate(fundCode, bizCode, "");
        System.out.println("batchId=" + batchId);
        result += batchId.compareTo(20150801180212l) == 0 ? "1" : "0";

        batchId = jijinExDiscountRepository.getLatestBatchIdByDate(fundCode, "", "");
        System.out.println("batchId=" + batchId);
        result += batchId.compareTo(20150803145010l) == 0 ? "1" : "0";

        System.out.println(result);
        assertTrue("111".equals(result));
    }

    @Test@Ignore
    public void testGetUnDispachedJijinExDiscount(){
        List<JijinExDiscountDTO> unDispachedJijinExDiscount = jijinExDiscountRepository.getUnDispachedJijinExDiscount(3);
        System.out.println("========="+unDispachedJijinExDiscount.size());

        unDispachedJijinExDiscount = jijinExDiscountRepository.getUnDispachedJijinExDiscount(100);
        System.out.println("========="+unDispachedJijinExDiscount.size());
        assertTrue(unDispachedJijinExDiscount.size() > 0);
    }

}