package com.lufax.jijin.daixiao.service;

import com.google.gson.Gson;
import com.lufax.jijin.base.utils.*;
import com.lufax.jijin.daixiao.remote.JijinExGatewayRemoteService;
import com.lufax.jijin.fundation.constant.SyncFileStatus;
import com.lufax.jijin.fundation.dto.JijinSyncFileDTO;
import com.lufax.jijin.fundation.remote.gson.response.GWResponseGson;
import com.lufax.jijin.fundation.repository.JijinSyncFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Random;

/**
 * Created by NiuZhanJun on 7/28/15.
 */
@Service
public class JijinExFileService {
    public static final String SEPERATOR = "|";
    public static final String LINECHANGE = "\n";

    @Autowired
    private JijinSyncFileRepository jijinSyncFileRepository;
    @Autowired
    private JijinExGatewayRemoteService jijinExGatewayRemoteService;
    @Autowired
    private JijinAppProperties jijinAppProperties;

    public void setJijinExGatewayRemoteService(JijinExGatewayRemoteService jijinExGatewayRemoteService) {
        this.jijinExGatewayRemoteService = jijinExGatewayRemoteService;
    }

    public void scanFile(JijinSyncFileDTO jijinSyncFileDTO) {
        String fileName = jijinSyncFileDTO.getFileName();
        boolean scanResult = false;//扫描下载的结果
        boolean needPullFile = true;//是否需要调用下载接口进行下载
        GWResponseGson gwResponseGson = null;

        Logger.info(this, String.format("Start pull_wind_file[%s]", fileName));
        try {
            //防止并发，先判断状态
            jijinSyncFileDTO = jijinSyncFileRepository.findBusJijinSyncFile(MapUtils.buildKeyValueMap("id", jijinSyncFileDTO.getId()));
            if (!jijinSyncFileDTO.getStatus().equals(SyncFileStatus.INIT.name())) {
                Logger.info(this, String.format("not process unInit record.syncFileId is[%S]", jijinSyncFileDTO.getId()));
                return;
            }

            //判断完整文件目录文件是否存在
            String fileDate = getTargetDateOfWindFile(fileName);
            String fullFilePath = jijinAppProperties.getJijinExNasRootDir() + "download/" + fileDate + "/" + fileName;
            System.out.println("fullfilepath=" + fullFilePath);
            File okFile = new File(fullFilePath);

            if (okFile.exists()) {
                Logger.info(this, String.format("pull_wind_file[%s] successed for ok file is exist", fileName));
                scanResult = true;
                needPullFile = false;
            }

            //如果需要pull文件，调用接口处理
            if (needPullFile) {
                //下载文件到NAS
                gwResponseGson = jijinExGatewayRemoteService.pullFile(fileName);
                Logger.info(this, String.format("pull_wind_file[%s] result is[%S]", fileName, new Gson().toJson(gwResponseGson)));
                if (gwResponseGson != null && gwResponseGson.isSuccess()) {
                    scanResult = true;
                }
            }

            //根据下载结果进行处理
            if (scanResult) {
                Map mapUpd = MapUtils.buildKeyValueMap("id", jijinSyncFileDTO.getId(),
                        "status", SyncFileStatus.READY.name(),
                        "fileName", fullFilePath);
                jijinSyncFileRepository.updateBusJijinSyncFile(mapUpd);
            } else {

                String status = SyncFileStatus.NOT_EXIST.name();
                if (jijinSyncFileDTO.getRetryTimes() < 5) {
                    status = SyncFileStatus.INIT.name();
                }
                //最终确认
                if (okFile.exists()) {
                    status = SyncFileStatus.READY.name();
                }

                String memo = "pull failed:";
                if (jijinSyncFileDTO.getMemo() == null || jijinSyncFileDTO.getMemo().length() == 0) {
                    memo += new Gson().toJson(gwResponseGson);
                } else {
                    memo = jijinSyncFileDTO.getMemo() + "|" + memo + new Gson().toJson(gwResponseGson);
                }
                jijinSyncFileRepository.updateBusJijinSyncFile(MapUtils.buildKeyValueMap("id", jijinSyncFileDTO.getId(), "status", status, "memo", memo, "retryTimes", jijinSyncFileDTO.getRetryTimes() + 1));
            }
        } catch (Exception e) {
            String status = SyncFileStatus.NOT_EXIST.name();
            if (jijinSyncFileDTO.getRetryTimes() < 20) {
                status = SyncFileStatus.INIT.name();
            }
            if (!scanResult) {
                jijinSyncFileRepository.updateBusJijinSyncFile(MapUtils.buildKeyValueMap("id", jijinSyncFileDTO.getId(), "status", status));
                Logger.error(this, String.format("pull_wind_file[%s] occur exception[%S]", fileName, new Gson().toJson(e)));
            }
        }


    }


