package com.lufax.jijin.ylx.fund.repository;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.lufax.jijin.base.repository.BusdataBaseRepository;
import com.lufax.jijin.base.utils.DateUtils;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.ylx.fund.dto.InsuranceFundRecordDTO;

@Repository
public class InsuranceFundRecordRepository extends BusdataBaseRepository<InsuranceFundRecordDTO> {

    @Override
    protected String nameSpace() {
        return "INSURANCE_FUND_RECORD";
    }

    public InsuranceFundRecordDTO insertInsuranceFundRecord(InsuranceFundRecordDTO insuranceFundRecordDTO) {
        return insert("insertInsuranceFundRecord", insuranceFundRecordDTO);
    }

    public List<InsuranceFundRecordDTO> findInsuranceFundRecordsByStatusAndBatchNum(Integer status, int batchNumber, Integer insuranceType) {
        return queryList("findInsuranceFundRecordsByStatusAndBatchNum", MapUtils.buildKeyValueMap("status", status, "batchNumber", batchNumber, "insuranceType", insuranceType));
    }

    public Integer updateInsuranceFundRecord(Long id, Long version, Integer status, String recordId) {
        return update("updateInsuranceFundRecord", MapUtils.buildKeyValueMap("id", id, "status", status, "version", version, "recordId", recordId));
    }
    
    public Integer updateInsuranceFundRecordStatus(Integer statusFrom ,Integer statusTo, Long id, Long version) {
        return update("updateInsuranceFundRecordByStatus", MapUtils.buildKeyValueMap("id", id, "statusFrom", statusFrom, "version", version, "statusTo", statusTo));
    }
    
    public Integer updateInsuranceFundRecordStatusById(Integer status, Long id) {
        return update("updateInsuranceFundRecordStatusById", MapUtils.buildKeyValueMap("id", id, "status", status));
    }

    public InsuranceFundRecordDTO findByRecordId(String recordId) {
        return query("findByRecordId", MapUtils.buildKeyValueMap("recordId", recordId));
    }

    public InsuranceFundRecordDTO findById(Long id) {
        return query("findById", MapUtils.buildKeyValueMap("id", id));
    }

    public InsuranceFundRecordDTO findByTypeFundDateProductId(int type, String fundDate, long productId) {
        return query("findByTypeFundDateProductId", MapUtils.buildKeyValueMap("type", type, "fundDate", fundDate, "productId", productId));
    }

    public List<InsuranceFundRecordDTO> findOvertimeProcessingFundRecords(int batchNumber, Integer insuranceType) {
        return queryList("findOvertimeProcessingFundRecords", MapUtils.buildKeyValueMap("insuranceType", insuranceType, "batchNumber", batchNumber, "dateTime", DateUtils.startOfDay(new Date())));
    }
}

