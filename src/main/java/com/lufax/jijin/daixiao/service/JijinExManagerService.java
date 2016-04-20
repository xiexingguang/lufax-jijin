package com.lufax.jijin.daixiao.service;


import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.daixiao.constant.RecordStatus;
import com.lufax.jijin.daixiao.dto.JijinExManagerDTO;
import com.lufax.jijin.daixiao.gson.JijinUpdateGson;
import com.lufax.jijin.daixiao.repository.JijinExManagerRepository;
import com.lufax.jijin.daixiao.schedular.domain.JijinExManagerReader;
import com.lufax.jijin.fundation.constant.SyncFileStatus;
import com.lufax.jijin.fundation.dto.JijinInfoDTO;
import com.lufax.jijin.fundation.dto.JijinSyncFileDTO;
import com.lufax.jijin.fundation.gson.BaseGson;
import com.lufax.jijin.fundation.gson.CreateJijinInfoResultGson;
import com.lufax.jijin.fundation.repository.JijinInfoRepository;
import com.lufax.jijin.fundation.repository.JijinSyncFileRepository;
import com.site.lookup.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class JijinExManagerService {

    @Autowired
    private JijinExManagerRepository jijinExManagerRepository;
    @Autowired
    private JijinSyncFileRepository jijinSyncFileRepository;
    @Autowired
    private JijinInfoRepository jijinInfoRepository;
    @Autowired
    private JijinDaixiaoInfoService jijinDaixiaoInfoService;

    public void recordFileSync(JijinSyncFileDTO jijinSyncFileDTO) throws IOException {

        String fileName = jijinSyncFileDTO.getFileName(); // this file name includes path
        File sourceFile = new File(fileName);
        dealFileWithBatchSize(sourceFile, jijinSyncFileDTO.getCurrentLine(), 200, jijinSyncFileDTO);
        jijinSyncFileRepository.updateBusJijinSyncFile(MapUtils.buildKeyValueMap("id", jijinSyncFileDTO.getId(), "status", SyncFileStatus.READ_SUCCESS.name()));
    }

    public void dealFileWithBatchSize(File sourceFile, long startLine, long rownum, JijinSyncFileDTO syncFile) throws IOException {
        InputStreamReader in = new InputStreamReader(new FileInputStream(sourceFile), "UTF-8");
        LineNumberReader reader = new LineNumberReader(in);
        try {
            String s = null;
            List<JijinExManagerDTO> dtos = new ArrayList<JijinExManagerDTO>();
            JijinExManagerReader contentreader = new JijinExManagerReader();

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
                    JijinExManagerDTO dto = contentreader.readLine(s, reader.getLineNumber(), syncFile);
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
    public void batchInsertSyncAndUpdateSyncFile(List<JijinExManagerDTO> dtos, long lineNum, JijinSyncFileDTO syncFile) {
        jijinExManagerRepository.batchInsertDTOs(dtos);
        jijinSyncFileRepository.updateBusJijinSyncFile(MapUtils.buildKeyValueMap("id", syncFile.getId(), "currentLine", lineNum));
    }

    @Transactional
    public void handleJijinExManager(JijinExManagerDTO jijinExManagerDTO) {
        JijinInfoDTO jijinInfoDTO = jijinInfoRepository.findJijinInfoByFundCode(jijinExManagerDTO.getFundCode());
        if (jijinInfoDTO == null) {
            Logger.info(this, String.format("can not find jijinInfo with fundCode [%s],do nothing", jijinExManagerDTO.getFundCode()));
            jijinExManagerRepository.updateJijinExManager(MapUtils.buildKeyValueMap("id", jijinExManagerDTO.getId(), "status", RecordStatus.NO_USED.name()));
            return;
        } else {
            jijinExManagerDTO = jijinExManagerRepository.getJijinExManagerById(jijinExManagerDTO.getId());
            if (!jijinExManagerDTO.getStatus().equals(RecordStatus.NEW.name())) {
                return;
            }
            List<JijinExManagerDTO> dtos = jijinExManagerRepository.getJijinExManagersByBatchIdAndFundCode(jijinExManagerDTO.getBatchId(), jijinExManagerDTO.getFundCode());
            StringBuffer sb = new StringBuffer();
            for (JijinExManagerDTO dto : dtos) {
                sb.append(dto.getManager()).append(",");
            }
            String manager = sb.substring(0, sb.length() - 1);
            JijinUpdateGson jijinUpdateGson = new JijinUpdateGson();
            jijinUpdateGson.setCode(jijinInfoDTO.getProductCode());
            jijinUpdateGson.setFundManager(manager);
            BaseGson createJijinInfoResultGson = jijinDaixiaoInfoService.updateProduct(jijinUpdateGson);
            if (createJijinInfoResultGson.getRetCode().equals("000")) {
                for (JijinExManagerDTO dto : dtos) {
                    jijinExManagerRepository.updateJijinExManager(MapUtils.buildKeyValueMap("id", dto.getId(), "status", RecordStatus.DISPACHED.name()));
                }
            }else{
                for (JijinExManagerDTO dto : dtos) {
                    jijinExManagerRepository.updateJijinExManager(MapUtils.buildKeyValueMap("id", dto.getId(), "status", RecordStatus.FAILED.name()));
                }
            }
        }
    }
}
