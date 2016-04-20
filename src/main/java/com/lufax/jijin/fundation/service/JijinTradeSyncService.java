package com.lufax.jijin.fundation.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lufax.jijin.base.constant.SmsTemplate;
import com.lufax.jijin.base.utils.DateUtils;
import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.fundation.constant.BalanceHistoryBizType;
import com.lufax.jijin.fundation.constant.JijinBizType;
import com.lufax.jijin.fundation.constant.TradeRecordStatus;
import com.lufax.jijin.fundation.constant.TradeRecordType;
import com.lufax.jijin.fundation.constant.TransactionType;
import com.lufax.jijin.fundation.dto.JijinAccountDTO;
import com.lufax.jijin.fundation.dto.JijinFreezeRuleDTO;
import com.lufax.jijin.fundation.dto.JijinInfoDTO;
import com.lufax.jijin.fundation.dto.JijinTradeLogDTO;
import com.lufax.jijin.fundation.dto.JijinTradeRecordDTO;
import com.lufax.jijin.fundation.dto.JijinTradeSyncDTO;
import com.lufax.jijin.fundation.dto.JijinUserBalanceDTO;
import com.lufax.jijin.fundation.dto.JijinUserBalanceHistoryDTO;
import com.lufax.jijin.fundation.dto.builder.JijinUserBalenceHistoryDTOBuilder;
import com.lufax.jijin.fundation.gson.BaseResponseGson;
import com.lufax.jijin.fundation.remote.AccountAppCallerService;
import com.lufax.jijin.fundation.remote.FundAppCallerService;
import com.lufax.jijin.fundation.remote.gson.request.PaymentRequestParam;
import com.lufax.jijin.fundation.remote.gson.request.RedeemAuditRequestGson;
import com.lufax.jijin.fundation.repository.JijinAccountRepository;
import com.lufax.jijin.fundation.repository.JijinFreezeRecordRepository;
import com.lufax.jijin.fundation.repository.JijinFreezeRuleRepository;
import com.lufax.jijin.fundation.repository.JijinInfoRepository;
import com.lufax.jijin.fundation.repository.JijinTradeLogRepository;
import com.lufax.jijin.fundation.repository.JijinTradeRecordRepository;
import com.lufax.jijin.fundation.repository.JijinTradeSyncRepository;
import com.lufax.jijin.fundation.repository.JijinUnFreezeRecordRepository;
import com.lufax.jijin.fundation.repository.JijinUnFreezeScheduleRepository;
import com.lufax.jijin.fundation.repository.JijinUserBalanceHistoryRepository;
import com.lufax.jijin.fundation.repository.JijinUserBalanceRepository;
import com.lufax.jijin.service.MqService;
import com.lufax.jijin.sysFacade.gson.BatchTxUnfreezeRequest;
import com.lufax.jijin.sysFacade.gson.FrozenNo;
import com.lufax.jijin.sysFacade.gson.PaymentIncreaseRequest;
import com.lufax.jijin.sysFacade.gson.PaymentRequest;
import com.lufax.jijin.sysFacade.gson.result.PaymentResult;
import com.lufax.jijin.sysFacade.service.ExtInterfaceService;
import com.lufax.jijin.sysFacade.service.PaymentAppService;
import com.lufax.jijin.user.domain.UserInfoGson;
import com.lufax.jijin.user.service.UserService;
import com.site.lookup.util.StringUtils;


@Service
public class JijinTradeSyncService {

    @Autowired
    private JijinTradeSyncRepository jijinTradeSyncRepository;
    @Autowired
    private JijinTradeRecordRepository jijinTradeRecordRepository;
    @Autowired
    private JijinUserBalanceRepository jijinUserBalanceRepository;
    @Autowired
    private JijinUserBalanceHistoryRepository jijinUserBalanceHistoryRepository;
    @Autowired
    private MqService mqService;
    @Autowired
    private JijinInfoRepository jijinInfoRepository;
    @Autowired
    private JijinTradeLogRepository jijinTradeLogRepository;
    @Autowired
    private ExtInterfaceService extInterfaceService;
    @Autowired
    private UserService userService;
    @Autowired
    private AccountAppCallerService accountAppCallerService;
    @Autowired
    private FundAppCallerService fundAppCallerService;
    @Autowired
    private JijinAccountRepository jijinAccountRepository;
    @Autowired
    private SequenceService sequenceService;
    @Autowired
    private JijinFreezeRuleRepository jijinFreezeRuleRepository;
    @Autowired
    private JijinFreezeRecordRepository jijinFreezeRecordRepository;
    @Autowired
    private JijinUnFreezeScheduleRepository jijinUnFreezeScheduleRepository;
    @Autowired
    private JijinUnFreezeRecordRepository jijinUnFreezeRecordRepository;
    @Autowired
    private JijinFreezeService jijinFreezeService;
    @Autowired
    private PaymentAppService paymentAppService;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setExtInterfaceService(ExtInterfaceService extInterfaceService) {
        this.extInterfaceService = extInterfaceService;
    }

