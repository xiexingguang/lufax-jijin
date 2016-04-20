package com.lufax.jijin.fundation.service;

import com.google.gson.Gson;
import com.lufax.jijin.base.utils.DateUtils;
import com.lufax.jijin.base.utils.JsonHelper;
import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.fundation.constant.BalanceHistoryBizType;
import com.lufax.jijin.fundation.constant.JijinBizType;
import com.lufax.jijin.fundation.constant.TradeRecordStatus;
import com.lufax.jijin.fundation.constant.TradeRecordType;
import com.lufax.jijin.fundation.dto.*;
import com.lufax.jijin.fundation.dto.builder.JijinUserBalenceHistoryDTOBuilder;
import com.lufax.jijin.fundation.gson.BaseGson;
import com.lufax.jijin.fundation.remote.JijinGatewayRemoteService;
import com.lufax.jijin.fundation.remote.gson.request.GWCancelOrderRequestGson;
import com.lufax.jijin.fundation.remote.gson.response.GWCancelOrderResponseGson;
import com.lufax.jijin.fundation.remote.gson.response.GWResponseGson;
import com.lufax.jijin.fundation.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

/**
 * 转购
 *
 * @author chenqunhui
 */
@Service
public class RedeemToPurchaseService {

    @Autowired
    private JijinUserBalanceRepository jijinUserBalanceRepository;
    @Autowired
    private JijinGatewayRemoteService jijinGatewayService;
    @Autowired
    private SequenceService sequenceService;
    @Autowired
    private JijinTradeLogRepository jijinTradeLogRepository;
    @Autowired
    private JijinTradeRecordRepository jijinTradeRecordRepository;
    @Autowired
    private JijinUserBalanceHistoryRepository jijinUserBalanceHistoryRepository;
    @Autowired
    private JijinInfoRepository jijinInfoRepository;

    /**
     * 检查是否可以撤销
     *
     * @param jijinTradeRecordDTO
     * @param userId
     * @return
     */
    public BaseGson checkCanBeCancelOrNot(JijinTradeRecordDTO jijinTradeRecordDTO, Long userId) {
        BaseGson baseGson = new BaseGson();
        baseGson.setRetCode("00"); // can cancel
        baseGson.setRetMessage("can cancel");
        //（１）幂等检查
        if (jijinTradeRecordDTO.getStatus().equals(TradeRecordStatus.CANCEL_SUCCESS.name())) {
            baseGson.setRetCode("00");
            baseGson.setRetMessage("cancel RedeemToPurchase success");
            return baseGson;
        }
        //（２）订单只能由下单用户撤销
        if (!jijinTradeRecordDTO.getUserId().equals(userId)) {
            baseGson.setRetCode("01"); // can not cancel
            baseGson.setRetMessage("not belong to current user,can not cancel");
            return baseGson;
        }

        //（３）转购申购才能撤单，认购不允许
        if (TradeRecordType.HZ_PURCHASE != jijinTradeRecordDTO.getType()) {
            baseGson.setRetCode("01"); // can not cancel
            baseGson.setRetMessage("type not correct, can not cancel");
            return baseGson;
        }

        //（４）转购的下单成功状态为SUBMIT_SUCCESS,为其他状态时不允许撤销
        if (!TradeRecordStatus.SUBMIT_SUCCESS.name().equals(jijinTradeRecordDTO.getStatus())) {
            baseGson.setRetCode("01"); // can not cancel
            return baseGson;
        }

        //（５）如果是交易日的14：59：30后，则不能撤单
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
        return baseGson;
    }


    /**
     * 调用ＧＷ，撤销转购
     *
     * @param fundCode
     * @param jijinTradeRecordDTO
     * @return
     */
    public GWResponseGson callGw(String fundCode, JijinTradeRecordDTO jijinTradeRecordDTO) {
        JijinInfoDTO info = jijinInfoRepository.findJijinInfoByFundCode(fundCode);
        String cancelAppNo = sequenceService.getSerialNumber(JijinBizType.CANCEL_ORDER.getCode());
        GWCancelOrderRequestGson request = new GWCancelOrderRequestGson();
        request.setVersion("1.0");
        request.setInstId(info.getInstId());
        request.setIsIndividual("1");
        request.setContractNo(jijinTradeRecordDTO.getContractNo());
        request.setApplicationNo(cancelAppNo);
        request.setOriginalAppSheetNo(jijinTradeRecordDTO.getAppSheetNo());
        GWResponseGson res = jijinGatewayService.cancel(info.getInstId(), JsonHelper.toJson(request));
        return res;
    }

