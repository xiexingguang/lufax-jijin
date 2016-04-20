package com.lufax.jijin.fundation.dto;

import com.lufax.jijin.base.dto.BaseDTO;

import java.util.Date;

/**
 * @author liudong735
 * @date 2016年03月10日
 */
public class JijinFreezeRuleDTO extends BaseDTO {
    private static final long serialVersionUID = 2491046070223853336L;

    private String fundCode;
    private String businessMode;
    private Long freezeDay;
    private boolean isActive;
    /* default sysdate */
    private Date createdAt;
    /* default sysdate */
    private Date updatedAt;
    private String createdBy;
    private String updatedBy;

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getBusinessMode() {
        return businessMode;
    }

    public void setBusinessMode(String businessMode) {
        this.businessMode = businessMode;
    }

    public Long getFreezeDay() {
        return freezeDay;
    }

    public void setFreezeDay(Long freezeDay) {
        this.freezeDay = freezeDay;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
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
