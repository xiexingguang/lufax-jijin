package com.lufax.jijin.ylx.dto.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.lufax.jijin.base.repository.BusdataBaseRepository;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.product.constant.ProductCategory;
import com.lufax.jijin.ylx.dto.YLXSellRequestDetailDTO;
import com.lufax.jijin.ylx.enums.YLXTradeDetailType;

@Repository
public class YLXSellRequestDetailRepository extends BusdataBaseRepository<YLXSellRequestDetailDTO>{

	@Override
	protected String nameSpace() {
		
		return "YLX_SELL_REQUEST_DETAIL";
	}
	
    @Transactional
    public void batchInsert(List<YLXSellRequestDetailDTO> YLXSellRequestDTOs) {
        batchInsert("insert", YLXSellRequestDTOs);
    }

	public YLXSellRequestDetailDTO getYLXSellRequestById(long id) {
		return (YLXSellRequestDetailDTO)queryObject("getYLXSellRequestById",MapUtils.buildKeyValueMap("id",	id));
	}
	
	// count for batch file pagination
	public Long countSellRequestBybatchId(long batchId) {
		return (Long)queryObject("countSellRequestBybatchId",batchId);
	}
	
	
	//sum request amount by rownum
	public BigDecimal sumAmountByBatchIdAndRownum(long batchId,long start, long end) {
		return (BigDecimal)queryObject("sumAmountByBatchIdAndRownum",
				MapUtils.buildKeyValueMap("batchId",	batchId, 
						"start", start, 
						"end",end));
	}
	
	/**
	 * select batch size from request table
	 */
	public List<YLXSellRequestDetailDTO> getYLXSellRequestDTOsByBatchId(long batchId, long start,long end) {
		return queryList("getYLXSellRequestDTOsByBatchId",
				MapUtils.buildKeyValueMap("batchId",batchId, 
				"start", start, 
				"end",end));
	}
	
	/**
	 * select batch size from request table
	 */
	public List<YLXSellRequestDetailDTO> getYLXSellRequestDTOsByBatchId(long batchId) {
		return getYLXSellRequestDTOsByBatchId(batchId, 0 , Long.MAX_VALUE);
	}
	
	/**
	 * select unfilled sell requests
	 */
	public List<YLXSellRequestDetailDTO> getUnfilledSellRequests(long batchId, long size) {
		return queryList("getUnfilledSellRequests",
				MapUtils.buildKeyValueMap("batchId",batchId, 
				"size",size));
	}


    public List<YLXSellRequestDetailDTO> getYLXSellRequestDTOsByBatchIdandStatus(long batchId,long rowNum, String status) {
        return queryList("getYLXSellRequestDTOsByBatchIdandStatus",  MapUtils.buildKeyValueMap("batchId",batchId, "rowNum",rowNum,"status",status));
    }
    

    public List<YLXSellRequestDetailDTO> getYLXSellRequestDTOsByRecordIdandStatus(String recordId,long rowNum, String status) {
        return queryList("getYLXSellRequestDTOsByRecordIdandStatus", MapUtils.buildKeyValueMap("recordId",recordId, "rowNum",rowNum,"status",status));
    }
    
    public void updateSellRequestStatusById(long id,String status , BigDecimal commissionFee, BigDecimal confirmUnitPrice, BigDecimal amount, BigDecimal confirmFundShare) {
        update("updateSellRequestStatusById", MapUtils.buildKeyValueMap("id", id, "status", status,"commissionFee",commissionFee,"confirmUnitPrice",confirmUnitPrice,"amount",amount,"confirmFundShare",confirmFundShare));
    }
    
    public void updateSellRequestJustStatusById(long id,String status) {
        update("updateSellRequestJustStatusById", MapUtils.buildKeyValueMap("id", id, "status", status));
    }

    @Transactional
    public void batchUpdateSellRequest(List<YLXSellRequestDetailDTO> YLXSellRequestDetailDTOs) {
        batchUpdate("updateYLXSellRequest", YLXSellRequestDetailDTOs);
    }

    public int updateBatchId(long fromBatchId,long toBatchId, Date targetDate,ProductCategory productCategory,YLXTradeDetailType sellType ){
        return update("updateBatchId", MapUtils.buildKeyValueMap("fromBatchId",fromBatchId,"toBatchId",toBatchId,"targetDate",targetDate,"productCategory",productCategory.getCode(),"sellType",sellType.name()));
    }

    public void updateSellRequestToNewRecordId(String recordId, String newRecordId) {
        update("updateSellRequestToNewRecordId", MapUtils.buildKeyValueMap("recordId", recordId, "newRecordId", newRecordId));
    }
}
