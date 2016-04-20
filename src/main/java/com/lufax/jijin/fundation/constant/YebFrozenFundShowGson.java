package com.lufax.jijin.fundation.constant;

import com.lufax.jijin.base.utils.DateUtils;
import com.lufax.jijin.fundation.dto.JijinFrozenDetailDTO;

import java.math.BigDecimal;


public class YebFrozenFundShowGson {
	private long id;
    private String createdAt;
    private String type;
    private String remark;
    private BigDecimal frozenAmount;
    private BigDecimal unFrozenAmount;
    private BigDecimal yebAccountFrozenAmount;

    public YebFrozenFundShowGson(JijinFrozenDetailDTO jijinFrozenDetailDTO) {

        this.id = jijinFrozenDetailDTO.getId();
        this.createdAt = DateUtils.formatDateTime(jijinFrozenDetailDTO.getCreatedAt());

        if (isFrozen(jijinFrozenDetailDTO.getTransactionType())) {
            this.frozenAmount = jijinFrozenDetailDTO.getFrozenAmount();
            this.type = "冻结";
        } else {
            this.frozenAmount = BigDecimal.ZERO;
        }

        if (isUnFrozen(jijinFrozenDetailDTO.getTransactionType())) {
            this.unFrozenAmount = jijinFrozenDetailDTO.getUnFrozenAmount();
            this.type = "解冻";
        } else {
            this.unFrozenAmount = BigDecimal.ZERO;
        }

        this.yebAccountFrozenAmount = jijinFrozenDetailDTO.getTotalFrozenAmount();

        this.remark = jijinFrozenDetailDTO.getRemark();
    }

    public Boolean isFrozen(String type) {
        return YebTransactionType.SELL_FREEZE.getCode().equals(type);
    }

    public Boolean isUnFrozen(String type) {
        return YebTransactionType.SELL_SUCCESS_UNFREEZE.getCode().equals(type) || YebTransactionType.SELL_FAIL_UNFREEZE.getCode().equals(type);
    }

    public long getId() {
		return id;
	}
}
