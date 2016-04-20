package com.lufax.jijin.daixiao.dto;

import com.lufax.jijin.base.dto.BaseDTO;

/**
 * Created by chenguang451 on 2016/1/6.
 */
public class JijinExAssetConfDTO extends BaseDTO {

    private String fundCode;

    private String endDate;

    private String assetValue;

    private String stockPer;

    private String bondPer;

    private String cashPer;

    private String otherPer;

    private String nationalDebtPer;

    private String finanicialBondPer;

    private String enterpriseBondPer;
    
    private String centralBankBillPer;

    private Long batchId;

    private Long isValid = 1L;

    private String status = "DISPATCHED";

    private String errorMsg;

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getAssetValue() {
        return assetValue;
    }

    public void setAssetValue(String assetValue) {
        this.assetValue = assetValue;
    }

    public String getStockPer() {
        return stockPer;
    }

    public void setStockPer(String stockPer) {
        this.stockPer = stockPer;
    }

    public String getBondPer() {
        return bondPer;
    }

    public void setBondPer(String bondPer) {
        this.bondPer = bondPer;
    }

    public String getCashPer() {
        return cashPer;
    }

    public void setCashPer(String cashPer) {
        this.cashPer = cashPer;
    }

    public String getOtherPer() {
        return otherPer;
    }

    public void setOtherPer(String otherPer) {
        this.otherPer = otherPer;
    }

    public String getNationalDebtPer() {
        return nationalDebtPer;
    }

    public void setNationalDebtPer(String nationalDebtPer) {
        this.nationalDebtPer = nationalDebtPer;
    }

    public String getFinanicialBondPer() {
        return finanicialBondPer;
    }

    public void setFinanicialBondPer(String finanicialBondPer) {
        this.finanicialBondPer = finanicialBondPer;
    }

    public String getEnterpriseBondPer() {
        return enterpriseBondPer;
    }

    public void setEnterpriseBondPer(String enterpriseBondPer) {
        this.enterpriseBondPer = enterpriseBondPer;
    }

    public Long getBatchId() {
        return batchId;
    }

    public void setBatchId(Long batchId) {
        this.batchId = batchId;
    }

    public Long getIsValid() {
        return isValid;
    }

    public void setIsValid(Long isValid) {
        this.isValid = isValid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

	public String getCentralBankBillPer() {
		return centralBankBillPer;
	}

	public void setCentralBankBillPer(String centralBankBillPer) {
		this.centralBankBillPer = centralBankBillPer;
	}
}
