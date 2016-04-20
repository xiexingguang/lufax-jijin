package com.lufax.jijin.daixiao.service;


import com.lufax.jijin.base.repository.BizParametersRepository;
import com.lufax.jijin.base.utils.ConstantsHelper;
import com.lufax.jijin.base.utils.DateUtils;
import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.daixiao.constant.JijinExValidEnum;
import com.lufax.jijin.daixiao.constant.RecordStatus;
import com.lufax.jijin.daixiao.dto.JijinExNetValueDTO;
import com.lufax.jijin.daixiao.dto.JijinExYieldRateDTO;
import com.lufax.jijin.daixiao.gson.JijinUpdateGson;
import com.lufax.jijin.daixiao.repository.JijinExNetValueRepository;
import com.lufax.jijin.daixiao.repository.JijinExYieldRateRepository;
import com.lufax.jijin.daixiao.schedular.domain.JijinExNetValueReader;
import com.lufax.jijin.daixiao.schedular.domain.JijinExYieldRateReader;
import com.lufax.jijin.fundation.constant.SyncFileStatus;
import com.lufax.jijin.fundation.dto.JijinInfoDTO;
import com.lufax.jijin.fundation.dto.JijinSyncFileDTO;
import com.lufax.jijin.fundation.gson.BaseGson;
import com.lufax.jijin.fundation.repository.JijinInfoRepository;
import com.lufax.jijin.fundation.repository.JijinSyncFileRepository;
import com.site.lookup.util.StringUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class JijinExYieldRateService {

    @Autowired
    private JijinExYieldRateRepository jijinExYieldRateRepository;
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
        try {
            String s = null;
            List<JijinExYieldRateDTO> dtos = new ArrayList<JijinExYieldRateDTO>();
            JijinExYieldRateReader contentreader = new JijinExYieldRateReader();

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
                	JijinExYieldRateDTO dto = contentreader.readLine(s, reader.getLineNumber(), syncFile);
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

    public void handleJijinExYieldRate(JijinExYieldRateDTO dto) {
    	//是否需要更新至product
    	boolean isNeedSendToProduct = true;
        JijinInfoDTO info = jijinInfoRepository.findJijinInfoByFundCode(dto.getFundCode());
        if(null == info){
    		//基金未销售，不更新
    		isNeedSendToProduct = false;
    	}
        if(isNeedSendToProduct){
        	//存在该日期以后的数据，则不更新至product
        	Integer count = jijinExYieldRateRepository.countNumberOfAfterDate(dto.getFundCode(), dto.getEndDate());
        	if(null != count && count>0){
        		isNeedSendToProduct = false;
        	}
        }
    	if(!isNeedSendToProduct){
    		jijinExYieldRateRepository.updateJijinExYieldRateStatus(dto.getId(), RecordStatus.NO_USED.name(),1,null);
    	}else{
    		JijinUpdateGson gson = new JijinUpdateGson();
    		gson.setCode(info.getProductCode());
    		gson.setBenefitPerTenThousand(dto.getBenefitPerTenthousand());
    		if(null != dto.getInterestratePerSevenday()){
    			gson.setInterestRatePerSevenDay(dto.getInterestratePerSevenday().divide(new BigDecimal("100")));
    		}
    		gson.setProfitEndDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(DateUtils.parseDate(dto.getEndDate(), DateUtils.DATE_STRING_FORMAT)));

	    	BaseGson rt = jijinDaixiaoInfoService.updateProduct(gson);
	    	if(!"000".equals(rt.getRetCode())){
        		//更新product失败
        		Logger.error(this, String.format("update product yield rate failed ，JijinExMfPerformDTO id is[%s],fundCode is[%s]", dto.getId(),dto.getFundCode()));
        		throw new RuntimeException("调用list接口失败，错误信息：retCode="+rt.getRetCode()+","+rt.getRetMessage());
        	}else{
        		jijinExYieldRateRepository.updateJijinExYieldRateStatus(dto.getId(), RecordStatus.DISPACHED.name(),1,null);
        	}
    	}
    	//把相同日期\相同fundCode\且isValid为1的数据，将其isValid更新为0
    	jijinExYieldRateRepository.updateSameFundCodeDateRecordToNotVaild(dto.getId(), dto.getFundCode(), dto.getEndDate());
    }


    @Transactional
    public void batchInsertSyncAndUpdateSyncFile(List<JijinExYieldRateDTO> dtos, long lineNum, JijinSyncFileDTO syncFile) {
    	jijinExYieldRateRepository.batchInsertJijinExYieldRate(dtos);
        jijinSyncFileRepository.updateBusJijinSyncFile(MapUtils.buildKeyValueMap("id", syncFile.getId(), "currentLine", lineNum));
    }
}
