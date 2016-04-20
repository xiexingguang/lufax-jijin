package com.lufax.jijin.fundation.service;

import java.io.File;
import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.lufax.jijin.base.utils.FileUtils;
import com.lufax.jijin.base.utils.JijinAppProperties;
import com.lufax.jijin.fundation.util.DahuaDecrptyUtils;
import com.lufax.kernel.security.kms.api.KmsService;

@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class DESTest extends AbstractTransactionalJUnit4SpringContextTests{
    @Autowired
    private KmsService kmsService;
    
    @Autowired
    private JijinAppProperties jijinAppProperties;
	
	@Test@Ignore
	public void testGet3DESKey() throws IOException {
		
        String base64key = kmsService.getRawKey(jijinAppProperties.getDahua3des());
		System.out.println("3desKey:"+base64key);
		
		genZip("/nfsc/sftp_user/pafsftp/dh103/upload/20150930/dh103_20150930_25.txt");
		
        File inFile = new File("/nfsc/sftp_user/pafsftp/dh103/upload/20150930/dh103_20150930_25.zip");
        File encinFile = new File("/nfsc/sftp_user/pafsftp/dh103/upload/20150930/dh103_20150930_25_enc.zip");
        File outFile = new File(System.getProperty("user.dir"), "out.zip");

        byte[] inbyte = DahuaDecrptyUtils.readFile(inFile);
        //加密文件in.zip
        byte[] enbytes = DahuaDecrptyUtils.encryptBy3DES(inbyte, DahuaDecrptyUtils.build3DesKey(base64key));
        //写到加密后的文件encin.zip
        DahuaDecrptyUtils.writeFile(enbytes, encinFile);
        byte[] eninbytes = DahuaDecrptyUtils.readFile(encinFile);
//        //解密文件到out.zip
//        byte[] plianbytes = DahuaDecrptyUtils.decryptBy3DES(eninbytes, DahuaDecrptyUtils.build3DesKey("123"));
//        DahuaDecrptyUtils.writeFile(plianbytes, outFile);
		
	}
	
	private void genZip(String fileName){
    	String[] token = fileName.split("/");
    	String sourcefileName = token[7];
		String srcPathName = fileName.replace("/"+sourcefileName, "");
		String zipPathName = fileName.replace(".txt", ".tmp.zip");// "/nfsc/sftp_user/pafsftp/dh103/upload/20150930/dh103_20150930_25.txt";

		FileUtils.compress(srcPathName, zipPathName, sourcefileName);
		
	}



}
