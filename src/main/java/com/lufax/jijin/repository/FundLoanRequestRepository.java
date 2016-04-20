package com.lufax.jijin.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lufax.jijin.base.repository.BusdataBaseRepository;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.dto.FundLoanRequestDTO;

@Repository
public class FundLoanRequestRepository extends BusdataBaseRepository<FundLoanRequestDTO> {

    public Object insertFundLoanRequest(FundLoanRequestDTO fundLoanRequestDTO) {
        return super.insert("FundLoanRequest.insertFundLoanRequest", fundLoanRequestDTO);
    }

    public FundLoanRequestDTO getFundLoanRequestByForeignId(Long foreignId) {
        return super.query("getFundLoanRequestByForeignId", MapUtils.buildKeyValueMap("foreignId", foreignId));
    }

    public FundLoanRequestDTO getFundLoanRequestByProductCode(String productCode) {
        return super.query("getFundLoanRequestByProductCode", MapUtils.buildKeyValueMap("productCode", productCode));
    }
    
    
    public List<FundLoanRequestDTO> getFundLoanRequestBySourceTypeAndCategory(String sourceType, String prodCategory) {
        return super.queryList("getFundLoanRequestBySourceTypeAndCategory",
                MapUtils.buildKeyValueMap("sourceType", sourceType, "productCategory", prodCategory));
    }

    @Override
    protected String nameSpace() {
        return "FundLoanRequest";
    }
}
