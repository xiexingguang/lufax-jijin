package com.lufax.jijin.daixiao.gson;

import com.lufax.jijin.daixiao.dto.JijinExFundTypeDTO;


public class JijinExFundTypeGson {

    private String fundCode;  //基金代码
    private String fundType;  //基金类型

    public JijinExFundTypeGson() {
    }

    public JijinExFundTypeGson(JijinExFundTypeDTO jijinExFundTypeDTO) {
        this.fundCode = jijinExFundTypeDTO.getFundCode();
        this.fundType = jijinExFundTypeDTO.getFundType();
    }

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getFundType() {
        return fundType;
    }

    public void setFundType(String fundType) {
        this.fundType = fundType;
    }
}
