package com.lufax.jijin.ylx.job;

import com.lufax.jijin.base.service.TradeDayService;
import com.lufax.jijin.base.utils.DateUtils;
import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.scheduler.basejob.BaseJob;
import com.lufax.jijin.trade.service.YlxFundRedeemService;
import com.lufax.jijin.ylx.batch.domain.YLXBatchStatus2;
import com.lufax.jijin.ylx.batch.domain.YLXBatchType;
import com.lufax.jijin.ylx.batch.dto.BatchRunStatus;
import com.lufax.jijin.ylx.batch.dto.YLXBatchDTO;
import com.lufax.jijin.ylx.batch.repository.YLXBatchRepository;
import com.lufax.jijin.ylx.dto.repository.YLXSellRequestDetailRepository;
import com.lufax.jijin.ylx.enums.YLXSellRequestStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class YLXFundRechargeStep_1  extends BaseJob<YLXBatchDTO,Long> {
    @Autowired
    private YLXBatchRepository ylxBatchRepository;
    @Autowired
    private YLXSellRequestDetailRepository ylxSellRequestDetailRepository;
    @Autowired
    private YlxFundRedeemService redeemService;
    @Autowired
    private TradeDayService tradeDayService;

    @Override
    protected List<YLXBatchDTO> fetchList(int batchAmount) {
        return ylxBatchRepository.getYLXBatchDTOByTypeAndStatusAndRunStatus(YLXBatchStatus2.CONFIRMED.name(), YLXBatchType.YLX_SLP_REDEEM_REQUEST.name(),BatchRunStatus.IDLE);
    }

    @Override
    protected int getBatchAmount() {
        return 0;
    }

    @Override
    protected void processList(List<YLXBatchDTO> list) {
    	List<Long> batchIds = new ArrayList<Long>(list.size());
    	for(YLXBatchDTO batchDTO : list){
    		batchIds.add(batchDTO.getId());
    	}
    	
    	try{
    		ylxBatchRepository.updateBatchRunStatusByIds(batchIds, BatchRunStatus.ONGOING);
			for(YLXBatchDTO batchDTO : list){
                try {
                    //request 下下一个交易日作为触发条件
                    if(DateUtils.startOfDay(tradeDayService.getNextTradeDayAsDate(tradeDayService.getNextTradeDayAsDate(batchDTO.getTargetDate()))).before(new Date()))
                    {
                        redeemService.createFundRecord(ylxSellRequestDetailRepository.getYLXSellRequestDTOsByBatchIdandStatus(batchDTO.id(), Long.MAX_VALUE, YLXSellRequestStatus.CONFIRMED.getCode()), batchDTO);
                        ylxBatchRepository.updateBatchStatusById(batchDTO.id(), YLXBatchStatus2.CONFIRM_RECHARGE.name());
                    }
                }catch (Exception e){
                    Logger.error(this, String.format("buyRequestDataPrepare failed,batchId:%s", batchDTO.getId()),e);
                }
            }
    	}finally{
    		ylxBatchRepository.updateBatchRunStatusByIds(batchIds, BatchRunStatus.IDLE);
    	}
    }
}