    @Transactional
    public void dispatchPurchaseTradeSync(JijinTradeSyncDTO jijinTradeSyncDTO) {
        String appNo = jijinTradeSyncDTO.getLufaxRequestNo();
        JijinTradeRecordDTO jijinTradeRecordDTO = jijinTradeRecordRepository.getRecordByAppNo(appNo);

        if (null == jijinTradeRecordDTO) {
            Logger.warn(this, String.format("jijin purchase recon: can not find jijin trade record with sync file's app no [%s]", appNo));
            jijinTradeSyncRepository.updateJijinTradeSyncStatusById(jijinTradeSyncDTO.getId(), "UNMATCH");
            return;
        }

        //此条件只在代发文件比交易确认文件提前发送的情况下处理
        if (jijinTradeRecordDTO.getStatus().equals(TradeRecordStatus.FAIL.name())) {
            Logger.warn(this, String.format("jijinTradeRecord status is already fail,need do nothing ,appNo [%s]", appNo));
            jijinTradeSyncRepository.updateJijinTradeSyncStatusById(jijinTradeSyncDTO.getId(), "DISPATCHED");
            return;
        }

        //撤单不在对账文件中处理
        if (jijinTradeRecordDTO.getStatus().equals(TradeRecordStatus.CANCEL_SUCCESS.name())) {
            Logger.info(this, String.format("trade record has already cancel,need do nothing.tradeRecordId [%s]", jijinTradeRecordDTO.getId()));
            jijinTradeSyncRepository.updateJijinTradeSyncStatusById(jijinTradeSyncDTO.getId(), "DISPATCHED");
            return;
        }

        
        //转购逻辑单独处理
        if (jijinTradeRecordDTO.getType().equals(TradeRecordType.HZ_PURCHASE) || jijinTradeRecordDTO.getType().equals(TradeRecordType.HZ_APPLY)) {
            JijinInfoDTO toJijinInfo = jijinInfoRepository.findJijinInfoByFundCode(jijinTradeRecordDTO.getFundCode());
            //认购等到130文件或者149给到后再处理
            if (jijinTradeSyncDTO.getBusinessCode().equals(JijinBizType.APPLY_CONFIRM_TMP.getCode())) {
                if (jijinTradeSyncDTO.getTradeResCode().equals("0000")) {
                    //成功有130或者149文件,120不处理
                    jijinTradeSyncRepository.updateJijinTradeSyncStatusById(jijinTradeSyncDTO.getId(), "DISPATCHED");
                    return;
                }
                //失败情况下肯定没有130或者149文件,所以此时当成失败处理,等待代发结果文件退款
                JijinTradeSyncDTO redeem = jijinTradeSyncRepository.getJijinTradeSyncByAppNoAndFundCodeAndBusCode(appNo, jijinTradeRecordDTO.getSourceFundCode(), JijinBizType.REDEEM_CONFIRM.getCode());
                //未给赎回确认先给了申购确认,此时不处理
                if (null == redeem) {
                    Logger.info(this, String.format("[zhuangou] redeem trade sync before purchase trade sync,do nothing. tradeSyncId:[%s] ", jijinTradeSyncDTO.getId()));
                    return;
                }

                //赎回成功,申购失败,更新申购状态至UNTRANSFER_MONEY,走申购失败退款流程
                if (redeem.getTradeResCode().equals("0000")) {
                    jijinTradeSyncRepository.updateJijinTradeSyncStatusById(jijinTradeSyncDTO.getId(), "DISPATCHED");
                    int result = jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "status", TradeRecordStatus.UNTRANSFER_MONEY.name()));
                    if (result != 1) {
                        throw new OptimisticLockingFailureException("");
                    }
                    return;
                } else {
                    //赎回失败,申购失败解冻
                    //解冻并返还份额/金额，更新trade record 状态为FAIL, 插 trade log
                    BigDecimal amount = jijinTradeRecordDTO.getReqShare() == null ? jijinTradeRecordDTO.getReqAmount() : jijinTradeRecordDTO.getReqShare();

                    JijinUserBalanceDTO userBalance = jijinUserBalanceRepository.findUserBalanceByFundCode(jijinTradeRecordDTO.getUserId(), jijinTradeRecordDTO.getSourceFundCode());
                    userBalance.setShareBalance(userBalance.getShareBalance().add(amount));
                    userBalance.setFrozenShare(userBalance.getFrozenShare().subtract(amount));

                    int result = jijinUserBalanceRepository.updateFundAccount(userBalance);

                    if (result == 1) {
                        jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "status", TradeRecordStatus.FAIL.name()));
                        //insert user balance history
                        JijinUserBalanceHistoryDTO jijinUserBalanceHistoryDTO = JijinUserBalenceHistoryDTOBuilder.buildUserBalanceHistoryDTO(userBalance, jijinTradeRecordDTO.getTrxDate(), amount, new BigDecimal("0"), "转购失败，退还冻结份额", BalanceHistoryBizType.转购失败, jijinTradeRecordDTO.getId());
                        jijinUserBalanceHistoryRepository.insertBusJijinUserBalanceHistory(jijinUserBalanceHistoryDTO);
                        jijinTradeLogRepository.insertBusJijinTradeLog(new JijinTradeLogDTO(jijinTradeRecordDTO.getUserId(), jijinTradeRecordDTO.getFundCode(), jijinTradeRecordDTO.getId(), TradeRecordStatus.FAIL.name(), jijinTradeRecordDTO.getType(), amount, null, jijinTradeRecordDTO.getTrxTime(), jijinTradeRecordDTO.getTrxDate()));
                        jijinTradeLogRepository.insertBusJijinTradeLog(new JijinTradeLogDTO(jijinTradeRecordDTO.getUserId(), jijinTradeRecordDTO.getSourceFundCode(), jijinTradeRecordDTO.getId(), TradeRecordStatus.FAIL.name(), TradeRecordType.HZ_REDEEM, amount, amount, jijinTradeRecordDTO.getTrxTime(), jijinTradeRecordDTO.getTrxDate()));
                        jijinTradeSyncRepository.updateJijinTradeSyncStatusById(jijinTradeSyncDTO.getId(), "DISPATCHED");
                        mqService.sendPurchaseResultMsg(jijinTradeRecordDTO.getTrxId(), "fail", BigDecimal.ZERO);
                    }
                    return;
                }
            }
            //申购成功=转购成功
            if (jijinTradeSyncDTO.getTradeResCode().equals("0000")) {
                if (jijinTradeRecordDTO.getStatus().equals(TradeRecordStatus.SUBMIT_SUCCESS.name())) {
                    //转购出账户
                    JijinUserBalanceDTO fromBalance = jijinUserBalanceRepository.findUserBalanceByFundCode(jijinTradeRecordDTO.getUserId(), jijinTradeRecordDTO.getSourceFundCode());
                    //转购入账户
                    JijinUserBalanceDTO toBalance = jijinUserBalanceRepository.findUserBalanceByFundCode(jijinTradeRecordDTO.getUserId(), jijinTradeRecordDTO.getFundCode());

                    //货基转购的时候申购交易确认是累加份额
                    BigDecimal realChangeAmount = jijinTradeSyncDTO.getConfirmShare();

                    //需更新转出账户份额,转入账户份额
                    if (null == toBalance) {
                        toBalance = new JijinUserBalanceDTO();
                        toBalance.setUserId(jijinTradeRecordDTO.getUserId());
                        toBalance.setFundCode(jijinTradeRecordDTO.getFundCode());
                        toBalance.setDividendType(jijinTradeRecordDTO.getDividendType());
                        toBalance.setDividendStatus("DONE");

                        toBalance.setShareBalance(realChangeAmount);
                        toBalance.setFrozenShare(new BigDecimal("0.00"));
                        toBalance.setApplyingAmount(new BigDecimal("0.00"));
                        toBalance.setIsShow(toJijinInfo.getIsShow());//大华直销货基无法显示在前台,此处字段做兼容
                        jijinUserBalanceRepository.insertBusJijinUserBalance(toBalance);

                        fromBalance.setFrozenShare(fromBalance.getFrozenShare().subtract(jijinTradeSyncDTO.getConfirmAmount())); //转出货基需要调减冻结份额
                        int result = jijinUserBalanceRepository.updateFundAccount(fromBalance);
                        if (result != 1) {
                            throw new OptimisticLockingFailureException("");
                        }

                        JijinUserBalanceHistoryDTO toBalanceHistoryDTO = JijinUserBalenceHistoryDTOBuilder.buildUserBalanceHistoryDTO(toBalance, jijinTradeRecordDTO.getTrxDate(), realChangeAmount, new BigDecimal("0"), "转购转入成功份额", BalanceHistoryBizType.转购转入成功, jijinTradeRecordDTO.getId());
                        jijinUserBalanceHistoryRepository.insertBusJijinUserBalanceHistory(toBalanceHistoryDTO);
                        JijinUserBalanceHistoryDTO fromBalanceHistoryDTO = JijinUserBalenceHistoryDTOBuilder.buildUserBalanceHistoryDTO(fromBalance, jijinTradeRecordDTO.getTrxDate(), new BigDecimal("0"), jijinTradeSyncDTO.getConfirmAmount(), "转购转出成功份额", BalanceHistoryBizType.转购转出成功, jijinTradeRecordDTO.getId());
                        jijinUserBalanceHistoryRepository.insertBusJijinUserBalanceHistory(fromBalanceHistoryDTO);

                    } else {
                        fromBalance.setFrozenShare(fromBalance.getFrozenShare().subtract(jijinTradeSyncDTO.getConfirmAmount()));
                        toBalance.setShareBalance(toBalance.getShareBalance().add(realChangeAmount));
                        int fromUpdateResult = jijinUserBalanceRepository.updateFundAccount(fromBalance);
                        int toUpdateResult = jijinUserBalanceRepository.updateFundAccount(toBalance);
                        if (fromUpdateResult != 1 || toUpdateResult != 1) {
                            throw new OptimisticLockingFailureException("");
                        }
                        JijinUserBalanceHistoryDTO toBalanceHistoryDTO = JijinUserBalenceHistoryDTOBuilder.buildUserBalanceHistoryDTO(toBalance, jijinTradeRecordDTO.getTrxDate(), realChangeAmount, new BigDecimal("0"), "转购转入成功份额", BalanceHistoryBizType.转购转入成功, jijinTradeRecordDTO.getId());
                        jijinUserBalanceHistoryRepository.insertBusJijinUserBalanceHistory(toBalanceHistoryDTO);
                        JijinUserBalanceHistoryDTO fromBalanceHistoryDTO = JijinUserBalenceHistoryDTOBuilder.buildUserBalanceHistoryDTO(fromBalance, jijinTradeRecordDTO.getTrxDate(), new BigDecimal("0"), jijinTradeSyncDTO.getConfirmAmount(), "转购转出成功份额", BalanceHistoryBizType.转购转出成功, jijinTradeRecordDTO.getId());
                        jijinUserBalanceHistoryRepository.insertBusJijinUserBalanceHistory(fromBalanceHistoryDTO);
                    }

                    //此处校验转购对应的申购金额是否与发起的转购金额匹配,不匹配则是部分成功,需要等到平安符代发结果文件回来后调减源货币基金的冻结份额，同时调整增用户虚拟户
                    //WEBDEV-8355 基金(PC)赎回到账时间显示优化
                    String confirmDate = DateUtils.formatDateAddHMS(jijinTradeSyncDTO.getTrxConfirmDate());
                    if (jijinTradeRecordDTO.getReqAmount().compareTo(jijinTradeSyncDTO.getConfirmAmount()) != 0) {
                        jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "status", TradeRecordStatus.PARTIAL_SUCCESS.name(),"confirmDate",confirmDate));
                    } else {
                        jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "status", TradeRecordStatus.SUCCESS.name(),"confirmDate",confirmDate));
                        JijinTradeLogDTO jijinTradeLogDTO = new JijinTradeLogDTO(jijinTradeRecordDTO.getUserId(), jijinTradeRecordDTO.getFundCode(), jijinTradeRecordDTO.getId(), TradeRecordStatus.SUCCESS.name(), jijinTradeRecordDTO.getType(), jijinTradeRecordDTO.getReqAmount(), jijinTradeSyncDTO.getConfirmShare(), jijinTradeRecordDTO.getTrxTime(), jijinTradeRecordDTO.getTrxDate());
                        jijinTradeLogDTO.setFee(jijinTradeSyncDTO.getPurchaseFee());
                        jijinTradeLogDTO.setConfirmDate(confirmDate);
                        jijinTradeLogRepository.insertBusJijinTradeLog(jijinTradeLogDTO);
                        JijinTradeLogDTO redeemLog = new JijinTradeLogDTO(jijinTradeRecordDTO.getUserId(), jijinTradeRecordDTO.getSourceFundCode(), jijinTradeRecordDTO.getId(), TradeRecordStatus.SUCCESS.name(), TradeRecordType.HZ_REDEEM, jijinTradeRecordDTO.getReqAmount(), jijinTradeRecordDTO.getReqAmount(), jijinTradeRecordDTO.getTrxTime(), jijinTradeRecordDTO.getTrxDate());
                        jijinTradeLogRepository.insertBusJijinTradeLog(redeemLog);
                    }
                    jijinTradeSyncRepository.updateJijinTradeSyncStatusById(jijinTradeSyncDTO.getId(), "DISPATCHED");

                    //此处通知trading-app成功的金额应该是申购确认成功的金额
                    mqService.sendPurchaseResultMsg(jijinTradeRecordDTO.getTrxId(), "success", jijinTradeSyncDTO.getConfirmAmount());
                    Logger.info(this, String.format("[zhuan gou] dispatch purchase trade sync success,tradeSyncId [%s]", jijinTradeSyncDTO.getId()));
                } else {
                    Logger.info(this, String.format("[zhuan gou] tradeSyncStatus is success, but trade record status is not in condition do nothing tradeRecordStatus [%s],recordId [%s] ", jijinTradeRecordDTO.getStatus(), jijinTradeRecordDTO.getId()));
                }
            } else {
                //申购失败流程,先拿到赎回的交易确认
                JijinTradeSyncDTO redeem = jijinTradeSyncRepository.getJijinTradeSyncByAppNoAndFundCodeAndBusCode(appNo, jijinTradeRecordDTO.getSourceFundCode(), JijinBizType.REDEEM_CONFIRM.getCode());
                //未给赎回确认先给了申购确认,此时不处理
                if (null == redeem) {
                    Logger.info(this, String.format("[zhuangou] redeem trade sync before purchase trade sync,do nothing. tradeSyncId:[%s] ", jijinTradeSyncDTO.getId()));
                    return;
                }

                //赎回成功,申购失败,更新申购状态至UNTRANSFER_MONEY,走申购失败退款流程
                if (redeem.getTradeResCode().equals("0000")) {
                    jijinTradeSyncRepository.updateJijinTradeSyncStatusById(jijinTradeSyncDTO.getId(), "DISPATCHED");
                    int result = jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "status", TradeRecordStatus.UNTRANSFER_MONEY.name()));
                    if (result != 1) {
                        throw new OptimisticLockingFailureException("");
                    }
                    return;
                } else {
                    //赎回失败,申购失败解冻
                    //解冻并返还份额/金额，更新trade record 状态为FAIL, 插 trade log
                    BigDecimal amount = jijinTradeRecordDTO.getReqShare() == null ? jijinTradeRecordDTO.getReqAmount() : jijinTradeRecordDTO.getReqShare();

                    JijinUserBalanceDTO userBalance = jijinUserBalanceRepository.findUserBalanceByFundCode(jijinTradeRecordDTO.getUserId(), jijinTradeRecordDTO.getSourceFundCode());
                    userBalance.setShareBalance(userBalance.getShareBalance().add(amount));
                    userBalance.setFrozenShare(userBalance.getFrozenShare().subtract(amount));

                    int result = jijinUserBalanceRepository.updateFundAccount(userBalance);

                    if (result == 1) {
                        jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "status", TradeRecordStatus.FAIL.name()));
                        //insert user balance history
                        JijinUserBalanceHistoryDTO jijinUserBalanceHistoryDTO = JijinUserBalenceHistoryDTOBuilder.buildUserBalanceHistoryDTO(userBalance, jijinTradeRecordDTO.getTrxDate(), amount, new BigDecimal("0"), "转购失败，退还冻结份额", BalanceHistoryBizType.转购失败, jijinTradeRecordDTO.getId());
                        jijinUserBalanceHistoryRepository.insertBusJijinUserBalanceHistory(jijinUserBalanceHistoryDTO);
                        jijinTradeLogRepository.insertBusJijinTradeLog(new JijinTradeLogDTO(jijinTradeRecordDTO.getUserId(), jijinTradeRecordDTO.getFundCode(), jijinTradeRecordDTO.getId(), TradeRecordStatus.FAIL.name(), jijinTradeRecordDTO.getType(), amount, null, jijinTradeRecordDTO.getTrxTime(), jijinTradeRecordDTO.getTrxDate()));
                        jijinTradeLogRepository.insertBusJijinTradeLog(new JijinTradeLogDTO(jijinTradeRecordDTO.getUserId(), jijinTradeRecordDTO.getSourceFundCode(), jijinTradeRecordDTO.getId(), TradeRecordStatus.FAIL.name(), TradeRecordType.HZ_REDEEM, amount, amount, jijinTradeRecordDTO.getTrxTime(), jijinTradeRecordDTO.getTrxDate()));
                        jijinTradeSyncRepository.updateJijinTradeSyncStatusById(jijinTradeSyncDTO.getId(), "DISPATCHED");
                        mqService.sendPurchaseResultMsg(jijinTradeRecordDTO.getTrxId(), "fail", BigDecimal.ZERO);
                    }
                    return;
                }
            }
        } else{

            JijinInfoDTO jijinInfoDTO = jijinInfoRepository.findJijinInfo(MapUtils.buildKeyValueMap("fundCode", jijinTradeRecordDTO.getFundCode()));
            //认购对账流程 失败情况下没有130或者149文件  0000分部分成功和全额成功,并且必有130或者149
            if (jijinTradeSyncDTO.getBusinessCode().equals(JijinBizType.APPLY_CONFIRM_TMP.getCode())) {
                if (jijinTradeSyncDTO.getTradeResCode().equals("0000")) {
                    //成功有130或者149文件,120不处理
                    jijinTradeSyncRepository.updateJijinTradeSyncStatusById(jijinTradeSyncDTO.getId(), "DISPATCHED");
                    
                 	PaymentIncreaseRequest request = new PaymentIncreaseRequest("JIJIN", String.valueOf(jijinTradeRecordDTO.getId()),
                 			jijinTradeRecordDTO.getUserId(), TransactionType.INCOME_APPLY_REFUND.name(),
                 			jijinTradeRecordDTO.getAppSheetNo(), jijinInfoDTO.getInstId(), 
                 			jijinTradeRecordDTO.getReqAmount(), jijinTradeRecordDTO.getTrxDate(),
                             "认购失败退款：" + jijinInfoDTO.getFundName(),jijinTradeRecordDTO.getTrxDate(),            
                             jijinInfoDTO.getProductCode(),jijinTradeRecordDTO.getAppSheetNo(),"JIJIN_APPLY_REFUND",
                            "认购失败退款入账", String.valueOf(jijinTradeRecordDTO.getId()),"JIJIN_APPLY_REFUND");
                    PaymentResult response = paymentAppService.increaseMoneyWithAudit(request);
                    if(!"SUCCESS".equals(response.getStatus())){
                    	Logger.error(this, String.format("NEED_MANUAL_HANDLE,认购失败退款入账失败, ,tradeRecordId [%s]", jijinTradeRecordDTO.id()));
                    }
                    return;

                } else {
                    //失败情况下肯定没有130或者149文件,所以此时当成失败处理
                    //申购对账失败的资金需要T+2日才能返回，这里只更新状态为未转账
                    if (jijinTradeRecordDTO.getStatus().equals(TradeRecordStatus.NOTIFY_SUCCESS.name()) || jijinTradeRecordDTO.getStatus().equals(TradeRecordStatus.NOTIFY_FAIL.name()) || jijinTradeRecordDTO.getStatus().equals(TradeRecordStatus.WITHDRAW_SUCCESS.name())) {
                        jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "status", TradeRecordStatus.UNTRANSFER_MONEY.name()));
                        jijinTradeSyncRepository.updateJijinTradeSyncStatusById(jijinTradeSyncDTO.getId(), "DISPATCHED");
                        return;
                    }

                    //调用平安付超时,对账文件返回失败需要解冻并且发送投资失败MQ
                    if (jijinTradeRecordDTO.getStatus().equals(TradeRecordStatus.SUBMIT_SUCCESS.name())) {
                        String remark = "认购：" + jijinInfoDTO.getFundName();
                        
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
                        
                        jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "status", TradeRecordStatus.FAIL.name()));
                        jijinTradeSyncRepository.updateJijinTradeSyncStatusById(jijinTradeSyncDTO.getId(), "DISPATCHED");
                        mqService.sendPurchaseResultMsg(jijinTradeRecordDTO.getTrxId(), "fail", BigDecimal.ZERO);
                        return;
                    }

                    Logger.info(this, String.format("tradeSyncStatus is fail, but trade record status is not in condition do nothing tradeRecordStatus [%s],recordId [%s] ", jijinTradeRecordDTO.getStatus(), jijinTradeRecordDTO.getId()));
                }

            } else {
                if (jijinTradeSyncDTO.getTradeResCode().equals("0000")) {
                    if (jijinTradeRecordDTO.getStatus().equals(TradeRecordStatus.NOTIFY_SUCCESS.name()) || jijinTradeRecordDTO.getStatus().equals(TradeRecordStatus.NOTIFY_FAIL.name()) || jijinTradeRecordDTO.getStatus().equals(TradeRecordStatus.WITHDRAW_SUCCESS.name())) {
                        JijinUserBalanceDTO jijinUserBalanceDTO = jijinUserBalanceRepository.findUserBalanceByFundCode(jijinTradeRecordDTO.getUserId(), jijinTradeRecordDTO.getFundCode());
                        BigDecimal confirm = new BigDecimal("0");
                        int result = 0;
                        JijinFreezeRuleDTO jijinFreezeRuleDTO = null;
                        if (StringUtils.isNotEmpty(jijinTradeRecordDTO.getBusinessMode())) {
                            jijinFreezeRuleDTO = jijinFreezeRuleRepository.getJijinFreezeRuleByFundCodeAndMode(
                                    jijinTradeRecordDTO.getFundCode(), jijinTradeRecordDTO.getBusinessMode()
                            );
                        }
                        if (jijinFreezeRuleDTO == null) {
                            confirm = jijinUserBalanceDTO.getShareBalance().add(jijinTradeSyncDTO.getConfirmShare());
                            jijinUserBalanceDTO.setShareBalance(confirm);
                        } else {
                            confirm = BigDecimal.ZERO;

                            jijinUserBalanceDTO.setFrozenShare(jijinUserBalanceDTO.getFrozenShare().add(jijinTradeSyncDTO.getConfirmShare()));

                            //insert
                            boolean freezeResult = jijinFreezeService.freezeFund(jijinTradeRecordDTO, MapUtils.buildKeyValueMap(
                                    "trxConfirmDate", jijinTradeSyncDTO.getTrxConfirmDate(),
                                    "freezeShare", jijinTradeSyncDTO.getConfirmShare(),
                                    "freezeDay", jijinFreezeRuleDTO.getFreezeDay(),
                                    "userBalanceId", jijinUserBalanceDTO.getId()
                            ));
                            if (!freezeResult) {
                                Logger.error(this, String.format("NEED_MANUAL_HANDLE, jijin freeze fail.", jijinTradeRecordDTO.getAppNo()));
                                return;
                            }
                        }
                        BigDecimal applyingAmount = jijinUserBalanceDTO.getApplyingAmount().subtract(jijinTradeSyncDTO.getConfirmAmount());
                        jijinUserBalanceDTO.setApplyingAmount(applyingAmount);
                        result = jijinUserBalanceRepository.updateFundAccount(jijinUserBalanceDTO);

                        if (result == 1) {

                            //insert user balance history
                            JijinUserBalanceHistoryDTO jijinUserBalanceHistoryDTO = JijinUserBalenceHistoryDTOBuilder.buildUserBalanceHistoryDTO(jijinUserBalanceDTO, jijinTradeRecordDTO.getTrxDate(), confirm, new BigDecimal("0"), "转入申购份额", BalanceHistoryBizType.申购确认成功, jijinTradeRecordDTO.getId());
                            jijinUserBalanceHistoryRepository.insertBusJijinUserBalanceHistory(jijinUserBalanceHistoryDTO);
                            //WEBDEV-8355 基金(PC)赎回到账时间显示优化
                            String confirmDate = DateUtils.formatDateAddHMS(jijinTradeSyncDTO.getTrxConfirmDate());
                            if (jijinTradeRecordDTO.getReqAmount().compareTo(jijinTradeSyncDTO.getConfirmAmount()) != 0) {
                                jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "status", TradeRecordStatus.PARTIAL_SUCCESS.name(),"confirmDate",confirmDate));
                                PaymentIncreaseRequest request = new PaymentIncreaseRequest("JIJIN", String.valueOf(jijinTradeRecordDTO.getId()),
                             			jijinTradeRecordDTO.getUserId(), TransactionType.INCOME_PURCHASE_REFUND.name(),
                             			jijinTradeRecordDTO.getAppSheetNo(), jijinInfoDTO.getInstId(), 
                             			jijinTradeRecordDTO.getReqAmount().subtract(jijinTradeSyncDTO.getConfirmAmount()), jijinTradeRecordDTO.getTrxDate(),
                                         "申购部分失败退款：" + jijinInfoDTO.getFundName(),jijinTradeRecordDTO.getTrxDate(),            
                                         jijinInfoDTO.getProductCode(),jijinTradeRecordDTO.getAppSheetNo(),"JIJIN_PURCHASE_REFUND",
                                        "申购部分失败退款入账",jijinTradeRecordDTO.getAppSheetNo(),"JIJIN_PURCHASE_REFUND");
                                PaymentResult response = paymentAppService.increaseMoneyWithAudit(request);
                                if(!"SUCCESS".equals(response.getStatus())){
                                	Logger.error(this, String.format("NEED_MANUAL_HANDLE,申购失败退款入账失败, ,tradeRecordId [%s]", jijinTradeRecordDTO.id()));
                                }
                            } else {
                                jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "status", TradeRecordStatus.SUCCESS.name(),"confirmDate",confirmDate));
                                JijinTradeLogDTO jijinTradeLogDTO = new JijinTradeLogDTO(jijinTradeRecordDTO.getUserId(), jijinTradeRecordDTO.getFundCode(), jijinTradeRecordDTO.getId(), TradeRecordStatus.SUCCESS.name(), jijinTradeRecordDTO.getType(), jijinTradeRecordDTO.getReqAmount(), jijinTradeSyncDTO.getConfirmShare(), jijinTradeRecordDTO.getTrxTime(), jijinTradeRecordDTO.getTrxDate());
                                jijinTradeLogDTO.setFee(jijinTradeSyncDTO.getPurchaseFee());
                                jijinTradeLogDTO.setBusinessMode(jijinTradeRecordDTO.getBusinessMode());
                                jijinTradeLogDTO.setConfirmDate(confirmDate);
                                jijinTradeLogRepository.insertBusJijinTradeLog(jijinTradeLogDTO);
                            }

                            jijinTradeSyncRepository.updateJijinTradeSyncStatusById(jijinTradeSyncDTO.getId(), "DISPATCHED");

                            //send purchase success sms
                            UserInfoGson userInfoGson = userService.getUserInfo(jijinTradeRecordDTO.getUserId());
                            List<Map<String, String>> contentList = new ArrayList<Map<String, String>>();
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("realName", userInfoGson.getName());
                            params.put("money", jijinTradeSyncDTO.getConfirmAmount().toPlainString());
                            params.put("fundName", jijinInfoDTO.getFundName());
                            contentList.add(extInterfaceService.generateSmsContentMap(userInfoGson.getMobileNo(), SmsTemplate.JIJIN_PURCHASE_SUCCESS,
                                    params));

                            extInterfaceService.sendSms(contentList);
                            mqService.sendPurchaseResultMsg(jijinTradeRecordDTO.getTrxId(), "success", jijinTradeSyncDTO.getConfirmAmount());
                            Logger.info(this, String.format("dispatch purchase trade sync success,tradeSyncId [%s]", jijinTradeSyncDTO.getId()));
                        } else {
                            Logger.info(this, String.format("dispatch purchase trade sync failed,update jijin user balance failed userId [%s],tradeSyncId [%s]", jijinTradeRecordDTO.getUserId(), jijinTradeSyncDTO.getId()));
                        }
                        return;
                    }

                    //平安付付款成功操作
                    if (jijinTradeRecordDTO.getStatus().equals(TradeRecordStatus.SUBMIT_SUCCESS.name())) {
                        jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "isControversial", "1", "errorMsg", "平安付付款状态未知,交易确认文件成功，需要人工解冻调减虚拟户资金，调增applyingAmount"));
                        jijinTradeSyncRepository.updateJijinTradeSyncStatusById(jijinTradeSyncDTO.getId(), "ERROR");
                        return;
                    }
                    Logger.info(this, String.format("tradeSyncStatus is success, but trade record status is not in condition do nothing tradeRecordStatus [%s],recordId [%s] ", jijinTradeRecordDTO.getStatus(), jijinTradeRecordDTO.getId()));
                } else {
                    //申购对账失败的资金需要T+2日才能返回，这里只更新状态为未转账
                    if (jijinTradeRecordDTO.getStatus().equals(TradeRecordStatus.NOTIFY_SUCCESS.name()) || jijinTradeRecordDTO.getStatus().equals(TradeRecordStatus.NOTIFY_FAIL.name()) || jijinTradeRecordDTO.getStatus().equals(TradeRecordStatus.WITHDRAW_SUCCESS.name())) {
                        jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "status", TradeRecordStatus.UNTRANSFER_MONEY.name()));
                        jijinTradeSyncRepository.updateJijinTradeSyncStatusById(jijinTradeSyncDTO.getId(), "DISPATCHED");
                        
                    	PaymentIncreaseRequest request = new PaymentIncreaseRequest("JIJIN", String.valueOf(jijinTradeRecordDTO.getId()),
                     			jijinTradeRecordDTO.getUserId(), TransactionType.INCOME_PURCHASE_REFUND.name(),
                     			jijinTradeRecordDTO.getAppSheetNo(), jijinInfoDTO.getInstId(), 
                     			jijinTradeRecordDTO.getReqAmount(), jijinTradeRecordDTO.getTrxDate(),
                                 "申购失败退款：" + jijinInfoDTO.getFundName(),jijinTradeRecordDTO.getTrxDate(),            
                                 jijinInfoDTO.getProductCode(),jijinTradeRecordDTO.getAppSheetNo(),"JIJIN_PURCHASE_REFUND",
                                "申购失败退款入账",jijinTradeRecordDTO.getAppSheetNo(),"JIJIN_PURCHASE_REFUND");
                        PaymentResult response = paymentAppService.increaseMoneyWithAudit(request);
                        if(!"SUCCESS".equals(response.getStatus())){
                        	Logger.error(this, String.format("NEED_MANUAL_HANDLE,申购失败退款入账失败, ,tradeRecordId [%s]", jijinTradeRecordDTO.id()));
                        }
                        return;
                    }

                    //调用平安付超时,对账文件返回失败需要解冻并且发送投资失败MQ
                    if (jijinTradeRecordDTO.getStatus().equals(TradeRecordStatus.SUBMIT_SUCCESS.name())) {
                        String remark = "申购：" + jijinInfoDTO.getFundName();
                        
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
                        jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "status", TradeRecordStatus.FAIL.name()));
                        jijinTradeSyncRepository.updateJijinTradeSyncStatusById(jijinTradeSyncDTO.getId(), "DISPATCHED");
                        mqService.sendPurchaseResultMsg(jijinTradeRecordDTO.getTrxId(), "fail", BigDecimal.ZERO);
                        return;
                    }
                    Logger.info(this, String.format("tradeSyncStatus is fail, but trade record status is not in condition do nothing tradeRecordStatus [%s],recordId [%s] ", jijinTradeRecordDTO.getStatus(), jijinTradeRecordDTO.getId()));
                }      
            }
        }
    }

    /**
     * 基金公司赎回交易确认文件 对账
     *
     * @param jijinTradeSyncDTO
     */
    @Transactional
    public void dispatchRedeemTradeSync(JijinTradeSyncDTO jijinTradeSyncDTO) {
        String appSheetNo = jijinTradeSyncDTO.getAppSheetNo();
        JijinInfoDTO jijinInfoDTO = jijinInfoRepository.findJijinInfoByFundCode(jijinTradeSyncDTO.getFundCode());
        if (null == jijinInfoDTO) {
            Logger.warn(this, String.format("jijin redeem recon: can not find jijin info with sync file's appSheetNo [%s]", appSheetNo));
            jijinTradeSyncRepository.updateJijinTradeSyncStatusById(jijinTradeSyncDTO.getId(), "ERROR");
            return;
        }
        JijinTradeRecordDTO jijinTradeRecordDTO = jijinTradeRecordRepository.getRecordByAppSheetNoAndInstId(appSheetNo, jijinInfoDTO.getInstId());

        if (null == jijinTradeRecordDTO) {
            Logger.warn(this, String.format("jijin redeem recon: can not find jijin trade record with sync file's appSheetNo [%s]", appSheetNo));
            jijinTradeSyncRepository.updateJijinTradeSyncStatusById(jijinTradeSyncDTO.getId(), "UNMATCH");
            return;
        }

        //转购的赎回对账由申购对账一起处理,不单独处理
        if (StringUtils.isNotEmpty(jijinTradeRecordDTO.getSourceFundCode())) {
            Logger.warn(this, String.format("[zhuangou] jijin redeem do nothing ,purchase sync will dispatch, app no [%s]", jijinTradeRecordDTO.getAppNo()));
            jijinTradeSyncRepository.updateJijinTradeSyncStatusById(jijinTradeSyncDTO.getId(), "DISPATCHED");
            return;
        }

        // paf的赎回资金确认文件先回来，状态已变成success，不做任何处理
        if (jijinTradeRecordDTO.getStatus().equals(TradeRecordStatus.SUCCESS.name())) {
            Logger.warn(this, String.format("jijin redeem recon: 平安付代付文件比基金公司交易确认文件先回来了， appSheetNo [%s]", appSheetNo));
            jijinTradeSyncRepository.updateJijinTradeSyncStatusById(jijinTradeSyncDTO.getId(), "DISPATCHED");
            return;
        }

        if (jijinTradeSyncDTO.getTradeResCode().equals("0000")) { // 交易确认文件 - 赎回确认为成功

            //撤单成功的记录
            if (jijinTradeRecordDTO.getStatus().equals(TradeRecordStatus.CANCEL_SUCCESS.name())) {
                Logger.error(this, String.format("实时撤单成功,但交易确认文件返回赎回成功，需要人工核查，tradeRecordId [%s]", jijinTradeRecordDTO.getId()));
                jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "isControversial", "1", "errorMsg", "实时撤单成功,但交易确认文件返回赎回成功，需要人工核查"));
                jijinTradeSyncRepository.updateJijinTradeSyncStatusById(jijinTradeSyncDTO.getId(), "DISPATCHED");
                return;
            }

            //提交成功的记录， 翻转状态到waiting-money，后续资金到帐后再翻转trade record到终态success
            if (jijinTradeRecordDTO.getStatus().equals(TradeRecordStatus.SUBMIT_SUCCESS.name())) {

                BigDecimal amount = jijinTradeRecordDTO.getReqShare() == null ? jijinTradeRecordDTO.getReqAmount() : jijinTradeRecordDTO.getReqShare();

                if (amount.compareTo(jijinTradeSyncDTO.getRedeemShare()) == 0) { //对账成功
                    //WEBDEV-8355 基金(PC)赎回到账时间显示优化
                    String confirmDate = DateUtils.formatDateAddHMS(jijinTradeSyncDTO.getTrxConfirmDate());
                    jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "status", TradeRecordStatus.WAITING_MONEY.name(), "flag", "1","confirmDate",confirmDate));

                 	PaymentIncreaseRequest request = new PaymentIncreaseRequest("JIJIN", String.valueOf(jijinTradeRecordDTO.getId()),
                            jijinTradeRecordDTO.getUserId(), TransactionType.INCOME_REDEMPTION.name(),
                            jijinTradeRecordDTO.getAppSheetNo(), jijinInfoDTO.getInstId(), 
                            jijinTradeSyncDTO.getConfirmAmount(), jijinTradeRecordDTO.getTrxDate(),
                             "赎回：" + jijinInfoDTO.getFundName(),jijinTradeRecordDTO.getTrxDate(),            
                            jijinInfoDTO.getProductCode(),jijinTradeRecordDTO.getAppSheetNo(),"JIJIN_REDEEM",
                            "赎回入账", String.valueOf(jijinTradeRecordDTO.getId()),"JIJIN_REDEEM");
                    PaymentResult response = paymentAppService.increaseMoneyWithAudit(request);
                    
                    if (!"SUCCESS".equals(response.getStatus()) ){
                        Logger.error(this, String.format("NEED_MANUAL_HANDLE,call account to do redeem audit failed,tradeRecordId [%s]", jijinTradeRecordDTO.id()));
                    }

                } else {//对账失败
                    jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "isControversial", "1", "errorMsg", "对账失败,提交份额和确认份额不一致"));
                }
                jijinTradeSyncRepository.updateJijinTradeSyncStatusById(jijinTradeSyncDTO.getId(), "DISPATCHED");
                return;

            }

            //提交失败，交易确认为成功，需要人工干预
            if (jijinTradeRecordDTO.getStatus().equals(TradeRecordStatus.SUBMIT_FAIL.name())) {
                jijinTradeSyncRepository.updateJijinTradeSyncStatusById(jijinTradeSyncDTO.getId(), "DISPATCHED");
                jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "isControversial", "1", "errorMsg", "对账失败,提交赎回申请失败，但是交易确认返回成功,需要人工核查"));

                return;
            }

        } else { // 交易确认文件 - 赎回确认为失败

            //撤单成功的记录
            if (jijinTradeRecordDTO.getStatus().equals(TradeRecordStatus.CANCEL_SUCCESS.name())) {
                Logger.warn(this, String.format("trade record has already cancel,need do nothing.tradeRecordId [%s]", jijinTradeRecordDTO.getId()));
                jijinTradeSyncRepository.updateJijinTradeSyncStatusById(jijinTradeSyncDTO.getId(), "DISPATCHED");
                return;
            }


            //提交成功的记录 - 交易确认返回失败
            if (jijinTradeRecordDTO.getStatus().equals(TradeRecordStatus.SUBMIT_SUCCESS.name())) {

                //解冻并返还份额/金额，更新trade record 状态为FAIL, 插 trade log
                BigDecimal amount = jijinTradeRecordDTO.getReqShare() == null ? jijinTradeRecordDTO.getReqAmount() : jijinTradeRecordDTO.getReqShare();
                JijinUserBalanceDTO userBalance = jijinUserBalanceRepository.findUserBalanceByFundCode(jijinTradeRecordDTO.getUserId(), jijinTradeRecordDTO.getFundCode());

                userBalance.setShareBalance(userBalance.getShareBalance().add(amount));
                userBalance.setFrozenShare(userBalance.getFrozenShare().subtract(amount));

                int result = jijinUserBalanceRepository.updateFundAccount(userBalance);

                if (result == 1) {
                    jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "status", TradeRecordStatus.FAIL.name()));

                    //insert user balance history
                    JijinUserBalanceHistoryDTO jijinUserBalanceHistoryDTO = JijinUserBalenceHistoryDTOBuilder.buildUserBalanceHistoryDTO(userBalance, jijinTradeRecordDTO.getTrxDate(), amount, new BigDecimal("0"), "赎回确认失败，退还冻结份额", BalanceHistoryBizType.赎回确认失败, jijinTradeRecordDTO.getId());
                    jijinUserBalanceHistoryRepository.insertBusJijinUserBalanceHistory(jijinUserBalanceHistoryDTO);
                    jijinTradeLogRepository.insertBusJijinTradeLog(new JijinTradeLogDTO(jijinTradeRecordDTO.getUserId(), jijinTradeRecordDTO.getFundCode(), jijinTradeRecordDTO.getId(), TradeRecordStatus.FAIL.name(), TradeRecordType.REDEEM, null, amount, jijinTradeRecordDTO.getTrxTime(), jijinTradeRecordDTO.getTrxDate()));

                    jijinTradeSyncRepository.updateJijinTradeSyncStatusById(jijinTradeSyncDTO.getId(), "DISPATCHED");
                }
                return;
            }

            //提交失败，交易确认为失败，按道理不会有交易确认记录，现在处理成什么都不做
            if (jijinTradeRecordDTO.getStatus().equals(TradeRecordStatus.SUBMIT_FAIL.name())) {
                jijinTradeSyncRepository.updateJijinTradeSyncStatusById(jijinTradeSyncDTO.getId(), "DISPATCHED");
                Logger.info(this, String.format("tradeSyncStatus is fail, but trade record status is not in condition do nothing tradeRecordStatus [%s],recordId [%s] ", jijinTradeRecordDTO.getStatus(), jijinTradeRecordDTO.getId()));
                return;
            }

        }

    }

    /**
     * 强增
     *
     * @param jijinTradeSyncDTO
     */
    @Transactional
    public void dispatchForceIncrease(JijinTradeSyncDTO jijinTradeSyncDTO) {
        if (!jijinTradeSyncDTO.getTradeResCode().equals("0000")) {
            Logger.info(this, String.format("[force increase] trade sync res code is not success ,id [%s] ", jijinTradeSyncDTO.getId()));
            jijinTradeSyncRepository.updateJijinTradeSyncStatusAndMemoById(jijinTradeSyncDTO.getId(), "ERROR", "强制调增返回结果失败，数据异常");
            return;
        }

        JijinInfoDTO jijinInfoDTO = jijinInfoRepository.findJijinInfoByFundCode(jijinTradeSyncDTO.getFundCode());
        if (null == jijinInfoDTO) {
            Logger.info(this, String.format("[force increase] can not find jijinInfo ,fundCode [%s] ", jijinTradeSyncDTO.getFundCode()));
            jijinTradeSyncRepository.updateJijinTradeSyncStatusAndMemoById(jijinTradeSyncDTO.getId(), "ERROR", "强制调增找不到基金信息");
            return;
        }
        JijinAccountDTO jijinAccountDTO = jijinAccountRepository.findBusJijinAccount(MapUtils.buildKeyValueMap("instId", jijinInfoDTO.getInstId(), "contractNo", jijinTradeSyncDTO.getContractNo(), "channel", "PAF", "deleted", "0"));
        if (null == jijinAccountDTO) {
            Logger.info(this, String.format("[force increase] can not find jijinAccount ,jijinTradeSyncId [%s] ", jijinTradeSyncDTO.getId()));
            jijinTradeSyncRepository.updateJijinTradeSyncStatusAndMemoById(jijinTradeSyncDTO.getId(), "ERROR", "强制调增找不到开户信息");
            return;
        }

        JijinUserBalanceDTO jijinUserBalanceDTO = jijinUserBalanceRepository.findUserBalanceByFundCode(jijinAccountDTO.getUserId(), jijinTradeSyncDTO.getFundCode());
        try {
            if (null == jijinUserBalanceDTO) {
                jijinUserBalanceDTO = new JijinUserBalanceDTO();
                jijinUserBalanceDTO.setUserId(jijinAccountDTO.getUserId());
                jijinUserBalanceDTO.setFundCode(jijinTradeSyncDTO.getFundCode());
                jijinUserBalanceDTO.setDividendType(jijinTradeSyncDTO.getDividentType());
                jijinUserBalanceDTO.setDividendStatus("DONE");
                jijinUserBalanceDTO.setShareBalance(jijinTradeSyncDTO.getConfirmShare());
                jijinUserBalanceDTO.setFrozenAmount(new BigDecimal("0.00"));
                jijinUserBalanceDTO.setFrozenShare(new BigDecimal("0.00"));
                jijinUserBalanceDTO.setApplyingAmount(new BigDecimal("0.00"));
                jijinUserBalanceRepository.insertBusJijinUserBalance(jijinUserBalanceDTO);
            } else {
                jijinUserBalanceDTO.setShareBalance(jijinUserBalanceDTO.getShareBalance().add(jijinTradeSyncDTO.getConfirmShare()));
                int result = jijinUserBalanceRepository.updateFundAccount(jijinUserBalanceDTO);
                if (result < 1) {
                    Logger.info(this, String.format("[force increase] update user balance failed ,jijinTradeSyncId [%s]", jijinTradeSyncDTO.getId()));
                    return;
                }
            }


            JijinTradeRecordDTO jijinTradeRecordDTO = new JijinTradeRecordDTO();
            jijinTradeRecordDTO.setInstId(jijinInfoDTO.getInstId());
            jijinTradeRecordDTO.setUserId(jijinAccountDTO.getUserId());
            jijinTradeRecordDTO.setFundCode(jijinTradeSyncDTO.getFundCode());
            jijinTradeRecordDTO.setStatus(TradeRecordStatus.SUCCESS.name());
            jijinTradeRecordDTO.setType(TradeRecordType.FORCE_INCREASE);
            jijinTradeRecordDTO.setReqShare(jijinTradeSyncDTO.getConfirmShare());
            jijinTradeRecordDTO.setContractNo(jijinAccountDTO.getContractNo());
            jijinTradeRecordDTO.setAppNo(jijinTradeSyncDTO.getLufaxRequestNo());
            jijinTradeRecordDTO.setAppSheetNo(jijinTradeSyncDTO.getAppSheetNo());
            jijinTradeRecordDTO.setChannel("PAF");
            jijinTradeRecordDTO.setDividendType(jijinTradeSyncDTO.getDividentType());
            jijinTradeRecordDTO.setChargeType(jijinTradeSyncDTO.getChargeType());
            jijinTradeRecordDTO.setTrxDate(jijinTradeSyncDTO.getTrxDate());
            jijinTradeRecordDTO.setTrxTime(jijinTradeSyncDTO.getTrxTime());
            jijinTradeRecordDTO.setRetryTimes(0L);
            jijinTradeRecordDTO.setIsControversial("0");
            jijinTradeRecordDTO.setFlag("1");
            jijinTradeRecordDTO.setErrorMsg("强制调增");

            jijinTradeRecordDTO.setUkToken("JIJINFI" + jijinTradeSyncDTO.getAppSheetNo());
            //jijinTradeRecordDTO.setTrxId(Long.valueOf(jijinTradeSyncDTO.getAppSheetNo()));

            JijinTradeRecordDTO record = jijinTradeRecordRepository.insertJijinTradeRecord(jijinTradeRecordDTO);

            //先插入tradeRecord,再写balanceHistory
            JijinUserBalanceHistoryDTO jijinUserBalanceHistoryDTO = JijinUserBalenceHistoryDTOBuilder.buildUserBalanceHistoryDTO(jijinUserBalanceDTO, jijinTradeSyncDTO.getTrxDate(), jijinTradeSyncDTO.getConfirmShare(), new BigDecimal("0"), "强制调增", BalanceHistoryBizType.强制调增, record.getId());
            jijinUserBalanceHistoryRepository.insertBusJijinUserBalanceHistory(jijinUserBalanceHistoryDTO);

            JijinTradeLogDTO jijinTradeLogDTO = new JijinTradeLogDTO();
            jijinTradeLogDTO.setUserId(jijinAccountDTO.getUserId());
            jijinTradeLogDTO.setFundCode(jijinTradeSyncDTO.getFundCode());
            jijinTradeLogDTO.setTradeRecordId(record.getId());
            jijinTradeLogDTO.setStatus(TradeRecordStatus.SUCCESS.name());
            jijinTradeLogDTO.setDividendType(jijinTradeSyncDTO.getDividentType());
            jijinTradeLogDTO.setType(TradeRecordType.FORCE_INCREASE);
            jijinTradeLogDTO.setReqShare(jijinTradeSyncDTO.getConfirmShare());
            jijinTradeLogDTO.setTrxSerial(jijinTradeSyncDTO.getLufaxRequestNo());
            jijinTradeLogDTO.setFee(jijinTradeSyncDTO.getPurchaseFee());
            jijinTradeLogDTO.setAmount(jijinTradeSyncDTO.getConfirmShare().multiply(jijinTradeSyncDTO.getNetValue()).setScale(2, BigDecimal.ROUND_DOWN));
            jijinTradeLogRepository.insertBusJijinTradeLog(jijinTradeLogDTO);

            jijinTradeSyncRepository.updateJijinTradeSyncStatusById(jijinTradeSyncDTO.getId(), "DISPATCHED");

        } catch (Exception e) {
            Logger.error(this, e.getMessage());
            return;
        }
    }

    /**
     * 强减
     *
     * @param jijinTradeSyncDTO
     */
    @Transactional
    public void dispatchForceReduced(JijinTradeSyncDTO jijinTradeSyncDTO) {
        if (!jijinTradeSyncDTO.getTradeResCode().equals("0000")) {
            Logger.info(this, String.format("[force reduced] trade sync res code is not success ,id [%s] ", jijinTradeSyncDTO.getId()));
            jijinTradeSyncRepository.updateJijinTradeSyncStatusAndMemoById(jijinTradeSyncDTO.getId(), "ERROR", "强制调减返回结果失败");
            return;
        }

        JijinInfoDTO jijinInfoDTO = jijinInfoRepository.findJijinInfoByFundCode(jijinTradeSyncDTO.getFundCode());
        if (null == jijinInfoDTO) {
            Logger.info(this, String.format("[force reduced] can not find jijinInfo ,fundCode [%s] ", jijinTradeSyncDTO.getFundCode()));
            jijinTradeSyncRepository.updateJijinTradeSyncStatusAndMemoById(jijinTradeSyncDTO.getId(), "ERROR", "强制调减找不到基金信息");
            return;
        }
        JijinAccountDTO jijinAccountDTO = jijinAccountRepository.findBusJijinAccount(MapUtils.buildKeyValueMap("instId", jijinInfoDTO.getInstId(), "contractNo", jijinTradeSyncDTO.getContractNo(), "channel", "PAF", "deleted", "0"));
        if (null == jijinAccountDTO) {
            Logger.info(this, String.format("[force reduced] can not find jijinAccount ,jijinTradeSyncId [%s] ", jijinTradeSyncDTO.getId()));
            jijinTradeSyncRepository.updateJijinTradeSyncStatusAndMemoById(jijinTradeSyncDTO.getId(), "ERROR", "强制调减找不到开户信息");
            return;
        }

        JijinUserBalanceDTO jijinUserBalanceDTO = jijinUserBalanceRepository.findUserBalanceByFundCode(jijinAccountDTO.getUserId(), jijinTradeSyncDTO.getFundCode());
        if (null == jijinUserBalanceDTO) {
            Logger.info(this, String.format("[force reduced] can not find jijinUserBalance ,jijinTradeSyncId [%s] ", jijinTradeSyncDTO.getId()));
            jijinTradeSyncRepository.updateJijinTradeSyncStatusAndMemoById(jijinTradeSyncDTO.getId(), "ERROR", "强制调减找不到用户资金账户");
            return;
        }

        if (jijinUserBalanceDTO.getShareBalance().compareTo(jijinTradeSyncDTO.getConfirmShare()) < 0) {
            Logger.info(this, String.format("[force reduced] share balance is less than need reduced ,jijinTradeSyncId [%s] ", jijinTradeSyncDTO.getId()));
            jijinTradeSyncRepository.updateJijinTradeSyncStatusAndMemoById(jijinTradeSyncDTO.getId(), "ERROR", "强制调减账户余额小于调减金额");
            return;
        }

        jijinUserBalanceDTO.setShareBalance(jijinUserBalanceDTO.getShareBalance().subtract(jijinTradeSyncDTO.getConfirmShare()));
        int result = jijinUserBalanceRepository.updateFundAccount(jijinUserBalanceDTO);
        if (result < 1) {
            Logger.info(this, String.format("[force reduced] update user balance failed ,jijinTradeSyncId [%s]", jijinTradeSyncDTO.getId()));
            return;
        }


        JijinTradeRecordDTO jijinTradeRecordDTO = new JijinTradeRecordDTO();
        jijinTradeRecordDTO.setInstId(jijinInfoDTO.getInstId());
        jijinTradeRecordDTO.setUserId(jijinAccountDTO.getUserId());
        jijinTradeRecordDTO.setFundCode(jijinTradeSyncDTO.getFundCode());
        jijinTradeRecordDTO.setStatus(TradeRecordStatus.SUCCESS.name());
        jijinTradeRecordDTO.setType(TradeRecordType.FORCE_REDUCED);
        jijinTradeRecordDTO.setReqShare(jijinTradeSyncDTO.getConfirmShare());
        jijinTradeRecordDTO.setContractNo(jijinAccountDTO.getContractNo());
        jijinTradeRecordDTO.setAppNo(jijinTradeSyncDTO.getLufaxRequestNo());
        jijinTradeRecordDTO.setAppSheetNo(jijinTradeSyncDTO.getAppSheetNo());
        jijinTradeRecordDTO.setChannel("PAF");
        jijinTradeRecordDTO.setDividendType(jijinTradeSyncDTO.getDividentType());
        jijinTradeRecordDTO.setChargeType(jijinTradeSyncDTO.getChargeType());
        jijinTradeRecordDTO.setRetryTimes(0L);
        jijinTradeRecordDTO.setIsControversial("0");
        jijinTradeRecordDTO.setFlag("1");
        jijinTradeRecordDTO.setErrorMsg("强制调减");
        jijinTradeRecordDTO.setTrxDate(jijinTradeSyncDTO.getTrxDate());
        jijinTradeRecordDTO.setTrxTime(jijinTradeSyncDTO.getTrxTime());
        jijinTradeRecordDTO.setUkToken("JIJINFR" + jijinTradeSyncDTO.getAppSheetNo());
        //jijinTradeRecordDTO.setTrxId(Long.valueOf(jijinTradeSyncDTO.getAppSheetNo()));
        JijinTradeRecordDTO record = jijinTradeRecordRepository.insertJijinTradeRecord(jijinTradeRecordDTO);

        JijinUserBalanceHistoryDTO jijinUserBalanceHistoryDTO = JijinUserBalenceHistoryDTOBuilder.buildUserBalanceHistoryDTO(jijinUserBalanceDTO, jijinTradeSyncDTO.getTrxDate(), new BigDecimal("0"), jijinTradeSyncDTO.getConfirmShare(), "强制调减", BalanceHistoryBizType.强制调减, record.getId());
        jijinUserBalanceHistoryRepository.insertBusJijinUserBalanceHistory(jijinUserBalanceHistoryDTO);


        JijinTradeLogDTO jijinTradeLogDTO = new JijinTradeLogDTO();
        jijinTradeLogDTO.setUserId(jijinAccountDTO.getUserId());
        jijinTradeLogDTO.setFundCode(jijinTradeSyncDTO.getFundCode());
        jijinTradeLogDTO.setTradeRecordId(record.getId());
        jijinTradeLogDTO.setStatus(TradeRecordStatus.SUCCESS.name());
        jijinTradeLogDTO.setDividendType(jijinTradeSyncDTO.getDividentType());
        jijinTradeLogDTO.setType(TradeRecordType.FORCE_REDUCED);
        jijinTradeLogDTO.setReqShare(jijinTradeSyncDTO.getConfirmShare());
        jijinTradeLogDTO.setTrxSerial(jijinTradeSyncDTO.getLufaxRequestNo());
        jijinTradeLogDTO.setFee(jijinTradeSyncDTO.getPurchaseFee());
        jijinTradeLogDTO.setAmount(jijinTradeSyncDTO.getConfirmShare().multiply(jijinTradeSyncDTO.getNetValue()).setScale(2, BigDecimal.ROUND_DOWN));
        jijinTradeLogRepository.insertBusJijinTradeLog(jijinTradeLogDTO);
        jijinTradeSyncRepository.updateJijinTradeSyncStatusById(jijinTradeSyncDTO.getId(), "DISPATCHED");
    }

    /**
     * 强制赎回
     *
     * @param jijinTradeSyncDTO
     */
    @Transactional
    public void dispatchForceRedeem(JijinTradeSyncDTO jijinTradeSyncDTO) {
        if ("0000".equals(jijinTradeSyncDTO.getTradeResCode())) {
            JijinInfoDTO jijinInfo = jijinInfoRepository.findJijinInfoByFundCode(jijinTradeSyncDTO.getFundCode());
            if (null == jijinInfo) {
                Logger.error(this, String.format("froce redeem is error ，fundCode [%s] is not in bus_jijin_info", jijinTradeSyncDTO.getFundCode()));
                jijinTradeSyncRepository.updateJijinTradeSyncStatusAndMemoById(jijinTradeSyncDTO.getId(), "ERROR", "强制赎回找不到基金信息");
                return;
            }
            JijinAccountDTO accountDto = jijinAccountRepository.findJijinAccountByInstIdAndContractNo(jijinInfo.getInstId(), jijinTradeSyncDTO.getContractNo());
            if (null == accountDto) {
                Logger.error(this, String.format("froce redeem is error ，fundCode = [%s] tradeSyncId = [%s],there is no jijin account with contractNo[%s]", jijinTradeSyncDTO.getFundCode(), jijinTradeSyncDTO.getId(), jijinTradeSyncDTO.getContractNo()));
                jijinTradeSyncRepository.updateJijinTradeSyncStatusAndMemoById(jijinTradeSyncDTO.getId(), "ERROR", "强制赎回找不到开户信息");
                return;
            }
            JijinUserBalanceDTO balance = jijinUserBalanceRepository.findUserBalanceByFundCode(accountDto.getUserId(), jijinTradeSyncDTO.getFundCode());
            if (null == balance) {
                Logger.error(this, String.format("froce redeem is error ，fundCode = [%s] tradeSyncId = [%s],there is no jijin balance", jijinTradeSyncDTO.getFundCode(), jijinTradeSyncDTO.getId()));
                jijinTradeSyncRepository.updateJijinTradeSyncStatusAndMemoById(jijinTradeSyncDTO.getId(), "ERROR", "强制赎回找不到用户资金帐户");
                return;
            }

            if (balance.getShareBalance().compareTo(jijinTradeSyncDTO.getConfirmShare()) < 0) {
                //强赎份额不够，则重试
                Logger.error(this, String.format("froce redeem is error ，fundCode = [%s] tradeSyncId = [%s],the userbalance is less than redeemShare", jijinTradeSyncDTO.getFundCode(), jijinTradeSyncDTO.getId()));
                jijinTradeSyncRepository.updateJijinTradeSyncStatusAndMemoById(jijinTradeSyncDTO.getId(), "REDO", "强制赎回份额大于持有份额");
                return;
            }

            balance.setShareBalance(balance.getShareBalance().subtract(jijinTradeSyncDTO.getConfirmShare()));
            balance.setFrozenShare(balance.getFrozenShare().add(jijinTradeSyncDTO.getConfirmShare()));
            int i = jijinUserBalanceRepository.updateFundAccount(balance);
            if (i != 1) {
                //冻结份额失败，不作处理，下次job触发时再处理
                return;
            }
            //插入tradeRecord
            JijinTradeRecordDTO dto = buildForceRedeemTradeRecord(jijinTradeSyncDTO, accountDto, balance);
            dto = jijinTradeRecordRepository.insertJijinTradeRecord(dto);
            JijinUserBalanceHistoryDTO jijinUserBalanceHistoryDTO = JijinUserBalenceHistoryDTOBuilder.buildUserBalanceHistoryDTO(balance, jijinTradeSyncDTO.getTrxDate(), new BigDecimal("0"), jijinTradeSyncDTO.getRedeemShare(), "强制赎回，冻结份额", BalanceHistoryBizType.强制赎回, dto.getId());
            jijinUserBalanceHistoryRepository.insertBusJijinUserBalanceHistory(jijinUserBalanceHistoryDTO);
            //清销系统入账
//            RedeemAuditRequestGson redeemAudit = new RedeemAuditRequestGson("JIJIN", String.valueOf(dto.getId()),
//                    dto.getUserId(), TransactionType.INCOME_FORCE_REDEEM.name(), dto.getAppSheetNo(),
//                    jijinInfo.getInstId(), jijinTradeSyncDTO.getConfirmAmount(), dto.getTrxDate(), jijinInfo.getProductCode(), "强制赎回：" + jijinInfo.getFundName());
//            boolean res = callAccountRedeemAudit(redeemAudit);
            
         	PaymentIncreaseRequest request = new PaymentIncreaseRequest("JIJIN", String.valueOf(dto.getId()),
         			dto.getUserId(), TransactionType.INCOME_REDEMPTION.name(),
         			dto.getAppSheetNo(), jijinInfo.getInstId(), 
                    jijinTradeSyncDTO.getConfirmAmount(), dto.getTrxDate(),
                     "赎回：" + jijinInfo.getFundName(),dto.getTrxDate(),            
                     jijinInfo.getProductCode(),dto.getAppSheetNo(),"JIJIN_REDEEM",
                    "强赎入账", String.valueOf(dto.getId()),"JIJIN_REDEEM");
            PaymentResult response = paymentAppService.increaseMoneyWithAudit(request);
            
            if (!"SUCCESS".equals(response.getStatus())) {
                Logger.error(this, String.format("NEED_MANUAL_HANDLE,call account to do redeem audit failed,tradeRecordId [%s]", dto.id()));
            }
            jijinTradeSyncRepository.updateJijinTradeSyncStatusById(jijinTradeSyncDTO.getId(), "DISPATCHED");

            //send MQ to trigger pay
            mqService.sendForceRedemmPayMsg(dto.getId());

        } else {
            jijinTradeSyncRepository.updateJijinTradeSyncStatusAndMemoById(jijinTradeSyncDTO.getId(), "ERROR", "强制赎回返回结果失败");
        }
    }

    public void setAccountAppCallerService(
            AccountAppCallerService accountAppCallerService) {
        this.accountAppCallerService = accountAppCallerService;
    }

    private JijinTradeRecordDTO buildForceRedeemTradeRecord(JijinTradeSyncDTO jijinTradeSyncDTO, JijinAccountDTO account, JijinUserBalanceDTO balance) {
        JijinTradeRecordDTO dto = new JijinTradeRecordDTO();
        String appNo = sequenceService.getSerialNumber(JijinBizType.FORCE_REDEEM.getCode());
        dto.setAppNo(appNo);
        dto.setChannel(account.getChannel());
        dto.setUserId(account.getUserId());
        dto.setFundCode(balance.getFundCode());
        dto.setStatus(TradeRecordStatus.WAITING_MONEY.name());
        dto.setType(TradeRecordType.FORCE_REDEEM);
        //强赎的申请份额直接使用tradeSync的确认份额
        dto.setReqShare(jijinTradeSyncDTO.getConfirmShare());
        dto.setContractNo(account.getContractNo());
        dto.setAppNo(appNo);
        dto.setChargeType("A");
        dto.setInstId(account.getInstId());
        //以下字段只有在jijin response is success，才需要填充
        dto.setAppSheetNo(jijinTradeSyncDTO.getAppSheetNo());
        dto.setTrxDate(jijinTradeSyncDTO.getTrxDate());
        dto.setTrxTime(jijinTradeSyncDTO.getTrxTime());
        dto.setErrorCode("0000");
        dto.setErrorMsg("强制赎回");
        dto.setIsControversial("0");
        dto.setFlag("1");//走入销销帐模式
        //dto.setTrxId(Long.valueOf(jijinTradeSyncDTO.getAppSheetNo()));
        dto.setUkToken("JIJINFRD" + jijinTradeSyncDTO.getAppSheetNo());
        return dto;
    }
}
