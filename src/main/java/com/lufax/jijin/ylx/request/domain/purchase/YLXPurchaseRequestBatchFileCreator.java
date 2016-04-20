package com.lufax.jijin.ylx.request.domain.purchase;


import com.lufax.jijin.ylx.request.domain.IYLXRequestWriter;
import com.lufax.jijin.ylx.request.domain.YLXRequestBatchFileBaseCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class YLXPurchaseRequestBatchFileCreator extends YLXRequestBatchFileBaseCreator {

    @Autowired
    YLXPurchaseRequestWriter purchaseRequestWriter;

    @Override
    protected IYLXRequestWriter getWriter() {
        return purchaseRequestWriter;
    }
}
