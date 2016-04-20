package com.lufax.jijin.ylx.request.domain.redeem;


import com.lufax.jijin.ylx.request.domain.IYLXRequestWriter;
import com.lufax.jijin.ylx.request.domain.YLXRequestBatchFileBaseCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class YLXRedeemRequestBatchFileCreator extends YLXRequestBatchFileBaseCreator {

    @Autowired
    YLXRedeemRequestWriter redeemRequestWriter;

    @Override
    protected IYLXRequestWriter getWriter() {
        return redeemRequestWriter;
    }
}
