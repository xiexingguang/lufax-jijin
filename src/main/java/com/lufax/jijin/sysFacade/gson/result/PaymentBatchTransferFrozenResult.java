package com.lufax.jijin.sysFacade.gson.result;

import java.util.List;

public class PaymentBatchTransferFrozenResult {
	
	private String res_code;
	
	private String res_msg;
	
	private String status;
	
	private String channelId;
	
	private String instructionNo;
	
	private String responseNo;
	
	private List<FrozenInfo> frozenInfoList;


	public List<FrozenInfo> getFrozenInfoList() {
		return frozenInfoList;
	}

	public void setFrozenInfoList(List<FrozenInfo> frozenInfoList) {
		this.frozenInfoList = frozenInfoList;
	}

	public String getStatus() {
		return status;
	}

	public String getChannelId() {
		return channelId;
	}

	public String getInstructionNo() {
		return instructionNo;
	}

	public String getResponseNo() {
		return responseNo;
	}



	public void setStatus(String status) {
		this.status = status;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public void setInstructionNo(String instructionNo) {
		this.instructionNo = instructionNo;
	}

	public void setResponseNo(String responseNo) {
		this.responseNo = responseNo;
	}


	/**
	 * 是否成功
	 * @return
	 */
	public boolean isSuccess(){
		return "SUCCESS".equals(status);
	}
	
	/**
	 * 执行中
	 * @return
	 */
	public boolean isProcessing(){
		return "PROCESSING".equals(status);
	}

	public String getRes_code() {
		return res_code;
	}

	public String getRes_msg() {
		return res_msg;
	}

	public void setRes_code(String res_code) {
		this.res_code = res_code;
	}

	public void setRes_msg(String res_msg) {
		this.res_msg = res_msg;
	}


}
