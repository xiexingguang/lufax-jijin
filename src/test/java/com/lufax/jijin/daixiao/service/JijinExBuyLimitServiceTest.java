package com.lufax.jijin.daixiao.service;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.google.gson.Gson;
import com.lufax.jijin.daixiao.dto.JijinExBuyLimitDTO;
import com.lufax.jijin.daixiao.gson.JijinUpdateGson;
import com.lufax.jijin.daixiao.repository.JijinExBuyLimitRepository;
import com.lufax.jijin.fundation.dto.JijinInfoDTO;
import com.lufax.jijin.fundation.dto.JijinSyncFileDTO;
import com.lufax.jijin.fundation.gson.BaseGson;
import com.lufax.jijin.fundation.repository.JijinSyncFileRepository;

/**
 * Created by NiuZhanJun on 7/27/15.
 */
@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class JijinExBuyLimitServiceTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    private JijinExBuyLimitService jijinExBuyLimitService;
    @Autowired
    private JijinExBuyLimitRepository jijinExBuyLimitRepository;
    @Autowired
    private JijinSyncFileRepository jijinSyncFielRepository;
    @Autowired
    private JijinDaixiaoInfoService jijinDaixiaoInfoService;


    @Before
    public void setUp() throws Exception {

        jijinDaixiaoInfoService=mock(JijinDaixiaoInfoService.class);
        jijinExBuyLimitService.setJijinDaixiaoInfoService(jijinDaixiaoInfoService);
    }

    @Test@Ignore
    public void testRecordFileSync() throws Exception {

        JijinSyncFileDTO jijinSyncFileDTO = new JijinSyncFileDTO();
        jijinSyncFileDTO.setId(378);
        jijinSyncFileDTO.setFileName("/home/NiuZhanJun/WorkLufax/Projects/30_jijindaixiao/sampleFiles/wind_20150806010000_01.txt");
        jijinSyncFileDTO.setCurrentLine(1l);
        jijinExBuyLimitService.recordFileSync(jijinSyncFileDTO);
        assertTrue(1 == 1);
    }

    @Test@Ignore
    public void testHandleBuyLimit_NoJijinInfo(){
        try {
            System.out.println("=============start testHandleBuyLimit_NoJijinInfo=============");
            JijinExBuyLimitDTO dto=new JijinExBuyLimitDTO();
            dto.setId(101);
            dto.setFundCode("abcdedf");
            jijinExBuyLimitService.handleBuyLimit(dto);
        } catch (Exception e) {
            System.out.println("testHandleBuyLimit_NoJijinInfo test exception");
            e.printStackTrace();
        }
        assertTrue(1 == 1);
    }
    @Test@Ignore
    public void testHandleBuyLimit_Dispatched(){
        try {
            System.out.println("=============start testHandleBuyLimit_Dispatched=============");
            JijinExBuyLimitDTO dto=new JijinExBuyLimitDTO();
            dto.setId(23);
            dto.setStatus("DISPACHED");
            dto.setFundCode("110020");
            jijinExBuyLimitService.handleBuyLimit(dto);
        } catch (Exception e) {
            System.out.println("testHandleBuyLimit_Dispatched test exception");
            e.printStackTrace();
        }
        assertTrue(1 == 1);
    }

    @Test@Ignore
    public void testHandleBuyLimit_NotBuyBizCode(){
        try {
            System.out.println("=============start testHandleBuyLimit_NotBuyBizCode=============");
            JijinExBuyLimitDTO dto=new JijinExBuyLimitDTO();
            dto.setId(201);
            dto.setStatus("NEW");
            dto.setFundCode("110020");
            jijinExBuyLimitService.handleBuyLimit(dto);
        } catch (Exception e) {
            System.out.println("testHandleBuyLimit_NotBuyBizCode test exception");
            e.printStackTrace();
        }
        assertTrue(1 == 1);
    }
    @Test@Ignore
    public void testHandleBuyLimit_UpdateFailed(){
        BaseGson baseGson=new BaseGson();
        baseGson.setRetCode("001");
        when(jijinDaixiaoInfoService.updateProduct((JijinUpdateGson)anyObject())).thenReturn(baseGson);
        try {
            System.out.println("=============start testHandleBuyLimit_UpdateFailed=============");
            JijinExBuyLimitDTO dto=new JijinExBuyLimitDTO();
            dto.setId(2);
            dto.setStatus("NEW");
            dto.setFundCode("001468");
            jijinExBuyLimitService.handleBuyLimit(dto);
        } catch (Exception e) {
            System.out.println("testHandleBuyLimit_UpdateFailed test exception");
            e.printStackTrace();
        }
        assertTrue(1 == 1);
    }
    @Test@Ignore
    public void testHandleBuyLimit_Success(){
        BaseGson baseGson=new BaseGson();
        baseGson.setRetCode("000");
        when(jijinDaixiaoInfoService.updateProduct((JijinUpdateGson)anyObject())).thenReturn(baseGson);
        try {
            System.out.println("=============start testHandleBuyLimit_Success=============");
            JijinExBuyLimitDTO dto=new JijinExBuyLimitDTO();
            dto.setId(202);
            dto.setStatus("NEW");
            dto.setFundCode("110020");
            jijinExBuyLimitService.handleBuyLimit(dto);
        } catch (Exception e) {
            System.out.println("testHandleBuyLimit_Success test exception");
            e.printStackTrace();
        }
        assertTrue(1 == 1);
    }

    @Test@Ignore
    public void testAddToJijinInfoDto(){
        JijinExBuyLimitDTO jijinExBuyLimitDTO=new JijinExBuyLimitDTO();
        JijinInfoDTO jijinInfoDTO=new JijinInfoDTO();
        jijinInfoDTO.setChargeType("B");
        jijinExBuyLimitDTO.setInvestDailyLimit(BigDecimal.TEN);
        jijinExBuyLimitDTO.setSingleInvestMaxAmount(new BigDecimal(88888888));
        jijinExBuyLimitDTO.setSingleInvestMinAmount(BigDecimal.ONE);
        jijinExBuyLimitDTO.setRaisedAmount(BigDecimal.TEN);
        jijinInfoDTO=jijinExBuyLimitService.addToJijinInfoDto(jijinInfoDTO,null);
        System.out.println(new Gson().toJson(jijinInfoDTO));
    }
}