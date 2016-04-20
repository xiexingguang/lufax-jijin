package com.lufax.jijin.ylx.request.service;


import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.ylx.batch.dto.YLXBatchDTO;
import com.lufax.jijin.ylx.request.domain.purchase.YLXPurchaseRequestBatchFileCreator;
import com.lufax.jijin.ylx.request.domain.purchase.YLXPurchaseRequestBatchFileRecordCreator;
import com.lufax.jijin.ylx.request.domain.purchase.YLXPurchaseRequestRecordHandler;
import com.lufax.jijin.ylx.request.domain.purchase.YLXPurchaseRequestWriter;
import com.lufax.jijin.ylx.request.domain.redeem.YLXRedeemRequestBatchFileCreator;
import com.lufax.jijin.ylx.request.domain.redeem.YLXRedeemRequestBatchFileRecordCreator;
import com.lufax.jijin.ylx.request.domain.redeem.YLXRedeemRequestRecordHandler;
import com.lufax.jijin.ylx.request.domain.redeem.YLXRedeemRequestWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class YLXRedeemRequestService extends BaseRequestService{

    @Autowired
    YLXRedeemRequestBatchFileRecordCreator ylxRedeemRequestBatchFileRecordCreator;
    @Autowired
    YLXRedeemRequestRecordHandler ylxRedeemRequestRecordHandler;
    @Autowired
    YLXRedeemRequestBatchFileCreator ylxRedeemRequestBatchFileCreator;



    public void prepareData(YLXBatchDTO batch){
        Logger.info(this, String.format("RedeemRequestDataPrepare begin, batchId:%s", batch.getId()));

        ylxRedeemRequestRecordHandler.updateBuyRequestWithCurrentBatchId(batch);

        ylxRedeemRequestBatchFileRecordCreator.insertBatchFile(batch);

        Logger.info(this, String.format("RedeemRequestDataPrepare end, batchId:%s",batch.getId()));
    }

    public void createFiles(YLXBatchDTO batch){
        Logger.info(this, String.format("RedeemRequestFileCreation begin, batchId:%s",batch.getId()));
        ylxRedeemRequestBatchFileCreator.createFiles(batch);
        Logger.info(this, String.format("RedeemRequestDataPrepare end, batchId:%s",batch.getId()));
    }
}
