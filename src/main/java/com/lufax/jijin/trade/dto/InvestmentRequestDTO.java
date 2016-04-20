package com.lufax.jijin.trade.dto;


import com.lufax.jijin.base.dto.BaseDTO;

import java.math.BigDecimal;
import java.util.Date;

public class InvestmentRequestDTO extends BaseDTO {

    private Long productId;

    private Long loanerId;

    private String status;

    private Date createdAt;

    private Date updatedAt;

    private Boolean statusToXinbao;

    private Long version;

    private Long trxId;

    private BigDecimal investmentAmount;

    private Date orderTime;

    private String orderId;
    private String proposalNo;
    private String policyNo;


    public InvestmentRequestDTO() {
    }

    public BigDecimal getInvestmentAmount() {
        return investmentAmount;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getLoanerId() {
        return loanerId;
    }

    public void setLoanerId(Long loanerId) {
        this.loanerId = loanerId;
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

    public Date getOrderTime() {
        return orderTime;
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

    public Boolean getStatusToXinbao() {
        return statusToXinbao;
    }

    public void setStatusToXinbao(Boolean statusToXinbao) {
        this.statusToXinbao = statusToXinbao;
    }

    public Long getTrxId() {
        return trxId;
    }

    public void setInvestmentAmount(BigDecimal investmentAmount) {
        this.investmentAmount = investmentAmount;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getProposalNo() {
        return proposalNo;
    }

    public String getPolicyNo() {
        return policyNo;
    }
}
