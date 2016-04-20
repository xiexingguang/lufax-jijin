package com.lufax.jijin.daixiao.repository;

import com.lufax.jijin.base.repository.BaseRepository;
import com.lufax.jijin.base.utils.EmptyChecker;
import com.lufax.jijin.daixiao.dto.JijinExMfPerformDTO;
import com.lufax.mq.client.util.MapUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 净值增长率
 *
 * @author chenqunhui
 */
@Repository
public class JijinExMfPerformRepository extends BaseRepository<JijinExMfPerformDTO> {

    @Override
    protected String nameSpace() {
        return "BusJijinExMfPerform";
    }

    public JijinExMfPerformDTO insertJijinExMfPerfom(JijinExMfPerformDTO dto) {
        return super.insert("insertJijinExMfPerform", dto);
    }


    public void batchInsertJijinExMfPerform(List<JijinExMfPerformDTO> list) {
        batchInsert("insertJijinExMfPerform", list);
    }

    public List<JijinExMfPerformDTO> getJijinExMfPerformDTOByFundCodeAndPerformanceDay(String fundCode, String performanceDay) {
        return super.queryList("getJijinExMfPerformDTOByFundCodeAndPerformanceDay", MapUtils.buildKeyValueMap("fundCode", fundCode, "performanceDay", performanceDay));
    }

    public List<JijinExMfPerformDTO> findJijinExMfPerformByFundCodeAndDate(String fundCode, String performanceDay) {
        return super.queryListObject("findJijinExMfPerformByFundCodeAndDate", MapUtils.buildKeyValueMap("fundCode", fundCode, "performanceDay", performanceDay));
    }

    public JijinExMfPerformDTO findLastestJijinExMfPerformByFundCodeAndDate(String fundCode, String performanceDay) {
        List<JijinExMfPerformDTO> jijinExMfPerformDTOs = super.queryListObject("findLastestJijinExMfPerformByFundCodeAndDate", MapUtils.buildKeyValueMap("fundCode", fundCode, "performanceDay", performanceDay));
        if(CollectionUtils.isEmpty(jijinExMfPerformDTOs)){
           return null;
        }
        return jijinExMfPerformDTOs.get(0);
    }

    /**
     * 获取最新净值增长率(不管状态字段)
     *
     * @param fundCode
     * @return
     */
    public JijinExMfPerformDTO getLastJijinExMfPerformDTOByFundCode(String fundCode) {
        List<JijinExMfPerformDTO> list = super.queryList("getLastJijinExMfPerformDTOByFundCode", fundCode);
        if (!EmptyChecker.isEmpty(list)) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public int updateJijinExMfPerformStatus(Long id, String status, Integer isValid, String errMsg) {
        return super.update("updateJijinExMfPerformStatus", MapUtils.buildKeyValueMap("id", id, "status", status, "isValid", isValid, "errMsg", errMsg));
    }

    public List<JijinExMfPerformDTO> getUnDispatchedJijinExMfPerform(int limit) {
        return super.queryList("getUnDispatchedJijinExMfPerform", limit);
    }

    /**
     * 查询该fundCode下指定日期之后的数据，如果有数据，本条记录将不会推送到product
     *
     * @param fundCode
     * @param date
     * @return
     */
    public Integer countNumberOfAfterDate(String fundCode, String date) {
        return (Integer) super.queryObject("countNumberOfAfterDate", MapUtils.buildKeyValueMap("fundCode", fundCode, "date", date));
    }

    /**
     * 将相同日期、fundCode的有效数据更新为无效
     *
     * @param id
     * @param fundCode
     * @param date
     * @return
     */
    public int updateSameFundCodeDateRecordToNotVaild(Long id, String fundCode, String date) {
        return super.update("updateSameFundCodeDateRecordToNotVaild", MapUtils.buildKeyValueMap("id", id, "fundCode", fundCode, "date", date));
    }
}


