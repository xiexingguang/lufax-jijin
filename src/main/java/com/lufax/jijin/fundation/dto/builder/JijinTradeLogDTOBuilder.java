/**
 * 
 */
package com.lufax.jijin.fundation.dto.builder;

import java.math.BigDecimal;

import com.lufax.jijin.fundation.constant.TradeRecordType;
import com.lufax.jijin.fundation.dto.JijinTradeLogDTO;

/**
 * @author chenguang E-mail:chenguang451@pingan.com.cn
 * @version create time:May 15, 2015 6:20:06 PM
 * 
 */
public class JijinTradeLogDTOBuilder{
	
	private Long userId;
    private String fundCode;
    private String trxTime;
    private String trxDate;
    private Long tradeRecordId = 0l;
    private String status;
    private String dividendType;
    private TradeRecordType type;
    private BigDecimal amount;
    private BigDecimal reqShare;
    private String trxSeria;
    private BigDecimal fee;
	private String confirmDate;
	private String expectAccountDate;
	private String accountDate;//到账日期
	
	public JijinTradeLogDTOBuilder addUserId(long userId){
		this.userId = userId;
		return this;
	}
	
	public JijinTradeLogDTOBuilder addFundCode(String fundCode){
		this.fundCode = fundCode;
		return this;
	}
	
	public JijinTradeLogDTOBuilder addTrxTime(String trxTime){
		this.trxTime = trxTime;
		return this;
	}
   	public JijinTradeLogDTOBuilder addTrxDate(String trxDate){
		this.trxDate = trxDate;
		return this;
	}
	
	public JijinTradeLogDTOBuilder addTradeRecordId(long tradeRecordId){
		this.tradeRecordId = tradeRecordId;
		return this;
	}
	
	public JijinTradeLogDTOBuilder addStatus(String status){
		this.status = status;
		return this;
	}
   	public JijinTradeLogDTOBuilder addDividendType(String dividendType){
		this.dividendType = dividendType;
		return this;
	}
	
	public JijinTradeLogDTOBuilder addType(TradeRecordType type){
		this.type = type;
		return this;
	}
	
	public JijinTradeLogDTOBuilder addAmount(BigDecimal amount){
		this.amount = amount;
		return this;
	}
	
	public JijinTradeLogDTOBuilder addReqShare(BigDecimal reqShare){
		this.reqShare = reqShare;
		return this;
	}
	public JijinTradeLogDTOBuilder addTrxSeria(String trxSeria){
		this.trxSeria = trxSeria;
		return this;
	}
	public JijinTradeLogDTOBuilder addFee(BigDecimal fee){
		this.fee = fee;
		return this;
	}
	public JijinTradeLogDTOBuilder addConfirmDate(String confirmDate){
		this.confirmDate = confirmDate;
		return this;
	}
	public JijinTradeLogDTOBuilder addExpectAccountDate(String expectAccountDate){
		this.expectAccountDate = expectAccountDate;
		return this;
	}
	public JijinTradeLogDTOBuilder addAccountDate(String accountDate){
		this.accountDate = accountDate;
		return this;
	}
	public JijinTradeLogDTO build(){
		JijinTradeLogDTO log = new JijinTradeLogDTO();
		log.setAmount(this.amount);
		log.setDividendType(this.dividendType);
		log.setFundCode(this.fundCode);
		log.setReqShare(this.reqShare);
		log.setStatus(this.status);
		log.setTradeRecordId(this.tradeRecordId);
		log.setTrxDate(this.trxDate);
		log.setTrxTime(this.trxTime);
		log.setType(this.type);
		log.setUserId(this.userId);
		log.setTrxSerial(this.trxSeria);
		log.setFee(this.fee);
		log.setConfirmDate(this.confirmDate);
		log.setExpectAccountDate(this.expectAccountDate);
		log.setAccountDate(this.accountDate);
		return log;
	}
}
