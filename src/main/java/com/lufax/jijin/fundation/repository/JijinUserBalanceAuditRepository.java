/**
 * 
 */
package com.lufax.jijin.fundation.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.lufax.jijin.base.repository.BaseRepository;
import com.lufax.jijin.fundation.dto.JijinUserBalanceAuditDTO.Status;
import com.lufax.jijin.fundation.dto.JijinPAFBuyAuditDTO;
import com.lufax.jijin.fundation.dto.JijinUserBalanceAuditDTO;
import com.lufax.mq.client.util.MapUtils;


@Repository
public class JijinUserBalanceAuditRepository extends BaseRepository<JijinUserBalanceAuditDTO>{
	
	
	public JijinUserBalanceAuditDTO insertBusJijinUserBalanceAudit(JijinUserBalanceAuditDTO jijinUserBalanceAuditDTO) {
        return super.insert("insertBusJijinUserBalanceAudit", jijinUserBalanceAuditDTO);
    }
	
	public JijinUserBalanceAuditDTO findBusJijinUserBalanceAudit(Map condition) {
        return super.query("findBusJijinUserBalanceAudit", condition);
    }

	public List<JijinUserBalanceAuditDTO> findBusJijinUserBalanceAuditList(Map condition) {
        return super.queryList("findBusJijinUserBalanceAudit", condition);
    }

    public int updateBusJijinUserBalanceAudit(JijinUserBalanceAuditDTO dto) {
    	if(null==dto.getVersion()){//历史数据无version
    		return super.update("updateBusJijinUserBalanceAuditWithoutVersionLock",dto);
    	}
        return super.update("updateBusJijinUserBalanceAudit", dto);
    }
    
    public List<JijinUserBalanceAuditDTO> getUnDispatchedAuditsByType(String type,Status status, int batchAmount){
    
         return super.queryList("getUnDispatchedAuditsByType", MapUtils.buildKeyValueMap("status",status.name(),"type",type,"maxNum", batchAmount));
    }
    
    public int batchInsertBusJijinUserBalanceAudit(List<JijinUserBalanceAuditDTO> dtos) {
        return super.batchInsert("insertBusJijinUserBalanceAudit", dtos);
    }
    
    public BigDecimal sumAmountByCondition(Map condition) {
		return (BigDecimal)queryObject("findSumBusJijinUserBalanceAudit",condition);
	}
    
	@Override
	protected String nameSpace() {
		return "BusJijinUserBalanceAudit";
	}
	

}
