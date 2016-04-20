package com.lufax.jijin.fundation.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.lufax.jijin.base.utils.DateUtils;
import com.lufax.jijin.base.utils.JijinInstIdPlatMerchantIdMapHolder;
import com.lufax.jijin.base.utils.JsonHelper;
import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.base.utils.NumberUtils;
import com.lufax.jijin.fundation.constant.BalanceHistoryBizType;
import com.lufax.jijin.fundation.constant.FundType;
import com.lufax.jijin.fundation.constant.JijinBizType;
import com.lufax.jijin.fundation.constant.TradeRecordStatus;
import com.lufax.jijin.fundation.constant.TradeRecordType;
import com.lufax.jijin.fundation.constant.TransactionType;
import com.lufax.jijin.fundation.dto.JijinAccountDTO;
import com.lufax.jijin.fundation.dto.JijinInfoDTO;
import com.lufax.jijin.fundation.dto.JijinTradeLogDTO;
import com.lufax.jijin.fundation.dto.JijinTradeRecordDTO;
import com.lufax.jijin.fundation.dto.JijinUserBalanceDTO;
import com.lufax.jijin.fundation.dto.JijinUserBalanceHistoryDTO;
import com.lufax.jijin.fundation.dto.builder.JijinUserBalenceHistoryDTOBuilder;
import com.lufax.jijin.fundation.gson.BaseResponseGson;
import com.lufax.jijin.fundation.gson.PurchaseOrderResultGson;
import com.lufax.jijin.fundation.remote.AccountAppCallerService;
import com.lufax.jijin.fundation.remote.FundAppCallerService;
import com.lufax.jijin.fundation.remote.JijinGatewayRemoteService;
import com.lufax.jijin.fundation.remote.LjbGateWayRemoteService;
import com.lufax.jijin.fundation.remote.gson.paf.request.PAFPayMerReservedRequestGson;
import com.lufax.jijin.fundation.remote.gson.paf.request.PAFPayRequestGson;
import com.lufax.jijin.fundation.remote.gson.paf.response.PAFPayResponseGson;
import com.lufax.jijin.fundation.remote.gson.request.BuyNotifyExtension;
import com.lufax.jijin.fundation.remote.gson.request.GWBuyApplyNotifyRequestGson;
import com.lufax.jijin.fundation.remote.gson.request.GWPurchaseRequestGson;
import com.lufax.jijin.fundation.remote.gson.request.PaymentInfo;
import com.lufax.jijin.fundation.remote.gson.request.PaymentRequestGson;
import com.lufax.jijin.fundation.remote.gson.request.PaymentRequestParam;
import com.lufax.jijin.fundation.remote.gson.request.PurchaseExtension;
import com.lufax.jijin.fundation.remote.gson.request.SubTractInfo;
import com.lufax.jijin.fundation.remote.gson.response.GWBuyApplyNotifyResponseGson;
import com.lufax.jijin.fundation.remote.gson.response.GWResponseCode;
import com.lufax.jijin.fundation.remote.gson.response.GWResponseGson;
import com.lufax.jijin.fundation.repository.JijinAccountRepository;
import com.lufax.jijin.fundation.repository.JijinInfoRepository;
import com.lufax.jijin.fundation.repository.JijinTradeLogRepository;
import com.lufax.jijin.fundation.repository.JijinTradeRecordRepository;
import com.lufax.jijin.fundation.repository.JijinUserBalanceHistoryRepository;
import com.lufax.jijin.fundation.repository.JijinUserBalanceRepository;
import com.lufax.jijin.fundation.util.JijinUtils;
import com.lufax.jijin.service.MqService;
import com.lufax.jijin.sysFacade.gson.BatchTxUnfreezeRequest;
import com.lufax.jijin.sysFacade.gson.ClearInfo;
import com.lufax.jijin.sysFacade.gson.FrozenNo;
import com.lufax.jijin.sysFacade.gson.PaymentRequest;
import com.lufax.jijin.sysFacade.gson.SubtractInfo;
import com.lufax.jijin.sysFacade.gson.result.PaymentResult;
import com.lufax.jijin.sysFacade.service.PaymentAppService;
import com.lufax.jijin.user.domain.UserInfoGson;
import com.lufax.jijin.user.service.UserService;

/**
 * T0 货基认申购
 * （下单，付款，通知 ）
 * @author XUNENG311
 *
 */
@Service
public class JijinyT0SyncService {
	
	@Autowired
    private JijinAccountRepository jijinAccountRepository;
    @Autowired
    private JijinTradeRecordRepository jijinTradeRecordRepository;
    @Autowired
    private JijinTradeLogRepository jijinTradeLogRepository;
    @Autowired
    private MqService mqService;
    @Autowired
    private AccountAppCallerService accountAppCallerService;
    @Autowired
    private SequenceService sequenceService;
    @Autowired
    private JijinUserBalanceRepository jijinUserBalanceRepository;
    @Autowired
    private JijinUserBalanceHistoryRepository jijinUserBalanceHistoryRepository;
    @Autowired
    private JijinGatewayRemoteService jijinGatewayRemoteService;
    @Autowired
    private JijinInfoRepository jijinInfoRepository;
    @Autowired
    private FundAppCallerService fundAppCallerService;
    @Autowired
    private LjbGateWayRemoteService pafGateWayRemoteService;
    @Autowired
    private JijinInstIdPlatMerchantIdMapHolder jijinInstIdPlatMerchantIdMapHolder;
    @Autowired
    private UserService userService;
    @Autowired
    private PaymentAppService paymentAppService ;
	

