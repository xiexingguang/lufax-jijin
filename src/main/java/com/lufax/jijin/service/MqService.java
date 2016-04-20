package com.lufax.jijin.service;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.lufax.jersey.utils.Logger;
import com.lufax.jijin.base.utils.DateUtils;
import com.lufax.jijin.base.utils.JsonHelper;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.fundation.dto.JijinIncreaseDTO;
import com.lufax.jijin.fundation.dto.JijinNetValueDTO;
import com.lufax.mq.client.MqClient;
import com.lufax.mq.client.model.values.DestinationType;
import org.apache.activemq.util.MapHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Service
public class MqService {

    @Autowired
    private MqClient mqClient;
    @Autowired 
    private MqClient mqClientLBO;

    public void sendJobExeResultSuccess(String runId, String result, String runCallbackDest) {
        Logger.info(this, String.format("Send Job Status to MQ [runId:%s].[result:%s].[runCallbackDest:%s].", runId, result, runCallbackDest));
        mqClient.send(UUID.randomUUID().toString(), runCallbackDest, DestinationType.TOPIC, String.format("{\"runId\":%s,\"result\":%s}", runId, result));
    }

    /**
     * YLX同步buy confirm 信息给trade
     */
    public void sendYLXBuyConfirmMsg(String refId, Long trxId, String type, boolean result) {
        Map msg = MapUtils.buildKeyValueMap("trxId", trxId, "type", type, "result", result);
        mqClient.send(refId, "YLX.FUND.BUY.CONFIRMED", DestinationType.TOPIC, new Gson().toJson(msg));
    }


    /**
     * 基金直连申购落地成功消息
     *
     * @param tradeRecordId
     */
    public void sendJijinRecordSuccessMsg(Long tradeRecordId) {
        Map msg = MapUtils.buildKeyValueMap("tradeRecordId", tradeRecordId);
        mqClient.send("", "JIJIN.RECORD.SUCCESS", DestinationType.QUEUE, JsonHelper.toJson(msg));
    }

    /**
     * 基金直连申购下单成功消息
     *
     * @param tradeRecordId
     */
    public void sendJijinOrderSuccessMsg(Long tradeRecordId) {
        Map msg = MapUtils.buildKeyValueMap("tradeRecordId", tradeRecordId);
        mqClient.send("", "JIJIN.ORDER.SUCCESS", DestinationType.QUEUE, JsonHelper.toJson(msg));
    }

    /**
     * 基金直连申购通知PAF扣款成功消息
     *
     * @param tradeRecordId
     */
    public void sendJijinWithdrawSuccessMsg(Long tradeRecordId) {
        Map msg = MapUtils.buildKeyValueMap("tradeRecordId", tradeRecordId);
        mqClient.send("", "JIJIN.WITHDRAW.SUCCESS", DestinationType.QUEUE, JsonHelper.toJson(msg));
    }

    /**
     * 基金直连申购成功or失败消息,通知trading
     *
     * @param trxId
     * @param status
     * @param confirmAmount
     */
    public void sendPurchaseResultMsg(Long trxId, String status, BigDecimal confirmAmount) {
        Map msg = MapUtils.buildKeyValueMap("trxId", trxId, "status", status, "confirmAmount", confirmAmount);
        mqClient.send("", "PURCHASE.RESULT", DestinationType.TOPIC, JsonHelper.toJson(msg));
    }

    /**
     * 基金直连货基消息体, 这个消息只给YEB消费
     *
     * @param trxId
     * @param status
     */
    public void sendDhTradeResultMsg(Long trxId, String status) {
        Map msg = MapUtils.buildKeyValueMap("trxId", trxId, "status", status);
        mqClient.send("", "DH.TRADE.RESULT", DestinationType.QUEUE, JsonHelper.toJson(msg));
    }
    
