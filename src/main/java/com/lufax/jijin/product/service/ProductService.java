package com.lufax.jijin.product.service;

import com.lufax.jijin.product.gson.ProductReturnResultGson;
import com.lufax.jijin.sysFacade.gson.ListProductGson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.dto.FundLoanRequestDTO;
import com.lufax.jijin.product.gson.ProductInputGson;
import com.lufax.jijin.sysFacade.caller.InterfaceCallObject;
import com.lufax.jijin.sysFacade.caller.ListInterfaceCaller;
import com.lufax.jijin.sysFacade.caller.ProdOprSvcInterfaceCaller;
import com.lufax.jijin.sysFacade.gson.result.ListCreateProductResultGson;

@Service
public class ProductService {
	private static final String LIST_CREATE_PRODUCT_URL = "product/generate";
	private static final String LIST_CLOSE_PRODUCT_URL = "product/close";
	private static final String LIST_CANCEL_PRODUCT_URL = "product/cancel";
    @Autowired
    private ProdOprSvcInterfaceCaller listAppInterfaceCaller;
    
    public boolean createProduct(ListProductGson listProductGson) {
        Logger.info(this, String.format("Start to create product for loan request(productCode=%s).", listProductGson.getCode()));

        try {
        	InterfaceCallObject interfaceCallObject = new InterfaceCallObject().withUrl(LIST_CREATE_PRODUCT_URL).addFormValue("message", new Gson().toJson(listProductGson));
        	ListCreateProductResultGson resultGson = listAppInterfaceCaller.post(interfaceCallObject, ListCreateProductResultGson.class);
            
        	if (null != resultGson && resultGson.isSuccess()) {
                Logger.info(this, String.format("Create product [id:%s] for Fund loanRequest [productCode:%s] successfully.", resultGson.getProductId(), listProductGson.getCode()));
                return true;
            } else {
                Logger.error(this, String.format("Create product for Fund loanRequest [productCode:%d] failed, NEED_MANUAL_HANDLE.", listProductGson.getCode()));
                return false;
            }
        } catch (Exception e) {
            Logger.error(this, String.format("Create product for Fund loan Request [productCode:%d] failed due to exception, NEED_MANUAL_HANDLE.", listProductGson.getCode()), e);
            return false;
        }
    }
    
    @Transactional
	public boolean updateProduct(long productId, boolean result) {
    	String url = LIST_CANCEL_PRODUCT_URL;
    	String remark = "募集失败";
		if(result){
			url = LIST_CLOSE_PRODUCT_URL;
			remark = "募集成功，进入封闭期";
		}
		InterfaceCallObject interfaceCallObject = new InterfaceCallObject().withUrl(url).addFormValue("message", "[{\"productId\":"+ productId +", \"remark\" :\""+ remark +"\"}]");
        ProductReturnResultGson resultGson = listAppInterfaceCaller.post(interfaceCallObject, ProductReturnResultGson.class);
		return resultGson.getResult();
	}
}
