package com.lufax.jijin.base.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lufax.jijin.base.dto.TradeDaysDTO;
import com.lufax.jijin.base.repository.ChinaKongTradeDaysRepository;
import com.lufax.jijin.base.utils.DateUtils;
import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.base.utils.StringUtils;

@Service
public class ChinaKongTradeDayService {

    @Autowired
    private ChinaKongTradeDaysRepository tradeDaysRepository;

    /**
     * 判断是否是交易日
     *
     * @param date
     * @return
     */
    public boolean isTradeDay(Date date) {
        Logger.debug(this, String.format("decide today(%s) is tradeDay or not.", date));

        TradeDaysDTO tradeDay = tradeDaysRepository.getTradeDay(new DateTime(date).dayOfYear().roundFloorCopy().toDate());

        if (tradeDay != null) {
            return tradeDay.getIsTradeDay().equals("Y");
        }

        return false;
    }

    public String getNextTradeDayAsString(Date date) {
        List<TradeDaysDTO> tradeDaysDTOs = tradeDaysRepository.getTradeDayByDate(new DateTime(date).dayOfYear().roundFloorCopy().toDate());
        if (!tradeDaysDTOs.isEmpty()) {
            for (TradeDaysDTO tradeDaysDTO : tradeDaysDTOs) {
                if (tradeDaysDTO.getIsTradeDay().equals("Y")) {
                    return DateUtils.formatDate(tradeDaysDTO.getTradeDate());
                }
            }
        }
        return StringUtils.EMPTY;
    }

    public Date getNextTradeDayAsDate(Date date) {
        List<TradeDaysDTO> tradeDaysDTOs = tradeDaysRepository.getTradeDayByDate(new DateTime(date).dayOfYear().roundFloorCopy().toDate());
        if (!tradeDaysDTOs.isEmpty()) {
            for (TradeDaysDTO tradeDaysDTO : tradeDaysDTOs) {
                if (tradeDaysDTO.getIsTradeDay().equals("Y")) {
                    return tradeDaysDTO.getTradeDate();
                }
            }
        }
        return null;
    }
    
  /**
   * 基金直连项目
   * 得到赎回预计到帐时间
   * @param date
   * @param n  T+N天到帐
   * @return
   */
    public Date getRedeemEstimateDate(Date date, int n){

       	//15:00为日切时间
    	Calendar cutOffTime = Calendar.getInstance();
		cutOffTime.setTime(date);
    	cutOffTime.set(Calendar.HOUR_OF_DAY, 15);
    	cutOffTime.set(Calendar.MINUTE, 0);
    	cutOffTime.set(Calendar.SECOND, 0);
    	
    	if(date.after(cutOffTime.getTime())){//如果是15：00以后，归入下一天
    		Calendar  c = Calendar.getInstance();
    		c.setTime(date);
    		c.add(Calendar.DATE, 1);//日期加一
    		date=c.getTime();
    	}
    	
    	boolean todayIsTradeDay = isTradeDay(date);
    	
    	
    	List<TradeDaysDTO> tradeDaysDTOs = tradeDaysRepository.getTradeDayByDate(new DateTime(date).dayOfYear().roundFloorCopy().toDate());
    	
		if (!tradeDaysDTOs.isEmpty() && tradeDaysDTOs.size()>=n+1) {
			if(todayIsTradeDay){
				return tradeDaysDTOs.get(n-1).getTradeDate();
			}else{
				return tradeDaysDTOs.get(n).getTradeDate();
			}
		}
    	return null;
    }

    /**
     * YLX 实利派使用15：00以后算到下一个交易日
     * @param date
     * @return
     */
    public Date getTargetTradeDay(Date date){
    	
    	//15:00为日切时间
    	Calendar cutOffTime = Calendar.getInstance();
		cutOffTime.setTime(date);
    	cutOffTime.set(Calendar.HOUR_OF_DAY, 15);
    	cutOffTime.set(Calendar.MINUTE, 0);
    	cutOffTime.set(Calendar.SECOND, 0);
    	
    	if(date.after(cutOffTime.getTime())){//如果是15：00以后，归入下一天
    		Calendar  c = Calendar.getInstance();
    		c.setTime(date);
    		c.add(Calendar.DATE, 1);//日期加一
    		date=c.getTime();
    	}
    	
    	boolean todayIsTradeDay = isTradeDay(date);

    	if (todayIsTradeDay){
    		return date;
    	}else{
    		//Date input = DateUtils.parseDate(nextDay);
    		return getNextTradeDayAsDate(date);
    	}
    }
    
    /**
     * 取上一个交易日
     * @param date
     * @return
     */
    public String getLastTradeDay(Date date){
    	
    	TradeDaysDTO tradeDaysDTO = tradeDaysRepository.getLastTradeDay(new DateTime(date).dayOfYear().roundFloorCopy().toDate());
    	
    	if(null!=tradeDaysDTO){
    		 return DateUtils.formatDate(tradeDaysDTO.getTradeDate(),DateUtils.DATE_STRING_FORMAT);
    	}else{
    		return "";
    	}
 
    }
}
