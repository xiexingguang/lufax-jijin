package com.lufax.jijin.fundation.dto;


import com.lufax.jijin.base.dto.BaseDTO;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 大华交易确认信息，包含申购/赎回
 */
public class JijinTradeConfirmDTO extends BaseDTO {

    private Long fileId;
    private String payNo;     //平安付签约协议号
    private String contractNo; //基金公司交易帐号
    private String applyDate;  //申请日期
    private String lufaxRequestNo;//陆金所订单号
    private String appSheetNo; //基金公司申请单号
    private String bizType;//业务类型
    private String fundCode; //基金代码
    private String confirmDate; //确认日期
    private BigDecimal amount;  //金额
    private BigDecimal confirmShare; //确认份额
    private String companyConfirmNo; //基金公司确认单号
    private String tradeResCode; //返回码
    private String tradeResMemo; //失败原因
    private String status;
    private Date createdAt;
    private Date updatedAt;
    private String createdBy;
    private String updatedBy;

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

    public String getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(String applyDate) {
        this.applyDate = applyDate;
    }

    public String getLufaxRequestNo() {
        return lufaxRequestNo;
    }

    public void setLufaxRequestNo(String lufaxRequestNo) {
        this.lufaxRequestNo = lufaxRequestNo;
    }

    public String getAppSheetNo() {
        return appSheetNo;
    }

    public void setAppSheetNo(String appSheetNo) {
        this.appSheetNo = appSheetNo;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getConfirmDate() {
        return confirmDate;
    }

    public void setConfirmDate(String confirmDate) {
        this.confirmDate = confirmDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getConfirmShare() {
        return confirmShare;
    }

    public void setConfirmShare(BigDecimal confirmShare) {
        this.confirmShare = confirmShare;
    }

    public String getCompanyConfirmNo() {
        return companyConfirmNo;
    }

    public void setCompanyConfirmNo(String companyConfirmNo) {
        this.companyConfirmNo = companyConfirmNo;
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

    public String getPayNo() {
        return payNo;
    }

    public void setPayNo(String payNo) {
        this.payNo = payNo;
    }
}
