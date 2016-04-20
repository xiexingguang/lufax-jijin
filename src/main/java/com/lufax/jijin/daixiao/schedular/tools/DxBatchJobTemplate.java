/**
 * 
 */
package com.lufax.jijin.daixiao.schedular.tools;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lufax.jersey.utils.Logger;
import com.lufax.jijin.base.utils.JijinAppProperties;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.daixiao.constant.DaiXiaoFileTypeEnum;
import com.lufax.jijin.fundation.constant.SyncFileBizType;
import com.lufax.jijin.fundation.constant.SyncFileStatus;
import com.lufax.jijin.fundation.dto.JijinSyncFileDTO;
import com.lufax.jijin.fundation.repository.JijinSyncFileRepository;
import com.lufax.jijin.fundation.schedular.SyncFileLineFilter;
import com.lufax.jijin.fundation.schedular.SyncFileLineProcessor;
import com.lufax.jijin.fundation.schedular.SyncFileRepository;
import com.lufax.jijin.fundation.util.ApplicationContextUtils;
import org.apache.avro.Schema;
import org.apache.avro.Schema.Field;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unitils.thirdparty.org.apache.commons.io.IOUtils;

import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * The template will read lines from file and save them into DB directly.
 * @author chenguang E-mail:chenguang451@pingan.com.cn
 * @version create time:May 12, 2015 8:33:26 PM
 * 
 */
@Service
public class DxBatchJobTemplate {
	
	@Autowired
    private JijinAppProperties jijinAppProperties;
	
	@Autowired
	private JijinSyncFileRepository jijinSyncFielRepository;

	private int fileBatchAmount = 1000;
	private int lineBatchAmount = 200;
	private int processLimit = 1000;
	
	public int execute(String schemaName){
		return execute(schemaName, null, null, processLimit);
	}
	
	public int execute(String schemaName, int processLimit){
		return execute(schemaName, null, null, processLimit);
	}
	
	public int execute(String schemaName, SyncFileLineProcessor processer){
		return execute(schemaName, null, processer, processLimit);
	}
	
	public int execute(String schemaName, SyncFileLineFilter filter){
		return doExecute(schemaName, filter, null, processLimit);
	}
	
	public int execute(String schemaName, SyncFileLineFilter filter, SyncFileLineProcessor processer, int processLimit){
		return doExecute(schemaName, filter, processer, processLimit);
	}
	
	
	private int doExecute(String schemaName, SyncFileLineFilter filter, SyncFileLineProcessor processer, int processLimit){
		Logger.info(this, "start the job " + schemaName);
		if(processLimit<=0){
			processLimit = this.processLimit;
		}
		int fileCount = 0;
		int failCount = 0;
		Schema schema = null;
		try {
			schema = getSchema(schemaName);
		} catch (IOException e) {
			Logger.warn(this, "Can't get schema file " + schemaName, e);
			return 0;
		}
        List<JijinSyncFileDTO> dataList = fetchFileList(fileBatchAmount, schema);
        do{
        	for(JijinSyncFileDTO fileTask : dataList){
        		try {
                    Logger.info(this, String.format("Start to read file :[id: %s] [fileName : %s]", fileTask.getId(), fileTask.getFileName()));
            		dealFile(schemaName, fileTask, lineBatchAmount, filter, processer);
                    jijinSyncFielRepository.updateBusJijinSyncFileStatus(fileTask.getId(), SyncFileStatus.READ_SUCCESS, null);
                    Logger.info(this, String.format("Success read file :[id: %s] [fileName : %s]", fileTask.getId(), fileTask.getFileName()));
                    fileCount++;
                    if(fileCount>processLimit){
                    	Logger.info(this, String.format("Has process %d files, up to the limit %d, stop the job.", fileCount, processLimit));
                    }
                } catch (Exception e) {
                	Logger.warn(this, e.getMessage(), e);
                    Logger.error(this, String.format("Read file :[id: %s] [fileName : %s] failed !", fileTask.getId(), fileTask.getFileName()));
                    jijinSyncFielRepository.updateBusJijinSyncFileStatus(fileTask.getId(), SyncFileStatus.READ_FAIL, e.getMessage());
                    failCount++;
                }
        	}
        }while(dataList.size()>=fileBatchAmount);
        Logger.info(this, "This time total read " + (failCount+fileCount) + " files, success " + fileCount + ", fail " + failCount);
        return fileCount;
	}

