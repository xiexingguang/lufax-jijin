package com.lufax.jijin.fundation.gson;

import java.math.BigDecimal;
import com.lufax.jijin.fundation.dto.JijinInfoDTO;
import com.lufax.jijin.fundation.dto.JijinUserBalanceDTO;

public class CurrencyToStockGson {
    /* 基金产品代码-只能是货基 */
    private String fundCode;
    private Long productId;
    /* 基金名称 */
    private String fundName;
    /* 基金份额（可赎回部分） */
    private BigDecimal shareBalance;
    private BigDecimal minHoldShareCount; //最低持有份额

    public CurrencyToStockGson(JijinUserBalanceDTO jijinUserBalanceDTO, JijinInfoDTO jijinInfoDTO,BigDecimal minHoldShareCount) {
//        this.fundCode = fundCode;
        this.fundCode = jijinUserBalanceDTO.getFundCode();
        this.fundName = jijinInfoDTO.getFundName();
        this.shareBalance = jijinUserBalanceDTO.getShareBalance();
        this.productId = jijinInfoDTO.getProductId();
        this.minHoldShareCount= minHoldShareCount;
    }

    public BigDecimal getShareBalance() {
        return shareBalance;
    }
}
