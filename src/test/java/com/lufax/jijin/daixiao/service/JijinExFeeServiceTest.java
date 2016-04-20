package com.lufax.jijin.daixiao.service;

import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.daixiao.dto.JijinExFeeDTO;
import com.lufax.jijin.daixiao.gson.JijinProductFeeGson;
import com.lufax.jijin.fundation.dto.JijinSyncFileDTO;
import com.lufax.jijin.fundation.gson.BaseGson;
import com.lufax.jijin.fundation.repository.JijinSyncFileRepository;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by NiuZhanJun on 7/27/15.
 */
@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class JijinExFeeServiceTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private JijinExFeeService jijinExFeeService;
    @Autowired
    private JijinDaixiaoInfoService jijinDaixiaoInfoService;
    @Autowired
    private JijinSyncFileRepository jijinSyncFileRepository;

    @Before
    public void setUp() throws Exception {
        /*jijinDaixiaoInfoService=mock(JijinDaixiaoInfoService.class);
        jijinExFeeService.setJijinDaixiaoInfoService(jijinDaixiaoInfoService);*/
    }
    @Test@Ignore
    public void testRecordFileSync() throws Exception {
        JijinSyncFileDTO jijinSyncFileDTO = new JijinSyncFileDTO();
        jijinSyncFileDTO.setId(999);
        jijinSyncFileDTO=jijinSyncFileRepository.findBusJijinSyncFile(MapUtils.buildKeyValueMap("id",678));
        jijinSyncFileDTO.setFileName("/home/NiuZhanJun/WorkLufax/Projects/30_jijindaixiao/sampleFiles/wind_20150806010000_05.txt");
        jijinSyncFileDTO.setCurrentLine(1l);
        jijinSyncFileDTO.setStatus("READY");
        jijinExFeeService.recordFileSync(jijinSyncFileDTO);
        assertTrue(1==1);
    }

    @Test@Ignore
    public void testHandleFee_NoJijinInfo(){
        try {
            System.out.println("=======================================start testHandleFee_NoJijinInfo=======================================");
            JijinExFeeDTO dto=new JijinExFeeDTO();
            dto.setId(11);
            dto.setFundCode("abcdedf");
            jijinExFeeService.handleFee(dto);
        } catch (Exception e) {
            System.out.println("testHandleFee_NoJijinInfo test exception");
            e.printStackTrace();
        }
        System.out.println("=======================================end testHandleFee_NoJijinInfo=======================================");
        assertTrue(1 == 1);
    }
    @Test@Ignore
    public void testHandleFee_Dispatched(){
        try {
            System.out.println("=======================================start testHandleFee_Dispatched=======================================");
            JijinExFeeDTO dto=new JijinExFeeDTO();
            dto.setId(101);
            dto.setStatus("DISPACHED");
            dto.setFundCode("110020");
            jijinExFeeService.handleFee(dto);
        } catch (Exception e) {
            System.out.println("testHandleFee_Dispatched test exception");
            e.printStackTrace();
        }
        System.out.println("=======================================end testHandleFee_Dispatched=======================================");
        assertTrue(1 == 1);
    }

    @Test@Ignore
    public void testHandleFee_UpdateFailed(){
        BaseGson baseGson=new BaseGson();
        baseGson.setRetCode("001");
       // when(jijinDaixiaoInfoService.sendFeeToProduct((List<JijinProductFeeGson>) anyObject())).thenReturn(baseGson);
        try {
            System.out.println("=======================================start testHandleFee_UpdateFailed=======================================");
            JijinExFeeDTO dto=new JijinExFeeDTO();
            dto.setId(203);
            dto.setStatus("NEW");
            dto.setFundCode("110020");
            jijinExFeeService.handleFee(dto);
        } catch (Exception e) {
            System.out.println("testHandleFee_UpdateFailed test exception");
            e.printStackTrace();
        }
        System.out.println("=======================================end testHandleFee_UpdateFailed=======================================");
        assertTrue(1 == 1);
    }
    @Test@Ignore
    public void testHandleFee_Success(){
        BaseGson baseGson=new BaseGson();
        baseGson.setRetCode("000");
        //when(jijinDaixiaoInfoService.sendFeeToProduct((List<JijinProductFeeGson>) anyObject())).thenReturn(baseGson);
        try {
            System.out.println("=======================================start testHandleFee_Success=======================================");
            JijinExFeeDTO dto=new JijinExFeeDTO();
            dto.setId(601);
            dto.setStatus("NEW");
            dto.setFundCode("110020");
            jijinExFeeService.handleFee(dto);
        } catch (Exception e) {
            System.out.println("testHandleFee_Success test exception");
            e.printStackTrace();
        }
        System.out.println("=======================================end testHandleFee_Success=======================================");
        assertTrue(1 == 1);
    }
}