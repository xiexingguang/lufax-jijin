package com.lufax.jijin.daixiao.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lufax.jijin.base.repository.BizParametersRepository;
import com.lufax.jijin.base.utils.ConstantsHelper;
import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.daixiao.constant.RecordStatus;
import com.lufax.jijin.daixiao.dto.JijinExMfPerformDTO;
import com.lufax.jijin.daixiao.gson.JijinUpdateGson;
import com.lufax.jijin.daixiao.repository.JijinExMfPerformRepository;
import com.lufax.jijin.daixiao.schedular.domain.JijinExMfPerformSyncReader;
import com.lufax.jijin.fundation.constant.SyncFileStatus;
import com.lufax.jijin.fundation.dto.JijinInfoDTO;
import com.lufax.jijin.fundation.dto.JijinSyncFileDTO;
import com.lufax.jijin.fundation.gson.BaseGson;
import com.lufax.jijin.fundation.repository.JijinInfoRepository;
import com.lufax.jijin.fundation.repository.JijinSyncFileRepository;
import com.site.lookup.util.StringUtils;
@Service
public class JijinExMfPerformService {

	@Autowired
    private JijinSyncFileRepository jijinSyncFielRepository;
	
	@Autowired
	private JijinExMfPerformRepository jijinExMfPerformRepository;
	@Autowired
	private JijinInfoRepository jijinInfoRepository;
	@Autowired
	private JijinDaixiaoInfoService jijinDaixiaoInfoService;
	@Autowired
	private BizParametersRepository bizParametersRepository;
	
	public void recordFileSync(JijinSyncFileDTO jijinSyncFileDTO) throws IOException {

        String fileName = jijinSyncFileDTO.getFileName(); // this file name includes path
        File sourceFile = new File(fileName);
        String switchParam = bizParametersRepository.findValueByCode(ConstantsHelper.JIJIN_DAIXIAO_INIT_SWITCH);
        dealFileWithBatchSize(sourceFile, jijinSyncFileDTO.getCurrentLine(), 200, jijinSyncFileDTO,switchParam);
        jijinSyncFielRepository.updateBusJijinSyncFile(MapUtils.buildKeyValueMap("id", jijinSyncFileDTO.getId(), "status", SyncFileStatus.READ_SUCCESS.name()));
    }
	
	public void dealFileWithBatchSize(File sourceFile, long startLine, long rownum, JijinSyncFileDTO syncFile,String switchParam) throws IOException {
        InputStreamReader in = new InputStreamReader(new FileInputStream(sourceFile), "UTF-8");
        LineNumberReader reader = new LineNumberReader(in);
        try {
            String s = null;
            List<JijinExMfPerformDTO> dtos = new ArrayList<JijinExMfPerformDTO>();
            JijinExMfPerformSyncReader contentreader = new JijinExMfPerformSyncReader();

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
                	JijinExMfPerformDTO dto = contentreader.readLine(s, reader.getLineNumber(), syncFile,switchParam);
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
    public void batchInsertSyncAndUpdateSyncFile(List<JijinExMfPerformDTO> dtos, long lineNum, JijinSyncFileDTO syncFile) {
    	jijinExMfPerformRepository.batchInsertJijinExMfPerform(dtos);
        jijinSyncFielRepository.updateBusJijinSyncFile(MapUtils.buildKeyValueMap("id", syncFile.getId(), "currentLine", lineNum));
    }
    
    @Transactional
    public void handleJijinExMfPerform(JijinExMfPerformDTO dto){
    	//是否需要更新至product
    	boolean isNeedSendToProduct = true;
    	//存在该日期以后的数据，则不更新至product
    	Integer count = jijinExMfPerformRepository.countNumberOfAfterDate(dto.getFundCode(), dto.getPerformanceDay());
    	if(null != count && count>0){
    		isNeedSendToProduct = false;
    	}
    	JijinInfoDTO info  = jijinInfoRepository.findJijinInfoByFundCode(dto.getFundCode());
    	if(null == info){
    		//基金未销售，不更新
    		isNeedSendToProduct = false;
    	}
    	if(!isNeedSendToProduct){
    		jijinExMfPerformRepository.updateJijinExMfPerformStatus(dto.getId(), RecordStatus.NO_USED.name(),1,null);
    	}else{
    		JijinUpdateGson gson = new JijinUpdateGson();
    		gson.setCode(info.getProductCode());
    		if(null != dto.getBenefitDaily()){
    			gson.setDayIncrease(dto.getBenefitDaily().divide(new BigDecimal("100")).toString());
	    	}
    		if(null != dto.getBenefitOneWeek()){
            	gson.setOneWeekIncrease(dto.getBenefitOneWeek().divide(new BigDecimal("100")));
            }
	    	if(null != dto.getBenefitOneMonth()){
	    		gson.setOneMonIncrease(dto.getBenefitOneMonth().divide(new BigDecimal("100")));
	    	}
	    	if(null != dto.getBenefitThreeMonth()){
	    		gson.setThreeMonIncrease(dto.getBenefitThreeMonth().divide(new BigDecimal("100")));
	    	}
	    	if(null != dto.getBenefitSixMonth()){
	    		gson.setSixMonIncrease(dto.getBenefitSixMonth().divide(new BigDecimal("100")));
	    	}
	    	if(null != dto.getBenefitOneYear()){
	    		gson.setTwelveMonIncrease(dto.getBenefitOneYear().divide(new BigDecimal("100")));
	    	}
	    	if(null != dto.getBenefitThisYear()){
	    		gson.setMoreTwelveMonIncrease(dto.getBenefitThisYear().divide(new BigDecimal("100")));
	    	}
	    	if(null != dto.getBenefitTotal()){
	    		gson.setTotalIncrease(dto.getBenefitTotal().divide(new BigDecimal("100")));
	    	}
	    	try {
	    		gson.setIncreaseUpdateDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new SimpleDateFormat("yyyyMMdd").parse(dto.getPerformanceDay())));
			} catch (ParseException e) {
				jijinExMfPerformRepository.updateJijinExMfPerformStatus(dto.getId(), RecordStatus.FAILED.name(),0,"PerformanceDay日期格式错误");
				Logger.error(JijinExMfPerformService.class, String.format("handle JijinExMfPerformDTO error ，id=[%s] PerformanceDay is not yyyymmdd but is[%s] ", dto.getId(),dto.getPerformanceDay()));
				return;
			}
	    	BaseGson rt = jijinDaixiaoInfoService.updateProduct(gson);
	    	if(!"000".equals(rt.getRetCode())){
        		//更新product失败
        		Logger.error(this, String.format("更新product净值增长率失败，JijinExMfPerformDTO id is[%s],fundCode is[%s]", dto.getId(),dto.getFundCode()));
        		throw new RuntimeException("调用list接口失败，错误信息：retCode="+rt.getRetCode()+","+rt.getRetMessage());
        	}else{
        		jijinExMfPerformRepository.updateJijinExMfPerformStatus(dto.getId(), RecordStatus.DISPACHED.name(),1,null);
        	}
    	}
    	//把相同日期\相同fundCode\且isValid为1的数据，将其isValid更新为0
    	jijinExMfPerformRepository.updateSameFundCodeDateRecordToNotVaild(dto.getId(), dto.getFundCode(), dto.getPerformanceDay());
    }
}
