package com.lufax.jijin.ylx.job;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.lufax.jijin.scheduler.basejob.BaseBatchJob;
import com.lufax.jijin.trade.service.YlxFundRedeemService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.product.constant.ProductCategory;
import com.lufax.jijin.product.dto.ProductDTO;
import com.lufax.jijin.product.repository.ProductRepository;
import com.lufax.jijin.ylx.fund.dto.InsuranceFundRecordDTO;
import com.lufax.jijin.ylx.fund.repository.InsuranceFundRecordRepository;
import com.lufax.jijin.ylx.request.service.YLXFundWithdrawService;
import com.lufax.jijin.ylx.util.InsuranceFundRecordStatus;

@Component
public class YLXFundRechargeStep_3 extends BaseBatchJob<InsuranceFundRecordDTO,Long> {
    @Autowired
    private YLXFundWithdrawService ylxFundWithdrawService;
    @Autowired
    private InsuranceFundRecordRepository insuranceFundRecordRepository;
    @Autowired
    private YlxFundRedeemService ylxFundRedeemService;
    @Autowired
    private ProductRepository productDao;
    @Override
    protected Long getKey(InsuranceFundRecordDTO item) {
        return item.id();
    }

    @Override
    protected List<InsuranceFundRecordDTO> fetchList(int batchAmount) {
    	List<InsuranceFundRecordDTO> result = insuranceFundRecordRepository.findInsuranceFundRecordsByStatusAndBatchNum(InsuranceFundRecordStatus.RECHARGE_SUCCESS.getCode(), batchAmount, Integer.valueOf(ProductCategory.SLP.getCode()));
    	
    	List<Long> productIds = new ArrayList<Long>();
    	for(InsuranceFundRecordDTO d : result){
    		productIds.add(d.getProductId());
    	}
    	List<ProductDTO> products = productDao.getByIds(productIds);
    	
    	for(InsuranceFundRecordDTO d : result){
    		for(ProductDTO p : products){
    			if(d.getProductId().longValue() == p.getId()){
    				d.setProductDisPlayName(p.getDisplayName());
    				break;
    			}
    		}
    	}
        return result;
    }

    @Override
    protected void process(InsuranceFundRecordDTO insuranceFundRecordDTO) {
        try {
        	int num = insuranceFundRecordRepository.updateInsuranceFundRecordStatus(InsuranceFundRecordStatus.RECHARGE_SUCCESS.getCode(), InsuranceFundRecordStatus.DISTRIBUTING.getCode(), insuranceFundRecordDTO.getId(), insuranceFundRecordDTO.getVersion());
        	if(num > 0){
        		ylxFundRedeemService.rechargeForUser(insuranceFundRecordDTO);
        		insuranceFundRecordRepository.updateInsuranceFundRecordStatusById(InsuranceFundRecordStatus.DISTRIBUTED.getCode(), insuranceFundRecordDTO.getId());
        	}
        }catch (Exception e){
            Logger.error(this, String.format("withdrawForCompany failed,recordId:%s", insuranceFundRecordDTO.getRecordId()),e);
            insuranceFundRecordRepository.updateInsuranceFundRecordStatusById(InsuranceFundRecordStatus.RECHARGE_SUCCESS.getCode(), insuranceFundRecordDTO.getId());
        }
    }
}
