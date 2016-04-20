package com.lufax.jijin.event.handler.impl;

import com.lufax.jijin.base.utils.DateUtils;
import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.event.entity.FundPaymentResultEvent;
import com.lufax.jijin.event.entity.FundPaymentResultEventParser;
import com.lufax.jijin.event.handler.EventContext;
import com.lufax.jijin.event.handler.EventHandler;
import com.lufax.jijin.ylx.fund.dto.InsuranceFundRecordDTO;
import com.lufax.jijin.ylx.fund.repository.InsuranceFundRecordRepository;
import com.lufax.jijin.ylx.remote.YLXSmsService;
import com.lufax.jijin.ylx.request.service.YLXFundWithdrawService;
import com.lufax.jijin.ylx.util.InsuranceFundRecordType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class YLXFundPaymentResultHandler extends EventHandler{
    @Autowired
    private YLXFundWithdrawService ylxFundWithdrawService;
    @Autowired
    private InsuranceFundRecordRepository insuranceFundRecordRepository;
    @Autowired
    private YLXSmsService smsService;

    @Override
    public void handle(EventContext ctx) {
        Logger.info(this, String.format("Event of [insurance-fund-payment-result][message: %s] received.", ctx.getMessage()));


        FundPaymentResultEvent event = FundPaymentResultEventParser.parse(ctx.getMessage());
        InsuranceFundRecordDTO insuranceFundRecordDTO = insuranceFundRecordRepository.findByRecordId(event.getRecordId());

        if (null == insuranceFundRecordDTO) {
            Logger.warn(this, String.format("InsuranceFundRecordType not exists , event ignore. recordId [%s]", event.getRecordId()));
            return;
        }
        InsuranceFundRecordType fundType = InsuranceFundRecordType.getByCode(insuranceFundRecordDTO.getType());
        if (null == fundType) {
            Logger.warn(this, String.format("InsuranceFundRecordType not exists , event ignore. recordId [%s],InsuranceFundRecordType [%s]", event.getRecordId(), fundType));
            return;
        }
        if (InsuranceFundRecordType.WITHDRAW == fundType) {
            YLXFundWithdrawService.RESULT result = ylxFundWithdrawService.handleWithdrawForCompanyResult(insuranceFundRecordDTO, event);
            if (YLXFundWithdrawService.RESULT.SUCCESS.equals(result)) {
                smsService.sendYlxWithdrawSuccessMessage(
                        DateUtils.parseDate(insuranceFundRecordDTO.getFundDate()),
                        insuranceFundRecordDTO.getRemark(), insuranceFundRecordDTO.getAmount());
            } else if (YLXFundWithdrawService.RESULT.FAIL.equals(result)) {
                smsService.sendYlxWithdrawFailedMessage(
                        DateUtils.parseDate(insuranceFundRecordDTO.getFundDate()),
                        insuranceFundRecordDTO.getRemark());
            } else {
                Logger.warn(this, "withdraw result IGNORE found. record id:" + event.getRecordId());
            }
        } else if (InsuranceFundRecordType.RECHARGE == fundType) {
        	 YLXFundWithdrawService.RESULT result = ylxFundWithdrawService.handleRechargeForCompanyResult(insuranceFundRecordDTO, event);
             if (YLXFundWithdrawService.RESULT.SUCCESS.equals(result)) {
                 smsService.sendYlxWithdrawSuccessMessage(
                         DateUtils.parseDate(insuranceFundRecordDTO.getFundDate()),
                         insuranceFundRecordDTO.getRemark(), insuranceFundRecordDTO.getAmount());
             } else if (YLXFundWithdrawService.RESULT.FAIL.equals(result)) {
                 smsService.sendYlxWithdrawFailedMessage(
                         DateUtils.parseDate(insuranceFundRecordDTO.getFundDate()),
                         insuranceFundRecordDTO.getRemark());
             } else {
                 Logger.warn(this, "recharge result IGNORE found. record id:" + event.getRecordId());
             }
        }
        Logger.info(this, String.format("Event of [insurance-fund-payment-result][message: %s] handled.", ctx.getMessage()));
    }
}
