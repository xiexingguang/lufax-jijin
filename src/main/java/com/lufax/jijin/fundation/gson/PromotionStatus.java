package com.lufax.jijin.fundation.gson;

/**
 * 基金活动信息
 * @author chenqunhui168
 *
 */
public class PromotionStatus {

	
	private String fundCode;
	private String status;//0：活动进行中 1：活动结束
	private String actualEndTime;
	
	public String getFundCode() {
		return fundCode;
	}
	public void setFundCode(String fundCode) {
		this.fundCode = fundCode;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getActualEndTime() {
		return actualEndTime;
	}
	public void setActualEndTime(String actualEndTime) {
		this.actualEndTime = actualEndTime;
	}
	
	
}
