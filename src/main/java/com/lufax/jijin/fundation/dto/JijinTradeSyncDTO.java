package com.lufax.jijin.fundation.dto;

import com.lufax.jijin.base.dto.BaseDTO;

import java.math.BigDecimal;
import java.util.Date;

public class JijinTradeSyncDTO extends BaseDTO {

    private Long fileId;
    private String contractNo;
    private String taNo;
    private String appSheetNo;
    private String lufaxRequestNo;
    private String fundCompanyCode;
    private String trxDate;
    private String trxTime;
    private String trxConfirmDate;
    private String businessCode;
    private String fundCode;
    private String chargeType;
    private BigDecimal netValue;
    private BigDecimal purchaseAmount;
    private BigDecimal redeemShare;
    private BigDecimal confirmShare;
    private BigDecimal confirmAmount;
    private String dividentType;
    private String tradeResCode;
    private String tradeResMemo;
    private String taConfirmSign;
    private String businessFinishFlag;
    private BigDecimal purchaseFee;
    private String status;
    private Date createdAt;
    private Date updatedAt;
    private String createdBy;
    private String updatedBy;
    private String memo;

    public JijinTradeSyncDTO() {
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getTaNo() {
        return taNo;
    }

    public void setTaNo(String taNo) {
        this.taNo = taNo;
    }

    public String getAppSheetNo() {
        return appSheetNo;
    }

    public void setAppSheetNo(String appSheetNo) {
        this.appSheetNo = appSheetNo;
    }

    public String getLufaxRequestNo() {
        return lufaxRequestNo;
    }

    public void setLufaxRequestNo(String lufaxRequestNo) {
        this.lufaxRequestNo = lufaxRequestNo;
    }

    public String getFundCompanyCode() {
        return fundCompanyCode;
    }

    public void setFundCompanyCode(String fundCompanyCode) {
        this.fundCompanyCode = fundCompanyCode;
    }

    public String getTrxDate() {
        return trxDate;
    }

    public void setTrxDate(String trxDate) {
        this.trxDate = trxDate;
    }

    public String getTrxTime() {
        return trxTime;
    }

    public void setTrxTime(String trxTime) {
        this.trxTime = trxTime;
    }

    public String getTrxConfirmDate() {
        return trxConfirmDate;
    }

    public void setTrxConfirmDate(String trxConfirmDate) {
        this.trxConfirmDate = trxConfirmDate;
    }

    public String getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
    }

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getChargeType() {
        return chargeType;
    }

    public void setChargeType(String chargeType) {
        this.chargeType = chargeType;
    }

    public BigDecimal getNetValue() {
        return netValue;
    }

    public void setNetValue(BigDecimal netValue) {
        this.netValue = netValue;
    }

    public BigDecimal getPurchaseAmount() {
        return purchaseAmount;
    }

    public void setPurchaseAmount(BigDecimal purchaseAmount) {
        this.purchaseAmount = purchaseAmount;
    }

    public BigDecimal getRedeemShare() {
        return redeemShare;
    }

    public void setRedeemShare(BigDecimal redeemShare) {
        this.redeemShare = redeemShare;
    }

    public BigDecimal getConfirmShare() {
        return confirmShare;
    }

    public void setConfirmShare(BigDecimal confirmShare) {
        this.confirmShare = confirmShare;
    }

    public BigDecimal getConfirmAmount() {
        return confirmAmount;
    }

    public void setConfirmAmount(BigDecimal confirmAmount) {
        this.confirmAmount = confirmAmount;
    }

    public String getDividentType() {
        return dividentType;
    }

    public void setDividentType(String dividentType) {
        this.dividentType = dividentType;
    }

    public String getTradeResCode() {
        return tradeResCode;
    }

    public void setTradeResCode(String tradeResCode) {
        this.tradeResCode = tradeResCode;
    }

    public String getTradeResMemo() {
        return tradeResMemo;
    }

    public void setTradeResMemo(String tradeResMemo) {
        this.tradeResMemo = tradeResMemo;
    }

    public String getTaConfirmSign() {
        return taConfirmSign;
    }

    public void setTaConfirmSign(String taConfirmSign) {
        this.taConfirmSign = taConfirmSign;
    }

    public String getBusinessFinishFlag() {
        return businessFinishFlag;
    }

    public void setBusinessFinishFlag(String businessFinishFlag) {
        this.businessFinishFlag = businessFinishFlag;
    }

    public BigDecimal getPurchaseFee() {
        return purchaseFee;
    }

    public void setPurchaseFee(BigDecimal purchaseFee) {
        this.purchaseFee = purchaseFee;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}



