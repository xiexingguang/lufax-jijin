package com.lufax.jijin.fundation.service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.lufax.jijin.base.utils.DateUtils;
import com.lufax.jijin.base.utils.JijinAppProperties;
import com.lufax.jijin.base.utils.JijinInstIdPlatMerchantIdMapHolder;
import com.lufax.jijin.base.utils.JsonHelper;
import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.fundation.constant.BalanceHistoryBizType;
import com.lufax.jijin.fundation.constant.JijinBizType;
import com.lufax.jijin.fundation.constant.ResourceResponseCode;
import com.lufax.jijin.fundation.constant.TradeRecordStatus;
import com.lufax.jijin.fundation.constant.TradeRecordType;
import com.lufax.jijin.fundation.constant.TransactionType;
import com.lufax.jijin.fundation.dto.JijinInfoDTO;
import com.lufax.jijin.fundation.dto.JijinTradeLogDTO;
import com.lufax.jijin.fundation.dto.JijinTradeRecordDTO;
import com.lufax.jijin.fundation.dto.JijinUserBalanceDTO;
import com.lufax.jijin.fundation.dto.JijinUserBalanceHistoryDTO;
import com.lufax.jijin.fundation.dto.builder.JijinUserBalenceHistoryDTOBuilder;
import com.lufax.jijin.fundation.gson.BaseGson;
import com.lufax.jijin.fundation.gson.BaseResponseGson;
import com.lufax.jijin.fundation.remote.AccountAppCallerService;
import com.lufax.jijin.fundation.remote.JijinGatewayRemoteService;
import com.lufax.jijin.fundation.remote.LjbGateWayRemoteService;
import com.lufax.jijin.fundation.remote.gson.paf.request.PAFCancelRequestGson;
import com.lufax.jijin.fundation.remote.gson.paf.response.PAFCancelResponseGson;
import com.lufax.jijin.fundation.remote.gson.request.GWCancelOrderRequestGson;
import com.lufax.jijin.fundation.remote.gson.request.GWCancelRequestGson;
import com.lufax.jijin.fundation.remote.gson.request.PaymentInfo;
import com.lufax.jijin.fundation.remote.gson.request.PaymentRequestGson;
import com.lufax.jijin.fundation.remote.gson.request.RevokeInfo;
import com.lufax.jijin.fundation.remote.gson.response.GWCancelOrderResponseGson;
import com.lufax.jijin.fundation.remote.gson.response.GWResponseCode;
import com.lufax.jijin.fundation.remote.gson.response.GWResponseGson;
import com.lufax.jijin.fundation.repository.JijinInfoRepository;
import com.lufax.jijin.fundation.repository.JijinTradeLogRepository;
import com.lufax.jijin.fundation.repository.JijinTradeRecordRepository;
import com.lufax.jijin.fundation.repository.JijinUserBalanceHistoryRepository;
import com.lufax.jijin.fundation.repository.JijinUserBalanceRepository;
import com.lufax.jijin.fundation.util.JijinUtils;
import com.lufax.jijin.service.MqService;
import com.lufax.jijin.sysFacade.gson.result.PaymentResult;
import com.lufax.jijin.sysFacade.gson.result.PolicyInfoGson;
import com.lufax.jijin.sysFacade.service.PaymentAppService;
import com.lufax.jijin.sysFacade.service.TradeSvcService;


@Service
public class CancelOrderService {

