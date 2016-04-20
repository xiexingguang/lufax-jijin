package com.lufax.jijin.fundation.service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.lufax.jijin.base.repository.BizParametersRepository;
import com.lufax.jijin.base.service.TradeDayService;
import com.lufax.jijin.base.utils.DateUtils;
import com.lufax.jijin.base.utils.FileUtils;
import com.lufax.jijin.base.utils.JsonHelper;
import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.daixiao.repository.JijinExCharacterRepository;
import com.lufax.jijin.fundation.constant.BalanceHistoryBizType;
import com.lufax.jijin.fundation.constant.FundSaleCode;
import com.lufax.jijin.fundation.constant.JijinBizType;
import com.lufax.jijin.fundation.constant.JijinConstants;
import com.lufax.jijin.fundation.constant.RedeemTypeEnum;
import com.lufax.jijin.fundation.constant.ResourceResponseCode;
import com.lufax.jijin.fundation.constant.SyncFileStatus;
import com.lufax.jijin.fundation.constant.TradeRecordStatus;
import com.lufax.jijin.fundation.constant.TradeRecordType;
import com.lufax.jijin.fundation.constant.TransactionType;
import com.lufax.jijin.fundation.constant.TransferTypeEnum;
import com.lufax.jijin.fundation.constant.YebTransactionType;
import com.lufax.jijin.fundation.dto.JijinAccountDTO;
import com.lufax.jijin.fundation.dto.JijinFrozenDetailDTO;
import com.lufax.jijin.fundation.dto.JijinInfoDTO;
import com.lufax.jijin.fundation.dto.JijinRedeemBalHisDTO;
import com.lufax.jijin.fundation.dto.JijinSyncFileDTO;
import com.lufax.jijin.fundation.dto.JijinTradeLogDTO;
import com.lufax.jijin.fundation.dto.JijinTradeRecordCountDTO;
import com.lufax.jijin.fundation.dto.JijinTradeRecordDTO;
import com.lufax.jijin.fundation.dto.JijinUserBalanceDTO;
import com.lufax.jijin.fundation.dto.JijinUserBalanceHistoryDTO;
import com.lufax.jijin.fundation.dto.builder.JijinUserBalenceHistoryDTOBuilder;
import com.lufax.jijin.fundation.gson.PaymentWithdrawResultGson;
import com.lufax.jijin.fundation.gson.RedeemResultGson;
import com.lufax.jijin.fundation.gson.YebRedeemGson;
import com.lufax.jijin.fundation.remote.AccountAppCallerService;
import com.lufax.jijin.fundation.remote.JijinGatewayRemoteService;
import com.lufax.jijin.fundation.remote.gson.request.GWRedeemRequestGson;
import com.lufax.jijin.fundation.remote.gson.response.GWRedeemResponseGson;
import com.lufax.jijin.fundation.remote.gson.response.GWResponseCode;
import com.lufax.jijin.fundation.remote.gson.response.GWResponseGson;
import com.lufax.jijin.fundation.repository.JijinAccountRepository;
import com.lufax.jijin.fundation.repository.JijinFrozenDetailRepository;
import com.lufax.jijin.fundation.repository.JijinInfoRepository;
import com.lufax.jijin.fundation.repository.JijinRedeemBalHisRepository;
import com.lufax.jijin.fundation.repository.JijinSequenceRepository;
import com.lufax.jijin.fundation.repository.JijinSyncFileRepository;
import com.lufax.jijin.fundation.repository.JijinTradeLogRepository;
import com.lufax.jijin.fundation.repository.JijinTradeRecordRepository;
import com.lufax.jijin.fundation.repository.JijinUserBalanceHistoryRepository;
import com.lufax.jijin.fundation.repository.JijinUserBalanceRepository;
import com.lufax.jijin.fundation.service.domain.FileHolder;
import com.lufax.jijin.fundation.service.domain.JiJinDateUtil;
import com.lufax.jijin.fundation.util.RedeemFileContentUtil;
import com.lufax.jijin.product.dto.ProductDTO;
import com.lufax.jijin.product.repository.ProductRepository;
import com.lufax.jijin.service.MqService;
import com.lufax.jijin.sysFacade.gson.BatchTransferAndFreezeGson;
import com.lufax.jijin.sysFacade.gson.FreezeInfo;
import com.lufax.jijin.sysFacade.gson.Instruction;
import com.lufax.jijin.sysFacade.gson.PaymentWithdrawRequest;
import com.lufax.jijin.sysFacade.gson.TransferFreezeInstructionDetail;
import com.lufax.jijin.sysFacade.gson.TransferInfo;
import com.lufax.jijin.sysFacade.gson.result.PaymentBatchTransferFrozenResult;
import com.lufax.jijin.sysFacade.gson.result.PaymentResult;
import com.lufax.jijin.sysFacade.service.PaymentAppService;
import com.lufax.jijin.user.domain.CheckTradePasswordGson;
import com.lufax.jijin.user.service.UserService;
import com.lufax.mq.client.util.MapUtils;

@Service
public class RedeemService {

    @Autowired
    JijinUserBalanceRepository jijinUserBalanceRepository;
    @Autowired
    JijinUserBalanceHistoryRepository jijinUserBalanceHistoryRepository;
    @Autowired
    JijinGatewayRemoteService gatewayService;
    @Autowired
    JijinInfoRepository jijinInfoRepository;
    @Autowired
    JijinTradeRecordRepository jijinTradeRecordRepository;
    @Autowired
    JijinTradeLogRepository jijinTradeLogRepository;
    @Autowired
    JijinAccountRepository jijinAccountRepository;
    @Autowired
    UserService userService;
    @Autowired
    SequenceService sequenceService;
    @Autowired
    TradeDayService tradeDayService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private AccountAppCallerService accountAppCallerService;
    @Autowired
    private JijinSequenceRepository jijinSequenceRepository;
    @Autowired
    private BizParametersRepository bizParametersRepository;
    @Autowired
    private JijinSyncFileRepository jijinSyncFileRepository;
    @Autowired
    private MqService mqService;
    @Autowired
    private JijinDahuaRedisService jijinDahuaRedisService;
    @Autowired
    private JijinRedeemBalHisRepository jijinRedeemBalHisRepository;
    @Autowired
    private JijinFrozenDetailRepository jijinFrozenDetailRepository;
    @Autowired
    private RedeemServiceUtil redeemServiceUtil;
    @Autowired
    private PaymentAppService paymentAppService;
    @Autowired
    JijinExCharacterRepository jijinExCharacterRepository;
    @Autowired
    private JiJinDateUtil jiJinDateUtil;

    private static final int ROWNUM = 500;

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setJijinGatewayService(JijinGatewayRemoteService gatewayService) {
        this.gatewayService = gatewayService;
    }

