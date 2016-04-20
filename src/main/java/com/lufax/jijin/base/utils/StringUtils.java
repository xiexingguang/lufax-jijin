package com.lufax.jijin.base.utils;

public class StringUtils {

    public final static String EMPTY = "";

    public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(str.charAt(i)) == false)) {
                return false;
            }
        }
        return true;

    }

    public static boolean isNotBlank(String str) {
        return !StringUtils.isBlank(str);
    }

    public static String bytesToHex(byte[] digest) {
        StringBuffer buffer = new StringBuffer("");
        for (int i = 0; i < digest.length; i++) {
            String hexValue = Integer.toHexString(digest[i] & 0xFF);
            if (hexValue.length() == 1) {
                buffer.append(0);
            }
            buffer.append(hexValue);
        }
        return buffer.toString();
    }

    public static String emptyString() {
        return "";
    }

    public static String toString(Object obj) {
        if (obj == null) {
            return EMPTY;
        } else {
            return String.valueOf(obj);
        }
    }
}