    /**
     * 基金直连货基消息体,这个消息只给LBO消费
     * {'trxId':xxxxx,'result':'xxxx','type':'xxxx','errorCode':'xxxx'}
     *
     * @param trxId
     * @param status
     */
    public void sendLBOTradeResultMsg(Long trxId, String result,String type,String errorCode) {
        Map msg = MapUtils.buildKeyValueMap("trxId", trxId, "result", result, "type", type, "errorCode", errorCode);
        mqClientLBO.send("", "JIJIN.LBO.TRX.RESULT", DestinationType.QUEUE, JsonHelper.toJson(msg));
    }

    /**
     * 基金代销落地平安付代发文件成功后发送消息给jijin-app消费 做文件copy
     *
     * @param fileId bus_jijin_sync_file主键
     * @param instId
     */
    public void sendRecordPafFileSuccessMsg(Long fileId, String instId) {
        Map msg = MapUtils.buildKeyValueMap("fileId", fileId, "instId", instId);
        mqClient.send("", "PAF.FILE.RECORD.SUCCESS", DestinationType.TOPIC, JsonHelper.toJson(msg));
    }

    public void sendJijinNetValue(JijinNetValueDTO netValue) {
        Logger.info(this, "send the jijin net value message: " + netValue);
        Map<String, String> paras = Maps.newHashMap();
        paras.put("fundCode", netValue.getFundCode());
        paras.put("netValueDate", netValue.getNetValueDate());
        paras.put("netValue", netValue.getNetValue().setScale(4, BigDecimal.ROUND_HALF_UP).toString());
        paras.put("totalNetValue", netValue.getTotalNetValue().setScale(4, BigDecimal.ROUND_HALF_UP).toString());
        paras.put("benefitPerTenthousand", netValue.getBenefitPerTenthousand().setScale(4, BigDecimal.ROUND_HALF_UP).toString());
        paras.put("interestratePerSevenday", netValue.getInterestratePerSevenday().setScale(4, BigDecimal.ROUND_HALF_UP).toString());
        mqClient.send("", "JIJIN.NETVALUE", DestinationType.TOPIC, JsonHelper.toJson(paras));
    }

    public void sendJijinIncrease(JijinIncreaseDTO increase) {
        Logger.info(this, "send the jijin increase message: " + increase);
        Map<String, String> paras = Maps.newHashMap();
        paras.put("fundCode", increase.getFundCode());
        paras.put("increaseDate", increase.getIncreaseDate());
        paras.put("dayIncrease", getIncreaseValue(increase.getDayIncrease()));
        paras.put("monthIncrease", getIncreaseValue(increase.getMonthIncrease()));
        paras.put("sixMonthIncrease", getIncreaseValue(increase.getSixMonthIncrease()));
        paras.put("thisYearIncrease", getIncreaseValue(increase.getThisYearIncrease()));
        paras.put("threeMonthIncrease", getIncreaseValue(increase.getThreeMonthIncrease()));
        paras.put("totalIncrease", getIncreaseValue(increase.getTotalIncrease()));
        paras.put("yearIncrease", getIncreaseValue(increase.getYearIncrease()));
        mqClient.send("", "JIJIN.INCREASE", DestinationType.TOPIC, JsonHelper.toJson(paras));
    }

    public void sendDahuaAccountBalanceChange(String fundCode, String msgType) {
        Logger.info(this, "send the dahua jijin account changed message: " + fundCode);
        Map<String, String> paras = Maps.newHashMap();
        paras.put("fundCode", fundCode);
        paras.put("type", msgType);
        mqClient.send("", "JIJIN.DH.ACCOUNT.CHANGED", DestinationType.TOPIC, JsonHelper.toJson(paras));
    }

    /**
     *  强赎消息，驱动强赎资金分发
     * @param tradeRecordId
     */
    public void sendForceRedemmPayMsg(Long tradeRecordId) {
        Map msg = MapUtils.buildKeyValueMap("tradeRecordId", tradeRecordId);
        mqClient.send("", "JIJIN.FROCE_REDEEM.PAY", DestinationType.TOPIC, JsonHelper.toJson(msg));
    }
    
