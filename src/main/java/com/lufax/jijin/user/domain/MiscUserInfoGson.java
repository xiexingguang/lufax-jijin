package com.lufax.jijin.user.domain;


public class MiscUserInfoGson {

    private Long userId;
    private String partyNo;
    private String name;
    private String userName;
    private String companyName;
    private String mobileNo;
    private String userType;
    private String email;
    private String isPublic;
    private String userRole;
    private String nameAuthentication;
    private String cardBindStatus;
    private String createTime;
    private String updateTime;

    public String getCardBindStatus() {
        return cardBindStatus;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getEmail() {
        return email;
    }

    public String getPublic() {
        return isPublic;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public String getName() {
        return name;
    }

    public String getNameAuthentication() {
        return nameAuthentication;
    }

    public String getPartyNo() {
        return partyNo;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserRole() {
        return userRole;
    }

    public String getUserType() {
        return userType;
    }
}
