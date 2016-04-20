/**
 * 
 */
package com.lufax.jijin.fundation.repository;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;
import com.lufax.jijin.base.repository.BaseRepository;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.fundation.dto.JijinThirdPaySyncDTO;


@Repository
public class JijinThirdPaySyncRepository extends BaseRepository<JijinThirdPaySyncDTO>{
	
	
    public Object insertBusJijinThirdPaySync(JijinThirdPaySyncDTO busJijinPafSyncDTO) {
        return super.insert("insertBusJijinThirdPaySync", busJijinPafSyncDTO);
    }

    public List<JijinThirdPaySyncDTO> findBusJijinThirdPaySync(Map condition) {
        return super.queryList("findBusJijinThirdPaySync", condition);
    }

    public int updateBusJijinThirdPaySync(Map condition) {
        return super.update("updateBusJijinThirdPaySync", condition);
    }
    
    public int batchInsertBusJijinThirdPaySync(List<JijinThirdPaySyncDTO> dtos) {
        return super.batchInsert("insertBusJijinThirdPaySync", dtos);
    }
    
    public List<JijinThirdPaySyncDTO> getUnDispatchRecords(JijinThirdPaySyncDTO.Status status,
    		String channel, String type, int batchAmount) {
        return super.queryList("getUnDispatchRecords", MapUtils.buildKeyValueMap("status", status,"channel",channel,"type",type,"maxNum", batchAmount));
    }
    
    public int updateBusJijinThirdPaySyncStatus(long id, String status){
    	if(id!=0){
    		return super.update("updateBusJijinThirdPaySync", MapUtils.buildKeyValueMap("id", id, "status", status));
    	}else{
    		return -1;
    	}
    	
    }

	@Override
	protected String nameSpace() {
		return "BusJijinThirdPaySync";
	}
	
	public int updateBusJijinThirdPaySyncWithMemo(long id, String status, String memo) {
        return super.update("updateBusJijinThirdPaySyncWithMemo", MapUtils.buildKeyValueMap("id", id, "status", status, "memo", memo));
    }

}
