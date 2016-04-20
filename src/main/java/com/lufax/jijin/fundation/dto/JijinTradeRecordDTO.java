package com.lufax.jijin.fundation.dto;

import com.lufax.jijin.base.dto.BaseDTO;
import com.lufax.jijin.fundation.constant.TradeRecordType;

import java.math.BigDecimal;
import java.util.Date;

public class JijinTradeRecordDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 5954538895875999018L;

    private Long userId;
    private String fundCode;
    private String trxTime;
    private String trxDate;
    private String status;
    private TradeRecordType type;
    private BigDecimal reqAmount;
    private BigDecimal reqShare;
    private String contractNo;
    private String appNo;
    private String appSheetNo;
    private String notifyAppNo;
    private String cancelAppNo;
    private String cancelAppSheetNo;
    private String dividendType;
    private String chargeType;
    private String payOrderNo;
    private String payCancelOrderNo;
    private String errorCode;
    private String errorMsg;
    private Long retryTimes;
    private String isControversial;
    private Date createdAt;
    private Date updatedAt;
    private String createdBy;
    private String updatedBy;
    private Long trxId;
    private String frozenCode;
    private String channel;
    private String frozenType;
    private String instId;
    private String orderCompleteTime;
    private String isAgreeRisk;
    private String flag;
    private String redeemType;//赎回类型 0：T+0  1：T+1
    private String ukToken;  //组合UK 渠道名+trxId
    private String businessMode; //market:营销
    private String prodCode;//用赎回购买的产品编码
    private String transferType; // 赎回到银行卡,赎回到虚拟户 TransferTypeEnum
    private String confirmDate;//确认日期
    private String accountDate;//到账日期
    private String  expectConfirmDate;//预计确认日期
    private String  expectAccountDate;//预计到账日期
    
    
    public String getTransferType() {
		return transferType;
	}

	public void setTransferType(String transferType) {
		this.transferType = transferType;
	}



    public String getExpectAccountDate() {
        return expectAccountDate;
    }

    public void setExpectAccountDate(String expectAccountDate) {
        this.expectAccountDate = expectAccountDate;
    }

    public String getExpectConfirmDate() {
        return expectConfirmDate;
    }

    public void setExpectConfirmDate(String expectConfirmDate) {
        this.expectConfirmDate = expectConfirmDate;
    }

    public String getConfirmDate() {
        return confirmDate;
    }

    public void setConfirmDate(String confirmDate) {
        this.confirmDate = confirmDate;
    }

    public String getAccountDate() {
        return accountDate;
    }

    public void setAccountDate(String accountDate) {
        this.accountDate = accountDate;
    }
    private String sourceFundCode;//转购目标基金code
    private String remark;//备注

    public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getProdCode() {
		return prodCode;
	}

	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getIsAgreeRisk() {
		return isAgreeRisk;
	}

	public void setIsAgreeRisk(String isAgreeRisk) {
		this.isAgreeRisk = isAgreeRisk;
	}

	public JijinTradeRecordDTO() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getTrxTime() {
        return trxTime;
    }

    public void setTrxTime(String trxTime) {
        this.trxTime = trxTime;
    }

    public String getTrxDate() {
        return trxDate;
    }

    public void setTrxDate(String trxDate) {
        this.trxDate = trxDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public TradeRecordType getType() {
        return type;
    }

    public void setType(TradeRecordType type) {
        this.type = type;
    }

    public BigDecimal getReqAmount() {
        return reqAmount;
    }

    public void setReqAmount(BigDecimal reqAmount) {
        this.reqAmount = reqAmount;
    }

    public BigDecimal getReqShare() {
        return reqShare;
    }

    public void setReqShare(BigDecimal reqShare) {
        this.reqShare = reqShare;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getAppNo() {
        return appNo;
    }

    public void setAppNo(String appNo) {
        this.appNo = appNo;
    }

    public String getAppSheetNo() {
        return appSheetNo;
    }

    public void setAppSheetNo(String appSheetNo) {
        this.appSheetNo = appSheetNo;
    }

    public String getNotifyAppNo() {
        return notifyAppNo;
    }

    public void setNotifyAppNo(String notifyAppNo) {
        this.notifyAppNo = notifyAppNo;
    }

    public String getCancelAppNo() {
        return cancelAppNo;
    }

    public void setCancelAppNo(String cancelAppNo) {
        this.cancelAppNo = cancelAppNo;
    }

    public String getCancelAppSheetNo() {
        return cancelAppSheetNo;
    }

    public void setCancelAppSheetNo(String cancelAppSheetNo) {
        this.cancelAppSheetNo = cancelAppSheetNo;
    }

    public String getDividendType() {
        return dividendType;
    }

    public void setDividendType(String dividendType) {
        this.dividendType = dividendType;
    }

    public String getChargeType() {
        return chargeType;
    }

    public void setChargeType(String chargeType) {
        this.chargeType = chargeType;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Long getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(Long retryTimes) {
        this.retryTimes = retryTimes;
    }

    public String getIsControversial() {
        return isControversial;
    }

    public void setIsControversial(String isControversial) {
        this.isControversial = isControversial;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Long getTrxId() {
        return trxId;
    }

    public void setTrxId(Long trxId) {
        this.trxId = trxId;
    }

    public String getFrozenCode() {
        return frozenCode;
    }

    public void setFrozenCode(String frozenCode) {
        this.frozenCode = frozenCode;
    }

    public String getPayOrderNo() {
        return payOrderNo;
    }

    public void setPayOrderNo(String payOrderNo) {
        this.payOrderNo = payOrderNo;
    }

    public String getPayCancelOrderNo() {
        return payCancelOrderNo;
    }

    public void setPayCancelOrderNo(String payCancelOrderNo) {
        this.payCancelOrderNo = payCancelOrderNo;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getFrozenType() {
        return frozenType;
    }

    public void setFrozenType(String frozenType) {
        this.frozenType = frozenType;
    }

    public String getInstId() {
        return instId;
    }

    public void setInstId(String instId) {
        this.instId = instId;
    }

    public String getOrderCompleteTime() {
        return orderCompleteTime;
    }

    public void setOrderCompleteTime(String orderCompleteTime) {
        this.orderCompleteTime = orderCompleteTime;
    }

    public String getRedeemType() {
        return redeemType;
    }

    public void setRedeemType(String redeemType) {
        this.redeemType = redeemType;
    }

    public String getUkToken() {
        return ukToken;
    }

    public void setUkToken(String ukToken) {
        this.ukToken = ukToken;
    }

    public String getBusinessMode() {
        return businessMode;
    }

    public void setBusinessMode(String businessMode) {
        this.businessMode = businessMode;
    }

    public String getSourceFundCode() {
        return sourceFundCode;
    }

    public void setSourceFundCode(String sourceFundCode) {
        this.sourceFundCode = sourceFundCode;
    }
}
