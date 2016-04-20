package com.lufax.jijin.daixiao.service;

import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.daixiao.constant.DaiXiaoFileTypeEnum;
import com.lufax.jijin.daixiao.dto.JijinExDiscountDTO;
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

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.contains;
import static org.mockito.Mockito.when;

/**
 * Created by NiuZhanJun on 7/27/15.
 */
@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class JijinExDiscountServiceTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private JijinExDiscountService jijinExDiscountService;

    @Autowired
    private JijinDaixiaoInfoService jijinDaixiaoInfoService;

    @Autowired
    private JijinSyncFileRepository jijinSyncFileRepository;

    @Before
    public void setUp() throws Exception {
        /*jijinDaixiaoInfoService=mock(JijinDaixiaoInfoService.class);
        jijinExDiscountService.setJijinDaixiaoInfoService(jijinDaixiaoInfoService);*/
    }

    @Test@Ignore
    public void testRecordFileSync() throws Exception {
        JijinSyncFileDTO jijinSyncFileDTO = new JijinSyncFileDTO();
        jijinSyncFileDTO.setId(999);
        jijinSyncFileDTO.setFileName("/home/NiuZhanJun/WorkLufax/Projects/30_jijindaixiao/sampleFiles/wind_20150806010000_02.txt");
        jijinSyncFileDTO.setCurrentLine(1l);
        jijinExDiscountService.recordFileSync(jijinSyncFileDTO);
        assertTrue(1 == 1);
    }

    @Test@Ignore
    public void testHandleDiscount_NoJijinInfo() {
        try {
            System.out.println("=======================================start testHandleDiscount_NoJijinInfo=======================================");
            JijinExDiscountDTO dto = new JijinExDiscountDTO();
            dto.setId(11);
            dto.setFundCode("abcdedf");
            jijinExDiscountService.handleDiscount(dto);
        } catch (Exception e) {
            System.out.println("testHandleDiscount_NoJijinInfo test exception");
            e.printStackTrace();
        }
        System.out.println("=======================================end testHandleDiscount_NoJijinInfo=======================================");
        assertTrue(1 == 1);
    }

    @Test@Ignore
    public void testHandleDiscount_Dispatched() {
        try {
            System.out.println("=======================================start testHandleDiscount_Dispatched=======================================");
            JijinExDiscountDTO dto = new JijinExDiscountDTO();
            dto.setId(101);
            dto.setStatus("DISPACHED");
            dto.setFundCode("110020");
            jijinExDiscountService.handleDiscount(dto);
        } catch (Exception e) {
            System.out.println("testHandleDiscount_Dispatched test exception");
            e.printStackTrace();
        }
        System.out.println("=======================================end testHandleDiscount_Dispatched=======================================");
        assertTrue(1 == 1);
    }

    @Test@Ignore
    public void testHandleDiscount_UpdateFailed() {
        BaseGson baseGson = new BaseGson();
        baseGson.setRetCode("001");
        when(jijinDaixiaoInfoService.sendDiscountToProduct((JijinProductFeeGson) anyObject())).thenReturn(baseGson);
        try {
            System.out.println("=======================================start testHandleDiscount_UpdateFailed=======================================");
            JijinExDiscountDTO dto = new JijinExDiscountDTO();
            dto.setId(203);
            dto.setStatus("NEW");
            dto.setFundCode("110020");
            jijinExDiscountService.handleDiscount(dto);
        } catch (Exception e) {
            System.out.println("testHandleDiscount_UpdateFailed test exception");
            e.printStackTrace();
        }
        System.out.println("=======================================end testHandleDiscount_UpdateFailed=======================================");
        assertTrue(1 == 1);
    }

    @Test@Ignore
    public void testHandleDiscount_Success() {
        BaseGson baseGson = new BaseGson();
        baseGson.setRetCode("000");
        //when(jijinDaixiaoInfoService.sendDiscountToProduct((List<JijinProductFeeGson>) anyObject())).thenReturn(baseGson);
        try {
            System.out.println("=======================================start testHandleDiscount_Success=======================================");
            JijinExDiscountDTO dto = new JijinExDiscountDTO();
            dto.setId(601);
            dto.setStatus("NEW");
            dto.setFundCode("110020");
            jijinExDiscountService.handleDiscount(dto);
        } catch (Exception e) {
            System.out.println("testHandleDiscount_Success test exception");
            e.printStackTrace();
        }
        System.out.println("=======================================end testHandleDiscount_Success=======================================");
        assertTrue(1 == 1);
    }

    @Test@Ignore
    public void testNeo() {
        String fileName = "wind-20150725121212-03.txt";
        String result = "";


        JijinSyncFileDTO jijinSyncFileDTO = new JijinSyncFileDTO();
        String year = "2015";

        for (int i = 1; i < 19; i++) {

            if (!(i == 11 || i == 12 || i == 16 || i == 17)) {
                continue;
            }
            jijinSyncFileDTO = new JijinSyncFileDTO();
            jijinSyncFileDTO.setCurrentLine(1l);
            String fileNO = i + "";
            if (i < 10) {
                fileNO = "0" + fileNO;
            }
            jijinSyncFileDTO.setSourcePath("wind_" + year + "0814110000_" + fileNO + ".txt");
            jijinSyncFileDTO.setStatus("INIT");
            String bizType = DaiXiaoFileTypeEnum.getTypeNameByCode(fileNO);
            jijinSyncFileDTO.setBizType(bizType);
            jijinSyncFileDTO.setBizDate(year + "0814");
            jijinSyncFileDTO.setFileName("wind_" + year + "0814110000_" + fileNO + ".txt");
            jijinSyncFileDTO.setMemo("");
            jijinSyncFileDTO.setRetryTimes(0l);
            jijinSyncFileRepository.insertBusJijinSyncFile(jijinSyncFileDTO);
        }
    }

    @Test@Ignore
    public void testNeo2(){

        Long pid=715l;
        String fileName="/home/NiuZhanJun/WorkLufax/Projects/30_jijindaixiao/sampleFiles/wind_20150806010000_02.txt";
        JijinSyncFileDTO jijinSyncFileDTO = jijinSyncFileRepository.findBusJijinSyncFile(MapUtils.buildKeyValueMap("id", pid));

        String downloadSuccessFileName = fileName + ".ok";
        String downloadingFileName = fileName + ".down";
        String downloadFailedFileName = fileName + ".error";
        File okFile=new File(downloadSuccessFileName);
        File errorFile=new File(downloadFailedFileName);
        File downingFile=new File(downloadingFileName);

        if (jijinSyncFileDTO.getRetryTimes() > 0) {
            //存在下载成功的标识文件
            if (okFile.exists()) {
                System.out.println("sssssssssssssssssssssss");
            } else if (errorFile.exists()) {
                errorFile.delete();
                System.out.println("fffffffffffffffffffffff");
            } else if (downingFile.exists()) {
                System.out.println("dddddddddddddddddddddddd");
            }else{
                try {
                    downingFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("oooooooooooooooooooooooooo");
            }
        } else {
            System.out.println("0000000000000000000000");
        }
    }


}


