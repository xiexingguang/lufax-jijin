package com.lufax.jijin.ylx.enums;

import java.util.ArrayList;
import java.util.List;


public enum YLXTradeDetailType {
    BUY(YLXTradeType.BUY_TYPE),
    PURCHASE(YLXTradeType.BUY_TYPE),
    REDEEM(YLXTradeType.SELL_TYPE)
    ;
    private YLXTradeType tradeType;
    YLXTradeDetailType(YLXTradeType tradeType){
        this.tradeType = tradeType;
    }


    public static enum YLXTradeType{
        BUY_TYPE,
        SELL_TYPE
        ;

        public List<YLXTradeDetailType> getDetailTypeList() {
            List<YLXTradeDetailType> result = new ArrayList<YLXTradeDetailType>();
            for (YLXTradeDetailType status : YLXTradeDetailType.values()) {
                if (this.equals(status.tradeType)) {
                    result.add(status);
                }
            }
            return result;
        }
    }
}
