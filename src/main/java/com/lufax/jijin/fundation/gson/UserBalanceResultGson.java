package com.lufax.jijin.fundation.gson;

import java.math.BigDecimal;



public class UserBalanceResultGson extends BaseGson {
	
	//预期到帐时间
	private String estimateTime;
	//单位净值
	private BigDecimal netValue;
	//账户可余额
	private BigDecimal balanceAmount;
	//冻结部分
	private BigDecimal frozenAmount;
	//当前基金状态
	private String fundStatus;
	//基金类型 货基 1/非货基 0
    private String isHuoji;

	/*单笔赎回最低限额*/
	private BigDecimal singleRedeemMinAmount;
	/*单笔赎回最高限额*/
	private BigDecimal singleRedeemMaxAmount;
	/*最低持有限额*/
	private BigDecimal minHoldShareCount;
	/*业务类型Code*/
	private String bizCode;
	/*业务类型Name*/
	private String bizName;

	public String getIsHuoji() {
		return isHuoji;
	}

	public void setIsHuoji(String isHuoji) {
		this.isHuoji = isHuoji;
	}

	public String getFundStatus() {
		return fundStatus;
	}

	public void setFundStatus(String fundStatus) {
		this.fundStatus = fundStatus;
	}

	public BigDecimal getNetValue() {
		return netValue;
	}

	public void setNetValue(BigDecimal netValue) {
		this.netValue = netValue;
	}

	public BigDecimal getBalanceAmount() {
		return balanceAmount;
	}

	public void setBalanceAmount(BigDecimal balanceAmount) {
		this.balanceAmount = balanceAmount;
	}

	public BigDecimal getFrozenAmount() {
		return frozenAmount;
	}

	public void setFrozenAmount(BigDecimal frozenAmount) {
		this.frozenAmount = frozenAmount;
	}

	public String getEstimateTime() {
		return estimateTime;
	}

	public void setEstimateTime(String estimateTime) {
		this.estimateTime = estimateTime;
	}

	public BigDecimal getSingleRedeemMinAmount() {
		return singleRedeemMinAmount;
	}

	public void setSingleRedeemMinAmount(BigDecimal singleRedeemMinAmount) {
		this.singleRedeemMinAmount = singleRedeemMinAmount;
	}

	public BigDecimal getSingleRedeemMaxAmount() {
		return singleRedeemMaxAmount;
	}

	public void setSingleRedeemMaxAmount(BigDecimal singleRedeemMaxAmount) {
		this.singleRedeemMaxAmount = singleRedeemMaxAmount;
	}

	public String getBizCode() {
		return bizCode;
	}

	public void setBizCode(String bizCode) {
		this.bizCode = bizCode;
	}

	public String getBizName() {
		return bizName;
	}

	public void setBizName(String bizName) {
		this.bizName = bizName;
	}

	public BigDecimal getMinHoldShareCount() {
		return minHoldShareCount;
	}

	public void setMinHoldShareCount(BigDecimal minHoldShareCount) {
		this.minHoldShareCount = minHoldShareCount;
	}
}
