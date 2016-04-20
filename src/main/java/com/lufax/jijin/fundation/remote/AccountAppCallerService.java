package com.lufax.jijin.fundation.remote;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.lufax.jersey.utils.Logger;
import com.lufax.jijin.base.utils.JsonHelper;
import com.lufax.jijin.facade.caller.AccountInterfaceCaller;
import com.lufax.jijin.fundation.constant.TransactionType;
import com.lufax.jijin.fundation.gson.BaseResponseGson;
import com.lufax.jijin.fundation.gson.StandardTransferParamGson;
import com.lufax.jijin.fundation.gson.StandardTransferResponse;
import com.lufax.jijin.fundation.remote.gson.request.RedeemAuditRequestGson;
import com.lufax.jijin.fundation.remote.gson.request.SpecialUnfreezeFundRequestGson;
import com.lufax.jijin.fundation.remote.gson.request.UnfreezeFundRequestGson;
import com.lufax.jijin.sysFacade.caller.InterfaceCallObject;
import com.sun.jersey.api.client.ClientResponse;

@Service
public class AccountAppCallerService {

    @Autowired
    private AccountInterfaceCaller accountInterfaceCaller;

    public BaseResponseGson unfreezeFund(Long userId, Long freezeNo, Long instructionId, String remark) {
        UnfreezeFundRequestGson unfreezeFundRequestGson = new UnfreezeFundRequestGson(instructionId, freezeNo, TransactionType.UNFROZEN_FUND.name(), remark);

        String requestParams = new Gson().toJson(unfreezeFundRequestGson);

        Logger.info(this, String.format("Call account-app to unfreeze fund, the request params is : [%s]", requestParams));
        ClientResponse clientResponse = accountInterfaceCaller.postClientResponse(new InterfaceCallObject()
                .addFormValue("dto", requestParams)
                .withUrl(String.format("/users/%s/unfreezeFund", userId)).withUserId(userId));
        Logger.info(this, "Call account-app unfreeze fund response status is : " + clientResponse);
        String result = clientResponse.getEntity(String.class);
        Logger.info(this, "Call account-app unfreeze fund response info is : " + result);
        BaseResponseGson responseGson = new Gson().fromJson(result, BaseResponseGson.class);
        return responseGson;
    }

    /**
     * 解冻并且扣减虚拟户钱
     * @param userId
     * @param freezeNo
     * @param instructionId
     * @param remark
     * @param subAmount
     * @param subTransactionType
     * @param bankRefId
     * @param subRemark
     * @return
     */
    public BaseResponseGson specialUnfreezeFund(Long userId, Long freezeNo, Long instructionId, String remark, BigDecimal subAmount, String subTransactionType, String bankRefId, String subRemark) {
        List<Long> frozenNoList = new ArrayList<Long>();
        frozenNoList.add(freezeNo);
        SpecialUnfreezeFundRequestGson specialUnfreezeFundRequestGson = new SpecialUnfreezeFundRequestGson(instructionId, frozenNoList, remark, subAmount, subTransactionType, bankRefId, subRemark);
        String requestParams = new Gson().toJson(specialUnfreezeFundRequestGson);

        Logger.info(this, String.format("Call account-app to special unfreeze fund, the request params is : [%s]", requestParams));
        ClientResponse clientResponse = accountInterfaceCaller.postClientResponse(new InterfaceCallObject()
                .addFormValue("dto", requestParams)
                .withUrl(String.format("/users/%s/batchTxUnfreezeFund/subtract", userId)).withUserId(userId));
        Logger.info(this, "Call account-app batchTxUnfreezeFund subtract account response status is : " + clientResponse);
        String result = clientResponse.getEntity(String.class);
        Logger.info(this, "Call account-app batchTxUnfreezeFund subtract account response info is : " + result);
        BaseResponseGson responseGson = new Gson().fromJson(result, BaseResponseGson.class);
        return responseGson;

    }

    /**
     * 申购对账失败，赎回，现金分红，虚拟户加钱
     * 
     *
     * @param amount
     * @param channelId
     * @param userId
     * @param remark
     * @param instructionNo
     * @param appSheetNo
     * @return
     */
    public BaseResponseGson plusMoney(BigDecimal amount, String channelId, Long userId, String remark, String instructionNo, String type, String appSheetNo) {
        Logger.info(this, String.format("plusMoney:plus account money(%s) userId:%s instructionNo:%s type:%s appSheetNo:%s by call account system", amount, userId, instructionNo, type, appSheetNo));

        ClientResponse clientResponse = accountInterfaceCaller.postClientResponse(new InterfaceCallObject()
                .addFormValue("instructionNo", instructionNo)
                .addFormValue("channelId", channelId)
                .addFormValue("amount", amount)
                .addFormValue("userId", userId)
                .addFormValue("type", type)
                .addFormValue("remark", remark)
                .addFormValue("bankRefId", appSheetNo)
                .withUrl(String.format("/users/%s/plus/foundation", 0l)).withUserId(userId));

        Logger.info(this, "Call account-app plus amount response status is : " + clientResponse);
        String result = clientResponse.getEntity(String.class);
        Logger.info(this, "Call account-app plus amount response info is : " + result);
        BaseResponseGson responseGson = new Gson().fromJson(result, BaseResponseGson.class);

        return responseGson;
    }

    public BaseResponseGson redemmAudit(RedeemAuditRequestGson request) {
        Logger.info(this, "redeem audit:"+JsonHelper.toJson(request));
        
        //http://account.lufax.app/account-app/service/users/{userId}/frozen-fund-subtract/redeem
        ClientResponse clientResponse = accountInterfaceCaller.postClientResponse(new InterfaceCallObject()                      
                .addFormValue("dto", JsonHelper.toJson(request))
                .withUrl(String.format("/users/%s/frozen-fund-subtract/redeem", 0l)).withUserId(request.getUserId()));

        Logger.info(this, "redeem audit response status is : " + clientResponse);
        String result = clientResponse.getEntity(String.class);
        Logger.info(this, "redeem auditresponse info is : " + result);
        BaseResponseGson responseGson = new Gson().fromJson(result, BaseResponseGson.class);

        return responseGson;
    }

    /**
     * 标准转账接口
     * @param standardTransferParamGson
     * @return
     */
    public StandardTransferResponse transferMoney(StandardTransferParamGson standardTransferParamGson) {
        InterfaceCallObject interfaceCallObject = new InterfaceCallObject()
                .withUrl("/users/0/transfer/standard")
                .addFormValue("dto", new Gson().toJson(standardTransferParamGson));
        return accountInterfaceCaller.post(interfaceCallObject, StandardTransferResponse.class);
    }
}
