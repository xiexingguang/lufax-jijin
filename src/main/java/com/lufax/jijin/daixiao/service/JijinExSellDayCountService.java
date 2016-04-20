package com.lufax.jijin.daixiao.service;

import com.google.gson.Gson;
import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.daixiao.constant.RecordStatus;
import com.lufax.jijin.daixiao.dto.JijinExSellDayCountDTO;
import com.lufax.jijin.daixiao.gson.JijinUpdateGson;
import com.lufax.jijin.daixiao.repository.JijinExSellDayCountRepository;
import com.lufax.jijin.daixiao.schedular.domain.JijinExSellDayCountSyncReader;
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
import java.util.Map;

/**
 * Created by NiuZhanJun on 7/23/15.
 */
@Service
public class JijinExSellDayCountService {
    @Autowired
    private JijinExSellDayCountRepository jijinExSellDayCountRepository;
    @Autowired
    private JijinSyncFileRepository jijinSyncFielRepository;
    @Autowired
    private JijinInfoRepository jijinInfoRepository;
    @Autowired
    private JijinDaixiaoInfoService jijinDaixiaoInfoService;

    public void setJijinDaixiaoInfoService(JijinDaixiaoInfoService jijinDaixiaoInfoService) {
        this.jijinDaixiaoInfoService = jijinDaixiaoInfoService;
    }


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
            List<JijinExSellDayCountDTO> dtos = new ArrayList<JijinExSellDayCountDTO>();
            JijinExSellDayCountSyncReader contentreader = new JijinExSellDayCountSyncReader();

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
                    JijinExSellDayCountDTO dto = contentreader.readLine(s, reader.getLineNumber(), syncFile);
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
    public void batchInsertSyncAndUpdateSyncFile(List<JijinExSellDayCountDTO> dtos, long lineNum, JijinSyncFileDTO syncFile) {
        jijinExSellDayCountRepository.batchInsertJijinExSellDayCount(dtos);
        jijinSyncFielRepository.updateBusJijinSyncFile(MapUtils.buildKeyValueMap("id", syncFile.getId(), "currentLine", lineNum));
    }

    /**
     * 处理基金赎回到帐日期数据
     *
     * @param dto
     */
    @Transactional
    public void handleSellDayCount(JijinExSellDayCountDTO dto) throws Exception {
        JijinInfoDTO jijinInfoDTO = jijinInfoRepository.findJijinInfoByFundCode(dto.getFundCode());
        if (jijinInfoDTO == null) {
            Logger.info(this, String.format("can not find jijinInfo with fundCode [%s],do nothing", dto.getFundCode()));
            jijinExSellDayCountRepository.updateJijinExSellDayCount(MapUtils.buildKeyValueMap("id", dto.getId(), "status", RecordStatus.NO_USED.toString()));
            return;
        } else {
            dto = jijinExSellDayCountRepository.getJijinExSellDayCountById(dto.getId());
            if (!dto.getStatus().equals(RecordStatus.NEW.toString())) {
                Logger.info(this, String.format("need not process for dispached record.JijinExSellDayCount Id = [%s]", dto.getId()));
                return;
            }

            JijinUpdateGson jijinUpdateGson = new JijinUpdateGson();
            jijinUpdateGson.setCode(jijinInfoDTO.getProductCode());

            //赎回到帐时间
            jijinInfoDTO.setRedemptionArrivalDay(dto.getSellConfirmDayCount().intValue());
            jijinUpdateGson.setRedemptionArrivalDay(dto.getSellConfirmDayCount().intValue());

            Map map = MapUtils.buildKeyValueMap("redemptionArrivalDay", jijinInfoDTO.getRedemptionArrivalDay(),
                    "fundCode", jijinInfoDTO.getFundCode());
            int i = jijinInfoRepository.updateJijinInfo(map);

            //更新Products
            BaseGson baseGson = jijinDaixiaoInfoService.updateProduct(jijinUpdateGson);

            //更新记录处理状态
            jijinExSellDayCountRepository.updateJijinExSellDayCount(MapUtils.buildKeyValueMap("id", dto.getId(), "status", RecordStatus.DISPACHED.toString()));

            if (!"000".equals(baseGson.getRetCode())) {
                //更新product失败
                Logger.error(this, String.format("更新product RedemptionArrivalDay失败，JijinExSellDayCount id is[%s],fundCode is[%s]", dto.getId(), dto.getFundCode()));
                throw new RuntimeException("更新product fundType失败");
            }
        }
    }
}
