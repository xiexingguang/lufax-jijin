package com.lufax.jijin.fundation.repository;

import com.lufax.jijin.base.repository.BaseRepository;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.fundation.dto.JijinWithdrawRecordDTO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by NiuZhanJun on 9/28/15.
 */
@Repository
public class JijinWithdrawRecordRepository extends BaseRepository<JijinWithdrawRecordDTO> {

    @Override
    protected String nameSpace() {
        return "JijinWithdrawRecord";
    }

    public Object insertJijinWithdrawRecord(JijinWithdrawRecordDTO jijinWithdrawRecordDTO) {
        return super.insert("insertJijinWithdrawRecord", jijinWithdrawRecordDTO);
    }

    public int updateJijinWithdrawRecord(Map map) {
        return super.update("updateJijinWithdrawRecord", map);
    }

    public JijinWithdrawRecordDTO getJijinWithdrawRecordByRecordId(String recordId) {
        List<JijinWithdrawRecordDTO> list = super.queryList("getJijinWithdrawRecordByRecordId", MapUtils.buildKeyValueMap("recordId", recordId));
        if (list != null && list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }
}
