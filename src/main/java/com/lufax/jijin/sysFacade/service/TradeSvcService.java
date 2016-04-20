package com.lufax.jijin.sysFacade.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lufax.jijin.base.utils.JsonHelper;
import com.lufax.jijin.sysFacade.caller.InterfaceCallObject;
import com.lufax.jijin.sysFacade.caller.TradeSvcCaller;
import com.lufax.jijin.sysFacade.gson.result.PolicyInfoGson;

@Service
public class TradeSvcService {

	private static Logger logger = Logger.getLogger(TradeSvcService.class);
	
	private static final String QUERY_COIN_BY_TRXID = "/trade/insurePolicyQueryByTrxid";
	
	@Autowired
	private TradeSvcCaller tradeSvcCaller;
	
	
	/**
	 * 查询trade使用陆金币的信息
	 * @param trxId
	 * @return
	 */
	public PolicyInfoGson getTradeInfoByTrxId(String trxId){
		try{
			logger.info(String.format("call trading-svc /trade/insurePolicyQueryByTrxid request is [trxId = %s]", trxId));
			InterfaceCallObject interfaceCallObject = new InterfaceCallObject().withUrl(QUERY_COIN_BY_TRXID);
			interfaceCallObject.addQueryParam("trxid",trxId);
			PolicyInfoGson gson =  tradeSvcCaller.getWithQueryParams(interfaceCallObject, PolicyInfoGson.class);
			logger.info(String.format("call trading-svc /trade/insurePolicyQueryByTrxid reponse is [%s]", JsonHelper.toJson(gson)));
			return gson;
		}catch(Exception e){
			logger.error("call trading-svc /trade/insurePolicyQueryByTrxid error", e);
			return null;
		}
	}
	
}
