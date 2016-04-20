/**
 *
 */
package com.lufax.jijin.fundation.constant;

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * @author chenguang E-mail:chenguang451@pingan.com.cn
 * @version create time:May 25, 2015 7:57:05 PM
 */
public enum FundSaleCode {

    YFD("yfd101", "211"),
    HTF("htf102", "247"),
    LFX("lfx201", "376"),
    DHC("dh103", "270A");

    private String instId;
    private String saleCode;

    private static Map<String, String> saleCodes = Maps.newHashMap();

    private FundSaleCode(String instId, String saleCode) {
        this.instId = instId;
        this.saleCode = saleCode;
    }

    public String getInstId() {
        return instId;
    }

    public String getSaleCode() {
        return saleCode;
    }

    static Map<String, String> create() {
        saleCodes.put(YFD.instId, YFD.saleCode);
        saleCodes.put(HTF.instId, HTF.saleCode);
        saleCodes.put(LFX.instId, LFX.saleCode);
        saleCodes.put(DHC.instId, DHC.saleCode);
        return saleCodes;
    }

    public static Map<String, String> getSaleCodes() {
        if (saleCodes.isEmpty()) {
            create();
        }
        return saleCodes;
    }
}
