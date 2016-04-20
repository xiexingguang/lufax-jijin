package com.lufax.jijin.fundation.remote;

import com.google.gson.Gson;
import com.lufax.jijin.base.utils.DateUtils;
import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.fundation.constant.JijinConstants;
import com.lufax.jijin.fundation.remote.gson.request.PaymentRequestGson;
import com.lufax.jijin.fundation.remote.gson.request.PaymentRequestParam;
import com.lufax.jijin.fundation.remote.gson.request.SpecialPaymentRequestParam;
import com.lufax.jijin.fundation.remote.gson.request.UploadResultGson;
import com.lufax.jijin.fundation.remote.gson.response.FundResponseGson;
import com.lufax.jijin.sysFacade.caller.FundInterfaceCaller;
import com.lufax.jijin.sysFacade.caller.InterfaceCallObject;
import com.lufax.jijin.sysFacade.gson.result.BaseResultDTO;
import com.sun.jersey.api.client.ClientResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class FundAppCallerService {

    @Autowired
    private FundInterfaceCaller fundInterfaceCaller;

    /**
     * 先投资后代扣做解冻(申购失败情况)
     *
     * @param paymentRequestParam
     * @return
     */
    public FundResponseGson paymentRequest(PaymentRequestParam paymentRequestParam) {
        InterfaceCallObject object = new InterfaceCallObject().withUrl("/payment-order/payment-request").addFormValue("dto", new Gson().toJson(paymentRequestParam));
        Logger.info(this, "call fund payment request start");
        FundResponseGson resultDTO = fundInterfaceCaller.post(object, FundResponseGson.class);
        Logger.info(this, "call fund payment request end");
        return resultDTO;
    }


    /**
     * 先投资后代扣做解冻(申购成功情况)
     *
     * @param specialPaymentRequestParam
     * @return
     */
    public FundResponseGson specialPaymentRequest(SpecialPaymentRequestParam specialPaymentRequestParam) {
        InterfaceCallObject object = new InterfaceCallObject().withUrl("/payment-order/payment-request").addFormValue("dto", new Gson().toJson(specialPaymentRequestParam));
        Logger.info(this, "call fund payment request start");
        FundResponseGson resultDTO = fundInterfaceCaller.post(object, FundResponseGson.class);
        Logger.info(this, "call fund payment request end");
        return resultDTO;
    }


    /**
     * 1.先投资后代扣做解冻-带清算(申购成功情况)
     * 2.申购撤单
     *
     * @param paymentRequestGson
     * @return
     */
    public FundResponseGson auditPaymentRequest(PaymentRequestGson paymentRequestGson) {
        InterfaceCallObject object = new InterfaceCallObject().withUrl("/payment-order/payment-request").addFormValue("dto", new Gson().toJson(paymentRequestGson));
        Logger.info(this, "call fund audit payment request start:" + new Gson().toJson(paymentRequestGson));
        FundResponseGson resultDTO = fundInterfaceCaller.post(object, FundResponseGson.class);
        Logger.info(this, "call fund audit payment request end");
        return resultDTO;
    }

    /**
     * 1.认申购清账
     *
     * @param uploadResultGson
     * @return
     */
    public FundResponseGson auditUploadResult(UploadResultGson uploadResultGson) {
        InterfaceCallObject object = new InterfaceCallObject().withUrl("/clearing/upload-result").addFormValue("dto", new Gson().toJson(uploadResultGson));
        Logger.info(this, "call fund audit upload result start:" + new Gson().toJson(uploadResultGson));
        FundResponseGson resultDTO = new FundResponseGson();

        try {
            resultDTO = fundInterfaceCaller.post(object, FundResponseGson.class);
        } catch (Exception e) {
            Logger.error(this, "call fund audit occure exception:", e);
            resultDTO.setResultStatus("FAILURE");
            resultDTO.setRetMessage("runtime exception" + e.getClass().getName());
            resultDTO.setRetCode("999");
        }
        Logger.info(this, "call fund audit upload result end");
        return resultDTO;
    }

    /**
     * 先投资后代扣做解冻
     *
     * @param paymentRequestParam
     */
    public boolean callFundUnfrozen(PaymentRequestParam paymentRequestParam) {
        int retry_times = 0;
        FundResponseGson fundResponseGson = new FundResponseGson();
        boolean result = false;

        while (!result) {
            try {
                if (retry_times > 10) break;
                fundResponseGson = paymentRequest(paymentRequestParam);
                result = fundResponseGson.isSuccess();
                return result;
            } catch (Exception e) {
                retry_times = retry_times + 1;
                Logger.warn(this, String.format("call fund unfrozen failed ,recordId [%s]", paymentRequestParam.getInstructionNo()));
            }
        }

        return result;
    }

    /**
     * 先投资后代扣做解冻调减虚拟户
     *
     * @param specialPaymentRequestParam
     */
    public boolean callFundDoSpecialUnfrozen(SpecialPaymentRequestParam specialPaymentRequestParam) {
        int retry_times = 0;
        FundResponseGson fundResponseGson = new FundResponseGson();
        boolean result = false;
        while (!result) {
            try {
                if (retry_times > 100) break;
                fundResponseGson = specialPaymentRequest(specialPaymentRequestParam);
                result = fundResponseGson.isSuccess();
                return result;
            } catch (Exception e) {
                retry_times = retry_times + 1;
                Logger.warn(this, String.format("call fund do special unfrozen failed ,recordId [%s]", specialPaymentRequestParam.getInstructionNo()));
            }
        }
        return result;
    }

    /**
     * 对公代扣(网上钱包使用)
     *
     * @param userId
     * @param instructionId
     * @param amount
     * @param cardId
     * @return
     */
    public BaseResultDTO rechargeDahua(Long userId, String instructionId, BigDecimal amount, String cardId) {
        Logger.info(this, String.format("[rechargeDahua] process start.parameters is [%s]|[%s]|[%s]|[%s]", userId, instructionId, amount, cardId));
        com.lufax.jersey.utils.Logger.info(this, String.format("[rechargeDahua] Call fund-app to recharge for dahua , amount %s ", amount));
        //对公代扣参数
        InterfaceCallObject interfaceCallObject = new InterfaceCallObject().withUrl(String.format("/users/%s/recharge/company", 0))
                .addFormValue("userId", userId)
                .addFormValue("amount", amount)
                .addFormValue("type", JijinConstants.RECHARGE_NAME)
                .addFormValue("instructionId", instructionId)
                .addFormValue("tradeTime", DateUtils.formatDate(new Date(), DateUtils.DATE_TIME_FORMAT))
                .addFormValue("channelId", JijinConstants.RECHARGE_CHANNEL_ID)
                .addFormValue("callbackUrl", "")
                .addFormValue("remark", "大华货基对公代扣")
                .addFormValue("cardId", cardId);
        
        BaseResultDTO baseResultDTO =  new BaseResultDTO();
        try {
	        ClientResponse clientResponse = fundInterfaceCaller.postClientResponse(interfaceCallObject);
	        com.lufax.jersey.utils.Logger.info(this, "[rechargeDahua] Call fund-app to recharge for dahua response status is : " + clientResponse);
	        String result = clientResponse.getEntity(String.class);
	        Logger.info(this, String.format("[rechargeDahua] postClientResponse request is [%s],response is [%s]", new Gson().toJson(interfaceCallObject), result));
	        com.lufax.jersey.utils.Logger.info(this, "[rechargeDahua] Call fund-app to recharge for dahua response info is : " + result);
	        baseResultDTO = new Gson().fromJson(result, BaseResultDTO.class);
	        Logger.info(this, String.format("[rechargeDahua] baseResultDTO is [%s]", new Gson().toJson(baseResultDTO)));
        }catch(Exception e ){
        	baseResultDTO.setRetCode("999");
        	baseResultDTO.setRetMessage("大华货基对公代扣exception:"+e.getClass().getName());
        }
        return baseResultDTO;
    }
}
