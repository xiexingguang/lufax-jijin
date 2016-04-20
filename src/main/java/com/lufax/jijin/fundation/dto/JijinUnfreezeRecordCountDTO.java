package com.lufax.jijin.fundation.dto;

import com.lufax.jijin.base.dto.BaseDTO;

import java.math.BigDecimal;

/**
 * @author liudong735
 * @date 2016年03月10日
 */
public class JijinUnfreezeRecordCountDTO extends BaseDTO {
    private static final long serialVersionUID = -3885614501483593471L;

    private Long count;
    private BigDecimal totalFreezeShare;

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public BigDecimal getTotalFreezeShare() {
        return totalFreezeShare;
    }

    public void setTotalFreezeShare(BigDecimal totalFreezeShare) {
        this.totalFreezeShare = totalFreezeShare;
    }
}
