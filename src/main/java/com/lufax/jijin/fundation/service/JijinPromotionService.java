package com.lufax.jijin.fundation.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import jxl.common.Logger;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lufax.jijin.fundation.dto.JijinPromotionConfigDTO;
import com.lufax.jijin.fundation.gson.PromotionStatus;
import com.lufax.jijin.fundation.gson.PromotionStatusGson;
import com.lufax.jijin.fundation.repository.JijinPromotionConfigRepository;
import com.lufax.jijin.service.RedisCacheService;
import com.site.lookup.util.StringUtils;

@Service
public class JijinPromotionService {
	
	private static Logger logger = Logger.getLogger(JijinPromotionService.class);
	
	public static final String PROMOTION_STATUS_KEY = "jijin_promotion_status";//基金促销活动KEY
	
	public  static final int CACHE_EXPIRE_SECOND = 60*30;//缓存过期时间
	
	private static final AtomicInteger countLock = new AtomicInteger(0);
	
	private static final int MAX_GET_DB_COUNT = 50;
	
	@Autowired
	private RedisCacheService redisCacheService;
	@Autowired
	private JijinPromotionConfigRepository jijinPromotionConfigRepository;
	
	public PromotionStatusGson getPromotionStatus(){
		PromotionStatusGson result = new PromotionStatusGson();
		String gsonStr = null;
		List<PromotionStatus> statusList = null;
		try{
			gsonStr = redisCacheService.get(this.getPromotionStatusKey());
		}catch(Exception e){
			logger.error("get jijin promotion status from redis error...",e);
		}
		if(StringUtils.isEmpty(gsonStr)){
			if(countLock.incrementAndGet()<=MAX_GET_DB_COUNT){
				try{
					statusList = getPromotionStatusFromDB();
					gsonStr = new Gson().toJson(statusList);
					logger.info(String.format("get jijin promotion status from db success,value is[%s] ", gsonStr));
					redisCacheService.put(getPromotionStatusKey(), gsonStr, CACHE_EXPIRE_SECOND);
				}catch(Exception e){
					logger.error("get jijin promotion status from db or send to redis error...",e);
				}
			}
			countLock.getAndDecrement();
		}else{
			statusList = new Gson().fromJson(gsonStr, new TypeToken<List<PromotionStatus>>(){}.getType());
		}
		if(CollectionUtils.isEmpty(statusList)){
			result.setRetCode("01");
			result.setRetMessage("接口调用失败，请重试");
			logger.error("too many  calls in the current of no-cache time ");
		}else{
			result.setRetCode("00");
			result.setContent(statusList);
		}
		return result;
	}
	
	/**
	 * 促销活动key
	 * @param fundCode
	 * @return
	 */
	public String getPromotionStatusKey(){
		return PROMOTION_STATUS_KEY;
	}
	
	/**
	 * 从DB查询活动状态
	 * @return
	 */
	public List<PromotionStatus>  getPromotionStatusFromDB(){
		List<JijinPromotionConfigDTO> promotionList = jijinPromotionConfigRepository.findAllActivePromotionList();
		List<PromotionStatus> statusList = new ArrayList<PromotionStatus>();
		if(CollectionUtils.isNotEmpty(promotionList)){
			for(JijinPromotionConfigDTO promotion : promotionList){
				PromotionStatus gson = new PromotionStatus();
				gson.setFundCode(promotion.getFundCode());
				gson.setStatus(promotion.getStatus());
				gson.setActualEndTime(promotion.getActualEndTime());
				statusList.add(gson);
			}
		}
		return statusList;
	}
}
