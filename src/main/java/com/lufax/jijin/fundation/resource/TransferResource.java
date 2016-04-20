package com.lufax.jijin.fundation.resource;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import com.google.gson.reflect.TypeToken;
import com.lufax.jersey.utils.Logger;
import com.lufax.jijin.base.utils.JsonHelper;
import com.lufax.jijin.fundation.gson.CurrencyToStockGson;
import com.lufax.jijin.fundation.gson.PaginationByPageNoGson;
import com.lufax.jijin.fundation.gson.PaginationGson;
import com.lufax.jijin.fundation.service.TransferService;
import com.sun.jersey.api.core.InjectParam;

@Path("/fundation/transfer")
public class TransferResource extends AuthenticatedRequiredRootResource {

    @InjectParam
    private TransferService transferService;
    
    /**
     * 货转股-展示客户持有的份额超过客户申购金额的货币基金及其金额 接口
     * GET:http://localhost:8080/jijin-app/service/fundation/transfer/currency-to-stock?fundCode=11464&amount=1000
     * @param fundCode 目标基金
     * @param amount 用户输入的申购金额
     * @param pageNum 可选，当前页，默认从1开始
     * @param pageSize 可选，默认每页3000条
     * @param shouldPageAt 可选，默认3000-若总记录数大于3000条，则自动分页
     * @return 
     *         attachedInfoMap.put("isMtsSub", 0);        //预购基金是否支持货转股转购入
               attachedInfoMap.put("isMtsRedeem", -1);    //持有货基是否支持货转股转购出  -1-无可用货币基金;0-有货币基金，但所有货币基金份额(金额)小于申购金额；1-支持货转股转购出
     * 分页规则：1. 若总记录数<shouldPageAt(默认3000条，可传值修改) && (不传pageNum(默认==1) || 传过来的pageNum==1)，则默认不分页，返回全部货基
     *       2. 若总记录数>=shouldPageAt || 传过来的pageNo>1，则会强制分页； pageSize默认每页3000条，可传值修改
     */
    @GET
    @Path("/currency-to-stock")
    @Produces(MediaType.APPLICATION_JSON)
    public String currency2Stock(@QueryParam("fundCode") String fundCode, @QueryParam("amount") BigDecimal inputAmount, 
            @QueryParam("pageNum") @DefaultValue("1") int pageNo, @QueryParam("pageSize") @DefaultValue("3000") int pageSize,
            @QueryParam("shouldPageAt") @DefaultValue("3000") int shouldPageAt) {
        Long userId = currentUser();
        String logStr = String.format("Input params are [userId=%s, targetFundCode=%s, inputAmount=%s, pageNo=%s, pageLimit=%s, shouldPageAt=%s] ", 
                userId, fundCode, inputAmount, pageNo, pageSize, shouldPageAt);
        Logger.info(this, "Query currency to stock start!" + logStr);
        
        PaginationByPageNoGson<CurrencyToStockGson> paginationGson = transferService.getConvertedCurrency2StockPage(userId, fundCode, inputAmount, pageNo, pageSize, shouldPageAt);
        
        Type type = new TypeToken<PaginationGson<CurrencyToStockGson>>(){}.getType();
        String retJson = JsonHelper.toJson(paginationGson, type);
        
        Logger.info(this, String.format("Query currency to stock end. "+ logStr +"PaginationGson is [%s] ", retJson));
        return retJson;
    }
}
