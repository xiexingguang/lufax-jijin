package com.lufax.jijin.base.utils;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Utils {

    public static byte[] digest(byte[] bytes) {
        try {
            return MessageDigest.getInstance("MD5").digest(bytes);
        } catch (NoSuchAlgorithmException e) {
            //shit happens
            return null;
        }
    }

    public static String digestToHex(String str) {
        return StringUtils.bytesToHex(digest(str.getBytes()));
    }
}
