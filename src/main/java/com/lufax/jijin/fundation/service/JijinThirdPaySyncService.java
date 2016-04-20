package com.lufax.jijin.fundation.service;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lufax.jijin.base.utils.DateUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lufax.jijin.base.constant.SmsTemplate;
import com.lufax.jijin.base.utils.DateUtils;
import com.lufax.jijin.base.utils.JsonHelper;
import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.fundation.constant.BalanceHistoryBizType;
import com.lufax.jijin.fundation.constant.JijinBizType;
import com.lufax.jijin.fundation.constant.JijinConstants;
import com.lufax.jijin.fundation.constant.TradeRecordStatus;
import com.lufax.jijin.fundation.constant.TradeRecordType;
import com.lufax.jijin.fundation.constant.TransactionType;
import com.lufax.jijin.fundation.constant.TransferTypeEnum;
import com.lufax.jijin.fundation.dto.JijinInfoDTO;
import com.lufax.jijin.fundation.dto.JijinThirdPaySyncDTO;
import com.lufax.jijin.fundation.dto.JijinTradeLogDTO;
import com.lufax.jijin.fundation.dto.JijinTradeRecordDTO;
import com.lufax.jijin.fundation.dto.JijinTradeSyncDTO;
import com.lufax.jijin.fundation.dto.JijinUserBalanceDTO;
import com.lufax.jijin.fundation.dto.JijinUserBalanceHistoryDTO;
import com.lufax.jijin.fundation.dto.builder.JijinUserBalenceHistoryDTOBuilder;
import com.lufax.jijin.fundation.gson.BaseResponseGson;
import com.lufax.jijin.fundation.remote.AccountAppCallerService;
import com.lufax.jijin.fundation.remote.FundAppCallerService;
import com.lufax.jijin.fundation.remote.gson.request.DetailResultList;
import com.lufax.jijin.fundation.remote.gson.request.UploadResultGson;
import com.lufax.jijin.fundation.remote.gson.response.FundResponseGson;
import com.lufax.jijin.fundation.repository.JijinInfoRepository;
import com.lufax.jijin.fundation.repository.JijinThirdPaySyncRepository;
import com.lufax.jijin.fundation.repository.JijinTradeLogRepository;
import com.lufax.jijin.fundation.repository.JijinTradeRecordRepository;
import com.lufax.jijin.fundation.repository.JijinTradeSyncRepository;
import com.lufax.jijin.fundation.repository.JijinUserBalanceHistoryRepository;
import com.lufax.jijin.fundation.repository.JijinUserBalanceRepository;
import com.lufax.jijin.service.MqService;
import com.lufax.jijin.sysFacade.gson.Instruction;
import com.lufax.jijin.sysFacade.gson.PaymentWithdrawRequest;
import com.lufax.jijin.sysFacade.gson.PlusFreezeInstructionDetail;
import com.lufax.jijin.sysFacade.gson.TransferFreezeInstructionDetail;
import com.lufax.jijin.sysFacade.gson.result.PaymentResult;
import com.lufax.jijin.sysFacade.service.ExtInterfaceService;
import com.lufax.jijin.sysFacade.service.PaymentAppService;
import com.lufax.jijin.user.domain.UserInfoGson;
import com.lufax.jijin.user.service.UserService;

@Service
public class JijinThirdPaySyncService {

    @Autowired
    private JijinTradeRecordRepository jijinTradeRecordRepository;
    @Autowired
    private JijinThirdPaySyncRepository jijinThirdPaySyncRepository;
    @Autowired
    private AccountAppCallerService accountAppCallerService;
	@Autowired
    private JijinUserBalanceRepository jijinUserBalanceRepository;
    @Autowired
    private JijinUserBalanceHistoryRepository jijinUserBalanceHistoryRepository;
    @Autowired
    private JijinTradeLogRepository jijinTradeLogRepository;

    @Autowired
    private MqService mqService;
    @Autowired
    private JijinInfoRepository jijinInfoRepository;
    @Autowired
    private UserService userService;
	@Autowired
    private ExtInterfaceService extInterfaceService;
    @Autowired
    private JijinTradeSyncRepository jijinTradeSyncRepository;
    @Autowired
    private FundAppCallerService fundAppCallerService;
    @Autowired
    private PaymentAppService paymentAppService;

    public void setFundAppCallerService(FundAppCallerService fundAppCallerService) {
		this.fundAppCallerService = fundAppCallerService;
	}
    public void setAccountAppCallerService(
			AccountAppCallerService accountAppCallerService) {
		this.accountAppCallerService = accountAppCallerService;
	}
    public void setUserService(UserService userService) {
		this.userService = userService;
	}
    public void setExtInterfaceService(ExtInterfaceService extInterfaceService) {
		this.extInterfaceService = extInterfaceService;
	}


