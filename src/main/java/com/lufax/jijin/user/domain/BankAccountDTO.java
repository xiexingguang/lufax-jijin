package com.lufax.jijin.user.domain;

public class BankAccountDTO {

    private String id;

    /**
     * 总行代码（3位）
     */
    private String bankCode;

    /**
     * 分行code
     */
    private String bankId;

    /**
     * 银行账号
     */
    private String bankAccount;

    /**
     * 认证状态  true:认证成功 false:认证中
     */
    private boolean status = true;

    /**
     * 0：不是来自信保 1：来自信保
     */
    private String source;

    /**
     * 财付通开户城市id
     */
    private String tenpayCityId;

    /**
     * 财付通开户支行中文名称
     */
    private String tenpayBranchName;

    private String bankName;

    public String getId() {
        return id;
    }

    public String getBankCode() {
        return bankCode;
    }

    public String getBankId() {
        return bankId;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getStatus() {
        if ("1".equals(source)) {
            return "-1";
        } else if (status) {
            return "0";
        } else {
            return "inAuth";
        }
    }

    public boolean isAuth() {
        return status;
    }

    public String getSource() {
        return source;
    }

    public String getTenpayCityId() {
        return tenpayCityId;
    }

    public String getTenpayBranchName() {
        return tenpayBranchName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getBankName() {
        return bankName;
    }

}
