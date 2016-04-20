package com.lufax.jijin.daixiao.gson;

import java.math.BigDecimal;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import com.lufax.jijin.daixiao.dto.JijinExMaPerfDTO;

/**
 * Created by chenguang451 on 2016/1/6.
 */
public class JijinExMaPerfGson {

    private String maId;//基金经理ID

    private String pubDate;//基金经理业绩表现的公告日期

    private String maPointType; //基金经理指数类型

    private BigDecimal maPoint;//指数点位

    private BigDecimal benefitThisYear;//今年以来回报

    private String rankThisYear;//今年以来排名

    private BigDecimal benefitThisWeek;//一周回报

    private String rankThisWeek;//一周排名

    private BigDecimal benefitThisMonth;//一月回报

    private String rankThisMonth;//一月排名

    private BigDecimal benefitThreeMonth;//

    private String rankThreeMonth;//

    private BigDecimal benefitSixMonth;//

    private String rankSixMonth;//

    private BigDecimal benefitOneYear;//

    private String rankOneYear;//

    private BigDecimal benefitTwoYear;//

    private String rankTwoYear;//

    private BigDecimal benefitThreeYear;//

    private String rankThreeYear;//

    private BigDecimal benefitFiveYear;//

    private String rankFiveYear;//

    private BigDecimal benefitTenYear;//

    private String rankTenYear;//

    private BigDecimal benefitFromBegin;//履任以来回报

    private BigDecimal yearBenefitFromBegin;//履任以来年化回报

    private String rankFromBegin;//履任以来排名

    private BigDecimal benefitWorstSixMonth;//最差连续六月回报

    private BigDecimal benefitBestSixMonth;//最佳连续六月回报
    
    
    public JijinExMaPerfGson(JijinExMaPerfDTO dto){
    	this.maId = dto.getMaId();//基金经理ID
	    this.pubDate= dto.getPubDate(); //基金经理业绩表现的公告日期
	    this.maPointType=  dto.getMaPointType(); //基金经理指数类型
	    
	   
	    this.rankThisYear=  dto.getRankThisYear();//今年以来排名
	    
	    this.rankThisWeek= dto.getRankThisWeek();  //一周排名
	    
	    this.rankThisMonth= dto.getRankThisMonth(); //一月排名
	    
	    this.rankThreeMonth= dto.getRankThreeMonth(); //
	    
	    this.rankSixMonth= dto.getRankSixMonth(); //
	    
	    this.rankOneYear= dto.getRankOneYear(); //
	    
	    this.rankTwoYear= dto.getRankTwoYear(); //
	    
	    this.rankThreeYear= dto.getRankThreeYear(); //
	    
	    this.rankFiveYear=  dto.getRankFiveYear();//
	    
	    this.rankTenYear=  dto.getRankTenYear();//
	    this.rankFromBegin = dto.getRankFromBegin();
	    try{
	    	this.maPoint= new BigDecimal(dto.getMaPoint()); //指数点位
	    }catch(Exception e){
	    	
	    }
    	try{
    		this.benefitThisYear = new BigDecimal(dto.getBenefitThisYear());
    	}catch(Exception e){
    	}
	    try{
	    	//private BigDecimal benefitThisWeek=  //一周回报
	    	this.benefitThisWeek = new BigDecimal(dto.getBenefitThisWeek());
    	}catch(Exception e){
    		
    	}
	    try{
	    	//private BigDecimal benefitThisMonth=  //一月回报
	    	this.benefitThisMonth = new BigDecimal(dto.getBenefitThisMonth());
    	}catch(Exception e){
    		
    	}
	    try{
	    	//private BigDecimal =  //
    		this.benefitThreeMonth = new BigDecimal(dto.getBenefitThreeMonth());
    	}catch(Exception e){
    		
    	}
	    try{
	    	//private BigDecimal =  //
    		this.benefitSixMonth = new BigDecimal(dto.getBenefitSixMonth());
    	}catch(Exception e){
    		
    	}
	    try{
	    	//private BigDecimal benefitOneYear=  //
    		this.benefitOneYear = new BigDecimal(dto.getBenefitOneYear());
    	}catch(Exception e){
    		
    	}
	    try{
	    	//private BigDecimal =  //
    		this.benefitTwoYear = new BigDecimal(dto.getBenefitTwoYear());
    	}catch(Exception e){
    		
    	}
	    try{
	    	//private BigDecimal benefitThreeYear=  //
    		this.benefitThreeYear = new BigDecimal(dto.getBenefitThreeYear());
    	}catch(Exception e){
    		
    	}
	    try{
	    	//private BigDecimal =  //
    		this.benefitFiveYear = new BigDecimal(dto.getBenefitFiveYear());
    	}catch(Exception e){
    		
    	}
	    try{
	    	//private BigDecimal =  //
    		this.benefitTenYear = new BigDecimal(dto.getBenefitTenYear());
    	}catch(Exception e){
    		
    	}
	    try{
	    	//履任以来回报
    		this.benefitFromBegin = new BigDecimal(dto.getBenefitFromBegin());
    	}catch(Exception e){
    		
    	}
	    try{
    		this.yearBenefitFromBegin = new BigDecimal(dto.getYearBenefitFromBegin());
    	}catch(Exception e){
    		
    	}
	    try{
	    	//最差连续六月回报
    		this.benefitWorstSixMonth = new BigDecimal(dto.getBenefitWorstSixMonth());
    	}catch(Exception e){
    		
    	}
	    try{ //最佳连续六月回报
    		this.benefitBestSixMonth = new BigDecimal(dto.getBenefitBestSixMonth());
    	}catch(Exception e){
    		
    	}
	   
    	
    }
    

    public String getMaId() {
        return maId;
    }

