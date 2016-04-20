package com.lufax.jijin.fundation.service;

import com.lufax.jijin.base.utils.FileUtils;
import com.lufax.jijin.base.utils.JijinAppProperties;
import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.base.utils.JijinInstIdPlatMerchantIdMapHolder;
import com.lufax.jijin.fundation.constant.SyncFileBizType;
import com.lufax.jijin.fundation.constant.SyncFileStatus;
import com.lufax.jijin.fundation.dto.JijinSyncFileDTO;
import com.lufax.jijin.fundation.repository.JijinSyncFileRepository;
import com.lufax.jijin.fundation.service.domain.FileHolder;
import com.lufax.jijin.fundation.util.DahuaDecrptyUtils;
import com.lufax.kernel.security.kms.api.KmsService;
import com.lufax.jijin.service.MqService;
import com.lufax.mq.client.util.MapUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 扫描基金对账文件是否存在
 *
 * @author xuneng
 */

@Service
public class ScanService {

    @Autowired
    private JijinSyncFileRepository jijinSyncFielRepository;
    @Autowired
    private KmsService kmsService;

    @Autowired
    private JijinAppProperties jijinAppProperties;
    @Autowired
    private MqService mqService;
    @Autowired
    private JijinInstIdPlatMerchantIdMapHolder jijinInstIdPlatMerchantIdMapHolder;

    // batch Insert
    // select，scan ，翻状态
    // bizDate yyyyMMdd
    public void yiTiaoLong(List<FileHolder> fileHolderList, String bizDate, String bizType) {

        List<JijinSyncFileDTO> list = new ArrayList<JijinSyncFileDTO>();
        for (FileHolder file : fileHolderList) {
            String filePathAndName = file.getFileAbsolutePath() + file.getFileName();

            JijinSyncFileDTO existFile = jijinSyncFielRepository.findBusJijinSyncFile(MapUtils.buildKeyValueMap("fileName", filePathAndName));
            if (existFile == null) {
                JijinSyncFileDTO dto = new JijinSyncFileDTO();
                dto.setBizDate(bizDate);
                dto.setBizType(bizType);
                dto.setCurrentLine(1l);
                dto.setFileName(filePathAndName);
                dto.setRetryTimes(0l);
                dto.setStatus(SyncFileStatus.INIT.name());
                list.add(dto);
            }
        }

        if (!CollectionUtils.isEmpty(list))
            jijinSyncFielRepository.batchInsertBusJijinSyncFile(list);


        list = jijinSyncFielRepository.findBusJijinSyncFileList(MapUtils.buildKeyValueMap("status", SyncFileStatus.INIT.name(), "bizType", bizType));

        for (JijinSyncFileDTO targetFile : list) {

            File target = new File(targetFile.getFileName());

            if (target.exists()) {
                //update status
                jijinSyncFielRepository.updateBusJijinSyncFile(MapUtils.buildKeyValueMap("id", targetFile.getId(), "status", SyncFileStatus.READY.name()));
                if (targetFile.getBizType().equals(SyncFileBizType.PAF_REDEEM_DIVIDEND.name())) {
                    if (targetFile.getFileName().indexOf(jijinInstIdPlatMerchantIdMapHolder.getPlatMerchantId("lfx201")) != -1) {
                        mqService.sendRecordPafFileSuccessMsg(targetFile.getId(), "lfx201");
                    }
                }
            } else {

                //如果是平安付代付结果文件，则永远不过期
                if (isPAFPayFile(targetFile.getFileName())) {
                    continue;
                }

                // if is final round, set status as NOT_EXIST
                Calendar c = Calendar.getInstance();
                c.setTime(new Date());
                int hour = c.get(Calendar.HOUR_OF_DAY);
                if (hour > 22)
                    jijinSyncFielRepository.updateBusJijinSyncFile(MapUtils.buildKeyValueMap("id", targetFile.getId(), "status", SyncFileStatus.NOT_EXIST.name()));
            }

        }

    }

