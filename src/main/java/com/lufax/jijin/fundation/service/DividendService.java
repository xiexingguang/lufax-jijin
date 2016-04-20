/**
 *
 */
package com.lufax.jijin.fundation.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.lufax.jijin.base.utils.DateUtils;
import com.lufax.jijin.fundation.constant.*;
import com.lufax.jijin.fundation.service.domain.JiJinDateUtil;
import com.lufax.jijin.fundation.util.JijinUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.fundation.constant.BalanceHistoryBizType;
import com.lufax.jijin.fundation.constant.TradeRecordStatus;
import com.lufax.jijin.fundation.constant.TradeRecordType;
import com.lufax.jijin.fundation.constant.TransactionType;
import com.lufax.jijin.fundation.dto.JijinDividendDTO;
import com.lufax.jijin.fundation.dto.JijinInfoDTO;
import com.lufax.jijin.fundation.dto.JijinThirdPaySyncDTO;
import com.lufax.jijin.fundation.dto.JijinTradeLogDTO;
import com.lufax.jijin.fundation.dto.JijinUserBalanceDTO;
import com.lufax.jijin.fundation.dto.JijinUserBalanceHistoryDTO;
import com.lufax.jijin.fundation.dto.builder.JijinUserBalenceHistoryDTOBuilder;
import com.lufax.jijin.fundation.gson.BaseResponseGson;
import com.lufax.jijin.fundation.remote.AccountAppCallerService;
import com.lufax.jijin.fundation.repository.JijinDividendRepository;
import com.lufax.jijin.fundation.repository.JijinInfoRepository;
import com.lufax.jijin.fundation.repository.JijinThirdPaySyncRepository;
import com.lufax.jijin.fundation.repository.JijinTradeLogRepository;
import com.lufax.jijin.fundation.repository.JijinUserBalanceHistoryRepository;
import com.lufax.jijin.fundation.repository.JijinUserBalanceRepository;
import com.lufax.jijin.fundation.service.domain.Dividend;
import com.lufax.mq.client.util.Logger;

/**
 * @author chenguang E-mail:chenguang451@pingan.com.cn
 * @version create time:May 15, 2015 10:51:43 AM
 */
@Service
public class DividendService {

    @Autowired
    private JijinUserBalanceRepository userBalanceRepo;

    @Autowired
    private JijinUserBalanceHistoryRepository jijinUserBalanceHistoryRepository;

    @Autowired
    private JijinDividendRepository dividendRepo;

    @Autowired
    private JijinTradeLogRepository logRepo;

    @Autowired
    private JijinThirdPaySyncRepository payRepo;

    @Autowired
    private JijinInfoRepository jijinRepo;

    @Autowired
    private AccountAppCallerService accountCaller;

    @Autowired
    private JiJinDateUtil jiJinDateUtil;
    private SimpleDateFormat format = new SimpleDateFormat("hhmmss");


    public List<Dividend> getNewDividendsByType(Dividend.Type type, int limit) {
        List<Dividend> dividends = Lists.newArrayList();
        if (type.equals(Dividend.Type.SWITCH_DIVIDEND)) {
            List<JijinDividendDTO> dtos = dividendRepo.findBatchNewDividendsByType(type.getName(), limit);
            for (JijinDividendDTO dto : dtos) {
                if (validSwitchDividend(dto)) {
                    Dividend dividend = buildDividend(dto);
                    dividends.add(dividend);
                }

            }
        } else if (type.equals(Dividend.Type.CASH)) {
            List<JijinThirdPaySyncDTO> pays = payRepo.getUnDispatchRecords(JijinThirdPaySyncDTO.Status.NEW,
                    "PAF", "06", limit);
            for (JijinThirdPaySyncDTO pay : pays) {
                JijinDividendDTO dto = dividendRepo.findJijinDividendByAppno(pay.getAppSheetNo(), pay.getInstId());
                if (dto != null && validDividend(pay, dto)) {
                    Dividend dividend = buildDividend(dto);
                    dividend.setPayId(pay.getId());
                    dividends.add(dividend);

                } else {
                    Logger.warn(this, "The pay dividend " + pay.getAppSheetNo() + ", instId " + pay.getInstId() + " arrived, but can't find jijin record or the record is not valid.");
                    payRepo.updateBusJijinThirdPaySyncStatus(pay.getId(), JijinThirdPaySyncDTO.Status.UNMATCH.name());
                }
            }
        }

        return dividends;
    }

