package com.lufax.jijin.fundation.remote.gson.paf.response;

public class PAFPayResponseGson extends BaseResponseGson {


    private String mercOrderNo;
    private String orderTraceNo;
    private String orderCompleteTime;

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

    public String getOrderCompleteTime() {
        return orderCompleteTime;
    }

    public void setOrderCompleteTime(String orderCompleteTime) {
        this.orderCompleteTime = orderCompleteTime;
    }
}
