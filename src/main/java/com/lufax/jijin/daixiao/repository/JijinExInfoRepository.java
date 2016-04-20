package com.lufax.jijin.daixiao.repository;

import com.lufax.jijin.base.repository.BaseRepository;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.daixiao.dto.JijinExInfoDTO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 基金其他基本信息
 *
 * @author
 */
@Repository
public class JijinExInfoRepository extends BaseRepository<JijinExInfoDTO> {

    @Override
    protected String nameSpace() {
        return "BusJijinExInfo";
    }

    public JijinExInfoDTO insertJijinExInfo(JijinExInfoDTO jijinExInfoDTO) {
        return super.insert("insertJijinExInfo", jijinExInfoDTO);
    }

    public int updateJijinExInfo(Map condition) {
        return super.update("updateJijinExInfo", condition);
    }

    public List<JijinExInfoDTO> getJijinExInfoByFundCode(String fundCode) {
        return super.queryList("getJijinExInfoByFundCode", fundCode);
    }

    public JijinExInfoDTO getLastestJijinExInfoByFundCode(String fundCode) {
        List<JijinExInfoDTO> list = super.queryList("getLastestJijinExInfoByFundCode", fundCode);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return list.get(0);
    }


    public List<JijinExInfoDTO> getJijinExInfoByStatus(String status, int maxNum) {
        return super.queryList("getJijinExInfoByStatus", MapUtils.buildKeyValueMap("status", status, "maxNum", maxNum));
    }

    public JijinExInfoDTO getJijinExInfoById(Long id) {
        return (JijinExInfoDTO) super.queryObject("getJijinExInfoById", id);
    }

    public void batchInsertDTOs(List dtos) {
        super.batchInsert("insertJijinExInfo", dtos);
    }
}
