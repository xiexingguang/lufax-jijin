package com.lufax.jijin.ylx.request.domain;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class YLXOpenRequestBatchFileCreator extends YLXRequestBatchFileBaseCreator {
    @Autowired
    YLXOpenRequestWriter openWriter;

    @Override
    protected IYLXRequestWriter getWriter() {
        return openWriter;
    }
}
