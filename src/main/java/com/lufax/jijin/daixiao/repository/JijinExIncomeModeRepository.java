package com.lufax.jijin.daixiao.repository;

import com.lufax.jijin.base.repository.BaseRepository;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.daixiao.dto.JijinExIncomeModeDTO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 默认分红方式
 * @author 
 *
 */
@Repository
public class JijinExIncomeModeRepository extends BaseRepository<JijinExIncomeModeDTO> {

    @Override
    protected String nameSpace() {
        return "BusJijinExIncomeMode";
    }

    public JijinExIncomeModeDTO insertJijinExIncomeMode(JijinExIncomeModeDTO JijinExIncomeModeDTO) {
        return super.insert("insertJijinExIncomeMode", JijinExIncomeModeDTO);
    }

    public void batchInsertJijinExIncomeMode(List<JijinExIncomeModeDTO> list){
        batchInsert("insertJijinExIncomeMode",list);
    }

    public int updateJijinExIncomeMode(Map condition) {
        return super.update("updateJijinExIncomeMode", condition);
    }

    public List<JijinExIncomeModeDTO> getJijinExIncomeMode(Map condition) {
        return super.queryList("getJijinExIncomeMode", condition);
    }

    public JijinExIncomeModeDTO getJijinExIncomeModeById(Long id) {
        List<JijinExIncomeModeDTO> jijinExIncomeModeDTOs = super.queryList("getJijinExIncomeMode", MapUtils.buildKeyValueMap("id", id));
        if (jijinExIncomeModeDTOs != null && jijinExIncomeModeDTOs.size() > 0) {
            return jijinExIncomeModeDTOs.get(0);
        } else {
            return null;
        }
    }

    public List<JijinExIncomeModeDTO> getJijinExIncomeModeByFundCode(String fundCode) {
        return super.queryList("getJijinExIncomeModeByFundCode", fundCode);
    }

    public JijinExIncomeModeDTO getLastestJijinExIncomeModeByFundCode(String fundCode) {
        List<JijinExIncomeModeDTO> list= super.queryList("getLastestJijinExIncomeModeByFundCode", fundCode);
        if(CollectionUtils.isEmpty(list)){
            return null;
        }
        return list.get(0);
    }

    public List<JijinExIncomeModeDTO> getUnDispachedJijinExIncomeMode(int limit) {
        return super.queryList("getJijinExIncomeMode", MapUtils.buildKeyValueMap("limit", limit, "status", "NEW"));
    }

    /**
     * 获取某基金分红方式信息的最新batch_id
     * @param fundCode
     * @return
     */
    public Long getLatestBatchIdByFundCode(String fundCode){
        return (Long) super.queryObject("getLatestBatchIdByFundCode", MapUtils.buildKeyValueMap("fundCode", fundCode));
    }
}
