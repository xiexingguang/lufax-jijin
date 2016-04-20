package com.lufax.jijin.ylx.confirm.service;

import com.google.gson.Gson;
import com.lufax.jersey.utils.Logger;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.service.MqService;
import com.lufax.jijin.ylx.batch.dto.YLXBatchFileDTO;
import com.lufax.jijin.ylx.batch.repository.YLXBatchFileRepository;
import com.lufax.jijin.ylx.dto.*;
import com.lufax.jijin.ylx.dto.repository.*;
import com.lufax.jijin.ylx.enums.YLXSellRequestStatus;
import com.lufax.jijin.ylx.enums.YLXTransactionHistoryType;
import com.lufax.jijin.ylx.remote.YLXSmsService;
import com.lufax.jijin.ylx.util.YlxConstants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class RedeemConfirmHandler {
	
	@Autowired
	private YLXBatchFileRepository ylxBatchFileRepository;
    @Autowired
    private YLXSellRequestDetailRepository ylxSellRequestDetailRepository;
    @Autowired
    private YLXFundBalanceRepository ylxFundBalanceRepository;
    @Autowired
    private YLXSellConfirmDetailRepository sellConfirmDetailDAO;
    @Autowired
    private MqService mqService;
    @Autowired
    private YLXTransactionHistoryRepository ylxTransactionHistoryRepository;
    @Autowired
    private YLXSmsService ylxSmsService;
	
    @Transactional
	public boolean handleRedeemConfirmData(YLXSellRequestDetailDTO ylxSellRequestDTO, Map<Long, YLXSellConfirmDetailDTO> redeemConfirmMap){
        if (redeemConfirmMap.containsKey(ylxSellRequestDTO.getId())) {
        	YLXSellConfirmDetailDTO ylxSellConfirmDTO = redeemConfirmMap.get(ylxSellRequestDTO.getId());
            if(ylxSellConfirmDTO.getResultCode().equals(YlxConstants.SUCCESS_RESULT_CODE)){
            	ylxSellRequestDTO.setStatus(YLXSellRequestStatus.CONFIRMED.name());
                ylxSellRequestDTO.setCommissionFee(ylxSellConfirmDTO.getCommissionFee());
                ylxSellRequestDTO.setPrincipal(ylxSellConfirmDTO.getAmount());
                ylxSellRequestDTO.setConfirmUnitPrice(ylxSellConfirmDTO.getConfirmUnitPrice());
                ylxSellRequestDTO.setMemo(String.format("redeem success [rtn code :%s],",ylxSellConfirmDTO.getResultCode()));
                ylxSellRequestDTO.setConfirmFundShare(ylxSellConfirmDTO.getConfirmFundShare());
                updateFundBalanceWhenSuccess(ylxSellRequestDTO, ylxSellConfirmDTO);
                ylxSellRequestDetailRepository.updateSellRequestStatusById(ylxSellRequestDTO.getId(), ylxSellRequestDTO.getStatus(),ylxSellRequestDTO.getCommissionFee(),ylxSellConfirmDTO.getConfirmUnitPrice(), ylxSellConfirmDTO.getAmount(),ylxSellConfirmDTO.getConfirmFundShare());
                return true;
            }else{
                ylxSellRequestDTO.setStatus(YLXSellRequestStatus.FAIL.name());
                ylxSellRequestDTO.setMemo(String.format("redeem fail [rtn code :%s],",ylxSellConfirmDTO.getResultCode()));
                updateFundBalanceWhenFail(ylxSellRequestDTO, ylxSellConfirmDTO);
                ylxSellRequestDetailRepository.updateSellRequestStatusById(ylxSellRequestDTO.getId(), ylxSellRequestDTO.getStatus(),BigDecimal.ZERO , BigDecimal.ONE,BigDecimal.ZERO,BigDecimal.ZERO);
                return false;
            }
        } else {
            Logger.warn(this, String.format("redeem confirm not found sellRequestId [%s]", ylxSellRequestDTO.getId()));
        	ylxSellRequestDTO.setStatus(YLXSellRequestStatus.UN_CONFIRM.name());
            updateFundBalanceWhenUnconfirmed(ylxSellRequestDTO);
            ylxSellRequestDetailRepository.updateSellRequestStatusById(ylxSellRequestDTO.getId(), ylxSellRequestDTO.getStatus(),BigDecimal.ZERO,BigDecimal.ONE,BigDecimal.ZERO,BigDecimal.ZERO);
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
    public void batchInsertRedeemConfirm(List<YLXSellConfirmDetailDTO> dtos, long lineNum, YLXBatchFileDTO batchFile) {
    	sellConfirmDetailDAO.batchinsert(dtos);
    	ylxBatchFileRepository.update("updateCurrentLineById",
				MapUtils.buildKeyValueMap("id", batchFile.getId(), "currentLine", lineNum));
    }
    
    /**
     * 1.同步此条ylxSellRequestDetailDTO 数据到bus_ylx_fund_balance
     * 2.Insert 数据到YLX_OP_TRANSACTION_HISTORY
     * @param ylxSellRequestDetailDTO
     */
    public void updateFundBalanceWhenSuccess(YLXSellRequestDetailDTO ylxSellRequestDetailDTO,YLXSellConfirmDetailDTO ylxSellConfirmDTO){
//        List<YLXFundBalanceDTO> ylxFundBalances = ylxFundBalanceRepository.findYlxFundBalanceByUserIdAndProductCode(MapUtils.buildKeyValueMap("userId", ylxSellRequestDetailDTO.getBankAccount(), "productCode", ylxSellRequestDetailDTO.getProductCode()));
//        for (YLXFundBalanceDTO ylxFundBalanceDTO : ylxFundBalances) {
//            ylxFundBalanceDTO.setFrozenFundShare(ylxFundBalanceDTO.getFrozenFundShare().subtract(ylxSellRequestDetailDTO.getFundShare()));
//            ylxFundBalanceDTO.setFundShare(ylxFundBalanceDTO.getFundShare().add(ylxSellRequestDetailDTO.getFundShare().subtract(ylxSellConfirmDTO.getConfirmFundShare())));
//            ylxFundBalanceRepository.updateYlxFundBalance(MapUtils.buildKeyValueMap(
//            		"id", ylxFundBalanceDTO.getId(), 
//            		"frozenFundShare", ylxFundBalanceDTO.getFrozenFundShare(), 
//            		"fundShare",ylxFundBalanceDTO.getFundShare(),
//            		"version", ylxFundBalanceDTO.getVersion()));
//        }
//        YLXTransactionHistoryDTO ylxTransactionHistoryDTO = new YLXTransactionHistoryDTO();
//        ylxTransactionHistoryDTO.setSerialNo(ylxSellRequestDetailDTO.getId());
//        ylxTransactionHistoryDTO.setUserId(Long.valueOf(ylxSellRequestDetailDTO.getBankAccount()));
//        ylxTransactionHistoryDTO.setProductCode(ylxSellRequestDetailDTO.getProductCode());
//        ylxTransactionHistoryDTO.setRemark("赎回成功");
//        ylxTransactionHistoryDTO.setTransactionType(YLXTransactionHistoryType.REDEEM.name());
//        ylxTransactionHistoryDTO.setFundShare(ylxSellConfirmDTO.getConfirmFundShare());
//        ylxTransactionHistoryDTO.setTransactionAmount(ylxSellConfirmDTO.getAmount());
//        ylxTransactionHistoryDTO.setCreatedAt(new Date());
//        ylxTransactionHistoryDTO.setUpdatedAt(new Date());
//        ylxTransactionHistoryDTO.setCreatedBy("SYS");
//        ylxTransactionHistoryDTO.setUpdatedBy("SYS");
//        ylxTransactionHistoryDTO.setVersion(0L);
//        ylxTransactionHistoryRepository.insertYlxOpTransactionHistory(ylxTransactionHistoryDTO);
    }

    private void updateFundBalanceWhenFail(YLXSellRequestDetailDTO ylxSellRequestDetailDTO,YLXSellConfirmDetailDTO ylxSellConfirmDTO){
        List<YLXFundBalanceDTO> ylxFundBalances = ylxFundBalanceRepository.findYlxFundBalanceByUserIdAndProductCode(MapUtils.buildKeyValueMap("userId", ylxSellRequestDetailDTO.getBankAccount(), "productCode", ylxSellRequestDetailDTO.getProductCode()));
        for (YLXFundBalanceDTO ylxFundBalanceDTO : ylxFundBalances) {
            ylxFundBalanceDTO.setFrozenFundShare(ylxFundBalanceDTO.getFrozenFundShare().subtract(ylxSellRequestDetailDTO.getFundShare()));
            ylxFundBalanceDTO.setFundShare(ylxFundBalanceDTO.getFundShare().add(ylxSellRequestDetailDTO.getFundShare()));
            ylxFundBalanceRepository.updateYlxFundBalance(MapUtils.buildKeyValueMap("fundShare",ylxFundBalanceDTO.getFundShare() ,"id", ylxFundBalanceDTO.getId(), "frozenFundShare", ylxFundBalanceDTO.getFrozenFundShare(), "version", ylxFundBalanceDTO.getVersion()));
        }
        YLXTransactionHistoryDTO ylxTransactionHistoryDTO = new YLXTransactionHistoryDTO();
        ylxTransactionHistoryDTO.setSerialNo(ylxSellRequestDetailDTO.getId());
        ylxTransactionHistoryDTO.setUserId(Long.valueOf(ylxSellRequestDetailDTO.getBankAccount()));
        ylxTransactionHistoryDTO.setProductCode(ylxSellRequestDetailDTO.getProductCode());
        ylxTransactionHistoryDTO.setRemark("赎回失败");
        ylxTransactionHistoryDTO.setTransactionType(YLXTransactionHistoryType.REDEEM.name());
        ylxTransactionHistoryDTO.setTransactionAmount(ylxSellConfirmDTO.getAmount());
        ylxTransactionHistoryDTO.setFundShare(ylxSellConfirmDTO.getConfirmFundShare());
        ylxTransactionHistoryDTO.setCreatedAt(new Date());
        ylxTransactionHistoryDTO.setUpdatedAt(new Date());
        ylxTransactionHistoryDTO.setCreatedBy("SYS");
        ylxTransactionHistoryDTO.setUpdatedBy("SYS");
        ylxTransactionHistoryDTO.setVersion(0L);
        ylxTransactionHistoryRepository.insertYlxOpTransactionHistory(ylxTransactionHistoryDTO);
    }


    private void updateFundBalanceWhenUnconfirmed(YLXSellRequestDetailDTO ylxSellRequestDetailDTO){
        List<YLXFundBalanceDTO> ylxFundBalances = ylxFundBalanceRepository.findYlxFundBalanceByUserIdAndProductCode(MapUtils.buildKeyValueMap("userId", ylxSellRequestDetailDTO.getBankAccount(), "productCode", ylxSellRequestDetailDTO.getProductCode()));
        for (YLXFundBalanceDTO ylxFundBalanceDTO : ylxFundBalances) {
            ylxFundBalanceDTO.setFrozenFundShare(ylxFundBalanceDTO.getFrozenFundShare().subtract(ylxSellRequestDetailDTO.getFundShare()));
            ylxFundBalanceRepository.updateYlxFundBalance(MapUtils.buildKeyValueMap("id", ylxFundBalanceDTO.getId(), "frozenFundShare", ylxFundBalanceDTO.getFrozenFundShare(), "version", ylxFundBalanceDTO.getVersion()));
        }
    }

}
