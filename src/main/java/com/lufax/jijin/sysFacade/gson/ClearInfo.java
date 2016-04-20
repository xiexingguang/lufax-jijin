package com.lufax.jijin.sysFacade.gson;


public class ClearInfo{
	public ClearInfo(String businessRefNo, String vendorCode,String tradingDate) {
		this.businessRefNo = businessRefNo;
		this.vendorCode = vendorCode;
		this.tradingDate = tradingDate;
	}
	private String businessRefNo;//业务编号（业务流水号） - 该字段用于后续清算（核销）和对账，对应于原有的bankRefId	 
	private String vendorCode;//供应商代码（基金公司代码）
	private String tradingDate;
}