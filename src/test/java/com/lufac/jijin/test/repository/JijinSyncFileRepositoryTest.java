package com.lufac.jijin.test.repository;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.lufax.jijin.base.utils.DateUtils;
import com.lufax.jijin.daixiao.constant.DaiXiaoFileTypeEnum;
import com.lufax.jijin.daixiao.constant.JijinExAddFileResultEnum;
import com.lufax.jijin.fundation.constant.SyncFileStatus;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.lufax.jijin.fundation.dto.JijinSyncFileDTO;
import com.lufax.jijin.fundation.repository.JijinSyncFileRepository;
import com.lufax.mq.client.util.MapUtils;

@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class JijinSyncFileRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    protected JijinSyncFileRepository jijinSyncFielRepository;

    @Test@Ignore
    public void testGetNextVal(){
        System.out.println(jijinSyncFielRepository.getNextVal());
    }

    @Test@Ignore
    public void testInsert() {

        for (int i = 1; i < 19; i++) {
            if(i<4)continue;
            String typeCode = i + "";
            if (i < 10) typeCode = "0" + i;
            String typeName = DaiXiaoFileTypeEnum.getTypeNameByCode(typeCode);
            JijinSyncFileDTO dto = new JijinSyncFileDTO();
            String fileName = "wind_20150811010000_05.txt";
            dto.setBizDate("20150811");
            dto.setBizType("JIJIN_EX_BUY_LIMIT");
            dto.setCurrentLine(1l);
            dto.setFileName("wind_20150811220000_01.txt");
            dto.setRetryTimes(0l);
            dto.setStatus("INIT");
            dto.setSourcePath("");
            jijinSyncFielRepository.insertBusJijinSyncFile(dto);
        }

    }




    @Test@Ignore
    public void testFind() {

        //JijinSyncFileDTO dto = jijinSyncFielRepository.findBusJijinSyncFile(MapUtils.buildKeyValueMap("bizType", "TRADE", "status", "testStauts"));

        //List<JijinSyncFileDTO> dtos = jijinSyncFielRepository.findBusJijinSyncFileList(MapUtils.buildKeyValueMap("bizType", "TRADE", "status", "testStauts"));

        List<JijinSyncFileDTO> list=jijinSyncFielRepository.findBusJijinSyncFileList(com.lufax.jijin.base.utils.MapUtils.buildKeyValueMap("bizTypes", DaiXiaoFileTypeEnum.getTypeList(), "status", SyncFileStatus.INIT.name(), "limit", 100));
        System.out.println("list size=========="+list.size());


        //assertNotNull(dto);

        assertTrue(list.size() > 0);
    }

    @Test@Ignore
    public void testUpdate() {

        jijinSyncFielRepository.updateBusJijinSyncFile(MapUtils.buildKeyValueMap("id", 1l, "fileName", "changeName"));

    }

    @Test@Ignore
    public void testFindBusJijinSyncFileList() {

        List<JijinSyncFileDTO> busJijinSyncFileList = jijinSyncFielRepository.findBusJijinSyncFileList(com.lufax.jijin.base.utils.MapUtils.buildKeyValueMap("bizTypes", DaiXiaoFileTypeEnum.getTypeList(), "status", SyncFileStatus.INIT.name(), "limit", 100));

        System.out.println("list size="+busJijinSyncFileList.size());

        for (int i = 0; i < busJijinSyncFileList.size(); i++) {
            JijinSyncFileDTO jijinSyncFileDTO = busJijinSyncFileList.get(i);
            System.out.println(jijinSyncFileDTO.getId());
        }

    }

    @Test@Ignore
    public void testAdd() {

        String filePath = "1";
        String fileName = "2";
        String fileType = "15";

        JijinExAddFileResultEnum jijinExAddFileResultEnum = JijinExAddFileResultEnum.SUCCESS;

        try {
            String typeName = DaiXiaoFileTypeEnum.getTypeNameByCode(fileType);

            jijinExAddFileResultEnum = checkParam(filePath, fileName, fileType, typeName);
            if (jijinExAddFileResultEnum != JijinExAddFileResultEnum.SUCCESS) {
                System.out.println("111111111111111111111");
                System.out.println(jijinExAddFileResultEnum.toJson());
                assertFalse(1 == 1);
            }

            JijinSyncFileDTO jijinSyncFileDTO = new JijinSyncFileDTO();
            jijinSyncFileDTO.setSourcePath(filePath);
            jijinSyncFileDTO.setFileName(fileName);
            jijinSyncFileDTO.setBizType(typeName);
            jijinSyncFileDTO.setBizDate(DateUtils.formatDate(new Date(), "yyyyMMdd"));
            jijinSyncFileDTO.setCurrentLine(1l);
            jijinSyncFileDTO.setRetryTimes(0l);
            jijinSyncFileDTO.setStatus(SyncFileStatus.INIT.name());
            jijinSyncFielRepository.insertBusJijinSyncFile(jijinSyncFileDTO);

        } catch (Exception e) {
            jijinExAddFileResultEnum = JijinExAddFileResultEnum.ERROR_EXCEPTION;
        }

        System.out.println("2222222222222222222222222");
        System.out.println(jijinExAddFileResultEnum.toJson());
        assertTrue(1 == 1);
    }

    private JijinExAddFileResultEnum checkParam(String filePath, String fileName, String fileType, String typeName) {
        JijinExAddFileResultEnum jijinExAddFileResultEnum = JijinExAddFileResultEnum.SUCCESS;

        //参数不能为空
        if (filePath == null || fileName == null || fileType == null || typeName == null
                || "".equals(filePath) || "".equals(fileName) || "".equals(fileType) || "".equals(typeName)) {
            jijinExAddFileResultEnum = JijinExAddFileResultEnum.ERROR_PARAMETER;
        }

        List<JijinSyncFileDTO> busJijinSyncFileList = jijinSyncFielRepository.findBusJijinSyncFileList(MapUtils.buildKeyValueMap("sourcePath", filePath, "fileName", fileName));

        //同名文件不能已存在
        if (busJijinSyncFileList != null && busJijinSyncFileList.size() > 0) {
            jijinExAddFileResultEnum = JijinExAddFileResultEnum.ERROR_FILE_EXISTED;
        }

        return jijinExAddFileResultEnum;
    }


}
