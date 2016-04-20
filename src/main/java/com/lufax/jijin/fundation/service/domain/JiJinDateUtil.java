package com.lufax.jijin.fundation.service.domain;

import com.lufax.jijin.base.service.TradeDayService;
import com.lufax.jijin.base.utils.DateUtils;
import com.lufax.jijin.fundation.constant.FundType;
import com.lufax.jijin.fundation.constant.JijinConstants;
import com.lufax.jijin.fundation.constant.TradeRecordType;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by wuxiujun on 2016/3/15.
 */
@Component
public class JiJinDateUtil {
    @Autowired
    private TradeDayService tradeDayService;

    /**
     * 计算预计OR确认日期
     *
     * @param tradeType    交易类型:申购、认购、赎回
     * @param strTradeDate 交易日期
     * @param fundType     基金类型
     * @return String 计算后的日期
     */
    public String getConfirmDate(String tradeType, String strTradeDate, String fundType) {
        String strConfirmDate = "";
        if(StringUtils.isBlank(strTradeDate)){
            return strConfirmDate;
        }
        //如果只是到天，补充时分秒
        if(strTradeDate.length()==8){
            strTradeDate = strTradeDate+"000000";
        }
        Date tradeDate = DateUtils.formatDate(strTradeDate);
        if (TradeRecordType.PURCHASE.name().equals(tradeType) || TradeRecordType.REDEEM.name().equals(tradeType)
            ||TradeRecordType.DIVIDEND_MODIFY.name().equals(tradeType)) {//申购，赎回，修改分红方式
            //“申购，赎回”，在未确认之前，确认时间显示【预计】YYYY-MM-DD，针对非QDII，为T+1；针对QDII，为T+2
            int n = 1;
            if (FundType.QDII.name().equals(fundType)) {
                n = 2;
            }
            strConfirmDate = DateUtils.formatDate(tradeDayService.getRedeemEstimateDate(tradeDate, n));
        }
        return strConfirmDate;
    }

    /**
     * 计算预计or到账日期
     *
     * @param tradeType
     * @param strTradeDate
     * @param fundType
     * @param days
     * @return strAccountDate 到账日期
     */
    public String getAccountDate(String tradeType, String strTradeDate, String fundType, int days) {
        String strAccountDate = "";
        if(StringUtils.isBlank(strTradeDate)){
            return strAccountDate;
        }
        //如果只是到天，补充时分秒
        if(strTradeDate.length()==8){
            strTradeDate = strTradeDate+"000000";
        }
        Date tradeDate = DateUtils.formatDate(strTradeDate);
        if (TradeRecordType.CURRENCY_INCOME.name().equals(tradeType) || TradeRecordType.REDEEM.name().equals(tradeType)) {//现金分红，赎回
            //“现金分红，赎回”，在未确认之前，确认时间显示【预计】YYYY-MM-DD，针对非QDII，为T+1；针对QDII，为T+2
            int n = 1;
            if (FundType.QDII.name().equals(fundType)) {
                n = 2;
            }
            n = n + days;
            strAccountDate = DateUtils.formatDate(tradeDayService.getRedeemEstimateDate(tradeDate, n));
        }
        return strAccountDate;
    }
}