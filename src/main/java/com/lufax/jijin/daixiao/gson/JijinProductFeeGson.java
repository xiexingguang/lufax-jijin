package com.lufax.jijin.daixiao.gson;

import java.util.ArrayList;
import java.util.List;

/**
 * 费率与折扣信息 
 * @author chenqunhui
 *
 */
public class JijinProductFeeGson {

	private Long productId;
	
	private List<JijinExFeeGson> feeList = new ArrayList<JijinExFeeGson>();
	
	private List<JijinExDiscountGson> discountList = new ArrayList<JijinExDiscountGson>();

	public List<JijinExFeeGson> getFeeList() {
		return feeList;
	}

	public void setFeeList(List<JijinExFeeGson> feeList) {
		this.feeList = feeList;
	}

	public List<JijinExDiscountGson> getDiscountList() {
		return discountList;
	}

	public void setDiscountList(List<JijinExDiscountGson> discountList) {
		this.discountList = discountList;
	}


	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}
}
