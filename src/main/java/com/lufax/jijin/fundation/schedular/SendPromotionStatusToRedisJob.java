package com.lufax.jijin.fundation.schedular;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jxl.common.Logger;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.lufax.jijin.base.utils.DateUtils;
import com.lufax.jijin.fundation.dto.JijinPromotionConfigDTO;
import com.lufax.jijin.fundation.gson.PromotionStatus;
import com.lufax.jijin.fundation.repository.JijinPromotionConfigRepository;
import com.lufax.jijin.fundation.repository.JijinTradeRecordRepository;
import com.lufax.jijin.fundation.service.JijinPromotionService;
import com.lufax.jijin.service.RedisCacheService;

/**
 * 发送基金活动状态信息到redis
 * @author chenqunhui168
 *
 */
@Service
public class SendPromotionStatusToRedisJob extends ZkLockJobHelper{
	
	private static Logger logger = Logger.getLogger(SendPromotionStatusToRedisJob.class);

	@Autowired
	private JijinPromotionConfigRepository jijinPromotionConfigRepository;
	@Autowired
	private JijinTradeRecordRepository jijinTradeRecordRepository;
	@Autowired
	private RedisCacheService redisCacheService;
	@Autowired
	private JijinPromotionService jijinPromotionService;
	
	
	@Override
	protected void process() {
		List<JijinPromotionConfigDTO> promotionList = jijinPromotionConfigRepository.findAllActivePromotionList();
		if(CollectionUtils.isEmpty(promotionList)){
			return;
		}
		List<PromotionStatus> gsonList = new ArrayList<PromotionStatus>();
		for(JijinPromotionConfigDTO promotion : promotionList){
			PromotionStatus gson = new PromotionStatus();
			gson.setFundCode(promotion.getFundCode());
			if(!promotion.isOver()){
				Date promotionStartTime = DateUtils.parseDate(promotion.getStartTime(), DateUtils.CMS_DRAW_SEQUENCE_FORMAT);
				Date promotionEndTime = DateUtils.parseDate(promotion.getEndTime(), DateUtils.CMS_DRAW_SEQUENCE_FORMAT);
				Date now = new Date();
				BigDecimal amount = jijinTradeRecordRepository.countPurchaseAmountByFundCodeAndTime(promotion.getFundCode(),promotionStartTime, now);
				logger.info(String.format("get promotion purchase amount from db is [%s]", amount));
				amount = (null == amount)? new BigDecimal(0) : amount;
				boolean isChange = false;
				if(now.after(promotionEndTime)){
					//活动时间已经结束,将实际结束时间设置为与活动结束时间相等
					promotion.setStatus("1");
					promotion.setActualEndTime(promotion.getEndTime());
					promotion.setActualAmount(amount);
					isChange = true;
				}else{
					//判断是否已经超限额
					if(amount.compareTo(promotion.getMaxAmount())>=0){
						promotion.setStatus("1");
						promotion.setActualEndTime(DateUtils.formatDate(now, DateUtils.CMS_DRAW_SEQUENCE_FORMAT));	
						promotion.setActualAmount(amount);
						isChange = true;
					}
				}
				if(isChange){
					jijinPromotionConfigRepository.updateJijinPromotionConfig(promotion);
				}
			}
			gson.setStatus(promotion.getStatus());
			gson.setActualEndTime(promotion.getActualEndTime());
			gsonList.add(gson);
		}
		try{
			logger.info(String.format("sendPromotionStatusToRedisJob send content=[%s]",new Gson().toJson(gsonList)));
			redisCacheService.put(jijinPromotionService.getPromotionStatusKey(), new Gson().toJson(gsonList), JijinPromotionService.CACHE_EXPIRE_SECOND);			
		}catch(Exception e){
			logger.error("send promotion status to redis error ...", e);
		}
	}
}
