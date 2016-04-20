package com.lufax.jijin.ylx.job;

import java.util.List;

import com.lufax.jijin.scheduler.basejob.BaseBatchJob;
import com.lufax.jijin.trade.service.YlxFundRedeemService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.product.constant.ProductCategory;
import com.lufax.jijin.ylx.fund.dto.InsuranceFundRecordDTO;
import com.lufax.jijin.ylx.fund.repository.InsuranceFundRecordRepository;
import com.lufax.jijin.ylx.request.service.YLXFundWithdrawService;
import com.lufax.jijin.ylx.util.InsuranceFundRecordStatus;

@Component
public class YLXFundRechargeStep_2 extends BaseBatchJob<InsuranceFundRecordDTO,Long> {
    @Autowired
    private YLXFundWithdrawService ylxFundWithdrawService;
    @Autowired
    private InsuranceFundRecordRepository insuranceFundRecordRepository;
    @Autowired
    private YlxFundRedeemService ylxFundRedeemService;
    @Override
    protected Long getKey(InsuranceFundRecordDTO item) {
        return item.id();
    }

    @Override
    protected List<InsuranceFundRecordDTO> fetchList(int batchAmount) {
        return insuranceFundRecordRepository.findInsuranceFundRecordsByStatusAndBatchNum(InsuranceFundRecordStatus.RECHARGE_NEW.getCode(), batchAmount, Integer.valueOf(ProductCategory.SLP.getCode()));
    }

    @Override
    protected void process(InsuranceFundRecordDTO insuranceFundRecordDTO) {
        try {
        	ylxFundRedeemService.rechargeForCompany(insuranceFundRecordDTO);
        }catch (Exception e){
            Logger.error(this, String.format("withdrawForCompany failed,recordId:%s", insuranceFundRecordDTO.getRecordId()),e);
        }
    }
}
