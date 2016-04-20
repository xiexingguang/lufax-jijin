package com.lufax.jijin.fundation.remote.gson.request;

import com.lufax.jijin.fundation.constant.TransactionType;

import java.math.BigDecimal;

/**
 * 基金投资成功,资金解冻&虚拟户减钱
 */
public class SpecialPaymentRequestParam {

    private String channelId;
    private String instructionNo;
    private PaymentInfo paymentInfo;
    private String bankRefId;

    public SpecialPaymentRequestParam(Long tradeRecordId, String paymentOrderNo, BigDecimal subAmount, String remark, String subTransactionType, String bankRefId) {
        this.channelId = "JIJIN";
        this.instructionNo = tradeRecordId.toString();
        this.paymentInfo = new PaymentInfo(paymentOrderNo, subAmount, remark, subTransactionType, bankRefId);
    }

    public static class PaymentInfo {
        private String paymentOrderNo;
        private ActionType action;
        private String unfreezeTransactionType;
        private String unfreezeRemark;
        private BigDecimal subAmount;
        private String subTransactionType;
        private String subRemark;
        private String bankRefId;

        public PaymentInfo(String paymentOrderNo, BigDecimal subAmount, String remark, String subTransactionType, String bankRefId) {
            this.paymentOrderNo = paymentOrderNo;
            this.action = ActionType.CANCEL_SUBTRACT;
            this.unfreezeTransactionType = TransactionType.UNFROZEN_FUND.name();
            this.unfreezeRemark = remark;
            this.subAmount = subAmount;
            this.subTransactionType = subTransactionType;
            this.subRemark = remark;
            this.bankRefId = bankRefId;

        }
    }

    public static enum ActionType {
        PAYMENT, CANCEL, CANCEL_SUBTRACT
    }

    public String getInstructionNo() {
        return instructionNo;
    }
}
