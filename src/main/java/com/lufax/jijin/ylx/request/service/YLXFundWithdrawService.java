package com.lufax.jijin.ylx.request.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lufax.jijin.event.entity.FundPaymentResultEvent;
import com.lufax.jijin.ylx.dto.repository.YLXSellRequestDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.lufax.jersey.utils.Logger;
import com.lufax.jijin.base.constant.InstructionType;
import com.lufax.jijin.base.repository.BizParametersRepository;
import com.lufax.jijin.base.repository.InstructionNoGenerator;
import com.lufax.jijin.base.utils.DateUtils;
import com.lufax.jijin.dto.FundLoanRequestDTO;
import com.lufax.jijin.product.dto.ProductDTO;
import com.lufax.jijin.product.repository.ProductRepository;
import com.lufax.jijin.repository.FundLoanRequestRepository;
import com.lufax.jijin.sysFacade.gson.result.BaseResultDTO;
import com.lufax.jijin.sysFacade.service.ExtInterfaceService;
import com.lufax.jijin.sysFacade.service.FundService;
import com.lufax.jijin.user.domain.BankAccountDetailGson;
import com.lufax.jijin.user.service.UserService;
import com.lufax.jijin.ylx.batch.domain.YLXBatchStatus2;
import com.lufax.jijin.ylx.batch.dto.YLXBatchDTO;
import com.lufax.jijin.ylx.batch.repository.YLXBatchRepository;
import com.lufax.jijin.ylx.dto.YLXBuyRequestDetailDTO;
import com.lufax.jijin.ylx.fund.dto.InsuranceFundRecordDTO;
import com.lufax.jijin.ylx.fund.dto.InsuranceFundRecordHisDTO;
import com.lufax.jijin.ylx.fund.repository.InsuranceFundRecordHisRepository;
import com.lufax.jijin.ylx.fund.repository.InsuranceFundRecordRepository;
import com.lufax.jijin.ylx.util.InsuranceFundRecordStatus;
import com.lufax.jijin.ylx.util.InsuranceFundRecordType;
import com.lufax.jijin.ylx.util.YlxConstants;

@Component
public class YLXFundWithdrawService {

    @Autowired
    InsuranceFundRecordRepository insuranceFundRecordRepository;
    @Autowired
    InsuranceFundRecordHisRepository insuranceFundRecordHisRepository;
    @Autowired
    FundLoanRequestRepository fundLoanRequestRepository;
    @Autowired
    UserService userService;
    @Autowired
    InstructionNoGenerator instructionNoGenerator;
    @Autowired
    BizParametersRepository bizParametersRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    YLXBatchRepository ylxBatchRepository;
    @Autowired
    FundService fundService;
    @Autowired
    ExtInterfaceService extInterfaceService;
    @Autowired
    YLXSellRequestDetailRepository ylxSellRequestDetailRepository;

    @Transactional
    public void createFundRecord(List<YLXBuyRequestDetailDTO> list,YLXBatchDTO batchDTO){
        Logger.info(this, String.format("createFundRecord for batchId:%s,list size:%s",batchDTO.getId(),list.size()));
        Map<Long, List<YLXBuyRequestDetailDTO>>  map = groupRequestRecordsByProductId(list);
        for(Long prdId : map.keySet()){
            createFundRecordByProductId(map.get(prdId), batchDTO);
        }
        ylxBatchRepository.updateYLXBatchDTOSuccessById(batchDTO.getId(), YLXBatchStatus2.REQUEST_WITHDRAW.name(),null);
    }

    private void createFundRecordByProductId(List<YLXBuyRequestDetailDTO> list,YLXBatchDTO batchDTO){
        FundLoanRequestDTO fundLoanRequestDTO = fundLoanRequestRepository.getFundLoanRequestByProductCode(list.get(0).getProductCode());
        BankAccountDetailGson bankAccountDetailGson = userService.bankAccountDetail(fundLoanRequestDTO.getLoaneeUserId());
        ProductDTO productDTO = productRepository.getById(list.get(0).getProductId());
        BigDecimal totalAmt = BigDecimal.ZERO;
        for (YLXBuyRequestDetailDTO ylxBuyRequestDetailDTO : list) {
            totalAmt = totalAmt.add(ylxBuyRequestDetailDTO.getAmount());
        }
        List<BigDecimal> fundAmtList = splitAmt(totalAmt);
        for(int i=0;i<fundAmtList.size(); i++){
            generateFundRecord(fundAmtList.get(i),i,fundAmtList.size(),bankAccountDetailGson,productDTO,fundLoanRequestDTO,batchDTO);
        }
    }
    private Map<Long, List<YLXBuyRequestDetailDTO>> groupRequestRecordsByProductId(List<YLXBuyRequestDetailDTO> list) {
        Map<Long, List<YLXBuyRequestDetailDTO>> map = new HashMap<Long, List<YLXBuyRequestDetailDTO>>();


        for (YLXBuyRequestDetailDTO dto : list) {
            if (map.containsKey(dto.getProductId())) {
                List<YLXBuyRequestDetailDTO> subList = map.get(dto.getProductId());
                subList.add(dto);
            } else {
                List<YLXBuyRequestDetailDTO> subList = new ArrayList<YLXBuyRequestDetailDTO>();
                subList.add(dto);
                map.put(dto.getProductId(), subList);
            }
        }
        return map;
    }
    private BigDecimal getYlxWithdrawLimit() {
        return new BigDecimal(bizParametersRepository.findByCode(YlxConstants.YLX_WITHDRAW_LIMIT).getValue());
    }
    private List<BigDecimal> splitAmt(BigDecimal totalAmt){
        List<BigDecimal> list = new ArrayList<BigDecimal>();
        BigDecimal amtLimit = getYlxWithdrawLimit();
        while(totalAmt.compareTo(amtLimit) > 0){
            list.add(amtLimit);
            totalAmt = totalAmt.subtract(amtLimit);
        }
        list.add(totalAmt);
        return list;
    }

