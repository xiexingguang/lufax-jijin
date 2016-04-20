/**
 *
 */
package com.lufax.jijin.fundation.repository;

import com.lufax.jijin.base.repository.BaseRepository;
import com.lufax.jijin.fundation.dto.JijinPAFBuyAuditDTO;
import com.lufax.jijin.fundation.dto.PafBuyAuditCountDTO;
import com.lufax.mq.client.util.MapUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Repository
public class JijinPAFBuyAuditRepository extends BaseRepository<JijinPAFBuyAuditDTO> {


    public JijinPAFBuyAuditDTO insertBusJijinPAFBuyAudit(JijinPAFBuyAuditDTO jijinPAFBuyAuditDTO) {
        return super.insert("insertBusJijinPAFBuyAudit", jijinPAFBuyAuditDTO);
    }

    public List<JijinPAFBuyAuditDTO> findBusJijinPAFBuyAuditList(Map condition) {
        return super.queryList("findBusJijinPAFBuyAudit", condition);
    }

    // 有乐观锁
    public int updateBusJijinPAFBuyAuditDTO(JijinPAFBuyAuditDTO dto) {
        return update("updateBusJijinPAFBuyAuditDTO", dto);
    }

    public int batchInsertBusJijinPAFBuyAudit(List<JijinPAFBuyAuditDTO> dtos) {
        return super.batchInsert("insertBusJijinPAFBuyAudit", dtos);
    }

    public List<JijinPAFBuyAuditDTO> getUnDispatchRecords(int batchAmount) {
        return super.queryList("getUnDispatchRecords", MapUtils.buildKeyValueMap("status", "NEW", "maxNum", batchAmount));
    }


    public JijinPAFBuyAuditDTO getMaxRecordByFileId(Long fileId) {
        return super.query("getMaxRecordByFileId", MapUtils.buildKeyValueMap("fileId", fileId));
    }

    public List<String> getFundSeqIdsByFileId(Long fileId) {
        return super.queryListObject("getFundSeqIdsByFileId", fileId);
    }

    public PafBuyAuditCountDTO countByFileIdAndStatus(Long fileId, String status) {
        return (PafBuyAuditCountDTO) super.queryObject("countByFileIdAndStatus", MapUtils.buildKeyValueMap("fileId", fileId, "status", status));
    }


    @Override
    protected String nameSpace() {
        return "BusJijinPAFBuyAudit";
    }


}