	private List<JijinSyncFileDTO> fetchFileList(int batchAmount, Schema schema) {
		return jijinSyncFielRepository.findBusJijinSyncFileList(MapUtils.
				buildKeyValueMap("bizType", DaiXiaoFileTypeEnum.valueOf(schema.getName()).getTypeName(),
						"status", SyncFileStatus.READY,
						"limit", batchAmount));
	}
	
	
	private void dealFile(String schemaName, JijinSyncFileDTO fileTask, int batchSize, 
			SyncFileLineFilter filter, SyncFileLineProcessor processer) 
			throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException{
		Logger.info(this, "Load " + schemaName + " schema file...");
		ClassLoader loader = Thread.currentThread().getContextClassLoader(); 
	    URL url = loader.getResource(schemaName);   
		Schema schema = new Schema.Parser().parse(new File(url.getFile()));
		String target = schema.getProp("target");
		String targetRepo = schema.getProp("repository");
		
		Logger.info(this, "Create the " + schemaName + " class...");
		Class targetClass = Class.forName(target);
		java.lang.reflect.Field[] fields = targetClass.getDeclaredFields();
		Map<String, java.lang.reflect.Field> targetFields = Maps.newHashMap();
		for(java.lang.reflect.Field f : fields){
			targetFields.put(f.getName(), f);
		}
		
		long startLine = fileTask.getCurrentLine()==null?0:fileTask.getCurrentLine().longValue();
		String fileName = fileTask.getFileName();
		File sourceFile = new File(fileName);
		
		InputStreamReader in = null;
        LineNumberReader reader = null;
        
		String line = null;
		List dtos = Lists.newArrayList();
		int titleLineNumber = Integer.valueOf(schema.getProp("titlelinenumber"));
		String title = schema.getProp("title");
		if (validLineNumber(sourceFile, titleLineNumber)) {
			try {
				in = new InputStreamReader(new FileInputStream(sourceFile),
						"UTF-8");
				reader = new LineNumberReader(in);
				int addCount = 0;
				while ((line = reader.readLine()) != null) {
					int lineNumber = reader.getLineNumber();
					if (lineNumber == 1) {
						// skip the summary line
						continue;
					} else if (titleLineNumber>0 && lineNumber == titleLineNumber) {
						if(!validTitle(line, title)){
							throw new IOException(String.format("Sync file title line is not valid, should be %s, but is %s", title, line));
						}
					} else if(lineNumber>=startLine){
						Object instance = transferToObject(line, targetClass,
								schema, targetFields, fileTask);
						if (instance == null) {
							continue;
						}

						// filter some lines
						if (filter != null && filter.shouldFiltered(instance,fileTask)) {
							Logger.info(this, String.format("Read sync file which id = [%s] and name =[%s], skip lineNumber[%s]", fileTask.getId(),fileTask.getFileName(),lineNumber));
							continue;
						}
						
						dtos.add(instance);
						if(processer!=null) {
							processer.setPropertiesBeforeInsertToDB(instance);
						}

						if (dtos.size() >= batchSize) {
							Logger.info(this,String.format(
											"read jijin dispatch file - insert into DB endline:%s]",
											reader.getLineNumber()));							
							batchInsertSyncAndUpdateSyncFile(targetRepo, dtos,
									reader.getLineNumber(), fileTask);
							addCount += dtos.size();
							for(Object dto : dtos){
								if(processer!=null){
									processer.processLine(dto);
								}
							}
							dtos.clear();
						}
					}else{
						//previous processed line skip it
						Logger.info(this, "Skip the processed liens " + lineNumber);
					}
				}

				if (dtos.size() > 0) {
					// the left dtos to do
					Logger.info(this, String.format(
									"read jijin dispatch file  - insert last batch records into DB endline:%s]",
									reader.getLineNumber() - 1));
					batchInsertSyncAndUpdateSyncFile(targetRepo, dtos,
							reader.getLineNumber(), fileTask);
					addCount += dtos.size();
					for(Object dto : dtos){
						if(processer!=null){
							processer.processLine(dto);
						}
					}
					dtos.clear();
				}else{
					//the end of the file, update the start line
					jijinSyncFielRepository.updateBusJijinSyncFile(MapUtils.buildKeyValueMap("id", fileTask.getId(), "currentLine", reader.getLineNumber()));	
					Logger.info(this, "update the processed lien number to " + reader.getLineNumber());
				}
				Logger.info(this, "Total add " + addCount + " items from file " + fileTask.getFileName());
			} finally {
				IOUtils.closeQuietly(reader);
				IOUtils.closeQuietly(in);
			}
		} else {
			throw new IOException("Sync file total lines number is not valid.");
		}
	}
	
	private boolean validTitle(String line, String title) {
		Logger.info(this, "Validate the file title:" + title);
		return line.trim().equalsIgnoreCase(title.trim());
	}

