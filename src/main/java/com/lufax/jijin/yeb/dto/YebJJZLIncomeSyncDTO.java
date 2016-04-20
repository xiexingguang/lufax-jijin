package com.lufax.jijin.yeb.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.lufax.jijin.base.dto.BaseDTO;

public class YebJJZLIncomeSyncDTO extends BaseDTO {


    private String fundCode;
    
    private Long userId;
    
    private String incomeDate;
    /* 基金份额 */
    private BigDecimal amount;
    /* 昨日收益 */
    private BigDecimal yesterdayYield;
    /* 累计收益 */
    private BigDecimal totalYield;
    /* 未结收益 */
    private BigDecimal unpayYield;
    /* 处理状态 1-new 2-processing 3-success 4-fail */
    private String status;
    /* 创建时间 */
    private Date createdAt;
    /* 修改时间 */
    private Date updatedAt;
    /* 创建人 */
    private String createdBy;
    /* 修改人 */
    private String updatedBy;

    public YebJJZLIncomeSyncDTO() {
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getYesterdayYield() {
        return yesterdayYield;
    }

    public void setYesterdayYield(BigDecimal yesterdayYield) {
        this.yesterdayYield = yesterdayYield;
    }

    public BigDecimal getTotalYield() {
        return totalYield;
    }

    public void setTotalYield(BigDecimal totalYield) {
        this.totalYield = totalYield;
    }

    public BigDecimal getUnpayYield() {
        return unpayYield;
    }

    public void setUnpayYield(BigDecimal unpayYield) {
        this.unpayYield = unpayYield;
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

	public String getFundCode() {
		return fundCode;
	}

	public void setFundCode(String fundCode) {
		this.fundCode = fundCode;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getIncomeDate() {
		return incomeDate;
	}

	public void setIncomeDate(String incomeDate) {
		this.incomeDate = incomeDate;
	}

}
