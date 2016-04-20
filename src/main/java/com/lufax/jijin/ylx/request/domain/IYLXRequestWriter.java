package com.lufax.jijin.ylx.request.domain;

import com.lufax.jijin.ylx.batch.dto.YLXBatchDTO;
import com.lufax.jijin.ylx.batch.dto.YLXBatchFileDTO;

import java.io.File;
import java.math.BigDecimal;


public interface IYLXRequestWriter {
	
	public static final int ROWNUM = 500; // batchSize
	public BigDecimal writeRequestContent(File newFile, YLXBatchFileDTO batchFile, long start, long end, long total, YLXBatchDTO batch, int fileSize);
	public long getTotalRecords(long batchId);

}
