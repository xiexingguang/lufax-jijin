package com.lufax.jijin.fundation.gson;


public class PurchaseOrderResultGson extends BaseGson {

    private String version;
    private String instId;    //陆金所自定义（机构标识）,基金公司
    private String isIndividual; //0-机构 1-个人
    private String contractNo;   //基金公司用户交易帐号
    private String applicationNo; //申请流水号
    private String appSheetSerialNo; //基金公司申请单据流水号
    private String transactionDate;  //交易所属日期
    private String transactionTime; //交易发生时间
    private String errorCode;
    private String errorMessage;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getInstId() {
        return instId;
    }

    public void setInstId(String instId) {
        this.instId = instId;
    }

    public String getIsIndividual() {
        return isIndividual;
    }

    public void setIsIndividual(String isIndividual) {
        this.isIndividual = isIndividual;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getApplicationNo() {
        return applicationNo;
    }

    public void setApplicationNo(String applicationNo) {
        this.applicationNo = applicationNo;
    }

    public String getAppSheetSerialNo() {
        return appSheetSerialNo;
    }

    public void setAppSheetSerialNo(String appSheetSerialNo) {
        this.appSheetSerialNo = appSheetSerialNo;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(String transactionTime) {
        this.transactionTime = transactionTime;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