    private static String getTargetDateOfWindFile(String fileName) {
        try {
            String[] params = fileName.split("_");

            String fileDatetime = params[1];

            return fileDatetime.substring(0, 8);
        } catch (Exception e) {
            return new SimpleDateFormat("yyyyMMdd").format(new Date());
        }
    }

    public void createBuyLimitFile(int recordCount) {
        String rootDir = "/home/NiuZhanJun/WorkLufax/Projects/30_jijindaixiao/sampleFiles/";
        String fileName = "wind_" + DateUtils.formatDate(new Date(), "yyyyMMddHHmmss") + "_01.txt";
        StringBuilder sbContent = new StringBuilder();

        String firstLine = "认申购起点限额|" + recordCount + "|" + DateUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss") + "|" + DateUtils.formatDate(new Date(), "yyyy-MM-dd 23:59:59") + "\n";
        String secondLine = "序号|基金代码|业务代码|首次最低限额|首次最高限额|追加最低限额|追加最高限额|单日累计最高限额|单笔最低限额|单笔最高限额|级差|OPDATE\n";

        sbContent.append(firstLine);
        sbContent.append(secondLine);

        for (int i = 1; i < recordCount + 1; i++) {
            sbContent.append(i).append(SEPERATOR);
            sbContent.append(createRandFundCode()).append(SEPERATOR);
            sbContent.append(createRandBizCode()).append(SEPERATOR);
            sbContent.append(createRandom(100, 200)).append(SEPERATOR);
            sbContent.append(createRandom(10000, 20000)).append(SEPERATOR);
            sbContent.append(createRandom(100, 200)).append(SEPERATOR);
            sbContent.append(createRandom(10000, 20000)).append(SEPERATOR);
            sbContent.append(createRandom(100000, 200000)).append(SEPERATOR);
            sbContent.append(createRandom(100, 200)).append(SEPERATOR);
            sbContent.append(createRandom(10000, 20000)).append(SEPERATOR);
            sbContent.append(createRandom(100, 500)).append(SEPERATOR);
            sbContent.append(DateUtils.formatDateTime(new Date())).append(LINECHANGE);
        }
        File emptyFile = FileUtils.createEmptyFile(fileName, rootDir);
        FileUtils.appendContent(emptyFile, sbContent.toString(), "UTF-8");
    }

    public void createDividendFile(int recordCount) {
        String rootDir = "/home/NiuZhanJun/WorkLufax/Projects/30_jijindaixiao/sampleFiles/";
        String fileName = "wind_" + DateUtils.formatDate(new Date(), "yyyyMMddHHmmss") + "_10.txt";
        StringBuilder sbContent = new StringBuilder();

        String firstLine = "认申购起点限额|" + recordCount + "|" + DateUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss") + "|" + DateUtils.formatDate(new Date(), "yyyy-MM-dd 23:59:59") + "\n";
        String secondLine = "序号|基金代码|公告日期|权益登记日|除息日|场外除息日|净值除权日|分红发放日|货币代码|每份分红|OPDATE\n";

        sbContent.append(firstLine);
        sbContent.append(secondLine);

        File emptyFile = FileUtils.createEmptyFile(fileName, rootDir);
        FileUtils.appendContent(emptyFile, sbContent.toString(), "UTF-8");

        Date startDay = DateUtils.parseDate("1900-01-01");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDay);

        boolean hasData = true;
        int processCount = 0;

        int no = 0;


        for (int j = 0; j < 10; j++) {
            sbContent = new StringBuilder();
            for (int i = 1; i < 10001; i++) {
                no++;
                sbContent.append(no).append(SEPERATOR);
                sbContent.append(createRandFundCode()).append(SEPERATOR);
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                String dd = DateUtils.formatDateAsString(calendar.getTime());
                sbContent.append(dd).append(SEPERATOR);
                sbContent.append(dd).append(SEPERATOR);
                sbContent.append(dd).append(SEPERATOR);
                sbContent.append(dd).append(SEPERATOR);
                sbContent.append(dd).append(SEPERATOR);
                sbContent.append(dd).append(SEPERATOR);
                sbContent.append("RMB").append(SEPERATOR);
                sbContent.append(createRandom(1, 10)).append(SEPERATOR);
                sbContent.append(DateUtils.formatDateTime(new Date())).append(LINECHANGE);
                processCount++;
            }
            FileUtils.appendContent(emptyFile, sbContent.toString(), "UTF-8");
        }
    }

    private String createRandFundCode() {
        return "B" + createRandom(2000, 3000);
    }

    private String createRandBizCode() {
        return createRandom(20, 30);
    }

    private String createRandom(int min, int max) {
        Random random = new Random();
        int s = random.nextInt(max) % (max - min + 1) + min;
        return s + "";
    }
}
