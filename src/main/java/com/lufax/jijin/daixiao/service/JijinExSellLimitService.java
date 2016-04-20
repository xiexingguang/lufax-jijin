package com.lufax.jijin.daixiao.service;

import com.google.gson.Gson;
import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.daixiao.dto.JijinExSellLimitDTO;
import com.lufax.jijin.daixiao.repository.JijinExSellLimitRepository;
import com.lufax.jijin.daixiao.schedular.domain.JijinExSellLimitSyncReader;
import com.lufax.jijin.fundation.constant.SyncFileStatus;
import com.lufax.jijin.fundation.dto.JijinSyncFileDTO;
import com.lufax.jijin.fundation.repository.JijinInfoRepository;
import com.lufax.jijin.fundation.repository.JijinSyncFileRepository;
import com.site.lookup.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by NiuZhanJun on 7/23/15.
 */
@Service
public class JijinExSellLimitService {
    @Autowired
    private JijinExSellLimitRepository jijinExSellLimitRepository;
    @Autowired
    private JijinSyncFileRepository jijinSyncFielRepository;
    @Autowired
    private JijinInfoRepository jijinInfoRepository;

    public void setJijinDaixiaoInfoService(JijinDaixiaoInfoService jijinDaixiaoInfoService) {
        this.jijinDaixiaoInfoService = jijinDaixiaoInfoService;
    }

    @Autowired
    private JijinDaixiaoInfoService jijinDaixiaoInfoService;

    public void recordFileSync(JijinSyncFileDTO jijinSyncFileDTO) throws IOException {

        String fileName = jijinSyncFileDTO.getFileName(); // this file name includes path
        File sourceFile = new File(fileName);
        dealFileWithBatchSize(sourceFile, jijinSyncFileDTO.getCurrentLine(), 200, jijinSyncFileDTO);
        jijinSyncFielRepository.updateBusJijinSyncFile(MapUtils.buildKeyValueMap("id", jijinSyncFileDTO.getId(), "status", SyncFileStatus.READ_SUCCESS.name()));
    }

    public void dealFileWithBatchSize(File sourceFile, long startLine, long rownum, JijinSyncFileDTO syncFile) throws IOException {
        InputStreamReader in = new InputStreamReader(new FileInputStream(sourceFile), "UTF-8");
        LineNumberReader reader = new LineNumberReader(in);
        try {
            String s = null;
            List<JijinExSellLimitDTO> dtos = new ArrayList<JijinExSellLimitDTO>();
            JijinExSellLimitSyncReader contentreader = new JijinExSellLimitSyncReader();

            do {
                s = reader.readLine();
                if (StringUtils.isEmpty(s)) {// 文件读取完毕,插入最后一批数据
                    Logger.info(this, String.format("read jijin dispatch file  - insert last batch records into DB endline:%s]", reader.getLineNumber() - 1));
                    if (dtos.size() > 0) {
                        batchInsertSyncAndUpdateSyncFile(dtos, reader.getLineNumber(), syncFile);
                    }
                    break;
                }
                if (reader.getLineNumber() >= startLine && reader.getLineNumber() < startLine + rownum) {
                    // 解析行，转换成DTO
                    JijinExSellLimitDTO dto = contentreader.readLine(s, reader.getLineNumber(), syncFile);
                    if (dto != null)
                        dtos.add(dto);
                }

                if (reader.getLineNumber() == startLine + rownum - 1) {// 已达到批次记录集，进行插数据库，清空缓存，并set下一个startLine
                    Logger.info(this, String.format("read jijin dispatch file - insert into DB endline:%s]", reader.getLineNumber()));
                    startLine = reader.getLineNumber() + 1;// set new startLine
                    batchInsertSyncAndUpdateSyncFile(dtos, startLine, syncFile);
                    dtos.clear();
                }
            } while (s != null);
        } finally {
            reader.close();
            in.close();
        }

    }

    @Transactional
    public void batchInsertSyncAndUpdateSyncFile(List<JijinExSellLimitDTO> dtos, long lineNum, JijinSyncFileDTO syncFile) {
        jijinExSellLimitRepository.batchInsertJijinExSellLimit(dtos);
        jijinSyncFielRepository.updateBusJijinSyncFile(MapUtils.buildKeyValueMap("id", syncFile.getId(), "currentLine", lineNum));
    }

    /**
     * 获取基金+业务最新限制信息
     *
     * @param fundCode 基金Code
     * @param bizCode  24:赎回  36：转换
     * @return
     */
    public JijinExSellLimitDTO getLatestSellLimitDtoByFundCode(String fundCode, String bizCode) {
        Long latestBatchId = jijinExSellLimitRepository.getLatestBatchIdByFundAndBizCode(fundCode, bizCode);
        Logger.info(this,String.format("getLatestSellLimitDtoByFundCode latestBatchId is [%s]",latestBatchId));
        List<JijinExSellLimitDTO> jijinExSellLimitDTOList = jijinExSellLimitRepository.getJijinExSellLimit(MapUtils.buildKeyValueMap("fundCode", fundCode, "bizCode", bizCode, "batchId", latestBatchId));
        if (jijinExSellLimitDTOList != null && jijinExSellLimitDTOList.size() > 0) {
            return jijinExSellLimitDTOList.get(0);
        } else {
            return null;
        }
    }

}
