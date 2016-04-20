package com.lufax.jijin.fundation.gson;

import java.util.Date;

/**
 * Created by NiuZhanJun on 10/28/15.
 */
public class StandardTransferResponse {
    private Long responseNo; // 应答号(请求记录表主键)
    private Long transactionNo;// 转账交易号
    private String retCode;// 返回码
    private String retMessage;// 返回信息
    private Date executedTime;// 数据库执行时间

    public boolean isSuccess() {
        return this.retCode.equals("000");
    }

    public Long getResponseNo() {
        return responseNo;
    }

    public void setResponseNo(Long responseNo) {
        this.responseNo = responseNo;
    }

    public Long getTransactionNo() {
        return transactionNo;
    }

    public void setTransactionNo(Long transactionNo) {
        this.transactionNo = transactionNo;
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
}