    private void generateFundRecord(BigDecimal fundAmt,int withdrawBatchId,
                                                     int totalBatch,BankAccountDetailGson bankAccountDetailGson, ProductDTO productDTO,FundLoanRequestDTO fundLoanRequestDTO,YLXBatchDTO batchDTO) {
        String recordId = String.format("%s%s", YlxConstants.FUND_RECORD_PREFIX, instructionNoGenerator.generate(InstructionType.WITHDRAWAL));

        InsuranceFundRecordDTO fundRecord = new InsuranceFundRecordDTO();
        fundRecord.setAmount(fundAmt);
        fundRecord.setProductId(productDTO.getId());
        fundRecord.setType(InsuranceFundRecordType.WITHDRAW.getCode());
        fundRecord.setStatus(InsuranceFundRecordStatus.WITHDRAW_NEW.getCode());
        fundRecord.setRecordId(recordId);
        fundRecord.setFromUserId(fundLoanRequestDTO.getLoaneeUserId());
        fundRecord.setToCardId(bankAccountDetailGson.getId());
        fundRecord.setInsuranceType(Integer.valueOf(productDTO.getProductCategory()));
        fundRecord.setFundDate(String.format("%s-%s", DateUtils.formatDate(batchDTO.getTargetDate()), withdrawBatchId));
        fundRecord.setRemark(String.format("%s-%s-(%s/%s)", batchDTO.getId(), productDTO.getCode(), withdrawBatchId, totalBatch));
        if (null != insuranceFundRecordRepository.findByTypeFundDateProductId(fundRecord.getType(), fundRecord.getFundDate(), fundRecord.getProductId())) {
            Logger.info(this, String.format("fund record has already exists , type:[%s],fundDate:[%s],productId[%s]:", fundRecord.getType(), fundRecord.getFundDate(), fundRecord.getProductId()));
            return;
        }

        insuranceFundRecordRepository.insertInsuranceFundRecord(fundRecord);
        insuranceFundRecordHisRepository.insertFaInsuranceFundRecordHis(new InsuranceFundRecordHisDTO(fundRecord, InsuranceFundRecordStatus.WITHDRAW_NEW.getCode()));
        Logger.info(this,String.format("fund record for productId:%s,recordId:%s",fundRecord.getProductId(),fundRecord.getRecordId()));
    }

