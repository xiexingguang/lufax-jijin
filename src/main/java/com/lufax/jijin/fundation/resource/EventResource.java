package com.lufax.jijin.fundation.resource;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringUtils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.lufax.jijin.base.utils.JsonHelper;
import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.event.gson.EventResultGson;
import com.lufax.jijin.fundation.dto.JijinTradeRecordDTO;
import com.lufax.jijin.fundation.gson.AccountChangeMsgGson;
import com.lufax.jijin.fundation.gson.JijinPaymentResultGson;
import com.lufax.jijin.fundation.gson.JijinStatusChangeMsgGson;
import com.lufax.jijin.fundation.gson.ListMessageGson;
import com.lufax.jijin.fundation.gson.PaymentWithdrawResultGson;
import com.lufax.jijin.fundation.repository.JijinTradeRecordRepository;
import com.lufax.jijin.fundation.service.DhEventService;
import com.lufax.jijin.fundation.service.EventService;
import com.lufax.jijin.fundation.service.PurchaseService;
import com.lufax.jijin.fundation.service.RedeemService;
import com.lufax.jijin.product.constant.ProductCategory;
import com.sun.jersey.api.core.InjectParam;

@Path("/fundation/event")
public class EventResource {

    @InjectParam
    private EventService eventService;
    @InjectParam
    private DhEventService dhEventService;
    @InjectParam
    private JijinTradeRecordRepository jijinTradeRecordRepository;
    @InjectParam
    private PurchaseService purchaseService;
    @InjectParam
    private RedeemService redeemService;


    /**
     * 基金直连认购/申购下单消息 TRX_REQUEST
     *
     * @param message
     * @return
     */
    @POST
    @Path("order-jijin")
    public String orderJijinEvent(@FormParam("message") String message) {
        Logger.info(this, String.format("Event of [TRX REQUEST][message: %s] received.", message));
        String productCategory = JsonHelper.parse(message).get("productCategory").getAsString();
        if (!ProductCategory.isJijin(productCategory)) {
            Logger.info(this, String.format("message is not for jijin ignore message [%s]", message));
            return new Gson().toJson(new EventResultGson("true"));
        }
        Logger.info(this, String.format("start event of [TRX REQUEST][message: %s] handled.", message));
        JsonElement businessMode = JsonHelper.parse(message).get("businessMode");
        String sourceFundCode = null;
        if (null != JsonHelper.parse(message).get("sourceFundCode")) {
            sourceFundCode = JsonHelper.parse(message).get("sourceFundCode").getAsString();
        }
        eventService.orderPurchase(JsonHelper.parse(message).get("fundCode").getAsString(),
                JsonHelper.parse(message).get("userId").getAsLong(),
                JsonHelper.parse(message).get("amount").getAsBigDecimal(),
                JsonHelper.parse(message).get("type").getAsString(),
                JsonHelper.parse(message).get("transactionId").getAsLong(),
                JsonHelper.parse(message).get("frozenCode").getAsString(),
                JsonHelper.parse(message).get("frozenType").getAsString(),
                JsonHelper.parse(message).get("isAgreeRisk").getAsString(),
                (businessMode == null) ? "" : businessMode.getAsString(), sourceFundCode);
        Logger.info(this, String.format("End Event of [TRX REQUEST][message: %s] handled.", message));
        return new Gson().toJson(new EventResultGson("true"));
    }

    /**
     * 基金直连认购/申购请求消息
     *
     * @param message
     * @return
     */
    @POST
    @Path("apply-jijin")
    public String applyJijinEvent(@FormParam("message") String message) {
        Logger.info(this, String.format("Event of [APPLY JIJIN][message: %s] received.", message));
        Long tradeRecordId = JsonHelper.parse(message).get("tradeRecordId").getAsLong();
        JijinTradeRecordDTO jijinTradeRecordDTO = jijinTradeRecordRepository.getRecordById(tradeRecordId);
        if (jijinTradeRecordDTO.getInstId().equals("dh103")) {
            dhEventService.doPurchase(tradeRecordId);
        } else {
            eventService.doPurchase(tradeRecordId);
        }

        Logger.info(this, String.format("Event of [APPLY JIJIN][message: %s] handled.", message));
        return new Gson().toJson(new EventResultGson("true"));
    }


    /**
     * 基金直连认购/申购下单成功后通知PAF做扣款
     *
     * @param message
     * @return
     */
    @POST
    @Path("order-jijin-success")
    public String jijinOrderSuccessEvent(@FormParam("message") String message) {
        Logger.info(this, String.format("Event of [ORDER JIJIN SUCCESS][message: %s] received.", message));
        Long tradeRecordId = JsonHelper.parse(message).get("tradeRecordId").getAsLong();
        JijinTradeRecordDTO jijinTradeRecordDTO = jijinTradeRecordRepository.getRecordById(tradeRecordId);
        if (jijinTradeRecordDTO.getInstId().equals("dh103")) {
            dhEventService.doWithdrawByPAF(tradeRecordId);
        } else {
            eventService.doWithdrawByPAF(tradeRecordId);
        }
        Logger.info(this, String.format("Event of [ORDER JIJIN SUCCESS][message: %s] handled.", message));
        return new Gson().toJson(new EventResultGson("true"));
    }

