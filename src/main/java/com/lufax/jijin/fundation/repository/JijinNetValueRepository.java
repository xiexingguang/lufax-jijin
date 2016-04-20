/**
 * 
 */
package com.lufax.jijin.fundation.repository;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.lufax.jijin.base.repository.BaseRepository;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.daixiao.gson.JijinYieldRateGson;
import com.lufax.jijin.fundation.dto.JijinNetValueDTO;
import com.lufax.jijin.fundation.schedular.SyncFileRepository;

/**
 * @author chenguang E-mail:chenguang451@pingan.com.cn
 * @version create time:May 6, 2015 10:19:04 AM
 * 
 */
@Repository
public class JijinNetValueRepository  extends BaseRepository<JijinNetValueDTO>
  implements SyncFileRepository{

	@Override
	protected String nameSpace() {
		return "BusJijinNetValue";
	}
	
	public JijinNetValueDTO insertJijinNetValue(JijinNetValueDTO dto) {
        return super.insert("insertBusJijinNetValue", dto);
    }
	
	public int batchInsertJijinNetValues(List<JijinNetValueDTO> dtos){
		return super.batchInsert("insertBusJijinNetValue", dtos);
	}
	
	public JijinNetValueDTO findBusJijinNetValue(Map condition) {
        return super.query("findBusJijinNetValue", condition);
    }

    public List<JijinNetValueDTO> findBusJijinNetValues(Map condition) {
        return super.queryList("findBusJijinNetValue", condition);
    }

	public JijinNetValueDTO findLatestBusJijinNetValueByFundCode(String fundCode) {
        return super.query("findLatestBusJijinNetValueByFundCode", MapUtils.buildKeyValueMap("fundCode",fundCode));
    }

	@Override
	public int batchInsertDTOs(List dtos) {
		return super.batchInsert("insertBusJijinNetValue", dtos);
	}
	
    public List<JijinNetValueDTO> findBusJijinNetValuesByFundAndDate(String fundCode, List<String> dates) {
        return super.queryList("findBusJijinNetValueByFundCodeAndDate", MapUtils.buildKeyValueMap("fundCode",fundCode, "dates", dates));
    }
    
    public List<JijinNetValueDTO> findBusJijinNetValuesByFundAndDate(String fundCode, String startDate, String endDate) {
        return super.queryList("findBusJijinNetValueByFundCodeAndBetweenDate", 
        		MapUtils.buildKeyValueMap("fundCode",fundCode,
        				"startDate", startDate,
        				"endDate", endDate));
    }
    
    public List<JijinNetValueDTO> getUnDispachedNetValue(int limit){
    	return super.queryList("getUnDispachedNetValue", limit);
    }
    
    public int updateJijinNetValueStatus(Long id,String status){
    	return super.update("updateJijinNetValueStatus", MapUtils.buildKeyValueMap("id",id,"status",status));
    }
    
    public List<JijinYieldRateGson> getJijinYieldRateByPageFromNetValueTable(String fundCode,int start,int end){
    	return super.queryListObject("getJijinYieldRateByPageFromNetValueTable",MapUtils.buildKeyValueMap("fundCode",fundCode,"start",start,"end",end));
    }
}
