package com.lufax.jijin.fundation.service;


import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lufax.jersey.utils.Logger;
import com.lufax.jijin.base.utils.JsonHelper;
import com.lufax.jijin.fundation.constant.JijinStatus;
import com.lufax.jijin.fundation.constant.ResourceResponseCode;
import com.lufax.jijin.fundation.dto.JijinInfoDTO;
import com.lufax.jijin.fundation.dto.JijinInfoToListDTO;
import com.lufax.jijin.fundation.exception.ListAppException;
import com.lufax.jijin.fundation.gson.BaseGson;
import com.lufax.jijin.fundation.gson.CreateJijinInfoResultGson;
import com.lufax.jijin.fundation.repository.JijinInfoRepository;
import com.lufax.jijin.sysFacade.gson.result.ListCreateProductResultGson;
import com.lufax.jijin.sysFacade.service.ProdOprSvcInterfaceService;
import com.lufax.mq.client.util.MapUtils;
import com.site.lookup.util.StringUtils;

/**
 * 基金基本信息service
 * @author chenqunhui
 *
 */
@Service
public class JijinInfoService {

	@Autowired
	private JijinInfoRepository  jijinInfoRepository;
	
	@Autowired
	private ProdOprSvcInterfaceService prodOprSvcInterfaceService;
	
	public void setProdOprSvcInterfaceService(
			ProdOprSvcInterfaceService prodOprSvcInterfaceService) {
		this.prodOprSvcInterfaceService = prodOprSvcInterfaceService;
	}

	/**
	 * 新增基金产品信息
	 * @param dto
	 * @return
	 */
	@Transactional
	public CreateJijinInfoResultGson addJiJinInfo(JijinInfoDTO dto) throws ListAppException{
		CreateJijinInfoResultGson rt = new CreateJijinInfoResultGson();
		Logger.info(this, String.format("add ji jin info dto [%s].", dto));
		if(!checkNotEmptyProperties(dto)){
			rt.setRetCode(ResourceResponseCode.JIJIN_INFO_ADD_FAIL);
			rt.setRetMessage("fundCode、fundType、instId和productCode不允许为空");
			Logger.error(this, String.format("add Jijin Info failed in list app   ,fundCode=%s", dto.getFundCode()));
		}
		//通过productCode幂等
		JijinInfoDTO dbDto = jijinInfoRepository.findJijinInfo(MapUtils.buildKeyValueMap("productCode",dto.getProductCode()));
		if(null == dbDto){
			//(1)新增基金信息
			try{
				dto = jijinInfoRepository.addJijinInfo(dto);
			}catch(Exception e){
				//并发新增失败，说明fundCode或者productCode已经存在；
				rt.setRetCode(ResourceResponseCode.JIJIN_INFO_ADD_FAIL);
				rt.setRetMessage("新增基金信息异常");
				return rt;
			}
		}else{
			dto = dbDto;
		}
		//(2)调用list-app接口增加基金产品信息
		ListCreateProductResultGson listResult = prodOprSvcInterfaceService.addJijinInfo(new JijinInfoToListDTO(dto));
		if(listResult.isSuccess()){
			//（3）list成功，更新productId
			jijinInfoRepository.updateJijinInfo(MapUtils.buildKeyValueMap("fundCode",dto.getFundCode(),"productId",listResult.getProductId()));
			rt.setProductId(listResult.getProductId());
			rt.setRetCode(ResourceResponseCode.SUCCESS);
		}else{
			//(4)失败,抛异常回滚bus_jijin-info表的记录
			Logger.error(this, String.format("add Jijin Info failed in call list-app   ,fundCode=%s", dto.getFundCode()));
			throw new ListAppException(listResult.getRetCode(),listResult.getRetMessage());
		}
		return rt; 
	}
	
