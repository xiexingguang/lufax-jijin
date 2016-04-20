package com.lufax.jijin.fundation.gson;



public class RedeemResultGson extends BaseGson {
	
	//预期到帐时间
	private String estimateTime;
	//赎回金额
	private String redeemAmount;
	//账户可余额
	private String availableAmount;
	

    private String memo;
    private String lockHours;
    private String lockRange;
    private String maxErrorTime;
    private String errorTime;
    private Long tradeRecordId;

	
	public Long getTradeRecordId() {
		return tradeRecordId;
	}

	public void setTradeRecordId(Long tradeRecordId) {
		this.tradeRecordId = tradeRecordId;
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

	public String getRedeemAmount() {
		return redeemAmount;
	}

	public void setRedeemAmount(String redeemAmount) {
		this.redeemAmount = redeemAmount;
	}

	public String getAvailableAmount() {
		return availableAmount;
	}

	public void setAvailableAmount(String availableAmount) {
		this.availableAmount = availableAmount;
	}

	public RedeemResultGson(){
		
	}

	public String getEstimateTime() {
		return estimateTime;
	}

	public void setEstimateTime(String estimateTime) {
		this.estimateTime = estimateTime;
	}
    
}
