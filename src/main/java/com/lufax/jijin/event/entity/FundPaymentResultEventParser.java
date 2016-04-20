package com.lufax.jijin.event.entity;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lufax.jijin.base.utils.JsonHelper;

import java.math.BigDecimal;

public class FundPaymentResultEventParser {

    public static String FUND_WITHDRAW_INSTRUCTION_ID = "instructionId";
    public static String FUND_WITHDRAW_CHANNEL_ID = "channelId";
    public static String FUND_WITHDRAW_STATUS = "status";
    public static String FUND_WITHDRAW_ACTUAL_AMOUNT = "actualAmount";
    public static String FUND_WITHDRAW_ERROR_CODE = "errorCode";

    public static FundPaymentResultEvent parse(String message){

        FundPaymentResultEvent event = new FundPaymentResultEvent();
        JsonObject jsonResult = JsonHelper.parse(message);
        String recordId = jsonResult.get(FUND_WITHDRAW_INSTRUCTION_ID).getAsString();
        String channelId = jsonResult.get(FUND_WITHDRAW_CHANNEL_ID).getAsString();
        String status = jsonResult.get(FUND_WITHDRAW_STATUS).getAsString();
        BigDecimal amt = jsonResult.get(FUND_WITHDRAW_ACTUAL_AMOUNT).getAsBigDecimal();
        JsonElement errorCode =  jsonResult.get(FUND_WITHDRAW_ERROR_CODE);
        event.setAmt(amt);
        event.setStatus(status);
        event.setChannelId(channelId);
        event.setRecordId(recordId);
        event.setErrorCode(null!=errorCode?errorCode.getAsString():null);
        return event;
    }
}
