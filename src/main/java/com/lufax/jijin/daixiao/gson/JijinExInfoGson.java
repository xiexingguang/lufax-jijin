package com.lufax.jijin.daixiao.gson;

import com.lufax.jijin.base.dto.BaseDTO;
import com.lufax.jijin.daixiao.dto.JijinExInfoDTO;

import java.math.BigDecimal;


public class JijinExInfoGson {

    private String fundCode; //基金代码
    private String name;//简称
    private String fullName;// 名称
    private String setupDate; //成立日期
    private String investType;//投资类型
    private String managementComp;// 管理人(基金公司简称)
    private String companyFullName;//管理人全称(基金公司全称)
    private String companyCode;//管理人Code(基金公司Code)
    private String custodianBank;// 托管人
    private String benchMark; // 业绩比较基准
    private BigDecimal managementFee;//管理费
    private BigDecimal trusteeFee;//托管费


    public JijinExInfoGson() {
    }

    public JijinExInfoGson(JijinExInfoDTO jijinExInfoDTO) {
        this.fundCode = jijinExInfoDTO.getFundCode();
        this.name = jijinExInfoDTO.getName();
        this.fullName = jijinExInfoDTO.getFullName();
        this.setupDate = jijinExInfoDTO.getSetupDate();
        this.investType = jijinExInfoDTO.getInvestType();
        this.managementComp = jijinExInfoDTO.getManagementComp();
        this.companyCode = jijinExInfoDTO.getCompanyCode();
        this.companyFullName = jijinExInfoDTO.getCompanyFullName();
        this.custodianBank = jijinExInfoDTO.getCustodianBank();
        this.benchMark = jijinExInfoDTO.getBenchMark();
        this.managementFee = jijinExInfoDTO.getManagementFee();
        this.trusteeFee = jijinExInfoDTO.getTrusteeFee();
    }

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getSetupDate() {
        return setupDate;
    }

    public void setSetupDate(String setupDate) {
        this.setupDate = setupDate;
    }

    public String getInvestType() {
        return investType;
    }

    public void setInvestType(String investType) {
        this.investType = investType;
    }

    public String getManagementComp() {
        return managementComp;
    }

    public void setManagementComp(String managementComp) {
        this.managementComp = managementComp;
    }

    public String getCompanyFullName() {
        return companyFullName;
    }

    public void setCompanyFullName(String companyFullName) {
        this.companyFullName = companyFullName;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getCustodianBank() {
        return custodianBank;
    }

    public void setCustodianBank(String custodianBank) {
        this.custodianBank = custodianBank;
    }

    public String getBenchMark() {
        return benchMark;
    }

    public void setBenchMark(String benchMark) {
        this.benchMark = benchMark;
    }

    public BigDecimal getManagementFee() {
        return managementFee;
    }

    public void setManagementFee(BigDecimal managementFee) {
        this.managementFee = managementFee;
    }

    public BigDecimal getTrusteeFee() {
        return trusteeFee;
    }

    public void setTrusteeFee(BigDecimal trusteeFee) {
        this.trusteeFee = trusteeFee;
    }
}
