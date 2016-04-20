package com.lufax.jijin.fundation.remote.gson.request;

public class UnfreezeFundRequestGson extends BaseRequestGson {
    /**
     * （必选）冻结号
     */
    private Long frozenNo;
    /**
     * （必选）解冻类型【UNFROZEN_FUND】
     */
    private String transactionType;
    /**
     * （必选）备注
     */
    private String remark;

    public UnfreezeFundRequestGson(Long recordId, Long frozenNo, String transactionType, String remark) {
        super(recordId);
        this.frozenNo = frozenNo;
        this.transactionType = transactionType;
        this.remark = remark;
    }
}
