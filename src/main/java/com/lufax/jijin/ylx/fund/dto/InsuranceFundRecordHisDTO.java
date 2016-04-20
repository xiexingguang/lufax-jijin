package com.lufax.jijin.ylx.fund.dto;
import com.lufax.jijin.base.dto.BaseDTO;

import java.math.BigDecimal;
import java.util.Date;

public class InsuranceFundRecordHisDTO extends BaseDTO {
    private Long productId;
    private Long fromUserId;
    private String toCardId;
    private int type;
    private BigDecimal amount;
    private String remark;
    private String recordId;
    private Integer status;
    private Date createdAt;
    private String createdBy;
    private Integer insuranceType;
    private String fundDate;
    public InsuranceFundRecordHisDTO() {
    }

    public InsuranceFundRecordHisDTO(Long productId, Long fromUserId, String toCardId, int type, BigDecimal amount, String remark, String recordId, Integer status, Integer insuranceType, String fundDate) {
        this.productId = productId;
        this.fromUserId = fromUserId;
        this.toCardId = toCardId;
        this.type = type;
        this.amount = amount;
        this.remark = remark;
        this.recordId = recordId;
        this.status = status;
        this.createdAt = new Date();
        this.createdBy = "SYS";
        this.insuranceType = insuranceType;
        this.fundDate = fundDate;
    }


    public InsuranceFundRecordHisDTO(InsuranceFundRecordDTO faInsuranceFundRecordDTO, Integer status) {
        this.productId = faInsuranceFundRecordDTO.getProductId();
        this.fromUserId = faInsuranceFundRecordDTO.getFromUserId();
        this.toCardId = faInsuranceFundRecordDTO.getToCardId();
        this.type = faInsuranceFundRecordDTO.getType();
        this.amount = faInsuranceFundRecordDTO.getAmount();
        this.remark = faInsuranceFundRecordDTO.getRemark();
        this.recordId = faInsuranceFundRecordDTO.getRecordId();
        this.status = status;
        this.createdAt = new Date();
        this.createdBy = "SYS";
        this.insuranceType = faInsuranceFundRecordDTO.getInsuranceType();
        this.fundDate = faInsuranceFundRecordDTO.getFundDate();
    }

    public Long getProductId() {
        return productId;
    }

    public Long getFromUserId() {
        return fromUserId;
    }

    public String getToCardId() {
        return toCardId;
    }

    public int getType() {
        return type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getRemark() {
        return remark;
    }

    public String getRecordId() {
        return recordId;
    }

    public Integer getStatus() {
        return status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public Integer getInsuranceType() {
        return insuranceType;
    }

	public String getFundDate() {
		return fundDate;
	}

	public void setFundDate(String fundDate) {
		this.fundDate = fundDate;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public void setFromUserId(Long fromUserId) {
		this.fromUserId = fromUserId;
	}

	public void setToCardId(String toCardId) {
		this.toCardId = toCardId;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public void setInsuranceType(Integer insuranceType) {
		this.insuranceType = insuranceType;
	}
    
}
