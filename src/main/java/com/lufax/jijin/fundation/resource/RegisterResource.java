package com.lufax.jijin.fundation.resource;

import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.lufax.jersey.utils.Logger;
import com.lufax.jijin.base.utils.JsonHelper;
import com.lufax.jijin.daixiao.dto.JijinExCharacterDTO;
import com.lufax.jijin.daixiao.repository.JijinExCharacterRepository;
import com.lufax.jijin.fundation.constant.FundSaleCode;
import com.lufax.jijin.fundation.constant.ResourceResponseCode;
import com.lufax.jijin.fundation.dto.JijinInfoDTO;
import com.lufax.jijin.fundation.gson.BaseGson;
import com.lufax.jijin.fundation.repository.JijinInfoRepository;
import com.lufax.jijin.fundation.service.AccountService;
import com.lufax.jijin.fundation.service.domain.Account;
import com.sun.jersey.api.core.InjectParam;

/**
 * @author chenguang E-mail:chenguang451@pingan.com.cn
 * @version create time:May 7, 2015 10:02:10 AM
 */
@Path("/fundation/register")
public class RegisterResource extends AuthenticatedRequiredRootResource {

    @InjectParam
    private AccountService accountService;

    @InjectParam
    private JijinInfoRepository jijinInfoRepository;
    @InjectParam
    private JijinExCharacterRepository jijinExCharacterRepository;

    private Map<String, String> saleCodes = Maps.newHashMap();


    public Map<String, String> getSaleCodes() {
        return saleCodes;
    }

    @PostConstruct
    public void initSaleCodesMap() {
        saleCodes = FundSaleCode.getSaleCodes();
    }

    @POST
    @Path("/register-account")
    @Produces(MediaType.APPLICATION_JSON)
    public String registerAccount(@FormParam("fundCode") String fundCode) {
        Logger.info(this, "call /fundation/register-account.");

        BaseGson response = accountService.processRegister(fundCode, currentUser());

        Logger.info(this, "Jijin register user response is : " + response);
        return JsonHelper.toJson(response);
    }

    @POST
    @Path("/check-user")
    @Produces(MediaType.APPLICATION_JSON)
    public String checkUser(@FormParam("fundCode") String fundCode) {
        Logger.info(this, "call /fundation/register/check-user.");
        Long userId = currentUser();
        BaseGson response = new BaseGson();
        Account account = accountService.findAccount(userId, fundCode);
        if (account != null) {
            response.setRetCode(ResourceResponseCode.SUCCESS);
        } else {
            response.setRetCode(ResourceResponseCode.ACCOUNT_NOT_EXIST);
            response.setRetMessage("account not exist");
        }
        Logger.info(this, "Jijin check user response is : " + response);
        return new Gson().toJson(response);
    }

    /**
     * 校验用户是否能注册基金
     *
     * @return
     */
    @GET
    @Path("/validate-user")
    @Produces(MediaType.APPLICATION_JSON)
    public String validateUser(@QueryParam("productId") Long productId) {
        Logger.info(this, "call /fundation/register/validate-user.");
        Long userId = currentUser();
        BaseGson response = new BaseGson();
        JijinInfoDTO jijinInfoDTO = jijinInfoRepository.findJijinInfoByProductId(productId);
        if(null==jijinInfoDTO){
            response.setRetCode(ResourceResponseCode.USER_CAN_NOT_REGISTER);
            response.setRetMessage("user can not register");
            return new Gson().toJson(response);
        }

        Boolean result = accountService.checkUserForPurchase(userId,jijinInfoDTO.getInstId());

        if (result) {
            response.setRetCode(ResourceResponseCode.SUCCESS);
            response.setRetMessage("success");
        } else {
            response.setRetCode(ResourceResponseCode.USER_CAN_NOT_REGISTER);
            response.setRetMessage("user can not register");
        }
        Logger.info(this, "validate user response is : " + response);
        return new Gson().toJson(response);
    }
    
    
    /**
     * 校验用户是否能购买该基金
     *
     * @return
     */
    @GET
    @Path("/validate-prod")
    @Produces(MediaType.APPLICATION_JSON)
    public String checkUserIsLegalForProd(@QueryParam("productId") Long productId) {
        Logger.info(this, "call /fundation/register/validate-prod.");
        Long userId = currentUser();
        BaseGson response = new BaseGson();
        JijinInfoDTO jijinInfoDTO = jijinInfoRepository.findJijinInfoByProductId(productId);
        if(null==jijinInfoDTO){
            response.setRetCode(ResourceResponseCode.NO_SUCH_JIJIN);
            response.setRetMessage("no such jijin info can be found for this prodId");
            return new Gson().toJson(response);
        }
        
        JijinExCharacterDTO jijinExCharacterDto=jijinExCharacterRepository.queryLatestRecordByFundCode(jijinInfoDTO.getFundCode());
        Boolean result = accountService.checkUserForProd(userId,jijinExCharacterDto);

        if (result) {
            response.setRetCode(ResourceResponseCode.SUCCESS);
            response.setRetMessage("success");
        } else {
            response.setRetCode(ResourceResponseCode.USER_CAN_NOT_BUY);
            response.setRetMessage("user can not buy this fund");
        }
        Logger.info(this, "validate user response is : " + response);
        return new Gson().toJson(response);
    }

}