	@Transactional
    public void dispatchPurchaseRefund(JijinThirdPaySyncDTO jijinThirdPaySyncDTO) {
        JijinTradeRecordDTO jijinTradeRecordDTO = jijinTradeRecordRepository.getRecordByAppSheetNoAndInstId(jijinThirdPaySyncDTO.getAppSheetNo(), jijinThirdPaySyncDTO.getInstId());
        
        if(null == jijinTradeRecordDTO) {
            JijinTradeSyncDTO jijinTradeSyncDTO = jijinTradeSyncRepository.getJijinTradeSyncTransformed(jijinThirdPaySyncDTO.getAppSheetNo());
            if(null==jijinTradeSyncDTO) {
                String memo = String.format("can not find tradeSync By appSheetNo [%s], do nothing", jijinThirdPaySyncDTO.getAppSheetNo());
                Logger.info(this, memo);
                jijinThirdPaySyncRepository.updateBusJijinThirdPaySyncWithMemo(jijinThirdPaySyncDTO.getId(), JijinThirdPaySyncDTO.Status.UNMATCH.name(), memo);
                return;
            }
            jijinTradeRecordDTO = jijinTradeRecordRepository.getRecordByAppNo(jijinTradeSyncDTO.getLufaxRequestNo());
            if (jijinTradeRecordDTO == null) {
                String memo = String.format("can not find tradeRecord By appSheetNo [%s] ,do nothing", jijinThirdPaySyncDTO.getAppSheetNo());
                Logger.info(this, memo);
                jijinThirdPaySyncRepository.updateBusJijinThirdPaySyncWithMemo(jijinThirdPaySyncDTO.getId(), JijinThirdPaySyncDTO.Status.UNMATCH.name(), memo);
                return;
            }
        }
        
        JijinTradeSyncDTO jijinTradeSyncDTO;
        if (jijinTradeRecordDTO.getType() == TradeRecordType.APPLY||jijinTradeRecordDTO.getType() == TradeRecordType.HZ_APPLY) {
            //部分成功情况下 先查询130,如若没有则查149
            if (jijinThirdPaySyncDTO.getAmount().compareTo(jijinTradeRecordDTO.getReqAmount()) != 0) {
                jijinTradeSyncDTO = jijinTradeSyncRepository.getJijinTradeSyncByAppNoAndFundCodeAndBusCode(jijinTradeRecordDTO.getAppNo(), jijinTradeRecordDTO.getFundCode(), JijinBizType.APPLY_CONFIRM.getCode());
                if (null == jijinTradeSyncDTO) {
                    jijinTradeSyncDTO = jijinTradeSyncRepository.getJijinTradeSyncByAppNoAndFundCodeAndBusCode(jijinTradeRecordDTO.getAppNo(), jijinTradeRecordDTO.getFundCode(), JijinBizType.CREATE_FAIL.getCode());
                }
            } else {
                //全部失败下,直接查询120
                jijinTradeSyncDTO = jijinTradeSyncRepository.getJijinTradeSyncByAppNoAndFundCodeAndBusCode(jijinTradeRecordDTO.getAppNo(), jijinTradeRecordDTO.getFundCode(), JijinBizType.APPLY_CONFIRM_TMP.getCode());
            }
            //转购交易确认文件无法用fundCode和appSheetNo确认唯一,因为赎回确认和申购确认返回fundCode和appSheetNo一样，需要加上businessCode区分
        } else if(jijinTradeRecordDTO.getType() == TradeRecordType.HZ_PURCHASE){
            jijinTradeSyncDTO = jijinTradeSyncRepository.getJijinTradeSyncByAppNoAndFundCodeAndBusCode(jijinTradeRecordDTO.getAppNo(), jijinTradeRecordDTO.getFundCode(),JijinBizType.PURCHASE_CONFIRM.getCode());
        }else {
            jijinTradeSyncDTO = jijinTradeSyncRepository.getJijinTradeSyncByAppSheetNoAndFundCode(jijinTradeRecordDTO.getAppSheetNo(), jijinTradeRecordDTO.getFundCode());
        }

        if (jijinTradeSyncDTO == null) {
            String memo = String.format("can not find tradeSync By appSheetNo [%s],fundCode [%s] ,do nothing", jijinThirdPaySyncDTO.getAppSheetNo(), jijinTradeRecordDTO.getFundCode());
            Logger.info(this, memo);
            jijinThirdPaySyncRepository.updateBusJijinThirdPaySyncWithMemo(jijinThirdPaySyncDTO.getId(), "ERROR", memo);
            return;
        }
        JijinInfoDTO jijinInfoDTO = jijinInfoRepository.findJijinInfo(MapUtils.buildKeyValueMap("fundCode", jijinTradeRecordDTO.getFundCode()));
        if (jijinTradeRecordDTO.getType() == TradeRecordType.APPLY || jijinTradeRecordDTO.getType() == TradeRecordType.PURCHASE) {
            if (jijinThirdPaySyncDTO.getTrxResult().equals("S")) {
                //金额不等的情况下，该笔数据属于error异常,需要人工处理
                if (jijinThirdPaySyncDTO.getAmount().compareTo(jijinTradeRecordDTO.getReqAmount()) > 0) {
                    String memo = String.format("NEED_MANUAL_CONFIRM,thirdPaySync amount is bigger than tradeRecord amount , do nothing ,jijinThirdPaySyncId [%s]", jijinThirdPaySyncDTO.getId());
                    Logger.error(this, memo);
                    jijinThirdPaySyncRepository.updateBusJijinThirdPaySyncWithMemo(jijinThirdPaySyncDTO.getId(), "ERROR", memo);
                    return;
                }
                
                //走清算接口
                //调用fund接口清算赎回帐目,并调增虚拟户
                DetailResultList detailResult = new DetailResultList(jijinTradeRecordDTO.getAppSheetNo(), jijinThirdPaySyncDTO.getAmount(), jijinTradeRecordDTO.getTrxDate(), jijinInfoDTO.getInstId(), "1", jijinInfoDTO.getProductCode());
                List<DetailResultList> list = new ArrayList<DetailResultList>();
                list.add(detailResult);
                UploadResultGson request = new UploadResultGson("JIJIN", String.valueOf(jijinTradeRecordDTO.getId()), list);

                boolean pludResult = callFundAuditUploadResult(request);
                
                if (!pludResult) {

                    Logger.error(this, String.format("NEED_MANUAL_CONFIRM,purchase failed,call account plus failed,tradeRecordId [%s]", jijinTradeRecordDTO.getId()));
                    return;
                } else {
                    JijinUserBalanceDTO jijinUserBalanceDTO = jijinUserBalanceRepository.findUserBalanceByFundCode(jijinTradeRecordDTO.getUserId(), jijinTradeRecordDTO.getFundCode());
                    BigDecimal applyingAmount = jijinUserBalanceDTO.getApplyingAmount().subtract(jijinThirdPaySyncDTO.getAmount());
                    Logger.info(this, "the apply amount " + jijinThirdPaySyncDTO.getAppSheetNo() + " from " + jijinUserBalanceDTO.getApplyingAmount() + ", to subtract " + jijinThirdPaySyncDTO.getAmount() + " to " + applyingAmount);

                    jijinUserBalanceDTO.setApplyingAmount(applyingAmount);
                    int result = jijinUserBalanceRepository.updateFundAccount(jijinUserBalanceDTO);
                    String remark = "";
                    if (result == 1) {
                        BalanceHistoryBizType bizType;
                        if (jijinTradeRecordDTO.getStatus().equals(TradeRecordStatus.PARTIAL_SUCCESS.name())) {
                            jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "status", TradeRecordStatus.SUCCESS.name(), "errorMsg", "申购部分成功"));
                            JijinTradeLogDTO jijinTradeLogDTO = new JijinTradeLogDTO(jijinTradeRecordDTO.getUserId(), jijinTradeRecordDTO.getFundCode(), jijinTradeRecordDTO.getId(), TradeRecordStatus.SUCCESS.name(), jijinTradeRecordDTO.getType(), jijinTradeRecordDTO.getReqAmount().subtract(jijinThirdPaySyncDTO.getAmount()), jijinTradeSyncDTO.getConfirmShare(), jijinTradeRecordDTO.getTrxTime(), jijinTradeRecordDTO.getTrxDate());
                            jijinTradeLogDTO.setFee(jijinTradeSyncDTO.getPurchaseFee());
                            jijinTradeLogDTO.setBusinessMode(jijinTradeRecordDTO.getBusinessMode());
                            jijinTradeLogRepository.insertBusJijinTradeLog(jijinTradeLogDTO);
                            remark = "申购部分成功退款，调减在途资金";
                            bizType = BalanceHistoryBizType.申购部分成功;
                        } else {
                            jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "status", TradeRecordStatus.FAIL.name(), "errorMsg", "申购失败退款成功"));
                            JijinTradeLogDTO jijinTradeLogDTO = new JijinTradeLogDTO(jijinTradeRecordDTO.getUserId(), jijinTradeRecordDTO.getFundCode(), jijinTradeRecordDTO.getId(), TradeRecordStatus.FAIL.name(), jijinTradeRecordDTO.getType(), jijinTradeRecordDTO.getReqAmount(), jijinTradeRecordDTO.getTrxTime(), jijinTradeRecordDTO.getTrxDate());
                            jijinTradeLogDTO.setBusinessMode(jijinTradeRecordDTO.getBusinessMode());
                            jijinTradeLogRepository.insertBusJijinTradeLog(jijinTradeLogDTO);
                            mqService.sendPurchaseResultMsg(jijinTradeRecordDTO.getTrxId(), "fail", BigDecimal.ZERO);
                            remark = "申购失败退款，调减在途资金";
                            bizType = BalanceHistoryBizType.申购失败回款;
                        }

                        JijinUserBalanceHistoryDTO jijinUserBalanceHistoryDTO = JijinUserBalenceHistoryDTOBuilder.buildUserBalanceHistoryDTO(jijinUserBalanceDTO, jijinTradeRecordDTO.getTrxDate(), new BigDecimal("0"), new BigDecimal("0"), remark, bizType, jijinTradeRecordDTO.getId());
                        jijinUserBalanceHistoryRepository.insertBusJijinUserBalanceHistory(jijinUserBalanceHistoryDTO);

                        jijinThirdPaySyncRepository.updateBusJijinThirdPaySync(MapUtils.buildKeyValueMap("id", jijinThirdPaySyncDTO.getId(), "status", "DISPATCHED"));
                    } else {
                        Logger.info(this, String.format("dispatch purchase trade sync failed,update jijin user balance failed userId [%s],fundCode [%s]", jijinTradeRecordDTO.getUserId(), jijinTradeRecordDTO.getFundCode()));
                    }
                    return;
                }
            } else {
                //代发文件中申购失败扣款失败,不应该出现这种情况.应该返回扣款成功,同时给用户虚拟户加钱，并且发送purchaseResultMsg,状态停留在withdraw_fail
                Logger.error(this, String.format("NEED_MANUAL_HANDLE,purchase refund result is fail,tradeRecordId [%s]", jijinTradeRecordDTO.getId()));
                jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "isControversial", "1", "errorMsg", "申购失败,代发结果失败"));
                jijinThirdPaySyncRepository.updateBusJijinThirdPaySync(MapUtils.buildKeyValueMap("id", jijinThirdPaySyncDTO.getId(), "status", "DISPATCHED"));
                return;
            }

        } else if(jijinTradeRecordDTO.getType() == TradeRecordType.HZ_APPLY || jijinTradeRecordDTO.getType() == TradeRecordType.HZ_PURCHASE){
            if (jijinThirdPaySyncDTO.getTrxResult().equals("S")) {
                //金额不等的情况下，该笔数据属于error异常,需要人工处理
                if (jijinThirdPaySyncDTO.getAmount().compareTo(jijinTradeRecordDTO.getReqAmount()) > 0) {
                    String memo = String.format("NEED_MANUAL_CONFIRM,thirdPaySync amount is bigger than tradeRecord amount , do nothing ,jijinThirdPaySyncId [%s]", jijinThirdPaySyncDTO.getId());
                    Logger.error(this, memo);
                    jijinThirdPaySyncRepository.updateBusJijinThirdPaySyncWithMemo(jijinThirdPaySyncDTO.getId(), "ERROR", memo);
                    return;
                }

                String subTransactionType = TransactionType.INCOME_PURCHASE_REFUND.name();
                if (jijinTradeRecordDTO.getType() == TradeRecordType.HZ_APPLY) {
                    subTransactionType = TransactionType.INCOME_APPLY_REFUND.name();
                }
                BaseResponseGson baseResponseGson = accountAppCallerService.plusMoney(jijinThirdPaySyncDTO.getAmount(), "JIJIN", jijinTradeRecordDTO.getUserId(), "投资失败：" + jijinInfoDTO.getFundName(), String.valueOf(jijinTradeRecordDTO.getId()), subTransactionType, jijinTradeRecordDTO.getAppSheetNo());
                if (!baseResponseGson.isSuccess()) {
                    Logger.error(this, String.format("NEED_MANUAL_CONFIRM,purchase failed,call account plus failed,tradeRecordId [%s]", jijinTradeRecordDTO.getId()));
                    return;
                }
                String remark = "";
                BalanceHistoryBizType bizType;
                if (jijinTradeRecordDTO.getStatus().equals(TradeRecordStatus.PARTIAL_SUCCESS.name())) {
                    jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "status", TradeRecordStatus.SUCCESS.name(), "errorMsg", "申购部分成功"));
                    JijinTradeLogDTO jijinTradeLogDTO = new JijinTradeLogDTO(jijinTradeRecordDTO.getUserId(), jijinTradeRecordDTO.getFundCode(), jijinTradeRecordDTO.getId(), TradeRecordStatus.SUCCESS.name(), jijinTradeRecordDTO.getType(), jijinTradeRecordDTO.getReqAmount().subtract(jijinThirdPaySyncDTO.getAmount()), jijinTradeSyncDTO.getConfirmShare(), jijinTradeRecordDTO.getTrxTime(), jijinTradeRecordDTO.getTrxDate());
                    jijinTradeLogDTO.setFee(jijinTradeSyncDTO.getPurchaseFee());
                    jijinTradeLogDTO.setBusinessMode(jijinTradeRecordDTO.getBusinessMode());
                    jijinTradeLogRepository.insertBusJijinTradeLog(jijinTradeLogDTO);

                    //插入赎回成功的log
                    JijinTradeLogDTO redeemLog = new JijinTradeLogDTO(jijinTradeRecordDTO.getUserId(), jijinTradeRecordDTO.getSourceFundCode(), jijinTradeRecordDTO.getId(), TradeRecordStatus.SUCCESS.name(), TradeRecordType.HZ_REDEEM, jijinTradeRecordDTO.getReqAmount(),jijinTradeRecordDTO.getReqAmount(), jijinTradeRecordDTO.getTrxTime(), jijinTradeRecordDTO.getTrxDate());
                    jijinTradeLogRepository.insertBusJijinTradeLog(redeemLog);

                    remark = "申购部分成功退款，调减在途资金";
                    bizType = BalanceHistoryBizType.申购部分成功;

                    JijinUserBalanceDTO jijinUserBalanceDTO = jijinUserBalanceRepository.findUserBalanceByFundCode(jijinTradeRecordDTO.getUserId(), jijinTradeRecordDTO.getSourceFundCode());
                    jijinUserBalanceDTO.setFrozenShare(jijinUserBalanceDTO.getFrozenShare().subtract(jijinThirdPaySyncDTO.getAmount()));
                    int result = jijinUserBalanceRepository.updateFundAccount(jijinUserBalanceDTO);
                    if(result!=1){
                        Logger.info(this,String.format("[zhuangou] purchase refund update userBalance failed sourceFundCode[%s],userId[%s],amount[%s]",jijinTradeRecordDTO.getSourceFundCode(),jijinTradeRecordDTO.getUserId(),jijinThirdPaySyncDTO.getAmount()));
                        return;
                    }

                    JijinUserBalanceHistoryDTO jijinUserBalanceHistoryDTO = JijinUserBalenceHistoryDTOBuilder.buildUserBalanceHistoryDTO(jijinUserBalanceDTO, jijinTradeRecordDTO.getTrxDate(), new BigDecimal("0"), new BigDecimal("0"), remark, bizType, jijinTradeRecordDTO.getId());
                    jijinUserBalanceHistoryRepository.insertBusJijinUserBalanceHistory(jijinUserBalanceHistoryDTO);

                } else {
                    jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "status", TradeRecordStatus.FAIL.name(), "errorMsg", "申购失败退款成功"));
                    JijinTradeLogDTO jijinTradeLogDTO = new JijinTradeLogDTO(jijinTradeRecordDTO.getUserId(), jijinTradeRecordDTO.getFundCode(), jijinTradeRecordDTO.getId(), TradeRecordStatus.FAIL.name(), jijinTradeRecordDTO.getType(), jijinTradeRecordDTO.getReqAmount(), jijinTradeRecordDTO.getTrxTime(), jijinTradeRecordDTO.getTrxDate());
                    jijinTradeLogDTO.setBusinessMode(jijinTradeRecordDTO.getBusinessMode());
                    jijinTradeLogRepository.insertBusJijinTradeLog(jijinTradeLogDTO);
                    JijinTradeLogDTO redeemLog = new JijinTradeLogDTO(jijinTradeRecordDTO.getUserId(), jijinTradeRecordDTO.getSourceFundCode(), jijinTradeRecordDTO.getId(), TradeRecordStatus.SUCCESS.name(), TradeRecordType.HZ_REDEEM, jijinTradeRecordDTO.getReqAmount(),jijinTradeRecordDTO.getReqAmount(), jijinTradeRecordDTO.getTrxTime(), jijinTradeRecordDTO.getTrxDate());
                    jijinTradeLogRepository.insertBusJijinTradeLog(redeemLog);
                    remark = "申购失败赎回成功退款，调减在途资金";
                    bizType = BalanceHistoryBizType.转购失败回款;

                    JijinUserBalanceDTO jijinUserBalanceDTO = jijinUserBalanceRepository.findUserBalanceByFundCode(jijinTradeRecordDTO.getUserId(), jijinTradeRecordDTO.getSourceFundCode());
                    jijinUserBalanceDTO.setFrozenShare(jijinUserBalanceDTO.getFrozenShare().subtract(jijinThirdPaySyncDTO.getAmount()));
                    int result = jijinUserBalanceRepository.updateFundAccount(jijinUserBalanceDTO);
                    if(result!=1){
                        Logger.info(this,String.format("[zhuangou] purchase refund update userBalance failed sourceFundCode[%s],userId[%s],amount[%s]",jijinTradeRecordDTO.getSourceFundCode(),jijinTradeRecordDTO.getUserId(),jijinThirdPaySyncDTO.getAmount()));
                        return;
                    }

                    JijinUserBalanceHistoryDTO jijinUserBalanceHistoryDTO = JijinUserBalenceHistoryDTOBuilder.buildUserBalanceHistoryDTO(jijinUserBalanceDTO, jijinTradeRecordDTO.getTrxDate(), new BigDecimal("0"), new BigDecimal("0"), remark, bizType, jijinTradeRecordDTO.getId());
                    jijinUserBalanceHistoryRepository.insertBusJijinUserBalanceHistory(jijinUserBalanceHistoryDTO);
                    
                    mqService.sendPurchaseResultMsg(jijinTradeRecordDTO.getTrxId(), "fail", BigDecimal.ZERO);
                }

                jijinThirdPaySyncRepository.updateBusJijinThirdPaySync(MapUtils.buildKeyValueMap("id", jijinThirdPaySyncDTO.getId(), "status", "DISPATCHED"));
                return;
            } else {
                //代发文件中申购失败扣款失败,不应该出现这种情况.应该返回扣款成功,同时给用户虚拟户加钱，并且发送purchaseResultMsg,状态停留在withdraw_fail
                Logger.error(this, String.format("NEED_MANUAL_HANDLE,purchase refund result is fail,tradeRecordId [%s]", jijinTradeRecordDTO.getId()));
                jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "isControversial", "1", "errorMsg", "申购失败,代发结果失败"));
                jijinThirdPaySyncRepository.updateBusJijinThirdPaySync(MapUtils.buildKeyValueMap("id", jijinThirdPaySyncDTO.getId(), "status", "DISPATCHED"));
                return;
            }
        } else {
            Logger.info(this, String.format("appSheetNo is not apply or purchase type ,do nothing,appSheetNo is [%s]", jijinThirdPaySyncDTO.getAppSheetNo()));
            jijinThirdPaySyncRepository.updateBusJijinThirdPaySync(MapUtils.buildKeyValueMap("id", jijinThirdPaySyncDTO.getId(), "status", "DISPATCHED"));
        }
    }


    /**
     * 赎回代发， 如果成功，扣除冻结份额，调增虚拟户资金， 翻转trade record状态为success，插trade log
     * 赎回代发， 如果失败，不解冻份额，不调增虚拟户资金， 设置“人工处理”
     *
     * @param jijinThirdPaySyncDTO
     */

    public void dispatchPAFRedeemSync(JijinThirdPaySyncDTO jijinThirdPaySyncDTO) {

        String appSheetNo = jijinThirdPaySyncDTO.getAppSheetNo();
        JijinTradeRecordDTO jijinTradeRecordDTO = jijinTradeRecordRepository.getRecordByAppSheetNoAndInstId(appSheetNo, jijinThirdPaySyncDTO.getInstId());

        // appSheetNo 找不到对应记录，直接返回,打warn log
        if (null == jijinTradeRecordDTO) {
            String memo = String.format("NEED MANUAL CHECK - paf redeem recon job: can not find jijin trade record with sync file's appSheetNo no [%s], thirdPaySyncRecord Id [%s]", appSheetNo, jijinThirdPaySyncDTO.getId());
            Logger.warn(this, memo);
            jijinThirdPaySyncRepository.updateBusJijinThirdPaySyncWithMemo(jijinThirdPaySyncDTO.getId(), JijinThirdPaySyncDTO.Status.UNMATCH.name(), memo);

            return;
        }

        // 非赎回类型记录，直接返回,打error log，开发double check，通常不应该发生
        if (jijinTradeRecordDTO.getType() != TradeRecordType.REDEEM && jijinTradeRecordDTO.getType() != TradeRecordType.FORCE_REDEEM) {
            Logger.error(this, String.format("NEED MANUAL CHECK - paf redeem recon job: return appSheetNo is not belong to a redeem type record, sync file's appSheetNo no [%s], thirdPaySyncRecord Id [%s]", appSheetNo, jijinThirdPaySyncDTO.getId()));
            jijinThirdPaySyncRepository.updateBusJijinThirdPaySync(MapUtils.buildKeyValueMap("id", jijinThirdPaySyncDTO.getId(), "status", "DISPATCHED"));
            return;
        }

        JijinInfoDTO jijinInfoDTO = jijinInfoRepository.findJijinInfo(MapUtils.buildKeyValueMap("fundCode", jijinTradeRecordDTO.getFundCode()));
        // 代发成功
        if (jijinThirdPaySyncDTO.getTrxResult().equals("S")) {

            //基金公司交易确认文件已确认赎回成功，,trade record 状态为 waiting_money (正常flow)
            if (jijinTradeRecordDTO.getStatus().equals(TradeRecordStatus.WAITING_MONEY.name())) {

                JijinTradeSyncDTO jijinTradeSyncDTO = jijinTradeSyncRepository.getJijinTradeSyncByAppSheetNoAndFundCode(jijinTradeRecordDTO.getAppSheetNo(), jijinTradeRecordDTO.getFundCode());
                
                BigDecimal fee = BigDecimal.ZERO;
                if(null!=jijinTradeSyncDTO)
                	fee= jijinTradeSyncDTO.getPurchaseFee();
                confirmRedeemAndIncreaseMoney(jijinThirdPaySyncDTO, jijinTradeRecordDTO, jijinInfoDTO,fee);
            } else if (jijinTradeRecordDTO.getStatus().equals(TradeRecordStatus.SUBMIT_SUCCESS.name())) {
                //平安付代发文件先于基金公司交易确认文件返回， trade record 状态为 submit_success （非正常flow）
                // 按正常逻辑处理，先翻转到终态succss
                Logger.warn(this, String.format("paf redeem recon: 基金公司交易确认文件还没返回，平安付代发文件已返回，先翻转到终态success， sync file's appSheetNo no [%s], thirdPaySyncRecord Id [%s]", appSheetNo, jijinThirdPaySyncDTO.getId()));
                confirmRedeemAndIncreaseMoney(jijinThirdPaySyncDTO, jijinTradeRecordDTO, jijinInfoDTO, null);
            } else {
                // 交易记录处于其他状态，不应该发生代发
                Logger.error(this, String.format("NEED_MANUAL_HANDLE, 发生赎回代发,但是当前交易记录不该发生代发，tradeRecordId [%s]", jijinTradeRecordDTO.getId()));
                jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "isControversial", "1", "errorMsg", "发生赎回代发,但是当前交易记录不该发生代发，需要人工处理"));
            }
        } else {// 赎回代发失败，转人工处理

            Logger.error(this, String.format("NEED_MANUAL_HANDLE, 赎回代发返回结果是失败,tradeRecordId [%s]", jijinTradeRecordDTO.getId()));
            jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "isControversial", "1", "errorMsg", "赎回代发结果为失败，需要人工处理"));
            jijinThirdPaySyncRepository.updateBusJijinThirdPaySync(MapUtils.buildKeyValueMap("id", jijinThirdPaySyncDTO.getId(), "status", "DISPATCHED"));

        }

    }

    private void sendRedeemSuccessSMS(JijinTradeRecordDTO jijinTradeRecordDTO, JijinInfoDTO jijinInfoDTO) {

        //send redeem success sms
        UserInfoGson userInfoGson = userService.getUserInfo(jijinTradeRecordDTO.getUserId());
        List<Map<String, String>> contentList = new ArrayList<Map<String, String>>();
        Map<String, String> params = new HashMap<String, String>();
        params.put("realName", userInfoGson.getName());

        BigDecimal amount = jijinTradeRecordDTO.getReqShare() == null ? jijinTradeRecordDTO.getReqAmount() : jijinTradeRecordDTO.getReqShare();
        params.put("amount", amount.toPlainString());
        params.put("fundName", jijinInfoDTO.getFundName());
        contentList.add(extInterfaceService.generateSmsContentMap(userInfoGson.getMobileNo(), SmsTemplate.JIJIN_REDEEM_SUCCESS,
                params));

        extInterfaceService.sendSms(contentList);
    }


    /**
     * 减去冻结份额，调增虚拟户
     *
     * @param jijinThirdPaySyncDTO
     * @param jijinTradeRecordDTO
     * @param jijinInfoDTO
     */
    @Transactional
    public void confirmRedeemAndIncreaseMoney(JijinThirdPaySyncDTO jijinThirdPaySyncDTO, JijinTradeRecordDTO jijinTradeRecordDTO, JijinInfoDTO jijinInfoDTO, BigDecimal fee) {

    	if(TransferTypeEnum.card.name().equals(jijinTradeRecordDTO.getTransferType())){ //转到银行卡
    		
            //走清算接口
            //调用fund接口清算赎回帐目,并调增虚拟户
            DetailResultList detailResult = new DetailResultList(jijinTradeRecordDTO.getAppSheetNo(), jijinThirdPaySyncDTO.getAmount(), jijinTradeRecordDTO.getTrxDate(), jijinInfoDTO.getInstId(), "1", jijinInfoDTO.getProductCode());
            List<DetailResultList> list = new ArrayList<DetailResultList>();
            list.add(detailResult);
            UploadResultGson request = new UploadResultGson("JIJIN", String.valueOf(jijinTradeRecordDTO.getId()), list);

            boolean pludResult = callFundAuditUploadResult(request);

	        if(pludResult){
	        	 // 更新状态到处理中 PROCCESSING
	        	jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id",jijinTradeRecordDTO.getId(),"status",TradeRecordStatus.PROCESSING.name()));
	        	jijinThirdPaySyncRepository.updateBusJijinThirdPaySync(MapUtils.buildKeyValueMap("id", jijinThirdPaySyncDTO.getId(), "status", JijinThirdPaySyncDTO.Status.DISPATCHED.name()));
	        }else{
	            Logger.error(this, String.format("[handelT0RedeemApplyToBank] jijinTradeRecordDTO[id=%s] transfer account failed since:销账接口调用失败", jijinTradeRecordDTO.getId()));
	            //更新失败状态
	            Map<Object, Object> map = MapUtils.buildKeyValueMap(
	                    "id", jijinTradeRecordDTO.getId(),
	                    "status", TradeRecordStatus.WAITING_MONEY.name(),
	                    "errorCode", "999",
	                    "errorMsg","需人工干预，代发调增到银行卡失败，份额未解冻",
	                    "isControversial","1");
	            jijinTradeRecordRepository.updateBusJijinTradeRecord(map);
	            jijinThirdPaySyncRepository.updateBusJijinThirdPaySync(MapUtils.buildKeyValueMap("id", jijinThirdPaySyncDTO.getId(), "status", JijinThirdPaySyncDTO.Status.DISPATCHED.name()));
	        }

    	}else{
    		
	        boolean pludResult = true;
	        if ("1".equals(jijinTradeRecordDTO.getFlag())) {
	            //走清算接口
	            //调用fund接口清算赎回帐目,并调增虚拟户
	            DetailResultList detailResult = new DetailResultList(jijinTradeRecordDTO.getAppSheetNo(), jijinThirdPaySyncDTO.getAmount(), jijinTradeRecordDTO.getTrxDate(), jijinInfoDTO.getInstId(), "1", jijinInfoDTO.getProductCode());
	            List<DetailResultList> list = new ArrayList<DetailResultList>();
	            list.add(detailResult);
	            UploadResultGson request = new UploadResultGson("JIJIN", String.valueOf(jijinTradeRecordDTO.getId()), list);
	
	            pludResult = callFundAuditUploadResult(request);
	
	        } else {
	            //走老接口，仅调增
	            BaseResponseGson baseResponseGson = accountAppCallerService.plusMoney(jijinThirdPaySyncDTO.getAmount(), "JIJIN", jijinTradeRecordDTO.getUserId(), "赎回：" + jijinInfoDTO.getFundName(), String.valueOf(jijinTradeRecordDTO.getId()), TransactionType.INCOME_REDEMPTION.name(), jijinTradeRecordDTO.getAppSheetNo());
	            pludResult = baseResponseGson.isSuccess();
	        }
	
	        if (!pludResult) {
	            Logger.error(this, String.format("NEED_MANUAL_CONFIRM,赎回代发结果为成功,call account plus failed,tradeRecordId [%s]", jijinTradeRecordDTO.getId()));
	            jijinThirdPaySyncRepository.updateBusJijinThirdPaySync(MapUtils.buildKeyValueMap("id", jijinThirdPaySyncDTO.getId(), "status", JijinThirdPaySyncDTO.Status.DISPATCHED.name()));
	            //更新失败状态
	            Map<Object, Object> map = MapUtils.buildKeyValueMap(
	                    "id", jijinTradeRecordDTO.getId(),
	                    "status", TradeRecordStatus.WAITING_MONEY.name(),
	                    "errorMsg","需人工干预，代发调增失败，份额未解冻",
	                    "isControversial","1");
	            jijinTradeRecordRepository.updateBusJijinTradeRecord(map);
	        } else {
	            JijinUserBalanceDTO jijinUserBalanceDTO = jijinUserBalanceRepository.findUserBalanceByFundCode(jijinTradeRecordDTO.getUserId(), jijinTradeRecordDTO.getFundCode());
	            BigDecimal amount = null;
	            jijinUserBalanceDTO.setFrozenShare(jijinUserBalanceDTO.getFrozenShare().subtract(jijinTradeRecordDTO.getReqShare()));
	            amount = jijinTradeRecordDTO.getReqShare();
	
	            int result = jijinUserBalanceRepository.updateFundAccount(jijinUserBalanceDTO);
	            if (result == 1) {
	                String msg = TradeRecordType.FORCE_REDEEM.name().equals(jijinTradeRecordDTO.getType()) ? "强制赎回成功" : "赎回成功";
	                //WEBDEV-8355:基金(PC)赎回到账时间显示优化
	                //基金公司赎回文件已确认，更新到账日期
	                String accountDate  = DateUtils.formatDateAddHMS(jijinThirdPaySyncDTO.getTrxTime());
	                jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "status", TradeRecordStatus.SUCCESS.name(),
	                        "errorMsg", msg,"accountDate",accountDate));
	                jijinThirdPaySyncRepository.updateBusJijinThirdPaySync(MapUtils.buildKeyValueMap("id", jijinThirdPaySyncDTO.getId(), "status", JijinThirdPaySyncDTO.Status.DISPATCHED.name()));

	                //insert user balance history
	                JijinUserBalanceHistoryDTO jijinUserBalanceHistoryDTO = JijinUserBalenceHistoryDTOBuilder.buildUserBalanceHistoryDTO(jijinUserBalanceDTO, jijinTradeRecordDTO.getTrxDate(), new BigDecimal("0"), new BigDecimal("0"), "赎回代付成功，冻结份额调减", BalanceHistoryBizType.赎回成功回款, jijinTradeRecordDTO.getId());
	                jijinUserBalanceHistoryRepository.insertBusJijinUserBalanceHistory(jijinUserBalanceHistoryDTO);

	                JijinTradeLogDTO log = new JijinTradeLogDTO(jijinTradeRecordDTO.getUserId(), jijinTradeRecordDTO.getFundCode(), jijinTradeRecordDTO.getId(), TradeRecordStatus.SUCCESS.name(), jijinTradeRecordDTO.getType(), jijinThirdPaySyncDTO.getAmount(), amount, jijinTradeRecordDTO.getTrxTime(), jijinTradeRecordDTO.getTrxDate());
	                log.setFee(fee);
	                log.setAccountDate(accountDate);
	                log.setConfirmDate(jijinTradeRecordDTO.getConfirmDate());
	                jijinTradeLogRepository.insertBusJijinTradeLog(log);
	
	                sendRedeemSuccessSMS(jijinTradeRecordDTO, jijinInfoDTO);
	                
	                //大华货基，send MQ to yeb
	                if(JijinConstants.DAHUA_FUND_CODE.equals(jijinTradeRecordDTO.getFundCode()))              
	                if("LBO".equals(jijinTradeRecordDTO.getChannel())){
	                	mqService.sendLBOTradeResultMsg(jijinTradeRecordDTO.getTrxId(), "success","REDEEM","000");
	                }else{
	                	mqService.sendDhTradeResultMsg(jijinTradeRecordDTO.getTrxId(), "success");
	                } 
	            } else {
	                Logger.info(this,
	                        String.format(
	                                "paf redeem recon job - update jijin user balance failed userId [%s],fundCode [%s], will try again",
	                                jijinTradeRecordDTO.getUserId(),
	                                jijinTradeRecordDTO.getFundCode()));
	            }
	        }
    	}

    }


    /**
     * 赎回清账
     *
     * @param uploadResultGson
     * @return
     */
    private boolean callFundAuditUploadResult(UploadResultGson uploadResultGson) {
        int retry_times = 0;
        FundResponseGson fundResponseGson = new FundResponseGson();
        boolean result = false;
        while (!result) {
            fundResponseGson = fundAppCallerService.auditUploadResult(uploadResultGson);

            if ("SUCCESS".equals(fundResponseGson.getResultStatus())) {
                result = true;
            } else if ("PROCESSING".equals(fundResponseGson.getResultStatus()) || StringUtils.isEmpty(fundResponseGson.getResultStatus())) {
                result = false;
            } else {
                Logger.info(this, String.format("This trade record [%s], audit remote service response fail, retCode [%s]", uploadResultGson.getInstructionNo(), fundResponseGson.getRetCode()));
                result = false;
                break;
            }
            retry_times = retry_times + 1;
            if (retry_times > 100 || result) break;
        }
        return result;
    }
    
    @Transactional
    /**
     * 
     * @param tradeRecordId
     */
    public void handForceRedeemPay(Long tradeRecordId){
    	
    	JijinTradeRecordDTO record = jijinTradeRecordRepository.getRecordById(tradeRecordId);
    	 List<JijinThirdPaySyncDTO> thirdPayRecords =  jijinThirdPaySyncRepository.findBusJijinThirdPaySync(MapUtils.buildKeyValueMap("appSheetNo",record.getAppSheetNo()));
    	
    	if(CollectionUtils.isEmpty(thirdPayRecords)|| JijinThirdPaySyncDTO.Status.DISPATCHED.name().equals(thirdPayRecords.get(0).getStatus())){
    		//do nothing,just return
    		return;
    	}
    	
    	//分钱
    	dispatchPAFRedeemSync(thirdPayRecords.get(0));
    }
    
