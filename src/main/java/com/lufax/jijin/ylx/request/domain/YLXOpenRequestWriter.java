package com.lufax.jijin.ylx.request.domain;

import com.lufax.jijin.base.utils.FileUtils;
import com.lufax.jijin.ylx.batch.domain.BatchFileContentUtil;
import com.lufax.jijin.ylx.batch.domain.YLXBatchType;
import com.lufax.jijin.ylx.batch.dto.YLXBatchDTO;
import com.lufax.jijin.ylx.batch.dto.YLXBatchFileDTO;
import com.lufax.jijin.ylx.dto.YLXOpenRequestDetailDTO;
import com.lufax.jijin.ylx.dto.repository.YLXOpenRequestDetailRepository;
import com.lufax.jijin.ylx.util.YlxConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;

@Component
public class YLXOpenRequestWriter implements IYLXRequestWriter{

	@Autowired
	private YLXOpenRequestDetailRepository ylxOpenRequestDetailRepository;

	@Override
	public BigDecimal writeRequestContent(File newFile, YLXBatchFileDTO batchFile,
			long start, long end, long total, YLXBatchDTO batch,int fileSize) {
		FileUtils.appendHeader(newFile, YlxConstants.OPEN_FILE_TYPE, batchFile.getVersion(),batchFile.getOrgCode(), batchFile.getTrxDate(), total,null);
		FileUtils.appendContent(newFile, BatchFileContentUtil.createColumnName(YLXBatchType.YLX_SLP_OPEN_REQUEST));
		while (start <= end) {
			
			long targetEnd =0;
			if(fileSize<ROWNUM){
				targetEnd = start + fileSize;
			}else{
				targetEnd =  start + ROWNUM;
			}
			List<YLXOpenRequestDetailDTO> dtos = ylxOpenRequestDetailRepository.getYLXOpenRequestDTOsByBatchId(batch.getId(), start, targetEnd);
			String content = "";
			for (YLXOpenRequestDetailDTO dto : dtos) {
				content = BatchFileContentUtil.createOpenRequestContent(dto,batch);
				FileUtils.appendContent(newFile, content);
			}

			start = targetEnd;
		}
		
		return new BigDecimal(0);
	}
	
	@Override
	public long getTotalRecords(long batchId) {
       	
     	return ylxOpenRequestDetailRepository.countOpenRequestBybatchId(batchId);
    	
	}

}
