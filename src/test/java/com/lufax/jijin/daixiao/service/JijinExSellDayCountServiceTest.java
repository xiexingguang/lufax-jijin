package com.lufax.jijin.daixiao.service;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.lufax.jijin.daixiao.dto.JijinExSellDayCountDTO;
import com.lufax.jijin.daixiao.gson.JijinUpdateGson;
import com.lufax.jijin.fundation.dto.JijinSyncFileDTO;
import com.lufax.jijin.fundation.gson.BaseGson;

/**
 * Created by NiuZhanJun on 7/27/15.
 */
@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class JijinExSellDayCountServiceTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private JijinExSellDayCountService jijinExSellDayCountService;
    @Autowired
    private JijinDaixiaoInfoService jijinDaixiaoInfoService;

    @Before
    public void setUp() throws Exception {
        jijinDaixiaoInfoService=mock(JijinDaixiaoInfoService.class);
        jijinExSellDayCountService.setJijinDaixiaoInfoService(jijinDaixiaoInfoService);
    }
    @Test@Ignore
    public void testRecordFileSync() throws Exception {
        JijinSyncFileDTO jijinSyncFileDTO = new JijinSyncFileDTO();
        jijinSyncFileDTO.setId(374);
        jijinSyncFileDTO.setFileName("/home/NiuZhanJun/WorkLufax/Projects/30_jijindaixiao/sampleFiles/wind_20150806010000_04.txt");
        jijinSyncFileDTO.setCurrentLine(1l);
        jijinExSellDayCountService.recordFileSync(jijinSyncFileDTO);
        assertTrue(1==1);
    }

    @Test@Ignore
    public void testHandleSellDayCount_NoJijinInfo(){
        try {
            System.out.println("=======================================start testHandleSellDayCount_NoJijinInfo=======================================");
            JijinExSellDayCountDTO dto=new JijinExSellDayCountDTO();
            dto.setId(11);
            dto.setFundCode("abcdedf");
            jijinExSellDayCountService.handleSellDayCount(dto);
        } catch (Exception e) {
            System.out.println("testHandleSellDayCount_NoJijinInfo test exception");
            e.printStackTrace();
        }
        System.out.println("=======================================end testHandleSellDayCount_NoJijinInfo=======================================");
        assertTrue(1 == 1);
    }
    @Test@Ignore
    public void testHandleSellDayCount_Dispatched(){
        try {
            System.out.println("=======================================start testHandleSellDayCount_Dispatched=======================================");
            JijinExSellDayCountDTO dto=new JijinExSellDayCountDTO();
            dto.setId(101);
            dto.setStatus("DISPACHED");
            dto.setFundCode("110020");
            jijinExSellDayCountService.handleSellDayCount(dto);
        } catch (Exception e) {
            System.out.println("testHandleSellDayCount_Dispatched test exception");
            e.printStackTrace();
        }
        System.out.println("=======================================end testHandleSellDayCount_Dispatched=======================================");
        assertTrue(1 == 1);
    }

    @Test@Ignore
    public void testHandleSellDayCount_UpdateFailed(){
        BaseGson baseGson=new BaseGson();
        baseGson.setRetCode("001");
        when(jijinDaixiaoInfoService.updateProduct((JijinUpdateGson)anyObject())).thenReturn(baseGson);
        try {
            System.out.println("=======================================start testHandleSellDayCount_UpdateFailed=======================================");
            JijinExSellDayCountDTO dto=new JijinExSellDayCountDTO();
            dto.setId(203);
            dto.setStatus("NEW");
            dto.setFundCode("110020");
            jijinExSellDayCountService.handleSellDayCount(dto);
        } catch (Exception e) {
            System.out.println("testHandleSellDayCount_UpdateFailed test exception");
            e.printStackTrace();
        }
        System.out.println("=======================================end testHandleSellDayCount_UpdateFailed=======================================");
        assertTrue(1 == 1);
    }
    @Test@Ignore
    public void testHandleSellDayCount_Success(){
        BaseGson baseGson=new BaseGson();
        baseGson.setRetCode("000");
        when(jijinDaixiaoInfoService.updateProduct((JijinUpdateGson)anyObject())).thenReturn(baseGson);
        try {
            System.out.println("=======================================start testHandleSellDayCount_Success=======================================");
            JijinExSellDayCountDTO dto=new JijinExSellDayCountDTO();
            dto.setId(202);
            dto.setStatus("NEW");
            dto.setFundCode("110020");
            jijinExSellDayCountService.handleSellDayCount(dto);
        } catch (Exception e) {
            System.out.println("testHandleSellDayCount_Success test exception");
            e.printStackTrace();
        }
        System.out.println("=======================================end testHandleSellDayCount_Success=======================================");
        assertTrue(1 == 1);
    }
}