//    /**
//     * 调增后再转入银行卡
//     * @param jijinTradeRecordDTO
//     * @param jijinInfoDTO
//     * @return
//     */
//    private PaymentResult transferToBankCard(JijinTradeRecordDTO jijinTradeRecordDTO,JijinInfoDTO jijinInfoDTO ){
//    	PaymentResult res = new PaymentResult();  	
//    	
//    	PlusFreezeInstructionDetail detail = new PlusFreezeInstructionDetail(jijinTradeRecordDTO.getReqShare(), jijinTradeRecordDTO.getAppSheetNo(),
//    			jijinInfoDTO.getInstId(), jijinTradeRecordDTO.getTrxDate(), "赎回调增",
//    			"赎回调增", String.valueOf(jijinTradeRecordDTO.getId()), "赎回调增",
//    			String.valueOf(jijinTradeRecordDTO.getId()), "赎回调增清算",
//    			"FORZEN_FUND", "赎回调增冻结",
//    			String.valueOf(jijinTradeRecordDTO.getId()), "赎回调增冻结", jijinInfoDTO.getProductCode(),
//    			DateUtils.formatDate(new Date(), DateUtils.DATE_TIME_FORMAT), jijinTradeRecordDTO.getAppSheetNo(), "赎回调增",
//    			"赎回调增转银行卡");
//    	
//    	
//    	List<Instruction> prepareOperationList = new ArrayList<Instruction>();
//    	Instruction ins = new Instruction("PLUS_FREEZE",JsonHelper.toJson(detail));
//    	prepareOperationList.add(ins);
//    	PaymentWithdrawRequest request = new PaymentWithdrawRequest("JIJIN", String.valueOf(jijinTradeRecordDTO.getId()),
//    			"0", jijinTradeRecordDTO.getUserId(), jijinTradeRecordDTO.getReqShare(),
//    			"CUSTOMER_WITHDRAWAL", DateUtils.formatDate(new Date(), DateUtils.DATE_TIME_FORMAT), "陆金宝赎回进银行卡",
//    			null, null, prepareOperationList, jijinInfoDTO.getProductCode(),
//    			jijinTradeRecordDTO.getTrxDate(), jijinTradeRecordDTO.getAppSheetNo(), "大华赎回进银行卡",
//    			"大华赎回进银行卡", String.valueOf(jijinTradeRecordDTO.getId()), "大华赎回进银行卡");
//    	
//    	res = paymentAppService.cashierWithdraw(request);
//    	
//    	return res;
//    }
}
