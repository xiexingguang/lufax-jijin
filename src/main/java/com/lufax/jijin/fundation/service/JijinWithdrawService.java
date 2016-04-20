package com.lufax.jijin.fundation.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.lufax.jersey.utils.Logger;
import com.lufax.jijin.base.repository.BizParametersRepository;
import com.lufax.jijin.base.utils.ConstantsHelper;
import com.lufax.jijin.base.utils.DateUtils;
import com.lufax.jijin.base.utils.JijinAppProperties;
import com.lufax.jijin.base.utils.JsonHelper;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.fundation.constant.DahuaAccountTypeEnum;
import com.lufax.jijin.fundation.constant.JijinBizType;
import com.lufax.jijin.fundation.constant.JijinConstants;
import com.lufax.jijin.fundation.constant.JijinRedeemStatus;
import com.lufax.jijin.fundation.constant.JijinWithdrawStatus;
import com.lufax.jijin.fundation.dto.JijinInfoDTO;
import com.lufax.jijin.fundation.dto.JijinRedeemBalHisDTO;
import com.lufax.jijin.fundation.dto.JijinRedeemThresholdDTO;
import com.lufax.jijin.fundation.dto.JijinWithdrawRecordDTO;
import com.lufax.jijin.fundation.remote.FundAppCallerService;
import com.lufax.jijin.fundation.remote.JijinGatewayRemoteService;
import com.lufax.jijin.fundation.remote.gson.response.GWQueryDahuaBalanceResponseGson;
import com.lufax.jijin.fundation.remote.gson.response.GWResponseCode;
import com.lufax.jijin.fundation.remote.gson.response.GWResponseGson;
import com.lufax.jijin.fundation.repository.JijinInfoRepository;
import com.lufax.jijin.fundation.repository.JijinRedeemBalHisRepository;
import com.lufax.jijin.fundation.repository.JijinRedeemThresholdRepository;
import com.lufax.jijin.fundation.repository.JijinWithdrawRecordRepository;
import com.lufax.jijin.service.MqService;
import com.lufax.jijin.sysFacade.gson.result.BaseResultDTO;
import com.lufax.jijin.user.domain.BankAccountDetailGson;
import com.lufax.jijin.user.service.UserService;
import com.site.lookup.util.StringUtils;


@Service
public class JijinWithdrawService {
    @Autowired
    private JijinWithdrawRecordRepository jijinWithdrawRecordRepository;
    @Autowired
    private FundAppCallerService fundAppCallerService;
    @Autowired
    private SequenceService sequenceService;
    @Autowired
    private MqService mqService;
    @Autowired
    JijinGatewayRemoteService jijinGatewayRemoteService;
    @Autowired
    private BizParametersRepository bizParametersRepository;
    @Autowired
    private JijinRedeemThresholdRepository jijinRedeemThresholdRepository;
    @Autowired
    private JijinInfoRepository jijinInfoRepository;
    @Autowired
    private JijinAppProperties jijinAppProperties;
    @Autowired
    private UserService userService;
    @Autowired
    private JijinDahuaRedisService jijinDahuaRedisService;
    @Autowired
    private JijinRedeemBalHisRepository jijinRedeemBalHisRepository;

    public void setFundAppCallerService(FundAppCallerService fundAppCallerService) {
		this.fundAppCallerService = fundAppCallerService;
	}

	public void setMqService(MqService mqService) {
		this.mqService = mqService;
	}

