package com.lufax.jijin.base.dto;

import java.math.BigDecimal;
import com.lufax.jijin.ylx.dto.YLXAccountTransferRecordDTO;

public class TransferRefundParam {

    private Long fromUserId;
    private BigDecimal transferredAmount;
    private Long toUserId;
    private String fromTransactionType;
    private String toTransactionType;
    private String fromRemark;
    private String toRemark;
    private Long recordId;
    private String channelId;

    public TransferRefundParam() {}
    
    public TransferRefundParam(YLXAccountTransferRecordDTO dto) {
    	this.recordId = Long.parseLong(dto.getRecordId());
		this.fromUserId = dto.getFromUserId();
		this.toUserId = dto.getToUserId();
		this.fromTransactionType = dto.getFromTransactionType();
		this.toTransactionType = dto.getToTransactionType();
		this.transferredAmount = dto.getTotalAmount();
		this.fromRemark = dto.getFromRemark();
		this.toRemark = dto.getToRemark();
		this.channelId = "JIJIN";
    }

    public Long getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(Long fromUserId) {
        this.fromUserId = fromUserId;
    }

    public BigDecimal getTransferredAmount() {
		return transferredAmount;
	}

	public void setTransferredAmount(BigDecimal transferredAmount) {
		this.transferredAmount = transferredAmount;
	}

	public Long getToUserId() {
        return toUserId;
    }

    public void setToUserId(Long toUserId) {
        this.toUserId = toUserId;
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

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }
}
