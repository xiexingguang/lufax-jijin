/**
 * 
 */
package com.lufax.jijin.fundation.repository;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.lufax.jijin.base.repository.BaseRepository;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.fundation.dto.JijinDividendDTO;
import com.lufax.jijin.fundation.schedular.SyncFileRepository;

/**
 * @author chenguang E-mail:chenguang451@pingan.com.cn
 * @version create time:May 6, 2015 9:30:24 AM
 * 
 */
@Repository
public class JijinDividendRepository extends BaseRepository<JijinDividendDTO>
    implements SyncFileRepository{

	@Override
	protected String nameSpace() {
		return "BusJijinDividend";
	}

	public JijinDividendDTO insertJijinDividend(JijinDividendDTO dto) {
        return super.insert("insertBusJijinDividend", dto);
    }
	
	public JijinDividendDTO findJijinDividend(Map condition) {
        return super.query("findBusJijinDividend", condition);
    }

    public List<JijinDividendDTO> findJijinDividends(Map condition) {
        return super.queryList("findBusJijinDividend", condition);
    }
    
    public JijinDividendDTO findJijinDividendByAppno(String appNo, String instId){
    	return super.query("findBusJijinDividend", MapUtils.buildKeyValueMap("appSheetNo", appNo, "instId", instId));
    }
    
    public List<JijinDividendDTO> findBatchNewDividendsByType(String type, int limit){
    	return super.queryList("findBatchBusJijinDividend", MapUtils.buildKeyValueMap("dividendType", type,
    			"status", JijinDividendDTO.Status.NEW, 
    			"maxNum", limit));
    }

    public int updateJijinDividend(Map condition) {
        return super.update("updateBusJijinDividend", condition);
    }
    
    public int updateJijindividendStatus(long id, String status){
    	return super.update("updateBusJijinDividend", MapUtils.buildKeyValueMap("id", id, "status", status));
    }

	@Override
	public int batchInsertDTOs(List dtos) {
		return super.batchInsert("insertBusJijinDividend", dtos);
	}
	
}
