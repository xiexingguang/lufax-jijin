package com.lufax.jijin.product.resource;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lufax.jersey.utils.Logger;
import com.lufax.jijin.base.utils.DateUtils;
import com.lufax.jijin.base.utils.StringUtils;
import com.lufax.jijin.product.dto.RemoteCallerException;
import com.lufax.jijin.product.dto.UpdateStatusGson;
import com.lufax.jijin.product.gson.BeFundResultGson;
import com.lufax.jijin.product.gson.ProductInputGson;
import com.lufax.jijin.product.service.FundLoanRequestService;
import com.lufax.jijin.sysFacade.caller.InterfaceCallObject;
import com.lufax.jijin.sysFacade.caller.ListInterfaceCaller;
import com.lufax.jijin.sysFacade.gson.FundProductGson;
import com.sun.jersey.api.core.InjectParam;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("product")
public class ProductResource {
	
	@InjectParam
	private ListInterfaceCaller caller;
	
	@InjectParam
	private FundLoanRequestService fundLoanRequestService;
	
    @POST
    @Path("generate")
    @Produces(MediaType.APPLICATION_JSON)
    public String importProduct(@FormParam("message") String message){
        Gson gson =  new GsonBuilder().setDateFormat(DateUtils.DATE_TIME_FORMAT).create();
    	ProductInputGson fundProductGson = gson.fromJson(message, ProductInputGson.class);
    	Logger.info(this, String.format("input message : [%s]", message));
        try {
            fundLoanRequestService.createFundLoanRequest(fundProductGson);
            return BeFundResultGson.successfulBeFundResult();
        } catch (RemoteCallerException re) {
        	re.printStackTrace();
            return new Gson().toJson(new BeFundResultGson("01", re.getMessage()));
        } catch (Exception e) {
        	e.printStackTrace();
            return new Gson().toJson(new BeFundResultGson("99", e.getMessage()));
        }
    }
    
    @POST
    @Path("update-status")
    @Produces(MediaType.APPLICATION_JSON)
    public String handFundTransferSuccess(@FormParam("message") String message){
        Logger.info(this, String.format("input message : [%s]", message));
    	UpdateStatusGson gson = new Gson().fromJson(message, UpdateStatusGson.class);
    	fundLoanRequestService.confirmLoanRequestRaiseResult(gson);
        return BeFundResultGson.successfulBeFundResult();
    }
}
