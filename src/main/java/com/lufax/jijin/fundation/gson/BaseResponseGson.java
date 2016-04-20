package com.lufax.jijin.fundation.gson;

import java.util.Date;

import com.lufax.jijin.base.utils.ConstantsHelper;


public class BaseResponseGson {
    /**
     * 返回码
     */
    private String retCode = ConstantsHelper.RET_CODE_SUCCESS;     //默认为000
    /**
     * 返回信息
     */
    private String retMessage;
    /**
     * 数据库执行时间
     */
    private Date executedTime;

    public BaseResponseGson() {

    }

    public Boolean isSuccess() {
        return ConstantsHelper.RET_CODE_SUCCESS.equals(retCode);
    }

    public BaseResponseGson(String retMessage, String retCode) {
        this.retMessage = retMessage;
        this.retCode = retCode;
    }

    /**
     * 返回码
     */
    public String getRetCode() {
        return retCode;
    }

    /**
     * 返回码
     */
    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }

    /**
     * 返回信息
     */
    public String getRetMessage() {
        return retMessage;
    }

    /**
     * 返回信息
     */
    public void setRetMessage(String retMessage) {
        this.retMessage = retMessage;
    }

    /**
     * 数据库执行时间
     */
    public Date getExecutedTime() {
        return executedTime;
    }

    /**
     * 数据库执行时间
     */
    public void setExecutedTime(Date dbCreatedTime) {
        this.executedTime = dbCreatedTime;
    }

    @Override
    public String toString() {
        return "BaseResponseGson{" +
                ", retCode='" + retCode + '\'' +
                ", retMessage='" + retMessage + '\'' +
                ", executedTime=" + executedTime +
                '}';
    }
}
