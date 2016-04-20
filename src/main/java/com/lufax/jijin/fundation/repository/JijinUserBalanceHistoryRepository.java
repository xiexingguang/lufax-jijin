package com.lufax.jijin.fundation.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lufax.jijin.base.repository.BaseRepository;
import com.lufax.jijin.fundation.dto.JijinUserBalanceHistoryDTO;
import com.lufax.mq.client.util.MapUtils;

@Repository
public class JijinUserBalanceHistoryRepository extends BaseRepository<JijinUserBalanceHistoryDTO>{

    protected String nameSpace(){
        return "BusJijinUserBalanceHistory";
    }

    public Object insertBusJijinUserBalanceHistory(JijinUserBalanceHistoryDTO jijinUserBalanceHistoryDTO) {
        return super.insert("insertBusJijinUserBalanceHistory", jijinUserBalanceHistoryDTO);
    }

    public List<JijinUserBalanceHistoryDTO> findBusJijinUserBalanceHistoryList(long userId,String fundCode,String bizDate) {
        return super.queryList("findBusJijinUserBalanceHistory", MapUtils.buildKeyValueMap("userId",userId,"fundCode",fundCode,"bizDate",bizDate));
    }
}
