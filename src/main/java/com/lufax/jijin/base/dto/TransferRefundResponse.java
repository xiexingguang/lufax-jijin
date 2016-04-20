package com.lufax.jijin.base.dto;


import java.util.Date;

public class TransferRefundResponse {

    private Long responseNo;
    private String retCode;
    private String retMessage;
    private Date executedTime;

    public Long getResponseNo() {
        return responseNo;
    }

    public void setResponseNo(Long responseNo) {
        this.responseNo = responseNo;
    }

    public String getRetCode() {
        return retCode;
    }

    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }

    public String getRetMessage() {
        return retMessage;
    }

    public void setRetMessage(String retMessage) {
        this.retMessage = retMessage;
    }

    public Date getExecutedTime() {
        return executedTime;
    }

    public void setExecutedTime(Date executedTime) {
        this.executedTime = executedTime;
    }
    
    public boolean isSuccess(){
    	return getRetCode().equals("000");
    }
}
