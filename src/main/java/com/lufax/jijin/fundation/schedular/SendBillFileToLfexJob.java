package com.lufax.jijin.fundation.schedular;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lufax.jijin.base.service.TradeDayService;
import com.lufax.jijin.base.utils.DateUtils;
import com.lufax.jijin.base.utils.JijinAppProperties;
import com.lufax.jijin.fundation.constant.FundSaleCode;
import com.lufax.jijin.fundation.constant.SyncFileBizType;
import com.lufax.jijin.fundation.constant.SyncFileStatus;
import com.lufax.jijin.fundation.constant.TradeRecordType;
import com.lufax.jijin.fundation.dto.JijinRegisteRecordDTO;
import com.lufax.jijin.fundation.dto.JijinSyncFileDTO;
import com.lufax.jijin.fundation.dto.JijinTradeRecordCountDTO;
import com.lufax.jijin.fundation.dto.JijinTradeRecordDTO;
import com.lufax.jijin.fundation.repository.JijinRegisteRecordRepository;
import com.lufax.jijin.fundation.repository.JijinSyncFileRepository;
import com.lufax.jijin.fundation.repository.JijinTradeRecordRepository;
import com.lufax.kernel.zookeeper.AcquireFailedException;
import com.lufax.kernel.zookeeper.LufaxInterProcessMutex;
import com.lufax.kernel.zookeeper.ZookeeperService;
import com.lufax.mq.client.util.MapUtils;
import com.site.lookup.util.StringUtils;

/**
 * 创建文件 to lfex
 * @author chenqunhui
 *
 */

@Service
public class SendBillFileToLfexJob {

	private Logger logger = Logger.getLogger(SendBillFileToLfexJob.class);
	
	@Autowired
	private TradeDayService tradeDayService;
	@Autowired
	private JijinSyncFileRepository jijinSyncFileRepository;
	@Autowired
	private JijinRegisteRecordRepository  jijinRegisteRecordRepository;
	@Autowired
	private JijinTradeRecordRepository jjinTradeRecordRepository;
	@Autowired
	private JijinAppProperties jijinAppProperties;
	@Resource
    private ZookeeperService zookeeperService;
	
