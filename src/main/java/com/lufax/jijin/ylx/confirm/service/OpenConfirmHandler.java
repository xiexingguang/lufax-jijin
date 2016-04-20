package com.lufax.jijin.ylx.confirm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.ylx.batch.dto.YLXBatchFileDTO;
import com.lufax.jijin.ylx.batch.repository.YLXBatchFileRepository;
import com.lufax.jijin.ylx.batch.repository.YLXBatchRepository;
import com.lufax.jijin.ylx.dto.OpenConfirmDetailDTO;
import com.lufax.jijin.ylx.dto.repository.OpenConfirmDetailRepository;

@Service
@Transactional
public class OpenConfirmHandler {
	
    @Autowired
    private OpenConfirmDetailRepository openConfirmDetailDAO;
    @Autowired
    private YLXBatchFileRepository ylxBatchFileRepository;
    @Autowired
    private YLXBatchRepository ylxBatchRepository;
    /**
     * 将确认文件记录插入ylx_open_confirm,更新BatchFile记录文件处理到当前行
     * @param dtos
     * @param lineNum
     * @param batchFile
     */
    public void batchInsertOpenConfirm(List<OpenConfirmDetailDTO> dtos, long lineNum, YLXBatchFileDTO batchFile) {
    	openConfirmDetailDAO.batchInsert("insert", dtos);
    	ylxBatchFileRepository.update("updateCurrentLineById",
				MapUtils.buildKeyValueMap("id", batchFile.getId(), "currentLine", lineNum));
    }
}
