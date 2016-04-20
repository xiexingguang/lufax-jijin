package com.lufax.jijin.sysFacade.gson;

public class FrozenNo {
	

	private Long frozenNo;//	冻结号
	private String productCode;//支付相关引用信息，产品编码
	private String trxDate;//支付相关引用信息，交易日期
	private String referenceId;//业务参考id（具体值业务系统负责）
	private String referenceType;//支付相关引用信息，调用方标明
	private String useCase;//业务场景
	private String bizId;//业务id
	private String bizType;//业务类型
	
	public FrozenNo(Long frozenNo, String productCode, String trxDate,
			String referenceId, String referenceType, String useCase,
			String bizId, String bizType) {
		this.frozenNo = frozenNo;
		this.productCode = productCode;
		this.trxDate = trxDate;
		this.referenceId = referenceId;
		this.referenceType = referenceType;
		this.useCase = useCase;
		this.bizId = bizId;
		this.bizType = bizType;
	}
	
	public Long getFrozenNo() {
		return frozenNo;
	}
	public void setFrozenNo(Long frozenNo) {
		this.frozenNo = frozenNo;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getTrxDate() {
		return trxDate;
	}
	public void setTrxDate(String trxDate) {
		this.trxDate = trxDate;
	}
	public String getReferenceId() {
		return referenceId;
	}
	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}
	public String getReferenceType() {
		return referenceType;
	}
	public void setReferenceType(String referenceType) {
		this.referenceType = referenceType;
	}
	public String getUseCase() {
		return useCase;
	}
	public void setUseCase(String useCase) {
		this.useCase = useCase;
	}
	public String getBizId() {
		return bizId;
	}
	public void setBizId(String bizId) {
		this.bizId = bizId;
	}
	public String getBizType() {
		return bizType;
	}
	public void setBizType(String bizType) {
		this.bizType = bizType;
	}
	
}
