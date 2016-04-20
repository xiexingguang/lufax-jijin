package com.lufax.jijin.fundation.constant;


public enum TradeRecordStatus {
    INIT,
    SUBMIT_SUCCESS,
    SUBMIT_FAIL,
    WITHDRAW_SUCCESS, WITHDRAW_FAIL,
    NOTIFY_SUCCESS, NOTIFY_FAIL,
    UNTRANSFER_MONEY, // 等待后续资金
    WAITING_MONEY,
    SUCCESS, FAIL,
    PARTIAL_SUCCESS,
    PAF_TRANSFER_SUCCESS, PAF_TRANSFER_FAIL,
    CANCEL_SUCCESS, CANCEL_FAIL,
    UNKNOWN,
    RECALLING,//赎回撤单网络超时等异常，系统会重试
    PROCESSING, // 转到银行卡处理中
    PAY_IN_ADVANCE, //还未进行先行垫付（大华货基异步流程）
    PAID_IN_ADVANCE, // 已先行垫付（大华货基异步流程）
    SENDING; // 大华异步流程补偿MQ发送中

}