    @Autowired
    private JijinTradeRecordRepository jijinTradeRecordRepository;
    @Autowired
    private JijinTradeLogRepository jijinTradeLogRepository;
    @Autowired
    private SequenceService sequenceService;
    @Autowired
    private RedeemServiceUtil redeemServiceUtil;
    @Autowired
    private JijinGatewayRemoteService jijinGatewayService;
    @Autowired
    private JijinInfoRepository jijinInfoRepository;
    @Autowired
    private LjbGateWayRemoteService pafGateWayRemoteService;
    @Autowired
    private AccountAppCallerService accountAppCallerService;
    @Autowired
    private JijinUserBalanceRepository jijinUserBalanceRepository;
    @Autowired
    private JijinUserBalanceHistoryRepository jijinUserBalanceHistoryRepository;
    @Autowired
    private JijinInstIdPlatMerchantIdMapHolder jijinInstIdPlatMerchantIdMapHolder;
    @Autowired
    private MqService mqService;
    @Autowired
    private EventService eventService;
    @Autowired
    private JijinAppProperties jijinAppProperties;
    @Autowired
    private PaymentAppService paymentAppService;
    @Autowired
    private TradeSvcService tradeSvcService;
    @Autowired
    private RedeemToPurchaseService redeemToPurchaseService;

    public void setJijinGateway(JijinGatewayRemoteService jijinGatewayService) {
        this.jijinGatewayService = jijinGatewayService;
    }
    
    public void setJijinPafGateway(LjbGateWayRemoteService pafGateWayRemoteService) {
        this.pafGateWayRemoteService = pafGateWayRemoteService;
    }
    
    public void setEventService(EventService eventService) {
        this.eventService = eventService;
    }

	public void setPafGateWayRemoteService(
			LjbGateWayRemoteService pafGateWayRemoteService) {
		this.pafGateWayRemoteService = pafGateWayRemoteService;
	}


