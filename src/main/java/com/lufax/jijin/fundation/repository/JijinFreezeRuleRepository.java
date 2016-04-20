package com.lufax.jijin.fundation.repository;

import com.lufax.jijin.base.repository.BaseRepository;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.fundation.dto.JijinFreezeRuleDTO;
import org.springframework.stereotype.Repository;

/**
 * @author liudong735
 * @date 2016年03月10日
 */
@Repository
public class JijinFreezeRuleRepository extends BaseRepository<JijinFreezeRuleDTO> {
    @Override
    protected String nameSpace() {
        return "BusJijinFreezeRule";
    }

    public JijinFreezeRuleDTO getJijinFreezeRuleByFundCodeAndMode(String fundCode, String businessMode) {
        return super.query("getJijinFreezeRuleByFundCodeAndMode", MapUtils.buildKeyValueMap(
                "fundCode", fundCode,
                "businessMode", businessMode,
                "isActive", 1
        ));
    }
}
