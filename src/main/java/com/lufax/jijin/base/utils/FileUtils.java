package com.lufax.jijin.base.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;

import com.lufax.jijin.fundation.util.DahuaDecrptyUtils;

public class FileUtils {
    public static final String SEPERATOR = "|";
    public static final String LINECHANGE = "\n";
    private static int BUFFER_SIZE = 2048;

    public static File forceMkdir(String dirName) {
        return forceMkdir(new File(dirName));
    }

    public static File forceMkdir(File directory) {
        try {
            org.apache.commons.io.FileUtils.forceMkdir(directory);
            return directory;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static void forceDelete(File directory) {
        if(directory == null || !directory.exists()) {
            return;
        }
        try {
            org.apache.commons.io.FileUtils.forceDelete(directory);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * build YLX_OPEN_REQUEST file header
     * @param fileName
     * @param fileType
     * @param version
     * @param org
     * @param trxDate
     * @param total
     */
    public static void appendHeader(File fileName, String fileType,
                                    String version, String org, Date trxDate, long total,BigDecimal amount) {

        // first line
        StringBuilder sb = new StringBuilder();
        sb.append(fileType).append(SEPERATOR).append(version).append(LINECHANGE);

        // second line
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String target = format.format(trxDate);
        sb.append(org).append(SEPERATOR).append(target).append(SEPERATOR)
                .append(total);
        if(null!=amount){
            sb.append(SEPERATOR).append(String.format("%.2f", amount)).append(LINECHANGE);
        }else{
            sb.append(LINECHANGE);
        }


        Writer writer = null;
        try {
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            writer =  new OutputStreamWriter(new FileOutputStream(fileName,true), "GBK");
            writer.write(sb.toString());
        } catch (IOException e) {
            Logger.error(FileUtils.class, "write file"+fileName.getName()+" fail since IOException");
            throw new RuntimeException("write file"+fileName.getName()+" fail since IOException",e);
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                Logger.error(FileUtils.class, "close writeHandler fail since IOException");
                throw new RuntimeException("close writeHandler fail since IOException",e);
            }
        }

    }


    public static void appendContent(File fileName, String content) {

        Writer writer = null;
        try {
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            writer = new OutputStreamWriter(new FileOutputStream(fileName,true), "GBK");
            writer.write(content);
        } catch (IOException e) {
            Logger.error(FileUtils.class, "write file content fail since IOException");
            throw new RuntimeException("write file content fail since IOException",e);
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                Logger.error(FileUtils.class, "close writeHandler fail since IOException");
                throw new RuntimeException("close writeHandler fail since IOException",e);
            }
        }

    }

    public static void appendContent(File file, String content,String encode) {

        Writer writer = null;
        try {
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            writer = new OutputStreamWriter(new FileOutputStream(file,true), encode);
            writer.write(content);
        } catch (IOException e) {
            Logger.error(FileUtils.class, "write file content fail since IOException");
            throw new RuntimeException("write file content fail since IOException",e);
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                Logger.error(FileUtils.class, "close writeHandler fail since IOException");
                throw new RuntimeException("close writeHandler fail since IOException",e);
            }
        }

    }
    
    public static File createEmptyFile(String absoulteFilePathAndName){
    	Logger.info(FileUtils.class, "path:"+absoulteFilePathAndName);
        File requestFile = new File(absoulteFilePathAndName);

        if (!requestFile.getParentFile().exists()) {
            Logger.info(FileUtils.class, "Creating dir");
            if (!requestFile.getParentFile().mkdirs()) {
                Logger.error(FileUtils.class, "Create the des dir failed！");
                throw new RuntimeException("Create the des dir failed！");
            }
        }

        if(requestFile.exists())
        {
            requestFile.delete();
        }
        try {
            if(requestFile.createNewFile()){
                return requestFile;
            }
            else{
                Logger.error(FileUtils.class, "Create the des file failed！");
                throw new RuntimeException("Create the des file failed！");
            }
        } catch (IOException e) {
            Logger.error(FileUtils.class, "Create the des file failed！");
            throw new RuntimeException("Create the des file failed！",e);
        }
    	
    }

    public static File createEmptyFile(String fileName,String rootDir){
        //String rootDir = faAppProperties.getAnshuoBatchFilePushRootDir();
        String absoluteDir = rootDir + fileName;
        Logger.info(FileUtils.class, "path:"+absoluteDir);
        File requestFile = new File(absoluteDir);

        if (!requestFile.getParentFile().exists()) {
            Logger.info(FileUtils.class, "Creating dir");
            if (!requestFile.getParentFile().mkdirs()) {
                Logger.error(FileUtils.class, "Create the des dir failed！");
                throw new RuntimeException("Create the des dir failed！");
            }
        }

        if(requestFile.exists())
        {
            requestFile.delete();
        }
        try {
            if(requestFile.createNewFile()){
                return requestFile;
            }
            else{
                Logger.error(FileUtils.class, "Create the des file failed！");
                throw new RuntimeException("Create the des file failed！");
            }
        } catch (IOException e) {
            Logger.error(FileUtils.class, "Create the des file failed！");
            throw new RuntimeException("Create the des file failed！",e);
        }

    }

    public static void unzipFile(String zipFilePath) {
        File file = new File(zipFilePath);
        if (!file.exists()) {
            throw new RuntimeException("Given zip file is not exist. zipFilePath=" + zipFilePath);
        }

        String outFilePath = null;
        try {
            ZipFile zipFile = new ZipFile(file);
            Enumeration<? extends ZipEntry> enumeration = zipFile.entries();
            while (enumeration.hasMoreElements()) {
                ZipEntry zipEntry = enumeration.nextElement();
                if (zipEntry.isDirectory()) {
                    continue;//skip directory
                }
                outFilePath = file.getParentFile().getAbsolutePath() + "/" + zipEntry.getName();
                extractEntry(zipFile, zipEntry, outFilePath);
            }
            zipFile.close();
        } catch (Exception e) {
            throw new RuntimeException("unzip file failed.", e);
        }

    }

    private static void extractEntry(ZipFile zipFile, ZipEntry zipEntry, String targetFilePath) throws IOException {
        InputStream inputStream = zipFile.getInputStream(zipEntry);
        FileOutputStream fileOutputStream = null;
        try {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            fileOutputStream = new FileOutputStream(targetFilePath);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream, BUFFER_SIZE);
            int count;
            byte data[] = new byte[BUFFER_SIZE];
            while ((count = bufferedInputStream.read(data, 0, data.length)) != -1) {
                bufferedOutputStream.write(data, 0, count);
            }
            bufferedInputStream.close();
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
        } finally {
            closeStream(fileOutputStream);
            closeStream(inputStream);
        }
    }

