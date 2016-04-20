package com.lufax.jijin.fundation.dto;

import com.lufax.jijin.base.dto.BaseDTO;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author liudong735
 * @date 2016年03月10日
 */
public class JijinFreezeRecordDTO extends BaseDTO {
    private static final long serialVersionUID = 2491046070223853336L;

    private String appNo;
    private String appSheetNo;
    private BigDecimal freezeShare;
    private String buyConfirmDate;
    private String unfreezeDate;
    private Long userId;
    private Integer freezeType;
    private String freezeMsg;
    private Long userBalanceId;
    private String fundCode;
    /* default sysdate */
    private Date createdAt;
    /* default sysdate */
    private Date updatedAt;
    private String createdBy;
    private String updatedBy;

    public String getAppNo() {
        return appNo;
    }

    public void setAppNo(String appNo) {
        this.appNo = appNo;
    }

    public String getAppSheetNo() {
        return appSheetNo;
    }

    public void setAppSheetNo(String appSheetNo) {
        this.appSheetNo = appSheetNo;
    }

    public BigDecimal getFreezeShare() {
        return freezeShare;
    }

    public void setFreezeShare(BigDecimal freezeShare) {
        this.freezeShare = freezeShare;
    }

    public String getBuyConfirmDate() {
        return buyConfirmDate;
    }

    public void setBuyConfirmDate(String buyConfirmDate) {
        this.buyConfirmDate = buyConfirmDate;
    }

    public String getUnfreezeDate() {
        return unfreezeDate;
    }

    public void setUnfreezeDate(String unfreezeDate) {
        this.unfreezeDate = unfreezeDate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getFreezeType() {
        return freezeType;
    }

    public void setFreezeType(Integer freezeType) {
        this.freezeType = freezeType;
    }

    public String getFreezeMsg() {
        return freezeMsg;
    }

    public void setFreezeMsg(String freezeMsg) {
        this.freezeMsg = freezeMsg;
    }

    public Long getUserBalanceId() {
        return userBalanceId;
    }

    public void setUserBalanceId(Long userBalanceId) {
        this.userBalanceId = userBalanceId;
    }

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
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
