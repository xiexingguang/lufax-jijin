package com.lufax.jijin.daixiao.gson;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * Created by chenguang451 on 2016/1/6.
 */
public class JijinExAnnounceGson {

    private String fundCode;

    private String securityId;

    private String companyId;

    private String pubDate;

    private String title;

    private String colCode;

    private String abstractInfo;

    private String pubLink;

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getSecurityId() {
        return securityId;
    }

    public void setSecurityId(String securityId) {
        this.securityId = securityId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getColCode() {
        return colCode;
    }

    public void setColCode(String colCode) {
        this.colCode = colCode;
    }

    public String getAbstractInfo() {
        return abstractInfo;
    }

    public void setAbstractInfo(String abstractInfo) {
        this.abstractInfo = abstractInfo;
    }

    public String getPubLink() {
        return pubLink;
    }

    public void setPubLink(String pubLink) {
        this.pubLink = pubLink;
    }
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

}
