package com.lufax.jijin.ylx.batch.dto;


import com.lufax.jijin.base.dto.YLXBaseDTO;

import java.math.BigDecimal;
import java.util.Date;

public class YLXBatchFileDTO extends YLXBaseDTO {

	private long batchId;
	private String fileName;
	private String status; // init, created,sent,received
	private String orgCode;
	private Date trxDate; // yyyyMMdd
	private Long total;
	private BigDecimal amount;
	private Long successTotal;
	private BigDecimal successAmount;
	private Long abnormalTotal;
	private String version;
	private int retryTimes;
	private Long currentLine;
	private String returnCode;
	private String fileId;

	public long getBatchId() {
		return batchId;
	}

	public void setBatchId(long batchId) {
		this.batchId = batchId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public Date getTrxDate() {
		return trxDate;
	}

	public void setTrxDate(Date trxDate) {
		this.trxDate = trxDate;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Long getSuccessTotal() {
		return successTotal;
	}

	public void setSuccessTotal(Long successTotal) {
		this.successTotal = successTotal;
	}

	public BigDecimal getSuccessAmount() {
		return successAmount;
	}

	public void setSuccessAmount(BigDecimal successAmount) {
		this.successAmount = successAmount;
	}

	public Long getAbnormalTotal() {
		return abnormalTotal;
	}

	public void setAbnormalTotal(Long abnormalTotal) {
		this.abnormalTotal = abnormalTotal;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public int getRetryTimes() {
		return retryTimes;
	}

	public void setRetryTimes(int retryTimes) {
		this.retryTimes = retryTimes;
	}

	public Long getCurrentLine() {
		return currentLine;
	}

	public long getCurrentLineNotNull() {
		if(currentLine == null){
			return 0;
		}
		return currentLine;
	}
	
	public void setCurrentLine(Long currentLine) {
		this.currentLine = currentLine;
	}

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

}
