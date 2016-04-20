package com.lufax.jijin.fundation.resource;

import com.google.gson.Gson;
import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.fundation.gson.PurchaseResultGson;
import com.lufax.jijin.fundation.service.PurchaseService;
import com.sun.jersey.api.core.InjectParam;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import java.math.BigDecimal;

@Path("/fundation/purchase")
public class PurchaseResource extends AuthenticatedRequiredRootResource{

    @InjectParam
    private PurchaseService purchaseService;

    /**
     * 大华基金直连申购下单接口
     * @param fundCode
     * @param userId
     * @param amount
     * @param type
     * @param transactionId
     * @param frozenCode
     * @param isAgreeRisk
     * @param frozenType
     * @param prodCode
     * @param channel
     * @return
     */
    @POST
    @Path("submit-purchase")
    public String confirmPurchase(@FormParam("fundCode") String fundCode, @FormParam("userId") String userId, @FormParam("amount") BigDecimal amount,
                                  @FormParam("type") String type, @FormParam("transactionId") String transactionId, @FormParam("frozenCode") String frozenCode,
                                  @FormParam("isAgreeRisk") String isAgreeRisk, @FormParam("frozenType") String frozenType,@FormParam("prodCode") String prodCode,
                                  @FormParam("channel") String channel) {
        Logger.info(this, String.format("submit purchase start. fundCode:[%s],userId:[%s],amount:[%s],type:[%s],transactionId:[%s],frozenCode:[%s],isAgreeRisk:[%s],"
        		+ "frozenType:[%s],prodCode:[%s],channel:[%s]",fundCode, userId, amount, type, transactionId, frozenCode, isAgreeRisk, frozenType,prodCode,channel));
        if(null==channel) channel="YEB"; //默认是YEB发起的申购， 其他基金目前还是走的三部曲
        
        PurchaseResultGson purchaseResultGson= new PurchaseResultGson();
        
        if(!Long.valueOf(userId).equals(currentUser())){
            purchaseResultGson.setRetMessage("user info is not correct");
            purchaseResultGson.setRetCode("99");
        }else{
        	purchaseResultGson = purchaseService.orderPurchase(fundCode, userId, amount, type, transactionId, frozenCode, isAgreeRisk, frozenType,prodCode,channel);
        }
       
        Logger.info(this, String.format("submit purchase end. purchaseResult is [%s] ", new Gson().toJson(purchaseResultGson)));
        return new Gson().toJson(purchaseResultGson);
    }
}
