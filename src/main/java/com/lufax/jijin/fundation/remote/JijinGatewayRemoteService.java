package com.lufax.jijin.fundation.remote;

import com.lufax.jersey.utils.Logger;
import com.lufax.jijin.base.utils.DateUtils;
import com.lufax.jijin.base.utils.JsonHelper;
import com.lufax.jijin.fundation.constant.FundSaleCode;
import com.lufax.jijin.fundation.remote.gson.request.GWQueryDahuaBalanceRequestGson;
import com.lufax.jijin.fundation.remote.gson.response.GWResponseCode;
import com.lufax.jijin.fundation.remote.gson.response.GWResponseGson;
import com.lufax.jijin.sysFacade.caller.InterfaceCallObject;
import com.lufax.jijin.sysFacade.caller.JijinGWInterfaceCaller;
import com.lufax.jijin.sysFacade.caller.DahuaGWInterfaceCaller;
import com.sun.jersey.api.representation.Form;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JijinGatewayRemoteService {
    private static final String REGISTER_URL = "/service/jijin/register"; //开户
    private static final String APPLY_URL = "/service/jijin/apply"; // 认购
    private static final String BUY_URL = "/service/jijin/buy";// 申购
    private static final String BUY_NOTIFY_URL = "/service/jijin/buy-notify";// 支付通知
    private static final String REDEEM_URL = "/service/jijin/redeem"; //赎回
    private static final String CANCEL_URL = "/service/jijin/cancel"; //撤单
    private static final String MODIFY_DIVIDEND_URL = "/service/jijin/modify-dividend"; //分红修改
    private static final String GET_DAHUA_BALANCE = "/service/jijin/get-dahua-balance"; //获取大华垫资户的余额
    private static final String TRANSFORM_BUY_URL = "/service/jijin/transform-buy"; //代销转购URL

    @Autowired
    private JijinGWInterfaceCaller jijinCaller;
    @Autowired
    private DahuaGWInterfaceCaller dahuaJijinCaller;

    public GWResponseGson register(String code, String json) {

        return callGateway(code, json, REGISTER_URL);

    }

    public GWResponseGson apply(String code, String json) {

        return callGateway(code, json, APPLY_URL);
    }

    public GWResponseGson buy(String code, String json) {

        return callGateway(code, json, BUY_URL);
    }

    public GWResponseGson buyNotify(String code, String json) {

        return callGateway(code, json, BUY_NOTIFY_URL);
    }


    public GWResponseGson redeem(String code, String json) {
        return callGateway(code, json, REDEEM_URL);
    }

    public GWResponseGson cancel(String code, String json) {

        return callGateway(code, json, CANCEL_URL);
    }

    public GWResponseGson getDahuaBalance(int accountType) {
        GWQueryDahuaBalanceRequestGson requestGson = new GWQueryDahuaBalanceRequestGson();
        requestGson.setInstId(FundSaleCode.DHC.getInstId());
        requestGson.setVersion("1.0");
        requestGson.setAccountType(accountType);//大华基金中间账户
        requestGson.setRequestTime(DateUtils.formatDateAsCmsDrawSequence(new Date()));
        return getDahuaBalance(FundSaleCode.DHC.getInstId(), JsonHelper.toJson(requestGson));
    }

    public GWResponseGson getDahuaBalance(String code, String json) {
        return callGateway(code, json, GET_DAHUA_BALANCE);
    }

    /**
     * 调用基金公司API ：申请修改分红方式
     *
     * @param code
     * @param json
     * @return
     */
    public GWResponseGson applyModifyDividend(String code, String json) {

        return callGateway(code, json, MODIFY_DIVIDEND_URL);
    }

    private GWResponseGson callGateway(String code, String json, String url) {

        Form form = new Form();
        form.add("json", json);
        form.add("code", code); // 基金机构代码
        GWResponseGson resp = new GWResponseGson();
        try {
            Logger.info(this, String.format("callGateway " + url + ", request parameter is [%s]|[%s]", code, json));
            InterfaceCallObject interfaceCallObject = new InterfaceCallObject().withUrl(url).withForm(form);
            if (code.equals("dh103")) {
                resp = dahuaJijinCaller.post(interfaceCallObject, GWResponseGson.class);
            } else {
                resp = jijinCaller.post(interfaceCallObject, GWResponseGson.class);
            }

        } catch (Exception e) {
            resp.setRetCode(GWResponseCode.RUNTIME_ERROR);
            resp.setRetMessage(e.getMessage());
        }
        Logger.info(this, "call jijin " + url + ", response:" + JsonHelper.toJson(resp));
        return resp;
    }

    public GWResponseGson transformBuy(String code, String json) {
        return callGateway(code, json, TRANSFORM_BUY_URL);
    }

}
