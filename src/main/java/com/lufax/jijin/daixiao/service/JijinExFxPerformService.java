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

import com.lufax.jijin.base.repository.BizParametersRepository;
import com.lufax.jijin.base.utils.ConstantsHelper;
import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.daixiao.constant.RecordStatus;
import com.lufax.jijin.daixiao.dto.JijinExFxPerformDTO;
import com.lufax.jijin.daixiao.repository.JijinExFxPerformRepository;
import com.lufax.jijin.daixiao.schedular.domain.JijinExFxPerformSyncReader;
import com.lufax.jijin.fundation.constant.SyncFileStatus;
import com.lufax.jijin.fundation.dto.JijinSyncFileDTO;
import com.lufax.jijin.fundation.repository.JijinSyncFileRepository;
import com.site.lookup.util.StringUtils;
@Service
public class JijinExFxPerformService {

	@Autowired
    private JijinSyncFileRepository jijinSyncFielRepository;
	
	@Autowired
	private JijinExFxPerformRepository jijinExFxPerformRepository;
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
            List<JijinExFxPerformDTO> dtos = new ArrayList<JijinExFxPerformDTO>();
            JijinExFxPerformSyncReader contentreader = new JijinExFxPerformSyncReader();

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
                	JijinExFxPerformDTO dto = contentreader.readLine(s, reader.getLineNumber(), syncFile,switchParam);
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
    public void batchInsertSyncAndUpdateSyncFile(List<JijinExFxPerformDTO> dtos, long lineNum, JijinSyncFileDTO syncFile) {
    	jijinExFxPerformRepository.batchInserJijinExFxPerform(dtos);
        jijinSyncFielRepository.updateBusJijinSyncFile(MapUtils.buildKeyValueMap("id", syncFile.getId(), "currentLine", lineNum));
    }
    
    @Transactional
    public void handleJijinExPerform(JijinExFxPerformDTO dto){
    	jijinExFxPerformRepository.updateTheSameFindexCodeAndDateToNotValid(dto.getId(), dto.getFindexCode(), dto.getPerformanceDay());
    	jijinExFxPerformRepository.updateJijinExFxPerform(MapUtils.buildKeyValueMap("id",dto.getId(),"status",RecordStatus.DISPACHED,"isValid",1));
    }
}
