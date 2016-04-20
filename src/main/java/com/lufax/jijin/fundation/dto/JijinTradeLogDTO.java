package com.lufax.jijin.fundation.dto;

import com.lufax.jijin.base.dto.BaseDTO;
import com.lufax.jijin.fundation.constant.TradeRecordType;
import com.lufax.jijin.fundation.dto.builder.JijinTradeLogDTOBuilder;

import java.math.BigDecimal;
import java.util.Date;

public class JijinTradeLogDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 6846035820398355213L;

    private Long userId;
    private String fundCode;
    private String trxTime;
    private String trxDate;
    private Long tradeRecordId;
    private String status;
    private String dividendType;
    private TradeRecordType type;
    private BigDecimal amount;
    private BigDecimal reqShare;
    private BigDecimal fee;
    private String trxSerial;
    private String businessMode;
    private Date createdAt;
    private Date updatedAt;
    private String createdBy;
    private String updatedBy;

    //只可以set，来自于其他关联表
    private String channel;
    private String remark = "";
    private String confirmDate;//确认日期
    private String accountDate;//到账日期
    private String  expectConfirmDate;//预计确认日期
    private String  expectAccountDate;//预计到账日期

    public String getExpectAccountDate() {
        return expectAccountDate;
    }

    public void setExpectAccountDate(String expectAccountDate) {
        this.expectAccountDate = expectAccountDate;
    }

    public String getExpectConfirmDate() {
        return expectConfirmDate;
    }

    public void setExpectConfirmDate(String expectConfirmDate) {
        this.expectConfirmDate = expectConfirmDate;
    }

    public String getConfirmDate() {
        return confirmDate;
    }

    public void setConfirmDate(String confirmDate) {
        this.confirmDate = confirmDate;
    }

    public String getAccountDate() {
        return accountDate;
    }

    public void setAccountDate(String accountDate) {
        this.accountDate = accountDate;
    }

    public JijinTradeLogDTO() {
    }

    public JijinTradeLogDTO(Long userId, String fundCode, Long tradeRecordId, String status, TradeRecordType type, BigDecimal amount, String trxTime, String trxDate) {
        this.userId = userId;
        this.fundCode = fundCode;
        this.tradeRecordId = tradeRecordId;
        this.status = status;
        this.type = type;
        this.amount = amount;
        this.trxTime = trxTime;
        this.trxDate = trxDate;
    }

    public JijinTradeLogDTO(Long userId, String fundCode, Long tradeRecordId, String status, TradeRecordType type, BigDecimal amount, BigDecimal reqShare, String trxTime, String trxDate) {
        this.userId = userId;
        this.fundCode = fundCode;
        this.tradeRecordId = tradeRecordId;
        this.status = status;
        this.type = type;
        this.amount = amount;
        this.reqShare = reqShare;
        this.trxTime = trxTime;
        this.trxDate = trxDate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getTrxTime() {
        return trxTime;
    }

    public void setTrxTime(String trxTime) {
        this.trxTime = trxTime;
    }

    public String getTrxDate() {
        return trxDate;
    }

    public void setTrxDate(String trxDate) {
        this.trxDate = trxDate;
    }

    public Long getTradeRecordId() {
        return tradeRecordId;
    }

    public void setTradeRecordId(Long tradeRecordId) {
        this.tradeRecordId = tradeRecordId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDividendType() {
        return dividendType;
    }

    public void setDividendType(String dividendType) {
        this.dividendType = dividendType;
    }

    public TradeRecordType getType() {
        return type;
    }

    public void setType(TradeRecordType type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getReqShare() {
        return reqShare;
    }

    public void setReqShare(BigDecimal reqShare) {
        this.reqShare = reqShare;
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

    public String getTrxSerial() {
        return trxSerial;
    }

    public void setTrxSerial(String trxSerial) {
        this.trxSerial = trxSerial;
    }

    public static JijinTradeLogDTOBuilder create() {
        return new JijinTradeLogDTOBuilder();
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public String getBusinessMode() {
        return businessMode;
    }

    public void setBusinessMode(String businessMode) {
        this.businessMode = businessMode;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
