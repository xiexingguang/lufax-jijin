package com.lufax.jijin.daixiao.gson;

import com.lufax.jijin.daixiao.dto.JijinExGoodSubjectDTO;


public class JijinExGoodSubjectGson {
    private String fundCode;  //基金代码
    private String subjectName;  //主题名称
    private Long subjectIndex;  //主题序号

    public JijinExGoodSubjectGson(JijinExGoodSubjectDTO jijinExGoodSubjectDTO) {
        this.fundCode = jijinExGoodSubjectDTO.getFundCode();
        this.subjectName = jijinExGoodSubjectDTO.getSubjectName();
        this.subjectIndex = jijinExGoodSubjectDTO.getSubjectIndex();
    }

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public Long getSubjectIndex() {
        return subjectIndex;
    }

    public void setSubjectIndex(Long subjectIndex) {
        this.subjectIndex = subjectIndex;
    }
}
