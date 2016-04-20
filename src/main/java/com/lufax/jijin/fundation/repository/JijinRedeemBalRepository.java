package com.lufax.jijin.fundation.repository;

import java.math.BigDecimal;

import org.springframework.stereotype.Repository;

import com.lufax.jijin.base.repository.BaseRepository;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.fundation.dto.JijinRedeemBalDTO;


@Repository
public class JijinRedeemBalRepository extends BaseRepository<JijinRedeemBalDTO> {


    public JijinRedeemBalDTO insertBusJijinRedeemBal(JijinRedeemBalDTO jijinRedeemBalDTO) {
        return super.insert("insertBusJijinRedeemBal", jijinRedeemBalDTO);
    }

    public JijinRedeemBalDTO findBusJijinRedeemBalByFundCode(String fundCode) {
        return super.query("findBusJijinRedeemBalByFundCode", fundCode);
    }

    public int updateBusJijinRedeemBalById(Long id,BigDecimal amount,Long version,String snapshotTime) {
        return super.update("updateBusJijinRedeemBalById",MapUtils.buildKeyValueMap("id",id,"amount",amount,"version",version,"snapshotTime",snapshotTime));
    }

    @Override
    protected String nameSpace() {
        return "BusJijinRedeemBal";
    }

}
