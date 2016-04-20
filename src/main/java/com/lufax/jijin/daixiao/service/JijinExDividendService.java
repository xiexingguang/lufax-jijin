package com.lufax.jijin.daixiao.service;

import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.daixiao.dto.JijinExDividendDTO;
import com.lufax.jijin.daixiao.repository.JijinExDividendRepository;
import com.lufax.jijin.daixiao.schedular.domain.JijinExDividendReader;
import com.lufax.jijin.fundation.constant.SyncFileStatus;
import com.lufax.jijin.fundation.dto.JijinSyncFileDTO;
import com.lufax.jijin.fundation.repository.JijinSyncFileRepository;
import com.site.lookup.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class JijinExDividendService {

    @Autowired
    private JijinExDividendRepository jijinExDividendRepository;
    @Autowired
    private JijinSyncFileRepository jijinSyncFileRepository;

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
            List<JijinExDividendDTO> dtos = new ArrayList<JijinExDividendDTO>();
            JijinExDividendReader contentreader = new JijinExDividendReader();

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
                    JijinExDividendDTO dto = contentreader.readLine(s, reader.getLineNumber(), syncFile);
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
    public void batchInsertSyncAndUpdateSyncFile(List<JijinExDividendDTO> dtos, long lineNum, JijinSyncFileDTO syncFile) {
        jijinExDividendRepository.batchInsertDTOs(dtos);
        jijinSyncFileRepository.updateBusJijinSyncFile(MapUtils.buildKeyValueMap("id", syncFile.getId(), "currentLine", lineNum));
    }
}
