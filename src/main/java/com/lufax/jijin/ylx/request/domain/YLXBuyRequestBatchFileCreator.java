package com.lufax.jijin.ylx.request.domain;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class YLXBuyRequestBatchFileCreator extends YLXRequestBatchFileBaseCreator{

    @Autowired
    YLXBuyRequestWriter buyWriter;

    @Override
    protected IYLXRequestWriter getWriter() {
        return buyWriter;
    }
}
