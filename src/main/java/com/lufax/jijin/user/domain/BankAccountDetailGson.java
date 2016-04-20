package com.lufax.jijin.user.domain;

public class BankAccountDetailGson {

    private String id;
    private String bankCode;
    private String bankId;
    private String bankAccount;
    private String source;

    private String tenpayCityId;//财付通开户城市ID

    private String bankName;
    private String createdAt;
    private String isValid;

    public BankAccountDetailGson() {
    }
    public BankAccountDetailGson(String id) {
    	this.id= id ;
    }
    public String getBankAccount() {
        return bankAccount;
    }

    public String getBankCode() {
        return bankCode;
    }

    public String getBankId() {
        return bankId;
    }

    public String getBankName() {
        return bankName;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getId() {
        return id;
    }

    public String getValid() {
        return isValid;
    }

    public String getSource() {
        return source;
    }

    public String getTenpayCityId() {
        return tenpayCityId;
    }
}