	public void execute(String bizType, Date date){
		
		//Date today = date; //new Date();
		if(StringUtils.isEmpty(bizType)){
			logger.error("SendBillFileToLfexJob error,bizType is null");
			return;
		}
		if(null == date){
			logger.error("SendBillFileToLfexJob error,date is null");
			return;
		}
		LufaxInterProcessMutex mutex = zookeeperService.newInterProcessMutex("/jobs/"+this.getClass().getSimpleName()+"-"+bizType);
		try {
			mutex.acquire(3000l, TimeUnit.MILLISECONDS);
			logger.info(String.format("SendBillFileToLfexJob start,bizType is [%s],date is[%s]",bizType, date));
			boolean isTradeDay = tradeDayService.isTradeDay(date);
			if (!isTradeDay) {
				logger.info(DateUtils.formatDate(date, DateUtils.DATE_FORMAT)+ " is not trade day!");
				logger.info(String.format("SendBillFileToLfexJob end,bizType is [%s],date is[%s]",bizType, date));
				return;
			}
			creatFileAop(bizType, DateUtils.formatDateAsString(date));
			logger.info(String.format("SendBillFileToLfexJob end,bizType is [%s],date is[%s]",bizType, date));
		} catch (AcquireFailedException e) { // 表明加锁失败
			logger.info(String.format("SendBillFileToLfexJob got zk lock failed...,bizType is [%s],date is[%s]",bizType, date));
		} catch (Exception e) { // 网络异常，zookeeper客户端异常等
			logger.error(String.format("SendBillFileToLfexJob got zk lock error,throwing Exception...,bizType is [%s],date is[%s]",bizType, date),e);
		} finally {
			mutex.release(); // 释放锁
		}
		
		
	}
	
	
	public void creatFileAop(String bizType,String bizDate){
		String dirPath = getFilePath(bizDate);
		String fileName = dirPath+getFileName(bizType, bizDate);
		JijinSyncFileDTO dto = jijinSyncFileRepository.findBusJijinSyncFile(MapUtils.buildKeyValueMap("bizType",bizType,"bizDate",bizDate,"fileName",fileName));
		if(null == dto){
			try{
				dto = addNewSyncFile(bizType,bizDate,fileName);
			}catch(Exception e){
				logger.warn(String.format("Sync file has been inserted by other thread,so pass it,file name is [%s]",fileName));
				return;
			}
		}else if(SyncFileStatus.WRITE_SUCCESS.name().equals(dto.getStatus())){
			return;
		}
		//创建目录
		File dirFile = new File(dirPath);
		if(!dirFile.exists()){
			try{
				dirFile.mkdirs();
			}catch(Exception e){
				logger.error(e.getMessage());
			}
		}
		//创建临时文件
		File file = new File(fileName);
		if(file.exists()){
			file.delete();
		}
		File ephmeralfile = new File(fileName+".tmp");
		if(ephmeralfile.exists()){
			ephmeralfile.delete();
		}
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(ephmeralfile),"UTF-8"),1024*10);
			int totalCount = 0;
			if(SyncFileBizType.JIJIN_LFX_REGISTER_RESULT.name().equals(bizType)){
				//注册
				totalCount = writeFileForRegist(out,bizDate);
			}else if(SyncFileBizType.JIJIN_LFX_DIVIDEND_RESULT.name().equals(bizType)){
				//分红修改
				totalCount = writeFileForDividend(out,bizDate);
			}
			out.flush();
			//rename
			boolean flag = ephmeralfile.renameTo(new File(fileName));
			if(flag){
				jijinSyncFileRepository.updateBusJijinSyncFile(MapUtils.buildKeyValueMap("id",dto.getId(),"currentLine",totalCount+2,"status",SyncFileStatus.WRITE_SUCCESS,"memo",""));//(dto.getId(), SyncFileStatus.WRITE_SUCCESS, "");
			}else{
				ephmeralfile.delete();
				jijinSyncFileRepository.updateBusJijinSyncFile(MapUtils.buildKeyValueMap("id",dto.getId(),"currentLine",totalCount+2,"status",SyncFileStatus.WRITE_FAIL,"memo","rename file fail"));//(dto.getId(), SyncFileStatus.WRITE_SUCCESS, "");
				//jijinSyncFileRepository.updateBusJijinSyncFileStatus(dto.getId(), SyncFileStatus.WRITE_FAIL, "rename file fail");
			}
		} catch (IOException e) {
			jijinSyncFileRepository.updateBusJijinSyncFileStatus(dto.getId(), SyncFileStatus.WRITE_FAIL, subMessage(e.getMessage()));
			logger.error(String.format("create file to lfex fail,file id is[%s], name is[%s]",dto.getId(),fileName));
		}finally{
			if(null != out){
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
			
	}
	//写开户记录文件
	public int writeFileForRegist(BufferedWriter out,String bizDate) throws IOException{
		String lastTradeDay = tradeDayService.getLastTradeDay(DateUtils.parseDate(bizDate,DateUtils.DATE_STRING_FORMAT));
		Date createFrom = DateUtils.parseDate(lastTradeDay+"150000",DateUtils.XINBAO_MOBILENO_SEQUENCE_FORMATE);
		Date createTo = DateUtils.parseDate(bizDate+"150000",DateUtils.XINBAO_MOBILENO_SEQUENCE_FORMATE);
		String instId = FundSaleCode.LFX.getInstId();
		//统计总数
		int count = jijinRegisteRecordRepository.countRegisteRecord(instId, createFrom, createTo);
	    //first line
		out.write(String.valueOf(count));
	    out.newLine();
	    //title line
		out.write(new StringBuilder()
				.append("机构标识").append("|")
				.append("Lufax申请流水号").append("|")
				.append("签约协议号").append("|")
				.append("投资人姓名").append("|")
				.append("陆金所用户ID").append("|")
				.append("证件类型").append("|")
				.append("证件号码").append("|")
				.append("投资人手机号码").append("|")
				.append("基金公司用户交易账号").append("|")
				.append("客户号").append("|")
				.append("状态").append("|")
				.append("申请时间").append("|")
				.append("返回错误码").append("|")
				.append("返回错误信息")
				.toString());
		out.newLine();
		if(count == 0){
			//给空文件
			return 0;
		}
		int batchNum = 500;
		for(int page = 1;(page-1)*batchNum<count;page++){
			List<JijinRegisteRecordDTO> recordList = jijinRegisteRecordRepository.batchFindRegisteList(instId,createFrom,createTo,batchNum, page);
			if(CollectionUtils.isEmpty(recordList)){
				break;
			}
			for(JijinRegisteRecordDTO regist : recordList){
				//content line
				out.write(new StringBuilder()
				.append(regist.getInstId()).append("|")
				.append(regist.getAppNo()).append("|")
				.append(getUnNullValue(regist.getPayNo())).append("|")
				.append("").append("|")//投资人姓名不写
				.append(regist.getUserId()).append("|")
				.append("").append("|")//证件类型不写
				.append("").append("|")//证件号码不写
				.append("").append("|")//投资人手机号码不写
				.append(getUnNullValue(regist.getContractNo())).append("|")
				.append(getUnNullValue(regist.getCustNo())).append("|")
				.append(regist.getStatus()).append("|")
				.append(DateUtils.formatDate(regist.getCreatedAt(), DateUtils.DATE_TIME_FORMAT)).append("|")
				.append(getUnNullValue(regist.getErrorCode())).append("|")
				.append(getUnNullValue(regist.getErrorMsg()))
				.toString());
				out.newLine();
			}
		}
		return count;
	}
	
	//写分红修改文件
	public int writeFileForDividend(BufferedWriter out,String bizDate) throws IOException{
		String lastTradeDay = tradeDayService.getLastTradeDay(DateUtils.parseDate(bizDate,DateUtils.DATE_STRING_FORMAT));
		Date createFrom = DateUtils.parseDate(lastTradeDay+"150000",DateUtils.XINBAO_MOBILENO_SEQUENCE_FORMATE);
		Date createTo = DateUtils.parseDate(bizDate+"150000",DateUtils.XINBAO_MOBILENO_SEQUENCE_FORMATE);
		String instId = FundSaleCode.LFX.getInstId();
		ArrayList<String> list = new ArrayList<String>();
		list.add(TradeRecordType.DIVIDEND_MODIFY.name());
		JijinTradeRecordCountDTO countDto = jjinTradeRecordRepository.countTradeRecordByCreatedAtAndTypes(createFrom, createTo, instId,list);
		if(null == countDto){
			countDto = new JijinTradeRecordCountDTO();
		}
		//first line
		out.write(String.valueOf(countDto.getTotal()));
	    out.newLine();
	    //title line
	    out.write(new StringBuilder()
		.append("机构标识").append("|")
		.append("基金公司用户交易账号").append("|")
		.append("Lufax申请流水号").append("|")
		.append("基金公司流水号").append("|")
		.append("业务代码").append("|")
		.append("基金代码").append("|")
		.append("收费方式").append("|")
		.append("默认分红方式").append("|")
		.append("所属交易日").append("|")
		.append("申请时间").append("|")
		.append("状态").append("|")
		.append("返回错误码").append("|")
		.append("返回错误信息")
		.toString());
	    out.newLine();
		if(countDto.getTotal() == 0){
			return 0;
		}
		int batchNum = 500;
		for(int page = 1;(page-1)*batchNum<countDto.getTotal();page++){
			List<JijinTradeRecordDTO>  recordList = jjinTradeRecordRepository.getRecordsByCreatedAtAndTypes(createFrom, createTo, batchNum*(page-1)+1,batchNum*page+1, instId,list);
			if(CollectionUtils.isEmpty(recordList)){
				break;
			}
			for(JijinTradeRecordDTO record : recordList){
				out.write(new StringBuilder()
				.append(getUnNullValue(record.getInstId())).append("|")
				.append(getUnNullValue(record.getContractNo())).append("|")
				.append(getUnNullValue(record.getAppNo())).append("|")
				.append(getUnNullValue(record.getAppSheetNo())).append("|")
				.append("029").append("|")
				.append(getUnNullValue(record.getFundCode())).append("|")
				.append("A").append("|")//默认全部前收费
				.append(getUnNullValue(record.getDividendType())).append("|")
				.append(getUnNullValue(record.getTrxDate())).append("|")
				.append(DateUtils.formatDateTime(record.getCreatedAt())).append("|")
				.append(getUnNullValue(record.getStatus())).append("|")
				.append(getUnNullValue(record.getErrorCode())).append("|")
				.append(getUnNullValue(record.getErrorMsg()))
				.toString());
				out.newLine();
			}
		}
		return countDto.getTotal();
	}
	
	
	public JijinSyncFileDTO   addNewSyncFile(String bizType,String bizDate,String fileName) throws Exception{
		JijinSyncFileDTO dto = new JijinSyncFileDTO();
		dto.setBizDate(bizDate);
		dto.setBizType(bizType);
		dto.setCreatedAt(new Date());
		dto.setCreatedBy("SYS");
		dto.setCurrentLine(1l);
		dto.setFileName(fileName);
		dto.setRetryTimes(0l);
		//dto.setSourcePath(sourcePath)
		dto.setStatus(SyncFileStatus.INIT.name());
		dto.setUpdatedAt(new Date());
		dto.setUpdatedBy("SYS");
		jijinSyncFileRepository.insertBusJijinSyncFile(dto);
		return dto;
	}
	
	/**
	 * 获取文件名称
	 * @param bizType
	 * @param bizDate
	 * @return
	 */
	public String getFileName(String bizType,String bizDate){
		if(SyncFileBizType.JIJIN_LFX_REGISTER_RESULT.name().equals(bizType)){
			//注册
			return new StringBuilder("").append("lfx201_").append(bizDate).append("_27.txt").toString();
		}else if(SyncFileBizType.JIJIN_LFX_DIVIDEND_RESULT.name().equals(bizType)){
			//分红修改
			return new StringBuilder("").append("lfx201_").append(bizDate).append("_30.txt").toString();
		}
		throw new RuntimeException(String.format("bizType [%s] is error", bizType));
	}
	
	
	/**
	 * 文件完整目录
	 * @param bizDate
	 * @return
	 */
	public String getFilePath(String bizDate){
		return new StringBuilder("").append(jijinAppProperties.getJijinNasRootDir()).append("lfx201").append("/download/").append(bizDate).append("/").toString();
	}
	
	
	/**
	 * 截断超过1000的错误日志
	 * @param msg
	 * @return
	 */
	public String subMessage(String msg){
		if(StringUtils.isEmpty(msg)){
			return "";
		}else if(msg.length()>1000){
			return msg.substring(0,1000);
		}else{
			return msg;
		}
	}
	
	public String getUnNullValue(String str){
		return (null == str)? "" : str;
	}
}