	/**
	 * 修改基金信息
	 * @param dto
	 * @return
	 */
	public BaseGson modifyJiJinInfo(JijinInfoDTO obj){
		if(null == obj.getProductCode()){
			Logger.error(this, String.format("update Jijin Info failed in list app ,fundCode=%s", obj.getFundCode()));
			return new BaseGson("productCode不能为空",ResourceResponseCode.JIJIN_INFO_UPDATE_FAIL);
		}
		Map<String,Object> updateMap = new HashMap<String,Object>();
		updateMap.put("code", obj.getProductCode());
		updateMap.put("fundCode", obj.getFundCode());
		if(StringUtils.isNotEmpty(obj.getIsBuyDailyLimit())){
			updateMap.put("isBuyDailyLimit", obj.getIsBuyDailyLimit());
		}
		if(null != obj.getBuyDailyLimit()){
			updateMap.put("buyDailyLimit", obj.getBuyDailyLimit());
		}
		if(StringUtils.isNotEmpty(obj.getBuyFeeRateDesc())){
			updateMap.put("buyFeeRateDesc", obj.getBuyFeeRateDesc());
		}
		if(StringUtils.isNotEmpty(obj.getBuyFeeDiscountDesc())){
			updateMap.put("buyFeeDiscountDesc",obj.getBuyFeeDiscountDesc());
		}
		if(null != obj.getMinInvestAmount()){
			updateMap.put("minInvestAmount",obj.getMinInvestAmount());
		}
		if(StringUtils.isNotEmpty(obj.getRedemptionFeeRateDesc())){
			updateMap.put("redemptionFeeRateDesc",obj.getRedemptionFeeRateDesc());
		}
		if(StringUtils.isNotEmpty(obj.getFundManager())){
			updateMap.put("fundManager", obj.getFundManager());
		}
		if(StringUtils.isNotEmpty(obj.getFundManagerIntroduce())){
			updateMap.put("fundManagerIntroduce", obj.getFundManagerIntroduce());
		}
		if(StringUtils.isNotEmpty(obj.getFundIntroduce())){
			updateMap.put("fundIntroduce", obj.getFundIntroduce());
		}
		//list-app修改基金信息的接口
		ListCreateProductResultGson listResult = prodOprSvcInterfaceService.updateJijinInfo(JsonHelper.toJson(updateMap));
		if(!"000".equals(listResult.getRetCode())){
			//list失败直接返回
			Logger.error(this, String.format("update Jijin Info failed in list app,errCode=[%s],retMessage=[%s],fundCode=[%s]",listResult.getRetCode(),listResult.getRetMessage(), obj.getFundCode()));
			return new BaseGson(listResult.getRetMessage(),ResourceResponseCode.JIJIN_INFO_UPDATE_FAIL);
			
		}
		jijinInfoRepository.updateJijinInfo(updateMap);
		return new BaseGson(null,ResourceResponseCode.SUCCESS);
	}
	
	
	/**
	 * 将BE后台传过来的枚举参数转换为jijin的枚举值
	 * @param obj
	 */
	public BaseGson translateBeEnumToJijinApp(JijinInfoDTO obj){
		if(!JijinStatus.isBuyStatus(obj.getBuyStatus())){
			//校验输入值
			return new BaseGson("申购状态(buyStatus)枚举值错误",ResourceResponseCode.JIJIN_INFO_ADD_FAIL);
			//throw new RuntimeException("BE add jijin info,buyStatus is error");
		}
		if(!JijinStatus.isRedStatus(obj.getRedemptionStatus())){
			return new BaseGson("赎回状态(redemptionStatus)枚举值错误",ResourceResponseCode.JIJIN_INFO_ADD_FAIL);
			//throw new RuntimeException("BE add jijin info,redemptionStatus is error");
		}
		//运作期限
		if("PERMANENT".equals(obj.getFundOpeningType())){
			obj.setFundOpeningType("1");//永久开放
		}else if("REGULAR".equals(obj.getFundOpeningType())){
			obj.setFundOpeningType("2");//定期开放
		}else{
			return new BaseGson("运作期限(fundOpening)枚举值错误",ResourceResponseCode.JIJIN_INFO_ADD_FAIL);
			//throw new RuntimeException("BE add jijin info,fundOpening is error");
		}
		//分红方式
		if("CASH".equals(obj.getDividendType())){
			obj.setDividendType("1");
		}else if("REINVESTMENT".equals(obj.getDividendType())){
			obj.setDividendType("0");
		}else{
			return new BaseGson("分红方式(dividendType)枚举值错误",ResourceResponseCode.JIJIN_INFO_ADD_FAIL);
			//throw new RuntimeException("BE add jijin info,dividendType is error");
		}
		return new BaseGson(null,ResourceResponseCode.SUCCESS);
	}
	
	private boolean checkNotEmptyProperties(JijinInfoDTO dto){
		if(StringUtils.isEmpty(dto.getProductCode()) 
				|| StringUtils.isEmpty(dto.getFundCode()) 
				|| StringUtils.isEmpty(dto.getFundType())
				|| StringUtils.isEmpty(dto.getInstId())){
			return false;
		}
		return true;
	}

	public JijinInfoDTO getJijinInfoByProductId(Long productId){
		return jijinInfoRepository.findJijinInfoByProductId(productId);
	}
}