    public String cancelPurchase(Long tradeRecordId, long userId) {
        //合并申购撤单
        BaseGson baseGson = new BaseGson();
        JijinTradeRecordDTO jijinTradeRecordDTO = jijinTradeRecordRepository.getRecordById(tradeRecordId);
        JijinInfoDTO jijinInfoDTO = jijinInfoRepository.findJijinInfo(MapUtils.buildKeyValueMap("fundCode", jijinTradeRecordDTO.getFundCode()));
        if (jijinTradeRecordDTO.getStatus().equals(TradeRecordStatus.CANCEL_SUCCESS.name())) {
            baseGson.setRetCode("00");
            baseGson.setRetMessage("cancel purchase success");
            Logger.info(this, String.format("do cancel purchase success, tradeRecordId [%s]", tradeRecordId));
            return new Gson().toJson(baseGson);
        }

        //double check can cancel
        String result = checkCancel(tradeRecordId, userId);
        Logger.info(this, String.format("check cancel result is [%s]", result));
        BaseGson bGson = JsonHelper.fromJson(result, BaseGson.class);
        if (!bGson.getRetCode().equals("00")) {
            baseGson.setRetCode("01");
            baseGson.setRetMessage("cancel purchase failed");
            Logger.info(this, String.format("do cancel purchase failed, tradeRecordId [%s]", tradeRecordId));
            return new Gson().toJson(baseGson);
        }

        String cancelAppNo = sequenceService.getSerialNumber(JijinBizType.CANCEL_ORDER.getCode());

        GWCancelRequestGson gwCancelRequestGson = new GWCancelRequestGson();
        gwCancelRequestGson.setOriginalAppSheetNo(jijinTradeRecordDTO.getAppSheetNo());
        gwCancelRequestGson.setVersion("1.0");
        gwCancelRequestGson.setIsIndividual("1");
        gwCancelRequestGson.setInstId(jijinInfoDTO.getInstId());
        gwCancelRequestGson.setContractNo(jijinTradeRecordDTO.getContractNo());
        gwCancelRequestGson.setApplicationNo(cancelAppNo);

        GWResponseGson gwResponseGson = jijinGatewayService.cancel(jijinInfoDTO.getInstId(), new Gson().toJson(gwCancelRequestGson));
        GWCancelOrderResponseGson cancelOrderResultGson = new GWCancelOrderResponseGson();
        if (gwResponseGson.getRetCode().equals(GWResponseCode.SUCCESS)) {
            cancelOrderResultGson = JsonHelper.fromJson(gwResponseGson.getContent(), GWCancelOrderResponseGson.class);
        } else {
            jijinTradeLogRepository.insertBusJijinTradeLog(new JijinTradeLogDTO(jijinTradeRecordDTO.getUserId(), jijinTradeRecordDTO.getFundCode(), jijinTradeRecordDTO.getId(), TradeRecordStatus.CANCEL_FAIL.name(), jijinTradeRecordDTO.getType(), jijinTradeRecordDTO.getReqAmount(), jijinTradeRecordDTO.getTrxTime(), jijinTradeRecordDTO.getTrxDate()));
            baseGson.setRetCode("01");
            baseGson.setRetMessage("cancel purchase failed");
            Logger.info(this, String.format("do cancel purchase failed , tradeRecordId [%s]", tradeRecordId));
            return new Gson().toJson(baseGson);
        }


        //基金公司处理成功后调用paf接口做划款
        if (cancelOrderResultGson.getErrorCode().equals("0000")) {
            PAFCancelRequestGson pafCancelRequestGson = new PAFCancelRequestGson();
            pafCancelRequestGson.setTransType("002");
            pafCancelRequestGson.setMercOrderNo(sequenceService.getSerialNumber(JijinBizType.PAF_CANCEL.getCode()));
            pafCancelRequestGson.setOrigMercOrderNo(jijinTradeRecordDTO.getAppSheetNo());
            pafCancelRequestGson.setOrderAmount(jijinTradeRecordDTO.getReqAmount().multiply(new BigDecimal(100)).longValue());
            pafCancelRequestGson.setOrderCurrency("CNY");
            pafCancelRequestGson.setMerchantId(jijinInstIdPlatMerchantIdMapHolder.getPlatMerchantId(jijinInfoDTO.getInstId()));
            pafCancelRequestGson.setPlatMerchantId(jijinInstIdPlatMerchantIdMapHolder.getPlatMerchantId("lufax"));

            Logger.info(this, String.format("call paf do cancel,tradeRecordId is [%s] , requestGson is [%s]", tradeRecordId, new Gson().toJson(pafCancelRequestGson)));
            GWResponseGson gson = pafGateWayRemoteService.cancel(new Gson().toJson(pafCancelRequestGson));
            Logger.info(this, String.format("call paf do cancel tradeRecordId [%s] , responseGson is [%s], ", tradeRecordId, new Gson().toJson(gson)));
            if (gson.isSuccess()) {
                PAFCancelResponseGson pafCancelResponseGson = JsonHelper.fromJson(gson.getContent(), PAFCancelResponseGson.class);
                Logger.info(this, String.format("call paf do cancel tradeRecordId [%s] , pafCancelResponseGson is [%s], ", tradeRecordId, new Gson().toJson(pafCancelResponseGson)));
                if (pafCancelResponseGson.getRespCode().equals("0000") || pafCancelResponseGson.getRespCode().equals("0044")) {
                	
                	//查询trading相关信息
                	PolicyInfoGson policyInfo = tradeSvcService.getTradeInfoByTrxId(String.valueOf(jijinTradeRecordDTO.getTrxId()));
                	if(null == policyInfo || !policyInfo.isSuccess()){
                		//查询不trading的信息或者查询接口失败
                		Logger.info(this, String.format("call trade get cancel info failed tradeRecordId [%s]", tradeRecordId));
                		jijinTradeLogRepository.insertBusJijinTradeLog(new JijinTradeLogDTO(jijinTradeRecordDTO.getUserId(), jijinTradeRecordDTO.getFundCode(), jijinTradeRecordDTO.getId(), "CANCEL_FAIL", TradeRecordType.PURCHASE, jijinTradeRecordDTO.getReqAmount(), jijinTradeRecordDTO.getTrxTime(), jijinTradeRecordDTO.getTrxDate()));
                		baseGson.setRetCode("01");
                        baseGson.setRetMessage("cancel purchase failed");
                        return new Gson().toJson(baseGson);
                	}
                	//调用payment调增用户虚拟户，冻结陆金币（如有），撤销入账
                	PaymentResult paymentRt = paymentAppService.canceOrderAndFrozenCoin(jijinInfoDTO, tradeRecordId, userId, policyInfo.getAmount(), policyInfo.getCoinAmount(), cancelOrderResultGson.getAppSheetSerialNo(), jijinTradeRecordDTO.getAppSheetNo(), jijinInfoDTO.getInstId(), jijinTradeRecordDTO.getTrxDate());
                    if (!paymentRt.isSuccess()) {
                    	//调用payment调增失败
                        Logger.error(this, String.format("NEED_MANUAL_HANDLE,cancel order success but call account plus money failed,tradeRecordId [%s]", tradeRecordId));
                    }else{
                    	if(null !=paymentRt.getFrozenInfoResultGson() && null != paymentRt.getFrozenInfoResultGson().getFrozenNo()){
                    		//如果有陆金币，需要发消息解冻
                    		mqService.sendCancleOrderCoinFrozenCode(userId, jijinTradeRecordDTO.getTrxId(), paymentRt.getFrozenInfoResultGson().getFrozenNo(), jijinTradeRecordDTO.getTrxTime());
                    	}
                    }
                    jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", tradeRecordId, "status", TradeRecordStatus.CANCEL_SUCCESS.name(), "cancelAppNo", cancelAppNo, "cancelAppSheetNo", cancelOrderResultGson.getAppSheetSerialNo()));
                    jijinTradeLogRepository.insertBusJijinTradeLog(new JijinTradeLogDTO(jijinTradeRecordDTO.getUserId(), jijinTradeRecordDTO.getFundCode(), jijinTradeRecordDTO.getId(), "CANCEL_SUCCESS", jijinTradeRecordDTO.getType(), jijinTradeRecordDTO.getReqAmount(), jijinTradeRecordDTO.getTrxTime(), jijinTradeRecordDTO.getTrxDate()));
                    updateApplyingAmount(jijinTradeRecordDTO.getUserId(), jijinTradeRecordDTO.getFundCode(), jijinTradeRecordDTO.getReqAmount(),jijinTradeRecordDTO.getTrxDate(),tradeRecordId);
                    mqService.sendPurchaseResultMsg(jijinTradeRecordDTO.getTrxId(), "fail", BigDecimal.ZERO);
                    baseGson.setRetCode("00");
                    baseGson.setRetMessage("cancel purchase success");
                    Logger.info(this, String.format("do cancel purchase success, tradeRecordId [%s]", tradeRecordId));
                      
                    return new Gson().toJson(baseGson);
                }
            } else {
                Logger.info(this, String.format("call paf do cancel failed tradeRecordId [%s]", tradeRecordId));
            }
        } else {
            Logger.info(this, String.format("call jijin-gw do cancel purchase failed,tradeRecordId [%s]", tradeRecordId));
        }

        //撤单失败
        jijinTradeLogRepository.insertBusJijinTradeLog(new JijinTradeLogDTO(jijinTradeRecordDTO.getUserId(), jijinTradeRecordDTO.getFundCode(), jijinTradeRecordDTO.getId(), "CANCEL_FAIL", TradeRecordType.PURCHASE, jijinTradeRecordDTO.getReqAmount(), jijinTradeRecordDTO.getTrxTime(), jijinTradeRecordDTO.getTrxDate()));
        baseGson.setRetCode("01");
        baseGson.setRetMessage("cancel purchase failed");
        return new Gson().toJson(baseGson);
    }

