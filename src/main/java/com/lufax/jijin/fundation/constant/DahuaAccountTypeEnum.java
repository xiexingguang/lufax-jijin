package com.lufax.jijin.fundation.constant;

/**
 * Created by NiuZhanJun on 10/12/15.
 */
public enum DahuaAccountTypeEnum {
    垫资户(1),
    中间户(2);

    private int accountType;

    DahuaAccountTypeEnum(int accountType) {
        setAccountType(accountType);
    }

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }
}