	public void setJijinGatewayRemoteService(
			JijinGatewayRemoteService jijinGatewayRemoteService) {
		this.jijinGatewayRemoteService = jijinGatewayRemoteService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	/**
	 * 发起大华货基中间户代扣
	 * @return
	 */
	public void createWithdrawRecord() {

        Logger.info(this, String.format("[createWithdrawRecord] process start"));
        BigDecimal dahuaBalance = BigDecimal.ZERO;

        //1、获取大华中间户可扣金额
        try {
            GWResponseGson gwResponseGson = jijinGatewayRemoteService.getDahuaBalance(DahuaAccountTypeEnum.中间户.getAccountType());
            Logger.info(this, String.format("[createWithdrawRecord] getDahuaBalance response is [%s]", new Gson().toJson(gwResponseGson)));
            if (gwResponseGson != null && GWResponseCode.SUCCESS.equals(gwResponseGson.getRetCode())) {
                GWQueryDahuaBalanceResponseGson remoteResponseGson = JsonHelper.fromJson(gwResponseGson.getContent(), GWQueryDahuaBalanceResponseGson.class);
                if (remoteResponseGson != null) {
                    dahuaBalance = new BigDecimal(remoteResponseGson.getBalance());
                }
            }
        } catch (Exception e) {
            Logger.warn(this, "大华货基查询中间户失败，since "+ e.getClass().getName());
        }

        Long productId = 0l;
        JijinInfoDTO jijinInfoDTO = jijinInfoRepository.findJijinInfoByFundCode(JijinConstants.DAHUA_FUND_CODE);
        if (jijinInfoDTO != null) {
            productId = jijinInfoDTO.getProductId();
        }
        //2、发起对公代扣
        if (dahuaBalance != null && dahuaBalance.compareTo(BigDecimal.ZERO) > 0) {
            JijinWithdrawRecordDTO jijinWithdrawRecordDTO = new JijinWithdrawRecordDTO();
            jijinWithdrawRecordDTO.setProductId(productId);

            Long dahuaSpvUserId = 0l;
            try {
                String bizValue = bizParametersRepository.findValueByCode(JijinConstants.JIJIN_DAHUA_ACCOUNT_ID_BIZ_PARAMETER_CODE);
                dahuaSpvUserId = new Long(bizValue);
            } catch (Exception e) {
                Logger.error(this, String.format("get dahua account id exception"));
                return;
            }

            jijinWithdrawRecordDTO.setTradeUserId(dahuaSpvUserId);

            jijinWithdrawRecordDTO.setType(2);//代扣
            jijinWithdrawRecordDTO.setOperateDate(DateUtils.formatDateTime(new Date()));
            jijinWithdrawRecordDTO.setRequestAmount(dahuaBalance);
            jijinWithdrawRecordDTO.setVersion(0);
            jijinWithdrawRecordDTO.setStatus("INIT");

            String recordId = sequenceService.getSerialNumber(JijinBizType.DAHUA_WITHDRAW_RECHARGE.getCode());
            jijinWithdrawRecordDTO.setRecordId(recordId);
            jijinWithdrawRecordDTO.setRemark("");

            BankAccountDetailGson bankAccountDetailGson = userService.bankAccountDetail(dahuaSpvUserId);
            Logger.info(this, String.format("[createWithdrawRecord] bankAccountDetail is [%s]", new Gson().toJson(bankAccountDetailGson)));
            jijinWithdrawRecordDTO.setTradeAccountNo(bankAccountDetailGson.getId());

            Logger.info(this, String.format("[createWithdrawRecord] jijinWithdrawRecordDTO is [%s]", new Gson().toJson(jijinWithdrawRecordDTO)));
            //发起对公代扣
            BaseResultDTO baseResultDTO = fundAppCallerService.rechargeDahua(
                    jijinWithdrawRecordDTO.getTradeUserId(),
                    jijinWithdrawRecordDTO.getRecordId(),
                    jijinWithdrawRecordDTO.getRequestAmount(),
                    jijinWithdrawRecordDTO.getTradeAccountNo());

            Logger.info(this, String.format("[createWithdrawRecord] rechargeDahua response is [%s]", new Gson().toJson(baseResultDTO)));

            //代扣消息发送成功，落地记录
            if (baseResultDTO.isSuccess()) {
            	Logger.info(this, String.format("[createWithdrawRecord] insertJijinWithdrawRecord newRecord"));           		
                jijinWithdrawRecordRepository.insertJijinWithdrawRecord(jijinWithdrawRecordDTO);
            }
        }
    }

    @Transactional
    public void handlePaymentResult(String recordId, String type, String status, BigDecimal amount, BigDecimal actualAmount, String channelId) {
        Logger.info(this, String.format("[handlePaymentResult] process start.parameters is [%s]|[%s]|[%s]|[%s]|[%s]|[%s]", recordId, type, status, amount, actualAmount, channelId));
        if (!JijinConstants.RECHARGE_CHANNEL_ID.equals(channelId)) {
            Logger.info(this, String.format("[handlePaymentResult] this message not for jijin-app. message ignore,parameter is [%s|%s]", channelId, type));
            return;
        }
        JijinWithdrawRecordDTO jijinWithdrawRecordDTO = jijinWithdrawRecordRepository.getJijinWithdrawRecordByRecordId(recordId);
        if (jijinWithdrawRecordDTO == null || !jijinWithdrawRecordDTO.getStatus().equals(JijinWithdrawStatus.INIT)) {
            Logger.warn(this, String.format("[handlePaymentResult] can not find jijinWithdrawRecord  message ignore,recordId [%s]", recordId));
            return;
        }
        
        //处理代扣结果
        if (JijinConstants.RECHARGE_NAME.equals(type)) {
            Date date = new Date();
            String withdrawStatus = JijinWithdrawStatus.FAIL;

            actualAmount = null == actualAmount ? BigDecimal.ZERO : actualAmount;//本次扣成功金额
            BigDecimal rechargeAmount = jijinWithdrawRecordDTO.getRequestAmount();//理论总代扣金额

            jijinWithdrawRecordDTO.setSuccessAmount(actualAmount);//实际已扣金额

            //如果代扣金额(理论总金额)不等于实际累计已扣金额，视为失败
            if (status.equals("success")) {
                if (rechargeAmount.compareTo(actualAmount) == 0) {
                    withdrawStatus = JijinWithdrawStatus.SUCCESS;
                } else {
                    withdrawStatus = JijinWithdrawStatus.PART_SUCCESS;
                }

                //发送大华基金虚拟户资金变动消息
                mqService.sendDahuaAccountBalanceChange(JijinConstants.DAHUA_FUND_CODE, "1");
                
                // 插redis和流水表
                try{
                	JijinRedeemBalHisDTO jijinRedeemBalHisDTO = new JijinRedeemBalHisDTO();
                	jijinRedeemBalHisDTO.setAmount(actualAmount);
                	jijinRedeemBalHisDTO.setRemark("代扣成功");
                	jijinRedeemBalHisDTO = jijinRedeemBalHisRepository.insertBusJijinRedeemBalHis(jijinRedeemBalHisDTO);
                	
                	jijinDahuaRedisService.changeRedisValue(actualAmount);
                }catch(Exception e){
                	//any exception,no rollback
                	Logger.error(this, "大华代扣更新redis，插流水失败"+e.getClass().getName());
                }
            } else {
                withdrawStatus = JijinWithdrawStatus.FAIL;
            }

            //更新代扣结果记录
            Map map = MapUtils.buildKeyValueMap(
                    "id", jijinWithdrawRecordDTO.getId(),
                    "status", withdrawStatus,
                    "version", jijinWithdrawRecordDTO.getVersion(),
                    "successAmount", actualAmount);
            int i = jijinWithdrawRecordRepository.updateJijinWithdrawRecord(map);
            Logger.info(this, String.format("[handlePaymentResult] updateJijinWithdrawRecord result is [%s],updMap is[%s]", i, new Gson().toJson(map)));
        } else {
            Logger.info(this, String.format("[handlePaymentResult] this message not for recharge. message ignore,parameter is [%s|%s]", channelId, type));
        }
    }

    /**
     * 定时查询大华垫资户的金额，更改状态
     */
    public void handleDaHuaAccountStatus() {
        try {
            GWResponseGson gwResponseGson = jijinGatewayRemoteService.getDahuaBalance(DahuaAccountTypeEnum.垫资户.getAccountType());
            if (gwResponseGson != null && GWResponseCode.SUCCESS.equals(gwResponseGson.getRetCode())) {
                GWQueryDahuaBalanceResponseGson remoteResponseGson = JsonHelper.fromJson(gwResponseGson.getContent(), GWQueryDahuaBalanceResponseGson.class);
                if (remoteResponseGson != null) {
                    BigDecimal dahuaBalance = new BigDecimal(remoteResponseGson.getBalance());
                    JijinRedeemThresholdDTO dto = jijinRedeemThresholdRepository.getRedeemThresholdByFundCode(JijinConstants.DAHUA_FUND_CODE);
                    String switchLevel = bizParametersRepository.findValueByCode(ConstantsHelper.JIJIN_DAHUA_ACCOUNT_STATUS_CHANGE_SWITCH);
                    if (StringUtils.isEmpty(switchLevel)) {
                        switchLevel = "0";
                    }
                    if (null != dto && JijinRedeemStatus.CLOSE.name().equals(dto.getAccountStatus()) && dahuaBalance.compareTo(new BigDecimal(switchLevel)) > 0) {
                        jijinRedeemThresholdRepository.updateDahuaAccountStatusByFundCode(JijinConstants.DAHUA_FUND_CODE, JijinRedeemStatus.OPEN.name());
                    } else if (null != dto && JijinRedeemStatus.OPEN.name().equals(dto.getAccountStatus()) && dahuaBalance.compareTo(new BigDecimal(switchLevel)) < 0) {
                        jijinRedeemThresholdRepository.updateDahuaAccountStatusByFundCode(JijinConstants.DAHUA_FUND_CODE, JijinRedeemStatus.CLOSE.name());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
