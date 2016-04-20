package com.lufax.jijin.daixiao.repository;

import com.google.gson.Gson;
import com.lufax.jijin.base.utils.DateUtils;
import com.lufax.jijin.daixiao.dto.JijinExYieldRateDTO;

import com.lufax.jijin.daixiao.gson.JijinExYieldRateGson;
import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.assertTrue;

/**
 * Created by NiuZhanJun on 9/16/15.
 */
@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class JijinExYieldRateRepositoryTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    private JijinExYieldRateRepository jijinExYieldRateRepository;

    @Test
    @Ignore
    public void testInsertJijinExYieldRate() throws Exception {
        assertTrue(1 == 1);
    }

    @Test
    @Ignore
    public void testBatchInsertJijinExYieldRate() throws Exception {
        assertTrue(1 == 1);
    }

    @Test
    @Ignore
    public void testUpdateJijinExYieldRateStatus() throws Exception {
        assertTrue(1 == 1);
    }

    @Test
    @Ignore
    public void testCountNumberOfAfterDate() throws Exception {
        assertTrue(1 == 1);
    }

    @Test
    @Ignore
    public void testUpdateSameFundCodeDateRecordToNotVaild() throws Exception {
        assertTrue(1 == 1);
    }

    @Test
    public void testGetUnDispatchedJijinExYieldRate() throws Exception {
        assertTrue(1 == 1);
    }

    @Test
    @Ignore
    public void testGetLastestJijinExYieldRateByFundCode() throws Exception {
        assertTrue(1 == 1);
    }

    @Test@Ignore
    public void testGetYieldRateDtoByFundCodeAndEndDate() throws Exception {
        List<JijinExYieldRateDTO> list = jijinExYieldRateRepository.getYieldRateDtoByFundCodeAndEndDate("000917", "20150615");
        Map<String, JijinExYieldRateDTO> mapYieldRate = new HashMap<String, JijinExYieldRateDTO>();
        List<JijinExYieldRateGson> jijinExYieldRateAllGsons = new ArrayList<JijinExYieldRateGson>();
        List<JijinExYieldRateGson> tmpJijinExYieldRateGsons = new ArrayList<JijinExYieldRateGson>();

        for (JijinExYieldRateDTO jijinExYieldRateDTO : list) {
            if (!mapYieldRate.containsKey(jijinExYieldRateDTO.getEndDate())) {
                mapYieldRate.put(jijinExYieldRateDTO.getEndDate(), jijinExYieldRateDTO);
                JijinExYieldRateGson jijinExYieldRateGson = new JijinExYieldRateGson(jijinExYieldRateDTO);
                tmpJijinExYieldRateGsons.add(jijinExYieldRateGson);
            }
        }

        int totalCount=tmpJijinExYieldRateGsons.size();
        //重新排序
        for (int i=0;i<totalCount;i++){
            jijinExYieldRateAllGsons.add(tmpJijinExYieldRateGsons.get(totalCount - i - 1));
        }

        System.out.println(new Gson().toJson(list));
        System.out.println(list.size());
        System.out.println(new Gson().toJson(jijinExYieldRateAllGsons));
        System.out.println(jijinExYieldRateAllGsons.size());

        assertTrue(1 == 1);
    }

    @Test@Ignore
    public void testInsert() {
        Calendar calendarS = Calendar.getInstance();
        calendarS.setTime(DateUtils.parseDate("2015-05-01"));
        Calendar calendarE = Calendar.getInstance();
        calendarE.setTime(DateUtils.parseDate("2015-06-01"));
        for (int i = 0; i < 100; i++) {
            calendarS.add(Calendar.DAY_OF_YEAR, 1);
            calendarE.add(Calendar.DAY_OF_YEAR, 1);
            String startDate = DateUtils.formatDateAsString(calendarS.getTime());
            String endDate = DateUtils.formatDateAsString(calendarE.getTime());
            JijinExYieldRateDTO dto = new JijinExYieldRateDTO();
            dto.setBenefitPerTenthousand(new BigDecimal("1.535"));
            dto.setEndDate(endDate);
            dto.setFundCode("000917");
            dto.setInterestratePerSevenday(new BigDecimal("5.5"));
            dto.setNoticeDate(endDate);
            dto.setStartDate(startDate);
            dto.setBatchId(112l);
            dto.setIsValid(1);
            dto.setStatus("NEW");
            jijinExYieldRateRepository.insertJijinExYieldRate(dto);
        }

    }

    @Test@Ignore
    public void testGetJijinExNetValueByFundCode() {
        int count = jijinExYieldRateRepository.countNumberOfAfterDate("000917", "20150615");
        Assert.assertTrue(count > 0);
        JijinExYieldRateDTO dto = jijinExYieldRateRepository.getLastestJijinExYieldRateByFundCode("000917");
        Assert.assertTrue(null != dto && "20150917".equals(dto.getEndDate()));
    }

}