package com.lufax.jijin.ylx.batch.repository;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.lufax.jijin.base.repository.BusdataBaseRepository;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.ylx.batch.domain.YLXBatchStatus2;
import com.lufax.jijin.ylx.batch.domain.YLXBatchType;
import com.lufax.jijin.ylx.batch.dto.BatchRunStatus;
import com.lufax.jijin.ylx.batch.dto.YLXBatchDTO;

@Repository
public class YLXBatchRepository extends BusdataBaseRepository<YLXBatchDTO> {

	@Override
	protected String nameSpace() {

		return "YLX_BATCH";
	}

	@Transactional
	public void batchInsert(List<YLXBatchDTO> YLXBatchDTOs) {
		batchInsert("insertYLXBatchDTO", YLXBatchDTOs);
	}

	public YLXBatchDTO insertYLXBatchDTO(YLXBatchDTO YLXBatchDTO) {
		return insert("insertYLXBatchDTO", YLXBatchDTO);
	}

	public List<YLXBatchDTO> getYLXBatchDTOByStatusAndType(String status, String type) {
		return queryList("getYLXBatchDTOByStatusAndType", MapUtils.buildKeyValueMap("status", status, "type", type));
	}

    public List<YLXBatchDTO> getYLXBatchDTOByTypeAndStatusAndRunStatus(String status, String type,BatchRunStatus runStatus){
        return queryList("getYLXBatchDTOByTypeAndStatusAndRunStatus", MapUtils.buildKeyValueMap("status", status,
                "type",type,"runStatus",runStatus.name()));
    }

	public void updateYLXBatchDTOSuccessById(Long id, String status, String subStep) {
		update("updateYLXBatchDTOSuccessById", MapUtils.buildKeyValueMap("batchId", id, "status", status, "subStep",
				subStep, "runStatus", BatchRunStatus.IDLE));
	}

    public void updateBatchStatusById(Long id, String status) {
        update("updateBatchStatusById", MapUtils.buildKeyValueMap("batchId", id, "status", status));
    }

	public void updateYLXBatchDTOFailById(Long id, String status, String errMsg, String failToken, int retryTimes) {
		update("updateYLXBatchDTOFailById", MapUtils.buildKeyValueMap("id", id, "status", status, "errMsg", errMsg,
				"failToken", failToken, "retryTimes", retryTimes, "runStatus", BatchRunStatus.IDLE));

	}

    public void updateYLXBatchRetryTimes(Long id, String errMsg, String failToken) {
        update("updateYLXBatchRetryTimes", MapUtils.buildKeyValueMap("id", id, "errMsg", errMsg,
                "failToken", failToken, "runStatus", BatchRunStatus.IDLE));

    }

	public List<YLXBatchDTO> getYLXBatchDTOByTriggerDateAndTypeAndStatus(Date triggerDate, YLXBatchType batchType, List<YLXBatchStatus2> statusList) {
		return queryList("getYLXBatchDTOByTriggerDateAndType", MapUtils.buildKeyValueMap("triggerDate", triggerDate, 
				 "type", batchType, "statusList", statusList));
	}

    public List<YLXBatchDTO> getYLXBatchDTOByTriggerDateAndTypeAndRunStatus(Date triggerDate, YLXBatchType batchType, List<YLXBatchStatus2> statusList,BatchRunStatus runStatus) {
        return queryList("getYLXBatchDTOByTriggerDateAndTypeAndRunStatus", MapUtils.buildKeyValueMap("triggerDate", triggerDate,
                "type", batchType, "statusList", statusList,"runStatus",runStatus));
    }

	public List<YLXBatchDTO> queryYlxBatchListByCutOffId(Long cutoffId, List<YLXBatchStatus2> statusList) {
		return queryList("getYLXBatchDTOByCutoffId",
				MapUtils.buildKeyValueMap("cutoffId", cutoffId, "statusList", statusList));
	}

	public void updateBatchRunStatusById(Long batchId, BatchRunStatus batchRunStatus) {
		update("updateBatchRunStatusById", MapUtils.buildKeyValueMap("batchId", batchId, "runStatus", batchRunStatus));
	}
	
	public Integer updateBatchRunStatusByIds(List<Long> batchIds, BatchRunStatus batchRunStatus) {
		return update("updateBatchRunStatusByIds", MapUtils.buildKeyValueMap("batchIds", batchIds, "runStatus", batchRunStatus));
	}
	
	public YLXBatchDTO getYlxBatchDTOById(long batchId) {
		return query("getById", MapUtils.buildKeyValueMap("batchId", batchId));
	}
	
	public YLXBatchDTO getYLXBatchDTOByTypeAndTargetDate(String type, Date targetDate){
		return query("getYLXBatchDTOByTypeAndTargetDate", MapUtils.buildKeyValueMap("targetDate", targetDate,
				"type",type));
	}

    public YLXBatchDTO getYLXBatchDTOByTypeAndStatus(String type,YLXBatchStatus2 status){
        return query("getYLXBatchDTOByTypeAndStatus", MapUtils.buildKeyValueMap("status", status.name(),
                "type",type));
    }




}
