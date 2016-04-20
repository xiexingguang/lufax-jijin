package com.lufax.jijin.fundation.dto;

import com.lufax.jijin.base.dto.BaseDTO;

import java.math.BigDecimal;

/**
 * Created by NiuZhanJun on 9/28/15.
 */
public class JijinWithdrawRecordDTO extends BaseDTO {
    private Long productId;// 产品ID
    private Long tradeUserId;// 交易虚拟户ID
    private String tradeAccountNo;// 交易实体户帐号
    private int type;// 操作类型 1：代付 2：代扣
    private String operateDate;// 操作时间
    private BigDecimal requestAmount;// 请求金额
    private BigDecimal successAmount;// 成功金额
    private String remark;// 备注
    private String recordId;// 流水号
    private int version;// 版本号
    private String status;//状态INIT,SUCCESS,FAIL

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getOperateDate() {
        return operateDate;
    }

    public void setOperateDate(String operateDate) {
        this.operateDate = operateDate;
    }

    public BigDecimal getRequestAmount() {
        return requestAmount;
    }

    public void setRequestAmount(BigDecimal requestAmount) {
        this.requestAmount = requestAmount;
    }

    public BigDecimal getSuccessAmount() {
        return successAmount;
    }

    public void setSuccessAmount(BigDecimal successAmount) {
        this.successAmount = successAmount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getTradeUserId() {
        return tradeUserId;
    }

    public void setTradeUserId(Long tradeUserId) {
        this.tradeUserId = tradeUserId;
    }

    public String getTradeAccountNo() {
        return tradeAccountNo;
    }

    public void setTradeAccountNo(String tradeAccountNo) {
        this.tradeAccountNo = tradeAccountNo;
    }
}
