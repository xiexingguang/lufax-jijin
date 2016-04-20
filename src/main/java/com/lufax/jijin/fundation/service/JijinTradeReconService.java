package com.lufax.jijin.fundation.service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lufax.jijin.base.utils.FileUtils;
import com.lufax.jijin.base.utils.JijinAppProperties;
import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.fundation.constant.SyncFileStatus;
import com.lufax.jijin.fundation.constant.TradeReconStatus;
import com.lufax.jijin.fundation.dto.JijinAccountDTO;
import com.lufax.jijin.fundation.dto.JijinPAFBuyAuditDTO;
import com.lufax.jijin.fundation.dto.JijinSyncFileDTO;
import com.lufax.jijin.fundation.dto.JijinTradeReconDTO;
import com.lufax.jijin.fundation.dto.JijinTradeRecordCountDTO;
import com.lufax.jijin.fundation.dto.JijinTradeRecordDTO;
import com.lufax.jijin.fundation.repository.JijinAccountRepository;
import com.lufax.jijin.fundation.repository.JijinPAFBuyAuditRepository;
import com.lufax.jijin.fundation.repository.JijinSyncFileRepository;
import com.lufax.jijin.fundation.repository.JijinTradeReconRepository;
import com.lufax.jijin.fundation.repository.JijinTradeRecordRepository;
import com.lufax.jijin.fundation.repository.JijinTradeSyncRepository;
import com.lufax.jijin.fundation.util.ReconFileContentUtil;
import com.lufax.kernel.security.kms.api.KmsService;

@Service
public class JijinTradeReconService {

    @Autowired
    private JijinTradeSyncRepository jijinTradeSyncRepository;
    @Autowired
    private JijinTradeRecordRepository jijinTradeRecordRepository;
    @Autowired
    private JijinPAFBuyAuditRepository jijinPAFBuyAuditRepository;
    @Autowired
    private JijinTradeReconRepository jijinTradeReconRepository;
   	@Autowired
	private JijinSyncFileRepository jijinSyncFielRepository;
    @Autowired
    private KmsService kmsService;
    @Autowired
    private JijinAppProperties jijinAppProperties;
    @Autowired
    private JijinAccountRepository jijinAccountRepository;
    
    private static final int ROWNUM = 500;
    
    
    /**
     * /nfsc/sftp_user/pafsftp/dh103/download/20150825/dh103_20150825_25.txt_{paf_buy_audit_file_id}
     * 
     * 1.判断paf_buy_audit是否全都dispatched
     * 2.对比是否少交易记录
     * 3.paf的认申购对账文件是否多记录
     * 4.如果以上步骤有问题，插bus_jijin_trade_recon
     * 5.如果没问题，生成新的交易对账文件给基金公司
     * @param jijinSyncFileDTO
     */
    public void process(JijinSyncFileDTO jijinSyncFileDTO){
    	
    	String tmpFileName = jijinSyncFileDTO.getFileName();
    	String[] infoList = tmpFileName.split("_");
    	Long pafBuyAuditFileId = Long.valueOf(infoList[4]);
    	String bizDate = infoList[2];
    	String targetFileName = tmpFileName.replace("_"+pafBuyAuditFileId, "");
    	
    	JijinPAFBuyAuditDTO auditDto = jijinPAFBuyAuditRepository.getMaxRecordByFileId(pafBuyAuditFileId);
    	JijinTradeRecordCountDTO countDto = jijinTradeRecordRepository.countSuccessTradeRecordByTradeDay(bizDate,"dh103");
    	
    	if(null==auditDto && countDto.getTotal()>0){
    		jijinSyncFielRepository.updateBusJijinSyncFileStatus(jijinSyncFileDTO.getId(), SyncFileStatus.RECON_FAIL, "lufax存在交易记录，但是平安付的认申购文件为空");
    	}else if(null==auditDto && countDto.getTotal() == 0){
    		// gen empty file
    		countDto.setAmount(new BigDecimal("0.00"));
    		try {
				genFile(targetFileName,bizDate,countDto,true);
				jijinSyncFielRepository.updateBusJijinSyncFileStatus(jijinSyncFileDTO.getId(), SyncFileStatus.RECON_SUCCESS, "今日无交易");
			} catch (IOException e) {
				Logger.error(this, "货基交易文件生成失败，文件名："+targetFileName);
				jijinSyncFielRepository.updateBusJijinSyncFileStatus(jijinSyncFileDTO.getId(), SyncFileStatus.RECON_FAIL, "IO exception,生成文件失败");
			}  
    	}else{
    		//that means the last one record has been processed
        	if(!"NEW".equals(auditDto.getStatus())){
        		
        		List<Long> unRecontradeRecords = jijinTradeRecordRepository.getUnreconTradeRecords(bizDate,"dh103",pafBuyAuditFileId);
        		
        		List<JijinPAFBuyAuditDTO> unmatchAudits = jijinPAFBuyAuditRepository.findBusJijinPAFBuyAuditList(MapUtils.buildKeyValueMap("status","UNMATCH","fileId",pafBuyAuditFileId));
        		
        		//if no any error, gen new file
        		if(CollectionUtils.isEmpty(unRecontradeRecords) && CollectionUtils.isEmpty(unmatchAudits)){
        			
        			try {
						genFile(targetFileName,bizDate,countDto,false);
						jijinSyncFielRepository.updateBusJijinSyncFile(MapUtils.buildKeyValueMap("id",jijinSyncFileDTO.getId(),"status",SyncFileStatus.RECON_SUCCESS.name(),"currentLine",countDto.getTotal()+2));
					} catch (IOException e) {
						Logger.error(this, "货基交易文件生成失败，文件名："+targetFileName);
						jijinSyncFielRepository.updateBusJijinSyncFileStatus(jijinSyncFileDTO.getId(), SyncFileStatus.RECON_FAIL, "IO exception,生成文件失败");
					}  					
        		}else{
        			//else insert BUS_JIJIN_TRADE_RECON
            		List<JijinTradeReconDTO> recons = new ArrayList<JijinTradeReconDTO>();
            		
            		for(Long tr: unRecontradeRecords){
            			
            			JijinTradeReconDTO rec = new JijinTradeReconDTO();
            			
            			rec.setRecordId(String.valueOf(tr));
            			rec.setRemark("无对应的平安付扣款明细");
            			rec.setStatus(TradeReconStatus.LACK_BUY_AUDIT.name());
            			
            			recons.add(rec);
            		}
            		for(JijinPAFBuyAuditDTO au: unmatchAudits){
            			JijinTradeReconDTO rec = new JijinTradeReconDTO();
            			
            			rec.setBuyAuditId(String.valueOf(au.getId()));
            			rec.setRemark("无对应的lufax交易记录");
            			rec.setStatus(TradeReconStatus.UNMATCH.name());
            			
            			recons.add(rec);
            		}
            		
            		jijinTradeReconRepository.batchInsertBusJijinBuyAudit(recons);
            		String memo = "";
            		for(JijinTradeReconDTO recon:recons){
            			
            			if(TradeReconStatus.LACK_BUY_AUDIT.name().equals(recon.getStatus())){
            				memo=memo+"|LACK"+recon.getRecordId();
            			}
            			if(TradeReconStatus.UNMATCH.name().equals(recon.getStatus())){
            				memo=memo+"|UNMATCH"+recon.getBuyAuditId();
            			}
            		}
            		
            		if(memo.length()>=4000){
            			memo.substring(0,4000);
            		}
            		
            		jijinSyncFielRepository.updateBusJijinSyncFileStatus(jijinSyncFileDTO.getId(), SyncFileStatus.RECON_FAIL, memo);
        		}

        	}
    	}
    }
    
