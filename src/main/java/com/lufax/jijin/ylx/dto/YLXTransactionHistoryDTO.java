package com.lufax.jijin.ylx.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.lufax.jijin.base.dto.YLXBaseDTO;

public class YLXTransactionHistoryDTO extends YLXBaseDTO {

    /*交易流水号*/
    private long serialNo;
    /* 交易类型认购/申购/赎回 */
    private String transactionType;
    /* 用户id */
    private long userId;
    /* 产品code */
    private String productCode;
    /* 金额 */
    private BigDecimal transactionAmount;
    /*份额*/
    private BigDecimal fundShare;
    /* 备注 */
    private String remark;
    
    private Date createdAt;
    private Date updatedAt;
    private String createdBy;
    private String updatedBy;
    private long version;
    
    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(BigDecimal transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public BigDecimal getFundShare() {
        return fundShare;
    }

    public void setFundShare(BigDecimal fundShare) {
        this.fundShare = fundShare;
    }

    public long getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(long serialNo) {
        this.serialNo = serialNo;
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

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}
}
