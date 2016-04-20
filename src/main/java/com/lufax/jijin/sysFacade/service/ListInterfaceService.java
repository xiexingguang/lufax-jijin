package com.lufax.jijin.sysFacade.service;

import java.util.List;

import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.daixiao.gson.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lufax.jijin.base.utils.JsonHelper;
import com.lufax.jijin.fundation.dto.JijinInfoToListDTO;
import com.lufax.jijin.fundation.gson.BaseGson;
import com.lufax.jijin.product.dto.ProductDTO;
import com.lufax.jijin.sysFacade.caller.InterfaceCallObject;
import com.lufax.jijin.sysFacade.caller.ListInterfaceCaller;
import com.lufax.jijin.sysFacade.gson.ListProductGson;
import com.lufax.jijin.sysFacade.gson.result.ListCreateProductResultGson;
import com.lufax.jijin.sysFacade.gson.result.ListResultGson;

@Service
public class ListInterfaceService {
    @Autowired
    private ListInterfaceCaller listAppInterfaceCaller;

    private static final String LIST_CREATE_PRODUCT_URL = "product/generate";
    private static final String LIST_UPDATE_HF_PRODUCT_URL = "product/update-product-info-for-huifu-product";
    private static final String LIST_EXPIRE_PRODUCT_URL = "product/expire";
    private static final String LIST_CANCEL_PRODUCT_URL = "product/cancel";
    private static final String LIST_UPDATE_JIJIN_INFO_URL ="product/update-product-info";
    private static final String LIST_UPDATE_JIJIN_STATUS_URL ="/product-status/update";
    private static final String LIST_SEND_SUBJECT_URL ="product/fund-group/update";
    private static final String LIST_SEND_FEE_URL ="productFee/generate";

    public ListInterfaceService() {
    }

    public ListCreateProductResultGson createProduct(ListProductGson listProductGson) {
        InterfaceCallObject interfaceCallObject = new InterfaceCallObject()
                .withUrl(LIST_CREATE_PRODUCT_URL)
                .addFormValue("message", new Gson().toJson(listProductGson));
        return listAppInterfaceCaller.post(interfaceCallObject, ListCreateProductResultGson.class);
    }



    public ListResultGson cancel(ProductDTO product, String productStatusChangeRemark) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("productId", product.id());
        jsonObject.addProperty("remark", productStatusChangeRemark);

        JsonArray jsonArray = new JsonArray();
        jsonArray.add(jsonObject);

        InterfaceCallObject interfaceCallObject = new InterfaceCallObject()
                .withUrl(LIST_CANCEL_PRODUCT_URL)
                .addFormValue("message", jsonArray.toString());
        return listAppInterfaceCaller.post(interfaceCallObject, ListResultGson.class);
    }
    
    /**
     * 调用List-app接口新增基金信息(直销)
     * @param dto
     * @return
     */
    public ListCreateProductResultGson addJijinInfo(JijinInfoToListDTO dto){
        InterfaceCallObject interfaceCallObject = new InterfaceCallObject()
                .withUrl(LIST_CREATE_PRODUCT_URL)
                .addFormValue("message", JsonHelper.toJson(dto));
        return listAppInterfaceCaller.post(interfaceCallObject, ListCreateProductResultGson.class);
    }
    
    /**
     * 修改基金信息 指定字段
     * @param message
     * @return
     */
    public ListCreateProductResultGson updateJijinInfo(String message){
    	InterfaceCallObject interfaceCallObject = new InterfaceCallObject()
        .withUrl(LIST_UPDATE_JIJIN_INFO_URL)
        .addFormValue("message", message);
    		return listAppInterfaceCaller.post(interfaceCallObject, ListCreateProductResultGson.class);
    }
    /**
     * 创建供销基金
     * @param gson
     * @return
     */
    public ListCreateProductResultGson addDaixiaoJijinInfo(JijinProductGson gson){
    	InterfaceCallObject interfaceCallObject = new InterfaceCallObject()
        .withUrl(LIST_CREATE_PRODUCT_URL)
        .addFormValue("message", JsonHelper.toJson(gson));
        return listAppInterfaceCaller.post(interfaceCallObject, ListCreateProductResultGson.class);
    }

    /**
     * 修改基金产品状态
     * @param gson
     * @return
     */
    public BaseGson updateJijinStatus(JijinUpdateStatusGson gson){
    	InterfaceCallObject interfaceCallObject = new InterfaceCallObject()
        .withUrl(LIST_UPDATE_JIJIN_STATUS_URL)
        .addFormValue("message", JsonHelper.toJson(gson));
        return listAppInterfaceCaller.post(interfaceCallObject, BaseGson.class);
    }
    /**
     * 推送人气方案和精选主题
     * @param gsonList
     * @return
     */
    public BaseGson sendSubjectToProduct(List<JijinSubjectGson> gsonList){
    	InterfaceCallObject interfaceCallObject = new InterfaceCallObject()
        .withUrl(LIST_SEND_SUBJECT_URL)
        .addFormValue("message", JsonHelper.toJson(gsonList));
        return listAppInterfaceCaller.post(interfaceCallObject, BaseGson.class);
    }

    public BaseGson sendFeeAndDiscountToProduct(JijinProductFeeGson gson){
        InterfaceCallObject interfaceCallObject = new InterfaceCallObject()
                .withUrl(LIST_SEND_FEE_URL)
                .addFormValue("message", JsonHelper.toJson(gson));
        return listAppInterfaceCaller.post(interfaceCallObject, BaseGson.class);
    }
    
    /**
     * 更新基金产品信息 可维护字段都可以更新
     * @param product
     * @return
     */
    public BaseGson updateProduct(JijinUpdateGson product){
        Logger.info(this,String.format("call product/update-product-info parameter is [%s]",new Gson().toJson(product)));
        InterfaceCallObject interfaceCallObject = new InterfaceCallObject()
        .withUrl(LIST_UPDATE_JIJIN_INFO_URL)
        .addFormValue("message", JsonHelper.toJson(product));
        return listAppInterfaceCaller.post(interfaceCallObject, BaseGson.class);
    }
}
