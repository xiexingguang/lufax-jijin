package com.lufax.jijin.daixiao.service;

import com.google.gson.Gson;
import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.daixiao.constant.JijinExFundBizEnum;
import com.lufax.jijin.daixiao.constant.RecordStatus;
import com.lufax.jijin.daixiao.dto.JijinExBuyLimitDTO;
import com.lufax.jijin.daixiao.gson.JijinUpdateGson;
import com.lufax.jijin.daixiao.repository.JijinExBuyLimitRepository;
import com.lufax.jijin.daixiao.schedular.domain.JijinExBuyLimitSyncReader;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by NiuZhanJun on 7/23/15.
 */
@Service
public class JijinExBuyLimitService {
    @Autowired
    private JijinExBuyLimitRepository jijinExBuyLimitRepository;
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
            List<JijinExBuyLimitDTO> dtos = new ArrayList<JijinExBuyLimitDTO>();
            JijinExBuyLimitSyncReader contentreader = new JijinExBuyLimitSyncReader();

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
                    JijinExBuyLimitDTO dto = contentreader.readLine(s, reader.getLineNumber(), syncFile);
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
    public void batchInsertSyncAndUpdateSyncFile(List<JijinExBuyLimitDTO> dtos, long lineNum, JijinSyncFileDTO syncFile) {
        jijinExBuyLimitRepository.batchInsertJijinExBuyLimit(dtos);
        jijinSyncFielRepository.updateBusJijinSyncFile(MapUtils.buildKeyValueMap("id", syncFile.getId(), "currentLine", lineNum));
    }

    /**
     * 处理基金购买限制数据
     *
     * @param dto
     */
    @Transactional
    public void handleBuyLimit(JijinExBuyLimitDTO dto) throws Exception {
        JijinInfoDTO jijinInfoDTO = jijinInfoRepository.findJijinInfoByFundCode(dto.getFundCode());
        if (jijinInfoDTO == null) {
            Logger.info(this, String.format("can not find jijinInfo with fundCode [%s],do nothing", dto.getFundCode()));
            jijinExBuyLimitRepository.updateJijinExBuyLimit(MapUtils.buildKeyValueMap("id", dto.getId(), "status", RecordStatus.NO_USED.toString()));
            return;
        } else {
            dto = jijinExBuyLimitRepository.getJijinExBuyLimitById(dto.getId());
            if (!dto.getStatus().equals(RecordStatus.NEW.toString())) {
                Logger.info(this, String.format("need not process for not new status record.JiJinExBuyLimit Id = [%s]", dto.getId()));
                return;
            }
            JijinUpdateGson jijinUpdateGson = new JijinUpdateGson();
            jijinUpdateGson.setCode(jijinInfoDTO.getProductCode());
            //申购数据同步到jijinInfo和Products
            if (JijinExFundBizEnum.申购.getBizCode().equals(dto.getBizCode())) {
                //更新jijinInfoDto
                jijinInfoDTO = addToJijinInfoDto(jijinInfoDTO, dto);
                //更新到jijinUpdateGson
                jijinUpdateGson.setIsBuyDailyLimit(jijinInfoDTO.getIsBuyDailyLimit());
                jijinUpdateGson.setBuyDailyLimit(jijinInfoDTO.getBuyDailyLimit());
                jijinUpdateGson.setMinInvestAmount(jijinInfoDTO.getMinInvestAmount());
                jijinUpdateGson.setMaxInvestAmount(jijinInfoDTO.getMaxInvestAmount());
                jijinUpdateGson.setIncreaseInvestAmount(jijinInfoDTO.getIncreaseInvestAmount());
                //更新JijinInfo
                Map map = MapUtils.buildKeyValueMap("fundCode", dto.getFundCode(),
                        "isBuyDailyLimit", jijinInfoDTO.getIsBuyDailyLimit(),
                        "buyDailyLimit", jijinInfoDTO.getBuyDailyLimit(),
                        "minInvestAmount", jijinInfoDTO.getMinInvestAmount());
                int i = jijinInfoRepository.updateJijinInfo(map);
            } else if(JijinExFundBizEnum.认购.getBizCode().equals(dto.getBizCode())){
            	//认购只更新认购起点
                jijinUpdateGson.setMinSubAmount(dto.getFirstInvestMinAmount());
            } else if(JijinExFundBizEnum.定投.getBizCode().equals(dto.getBizCode())){
            	//定投只更新定投起点
            	jijinUpdateGson.setMinFixAmount(dto.getFirstInvestMinAmount());
            } else {
            	//其他类型直接更新为无效数据
	            Logger.info(this, String.format("need not process except sell biz.JiJinExBuyLimit Id = [%s]", dto.getId()));
	            jijinExBuyLimitRepository.updateJijinExBuyLimit(MapUtils.buildKeyValueMap("id", dto.getId(), "status", RecordStatus.NO_USED.toString()));
	            return;
	        }
            //更新Products
            BaseGson baseGson = jijinDaixiaoInfoService.updateProduct(jijinUpdateGson);
            //更新记录处理状态
            jijinExBuyLimitRepository.updateJijinExBuyLimit(MapUtils.buildKeyValueMap("id", dto.getId(), "status", RecordStatus.DISPACHED.toString()));
            if (!"000".equals(baseGson.getRetCode())) {
                //更新product失败
                Logger.error(this, String.format("更新product Buy Limit失败，JiJinExBuyLimit id is[%s],fundCode is[%s]", dto.getId(), dto.getFundCode()));
                throw new RuntimeException("更新product 认申购起点失败");
            }
        }
    }

