package com.lufax.jijin.ylx.request.domain.purchase;


import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.product.constant.ProductCategory;
import com.lufax.jijin.ylx.batch.dto.YLXBatchDTO;
import com.lufax.jijin.ylx.batch.repository.YLXBatchRepository;
import com.lufax.jijin.ylx.dto.repository.YLXBuyRequestDetailRepository;
import com.lufax.jijin.ylx.enums.YLXTradeDetailType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class YLXPurchaseRequestRecordHandler {

    @Autowired
    protected YLXBuyRequestDetailRepository ylxBuyRequestDetailRepository;
    @Autowired
    protected YLXBatchRepository ylxBatchRepository;

    public void updateBuyRequestWithCurrentBatchId(YLXBatchDTO batch){
        int count = ylxBuyRequestDetailRepository.updateBatchId(-1L, batch.getId(), batch.getTargetDate(), ProductCategory.SLP, YLXTradeDetailType.PURCHASE);
        Logger.info(this,String.format("update %s purchase request detail for batchId:%s",count,batch.getId()));
    }
}
