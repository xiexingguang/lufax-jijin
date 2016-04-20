package com.lufax.jijin.fundation.resource;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import com.google.gson.reflect.TypeToken;
import com.lufax.jersey.utils.Logger;
import com.lufax.jijin.base.utils.ConstantsHelper;
import com.lufax.jijin.base.utils.JsonHelper;
import com.lufax.jijin.fundation.constant.YebFrozenFundShowGson;
import com.lufax.jijin.fundation.constant.YebTransactionType;
import com.lufax.jijin.fundation.gson.AccountResponseGson;
import com.lufax.jijin.fundation.gson.PaginationByPageNoGson;
import com.lufax.jijin.fundation.gson.YebTransactionHistoryGson;
import com.lufax.jijin.fundation.repository.JijinUserBalanceRepository;
import com.lufax.jijin.fundation.service.JijinUserService;
import com.sun.jersey.api.core.InjectParam;

@Path("/fundation/account")
public class AccountResource extends AuthenticatedRequiredRootResource{
	
	@InjectParam
    private JijinUserService jijinUserService;
	@InjectParam
	private JijinUserBalanceRepository jijinUserBalanceRepository;
	
	@GET
    @Path("/get-account")
    public String getJijinUserBalanceInfo() {
		Long userId = currentUser();

		Logger.info(this, String.format("call /service/fundation/account/get-account.[userId: %s] ", userId));
		
		AccountResponseGson accountRespGson = jijinUserService.buildAccountResponseGson(String.valueOf(userId));
		
		String retJson = JsonHelper.toJson(accountRespGson);
		Logger.info(this, String.format("call /service/fundation/account/get-account end. accountRespGson is [%s] ", retJson));
		
		return retJson;
	}
	
	
	//入参PageNum从1开始
	@GET
	@Path("/frozen-detail")
    public String getJijinFrozenFund(@QueryParam("pageNum") int pageNum) {
		Long userId = currentUser();   
		Logger.info(this, String.format("call /service/fundation/account/frozen-detail. [userId: %s] [pageNum: %s] ", userId, pageNum));
		
        PaginationByPageNoGson<YebFrozenFundShowGson> paginationGson = 
        		jijinUserService.buildYebFrozenFundShowPageGson(userId, ConstantsHelper.PAGE_LIMIT_15, pageNum);
        
        Type type = new TypeToken<PaginationByPageNoGson<YebFrozenFundShowGson>>(){}.getType();
        String retJson = JsonHelper.toJson(paginationGson, type);
        Logger.info(this, String.format("call /service/fundation/account/frozen-detail end. PaginationGson is [%s] ", retJson));
        
        return retJson;
	}
	
	//入参PageNum从0开始
	@GET
	@Path(value = "/transaction-history")
	public String getYebTransactionHistory(@QueryParam("pageNum") int pageNum,
										   @QueryParam("pageSize") @DefaultValue("15")  int pageLimit,
										   @QueryParam("transactionTypeList") List<String> txTypeCodeList) {
		Logger.info(this, "Query Yeb Transaction history start !");
		
		Long userId = currentUser();
		Logger.info(this, String.format("[userId: %s], [transactionTypeList: %s] ", userId, JsonHelper.toJson(txTypeCodeList)));
		
		PaginationByPageNoGson<YebTransactionHistoryGson> paginationGson = 
				jijinUserService.buildYebTransactionHistoryPageGson(userId, txTypeCodeList, pageLimit, pageNum);
		
		Type type = new TypeToken<PaginationByPageNoGson<YebTransactionHistoryGson>>(){}.getType();
        String retJson = JsonHelper.toJson(paginationGson, type);
        Logger.info(this, String.format("Query Yeb Transaction history end. PaginationGson is [%s] ", retJson));
        
		return retJson;
	}
	
	
	
}
