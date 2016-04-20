package com.lufax.jijin.daixiao.repository;

import com.lufax.jijin.base.repository.BaseRepository;
import com.lufax.jijin.daixiao.dto.JijinExFxPerformDTO;
import com.lufax.mq.client.util.MapUtils;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 指数涨跌幅
 *
 * @author chenqunhui
 */
@Repository
public class JijinExFxPerformRepository extends BaseRepository<JijinExFxPerformDTO> {

    @Override
    protected String nameSpace() {
        return "BusJijinExFxPerform";
    }

    public JijinExFxPerformDTO insertJijinExFxPerform(JijinExFxPerformDTO dto) {
        return super.insert("insertJijinExFxPerform", dto);
    }

    public Long getLatestBatchId() {
        return (Long) super.queryObject("getLatestBatchId");
    }

    public List<JijinExFxPerformDTO> getJijinExFxPerformByBatchId(Long batchId) {
        return super.queryList("getJijinExFxPerformByBatchId", batchId);
    }

    public void batchInserJijinExFxPerform(List<JijinExFxPerformDTO> list) {
        batchInsert("insertJijinExFxPerform", list);
    }

    public List<JijinExFxPerformDTO> getJijinExFxPerformByFindexCode(String findexCode) {
        return super.queryList("getJijinExFxPerformByFindexCode", findexCode);
    }

    public JijinExFxPerformDTO getLastestJijinExFxPerformByFindexCode(String findexCode) {
        List<JijinExFxPerformDTO> list = super.queryList("getLastestJijinExFxPerformByFindexCode", findexCode);
        if(CollectionUtils.isEmpty(list)){
            return null;
        }
        return list.get(0);
    }


    public int updateTheSameFindexCodeAndDateToNotValid(Long id, String findexCode, String date) {
        return super.update("updateTheSameFindexCodeAndDateToNotValid", MapUtils.buildKeyValueMap("id", id, "findexCode", findexCode, "date", date));
    }

    public int updateJijinExFxPerform(Map condition) {
        return super.update("updateJijinExFxPerform", condition);
    }

    public List<JijinExFxPerformDTO> getUnDispachedJijinExFxPerform(int limit) {
        return super.queryList("getUnDispachedJijinExFxPerform", limit);
    }
}
