package com.lufax.jijin.trade.resource;

import com.google.gson.Gson;
import com.lufax.jersey.utils.Logger;
import com.lufax.jijin.base.resource.AuthenticatedRequiredRootResource;
import com.lufax.jijin.base.service.TradeDayService;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.dto.SLPRedeemPageDTO;
import com.lufax.jijin.product.dto.ProductDTO;
import com.lufax.jijin.product.repository.ProductRepository;
import com.lufax.jijin.trade.service.YlxFundRedeemService;
import com.lufax.jijin.ylx.dto.YLXFundBalanceDTO;
import com.lufax.jijin.ylx.dto.repository.YLXFundBalanceRepository;
import com.sun.jersey.api.core.InjectParam;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Path("/slp-redeem")
public class RedeemResource extends AuthenticatedRequiredRootResource {
	
	@InjectParam
	private YlxFundRedeemService redeemService;
	@InjectParam
    private YLXFundBalanceRepository ylxFundBalanceRepository;
	@InjectParam
	private ProductRepository productDao;
	@InjectParam
	private TradeDayService tradeDayService;
    @POST
    @Path("/redeem")
    public String redeem(@FormParam("redeemAmount") String redeemAmount, @FormParam("tradePassword") String tradePassword, @FormParam("productId") Integer productId) {

        Logger.info(this, String.format("call /slp-redeem/redeem.%s,%s,%d",redeemAmount,tradePassword,productId));

        Long userId = currentUserAsLong();
        
        BigDecimal amount = new BigDecimal(redeemAmount);
        
        return redeemService.redeem(amount, tradePassword, productId, userId);
    }
    @GET
    @Path("/redeem-page")
    @Produces(MediaType.APPLICATION_JSON)
    public String redeem(@QueryParam("productId") Integer productId) {

        Logger.info(this, "call /slp-redeem/redeem-page. productId:" + productId);

        Long userId = currentUserAsLong();
        
        SLPRedeemPageDTO result = new SLPRedeemPageDTO();
        
        ProductDTO product = productDao.getById(productId);
        List<YLXFundBalanceDTO> ylxFundBalances = ylxFundBalanceRepository.findYlxFundBalanceByUserIdAndProductCode(MapUtils.buildKeyValueMap("userId", userId, "productCode", product.getCode()));
        
        if(CollectionUtils.isNotEmpty(ylxFundBalances)){
        	YLXFundBalanceDTO ylxFundBalance = ylxFundBalances.get(0);
        	result.setFundShare(ylxFundBalance.getFundShare());
        	result.setUnitPrice(ylxFundBalance.getUnitPrice());
        	result.setEstimateTime(tradeDayService.getRedeemEstimateDate(new Date(), 3));
        }
        Logger.info(this, "call /slp-redeem/redeem-page. result : " + new Gson().toJson(result));
        return new Gson().toJson(result);
    }
}
