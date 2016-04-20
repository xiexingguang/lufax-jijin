package com.lufax.jijin.event.gson;


import com.lufax.jijin.sysFacade.gson.BaseGson;

public class EventResultGson extends BaseGson {

    private String result;

    public EventResultGson(String result) {
        this.result = result;
    }
}
