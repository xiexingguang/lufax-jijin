package com.lufax.jijin.daixiao.repository;

import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.daixiao.dto.JijinExIncomeModeDTO;

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
public class JijinExIncomeModeRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {

    private Long batchId = 0l;
    private String status = "";
    private String fundCode = "B00001";

    @Before
    public void setUp() throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        batchId = Long.valueOf(simpleDateFormat.format(new Date()));
        status = "NEW";
    }

    @Autowired
    private JijinExIncomeModeRepository jijinExIncomeModeRepository;

    @Test@Ignore
    public void testInsertJijinExIncomeMode() throws Exception {

        JijinExIncomeModeDTO jijinExIncomeModeDTO = new JijinExIncomeModeDTO();
        jijinExIncomeModeDTO.setFundCode(fundCode);
        jijinExIncomeModeDTO.setIncomeMode("1");
        jijinExIncomeModeDTO.setMinHoldShareCount(new BigDecimal(100));
        jijinExIncomeModeDTO.setBatchId(batchId);
        jijinExIncomeModeDTO.setStatus(status);
        JijinExIncomeModeDTO jijinExIncomeModeDTO1 = jijinExIncomeModeRepository.insertJijinExIncomeMode(jijinExIncomeModeDTO);

        assertTrue(jijinExIncomeModeDTO1.getId() > 0);
    }

    @Test@Ignore
    public void testUpdateJijinExIncomeMode() throws Exception {
        Long testId = 1l;
        String updStatus = "INIT";
        List<JijinExIncomeModeDTO> list = jijinExIncomeModeRepository.getJijinExIncomeMode(MapUtils.buildKeyValueMap("id", testId));
        JijinExIncomeModeDTO jijinExIncomeModeDTO = list.get(0);
        if (jijinExIncomeModeDTO.getStatus().equals("INIT")) {
            updStatus = "COMPLETE";
        }
        Map map = new HashMap();
        map.put("id", testId);
        map.put("status", updStatus);
        int i = jijinExIncomeModeRepository.updateJijinExIncomeMode(map);
        assertTrue(i == 1);
    }

    @Test@Ignore
    public void testGetJijinExIncomeModeByFundCodeAndBizCode() throws Exception {
        List<JijinExIncomeModeDTO> list = jijinExIncomeModeRepository.getJijinExIncomeModeByFundCode(fundCode);
        assertTrue(list.size() > 0);
    }

    @Test@Ignore
    public void testGetUnDispachedJijinExIncomeMode(){
        List<JijinExIncomeModeDTO> unDispachedJijinExIncomeMode = jijinExIncomeModeRepository.getUnDispachedJijinExIncomeMode(3);
        System.out.println("========="+unDispachedJijinExIncomeMode.size());

        unDispachedJijinExIncomeMode = jijinExIncomeModeRepository.getUnDispachedJijinExIncomeMode(100);
        System.out.println("========="+unDispachedJijinExIncomeMode.size());
        assertTrue(unDispachedJijinExIncomeMode.size() > 0);
    }

    @Test@Ignore
    public void testGetLatestBatchIdByFundCode(){
        Long latestBatchId = jijinExIncomeModeRepository.getLatestBatchIdByFundCode("B00001");
        System.out.println(latestBatchId);
        assertTrue(latestBatchId>0);
    }
}