    public void setMaId(String maId) {
        this.maId = maId;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getMaPointType() {
        return maPointType;
    }

    public void setMaPointType(String maPointType) {
        this.maPointType = maPointType;
    }





    public String getRankThisYear() {
        return rankThisYear;
    }

    public void setRankThisYear(String rankThisYear) {
        this.rankThisYear = rankThisYear;
    }


    public String getRankThisWeek() {
        return rankThisWeek;
    }

    public void setRankThisWeek(String rankThisWeek) {
        this.rankThisWeek = rankThisWeek;
    }


    public String getRankThisMonth() {
        return rankThisMonth;
    }

    public void setRankThisMonth(String rankThisMonth) {
        this.rankThisMonth = rankThisMonth;
    }



    public String getRankThreeMonth() {
        return rankThreeMonth;
    }

    public void setRankThreeMonth(String rankThreeMonth) {
        this.rankThreeMonth = rankThreeMonth;
    }


    public String getRankSixMonth() {
        return rankSixMonth;
    }

    public void setRankSixMonth(String rankSixMonth) {
        this.rankSixMonth = rankSixMonth;
    }



    public String getRankOneYear() {
        return rankOneYear;
    }

    public void setRankOneYear(String rankOneYear) {
        this.rankOneYear = rankOneYear;
    }



    public String getRankTwoYear() {
        return rankTwoYear;
    }

    public void setRankTwoYear(String rankTwoYear) {
        this.rankTwoYear = rankTwoYear;
    }


    public String getRankThreeYear() {
        return rankThreeYear;
    }

    public void setRankThreeYear(String rankThreeYear) {
        this.rankThreeYear = rankThreeYear;
    }


    public String getRankFiveYear() {
        return rankFiveYear;
    }

    public void setRankFiveYear(String rankFiveYear) {
        this.rankFiveYear = rankFiveYear;
    }



    public String getRankTenYear() {
        return rankTenYear;
    }

    public void setRankTenYear(String rankTenYear) {
        this.rankTenYear = rankTenYear;
    }

  
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

	public BigDecimal getBenefitThisYear() {
		return benefitThisYear;
	}

	public BigDecimal getBenefitThisWeek() {
		return benefitThisWeek;
	}

	public BigDecimal getBenefitThisMonth() {
		return benefitThisMonth;
	}

	public BigDecimal getBenefitThreeMonth() {
		return benefitThreeMonth;
	}

	public BigDecimal getBenefitSixMonth() {
		return benefitSixMonth;
	}

	public BigDecimal getBenefitOneYear() {
		return benefitOneYear;
	}

	public BigDecimal getBenefitTwoYear() {
		return benefitTwoYear;
	}

	public BigDecimal getBenefitThreeYear() {
		return benefitThreeYear;
	}

	public BigDecimal getBenefitFiveYear() {
		return benefitFiveYear;
	}

	public BigDecimal getBenefitTenYear() {
		return benefitTenYear;
	}

	public BigDecimal getBenefitFromBegin() {
		return benefitFromBegin;
	}

	public BigDecimal getYearBenefitFromBegin() {
		return yearBenefitFromBegin;
	}


	public BigDecimal getBenefitWorstSixMonth() {
		return benefitWorstSixMonth;
	}

	public BigDecimal getBenefitBestSixMonth() {
		return benefitBestSixMonth;
	}

	public void setBenefitThisYear(BigDecimal benefitThisYear) {
		this.benefitThisYear = benefitThisYear;
	}

	public void setBenefitThisWeek(BigDecimal benefitThisWeek) {
		this.benefitThisWeek = benefitThisWeek;
	}

	public void setBenefitThisMonth(BigDecimal benefitThisMonth) {
		this.benefitThisMonth = benefitThisMonth;
	}

	public void setBenefitThreeMonth(BigDecimal benefitThreeMonth) {
		this.benefitThreeMonth = benefitThreeMonth;
	}

	public void setBenefitSixMonth(BigDecimal benefitSixMonth) {
		this.benefitSixMonth = benefitSixMonth;
	}

	public void setBenefitOneYear(BigDecimal benefitOneYear) {
		this.benefitOneYear = benefitOneYear;
	}

	public void setBenefitTwoYear(BigDecimal benefitTwoYear) {
		this.benefitTwoYear = benefitTwoYear;
	}

	public void setBenefitThreeYear(BigDecimal benefitThreeYear) {
		this.benefitThreeYear = benefitThreeYear;
	}

	public void setBenefitFiveYear(BigDecimal benefitFiveYear) {
		this.benefitFiveYear = benefitFiveYear;
	}

	public void setBenefitTenYear(BigDecimal benefitTenYear) {
		this.benefitTenYear = benefitTenYear;
	}

	public void setBenefitFromBegin(BigDecimal benefitFromBegin) {
		this.benefitFromBegin = benefitFromBegin;
	}

	public void setYearBenefitFromBegin(BigDecimal yearBenefitFromBegin) {
		this.yearBenefitFromBegin = yearBenefitFromBegin;
	}


	public void setBenefitWorstSixMonth(BigDecimal benefitWorstSixMonth) {
		this.benefitWorstSixMonth = benefitWorstSixMonth;
	}

	public void setBenefitBestSixMonth(BigDecimal benefitBestSixMonth) {
		this.benefitBestSixMonth = benefitBestSixMonth;
	}


	public String getRankFromBegin() {
		return rankFromBegin;
	}


	public void setRankFromBegin(String rankFromBegin) {
		this.rankFromBegin = rankFromBegin;
	}


	public BigDecimal getMaPoint() {
		return maPoint;
	}


	public void setMaPoint(BigDecimal maPoint) {
		this.maPoint = maPoint;
	}

}
