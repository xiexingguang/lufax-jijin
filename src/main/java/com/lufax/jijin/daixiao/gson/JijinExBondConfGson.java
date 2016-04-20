package com.lufax.jijin.daixiao.gson;

import java.math.BigDecimal;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import com.lufax.jersey.utils.Logger;
import com.lufax.jijin.daixiao.dto.JijinExBondConfDTO;
import com.site.lookup.util.StringUtils;

/**
 * Created by chenguang451 on 2016/1/6.
 */
public class JijinExBondConfGson {

    private String fundCode;

    private String endDate;

    private String bondCode;

    private String bondName;

    private BigDecimal bondValue;

    private BigDecimal bondPer;
    
    public static JijinExBondConfGson castFromDto(JijinExBondConfDTO dto){
    	if(null == dto) return null;
    	JijinExBondConfGson gson = new JijinExBondConfGson();
    	gson.setFundCode(dto.getFundCode());
    	gson.setEndDate(dto.getEndDate());
    	gson.setBondCode(dto.getBondCode());
    	gson.setBondName(dto.getBondName());
    	if(StringUtils.isNotEmpty(dto.getBondValue())){
    		try{
    			gson.setBondValue(new BigDecimal(dto.getBondValue()));
    		}catch(Exception e){
    			Logger.warn(JijinExBondConfGson.class,"JijinExBondConfGson cast from dto [bondValue] error");
    		}
    	}
    	if(StringUtils.isNotEmpty(dto.getBondPer())){
    		try{
    			gson.setBondPer(new BigDecimal(dto.getBondPer()));
    		}catch(Exception e){
    			Logger.warn(JijinExBondConfGson.class,"JijinExBondConfGson cast from dto [BondPer] error");
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

    public String getBondCode() {
        return bondCode;
    }

    public void setBondCode(String bondCode) {
        this.bondCode = bondCode;
    }

    public String getBondName() {
        return bondName;
    }

    public void setBondName(String bondName) {
        this.bondName = bondName;
    }


    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

	public BigDecimal getBondValue() {
		return bondValue;
	}

	public BigDecimal getBondPer() {
		return bondPer;
	}

	public void setBondValue(BigDecimal bondValue) {
		this.bondValue = bondValue;
	}

	public void setBondPer(BigDecimal bondPer) {
		this.bondPer = bondPer;
	}
}
