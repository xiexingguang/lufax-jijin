package com.lufax.jijin.sysFacade.gson.result;
import com.lufax.jijin.base.utils.StringUtils;

import java.math.BigDecimal;

/**
 * payment旧接口的返回值
 * @author chenqunhui168
 *
 */
@Deprecated
public class PaymentResultGson {

    private String channelId;//渠道
    private String instructionId;//流水号
    private String type;//类型
    private BigDecimal amount;//申请金额
    private Long userId;//用户id
    private String status;  //支付结果 success 成功/fail 失败
    private String requestStatus;//支付申请结果， SUCCESS 成功 FAILURE 失败
    private String tradeTime;// 交易发起时间 业务系统申请时候带的参数
    private String settleTime;//实际支付时间
    private BigDecimal actualAmount;//支付成功金额
    private String retCode;// 查询请求 状态 -000 成功 /其他  失败
    private String retMessage;//查询请求错误信息

    public boolean isSuccess() {
        if (!this.retCode.equals("000") || StringUtils.isBlank(this.status)) {
            return false;
        }
        return this.retCode.equals("000") && this.status.equals("success");
    }

    public boolean isFail() {
        if (!this.retCode.equals("000") || StringUtils.isBlank(this.status)) {
            return false;
        }
        return this.retCode.equals("000") && this.status.equals("fail");
    }

    public BigDecimal getActualAmount() {
        return actualAmount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getChannelId() {
        return channelId;
    }

    public String getInstructionId() {
        return instructionId;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public String getRetCode() {
        return retCode;
    }

    public String getRetMessage() {
        return retMessage;
    }

    public String getSettleTime() {
        return settleTime;
    }

    public String getStatus() {
        return status;
    }

    public String getTradeTime() {
        return tradeTime;
    }

    public String getType() {
        return type;
    }

    public Long getUserId() {
        return userId;
    }
}