    /**
     * 更新申购在途金额
     *
     * @param userId
     * @param fundCode
     * @param reqAmount
     */
    private void updateApplyingAmount(Long userId, String fundCode, BigDecimal reqAmount, String bizDate,Long tradeRecordId) {
        int result = 0;
        int retry_times = 0;
        JijinUserBalanceDTO userBalanceDTO = new JijinUserBalanceDTO(); 
        while (result == 0) {
            userBalanceDTO = jijinUserBalanceRepository.findUserBalanceByFundCode(userId, fundCode);
            userBalanceDTO.setApplyingAmount(userBalanceDTO.getApplyingAmount().subtract(reqAmount));
            result = jijinUserBalanceRepository.updateFundAccount(userBalanceDTO);
            retry_times = retry_times + 1;
            if (retry_times > 10) break;
        }
        // insert user balance history
        if(result==1){
            JijinUserBalanceHistoryDTO jijinUserBalanceHistoryDTO = JijinUserBalenceHistoryDTOBuilder.buildUserBalanceHistoryDTO(userBalanceDTO,bizDate,new BigDecimal("0"),new BigDecimal("0"),"申购撤单成功，调减在徒资金",BalanceHistoryBizType.申购撤单,tradeRecordId);
            jijinUserBalanceHistoryRepository.insertBusJijinUserBalanceHistory(jijinUserBalanceHistoryDTO);
        }
    }