    /**
     * 基金公司返回撤单成功
     *
     * @param tradeRecord 早请份额就是需要解冻的份额，同赎回
     * @param userId
     */
    @Transactional
    public void cancelSuccess(JijinTradeRecordDTO tradeRecord, Long userId, GWCancelOrderResponseGson jijinRes) {
        //解冻sourceFundCode的份额,写history
        boolean unFrezeeSuccess = this.unFrezeeForCancelOrder(tradeRecord, tradeRecord.getReqAmount(), userId, tradeRecord.getSourceFundCode());
        if (unFrezeeSuccess) {
            //解冻成功,插撤销fundCode转购的tradeLog、更新tradeRecord
            jijinTradeLogRepository.insertBusJijinTradeLog(new JijinTradeLogDTO(tradeRecord.getUserId(), tradeRecord.getFundCode(), tradeRecord.getId(), TradeRecordStatus.CANCEL_SUCCESS.name(), tradeRecord.getType(), tradeRecord.getReqAmount(), tradeRecord.getTrxTime(), tradeRecord.getTrxDate()));
            jijinTradeLogRepository.insertBusJijinTradeLog(new JijinTradeLogDTO(tradeRecord.getUserId(), tradeRecord.getSourceFundCode(), tradeRecord.getId(), TradeRecordStatus.CANCEL_SUCCESS.name(), TradeRecordType.HZ_REDEEM, tradeRecord.getReqAmount(), tradeRecord.getReqAmount(), tradeRecord.getTrxTime(), tradeRecord.getTrxDate()));

            jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", tradeRecord.getId(), "errorCode", jijinRes.getErrorCode(), "cancelAppNo", jijinRes.getApplicationNo(), "cancelAppSheetNo", jijinRes.getAppSheetSerialNo(), "status", TradeRecordStatus.CANCEL_SUCCESS.name()));

        } else {
            //解冻失败,打标人工干预
            Logger.info(this, String.format("do cancel redeemToPurchase success,but unfreeze fail,,NEED MANUAL ATTENTION,tradeRecordId [%s]", tradeRecord.getId()));
            jijinTradeLogRepository.insertBusJijinTradeLog(new JijinTradeLogDTO(tradeRecord.getUserId(), tradeRecord.getFundCode(), tradeRecord.getId(), TradeRecordStatus.CANCEL_SUCCESS.name(), tradeRecord.getType(), tradeRecord.getReqAmount(), tradeRecord.getTrxTime(), tradeRecord.getTrxDate()));
            jijinTradeLogRepository.insertBusJijinTradeLog(new JijinTradeLogDTO(tradeRecord.getUserId(), tradeRecord.getSourceFundCode(), tradeRecord.getId(), TradeRecordStatus.CANCEL_SUCCESS.name(), TradeRecordType.HZ_REDEEM, tradeRecord.getReqAmount(), tradeRecord.getReqAmount(), tradeRecord.getTrxTime(), tradeRecord.getTrxDate()));
            jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", tradeRecord.getId(), "errorCode", jijinRes.getErrorCode(), "cancelAppNo", jijinRes.getApplicationNo(), "cancelAppSheetNo", jijinRes.getAppSheetSerialNo(), "status", TradeRecordStatus.CANCEL_SUCCESS.name(), "errorMsg", "转购撤单成功，但是解冻失败，需要人工解冻", "isControversial", "1"));
        }
    }

    /**
     * 转购撤单失败
     * 记录日志即可
     *
     * @param tradeRecord
     */
    @Transactional
    public void cancelFail(JijinTradeRecordDTO tradeRecord) {
        jijinTradeLogRepository.insertBusJijinTradeLog(new JijinTradeLogDTO(tradeRecord.getUserId(), tradeRecord.getFundCode(), tradeRecord.getId(), TradeRecordStatus.CANCEL_FAIL.name(), tradeRecord.getType(), tradeRecord.getReqAmount(), tradeRecord.getTrxTime(), tradeRecord.getTrxDate()));
        jijinTradeLogRepository.insertBusJijinTradeLog(new JijinTradeLogDTO(tradeRecord.getUserId(), tradeRecord.getSourceFundCode(), tradeRecord.getId(), TradeRecordStatus.CANCEL_FAIL.name(), TradeRecordType.HZ_REDEEM, tradeRecord.getReqAmount(), tradeRecord.getReqAmount(), tradeRecord.getTrxTime(), tradeRecord.getTrxDate()));
    }

    /**
     * 撤单超时，打标，用job重试
     *
     * @param tradeRecord
     */
    @Transactional
    public void cancelTimeout(JijinTradeRecordDTO tradeRecord) {
        jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", tradeRecord.getId(), "status", TradeRecordStatus.RECALLING.name()));
    }


    /**
     * 解冻份额，并加回到balance,写balanceHistory
     *
     * @param tradeRecord
     * @param unFrezeeAmount
     * @param userId
     * @param fundCode
     * @return
     */
    @Transactional
    public boolean unFrezeeForCancelOrder(JijinTradeRecordDTO tradeRecord, BigDecimal unFrezeeAmount, long userId, String fundCode) {
        JijinUserBalanceDTO jijinUserBalanceDTO;
        int result = 0;
        int retry_times = 0;
        while (result == 0) {
            jijinUserBalanceDTO = jijinUserBalanceRepository.findUserBalanceByFundCode(userId, fundCode);
            if (null == unFrezeeAmount || unFrezeeAmount.compareTo(BigDecimal.ZERO) < 0 && unFrezeeAmount.compareTo(jijinUserBalanceDTO.getFrozenShare()) > 0) {
                //如果解冻份额异常或者大于冻结份额，直接失败
                return false;
            }
            jijinUserBalanceDTO.setShareBalance(jijinUserBalanceDTO.getShareBalance().add(unFrezeeAmount));
            jijinUserBalanceDTO.setFrozenShare(jijinUserBalanceDTO.getFrozenShare().subtract(unFrezeeAmount));
            result = jijinUserBalanceRepository.updateFundAccount(jijinUserBalanceDTO);
            if (result == 1) {
                //写balanceHistory,转购撤销加到SOURCE_FUND_CODE上
                JijinUserBalanceHistoryDTO jijinUserBalanceHistoryDTO = JijinUserBalenceHistoryDTOBuilder.buildUserBalanceHistoryDTO(jijinUserBalanceDTO, tradeRecord.getTrxDate(), tradeRecord.getReqAmount(), new BigDecimal("0"), "转购撤单成功，调减冻结份额，调增份额", BalanceHistoryBizType.转购撤单成功, tradeRecord.getId());
                jijinUserBalanceHistoryRepository.insertBusJijinUserBalanceHistory(jijinUserBalanceHistoryDTO);
            }
            retry_times = retry_times + 1;
            if (retry_times > 10) break;
        }
        return result != 0;
    }
}