    /**
     * 基金直连认购/申购PAF扣款成功后发送扣款结果给基金公司
     *
     * @param message
     * @return
     */
    @POST
    @Path("withdraw-jijin-success")
    public String jijinWithdrawSuccessEvent(@FormParam("message") String message) {
        Logger.info(this, String.format("Event of [WITHDRAW JIJIN SUCCESS][message: %s] received.", message));
        Long tradeRecordId = JsonHelper.parse(message).get("tradeRecordId").getAsLong();
        JijinTradeRecordDTO jijinTradeRecordDTO = jijinTradeRecordRepository.getRecordById(tradeRecordId);
        if (jijinTradeRecordDTO.getInstId().equals("dh103")) {
            dhEventService.buyApplyNotify(tradeRecordId);
        } else {
            eventService.buyApplyNotify(tradeRecordId);
        }

        Logger.info(this, String.format("Event of [WITHDRAW JIJIN SUCCESS][message: %s] handled.", message));
        return new Gson().toJson(new EventResultGson("true"));
    }

    /**
     * 处理List app发送的基金状态变更消息
     *
     * @param message
     * @return
     */
    @POST
    @Path("jijin-status-update")
    public String JijinStatusUpdateEvent(@FormParam("message") String message) {
        Logger.info(this, String.format("Event of [ORDER STATUS UPDATE][message: %s] received.", message));
        ListMessageGson msg = JsonHelper.fromJson(message, ListMessageGson.class);
        if (msg.isSuccessFlag() && StringUtils.isNotEmpty(msg.getOriginalMsg())) {
            JijinStatusChangeMsgGson msg1 = JsonHelper.fromJson(msg.getOriginalMsg(), JijinStatusChangeMsgGson.class);
            eventService.updateJijinStatus(msg1);
        } else {
            Logger.info(this, String.format("Event of [ORDER STATUS UPDATE] not be handled, its successFlag is false."));
        }
        Logger.info(this, String.format("Event of [ORDER STATUS UPDATE][message: %s] handled.", message));
        return new Gson().toJson(new EventResultGson("true"));
    }

    /**
     * 处理平安付文件落地成功消息(代销基金流程使用)
     *
     * @param message
     * @return
     */
    @POST
    @Path("jijin-paf-record-success")
    public String pafFileRecordSuccessEvent(@FormParam("message") String message) {
        Logger.info(this, String.format("Event of [PAF FILE RECORD SUCCESS][message: %s] received.", message));
        eventService.copyFileForDaixiao(JsonHelper.parse(message).get("fileId").getAsLong(), JsonHelper.parse(message).get("instId").getAsString());
        Logger.info(this, String.format("Event of [PAF FILE RECORD SUCCESS][message: %s] handled.", message));
        return new Gson().toJson(new EventResultGson("true"));
    }

    @POST
    @Path("handle-dahua-account-change")
    public String jijinDahuaAccountChangeEvent(@FormParam("message") String message) {
        Logger.info(this, String.format("Event of [jijinDahuaAccountChangeEvent][message: %s] received.", message));
        AccountChangeMsgGson msg = JsonHelper.fromJson(message, AccountChangeMsgGson.class);
        eventService.handleAccountChangeMsg(msg);
        Logger.info(this, String.format("Event of [jijinDahuaAccountChangeEvent][message: %s] handled.", message));
        return new Gson().toJson(new EventResultGson("true"));
    }

    /**
     * 大华货基代扣回盘
     *
     * @param message
     * @return
     */
    @POST
    @Path("payment-result")
    @Produces(MediaType.APPLICATION_JSON)
    public String handlePaymentResult(@FormParam("message") final String message) {
        Logger.info(this, String.format("Event of [handlePaymentResult][message: %s] received.", message));
        JijinPaymentResultGson msg = JsonHelper.fromJson(message, JijinPaymentResultGson.class);
        eventService.handlePaymentResult(msg);
        Logger.info(this, String.format("Event of [handlePaymentResult][message: %s] handled.", message));
        return new Gson().toJson(new EventResultGson("0"));
    }

    @POST
    @Path("buy-currency-jijin")
    @Produces(MediaType.APPLICATION_JSON)
    public String buyT0CurrencyJjijn(@FormParam("message") final String message) {
        Logger.info(this, String.format("Event of [buyCurrencyFund][message: %s] received.", message));
        Long tradeRecordId = JsonHelper.parse(message).get("tradeRecordId").getAsLong();
        JijinTradeRecordDTO jijinTradeRecordDTO = jijinTradeRecordRepository.getRecordById(tradeRecordId);
        purchaseService.buyCurrencyJijin(jijinTradeRecordDTO);
        Logger.info(this, String.format("Event of [buyCurrencyFund][message: %s] handled.", message));
        return new Gson().toJson(new EventResultGson("0"));
    }

