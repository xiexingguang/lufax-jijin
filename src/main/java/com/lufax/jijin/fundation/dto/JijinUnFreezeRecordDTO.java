package com.lufax.jijin.fundation.dto;

import com.lufax.jijin.base.dto.BaseDTO;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by wuxiujun685 on 2016/3/10.
 */
public class JijinUnFreezeRecordDTO  extends BaseDTO {
    private static final long serialVersionUID = 2491046070223853336L;
    private String appNo         ;//申购确认流水号
    private String appSheetNo;
    private String unfreezeDate  ;//预期解冻日期,YYYYMMDD  
    private BigDecimal unfreezeShare ;//冻结份额
    private Long userId        ;//用户ID                 
    private Long userBalanceId ;//USER_BALANCE_ID
    private String fundCode      ;//基金code
    private String status        ;//状态
    private Integer freezeType    ;//冻结类型 1:市场活动冻结
    private String unfreezeMsg   ;//解冻原因               
    private Long version       ;//乐观锁                 
    private Date createdAt     ;//DEFAULT SYSDATE
    private Date updatedAt     ;//DEFAULT SYSDATE        
    private String createdBy     ;//                       
    private String updatedBy     ;//


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

    public String getUnfreezeDate() {
        return unfreezeDate;
    }

    public void setUnfreezeDate(String unfreezeDate) {
        this.unfreezeDate = unfreezeDate;
    }

    public BigDecimal getUnfreezeShare() {
        return unfreezeShare;
    }

    public void setUnfreezeShare(BigDecimal unfreezeShare) {
        this.unfreezeShare = unfreezeShare;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getFreezeType() {
        return freezeType;
    }

    public void setFreezeType(Integer freezeType) {
        this.freezeType = freezeType;
    }

    public String getUnfreezeMsg() {
        return unfreezeMsg;
    }

    public void setUnfreezeMsg(String unfreezeMsg) {
        this.unfreezeMsg = unfreezeMsg;
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
