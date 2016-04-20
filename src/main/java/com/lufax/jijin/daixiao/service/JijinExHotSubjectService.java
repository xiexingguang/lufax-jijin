package com.lufax.jijin.daixiao.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lufax.jijin.base.utils.EmptyChecker;
import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.daixiao.constant.RecordStatus;
import com.lufax.jijin.daixiao.dto.JijinExHotSubjectDTO;
import com.lufax.jijin.daixiao.gson.JijinSubjectGson;
import com.lufax.jijin.daixiao.repository.JijinExHotSubjectRepository;
import com.lufax.jijin.daixiao.schedular.domain.JijinExHotSubjectSyncReader;
import com.lufax.jijin.fundation.constant.SyncFileStatus;
import com.lufax.jijin.fundation.dto.JijinInfoDTO;
import com.lufax.jijin.fundation.dto.JijinSyncFileDTO;
import com.lufax.jijin.fundation.gson.BaseGson;
import com.lufax.jijin.fundation.repository.JijinInfoRepository;
import com.lufax.jijin.fundation.repository.JijinSyncFileRepository;
import com.site.lookup.util.StringUtils;

@Service
public class JijinExHotSubjectService {

	@Autowired
    private JijinSyncFileRepository jijinSyncFielRepository;
	@Autowired
	private JijinExHotSubjectRepository jijinExHotSubjectRepository;
	@Autowired
	private JijinDaixiaoInfoService jijinDaixiaoInfoService;
	@Autowired
	private JijinInfoRepository jijinInfoRepository;
	
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
            List<JijinExHotSubjectDTO> dtos = new ArrayList<JijinExHotSubjectDTO>();
            JijinExHotSubjectSyncReader contentreader = new JijinExHotSubjectSyncReader();

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
                	JijinExHotSubjectDTO dto = contentreader.readLine(s, reader.getLineNumber(), syncFile);
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
    public void batchInsertSyncAndUpdateSyncFile(List<JijinExHotSubjectDTO> dtos, long lineNum, JijinSyncFileDTO syncFile) {
    	jijinExHotSubjectRepository.batchInsertJijinExHotSubject(dtos);
        jijinSyncFielRepository.updateBusJijinSyncFile(MapUtils.buildKeyValueMap("id", syncFile.getId(), "currentLine", lineNum));
    }
    
    @Transactional
    public void handleJijinExHotSubject(JijinExHotSubjectDTO dto) throws Exception{
    	List<JijinExHotSubjectDTO> dtoList = jijinExHotSubjectRepository.getJijinExHotSubjectsByBatchIdAndFundCode(dto.getBatchId(),dto.getFundCode());
    	if(EmptyChecker.isEmpty(dtoList) || !RecordStatus.NEW.name().equals(dtoList.get(0).getStatus())){
    		//已处理过的不再处理
    		return;
    	}
    	JijinInfoDTO jijin = jijinInfoRepository.findJijinInfoByFundCode(dto.getFundCode());
    	if(null == jijin){
    		//如果未生成基金信息，则不处理
    		jijinExHotSubjectRepository.batchUpdateHotSubjectStatusByBatchIdAndFundCode(dto.getBatchId(), dto.getFundCode(),RecordStatus.NO_USED.name(),null);
    		return;
    	}
    	jijinExHotSubjectRepository.batchUpdateHotSubjectStatusByBatchIdAndFundCode(dto.getBatchId(), dto.getFundCode(),RecordStatus.DISPACHED.name(),null);
    	List<JijinSubjectGson> gsonList = new ArrayList<JijinSubjectGson>();
    	for(JijinExHotSubjectDTO d : dtoList){
    		gsonList.add(new JijinSubjectGson(d));
    	}
    	BaseGson rt = jijinDaixiaoInfoService.sendSubjectToProduct(gsonList);
    	if(!"000".equals(rt.getRetCode())){
    		Logger.error(this, String.format("update hot subject error batchId=[%s] fundCode=[%s] ", dto.getBatchId(),dto.getFundCode() ));
    		throw new RuntimeException("update hot subject error");
    	}
    }
}