    private String getIncreaseValue(BigDecimal value) {
        if (value != null) {
            return value.setScale(4, BigDecimal.ROUND_HALF_UP).toString();
        } else {
            return null;
        }
    }

    /**
     * 基金退币发冻结号给coinApp
     * @param frozenCode
     */
    public void sendCancleOrderCoinFrozenCode(Long userId,Long trxId,String frozenNo,String tradeDate){
    	String dateFormat  = null;
    	try{
    		dateFormat = DateUtils.formatDate(DateUtils.parseDate(tradeDate, DateUtils.CMS_DRAW_SEQUENCE_FORMAT),DateUtils.DATE_TIME_FORMAT);
    	}catch(Exception e){
    		Logger.error(this, "formate date error before send message to coin-app tradeDate is["+tradeDate+"]", e);
    	}
    	Map map = MapUtils.buildKeyValueMap("userId",userId,"trxId",trxId,"frozenNo",frozenNo,"tradeDate",dateFormat,"strategy",3);
    	Logger.info(this, String.format("send message to coin-app  [%s]", JsonHelper.toJson(map)));
    	mqClient.send("", "JIJIN.CANCEL.ORDER.RETURN.COIN", DestinationType.QUEUE, JsonHelper.toJson(map));
    }
    
    
    /**
     * T0货币基金认申购MQ接口
     * @param tradeRecordId
     */
    public void sendCurrencyJijinBuyMQ(Long tradeRecordId){

        Map msg = MapUtils.buildKeyValueMap("tradeRecordId", tradeRecordId);
        mqClient.send("", "JIJIN.CURRENCY.BUY.REQUEST", DestinationType.QUEUE, JsonHelper.toJson(msg));
    }
    
    /**
     * T1基金认申购MQ接口
     * @param tradeRecordId
     */
    public void sendJijinBuyMQ(Long tradeRecordId){

        Map msg = MapUtils.buildKeyValueMap("tradeRecordId", tradeRecordId);
        mqClient.send("", "JIJIN.BUY.REQUEST", DestinationType.QUEUE, JsonHelper.toJson(msg));
    }
    
    /**
     * 基金T1赎回MQ接口
     * @param tradeRecordId
     */
    public void sendJijinRedeemMQ(Long tradeRecordId){

        Map msg = MapUtils.buildKeyValueMap("tradeRecordId", tradeRecordId);
        mqClient.send("", "JIJIN.REDEEM.REQUEST", DestinationType.QUEUE, JsonHelper.toJson(msg));
    }
    
    
    /**
     * 基金T0赎回MQ接口
     * @param tradeRecordId
     */
    public void sendJijinT0RedeemMQ(Long tradeRecordId){

        Map msg = MapUtils.buildKeyValueMap("tradeRecordId", tradeRecordId);
        mqClient.send("", "JIJIN.CURRENCY.REDEEM.REQUEST", DestinationType.QUEUE, JsonHelper.toJson(msg));
    }
    
    
    /**
     * 
     * 中途岛支付赎回通知结果
     * {'tradeRecordId':xxxxx,'frozenCode':'xxxxx','result':'success'}
     * @param tradeRecordId
     */
    public void sendJijinRedeemPayResultMQ(Long tradeRecordId, Long frozenCode, String result){

        Map msg = MapUtils.buildKeyValueMap("trxId", tradeRecordId,"frozenCode",frozenCode,"result",result);
        mqClient.send("", "JIJIN.REDEEM_PAY.RESULT", DestinationType.QUEUE, JsonHelper.toJson(msg));
    }
    
    
    public void sendPaidInAdvanceMQ(Long tradeRecordId){
        Map msg = MapUtils.buildKeyValueMap("tradeRecordId", tradeRecordId);
        mqClient.send("", "JIJIN.PAYINADVANCE.REQUEST", DestinationType.QUEUE, JsonHelper.toJson(msg));
    }

}
