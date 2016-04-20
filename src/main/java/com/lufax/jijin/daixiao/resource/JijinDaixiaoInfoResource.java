package com.lufax.jijin.daixiao.resource;


import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.lufax.jersey.utils.Logger;
import com.lufax.jijin.daixiao.gson.JijinDaixiaoInfoGson;
import com.lufax.jijin.daixiao.gson.JijinExAnnounceGson;
import com.lufax.jijin.daixiao.gson.JijinUpdateStatusGson;
import com.lufax.jijin.daixiao.service.JijinDaixiaoInfoService;
import com.lufax.jijin.daixiao.service.JijinExYieldRateService;
import com.lufax.jijin.fundation.constant.ResourceResponseCode;
import com.lufax.jijin.fundation.dto.JijinInfoDTO;
import com.lufax.jijin.fundation.gson.BaseGson;
import com.lufax.jijin.fundation.gson.PromotionStatus;
import com.lufax.jijin.fundation.gson.PromotionStatusGson;
import com.lufax.jijin.fundation.service.JijinInfoService;
import com.lufax.jijin.fundation.service.JijinPromotionService;
import com.sun.jersey.api.core.InjectParam;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/daixiao")
public class JijinDaixiaoInfoResource {

    @InjectParam
    private JijinDaixiaoInfoService jijinDaixiaoInfoService;
    @InjectParam
    private JijinPromotionService jijinPromotionService;
    @InjectParam
    private JijinInfoService jijinInfoService;


    @POST
    @Path("/info")
    @Produces(MediaType.APPLICATION_JSON)
    public String getJijinDaixiaoInfo(@FormParam("productId") String productId) {
        Logger.info(this, "call /daixiao/info start.");

        JijinDaixiaoInfoGson jijinDaixiaoInfoGson = new JijinDaixiaoInfoGson();
        try {
            jijinDaixiaoInfoGson = jijinDaixiaoInfoService.getJijinDaixiaoInfoGsonByProductId(productId);

        } catch (Exception e) {
            Logger.error(this, "get daixiao jijin info  occur Exception !", e);
            jijinDaixiaoInfoGson.setRetCode(ResourceResponseCode.FAIL_WITH_EXCEPTION);
            jijinDaixiaoInfoGson.setRetMessage("exception occured, fail to get daixiao jijin info");
        }

        String response = new Gson().toJson(jijinDaixiaoInfoGson);
        //Logger.info(this, "get  daixiao jijin info, the response is : " + response);
        return response;
    }
    
    @GET
    @Path("/get-announce-page")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAnnounceByPage(@QueryParam("productId") Long productId,
    								@QueryParam("pageSize") Integer pageSize,
    								@QueryParam("pageNum") Integer pageNum){
    	Logger.info(this, "call /daixiao/get-announce-page start.");
    	Map<String,Object> map = Maps.newHashMap();
    	if(null == productId){
    		map.put("retCode", ResourceResponseCode.FAIL_WITH_EXCEPTION);
    		map.put("retMessage", "productId is null");
    		return new Gson().toJson(map);
    	}
    	JijinInfoDTO info = jijinInfoService.getJijinInfoByProductId(productId);
    	if(null == info){
    		map.put("retCode", ResourceResponseCode.FAIL_WITH_EXCEPTION);
    		map.put("retMessage", "can not find jijin info by product id "+productId);
    		return new Gson().toJson(map);
    	}
    	pageSize = pageSize == null ? 20 :pageSize;
    	pageNum = pageNum == null ? 1 :pageNum;
    	int count = jijinDaixiaoInfoService.countExAnnounceByFundCode(info.getFundCode());
    	map.put("totalNum", count);
    	map.put("retCode", ResourceResponseCode.SUCCESS);
    	map.put("pageSize", pageSize);
    	map.put("pageNum", pageNum);
    	if(count > (pageNum-1) * pageSize){
    		List<JijinExAnnounceGson> list = jijinDaixiaoInfoService.getAnnounceByPage(info.getFundCode(), pageSize, pageNum);
    		map.put("announces", list);
    	}
    	return new Gson().toJson(map);
    }

    @GET
    @Path("/get-yield-rate")
    @Produces(MediaType.APPLICATION_JSON)
    public String getYieldRatePage(@QueryParam("fundCode") String fundCode,
    								@QueryParam("pageSize") Integer pageSize,
    								@QueryParam("pageNum") Integer pageNum){
    	Logger.info(this, "call /daixiao/get-yeild-rate start.");
    	Map<String,Object> map = Maps.newHashMap();
    	if(null == fundCode){
    		map.put("retCode", ResourceResponseCode.FAIL_WITH_EXCEPTION);
    		map.put("retMessage", "fundCode is null");
    		return new Gson().toJson(map);
    	}
    	pageSize = pageSize == null ? 7 :pageSize;
    	pageNum = pageNum == null ? 1 :pageNum;
    	map.put("retCode", ResourceResponseCode.SUCCESS);
    	map.put("pageSize", pageSize);
    	map.put("pageNum", pageNum);
    	map.put("retMessage", "success");
    	map.put("content", jijinDaixiaoInfoService.getYieldByPage(fundCode, pageNum, pageSize));
    	return new Gson().toJson(map);
    }
    
    @POST
    @Path("/update-jijin-status")
    @Produces(MediaType.APPLICATION_JSON)
    public String updateJijinDaixiaoStatus(@FormParam("message") String message) {
        JijinUpdateStatusGson gson = new Gson().fromJson(message, JijinUpdateStatusGson.class);
        Logger.info(this, "call /daixiao/update-jijin-status start.");
        BaseGson rt = new BaseGson();
        try {
            rt = jijinDaixiaoInfoService.updateJijinDaixiaoStatus(gson);

        } catch (Exception e) {
            Logger.error(this, "update jijin status  occur Exception !", e);
            rt.setRetCode(ResourceResponseCode.FAIL_WITH_EXCEPTION);
            rt.setRetMessage("exception occured, fail to update daixiao jijin status");
        }
        return new Gson().toJson(rt);
    }
    
    @GET
    @Path("/get-jijin-promotion-status")
    @Produces(MediaType.APPLICATION_JSON)
    public String getJijinPromotionStatus(){
    	PromotionStatusGson gson = jijinPromotionService.getPromotionStatus();
    	return new Gson().toJson(gson);
    }
}
