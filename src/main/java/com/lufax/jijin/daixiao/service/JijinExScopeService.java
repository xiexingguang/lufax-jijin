package com.lufax.jijin.daixiao.service;

import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.daixiao.constant.JijinExValidEnum;
import com.lufax.jijin.daixiao.constant.RecordStatus;
import com.lufax.jijin.daixiao.dto.JijinExScopeDTO;
import com.lufax.jijin.daixiao.gson.JijinUpdateGson;
import com.lufax.jijin.daixiao.repository.JijinExScopeRepository;
import com.lufax.jijin.daixiao.schedular.domain.JijinExScopeReader;
import com.lufax.jijin.fundation.constant.SyncFileStatus;
import com.lufax.jijin.fundation.dto.JijinInfoDTO;
import com.lufax.jijin.fundation.dto.JijinSyncFileDTO;
import com.lufax.jijin.fundation.gson.BaseGson;
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
public class JijinExScopeService {

    @Autowired
    private JijinExScopeRepository jijinExScopeRepository;
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
            List<JijinExScopeDTO> dtos = new ArrayList<JijinExScopeDTO>();
            JijinExScopeReader contentreader = new JijinExScopeReader();

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
                    JijinExScopeDTO dto = contentreader.readLine(s, reader.getLineNumber(), syncFile);
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
    public void batchInsertSyncAndUpdateSyncFile(List<JijinExScopeDTO> dtos, long lineNum, JijinSyncFileDTO syncFile) {
        jijinExScopeRepository.batchInsertDTOs(dtos);
        jijinSyncFileRepository.updateBusJijinSyncFile(MapUtils.buildKeyValueMap("id", syncFile.getId(), "currentLine", lineNum));
    }

    @Transactional
    public void handleJijinExScope(JijinExScopeDTO jijinExScopeDTO) {
        JijinInfoDTO jijinInfoDTO = jijinInfoRepository.findJijinInfoByFundCode(jijinExScopeDTO.getFundCode());
        jijinExScopeDTO = jijinExScopeRepository.getJijinExScopeById(jijinExScopeDTO.getId());
        if (!jijinExScopeDTO.getStatus().equals(RecordStatus.NEW.name())) {
            return;
        }

        //是否需要更新至product
        boolean isNeedSendToProduct = true;
        //存在该日期以后的数据，则不更新至product
        Integer count = jijinExScopeRepository.countNumberOfAfterReportDate(jijinExScopeDTO.getFundCode(), jijinExScopeDTO.getReportDate());
        if (null != count && count > 0) {
            isNeedSendToProduct = false;
        }
        JijinInfoDTO info = jijinInfoRepository.findJijinInfoByFundCode(jijinExScopeDTO.getFundCode());
        if (null == info) {
            //基金未销售，不更新
            isNeedSendToProduct = false;
        }

        if (!isNeedSendToProduct) {
            jijinExScopeRepository.updateJijinExScope(MapUtils.buildKeyValueMap("id", jijinExScopeDTO.getId(), "status", RecordStatus.NO_USED.name(), "isValid", JijinExValidEnum.IS_VALID.getCode()));
        } else {
            JijinUpdateGson jijinUpdateGson = new JijinUpdateGson();
            jijinUpdateGson.setCode(jijinInfoDTO.getProductCode());
            jijinUpdateGson.setFundScale(jijinExScopeDTO.getFundShare());
            BaseGson createJijinInfoResultGson = jijinDaixiaoInfoService.updateProduct(jijinUpdateGson);
            if (createJijinInfoResultGson.getRetCode().equals("000")) {
                jijinExScopeRepository.updateJijinExScope(MapUtils.buildKeyValueMap("id", jijinExScopeDTO.getId(), "status", RecordStatus.DISPACHED.name(), "isValid", JijinExValidEnum.IS_VALID.getCode()));
            } else {
                jijinExScopeRepository.updateJijinExScope(MapUtils.buildKeyValueMap("id", jijinExScopeDTO.getId(), "status", RecordStatus.FAILED.name(), "isValid", JijinExValidEnum.IS_VALID.getCode()));
            }
        }

        jijinExScopeRepository.updateSameFundCodeReportDateRecordToNotVaild(jijinExScopeDTO.getId(), jijinExScopeDTO.getFundCode(), jijinExScopeDTO.getReportDate());


    }
}
