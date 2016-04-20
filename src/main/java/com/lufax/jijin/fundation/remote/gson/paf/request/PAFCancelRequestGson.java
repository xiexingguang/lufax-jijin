package com.lufax.jijin.fundation.remote.gson.paf.request;

public class PAFCancelRequestGson {

    private String transType;
    private String mercOrderNo;
    private String origMercOrderNo;
    private Long orderAmount;
    private String orderCurrency;
    private String merchantId;
    private String platMerchantId;


    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getMercOrderNo() {
        return mercOrderNo;
    }

    public void setMercOrderNo(String mercOrderNo) {
        this.mercOrderNo = mercOrderNo;
    }

    public String getOrigMercOrderNo() {
        return origMercOrderNo;
    }

    public void setOrigMercOrderNo(String origMercOrderNo) {
        this.origMercOrderNo = origMercOrderNo;
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

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getPlatMerchantId() {
        return platMerchantId;
    }

    public void setPlatMerchantId(String platMerchantId) {
        this.platMerchantId = platMerchantId;
    }
}
