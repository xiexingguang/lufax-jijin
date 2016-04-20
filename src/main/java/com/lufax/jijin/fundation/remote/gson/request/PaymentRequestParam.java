package com.lufax.jijin.fundation.remote.gson.request;

import com.lufax.jijin.fundation.constant.TransactionType;

public class PaymentRequestParam {

    private String channelId;
    private String instructionNo;
    private PaymentInfo paymentInfo;

    public PaymentRequestParam(Long tradeRecordId, String paymentOrderNo, String remark) {
        this.channelId = "JIJIN";
        this.instructionNo = tradeRecordId.toString();
        this.paymentInfo = new PaymentInfo(paymentOrderNo, remark);
    }

    public static class PaymentInfo {
        private String paymentOrderNo;
        private ActionType action;
        private String unfreezeTransactionType;
        private String unfreezeRemark;

        public PaymentInfo(String paymentOrderNo, String remark) {
            this.paymentOrderNo = paymentOrderNo;
            this.action = ActionType.CANCEL;
            this.unfreezeTransactionType = TransactionType.UNFROZEN_FUND.name();
            this.unfreezeRemark = remark;
        }
    }

    public static enum ActionType {
        PAYMENT, CANCEL, CANCEL_SUBTRACT
    }

    public String getInstructionNo() {
        return instructionNo;
    }
}
