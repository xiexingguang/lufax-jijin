package com.lufax.jijin.fundation.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.lufax.jijin.base.repository.BaseRepository;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.fundation.dto.JijinRedeemBalHisDTO;


@Repository
public class JijinRedeemBalHisRepository extends BaseRepository<JijinRedeemBalHisDTO> {


    public JijinRedeemBalHisDTO insertBusJijinRedeemBalHis(JijinRedeemBalHisDTO jijinRedeemBalHisDTO) {
        return super.insert("insertBusJijinRedeemBalHis", jijinRedeemBalHisDTO);
    }

    public JijinRedeemBalHisDTO findBusJijinRedeemBalHisById(Long id) {
        return super.query("findBusJijinRedeemBalHisById", id);
    }
    
    
    public List<JijinRedeemBalHisDTO> findBusJijinRedeemBalHisAfterTime(String snapshotTime) {
        return super.queryList("findBusJijinRedeemBalHisAfterTime", snapshotTime);
    }
    
    /**
     * 
     * @param startTime
     * @param endTime
     * @return  sum(amount)  范围 startTime<result<=endTime
     */
    public BigDecimal findBusJijinRedeemBalHisBetweenStartEnd(String startTime,String endTime) {
        return (BigDecimal)super.queryObject("findBusJijinRedeemBalHisBetweenStartEnd", MapUtils.buildKeyValueMap("startTime",startTime,"endTime",endTime));
    }

    @Override
    protected String nameSpace() {
        return "BusJijinRedeemBalHis";
    }

}
