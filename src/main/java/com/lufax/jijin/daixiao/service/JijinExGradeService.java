package com.lufax.jijin.daixiao.service;

import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.daixiao.constant.JijinExValidEnum;
import com.lufax.jijin.daixiao.constant.RecordStatus;
import com.lufax.jijin.daixiao.dto.JijinExGradeDTO;
import com.lufax.jijin.daixiao.gson.JijinUpdateGson;
import com.lufax.jijin.daixiao.repository.JijinExGradeRepository;
import com.lufax.jijin.daixiao.schedular.domain.JijinExGradeReader;
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
public class JijinExGradeService {

    @Autowired
    private JijinExGradeRepository jijinExGradeRepository;
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
            List<JijinExGradeDTO> dtos = new ArrayList<JijinExGradeDTO>();
            JijinExGradeReader contentreader = new JijinExGradeReader();

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
                    JijinExGradeDTO dto = contentreader.readLine(s, reader.getLineNumber(), syncFile);
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
    public void batchInsertSyncAndUpdateSyncFile(List<JijinExGradeDTO> dtos, long lineNum, JijinSyncFileDTO syncFile) {
        jijinExGradeRepository.batchInsertDTOs(dtos);
        jijinSyncFileRepository.updateBusJijinSyncFile(MapUtils.buildKeyValueMap("id", syncFile.getId(), "currentLine", lineNum));
    }

    @Transactional
    public void handleJijinExGrade(JijinExGradeDTO jijinExGradeDTO) {
        jijinExGradeDTO = jijinExGradeRepository.getJijinExGradeById(jijinExGradeDTO.getId());
        if (!jijinExGradeDTO.getStatus().equals(RecordStatus.NEW.name())) {
            return;
        }

        //是否需要更新至product
        boolean isNeedSendToProduct = true;
        //存在该日期以后的数据，则不更新至product
        Integer count = jijinExGradeRepository.countNumberOfAfterRateDate(jijinExGradeDTO.getFundCode(), jijinExGradeDTO.getRateDate(), jijinExGradeDTO.getRatingInterval(),jijinExGradeDTO.getRatingGagency());
        if (null != count && count > 0) {
            isNeedSendToProduct = false;
        }
        JijinInfoDTO info = jijinInfoRepository.findJijinInfoByFundCode(jijinExGradeDTO.getFundCode());
        if (null == info) {
            //基金未销售，不更新
            isNeedSendToProduct = false;
        }

        if (!isNeedSendToProduct) {
            jijinExGradeRepository.updateJijinExGrade(MapUtils.buildKeyValueMap("id", jijinExGradeDTO.getId(), "status", RecordStatus.NO_USED.name(), "isValid", JijinExValidEnum.IS_VALID.getCode()));
        } else {
            String starLevel = jijinExGradeDTO.getStarLevel();
            Integer intLevel = 0;
            if (starLevel.equals("1.0000")) {
                intLevel = 1;
            } else if (starLevel.equals("2.0000")) {
                intLevel = 2;
            } else if (starLevel.equals("3.0000")) {
                intLevel = 3;
            } else if (starLevel.equals("4.0000")) {
                intLevel = 4;
            } else if (starLevel.equals("5.0000")) {
                intLevel = 5;
            }
            JijinUpdateGson jijinUpdateGson = new JijinUpdateGson();
            if (jijinExGradeDTO.getRatingGagency().equals("HAITONG")) {
                jijinUpdateGson.setHaitongGrade(intLevel);
            } else if (jijinExGradeDTO.getRatingGagency().equals("YINHE")) {
                jijinUpdateGson.setYinheGrade(intLevel);
            } else if (jijinExGradeDTO.getRatingGagency().equals("SHANGZHENG")) {
                jijinUpdateGson.setShangzhengGrade(intLevel);
            }

            jijinUpdateGson.setCode(info.getProductCode());
            BaseGson createJijinInfoResultGson = jijinDaixiaoInfoService.updateProduct(jijinUpdateGson);
            if (createJijinInfoResultGson.getRetCode().equals("000")) {
                jijinExGradeRepository.updateJijinExGrade(MapUtils.buildKeyValueMap("id", jijinExGradeDTO.getId(), "status", RecordStatus.DISPACHED.name(), "isValid", JijinExValidEnum.IS_VALID.getCode()));
            } else {
                jijinExGradeRepository.updateJijinExGrade(MapUtils.buildKeyValueMap("id", jijinExGradeDTO.getId(), "status", RecordStatus.FAILED.name(), "isValid", JijinExValidEnum.IS_VALID.getCode()));
            }
        }
        jijinExGradeRepository.updateSameFundCodeDateIntervalRecordToNotVaild(jijinExGradeDTO.getId(), jijinExGradeDTO.getFundCode(), jijinExGradeDTO.getRateDate(), jijinExGradeDTO.getRatingInterval(),jijinExGradeDTO.getRatingGagency());
    }
}
