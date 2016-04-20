package com.lufax.jijin.ylx.fund.repository;

import org.springframework.stereotype.Repository;

import com.lufax.jijin.base.repository.BusdataBaseRepository;
import com.lufax.jijin.ylx.fund.dto.InsuranceFundRecordHisDTO;

@Repository
public class InsuranceFundRecordHisRepository extends BusdataBaseRepository<InsuranceFundRecordHisDTO> {

    @Override
    protected String nameSpace() {
        return "INSURANCE_FUND_RECORD_HIS";
    }

    public InsuranceFundRecordHisDTO insertFaInsuranceFundRecordHis(InsuranceFundRecordHisDTO insuranceFundRecordHisDTO) {
        return insert("insertInsuranceFundRecordHis", insuranceFundRecordHisDTO);
    }

}
