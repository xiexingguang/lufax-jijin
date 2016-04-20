package com.lufax.jijin.ylx.dto.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lufax.jijin.base.repository.BusdataBaseRepository;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.ylx.dto.BuyConfirmDetailDTO;
@Repository
public class BuyConfirmDetailRepository extends BusdataBaseRepository<BuyConfirmDetailDTO> {

	@Override
	protected String nameSpace() {
		return "YLX_BUY_CONFIRM_DETAIL";
	}

	public void batchInsert(List<BuyConfirmDetailDTO> taskData) {
		batchInsert("insert", taskData);
	}
	
	public List<BuyConfirmDetailDTO> getYlxBuyConfirmsByInternalTrxId(List<Long> ids){
		return super.queryList("getYlxBuyConfirmsByInternalTrxId", MapUtils.buildKeyValueMap("ids", ids));
	}

}
