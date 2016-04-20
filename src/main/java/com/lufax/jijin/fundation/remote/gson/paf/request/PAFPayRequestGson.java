package com.lufax.jijin.fundation.remote.gson.paf.request;


import java.math.BigDecimal;

public class PAFPayRequestGson {

    private String transType;
    private String transCode;
    private String bizType;
    private String platMerchantId;
    private String mercOrderNo;
    private Long orderAmount;
    private String orderCurrency;
    private String token;
    private String merReserved;
    private String merchantId;

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getTransCode() {
        return transCode;
    }

    public void setTransCode(String transCode) {
        this.transCode = transCode;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getPlatMerchantId() {
        return platMerchantId;
    }

    public void setPlatMerchantId(String platMerchantId) {
        this.platMerchantId = platMerchantId;
    }

    public String getMercOrderNo() {
        return mercOrderNo;
    }

    public void setMercOrderNo(String mercOrderNo) {
        this.mercOrderNo = mercOrderNo;
    }

    public Long getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(Long orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getOrderCurrency() {
        return orderCurrency;
    }

    public void setOrderCurrency(String orderCurrency) {
        this.orderCurrency = orderCurrency;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMerReserved() {
        return merReserved;
    }

    public void setMerReserved(String merReserved) {
        this.merReserved = merReserved;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }
}