    /**
     * 赎回撤单
     *
     * @param tradeRecordId
     * @return
     */
    public String cancelRedeem(Long tradeRecordId, boolean isRetry, long userId) {

        BaseGson baseGson = new BaseGson();
        baseGson.setRetCode(ResourceResponseCode.SUCCESS);
        baseGson.setRetMessage("cancel redeem success");
        JijinTradeRecordDTO jijinTradeRecordDTO = jijinTradeRecordRepository.getRecordById(tradeRecordId);
        if (jijinTradeRecordDTO.getStatus().equals(TradeRecordStatus.CANCEL_SUCCESS.name())) {
            Logger.info(this, String.format("do cancel redeem success, tradeRecordId [%s]", tradeRecordId));
            return new Gson().toJson(baseGson);
        }

        //double check can cancel
        if (!isRetry) {
            String checkResult = checkCancel(tradeRecordId, userId);
            BaseGson bGson = JsonHelper.fromJson(checkResult, BaseGson.class);
            if (!bGson.getRetCode().equals(ResourceResponseCode.SUCCESS)) {
                baseGson.setRetCode(ResourceResponseCode.CANCEL_REDEEM_FAIL);
                baseGson.setRetMessage("cancel redeem failed");
                Logger.info(this, String.format("this record can not be cancelled now, tradeRecordId [%s]", tradeRecordId));
                return new Gson().toJson(baseGson);
            }
        }

        String cancelAppNo = sequenceService.getSerialNumber(JijinBizType.CANCEL_ORDER.getCode());
        JijinInfoDTO info = jijinInfoRepository.findJijinInfo(MapUtils.buildKeyValueMap("fundCode", jijinTradeRecordDTO.getFundCode()));

        GWCancelOrderRequestGson request = new GWCancelOrderRequestGson();
        request.setVersion("1.0");
        request.setInstId(info.getInstId());
        request.setIsIndividual("1");
        request.setContractNo(jijinTradeRecordDTO.getContractNo());
        request.setApplicationNo(cancelAppNo);
        request.setOriginalAppSheetNo(jijinTradeRecordDTO.getAppSheetNo());

        GWResponseGson res = jijinGatewayService.cancel(info.getInstId(), JsonHelper.toJson(request));
        BigDecimal amount = null;
        boolean isHuoji = JijinUtils.isHuoji(info);

        amount = jijinTradeRecordDTO.getReqShare();

        if (res.getRetCode().equals(GWResponseCode.SUCCESS)) {
            GWCancelOrderResponseGson jijinRes = JsonHelper.fromJson(res.getContent(), GWCancelOrderResponseGson.class);
            if (jijinRes.getErrorCode().equals("0000")) {  //基金公司返回撤单成功，解冻份额,update trade record, trade log
                boolean result = redeemServiceUtil.unFrezee(amount, jijinTradeRecordDTO.getUserId(), jijinTradeRecordDTO.getFundCode());
                if (!result) { //按撤单成功,但是解冻失败，打标人工解冻
                    Logger.info(this, String.format("do cancel redeem success,but unfreeze fail,,NEED MANUAL ATTENTION,tradeRecordId [%s]", tradeRecordId));
                    updateRedeemTradeRecordAndinsertTradeLog(isHuoji, jijinTradeRecordDTO, TradeRecordStatus.CANCEL_SUCCESS, amount, jijinRes.getErrorCode(), cancelAppNo, jijinRes.getAppSheetSerialNo(), true, true);
                } else {
                    Logger.info(this, String.format("do cancel redeem success, tradeRecordId [%s]", tradeRecordId));
                    updateRedeemTradeRecordAndinsertTradeLog(isHuoji, jijinTradeRecordDTO, TradeRecordStatus.CANCEL_SUCCESS, amount, jijinRes.getErrorCode(), cancelAppNo, jijinRes.getAppSheetSerialNo(), false, true);
                    // insert user balance history
                    JijinUserBalanceDTO jijinUserBalanceDTO = jijinUserBalanceRepository.findUserBalanceByFundCode(userId, jijinTradeRecordDTO.getFundCode());
                    JijinUserBalanceHistoryDTO jijinUserBalanceHistoryDTO = JijinUserBalenceHistoryDTOBuilder.buildUserBalanceHistoryDTO(jijinUserBalanceDTO,jijinTradeRecordDTO.getTrxDate(),amount,new BigDecimal("0"),"赎回撤单成功，调减冻结份额，调增份额",BalanceHistoryBizType.赎回撤单,jijinTradeRecordDTO.getId());
                    jijinUserBalanceHistoryRepository.insertBusJijinUserBalanceHistory(jijinUserBalanceHistoryDTO);
                
                }
            } else {
                // 插 trade log
                Logger.info(this, String.format("do cancel redeem fail, tradeRecordId [%s]", tradeRecordId));
                updateRedeemTradeRecordAndinsertTradeLog(isHuoji, jijinTradeRecordDTO, TradeRecordStatus.CANCEL_FAIL, amount, jijinRes.getErrorCode(), cancelAppNo, null, false, false);
                baseGson.setRetCode(ResourceResponseCode.CANCEL_REDEEM_FAIL);
                baseGson.setRetMessage("cancel redeem fail");
            }
        } else if (res.getRetCode().equals(GWResponseCode.RUNTIME_ERROR)) { // 超时,需要补偿job重试
            jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "status", TradeRecordStatus.RECALLING.name()));
            baseGson.setRetCode(ResourceResponseCode.CANCEL_REDEEM_RECALLING);
            baseGson.setRetMessage("cancel redeem timeout");
            Logger.info(this, String.format("do cancel redeem timeout,will retry later, tradeRecordId [%s]", tradeRecordId));

        } else {
            // 插 trade log
            updateRedeemTradeRecordAndinsertTradeLog(isHuoji, jijinTradeRecordDTO, TradeRecordStatus.CANCEL_FAIL, amount, null, cancelAppNo, null, false, false);
            baseGson.setRetCode(ResourceResponseCode.CANCEL_REDEEM_FAIL);
            baseGson.setRetMessage("cancel redeem fail");
            Logger.info(this, String.format("do cancel redeem fail, tradeRecordId [%s]", tradeRecordId));
        }

        return new Gson().toJson(baseGson);
    }

    /**
     * 检查交易是否可撤单
     *
     * @param tradeRecordId
     * @return
     */
    public String checkCancel(Long tradeRecordId, long userId) {

        BaseGson baseGson = new BaseGson();
        baseGson.setRetCode("00"); // can cancel
        baseGson.setRetMessage("can cancel");

        JijinTradeRecordDTO jijinTradeRecordDTO = jijinTradeRecordRepository.getRecordById(tradeRecordId);

        //user
        if (jijinTradeRecordDTO.getUserId() != userId) {
            baseGson.setRetCode("01"); // can not cancel
            baseGson.setRetMessage("not belong to current user,can not cancel");
            return new Gson().toJson(baseGson);
        }

        //活动代申购不能撤单
        if("market".equals(jijinTradeRecordDTO.getBusinessMode())){
            baseGson.setRetCode("01");
            baseGson.setRetMessage("market type can not cancel");
            return new Gson().toJson(baseGson);
        }


        // 只有申购 赎回可以撤单
        if (TradeRecordType.PURCHASE != jijinTradeRecordDTO.getType()
                && TradeRecordType.REDEEM != jijinTradeRecordDTO.getType()&&  TradeRecordType.HZ_PURCHASE != jijinTradeRecordDTO.getType()) {
            baseGson.setRetCode("01"); // can not cancel
            baseGson.setRetMessage("type not correct, can not cancel");
            return new Gson().toJson(baseGson);
        }

        if (TradeRecordType.PURCHASE == jijinTradeRecordDTO.getType()) {
            if (!TradeRecordStatus.NOTIFY_SUCCESS.name().equals(jijinTradeRecordDTO.getStatus()) && !TradeRecordStatus.NOTIFY_FAIL.name().equals(jijinTradeRecordDTO.getStatus())) {
                baseGson.setRetCode("01"); // can not cancel
                baseGson.setRetMessage("purchase status not correct can not cancel");
                return new Gson().toJson(baseGson);
            }
        }

        if (TradeRecordType.REDEEM == jijinTradeRecordDTO.getType()) {
            if (!TradeRecordStatus.SUBMIT_SUCCESS.name().equals(jijinTradeRecordDTO.getStatus())) {
                baseGson.setRetCode("01"); // can not cancel
                baseGson.setRetMessage("redeem status not correct can not cancel");
                return new Gson().toJson(baseGson);
            }
        }

        if (TradeRecordType.HZ_PURCHASE == jijinTradeRecordDTO.getType()) {
            if (!TradeRecordStatus.SUBMIT_SUCCESS.name().equals(jijinTradeRecordDTO.getStatus())) {
                baseGson.setRetCode("01"); // can not cancel
                baseGson.setRetMessage("redeem status not correct can not cancel");
                return new Gson().toJson(baseGson);
            }
        }

        String trxDate = jijinTradeRecordDTO.getTrxDate(); // 交易所属日期yyyyMMdd

        Date trx = DateUtils.parseDate(trxDate, DateUtils.DATE_STRING_FORMAT);

        Calendar trxCutOff = Calendar.getInstance();
        trxCutOff.setTime(trx);
        trxCutOff.set(Calendar.HOUR_OF_DAY, 14);
        trxCutOff.set(Calendar.MINUTE, 59);
        trxCutOff.set(Calendar.SECOND, 30);

        Date now = new Date();

        if (now.after(trxCutOff.getTime())) { //如果是交易日的14：59：30后，则不能撤单
            baseGson.setRetCode("01"); // can not cancel
            baseGson.setRetMessage("can  not cancel");
        }

        return new Gson().toJson(baseGson);
    }

    @Transactional
    public void updateRedeemTradeRecordAndinsertTradeLog(boolean isHuoji, JijinTradeRecordDTO jijinTradeRecordDTO, TradeRecordStatus stauts, BigDecimal amount, String errorCode, String cancelAppNo, String cancelAppSheetNo, boolean isControversial, boolean needUpdate) {

    	jijinTradeLogRepository.insertBusJijinTradeLog(new JijinTradeLogDTO(jijinTradeRecordDTO.getUserId(), jijinTradeRecordDTO.getFundCode(), jijinTradeRecordDTO.getId(), stauts.name(), TradeRecordType.REDEEM, null, amount, jijinTradeRecordDTO.getTrxTime(), jijinTradeRecordDTO.getTrxDate()));
        if (needUpdate) {
            if (isControversial) {
                jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "errorCode", errorCode, "cancelAppNo", cancelAppNo, "cancelAppSheetNo", cancelAppSheetNo, "status", stauts.name(), "errorMsg", "赎回撤单成功，但是解冻失败，需要人工解冻", "isControversial", "1"));
            } else {
                jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "errorCode", errorCode, "cancelAppNo", cancelAppNo, "cancelAppSheetNo", cancelAppSheetNo, "status", stauts.name()));
            }
        }
    }

    /**
     * 撤销转购
     * @param tradeRecordId
     * @param userId
     * @return
     */
    @Transactional
    public String cancelRedeemToPurchase(Long tradeRecordId,Long userId){
        BaseGson baseGson = new BaseGson();
        JijinTradeRecordDTO jijinTradeRecordDTO = jijinTradeRecordRepository.getRecordById(tradeRecordId);
        //(1)检查能否撤销
        BaseGson checkResult = redeemToPurchaseService.checkCanBeCancelOrNot(jijinTradeRecordDTO,userId);
        Logger.info(this,String.format("checkCanBeCancelOrNot result is [%s]",new Gson().toJson(checkResult)));
        if(!ResourceResponseCode.SUCCESS.equals(checkResult.getRetCode())){
            baseGson.setRetCode("01");
            baseGson.setRetMessage(checkResult.getRetMessage());
            return new Gson().toJson(baseGson);
        }
        //(2)call gw 撤销转购
        GWResponseGson res = redeemToPurchaseService.callGw(jijinTradeRecordDTO.getFundCode(), jijinTradeRecordDTO);
        if (res.getRetCode().equals(GWResponseCode.SUCCESS)) {//GW 返回成功
            GWCancelOrderResponseGson jijinRes = JsonHelper.fromJson(res.getContent(), GWCancelOrderResponseGson.class);
            if ("0000".equals(jijinRes.getErrorCode())) {
                //（01）基金公司返回撤单成功，解冻份额
                redeemToPurchaseService.cancelSuccess(jijinTradeRecordDTO, userId,jijinRes);
                baseGson.setRetCode(ResourceResponseCode.SUCCESS);
                baseGson.setRetMessage("cancel redeemToPurchase success");
                mqService.sendPurchaseResultMsg(jijinTradeRecordDTO.getTrxId(), "fail", BigDecimal.ZERO);
            }else{
                //(02)基金公司返回撤单失败，直接失败
                redeemToPurchaseService.cancelFail(jijinTradeRecordDTO);
                baseGson.setRetCode("01");
                baseGson.setRetMessage("cancel redeemToPurchase fail");
            }
        }else if(GWResponseCode.RUNTIME_ERROR.equals(res.getRetCode())){//GW返回异常，基金公司接口超时
            //（03）call gw 超时，打标，job重试
            redeemToPurchaseService.cancelTimeout(jijinTradeRecordDTO);
            baseGson.setRetCode("02");
            baseGson.setRetMessage("cancel redeemToPurchase timeout");
            Logger.info(this, String.format("do cancel redeemToPurchase timeout,will retry later, tradeRecordId [%s]", tradeRecordId));
        }else{
            //（04）调用GW失败,撤销失败
            redeemToPurchaseService.cancelFail(jijinTradeRecordDTO);
            baseGson.setRetCode("01");
            baseGson.setRetMessage("cancel redeemToPurchase fail");
            Logger.info(this, String.format("do cancel redeemToPurchase fail, tradeRecordId [%s]", tradeRecordId));
        }
        return new Gson().toJson(baseGson);
    }
}
