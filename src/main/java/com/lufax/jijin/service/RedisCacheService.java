package com.lufax.jijin.service;

import java.math.BigDecimal;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.lufax.jersey.utils.Logger;
import com.lufax.jijin.fundation.exception.DahuaReidsException;
import com.lufax.kernel.cache.CacheException;
import com.lufax.kernel.cache.redis.RedisCacheConfig;
import com.lufax.kernel.cache.redis.RedisCacheStore;

/**
 * 提供两个redis cache
 * 一个是基金促销用 
 * 一个是大华中间户
 * @author XUNENG311
 *
 */
@Service
@Scope("singleton")
public class RedisCacheService {

	@Resource
	private RedisCacheConfig redisCacheConfig;

	// 定义客户端对象
	private RedisCacheStore<String> cache;
	private RedisCacheStore<Long> dahuaBalanceCache;

	// 在bean初始化之后创建客户端
	@PostConstruct
	public void initRedisClient() {
		cache = new RedisCacheStore<String>(redisCacheConfig, String.class, null, null);
		dahuaBalanceCache = new RedisCacheStore<Long>(redisCacheConfig, Long.class, null, null);
	}

	// 在bean销毁时关闭客户端
	@PreDestroy
	public void destroyRedisClient() {
		try {
			cache.shutdown();
			dahuaBalanceCache.shutdown();
		} catch (CacheException e) {
			Logger.error(this, e.toString(), e);
		}
	}

	public String get(String key) throws CacheException {
			return cache.get(key);
	}
	
	public void put(String key, String value, int expireSeconds) throws CacheException {
			cache.put(key, value, expireSeconds);
	}
	
	public void putDahuaBalance(String key, Long value) {
		try{
			dahuaBalanceCache.put(key, value, 0);
		}catch(Exception e){
			Logger.error(this, String.format("put dahua balance redis[%s] failed, since [%s] ",key,e.getClass().getName()));
			throw new DahuaReidsException("put value in redis failed",e);
		}
	}
	
	public long changeDahuaBalance(String key, Long value) {
		try{
			return dahuaBalanceCache.incrBy(key, value, 0);
		}catch(Exception e){
			Logger.error(this, String.format("change dahua balance redis[%s] failed, since [%s] ",key,e.getClass().getName()));
			throw new DahuaReidsException("change dahua balance redis failed",e);
		}
	}
	
	public long getDahuaBalance(String key){
		try {
			return dahuaBalanceCache.get(key);
		} catch (Exception e) {
			Logger.error(this, String.format("get dahua balance redis[%s] failed, since [%s] ",key,e.getClass().getName()));
			throw new DahuaReidsException("get dahua balance redis failed",e);
		}
	}
	
	
	/**
	 *
	 * @param key  流量控制标识
	 * @param limit  一个时间范围里次数限制  必须大于0
	 * @param duration  时间范围，必须大于0  单位：毫秒
	 * @return
	 */
	public boolean checkOperatingFrequency(String key, int limit, long duration){

		Logger.info(this, String.format("begin to check operating frequency [key:%s; limit:%d; duration:%d].",key, limit, duration));

		boolean tooFrequently = false;

		try {
			tooFrequently = cache.isDeniedByLua(key,limit,duration);
		} catch (CacheException e) {
			Logger.error(this, String.format("check operating frequency error!!![key:%s; limit:%d; duration:%d]",key,limit,duration),e);
			tooFrequently = false;
		}

		return tooFrequently;
	}

}