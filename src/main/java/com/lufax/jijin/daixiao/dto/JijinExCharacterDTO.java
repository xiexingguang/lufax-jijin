package com.lufax.jijin.daixiao.dto;

import com.lufax.jijin.base.dto.BaseDTO;

/**
 * Created by chenguang451 on 2016/1/5.
 */
public class JijinExCharacterDTO extends BaseDTO {

    private String fundCode;
    private String isHongkong;
    private String isMacao;
    private String isTaiwan;
    private String isForeign;
    private String isPlan1;
    private String isPlan2;
    private String isPlan3;
    private String isPreRedeem;
    private String isPreSub;
    private String isRealRedeem;
    private String isMtsRedeem;
    private String isMtsSub;
    private String isStmRedeem;
    private String isStmSub;
    private String taCode;
    private Long batchId;
    private String status = "DISPATCHED";
    private Long isValid = 1L;

    public Long getIsValid() {
        return isValid;
    }

    public void setIsValid(Long isValid) {
        this.isValid = isValid;
    }

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getIsHongkong() {
        return isHongkong;
    }

    public void setIsHongkong(String isHongkong) {
        this.isHongkong = isHongkong;
    }

    public String getIsMacao() {
        return isMacao;
    }

    public void setIsMacao(String isMacao) {
        this.isMacao = isMacao;
    }

    public String getIsTaiwan() {
        return isTaiwan;
    }

    public void setIsTaiwan(String isTaiwan) {
        this.isTaiwan = isTaiwan;
    }

    public String getIsForeign() {
        return isForeign;
    }

    public void setIsForeign(String isForeign) {
        this.isForeign = isForeign;
    }

    public String getIsPlan1() {
        return isPlan1;
    }

    public void setIsPlan1(String isPlan1) {
        this.isPlan1 = isPlan1;
    }

    public String getIsPlan2() {
        return isPlan2;
    }

    public void setIsPlan2(String isPlan2) {
        this.isPlan2 = isPlan2;
    }

    public String getIsPlan3() {
        return isPlan3;
    }

    public void setIsPlan3(String isPlan3) {
        this.isPlan3 = isPlan3;
    }

    public String getIsPreRedeem() {
        return isPreRedeem;
    }

    public void setIsPreRedeem(String isPreRedeem) {
        this.isPreRedeem = isPreRedeem;
    }

    public String getIsPreSub() {
        return isPreSub;
    }

    public void setIsPreSub(String isPreSub) {
        this.isPreSub = isPreSub;
    }

    public String getIsRealRedeem() {
        return isRealRedeem;
    }

    public void setIsRealRedeem(String isRealRedeem) {
        this.isRealRedeem = isRealRedeem;
    }

    public String getIsMtsRedeem() {
        return isMtsRedeem;
    }

    public void setIsMtsRedeem(String isMtsRedeem) {
        this.isMtsRedeem = isMtsRedeem;
    }

    public String getIsMtsSub() {
        return isMtsSub;
    }

    public void setIsMtsSub(String isMtsSub) {
        this.isMtsSub = isMtsSub;
    }

    public String getIsStmRedeem() {
        return isStmRedeem;
    }

    public void setIsStmRedeem(String isStmRedeem) {
        this.isStmRedeem = isStmRedeem;
    }

    public String getIsStmSub() {
        return isStmSub;
    }

    public void setIsStmSub(String isStmSub) {
        this.isStmSub = isStmSub;
    }

    public String getTaCode() {
        return taCode;
    }

    public void setTaCode(String taCode) {
        this.taCode = taCode;
    }

    public Long getBatchId() {
        return batchId;
    }

    public void setBatchId(Long batchId) {
        this.batchId = batchId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
