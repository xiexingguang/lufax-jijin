package com.lufax.jijin.ylx.request.domain.redeem;


import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.product.constant.ProductCategory;
import com.lufax.jijin.ylx.batch.dto.YLXBatchDTO;
import com.lufax.jijin.ylx.batch.repository.YLXBatchRepository;
import com.lufax.jijin.ylx.dto.repository.YLXSellRequestDetailRepository;
import com.lufax.jijin.ylx.enums.YLXTradeDetailType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class YLXRedeemRequestRecordHandler {

    @Autowired
    protected YLXSellRequestDetailRepository ylxSellRequestDetailRepository;
    @Autowired
    protected YLXBatchRepository ylxBatchRepository;

    public void updateBuyRequestWithCurrentBatchId(YLXBatchDTO batch){
        int count = ylxSellRequestDetailRepository.updateBatchId(-1L, batch.getId(), batch.getTargetDate(), ProductCategory.SLP, YLXTradeDetailType.REDEEM);
        Logger.info(this,String.format("update %s redeem request detail for batchId:%s",count,batch.getId()));
    }
}
