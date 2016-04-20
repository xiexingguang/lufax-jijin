package com.lufax.jijin.fundation.repository;

import org.springframework.stereotype.Repository;

import com.lufax.jijin.base.repository.BaseRepository;
import com.lufax.jijin.fundation.dto.JijinRedeemThresholdDTO;
import com.lufax.mq.client.util.MapUtils;

@Repository
public class JijinRedeemThresholdRepository extends BaseRepository<JijinRedeemThresholdDTO> {

	@Override
	protected String nameSpace() {
		return "BusJijinRedeemThreshold";
	}

	public JijinRedeemThresholdDTO getRedeemThresholdByFundCode(String fundCode){
		return (JijinRedeemThresholdDTO)this.queryObject("getRedeemThresholdByFundCode", fundCode);
	}
	/**
	 * 更新赎回状态
	 * @param fundCode
	 * @param status
	 */
	public int updateJijinRedeemStatusByFundCode(String fundCode,String currentStatus){
		return this.update("updateJijinRedeemStatusByFundCode", MapUtils.buildKeyValueMap("fundCode",fundCode,"currentStatus",currentStatus));
	}
	
	public int updateDahuaAccountStatusByFundCode(String fundCode,String accountStatus){
		return this.update("updateDahuaAccountStatusByFundCode", MapUtils.buildKeyValueMap("fundCode",fundCode,"accountStatus",accountStatus));
	}
}
