package com.lufax.jijin.fundation.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.lufax.jijin.base.repository.BizParametersRepository;
import com.lufax.jijin.base.service.TradeDayService;
import com.lufax.jijin.base.utils.ConstantsHelper;
import com.lufax.jijin.fundation.dto.JijinFundDividendDTO;
import com.lufax.jijin.fundation.dto.JijinIncreaseDTO;
import com.lufax.jijin.fundation.dto.JijinInfoDTO;
import com.lufax.jijin.fundation.dto.JijinNetValueDTO;
import com.lufax.jijin.fundation.repository.JijinFundDividendRepository;
import com.lufax.jijin.fundation.repository.JijinIncreaseRepository;
import com.lufax.jijin.fundation.repository.JijinInfoRepository;
import com.lufax.jijin.fundation.repository.JijinNetValueRepository;
import com.lufax.jijin.fundation.service.domain.NetValueDay;
import com.lufax.jijin.service.MqService;
import com.lufax.mq.client.util.Logger;

@Service
public class IncreaseService {
	
	@Autowired
	private JijinInfoRepository jijinInfoRepo;
	
	@Autowired
    private TradeDayService tradeDayService;
	
	@Autowired
	private JijinNetValueRepository netvalueRepo;
	
	@Autowired
	private JijinIncreaseRepository increaseRepo;
	
	@Autowired
	private JijinFundDividendRepository dividendRepo;
	
	@Autowired
	private MqService mq;
	@Autowired
	private BizParametersRepository bizParametersRepository;
	
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
	
	
	public void calculateFundIncrease(JijinNetValueDTO netValue) throws Exception{
		Date date = formatter.parse(netValue.getNetValueDate());
		Map<String, JijinNetValueDTO> allNetValues = loadOneYearNetValuesByFund(netValue.getFundCode(), date);
		allNetValues.put(netValue.getNetValueDate(), netValue);
    	Map<NetValueDay, String> targetDates = calculateTargetDates(date, allNetValues); 	
		calculateIncrease(netValue.getFundCode(), targetDates, allNetValues);
	}
	
	
	public void calculateFundIncrease(String fundCode) throws Exception{
		Date date = new Date();
		Calendar oneYearBefore = Calendar.getInstance();
    	oneYearBefore.setTime(date);
    	oneYearBefore.roll(Calendar.YEAR, false);
    	oneYearBefore.roll(Calendar.DAY_OF_YEAR, -14);//14 days for holiday maybe
		
		//calculate all the dates
        if (tradeDayService.isTradeDay(date)) {
        	Map<String, JijinNetValueDTO> allNetValues = loadOneYearNetValuesByFund(fundCode, date);
        	Map<NetValueDay, String> targetDates = calculateTargetDates(date, allNetValues); 	
			calculateIncrease(fundCode, targetDates, allNetValues);
        }else{
        	Logger.info(this, "Today is not trade day, don't need to calculate the increase.");
        }
	}
	
	private Map<String, JijinNetValueDTO> loadOneYearNetValuesByFund(String fundCode, Date date){
		Map<String, JijinNetValueDTO> allNetValues = Maps.newHashMap();
		Calendar oneYearBefore = Calendar.getInstance();
    	oneYearBefore.setTime(date);
    	oneYearBefore.roll(Calendar.YEAR, false);
    	oneYearBefore.roll(Calendar.DAY_OF_YEAR, -14);//14 days for holiday maybe
    	
    	List<JijinNetValueDTO> dtos = netvalueRepo.
    			findBusJijinNetValuesByFundAndDate(fundCode, 
    					formatter.format(oneYearBefore.getTime()), 
    				    formatter.format(date));
    	
    	for(JijinNetValueDTO dto : dtos){
    		allNetValues.put(dto.getNetValueDate(), dto);
    	}
    	Logger.info(this, String.format("Load %d net values one year of fund %s", allNetValues.size(), fundCode));
		return allNetValues;
	}

