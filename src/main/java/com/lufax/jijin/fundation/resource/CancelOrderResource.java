package com.lufax.jijin.fundation.resource;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


import com.google.gson.Gson;
import com.lufax.jersey.utils.Logger;
import com.lufax.jijin.fundation.constant.TradeRecordType;
import com.lufax.jijin.fundation.dto.JijinTradeRecordDTO;
import com.lufax.jijin.fundation.gson.BaseGson;
import com.lufax.jijin.fundation.repository.JijinTradeRecordRepository;
import com.lufax.jijin.fundation.service.CancelOrderService;
import com.sun.jersey.api.core.InjectParam;


@Path("/fundation/cancel")
public class CancelOrderResource extends AuthenticatedRequiredRootResource{

	@InjectParam
    private CancelOrderService cancelOrderService;
    @InjectParam
    private JijinTradeRecordRepository jijinTradeRecordRepository;

    @POST
    @Path("/cancel-purchase")
    @Produces(MediaType.APPLICATION_JSON)
    public String cancelPurchase(@FormParam("tradeRecordId") String tradeRecordId) {
        Logger.info(this, "call /fundation/cancel-purchase.");
        Long userId = currentUser();
        JijinTradeRecordDTO jijinTradeRecordDTO =  jijinTradeRecordRepository.getRecordById(Long.valueOf(tradeRecordId));
        BaseGson baseGson = new BaseGson();
        if(null==jijinTradeRecordDTO){
            baseGson.setRetCode("01");
            baseGson.setRetMessage("cancel failed");
            Logger.info(this, String.format("do cancel failed, tradeRecordId [%s]", tradeRecordId));
            return new Gson().toJson(baseGson);
        }
        //为了移动端，写了如此恶心的代码！！！以后要配合改造
        if(TradeRecordType.PURCHASE == jijinTradeRecordDTO.getType()){
            return cancelOrderService.cancelPurchase(Long.valueOf(tradeRecordId),userId);
        }else if(TradeRecordType.HZ_PURCHASE == jijinTradeRecordDTO.getType()){
            return cancelOrderService.cancelRedeemToPurchase(Long.valueOf(tradeRecordId),userId);
        }
        baseGson.setRetCode("01");
        baseGson.setRetMessage("cancel failed");
        return new Gson().toJson(baseGson);
    }
    
    
    @POST
    @Path("/cancel-redeem")
    @Produces(MediaType.APPLICATION_JSON)
    public String cancelRedeem(@FormParam("tradeRecordId") String tradeRecordId) {
        Logger.info(this, "call /fundation/cancel-redeem.");
        Long userId = currentUser();
        return cancelOrderService.cancelRedeem(Long.valueOf(tradeRecordId),false,userId);
    }
    
    @POST
    @Path("/check-cancel")
    @Produces(MediaType.APPLICATION_JSON)
    public String checkCancel(@FormParam("tradeRecordId") String tradeRecordId) {
        Logger.info(this, "call /fundation/checnk-cancel.");
        Long userId = currentUser();
        return cancelOrderService.checkCancel(Long.valueOf(tradeRecordId),userId);
    }

    /**
     * 转购撤销
     * @param tradeRecordId
     * @return
     */
    @POST
    @Path("/cancel-redeem-to-purchase")
    @Produces(MediaType.APPLICATION_JSON)
    public String cancelRedeemToPurchase(@FormParam("tradeRecordId") String tradeRecordId){
        Logger.info(this, "call /fundation/cancel-redeem-to-purchase.");
        Long userId = currentUser();
        return cancelOrderService.cancelRedeemToPurchase(Long.valueOf(tradeRecordId),userId);
    }
}
