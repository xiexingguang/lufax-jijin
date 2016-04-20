package com.lufax.jijin.sysFacade.gson;

public class RevokeClearInfo {

	private String businessRefNo;
	private String originalBusinessRefNo;
	private String tradingDate;
	private String vendorCode;

	public String getBusinessRefNo() {
		return businessRefNo;
	}
	public String getOriginalBusinessRefNo() {
		return originalBusinessRefNo;
	}
	public String getTradingDate() {
		return tradingDate;
	}
	public String getVendorCode() {
		return vendorCode;
	}
	public void setBusinessRefNo(String businessRefNo) {
		this.businessRefNo = businessRefNo;
	}
	public void setOriginalBusinessRefNo(String originalBusinessRefNo) {
		this.originalBusinessRefNo = originalBusinessRefNo;
	}
	public void setTradingDate(String tradingDate) {
		this.tradingDate = tradingDate;
	}
	public void setVendorCode(String vendorCode) {
		this.vendorCode = vendorCode;
	}

}
