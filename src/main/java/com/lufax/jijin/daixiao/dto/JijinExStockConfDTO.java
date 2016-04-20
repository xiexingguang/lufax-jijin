package com.lufax.jijin.daixiao.dto;

import com.lufax.jijin.base.dto.BaseDTO;

/**
 * Created by chenguang451 on 2016/1/6.
 */
public class JijinExStockConfDTO extends BaseDTO {

    private String fundCode;

    private String endDate;

    private String stockCode;

    private String stockName;

    private String stockPer;

    private String announceDate;

    private String stockValuePer;

    private String capitalStockPer;

    private String stockValue;

    private String stockShare;

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

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public String getStockPer() {
        return stockPer;
    }

    public void setStockPer(String stockPer) {
        this.stockPer = stockPer;
    }

    public String getAnnounceDate() {
        return announceDate;
    }

    public void setAnnounceDate(String announceDate) {
        this.announceDate = announceDate;
    }

    public String getStockValuePer() {
        return stockValuePer;
    }

    public void setStockValuePer(String stockValuePer) {
        this.stockValuePer = stockValuePer;
    }

    public String getCapitalStockPer() {
        return capitalStockPer;
    }

    public void setCapitalStockPer(String capitalStockPer) {
        this.capitalStockPer = capitalStockPer;
    }

    public String getStockValue() {
        return stockValue;
    }

    public void setStockValue(String stockValue) {
        this.stockValue = stockValue;
    }

    public String getStockShare() {
        return stockShare;
    }

    public void setStockShare(String stockShare) {
        this.stockShare = stockShare;
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
