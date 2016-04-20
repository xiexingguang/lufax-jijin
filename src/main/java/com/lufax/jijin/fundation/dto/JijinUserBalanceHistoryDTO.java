package com.lufax.jijin.fundation.dto;

import com.lufax.jijin.base.dto.BaseDTO;

import java.math.BigDecimal;
import java.util.Date;

public class JijinUserBalanceHistoryDTO extends BaseDTO {

    /* 陆金所用户id */
    private Long userId;
    /* 基金产品代码 */
    private String fundCode;
    /* 帐号资金（可赎回部分） */
    private BigDecimal balance;
    /* 冻结资金 （赎回冻结） */
    private BigDecimal frozenAmount;
    /* 基金份额（可赎回部分） */
    private BigDecimal shareBalance;
    /* 冻结份额 */
    private BigDecimal frozenShare;
    /* 申购在途资金  */
    private BigDecimal applyingAmount;
    private String dividendType;
    private String dividendStatus; //分红状态

	private BigDecimal increase=new BigDecimal("0");
    private BigDecimal decrease=new BigDecimal("0");
    private String remark;
    
    /* 创建时间 */
    private Date createdAt;
    /* 更新时间 */
    private Date updatedAt;
    /* 创建人 */
    private String createdBy;
    /* 更新人 */
    private String updatedBy;
    private String bizDate;
    
    private String bizType;
    
    private Long tradeRecordId;
    
    private Date trxTime;//业务发起时间
    
    public String getBizDate() {
		return bizDate;
	}

	public void setBizDate(String bizDate) {
		this.bizDate = bizDate;
	}

	public BigDecimal getIncrease() {
		return increase;
	}

	public void setIncrease(BigDecimal increase) {
		this.increase = increase;
	}

	public BigDecimal getDecrease() {
		return decrease;
	}

	public void setDecrease(BigDecimal decrease) {
		this.decrease = decrease;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}


	

    public JijinUserBalanceHistoryDTO() {
    }

    public void addShare(BigDecimal increase){
    	if(this.shareBalance == null){
    		this.shareBalance = new BigDecimal(0);
    	}
    	this.shareBalance = this.shareBalance.add(increase);
    }
    
    public void addBalance(BigDecimal increase){
    	this.balance = this.balance.add(increase);
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

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getFrozenAmount() {
        return frozenAmount;
    }

    public void setFrozenAmount(BigDecimal frozenAmount) {
        this.frozenAmount = frozenAmount;
    }

    public BigDecimal getShareBalance() {
        return shareBalance;
    }

    public void setShareBalance(BigDecimal shareBalance) {
        this.shareBalance = shareBalance;
    }

    public BigDecimal getFrozenShare() {
        return frozenShare;
    }

    public void setFrozenShare(BigDecimal frozenShare) {
        this.frozenShare = frozenShare;
    }

    public String getDividendType() {
        return dividendType;
    }

    public void setDividendType(String dividendType) {
        this.dividendType = dividendType;
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

    public BigDecimal getApplyingAmount() {
        return applyingAmount;
    }

    public void setApplyingAmount(BigDecimal applyingAmount) {
        this.applyingAmount = applyingAmount;
    }
    
    public String getDividendStatus() {
		return dividendStatus;
	}

	public void setDividendStatus(String dividendStatus) {
		this.dividendStatus = dividendStatus;
	}

	public Long getTradeRecordId() {
		return tradeRecordId;
	}

	public void setTradeRecordId(Long tradeRecordId) {
		this.tradeRecordId = tradeRecordId;
	}

	public String getBizType() {
		return bizType;
	}

	public void setBizType(String bizType) {
		this.bizType = bizType;
	}

	public Date getTrxTime() {
		return trxTime;
	}

	public void setTrxTime(Date trxTime) {
		this.trxTime = trxTime;
	}



	
}
