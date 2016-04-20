package com.lufax.jijin.daixiao.gson;

import com.lufax.jijin.base.dto.BaseDTO;
import com.lufax.jijin.daixiao.dto.JijinExRiskGradeDTO;

import java.util.Date;


public class JijinExRiskGradeGson{

    private String fundCode;  //基金代码
    private String riskGrade;  //风险等级

    public JijinExRiskGradeGson() {
    }

    public JijinExRiskGradeGson(JijinExRiskGradeDTO jijinExRiskGradeDTO) {
        this.fundCode = jijinExRiskGradeDTO.getFundCode();
        this.riskGrade = jijinExRiskGradeDTO.getRiskGrade();
    }

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getRiskGrade() {
        return riskGrade;
    }

    public void setRiskGrade(String riskGrade) {
        this.riskGrade = riskGrade;
    }
}
