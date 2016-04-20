package com.lufax.jijin.fundation.gson;

import java.math.BigDecimal;

/**
 * Created by NiuZhanJun on 10/28/15.
 */
public class StandardTransferParamGson {
    /**
     * （可选）
     */
    private Long fromUserId;
    /**
     * （可选）转出账户的账号
     */
    private Long fromAccountNo;
    /**
     * （可选）转入账户的用户号
     */
    private Long toUserId;
    /**
     * （可选）转入账户的账号
     */
    private Long toAccountNo;
    /**
     * （必选）转出账户的转账类型
     */
    private String fromTransactionType;
    /**
     * （必选）转入账户的转账类型
     */
    private String toTransactionType;
    /**
     * （必选）转账金额
     * 客户端建议使用Double类型，可截断小数部分无效零
     */
    private BigDecimal transferredAmount;
    /**
     * （可选）转出账户的备注
     */
    private String fromRemark;
    /**
     * （可选）转入账户的备注
     */
    private String toRemark;

    /**
     * (必选)渠道标识
     */
    private String channelId;
    /**
     * (必选)指令号(客户端流水号)
     */
    private Long recordId;

    public StandardTransferParamGson() {
    }

    public StandardTransferParamGson(Long fromUserId, Long toUserId, String fromTransactionType, String toTransactionType, BigDecimal transferredAmount, String fromRemark, String toRemark, Long recordId,String channelId) {
        this.fromAccountNo = null;
        this.fromRemark = fromRemark;
        this.fromTransactionType = fromTransactionType;
        this.fromUserId = fromUserId;
        this.toAccountNo = null;
        this.toRemark = toRemark;
        this.toTransactionType = toTransactionType;
        this.toUserId = toUserId;
        this.transferredAmount = transferredAmount;
        this.channelId = channelId;
        this.recordId = recordId;
    }


    public void setFromTransactionType(String fromTransactionType) {
        this.fromTransactionType = fromTransactionType;
    }

    public void setToTransactionType(String toTransactionType) {
        this.toTransactionType = toTransactionType;
    }
}
