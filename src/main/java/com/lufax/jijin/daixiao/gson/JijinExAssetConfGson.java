package com.lufax.jijin.daixiao.gson;

import java.math.BigDecimal;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import com.lufax.jersey.utils.Logger;
import com.lufax.jijin.daixiao.dto.JijinExAssetConfDTO;
import com.site.lookup.util.StringUtils;

/**
 * Created by chenguang451 on 2016/1/6.
 */
public class JijinExAssetConfGson {

    private String fundCode;

    private String endDate;

    private BigDecimal assetValue;  //资产净值

    private BigDecimal stockPer;//股票市值占比

    private BigDecimal bondPer;//债券占比

    private BigDecimal cashPer;//现金占比

    private BigDecimal otherPer;//其他占比

    private BigDecimal nationalDebtPer;//国债占比

    private BigDecimal finanicialBondPer;//金融债占比

    private BigDecimal enterpriseBondPer;//企业债占比
    
    private BigDecimal centralBankBillPer;  //央行票据占比
    
    
    public static JijinExAssetConfGson castFromDto(JijinExAssetConfDTO dto){
    	if(null == dto ) return null;
    	JijinExAssetConfGson gson = new JijinExAssetConfGson();
    	gson.setFundCode(dto.getFundCode());
    	gson.setEndDate(dto.getEndDate());
    	if(!StringUtils.isEmpty(dto.getAssetValue())){
    		try{
    			gson.setAssetValue(new BigDecimal(dto.getAssetValue()));
    		}catch(Exception e){
    			Logger.warn(JijinExAssetConfGson.class, "JijinExAssetConfDTO cast to GSON [AssetValue] error ");
    		}
    	}
    	if(!StringUtils.isEmpty(dto.getStockPer())){
    		try{
    			gson.setStockPer(new BigDecimal(dto.getStockPer()));
    		}catch(Exception e){
    			Logger.warn(JijinExAssetConfGson.class, "JijinExAssetConfDTO cast to GSON [] error ");
    		}
    	}
    	if(!StringUtils.isEmpty(dto.getBondPer())){
    		try{
    			gson.setBondPer(new BigDecimal(dto.getBondPer()));
    		}catch(Exception e){
    			Logger.warn(JijinExAssetConfGson.class, "JijinExAssetConfDTO cast to GSON [getBondPer] error ");
    		}
    	}
    	if(!StringUtils.isEmpty(dto.getCashPer())){
    		try{
    			gson.setCashPer(new BigDecimal(dto.getCashPer()));
    		}catch(Exception e){
    			Logger.warn(JijinExAssetConfGson.class, "JijinExAssetConfDTO cast to GSON [getCashPer] error ");
    		}
    	}
    	if(!StringUtils.isEmpty(dto.getOtherPer())){
    		try{
    			gson.setOtherPer(new BigDecimal(dto.getOtherPer()));
    		}catch(Exception e){
    			Logger.warn(JijinExAssetConfGson.class, "JijinExAssetConfDTO cast to GSON [getOtherPer] error ");
    		}
    	}
    	if(!StringUtils.isEmpty(dto.getNationalDebtPer())){
    		try{
    			gson.setNationalDebtPer(new BigDecimal(dto.getNationalDebtPer()));
    		}catch(Exception e){
    			Logger.warn(JijinExAssetConfGson.class, "JijinExAssetConfDTO cast to GSON [getNationalDebtPer] error ");
    		}
    	}
    	if(!StringUtils.isEmpty(dto.getFinanicialBondPer())){
    		try{
    			gson.setFinanicialBondPer(new BigDecimal(dto.getFinanicialBondPer()));
    		}catch(Exception e){
    			Logger.warn(JijinExAssetConfGson.class, "JijinExAssetConfDTO cast to GSON [getFinanicialBondPer] error ");
    		}
    	}
    	if(!StringUtils.isEmpty(dto.getEnterpriseBondPer())){
    		try{
    			gson.setEnterpriseBondPer(new BigDecimal(dto.getEnterpriseBondPer()));
    		}catch(Exception e){
    			Logger.warn(JijinExAssetConfGson.class, "JijinExAssetConfDTO cast to GSON [getEnterpriseBondPer] error ");
    		}
    	}
    	if(!StringUtils.isEmpty(dto.getCentralBankBillPer())){
    		try{
    			gson.setCentralBankBillPer(new BigDecimal(dto.getCentralBankBillPer()));
    		}catch(Exception e){
    			Logger.warn(JijinExAssetConfGson.class, "JijinExAssetConfDTO cast to GSON [getCentralBankBillPer] error ");
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

	public BigDecimal getAssetValue() {
		return assetValue;
	}

	public BigDecimal getStockPer() {
		return stockPer;
	}

	public BigDecimal getBondPer() {
		return bondPer;
	}

	public BigDecimal getCashPer() {
		return cashPer;
	}

	public BigDecimal getOtherPer() {
		return otherPer;
	}

	public BigDecimal getNationalDebtPer() {
		return nationalDebtPer;
	}

	public BigDecimal getFinanicialBondPer() {
		return finanicialBondPer;
	}

	public BigDecimal getEnterpriseBondPer() {
		return enterpriseBondPer;
	}

	public BigDecimal getCentralBankBillPer() {
		return centralBankBillPer;
	}

	public void setAssetValue(BigDecimal assetValue) {
		this.assetValue = assetValue;
	}

	public void setStockPer(BigDecimal stockPer) {
		this.stockPer = stockPer;
	}

	public void setBondPer(BigDecimal bondPer) {
		this.bondPer = bondPer;
	}

	public void setCashPer(BigDecimal cashPer) {
		this.cashPer = cashPer;
	}

	public void setOtherPer(BigDecimal otherPer) {
		this.otherPer = otherPer;
	}

	public void setNationalDebtPer(BigDecimal nationalDebtPer) {
		this.nationalDebtPer = nationalDebtPer;
	}

	public void setFinanicialBondPer(BigDecimal finanicialBondPer) {
		this.finanicialBondPer = finanicialBondPer;
	}

	public void setEnterpriseBondPer(BigDecimal enterpriseBondPer) {
		this.enterpriseBondPer = enterpriseBondPer;
	}

	public void setCentralBankBillPer(BigDecimal centralBankBillPer) {
		this.centralBankBillPer = centralBankBillPer;
	}

   
}
