package com.lufax.jijin.fundation.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.lufax.jijin.fundation.constant.*;
import com.lufax.jijin.fundation.service.domain.JiJinDateUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.lufax.jijin.base.service.TradeDayService;
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
import com.lufax.jijin.fundation.gson.PurchaseOrderResultGson;
import com.lufax.jijin.fundation.remote.AccountAppCallerService;
import com.lufax.jijin.fundation.remote.FundAppCallerService;
import com.lufax.jijin.fundation.remote.JijinGatewayRemoteService;
import com.lufax.jijin.fundation.remote.LjbGateWayRemoteService;
import com.lufax.jijin.fundation.remote.gson.paf.request.PAFPayMerReservedRequestGson;
import com.lufax.jijin.fundation.remote.gson.paf.request.PAFPayRequestGson;
import com.lufax.jijin.fundation.remote.gson.paf.response.PAFPayResponseGson;
import com.lufax.jijin.fundation.remote.gson.request.GWBuyApplyNotifyRequestGson;
import com.lufax.jijin.fundation.remote.gson.request.GWPurchaseRequestGson;
import com.lufax.jijin.fundation.remote.gson.request.PaymentInfo;
import com.lufax.jijin.fundation.remote.gson.request.PaymentRequestGson;
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
import com.lufax.jijin.user.service.UserService;

import org.apache.commons.lang.StringUtils;
/**
 * T1 货基认申购
 * （下单，付款，通知 ）
 * @author XUNENG311
 *
 */
@Service
public class JijinyT1SyncService {
	
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
    private TradeDayService tradeDayService;
    @Autowired
    private PaymentAppService paymentAppService;
	@Autowired
    //中港互认基金列表，以后当wind有标志数据传过来后，可以用wind数据
    private static final List<String> CHINA_KONG_LIST = Arrays.asList("968006");
	@Autowired
    private JiJinDateUtil jiJinDateUtil;

