package com.lufax.jijin.sysFacade.gson.result;

import java.math.BigDecimal;
import java.util.Date;

public class PolicyInfoGson {
	private String retCode;
    private String retMsg;
	private Long id;
    private Long userId;
    private Long productId;
    private String email;
    private String province;
    private String city;
    private String address;
    private Long sid;
    private String orderId;
    private String proposalNo;
    private String policyNo;
    private String status;
    private Date createdAt;
    private Date updatedAt;
    private String createdBy;
    private String updatedBy;
    private String failReason;
    private BigDecimal amount; //总金额
    private Date tradeDate;
    private BigDecimal coinAmount;  //陆金币数量	
    private BigDecimal otherDiscountAmount;
    
    
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Long getSid() {
		return sid;
	}
	public void setSid(Long sid) {
		this.sid = sid;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getProposalNo() {
		return proposalNo;
	}
	public void setProposalNo(String proposalNo) {
		this.proposalNo = proposalNo;
	}
	public String getPolicyNo() {
		return policyNo;
	}
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	public String getFailReason() {
		return failReason;
	}
	public void setFailReason(String failReason) {
		this.failReason = failReason;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public Date getTradeDate() {
		return tradeDate;
	}
	public void setTradeDate(Date tradeDate) {
		this.tradeDate = tradeDate;
	}
	public BigDecimal getCoinAmount() {
		return coinAmount;
	}
	public void setCoinAmount(BigDecimal coinAmount) {
		this.coinAmount = coinAmount;
	}
	public BigDecimal getOtherDiscountAmount() {
		return otherDiscountAmount;
	}
	public void setOtherDiscountAmount(BigDecimal otherDiscountAmount) {
		this.otherDiscountAmount = otherDiscountAmount;
	}
	public String getRetCode() {
		return retCode;
	}
	public String getRetMsg() {
		return retMsg;
	}
	public void setRetCode(String retCode) {
		this.retCode = retCode;
	}
	public void setRetMsg(String retMsg) {
		this.retMsg = retMsg;
	}
    
    public boolean isSuccess(){
    	return "00".equals(retCode);
    }
    
    
    
}
