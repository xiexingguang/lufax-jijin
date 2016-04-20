package com.lufax.jijin.daixiao.repository;

import com.lufax.jijin.base.repository.BaseRepository;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.daixiao.dto.JijinExScopeDTO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 基金规模
 *
 * @author
 */
@Repository
public class JijinExScopeRepository extends BaseRepository<JijinExScopeDTO> {

    @Override
    protected String nameSpace() {
        return "BusJijinExScope";
    }

    public JijinExScopeDTO insertJijinExScope(JijinExScopeDTO jijinExScopeDTO) {
        return super.insert("insertJijinExScope", jijinExScopeDTO);
    }

    public int updateJijinExScope(Map condition) {
        return super.update("updateJijinExScope", condition);
    }

    /**
     * 按日期倒排的规模信息
     *
     * @param fundCode
     * @return
     */
    public List<JijinExScopeDTO> getJijinExScopeByFundCode(String fundCode, Integer isValid) {
        return super.queryList("getJijinExScopeByFundCode", MapUtils.buildKeyValueMap("fundCode", fundCode, "isValid", isValid));
    }

    public JijinExScopeDTO getLastestJijinExScopeByFundCode(String fundCode, Integer isValid) {
        List<JijinExScopeDTO> list = super.queryList("getLastestJijinExScopeByFundCode", MapUtils.buildKeyValueMap("fundCode", fundCode, "isValid", isValid));
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return list.get(0);
    }

    public void batchInsertDTOs(List dtos) {
        super.batchInsert("insertJijinExScope", dtos);
    }

    public List<JijinExScopeDTO> getJijinExScopeByStatusAndMaxNum(String status, int maxNum) {
        return super.queryList("getJijinExScopeByStatusAndMaxNum", MapUtils.buildKeyValueMap("status", status, "maxNum", maxNum));
    }

    public JijinExScopeDTO getJijinExScopeById(Long id) {
        return (JijinExScopeDTO) super.queryObject("getJijinExScopeById", id);
    }

    public Integer countNumberOfAfterReportDate(String fundCode, String reportDate) {
        return (Integer) super.queryObject("countNumberOfAfterReportDate", MapUtils.buildKeyValueMap("fundCode", fundCode, "reportDate", reportDate));
    }

    public int updateSameFundCodeReportDateRecordToNotVaild(Long id, String fundCode, String reportDate) {
        return super.update("updateSameFundCodeReportDateRecordToNotVaild", MapUtils.buildKeyValueMap("id", id, "fundCode", fundCode, "reportDate", reportDate));
    }
}