	 /**
	  * 调用jijin-gw做申购请求(T1 基金)
	  * @param jijinTradeRecordDTO
	  */
    public void callT1JijinPurchase(JijinTradeRecordDTO jijinTradeRecordDTO) {
        if (!jijinTradeRecordDTO.getStatus().equals(TradeRecordStatus.INIT.name())) {
            Logger.info(this, String.format("tradeRecord is not init status,do nothing,tradeRecordId [%s]", jijinTradeRecordDTO.getId()));
            return;
        }

        JijinInfoDTO jijinInfoDTO = jijinInfoRepository.findJijinInfo(MapUtils.buildKeyValueMap("fundCode", jijinTradeRecordDTO.getFundCode()));
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

        GWResponseGson gwResponseGson = new GWResponseGson();
        PurchaseOrderResultGson purchaseOrderResultGson = new PurchaseOrderResultGson();
        String remark = "";

        if (jijinTradeRecordDTO.getType() == TradeRecordType.APPLY) {
            gwPurchaseRequestGson.setBusinessCode("020");
            gwResponseGson = jijinGatewayRemoteService.apply(jijinInfoDTO.getInstId(), JsonHelper.toJson(gwPurchaseRequestGson));
            remark = "认购：" + jijinInfoDTO.getFundName();
        } else {
            gwPurchaseRequestGson.setBusinessCode("022");
            gwResponseGson = jijinGatewayRemoteService.buy(jijinInfoDTO.getInstId(), JsonHelper.toJson(gwPurchaseRequestGson));
            remark = "申购：" + jijinInfoDTO.getFundName();
        }

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
                mqService.sendJijinBuyMQ(jijinTradeRecordDTO.getId());
                return;
            } else {
                purchaseOrderResultGson.setErrorCode("01");
            }
        }

        //成功
        if (purchaseOrderResultGson.getErrorCode().equals("0000")) {
            jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id",  jijinTradeRecordDTO.getId(), "errorCode", purchaseOrderResultGson.getErrorCode(), "trxTime", purchaseOrderResultGson.getTransactionTime(), "trxDate", purchaseOrderResultGson.getTransactionDate(), "appSheetNo", purchaseOrderResultGson.getAppSheetSerialNo(), "status", TradeRecordStatus.SUBMIT_SUCCESS.name()));
            jijinTradeRecordDTO.setAppSheetNo(purchaseOrderResultGson.getAppSheetSerialNo());
            jijinTradeRecordDTO.setTrxDate(purchaseOrderResultGson.getTransactionDate());
            jijinTradeRecordDTO.setStatus(TradeRecordStatus.SUBMIT_SUCCESS.name());
        } else if (purchaseOrderResultGson.getErrorCode().equals("EEEE")) {
            //风险等级不匹配，，先更改风险等级，然后重新请求
            jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id",  jijinTradeRecordDTO.getId(), "isAgreeRisk", "1"));
            //retry
            mqService.sendJijinBuyMQ( jijinTradeRecordDTO.getId());
            return;
        } else {
            if (jijinTradeRecordDTO.getFrozenType().equals("NORMAL")) {
            	List<FrozenNo> fList = new ArrayList<FrozenNo>();
            	FrozenNo fNo = new FrozenNo(Long.valueOf(jijinTradeRecordDTO.getFrozenCode()),null, null,
            			String.valueOf(jijinTradeRecordDTO.getId()),"JIJIN_UNFREEZE",null,String.valueOf(jijinTradeRecordDTO.getId()),"JIJIN_UNFREEZE");
            	fList.add(fNo);
            	BatchTxUnfreezeRequest request = new BatchTxUnfreezeRequest("JIJIN",String.valueOf(jijinTradeRecordDTO.getId()),fList,TransactionType.UNFROZEN_FUND.name(),remark,
            			null,null,String.valueOf(jijinTradeRecordDTO.getId()),"JIJIN_BATCH_UNFREEZE",null,String.valueOf(jijinTradeRecordDTO.getId()),"JIJIN_BATCH_UNFREEZE");
            	PaymentResult res =paymentAppService.unfreeze(request);
            	
                if (!"SUCCESS".equals(res.getStatus())) {
                    Logger.error(this, String.format("NEED_MANUAL_HANDLE,do purchase failed,but call account do unfrozen failed ,tradeRecordId [%s]", jijinTradeRecordDTO.id()));
                }
            } else {
                PaymentRequest request = new PaymentRequest("JIJIN", String.valueOf(jijinTradeRecordDTO.getId()),
                		jijinTradeRecordDTO.getFrozenCode(), null, TransactionType.UNFROZEN_FUND.name(),
                		remark, null, null,null,null, String.valueOf(jijinTradeRecordDTO.getId()),
            			"JIJIN_UNFREEZE", null, String.valueOf(jijinTradeRecordDTO.getId()),"JIJIN_UNFREEZE");
                PaymentResult res =paymentAppService.cancelPay(request);
                
                if (!"SUCCESS".equals(res.getStatus())) {
                    Logger.error(this, String.format("NEED_MANUAL_HANDLE,do purchase failed,but call fund do unfrozen failed , tradeRecordId [%s]", jijinTradeRecordDTO.id()));
                }
            }
            String retMessage = purchaseOrderResultGson.getRetMessage();
            if (!StringUtils.isEmpty(retMessage) && retMessage.length() > 500) {
                retMessage = retMessage.substring(0, 500);
            }
            jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "errorCode", purchaseOrderResultGson.getErrorCode(), "status", TradeRecordStatus.FAIL.name(), "errorMsg", retMessage));
            JijinTradeLogDTO jijinTradeLogDTO =  new JijinTradeLogDTO(jijinTradeRecordDTO.getUserId(), jijinTradeRecordDTO.getFundCode(), jijinTradeRecordDTO.getId(), TradeRecordStatus.FAIL.name(), jijinTradeRecordDTO.getType(), jijinTradeRecordDTO.getReqAmount(), purchaseOrderResultGson.getTransactionTime(), purchaseOrderResultGson.getTransactionDate());
            jijinTradeLogDTO.setBusinessMode(jijinTradeRecordDTO.getBusinessMode());
            jijinTradeLogRepository.insertBusJijinTradeLog(jijinTradeLogDTO);
            mqService.sendPurchaseResultMsg(jijinTradeRecordDTO.getTrxId(), "fail", BigDecimal.ZERO);
        }
    }
    
    
    /**
     * 通知PAF做划款指令，成功做解冻并且减少虚拟户金额,失败解冻虚拟户冻结金额
     *
     * @param jijinTradeRecordDTO
     */
    @Transactional
    public void doT1WithdrawByPAF( JijinTradeRecordDTO jijinTradeRecordDTO) {

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
        String expectConfirmDate ="";
        if (jijinTradeRecordDTO.getType() == TradeRecordType.APPLY) {
            remark = "认购：" + jijinInfoDTO.getFundName();
            expectConfirmDate = "新发基金待确认";
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
        //中港互认
        if(CHINA_KONG_LIST.contains(jijinTradeRecordDTO.getFundCode())){
        	pafPayMerReservedRequestGson.setFundAttribution("HK");
        }else{
        	 pafPayMerReservedRequestGson.setFundAttribution("CHN");
        }
        pafPayRequestGson.setTransCode("0029");

        pafPayMerReservedRequestGson.setFundType(FundType.getCodeByName(jijinInfoDTO.getInstId(), jijinInfoDTO.getFundType()));
        pafPayRequestGson.setMerReserved(JsonHelper.toJson(pafPayMerReservedRequestGson));

        Logger.info(this, String.format("do withdraw from paf request is [%s],tradeRecordId is [%s]", new Gson().toJson(pafPayRequestGson), jijinTradeRecordDTO.getId()));
        GWResponseGson gwResponseGson = new GWResponseGson();

        try {
            gwResponseGson = pafGateWayRemoteService.pay(JsonHelper.toJson(pafPayRequestGson));
            Logger.info(this, String.format("do withdraw from paf response is [%s],tradeRecordId is [%s]", new Gson().toJson(gwResponseGson), jijinTradeRecordDTO.getId()));
        } catch (Exception e) {
            if (jijinTradeRecordDTO.getRetryTimes().compareTo(5L) < 0) {
                //小于5次的情况重试
                Logger.info(this, String.format("call gwpaf-gw do withdraw fail,fail times less than 5,tradeRecordId is [%s]", jijinTradeRecordDTO.getId()));
                jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "retryTimes", jijinTradeRecordDTO.getRetryTimes() + 1));
                mqService.sendJijinBuyMQ(jijinTradeRecordDTO.getId());
                return;
            }
            Logger.info(this, String.format("do paf withdraw retry last time still runtime exception fail, [%s],tradeRecordId is [%s]", new Gson().toJson(gwResponseGson), jijinTradeRecordDTO.getId()));
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

                // format 20150828164143
                Date date = DateUtils.parseDate(pafPayResponseGson.getOrderCompleteTime(), DateUtils.CMS_DRAW_SEQUENCE_FORMAT);
                String targetTradeDay = DateUtils.formatDate(tradeDayService.getTargetTradeDay(date), DateUtils.DATE_STRING_FORMAT);

                PaymentInfo info = new PaymentInfo(paymentOrderNo, frozenCode, "CLEAR_SUBTRACT", TransactionType.UNFROZEN_FUND.name(), remark,
                        new SubTractInfo(jijinTradeRecordDTO.getAppSheetNo(), jijinTradeRecordDTO.getReqAmount(), subTransactionType, remark, jijinInfoDTO.getInstId(), jijinInfoDTO.getProductCode(), targetTradeDay), null);
                PaymentRequestGson request = new PaymentRequestGson("JIJIN", String.valueOf(jijinTradeRecordDTO.getId()), info);

                boolean result = callFundAuditPayment(request,jijinTradeRecordDTO);

                if (result) {
                    updateApplyingAmount(jijinTradeRecordDTO.getUserId(), jijinTradeRecordDTO.getFundCode(), jijinTradeRecordDTO.getReqAmount(), jijinTradeRecordDTO.getTrxDate(), jijinTradeRecordDTO.getId());
                    //WEBDEV-8355:基金(PC)赎回到账时间显示优化
                    //申购或认购返回预确认时，需要保持预计确认日期
                    if(StringUtils.isBlank(expectConfirmDate)){
                        expectConfirmDate = jiJinDateUtil.getConfirmDate(TradeRecordType.PURCHASE.name(),pafPayResponseGson.getOrderCompleteTime(),
                                jijinInfoDTO.getFundType());
                    }
                    jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap(
                            "id", jijinTradeRecordDTO.getId(),
                            "trxDate", targetTradeDay,
                            "errorCode", pafPayResponseGson.getRespCode(),
                            "orderCompleteTime", pafPayResponseGson.getOrderCompleteTime(),
                            "status", TradeRecordStatus.WITHDRAW_SUCCESS.name(),
                            "expectConfirmDate",expectConfirmDate));
                    jijinTradeRecordDTO.setStatus(TradeRecordStatus.WITHDRAW_SUCCESS.name());
                } else {
                    //此处报警需要给先投资后代扣的tradeRecord做资金解冻并且扣减虚拟户
                    Logger.error(this, String.format("NEED_MANUAL_CONFIRM,do withdraw success ,call fund do unfrozen failed,tradeRecordId [%s]", jijinTradeRecordDTO.getId()));
                }

            } else {
                //小于5次，重试
                if (jijinTradeRecordDTO.getRetryTimes().compareTo(5L) < 0 || ("0043".equals(pafPayResponseGson.getRespCode()) && jijinTradeRecordDTO.getRetryTimes().compareTo(10L) < 0)) {
                    Logger.info(this, String.format("do withdraw from paf fail times less than 5,tradeRecordId is [%s]", jijinTradeRecordDTO.getId()));
                    if ("0043".equals(pafPayResponseGson.getRespCode()))
                        Logger.info(this, String.format("do one more retry since paf response 0043,tradeRecordId is [%s]", jijinTradeRecordDTO.getId()));
                    jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "retryTimes", jijinTradeRecordDTO.getRetryTimes() + 1));
                    mqService.sendJijinBuyMQ(jijinTradeRecordDTO.getId());
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
                            jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "errorCode", pafPayResponseGson.getRespCode(), "status", TradeRecordStatus.FAIL.name(), "errorMsg", "调用平安付重试超过5次,平安付返回付款失败，交易置为失败", "errorCode", pafPayResponseGson.getRespCode()));
                            JijinTradeLogDTO jijinTradeLogDTO =   new JijinTradeLogDTO(jijinTradeRecordDTO.getUserId(), jijinInfoDTO.getFundCode(), jijinTradeRecordDTO.getId(), TradeRecordStatus.FAIL.name(), jijinTradeRecordDTO.getType(), jijinTradeRecordDTO.getReqAmount(), jijinTradeRecordDTO.getTrxTime(), jijinTradeRecordDTO.getTrxDate());
                            jijinTradeLogDTO.setBusinessMode(jijinTradeRecordDTO.getBusinessMode());
                            jijinTradeLogRepository.insertBusJijinTradeLog(jijinTradeLogDTO);
                            mqService.sendPurchaseResultMsg(jijinTradeRecordDTO.getTrxId(), "fail", BigDecimal.ZERO);
                        } else {
                            //此处报警要给正常投资的tradeRecord做解冻
                            Logger.error(this, String.format("NEED_MANUAL_CONFIRM,do withdraw failed,call account do unfrozen failed,tradeRecordId [%s]", jijinTradeRecordDTO.getId()));
                            jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "errorCode", pafPayResponseGson.getRespCode(), "status", TradeRecordStatus.FAIL.name(), "isControversial", "1", "errorMsg", "重试超过5次,平安付返回付款失败，交易置为失败,但是虚拟户解冻失败，需人工解冻", "errorCode", pafPayResponseGson.getRespCode()));
                            JijinTradeLogDTO jijinTradeLogDTO =   new JijinTradeLogDTO(jijinTradeRecordDTO.getUserId(), jijinInfoDTO.getFundCode(), jijinTradeRecordDTO.getId(), TradeRecordStatus.FAIL.name(), jijinTradeRecordDTO.getType(), jijinTradeRecordDTO.getReqAmount(), jijinTradeRecordDTO.getTrxTime(), jijinTradeRecordDTO.getTrxDate());
                            jijinTradeLogDTO.setBusinessMode(jijinTradeRecordDTO.getBusinessMode());
                            jijinTradeLogRepository.insertBusJijinTradeLog(jijinTradeLogDTO);
                        }
                    } else {
                        PaymentRequest request = new PaymentRequest("JIJIN", String.valueOf(jijinTradeRecordDTO.getId()),
                        		jijinTradeRecordDTO.getFrozenCode(), null, TransactionType.UNFROZEN_FUND.name(),
                        		remark, null, null,null,null, String.valueOf(jijinTradeRecordDTO.getId()),
                    			"JIJIN_UNFREEZE", null, String.valueOf(jijinTradeRecordDTO.getId()),"JIJIN_UNFREEZE");
                        PaymentResult res =paymentAppService.cancelPay(request);
                        
                        if (!"SUCCESS".equals(res.getStatus())) {
                            //此处报警需要给先投资后代扣的tradeRecord做资金解冻
                            Logger.error(this, String.format("NEED_MANUAL_HANDLE,do withdraw failed,but call fund do unfrozen failed,tradeRecordId [%s]", jijinTradeRecordDTO.id()));
                            jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "errorCode", pafPayResponseGson.getRespCode(), "status", TradeRecordStatus.FAIL.name(), "isControversial", "1", "errorMsg", "调用平安付重试超过5次,付款失败,交易置为失败,但是先投资后代扣解冻失败，需人工解冻", "errorCode", pafPayResponseGson.getRespCode()));
                            JijinTradeLogDTO jijinTradeLogDTO =   new JijinTradeLogDTO(jijinTradeRecordDTO.getUserId(), jijinInfoDTO.getFundCode(), jijinTradeRecordDTO.getId(), TradeRecordStatus.FAIL.name(), jijinTradeRecordDTO.getType(), jijinTradeRecordDTO.getReqAmount(), jijinTradeRecordDTO.getTrxTime(), jijinTradeRecordDTO.getTrxDate());
                            jijinTradeLogDTO.setBusinessMode(jijinTradeRecordDTO.getBusinessMode());
                            jijinTradeLogRepository.insertBusJijinTradeLog(jijinTradeLogDTO);
                        } else {
                            jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "errorCode", pafPayResponseGson.getRespCode(), "status", TradeRecordStatus.FAIL.name(), "errorMsg", "调用平安付重试超过5次,平安付返回付款失败，交易置为失败", "errorCode", pafPayResponseGson.getRespCode()));
                            JijinTradeLogDTO jijinTradeLogDTO = new JijinTradeLogDTO(jijinTradeRecordDTO.getUserId(), jijinInfoDTO.getFundCode(), jijinTradeRecordDTO.getId(), TradeRecordStatus.FAIL.name(), jijinTradeRecordDTO.getType(), jijinTradeRecordDTO.getReqAmount(), jijinTradeRecordDTO.getTrxTime(), jijinTradeRecordDTO.getTrxDate());
                            jijinTradeLogDTO.setBusinessMode(jijinTradeRecordDTO.getBusinessMode());
                            jijinTradeLogRepository.insertBusJijinTradeLog(jijinTradeLogDTO);
                            mqService.sendPurchaseResultMsg(jijinTradeRecordDTO.getTrxId(), "fail", BigDecimal.ZERO);
                        }
                    }
                }
            }
        } else {
            if (jijinTradeRecordDTO.getRetryTimes().compareTo(5L) < 0) {
                //小于5次的情况重试
                Logger.info(this, String.format("do withdraw from paf fail times less than 5,tradeRecordId is [%s]", jijinTradeRecordDTO.getId()));
                jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "retryTimes", jijinTradeRecordDTO.getRetryTimes() + 1));
                //retry
                mqService.sendJijinBuyMQ(jijinTradeRecordDTO.getId());
            } else {
                //无响应 webdev-5085 大于5次， 打标人工处理（根据最终扣款结果 人工调增或调减）
                Logger.info(this, String.format("do withdraw from gwpaf-gw fail times large than 5, retCode [%s] tradeRecordId is [%s]", gwResponseGson.getRetCode(), jijinTradeRecordDTO.getId()));
                jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "isControversial", "1", "errorCode", gwResponseGson.getRetCode(), "errorMsg", "调用平安付重试超过5次无响应内容,打标人工处理-根据当日最终扣款结果,人工解冻调减或解冻"));
            }
        }
    }
    
    
    /**
     * 通知基金公司接收扣款结果
     *
     * @param jijinTradeRecordDTO
     */
    public void buyApplyT1Notify(JijinTradeRecordDTO jijinTradeRecordDTO) {

         String notifyAppNo = sequenceService.getSerialNumber(JijinBizType.BUY_NOTIFY.getCode());
         JijinInfoDTO jijinInfoDTO = jijinInfoRepository.findJijinInfo(MapUtils.buildKeyValueMap("fundCode", jijinTradeRecordDTO.getFundCode()));

         GWBuyApplyNotifyRequestGson gwBuyApplyNotifyRequestGson = new GWBuyApplyNotifyRequestGson();

         gwBuyApplyNotifyRequestGson.setVersion("1.0");
         gwBuyApplyNotifyRequestGson.setInstId(jijinInfoDTO.getInstId());
         gwBuyApplyNotifyRequestGson.setIsIndividual("1");
         gwBuyApplyNotifyRequestGson.setContractNo(jijinTradeRecordDTO.getContractNo());
         gwBuyApplyNotifyRequestGson.setApplicationNo(notifyAppNo);
         gwBuyApplyNotifyRequestGson.setOriginalApplicationNo(jijinTradeRecordDTO.getAppNo());
         gwBuyApplyNotifyRequestGson.setAppSheetSerialNo(jijinTradeRecordDTO.getAppSheetNo());
         gwBuyApplyNotifyRequestGson.setPayState("1");
         gwBuyApplyNotifyRequestGson.setPaidTime(jijinTradeRecordDTO.getOrderCompleteTime());

         GWResponseGson gwResponseGson = jijinGatewayRemoteService.buyNotify(jijinInfoDTO.getInstId(), JsonHelper.toJson(gwBuyApplyNotifyRequestGson));

         Logger.info(this, String.format("buyApplyNotify from jijin-gw result is [%s]", JsonHelper.toJson(gwResponseGson)));

         GWBuyApplyNotifyResponseGson gwBuyApplyNotifyResponseGson = new GWBuyApplyNotifyResponseGson();

         if (gwResponseGson.getRetCode().equals(GWResponseCode.SUCCESS)) {
             gwBuyApplyNotifyResponseGson = JsonHelper.fromJson(gwResponseGson.getContent(), GWBuyApplyNotifyResponseGson.class);
         } else if (gwResponseGson.getRetCode().equals(GWResponseCode.LACK_CLIENT_CONFIG)
                 || gwResponseGson.getRetCode().equals(GWResponseCode.JIJIN_RESPONSE_ERROR)
                 || gwResponseGson.getRetCode().equals(GWResponseCode.SIGN_FAIL)
                 || gwResponseGson.getRetCode().equals(GWResponseCode.VALIDATE_SIGN_FAIL)) {
             //返回支付确认失败

             String errorMessage = gwResponseGson.getRetMessage();
             if (!StringUtils.isEmpty(errorMessage) && errorMessage.length() >= 200) {
                 errorMessage = errorMessage.substring(0, 200);
             }

             jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "errorCode", gwBuyApplyNotifyResponseGson.getErrorCode(), "status", TradeRecordStatus.NOTIFY_FAIL.name(), "errorMsg", errorMessage));
         } else if (gwResponseGson.getRetCode().equals(GWResponseCode.RUNTIME_ERROR)) {
             if (jijinTradeRecordDTO.getStatus().equals(TradeRecordStatus.WITHDRAW_SUCCESS.name()) && jijinTradeRecordDTO.getRetryTimes().compareTo(5L) < 0) {
                 jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "notifyAppNo", notifyAppNo, "retryTimes", jijinTradeRecordDTO.getRetryTimes() + 1));
                 //retry
                 mqService.sendJijinBuyMQ(jijinTradeRecordDTO.getId());
                 return;
             } else {
                 String errorMessage = gwResponseGson.getRetMessage();
                 if (!StringUtils.isEmpty(errorMessage) && errorMessage.length() >= 200) {
                     errorMessage = errorMessage.substring(0, 200);
                 }
                 jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "notifyAppNo", notifyAppNo, "errorCode", "19999", "status", TradeRecordStatus.NOTIFY_FAIL.name(), "errorMsg", errorMessage));
                 return;
             }
         }

         if ("0000".equals(gwBuyApplyNotifyResponseGson.getErrorCode())) {
             jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "errorCode", gwBuyApplyNotifyResponseGson.getErrorCode(), "notifyAppNo", notifyAppNo, "status", TradeRecordStatus.NOTIFY_SUCCESS.name(), "trxDate", gwBuyApplyNotifyResponseGson.getTransactionDate()));
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
     * 带清算的申购撤单
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
     * 落地T+1基金订单
     *
     * @param fundCode
     * @param userId
     * @param amount
     * @param type
     * @param trxId
     * @param frozenCode
     * @param frozenType
     * @param businessMode
     */
    public void recordT1Purchase(String fundCode, Long userId, BigDecimal amount, String type, Long trxId, String frozenCode, String frozenType, String isAgreeRisk,String businessMode) {
        JijinInfoDTO jijinInfoDTO = jijinInfoRepository.findJijinInfo(MapUtils.buildKeyValueMap("fundCode", fundCode));
        if (jijinInfoDTO == null) {
            Logger.info(this, String.format("jijinInfoDTO is null,fundCode [%s]", fundCode));
            return;
        }

        JijinAccountDTO jijinAccountDTO = jijinAccountRepository.findActiveAccount(userId, jijinInfoDTO.getInstId(), "PAF");
        if (jijinAccountDTO == null) {
            Logger.info(this, String.format("jijinAccount is null,userId [%s]", userId));
            return;
        }

        JijinTradeRecordDTO jijinTradeRecordDTO = jijinTradeRecordRepository.getRecordByTrxIdAndTypeChannel(trxId, type,"PAF");
        if (jijinTradeRecordDTO != null) {
            Logger.info(this, String.format("jijinTradeRecord is exists,trxId [%s]", trxId));
            return;
        }

        String bizType = type.equals(TradeRecordType.PURCHASE.name()) ? "022" : "020";
        String appNo = sequenceService.getSerialNumber(bizType);
        jijinTradeRecordDTO = new JijinTradeRecordDTO();
        jijinTradeRecordDTO.setUserId(userId);
        jijinTradeRecordDTO.setFundCode(fundCode);
        jijinTradeRecordDTO.setStatus(TradeRecordStatus.INIT.name());
        jijinTradeRecordDTO.setType(TradeRecordType.valueOf(type));
        jijinTradeRecordDTO.setReqAmount(amount);
        jijinTradeRecordDTO.setContractNo(jijinAccountDTO.getContractNo());
        jijinTradeRecordDTO.setAppNo(appNo);
        jijinTradeRecordDTO.setChargeType("A");
        jijinTradeRecordDTO.setTrxId(trxId);
        jijinTradeRecordDTO.setFrozenCode(frozenCode);
        jijinTradeRecordDTO.setFrozenType(frozenType);
        jijinTradeRecordDTO.setChannel(jijinAccountDTO.getChannel());
        jijinTradeRecordDTO.setDividendType(jijinInfoDTO.getDividendType());
        jijinTradeRecordDTO.setInstId(jijinInfoDTO.getInstId());
        jijinTradeRecordDTO.setIsAgreeRisk(isAgreeRisk);
        jijinTradeRecordDTO.setFlag("1");
        jijinTradeRecordDTO.setIsControversial("0");
        jijinTradeRecordDTO.setUkToken("TRADE" + trxId);
        jijinTradeRecordDTO.setBusinessMode(businessMode);
        JijinTradeRecordDTO result = jijinTradeRecordRepository.insertJijinTradeRecord(jijinTradeRecordDTO);
        //落地成功后发送消息给自己消费 接着做下单处理
        mqService.sendJijinBuyMQ(result.getId());
    }

}
