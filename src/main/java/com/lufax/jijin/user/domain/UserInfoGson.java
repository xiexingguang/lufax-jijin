package com.lufax.jijin.user.domain;
import com.lufax.jijin.base.utils.EncryptUtils;
import com.lufax.jijin.base.utils.NumberUtils;
import com.lufax.jijin.sysFacade.gson.BaseGson;

import java.math.BigDecimal;
import java.util.List;

public class UserInfoGson extends BaseGson {
    public final static BigDecimal rank2Limit = BigDecimal.valueOf(500000);
    public final static BigDecimal rank3Limit = BigDecimal.valueOf(1500000);

    /**
     * userName
     */
    private String alias;
    private String mobileNo;

    /**
     * name(chinese)
     */
    private String name;
	private String idType;
	private String idNo;
    private String email;
    private String sex;


	private String latelyDate;

    /**
     * 021投资人 022借款人
     */
    private String userType;

    /**
     * 邮箱认证状态(0未提交1已提交未认证（认证邮件未发送）2认证中（邮件已发送，等待用户确认）3认证成功)
     */
    private String emailVerifyStatus;

    /**
     * 安全问题状态（1设置0未设）
     */
    private String securityQuestionStatus;

    /**
     * 银行卡绑定状态（-1未绑卡0绑卡中1绑卡成功）
     */
    private String cardBindStatus;

    /**
     * 实名认证状态（1已认证0未认证）
     */
    private String nameAuthentication;

    /**
     * 风险评估状态（0未评估1保守型2平衡型3稳健型4盈利型）
     */
    private String riskVerifyStatus;

    /**
     * 是否设置交易密码（1：设置 其他：未设置）
     */
    private String tradingPasswordStatus;

    private List<BankAccountDTO> bankAccountsDTO;

    /**
     * 归属地区（01香港 02澳门 03台湾 04外籍）
     */
    private String nationality;

    /**
     * 审批状态（0未审核 1初审通过 2初审拒绝 3复核通过 4复核拒绝）
     */
    private String authStatus;

    /**
     * 未通过审核的原因（01证件号码填写不正确 02证件已过期 03证件有效期已经小于2个月 04姓名与证件不符 05证件照片清晰度不高，请重新上传 09其他）
     */
    private String refuseReason;

    /**
     * 其他拒绝原因说明
     */
    private String refuseRemark;

    /**
     * 提交时间申请
     */
    private String submitDate;

    /**
     * 姓
     */
    private String surname;

    /**
     * 隐藏部分数据
     */
    private String idNoSecret;

    private String fullName;

    private String otpType;

    private boolean tokenSwitch;

    private boolean isUserCanChangeCard;

    private boolean hasCardInAuditing;

    private String passwordStatus;

    private BigDecimal investBalance;       //当日投资余额
    private String investBalanceRate;   //当日投资余额排名百分比
    private String v8RiskStatus;

    private BigDecimal amountToNextLevel;

    private String securityLevel;

    private String memberLevel;
    
    public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
    public String getIdType() {
		return idType;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}

	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}
    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
    public boolean isCardBinded() {
        return "1".equals(this.cardBindStatus);
    }

    public void encryptMobileInfo() {
        this.mobileNo = EncryptUtils.encryptMobileNumber(this.mobileNo);
    }

    public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public void setBankAccountsDTO(List<BankAccountDTO> bankAccountsDTO) {
        this.bankAccountsDTO = bankAccountsDTO;
    }

    public Boolean isShowFirst1() {
        return "2".equalsIgnoreCase(this.idType);
    }

    public void encryptIdNoInfo() {
        if (isShowFirst1()) {
            this.idNo = EncryptUtils.encryptIdNumShowFirst1AndLast4(this.idNo);
        } else {
            this.idNo = EncryptUtils.encryptIdNumShowLast4(this.idNo);
        }
    }

    public void encryptEmailInfo() {
        this.email = EncryptUtils.encryptEmailShowFirst1AndLast1(this.email);
    }

    public String getRiskVerifyStatus() {
        return riskVerifyStatus;
    }

    public void setTokenSwitch(boolean tokenSwitch) {
        this.tokenSwitch = tokenSwitch;
    }

    public void setUserCanChangeCard(boolean userCanChangeCard) {
        isUserCanChangeCard = userCanChangeCard;
    }

    public void setHasCardInAuditing(boolean hasCardInAuditing) {
        this.hasCardInAuditing = hasCardInAuditing;
    }

    public String getV8RiskStatus() {
        return v8RiskStatus;
    }

    public BigDecimal getInvestBalance() {
        return null != investBalance ? investBalance : BigDecimal.ZERO;
    }

    public String getInvestBalanceRate() {
        return null != investBalanceRate ? NumberUtils.percentageFormat(new BigDecimal(1).subtract(new BigDecimal(investBalanceRate))) : "0.00%";
    }

    public void setInvestBalanceRate(String investBalanceRate) {
        this.investBalanceRate = investBalanceRate;
    }

    public void setInvestBalance(BigDecimal investBalance) {
        this.investBalance = investBalance;
    }

    public BigDecimal getAmountToNextLevel() {
        BigDecimal investBalance = getInvestBalance();

        if ("3".equalsIgnoreCase(memberLevel)) {
            amountToNextLevel = BigDecimal.ZERO;
        }  else if ("2".equalsIgnoreCase(memberLevel)){
            amountToNextLevel = rank3Limit.subtract(investBalance);
        }  else {
            amountToNextLevel = rank2Limit.subtract(investBalance);
        }
        return amountToNextLevel;
    }

    public String getSecurityLevel() {
        int level = 0;
        if ("1".equalsIgnoreCase(nameAuthentication)) {
            level++;
        }
        if ("1".equalsIgnoreCase(cardBindStatus)) {
            level++;
        }

        if ("1".equalsIgnoreCase(tradingPasswordStatus)) {
            level++;
        }

        if ("1".equalsIgnoreCase(securityQuestionStatus)) {
            level++;
        }

        if (level >= 4) {
            securityLevel = "高";
        } else if (level <= 1) {
            securityLevel = "低";
        } else {
            securityLevel = "中";
        }

        return securityLevel;
    }

    public void setSecurityLevel(String securityLevel) {
        this.securityLevel = securityLevel;
    }

    public String getMemberLevel() {
        return memberLevel;
    }

    public void setMemberLevel(String memberLevel) {
        this.memberLevel = memberLevel;
    }

    public String getNationality() {
        return nationality;
    }
}
