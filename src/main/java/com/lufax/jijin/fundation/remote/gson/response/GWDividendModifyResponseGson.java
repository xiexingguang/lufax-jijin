package com.lufax.jijin.fundation.remote.gson.response;


/**
 * 申请分红修改调用jijin-gw的返回值
 * @author chenqunhui
 *
 */
public class GWDividendModifyResponseGson extends GWBaseResponseGson {
;
	
	private String appSheetSerialNo;
	
	private String transactionDate;
	
	private String transactionTime;


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
	
}
