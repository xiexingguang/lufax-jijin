package com.lufax.jijin.ylx.job;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.lufax.jijin.scheduler.basejob.BaseBatchJob;

import com.lufax.jijin.scheduler.basejob.BaseJob;
import com.lufax.jijin.trade.service.YlxFundRedeemService;
import com.lufax.jijin.ylx.confirm.service.RedeemConfirmHandler;
import com.lufax.jijin.ylx.confirm.service.YLXRedeemConfirmService;
import com.lufax.jijin.ylx.dto.YLXAccountTransferRecordDTO;
import com.lufax.jijin.ylx.dto.YLXFundBalanceDTO;
import com.lufax.jijin.ylx.dto.YLXSellConfirmDetailDTO;
import com.lufax.jijin.ylx.dto.YLXSellRequestDetailDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lufax.jijin.account.service.AccountForMoneyService;
import com.lufax.jijin.base.constant.SLPAccountTransferRecordStatus;
import com.lufax.jijin.base.dto.TransferRefundResponse;
import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.ylx.dto.repository.YLXAccountTransferRecordRepository;
import com.lufax.jijin.ylx.dto.repository.YLXFundBalanceRepository;
import com.lufax.jijin.ylx.dto.repository.YLXSellConfirmDetailRepository;
import com.lufax.jijin.ylx.dto.repository.YLXSellRequestDetailRepository;

@Component
public class YLXFundRechargeStep_4 extends BaseJob<YLXAccountTransferRecordDTO,Long> {
	
	@Autowired
    private YLXAccountTransferRecordRepository accTransferDao;
	@Autowired
	private AccountForMoneyService accountService;
	@Autowired
	private YLXSellRequestDetailRepository ylxSellRequestDetailRepository;
	@Autowired
    private YlxFundRedeemService ylxFundRedeemService;
	@Autowired
    private YLXFundBalanceRepository ylxFundBalanceRepository;
	@Autowired
    private YLXSellConfirmDetailRepository sellConfirmDetailDAO;
	@Autowired
	private YLXRedeemConfirmService smsService ; 
	
    protected int getBatchAmount(){return 500;}

    @Override
    protected List<YLXAccountTransferRecordDTO> fetchList(int batchAmount) {
    	return accTransferDao.findYLXAccountTransferRecordsByStatus(batchAmount, SLPAccountTransferRecordStatus.NEW);
    }

    @Override
    protected void processList(List<YLXAccountTransferRecordDTO> list) {
        for (YLXAccountTransferRecordDTO dto : list) {
            try {
                int num = accTransferDao.updateYLXAccountTransferRecordStatusById(dto.getId(), SLPAccountTransferRecordStatus.ONGOING, dto.getVersion());
                if (num > 0) {
                	
                	YLXSellRequestDetailDTO sell = ylxSellRequestDetailRepository.getYLXSellRequestById(dto.getSellRequestId());
                    YLXSellConfirmDetailDTO ylxSellConfirmDTO = sellConfirmDetailDAO.getYlxSellConfirmsByInternalTrxId(Arrays.asList(sell.getId())).get(0);
                    YLXFundBalanceDTO ylxFundBalance = ylxFundBalanceRepository.findYlxFundBalanceByUserIdAndProductCode(MapUtils.buildKeyValueMap("userId", sell.getBankAccount(), "productCode", sell.getProductCode())).get(0);
                    
                    TransferRefundResponse response = accountService.refundForInsurance(dto);
                    if (response.isSuccess()) {
                        ylxFundRedeemService.rechargeUpdateUser(dto, sell, ylxFundBalance,ylxSellConfirmDTO);
                        try{
                        	smsService.sendSuccessMessage(sell,ylxSellConfirmDTO);//sell成功 发送短信
                        }catch(Exception e){
                        	Logger.error(this, String.format("sendSuccessMessage failed,faSlpAccountTransferRecordDTO e:%s",e));
                        }
                    } else {
                        Logger.info(this, String.format("account transfer fail,recordId:%s, sellRequestId:%s", dto.getRecordId(), dto.getSellRequestId()));
                        accTransferDao.updateYLXAccountTransferRecordStatusById(dto.getId(), SLPAccountTransferRecordStatus.FAIL);
                    }
                }
            } catch (Exception e) {
                Logger.error(this, String.format("buyRequestDataPrepare failed,faSlpAccountTransferRecordDTO id:%s", dto.getId()), e);
                accTransferDao.updateYLXAccountTransferRecordStatusById(dto.getId(), SLPAccountTransferRecordStatus.FAIL);
            }
        }
    }
}
