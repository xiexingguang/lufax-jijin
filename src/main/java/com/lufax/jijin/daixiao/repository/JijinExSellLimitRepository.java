package com.lufax.jijin.daixiao.repository;

import com.lufax.jijin.base.repository.BaseRepository;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.daixiao.dto.JijinExSellLimitDTO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class JijinExSellLimitRepository extends BaseRepository<JijinExSellLimitDTO> {

    @Override
    protected String nameSpace() {
        return "BusJijinExSellLimit";
    }

    public JijinExSellLimitDTO insertJijinExSellLimit(JijinExSellLimitDTO JijinExSellLimitDTO) {
        return super.insert("insertJijinExSellLimit", JijinExSellLimitDTO);
    }

    public void batchInsertJijinExSellLimit(List<JijinExSellLimitDTO> list) {
        batchInsert("insertJijinExSellLimit", list);
    }

    public int updateJijinExSellLimit(Map condition) {
        return super.update("updateJijinExSellLimit", condition);
    }

    public List<JijinExSellLimitDTO> getJijinExSellLimit(Map condition) {
        return super.queryList("getJijinExSellLimit", condition);
    }

    public JijinExSellLimitDTO getJijinExSellLimitById(Long id) {
        List<JijinExSellLimitDTO> jijinExSellLimitDTOList = super.queryList("getJijinExSellLimit", MapUtils.buildKeyValueMap("id", id));
        if (jijinExSellLimitDTOList != null && jijinExSellLimitDTOList.size() > 0) {
            return jijinExSellLimitDTOList.get(0);
        } else {
            return null;
        }
    }

    public List<JijinExSellLimitDTO> getJijinExSellLimitByFundCodeAndBizCode(String fundCode, String bizCode) {
        return super.queryList("getJijinExSellLimitByFundCodeAndBizCode", MapUtils.buildKeyValueMap("fundCode", fundCode, "bizCode", bizCode));
    }


    public JijinExSellLimitDTO getLastestJijinExSellLimitByFundCodeAndBizCode(String fundCode, String bizCode) {
        List<JijinExSellLimitDTO> jijinExSellLimitDTOs = super.queryList("getLastestJijinExSellLimitByFundCodeAndBizCode", MapUtils.buildKeyValueMap("fundCode", fundCode, "bizCode", bizCode));
        if(CollectionUtils.isEmpty(jijinExSellLimitDTOs)){
            return null;
        }
        return jijinExSellLimitDTOs.get(0);
    }

    public List<JijinExSellLimitDTO> getUnDispachedJijinExSellLimit(int limit) {
        return super.queryList("getJijinExSellLimit", MapUtils.buildKeyValueMap("limit", limit, "status", "NEW"));
    }

    /**
     * 获取某基金赎回限制信息的最新batch_id
     *
     * @param fundCode
     * @return
     */
    public Long getLatestBatchIdByFundAndBizCode(String fundCode, String bizCode) {
        return (Long) super.queryObject("getLatestBatchIdByFundAndBizCode", MapUtils.buildKeyValueMap("fundCode", fundCode, "bizCode", bizCode));
    }
}
