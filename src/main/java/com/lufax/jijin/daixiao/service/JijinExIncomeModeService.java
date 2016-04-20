package com.lufax.jijin.daixiao.service;

import com.google.gson.Gson;
import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.daixiao.constant.RecordStatus;
import com.lufax.jijin.daixiao.dto.JijinExIncomeModeDTO;
import com.lufax.jijin.daixiao.gson.JijinUpdateGson;
import com.lufax.jijin.daixiao.repository.JijinExIncomeModeRepository;
import com.lufax.jijin.daixiao.schedular.domain.JijinExIncomeModeSyncReader;
import com.lufax.jijin.fundation.constant.SyncFileStatus;
import com.lufax.jijin.fundation.dto.JijinInfoDTO;
import com.lufax.jijin.fundation.dto.JijinSyncFileDTO;
import com.lufax.jijin.fundation.gson.BaseGson;
import com.lufax.jijin.fundation.repository.JijinInfoRepository;
import com.lufax.jijin.fundation.repository.JijinSyncFileRepository;
import com.lufax.jijin.fundation.util.JijinUtils;
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
public class JijinExIncomeModeService {
    @Autowired
    private JijinExIncomeModeRepository jijinExIncomeModeRepository;
    @Autowired
    private JijinSyncFileRepository jijinSyncFielRepository;
    @Autowired
    private JijinInfoRepository jijinInfoRepository;
    @Autowired
    private JijinDaixiaoInfoService jijinDaixiaoInfoService;
    public void setJijinDaixiaoInfoService(JijinDaixiaoInfoService jijinDaixiaoInfoService) {
        this.jijinDaixiaoInfoService = jijinDaixiaoInfoService;
    }

    /**
     * 获取基金最新的默认分红方式信息
     * @param fundCode
     * @return
     */
    public JijinExIncomeModeDTO getLatestIncomeModeByFundCode(String fundCode) {
        Long latestBatchId = jijinExIncomeModeRepository.getLatestBatchIdByFundCode(fundCode);
        System.out.println("getLatestIncomeModeByFundCode latestBatchId="+latestBatchId);
        List<JijinExIncomeModeDTO> jijinExIncomeModeList = jijinExIncomeModeRepository.getJijinExIncomeMode(MapUtils.buildKeyValueMap("fundCode", fundCode,
                "batchId", latestBatchId));
        if (jijinExIncomeModeList != null && jijinExIncomeModeList.size() > 0) {
            return jijinExIncomeModeList.get(0);
        } else {
            return null;
        }
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
            List<JijinExIncomeModeDTO> dtos = new ArrayList<JijinExIncomeModeDTO>();
            JijinExIncomeModeSyncReader contentreader = new JijinExIncomeModeSyncReader();

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
                    JijinExIncomeModeDTO dto = contentreader.readLine(s, reader.getLineNumber(), syncFile);
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
    public void batchInsertSyncAndUpdateSyncFile(List<JijinExIncomeModeDTO> dtos, long lineNum, JijinSyncFileDTO syncFile) {
        System.out.println("nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn");
        System.out.println(dtos.size());
        jijinExIncomeModeRepository.batchInsertJijinExIncomeMode(dtos);
        jijinSyncFielRepository.updateBusJijinSyncFile(MapUtils.buildKeyValueMap("id", syncFile.getId(), "currentLine", lineNum));
    }

    /**
     * 处理基金默认分红方式数据
     * @param dto
     */
    @Transactional
    public void handleIncomeMode(JijinExIncomeModeDTO dto) throws Exception{
        JijinInfoDTO jijinInfoDTO = jijinInfoRepository.findJijinInfoByFundCode(dto.getFundCode());
        if (jijinInfoDTO == null) {
            Logger.info(this, String.format("can not find jijinInfo with fundCode [%s],do nothing", dto.getFundCode()));
            jijinExIncomeModeRepository.updateJijinExIncomeMode(MapUtils.buildKeyValueMap("id", dto.getId(), "status", RecordStatus.NO_USED.toString()));
            return;
        } else if(JijinUtils.isHuoji(jijinInfoDTO)){
        	//货基不能修改分红方式
        	Logger.info(this, String.format(" fundCode [%s] isHouji == true, can not change inComeMode", dto.getFundCode()));
            jijinExIncomeModeRepository.updateJijinExIncomeMode(MapUtils.buildKeyValueMap("id", dto.getId(), "status", RecordStatus.NO_USED.toString()));
            return;
        }else {
            dto = jijinExIncomeModeRepository.getJijinExIncomeModeById(dto.getId());
            if (!dto.getStatus().equals(RecordStatus.NEW.toString())) {
                Logger.info(this, String.format("need not process for dispached record.JijinExIncomeMode Id = [%s]", dto.getId()));
                return;
            }

            JijinUpdateGson jijinUpdateGson = new JijinUpdateGson();
            jijinUpdateGson.setCode(jijinInfoDTO.getProductCode());

            //默认分红方式
            jijinInfoDTO.setDividendType(dto.getIncomeMode());
            jijinUpdateGson.setDividendMethod(dto.getIncomeMode());

            Map map = MapUtils.buildKeyValueMap("dividendType", jijinInfoDTO.getDividendType(), "fundCode", jijinInfoDTO.getFundCode());
            int i = jijinInfoRepository.updateJijinInfo(map);

            //更新Products
            BaseGson baseGson = jijinDaixiaoInfoService.updateProduct(jijinUpdateGson);

            //更新记录处理状态
            jijinExIncomeModeRepository.updateJijinExIncomeMode(MapUtils.buildKeyValueMap("id", dto.getId(), "status", RecordStatus.DISPACHED.toString()));

            if (!"000".equals(baseGson.getRetCode())) {
                //更新product失败
                Logger.error(this, String.format("更新product DividendMethod失败，JijinExIncomeMode id is[%s],fundCode is[%s]", dto.getId(), dto.getFundCode()));
                throw new RuntimeException("更新product fundType失败");
            }
        }
    }
}
