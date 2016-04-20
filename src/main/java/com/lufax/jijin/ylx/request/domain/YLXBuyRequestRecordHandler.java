package com.lufax.jijin.ylx.request.domain;


import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.product.constant.ProductCategory;
import com.lufax.jijin.ylx.batch.dto.YLXBatchDTO;
import com.lufax.jijin.ylx.batch.repository.YLXBatchRepository;
import com.lufax.jijin.ylx.dto.repository.YLXBuyRequestDetailRepository;
import com.lufax.jijin.ylx.enums.YLXTradeDetailType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class YLXBuyRequestRecordHandler {

    @Autowired
    protected YLXBuyRequestDetailRepository ylxBuyRequestDetailRepository;
    @Autowired
    protected YLXBatchRepository ylxBatchRepository;

    public void updateBuyRequestWithCurrentBatchId(YLXBatchDTO batch){
        int count = ylxBuyRequestDetailRepository.updateBatchId(-1L, batch.getId(), batch.getTargetDate(), ProductCategory.SLP, YLXTradeDetailType.BUY);
        Logger.info(this,String.format("update %s buy request detail for batchId:%s",count,batch.getId()));
       /* ylxBatchRepository.updateYLXBatchDTOSuccessById(batch.getId(),
                YLXBatchStatus.request_data_preparing.name(),
                YLXBatchSubStep.sub_step_1_3.name());
        batch.setSubNextStep(YLXBatchSubStep.sub_step_1_3.name());*/
    }
}
