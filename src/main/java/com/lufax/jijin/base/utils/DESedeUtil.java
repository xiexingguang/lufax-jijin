package com.lufax.jijin.base.utils;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import java.security.Key;


public class DESedeUtil {

    private static final String KEY_ALGORITHM = "DESede";
    private static final String CIPHER_ALGORITHM = "DESede/ECB/PKCS5Padding";

    public static String encryptToBase64(String content, String keyStr) throws Exception {
        return encryptToBase64(content, keyStr, ConstantsHelper.DEFAULT_CHARSET);
    }

    public static String decryptFromBase64(String content, String keyStr) throws Exception {
        return decryptFromBase64(content, keyStr, ConstantsHelper.DEFAULT_CHARSET);
    }

    public static String encryptToBase64(String content, String keyStr, String charset) throws Exception {
        return Base64.encodeBase64String(encrypt(content.getBytes(charset), keyStr.getBytes(charset)));
    }

    public static String decryptFromBase64(String content, String keyStr, String charset) throws Exception {
        return new String(decrypt(Base64.decodeBase64(content), keyStr.getBytes(charset)), charset);
    }

    private static Key toKey(byte[] keyStr) throws Exception {
        DESedeKeySpec dks = new DESedeKeySpec(keyStr);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_ALGORITHM);
        SecretKey secretKey = keyFactory.generateSecret(dks);
        return secretKey;
    }

    private static byte[] encrypt(byte[] content, byte[] keyStr) throws Exception {
        Key key = toKey(keyStr);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(content);
    }

    private static byte[] decrypt(byte[] content, byte[] keyStr) throws Exception {
        Key key = toKey(keyStr);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(content);
    }

}