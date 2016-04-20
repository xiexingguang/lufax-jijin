/**
 * 
 */
package com.lufax.jijin.fundation.repository;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.lufax.jijin.base.repository.BaseRepository;
import com.lufax.jijin.fundation.dto.JijinRedeemAuditDTO;


@Repository
public class JijinRedeemAuditRepository extends BaseRepository<JijinRedeemAuditDTO>{
	
	
	public JijinRedeemAuditDTO insertBusJijinRedeemAudit(JijinRedeemAuditDTO jijinRedeemAuditDTO) {
        return super.insert("insertBusJijinRedeemAudit", jijinRedeemAuditDTO);
    }
	
	public JijinRedeemAuditDTO findBusJijinRedeemAudit(Map condition) {
        return super.query("findBusJijinRedeemAudit", condition);
    }

	public List<JijinRedeemAuditDTO> findBusJijinRedeemAuditList(Map condition) {
        return super.queryList("findBusJijinRedeemAudit", condition);
    }

    public int updateBusJijinRedeemAudit(Map condition) {
        return super.update("updateBusJijinRedeemAudit", condition);
    }
    
    public int batchInsertBusJijinRedeemAudit(List<JijinRedeemAuditDTO> dtos) {
        return super.batchInsert("insertBusJijinRedeemAudit", dtos);
    }
    

	@Override
	protected String nameSpace() {
		return "BusJijinRedeemAudit";
	}
	

}