    /**
     * @param fileName file name sample: /nfsc/sftp_user/pafsftp/upload/20150708/FUND_RESULT_P_900000044504_20150708_1.txt
     * @return
     */
    private boolean isPAFPayFile(String fileName) {

        if (fileName.indexOf("FUND_RESULT") > 0)
            return true;

        return false;
    }

    public void yiTiaoLongForZIP(List<FileHolder> fileHolderList, String bizDate, String bizType) {

        List<JijinSyncFileDTO> list = new ArrayList<JijinSyncFileDTO>();
        for (FileHolder file : fileHolderList) {
            String filePathAndName = file.getFileAbsolutePath() + file.getFileName();

            JijinSyncFileDTO existFile = jijinSyncFielRepository.findBusJijinSyncFile(MapUtils.buildKeyValueMap("fileName", filePathAndName));
            if (existFile == null) {
                JijinSyncFileDTO dto = new JijinSyncFileDTO();
                dto.setBizDate(bizDate);
                dto.setBizType(bizType);
                dto.setCurrentLine(1l);
                dto.setFileName(filePathAndName);
                dto.setRetryTimes(0l);
                dto.setStatus(SyncFileStatus.INIT.name());
                list.add(dto);
            }
        }
        if (!CollectionUtils.isEmpty(list))
            jijinSyncFielRepository.batchInsertBusJijinSyncFile(list);


        list = jijinSyncFielRepository.findBusJijinSyncFileList(MapUtils.buildKeyValueMap("status", SyncFileStatus.INIT.name(), "bizType", bizType));

        for (JijinSyncFileDTO targetFile : list) {
            try {
                File target = new File(targetFile.getFileName());
                if (target.exists()) {
                    String outFileName = targetFile.getFileName().replace("upload", "download");
                    checkAndMkdir(outFileName.substring(0, outFileName.lastIndexOf("/")));
                    File outFile = new File(outFileName);
                    String base64key = kmsService.getRawKey(jijinAppProperties.getDahua3des());
                    byte[] eninbytes = DahuaDecrptyUtils.readFile(target);
                    byte[] plianbytes = DahuaDecrptyUtils.decryptBy3DES(eninbytes, DahuaDecrptyUtils.build3DesKey(base64key));
                    DahuaDecrptyUtils.writeFile(plianbytes, outFile);
                    if (outFile.exists()) {
                        FileUtils.unzipFile(outFileName);
                        jijinSyncFielRepository.updateBusJijinSyncFile(MapUtils.buildKeyValueMap("id", targetFile.getId(), "status", SyncFileStatus.READY.name()));
                    } else {
                        // if is final round, set status as NOT_EXIST
                        Calendar c = Calendar.getInstance();
                        c.setTime(new Date());
                        int hour = c.get(Calendar.HOUR_OF_DAY);
                        if (hour > 22)
                            jijinSyncFielRepository.updateBusJijinSyncFile(MapUtils.buildKeyValueMap("id", targetFile.getId(), "status", SyncFileStatus.NOT_EXIST.name()));
                    }
                } else {
                    // if is final round, set status as NOT_EXIST
                    Calendar c = Calendar.getInstance();
                    c.setTime(new Date());
                    int hour = c.get(Calendar.HOUR_OF_DAY);
                    if (hour > 22)
                        jijinSyncFielRepository.updateBusJijinSyncFile(MapUtils.buildKeyValueMap("id", targetFile.getId(), "status", SyncFileStatus.NOT_EXIST.name()));
                }

            } catch (Exception e) {
                Logger.error(this, String.format("decrypt dahua file failed,e [%s]", e.toString()));
                continue;
            }
        }
    }

    private void checkAndMkdir(String dir) {
        File pullDir = new File(dir);
        if (!pullDir.exists() || !pullDir.isDirectory()) {
            pullDir.mkdirs();
        }
    }
}
