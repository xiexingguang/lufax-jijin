package com.lufax.jijin.trade.service;

import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.ylx.dto.YLXAccountTransferRecordDTO;
import com.lufax.jijin.ylx.dto.YLXFundBalanceDTO;
import com.lufax.jijin.ylx.dto.YLXSellRequestDetailDTO;
import com.lufax.jijin.ylx.dto.repository.YLXAccountTransferRecordRepository;
import com.lufax.jijin.ylx.dto.repository.YLXFundBalanceRepository;
import com.lufax.jijin.ylx.dto.repository.YLXSellRequestDetailRepository;
import com.lufax.jijin.ylx.fund.dto.InsuranceFundRecordDTO;
import com.lufax.jijin.ylx.fund.dto.InsuranceFundRecordHisDTO;
import com.lufax.jijin.ylx.fund.repository.InsuranceFundRecordHisRepository;
import com.lufax.jijin.ylx.fund.repository.InsuranceFundRecordRepository;
import com.lufax.jijin.ylx.util.InsuranceFundRecordStatus;
import com.lufax.jijin.base.utils.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class YlxFundRedeemTransactionalService {
	@Autowired
    private YLXFundBalanceRepository ylxFundBalanceRepository;
	@Autowired
	private YLXSellRequestDetailRepository ylxSellRequestDetailRepository;
	@Autowired
	private InsuranceFundRecordRepository insuranceFundRecordRepository;
    @Autowired
    private InsuranceFundRecordHisRepository insuranceFundRecordHisRepository;
    @Autowired
    private YLXAccountTransferRecordRepository accTransferDao;
    
	@Transactional
    public void handleBatchFaSlpAccountTransferRecord(List<YLXAccountTransferRecordDTO> partList, List<Long> sellRequestIds) {
		accTransferDao.batchInsert("insertYLXAccountTransferRecord", partList);
		//ylxSellRequestDetailRepository.batchUpdate("updateSellRequestStatusToSuccessById", sellRequestIds);
	}
	@Transactional
    public void handleRedeem(YLXFundBalanceDTO ylxFundBalance, BigDecimal redeemAmount, YLXSellRequestDetailDTO insertDto){
    	int num = ylxFundBalanceRepository.updateYlxFundBalance(MapUtils.buildKeyValueMap("id",ylxFundBalance.getId(), "fundShare", ylxFundBalance.getFundShare().subtract(redeemAmount), "frozenFundShare", ylxFundBalance.getFrozenFundShare().add(redeemAmount), "version", ylxFundBalance.getVersion()));
    	if(num > 0)
    		ylxSellRequestDetailRepository.insert("insert", insertDto);
    }
	@Transactional
    public void insertInsuranceFundData(List<InsuranceFundRecordDTO> fundList, List<List<Long>> selllists){
		for(int index = 0;index < fundList.size(); index ++ ){
			InsuranceFundRecordDTO fundRecord = fundList.get(index);
			List<Long> ylxSellRequestIds = selllists.get(index);
			
			insuranceFundRecordRepository.insertInsuranceFundRecord(fundRecord);
			insuranceFundRecordHisRepository.insertFaInsuranceFundRecordHis(new InsuranceFundRecordHisDTO(fundRecord, InsuranceFundRecordStatus.RECHARGE_NEW.getCode()));
			List<Map<String,Object>> param = new ArrayList<Map<String,Object>>();
			for(Long requestId : ylxSellRequestIds){
				Map<String,Object> m = new HashMap<String,Object>(3);
				m.put("recordId", fundRecord.getRecordId());
				m.put("id", requestId);
				param.add(m);
				
			}			
			ylxSellRequestDetailRepository.batchUpdate("updateRecordIdById", param);
		}
	}

    @Transactional
    public void insertInsuranceFundData(List<YlxFundRedeemService.AmountWithSellRequestHolder> list) {
        for (int index = 0; index < list.size(); index++) {
            InsuranceFundRecordDTO fundRecord = list.get(index).getFundRecord();
            List<Long> ylxSellRequestIds = list.get(index).getRequestIds();
            if (null != insuranceFundRecordRepository.findByTypeFundDateProductId(fundRecord.getType(), fundRecord.getFundDate(), fundRecord.getProductId())) {
                Logger.info(this, String.format("fund record has already exists , type:[%s],fundDate:[%s],productId[%s]:", fundRecord.getType(), fundRecord.getFundDate(), fundRecord.getProductId()));
            } else {
                insuranceFundRecordRepository.insertInsuranceFundRecord(fundRecord);
                insuranceFundRecordHisRepository.insertFaInsuranceFundRecordHis(new InsuranceFundRecordHisDTO(fundRecord, InsuranceFundRecordStatus.RECHARGE_NEW.getCode()));
                List<Map<String, Object>> param = new ArrayList<Map<String, Object>>();
                for (Long requestId : ylxSellRequestIds) {
                    Map<String, Object> m = new HashMap<String, Object>(3);
                    m.put("recordId", fundRecord.getRecordId());
                    m.put("id", requestId);
                    param.add(m);

                }
                ylxSellRequestDetailRepository.batchUpdate("updateRecordIdById", param);
            }
        }
    }
}