	 /**
	  * 调用jijin-gw做申购请求(T0 货基)
	  * @param jijinTradeRecordDTO
	  * @param accountDTO
	  * @param jijinInfoDTO
	  */
    public void callT0JijinPurchase(JijinTradeRecordDTO jijinTradeRecordDTO) {
        Logger.info(this, String.format("dahua do purchase message be got ,tradeRecordId id [%s]", jijinTradeRecordDTO.getId()));
        
        JijinInfoDTO jijinInfoDTO = jijinInfoRepository.findJijinInfo(MapUtils.buildKeyValueMap("fundCode",jijinTradeRecordDTO.getFundCode()));
        JijinAccountDTO jijinAccountDTO = jijinAccountRepository.findActiveAccount(jijinTradeRecordDTO.getUserId(),jijinInfoDTO.getInstId(), "PAF");
        
        GWPurchaseRequestGson gwPurchaseRequestGson = new GWPurchaseRequestGson();
        gwPurchaseRequestGson.setVersion("1.0");
        gwPurchaseRequestGson.setInstId(jijinInfoDTO.getInstId());
        gwPurchaseRequestGson.setIsIndividual("1");
        gwPurchaseRequestGson.setContractNo(jijinTradeRecordDTO.getContractNo());
        gwPurchaseRequestGson.setApplicationNo(jijinTradeRecordDTO.getAppNo());
        gwPurchaseRequestGson.setFundCode(jijinTradeRecordDTO.getFundCode());
        gwPurchaseRequestGson.setAmount(NumberUtils.stringFormat(jijinTradeRecordDTO.getReqAmount()));
        gwPurchaseRequestGson.setChargeType("A");
        gwPurchaseRequestGson.setIsAgreeRisk(jijinTradeRecordDTO.getIsAgreeRisk());

        UserInfoGson userInfoGson = userService.getUserInfo(jijinTradeRecordDTO.getUserId());
        gwPurchaseRequestGson.setInvestorName(userInfoGson.getName());
        gwPurchaseRequestGson.setCertType("0");
        gwPurchaseRequestGson.setCertificateNo(userInfoGson.getIdNo());
        PurchaseExtension extension = new PurchaseExtension();
        extension.setCdCard(jijinAccountDTO.getPayNo());
        gwPurchaseRequestGson.setExtension(extension);


        GWResponseGson gwResponseGson = new GWResponseGson();
        PurchaseOrderResultGson purchaseOrderResultGson = new PurchaseOrderResultGson();
        String remark = "";
        Logger.info(this, String.format("dahua do purchase call jijin gw start... tradeRecordId is [%s]", jijinTradeRecordDTO.getId()));
        if (jijinTradeRecordDTO.getType() == TradeRecordType.APPLY) {
            gwPurchaseRequestGson.setBusinessCode("020");
            gwResponseGson = jijinGatewayRemoteService.apply(jijinInfoDTO.getInstId(), JsonHelper.toJson(gwPurchaseRequestGson));
            remark = "认购：" + jijinInfoDTO.getFundName();
        } else {
            gwPurchaseRequestGson.setBusinessCode("022");
            gwResponseGson = jijinGatewayRemoteService.buy(jijinInfoDTO.getInstId(), JsonHelper.toJson(gwPurchaseRequestGson));
            remark = "申购：" + jijinInfoDTO.getFundName();
        }
        Logger.info(this, String.format("dahua do purchase call jijin gw end...tradeRecordId is [%s], response is  [%s]", jijinTradeRecordDTO.getId(), new Gson().toJson(gwResponseGson)));
        if (gwResponseGson.getRetCode().equals(GWResponseCode.SUCCESS)) {
            purchaseOrderResultGson = JsonHelper.fromJson(gwResponseGson.getContent(), PurchaseOrderResultGson.class);
        } else if (gwResponseGson.getRetCode().equals(GWResponseCode.LACK_CLIENT_CONFIG)
                || gwResponseGson.getRetCode().equals(GWResponseCode.SIGN_FAIL)
                || gwResponseGson.getRetCode().equals(GWResponseCode.VALIDATE_SIGN_FAIL)) {
            //返回提交申请失败
            purchaseOrderResultGson.setErrorCode("01");
        } else if (gwResponseGson.getRetCode().equals(GWResponseCode.RUNTIME_ERROR)
                || gwResponseGson.getRetCode().equals(GWResponseCode.JIJIN_RESPONSE_ERROR)) {
            if (jijinTradeRecordDTO.getStatus().equals(TradeRecordStatus.INIT.name()) && jijinTradeRecordDTO.getRetryTimes().compareTo(3L) < 0) {
                jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "retryTimes", jijinTradeRecordDTO.getRetryTimes() + 1));
               //retry
                mqService.sendCurrencyJijinBuyMQ(jijinTradeRecordDTO.getId());
                return;
            } else {
                purchaseOrderResultGson.setErrorCode("01");
            }
        }

        //大华T+0货基处理流程：如果返回成功,直接修改状态;如果返回0099需要重试3次,发送下单失败mq;如果返回其他错误码,发送下单失败mq
        if (purchaseOrderResultGson.getErrorCode().equals("0000")) {
            jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "errorCode", purchaseOrderResultGson.getErrorCode(), "trxDate", purchaseOrderResultGson.getTransactionDate(), "trxTime", purchaseOrderResultGson.getTransactionTime(), "appSheetNo", purchaseOrderResultGson.getAppSheetSerialNo(), "status", TradeRecordStatus.SUBMIT_SUCCESS.name()));
            jijinTradeRecordDTO.setAppSheetNo(purchaseOrderResultGson.getAppSheetSerialNo());
            jijinTradeRecordDTO.setTrxDate(purchaseOrderResultGson.getTransactionDate());   
            jijinTradeRecordDTO.setStatus(TradeRecordStatus.SUBMIT_SUCCESS.name());
        } else if (purchaseOrderResultGson.getErrorCode().equals("0099") && jijinTradeRecordDTO.getRetryTimes().compareTo(3L) < 0) {
                 jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "retryTimes", jijinTradeRecordDTO.getRetryTimes() + 1));
                //retry
                mqService.sendCurrencyJijinBuyMQ(jijinTradeRecordDTO.getId());
        } else {
        	
        	PaymentResult res = new PaymentResult();
        	
        	if (jijinTradeRecordDTO.getFrozenType().equals("NORMAL")) {
        	List<FrozenNo> fList = new ArrayList<FrozenNo>();
        	FrozenNo fNo = new FrozenNo(Long.valueOf(jijinTradeRecordDTO.getFrozenCode()),null, null,
        			String.valueOf(jijinTradeRecordDTO.getId()),"JIJIN_UNFREEZE",null,String.valueOf(jijinTradeRecordDTO.getId()),"JIJIN_UNFREEZE");
        	fList.add(fNo);
        	BatchTxUnfreezeRequest request = new BatchTxUnfreezeRequest("JIJIN",String.valueOf(jijinTradeRecordDTO.getId()),fList,TransactionType.UNFROZEN_FUND.name(),remark,
        			null,null,String.valueOf(jijinTradeRecordDTO.getId()),"JIJIN_BATCH_UNFREEZE",null,String.valueOf(jijinTradeRecordDTO.getId()),"JIJIN_BATCH_UNFREEZE");
        		res =paymentAppService.unfreeze(request);
        	}else{
                PaymentRequest request = new PaymentRequest("JIJIN", String.valueOf(jijinTradeRecordDTO.getId()),
                		jijinTradeRecordDTO.getFrozenCode(), null, TransactionType.UNFROZEN_FUND.name(),
                		remark, null, null,null,null, String.valueOf(jijinTradeRecordDTO.getId()),
            			"JIJIN_UNFREEZE", "基金认申购解冻", String.valueOf(jijinTradeRecordDTO.getId()),"JIJIN_UNFREEZE");
                res =paymentAppService.cancelPay(request);
        	}       	
        	
            if (!"SUCCESS".equals(res.getStatus())) {
                    Logger.error(this, String.format("NEED_MANUAL_HANDLE,dahua do purchase failed,but call payment do unfrozen failed ,tradeRecordId [%s]", jijinTradeRecordDTO.id()));
                    jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "errorCode", purchaseOrderResultGson.getErrorCode(), "isControversial", "1", "status", TradeRecordStatus.FAIL.name(), "errorMsg", "调用大华基金公司返回0099超过3次，解冻资金失败"));
            } else {
                    jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "errorCode", purchaseOrderResultGson.getErrorCode(), "isControversial", "0", "status", TradeRecordStatus.FAIL.name(), "errorMsg", "调用大华基金公司下单返回0099超过3次"));
            }

            jijinTradeLogRepository.insertBusJijinTradeLog(new JijinTradeLogDTO(jijinTradeRecordDTO.getUserId(), jijinTradeRecordDTO.getFundCode(), jijinTradeRecordDTO.getId(), TradeRecordStatus.FAIL.name(), jijinTradeRecordDTO.getType(), jijinTradeRecordDTO.getReqAmount(), purchaseOrderResultGson.getTransactionTime(), purchaseOrderResultGson.getTransactionDate()));
            
            if("LBO".equals(jijinTradeRecordDTO.getChannel())){
            	mqService.sendLBOTradeResultMsg(jijinTradeRecordDTO.getTrxId(), "fail","PURCHASE",purchaseOrderResultGson.getErrorCode());
            }else{
            	mqService.sendDhTradeResultMsg(jijinTradeRecordDTO.getTrxId(), "fail");
            }  
         }
    }
    
    
    /**
     * 通知PAF做划款指令，成功做解冻并且减少虚拟户金额,失败解冻虚拟户冻结金额
     *
     * @param jijinTradeRecordDTO
     */
    @Transactional
    public void doT0WithdrawByPAF( JijinTradeRecordDTO jijinTradeRecordDTO) {

        String subTransactionType = jijinTradeRecordDTO.getType() == TradeRecordType.APPLY ? TransactionType.EXPENSE_APPLY.name() : TransactionType.EXPENSE_PURCHASE.name();
        JijinUserBalanceDTO jijinUserBalanceDTO = jijinUserBalanceRepository.findUserBalanceByFundCode(jijinTradeRecordDTO.getUserId(), jijinTradeRecordDTO.getFundCode());
        JijinInfoDTO jijinInfoDTO = jijinInfoRepository.findJijinInfo(MapUtils.buildKeyValueMap("fundCode", jijinTradeRecordDTO.getFundCode()));

        if (jijinUserBalanceDTO == null) {
            jijinUserBalanceDTO = new JijinUserBalanceDTO();
            jijinUserBalanceDTO.setUserId(jijinTradeRecordDTO.getUserId());
            jijinUserBalanceDTO.setFundCode(jijinTradeRecordDTO.getFundCode());
            jijinUserBalanceDTO.setDividendType(jijinTradeRecordDTO.getDividendType());
            jijinUserBalanceDTO.setDividendStatus("DONE");
            jijinUserBalanceDTO.setShareBalance(new BigDecimal("0.00"));
            jijinUserBalanceDTO.setFrozenShare(new BigDecimal("0.00"));
            jijinUserBalanceDTO.setApplyingAmount(new BigDecimal("0.00"));
            jijinUserBalanceRepository.insertBusJijinUserBalance(jijinUserBalanceDTO);
        }

        String remark = "";
        if (jijinTradeRecordDTO.getType() == TradeRecordType.APPLY) {
            remark = "认购：" + jijinInfoDTO.getFundName();
        } else if (jijinTradeRecordDTO.getType() == TradeRecordType.PURCHASE) {
            remark = "申购：" + jijinInfoDTO.getFundName();
        }

        JijinAccountDTO jijinAccountDTO = jijinAccountRepository.findActiveAccount(jijinTradeRecordDTO.getUserId(), jijinInfoDTO.getInstId(), "PAF");

        PAFPayRequestGson pafPayRequestGson = new PAFPayRequestGson();
        if (JijinUtils.isHuoji(jijinInfoDTO)) {
            pafPayRequestGson.setBizType("000002");
        } else {
            pafPayRequestGson.setBizType("000001");
        }


        pafPayRequestGson.setTransType("001");
        if(null==jijinTradeRecordDTO.getAppSheetNo()){
        	 Logger.error(this, String.format("app sheet no is null,tradeRecordId is [%s], occur time [%s]", jijinTradeRecordDTO.getId(), new Date(System.currentTimeMillis())));
        }
        pafPayRequestGson.setMercOrderNo(jijinTradeRecordDTO.getAppSheetNo());
        pafPayRequestGson.setOrderAmount(jijinTradeRecordDTO.getReqAmount().multiply(new BigDecimal(100)).longValue());
        pafPayRequestGson.setOrderCurrency("CNY");
        pafPayRequestGson.setToken(jijinAccountDTO.getPayNo());
        pafPayRequestGson.setPlatMerchantId(jijinInstIdPlatMerchantIdMapHolder.getPlatMerchantId("lufax"));
        pafPayRequestGson.setMerchantId(jijinInstIdPlatMerchantIdMapHolder.getPlatMerchantId(jijinInfoDTO.getInstId()));

        PAFPayMerReservedRequestGson pafPayMerReservedRequestGson = new PAFPayMerReservedRequestGson();
        pafPayMerReservedRequestGson.setFundCode(jijinTradeRecordDTO.getFundCode());
        pafPayMerReservedRequestGson.setFundApplyType(jijinTradeRecordDTO.getType().equals(TradeRecordType.PURCHASE) ? "S" : "R");
        pafPayMerReservedRequestGson.setFundSaleCode(jijinInstIdPlatMerchantIdMapHolder.getFundSaleCode(jijinAccountDTO.getInstId()));
        pafPayRequestGson.setTransCode("0034");
        pafPayMerReservedRequestGson.setFundDate(jijinTradeRecordDTO.getTrxDate());
        pafPayMerReservedRequestGson.setFundType(FundType.getCodeByName(jijinInfoDTO.getInstId(), jijinInfoDTO.getFundType()));
        pafPayRequestGson.setMerReserved(JsonHelper.toJson(pafPayMerReservedRequestGson));

        Logger.info(this, String.format("dahua do withdraw from paf request is [%s],tradeRecordId is [%s]", new Gson().toJson(pafPayRequestGson), jijinTradeRecordDTO.getId()));
        GWResponseGson gwResponseGson = new GWResponseGson();

        try {
            gwResponseGson = pafGateWayRemoteService.pay(JsonHelper.toJson(pafPayRequestGson));
            Logger.info(this, String.format("dahua do withdraw from paf response is [%s],tradeRecordId is [%s]", new Gson().toJson(gwResponseGson),  jijinTradeRecordDTO.getId()));
        } catch (Exception e) {
            if (jijinTradeRecordDTO.getRetryTimes().compareTo(5L) < 0) {
                //小于5次的情况重试
                Logger.info(this, String.format("dahua call gwpaf-gw with exception,fail times less than 5,tradeRecordId is [%s]",  jijinTradeRecordDTO.getId()));
                jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id",  jijinTradeRecordDTO.getId(), "retryTimes", jijinTradeRecordDTO.getRetryTimes() + 1));
                //retry
                mqService.sendCurrencyJijinBuyMQ(jijinTradeRecordDTO.getId());
                return;
            }
            Logger.info(this, String.format("dahua do paf withdraw retry last time still runtime exception fail, [%s],tradeRecordId is [%s]", new Gson().toJson(gwResponseGson),  jijinTradeRecordDTO.getId()));
        }

        if (gwResponseGson.isSuccess()) {
            PAFPayResponseGson pafPayResponseGson = JsonHelper.fromJson(gwResponseGson.getContent(), PAFPayResponseGson.class);

            if (pafPayResponseGson.getRespCode().equals("0000") || pafPayResponseGson.getRespCode().equals("0044")) {
                // 使用清算接口做解冻调减
                String paymentOrderNo = "";
                String frozenCode = "";
                if (jijinTradeRecordDTO.getFrozenType().equals("NORMAL")) {//区分是普通的解冻减钱还是先投资后代扣的解冻减钱
                    frozenCode = jijinTradeRecordDTO.getFrozenCode();
                } else {
                    paymentOrderNo = jijinTradeRecordDTO.getFrozenCode();
                }

                PaymentInfo info = new PaymentInfo(paymentOrderNo, frozenCode, "CLEAR_SUBTRACT", TransactionType.UNFROZEN_FUND.name(), remark,
                        new SubTractInfo(jijinTradeRecordDTO.getAppSheetNo(), jijinTradeRecordDTO.getReqAmount(), subTransactionType, remark, jijinInfoDTO.getInstId(), jijinInfoDTO.getProductCode(), jijinTradeRecordDTO.getTrxDate()), null);
                PaymentRequestGson request = new PaymentRequestGson("JIJIN",String.valueOf(jijinTradeRecordDTO.getId()) , info);

                boolean result = callFundAuditPayment(request,jijinTradeRecordDTO);

                if (result) {
                    updateApplyingAmount(jijinTradeRecordDTO.getUserId(), jijinTradeRecordDTO.getFundCode(), jijinTradeRecordDTO.getReqAmount(), jijinTradeRecordDTO.getTrxDate(), jijinTradeRecordDTO.getId());
                    jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "errorCode", pafPayResponseGson.getRespCode(), "orderCompleteTime", pafPayResponseGson.getOrderCompleteTime(), "status", TradeRecordStatus.WITHDRAW_SUCCESS.name()));
                } else {
                    //此处报警需要给先投资后代扣的tradeRecord做资金解冻并且扣减虚拟户
                    Logger.error(this, String.format("NEED_MANUAL_CONFIRM,dahua do withdraw call fund do unfrozen failed,tradeRecordId [%s]", jijinTradeRecordDTO.getId()));
                }
                jijinTradeRecordDTO.setStatus(TradeRecordStatus.WITHDRAW_SUCCESS.name());
                jijinTradeRecordDTO.setTrxTime(pafPayResponseGson.getOrderCompleteTime());
            } else {
                //小于5次，重试
                if (jijinTradeRecordDTO.getRetryTimes().compareTo(5L) < 0 || ("0043".equals(pafPayResponseGson.getRespCode()) && jijinTradeRecordDTO.getRetryTimes().compareTo(10L) < 0)) {
                    Logger.info(this, String.format("dahua do withdraw from paf fail times less than 5,tradeRecordId is [%s]", jijinTradeRecordDTO.getId()));
                    if ("0043".equals(pafPayResponseGson.getRespCode()))
                        Logger.info(this, String.format("dahua do one more retry since paf response 0043handlepurc,tradeRecordId is [%s]", jijinTradeRecordDTO.getId()));
                    jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "retryTimes", jijinTradeRecordDTO.getRetryTimes() + 1));
                    // retry
                    mqService.sendCurrencyJijinBuyMQ(jijinTradeRecordDTO.getId());
                } else {
                    if (jijinTradeRecordDTO.getFrozenType().equals("NORMAL")) {
                    	List<FrozenNo> fList = new ArrayList<FrozenNo>();
                    	FrozenNo fNo = new FrozenNo(Long.valueOf(jijinTradeRecordDTO.getFrozenCode()),null, null,
                    			String.valueOf(jijinTradeRecordDTO.getId()),"JIJIN_UNFREEZE",null,String.valueOf(jijinTradeRecordDTO.getId()),"JIJIN_UNFREEZE");
                    	fList.add(fNo);
                    	BatchTxUnfreezeRequest request = new BatchTxUnfreezeRequest("JIJIN",String.valueOf(jijinTradeRecordDTO.getId()),fList,TransactionType.UNFROZEN_FUND.name(),remark,
                    			null,null,String.valueOf(jijinTradeRecordDTO.getId()),"JIJIN_BATCH_UNFREEZE",null,String.valueOf(jijinTradeRecordDTO.getId()),"JIJIN_BATCH_UNFREEZE");
                    	PaymentResult res =paymentAppService.unfreeze(request);
                    	
                        if ("SUCCESS".equals(res.getStatus())) {
                            jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "errorCode", pafPayResponseGson.getRespCode(), "isControversial", "0", "status", TradeRecordStatus.FAIL.name(), "errorMsg", "调用平安付重试超过5次,平安付返回付款失败，交易置为失败", "errorCode", pafPayResponseGson.getRespCode()));
                            jijinTradeLogRepository.insertBusJijinTradeLog(new JijinTradeLogDTO(jijinTradeRecordDTO.getUserId(), jijinInfoDTO.getFundCode(), jijinTradeRecordDTO.getId(), TradeRecordStatus.FAIL.name(), jijinTradeRecordDTO.getType(), jijinTradeRecordDTO.getReqAmount(), jijinTradeRecordDTO.getTrxTime(), jijinTradeRecordDTO.getTrxDate()));
                            if("LBO".equals(jijinTradeRecordDTO.getChannel())){
                            	mqService.sendLBOTradeResultMsg(jijinTradeRecordDTO.getTrxId(), "fail","PURCHASE",pafPayResponseGson.getRespCode());
                            }else{
                            	mqService.sendDhTradeResultMsg(jijinTradeRecordDTO.getTrxId(), "fail");
                            }                             
                        } else {
                            //此处报警要给正常投资的tradeRecord做解冻
                            Logger.error(this, String.format("NEED_MANUAL_CONFIRM,dahua do withdraw failed,call account do unfrozen failed,tradeRecordId [%s]", jijinTradeRecordDTO.getId()));
                            jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "errorCode", pafPayResponseGson.getRespCode(), "isControversial", "1", "status", TradeRecordStatus.FAIL.name(), "errorMsg", "重试超过5次,平安付返回付款失败，交易置为失败,但是虚拟户解冻失败，需人工解冻", "errorCode", pafPayResponseGson.getRespCode()));
                            jijinTradeLogRepository.insertBusJijinTradeLog(new JijinTradeLogDTO(jijinTradeRecordDTO.getUserId(), jijinInfoDTO.getFundCode(), jijinTradeRecordDTO.getId(), TradeRecordStatus.FAIL.name(), jijinTradeRecordDTO.getType(), jijinTradeRecordDTO.getReqAmount(), jijinTradeRecordDTO.getTrxTime(), jijinTradeRecordDTO.getTrxDate()));
                        }
                    } else {
                        PaymentRequest request = new PaymentRequest("JIJIN", String.valueOf(jijinTradeRecordDTO.getId()),
                        		jijinTradeRecordDTO.getFrozenCode(), null, TransactionType.UNFROZEN_FUND.name(),
                        		remark, null, null,null,null, String.valueOf(jijinTradeRecordDTO.getId()),
                    			"JIJIN_UNFREEZE", null, String.valueOf(jijinTradeRecordDTO.getId()),"JIJIN_UNFREEZE");
                        PaymentResult res =paymentAppService.cancelPay(request);
                        
                        if (!"SUCCESS".equals(res.getStatus())) {
                            //此处报警需要给先投资后代扣的tradeRecord做资金解冻
                            Logger.error(this, String.format("NEED_MANUAL_HANDLE,dahua do withdraw failed,but call fund do unfrozen failed,tradeRecordId [%s]", jijinTradeRecordDTO.id()));
                            jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "errorCode", pafPayResponseGson.getRespCode(), "status", TradeRecordStatus.FAIL.name(), "isControversial", "1", "errorMsg", "调用平安付重试超过5次,付款失败,交易置为失败,但是先投资后代扣解冻失败，需人工解冻", "errorCode", pafPayResponseGson.getRespCode()));
                            jijinTradeLogRepository.insertBusJijinTradeLog(new JijinTradeLogDTO(jijinTradeRecordDTO.getUserId(), jijinInfoDTO.getFundCode(), jijinTradeRecordDTO.getId(), TradeRecordStatus.FAIL.name(), jijinTradeRecordDTO.getType(), jijinTradeRecordDTO.getReqAmount(), jijinTradeRecordDTO.getTrxTime(), jijinTradeRecordDTO.getTrxDate()));
                        } else {
                            jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "errorCode", pafPayResponseGson.getRespCode(), "status", TradeRecordStatus.FAIL.name(), "isControversial", "0", "errorMsg", "调用平安付重试超过5次,平安付返回付款失败，交易置为失败", "errorCode", pafPayResponseGson.getRespCode()));
                            jijinTradeLogRepository.insertBusJijinTradeLog(new JijinTradeLogDTO(jijinTradeRecordDTO.getUserId(), jijinInfoDTO.getFundCode(), jijinTradeRecordDTO.getId(), TradeRecordStatus.FAIL.name(), jijinTradeRecordDTO.getType(), jijinTradeRecordDTO.getReqAmount(), jijinTradeRecordDTO.getTrxTime(), jijinTradeRecordDTO.getTrxDate()));
                            if("LBO".equals(jijinTradeRecordDTO.getChannel())){
                            	mqService.sendLBOTradeResultMsg(jijinTradeRecordDTO.getTrxId(), "fail","PURCHASE",pafPayResponseGson.getRespCode());
                            }else{
                            	mqService.sendDhTradeResultMsg(jijinTradeRecordDTO.getTrxId(), "fail");
                            }  
                        }
                    }
                }
            }
        } else {
            if (jijinTradeRecordDTO.getRetryTimes().compareTo(5L) < 0) {
                //小于5次的情况重试
                Logger.info(this, String.format("dahua do withdraw from paf fail times less than 5,tradeRecordId is [%s]", jijinTradeRecordDTO.getId()));
                jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "retryTimes", jijinTradeRecordDTO.getRetryTimes() + 1));
                //
                mqService.sendCurrencyJijinBuyMQ(jijinTradeRecordDTO.getId());
            } else {
                //无响应 webdev-5085 大于5次， 打标人工处理（根据最终扣款结果 人工调增或调减）
                Logger.info(this, String.format("dahua do withdraw from gwpaf-gw fail times large than 5, retCode [%s] tradeRecordId is [%s]", gwResponseGson.getRetCode(), jijinTradeRecordDTO.getId()));
                jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "isControversial", "1", "errorCode", gwResponseGson.getRetCode(), "errorMsg", "调用平安付重试超过5次无响应内容,打标人工处理-根据当日最终扣款结果,人工解冻调减或解冻"));

            }
        }
    }
    
    
    /**
     * 通知基金公司接收扣款结果
     *
     * @param jijinTradeRecordDTO
     */
    public void buyApplyT0Notify(JijinTradeRecordDTO jijinTradeRecordDTO) {

        String notifyAppNo = sequenceService.getSerialNumber(JijinBizType.BUY_NOTIFY.getCode());
        JijinInfoDTO jijinInfoDTO = jijinInfoRepository.findJijinInfo(MapUtils.buildKeyValueMap("fundCode", jijinTradeRecordDTO.getFundCode()));
        JijinAccountDTO accountDTO = jijinAccountRepository.findActiveAccount(jijinTradeRecordDTO.getUserId(), jijinTradeRecordDTO.getInstId(), "PAF");
        GWBuyApplyNotifyRequestGson gwBuyApplyNotifyRequestGson = new GWBuyApplyNotifyRequestGson();

        gwBuyApplyNotifyRequestGson.setVersion("1.0");
        gwBuyApplyNotifyRequestGson.setInstId(jijinInfoDTO.getInstId());
        gwBuyApplyNotifyRequestGson.setIsIndividual("1");
        gwBuyApplyNotifyRequestGson.setContractNo(jijinTradeRecordDTO.getContractNo());
        gwBuyApplyNotifyRequestGson.setApplicationNo(notifyAppNo);
        gwBuyApplyNotifyRequestGson.setOriginalApplicationNo(jijinTradeRecordDTO.getAppNo());
        gwBuyApplyNotifyRequestGson.setAppSheetSerialNo(jijinTradeRecordDTO.getAppSheetNo());
        gwBuyApplyNotifyRequestGson.setPayState("1");
        gwBuyApplyNotifyRequestGson.setExtension(new BuyNotifyExtension(jijinTradeRecordDTO.getFundCode(), jijinTradeRecordDTO.getReqAmount(), accountDTO.getPayNo()));
        gwBuyApplyNotifyRequestGson.setPaidTime(jijinTradeRecordDTO.getTrxTime());

        GWResponseGson gwResponseGson = jijinGatewayRemoteService.buyNotify(jijinInfoDTO.getInstId(), JsonHelper.toJson(gwBuyApplyNotifyRequestGson));

        Logger.info(this, String.format("dahua buyApplyNotify from jijin-gw result is [%s]", JsonHelper.toJson(gwResponseGson)));

        GWBuyApplyNotifyResponseGson gwBuyApplyNotifyResponseGson = new GWBuyApplyNotifyResponseGson();

        if (gwResponseGson.getRetCode().equals(GWResponseCode.SUCCESS)) {
            gwBuyApplyNotifyResponseGson = JsonHelper.fromJson(gwResponseGson.getContent(), GWBuyApplyNotifyResponseGson.class);
        } else if (gwResponseGson.getRetCode().equals(GWResponseCode.LACK_CLIENT_CONFIG)
                || gwResponseGson.getRetCode().equals(GWResponseCode.JIJIN_RESPONSE_ERROR)
                || gwResponseGson.getRetCode().equals(GWResponseCode.SIGN_FAIL)
                || gwResponseGson.getRetCode().equals(GWResponseCode.VALIDATE_SIGN_FAIL)) {
            //返回支付确认失败

            String errorMessage = gwResponseGson.getRetMessage();
            if (!com.site.lookup.util.StringUtils.isEmpty(errorMessage) && errorMessage.length() >= 200) {
                errorMessage = errorMessage.substring(0, 200);
            }

            jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "errorCode", gwBuyApplyNotifyResponseGson.getErrorCode(), "status", TradeRecordStatus.NOTIFY_FAIL.name(), "errorMsg", errorMessage));
        } else if (gwResponseGson.getRetCode().equals(GWResponseCode.RUNTIME_ERROR)) {
            if (jijinTradeRecordDTO.getStatus().equals(TradeRecordStatus.WITHDRAW_SUCCESS.name()) && jijinTradeRecordDTO.getRetryTimes().compareTo(5L) < 0) {
                jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "notifyAppNo", notifyAppNo, "retryTimes", jijinTradeRecordDTO.getRetryTimes() + 1));
                //retry
                mqService.sendCurrencyJijinBuyMQ(jijinTradeRecordDTO.getId());
                return;
            } else {
                String errorMessage = gwResponseGson.getRetMessage();
                if (!com.site.lookup.util.StringUtils.isEmpty(errorMessage) && errorMessage.length() >= 200) {
                    errorMessage = errorMessage.substring(0, 200);
                }
                jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "notifyAppNo", notifyAppNo, "errorCode", "19999", "status", TradeRecordStatus.NOTIFY_FAIL.name(), "errorMsg", errorMessage));
                return;
            }
        }

        if ("0000".equals(gwBuyApplyNotifyResponseGson.getErrorCode())) {
            jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "errorCode", gwBuyApplyNotifyResponseGson.getErrorCode(), "notifyAppNo", notifyAppNo, "status", TradeRecordStatus.NOTIFY_SUCCESS.name()));
            updateShareBalance(jijinTradeRecordDTO.getUserId(), jijinTradeRecordDTO.getFundCode(), jijinTradeRecordDTO.getReqAmount(), jijinTradeRecordDTO.getTrxDate(), jijinTradeRecordDTO.getId());           
            if("LBO".equals(jijinTradeRecordDTO.getChannel())){
            	mqService.sendLBOTradeResultMsg(jijinTradeRecordDTO.getTrxId(), "success","PURCHASE","000");
            }else{
            	mqService.sendDhTradeResultMsg(jijinTradeRecordDTO.getTrxId(), "success");
            }  
            jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "status", TradeRecordStatus.SUCCESS.name()));
            JijinTradeLogDTO jijinTradeLogDTO = new JijinTradeLogDTO(jijinTradeRecordDTO.getUserId(), jijinTradeRecordDTO.getFundCode(), jijinTradeRecordDTO.getId(), TradeRecordStatus.SUCCESS.name(), jijinTradeRecordDTO.getType(), jijinTradeRecordDTO.getReqAmount(), jijinTradeRecordDTO.getReqAmount(), jijinTradeRecordDTO.getTrxTime(), jijinTradeRecordDTO.getTrxDate());
            jijinTradeLogDTO.setFee(new BigDecimal(0));
            jijinTradeLogRepository.insertBusJijinTradeLog(jijinTradeLogDTO);
        } else {
            String errorMessage = gwBuyApplyNotifyResponseGson.getErrorMessage();
            if (!StringUtils.isEmpty(errorMessage) && errorMessage.length() >= 200) {
                errorMessage = errorMessage.substring(0, 200);
            }
            jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "errorCode", gwBuyApplyNotifyResponseGson.getErrorCode(), "notifyAppNo", notifyAppNo, "status", TradeRecordStatus.NOTIFY_FAIL.name(), "errorMsg", errorMessage));
        }
    }
    
    
    /**
     * 带清算的解冻调减
     *
     * @param paymentRequestGson
     */
    private boolean callFundAuditPayment(PaymentRequestGson paymentRequestGson,JijinTradeRecordDTO jijinTradeRecordDTO) {
    	boolean result = false;
    	PaymentInfo info = paymentRequestGson.getPaymentInfo();
    	SubTractInfo sub =paymentRequestGson.getPaymentInfo().getSubtractInfo();
    	
    	PaymentRequest request = new PaymentRequest(paymentRequestGson.getChannelId(),paymentRequestGson.getInstructionNo(),
    			info.getPaymentOrderNo(),info.getFrozenCode(),
    			info.getUnfreezeTransactionType(),info.getUnfreezeRemark(),
    			new SubtractInfo(sub.getMinusAmount(),sub.getMinusTransactionType(),sub.getBusinessRefNo(),sub.getMinusRemark()),
    			new ClearInfo(sub.getBusinessRefNo(),sub.getVendorCode(),sub.getTrxDate()),
    			sub.getProductCode(),DateUtils.formatDate(jijinTradeRecordDTO.getCreatedAt(), DateUtils.CMS_DRAW_SEQUENCE_FORMAT),
    			sub.getBusinessRefNo(),"JIJIN_BUY_DECREASE",null,paymentRequestGson.getInstructionNo(),"JIJIN_BUY_DECREASE");

    	PaymentResult res=  paymentAppService.decreaseMoneyWithAudit(request);
        if ("SUCCESS".equals(res.getStatus())) {
            result = true;
        } else{
            result = false;
        }

        return result;
    }
    
    /**
     * 更新申购在途金额
     *
     * @param userId
     * @param fundCode
     * @param reqAmount
     */
    @Transactional
    public void updateApplyingAmount(Long userId, String fundCode, BigDecimal reqAmount, String bizDate, Long tradeRecordId) {
        int result = 0;
        int retry_times = 0;
        JijinUserBalanceDTO userBalanceDTO = new JijinUserBalanceDTO();
        while (result == 0) {
            userBalanceDTO = jijinUserBalanceRepository.findUserBalanceByFundCode(userId, fundCode);
            userBalanceDTO.setApplyingAmount(userBalanceDTO.getApplyingAmount().add(reqAmount));
            result = jijinUserBalanceRepository.updateFundAccount(userBalanceDTO);
            retry_times = retry_times + 1;
            if (retry_times > 10) break;
        }
        // insert user balance history
        if (result == 1) {
            JijinUserBalanceHistoryDTO jijinUserBalanceHistoryDTO = JijinUserBalenceHistoryDTOBuilder.buildUserBalanceHistoryDTO(userBalanceDTO, bizDate, new BigDecimal("0"), new BigDecimal("0"), "转入申购在途资金", BalanceHistoryBizType.申购转入在途成功, tradeRecordId);
            jijinUserBalanceHistoryRepository.insertBusJijinUserBalanceHistory(jijinUserBalanceHistoryDTO);
        }
    }
    
    /**
     * 更新申购份额
     *
     * @param userId
     * @param fundCode
     * @param reqAmount
     */
    @Transactional
    public void updateShareBalance(Long userId, String fundCode, BigDecimal reqAmount, String bizDate, Long tradeRecordId) {
        int result = 0;
        int retry_times = 0;
        JijinUserBalanceDTO userBalanceDTO = new JijinUserBalanceDTO();
        while (result == 0) {
            userBalanceDTO = jijinUserBalanceRepository.findUserBalanceByFundCode(userId, fundCode);
            userBalanceDTO.setShareBalance(userBalanceDTO.getShareBalance().add(reqAmount));
            userBalanceDTO.setApplyingAmount(userBalanceDTO.getApplyingAmount().subtract(reqAmount));
            result = jijinUserBalanceRepository.updateFundAccount(userBalanceDTO);
            retry_times = retry_times + 1;
            if (retry_times > 10) break;
        }
        // insert user balance history
        if (result == 1) {
            JijinUserBalanceHistoryDTO jijinUserBalanceHistoryDTO = JijinUserBalenceHistoryDTOBuilder.buildUserBalanceHistoryDTO(userBalanceDTO, bizDate, reqAmount, new BigDecimal("0"), "转入申购成功份额", BalanceHistoryBizType.申购下单成功, tradeRecordId);
            jijinUserBalanceHistoryRepository.insertBusJijinUserBalanceHistory(jijinUserBalanceHistoryDTO);
        }
    }
    
}
