package com.lufax.jijin.daixiao.gson;

import java.math.BigDecimal;

import com.lufax.jersey.utils.Logger;
import com.lufax.jijin.daixiao.dto.JijinExIndustryConfDTO;
import com.site.lookup.util.StringUtils;


/**
 * Created by chenguang451 on 2016/1/6.
 * 行业配置
 */

public class JijinExIndustryConfGson {

    private String fundCode;

    private String endDate;

    private String industryCode;   //行业编码

    private String industryName;  //行业名称

    private BigDecimal industryPer;   //行业占比
    
    public static JijinExIndustryConfGson castFromDto(JijinExIndustryConfDTO dto){
    	if(null == dto) return null;
    	JijinExIndustryConfGson gson = new JijinExIndustryConfGson();
    	gson.setFundCode(dto.getFundCode());
    	gson.setEndDate(dto.getEndDate());
    	gson.setIndustryCode(dto.getIndustryCode());
    	gson.setIndustryName(dto.getIndustryName());
    	if(StringUtils.isNotEmpty(dto.getIndustryPer())){
    		try{
    			gson.setIndustryPer(new BigDecimal(dto.getIndustryPer()));
    		}catch(Exception e){
    			Logger.warn(JijinExIndustryConfGson.class, "JijinExIndustryConfDTO cast to gson [industryPer] error");
    		}
    	}
    	return gson;
    }
    
    
    
    
    

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getIndustryCode() {
        return industryCode;
    }

    public void setIndustryCode(String industryCode) {
        this.industryCode = industryCode;
    }

    public String getIndustryName() {
        return industryName;
    }

    public void setIndustryName(String industryName) {
        this.industryName = industryName;
    }


	public BigDecimal getIndustryPer() {
		return industryPer;
	}

	public void setIndustryPer(BigDecimal industryPer) {
		this.industryPer = industryPer;
	}


}
