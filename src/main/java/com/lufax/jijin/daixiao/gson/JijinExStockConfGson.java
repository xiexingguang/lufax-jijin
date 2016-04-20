package com.lufax.jijin.daixiao.gson;

import java.math.BigDecimal;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import com.lufax.jersey.utils.Logger;
import com.lufax.jijin.daixiao.dto.JijinExStockConfDTO;
import com.site.lookup.util.StringUtils;

/**
 * Created by chenguang451 on 2016/1/6.
 */
public class JijinExStockConfGson {

    private String fundCode;

    private String endDate;

    private String stockCode;

    private String stockName;

    private BigDecimal stockPer;

    private String announceDate;

    private BigDecimal stockValuePer;

    private BigDecimal capitalStockPer;

    private BigDecimal stockValue;

    private BigDecimal stockShare;
    
    public static JijinExStockConfGson castFromDTO(JijinExStockConfDTO dto){
    	if(null == dto) return null;
    	JijinExStockConfGson gson = new JijinExStockConfGson();
    	gson.setFundCode(dto.getFundCode());
    	gson.setEndDate(dto.getEndDate());
    	gson.setStockCode(dto.getStockCode());
    	gson.setStockName(dto.getStockName());
    	if(StringUtils.isNotEmpty(dto.getStockPer())){
    		try{
    			gson.setStockPer(new BigDecimal(dto.getStockPer()));
    		}catch(Exception e){
    			Logger.warn(JijinExStockConfGson.class, "JijinExStockConfGson cast from dto [stockPer] error");
    		}
    	}
    	gson.setAnnounceDate(dto.getAnnounceDate());
    	if(StringUtils.isNotEmpty(dto.getStockValuePer())){
    		try{
    			gson.setStockValuePer(new BigDecimal(dto.getStockValuePer()));
    		}catch(Exception e){
    			Logger.warn(JijinExStockConfGson.class, "JijinExStockConfGson cast from dto [stockValuePer] error");
    		}
    	}
    	if(StringUtils.isNotEmpty(dto.getCapitalStockPer())){
    		try{
    			gson.setCapitalStockPer(new BigDecimal(dto.getCapitalStockPer()));
    		}catch(Exception e){
    			Logger.warn(JijinExStockConfGson.class, "JijinExStockConfGson cast from dto [capitalStockPer] error");
    		}
    	}
    	if(StringUtils.isNotEmpty(dto.getStockValue())){
    		try{
    			gson.setStockValue(new BigDecimal(dto.getStockValue()));
    		}catch(Exception e){
    			Logger.warn(JijinExStockConfGson.class, "JijinExStockConfGson cast from dto [stockValue] error");
    		}
    	}
    	if(StringUtils.isNotEmpty(dto.getStockShare())){
    		try{
    			gson.setStockShare(new BigDecimal(dto.getStockShare()));
    		}catch(Exception e){
    			Logger.warn(JijinExStockConfGson.class, "JijinExStockConfGson cast from dto [stockShare] error");
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

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

 

    public String getAnnounceDate() {
        return announceDate;
    }

    public void setAnnounceDate(String announceDate) {
        this.announceDate = announceDate;
    }




	public BigDecimal getStockPer() {
		return stockPer;
	}

	public void setStockPer(BigDecimal stockPer) {
		this.stockPer = stockPer;
	}

	public BigDecimal getCapitalStockPer() {
		return capitalStockPer;
	}

	public BigDecimal getStockValue() {
		return stockValue;
	}

	public BigDecimal getStockShare() {
		return stockShare;
	}

	public void setCapitalStockPer(BigDecimal capitalStockPer) {
		this.capitalStockPer = capitalStockPer;
	}

	public void setStockValue(BigDecimal stockValue) {
		this.stockValue = stockValue;
	}

	public void setStockShare(BigDecimal stockShare) {
		this.stockShare = stockShare;
	}

	public BigDecimal getStockValuePer() {
		return stockValuePer;
	}

	public void setStockValuePer(BigDecimal stockValuePer) {
		this.stockValuePer = stockValuePer;
	}
}
