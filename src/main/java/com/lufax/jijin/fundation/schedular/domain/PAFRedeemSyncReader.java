package com.lufax.jijin.fundation.schedular.domain;

import java.math.BigDecimal;

import com.lufax.jersey.utils.Logger;
import com.lufax.jijin.base.utils.EmptyChecker;
import com.lufax.jijin.fundation.dto.JijinThirdPaySyncDTO;
import com.lufax.jijin.fundation.dto.JijinSyncFileDTO;

/**
 * 平安付赎回代付文件读取
 * @author xuneng
 *
 */
public class PAFRedeemSyncReader {
	
	
	// 校验每条记录的完整性
	/*
	 * 序号
	 * 平安付代发流水号
	 * 商户端代发流水号
	 * 交易金额 - 分
	 * 币种
	 * 代付类型
	 * 代付时间 - yyyymmddhhmmss
	 * 帐号 - 平安付帐号（协议号）
	 * 代发结果
	 * 
	 */
    private boolean checkIntegrity(String[] args, JijinSyncFileDTO file, long num) {

		if (EmptyChecker.isEmpty(args[0]) || EmptyChecker.isEmpty(args[1])
				|| EmptyChecker.isEmpty(args[2])
				|| EmptyChecker.isEmpty(args[3])
				|| EmptyChecker.isEmpty(args[4])
				|| EmptyChecker.isEmpty(args[5])
				|| EmptyChecker.isEmpty(args[6])
				|| EmptyChecker.isEmpty(args[7])
				|| EmptyChecker.isEmpty(args[8])) {

			Logger.error(this,String.format(
							"File is not in the correct format :[id: %s] [fileName : %s], line num  %s",
							file.getId(), file.getFileName(),num));
			return false;
		}

		return true;
    }
	
	
	public JijinThirdPaySyncDTO readLine(String lineContent, long lineNum,JijinSyncFileDTO file,String instId){
		
		/*
		 * 序号
		 * 平安付代发流水号
		 * 商户端代发流水号
		 * 交易金额 - 分
		 * 币种
		 * 代付类型
		 * 代付时间 - yyyymmddhhmmss
		 * 帐号 - 平安付帐号（平安付协议号）
		 * 代发结果
		 * 
		 */
		if(lineNum>1){

			lineContent = lineContent+"|x"; // append '|x' to let parse correctly
			String[] args = lineContent.split("\\|");
			
			if(!checkIntegrity(args,file,lineNum)) return null;
			
			JijinThirdPaySyncDTO dto = new JijinThirdPaySyncDTO();
			
			dto.setPaySerialNo(args[1]);
			dto.setAppSheetNo(args[2]);
			dto.setAmount(new BigDecimal(args[3]).divide(new BigDecimal(100)));
			dto.setCurrency(args[4]);
			dto.setPayType(args[5]);
			dto.setTrxTime(args[6]);
			dto.setPayNo(args[7]);
			dto.setTrxResult(args[8]);
			
			dto.setChannel("PAF");
			dto.setFileId(file.getId());
			dto.setStatus(JijinThirdPaySyncDTO.Status.NEW);
			dto.setInstId(instId);
			return dto;
		
		}
		
		return null;// line 1, just return null
	}


}
