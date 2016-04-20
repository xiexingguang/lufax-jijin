package com.lufax.jijin.ylx.confirm.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lufax.jersey.utils.Logger;
import com.lufax.jijin.base.constant.FundName;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.dto.FundAccountDTO;
import com.lufax.jijin.repository.FundAccountRepository;
import com.lufax.jijin.service.MqService;
import com.lufax.jijin.ylx.batch.dto.YLXBatchFileDTO;
import com.lufax.jijin.ylx.batch.repository.YLXBatchFileRepository;
import com.lufax.jijin.ylx.dto.BuyConfirmDetailDTO;
import com.lufax.jijin.ylx.dto.YLXBuyRequestDetailDTO;
import com.lufax.jijin.ylx.dto.YLXFundBalanceDTO;
import com.lufax.jijin.ylx.dto.YLXTransactionHistoryDTO;
import com.lufax.jijin.ylx.dto.repository.BuyConfirmDetailRepository;
import com.lufax.jijin.ylx.dto.repository.YLXBuyRequestDetailRepository;
import com.lufax.jijin.ylx.dto.repository.YLXFundBalanceRepository;
import com.lufax.jijin.ylx.dto.repository.YLXTransactionHistoryRepository;
import com.lufax.jijin.ylx.enums.YLXBuyRequestStatus;
import com.lufax.jijin.ylx.enums.YLXFundBalanceStatus;
import com.lufax.jijin.ylx.enums.YLXTransactionHistoryType;
import com.lufax.jijin.ylx.util.YlxConstants;

@Service
@Scope
public class BuyConfirmHandler {
	
	@Autowired
	private YLXBatchFileRepository ylxBatchFileRepository;
    @Autowired
    private YLXBuyRequestDetailRepository ylxBuyRequestDetailRepository;
    @Autowired
    private FundAccountRepository fundAccountRepository;
    @Autowired
    private YLXFundBalanceRepository ylxFundBalanceRepository;
    @Autowired
    private BuyConfirmDetailRepository buyConfirmDetailDAO;
    @Autowired
    private MqService mqService;
    @Autowired
    private YLXTransactionHistoryRepository ylxTransactionHistoryRepository;
	
