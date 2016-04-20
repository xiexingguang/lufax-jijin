package com.lufax.jijin.daixiao.repository;

import com.lufax.jijin.base.repository.BaseRepository;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.daixiao.dto.JijinExManagerDTO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 基金经理
 *
 * @author chenqunhui
 */
@Repository
public class JijinExManagerRepository extends BaseRepository<JijinExManagerDTO> {

    @Override
    protected String nameSpace() {
        return "BusJijinExManager";
    }

    public JijinExManagerDTO insertJijinExManager(JijinExManagerDTO jijinExManagerDTO) {
        return super.insert("insertJijinExManager", jijinExManagerDTO);
    }

    public int updateJijinExManager(Map condition) {
        return super.update("updateJijinExManager", condition);
    }

    public Long getJijinExManagerBatchIdByFundCode(String fundCode) {
        return (Long) super.queryObject("getJijinExManagerBatchIdByFundCode", fundCode);
    }

    public void batchInsertDTOs(List dtos) {
        super.batchInsert("insertJijinExManager", dtos);
    }

    public List<JijinExManagerDTO> getJijinExManagersByStatus(String status, int maxNum) {
        return super.queryList("getJijinExManagersByStatus", MapUtils.buildKeyValueMap("status", status, "maxNum", maxNum));
    }

    public List<JijinExManagerDTO> getJijinExManagersByBatchIdAndFundCode(Long batchId, String fundCode) {
        return super.queryList("getJijinExManagersByBatchIdAndFundCode", MapUtils.buildKeyValueMap("batchId", batchId, "fundCode", fundCode));
    }

    public JijinExManagerDTO getJijinExManagerById(Long id) {
        return (JijinExManagerDTO)super.queryObject("getJijinExManagerById", id);
    }
}
