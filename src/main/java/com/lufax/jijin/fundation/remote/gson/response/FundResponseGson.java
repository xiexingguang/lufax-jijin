package com.lufax.jijin.fundation.remote.gson.response;


public class FundResponseGson {
    private Long responseNo;
    private String retCode;
    private String retMessage;
    private String resultId;
    private String resultStatus; // SUCCESS FAILURE PROCESSING

    public String getResultStatus() {
		return resultStatus;
	}

	public void setResultStatus(String resultStatus) {
		this.resultStatus = resultStatus;
	}

	public FundResponseGson() {
    }

    public FundResponseGson(String retMessage, String retCode, Long responseNo) {
        this.retMessage = retMessage;
        this.retCode = retCode;
        this.responseNo = responseNo;
    }

    public String getResponseNo() {
        if (responseNo != null) {
            return responseNo.toString();
        }
        return "";
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

    public boolean isSuccess() {
        return "000".equals(retCode);
    }


    public String getResultId() {
        return resultId;
    }

    @Override
    public String toString() {
        return "{responseNo: " + responseNo + " retCode: " + retCode + " retMessage: " + retMessage +"resultStatus"+resultStatus+ "}";
    }
}