	private void calculateIncrease(String fundCode,
			Map<NetValueDay, String> targetDates,
			Map<String, JijinNetValueDTO> allNetValues) {
		TreeMap<String, BigDecimal> dividends = loadDividends(fundCode);

		Set<String> queryDates = Sets.newHashSet();
		queryDates.addAll(dividends.keySet());
		queryDates.addAll(targetDates.values());
		
		TreeMap<String, BigDecimal> netValueMap = createUseNetValueMap(queryDates, allNetValues);
		JijinNetValueDTO todayNetValue = allNetValues.get(targetDates.get(NetValueDay.TODAY));

		JijinIncreaseDTO increase = new JijinIncreaseDTO();
		increase.setFundCode(fundCode);
		increase.setIncreaseDate(targetDates.get(NetValueDay.TODAY));
		increase.setDayIncrease(calDayIncrease(
				targetDates.get(NetValueDay.LAST_TRADE_DAY),
				targetDates.get(NetValueDay.TODAY), netValueMap));
		increase.setMonthIncrease(calIncrease(fundCode,
				targetDates.get(NetValueDay.LAST_MONTH_DAY),
				targetDates.get(NetValueDay.TODAY),
				targetDates.get(NetValueDay.LAST_TRADE_DAY), netValueMap,
				dividends));
		increase.setThreeMonthIncrease(calIncrease(fundCode,
				targetDates.get(NetValueDay.LAST_THREE_MONTH_DAY),
				targetDates.get(NetValueDay.TODAY),
				targetDates.get(NetValueDay.LAST_TRADE_DAY), netValueMap,
				dividends));
		increase.setSixMonthIncrease(calIncrease(fundCode,
				targetDates.get(NetValueDay.LAST_SIX_MONTH_DAY),
				targetDates.get(NetValueDay.TODAY),
				targetDates.get(NetValueDay.LAST_TRADE_DAY), netValueMap,
				dividends));
		increase.setThisYearIncrease(calIncrease(fundCode,
				targetDates.get(NetValueDay.THIS_YEAR_FIRST_DAY),
				targetDates.get(NetValueDay.TODAY),
				targetDates.get(NetValueDay.LAST_TRADE_DAY), netValueMap,
				dividends));
		increase.setYearIncrease(calIncrease(fundCode,
				targetDates.get(NetValueDay.LAST_YEAR_DAY),
				targetDates.get(NetValueDay.TODAY),
				targetDates.get(NetValueDay.LAST_TRADE_DAY), netValueMap,
				dividends));
		increase.setTotalIncrease(calTotalIncrease(todayNetValue));
		Logger.info(this, "The increase is: " + increase);
		increaseRepo.insertBusJijinIncrease(increase);
		//通过开关判断是否需要发消息
		String switchTag = bizParametersRepository.findValueByCode(ConstantsHelper.JIJIN_DAIXIAO_SEND_NET_VALUE_MSG);
		if(null == switchTag || switchTag.equals("on")){
		    //基金代销上线后不再发送消息，直接使用wind数据
		    if(couldSendMessage(increase)){
		    	mq.sendJijinIncrease(increase);
		    }
        }	
		Logger.info(this, "Calculate " + fundCode + " fund increases.");
	}
	
	private TreeMap<String, BigDecimal> createUseNetValueMap(
			Set<String> queryDates, Map<String, JijinNetValueDTO> allNetValues) {
		TreeMap<String, BigDecimal> useMap = Maps.newTreeMap();
		for(Entry<String, JijinNetValueDTO> entry : allNetValues.entrySet()){
			useMap.put(entry.getKey(), entry.getValue().getNetValue());
	    }
		
		
//		for(String targetDate : queryDates){
//			if(allNetValues.containsKey(targetDate)){
//				useMap.put(targetDate, allNetValues.get(targetDate).getNetValue());
//			}else{
//				//tree map can't put null, skip the date
//				Logger.info(this, "Can't find the target date " + targetDate + "'s net value, skip it.");
//			}			
//		}
		Logger.info(this, String.format("Total find %d useful netvalue.", useMap.size()));
		return useMap;
	}

	public void calculateAllIncrease() {
		Date date = new Date();
        List<JijinInfoDTO> funds = jijinInfoRepo.findAllJijins();
        int count = 0;
        //calculate all the dates
        if (tradeDayService.isTradeDay(date)) {        	
        	for(JijinInfoDTO fund : funds){		
        		try{
                	Map<String, JijinNetValueDTO> allNetValues = loadOneYearNetValuesByFund(fund.getFundCode(), date);
                	Map<NetValueDay, String> targetDates = calculateTargetDates(date, allNetValues);  
	        		calculateIncrease(fund.getFundCode(), targetDates, allNetValues);        		     	    
	        		count++;
        		}catch(Exception e){
        			Logger.warn(this, e.getMessage(), e);
        		}
        	}
        	Logger.info(this, "Total calculate " + count + " fund increases.");
        }else{
        	Logger.info(this, "Today is not trade day, don't need to calculate the increase.");
        }
	}
	
	private TreeMap<String, BigDecimal> loadDividends(String fundCode) {
		List<JijinFundDividendDTO> dividends = dividendRepo.findBusJijinFundDividendsByFund(fundCode);
		TreeMap<String, BigDecimal> dividendMap = Maps.newTreeMap();
		for(JijinFundDividendDTO dividend : dividends){
			dividendMap.put(dividend.getDividendDate(), dividend.getAmount());
		}
		return dividendMap;
	}

