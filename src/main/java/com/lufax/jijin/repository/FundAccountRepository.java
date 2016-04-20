package com.lufax.jijin.repository;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.lufax.jijin.base.repository.BusdataBaseRepository;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.dto.FundAccountDTO;

@Repository
public class FundAccountRepository extends BusdataBaseRepository<FundAccountDTO>{

    protected String nameSpace(){
        return "FundAccount";
    }

    public Object insertBusFundAccount(FundAccountDTO busFundAccountDTO) {
        return super.insert("insertBusFundAccount", busFundAccountDTO);
    }

    public List<FundAccountDTO> findBusFundAccount(Map condition) {
        return super.queryList("findBusFundAccount", condition);
    }

    public int updateBusFundAccount(Map condition) {
        return super.update("updateBusFundAccount", condition);
    }

	
	public List<FundAccountDTO> getOpenUserDTOsByIds(List<Long> ids){
		return queryList("getOpenUserDTOsByIds", MapUtils.buildKeyValueMap("ids", ids));
		
	}
	
	public List<FundAccountDTO> getOpenUserIdByIds(List<Long> ids){
		return queryList("getOpenUserIdByIds", MapUtils.buildKeyValueMap("ids", ids));
		
	}

	public void batchInsert(List accountList) {
		batchInsert("insertBusFundAccount", accountList);
	}

    public int batchUpdateFundAccount(List accountList) {
        return  batchUpdate("updateFundAccount", accountList);
    }
    
    public int updateFundAccount(FundAccountDTO dto) {
    	return update("updateFundAccount", dto);
    }


    public int updateFrozenAmount(Map condition) {
        return update("updateFrozenAmount", condition);
    }


    public FundAccountDTO findBusFundAccountByUserId(Long userId) {
        return super.query("findBusFundAccountByUserId", userId);
    }
}
