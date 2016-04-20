package com.lufax.jijin.fundation.remote.gson.paf.response;

public class PAFCancelResponseGson extends BaseResponseGson {

    private String mercOrderNo;
    private String orderTraceNo;

    public String getMercOrderNo() {
        return mercOrderNo;
    }

    public void setMercOrderNo(String mercOrderNo) {
        this.mercOrderNo = mercOrderNo;
    }

    public String getOrderTraceNo() {
        return orderTraceNo;
    }

    public void setOrderTraceNo(String orderTraceNo) {
        this.orderTraceNo = orderTraceNo;
    }
}
