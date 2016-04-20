package com.lufax.jijin.daixiao.service;

import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.daixiao.constant.JijinExFundTypeEnum;
import com.lufax.jijin.daixiao.constant.RecordStatus;
import com.lufax.jijin.daixiao.dto.JijinExFundTypeDTO;
import com.lufax.jijin.daixiao.gson.JijinUpdateGson;
import com.lufax.jijin.daixiao.repository.JijinExFundTypeRepository;
import com.lufax.jijin.daixiao.schedular.domain.JijinExFundTypeSyncReader;
import com.lufax.jijin.fundation.constant.FundSaleCode;
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
public class JijinExFundTypeService {

    @Autowired
    private JijinSyncFileRepository jijinSyncFielRepository;

    @Autowired
    private JijinExFundTypeRepository jijinExFundTypeRepository;
    @Autowired
    private JijinInfoRepository jijinInfoRepository;
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
            List<JijinExFundTypeDTO> dtos = new ArrayList<JijinExFundTypeDTO>();
            JijinExFundTypeSyncReader contentreader = new JijinExFundTypeSyncReader();

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
                    JijinExFundTypeDTO dto = contentreader.readLine(s, reader.getLineNumber(), syncFile);
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
    public void batchInsertSyncAndUpdateSyncFile(List<JijinExFundTypeDTO> dtos, long lineNum, JijinSyncFileDTO syncFile) {
        jijinExFundTypeRepository.batchInserJijinExFundType(dtos);
        jijinSyncFielRepository.updateBusJijinSyncFile(MapUtils.buildKeyValueMap("id", syncFile.getId(), "currentLine", lineNum));
    }

    /**
     * 处理基金类型数据
     *
     * @param dto
     */
    @Transactional
    public void handleFundType(JijinExFundTypeDTO dto) throws Exception {
    	JijinInfoDTO info = jijinInfoRepository.findJijinInfoByFundCode(dto.getFundCode());
    	if(null != info && FundSaleCode.LFX.getInstId().equals(info.getInstId())){
    		//更新jijinInfo
            int i = jijinInfoRepository.updateJijinInfo(MapUtils.buildKeyValueMap("fundCode", dto.getFundCode(), "fundType", JijinExFundTypeEnum.getByFundCode(dto.getFundType()).getTypeName()));
            if (i == 1) {
                //更新products
                JijinUpdateGson product = new JijinUpdateGson();
                product.setCode(info.getProductCode());
                product.setSubType(JijinExFundTypeEnum.getByFundCode(dto.getFundType()).getTypeName());
                BaseGson rt = jijinDaixiaoInfoService.updateProduct(product);
                if (!"000".equals(rt.getRetCode())) {
                    //更新product失败
                    Logger.error(this, String.format("更新product fundType失败，JijinExFundTypeDTO id is[%s],fundCode is[%s]", dto.getId(), dto.getFundCode()));
                    throw new RuntimeException("调用list接口失败，错误信息：retCode=" + rt.getRetCode() + "," + rt.getRetMessage());
                }
                jijinExFundTypeRepository.updateJijinExFundTypeStatus(dto.getId(), RecordStatus.DISPACHED.name(), null);
            } else {
                //没有jijinInfo信息的不更新
                jijinExFundTypeRepository.updateJijinExFundTypeStatus(dto.getId(), RecordStatus.NO_USED.name(), null);
            }
    	}else{
    		jijinExFundTypeRepository.updateJijinExFundTypeStatus(dto.getId(), RecordStatus.NO_USED.name(), null);
    	}
        


    }
}
