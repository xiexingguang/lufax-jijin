package com.lufax.jijin.daixiao.utils;

import com.google.common.base.Splitter;
import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.daixiao.dto.JijinExCharacterDTO;
import com.lufax.jijin.daixiao.gson.JijinExCharacterGson;

import net.sf.cglib.beans.BeanCopier;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.velocity.texen.util.PropertiesUtil;
import org.junit.Ignore;
import org.junit.Test;
import org.unitils.thirdparty.org.apache.commons.io.IOUtils;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.List;

/**
 * Created by chenguang451 on 2016/1/5.
 */
public class BeanTest {

    @Test@Ignore
    public void testBean(){
        JijinExCharacterDTO dto = new JijinExCharacterDTO();
        dto.setBatchId(1L);
        dto.setFundCode("000001");
        dto.setIsForeign("1");
        dto.setIsHongkong("1");
        dto.setIsMacao("1");
        dto.setIsMtsRedeem("1");
        dto.setIsMtsSub("1");
        dto.setIsPlan1("1");
        dto.setIsPlan2("1");
        dto.setIsPlan3("1");
        dto.setIsPreRedeem("1");
        dto.setIsPreSub("1");
        dto.setIsRealRedeem("1");
        dto.setIsStmRedeem("1");
        dto.setIsStmSub("1");
        dto.setIsTaiwan("1");
        dto.setIsValid(1L);
        dto.setStatus("NEW");
        dto.setTaCode("test_ta");




        long t = System.currentTimeMillis();
        for(int i=0;i<10;i++) {
            JijinExCharacterGson t1 = new JijinExCharacterGson();
            t1.setBatchId(dto.getBatchId());
            t1.setFundCode(dto.getFundCode());
            t1.setIsForeign(dto.getIsForeign());
            t1.setIsHongkong(dto.getIsHongkong());
            t1.setIsMacao(dto.getIsMacao());
            t1.setIsMtsRedeem(dto.getIsMtsRedeem());
            t1.setIsMtsSub(dto.getIsMtsSub());
            t1.setIsPlan1(dto.getIsPlan1());
            t1.setIsPlan2(dto.getIsPlan2());
            t1.setIsPlan3(dto.getIsPlan3());
            t1.setIsPreRedeem(dto.getIsPreRedeem());
            t1.setIsPreSub(dto.getIsPreSub());
            t1.setIsRealRedeem(dto.getIsRealRedeem());
            t1.setIsStmRedeem(dto.getIsStmRedeem());
            t1.setIsStmSub(dto.getIsStmSub());
            t1.setIsTaiwan(dto.getIsTaiwan());
            t1.setIsValid(dto.getIsValid());
            t1.setStatus(dto.getStatus());
            t1.setTaCode(dto.getTaCode());
            System.out.println(t1);
        }
        System.out.println("Set use time " + (System.currentTimeMillis()-t) + " ms.");



        BeanCopier copier = BeanCopier.create(JijinExCharacterDTO.class, JijinExCharacterGson.class, false);
        t = System.currentTimeMillis();
        for(int i=0;i<10;i++) {
//            try {
//                JijinExCharacterGson t2 = new JijinExCharacterGson();
//                BeanUtils.copyProperties(t2,dto);
////                PropertyUtils.copyProperties(t2, dto);
////                System.out.println(t2);
//            } catch (IllegalAccessException e) {
//                Logger.warn(this, e.getMessage(), e);
//            } catch (InvocationTargetException e) {
//                Logger.warn(this, e.getMessage(), e);
//            }

            JijinExCharacterGson t2 = new JijinExCharacterGson();
//            org.springframework.beans.BeanUtils.copyProperties(t2,dto);
            copier.copy(dto, t2, null);
            System.out.println(t2);
        }
        System.out.println("Utils use time " + (System.currentTimeMillis()-t) + " ms.");

    }

    @Test@Ignore
    public void testReadFile(){
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource("wind_20160106094100_20.txt");
        if(url==null){
            InputStream inputStream = ClassLoader.getSystemResourceAsStream("wind_20160106094100_20.txt");
            System.out.println(inputStream==null);
        }
//        String fileName = url.getFile();
        String fileName = "D:\\workspace\\jijin-app\\src\\test\\resources\\importfiles\\daixiao\\wind_20160106094100_20.txt";
        System.out.println(fileName);
        InputStreamReader in = null;
        LineNumberReader reader = null;
        int count = 0;
        try{
            in = new InputStreamReader(new FileInputStream(fileName), "UTF-8");
            reader = new LineNumberReader(in);
            String firstLine = reader.readLine();
            List<String> detail = Splitter.on("|").splitToList(firstLine);
            long lineCount = Long.valueOf(detail.get(1));

            while(reader.readLine()!=null){
                count++;
            }


        }catch(IOException e){
            com.lufax.jersey.utils.Logger.warn(this, e.getMessage(), e);

        }finally{
            IOUtils.closeQuietly(reader);
            IOUtils.closeQuietly(in);
        }
        System.out.println(count);
    }
}
