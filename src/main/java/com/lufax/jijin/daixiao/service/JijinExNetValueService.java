package com.lufax.jijin.daixiao.service;


import com.lufax.jijin.base.repository.BizParametersRepository;
import com.lufax.jijin.base.utils.ConstantsHelper;
import com.lufax.jijin.base.utils.DateUtils;
import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.daixiao.constant.JijinExValidEnum;
import com.lufax.jijin.daixiao.constant.RecordStatus;
import com.lufax.jijin.daixiao.dto.JijinExNetValueDTO;
import com.lufax.jijin.daixiao.gson.JijinUpdateGson;
import com.lufax.jijin.daixiao.repository.JijinExNetValueRepository;
import com.lufax.jijin.daixiao.schedular.domain.JijinExNetValueReader;
import com.lufax.jijin.fundation.constant.SyncFileStatus;
import com.lufax.jijin.fundation.dto.JijinInfoDTO;
import com.lufax.jijin.fundation.dto.JijinSyncFileDTO;
import com.lufax.jijin.fundation.gson.BaseGson;
import com.lufax.jijin.fundation.repository.JijinInfoRepository;
import com.lufax.jijin.fundation.repository.JijinSyncFileRepository;
import com.lufax.jijin.fundation.util.JijinUtils;
import com.site.lookup.util.StringUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class JijinExNetValueService {

    @Autowired
    private JijinExNetValueRepository jijinExNetValueRepository;
    @Autowired
    private JijinSyncFileRepository jijinSyncFileRepository;
    @Autowired
    private JijinInfoRepository jijinInfoRepository;
    @Autowired
    private JijinDaixiaoInfoService jijinDaixiaoInfoService;
    @Autowired
    private BizParametersRepository bizParametersRepository;

    public void recordFileSync(JijinSyncFileDTO jijinSyncFileDTO) throws IOException {

        String fileName = jijinSyncFileDTO.getFileName(); // this file name includes path
        File sourceFile = new File(fileName);
        dealFileWithBatchSize(sourceFile, jijinSyncFileDTO.getCurrentLine(), 200, jijinSyncFileDTO);
        jijinSyncFileRepository.updateBusJijinSyncFile(MapUtils.buildKeyValueMap("id", jijinSyncFileDTO.getId(), "status", SyncFileStatus.READ_SUCCESS.name()));
    }

    public void dealFileWithBatchSize(File sourceFile, long startLine, long rownum, JijinSyncFileDTO syncFile) throws IOException {
        InputStreamReader in = new InputStreamReader(new FileInputStream(sourceFile), "UTF-8");
        LineNumberReader reader = new LineNumberReader(in);
        String initSwitch = bizParametersRepository.findValueByCode(ConstantsHelper.JIJIN_DAIXIAO_INIT_SWITCH);

        try {
            String s = null;
            List<JijinExNetValueDTO> dtos = new ArrayList<JijinExNetValueDTO>();
            JijinExNetValueReader contentreader = new JijinExNetValueReader();

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
                    JijinExNetValueDTO dto = contentreader.readLine(s, reader.getLineNumber(), syncFile, initSwitch);
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

    public void handleJijinExNetValue(JijinExNetValueDTO jijinExNetValueDTO) {
        JijinInfoDTO jijinInfoDTO = jijinInfoRepository.findJijinInfoByFundCode(jijinExNetValueDTO.getFundCode());
        jijinExNetValueDTO = jijinExNetValueRepository.getJijinExNetValueById(jijinExNetValueDTO.getId());
        if (!jijinExNetValueDTO.getStatus().equals(RecordStatus.NEW.name())) {
            return;
        }
        if (jijinInfoDTO == null || JijinUtils.isHuoji(jijinInfoDTO)) {
        	//基金不存在或者是货基时，净值全部处理为无效
            Logger.info(this, String.format("can not find jijinInfo with fundCode [%s],do nothing", jijinExNetValueDTO.getFundCode()));
            //判断是否存在历史数据需要更新
            List<JijinExNetValueDTO> jijinExNetValueDTOs = jijinExNetValueRepository.getJijinExNetValuesByFundCodeAndEndDateAndBatchId(jijinExNetValueDTO.getFundCode(), jijinExNetValueDTO.getEndDate(), jijinExNetValueDTO.getBatchId());
            if (!CollectionUtils.isEmpty(jijinExNetValueDTOs)) {
                for (JijinExNetValueDTO dto : jijinExNetValueDTOs) {
                    jijinExNetValueRepository.updateJijinExNetValue(MapUtils.buildKeyValueMap("id", dto.getId(), "status", RecordStatus.NO_USED.name(), "isValid", JijinExValidEnum.IS_NOT_VALID.getCode()));
                }
            }
            jijinExNetValueRepository.updateJijinExNetValue(MapUtils.buildKeyValueMap("id", jijinExNetValueDTO.getId(), "status", RecordStatus.NO_USED.name(), "isValid", JijinExValidEnum.IS_VALID.getCode()));
            return;
        } else {
            List<JijinExNetValueDTO> jijinExNetValueDTOs = jijinExNetValueRepository.getJijinExNetValuesByFundCodeAndEndDateAndBatchId(jijinExNetValueDTO.getFundCode(), jijinExNetValueDTO.getEndDate(), jijinExNetValueDTO.getBatchId());
            if (!CollectionUtils.isEmpty(jijinExNetValueDTOs)) {
                for (JijinExNetValueDTO dto : jijinExNetValueDTOs) {
                    jijinExNetValueRepository.updateJijinExNetValue(MapUtils.buildKeyValueMap("id", dto.getId(), "status", RecordStatus.NO_USED.name(), "isValid", JijinExValidEnum.IS_NOT_VALID.getCode()));
                }
            }

            //是否需要更新至product
            boolean isNeedSendToProduct = true;
            //存在该日期以后的数据，则不更新至product
            Integer count = jijinExNetValueRepository.countNumberOfAfterEndDate(jijinExNetValueDTO.getFundCode(), jijinExNetValueDTO.getEndDate());
            if (null != count && count > 0) {
                isNeedSendToProduct = false;
            }
            if (!isNeedSendToProduct) {
                jijinExNetValueRepository.updateJijinExNetValue(MapUtils.buildKeyValueMap("id", jijinExNetValueDTO.getId(), "status", RecordStatus.NO_USED.name(), "isValid", JijinExValidEnum.IS_VALID.getCode()));
            } else {
                JijinUpdateGson jijinUpdateGson = new JijinUpdateGson();
                jijinUpdateGson.setCode(jijinInfoDTO.getProductCode());
                jijinUpdateGson.setUnitPrice2(jijinExNetValueDTO.getNetValue());
                jijinUpdateGson.setUnitPriceDate(DateUtils.formatDateFromStringToString(jijinExNetValueDTO.getEndDate()) + " 00:00:00");
                BaseGson createJijinInfoResultGson = jijinDaixiaoInfoService.updateProduct(jijinUpdateGson);
                if (createJijinInfoResultGson.getRetCode().equals("000")) {
                    jijinExNetValueRepository.updateJijinExNetValue(MapUtils.buildKeyValueMap("id", jijinExNetValueDTO.getId(), "status", RecordStatus.DISPACHED.name(), "isValid", JijinExValidEnum.IS_VALID.getCode()));
                } else {
                    jijinExNetValueRepository.updateJijinExNetValue(MapUtils.buildKeyValueMap("id", jijinExNetValueDTO.getId(), "status", RecordStatus.FAILED.name(), "isValid", JijinExValidEnum.IS_VALID.getCode()));
                }
            }
        }
    }


    @Transactional
    public void batchInsertSyncAndUpdateSyncFile(List<JijinExNetValueDTO> dtos, long lineNum, JijinSyncFileDTO syncFile) {
        jijinExNetValueRepository.batchInsertDTOs(dtos);
        jijinSyncFileRepository.updateBusJijinSyncFile(MapUtils.buildKeyValueMap("id", syncFile.getId(), "currentLine", lineNum));
    }
}
