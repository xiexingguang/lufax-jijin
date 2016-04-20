package com.lufax.jijin.fundation.remote.gson.response;

public class GWRedeemResponseGson extends GWBaseResponseGson{

	private String appSheetSerialNo; //基金公司申请单据流水号
	private String transactionDate; // 交易所属日期 yyymmdd
	private String transactionTime; // 交易发生时间 基金公司订单落地时间 yyyymmddHHmmss
	private String totalVol;
	private String frozenVol;

	public String getAppSheetSerialNo() {
		return appSheetSerialNo;
	}
	public void setAppSheetSerialNo(String appSheetSerialNo) {
		this.appSheetSerialNo = appSheetSerialNo;
	}
	public String getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}
	public String getTransactionTime() {
		return transactionTime;
	}
	public void setTransactionTime(String transactionTime) {
		this.transactionTime = transactionTime;
	}
	public String getTotalVol() {
		return totalVol;
	}
	public void setTotalVol(String totalVol) {
		this.totalVol = totalVol;
	}
	public String getFrozenVol() {
		return frozenVol;
	}
	public void setFrozenVol(String frozenVol) {
		this.frozenVol = frozenVol;
	}
	
}
