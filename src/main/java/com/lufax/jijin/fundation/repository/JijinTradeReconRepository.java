/**
 * 
 */
package com.lufax.jijin.fundation.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lufax.jijin.base.repository.BaseRepository;
import com.lufax.jijin.fundation.dto.JijinBuyAuditDTO;
import com.lufax.jijin.fundation.dto.JijinTradeReconDTO;


@Repository
public class JijinTradeReconRepository extends BaseRepository<JijinTradeReconDTO>{
	
	
	public JijinTradeReconDTO insertBusJijinBuyAudit(JijinTradeReconDTO jijinTradeReconDTO) {
        return super.insert("insertBusJijinTradeRecon", jijinTradeReconDTO);
    }

    
    public int batchInsertBusJijinBuyAudit(List<JijinTradeReconDTO> dtos) {
        return super.batchInsert("insertBusJijinTradeRecon", dtos);
    }
    

	@Override
	protected String nameSpace() {
		return "BusJijinTradeRecon";
	}
	

}
