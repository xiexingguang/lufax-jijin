package com.lufax.jijin.ylx.batch.dto;

import com.lufax.jijin.base.dto.YLXBaseDTO;

import java.util.Date;

public class YLXBatchDTO extends YLXBaseDTO {

	private String type;
	private Date targetDate;
	private String status;
	private String subNextStep;
	private long cutOffId;
	private String errorMsg;
	private int retryTimes;
	private String failToken;
	private Date triggerDate;
	private BatchRunStatus runStatus;
	private Long reqBatchId;

	public Long getReqBatchId() {
		return reqBatchId;
	}

	public void setReqBatchId(Long reqBatchId) {
		this.reqBatchId = reqBatchId;
	}

	public BatchRunStatus getRunStatus() {
		return runStatus;
	}

	public void setRunStatus(BatchRunStatus runStatus) {
		this.runStatus = runStatus;
	}

	public Date getTriggerDate() {
		return triggerDate;
	}

	public void setTriggerDate(Date triggerDate) {
		this.triggerDate = triggerDate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getTargetDate() {
		return targetDate;
	}

	public void setTargetDate(Date targeDate) {
		this.targetDate = targeDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSubNextStep() {
		return subNextStep;
	}

	public void setSubNextStep(String subNextStep) {
		this.subNextStep = subNextStep;
	}

	public long getCutOffId() {
		return cutOffId;
	}

	public void setCutOffId(long cutOffId) {
		this.cutOffId = cutOffId;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public int getRetryTimes() {
		return retryTimes;
	}

	public void setRetryTimes(int retryTimes) {
		this.retryTimes = retryTimes;
	}

	public String getFailToken() {
		return failToken;
	}

	public void setFailToken(String failToken) {
		this.failToken = failToken;
	}

}
