package com.lufax.jijin.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.ylx.dto.YlxFundInvestorProfitDTO;
import com.lufax.jijin.ylx.dto.YlxFundProdProfitDTO;

@Repository
public class YlxProfitDAO extends BusdataBaseDAO {

	@Override
	protected String nameSpace() {
		return "ylxProfit";
	}
	
	public List<YlxFundInvestorProfitDTO> selectYlxFundInvestorProfit(Map condition) {
        return super.queryForList("selectYlxFundInvestorProfit",condition);
    }
	public List<YlxFundProdProfitDTO> selectYlxFundProdProfit(String prodCode) {
        return super.queryForList("selectYlxFundProdProfit",MapUtils.buildKeyValueMap("productCode", prodCode));
    }
}
