package com.lufax.jijin.ylx.dto.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.OptimisticLockException;

import org.springframework.stereotype.Repository;

import com.lufax.jijin.base.repository.BusdataBaseRepository;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.ylx.dto.YLXFundBalanceDTO;


@Repository
public class YLXFundBalanceRepository extends BusdataBaseRepository<YLXFundBalanceDTO>{
    @Override
    protected String nameSpace() {
        return "YlxFundBalance";
    }
    
    public Object insertYlxFundBalance(YLXFundBalanceDTO ylxFundBalanceDTO) {
        return super.insert("insertYlxFundBalance", ylxFundBalanceDTO);
    }

    public List<YLXFundBalanceDTO> findYlxFundBalanceByUserIdAndProductCode(Map condition) {
        return super.queryList("findYlxFundBalanceByUserIdAndProductCode", condition);
    }

    public int updateYlxFundBalance(Map condition) {
        int cnt = super.update("updateYlxFundBalance", condition);
        if(cnt<=0){
        	throw new OptimisticLockException();
        }
        return cnt;
    }
    
    public Object updateYlxFundBalanceUnitPrice(String productCode, BigDecimal unitPrice ,Date profitDate) {
        return super.update("updateYlxFundBalanceUnitPrice", MapUtils.buildKeyValueMap("unitPrice",unitPrice,"productCode",productCode,"profitDate",profitDate));
    }

	public List<YLXFundBalanceDTO> getByThirdAccounts(List<String> thirdAccounts) {
		return super.queryList("getByThirdAccounts", MapUtils.buildKeyValueMap("accounts",thirdAccounts));
	}
}
