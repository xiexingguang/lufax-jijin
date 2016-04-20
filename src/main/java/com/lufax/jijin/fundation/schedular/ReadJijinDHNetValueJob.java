package com.lufax.jijin.fundation.schedular;

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
import org.apache.commons.lang.StringUtils;

import com.lufax.jersey.utils.Logger;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.fundation.constant.DHFileType;
import com.lufax.jijin.fundation.constant.SyncFileStatus;
import com.lufax.jijin.fundation.dto.JijinNetValueDTO;
import com.lufax.jijin.fundation.dto.JijinSyncFileDTO;
import com.lufax.jijin.fundation.dto.JijinUserBalanceAuditDTO;
import com.lufax.jijin.fundation.repository.JijinNetValueRepository;
import com.lufax.jijin.fundation.repository.JijinSyncFileRepository;
import com.lufax.jijin.fundation.schedular.domain.JijinDHNetValueReader;
import com.lufax.jijin.fundation.schedular.domain.JijinDHProfitReader;

/**
 * 大华净值读取job
 * @author chenqunhui
 *
 */
@Service
public class ReadJijinDHNetValueJob extends BaseBatchWithLimitJob<JijinSyncFileDTO, Long> {

	@Autowired
	private JijinSyncFileRepository jijinSyncFielRepository;
	@Autowired
	private JijinNetValueRepository jijinNetValueRepository;
	
	@Override
	protected Long getKey(JijinSyncFileDTO item) {
		return item.getId();
	}

	@Override
	protected List<JijinSyncFileDTO> fetchList(int batchAmount) {
		// TODO Auto-generated method stub
		return jijinSyncFielRepository.findBusJijinSyncFileList(MapUtils.buildKeyValueMap("bizType", DHFileType.DH_NET_VALUE.name(), "status", SyncFileStatus.READY.name(), "limit", batchAmount));
	}
	@Override
    public void process(JijinSyncFileDTO jijinSyncFileDTO) {
		try {
            Logger.info(this, String.format("Start to read file :[id: %s] [fileName : %s]", jijinSyncFileDTO.getId(), jijinSyncFileDTO.getFileName()));
            recordDHNetValueSync(jijinSyncFileDTO);
            Logger.info(this, String.format("Success read file :[id: %s] [fileName : %s]", jijinSyncFileDTO.getId(), jijinSyncFileDTO.getFileName()));
        } catch (Exception e) {
            Logger.error(this, String.format("Read file :[id: %s] [fileName : %s] failed !", jijinSyncFileDTO.getId(), jijinSyncFileDTO.getFileName()));
            jijinSyncFielRepository.updateBusJijinSyncFile(MapUtils.buildKeyValueMap("id", jijinSyncFileDTO.getId(), "status", SyncFileStatus.READ_FAIL.name(), "memo", e.getClass().getName()));
        }
    }
	
	private void recordDHNetValueSync(JijinSyncFileDTO jijinSyncFileDTO) throws IOException {
        String fileName = jijinSyncFileDTO.getFileName(); // this file name includes path
        fileName= fileName.replace("upload","download").replace("zip","txt");
        File sourceFile = new File(fileName);
        dealFileWithBatchSize(sourceFile, jijinSyncFileDTO.getCurrentLine(), 200, jijinSyncFileDTO);
        jijinSyncFielRepository.updateBusJijinSyncFile(MapUtils.buildKeyValueMap("id", jijinSyncFileDTO.getId(), "status", SyncFileStatus.READ_SUCCESS.name()));
    }
	
	 /**
     * 从起始行读取文件，并安输入行数批量插入数据库
     * <p/>
     * 注意：起始行不能以0开始，否则边界会出错，必须以1起始
     *
     * @param sourceFile
     * @param startLine
     * @param rownum
     * @throws java.io.IOException
     */
    protected void dealFileWithBatchSize(File sourceFile, long startLine, long rownum, JijinSyncFileDTO syncFile) throws IOException {

        InputStreamReader in = new InputStreamReader(new FileInputStream(sourceFile), "GBK");
        LineNumberReader reader = new LineNumberReader(in);
        try {
            String s = null;
            List<JijinNetValueDTO> dtos = new ArrayList<JijinNetValueDTO>();
            JijinDHNetValueReader contentreader = new JijinDHNetValueReader();

            do {
                s = reader.readLine();
                if (StringUtils.isEmpty(s)) {// 文件读取完毕,插入最后一批数据
                    Logger.info(this, String.format("read Huoji income file  - insert last batch records into DB endline:%s]", reader.getLineNumber() - 1));
                    if (dtos.size() > 0) {
                        batchInsertSyncAndUpdateSyncFile(dtos, reader.getLineNumber(), syncFile);
                    }
                    break;
                }
                if (reader.getLineNumber() >= startLine && reader.getLineNumber() < startLine + rownum) {
                    // 解析行，转换成DTO
                	JijinNetValueDTO dto = contentreader.readLine(s, reader.getLineNumber(), syncFile);
                    if (dto != null)
                        dtos.add(dto);
                }

                if (reader.getLineNumber() == startLine + rownum - 1) {// 已达到批次记录集，进行插数据库，清空缓存，并set下一个startLine
                    Logger.info(this, String.format("read da hua Huoji net value file - insert into DB endline:%s]", reader.getLineNumber()));
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
    public void batchInsertSyncAndUpdateSyncFile(List<JijinNetValueDTO> dtos, long lineNum, JijinSyncFileDTO syncFile) {
    	jijinNetValueRepository.batchInsertJijinNetValues(dtos);
    	jijinSyncFielRepository.updateBusJijinSyncFile(MapUtils.buildKeyValueMap("id",syncFile.getId(),"currentLine",lineNum));
    }
}
