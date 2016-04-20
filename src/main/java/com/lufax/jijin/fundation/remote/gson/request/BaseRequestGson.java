package com.lufax.jijin.fundation.remote.gson.request;

public class BaseRequestGson {
    /**
     * (必选)渠道标识
     */
    private String channelId = "JIJIN";
    /**
     * (必选)指令号(客户端流水号)
     */
    private Long recordId;

    public BaseRequestGson(Long recordId) {
        this.recordId = recordId;
    }
}
