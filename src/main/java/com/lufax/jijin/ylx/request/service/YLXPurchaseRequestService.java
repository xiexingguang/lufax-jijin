package com.lufax.jijin.ylx.request.service;


import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.ylx.batch.dto.YLXBatchDTO;
import com.lufax.jijin.ylx.request.domain.purchase.YLXPurchaseRequestBatchFileCreator;
import com.lufax.jijin.ylx.request.domain.purchase.YLXPurchaseRequestBatchFileRecordCreator;
import com.lufax.jijin.ylx.request.domain.purchase.YLXPurchaseRequestRecordHandler;
import com.lufax.jijin.ylx.request.domain.purchase.YLXPurchaseRequestWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class YLXPurchaseRequestService extends BaseRequestService{

    @Autowired
    YLXPurchaseRequestBatchFileRecordCreator ylxPurchaseRequestBatchFileRecordCreator;
    @Autowired
    YLXPurchaseRequestRecordHandler ylxPurchaseRequestRecordHandler;
    @Autowired
    YLXPurchaseRequestBatchFileCreator ylxPurchaseRequestBatchFileCreator;



    public void prepareData(YLXBatchDTO batch){
        Logger.info(this, String.format("PurchaseRequestDataPrepare begin, batchId:%s", batch.getId()));

        ylxPurchaseRequestRecordHandler.updateBuyRequestWithCurrentBatchId(batch);

        ylxPurchaseRequestBatchFileRecordCreator.insertBatchFile(batch);

        Logger.info(this, String.format("PurchaseRequestDataPrepare end, batchId:%s",batch.getId()));
    }

    public void createFiles(YLXBatchDTO batch){
        Logger.info(this, String.format("PurchaseRequestFileCreation begin, batchId:%s",batch.getId()));
        ylxPurchaseRequestBatchFileCreator.createFiles(batch);
        Logger.info(this, String.format("PurchaseRequestDataPrepare end, batchId:%s",batch.getId()));
    }
}
