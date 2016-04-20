package com.lufax.jijin.user.dto;

public class SLPRedeemResult{
	
	private String ret_code;
	private String ret_msg;
	
	private String memo;
    private String lockHours;
    private String lockRange;
    private String maxErrorTime;
    private String errorTime;
    
	public SLPRedeemResult(String ret_code, String ret_msg) {
		this.ret_code = ret_code;
		this.ret_msg = ret_msg;
	}
	public String getRet_code() {
		return ret_code;
	}
	public String getRet_msg() {
		return ret_msg;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getLockHours() {
		return lockHours;
	}
	public void setLockHours(String lockHours) {
		this.lockHours = lockHours;
	}
	public String getLockRange() {
		return lockRange;
	}
	public void setLockRange(String lockRange) {
		this.lockRange = lockRange;
	}
	public String getMaxErrorTime() {
		return maxErrorTime;
	}
	public void setMaxErrorTime(String maxErrorTime) {
		this.maxErrorTime = maxErrorTime;
	}
	public String getErrorTime() {
		return errorTime;
	}
	public void setErrorTime(String errorTime) {
		this.errorTime = errorTime;
	}
	public void setRet_code(String ret_code) {
		this.ret_code = ret_code;
	}
	public void setRet_msg(String ret_msg) {
		this.ret_msg = ret_msg;
	}
	
	
}
