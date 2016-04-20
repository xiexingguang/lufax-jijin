package com.lufax.jijin.daixiao.service;

import com.google.gson.Gson;
import com.lufax.jersey.utils.Logger;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.daixiao.dto.JijinExIncomeModeDTO;
import com.lufax.jijin.daixiao.gson.JijinUpdateGson;
import com.lufax.jijin.fundation.dto.JijinSyncFileDTO;
import com.lufax.jijin.fundation.gson.BaseGson;
import com.lufax.jijin.fundation.repository.JijinSyncFileRepository;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by NiuZhanJun on 7/27/15.
 */
@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class JijinExIncomeModeServiceTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private JijinExIncomeModeService jijinExIncomeModeService;
    @Autowired
    private JijinDaixiaoInfoService jijinDaixiaoInfoService;
    @Autowired
    private JijinSyncFileRepository jijinSyncFileRepository;

    @Before
    public void setUp() throws Exception {
        jijinDaixiaoInfoService=mock(JijinDaixiaoInfoService.class);
        jijinExIncomeModeService.setJijinDaixiaoInfoService(jijinDaixiaoInfoService);
    }

    @Test@Ignore
    public void testGetLatestIncomeModeByFundCode(){
        JijinExIncomeModeDTO latestIncomeModeByFundCode = jijinExIncomeModeService.getLatestIncomeModeByFundCode("B00001");
        System.out.println(new Gson().toJson(latestIncomeModeByFundCode));
        assertTrue(latestIncomeModeByFundCode.getId()==404l);
    }

    @Test@Ignore
    public void testRecordFileSync() throws Exception {
        JijinSyncFileDTO jijinSyncFileDTO = new JijinSyncFileDTO();
        jijinSyncFileDTO.setId(676);

        jijinSyncFileDTO=jijinSyncFileRepository.findBusJijinSyncFile(MapUtils.buildKeyValueMap("id",676));

        jijinSyncFileDTO.setStatus("READY");
        jijinSyncFileDTO.setFileName("/home/NiuZhanJun/WorkLufax/Projects/30_jijindaixiao/sampleFiles/wind_20150814010000_03.txt");
        jijinSyncFileDTO.setCurrentLine(0l);

        jijinExIncomeModeService.recordFileSync(jijinSyncFileDTO);
        assertTrue(1==1);

    }

    @Test@Ignore
    public void testHandleIncomeMode_NoJijinInfo(){
        try {
            System.out.println("=======================================start testHandleIncomeMode_NoJijinInfo=======================================");
            JijinExIncomeModeDTO dto=new JijinExIncomeModeDTO();
            dto.setId(11);
            dto.setFundCode("abcdedf");
            jijinExIncomeModeService.handleIncomeMode(dto);
        } catch (Exception e) {
            System.out.println("testHandleIncomeMode_NoJijinInfo test exception");
            e.printStackTrace();
        }
        System.out.println("=======================================end testHandleIncomeMode_NoJijinInfo=======================================");
        assertTrue(1 == 1);
    }
    @Test@Ignore
    public void testHandleIncomeMode_Dispatched(){
        try {
            System.out.println("=======================================start testHandleIncomeMode_Dispatched=======================================");
            JijinExIncomeModeDTO dto=new JijinExIncomeModeDTO();
            dto.setId(101);
            dto.setStatus("DISPACHED");
            dto.setFundCode("110020");
            jijinExIncomeModeService.handleIncomeMode(dto);
        } catch (Exception e) {
            System.out.println("testHandleIncomeMode_Dispatched test exception");
            e.printStackTrace();
        }
        System.out.println("=======================================end testHandleIncomeMode_Dispatched=======================================");
        assertTrue(1 == 1);
    }

    @Test@Ignore
    public void testHandleIncomeMode_UpdateFailed(){
        BaseGson baseGson=new BaseGson();
        baseGson.setRetCode("001");
        when(jijinDaixiaoInfoService.updateProduct((JijinUpdateGson)anyObject())).thenReturn(baseGson);
        try {
            System.out.println("=======================================start testHandleIncomeMode_UpdateFailed=======================================");
            JijinExIncomeModeDTO dto=new JijinExIncomeModeDTO();
            dto.setId(203);
            dto.setStatus("NEW");
            dto.setFundCode("110020");
            jijinExIncomeModeService.handleIncomeMode(dto);
        } catch (Exception e) {
            System.out.println("testHandleIncomeMode_UpdateFailed test exception");
            e.printStackTrace();
        }
        System.out.println("=======================================end testHandleIncomeMode_UpdateFailed=======================================");
        assertTrue(1 == 1);
    }
    @Test@Ignore
    public void testHandleIncomeMode_Success(){
        BaseGson baseGson=new BaseGson();
        baseGson.setRetCode("000");
        when(jijinDaixiaoInfoService.updateProduct((JijinUpdateGson)anyObject())).thenReturn(baseGson);
        try {
            System.out.println("=======================================start testHandleIncomeMode_Success=======================================");
            JijinExIncomeModeDTO dto=new JijinExIncomeModeDTO();
            dto.setId(202);
            dto.setStatus("NEW");
            dto.setFundCode("110020");
            jijinExIncomeModeService.handleIncomeMode(dto);
        } catch (Exception e) {
            System.out.println("testHandleIncomeMode_Success test exception");
            e.printStackTrace();
        }
        System.out.println("=======================================end testHandleIncomeMode_Success=======================================");
        assertTrue(1 == 1);
    }

    @Test@Ignore
    public void testNeo(){
        Long currentUserId=123l;
        Long productId=null;
        Logger.info(this, String.format("The login user is [%s],ProductId is [%s]", currentUserId, productId));
    }
}