package com.lufax.jijin.event.handler.impl;


import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.lufax.jijin.ylx.dto.YLXAccountDTO;
import com.lufax.jijin.ylx.dto.repository.YLXAccountRepository;
import com.lufax.jijin.ylx.enums.YLXOpenRequestStatus;
import com.lufax.jijin.ylx.enums.YLXBuyRequestStatus;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.JsonObject;
import com.lufax.jijin.base.constant.FundName;
import com.lufax.jijin.base.service.TradeDayService;
import com.lufax.jijin.base.utils.*;
import com.lufax.jijin.dto.FundAccountDTO;
import com.lufax.jijin.dto.FundLoanRequestDTO;
import com.lufax.jijin.event.handler.EventContext;
import com.lufax.jijin.event.handler.EventHandler;
import com.lufax.jijin.product.dto.ProductDTO;
import com.lufax.jijin.product.repository.ProductRepository;
import com.lufax.jijin.repository.FundAccountRepository;
import com.lufax.jijin.repository.FundLoanRequestRepository;
import com.lufax.jijin.trade.dto.InvestmentRequestDTO;
import com.lufax.jijin.trade.repository.InvestmentRequestRepository;
import com.lufax.jijin.user.domain.UserInfoGson;
import com.lufax.jijin.user.service.UserService;
import com.lufax.jijin.ylx.dto.YLXBuyRequestDetailDTO;
import com.lufax.jijin.ylx.dto.YLXOpenRequestDetailDTO;
import com.lufax.jijin.ylx.dto.repository.YLXBuyRequestDetailRepository;
import com.lufax.jijin.ylx.dto.repository.YLXOpenRequestDetailRepository;
import com.lufax.jijin.ylx.enums.YLXTradeDetailType;
import com.lufax.jijin.ylx.util.YlxConstants;

@Component
public class YLXFundTransferSuccessHandler extends EventHandler{

    @Autowired
    InvestmentRequestRepository investmentRequestRepository;
    @Autowired
    TradeDayService tradeDayService;
    @Autowired
    FundAccountRepository fundAccountRepository;
    @Autowired
    FundLoanRequestRepository fundLoanRequestRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    YLXBuyRequestDetailRepository ylxBuyRequestDetailRepository;
    @Autowired
    YLXOpenRequestDetailRepository ylxOpenRequestDetailRepository;
    @Autowired
    UserService userService;
    @Autowired
    YLXAccountRepository ylxAccountRepository;