	/**
	 * (T net value - (T-1) net value)/(T-1) net value
	 * @param targetDate
	 * @param today
	 * @param netValueMap
	 * @return
	 */
	public BigDecimal calDayIncrease(String targetDate, String today,
			TreeMap<String, BigDecimal> netValueMap){
		if(targetDate==null){
			Logger.warn(this, "Can't find the last trade day, set day increase to null.");
			return null;
		}
		BigDecimal todayNet = netValueMap.get(today);
		BigDecimal lastDayNet = netValueMap.get(targetDate);
		if(todayNet!=null && lastDayNet!=null){
			return todayNet.subtract(lastDayNet).divide(lastDayNet, 4, BigDecimal.ROUND_HALF_UP);		
		}else{
			return null;
		}		
	}
	
	/**
	 * 假设：
	 * T日净值为a元，一个月或者三个月前的净值是b元
	 * 期间发生过n次分红，每次分红前净值分别为T1,T2...Tn,每次每份派发分红分别是M1,M2...Mn元
	 * 则涨幅为：
	 * (T1/b)*(T2/(T1-M1))*(T3/(T2-M2))...*(a/(Tn-1 - Mn-1)) - 1
	 * 
	 * 公式展开化简后，成为：
	 * (a/b)*(T1/(T1-M1))*(T2/(T2-M2))*...*(Tn-1)/(Tn-1 - Mn-1)) - 1
	 * @param fundCode
	 * @param targetDate
	 * @param today
	 * @param lastDay
	 * @param netValueMap
	 * @param dividends
	 * @return
	 */
	public BigDecimal calIncrease(String fundCode, String targetDate, 
			String today, String lastDay,
			TreeMap<String, BigDecimal> netValueMap,
			TreeMap<String, BigDecimal> dividends){
		if(targetDate==null || lastDay==null){
			Logger.warn(this, "Can't find the target trade day, skip the value and set to null.");
			return null;
		}
		
		
	    if(targetDate.equalsIgnoreCase(lastDay)){
	    	return calDayIncrease(targetDate, today, netValueMap);
	    }		
	    
//	    for(Entry<String, BigDecimal> entry : netValueMap.entrySet()){
//	    	Logger.info(this, "=====netValueMap element: key: " + entry.getKey() + ", value: " + entry.getValue());
//	    }
	    
	    Logger.warn(this, "=====CalIncrease: start cal " + fundCode + " increase from " + targetDate + " to " + today);	    
	    BigDecimal a = netValueMap.get(today);
	    Logger.info(this, "=====CalIncrease: today " + today + " net vlaue is " + a);
	    BigDecimal b = netValueMap.get(targetDate);
	    Logger.info(this, "=====CalIncrease: targetDate " + targetDate + " net vlaue is " + b);
	    if(a!=null && b!=null){
	    	BigDecimal result = a.divide(b, 16, BigDecimal.ROUND_HALF_UP);
	    	Logger.info(this, "=====CalIncrease: result " + result);
		    String mDate = dividends.higherKey(targetDate);
		    while(mDate!=null){
		    	String tDate = netValueMap.lowerKey(mDate);
		    	Logger.info(this, "=====CalIncrease: the day before dividend date is " + tDate);
		    	if(tDate==null){
		    		Logger.warn(this, String
							.format("Error when calculate the increase, no net value of fund %s between %s and %s", 
									fundCode, mDate, today));
					return null;
		    	}else{
					BigDecimal t = netValueMap.get(tDate);
					Logger.warn(this, "=====CalIncrease: the net value date is " + tDate + ", net value is:" + t);
					BigDecimal m = dividends.get(mDate);
					Logger.warn(this, "=====CalIncrease: the dividend date is " + mDate + ", dividend is:" + m);
					result = t.divide(t.subtract(m), 16, BigDecimal.ROUND_HALF_UP)
							.multiply(result);
					mDate = dividends.higherKey(mDate);	
		    	}
		    }
		    return result.subtract(new BigDecimal(1)).setScale(4, BigDecimal.ROUND_HALF_UP);
	    }else{
	    	Logger.warn(this, String.format("Can't find net value of %s and %s, the increase is null.", today, targetDate));
	    	return null;
	    }	    
	}
	
	/**
	 * (total net value -1)
	 * @param netValue
	 * @return
	 */
	public BigDecimal calTotalIncrease(JijinNetValueDTO netValue){
		if(netValue!=null && netValue.getTotalNetValue()!=null){
		    return netValue.getTotalNetValue().subtract(BigDecimal.ONE).setScale(4, BigDecimal.ROUND_HALF_UP);
		}else{
			return null;
		}
	}