    @Transactional
	public boolean handleBuyConfirmData(YLXBuyRequestDetailDTO ylxBuyRequestDTO, Map<Long, BuyConfirmDetailDTO> buyConfirmMap){
        if (buyConfirmMap.containsKey(ylxBuyRequestDTO.getId())) {
        	BuyConfirmDetailDTO ylxBuyConfirmDTO = buyConfirmMap.get(ylxBuyRequestDTO.getId());
            if (ylxBuyRequestDTO.getAmount().compareTo(ylxBuyConfirmDTO.getAmount()) == 0) {
            	ylxBuyRequestDTO.setStatus(ylxBuyConfirmDTO.getResultCode().equals(YlxConstants.SUCCESS_RESULT_CODE) ? YLXBuyRequestStatus.SUCCESS.name() : YLXBuyRequestStatus.FAIL.name());
                FundAccountDTO fundAccountDTO = fundAccountRepository.findBusFundAccountByUserId(Long.valueOf(ylxBuyConfirmDTO.getBankAccount()));
                if (fundAccountDTO == null) {
                    Logger.warn(this, String.format("can not find user with userId [%s],fundBuyConfirmId [%s]", ylxBuyRequestDTO.getBankAccount(), ylxBuyConfirmDTO.getId()));
                    ylxBuyRequestDTO.setStatus(YLXBuyRequestStatus.NO_ACCOUNT.getCode());
                    ylxBuyRequestDetailRepository.update("updateBuyRequestStatusById", MapUtils.buildKeyValueMap("id", ylxBuyRequestDTO.getId(), "status", ylxBuyRequestDTO.getStatus()));
                    mqService.sendYLXBuyConfirmMsg(String.format("%s-%s", ylxBuyRequestDTO.getBatchId(), ylxBuyRequestDTO.id()), ylxBuyRequestDTO.getInternalTrxId(), YLXTransactionHistoryType.BUY.name(), false);
                    return false;
                }

                if (ylxBuyRequestDTO.getStatus().equals(YLXBuyRequestStatus.SUCCESS.name())) {
                	Logger.info(this, String.format("update fund balance [fundAccountId:%s].[buy confirm Id:%s].", fundAccountDTO.getId(), ylxBuyConfirmDTO.getId()));
                	updateFundBalance(ylxBuyRequestDTO,fundAccountDTO,ylxBuyConfirmDTO);
                	Logger.info(this, String.format("update buy request [buy request Id:%s].", ylxBuyRequestDTO.getId()));
                    ylxBuyRequestDetailRepository.update("updateBuyRequestStatusById",MapUtils.buildKeyValueMap("id", ylxBuyRequestDTO.getId(), "status", ylxBuyRequestDTO.getStatus()));
                    return true;
                } else {
                    ylxBuyRequestDetailRepository.update("updateBuyRequestStatusById", MapUtils.buildKeyValueMap("id", ylxBuyRequestDTO.getId(), "status", ylxBuyRequestDTO.getStatus()));
                    mqService.sendYLXBuyConfirmMsg(String.format("%s-%s", ylxBuyRequestDTO.getBatchId(), ylxBuyRequestDTO.id()), ylxBuyRequestDTO.getInternalTrxId(), YLXTransactionHistoryType.BUY.name(), false);
                    return false;
                }
            } else {
                Logger.warn(this, String.format("buy confirm amount not equal,buy amount [%s],buy confirm amount[%s] ,fundBuyRequestId [%s]", ylxBuyRequestDTO.getAmount(), ylxBuyConfirmDTO.getAmount(), ylxBuyRequestDTO.getId()));
                ylxBuyRequestDTO.setStatus(YLXBuyRequestStatus.FAIL.name());
                ylxBuyRequestDetailRepository.update("updateBuyRequestStatusById", MapUtils.buildKeyValueMap("id", ylxBuyRequestDTO.getId(), "status", ylxBuyRequestDTO.getStatus()));
                mqService.sendYLXBuyConfirmMsg(String.format("%s-%s", ylxBuyRequestDTO.getBatchId(), ylxBuyRequestDTO.id()), ylxBuyRequestDTO.getInternalTrxId(), YLXTransactionHistoryType.BUY.name(), false);
                return false;
            }
        } else {
        	ylxBuyRequestDTO.setStatus(YLXBuyRequestStatus.UN_CONFIRM.name());
            ylxBuyRequestDetailRepository.update("updateBuyRequestStatusById", MapUtils.buildKeyValueMap("id", ylxBuyRequestDTO.getId(), "status", ylxBuyRequestDTO.getStatus(),"confirmDate",ylxBuyRequestDTO.getCreatedAt()));
            mqService.sendYLXBuyConfirmMsg(String.format("%s-%s", ylxBuyRequestDTO.getBatchId(), ylxBuyRequestDTO.id()), ylxBuyRequestDTO.getInternalTrxId(), YLXTransactionHistoryType.BUY.name(), false);
            return false;
        }
	}
    
    /**
     * 将确认文件记录插入ylx_buy_confirm,更新BatchFile记录文件处理到当前行
     * @param dtos
     * @param lineNum
     * @param batchFile
     */
    @Transactional
    public void batchInsertOpenConfirm(List<BuyConfirmDetailDTO> dtos, long lineNum, YLXBatchFileDTO batchFile) {
    	buyConfirmDetailDAO.batchInsert("insert", dtos);
    	ylxBatchFileRepository.update("updateCurrentLineById",
				MapUtils.buildKeyValueMap("id", batchFile.getId(), "currentLine", lineNum));
    }
    
