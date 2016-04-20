package com.lufax.jijin.daixiao.service;

import com.google.gson.Gson;
import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.daixiao.remote.JijinExGatewayRemoteService;
import com.lufax.jijin.fundation.constant.SyncFileStatus;
import com.lufax.jijin.fundation.dto.JijinSyncFileDTO;
import com.lufax.jijin.fundation.remote.gson.response.GWResponseGson;
import com.lufax.jijin.fundation.repository.JijinInfoRepository;
import com.lufax.jijin.fundation.repository.JijinSyncFileRepository;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import java.util.Map;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by NiuZhanJun on 7/27/15.
 */
@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class JijinExFileServiceTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private JijinExFileService jijinExFileService;
    @Autowired
    private JijinExGatewayRemoteService jijinExGatewayRemoteService;
    @Autowired
    private JijinInfoRepository jijinInfoRepository;
    @Autowired
    private JijinSyncFileRepository jijinSyncFileRepository;

    @Test@Ignore
    public void testCreateFile() throws Exception {

        //jijinExFileService.createDividendFile(120000);

        JijinSyncFileDTO jijinSyncFileDTO = new JijinSyncFileDTO();
        jijinSyncFileDTO=jijinSyncFileRepository.findBusJijinSyncFile(MapUtils.buildKeyValueMap("id",969));

        System.out.println("file status=" + jijinSyncFileDTO.getStatus());


        assertTrue(1 == 1);
    }

    @Test@Ignore
    public void testScanFile_Success() {
        jijinExGatewayRemoteService = mock(JijinExGatewayRemoteService.class);
        jijinExFileService.setJijinExGatewayRemoteService(jijinExGatewayRemoteService);

        GWResponseGson gwResponseGson = new GWResponseGson();
        gwResponseGson.setRetCode("000");
        when(jijinExGatewayRemoteService.pullFile(anyString())).thenReturn(gwResponseGson);

        JijinSyncFileDTO jijinSyncFileDTO = new JijinSyncFileDTO();
        jijinSyncFileDTO.setId(503l);
        jijinSyncFileDTO.setFileName("wind_20150811101848_05.txt");

        jijinExFileService.scanFile(jijinSyncFileDTO);
    }
    @Test@Ignore
    public void testScanFile_Exist() {
        jijinExGatewayRemoteService = mock(JijinExGatewayRemoteService.class);
        jijinExFileService.setJijinExGatewayRemoteService(jijinExGatewayRemoteService);

        GWResponseGson gwResponseGson = new GWResponseGson();
        gwResponseGson.setRetCode("000");
        when(jijinExGatewayRemoteService.pullFile(anyString())).thenReturn(gwResponseGson);

        JijinSyncFileDTO jijinSyncFileDTO = new JijinSyncFileDTO();
        jijinSyncFileDTO=jijinSyncFileRepository.findBusJijinSyncFile(MapUtils.buildKeyValueMap("id",771));

        System.out.println("file status=" + jijinSyncFileDTO.getStatus());
        jijinExFileService.scanFile(jijinSyncFileDTO);
    }
    @Test@Ignore
    public void testScanFile_Failed() {
        jijinExGatewayRemoteService = mock(JijinExGatewayRemoteService.class);
        jijinExFileService.setJijinExGatewayRemoteService(jijinExGatewayRemoteService);

        GWResponseGson gwResponseGson = new GWResponseGson();
        gwResponseGson.setRetCode("000");
        when(jijinExGatewayRemoteService.pullFile(anyString())).thenReturn(gwResponseGson);

        JijinSyncFileDTO jijinSyncFileDTO = new JijinSyncFileDTO();
        jijinSyncFileDTO.setId(503l);
        jijinSyncFileDTO.setFileName("wind_20150811101848_05.txt");

        jijinExFileService.scanFile(jijinSyncFileDTO);
    }

    @Test@Ignore
    public void testNeo() {
        String status = SyncFileStatus.NOT_EXIST.name();
        JijinSyncFileDTO jijinSyncFileDTO = new JijinSyncFileDTO();
        jijinSyncFileDTO.setId(645l);
        jijinSyncFileDTO.setRetryTimes(0l);

        jijinSyncFileDTO = jijinSyncFileRepository.findBusJijinSyncFile(MapUtils.buildKeyValueMap("id", 645));

        if(!jijinSyncFileDTO.getStatus().equals(SyncFileStatus.INIT)){
            System.out.println(String.format("not process unInit record.syncFileId is[%S]", jijinSyncFileDTO.getId()));
            return;
        }

        if (jijinSyncFileDTO.getRetryTimes() < 10) {
            status = SyncFileStatus.INIT.name();
        }
        String memo = "pull failed:";
        if (jijinSyncFileDTO.getMemo() == null || jijinSyncFileDTO.getMemo().length() == 0) {
            memo += "gwResponseGson";
        } else {
            memo = jijinSyncFileDTO.getMemo() + "|" + memo + "gwResponseGson";
        }

        jijinSyncFileRepository.updateBusJijinSyncFile(MapUtils.buildKeyValueMap("id", jijinSyncFileDTO.getId(), "status", status, "memo", memo, "retryTimes", jijinSyncFileDTO.getRetryTimes() + 1));
    }


}