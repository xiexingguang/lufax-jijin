package com.lufax.jijin.fundation.repository;

import com.lufax.jijin.base.repository.BaseRepository;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.fundation.dto.JijinFreezeRecordDTO;
import com.lufax.jijin.fundation.dto.JijinUnFreezeRecordDTO;
import com.lufax.jijin.fundation.dto.JijinUnfreezeRecordCountDTO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by wuxiujun685 on 2016/3/10.
 */
@Repository
public class JijinUnFreezeRecordRepository extends BaseRepository<JijinUnFreezeRecordDTO> {
    @Override
    protected String nameSpace() {
        return "JijinUnFreezeRecord";
    }

    public JijinUnFreezeRecordDTO insertJijinUnFreezeRecord(JijinUnFreezeRecordDTO jijinUnFreezeRecordDTO) {
        return super.insert("insertJijinUnFreezeRecord", jijinUnFreezeRecordDTO);
    }

    public int updateJijinUnFreezeRecord(Map map) {
        return super.update("updateJijinUnFreezeRecord", map);
    }

    public JijinUnFreezeRecordDTO getJijinUnFreezeRecordByAppNo(String appNo) {
        return super.query("getJijinUnFreezeRecordByAppNo", MapUtils.buildKeyValueMap("appNo", appNo));
    }

    public List<JijinUnFreezeRecordDTO> getJijinUnFreezeRecordByDate(String unfreezeDate, String status, int batchAmount) {
        return super.queryList("getJijinUnFreezeRecordByDate", MapUtils.buildKeyValueMap("unfreezeDate", unfreezeDate, "status", status, "maxNum", batchAmount));
    }

    public JijinUnfreezeRecordCountDTO getJijinUnfreezeRecordCountDTO(String unfreezeDate) {
        return (JijinUnfreezeRecordCountDTO) super.queryObject("getJijinUnfreezeRecordCountDTO", MapUtils.buildKeyValueMap("unfreezeDate", unfreezeDate));
    }
}
