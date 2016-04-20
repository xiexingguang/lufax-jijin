package com.lufax.jijin.fundation.service;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.lufax.jijin.base.repository.BizParametersRepository;
import com.lufax.jijin.base.service.TradeDayService;
import com.lufax.jijin.base.utils.DateUtils;
import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.fundation.constant.BalanceHistoryBizType;
import com.lufax.jijin.fundation.constant.FundSaleCode;
import com.lufax.jijin.fundation.constant.JijinBizType;
import com.lufax.jijin.fundation.constant.JijinConstants;
import com.lufax.jijin.fundation.constant.JijinRedeemStatus;
import com.lufax.jijin.fundation.constant.RedeemTypeEnum;
import com.lufax.jijin.fundation.constant.TradeRecordStatus;
import com.lufax.jijin.fundation.constant.TradeRecordType;
import com.lufax.jijin.fundation.constant.YebTransactionType;
import com.lufax.jijin.fundation.dto.JijinAccountDTO;
import com.lufax.jijin.fundation.dto.JijinFrozenDetailDTO;
import com.lufax.jijin.fundation.dto.JijinInfoDTO;
import com.lufax.jijin.fundation.dto.JijinRedeemBalHisDTO;
import com.lufax.jijin.fundation.dto.JijinRedeemThresholdDTO;
import com.lufax.jijin.fundation.dto.JijinTradeLogDTO;
import com.lufax.jijin.fundation.dto.JijinTradeRecordDTO;
import com.lufax.jijin.fundation.dto.JijinUserBalanceDTO;
import com.lufax.jijin.fundation.dto.JijinUserBalanceHistoryDTO;
import com.lufax.jijin.fundation.dto.builder.JijinUserBalenceHistoryDTOBuilder;
import com.lufax.jijin.fundation.exception.DahuaReidsException;
import com.lufax.jijin.fundation.repository.JijinFrozenDetailRepository;
import com.lufax.jijin.fundation.repository.JijinRedeemBalHisRepository;
import com.lufax.jijin.fundation.repository.JijinRedeemThresholdRepository;
import com.lufax.jijin.fundation.repository.JijinTradeLogRepository;
import com.lufax.jijin.fundation.repository.JijinTradeRecordRepository;
import com.lufax.jijin.fundation.repository.JijinUserBalanceHistoryRepository;
import com.lufax.jijin.fundation.repository.JijinUserBalanceRepository;
import com.lufax.jijin.product.dto.ProductDTO;
import com.lufax.jijin.product.repository.ProductRepository;
import com.lufax.jijin.service.MqService;
import com.lufax.mq.client.util.MapUtils;
import com.site.lookup.util.StringUtils;

@Component
public class RedeemServiceUtil {
    @Autowired
    JijinTradeLogRepository jijinTradeLogRepository;
    @Autowired
    private JijinUserBalanceRepository jijinUserBalanceRepository;
    @Autowired
    private JijinUserBalanceHistoryRepository jijinUserBalanceHistoryRepository;
    @Autowired
    private SequenceService sequenceService;
    @Autowired
    TradeDayService tradeDayService;
    @Autowired
    private JijinDahuaRedisService jijinDahuaRedisService;
    @Autowired
    private JijinRedeemBalHisRepository jijinRedeemBalHisRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private JijinFrozenDetailRepository jijinFrozenDetailRepository;
    @Autowired
    private MqService mqService;
    @Autowired
    private JijinTradeRecordRepository jijinTradeRecordRepository;
    @Autowired
    private JijinRedeemThresholdRepository jijinRedeemThresholdRepository;
    @Autowired
    private BizParametersRepository bizParametersRepository;

