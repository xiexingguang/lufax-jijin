package com.lufax.jijin.daixiao.dto;

import com.lufax.jijin.base.dto.BaseDTO;

/**
 * Created by chenguang451 on 2016/1/6.
 */
public class JijinExBondConfDTO extends BaseDTO {

    private String fundCode;

    private String endDate;

    private String bondCode;

    private String bondName;

    private String bondValue;

    private String bondPer;

    private Long batchId;

    private String status = "DISPATCHED";

    private Long isValid = 1L;

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getBondCode() {
        return bondCode;
    }

    public void setBondCode(String bondCode) {
        this.bondCode = bondCode;
    }

    public String getBondName() {
        return bondName;
    }

    public void setBondName(String bondName) {
        this.bondName = bondName;
    }

    public String getBondValue() {
        return bondValue;
    }

    public void setBondValue(String bondValue) {
        this.bondValue = bondValue;
    }

    public String getBondPer() {
        return bondPer;
    }

    public void setBondPer(String bondPer) {
        this.bondPer = bondPer;
    }

    public Long getBatchId() {
        return batchId;
    }

    public void setBatchId(Long batchId) {
        this.batchId = batchId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getIsValid() {
        return isValid;
    }

    public void setIsValid(Long isValid) {
        this.isValid = isValid;
    }
}
