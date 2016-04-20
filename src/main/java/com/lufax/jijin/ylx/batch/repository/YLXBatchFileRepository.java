package com.lufax.jijin.ylx.batch.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.lufax.jijin.base.repository.BusdataBaseRepository;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.ylx.batch.dto.YLXBatchFileDTO;

@Repository
public class YLXBatchFileRepository extends BusdataBaseRepository<YLXBatchFileDTO> {

	@Override
	protected String nameSpace() {
		
		return "YLX_BATCH_FILE";
	}

	
//	public YLXBatchFileDTO insertYLXBatchDTO(YLXBatchFileDTO YLXBatchFileDTO) {
//		return insert("insert", YLXBatchFileDTO);
//	}

	
    @Transactional
    public void batchInsert(List<YLXBatchFileDTO> YLXBatchFileDTOs) {
        batchInsert("insert", YLXBatchFileDTOs);
    }
    
	
	public List<YLXBatchFileDTO> getYLXBatchFileDTOsByBatchId(long id) {
		return queryList("getYLXBatchFileDTOsByBatchId",id);
	}
	
	public List<YLXBatchFileDTO> getYLXBatchFileDTOsByBatchIdAndStatus(long batchId,String status) {
		return queryList("getYLXBatchFileDTOsByBatchIdAndStatus",MapUtils.buildKeyValueMap("id",batchId,"status",status));
	}
	
	public YLXBatchFileDTO getYLXBatchFileDTOsById(long id) {
		return query("getYLXBatchFileDTOsById",id);
	}

    /*public List<YLXBatchFileDTO> getYLXBatchFileDTOsByBatchIdAndTradeType(long batchId,YLXTradeDetailType tradeType) {
        return queryList("getYLXBatchFileDTOsByBatchId",MapUtils.buildKeyValueMap("batchId",batchId,"tradeType",tradeType.name()));
    }*/


	public void updateYLXBatchFileDTOSuccessById(Long id, String status,BigDecimal amount, Date trxDate,long total) {
		update("updateYLXBatchFileSuccessDTOById", 
					MapUtils.buildKeyValueMap("id",	id, 
							"status", status,
							"total",total,
							"amount",amount,
							"trxDate",trxDate));
	}
	
	public void updateYLXBatchFileDTOStatusById(Long id, String status,int retryTimes,String returnCode,String fileId) {
		update("updateYLXBatchFileDTOStatusById", 
					MapUtils.buildKeyValueMap("id",	id,
							"returnCode",returnCode,
							"status", status, 
							"retryTimes",retryTimes,
							"fileId",fileId));
	}

	public void updateByIdSelective(YLXBatchFileDTO batchFile) {
		update("updateByIdSelective", batchFile);
	}
	

}
