package com.lufax.jijin.fundation.resource;




import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lufax.jijin.base.utils.JsonHelper;
import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.fundation.constant.ResourceResponseCode;
import com.lufax.jijin.fundation.dto.JijinInfoDTO;
import com.lufax.jijin.fundation.dto.JijinUserBalanceDTO;
import com.lufax.jijin.fundation.gson.BaseGson;
import com.lufax.jijin.fundation.gson.CreateJijinInfoResultGson;
import com.lufax.jijin.fundation.repository.JijinUserBalanceRepository;
import com.lufax.jijin.fundation.service.DividendTypeService;
import com.lufax.jijin.fundation.service.JijinInfoService;
import com.lufax.mq.client.util.MapUtils;
import com.sun.jersey.api.core.InjectParam;

/**
 * 基金信息录入和修改接口
 * @author chenqunhui
 *
 */
@Path("fundation/jijin")
public class JiJinInfoResource extends AuthenticatedRequiredRootResource {
	
	@InjectParam
	private JijinInfoService jijinInfoService;
	
	@InjectParam
	private DividendTypeService dividendTypeService;
	
	@InjectParam
	private JijinUserBalanceRepository jijinUserBalanceRepository;
	

	/**
	 * 新增基金
	 * @return
	 */
	@POST
	@Path("/add-jijin-info")
	@Produces(value = MediaType.APPLICATION_JSON)
	public String handleAddJijinRequest(@FormParam("message") String message){
		Logger.info(this, String.format("call /fundation/add-jijin-info"));
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss") .create();
		JijinInfoDTO infoDto = gson.fromJson(message, JijinInfoDTO.class);
		//转换枚举值
		BaseGson  rt = jijinInfoService.translateBeEnumToJijinApp(infoDto);
		if(!ResourceResponseCode.SUCCESS.equals(rt.getRetCode())){
			return JsonHelper.toJson(rt);
		}
		CreateJijinInfoResultGson createGson;
		try{
			createGson = jijinInfoService.addJiJinInfo(infoDto);
		}catch(Exception e){
			createGson = new CreateJijinInfoResultGson();
			createGson.setRetCode(ResourceResponseCode.JIJIN_INFO_ADD_FAIL);
			createGson.setRetMessage(e.getMessage());
		}
		return JsonHelper.toJson(createGson);
	}
	
	
	
	
	/**
	 * 修改基金信息
	 * @param info 
	 * @return
	 */
	@POST
	@Path("/update-jijin-info")
	@Produces(value = MediaType.APPLICATION_JSON)
	public String handleModifyJijinRequest(@FormParam("message") String message){
		Logger.info(this, String.format("call /fundation/update-jijin-info"));
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss") .create();
		JijinInfoDTO obj = gson.fromJson(message,JijinInfoDTO.class);
		return  JsonHelper.toJson(jijinInfoService.modifyJiJinInfo(obj));

	}

	/**
	 * 修改基金的分红方式
	 * @param dividendInfo
	 * @return
	 */
	
	@POST
	@Path("/apply-update-dividend-type")
	@Produces(value = MediaType.APPLICATION_JSON)
	public String handleApplyModifyDividendTypeRequest(@FormParam("fundCode") String fundCode,
														@FormParam("dividendType") String dividendType){
		Long userId = currentUser();
	    Logger.info(this, String.format("call /fundation/apply-update-dividend-type.[userId: %s] [fundCode: %s] ", userId, fundCode));
		return JsonHelper.toJson(dividendTypeService.applyModifyDividendTypeRequest(userId,fundCode,dividendType));
	}
	@POST
	@Path("/get-current-dividend-type")
	public String handleQueryDividendTypeRequest(@FormParam("fundCode") String fundCode){
		Long userId = currentUser();
		Logger.info(this, String.format("call /fundation/jijin/get-dividend-type.[userId: %s] [fundCode: %s] ", userId, fundCode));
		JijinUserBalanceDTO  balance = jijinUserBalanceRepository.findBusJijinUserBalance(MapUtils.buildKeyValueMap("userId",userId,"fundCode",fundCode));
		if(null == balance){
			return new Gson().toJson(MapUtils.buildKeyValueMap("dividendType", null));
		}else{
			return new Gson().toJson(MapUtils.buildKeyValueMap("dividendType", balance.getDividendType()));
		}
	}	

}
