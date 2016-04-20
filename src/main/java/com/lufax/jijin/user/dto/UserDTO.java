package com.lufax.jijin.user.dto;


import com.lufax.jijin.base.dto.BaseDTO;

public class UserDTO extends BaseDTO {

    private String partyNo;

    private String username;

    private String identityNo;

    private String identityType;

    private String mobileNo;

    private String name;

    private String userRole;

    public UserDTO() {
    }

    public String getUserRole() {
        return userRole;
    }

    public String getIdentityNo() {
        return identityNo;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public String getName() {
        return name;
    }

    public String getPartyNo() {

        return partyNo;

    }

    public String getUsername() {
        return username;
    }

    public String getIdentityType() {
        return identityType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public void setPartyNo(String partyNo) {
        this.partyNo = partyNo;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setIdentityNo(String identityNo) {
        this.identityNo = identityNo;
    }

    public void setIdentityType(String identityType) {
        this.identityType = identityType;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }
}