    public JijinInfoDTO addToJijinInfoDto(JijinInfoDTO jijinInfoDTO, JijinExBuyLimitDTO jijinExBuyLimitDTO) {
        if (jijinInfoDTO != null) {
            String isBuyDailyLimit = "0";
            BigDecimal buydailylimit = BigDecimal.ZERO;
            //JijinInfo/Products：购买单日限额
            if (jijinExBuyLimitDTO != null && jijinExBuyLimitDTO.getInvestDailyLimit() != null
                    && jijinExBuyLimitDTO.getInvestDailyLimit().compareTo(BigDecimal.ZERO) > 0) {
                isBuyDailyLimit = "1";
                buydailylimit = jijinExBuyLimitDTO.getInvestDailyLimit();
            }
            jijinInfoDTO.setIsBuyDailyLimit(isBuyDailyLimit);
            jijinInfoDTO.setBuyDailyLimit(buydailylimit);

            //JijinInfo/Products：最小投资金额 使用“首次投资最小/最大限额”
            if (jijinExBuyLimitDTO != null && jijinExBuyLimitDTO.getFirstInvestMinAmount() != null) {
                jijinInfoDTO.setMinInvestAmount(jijinExBuyLimitDTO.getFirstInvestMinAmount());
            }else{
                jijinInfoDTO.setMinInvestAmount(BigDecimal.ZERO);
            }

            //Products：最大投资金额、级差
            BigDecimal maxInvestAmount = new BigDecimal("999999999999");
            if (jijinExBuyLimitDTO != null && jijinExBuyLimitDTO.getFirstInvestMaxAmount() != null
                    && jijinExBuyLimitDTO.getFirstInvestMaxAmount().compareTo(BigDecimal.ZERO) > 0) {
                maxInvestAmount = jijinExBuyLimitDTO.getFirstInvestMaxAmount();
            }
            jijinInfoDTO.setMaxInvestAmount(maxInvestAmount);

            if (jijinExBuyLimitDTO != null && jijinExBuyLimitDTO.getRaisedAmount() != null) {
                jijinInfoDTO.setIncreaseInvestAmount(jijinExBuyLimitDTO.getRaisedAmount());
            } else {
                jijinInfoDTO.setIncreaseInvestAmount(BigDecimal.ZERO);
            }

        }
        return jijinInfoDTO;
    }
}
