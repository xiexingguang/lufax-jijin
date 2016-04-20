package com.lufax.jijin.fundation.util;


import org.apache.commons.io.IOUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;

public class DahuaDecrptyUtils {

    public static byte[] encryptBy3DES(byte[] plainText, byte[] byteKey) {
        try {
            SecretKeySpec key = new SecretKeySpec(byteKey, "DESede");
            Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(plainText);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;

    }


    public static byte[] decryptBy3DES(byte[] plainText, byte[] byteKey) {
        try {
            SecretKeySpec key = new SecretKeySpec(byteKey, "DESede");
            Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key);
            return cipher.doFinal(plainText);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    //测试方法
    public static void main(String[] args) throws IOException {
        File inFile = new File(System.getProperty("user.dir"), "in.zip");
        File encinFile = new File(System.getProperty("user.dir"), "encin.zip");
        File outFile = new File(System.getProperty("user.dir"), "out.zip");

        byte[] inbyte = readFile(inFile);
        //加密文件in.zip
        byte[] enbytes = encryptBy3DES(inbyte, build3DesKey("123"));
        //写到加密后的文件encin.zip
        writeFile(enbytes, encinFile);
        byte[] eninbytes = readFile(encinFile);
        //解密文件到out.zip
        byte[] plianbytes = decryptBy3DES(eninbytes, build3DesKey("123"));
        writeFile(plianbytes, outFile);
    }


    public static void writeFile(byte[] data, File file) throws IOException {
        IOUtils.write(data, new FileOutputStream(file));
    }

    public static byte[] readFile(File file) throws IOException {
        return IOUtils.toByteArray(new FileInputStream(file));
    }


    public static byte[] build3DesKey(String keyStr) {
        byte[] key = new byte[24]; // 声明一个24位的字节数组，默认里面都是0
        byte[] temp = null;
        try {
            temp = keyStr.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            keyStr.getBytes();
        } // 将字符串转成字节数组
           /*
           * 执行数组拷贝 System.arraycopy(源数组，从源数组哪里开始拷贝，目标数组，拷贝多少位)
           */
        if (key.length > temp.length) {
            // 如果temp不够24位，则拷贝temp数组整个长度的内容到key数组中
            System.arraycopy(temp, 0, key, 0, temp.length);
        } else {
            // 如果temp大于24位，则拷贝temp数组24个长度的内容到key数组中
            System.arraycopy(temp, 0, key, 0, key.length);
        }
        return key;
    }
}