    public String redeem(long userId, String fundCode, String tradePassword, String amount, String channel, Long transactionId, String redeemType, String prodCode,String transferType,String remark) {

        RedeemResultGson response = new RedeemResultGson();

        if (amount == null || !NumberUtils.isNumber(amount) || NumberUtils.createBigDecimal(amount).compareTo(BigDecimal.ZERO) <= 0
                || !isLegalInput(amount)) {
            Logger.info(this, String.format("[jijin-redeem]redeem amount is not correct[%s] !", amount));
            response.setRetCode(ResourceResponseCode.REDEEM_AMOUNT_NOT_CORRECT);
            response.setRetMessage("redeem amount is not correct");
            return new Gson().toJson(response);
        }

        //1检查用户基金账户
        JijinUserBalanceDTO jijinUserBalanceDTO = jijinUserBalanceRepository.findUserBalanceByFundCode(userId, fundCode);
        if (jijinUserBalanceDTO == null) {
            Logger.info(this, String.format("[jijin-redeem]can not find a user account with fundCode:%s for user [userId: %s] !", fundCode, userId));
            response.setRetCode(ResourceResponseCode.ACCOUNT_NOT_EXIST);
            response.setRetMessage("account not exist");
            return new Gson().toJson(response);
        }

        JijinInfoDTO infoDto = jijinInfoRepository.findJijinInfo(MapUtils.buildKeyValueMap("fundCode", fundCode));
        if (infoDto == null) {
            Logger.info(this, String.format("[jijin-redeem]can not find jijin info by fundCode:%s ", fundCode));
            response.setRetCode(ResourceResponseCode.FAIL_WITH_EXCEPTION);
            response.setRetMessage("jijin info does not exist ");
            return new Gson().toJson(response);
        }

        BigDecimal redeemAmount = new BigDecimal(amount);
        redeemAmount = redeemAmount.setScale(2, BigDecimal.ROUND_DOWN); // 保留两位小数， 例 100 -> 100.00
        BigDecimal balance;
        //2.检查余额是否足够(货基 和 非货基)
        balance = jijinUserBalanceDTO.getShareBalance();

        if (balance.compareTo(redeemAmount) == -1) {

        	//如果份额不够，则再查一下幂等
        	JijinTradeRecordDTO record = jijinTradeRecordRepository.getRecordByTrxIdAndTypeChannel(transactionId, TradeRecordType.REDEEM.name(),channel);
            if (null != record) {
                response.setTradeRecordId(record.getId());
                response.setRetCode(ResourceResponseCode.SUCCESS);
                response.setRetMessage("redeem record was already submitted");
            } else {
            	 Logger.info(this, String.format("[jijin-redeem]balance is not enough [userId: %s] [amount: %s]!", userId, amount));
                 response.setRetCode(ResourceResponseCode.REDEEM_AMOUNT_NOT_ENOUGH);
                 response.setRetMessage("balance is not enough");
            }

            return new Gson().toJson(response);
        }


        //默认为T+1普通赎回
        if (redeemType == null || RedeemTypeEnum.NORMAL.getTypeCode().equals(redeemType)) {
            redeemType = RedeemTypeEnum.NORMAL.getTypeCode();
        } else {
            redeemType = RedeemTypeEnum.FAST.getTypeCode();
        }


        //大华货基T0赎回需要判断是否超限, redis判断
        if (("YEB".equals(channel) || "MIDWAY".equals(channel) || "LBO".equals(channel)) 
        		&& JijinConstants.DAHUA_FUND_CODE.equals(fundCode) 
        		&& RedeemTypeEnum.FAST.getTypeCode().equals(redeemType)) {

            String canRedeem = redeemServiceUtil.canBeRedeemed(fundCode); //阀值判断
            if (!"00".equals(canRedeem)) {
                Logger.info(this, String.format("[jijin-redeem]can not redeem dahua for beyond limit"));
                response.setRetCode(ResourceResponseCode.REDEEM_SUBMIT_FAIL);
                response.setRetMessage("超出阀值赎回总额限制");
                
            	JijinTradeRecordDTO record = jijinTradeRecordRepository.getRecordByTrxIdAndTypeChannel(transactionId, TradeRecordType.REDEEM.name(),channel);

                if (null != record) {
                    response.setTradeRecordId(record.getId());
                    response.setRetCode(ResourceResponseCode.SUCCESS);
                    response.setRetMessage("redeem record was already submitted");
                }

                return new Gson().toJson(response);
            }

            //redis准实时判断           
            BigDecimal currentValue = jijinDahuaRedisService.getRedisValue();
            if (currentValue.compareTo(BigDecimal.ZERO) < 0 || currentValue.subtract(redeemAmount).compareTo(BigDecimal.ZERO) < 0) {
                Logger.info(this, String.format("[jijin-redeem]can not redeem dahua for beyond limit"));
                response.setRetCode(ResourceResponseCode.REDEEM_SUBMIT_FAIL);
                response.setRetMessage("超出redis赎回总额限制");

            	JijinTradeRecordDTO record = jijinTradeRecordRepository.getRecordByTrxIdAndTypeChannel(transactionId, TradeRecordType.REDEEM.name(),channel);

                if (null != record) {
                    response.setTradeRecordId(record.getId());
                    response.setRetCode(ResourceResponseCode.SUCCESS);
                    response.setRetMessage("redeem record was already submitted");
                }
                return new Gson().toJson(response);
            }

        }


        JijinAccountDTO accountDto = jijinAccountRepository.findActiveAccount(userId, infoDto.getInstId(), "PAF");
        Long sequence = jijinSequenceRepository.findJijinSequence();
        String ukToken = "JIJIN" + sequence;
        //yeb发起的赎回transactionId不为空, jijin发起的赎回transactionId为空
        if ("YEB".equals(channel)) {
            ukToken = "YEB" + transactionId;
        } else if ("MIDWAY".equals(channel)) {
            ukToken = "MIDWAY" + transactionId;
        }  else if ("LBO".equals(channel)) {
            ukToken = "LBO" + transactionId;
        }else {
            transactionId = sequence;
        }

        //3.校验用户交易密码-陆金宝发起的赎回不需要校验密码
        if (!"YEB".equals(channel) && !"MIDWAY".equals(channel)&&!"LBO".equals(channel)) {
            //非陆金宝渠道屏蔽大华货基的赎回申请
            if (JijinConstants.DAHUA_FUND_CODE.equals(fundCode)) {
                response.setRetCode(ResourceResponseCode.FUND_CODE_WRONG);
                response.setRetMessage("不能发起大华货基赎回");
                return new Gson().toJson(response);
            }
            CheckTradePasswordGson checkTradePasswordGson = userService.checkTradePwd(Long.valueOf(userId), tradePassword, "5");
            if (!checkTradePasswordGson.isSuccess()) {
                Logger.info(this, String.format("[jijin-redeem]check trade password not success [userId: %s]!", userId));
                response.setRetCode(ResourceResponseCode.REDEEM_PWD_WRONG);
                response.setRetMessage("user pwd is wrong");
                if ("08".equals(checkTradePasswordGson.getResultId())) {
                    response.setRetCode(ResourceResponseCode.REDEEM_PWD_LOCK);
                    response.setRetMessage("user pwd is locked");
                }
                response.setMemo(checkTradePasswordGson.getResultMsg());
                response.setLockHours(checkTradePasswordGson.getLockHours());
                response.setLockRange(checkTradePasswordGson.getLockRange());
                response.setMaxErrorTime(checkTradePasswordGson.getMaxErrorTime());
                response.setErrorTime(checkTradePasswordGson.getErrorTime());

                return new Gson().toJson(response);
            }
        } else {
            //陆金宝渠道屏蔽非大华货基的赎回申请
            if (!JijinConstants.DAHUA_FUND_CODE.equals(fundCode)) {
                response.setRetCode(ResourceResponseCode.FUND_CODE_WRONG);
                response.setRetMessage("只能发起大华货基赎回");
                return new Gson().toJson(response);
            }
        }

        jijinUserBalanceDTO.setShareBalance(jijinUserBalanceDTO.getShareBalance().subtract(redeemAmount));
        jijinUserBalanceDTO.setFrozenShare(jijinUserBalanceDTO.getFrozenShare().add(redeemAmount));

        try {
            long result = redeemServiceUtil.recordRedeem(jijinUserBalanceDTO, infoDto, accountDto, userId, fundCode, transactionId, redeemAmount, 
            		redeemType, ukToken, prodCode, channel,transferType,remark);

            if (result > 0) {
                response.setTradeRecordId(result);
                response.setRetCode(ResourceResponseCode.SUCCESS);
                response.setRetMessage("redeem record success");

            } else {
                // 冻结份额失败，直接返回，让用户重试
                Logger.info(this, String.format("[jijin-redeem]基金赎回，冻结失败 with fundCode:%s for user [userId: %s]!", fundCode, userId));
                response.setRetCode(ResourceResponseCode.REDEEM_FREEZE_FAIL);
                response.setRetMessage("freeze failed");
            }
        } catch (Exception e) {
            // any runtime exception，rollback, 如果是UK冲突了，则按幂等处理
            Logger.warn(this, String.format("[jijin-redeem]基金赎回，occur exception [%s] with transactionId:%s for user [userId: %s]!", e.getClass().getName(), transactionId, userId),e);
            JijinTradeRecordDTO record = jijinTradeRecordRepository.getRecordByTrxIdAndTypeChannel(transactionId, TradeRecordType.REDEEM.name(),channel);

            if (null != record) {
                response.setTradeRecordId(record.getId());
                response.setRetCode(ResourceResponseCode.SUCCESS);
                response.setRetMessage("redeem record was already submitted");
            } else {
                response.setRetCode(ResourceResponseCode.REDEEM_SUBMIT_FAIL);
                response.setRetMessage("freeze failed with exception:" + e.getClass().getName());
            }
        }

        int nDay = infoDto.getRedemptionArrivalDay() + 2; // add additional 2 days
        response.setEstimateTime(DateUtils.formatDate(tradeDayService.getRedeemEstimateDate(new Date(), nDay), DateUtils.DATE_FORMAT));
        return new Gson().toJson(response);
    }


