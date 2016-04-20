package com.lufax.jijin.fundation.remote.gson.request;

import com.lufax.jijin.fundation.constant.TransactionType;

import java.math.BigDecimal;
import java.util.List;

/**
 * 基金直连项目使用,用于资金解冻并且虚拟户减钱
 */
public class SpecialUnfreezeFundRequestGson extends BaseRequestGson {
    /**
     * （必选）冻结号
     */
    private List<Long> frozenNoList;
    /**
     * （必选）解冻类型【UNFROZEN_FUND】
     */
    private String transactionType;
    /**
     * （必选）备注
     */
    private String remark;

    private BigDecimal subAmount;
    private String subTransactionType;
    private String subRemark;
    private String bankRefId;

    public SpecialUnfreezeFundRequestGson(Long recordId, List<Long> frozenNoList, String remark, BigDecimal subAmount, String subTransactionType, String bankRefId, String subRemark) {
        super(recordId);
        this.frozenNoList = frozenNoList;
        this.transactionType = TransactionType.UNFROZEN_FUND.name();
        this.remark = remark;
        this.subTransactionType = subTransactionType;
        this.subAmount = subAmount;
        this.bankRefId = bankRefId;
        this.subRemark = subRemark;
    }
}
