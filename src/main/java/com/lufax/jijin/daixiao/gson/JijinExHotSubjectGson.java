package com.lufax.jijin.daixiao.gson;

import com.lufax.jijin.daixiao.dto.JijinExHotSubjectDTO;

public class JijinExHotSubjectGson {

    private String fundCode;  //基金代码
    private String subjectName;  //主题名称
    private Long subjectIndex;  //主题序号

    public JijinExHotSubjectGson(JijinExHotSubjectDTO jijinExHotSubjectDTO) {
        this.fundCode = jijinExHotSubjectDTO.getFundCode();
        this.subjectName = jijinExHotSubjectDTO.getSubjectName();
        this.subjectIndex = jijinExHotSubjectDTO.getSubjectIndex();
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
