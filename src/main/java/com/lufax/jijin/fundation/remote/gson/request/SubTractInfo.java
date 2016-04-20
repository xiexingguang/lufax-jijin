package com.lufax.jijin.fundation.remote.gson.request;

import java.math.BigDecimal;

public class SubTractInfo {
	
	private String businessRefNo;
	private BigDecimal minusAmount;
	private String minusTransactionType;
	private String minusRemark;
	private String vendorCode;
	private String productCode;
	private String trxDate; //yyyymmdd
	
	public SubTractInfo(String businessRefNo, BigDecimal minusAmount, String minusTransactionType,String minusRemark,String vendorCode, String productCode,String trxDate){
		this.businessRefNo = businessRefNo;
		this.minusAmount = minusAmount;
		this.minusTransactionType = minusTransactionType;
		this.minusRemark = minusRemark;
		this.vendorCode = vendorCode;
		this.productCode = productCode;
		this.trxDate = trxDate;
	}
	
	public String getBusinessRefNo() {
		return businessRefNo;
	}
	public void setBusinessRefNo(String businessRefNo) {
		this.businessRefNo = businessRefNo;
	}
	public BigDecimal getMinusAmount() {
		return minusAmount;
	}
	public void setMinusAmount(BigDecimal minusAmount) {
		this.minusAmount = minusAmount;
	}
	public String getMinusTransactionType() {
		return minusTransactionType;
	}
	public void setMinusTransactionType(String minusTransactionType) {
		this.minusTransactionType = minusTransactionType;
	}
	public String getMinusRemark() {
		return minusRemark;
	}
	public void setMinusRemark(String minusRemark) {
		this.minusRemark = minusRemark;
	}
	public String getVendorCode() {
		return vendorCode;
	}
	public void setVendorCode(String vendorCode) {
		this.vendorCode = vendorCode;
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
