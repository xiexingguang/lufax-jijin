package com.lufax.jijin.fundation.repository;

import org.springframework.stereotype.Repository;

import com.lufax.jijin.base.repository.BaseRepository;
import com.lufax.jijin.fundation.dto.JijinRedeemThresholdHisDTO;

@Repository
public class JijinRedeemThresholdHisRepository extends BaseRepository<JijinRedeemThresholdHisDTO> {

	@Override
	protected String nameSpace() {
		return "BusJijinRedeemThresholdHis";
	}

	public JijinRedeemThresholdHisDTO insertJijinRedeemThresholdHis(JijinRedeemThresholdHisDTO dto){
		return this.insert("insertJijinRedeemThresholdHis", dto);
	}
}
