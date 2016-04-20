package com.lufax.jijin.daixiao.repository;

import com.lufax.jijin.base.repository.BaseRepository;
import com.lufax.jijin.base.utils.EmptyChecker;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.daixiao.dto.JijinExNetValueDTO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 基金净值
 *
 * @author
 */
@Repository
public class JijinExNetValueRepository extends BaseRepository<JijinExNetValueDTO> {

    @Override
    protected String nameSpace() {
        return "BusJijinExNetValue";
    }

    public JijinExNetValueDTO insertJijinExNetValue(JijinExNetValueDTO jijinExNetValueDTO) {
        return super.insert("insertBusJijinExNetValue", jijinExNetValueDTO);
    }

    public int updateJijinExNetValue(Map condition) {
        return super.update("updateJijinExNetValue", condition);
    }

    public List<JijinExNetValueDTO> getJijinExNetValueByFundCodeAndEndDate(String fundCode, String endDate) {
        return super.queryList("getJijinExNetValueByFundCodeAndEndDate", MapUtils.buildKeyValueMap("fundCode", fundCode, "endDate", endDate));
    }

    /**
     * 查询最新净值
     *
     * @param fundCode
     * @return
     */
    public JijinExNetValueDTO getLastJijinExNetValueByFundCode(String fundCode) {
        List<JijinExNetValueDTO> list = super.queryList("getLastJijinExNetValueByFundCode", fundCode);
        if (!EmptyChecker.isEmpty(list)) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public List<JijinExNetValueDTO> getJijinExNetValuesByStatus(String status, int maxNum) {
        return super.queryList("getJijinExNetValuesByStatus", MapUtils.buildKeyValueMap("status", status, "maxNum", maxNum));
    }

    public void batchInsertDTOs(List dtos) {
        super.batchInsert("insertBusJijinExNetValue", dtos);
    }

    public JijinExNetValueDTO getJijinExNetValueById(Long id) {
        return (JijinExNetValueDTO) super.queryObject("getJijinExNetValueById", id);
    }

    public List<JijinExNetValueDTO> getJijinExNetValuesByFundCodeAndEndDateAndBatchId(String fundCode, String endDate, Long batchId) {
        return super.queryList("getJijinExNetValuesByFundCodeAndEndDateAndBatchId", MapUtils.buildKeyValueMap("fundCode", fundCode, "endDate", endDate, "batchId", batchId));
    }

    public Integer countNumberOfAfterEndDate(String fundCode, String endDate) {
        return (Integer) super.queryObject("countNumberOfAfterEndDate", MapUtils.buildKeyValueMap("fundCode", fundCode, "endDate", endDate));
    }

    public List<JijinExNetValueDTO> getNetValuesByFundCodeAndEndDate(String fundCode, String endDate) {
        return super.queryList("getNetValuesByFundCodeAndEndDate", MapUtils.buildKeyValueMap("fundCode", fundCode, "endDate", endDate));
    }

    public JijinExNetValueDTO getLastestNetValuesByFundCodeAndEndDate(String fundCode, String endDate) {
        List<JijinExNetValueDTO> list = super.queryList("getLastestNetValuesByFundCodeAndEndDate", MapUtils.buildKeyValueMap("fundCode", fundCode, "endDate", endDate));
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return list.get(0);
    }
}