    private static void closeStream(Closeable is) {
        if (is != null) {
            try {
                is.close();
            } catch (Exception e) {
            }
        }
    }
    
 
    /**
     * 
     * srcPathName: 压缩文件根目录
     * zipPahtName: zip文件目录及路径
     * 
     */
    public static void compress(String srcPathName, String zipPathName,String fileName) {
    	
    	File zipFile = new File(zipPathName);  

        File srcdir = new File(srcPathName);  
        if (!srcdir.exists())  
            throw new RuntimeException(srcPathName + "不存在！");  
          
        Project prj = new Project();  
        Zip zip = new Zip();  
        zip.setProject(prj);  
        zip.setDestFile(zipFile);  
        FileSet fileSet = new FileSet();  
        fileSet.setProject(prj);  
        fileSet.setDir(srcdir);
        fileSet.setIncludes(fileName);
        //fileSet.setIncludes("**/*.java"); 包括哪些文件或文件夹 eg:zip.setIncludes("*.java");  
        //fileSet.setExcludes(...); 排除哪些文件或文件夹  
        zip.addFileset(fileSet);  
          
        zip.execute();
    }  
    
    /**
     * 
     * zipPahtName: zip文件目录及路径
     * @throws IOException 
     * 
     */
    public static void encryptDahuaZip(String zipPathAndName,String desKey) throws IOException {

    	
        File inFile = new File(zipPathAndName);
        String targetZipName = zipPathAndName.replace(".tmp", "");
        File encinFile = new File(targetZipName);

        byte[] inbyte = DahuaDecrptyUtils.readFile(inFile);
        //加密文件in.zip
        byte[] enbytes = DahuaDecrptyUtils.encryptBy3DES(inbyte, DahuaDecrptyUtils.build3DesKey(desKey));
        //写到加密后的文件encin.zip
        DahuaDecrptyUtils.writeFile(enbytes, encinFile);
    	
    }  
    
    

    
    
    
}