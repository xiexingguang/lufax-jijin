package com.lufax.jijin.ylx.dto.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lufax.jijin.base.repository.BusdataBaseRepository;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.ylx.dto.YLXSellConfirmDetailDTO;

@Repository
public class YLXSellConfirmDetailRepository extends BusdataBaseRepository<YLXSellConfirmDetailDTO> {

	@Override
	protected String nameSpace() {
		return "YLX_SELL_CONFIRM_DETAIL";
	}

	public void batchinsert(List<YLXSellConfirmDetailDTO> sellConfirmDetails) {
		super.batchInsert("insert", sellConfirmDetails);
	}



    public List<YLXSellConfirmDetailDTO> getYlxSellConfirmsByInternalTrxId(List<Long> ids){
        return super.queryList("getYlxSellConfirmsByInternalTrxId", MapUtils.buildKeyValueMap("ids", ids));
    }




}
