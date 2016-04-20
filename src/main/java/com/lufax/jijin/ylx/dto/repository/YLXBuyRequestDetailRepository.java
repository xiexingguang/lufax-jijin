package com.lufax.jijin.ylx.dto.repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.lufax.jijin.base.repository.BusdataBaseRepository;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.product.constant.ProductCategory;
import com.lufax.jijin.ylx.dto.YLXBuyRequestDetailDTO;
import com.lufax.jijin.ylx.enums.YLXTradeDetailType;

@Repository
public class YLXBuyRequestDetailRepository extends BusdataBaseRepository<YLXBuyRequestDetailDTO> {

	@Override
	protected String nameSpace() {
		return "YLX_BUY_REQUEST_DETAIL";
	}
	
    @Transactional
    public void batchInsert(List<YLXBuyRequestDetailDTO> YLXBuyRequestDTOs) {
        batchInsert("insert", YLXBuyRequestDTOs);
    }
	
	public Long getMaxInternalTrxIdsBybatchId(long batchId) {
		return (Long)queryObject("getMaxInternalTrxIdsBybatchId",batchId);
	}

    public List<YLXBuyRequestDetailDTO> getByBatchId(long batchId) {
        return queryList("getByBatchId",batchId);
    }
	
	// count for batch file pagination
	public Long countBuyRequestBybatchId(long batchId) {
		return (Long)queryObject("countBuyRequestBybatchId",batchId);
	}


	//sum request amount by rownum
	public BigDecimal sumAmountByBatchIdAndRownum(long batchId,long start, long end) {
		return (BigDecimal)queryObject("sumAmountByBatchIdAndRownum",
				MapUtils.buildKeyValueMap("batchId", batchId,
                        "start", start,
                        "end", end));
	}

	/**
	 * select batch size from request table
	 */
	public List<YLXBuyRequestDetailDTO> getYLXBuyRequestDTOsByBatchId(long batchId, long start,long end) {
		return queryList("getYLXBuyRequestDTOsByBatchId",
				MapUtils.buildKeyValueMap("batchId",	batchId, 
				"start", start, 
				"end",end));
	}

	/**
	 * select batch size from request table
	 */
	public List<YLXBuyRequestDetailDTO> getYLXBuyRequestDTOsByBatchIdAndPK(long batchId, long rowNum,long maxSerialNum) {
		return queryList("getYLXBuyRequestDTOsByBatchIdAndPK",
				MapUtils.buildKeyValueMap("batchId",	batchId, 
				"rowNum", rowNum, 
				"maxSerialNum",maxSerialNum));
	}

    public YLXBuyRequestDetailDTO getYLXBuyRequestDetailDTOByTrxId(long trxId){
        return query("getYLXBuyRequestDetailDTOByTrxId", MapUtils.buildKeyValueMap("trxId", trxId));
    }

    public List<YLXBuyRequestDetailDTO> getYLXBuyRequestDTOsByTargetDateAndProductCategory(long batchId,Date targetDate,ProductCategory productCategory,long start,long end){
        return queryList("getYLXBuyRequestDTOsByTargetDateAndProductCategory",MapUtils.buildKeyValueMap("batchId",batchId,"targetDate",targetDate,"productCategory",productCategory.getCode(),"start",start,"end",end));
    }

    public List<YLXBuyRequestDetailDTO> getProductCodeByBatchId(long batchId){
        return queryList("getProductCodeByBatchId", batchId);
    }

    public int updateBatchId(long fromBatchId,long toBatchId, Date targetDate,ProductCategory productCategory,YLXTradeDetailType buyType ){
        return update("updateBatchId", MapUtils.buildKeyValueMap("fromBatchId",fromBatchId,"toBatchId",toBatchId,"targetDate",targetDate,"productCategory",productCategory.getCode(),"buyType",buyType.name()));
    }
    
    public List<YLXBuyRequestDetailDTO> getYLXBuyRequestDTOsByBatchIdandStatus(long batchId,long rowNum, String status){
        return queryList("getYLXBuyRequestDTOsByBatchIdandStatus", MapUtils.buildKeyValueMap("batchId", batchId, "rowNum", rowNum, "status", status));

    }
    
    public List<YLXBuyRequestDetailDTO> getYLXBuyRequestDTOsByStatus(long productId,int rowNum, String status){
        return queryList("getYLXBuyRequestDTOsByStatus",MapUtils.buildKeyValueMap("productId",productId, "rowNum",rowNum,"status",status));
    }
    
    public List<YLXBuyRequestDetailDTO> getYLXBuyRequestDTOsByStatusAndRowNum(long productId, int start, int end, String status){
        return queryList("getYLXBuyRequestDTOsByBatchIdandStatusAndRowNum",MapUtils.buildKeyValueMap("productId",productId,"start",start,"end",end,"status",status));
    }

    public void batchUpdate(List<YLXBuyRequestDetailDTO> taskData){
    	List<Map> lm = new ArrayList<Map>();
    	for(YLXBuyRequestDetailDTO d : taskData){
    		lm.add(MapUtils.buildKeyValueMap("id", d.getId(), "status", d.getStatus(),"confirmDate",new Date()));
    	}
    	
    	super.batchUpdate("updateBuyRequestStatusById", lm);
    }




}
