package com.lufax.jijin.user.domain;


public class CheckTradePasswordGson {

    private String resultId;
    private String resultMsg;
    private String lockHours;
    private String lockRange;
    private String maxErrorTime;
    private String errorTime;

    public Boolean isSuccess() {
        return "00".equalsIgnoreCase(resultId);
    }

    public CheckTradePasswordGson() {
    }

    public String getResultId() {
        return resultId;
    }

    public void setResultId(String resultId) {
        this.resultId = resultId;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public String getLockHours() {
        return lockHours;
    }

    public void setLockHours(String lockHours) {
        this.lockHours = lockHours;
    }

    public String getLockRange() {
        return lockRange;
    }

    public void setLockRange(String lockRange) {
        this.lockRange = lockRange;
    }

    public String getMaxErrorTime() {
        return maxErrorTime;
    }

    public void setMaxErrorTime(String maxErrorTime) {
        this.maxErrorTime = maxErrorTime;
    }

    public String getErrorTime() {
        return errorTime;
    }

    public void setErrorTime(String errorTime) {
        this.errorTime = errorTime;
    }
}
