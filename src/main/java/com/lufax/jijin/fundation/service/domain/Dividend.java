package com.lufax.jijin.fundation.service.domain;

import java.math.BigDecimal;
import java.util.Map;
import java.util.NoSuchElementException;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import com.google.common.collect.Maps;

public class Dividend {
	
	public enum Type{
		SWITCH_DIVIDEND("0"),
		CASH("1");
		
		private final String name;
		
        private static final Map<String, Type> map = Maps.newHashMap();
		
		static {
			for(Type type : Type.values()){
				map.put(type.name, type);
			}
		}
		
		Type(String type){
			this.name = type;
		}
		public String getName() {
			return name;
		}
		
		public static Type fromString(String value){
			if(map.containsKey(value)){
				return map.get(value);
			}
			throw new NoSuchElementException(value + " not found");
		}
	}
	
	private long id;
	
	private long payId;
	
	private long userId;
    /* 基金公司分红流水号 */
    private String appSheetNo;
    private String fundCode;
    /* 默认分红方式 0-红利转投 1-现金分红 */
    private Type type;
    /* 权益日期yyyymmdd */
    private String rightDate;
    /* 交易确认日期yyyymmdd */
    private String trxDate;
    /* 分红发放日yyyymmdd */
    private String dividendDate;
    /* 收费方式 a-前收费 b-后收费 */
    private String chargeType;
    /* 转投份额 */
    private BigDecimal dividendShare;
    /* 分红金额 */
    private BigDecimal dividendAmount;
    /* 手续费 */
    private BigDecimal fee;
    /* 分红类型 0 普通分红，1 质押基金分红，2 货币基金收益结转，3 保本基金赔付 */
    private String dividendMode;
	/**预计到账日期*/
    private String expectAccountDate;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getAppSheetNo() {
		return appSheetNo;
	}
	public void setAppSheetNo(String appSheetNo) {
		this.appSheetNo = appSheetNo;
	}
	public String getFundCode() {
		return fundCode;
	}
	public void setFundCode(String fundCode) {
		this.fundCode = fundCode;
	}
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	public String getRightDate() {
		return rightDate;
	}
	public void setRightDate(String rightDate) {
		this.rightDate = rightDate;
	}
	public String getTrxDate() {
		return trxDate;
	}
	public void setTrxDate(String trxDate) {
		this.trxDate = trxDate;
	}
	public String getDividendDate() {
		return dividendDate;
	}
	public void setDividendDate(String dividendDate) {
		this.dividendDate = dividendDate;
	}
	public String getChargeType() {
		return chargeType;
	}
	public void setChargeType(String chargeType) {
		this.chargeType = chargeType;
	}
	public BigDecimal getDividendShare() {
		return dividendShare;
	}
	public void setDividendShare(BigDecimal dividendShare) {
		this.dividendShare = dividendShare;
	}
	public BigDecimal getDividendAmount() {
		return dividendAmount;
	}
	public void setDividendAmount(BigDecimal dividendAmount) {
		this.dividendAmount = dividendAmount;
	}
	public BigDecimal getFee() {
		return fee;
	}
	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}
	public String getDividendMode() {
		return dividendMode;
	}
	public void setDividendMode(String dividendMode) {
		this.dividendMode = dividendMode;
	}

	public long getPayId() {
		return payId;
	}
	public void setPayId(long payId) {
		this.payId = payId;
	}
	public String getExpectAccountDate() {
		return expectAccountDate;
	}

	public void setExpectAccountDate(String expectAccountDate) {
		this.expectAccountDate = expectAccountDate;
	}
	@Override
	public String toString(){
		return ReflectionToStringBuilder.toString(this);
	}
}