	private Object transferToObject(String line, Class targetClass,
			Schema schema, Map<String, java.lang.reflect.Field> targetFields, 
			JijinSyncFileDTO fileTask) 
					throws InstantiationException, IllegalAccessException{
    	List<String> values = Splitter.on("|").splitToList(line);
    	Object instance = targetClass.newInstance();         	
		for(Field field : schema.getFields()){
			int index = Integer.valueOf(field.getProp("index"));
			String value = getValue(values, index, field);
			if(value==null){
				Logger.warn(this, "The required element " + field.name() + " is empty, skip this line " + line);
				instance=null;
				break;
			}
			if(targetFields.containsKey(field.name())){
				targetFields.get(field.name()).setAccessible(true);
				if(field.schema().getName().equalsIgnoreCase("double")){
					targetFields.get(field.name()).set(instance, createBigDecimal(value));
				}else if(field.schema().getName().equalsIgnoreCase("long")){
					targetFields.get(field.name()).set(instance, Long.valueOf(value));
				}else{
					//default is string
					targetFields.get(field.name()).set(instance, value);
				}							
			}else{
				Logger.warn(this, "Can't find " + field.name() + " in target class " + targetClass.getName());
			}
		}
		
		// set the file id if necessary
		if (instance!=null && targetFields.containsKey("fileId")) {
			targetFields.get("fileId").setAccessible(true);
			targetFields.get("fileId").set(instance, Long.valueOf(fileTask.getId()));
		}
		if (instance!=null && targetFields.containsKey("instId")) {
			targetFields.get("instId").setAccessible(true);
			targetFields.get("instId").set(instance, parseInstId(fileTask.getFileName()));
		}
		if (instance!=null && targetFields.containsKey("batchId")) {
			targetFields.get("batchId").setAccessible(true);
			targetFields.get("batchId").set(instance, Long.valueOf(fileTask.getId()));
		}

		return instance;
	}
	

	/**
	 * check and get value safely
	 * @param values
	 * @param index
	 * @return
	 */
	private String getValue(List<String> values, int index, Field field) {
		if(index>=values.size()){
			Logger.warn(this, String.format("Can't get the %d element, since values only have %d elements", index, values.size()));
			return null;
		}else{
			String value = values.get(index);
			if(StringUtils.isBlank(value)){
				if(field.getProp("required").equalsIgnoreCase("true")){
					Logger.warn(this, String.format("The %d element is empty!", index));
					return null;
				}else{
					//set default value
					if(field.schema().getName().equalsIgnoreCase("double")){
						return "0.0000";
					}else if(field.schema().getName().equalsIgnoreCase("long")){
						return "0";
					}else{
						//default is string
						return "";
					}	
				}				
			}else{
				return value;
			}
		}
	}

	/**
	 * TODO change logic
	 * Valid the file line number is right to the first line
	 * Format is: file desc|line count
	 * @throws IOException
	 */
	private boolean validLineNumber(File file, int titleLineNumber) throws IOException{
		Logger.info(this, "Validate the " + file.getName() + " line number...");
		//first time, check the row number
		InputStreamReader in = null;
		LineNumberReader reader = null;
		try{
			in = new InputStreamReader(new FileInputStream(file), "UTF-8");
			reader = new LineNumberReader(in);
			String firstLine = reader.readLine();
			List<String> detail = Splitter.on("|").splitToList(firstLine);
			long lineCount = Long.valueOf(detail.get(1));
			int count = 0;
			while(reader.readLine()!=null){
				count++;
			}
			
			if(titleLineNumber>0){
				//skip the title line
				return lineCount==(count-1);
			}else{
				return lineCount==count;
			}
		}catch(IOException e){
			Logger.warn(this, e.getMessage(), e);
			throw e;
		}finally{
			IOUtils.closeQuietly(reader);
			IOUtils.closeQuietly(in);
		}
	}
	

	//TODO
	@Transactional
	public void batchInsertSyncAndUpdateSyncFile(String repoName, List dtos,
			long startLine, JijinSyncFileDTO fileTask) {
		SyncFileRepository repo = (SyncFileRepository) ApplicationContextUtils.getSpringContext().getBean(repoName);
		repo.batchInsertDTOs(dtos);
		jijinSyncFielRepository.updateBusJijinSyncFile(MapUtils.buildKeyValueMap("id", fileTask.getId(), "currentLine", startLine));	
		Logger.info(this, "update the processed lien number to " + startLine);
	}
	
	private BigDecimal createBigDecimal(String value){
		BigDecimal b = new BigDecimal(0);		
		if(StringUtils.isNotBlank(value)){
			b = new BigDecimal(value);
		}		
		return b;
	}
	
	private Schema getSchema(String schemaName) throws IOException{
		ClassLoader loader = Thread.currentThread().getContextClassLoader(); 
	    URL url = loader.getResource(schemaName);   
		Schema schema = new Schema.Parser().parse(new File(url.getFile()));
		return schema;
	}
	
	private String parseInstId(String fileName){
		String head = jijinAppProperties.getJijinNasRootDir();
		String info = StringUtils.removeStart(fileName, head);
		return info.split("/")[0];
	}

	private String parseBatchId(String fileName){

		return "";
	}

}