	/**
	 * 
	 * @param fileName
	 * @param bizDate
	 * @throws IOException 
	 */
    private void genFile(String fileName, String bizDate,JijinTradeRecordCountDTO countDto,boolean isEmpty) throws IOException{
    	
    	long time = System.currentTimeMillis();
    	Logger.info(this, String.format("start gen trade recon file,start time: [%s]ms",time));
    	String tmpFileName = fileName+".tmp";
    	File tmpFile = FileUtils.createEmptyFile(tmpFileName); 
    	
    	String header = ReconFileContentUtil.createReconFileHeader(countDto.getTotal(), countDto.getAmount());
    	String columnName = ReconFileContentUtil.createReconFileColumnName();
    	
    	FileUtils.appendContent(tmpFile, header,"GBK");
    	FileUtils.appendContent(tmpFile, columnName,"GBK");
    	
    	if(!isEmpty){
    		int start=1;
        	int end = countDto.getTotal();
        	while (start <= end) {
    			
    			int targetEnd =0;
    			targetEnd =  start + ROWNUM;
    	
    			List<JijinTradeRecordDTO> dtos = jijinTradeRecordRepository.getRecordsByTradeDay(bizDate, start, targetEnd,"dh103");
    			String content = "";
    			for (JijinTradeRecordDTO dto : dtos) {
    				
    				JijinAccountDTO account = jijinAccountRepository.findActiveAccount(dto.getUserId(), dto.getInstId(), "PAF");
    				
    				content = ReconFileContentUtil.createReconFileContent(dto,account.getPayNo());
    				FileUtils.appendContent(tmpFile, content,"GBK");
    			}
    			start = targetEnd;
    		}
    	}
    	
    	tmpFile.renameTo(new File(fileName));
    	
    	//gen zip file	
    	String[] token = fileName.split("/");
    	String sourcefileName = token[7];
		String srcPathName = fileName.replace("/"+sourcefileName, "");
		String zipPathName = fileName.replace(".txt", ".tmp.zip");// "/nfsc/sftp_user/pafsftp/dh103/upload/20150930/dh103_20150930_25.tmp.zip";

		FileUtils.compress(srcPathName, zipPathName, sourcefileName);
		
		//加密
        String base64key = kmsService.getRawKey(jijinAppProperties.getDahua3des());
		FileUtils.encryptDahuaZip(zipPathName,base64key);
    	
		Logger.info(this, String.format("end gen trade recon file,spend [%s]ms",System.currentTimeMillis()-time));
    }

}
			