package com.lufax.jijin.product.constant;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;


public enum ProductCategory {

    ANYIDAI("101", "安E贷"),
    JIN_YING_TONG("201", "金盈通"),
    CAI_HONG("203", "彩虹"),
    ANXINDAI("204", "安鑫贷"),
    ANYIDAI_II("901", "安E贷II"),
    ANYEDAI("903", "安业贷"),
    HUIFU("301", "汇富"),
    YL("401", "养老险"),
    BAI_LING_DAI("902", ""),
    BILL("501", "票据"),
    ZJRS("601", "珠江人寿"),
    SLP("402", "养老险实利派"),
    JIJIN("802", "基金");
    private String code;
    private String desc;

    private ProductCategory(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public static ProductCategory convert(String productCategory) {
        for (ProductCategory category : ProductCategory.values()) {
            if (category.code.equalsIgnoreCase(productCategory)) return category;
        }
        return null;
    }

    public static List<String> getProductCategoryValues(List<ProductCategory> productCategories) {
        List<String> productCategoryValues = new ArrayList<String>();
        for (ProductCategory productCategory : productCategories) {
            productCategoryValues.add(productCategory.code);
        }
        return productCategoryValues;
    }

    public static List<ProductCategory> getAllProductCategories() {
        return asList(ProductCategory.values());
    }

    public static boolean isCaiHong(String category) {
        return ProductCategory.CAI_HONG.code.equals(category);
    }

    public static boolean isAnXinDai(String category) {
        return ProductCategory.ANXINDAI.code.equals(category);
    }

    public static boolean isAnYeDai(String category) {
        return ProductCategory.ANYEDAI.code.equals(category);
    }

    public static boolean isBill(String category) {
        return ProductCategory.BILL.code.equals(category);
    }

    public static boolean isYL(String category) {
        return ProductCategory.YL.code.equals(category);
    }

    public static boolean isJijin(String category) {
        return ProductCategory.JIJIN.code.equals(category);
    }
}
