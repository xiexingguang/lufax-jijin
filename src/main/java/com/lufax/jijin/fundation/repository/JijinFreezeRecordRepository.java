package com.lufax.jijin.fundation.repository;

import com.lufax.jijin.base.repository.BaseRepository;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.fundation.dto.JijinFreezeRecordDTO;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * @author liudong735
 * @date 2016年03月10日
 */
@Repository
public class JijinFreezeRecordRepository extends BaseRepository<JijinFreezeRecordDTO> {
    @Override
    protected String nameSpace() {
        return "BusJijinFreezeRecord";
    }

    public JijinFreezeRecordDTO insertBusJijinFreezeRecord(JijinFreezeRecordDTO jijinFreezeRecordDTO) {
        return super.insert("insertBusJijinFreezeRecord", jijinFreezeRecordDTO);
    }

    public JijinFreezeRecordDTO getJijinFreezeRecordByAppNo(String appNo) {
        return super.query("getJijinFreezeRecordByAppNo", MapUtils.buildKeyValueMap("appNo", appNo));
    }
}