    public void withdrawForCompany(InsuranceFundRecordDTO insuranceFundRecordDTO){
        BaseResultDTO baseResultDTO = fundService.withdrawForCompany(insuranceFundRecordDTO.getFromUserId(), insuranceFundRecordDTO.getRecordId(), insuranceFundRecordDTO.getAmount(), insuranceFundRecordDTO.getToCardId(), "养老险实力派对公代付");
        if (baseResultDTO.isSuccess()) {
            Logger.info(this, String.format("apply withdraw success for YLX InsuranceFundRecordId :[%s]", insuranceFundRecordDTO.id()));
            insuranceFundRecordRepository.updateInsuranceFundRecord(insuranceFundRecordDTO.id(), insuranceFundRecordDTO.getVersion(), InsuranceFundRecordStatus.WITHDRAW_PROCESSING.getCode(), insuranceFundRecordDTO.getRecordId());
            insuranceFundRecordHisRepository.insertFaInsuranceFundRecordHis(new InsuranceFundRecordHisDTO(insuranceFundRecordDTO, InsuranceFundRecordStatus.WITHDRAW_PROCESSING.getCode()));
        } else {
            insuranceFundRecordHisRepository.insertFaInsuranceFundRecordHis(new InsuranceFundRecordHisDTO(insuranceFundRecordDTO, InsuranceFundRecordStatus.INVOKE_FUND_FAIL.getCode()));
            Logger.info(this, String.format("call fund failed, withdraw company failed for YLX InsuranceFundRecordId:[%s]", insuranceFundRecordDTO.id()));
        }
    }
    public RESULT handleWithdrawForCompanyResult(InsuranceFundRecordDTO insuranceFundRecordDTO, FundPaymentResultEvent event) {

        if (!insuranceFundRecordDTO.getStatus().equals(InsuranceFundRecordStatus.WITHDRAW_PROCESSING.getCode())) {
            Logger.warn(this, String.format("insuranceFundRecordDTO status is not processing , event ignore. recordId [%s]", insuranceFundRecordDTO.getRecordId()));
            return RESULT.IGNORE;
        }
        if (event.getStatus().equalsIgnoreCase("success") && insuranceFundRecordDTO.getAmount().compareTo(event.getAmt()) != 0) {
            Logger.warn(this, String.format("NEED_MANUAL_HANDLE , actual withdraw amount not equal need withdraw amount, recordId [%s] actualAmount [%S] amount [%s]", insuranceFundRecordDTO.getRecordId(), event.getAmt(), insuranceFundRecordDTO.getAmount()));
        }

        if (event.getStatus().equalsIgnoreCase("success")) {
            insuranceFundRecordRepository.updateInsuranceFundRecord(insuranceFundRecordDTO.id(), insuranceFundRecordDTO.getVersion(), InsuranceFundRecordStatus.WITHDRAW_SUCCESS.getCode(), insuranceFundRecordDTO.getRecordId());
            insuranceFundRecordHisRepository.insertFaInsuranceFundRecordHis(new InsuranceFundRecordHisDTO(insuranceFundRecordDTO, InsuranceFundRecordStatus.WITHDRAW_SUCCESS.getCode()));
            return RESULT.SUCCESS;
        } else {
            insuranceFundRecordRepository.updateInsuranceFundRecord(insuranceFundRecordDTO.id(), insuranceFundRecordDTO.getVersion(), InsuranceFundRecordStatus.WITHDRAW_FAIL.getCode(), insuranceFundRecordDTO.getRecordId());
            insuranceFundRecordHisRepository.insertFaInsuranceFundRecordHis(new InsuranceFundRecordHisDTO(insuranceFundRecordDTO, InsuranceFundRecordStatus.WITHDRAW_FAIL.getCode()));
            return RESULT.FAIL;
        }
    }

    @Transactional
    public RESULT handleRechargeForCompanyResult(InsuranceFundRecordDTO insuranceFundRecordDTO, FundPaymentResultEvent event) {

        if (!insuranceFundRecordDTO.getStatus().equals(InsuranceFundRecordStatus.RECHARGE_PROCESSING.getCode())) {
            Logger.warn(this, String.format("insuranceFundRecordDTO status is not processing , event ignore. recordId [%s]", insuranceFundRecordDTO.getRecordId()));
            return RESULT.IGNORE;
        }
        if (event.getStatus().equalsIgnoreCase("success") && insuranceFundRecordDTO.getAmount().compareTo(event.getAmt()) != 0) {
            Logger.warn(this, String.format("NEED_MANUAL_HANDLE , actual recharge amount not equal need withdraw amount, recordId [%s] actualAmount [%S] amount [%s]", insuranceFundRecordDTO.getRecordId(), event.getAmt(), insuranceFundRecordDTO.getAmount()));
        }

        if (event.getStatus().equalsIgnoreCase("success")) {
            insuranceFundRecordRepository.updateInsuranceFundRecordStatusById(InsuranceFundRecordStatus.RECHARGE_SUCCESS.getCode(), insuranceFundRecordDTO.id());
            insuranceFundRecordHisRepository.insertFaInsuranceFundRecordHis(new InsuranceFundRecordHisDTO(insuranceFundRecordDTO, InsuranceFundRecordStatus.RECHARGE_SUCCESS.getCode()));
            return RESULT.SUCCESS;
        } else {
        	String newRecordId = String.format("%s%s", YlxConstants.FUND_RECORD_PREFIX, instructionNoGenerator.generate(InstructionType.RECHARGE));
            insuranceFundRecordRepository.updateInsuranceFundRecord(insuranceFundRecordDTO.id(), insuranceFundRecordDTO.getVersion(), InsuranceFundRecordStatus.RECHARGE_NEW.getCode(), newRecordId);
            insuranceFundRecordHisRepository.insertFaInsuranceFundRecordHis(new InsuranceFundRecordHisDTO(insuranceFundRecordDTO, InsuranceFundRecordStatus.RECHARGE_FAIL.getCode()));
            ylxSellRequestDetailRepository.updateSellRequestToNewRecordId(insuranceFundRecordDTO.getRecordId(),newRecordId);
            return RESULT.FAIL;
        }
    }
    public enum RESULT {
        SUCCESS,
        FAIL,
        IGNORE
    }


}