    /**
     * 接收MQ，进行基金公司赎回下单(T0,T1公用)
     *
     * @param jijinTradeRecordDTO
     */
    public void callJijinRedeem(JijinTradeRecordDTO jijinTradeRecordDTO) {

        if (!TradeRecordStatus.INIT.name().equals(jijinTradeRecordDTO.getStatus())) {
            //幂等
            return;
        }

        JijinAccountDTO accountDto = jijinAccountRepository.findActiveAccount(jijinTradeRecordDTO.getUserId(), jijinTradeRecordDTO.getInstId(), "PAF");
        //6.call GW
        Logger.info(this, String.format("[jijin-redeem]start call jijin gw for [userId: %s]!", jijinTradeRecordDTO.getUserId()));
        GWRedeemRequestGson request = new GWRedeemRequestGson();
        request.setApplicationNo(jijinTradeRecordDTO.getAppNo());
        request.setBusinessCode(JijinBizType.REDEEM.getCode());
        request.setContractNo(accountDto.getContractNo());
        request.setFundCode(jijinTradeRecordDTO.getFundCode());
        request.setInstId(jijinTradeRecordDTO.getInstId());
        request.setIsIndividual("1"); // 1 - 个人
        BigDecimal redeemAmount = jijinTradeRecordDTO.getReqShare().setScale(2, BigDecimal.ROUND_DOWN); // 保留两位小数， 例 100 -> 100.00
        request.setApplicationVol(redeemAmount.toString());
        request.setChargeType("A"); //A - 前收费
        request.setHugeSum("1");
        request.setVersion("1.0");
        //大华货基需要extension
        if (FundSaleCode.DHC.getInstId().equals(jijinTradeRecordDTO.getInstId())) {

            request.setExtension(DateUtils.formatDateAsCmsDrawSequence(new Date()), accountDto.getPayNo());
        }
        request.setType(!jijinTradeRecordDTO.getRedeemType().equals("0") ? "0" : "1"); //1:T+0赎回,0：T+1普通赎回

        GWResponseGson res = gatewayService.redeem(jijinTradeRecordDTO.getInstId(), JsonHelper.toJson(request));
        Logger.info(this, String.format("[jijin-redeem]jijin gateway response of redeem is [%s]!", JsonHelper.toJson(res)));

        if (res.getRetCode().equals(GWResponseCode.SUCCESS)) {
            GWRedeemResponseGson jijinRes = JsonHelper.fromJson(res.getContent(), GWRedeemResponseGson.class);
            // 返回提交成功
            if (jijinRes.getErrorCode().equals("0000")) {
                String appSheetNo = jijinRes.getAppSheetSerialNo();
                String trxDate = jijinRes.getTransactionDate();
                String trxTime = jijinRes.getTransactionTime();
                String errorCode = jijinRes.getErrorCode();
                String errorMsg = jijinRes.getErrorMessage();
                // update tradeRecord status and response's info
                //WEBDEV-8355:基金(PC)赎回到账时间显示优化
                JijinInfoDTO jijinInfoDTO = jijinInfoRepository.findJijinInfo(MapUtils.buildKeyValueMap("fundCode", jijinTradeRecordDTO.getFundCode()));
                //申购或认购返回预确认时，需要保持预计确认日期
                String expectConfirmDate = jiJinDateUtil.getConfirmDate(TradeRecordType.REDEEM.name(), trxTime,
                        jijinInfoDTO.getFundType());
                String expectAccountDate = jiJinDateUtil.getAccountDate(TradeRecordType.REDEEM.name(), trxTime,
                        jijinInfoDTO.getFundType(), jijinInfoDTO.getRedemptionArrivalDay());
                jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "status", TradeRecordStatus.SUBMIT_SUCCESS.name(),
                        "appSheetNo", appSheetNo, "trxDate", trxDate, "trxTime", trxTime, "errorCode", errorCode, "errorMsg", errorMsg,
                        "expectConfirmDate",expectConfirmDate,"expectAccountDate",expectAccountDate));
                Logger.info(this, String.format("[jijin-redeem]call jijin gw success, and return redeem success with tradeRecordId:%s for user [userId: %s]!", jijinTradeRecordDTO.getId(), jijinTradeRecordDTO.getUserId()));
            } else {
                // submit response fail， 解冻份额，更新失败数据，插log
                redeemServiceUtil.unfreezeRedeem(jijinTradeRecordDTO, redeemAmount, jijinRes.getErrorCode(), jijinRes.getErrorMessage());
            }
        } else if (res.getRetCode().equals(GWResponseCode.LACK_CLIENT_CONFIG)
                || res.getRetCode().equals(GWResponseCode.SIGN_FAIL)
                || res.getRetCode().equals(GWResponseCode.VALIDATE_SIGN_FAIL)) {
            Logger.info(this, String.format("[jijin-redeem]response fail [call gateway for redeem] code=[%s]", res.getRetCode()));
            //解冻,落地失败数据
            redeemServiceUtil.unfreezeRedeem(jijinTradeRecordDTO, redeemAmount, res.getRetCode(), res.getRetMessage());
        } else if (res.getRetCode().equals(GWResponseCode.RUNTIME_ERROR)
                || res.getRetCode().equals(GWResponseCode.JIJIN_RESPONSE_ERROR)) {
            Logger.info(this, String.format("[jijin-redeem]response error [call gateway for redeem] code=[%s]", res.getRetCode()));
            // 检查重试次数
            long times = jijinTradeRecordDTO.getRetryTimes() + 1;
            if (times > 5) {
                //如果大于5次无响应，置为需要人工处理，状态保持INIT
                jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "retryTimes", times, "errorMsg", "重试5次无响应,需要人工干预", "isControversial", "1"));
            } else {
                // 由MQ补偿重试
                mqService.sendJijinRedeemMQ(jijinTradeRecordDTO.getId());
                jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "retryTimes", jijinTradeRecordDTO.getRetryTimes() + 1));
            }
            Logger.info(this, String.format("[jijin-redeem]jijin GW runtime error，由MQ补偿重试  with tradeRecordId:%s for user [userId: %s]!", jijinTradeRecordDTO.getId(), jijinTradeRecordDTO.getUserId()));
        }
    }

    /**
     * 提交失败，解冻份额，插log
     */
    private void unfreezeRedeem(JijinTradeRecordDTO jijinTradeRecordDTO, BigDecimal redeemAmount, String errorCode, String errorMsg) {

        boolean unFreezeResult = redeemServiceUtil.unFrezee(redeemAmount, jijinTradeRecordDTO.getUserId(), jijinTradeRecordDTO.getFundCode());

        if (!unFreezeResult) {
            jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "status", TradeRecordStatus.SUBMIT_FAIL.name(),
                    "isControversial", "1", "errorMsg", "赎回解冻失败,需要人工解冻赎回份额"));

            jijinTradeLogRepository.insertBusJijinTradeLog(new JijinTradeLogDTO(jijinTradeRecordDTO.getUserId(), jijinTradeRecordDTO.getFundCode(),
                    jijinTradeRecordDTO.getId(), TradeRecordStatus.SUBMIT_FAIL.name(), TradeRecordType.REDEEM, null, jijinTradeRecordDTO.getReqShare(),
                    jijinTradeRecordDTO.getTrxTime(), jijinTradeRecordDTO.getTrxDate()));


        } else {
            jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "status", TradeRecordStatus.SUBMIT_FAIL.name(),
                    "errorCode", errorCode, "errorMsg", errorMsg));

            jijinTradeLogRepository.insertBusJijinTradeLog(new JijinTradeLogDTO(jijinTradeRecordDTO.getUserId(), jijinTradeRecordDTO.getFundCode(),
                    jijinTradeRecordDTO.getId(), TradeRecordStatus.SUBMIT_FAIL.name(), TradeRecordType.REDEEM, null, jijinTradeRecordDTO.getReqShare(),
                    jijinTradeRecordDTO.getTrxTime(), jijinTradeRecordDTO.getTrxDate()));
            String remark = "赎回陆金宝失败，资金在陆金宝中";
            if ("MIDWAY".equals(jijinTradeRecordDTO.getChannel()) && RedeemTypeEnum.FAST.getTypeCode().equals(jijinTradeRecordDTO.getRedeemType())) {
                ProductDTO productDTO = productRepository.getByCode(jijinTradeRecordDTO.getProdCode());
                remark = productDTO.getDisplayName() + "支付失败，资金在陆金宝中";
            }
            JijinUserBalanceDTO jijinUserBalanceDTO = jijinUserBalanceRepository.findUserBalanceByFundCode(jijinTradeRecordDTO.getUserId(), jijinTradeRecordDTO.getFundCode());
            JijinFrozenDetailDTO jijinFrozenDetailDTO = new JijinFrozenDetailDTO(YebTransactionType.SELL_FAIL_UNFREEZE.getCode(), jijinUserBalanceDTO.getId(), null, jijinTradeRecordDTO.getReqShare(), jijinUserBalanceDTO.getFrozenShare(), remark);
            jijinFrozenDetailRepository.insertJijinFrozenDetail(jijinFrozenDetailDTO);

            JijinUserBalanceHistoryDTO jijinUserBalanceHistoryDTO = JijinUserBalenceHistoryDTOBuilder.buildUserBalanceHistoryDTO(jijinUserBalanceDTO, jijinTradeRecordDTO.getTrxDate(), jijinTradeRecordDTO.getReqShare(), new BigDecimal("0"), "赎回失败，调减冻结份额，调增份额", BalanceHistoryBizType.赎回实时失败, jijinTradeRecordDTO.getId());
            jijinUserBalanceHistoryRepository.insertBusJijinUserBalanceHistory(jijinUserBalanceHistoryDTO);

        }

        //根据渠道，发送消息给陆金宝或midway
        if ("MIDWAY".equals(jijinTradeRecordDTO.getChannel())) {
            mqService.sendJijinRedeemPayResultMQ(jijinTradeRecordDTO.getTrxId(), null, "fail");
        } else if ("YEB".equals(jijinTradeRecordDTO.getChannel())) {
            mqService.sendDhTradeResultMsg(jijinTradeRecordDTO.getTrxId(), "fail");
        }else if("LBO".equals(jijinTradeRecordDTO.getChannel())) {
        	mqService.sendLBOTradeResultMsg(jijinTradeRecordDTO.getTrxId(), "fail","REDEEM","010");
        }

        //大华货基 , +redis & 流水
        try {
            if (FundSaleCode.DHC.getInstId().equals(jijinTradeRecordDTO.getInstId())) {
                JijinRedeemBalHisDTO jijinRedeemBalHisDTO = new JijinRedeemBalHisDTO();
                jijinRedeemBalHisDTO.setAmount(redeemAmount);
                jijinRedeemBalHisDTO.setRemark("赎回失败解冻");
                jijinRedeemBalHisRepository.insertBusJijinRedeemBalHis(jijinRedeemBalHisDTO);
                jijinDahuaRedisService.changeRedisValue(redeemAmount);
            }
        } catch (Exception e) {
            Logger.warn(this, "[jijin-redeem][submit fail, then unfreeze balance, but update hahua redis and redis balance failed]");
        }
        Logger.info(this, String.format("[jijin-redeem]submit response fail，解冻份额，with tradeRecordId:%s for user [userId: %s]!", jijinTradeRecordDTO.getId(), jijinTradeRecordDTO.getUserId()));
    }


    public void redeemCompensation(JijinTradeRecordDTO tradeRecord) {

        //大于5次无响应的记录，置为需要人工处理，状态为UNKNOWN
        //每次做补偿时，不再处理此类记录（由于这种情况极少且应该尽快手动处理完毕，故不再新设置状态）
        if (TradeRecordStatus.UNKNOWN.name().equals(tradeRecord.getStatus()) && tradeRecord.getRetryTimes() > 5) {
            Logger.info(this, String.format("[redeemCompensation] donot process this tradeRecord", new Gson().toJson(tradeRecord)));
            return;
        }
        GWRedeemRequestGson request = new GWRedeemRequestGson();
        JijinInfoDTO infoDto = jijinInfoRepository.findJijinInfo(MapUtils.buildKeyValueMap("fundCode", tradeRecord.getFundCode()));

        JijinAccountDTO accountDTO = jijinAccountRepository.findActiveAccount(tradeRecord.getUserId(), tradeRecord.getInstId(), "PAF");

        request.setVersion("1.0");
        request.setInstId(infoDto.getInstId());
        request.setIsIndividual("1");
        request.setContractNo(tradeRecord.getContractNo());
        request.setApplicationNo(tradeRecord.getAppNo());
        request.setBusinessCode(JijinBizType.REDEEM.getCode());
        request.setFundCode(tradeRecord.getFundCode());
        request.setHugeSum("1");

        BigDecimal amount = null;
        //货基也统一使用份额
        amount = tradeRecord.getReqShare();

        amount = amount.setScale(2, BigDecimal.ROUND_DOWN);
        request.setApplicationVol(amount.toString());
        request.setChargeType("A");

        //大华货基需要extension
        if (FundSaleCode.DHC.getInstId().equals(infoDto.getInstId())) {
            request.setExtension(DateUtils.formatDateAsCmsDrawSequence(new Date()), accountDTO.getPayNo());
            request.setType("1");//1:T+0赎回
        } else {
            request.setType("0");//0：普通赎回
        }

        GWResponseGson res = gatewayService.redeem(infoDto.getInstId(), JsonHelper.toJson(request));
        Logger.info(this, String.format("[redeemCompensation]redeem request is[%s],response is[%s]", new Gson().toJson(request), new Gson().toJson(res)));
        if (res.getRetCode().equals(GWResponseCode.SUCCESS)) {
            GWRedeemResponseGson jijinRes = JsonHelper.fromJson(res.getContent(), GWRedeemResponseGson.class);
            // submit response success 
            // update tradeRecord  as submit success
            if (jijinRes.getErrorCode().equals("0000")) {

                String appSheetNo = jijinRes.getAppSheetSerialNo();
                String trxDate = jijinRes.getTransactionDate();
                String trxTime = jijinRes.getTransactionTime();
                String errorCode = jijinRes.getErrorCode();
                String errorMsg = jijinRes.getErrorMessage();

                Map<Object, Object> map = MapUtils.buildKeyValueMap("id", tradeRecord.getId(), "status", TradeRecordStatus.SUBMIT_SUCCESS.name(), "appSheetNo", appSheetNo
                        , "trxDate", trxDate, "trxTime", trxTime, "errorCode", errorCode, "errorMsg", errorMsg);
                if (FundSaleCode.DHC.getInstId().equals(infoDto.getInstId())) {
                    map.put("isControversial", "-1");
                }
                jijinTradeRecordRepository.updateBusJijinTradeRecord(map);

                JijinUserBalanceDTO jijinUserBalanceDTO = jijinUserBalanceRepository.findUserBalanceByFundCode(tradeRecord.getUserId(), tradeRecord.getFundCode());
                JijinUserBalanceHistoryDTO jijinUserBalanceHistoryDTO = JijinUserBalenceHistoryDTOBuilder.buildUserBalanceHistoryDTO(jijinUserBalanceDTO, trxDate, new BigDecimal("0"), amount, "赎回，冻结份额", BalanceHistoryBizType.赎回下单成功, tradeRecord.getId());
                jijinUserBalanceHistoryRepository.insertBusJijinUserBalanceHistory(jijinUserBalanceHistoryDTO);

            } else {
                // submit response fail， 解冻份额 
                // update tradeRecord as submit_fail
                String errorCode = jijinRes.getErrorCode();
                String errorMsg = jijinRes.getErrorMessage();
                Logger.info(this, String.format("[redeemCompensation]errorCode is not 0000,unFreezeAndUpdateStatusSubmitFail"));
                redeemServiceUtil.unFreezeAndUpdateStatusSubmitFail(tradeRecord, errorCode, errorMsg, null, null);
            }
        } else if (res.getRetCode().equals(GWResponseCode.LACK_CLIENT_CONFIG)
                || res.getRetCode().equals(GWResponseCode.SIGN_FAIL)
                || res.getRetCode().equals(GWResponseCode.VALIDATE_SIGN_FAIL)) {
            //解冻
            //update tradeRecord as submit_fail
            Logger.info(this, String.format("[redeemCompensation]retCode is not success,unFreezeAndUpdateStatusSubmitFail"));
            redeemServiceUtil.unFreezeAndUpdateStatusSubmitFail(tradeRecord, res.getRetCode(), res.getRetMessage(), null, null);
        } else if (res.getRetCode().equals(GWResponseCode.RUNTIME_ERROR)
                || res.getRetCode().equals(GWResponseCode.JIJIN_RESPONSE_ERROR)) {
            Logger.info(this, String.format("[redeemCompensation]retCode is runtime error,updateBusJijinTradeRecord"));
            // 检查重试次数
            long times = tradeRecord.getRetryTimes() + 1;
            if (times > 5) { // update as submit fail and set
                //如果大于5次无响应，置为需要人工处理，状态保持UNKOWN
                jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", tradeRecord.getId(), "retryTimes", times, "errorMsg", "重试5次无响应,需要人工干预", "isControversial", "1"));
                //unFreezeAndUpdateStatusSubmitFail(tradeRecord, isHuoji, res.getRetCode(), res.getRetMessage(), null, null);
            } else {
                //continue retry
                jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", tradeRecord.getId(), "retryTimes", times));
            }
        }
    }

    /**
     * T0赎回转账job
     *
     * @param jijinTradeRecordDTO
     */
    @Transactional
    public void handelT0RedeemApply(JijinTradeRecordDTO jijinTradeRecordDTO) {
        Logger.info(this, String.format("[handelT0RedeemApply] start process.parameter is [%s]", new Gson().toJson(jijinTradeRecordDTO)));
        jijinTradeRecordDTO = jijinTradeRecordRepository.getRecordById(jijinTradeRecordDTO.getId());
        if (jijinTradeRecordDTO.getStatus().equals(TradeRecordStatus.SUCCESS.name()) || jijinTradeRecordDTO.getStatus().equals(TradeRecordStatus.PAID_IN_ADVANCE.name())) {
            Logger.info(this, String.format("[handelT0RedeemApply] jijinTradeRecordDTO[id=%s] status is success,not need process", jijinTradeRecordDTO.getId()));
            return;
        }

        //用户余额账户
        JijinUserBalanceDTO jijinUserBalanceDTO = jijinUserBalanceRepository.findUserBalanceByFundCode(jijinTradeRecordDTO.getUserId(), jijinTradeRecordDTO.getFundCode());
        JijinInfoDTO jijinInfoDTO = jijinInfoRepository.findJijinInfo(MapUtils.buildKeyValueMap("fundCode", jijinTradeRecordDTO.getFundCode()));
        if (jijinInfoDTO == null) {
            Logger.info(this, String.format("[handelT0RedeemApply] no such jijin[fundCode=%s]", jijinTradeRecordDTO.getFundCode()));
            return;
        }

    	//大华虚拟户转账到个人虚拟户
        Long dahuaSpvUserId = 0l;
        try {
            String bizValue = bizParametersRepository.findValueByCode(JijinConstants.JIJIN_DAHUA_ACCOUNT_ID_BIZ_PARAMETER_CODE);
            dahuaSpvUserId = new Long(bizValue);
        } catch (Exception e) {
            Logger.error(this, String.format("get dahua account id exception"));
            return;
        }
        
        //提交成功的记录,进行划帐处理（到银行卡）
        if(TransferTypeEnum.card.name().equals( jijinTradeRecordDTO.getTransferType())){
        	PaymentResult res = transferToBankCard(jijinTradeRecordDTO,jijinInfoDTO,dahuaSpvUserId);
	        boolean remitResult =false;
	        if("SUCCESS".equals(res.getStatus())){
	        	remitResult = true;
	        }else if("FAILURE".equals(res.getStatus())){
	            Logger.error(this, String.format("[handelT0RedeemApply] jijinTradeRecordDTO[id=%s] transfer account failed since:[%s]", jijinTradeRecordDTO.getId(),res.getRes_msg()));
	            //更新失败状态
	            Map<Object, Object> map = MapUtils.buildKeyValueMap(
	                    "id", jijinTradeRecordDTO.getId(),
	                    "status", TradeRecordStatus.WAITING_MONEY.name(),
	                    "errorCode", res.getRes_code(),
	                    "errorMsg","需人工干预，赎回已成功，转账失败，份额未解冻："+ res.getRes_msg(),
	                    "isControversial","1");
	            jijinTradeRecordRepository.updateBusJijinTradeRecord(map);
	            return;
	        }
        	
	        if (remitResult) { // 更新状态到处理中 PROCZESSING
	        	jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id",jijinTradeRecordDTO.getId(),"status",TradeRecordStatus.PROCESSING.name()));
	        }
        	
        }else{//直接划帐到用户虚拟户
        	PaymentBatchTransferFrozenResult res = transferToAccount(jijinTradeRecordDTO,jijinInfoDTO,dahuaSpvUserId);
  
	        boolean remitResult =false;
	        if("SUCCESS".equals(res.getStatus())){
	        	remitResult = true;
	        }else if("FAILURE".equals(res.getStatus())){
	            Logger.error(this, String.format("[handelT0RedeemApply] jijinTradeRecordDTO[id=%s] transfer account failed since:[%s]", jijinTradeRecordDTO.getId(),res.getRes_msg()));
	            //更新失败状态
	            Map<Object, Object> map = MapUtils.buildKeyValueMap(
	                    "id", jijinTradeRecordDTO.getId(),
	                    "status", TradeRecordStatus.WAITING_MONEY.name(),
	                    "errorCode", res.getRes_code(),
	                    "errorMsg","需人工干预，赎回已成功，转账失败，份额未解冻："+ res.getRes_msg(),
	                    "isControversial","1");
	            jijinTradeRecordRepository.updateBusJijinTradeRecord(map);
	            return;
	        }
	        
	        //划帐进行中,不改状态
	        if (!remitResult) {
	            Logger.warn(this, String.format("[handelT0RedeemApply] jijinTradeRecordDTO[id=%s] transfer account failed", jijinTradeRecordDTO.getId()));
	            //不更新状态，但是要更新retryTimes
	            Map<Object, Object> map = MapUtils.buildKeyValueMap(
	                    "id", jijinTradeRecordDTO.getId(),
	                    "retryTimes", jijinTradeRecordDTO.getRetryTimes() + 1,
	                    "errorCode", res.getRes_code(),
	                    "errorMsg", res.getRes_msg());
	            jijinTradeRecordRepository.updateBusJijinTradeRecord(map);
	            return;
	        }
	
	        //大华基金虚拟户资金变动时，发送消息
	        if (JijinConstants.DAHUA_FUND_CODE.equals(jijinTradeRecordDTO.getFundCode())) {
	            mqService.sendDahuaAccountBalanceChange(jijinTradeRecordDTO.getFundCode(), "2");
	        }
	
	        //调整份额,减去冻结金额
	        jijinUserBalanceDTO.setFrozenShare(jijinUserBalanceDTO.getFrozenShare().subtract(jijinTradeRecordDTO.getReqShare()));
	        int result = jijinUserBalanceRepository.updateFundAccount(jijinUserBalanceDTO);
	        
	        if (result == 1) {
	            JijinUserBalanceHistoryDTO jijinUserBalanceHistoryDTO = JijinUserBalenceHistoryDTOBuilder.buildUserBalanceHistoryDTO(jijinUserBalanceDTO, jijinTradeRecordDTO.getTrxDate(), new BigDecimal("0"), new BigDecimal("0"), "赎回成功，调减冻结份额", BalanceHistoryBizType.赎回成功回款, jijinTradeRecordDTO.getId());
	            jijinUserBalanceHistoryRepository.insertBusJijinUserBalanceHistory(jijinUserBalanceHistoryDTO);
	
	            //更新状态为SUCCESS(处理成功)
	            Map<Object, Object> map = MapUtils.buildKeyValueMap(
	                    "id", jijinTradeRecordDTO.getId(),
	                    "status", TradeRecordStatus.SUCCESS.name());
	            int upd = jijinTradeRecordRepository.updateBusJijinTradeRecord(map);
	           	
	            //add unfrozen detail
	            String remark = "赎回陆金宝成功";
	            if ("MIDWAY".equals(jijinTradeRecordDTO.getChannel()) &&RedeemTypeEnum.FAST.getTypeCode().equals(jijinTradeRecordDTO.getRedeemType())) {
	                ProductDTO productDTO = productRepository.getByCode(jijinTradeRecordDTO.getProdCode());
	                remark = productDTO.getDisplayName() + "支付成功，资金赎回且冻结在陆金所账户";
	            }
	            JijinFrozenDetailDTO jijinFrozenDetailDTO = new JijinFrozenDetailDTO(YebTransactionType.SELL_SUCCESS_UNFREEZE.getCode(), jijinUserBalanceDTO.getId(), null, jijinTradeRecordDTO.getReqShare(), jijinUserBalanceDTO.getFrozenShare(), remark);
	            jijinFrozenDetailRepository.insertJijinFrozenDetail(jijinFrozenDetailDTO);
	
	            //最终成功时，加log
	            JijinTradeLogDTO log = new JijinTradeLogDTO(jijinTradeRecordDTO.getUserId(), jijinTradeRecordDTO.getFundCode(), jijinTradeRecordDTO.getId(), TradeRecordStatus.SUCCESS.name(), jijinTradeRecordDTO.getType(), jijinTradeRecordDTO.getReqShare(), jijinTradeRecordDTO.getReqShare(), jijinTradeRecordDTO.getTrxTime(), jijinTradeRecordDTO.getTrxDate());
	            log.setFee(BigDecimal.ZERO);
	            jijinTradeLogRepository.insertBusJijinTradeLog(log);
	
	            //根据渠道，发送消息给陆金宝或midway
	            if ("MIDWAY".equals(jijinTradeRecordDTO.getChannel())) {
	                mqService.sendJijinRedeemPayResultMQ(jijinTradeRecordDTO.getTrxId(), Long.valueOf(res.getFrozenInfoList().get(0).getFrozenNo()), "success");
	            } else {
	                if("LBO".equals(jijinTradeRecordDTO.getChannel())){
	                	mqService.sendLBOTradeResultMsg(jijinTradeRecordDTO.getTrxId(), "success","REDEEM","000");
	                }else{
	                	mqService.sendDhTradeResultMsg(jijinTradeRecordDTO.getTrxId(), "success");
	                }  
	            }
	            Logger.info(this, String.format("[handelT0RedeemApply] sendRedeemResultMsg success [%s]", jijinTradeRecordDTO.getTrxId()));
	        }
        }
    }
    
    /**
     * 转入虚拟户后再转入银行卡
     * @param jijinTradeRecordDTO
     * @param jijinInfoDTO
     * @return
     */
    private PaymentResult transferToBankCard(JijinTradeRecordDTO jijinTradeRecordDTO,JijinInfoDTO jijinInfoDTO,Long dahuaSpvUserId ){
    	PaymentResult res = new PaymentResult();
    	
    	TransferFreezeInstructionDetail detail = new TransferFreezeInstructionDetail(jijinTradeRecordDTO.getReqShare(), dahuaSpvUserId,
    			TransactionType.EXPENSE_REDEMPTION.name(), "赎回：" + jijinInfoDTO.getFundName(),
    			TransactionType.INCOME_REDEMPTION.name(), "赎回：" + jijinInfoDTO.getFundName(), 
    			String.valueOf(jijinTradeRecordDTO.getId()),	"大华虚拟户转账", "FROZEN_FUND",
    			"大华赎回转账", String.valueOf(jijinTradeRecordDTO.getId()), "大华赎回转账冻结",
    			jijinInfoDTO.getProductCode(), DateUtils.formatDate(new Date(), DateUtils.DATE_TIME_FORMAT), jijinTradeRecordDTO.getAppSheetNo(),
    			"大华T0赎回转账", "大华T0赎回进银行卡");
    	
    	List<Instruction> prepareOperationList = new ArrayList<Instruction>();
    	Instruction ins = new Instruction("TRANSFER_FREEZE",JsonHelper.toJson(detail));
    	prepareOperationList.add(ins);
    	PaymentWithdrawRequest request = new PaymentWithdrawRequest("JIJIN", String.valueOf(jijinTradeRecordDTO.getId()),
    			"0", jijinTradeRecordDTO.getUserId(), jijinTradeRecordDTO.getReqShare(),
    			"CUSTOMER_WITHDRAWAL", DateUtils.formatDate(new Date(), DateUtils.DATE_TIME_FORMAT), "陆金宝赎回进银行卡",
    			null, null, prepareOperationList, jijinInfoDTO.getProductCode(),
    			jijinTradeRecordDTO.getTrxDate(), jijinTradeRecordDTO.getAppSheetNo(), "大华T0赎回进银行卡",
    			"大华T0赎回进银行卡", String.valueOf(jijinTradeRecordDTO.getId()), "大华T0赎回进银行卡");
    	
    	res = paymentAppService.cashierWithdraw(request);
    	
    	return res;
    }
    
    /**
     * 转账到个人虚拟户
     * @param jijinTradeRecordDTO
     * @param jijinInfoDTO
     * @param dahuaSpvUserId
     * @return
     */
    private PaymentBatchTransferFrozenResult transferToAccount(JijinTradeRecordDTO jijinTradeRecordDTO,JijinInfoDTO jijinInfoDTO,Long dahuaSpvUserId ){
    	
    	PaymentBatchTransferFrozenResult res = new PaymentBatchTransferFrozenResult();
        String fromTransactionType = TransactionType.EXPENSE_REDEMPTION.name();
        String toTransactionType = TransactionType.INCOME_REDEMPTION.name();
        BigDecimal transferredAmount = jijinTradeRecordDTO.getReqShare();

        // 转账 or 转账冻结
        BatchTransferAndFreezeGson request = new BatchTransferAndFreezeGson();
        request.setInstructionNo(String.valueOf(jijinTradeRecordDTO.getId()));
        List<TransferInfo> transferList = new ArrayList<TransferInfo>();
        TransferInfo tInfo = new TransferInfo();

        tInfo.setTransferAmount(transferredAmount);
        tInfo.setFromUserId(dahuaSpvUserId);
        tInfo.setFromTransactionType(fromTransactionType);
        tInfo.setFromRemark("赎回：" + jijinInfoDTO.getFundName());
        tInfo.setToUserId(jijinTradeRecordDTO.getUserId());
        tInfo.setToTransactionType(toTransactionType);
        tInfo.setToRemark("赎回：" + jijinInfoDTO.getFundName());
        tInfo.setProductCode(jijinInfoDTO.getProductCode());
        tInfo.setReferenceId(String.valueOf(jijinTradeRecordDTO.getAppSheetNo()));
        tInfo.setReferenceType("JIJIN_REDEEM");
        tInfo.setBizId(String.valueOf(jijinTradeRecordDTO.getId()));
        tInfo.setBizType("JIJIN_REDEEM");
        transferList.add(tInfo);
        request.setTransferList(transferList);

        //如果是支付赎回，则还需冻结
        if ("MIDWAY".equals(jijinTradeRecordDTO.getChannel())) {
            List<FreezeInfo> freezeList = new ArrayList<FreezeInfo>();
            FreezeInfo fInfo = new FreezeInfo();
            fInfo.setFrozenAmount(transferredAmount);
            fInfo.setUserId(jijinTradeRecordDTO.getUserId());
            fInfo.setBusinessId(jijinTradeRecordDTO.getId());
            fInfo.setTransactionType("FROZEN_FUND");
            fInfo.setRemark("T0投资:" + jijinTradeRecordDTO.getProdCode());
            fInfo.setReferenceId(String.valueOf(jijinTradeRecordDTO.getAppSheetNo()));
            fInfo.setReferenceType("JIJIN_REDEEM_PAY_FROZEN");
            fInfo.setBizId(String.valueOf(jijinTradeRecordDTO.getId()));
            fInfo.setBizType("JIJIN_REDEEM_PAY_FROZEN");
            freezeList.add(fInfo);
            request.setFreezeList(freezeList);
        }

        request.setProductCode(jijinTradeRecordDTO.getProdCode());
        request.setReferenceId(String.valueOf(jijinTradeRecordDTO.getAppSheetNo()));
        request.setReferenceType("JIJIN_REDEEM");
        request.setBizId(String.valueOf(jijinTradeRecordDTO.getId()));
        request.setBizType("JIJIN_BATCH_REDEEM");

        res = paymentAppService.transferAndFreeze(request);
        Logger.info(this, String.format("[handelT0RedeemApply] transferMoney.request is [%s].response is[%s]", new Gson().toJson(request), new Gson().toJson(res)));

        return res;
        
        
    }
    
    /**
     * 处理payment返回的处理结果消息
     * @param tradeRecord
     */
    public int handleTransferToBankCard(PaymentWithdrawResultGson msg ){
    	JijinTradeRecordDTO jijinTradeRecordDTO =  jijinTradeRecordRepository.getRecordById(Long.valueOf(msg.getInstructionNo()));
    	int result = 0;
    	if("SUCCESS".equals(msg.getPrepareStatus())){ // 转账成功,调减份额
    			
    			//大华基金虚拟户资金变动时，发送消息
    	        if (JijinConstants.DAHUA_FUND_CODE.equals(jijinTradeRecordDTO.getFundCode())) {
    	            mqService.sendDahuaAccountBalanceChange(jijinTradeRecordDTO.getFundCode(), "2");
    	        }
    	        
    	        JijinUserBalanceDTO jijinUserBalanceDTO = jijinUserBalanceRepository.findUserBalanceByFundCode(jijinTradeRecordDTO.getUserId(), jijinTradeRecordDTO.getFundCode());
    	        
    	        //调整份额,减去冻结金额
    	        jijinUserBalanceDTO.setFrozenShare(jijinUserBalanceDTO.getFrozenShare().subtract(jijinTradeRecordDTO.getReqShare()));
    	        int res = jijinUserBalanceRepository.updateFundAccount(jijinUserBalanceDTO);
    	        
    	        if (res == 1) {
    	            JijinUserBalanceHistoryDTO jijinUserBalanceHistoryDTO = JijinUserBalenceHistoryDTOBuilder.buildUserBalanceHistoryDTO(jijinUserBalanceDTO, jijinTradeRecordDTO.getTrxDate(), new BigDecimal("0"), new BigDecimal("0"), "赎回成功，调减冻结份额", BalanceHistoryBizType.赎回成功回款, jijinTradeRecordDTO.getId());
    	            jijinUserBalanceHistoryRepository.insertBusJijinUserBalanceHistory(jijinUserBalanceHistoryDTO);
    	
    	            //更新状态为SUCCESS(处理成功)
    	            Map<Object, Object> map = MapUtils.buildKeyValueMap(
    	                    "id", jijinTradeRecordDTO.getId(),
    	                    "status", TradeRecordStatus.SUCCESS.name());
    	             jijinTradeRecordRepository.updateBusJijinTradeRecord(map);
    	            
    	            //add unfrozen detail
    	            String remark = "赎回陆金宝成功";
    	            JijinFrozenDetailDTO jijinFrozenDetailDTO = new JijinFrozenDetailDTO(YebTransactionType.SELL_SUCCESS_UNFREEZE.getCode(), jijinUserBalanceDTO.getId(), null, jijinTradeRecordDTO.getReqShare(), jijinUserBalanceDTO.getFrozenShare(), remark);
    	            jijinFrozenDetailRepository.insertJijinFrozenDetail(jijinFrozenDetailDTO);
    	
    	            //最终成功时，加log
    	            JijinTradeLogDTO log = new JijinTradeLogDTO(jijinTradeRecordDTO.getUserId(), jijinTradeRecordDTO.getFundCode(), jijinTradeRecordDTO.getId(), TradeRecordStatus.SUCCESS.name(), jijinTradeRecordDTO.getType(), jijinTradeRecordDTO.getReqShare(), jijinTradeRecordDTO.getReqShare(), jijinTradeRecordDTO.getTrxTime(), jijinTradeRecordDTO.getTrxDate());
    	            log.setFee(BigDecimal.ZERO);
    	            jijinTradeLogRepository.insertBusJijinTradeLog(log);
    	        } else{
    	        	result=6; //重试消息
    	        }       
    		}else{// 转账失败，不调减份额，人工干预调整后，再解冻份额 
    			Logger.warn(this, String.format("[handelRedeemApply] transferMoney failed, recordId:[%s]",jijinTradeRecordDTO.getId()));
    			 jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id",jijinTradeRecordDTO.getId(),
    					 "status",TradeRecordStatus.WAITING_MONEY.name(),"errorMsg","赎回成功，转账失败，份额仍冻结着，人工干预","isControversial","1"));	
    		}
    		
    		//根据渠道，发送消息给YEB,LBO
    	    if(result==0){
    	    	if("SUCCESS".equals(msg.getStatus())){ // 转到银行ok
                    if("LBO".equals(jijinTradeRecordDTO.getChannel())){
                        mqService.sendLBOTradeResultMsg(jijinTradeRecordDTO.getTrxId(), "success","REDEEM","000");
                     }else{
                        mqService.sendDhTradeResultMsg(jijinTradeRecordDTO.getTrxId(), "success");
                     } 
    	    	}else{//赎回成功, 转银行失败  （1.转账or调增失败，转银行失败  2，转账 or调增成功，但转银行失败）
                    if("LBO".equals(jijinTradeRecordDTO.getChannel())){
                        mqService.sendLBOTradeResultMsg(jijinTradeRecordDTO.getTrxId(), "partial_success","REDEEM","000");
                     }else{
                        mqService.sendDhTradeResultMsg(jijinTradeRecordDTO.getTrxId(), "partial_success");
                     } 
    	    	}
    	    }
    	    return result;
    }
    

    @Transactional
    public void unFreezeAndUpdateStatusSubmitFail(JijinTradeRecordDTO tradeRecord, boolean isHuoji, String errorCode, String errorMsg, String trxDate, String trxTime) {

        BigDecimal amount = null;
        amount = tradeRecord.getReqShare();

        boolean result = unFrezee(amount, tradeRecord.getUserId(), tradeRecord.getFundCode(), isHuoji);
        if (!result) {
            Logger.info(this, String.format("赎回解冻失败,tradeRecordId is [%s]", tradeRecord.getId()));
            jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", tradeRecord.getId(), "status", TradeRecordStatus.SUBMIT_FAIL.name(), "errorMsg", "赎回解冻失败,需要人工解冻赎回份额或金额", "isControversial", "1"
                    , "errorCode", errorCode, "errorMsg", errorMsg));
        } else {
            jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", tradeRecord.getId(), "status", TradeRecordStatus.SUBMIT_FAIL.name()
                    , "errorCode", errorCode, "errorMsg", errorMsg));
        }

        insertTradeLog(tradeRecord, TradeRecordStatus.SUBMIT_FAIL, isHuoji, trxDate, trxTime);
    }

    private void insertTradeLog(JijinTradeRecordDTO tradeRecord, TradeRecordStatus status, boolean isHuoji, String trxDate, String trxTime) {

        BigDecimal amount = null;
        if (tradeRecord.getReqShare() != null) {
            amount = tradeRecord.getReqShare();
        } else {
            amount = tradeRecord.getReqAmount();
        }

        //货基也统一使用份额
        jijinTradeLogRepository.insertBusJijinTradeLog(new JijinTradeLogDTO(tradeRecord.getUserId(), tradeRecord.getFundCode(), tradeRecord.getId(), status.name(), TradeRecordType.REDEEM, null, amount, trxTime, trxDate));
    }

    /**
     * 解冻务必要成功，所以多试几次
     *
     * @param redeemAmount
     * @param userId
     * @param fundCode
     * @param isHuoji
     * @return
     */
    public boolean unFrezee(BigDecimal redeemAmount, long userId, String fundCode, boolean isHuoji) {
        JijinUserBalanceDTO jijinUserBalanceDTO;
        int result = 0;
        int retry_times = 0;
        while (result == 0) {
            jijinUserBalanceDTO = jijinUserBalanceRepository.findUserBalanceByFundCode(userId, fundCode);

            jijinUserBalanceDTO.setShareBalance(jijinUserBalanceDTO.getShareBalance().add(redeemAmount));
            jijinUserBalanceDTO.setFrozenShare(jijinUserBalanceDTO.getFrozenShare().subtract(redeemAmount));

            result = jijinUserBalanceRepository.updateFundAccount(jijinUserBalanceDTO);
            retry_times = retry_times + 1;
            if (retry_times > 10) break;
        }
        if (result == 0)
            return false;
        else
            return true;
    }

    /**
     * 判断如果是含小数，则小数位不超过2为
     *
     * @param inputAmount
     * @return
     */
    private boolean isLegalInput(String inputAmount) {

        if (inputAmount.contains(".") && inputAmount.split("\\.")[1].length() > 2) {
            return false;
        }

        return true;
    }

    /**
     * 判断T日大华货基还能赎回多少
     *
     * @param userId
     * @return
     */
    public String remaining(Long userId) {
        Date date = new Date();
        BigDecimal totalRedeemingAmount = jijinTradeRecordRepository.countRedeemAmountByFundCodeAndTime("000379", DateUtils.startOfDay(date), DateUtils.endOfDay(date), userId);
        if (null == totalRedeemingAmount) {
            totalRedeemingAmount = BigDecimal.ZERO;
        }
        BigDecimal jjzlMaxRedeemEveryday = getJJZLMaxRedeemEveryday();
        BigDecimal remainingAmountOfToday = jjzlMaxRedeemEveryday.subtract(totalRedeemingAmount);
        remainingAmountOfToday = remainingAmountOfToday.compareTo(BigDecimal.ZERO) > 0 ? remainingAmountOfToday : BigDecimal.ZERO;
        Logger.info(this, String.format("dahua jijin user[%s] today already redeem amount[%s], today remaining can redeem amount is %s !", userId, totalRedeemingAmount, remainingAmountOfToday));
        return new Gson().toJson(new YebRedeemGson(remainingAmountOfToday, totalRedeemingAmount, jjzlMaxRedeemEveryday, "1"));
    }

    private BigDecimal getJJZLMaxRedeemEveryday() {
        return bizParametersRepository.findByCode("yeb.redeem.jjzl.everyday.max").getParameterValue();
    }

    public void generateRedeemFile(FileHolder fileHolder, String bizDate, String bizType) {

        List<JijinSyncFileDTO> list = new ArrayList<JijinSyncFileDTO>();

        String filePathAndName = fileHolder.getFileAbsolutePath() + fileHolder.getFileName();

        JijinSyncFileDTO existFile = jijinSyncFileRepository.findBusJijinSyncFile(MapUtils.buildKeyValueMap("fileName", filePathAndName));
        if (existFile == null) {
            JijinSyncFileDTO dto = new JijinSyncFileDTO();
            dto.setBizDate(bizDate);
            dto.setBizType(bizType);
            dto.setCurrentLine(1l);
            dto.setFileName(filePathAndName);
            dto.setRetryTimes(0l);
            dto.setStatus(SyncFileStatus.INIT.name());
            list.add(dto);
        }

        if (!CollectionUtils.isEmpty(list))
            jijinSyncFileRepository.batchInsertBusJijinSyncFile(list);

        list = jijinSyncFileRepository.findBusJijinSyncFileList(MapUtils.buildKeyValueMap("status", SyncFileStatus.INIT.name(), "bizType", bizType));
        List<String> types = Arrays.asList("REDEEM");
        String lastTradeDay = tradeDayService.getLastTradeDay(DateUtils.parseDate(bizDate, DateUtils.DATE_STRING_FORMAT));
        Date createFrom = DateUtils.parseDate(lastTradeDay + "150000", DateUtils.XINBAO_MOBILENO_SEQUENCE_FORMATE);
        Date createTo = DateUtils.parseDate(bizDate + "150000", DateUtils.XINBAO_MOBILENO_SEQUENCE_FORMATE);
        for (JijinSyncFileDTO targetFile : list) {
            JijinTradeRecordCountDTO count = jijinTradeRecordRepository.countTradeRecordByCreatedAtAndTypes(createFrom, createTo, "lfx201", types);
            if (count.getTotal() == 0) {
                try {
                    genFile(targetFile.getFileName(), createFrom, createTo, count, true, types);
                    jijinSyncFileRepository.updateBusJijinSyncFileStatus(targetFile.getId(), SyncFileStatus.WRITE_SUCCESS, "今日无交易");
                } catch (IOException e) {
                    Logger.error(this, "代销赎回文件生成失败，文件名：" + targetFile.getFileName());
                    jijinSyncFileRepository.updateBusJijinSyncFileStatus(targetFile.getId(), SyncFileStatus.WRITE_FAIL, "IO exception,生成文件失败");
                }
            } else {
                try {
                    genFile(targetFile.getFileName(), createFrom, createTo, count, false, types);
                    jijinSyncFileRepository.updateBusJijinSyncFile(MapUtils.buildKeyValueMap("id", targetFile.getId(), "status", SyncFileStatus.WRITE_SUCCESS.name(), "currentLine", count.getTotal() + 2));
                } catch (IOException e) {
                    Logger.error(this, "代销赎回文件生成失败，文件名：" + targetFile.getFileName());
                    jijinSyncFileRepository.updateBusJijinSyncFileStatus(targetFile.getId(), SyncFileStatus.WRITE_FAIL, "IO exception,生成文件失败");
                }
            }

        }

    }

    private void genFile(String fileName, Date createFrom, Date createTo, JijinTradeRecordCountDTO countDto, boolean isEmpty, List<String> types) throws IOException {

        long time = System.currentTimeMillis();
        Logger.info(this, String.format("start gen lfx redeem file,start time: [%s]ms", time));
        String tmpFileName = fileName + ".tmp";
        File tmpFile = FileUtils.createEmptyFile(tmpFileName);

        String header = RedeemFileContentUtil.createRedeemFileHeader(countDto.getTotal());
        String columnName = RedeemFileContentUtil.createRedeemFileColumnName();

        FileUtils.appendContent(tmpFile, header, "UTF-8");
        FileUtils.appendContent(tmpFile, columnName, "UTF-8");

        if (!isEmpty) {
            int start = 1;
            int end = countDto.getTotal();
            while (start <= end) {

                int targetEnd = 0;
                targetEnd = start + ROWNUM;

                List<JijinTradeRecordDTO> dtos = jijinTradeRecordRepository.getRecordsByCreatedAtAndTypes(createFrom, createTo, start, targetEnd, "lfx201", types);
                String content = "";
                for (JijinTradeRecordDTO dto : dtos) {
                    content = RedeemFileContentUtil.createRedeemFileContent(dto);
                    FileUtils.appendContent(tmpFile, content, "UTF-8");
                }
                start = targetEnd;
            }
        }

        tmpFile.renameTo(new File(fileName));
        Logger.info(this, String.format("end gen lfx redeem file,spend [%s]ms", System.currentTimeMillis() - time));
    }
    
    /**
     * 发送QUEUE MQ
     * 
     * @param jijinTradeRecordDTO
     */
    public void sendPaidInAdvanceRedeemMQ(JijinTradeRecordDTO jijinTradeRecordDTO){
    	mqService.sendPaidInAdvanceMQ(jijinTradeRecordDTO.getId());	
    }
    
    /**
     * 接收MQ
     * 补偿调用 异步流程中还未发给jijin公司的请求
     * @param jijinTradeRecordDTO
     */
    public void handlePaidInAdvanceRedeemRecord(JijinTradeRecordDTO jijinTradeRecordDTO){

        if (!TradeRecordStatus.SENDING.name().equals(jijinTradeRecordDTO.getStatus())) {
            //幂等
            return;
        }

        JijinAccountDTO accountDto = jijinAccountRepository.findActiveAccount(jijinTradeRecordDTO.getUserId(), jijinTradeRecordDTO.getInstId(), "PAF");
        //6.call GW
        Logger.info(this, String.format("[jijin-redeem-PIA]start call jijin gw for [userId: %s]!", jijinTradeRecordDTO.getUserId()));
        GWRedeemRequestGson request = new GWRedeemRequestGson();
        request.setApplicationNo(jijinTradeRecordDTO.getAppNo());
        request.setBusinessCode(JijinBizType.REDEEM.getCode());
        request.setContractNo(accountDto.getContractNo());
        request.setFundCode(jijinTradeRecordDTO.getFundCode());
        request.setInstId(jijinTradeRecordDTO.getInstId());
        request.setIsIndividual("1"); // 1 - 个人
        BigDecimal redeemAmount = jijinTradeRecordDTO.getReqShare().setScale(2, BigDecimal.ROUND_DOWN); // 保留两位小数， 例 100 -> 100.00
        request.setApplicationVol(redeemAmount.toString());
        request.setChargeType("A"); //A - 前收费
        request.setHugeSum("1");
        request.setVersion("1.0");
        //大华货基需要extension
        if (FundSaleCode.DHC.getInstId().equals(jijinTradeRecordDTO.getInstId())) {

            request.setExtension(DateUtils.formatDateAsCmsDrawSequence(new Date()), accountDto.getPayNo());
        } 
        request.setType(!jijinTradeRecordDTO.getRedeemType().equals("0")?"0":"1"); //1:T+0赎回,0：T+1普通赎回
        GWResponseGson res = gatewayService.redeem(jijinTradeRecordDTO.getInstId(), JsonHelper.toJson(request));
        Logger.info(this, String.format("[jijin-redeem-PIA]]jijin gateway response of redeem is [%s]!", JsonHelper.toJson(res)));

        if (res.getRetCode().equals(GWResponseCode.SUCCESS)) {
            GWRedeemResponseGson jijinRes = JsonHelper.fromJson(res.getContent(), GWRedeemResponseGson.class);
            // 返回提交成功
            if (jijinRes.getErrorCode().equals("0000")) {
                String appSheetNo = jijinRes.getAppSheetSerialNo();
                String trxDate = jijinRes.getTransactionDate();
                String trxTime = jijinRes.getTransactionTime();
                String errorCode = jijinRes.getErrorCode();
                String errorMsg = jijinRes.getErrorMessage();
                // update tradeRecord status and response's info
                jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "status", TradeRecordStatus.SUCCESS.name(),
                        "appSheetNo", appSheetNo, "trxDate", trxDate, "trxTime", trxTime, "errorCode", errorCode, "errorMsg", errorMsg));
                Logger.info(this, String.format("[jijin-redeem-PIA]call jijin gw success, and return redeem success with tradeRecordId:%s for user [userId: %s]!", jijinTradeRecordDTO.getId(), jijinTradeRecordDTO.getUserId()));
            } else {
                // submit response fail， 需要报警，打标人工干预，因为资金已经垫付，赎回是不能失败的，出现失败，也要人工检查并调整到成功
            	jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "status", TradeRecordStatus.FAIL.name(),
                        "errorCode", jijinRes.getErrorCode(), "errorMsg", jijinRes.getErrorMessage()+",需要人工干预，T0支付赎回必须成功", "isControversial", "1"));
                Logger.warn(this, String.format("[jijin-redeem-PIA]call jijin gw success, and return redeem fail with tradeRecordId:%s for user [userId: %s]!", jijinTradeRecordDTO.getId(), jijinTradeRecordDTO.getUserId()));
            }
        } else if (res.getRetCode().equals(GWResponseCode.LACK_CLIENT_CONFIG)
                || res.getRetCode().equals(GWResponseCode.SIGN_FAIL)
                || res.getRetCode().equals(GWResponseCode.VALIDATE_SIGN_FAIL)) {
            Logger.error(this, String.format("[jijin-redeem-PIA]system env fatal error,need check [call gateway for redeem] code=[%s]", res.getRetCode()));
            jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "status", TradeRecordStatus.FAIL.name(),
                     "errorMsg", "环境配置有问题，需要人工干预，T0支付赎回必须成功", "isControversial", "1"));
        } else if (res.getRetCode().equals(GWResponseCode.RUNTIME_ERROR)
                || res.getRetCode().equals(GWResponseCode.JIJIN_RESPONSE_ERROR)) {
            Logger.info(this, String.format("[jijin-redeem-PIA]response error [call gateway for redeem] code=[%s]", res.getRetCode()));
            // 检查重试次数
            long times = jijinTradeRecordDTO.getRetryTimes() + 1;
            if (times > 5) {
                //如果大于5次无响应，置为需要人工处理，状态保持INIT
                jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "retryTimes", times, "errorMsg", "重试5次无响应,需要人工干预", "isControversial", "1"));
            } else {
                // 由MQ补偿重试
                mqService.sendPaidInAdvanceMQ(jijinTradeRecordDTO.getId());
                jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "retryTimes", jijinTradeRecordDTO.getRetryTimes() + 1));
            }
            Logger.info(this, String.format("[jijin-redeem-PIA]jijin GW runtime error，由MQ补偿重试  with tradeRecordId:%s for user [userId: %s]!", jijinTradeRecordDTO.getId(), jijinTradeRecordDTO.getUserId()));
        }
    
    	
    	
    	
    }
}
