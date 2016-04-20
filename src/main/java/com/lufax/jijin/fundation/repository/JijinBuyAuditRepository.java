/**
 * 
 */
package com.lufax.jijin.fundation.repository;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.lufax.jijin.base.repository.BaseRepository;
import com.lufax.jijin.fundation.dto.JijinBuyAuditDTO;
import com.lufax.jijin.fundation.dto.JijinPAFBuyAuditDTO;
import com.lufax.mq.client.util.MapUtils;


@Repository
public class JijinBuyAuditRepository extends BaseRepository<JijinBuyAuditDTO>{
	
	
	public JijinBuyAuditDTO insertBusJijinBuyAudit(JijinBuyAuditDTO jijinPAFBuyAuditDTO) {
        return super.insert("insertBusJijinBuyAudit", jijinPAFBuyAuditDTO);
    }
	
	public JijinBuyAuditDTO findBusJijinBuyAudit(Map condition) {
        return super.query("findBusJijinBuyAudit", condition);
    }

	public List<JijinBuyAuditDTO> findBusJijinBuyAuditList(Map condition) {
        return super.queryList("findBusJijinBuyAudit", condition);
    }

    public int updateBusJijinBuyAudit(Map condition) {
        return super.update("updateBusJijinBuyAudit", condition);
    }
    
    public int batchInsertBusJijinBuyAudit(List<JijinBuyAuditDTO> dtos) {
        return super.batchInsert("insertBusJijinBuyAudit", dtos);
    }
    

	@Override
	protected String nameSpace() {
		return "BusJijinBuyAudit";
	}
	

}
