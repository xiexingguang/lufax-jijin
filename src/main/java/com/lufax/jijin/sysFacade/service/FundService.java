package com.lufax.jijin.sysFacade.service;

import com.google.gson.Gson;
import com.lufax.jijin.base.utils.DateUtils;
import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.sysFacade.caller.FundInterfaceCaller;
import com.lufax.jijin.sysFacade.caller.InterfaceCallObject;
import com.lufax.jijin.sysFacade.gson.result.BaseResultDTO;
import com.lufax.jijin.sysFacade.gson.result.PaymentResultGson;
import com.lufax.jijin.user.repository.UserRepository;
import com.sun.jersey.api.client.ClientResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class FundService {

    @Autowired
    private FundInterfaceCaller fundInterfaceCaller;
    @Autowired
    private UserRepository userRepository;

    /**
     * 对公代扣
     *
     * @param userId
     * @param instructionId
     * @param amount
     * @param cardId
     * @return
     */
    public BaseResultDTO rechargeForCompany(Long userId, String instructionId, BigDecimal amount, String cardId,String remark) {
        Logger.info(this, String.format("Call fund-app to recharge for company , amount %s ", amount));
        long faSysUserId = userRepository.getFASysUserId();
        InterfaceCallObject interfaceCallObject = new InterfaceCallObject().withUrl(String.format("users/%s/recharge/company", faSysUserId)).withUserId(faSysUserId)
                .addFormValue("userId", userId)
                .addFormValue("amount", amount)
                .addFormValue("type", "AUTO_RECHARGE")
                .addFormValue("instructionId", instructionId)
                .addFormValue("tradeTime", DateUtils.formatDate(new Date(), DateUtils.DATE_TIME_FORMAT))
                .addFormValue("channelId", "JIJIN")
                .addFormValue("callbackUrl", "")
                .addFormValue("remark", remark)
                .addFormValue("cardId", cardId);


        ClientResponse clientResponse = fundInterfaceCaller.postClientResponse(interfaceCallObject);
        Logger.info(this, "Call fund-app to recharge for company response status is : " + clientResponse);
        String result = clientResponse.getEntity(String.class);
        Logger.info(this, "Call fund-app to recharge for company response info is : " + result);
        BaseResultDTO baseResultDTO = new Gson().fromJson(result, BaseResultDTO.class);
        return baseResultDTO;
    }

    /**
     * 对公代付
     *
     * @param userId
     * @param instructionId
     * @param amount
     * @param cardId
     * @return
     */
    public BaseResultDTO withdrawForCompany(Long userId, String instructionId, BigDecimal amount, String cardId,String remark) {
        Logger.info(this, String.format("Call fund-app to withdraw for company , amount %s ", amount));
        long faSysUserId = userRepository.getFASysUserId();
        InterfaceCallObject interfaceCallObject = new InterfaceCallObject().withUrl(String.format("users/%s/withdraw/company", faSysUserId)).withUserId(faSysUserId)
                .addFormValue("userId", userId)
                .addFormValue("amount", amount)
                .addFormValue("type", "CUSTOMER_WITHDRAWAL")
                .addFormValue("instructionId", instructionId)
                .addFormValue("tradeTime", DateUtils.formatDate(new Date(), DateUtils.DATE_TIME_FORMAT))
                .addFormValue("channelId", "JIJIN")
                .addFormValue("callbackUrl", "")
                .addFormValue("remark", remark)
                .addFormValue("cardId", cardId);


        ClientResponse clientResponse = fundInterfaceCaller.postClientResponse(interfaceCallObject);
        Logger.info(this, "Call fund-app to withdraw for company response status is : " + clientResponse);
        String result = clientResponse.getEntity(String.class);
        Logger.info(this, "Call fund-app to withdraw for company response info is : " + result);
        BaseResultDTO baseResultDTO = new Gson().fromJson(result, BaseResultDTO.class);
        return baseResultDTO;
    }

    public PaymentResultGson getPaymentResult(String instructionId) {
        Logger.info(this, String.format("Call fund-app to get payment result , instructionId %s ", instructionId));
        long faSysUserId = userRepository.getFASysUserId();
        InterfaceCallObject interfaceCallObject = new InterfaceCallObject().withUrl("foreign-request/payment-result").withUserId(faSysUserId)
                .addQueryParam("instructionId", String.valueOf(instructionId))
                .addQueryParam("channelId", "JIJIN");

        ClientResponse clientResponse = fundInterfaceCaller.getClientResponseWithQueryParams(interfaceCallObject);
        Logger.info(this, "Call fund-app to get payment result response status is : " + clientResponse);
        String result = clientResponse.getEntity(String.class);
        Logger.info(this, "Call fund-app to get payment result response info is : " + result);
        PaymentResultGson paymentResultGson = new Gson().fromJson(result, PaymentResultGson.class);
        return paymentResultGson;
    }

}
