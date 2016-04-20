package com.lufax.jijin.base.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
public class JijinInstIdPlatMerchantIdMapHolder {

    @Autowired
    private JijinAppProperties jijinAppProperties;

    //key:instId value:platMerchantId
    public Map<String, String> jijinInstIdAndPlatMerchantIdMap = new HashMap<String, String>();

    //key:platMerchantId value:instId
    public Map<String, String> jijinPlatMerchantIdAndInstIdMap = new HashMap<String, String>();
    
    public Map<String, String> jijinInstIdAndFundSaleCodeMap = new HashMap<String, String>();


    public Map<String, String> getJijinInstIdAndPlatMerchantIdMap() {
        return jijinInstIdAndPlatMerchantIdMap;
    }

    /**
     * @param instId 基金公司机构代码
     * @return
     */
    public String getPlatMerchantId(String instId) {
        return jijinInstIdAndPlatMerchantIdMap.get(instId);
    }

    public String getFundSaleCode(String instId) {
        return jijinInstIdAndFundSaleCodeMap.get(instId);
    }
    
    public String getInstId(String platMerchantId) {
        return jijinPlatMerchantIdAndInstIdMap.get(platMerchantId);
    }

    @PostConstruct
    public void buildMapping() throws Exception {
        if ("test".equals(jijinAppProperties.getEnvironment())) {
            jijinInstIdAndPlatMerchantIdMap.put("yfd101", "900000044504"); //900000028684
            jijinInstIdAndPlatMerchantIdMap.put("htf102", "900000044709");//900000033486
            jijinInstIdAndPlatMerchantIdMap.put("lfx201", "900000042922");
            jijinInstIdAndPlatMerchantIdMap.put("dh103", "900000050960");  //900000049501

            jijinInstIdAndPlatMerchantIdMap.put("lufax", "900000000006");
            jijinInstIdAndFundSaleCodeMap.put("yfd101", "211");
            jijinInstIdAndFundSaleCodeMap.put("htf102", "247");
            jijinInstIdAndFundSaleCodeMap.put("lfx201", "486");
            jijinInstIdAndFundSaleCodeMap.put("dh103", "270A");

            jijinPlatMerchantIdAndInstIdMap.put("900000044504", "yfd101");
            jijinPlatMerchantIdAndInstIdMap.put("900000044709", "htf102");
            jijinPlatMerchantIdAndInstIdMap.put("900000042922", "lfx201");
            jijinPlatMerchantIdAndInstIdMap.put("900000050960", "dh103"); 

            
        } else {
            jijinInstIdAndPlatMerchantIdMap.put("yfd101", "900000014935");
            jijinInstIdAndPlatMerchantIdMap.put("htf102", "900000014934");
            jijinInstIdAndPlatMerchantIdMap.put("lfx201", "900000015220");
            jijinInstIdAndPlatMerchantIdMap.put("lufax", "900000014797");
            jijinInstIdAndPlatMerchantIdMap.put("dh103", "900000015858");
            jijinInstIdAndFundSaleCodeMap.put("yfd101", "211");
            jijinInstIdAndFundSaleCodeMap.put("htf102", "247");
            jijinInstIdAndFundSaleCodeMap.put("lfx201", "376");
            jijinInstIdAndFundSaleCodeMap.put("dh103", "270A");

            jijinPlatMerchantIdAndInstIdMap.put("900000014935", "yfd101");
            jijinPlatMerchantIdAndInstIdMap.put("900000014934", "htf102");
            jijinPlatMerchantIdAndInstIdMap.put("900000015220", "lfx201");
            jijinPlatMerchantIdAndInstIdMap.put("900000015858", "dh103");
        }
    }
}