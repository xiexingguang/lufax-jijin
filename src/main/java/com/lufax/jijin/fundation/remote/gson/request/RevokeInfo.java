package com.lufax.jijin.fundation.remote.gson.request;


public class RevokeInfo {
	
	private String businessRefNo;
	private String originalBusinessRefNo;
	private String plusTransactionType;
	private String plusRemark;
	private String vendorCode;
	private String fromTransactionType;
	private String toTransactionType;
	private String fromRemark;
	private String toRemark;
	private String productCode;
	private String trxDate; //yyyyMMdd
	
	public RevokeInfo(String businessRefNo, String originalBusinessRefNo, String plusTransactionType, String plusRemark, String vendorCode,
			String fromTransactionType, String toTransactionType, String fromRemark, String toRemark,String productCode,String trxDate){
		this.businessRefNo = businessRefNo;
		this.originalBusinessRefNo = originalBusinessRefNo;
		this.plusTransactionType = plusTransactionType;
		this.plusRemark = plusRemark;
		this.vendorCode = vendorCode;
		this.fromTransactionType = fromTransactionType;
		this.toTransactionType = toTransactionType;
		this.fromRemark = fromRemark;
		this.toRemark = toRemark;
		this.productCode = productCode;
		this.trxDate = trxDate;
		
	}
	
	public String getBusinessRefNo() {
		return businessRefNo;
	}
	public void setBusinessRefNo(String businessRefNo) {
		this.businessRefNo = businessRefNo;
	}
	public String getOriginalBusinessRefNo() {
		return originalBusinessRefNo;
	}
	public void setOriginalBusinessRefNo(String originalBusinessRefNo) {
		this.originalBusinessRefNo = originalBusinessRefNo;
	}

	public String getPlusTransactionType() {
		return plusTransactionType;
	}
	public void setPlusTransactionType(String plusTransactionType) {
		this.plusTransactionType = plusTransactionType;
	}
	public String getPlusRemark() {
		return plusRemark;
	}
	public void setPlusRemark(String plusRemark) {
		this.plusRemark = plusRemark;
	}
	public String getVendorCode() {
		return vendorCode;
	}
	public void setVendorCode(String vendorCode) {
		this.vendorCode = vendorCode;
	}
	public String getFromTransactionType() {
		return fromTransactionType;
	}
	public void setFromTransactionType(String fromTransactionType) {
		this.fromTransactionType = fromTransactionType;
	}
	public String getToTransactionType() {
		return toTransactionType;
	}
	public void setToTransactionType(String toTransactionType) {
		this.toTransactionType = toTransactionType;
	}
	public String getFromRemark() {
		return fromRemark;
	}
	public void setFromRemark(String fromRemark) {
		this.fromRemark = fromRemark;
	}
	public String getToRemark() {
		return toRemark;
	}
	public void setToRemark(String toRemark) {
		this.toRemark = toRemark;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getTrxDate() {
		return trxDate;
	}
	public void setTrxDate(String trxDate) {
		this.trxDate = trxDate;
	}

}
