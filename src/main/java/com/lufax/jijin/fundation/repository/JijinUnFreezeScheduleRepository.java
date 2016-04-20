package com.lufax.jijin.fundation.repository;

import com.lufax.jijin.base.repository.BaseRepository;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.fundation.dto.JijinUnFreezeScheduleDTO;
import com.lufax.jijin.fundation.dto.JijinUnFreezeScheduleDTO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by wuxiujun685 on 2016/3/10.
 */
@Repository
public class JijinUnFreezeScheduleRepository extends BaseRepository<JijinUnFreezeScheduleDTO> {
    @Override
    protected String nameSpace() {
        return "JijinUnFreezeSchedule";
    }
    public JijinUnFreezeScheduleDTO insertJijinUnFreezeSchedule(JijinUnFreezeScheduleDTO JijinUnFreezeScheduleDTO) {
        return super.insert("insertJijinUnFreezeSchedule", JijinUnFreezeScheduleDTO);
    }

    public int updateJijinUnFreezeSchedule(Map map) {
        return super.update("updateJijinUnFreezeSchedule", map);
    }

    public JijinUnFreezeScheduleDTO getJijinUnFreezeScheduleListByUnfreezeDate(String unfreezeDate) {
        return super.query("getJijinUnFreezeScheduleByUnFreezeDate", MapUtils.buildKeyValueMap("unfreezeDate", unfreezeDate));
    }
}