    /**
     * 1.同步此条ylxBuyRequestDetailDTO 数据到bus_ylx_fund_balance
     * 2.Insert 数据到YLX_OP_TRANSACTION_HISTORY
     * @param ylxBuyRequestDetailDTO
     */
    private void updateFundBalance(YLXBuyRequestDetailDTO ylxBuyRequestDetailDTO,FundAccountDTO fundAccountDTO,BuyConfirmDetailDTO ylxBuyConfirmDTO){
    	List<YLXFundBalanceDTO> ylxFundBalances = ylxFundBalanceRepository.findYlxFundBalanceByUserIdAndProductCode(MapUtils.buildKeyValueMap("userId",ylxBuyRequestDetailDTO.getBankAccount(),"productCode",ylxBuyRequestDetailDTO.getProductCode()));
    	if(ylxFundBalances.size()<=0){//无记录
    		YLXFundBalanceDTO ylxFundBalance = new YLXFundBalanceDTO();
    		ylxFundBalance.setBalance(ylxBuyRequestDetailDTO.getAmount());
            ylxFundBalance.setFundAccountId(fundAccountDTO.getId());
    		ylxFundBalance.setCreatedAt(new Date());
    		ylxFundBalance.setCreatedBy("SYS");
    		ylxFundBalance.setUpdatedAt(new Date());
    		ylxFundBalance.setUpdatedBy("SYS");
    		ylxFundBalance.setFrozenAmount(BigDecimal.ZERO);
    		ylxFundBalance.setFrozenFundShare(BigDecimal.ZERO);
    		ylxFundBalance.setFundName(FundName.YLX.name());
    		ylxFundBalance.setFundShare(ylxBuyConfirmDTO.getConfirmFundShare());
    		ylxFundBalance.setProductCategory(ylxBuyRequestDetailDTO.getProductCategory());
    		ylxFundBalance.setProductCode(ylxBuyRequestDetailDTO.getProductCode());
    		ylxFundBalance.setProductId(ylxBuyRequestDetailDTO.getProductId());
    		ylxFundBalance.setStatus(YLXFundBalanceStatus.Valid.name());
    		ylxFundBalance.setThirdAccount(fundAccountDTO.getThirdAccount());
    		ylxFundBalance.setThirdCustomerAccount(fundAccountDTO.getThirdCustomerAccount());
    		ylxFundBalance.setTotalBuyAmount(ylxBuyRequestDetailDTO.getAmount());
    		ylxFundBalance.setUnitPrice(ylxBuyConfirmDTO.getConfirmUnitPrice());
    		ylxFundBalance.setUserId(Long.valueOf(ylxBuyRequestDetailDTO.getBankAccount()));
    		ylxFundBalance.setVersion(0L);
    		ylxFundBalance.setYestodayUnitPrice(ylxBuyConfirmDTO.getConfirmUnitPrice());
    		ylxFundBalanceRepository.insertYlxFundBalance(ylxFundBalance);
    	} else {
    		for(YLXFundBalanceDTO ylxFundBalanceDTO : ylxFundBalances){
    			ylxFundBalanceDTO.setBalance(ylxFundBalanceDTO.getBalance().add(ylxBuyRequestDetailDTO.getAmount()));
    			ylxFundBalanceDTO.setTotalBuyAmount(ylxFundBalanceDTO.getTotalBuyAmount().add(ylxBuyRequestDetailDTO.getAmount()));
    			ylxFundBalanceDTO.setFundShare(ylxFundBalanceDTO.getFundShare().add(ylxBuyConfirmDTO.getConfirmFundShare()));
    			ylxFundBalanceRepository.updateYlxFundBalance(MapUtils.buildKeyValueMap("id",ylxFundBalanceDTO.getId(),"balance",ylxFundBalanceDTO.getBalance(),"totalBuyAmount",ylxFundBalanceDTO.getTotalBuyAmount(),"fundShare",ylxFundBalanceDTO.getFundShare(),"version",ylxFundBalanceDTO.getVersion()));
    		}
    	}
    	YLXTransactionHistoryDTO ylxTransactionHistoryDTO = new YLXTransactionHistoryDTO();
        ylxTransactionHistoryDTO.setSerialNo(ylxBuyRequestDetailDTO.getId());
    	ylxTransactionHistoryDTO.setUserId(Long.valueOf(ylxBuyRequestDetailDTO.getBankAccount()));
    	ylxTransactionHistoryDTO.setProductCode(ylxBuyRequestDetailDTO.getProductCode());
    	ylxTransactionHistoryDTO.setRemark("认购交易交易记录");
    	ylxTransactionHistoryDTO.setTransactionType(YLXTransactionHistoryType.BUY.name());
    	ylxTransactionHistoryDTO.setTransactionAmount(ylxBuyRequestDetailDTO.getAmount());
        ylxTransactionHistoryDTO.setFundShare(BigDecimal.ZERO);
    	ylxTransactionHistoryDTO.setCreatedAt(new Date());
    	ylxTransactionHistoryDTO.setUpdatedAt(new Date());
    	ylxTransactionHistoryDTO.setCreatedBy("SYS");
    	ylxTransactionHistoryDTO.setUpdatedBy("SYS");
    	ylxTransactionHistoryDTO.setVersion(0L);
    	ylxTransactionHistoryRepository.insertYlxOpTransactionHistory(ylxTransactionHistoryDTO);
    }

}
