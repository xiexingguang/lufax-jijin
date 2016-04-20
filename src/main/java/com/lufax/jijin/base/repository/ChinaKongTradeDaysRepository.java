package com.lufax.jijin.base.repository;

import com.lufax.jijin.base.dto.TradeDaysDTO;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class ChinaKongTradeDaysRepository extends CfgdataBaseRepository<TradeDaysDTO> {

    public TradeDaysDTO getTradeDay(Date date) {
        return query("getTradeDay", date);
    }

    public List<TradeDaysDTO> getTradeDayByDate(Date date) {
        return queryList("getTradeDayByDate", date);
    }
    
    public TradeDaysDTO getLastTradeDay(Date date) {
        return query("getLastTradeDay", date);
    }

    @Override
    protected String nameSpace() {
        return "CFG_CHINAKONG_TRADE_DAYS";
    }
}