    private boolean validSwitchDividend(JijinDividendDTO dto) {
        if (dto.getResCode().equalsIgnoreCase("0000")) {
            return true;
        } else {
            Logger.warn(this, "Switch dividend not valid! The response response code is " + dto.getResCode());
            return false;
        }

    }

    private boolean validDividend(JijinThirdPaySyncDTO pay, JijinDividendDTO dto) {
        String failReason = "";
        if (pay.getAmount().compareTo(dto.getDividendAmount()) != 0) {
            failReason = String.format("Dividend not valid! The amount in pay and fund file are not eaqule, pay is: %s, fund is %s, app_no is %s",
                    pay.getAmount().toString(), dto.getDividendAmount().toString(), pay.getAppSheetNo());
        }
        if (!pay.getTrxResult().equals("S")) {
            failReason = String.format("Dividend not valid! The result in pay is fail, pay app_no is %s", pay.getAppSheetNo());
        }
        if (!dto.getResCode().equalsIgnoreCase("0000")) {
            failReason = String.format("Dividend not valid! The result in jijin record is fail, record rescode is %s", dto.getResCode());
        }

        if (StringUtils.isNotBlank(failReason)) {
            Logger.warn(this, failReason);
            dividendRepo.updateJijindividendStatus(dto.getId(), JijinDividendDTO.Status.DISPATCHED.name());
            payRepo.updateBusJijinThirdPaySyncStatus(pay.getId(), JijinThirdPaySyncDTO.Status.DISPATCHED.name());
            return false;
        } else {
            return true;
        }
    }

    private Dividend buildDividend(JijinDividendDTO dto) {
        Dividend dividend = new Dividend();
        dividend.setAppSheetNo(dto.getAppSheetNo());
        dividend.setChargeType(dto.getChargeType());
        dividend.setDividendAmount(dto.getDividendAmount());
        dividend.setDividendDate(dto.getDividendDate());
        dividend.setDividendMode(dto.getDividendMode());
        dividend.setDividendShare(dto.getDividendShare());
        dividend.setFee(dto.getFee());
        dividend.setFundCode(dto.getFundCode());
        dividend.setId(dto.getId());
        dividend.setRightDate(dto.getRightDate());
        dividend.setTrxDate(dto.getTrxDate());
        dividend.setType(Dividend.Type.fromString(dto.getDividendType()));
        dividend.setUserId(dto.getUserId());
        return dividend;
    }


    @Transactional
    public boolean increaseDividend(Dividend dividend) {
        JijinUserBalanceDTO userBalance = userBalanceRepo.findUserBalanceByFundCode(dividend.getUserId(), dividend.getFundCode());

        JijinInfoDTO jijinInfo = jijinRepo.findJijinInfo(MapUtils.buildKeyValueMap("fundCode", dividend.getFundCode()));

        if (null == userBalance) {
            dividendRepo.updateJijindividendStatus(dividend.getId(), JijinDividendDTO.Status.UNMATCH.name());
            return true;
        }

        //jijinInfo不能为NULL
        if (null == jijinInfo) {
            Logger.info(this, String.format("jijinInfo [code=%s] not exist", dividend.getFundCode()));
            dividendRepo.updateJijindividendStatus(dividend.getId(), JijinDividendDTO.Status.UNMATCH.name());
            return true;
        }

        //货基无需处理，直接置为DISPATCHED
        if (JijinUtils.isHuoji(jijinInfo)) {
            Logger.info(this, String.format("fund [code=%s productId=%s] type is currency,not need process", dividend.getFundCode(), jijinInfo.getProductId()));
            dividendRepo.updateJijindividendStatus(dividend.getId(), JijinDividendDTO.Status.DISPATCHED.name());
            return true;
        }

        /**
         * Why don't need to care the log DB error?
         * if addDividend update DB, the action and the log actions are in one transaction, and the log error could roll back the share update;
         * if addDividend call service, the log error should do the action again, and the call service is idempotent.
         */
        if (addDividend(userBalance, dividend)) {
            //webdev8355:基金(PC)赎回到账时间显示优化
            String expectAccountDate = jiJinDateUtil.getAccountDate(TradeRecordType.CURRENCY_INCOME.name(), dividend.getTrxDate(),
                    jijinInfo.getFundType(),jijinInfo.getRedemptionArrivalDay());
            dividend.setExpectAccountDate(expectAccountDate);
            dividendRepo.updateJijindividendStatus(dividend.getId(), JijinDividendDTO.Status.DISPATCHED.name());
            payRepo.updateBusJijinThirdPaySyncStatus(dividend.getPayId(), JijinThirdPaySyncDTO.Status.DISPATCHED.name());
            logRepo.insertBusJijinTradeLog(buildIncreaseDividendLog(dividend));
            return true;
        } else {
            Logger.warn(this, "Increase the user dividend fail, " + dividend);
            return false;
        }
    }