	private Map<NetValueDay, String> calculateTargetDates(Date date, Map<String, JijinNetValueDTO> allNetValues) throws Exception{
		Map<NetValueDay, String> targetDates = Maps.newHashMap();
		Calendar tradeCal = Calendar.getInstance();
		tradeCal.setTime(date);
		
		Date tradeDay = findTradeDay(tradeCal, allNetValues);
		if(tradeDay==null){
			throw new Exception("Can't find the trade day from " + formatDate(tradeDay) + " by net value, stop calculate process.");
		}
		
		
		Date lastTradeDay = getLastTradeDay(tradeDay, allNetValues);
		Date oneYearBeforeTradeDay = getOneYearBeforeTradeDay(tradeDay, allNetValues);
		Date thisYearFirstTradeDay = getThisYearFirstTradeDay(tradeDay, allNetValues);
		Date sixMonthBeforeTradeDay = getMonthBeforeTradeDay(tradeDay, -6, allNetValues);
		Date threeMonthBeforeTradeDay = getMonthBeforeTradeDay(tradeDay, -3, allNetValues);
		Date oneMonthBeforeTradeDay = getMonthBeforeTradeDay(tradeDay, -1, allNetValues);		
		
		
		
		targetDates.put(NetValueDay.TODAY, formatDate(tradeDay));
		targetDates.put(NetValueDay.LAST_TRADE_DAY, formatDate(lastTradeDay));
		targetDates.put(NetValueDay.LAST_MONTH_DAY, formatDate(oneMonthBeforeTradeDay));
		targetDates.put(NetValueDay.LAST_THREE_MONTH_DAY, formatDate(threeMonthBeforeTradeDay));
		targetDates.put(NetValueDay.LAST_SIX_MONTH_DAY, formatDate(sixMonthBeforeTradeDay));
		targetDates.put(NetValueDay.LAST_YEAR_DAY, formatDate(oneYearBeforeTradeDay));
		targetDates.put(NetValueDay.THIS_YEAR_FIRST_DAY, formatDate(thisYearFirstTradeDay));
		
		return targetDates;
	}
	
	private String formatDate(Date date){
		if(date != null){
			return formatter.format(date);
		}else{
			return null;
		}
	}
	
	private Date getLastTradeDay(Date date, Map<String, JijinNetValueDTO> allNetValues){
		Logger.info(this, "start look for the last trade day");
		Calendar laitTradeDay = Calendar.getInstance();
    	laitTradeDay.setTime(date);
    	laitTradeDay.add(Calendar.DAY_OF_YEAR, -1);
    	return findTradeDay(laitTradeDay, allNetValues);
	}

	private Date getMonthBeforeTradeDay(Date date, int n, Map<String, JijinNetValueDTO> allNetValues){
		Calendar oneMonthBefore = Calendar.getInstance();
    	oneMonthBefore.setTime(date);
    	oneMonthBefore.add(Calendar.MONTH, n);
    	return findTradeDay(oneMonthBefore, allNetValues);
	}
	
	private Date getOneYearBeforeTradeDay(Date date, Map<String, JijinNetValueDTO> allNetValues){
		Calendar oneYearBefore = Calendar.getInstance();
    	oneYearBefore.setTime(date);
    	oneYearBefore.add(Calendar.YEAR, -1);
    	return findTradeDay(oneYearBefore, allNetValues);
	}
	
	private Date getThisYearFirstTradeDay(Date date, Map<String, JijinNetValueDTO> allNetValues){
		Calendar target = Calendar.getInstance();
    	target.setTime(date);
    	target.set(Calendar.DAY_OF_YEAR, 1);
    	return findTradeDay(target, allNetValues);
	}
	
	private Date findTradeDay(Calendar target, Map<String, JijinNetValueDTO> allNetValues){
		//limit the find loops, if DB is empty, stop the loop
		int limit = 14;
		while(!allNetValues.containsKey(formatter.format(target.getTime()))){
    		target.add(Calendar.DAY_OF_YEAR, -1);
    		limit--;
    		if(limit<0){
    			Logger.warn(this, "Can't find the trade day from " + formatter.format(target.getTime()) + "!");
    			return null;
    		}
    	}
		return target.getTime();
	}
	
	private boolean couldSendMessage(JijinIncreaseDTO increase){
		if(increase!=null
				&& increase.getDayIncrease()!=null
				&& increase.getTotalIncrease()!=null){
			return true;
		}else{
			return false;
		}
	}
	
}
