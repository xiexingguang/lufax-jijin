package com.lufax.jijin.ylx.dto;


import java.math.BigDecimal;
import java.util.Date;

import com.lufax.jijin.base.dto.BaseDTO;


@SuppressWarnings("serial")
public class YLXAccountTransferRecordDTO extends BaseDTO {
    private String recordId;
    private Long fromUserId;
    private Long toUserId;
    private String fromTransactionType;
    private String toTransactionType;
    private BigDecimal totalAmount;
    private String fromRemark;
    private String toRemark;
    private Integer status;
    private String channelId;
    private Long version;
    private Date createdAt;
    private Date updatedAt;
    private String createdBy;
    private String updatedBy;
    private Long sellRequestId;
    
    
	public YLXAccountTransferRecordDTO() {}
    public YLXAccountTransferRecordDTO(String recordId,
                                       Long fromUserId,
                                       Long toUserId,
                                       String fromTransactionType,
                                       String toTransactionType,
                                       BigDecimal totalAmount,
                                       String fromRemark,
                                       String toRemark,
                                       Integer status,
                                       String channelId) {
		super();
		this.recordId = recordId;
		this.fromUserId = fromUserId;
		this.toUserId = toUserId;
		this.fromTransactionType = fromTransactionType;
		this.toTransactionType = toTransactionType;
		this.totalAmount = totalAmount;
		this.fromRemark = fromRemark;
		this.toRemark = toRemark;
		this.status = status;
		this.channelId = channelId;
	}
	public String getRecordId() {
        return recordId;
    }
	public Long getSellRequestId() {
		return sellRequestId;
	}
	public void setSellRequestId(Long sellRequestId) {
		this.sellRequestId = sellRequestId;
	}
    public Long getFromUserId() {
        return fromUserId;
    }

    public Long getToUserId() {
        return toUserId;
    }

    public String getFromTransactionType() {
        return fromTransactionType;
    }

    public String getChannelId() {
		return channelId;
	}
	public String getToTransactionType() {
        return toTransactionType;
    }

    public String getFromRemark() {
        return fromRemark;
    }

    public String getToRemark() {
        return toRemark;
    }

    public Integer getStatus() {
        return status;
    }

    public Long getVersion() {
        return version;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public YLXAccountTransferRecordDTO addVersion() {
		this.version = this.version + 1;
		return this;
	}
    
	public YLXAccountTransferRecordDTO setStatus(Integer status) {
		this.status = status;
		return this;
	}
	public String getUpdatedBy() {
        return updatedBy;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
}
