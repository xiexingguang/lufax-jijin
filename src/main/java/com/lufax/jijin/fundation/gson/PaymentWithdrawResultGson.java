package com.lufax.jijin.fundation.gson;

import java.math.BigDecimal;

/**
 * 
 * @author XUNENG311
 *
 */
public class PaymentWithdrawResultGson {
	
	
	private String res_code;
	private String res_msg;
	private String prepareStatus;//准备状态：SUCCESS成功；FAILURE失败。isPrepareRequired为'1'时会返回。取现失败，准备成功时资金会留在余额（解冻状态），由业务系统决定后续操作。
	private String status; //SUCCESS成功；FAILURE失败
	private String channelId;
    private String instructionNo;
    private String type;
    private BigDecimal requestAmout;
    private BigDecimal actualAmount;
    private String pbbCode;//失败时的具体错误码
    private String pbbMsg;//失败时的具体错误信息
    
	public String getRes_code() {
		return res_code;
	}
	public void setRes_code(String res_code) {
		this.res_code = res_code;
	}
	public String getRes_msg() {
		return res_msg;
	}
	public void setRes_msg(String res_msg) {
		this.res_msg = res_msg;
	}
	public String getPrepareStatus() {
		return prepareStatus;
	}
	public void setPrepareStatus(String prepareStatus) {
		this.prepareStatus = prepareStatus;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public String getInstructionNo() {
		return instructionNo;
	}
	public void setInstructionNo(String instructionNo) {
		this.instructionNo = instructionNo;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public BigDecimal getRequestAmout() {
		return requestAmout;
	}
	public void setRequestAmout(BigDecimal requestAmout) {
		this.requestAmout = requestAmout;
	}
	public BigDecimal getActualAmount() {
		return actualAmount;
	}
	public void setActualAmount(BigDecimal actualAmount) {
		this.actualAmount = actualAmount;
	}
	public String getPbbCode() {
		return pbbCode;
	}
	public void setPbbCode(String pbbCode) {
		this.pbbCode = pbbCode;
	}
	public String getPbbMsg() {
		return pbbMsg;
	}
	public void setPbbMsg(String pbbMsg) {
		this.pbbMsg = pbbMsg;
	}
   
}
