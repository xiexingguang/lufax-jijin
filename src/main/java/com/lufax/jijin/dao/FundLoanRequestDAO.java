package com.lufax.jijin.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.dto.FundLoanRequestDTO;

@Repository
public class FundLoanRequestDAO extends BusdataBaseDAO {

    public Object insertFundLoanRequest(FundLoanRequestDTO fundLoanRequestDTO) {
        return super.insert("insertFundLoanRequest", fundLoanRequestDTO);
    }

    public FundLoanRequestDTO getFundLoanRequestByForeignId(Long foreignId) {
        return super.queryForObject("getFundLoanRequestByForeignId", MapUtils.buildKeyValueMap("foreignId", foreignId));
    }

    public FundLoanRequestDTO getFundLoanRequestByProductCode(String productCode) {
        return super.queryForObject("getFundLoanRequestByProductCode", MapUtils.buildKeyValueMap("productCode", productCode));
    }
    
    public List<FundLoanRequestDTO> getFundLoanRequestByProductCodes(List<String> codes) {
        return super.queryForList("getFundLoanRequestByProductCodes", MapUtils.buildKeyValueMap("codes", codes));
    }
    
    public FundLoanRequestDTO getFundLoanRequestByThirdCompanyCode(String thirdCompanyCode) {
        return super.queryForObject("getFundLoanRequestByThirdCompanyCode", MapUtils.buildKeyValueMap("thirdCompanyCode", thirdCompanyCode));
    }
    
    public List<FundLoanRequestDTO> getFundLoanRequestBySourceTypeAndCategory(String sourceType, String prodCategory) {
        return super.queryForList("getFundLoanRequestBySourceTypeAndCategory",
        		MapUtils.buildKeyValueMap("sourceType", sourceType,"productCategory",prodCategory));
    }

	@Override
	protected String nameSpace() {
		return "FundLoanRequest";
	}

}