    @Transactional
    public void handle(EventContext ctx) {

        JsonObject jsonObj = JsonHelper.parse(ctx.getMessage());
        long trxId = jsonObj.get("trxId").getAsLong();
        long productId = jsonObj.get("productId").getAsLong();
        YLXBuyRequestDetailDTO ylxBuyRequestDetailDTO = ylxBuyRequestDetailRepository.getYLXBuyRequestDetailDTOByTrxId(trxId);
        if(null != ylxBuyRequestDetailDTO){
            Logger.info(this, String.format("YLXBuyRequest already created trxId:%s",trxId));
            return;
        }
        Date tradeDate = tradeDayService.getTargetTradeDay(new Date());
        YLXBuyRequestDetailDTO buyRequest = new YLXBuyRequestDetailDTO();
        ProductDTO productDTO = productRepository.getById(productId);
        InvestmentRequestDTO investmentRequestDTO = investmentRequestRepository.getInvestmentRequestByTrxId(trxId);
        FundLoanRequestDTO fundLoanRequestDTO = fundLoanRequestRepository.getFundLoanRequestByProductCode(productDTO.getCode());
        
        buyRequest.setBatchId(-1L);
        buyRequest.setInternalTrxId(trxId);
        buyRequest.setTrxDate(DateUtils.startOfDay(tradeDate));
        buyRequest.setTrxTime(tradeDate);
        buyRequest.setBankAccount(String.valueOf(investmentRequestDTO.getLoanerId()));
        buyRequest.setProdCode(fundLoanRequestDTO.getThirdCompanyCode());
        buyRequest.setBuyType(YlxConstants.BUY_TYPE);
        buyRequest.setFundShare(investmentRequestDTO.getInvestmentAmount());
        buyRequest.setAmount(investmentRequestDTO.getInvestmentAmount());
        buyRequest.setCurrency(YlxConstants.CURRENCY_RMB);
        buyRequest.setProductCode(productDTO.getCode());
        buyRequest.setProductCategory(productDTO.getProductCategory());
        buyRequest.setProductId(productId);
        buyRequest.setUserId(investmentRequestDTO.getLoanerId());
        if(!EmptyChecker.isEmpty(productDTO.getOnlineStatusFlag())&&productDTO.getOnlineStatusFlag())
            buyRequest.setInternalBuyType(YLXTradeDetailType.PURCHASE.name());
        else
            buyRequest.setInternalBuyType(YLXTradeDetailType.BUY.name());
        buyRequest.setStatus(YLXBuyRequestStatus.BUYING.name());

        ylxBuyRequestDetailRepository.batchInsert(Arrays.asList(buyRequest));

        List<YLXAccountDTO>  ylxAccountDTOs = ylxAccountRepository.getUserDTOsByIds(Arrays.asList(investmentRequestDTO.getLoanerId()));
        List<FundAccountDTO> fundAccountDTOs = fundAccountRepository.findBusFundAccount(MapUtils.buildKeyValueMap("userId", investmentRequestDTO.getLoanerId(), "fundName", FundName.YLX.name()));
        if(CollectionUtils.isNotEmpty(ylxAccountDTOs)&&CollectionUtils.isNotEmpty(fundAccountDTOs)) {
            Logger.info(this, String.format("fund account already created fundAccountId:%s", fundAccountDTOs.get(0).getId()));
            return;
        }
        if(CollectionUtils.isNotEmpty(ylxAccountDTOs)&&CollectionUtils.isEmpty(fundAccountDTOs)) {
            FundAccountDTO fundAccountDTO = new FundAccountDTO();
            fundAccountDTO.setUserId(ylxAccountDTOs.get(0).getUserId());
            fundAccountDTO.setThirdAccount(ylxAccountDTOs.get(0).getThirdAccount());
            fundAccountDTO.setThirdCustomerAccount(ylxAccountDTOs.get(0).getThirdCustomerAccount());
            fundAccountDTO.setFundName(FundName.YLX.name());
            fundAccountDTO.setBalance(BigDecimal.ZERO);
            fundAccountDTO.setFrozenAmount(BigDecimal.ZERO);
            fundAccountDTO.setFundShare(BigDecimal.ZERO);
            fundAccountDTO.setFrozenFundShare(BigDecimal.ZERO);
            fundAccountDTO.setVersion(0L);
            fundAccountRepository.insertBusFundAccount(fundAccountDTO);
            Logger.info(this, String.format("account already exists in ylx_account table,just add to bus_fund_account table ylxAccountId:%s", ylxAccountDTOs.get(0).getId()));
            return;
        }
        if(CollectionUtils.isEmpty(ylxAccountDTOs)&&CollectionUtils.isEmpty(fundAccountDTOs)){
            UserInfoGson user = userService.getUserInfo(investmentRequestDTO.getLoanerId());
            YLXOpenRequestDetailDTO openRequest = new YLXOpenRequestDetailDTO();

            openRequest.setBatchId(-1L);
            openRequest.setInternalTrxId(trxId);
            openRequest.setLufaxBank(YlxConstants.LUFAX_BANK);
            openRequest.setLufaxBankCode(YlxConstants.LUFAX_BANK_CODE);
            openRequest.setTrxDate(DateUtils.startOfDay(tradeDate));
            openRequest.setTrxTime(tradeDate);
            openRequest.setBankAccount(String.valueOf(investmentRequestDTO.getLoanerId()));
            openRequest.setVirtualBankAccount(String.valueOf(investmentRequestDTO.getLoanerId()));
            openRequest.setBankAccountType("01");

            openRequest.setName(user.getName());
            openRequest.setIdNo(user.getIdNo());
            String idType = YlxConstants.ID_TYPE_DESC_MAP.get(user.getIdType());
            if(null==idType)idType="7";
            openRequest.setIdType(idType);
            openRequest.setMobileNo(user.getMobileNo());
            openRequest.setSex(user.getSex());
            openRequest.setRiskLevel(getYLXRiskLevel(user.getRiskVerifyStatus()));
            openRequest.setStatus(YLXOpenRequestStatus.OPENING.name());

            ylxOpenRequestDetailRepository.batchInsert(Arrays.asList(openRequest));

        }

    }

    /**
     * usually the input lufaxRiskLevel should be 3 or 4
     * 3 mapping to 2
     * 4 mapping to 3
     *
     * consider some exception value, use 2 as default value to unblock biz
     * @param lufaxRiskLevel
     * @return
     */
    private String getYLXRiskLevel(String lufaxRiskLevel){

        String YLXRiskLevel = "2"; // set as default
        if("4".equals(lufaxRiskLevel)){
            YLXRiskLevel="3";
        }

        return YLXRiskLevel;
    }

}