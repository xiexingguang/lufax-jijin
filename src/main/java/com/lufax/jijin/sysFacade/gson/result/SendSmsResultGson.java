package com.lufax.jijin.sysFacade.gson.result;


import java.util.List;

public class SendSmsResultGson {
    private int result = -1;
    private long smsRequestId;
    private List<String> errorCodeList;
    private List<String> errorMsgList;

    public boolean isSucceed() {
        return this.result == 0;
    }

    public int getResult() {
        return result;
    }

    public long getSmsRequestId() {
        return smsRequestId;
    }
}
