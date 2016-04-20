package com.lufax.jijin.dto;

import com.lufax.jijin.base.constant.FundName;
import com.lufax.jijin.base.dto.BaseDTO;

import java.math.BigDecimal;

public class FundAccountDTO extends BaseDTO {
    /* id */
    private Long id;
    /* 陆金所用户id */
    private Long userId;
    /* 基金名称 */
    private String fundName;
    /* 第三方客户号 */
    private String thirdCustomerAccount;
    /* 第三方账户 */
    private String thirdAccount;
    /* 帐号资金（可赎回部分） */
    private BigDecimal balance;
    /* 冻结资金 （赎回冻结） */
    private BigDecimal frozenAmount;
    /* 基金份额 */
    private BigDecimal fundShare;
    /* 冻结份额 */
    private BigDecimal frozenFundShare;
    /*乐观锁*/
    private Long version;
    
    public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public FundAccountDTO() {
    }

    public FundAccountDTO(Long userId, String thirdCustomerAccount, String thirdAccount) {
        this.userId = userId;
        this.fundName = FundName.YLX.name();
        this.thirdCustomerAccount = thirdCustomerAccount;
        this.thirdAccount = thirdAccount;
        this.balance = BigDecimal.ZERO;
        this.frozenAmount = BigDecimal.ZERO;
        this.fundShare = BigDecimal.ZERO;
        this.frozenFundShare = BigDecimal.ZERO;
        this.version = 0L;
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

    public String getFundName() {
        return fundName;
    }

    public void setFundName(String fundName) {
        this.fundName = fundName;
    }

    public String getThirdCustomerAccount() {
        return thirdCustomerAccount;
    }

    public void setThirdCustomerAccount(String thirdCustomerAccount) {
        this.thirdCustomerAccount = thirdCustomerAccount;
    }

    public String getThirdAccount() {
        return thirdAccount;
    }

    public void setThirdAccount(String thirdAccount) {
        this.thirdAccount = thirdAccount;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getFrozenAmount() {
        return frozenAmount;
    }

    public void setFrozenAmount(BigDecimal frozenAmount) {
        this.frozenAmount = frozenAmount;
    }

    public BigDecimal getFundShare() {
        return fundShare;
    }

    public void setFundShare(BigDecimal fundShare) {
        this.fundShare = fundShare;
    }

    public BigDecimal getFrozenFundShare() {
        return frozenFundShare;
    }

    public void setFrozenFundShare(BigDecimal frozenFundShare) {
        this.frozenFundShare = frozenFundShare;
    }
}
