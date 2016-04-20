package com.lufax.jijin.fundation.resource;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import com.google.gson.Gson;
import com.lufax.jersey.utils.Logger;
import com.lufax.jijin.base.repository.BizParametersRepository;
import com.lufax.jijin.base.service.TradeDayService;
import com.lufax.jijin.fundation.constant.ResourceResponseCode;
import com.lufax.jijin.fundation.gson.BaseGson;
import com.lufax.jijin.fundation.gson.RedeemResultGson;
import com.lufax.jijin.fundation.remote.JijinGatewayRemoteService;
import com.lufax.jijin.fundation.repository.JijinAccountRepository;
import com.lufax.jijin.fundation.repository.JijinInfoRepository;
import com.lufax.jijin.fundation.repository.JijinTradeLogRepository;
import com.lufax.jijin.fundation.repository.JijinTradeRecordRepository;
import com.lufax.jijin.fundation.repository.JijinUserBalanceRepository;
import com.lufax.jijin.fundation.service.RedeemService;
import com.lufax.jijin.fundation.service.RedeemServiceUtil;
import com.lufax.jijin.fundation.service.SequenceService;
import com.lufax.jijin.user.service.UserService;
import com.sun.jersey.api.core.InjectParam;

@Path("/fundation/redeem")
public class RedeemResource extends AuthenticatedRequiredRootResource {

    @InjectParam
    private TradeDayService tradeDayService;
    @InjectParam
    private UserService userService;
    @InjectParam
    private JijinUserBalanceRepository jijinUserBalanceRepository;
    @InjectParam
    private JijinAccountRepository jijinAccountRepository;
    @InjectParam
    private JijinGatewayRemoteService gatewayService;
    @InjectParam
    private RedeemService redeemService;
    @InjectParam
    private RedeemServiceUtil redeemServiceUtil;
    @InjectParam
    private SequenceService sequenceService;
    @InjectParam
    private JijinTradeRecordRepository jijinTradeRecordRepository;
    @InjectParam
    private JijinTradeLogRepository jijinTradeLogRepository;
    @InjectParam
    private JijinInfoRepository jijinInfoRepository;
    @InjectParam
    private BizParametersRepository bizParametersRepository;

    /**
     * 赎回记录落地
     * @param amount
     * @param tradePwd
     * @param fundCode
     * @param channel
     * @param transactionId
     * @param redeemType
     * @param userId
     * @param prodCode
     * @return
     */
    @POST
    @Path("/submit-redeem")
    @Produces(MediaType.APPLICATION_JSON)
    public String confirmRedeem(@FormParam("amount") String amount,  // 代表份额
                                @FormParam("tradePwd") String tradePwd,
                                @FormParam("fundCode") String fundCode,
                                @FormParam("channel") @DefaultValue("PAF") String channel, //PAF,YEB,MIDWAY,LBO
                                @FormParam("transactionId") Long transactionId,
                                @FormParam("redeemType") String redeemType,
                                @FormParam("userId") Long userId,
                                @FormParam("prodCode") String prodCode,// T0中途岛赎回时会传
                                @FormParam("transferType") @DefaultValue("virtual") String transferType, 
                                @FormParam("remark") String remark) { // 备注

        Long currentUserId = userId;
        if (null == currentUserId) {
            currentUserId = currentUser();
        }
        Logger.info(this, String.format("call /fundation/redeem/submit-redeem. parameters is[%s|%s|%s|%s|%s|%s|%s|%s|%s|%s] ",
        		currentUserId, amount, tradePwd, fundCode, channel, transactionId, redeemType,prodCode,transferType,remark));
        try {
            String res = redeemService.redeem(currentUserId, fundCode, tradePwd, amount, channel, transactionId, redeemType,prodCode,transferType,remark);
            Logger.info(this, String.format("call /fundation/redeem/submit-redeem. response [%s] ", res));
            return res;
        } catch (Exception e) {
            Logger.error(this, "call /fundation/redeem/submit-redeem fail ", e);
            RedeemResultGson response = new RedeemResultGson();
            response.setRetCode(ResourceResponseCode.FAIL_WITH_EXCEPTION);
            response.setRetMessage(e.getClass().getName());
            return new Gson().toJson(response);
        }

    }

    /**
     * 判断大华货基能否赎回
     *
     * @param fundCode
     * @return
     */
    @POST
    @Path("/check-canbe-redeemed")
    @Produces(MediaType.APPLICATION_JSON)
    public String canBeRedeemed(@FormParam("fundCode") String fundCode) {
        BaseGson baseGson = new BaseGson();
        String code = redeemServiceUtil.canBeRedeemed(fundCode);
        baseGson.setRetCode(code);
        return new Gson().toJson(baseGson);
    }

    /**
     * 判断T日大华货基还能赎回多少
     *
     * @return
     */
    @GET
    @Path("/remaining")
    @Produces(MediaType.APPLICATION_JSON)
    public String remaining() {
        return redeemService.remaining(currentUser());
    }
}
