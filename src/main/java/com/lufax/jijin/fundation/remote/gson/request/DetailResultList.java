package com.lufax.jijin.fundation.remote.gson.request;

import java.math.BigDecimal;

public class DetailResultList {
	
	private String businessRefNo;
	private BigDecimal clearingAmount;
	private String trxDate; //yyyyMMdd
	private String vendorCode;
	private String transactionFlow;
	private String productCode;
	
	public DetailResultList(String businessRefNo, BigDecimal clearingAmount, String trxDate, String vendorCode, String transactionFlow,String productCode){
		this.businessRefNo = businessRefNo;
		this.clearingAmount = clearingAmount;
		this.trxDate = trxDate;
		this.vendorCode = vendorCode;
		this.transactionFlow = transactionFlow;
		this.productCode = productCode;
	}
	
	public String getBusinessRefNo() {
		return businessRefNo;
	}
	public void setBusinessRefNo(String businessRefNo) {
		this.businessRefNo = businessRefNo;
	}
	public BigDecimal getClearingAmount() {
		return clearingAmount;
	}
	public void setClearingAmount(BigDecimal clearingAmount) {
		this.clearingAmount = clearingAmount;
	}
	public String getTrxDate() {
		return trxDate;
	}
	public void setTrxDate(String trxDate) {
		this.trxDate = trxDate;
	}
	public String getVendorCode() {
		return vendorCode;
	}
	public void setVendorCode(String vendorCode) {
		this.vendorCode = vendorCode;
	}
	public String getTransactionFlow() {
		return transactionFlow;
	}
	public void setTransactionFlow(String transactionFlow) {
		this.transactionFlow = transactionFlow;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	
	

}
