package com.lufax.jijin.daixiao.repository;

import com.lufax.jijin.base.repository.BaseRepository;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.daixiao.constant.RecordStatus;
import com.lufax.jijin.daixiao.dto.JijinExSellDayCountDTO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 赎回到账日期
 *
 * @author
 */
@Repository
public class JijinExSellDayCountRepository extends BaseRepository<JijinExSellDayCountDTO> {

    @Override
    protected String nameSpace() {
        return "BusJijinExSellDayCount";
    }

    public JijinExSellDayCountDTO insertJijinExSellDayCount(JijinExSellDayCountDTO JijinExSellDayCountDTO) {
        return super.insert("insertJijinExSellDayCount", JijinExSellDayCountDTO);
    }

    public void batchInsertJijinExSellDayCount(List<JijinExSellDayCountDTO> list) {
        batchInsert("insertJijinExSellDayCount", list);
    }

    public int updateJijinExSellDayCount(Map condition) {
        return super.update("updateJijinExSellDayCount", condition);
    }

    public List<JijinExSellDayCountDTO> getJijinExSellDayCount(Map condition) {
        return super.queryList("getJijinExSellDayCount", condition);
    }

    public JijinExSellDayCountDTO getJijinExSellDayCountById(Long id) {
        List<JijinExSellDayCountDTO> jijinExSellDayCountDTOs = super.queryList("getJijinExSellDayCount", MapUtils.buildKeyValueMap("id", id));
        if (jijinExSellDayCountDTOs != null && jijinExSellDayCountDTOs.size() > 0) {
            return jijinExSellDayCountDTOs.get(0);
        } else {
            return null;
        }
    }

    public List<JijinExSellDayCountDTO> getJijinExSellDayCountByFundCode(String fundCode) {
        return super.queryList("getJijinExSellDayCountByFundCode", fundCode);
    }

    public JijinExSellDayCountDTO getLastestJijinExSellDayCountByFundCode(String fundCode) {
        List<JijinExSellDayCountDTO> list = super.queryList("getLastestJijinExSellDayCountByFundCode", fundCode);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return list.get(0);
    }

    public List<JijinExSellDayCountDTO> getUnDispachedJijinExSellDayCount(int limit) {
        return super.queryList("getUnDispachedJijinExSellDayCount", MapUtils.buildKeyValueMap("limit", limit, "status", RecordStatus.NEW.toString()));
    }

    /**
     * 获取某基金赎回到帐日期信息的最新batch_id
     *
     * @param fundCode
     * @return
     */
    public Long getLatestBatchIdByFundCode(String fundCode) {
        return (Long) super.queryObject("getLatestBatchIdByFundCode", MapUtils.buildKeyValueMap("fundCode", fundCode));
    }
}

