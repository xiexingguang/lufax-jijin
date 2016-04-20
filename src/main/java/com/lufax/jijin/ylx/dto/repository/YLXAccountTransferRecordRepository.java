package com.lufax.jijin.ylx.dto.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lufax.jijin.base.constant.SLPAccountTransferRecordStatus;
import com.lufax.jijin.base.repository.BusdataBaseRepository;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.ylx.dto.YLXAccountTransferRecordDTO;

@Repository
public class YLXAccountTransferRecordRepository extends BusdataBaseRepository<YLXAccountTransferRecordDTO> {

	@Override
	protected String nameSpace() {
		return "YLX_ACCOUNT_TRANSFER_RECORD";
	}

	public List<YLXAccountTransferRecordDTO> findYLXAccountTransferRecordsByStatus(long batchNumber, SLPAccountTransferRecordStatus status) {
		return queryList("findYLXAccountTransferRecordsByStatus", MapUtils.buildKeyValueMap("status", status.getCode(), "batchNumber",batchNumber));
	}
	
	public Integer updateYLXAccountTransferRecordStatusById(long id, SLPAccountTransferRecordStatus status, Long version){
		return update("updateYLXAccountTransferRecord", MapUtils.buildKeyValueMap("status", status.getCode(), "id", id, "version", version));
	}
	
	public Integer updateYLXAccountTransferRecordStatusById(long id, SLPAccountTransferRecordStatus status){
		return update("updateYLXAccountTransferRecordWithoutVersion", MapUtils.buildKeyValueMap("status", status.getCode(), "id", id));
	}
	
	public Integer updateYLXAccountTransferRecordStatusById(long id, SLPAccountTransferRecordStatus status, Long version, String remark){
		return update("updateYLXAccountTransferRecord", MapUtils.buildKeyValueMap("status", status.getCode(), "id", id, "version", version, "remark", remark));
	}
}
