package com.lufax.jijin.daixiao.repository;

import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.daixiao.dto.JijinExBuyLimitDTO;

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
public class JijinExBuyLimitRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {

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
    private JijinExBuyLimitRepository jijinExBuyLimitRepository;

    @Test@Ignore
    public void testInsertJijinExBuyLimit() throws Exception {

        JijinExBuyLimitDTO jijinExBuyLimitDTO = new JijinExBuyLimitDTO();
        jijinExBuyLimitDTO.setFundCode(fundCode);
        jijinExBuyLimitDTO.setBizCode(bizCode);
        jijinExBuyLimitDTO.setFirstInvestMaxAmount(new BigDecimal(100));
        jijinExBuyLimitDTO.setFirstInvestMinAmount(new BigDecimal(100));
        jijinExBuyLimitDTO.setAddInvestMaxAmount(new BigDecimal(100));
        jijinExBuyLimitDTO.setAddInvestMinAmount(new BigDecimal(100));
        jijinExBuyLimitDTO.setInvestDailyLimit(new BigDecimal(100));
        jijinExBuyLimitDTO.setSingleInvestMaxAmount(new BigDecimal(100));
        jijinExBuyLimitDTO.setSingleInvestMinAmount(new BigDecimal(100));
        jijinExBuyLimitDTO.setRaisedAmount(new BigDecimal(100));
        jijinExBuyLimitDTO.setBatchId(batchId);
        jijinExBuyLimitDTO.setStatus(status);
        JijinExBuyLimitDTO jijinExBuyLimitDTO1 = jijinExBuyLimitRepository.insertJijinExBuyLimit(jijinExBuyLimitDTO);

        assertTrue(jijinExBuyLimitDTO1.getId() > 0);
    }

    @Test@Ignore
    public void testUpdateJijinExBuyLimit() throws Exception {
        Long testId = 2l;
        String updStatus = "INIT";
        List<JijinExBuyLimitDTO> list = jijinExBuyLimitRepository.getJijinExBuyLimit(MapUtils.buildKeyValueMap("id", testId));
        JijinExBuyLimitDTO jijinExBuyLimitDTO = list.get(0);
        if (jijinExBuyLimitDTO.getStatus().equals("INIT")) {
            updStatus = "COMPLETE";
        }
        Map map = new HashMap();
        map.put("id", testId);
        map.put("status", updStatus);
        int i = jijinExBuyLimitRepository.updateJijinExBuyLimit(map);
        assertTrue(i == 1);
    }

    @Test@Ignore
    public void testGetJijinExBuyLimitByFundCodeAndBizCode() throws Exception {
        List<JijinExBuyLimitDTO> list = jijinExBuyLimitRepository.getJijinExBuyLimitByFundCodeAndBizCode(fundCode, bizCode);
        assertTrue(list.size() > 0);
    }

    @Test@Ignore
    public void testGetLatestBatchIdByDate(){
        Long batchId = jijinExBuyLimitRepository.getLatestBatchIdByDate(fundCode, bizCode);
        List<JijinExBuyLimitDTO> list = jijinExBuyLimitRepository.getJijinExBuyLimitByBatchIdAndFundCode(batchId,fundCode,"20");
        assertTrue(list.size() > 0);
    }

    @Test@Ignore
    public void testGetUnDispachedJijinExBuyLimit(){
        List<JijinExBuyLimitDTO> unDispachedJijinExBuyLimit = jijinExBuyLimitRepository.getUnDispachedJijinExBuyLimit(3);
        System.out.println("========="+unDispachedJijinExBuyLimit.size());
        assertTrue(unDispachedJijinExBuyLimit.size() > 0);
    }
}