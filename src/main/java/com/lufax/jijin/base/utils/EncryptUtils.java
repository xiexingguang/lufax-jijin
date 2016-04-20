package com.lufax.jijin.base.utils;


import org.apache.commons.lang.StringUtils;

public class EncryptUtils {
    private static final int DEFAULT_CARD_NUMBER_DISPLAY_LENGTH = 4;

    public static String encryptBankCardNo(String cardNo) {
        if (cardNo == null || cardNo.length() <= DEFAULT_CARD_NUMBER_DISPLAY_LENGTH) {
            return cardNo;
        }

        String preStr = cardNo.substring(DEFAULT_CARD_NUMBER_DISPLAY_LENGTH, cardNo.length() - DEFAULT_CARD_NUMBER_DISPLAY_LENGTH);
        preStr = preStr.replaceAll(".", "*");
        return cardNo.substring(0, DEFAULT_CARD_NUMBER_DISPLAY_LENGTH).concat(preStr).concat(cardNo.substring(cardNo.length() - DEFAULT_CARD_NUMBER_DISPLAY_LENGTH));
    }

    public static String encryptBankCardNoLast4Bits(String cardNo) {
        if (cardNo == null || cardNo.length() <= DEFAULT_CARD_NUMBER_DISPLAY_LENGTH) {
            return cardNo;
        }

        return cardNo.substring(cardNo.length() - DEFAULT_CARD_NUMBER_DISPLAY_LENGTH);
    }

    public static String encryptIdentityNumber(String identityNumber) {
        if (StringUtils.isBlank(identityNumber)) {
            return identityNumber;
        }

        if (identityNumber.length() == 18) {
            return identityNumber.replaceAll(".{6}(\\d{8}).{4}", "******$1****");
        } else {
            return identityNumber.replaceAll(".{6}(\\d{6}).{3}", "******$1***");
        }
    }

    public static String encryptLastFourIdentityNumber(String identityNumber) {
        if (StringUtils.isBlank(identityNumber)) {
            return identityNumber;
        }
        if (identityNumber.length() == 18) {
            return identityNumber.replaceAll(".{14}(.{4})", "**************$1");
        } else {
            return identityNumber.replaceAll(".{11}(.{4})", "***********$1");
        }
    }

    public static String encryptMobileNumber(String mobileNumber) {
        if (StringUtils.isBlank(mobileNumber)) {
            return mobileNumber;
        }

        return mobileNumber.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }

    public static String encryptUserRealName(String userRealName) {
        if (StringUtils.isBlank(userRealName)) {
            return userRealName;
        } else {
            return userRealName.substring(0, 1) + "**";
        }
    }

    public static String encryptIdNumShowFirst1AndLast4(String idNo){
        if (org.apache.commons.lang.StringUtils.isBlank(idNo) || idNo.length()<=5){
            return idNo;
        }

        String hideStr = idNo.substring(1, idNo.length()-4);
        hideStr = hideStr.replaceAll(".", "*");
        return idNo.substring(0,1).concat(hideStr).concat(idNo.substring(idNo.length()-4, idNo.length()));
    }

    public static String encryptIdNumShowLast4(String idNo){
        if (org.apache.commons.lang.StringUtils.isBlank(idNo) || idNo.length()<=5){
            return idNo;
        }

        String hideStr = idNo.substring(0, idNo.length()-4);
        hideStr = hideStr.replaceAll(".", "*");
        return hideStr.concat(idNo.substring(idNo.length()-4, idNo.length()));
    }
    public static String encryptEmailShowFirst1AndLast1(String email){
        if (org.apache.commons.lang.StringUtils.isBlank(email) || !email.contains("@")){
            return email;
        }

        String emailBeforeAt = email.substring(0, email.indexOf("@"));
        String emailAfterAt = email.substring(email.indexOf("@"));
        if (org.apache.commons.lang.StringUtils.isBlank(emailBeforeAt) || emailBeforeAt.length()<2){
            return email;
        }

        return emailBeforeAt.substring(0,1).concat("***").concat(emailBeforeAt.substring(emailBeforeAt.length()-1)).concat(emailAfterAt);
    }

}
