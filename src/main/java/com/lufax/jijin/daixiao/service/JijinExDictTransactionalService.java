package com.lufax.jijin.daixiao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lufax.jersey.utils.Logger;
import com.lufax.jijin.daixiao.dto.JijinExDictDTO;
import com.lufax.jijin.daixiao.gson.JijinProductGson;
import com.lufax.jijin.fundation.dto.JijinInfoDTO;
import com.lufax.jijin.fundation.repository.JijinInfoRepository;
import com.lufax.jijin.sysFacade.gson.result.ListCreateProductResultGson;
import com.lufax.jijin.sysFacade.service.ProdOprSvcInterfaceService;
import com.lufax.mq.client.util.MapUtils;

/**
 * 用来使生成代销基金事务生效的service
 * @author chenqunhui
 *
 */
@Service
public class JijinExDictTransactionalService {

	@Autowired
	private JijinInfoRepository jijinInfoRepository;
	@Autowired
	private ProdOprSvcInterfaceService prodOprSvcInterfaceService;
	@Autowired
	private JijinExDictService jijinExDictService;
	
	@Transactional
	public void addJijinInfoAndProduct(JijinExDictDTO item,String fundCode,JijinInfoDTO info, JijinProductGson product) throws Exception{
		info = jijinInfoRepository.addJijinInfo(info);
		product.setSourceId(info.getId());
		//保存product
		ListCreateProductResultGson listResult = prodOprSvcInterfaceService.addDaixiaoJijinInfo(product);
		Logger.info(JijinExDictService.class, String.format("handleJijinExDictJob call list-app create product .fundCode=[%s] return code =[%s]", item.getFundCode(),listResult.getRetCode()));
		if(listResult.isSuccess()){
			//（3）list成功，更新productId
			jijinInfoRepository.updateJijinInfo(MapUtils.buildKeyValueMap("fundCode",fundCode,"productId",listResult.getProductId()));
			jijinExDictService.setSuccess(item);
		}else{
			//(4)失败,抛异常回滚bus_jijin-info表的记录
			Logger.error(this, String.format("add Jijin Info failed in call list-app ,fundCode=[%s] fail code=[%s],errMsg=[%s]", fundCode, listResult.getRetCode(),listResult.getRetMessage()));
			throw new RuntimeException("调用list-app接口失败，返回值:"+listResult.getRetCode()+"；错误信息："+listResult.getRetMessage());
		}
	}
}
