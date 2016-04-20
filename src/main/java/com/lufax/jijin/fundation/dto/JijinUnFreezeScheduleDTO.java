package com.lufax.jijin.fundation.dto;

import com.lufax.jijin.base.dto.BaseDTO;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by wuxiujun685 on 2016/3/10.
 */
public class JijinUnFreezeScheduleDTO extends BaseDTO {
    private static final long serialVersionUID = 2491046070223853336L;
    private String unfreezeDate;
    private Long totalCount;
    private Long totalUnfreezeCount;
    private BigDecimal totalFreezeShare;
    private BigDecimal totalUnFreezeShare;
    private Long version;
    private Date createdAt;
    private Date updatedAt;
    private String createdBy;
    private String updatedBy;

    public String getUnfreezeDate() {
        return unfreezeDate;
    }

    public void setUnfreezeDate(String unfreezeDate) {
        this.unfreezeDate = unfreezeDate;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public Long getTotalUnfreezeCount() {
        return totalUnfreezeCount;
    }

    public void setTotalUnfreezeCount(Long totalUnfreezeCount) {
        this.totalUnfreezeCount = totalUnfreezeCount;
    }

    public BigDecimal getTotalFreezeShare() {
        return totalFreezeShare;
    }

    public void setTotalFreezeShare(BigDecimal totalFreezeShare) {
        this.totalFreezeShare = totalFreezeShare;
    }

    public BigDecimal getTotalUnFreezeShare() {
        return totalUnFreezeShare;
    }

    public void setTotalUnFreezeShare(BigDecimal totalUnFreezeShare) {
        this.totalUnFreezeShare = totalUnFreezeShare;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
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
