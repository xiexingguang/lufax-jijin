package com.lufax.jijin.fundation.dto.builder;

import java.math.BigDecimal;
import java.util.Date;

import com.lufax.jijin.fundation.constant.BalanceHistoryBizType;
import com.lufax.jijin.fundation.dto.JijinUserBalanceDTO;
import com.lufax.jijin.fundation.dto.JijinUserBalanceHistoryDTO;

/**
 * build history dto
 * @author xuneng
 *
 */
public class JijinUserBalenceHistoryDTOBuilder {
	
    /**
     * 申购下单成功，在徒资金增加
     * 申购确认成功，可用份额增加，在徒资金减少
     * 
     * 赎回下单成功，可用份额减少，冻结份额增加
     * 赎回确认失败，可用份额增加，冻结份额减少
     * 
     * 份额分红
     * 
     * @param jijinUserBalanceDTO
     * @param bizDate
     * @return
     */
    public static JijinUserBalanceHistoryDTO buildUserBalanceHistoryDTO(JijinUserBalanceDTO jijinUserBalanceDTO,String bizDate, BigDecimal increase,BigDecimal decrease,String remark,BalanceHistoryBizType hisType,Long tradeRecordId){
    	JijinUserBalanceHistoryDTO jijinUserBalanceHistoryDTO = new JijinUserBalanceHistoryDTO();

    	jijinUserBalanceHistoryDTO.setBalance(jijinUserBalanceDTO.getBalance());
    	jijinUserBalanceHistoryDTO.setFrozenAmount(jijinUserBalanceDTO.getFrozenAmount());
    	jijinUserBalanceHistoryDTO.setShareBalance(jijinUserBalanceDTO.getShareBalance());
    	jijinUserBalanceHistoryDTO.setFrozenShare(jijinUserBalanceDTO.getFrozenShare());
    	jijinUserBalanceHistoryDTO.setFundCode(jijinUserBalanceDTO.getFundCode());
    	jijinUserBalanceHistoryDTO.setBizDate(bizDate);
    	jijinUserBalanceHistoryDTO.setDecrease(decrease);
    	jijinUserBalanceHistoryDTO.setIncrease(increase);
    	jijinUserBalanceHistoryDTO.setDividendStatus(jijinUserBalanceDTO.getDividendStatus());
    	jijinUserBalanceHistoryDTO.setDividendType(jijinUserBalanceDTO.getDividendType());
    	jijinUserBalanceHistoryDTO.setRemark(remark);
    	jijinUserBalanceHistoryDTO.setUserId(jijinUserBalanceDTO.getUserId());
    	jijinUserBalanceHistoryDTO.setApplyingAmount(jijinUserBalanceDTO.getApplyingAmount());
    	jijinUserBalanceHistoryDTO.setBizType(hisType.getCode());
    	jijinUserBalanceHistoryDTO.setTradeRecordId(tradeRecordId);
    	jijinUserBalanceHistoryDTO.setTrxTime(new Date());
    	return jijinUserBalanceHistoryDTO;
    }

}