	@Transactional
    public long recordRedeem(JijinUserBalanceDTO jijinUserBalanceDTO, JijinInfoDTO infoDto, JijinAccountDTO accountDto,
                             Long userId, String fundCode, Long transactionId, BigDecimal redeemAmount, String redeemType, String ukToken,
                             String prodCode,String channel,String transferType,String remark) {

        long result = jijinUserBalanceRepository.updateFundAccount(jijinUserBalanceDTO);

        // tradeRecord 落地，send MQ,异步化处理
        if (result == 1) {
            String serialNum = sequenceService.getSerialNumber(JijinBizType.REDEEM.getCode());
            String isControversial = "0";
            if (FundSaleCode.DHC.getInstId().equals(infoDto.getInstId()))
                isControversial = "-1";

            /*
             * 异常开关， 只对大华货基T0赎回
             * 
             * 打开 走异步流程
             * 关闭 走正常流程
             */
            boolean isAbnormal = false;
            if (JijinConstants.DAHUA_FUND_CODE.equals(fundCode) && RedeemTypeEnum.FAST.getTypeCode().equals(redeemType)) {
    			String value =bizParametersRepository.findValueByCode("dahua.async.redeem.switch") ;
    			if(null==value || "00".equals(value)){
    				isAbnormal = false;
    			}else{
    				isAbnormal = true;
    			}
            }
   
            Long tradeRecordId=0l;
            if(isAbnormal){
            	tradeRecordId = insertRedeemTradeRecord(infoDto.getInstId(), channel, userId, fundCode,
                        TradeRecordStatus.PAY_IN_ADVANCE.name(), redeemAmount, accountDto.getContractNo(), serialNum, null, DateUtils.formatDate(new Date(),DateUtils.DATE_STRING_FORMAT),
                        null, null, null, isControversial, false, transactionId,
                        redeemType, ukToken, prodCode,transferType,remark);
            }else{
            	tradeRecordId = insertRedeemTradeRecord(infoDto.getInstId(), channel, userId, fundCode,
                        TradeRecordStatus.INIT.name(), redeemAmount, accountDto.getContractNo(), serialNum, null, DateUtils.formatDate(new Date(),DateUtils.DATE_STRING_FORMAT),
                        null, null, null, isControversial, false, transactionId,
                        redeemType, ukToken, prodCode,transferType,remark);
            }


            String trxDate = DateUtils.formatDate(tradeDayService.getTargetTradeDay(new Date()), DateUtils.DATE_STRING_FORMAT);

            JijinUserBalanceHistoryDTO jijinUserBalanceHistoryDTO = JijinUserBalenceHistoryDTOBuilder.buildUserBalanceHistoryDTO(jijinUserBalanceDTO, trxDate, new BigDecimal("0"), redeemAmount, "赎回，冻结份额", BalanceHistoryBizType.赎回下单成功, tradeRecordId);
            jijinUserBalanceHistoryRepository.insertBusJijinUserBalanceHistory(jijinUserBalanceHistoryDTO);

            //大华T0赎回, redis & 流水, 任何一个失败则回滚
            if (FundSaleCode.DHC.getInstId().equals(infoDto.getInstId()) && RedeemTypeEnum.FAST.getTypeCode().equals(redeemType) ) {
	            JijinRedeemBalHisDTO jijinRedeemBalHisDTO = new JijinRedeemBalHisDTO();
	            jijinRedeemBalHisDTO.setAmount(redeemAmount.multiply(new BigDecimal(-1)));
	            jijinRedeemBalHisDTO.setRemark("赎回");
	            jijinRedeemBalHisRepository.insertBusJijinRedeemBalHis(jijinRedeemBalHisDTO);
	            long res = jijinDahuaRedisService.changeRedisValue(redeemAmount.multiply(new BigDecimal(-1)));
	            if(res<0){
	            	throw new DahuaReidsException("redis balance is not enough");
	            }
            }
            
            //大华基金赎回的备注需要和1、2期调用account做赎回冻结时备注一致，所以下面的备注写死，与中途岛T+0支付的备注做区别
            if(StringUtils.isEmpty(remark)){
                remark = "赎回至陆金所账户";
                if ("MIDWAY".equals(channel)) {
                    ProductDTO productDTO = productRepository.getByCode(prodCode);
                    remark = "支付赎回，用于支付" + productDTO.getDisplayName();
                }
            }
            JijinFrozenDetailDTO jijinFrozenDetailDTO = new JijinFrozenDetailDTO(YebTransactionType.SELL_FREEZE.getCode(), jijinUserBalanceDTO.id(), redeemAmount, null, jijinUserBalanceDTO.getFrozenShare(), remark);
            jijinFrozenDetailRepository.insertJijinFrozenDetail(jijinFrozenDetailDTO);


            //判断是否大华T0赎回
            if (FundSaleCode.DHC.getInstId().equals(infoDto.getInstId())&& RedeemTypeEnum.FAST.getTypeCode().equals(redeemType) ) {
                mqService.sendJijinT0RedeemMQ(tradeRecordId);
            } else {
                mqService.sendJijinRedeemMQ(tradeRecordId);
            }
            result = tradeRecordId;

        }

        return result;
    }
	
	
	 @Transactional
	    public void unFreezeAndUpdateStatusSubmitFail(JijinTradeRecordDTO tradeRecord, String errorCode, String errorMsg, String trxDate, String trxTime) {

	        BigDecimal amount = null;
	        amount = tradeRecord.getReqShare();

	        boolean result = unFrezee(amount, tradeRecord.getUserId(), tradeRecord.getFundCode());
	        if (!result) {
	            Logger.info(this, String.format("赎回解冻失败,tradeRecordId is [%s]", tradeRecord.getId()));
	            jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", tradeRecord.getId(), "status", TradeRecordStatus.SUBMIT_FAIL.name(), "errorMsg", "赎回解冻失败,需要人工解冻赎回份额或金额", "isControversial", "1"
	                    , "errorCode", errorCode, "errorMsg", errorMsg));
	        } else {
	            jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", tradeRecord.getId(), "status", TradeRecordStatus.SUBMIT_FAIL.name()
	                    , "errorCode", errorCode, "errorMsg", errorMsg));
	        }

	        insertTradeLog(tradeRecord, TradeRecordStatus.SUBMIT_FAIL, trxDate, trxTime);
	    }
	 
	    /**
	     * 解冻务必要成功，所以多试几次
	     *
	     * @param redeemAmount
	     * @param userId
	     * @param fundCode
	     * @return
	     */
	    public boolean unFrezee(BigDecimal redeemAmount, long userId, String fundCode) {
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
	    
	    private void insertTradeLog(JijinTradeRecordDTO tradeRecord, TradeRecordStatus status, String trxDate, String trxTime) {

	        BigDecimal amount = null;
	        if (tradeRecord.getReqShare() != null) {
	            amount = tradeRecord.getReqShare();
	        } else {
	            amount = tradeRecord.getReqAmount();
	        }

	        jijinTradeLogRepository.insertBusJijinTradeLog(new JijinTradeLogDTO(tradeRecord.getUserId(), tradeRecord.getFundCode(), tradeRecord.getId(), status.name(), TradeRecordType.REDEEM, null, amount, trxTime, trxDate));
	    }

	    public long insertRedeemTradeRecord(String instId, String channel, long userId, String fundCode, String status, BigDecimal reqAmount, String contractNo,
	                                        String appNo, String appSheetNo, String trxDate, String trxTime, String errorCode, String errorMsg, String isControversial, boolean addLog,
	                                        Long trxId, String redeemType, String ukToken, String prodCode,String transferType,String remark) {


	        JijinTradeRecordDTO jijinTradeRecordDTO = new JijinTradeRecordDTO();

	        jijinTradeRecordDTO.setChannel(channel);
	        jijinTradeRecordDTO.setUserId(userId);
	        jijinTradeRecordDTO.setFundCode(fundCode);
	        jijinTradeRecordDTO.setStatus(status);
	        jijinTradeRecordDTO.setType(TradeRecordType.REDEEM);
	        jijinTradeRecordDTO.setReqShare(reqAmount);
	        jijinTradeRecordDTO.setTransferType(transferType);
	        jijinTradeRecordDTO.setContractNo(contractNo);
	        jijinTradeRecordDTO.setAppNo(appNo);
	        jijinTradeRecordDTO.setChargeType("A");
	        jijinTradeRecordDTO.setInstId(instId);

	        //以下字段只有在jijin response is success，才需要填充
	        jijinTradeRecordDTO.setAppSheetNo(appSheetNo);
	        jijinTradeRecordDTO.setTrxDate(trxDate);
	        jijinTradeRecordDTO.setTrxTime(trxTime);
	        jijinTradeRecordDTO.setErrorCode(errorCode);
	        jijinTradeRecordDTO.setErrorMsg(errorMsg);
	        jijinTradeRecordDTO.setIsControversial(isControversial);

	        jijinTradeRecordDTO.setTrxId(trxId);
	        jijinTradeRecordDTO.setRedeemType(redeemType);
	        jijinTradeRecordDTO.setUkToken(ukToken);
	        jijinTradeRecordDTO.setProdCode(prodCode);
	        jijinTradeRecordDTO.setRemark(remark);
	        
	        JijinTradeRecordDTO result = jijinTradeRecordRepository.insertJijinTradeRecord(jijinTradeRecordDTO);

	        if (addLog) {
	            jijinTradeLogRepository.insertBusJijinTradeLog(new JijinTradeLogDTO(userId, fundCode, result.getId(), status, TradeRecordType.REDEEM, null, reqAmount, trxTime, trxDate));
	        }

	        return result.getId();
	    }
	    

	    /**
	     * 判断大华货基能否赎回
	     *
	     * @param fundCode
	     * @return
	     */
	    public String canBeRedeemed(String fundCode) {
	        if (StringUtils.isEmpty(fundCode)) {
	            return "00";
	        }
	        JijinRedeemThresholdDTO dto = jijinRedeemThresholdRepository.getRedeemThresholdByFundCode(fundCode);
	        if (null == dto) {
	            return "00";
	        }
	        if (JijinRedeemStatus.CLOSE.name().equals(dto.getCurrentStatus().toUpperCase())) {
	            if (JijinRedeemStatus.CLOSE.name().equals(dto.getAccountStatus().toUpperCase())) {
	                return "02";//下个交易日再试
	            } else {
	                return "01";//稍后再试
	            }
	        } else {
	            if (JijinRedeemStatus.CLOSE.name().equals(dto.getAccountStatus().toUpperCase())) {
	                return "02";//下个交易日再试
	            }
	            return "00";//允许赎回
	        }
	    }


	    /**
	     * 提交失败，解冻份额，插log
	     */
	    @Transactional
	    public void unfreezeRedeem(JijinTradeRecordDTO jijinTradeRecordDTO, BigDecimal redeemAmount, String errorCode, String errorMsg) {

	        boolean unFreezeResult = unFrezee(redeemAmount, jijinTradeRecordDTO.getUserId(), jijinTradeRecordDTO.getFundCode());

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

	            // insert user balance history
	            JijinUserBalanceHistoryDTO jijinUserBalanceHistoryDTO = JijinUserBalenceHistoryDTOBuilder.buildUserBalanceHistoryDTO(jijinUserBalanceDTO,jijinTradeRecordDTO.getTrxDate(),jijinTradeRecordDTO.getReqShare(),new BigDecimal("0"),"赎回失败，调减冻结份额，调增份额",BalanceHistoryBizType.赎回实时失败,jijinTradeRecordDTO.getId());
	            jijinUserBalanceHistoryRepository.insertBusJijinUserBalanceHistory(jijinUserBalanceHistoryDTO);
	        }

	        //根据渠道，发送消息给陆金宝或midway
	        if ("MIDWAY".equals(jijinTradeRecordDTO.getChannel())) {
	            mqService.sendJijinRedeemPayResultMQ(jijinTradeRecordDTO.getTrxId(), null, "fail");
	        } else if("YEB".equals(jijinTradeRecordDTO.getChannel())) {
	            mqService.sendDhTradeResultMsg(jijinTradeRecordDTO.getTrxId(), "fail");
	        }else if("LBO".equals(jijinTradeRecordDTO.getChannel())) {
	        	mqService.sendLBOTradeResultMsg(jijinTradeRecordDTO.getTrxId(), "fail","REDEEM","010");
	        }

	        //大华货基 , +redis & 流水
	        try{
		        if (FundSaleCode.DHC.getInstId().equals(jijinTradeRecordDTO.getInstId())) {
		            JijinRedeemBalHisDTO jijinRedeemBalHisDTO = new JijinRedeemBalHisDTO();
		            jijinRedeemBalHisDTO.setAmount(redeemAmount);
		            jijinRedeemBalHisDTO.setRemark("赎回失败解冻");
		            jijinRedeemBalHisRepository.insertBusJijinRedeemBalHis(jijinRedeemBalHisDTO);
		            jijinDahuaRedisService.changeRedisValue(redeemAmount);
		        }
	        }catch(Exception e){
	        	Logger.warn(this, "[jijin-redeem][submit fail, then unfreeze balance, but update hahua redis and redis balance failed]");
	        }
	        Logger.info(this, String.format("[jijin-redeem]submit response fail，解冻份额，with tradeRecordId:%s for user [userId: %s]!", jijinTradeRecordDTO.getId(), jijinTradeRecordDTO.getUserId()));
	    }

}
