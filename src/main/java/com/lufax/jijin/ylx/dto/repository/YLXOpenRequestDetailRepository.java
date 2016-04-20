package com.lufax.jijin.ylx.dto.repository;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.lufax.jijin.base.repository.BusdataBaseRepository;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.ylx.dto.YLXOpenRequestDetailDTO;

@Repository
public class YLXOpenRequestDetailRepository extends BusdataBaseRepository<YLXOpenRequestDetailDTO>{

	@Override
	protected String nameSpace() {
		
		return "YLX_OPEN_REQUEST_DETAIL";
	}
	
    @Transactional
    public void batchInsert(List<YLXOpenRequestDetailDTO> YLXOpenRequestDTOs) {
        batchInsert("insert", YLXOpenRequestDTOs);
    }
	
	public Long getMaxInternalTrxIdsBybatchId(long batchId) {
		Object result = queryObject("getMaxInternalTrxIdsBybatchId",batchId);
		if(null!=result)
			return (Long)result;
		else
			return null;
	}
	
	// count for batch file pagination
	public Long countOpenRequestBybatchId(long batchId) {
		return (Long)queryObject("countOpenRequestBybatchId",batchId);
	}
	
	
	/**
	 * select batch size from request table
	 */
	public List<YLXOpenRequestDetailDTO> getYLXOpenRequestDTOsByBatchId(long batchId, long start,long end) {
		return queryList("getYLXOpenRequestDTOsByBatchId",
				MapUtils.buildKeyValueMap("batchId", batchId,
                        "start", start,
                        "end", end));
	}
	
	/**
	 * select unfilled open requests
	 */
	public List<YLXOpenRequestDetailDTO> getUnfilledOpenRequests(long batchId, long size) {
		return queryList("getUnfilledOpenRequests",
				MapUtils.buildKeyValueMap("batchId",batchId, 
				"size",size));
	}
	/**
	 * batch update for account filling
	 */
	
    public void batchUpdateOpenRequest(List<YLXOpenRequestDetailDTO> YLXOpenRequestDetailDTOs) {
        batchUpdate("updateYLXOpenRequest", YLXOpenRequestDetailDTOs);
    }

    public int updateBatchId(long fromBatchId,long toBatchId, Date targetDate){
        return update("updateBatchId", MapUtils.buildKeyValueMap("fromBatchId",fromBatchId,"toBatchId",toBatchId,"targetDate",targetDate));
    }
    
    /**
	 * select opening open requests
	 */
	public List<YLXOpenRequestDetailDTO> getOpeningOpenRequests(long batchId,String status, long size) {
		return queryList("getOpeningOpenRequests",
				MapUtils.buildKeyValueMap("batchId",batchId,"status", status,
				"size",size));
	}

}