    @POST
    @Path("buy-jijin")
    @Produces(MediaType.APPLICATION_JSON)
    public String buyJijin(@FormParam("message") final String message) {
        Logger.info(this, String.format("Event of [buyJijin][message: %s] received.", message));
        Long tradeRecordId = JsonHelper.parse(message).get("tradeRecordId").getAsLong();
        JijinTradeRecordDTO jijinTradeRecordDTO = jijinTradeRecordRepository.getRecordById(tradeRecordId);
        purchaseService.buyJijin(jijinTradeRecordDTO);
        Logger.info(this, String.format("Event of [buyJijin][message: %s] handled.", message));
        return new Gson().toJson(new EventResultGson("0"));
    }

    @POST
    @Path("redeem-jijin")
    @Produces(MediaType.APPLICATION_JSON)
    public String redeemJijin(@FormParam("message") final String message) {
        Logger.info(this, String.format("Event of [redeemT1Jijin][message: %s] received.", message));
        Long tradeRecordId = JsonHelper.parse(message).get("tradeRecordId").getAsLong();
        JijinTradeRecordDTO jijinTradeRecordDTO = jijinTradeRecordRepository.getRecordById(tradeRecordId);
        redeemService.callJijinRedeem(jijinTradeRecordDTO);
        Logger.info(this, String.format("Event of [redeemT1Jijin][message: %s] handled.", message));
        return new Gson().toJson(new EventResultGson("0"));
    }

    @POST
    @Path("redeem-currency-jijin")
    @Produces(MediaType.APPLICATION_JSON)
    public String redeemT0CurrencyJijin(@FormParam("message") final String message) {
        Logger.info(this, String.format("Event of [redeemT0Jijin][message: %s] received.", message));
        Long tradeRecordId = JsonHelper.parse(message).get("tradeRecordId").getAsLong();
        JijinTradeRecordDTO jijinTradeRecordDTO = jijinTradeRecordRepository.getRecordById(tradeRecordId);
        redeemService.callJijinRedeem(jijinTradeRecordDTO);
        Logger.info(this, String.format("Event of [redeemT0Jijin][message: %s] handled.", message));
        return new Gson().toJson(new EventResultGson("0"));
    }

    @POST
    @Path("handle-force-redeem-pay")
    @Produces(MediaType.APPLICATION_JSON)
    public String handleForceRedeemPay(@FormParam("message") final String message) {
        Logger.info(this, String.format("Event of [handleForceRedeemPay][message: %s] received.", message));
        Long tradeRecordId = JsonHelper.parse(message).get("tradeRecordId").getAsLong();
        eventService.handleForceRedeemPay(tradeRecordId);
        Logger.info(this, String.format("Event of [handleForceRedeemPay][message: %s] handled.", message));
        return new Gson().toJson(new EventResultGson("0"));
    }
    
    /**
     * 处理消息：PAYMENT.WITHDRAW.RESULT.JIJIN
     * @param message
     * @return
     */
    @POST
    @Path("handle-transfer-to-bank")
    @Produces(MediaType.APPLICATION_JSON)
    public String handleTransferToBank(@FormParam("message") final String message) {
        Logger.info(this, String.format("Event of [handleTransferToBank][message: %s] received.", message));
        PaymentWithdrawResultGson msg = JsonHelper.fromJson(message, PaymentWithdrawResultGson.class);
        int res =redeemService.handleTransferToBankCard(msg);
        Logger.info(this, String.format("Event of [handleTransferToBank][message: %s] handled.", message));
        return new Gson().toJson(new EventResultGson(String.valueOf(res)));
    }
    
    
	/**
	 *      * 接受MQ，处理对应record，发送请求给大华
	 * @param message
	 * @return
	 */
    @Path("handle-paid-in-advance-redeem")
    @Produces(MediaType.APPLICATION_JSON)
    public String handlePaidInAdvanceRedeemrRecord(@FormParam("message") final String message) {
        Logger.info(this, String.format("Event of [handlePaidInAdvanceRedeemrRecord][message: %s] received.", message));
        Long tradeRecordId = JsonHelper.parse(message).get("tradeRecordId").getAsLong();
        
        JijinTradeRecordDTO jijinTradeRecordDTO = jijinTradeRecordRepository.getRecordById(tradeRecordId);
        redeemService.handlePaidInAdvanceRedeemRecord(jijinTradeRecordDTO);
        Logger.info(this, String.format("Event of [handlePaidInAdvanceRedeemrRecord][message: %s] handled.", message));
        return new Gson().toJson(new EventResultGson("0"));
    }

}