    /**
     * to swtich dividend, update the share directly
     * to cash dividend, call account service
     *
     * @param userBalance
     * @param dividend
     * @return
     */
    private boolean addDividend(JijinUserBalanceDTO userBalance, Dividend dividend) {
        if (dividend.getType().equals(Dividend.Type.SWITCH_DIVIDEND)) {
            userBalance.addShare(dividend.getDividendShare());

            int result = userBalanceRepo.updateFundAccount(userBalance);
            if (result > 0) {
                //insert user balance history
                JijinUserBalanceHistoryDTO jijinUserBalanceHistoryDTO = JijinUserBalenceHistoryDTOBuilder.buildUserBalanceHistoryDTO(userBalance, dividend.getDividendDate(), dividend.getDividendShare(), new BigDecimal("0"), "份额分红，份额增加",BalanceHistoryBizType.份额分红,dividend.getId());
                jijinUserBalanceHistoryRepository.insertBusJijinUserBalanceHistory(jijinUserBalanceHistoryDTO);
            }

            return result > 0;
        } else {
            JijinInfoDTO jijinInfo = jijinRepo.findJijinInfo(MapUtils.buildKeyValueMap("fundCode", dividend.getFundCode()));
            BaseResponseGson resp = accountCaller.plusMoney(dividend.getDividendAmount(),
                    "JIJINDIVIDEND",
                    dividend.getUserId(),
                    jijinInfo.getFundName(),
                    String.valueOf(dividend.getId()),
                    TransactionType.INCOME_DIVIDENT.name(),
                    dividend.getAppSheetNo());
            return resp.isSuccess();
        }
    }


    private JijinTradeLogDTO buildIncreaseDividendLog(Dividend dividend) {
        JijinTradeLogDTO log = JijinTradeLogDTO
                .create()
                .addDividendType(dividend.getType().getName())
                .addFundCode(dividend.getFundCode())
                .addReqShare(dividend.getDividendShare())
                .addStatus(TradeRecordStatus.SUCCESS.name())
                .addAmount(dividend.getDividendAmount())
                .addFee(dividend.getFee())
                .addTrxDate(dividend.getTrxDate())
                .addTrxTime(dividend.getTrxDate() + format.format(new Date()))
                .addType(transferTradeRecordType(dividend.getType()))
                .addUserId(dividend.getUserId())
                .addTrxSeria(dividend.getAppSheetNo())
                .addConfirmDate(DateUtils.formatDateAddHMS(dividend.getTrxDate()))
                .addAccountDate(DateUtils.formatDateAddHMS(dividend.getTrxDate()))
                .addExpectAccountDate(dividend.getExpectAccountDate())
                .build();
        return log;
    }

    private TradeRecordType transferTradeRecordType(Dividend.Type type) {
        if (type.equals(Dividend.Type.SWITCH_DIVIDEND)) {
            return TradeRecordType.DIVIDEND_SHARE;
        } else {
            return TradeRecordType.DIVIDEND_CASH;
        }
    }
}
