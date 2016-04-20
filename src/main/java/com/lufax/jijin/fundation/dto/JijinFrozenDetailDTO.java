package com.lufax.jijin.fundation.dto;


import com.lufax.jijin.base.dto.BaseDTO;

import java.math.BigDecimal;
import java.util.Date;

public class JijinFrozenDetailDTO extends BaseDTO {

    private String transactionType;
    private Long userBalanceId;
    private BigDecimal frozenAmount;
    private BigDecimal unFrozenAmount;
    private BigDecimal totalFrozenAmount;
    private String remark;
    private Date createdAt;
    private Date updatedAt;
    private String createdBy;
    private String updatedBy;

    public JijinFrozenDetailDTO() {
    }
    
    public JijinFrozenDetailDTO(String transactionType, Long userBalanceId, BigDecimal frozenAmount, BigDecimal unFrozenAmount, BigDecimal totalFrozenAmount, String remark) {
        this.transactionType = transactionType;
        this.userBalanceId = userBalanceId;
        this.frozenAmount = frozenAmount;
        this.unFrozenAmount = unFrozenAmount;
        this.totalFrozenAmount = totalFrozenAmount;
        this.remark = remark;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public Long getUserBalanceId() {
        return userBalanceId;
    }

    public void setUserBalanceId(Long userBalanceId) {
        this.userBalanceId = userBalanceId;
    }

    public BigDecimal getFrozenAmount() {
        return frozenAmount;
    }

    public void setFrozenAmount(BigDecimal frozenAmount) {
        this.frozenAmount = frozenAmount;
    }

    public BigDecimal getUnFrozenAmount() {
        return unFrozenAmount;
    }

    public void setUnFrozenAmount(BigDecimal unFrozenAmount) {
        this.unFrozenAmount = unFrozenAmount;
    }

    public BigDecimal getTotalFrozenAmount() {
        return totalFrozenAmount;
    }

    public void setTotalFrozenAmount(BigDecimal totalFrozenAmount) {
        this.totalFrozenAmount = totalFrozenAmount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
}
