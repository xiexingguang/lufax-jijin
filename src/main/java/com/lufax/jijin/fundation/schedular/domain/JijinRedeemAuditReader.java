package com.lufax.jijin.fundation.schedular.domain;

import java.math.BigDecimal;

import com.lufax.jersey.utils.Logger;
import com.lufax.jijin.base.utils.EmptyChecker;
import com.lufax.jijin.fundation.dto.JijinBuyAuditDTO;
import com.lufax.jijin.fundation.dto.JijinRedeemAuditDTO;
import com.lufax.jijin.fundation.dto.JijinSyncFileDTO;

/**
 * 平安付认申购对账文件
 * @author xuneng
 *
 */
public class JijinRedeemAuditReader {
	
	// 校验每条记录的完整性
	/*
	//	    用户会员号
	//		证监会发的销售资格编码
	//		支付结算机构代码
	//		入账账户	e
	//		入账账户名称	 
	//		入账账户开户行	 
	//		入账账户联行号	
	//		出账账户
	//		出账账户名称（客户姓名）
	//		出账账户开户行（如果为个人，填写0）
	//		出账帐号联行号（如果为个人，填写0）
	//		支付商交易日期	
	//		交易金额 （元为单位，小数2位）
	//		币种 人民币
	//		交易类型 01申购 02认购 03定投
	//		交易日期yyyyMMdd	 
	//		交易时间戳HHmmss	 
	//		基金公司流水号	
	//		基金类型
	 * 		交易模式
	 * 
	 */
    private boolean checkIntegrity(String[] args, JijinSyncFileDTO file, long num) {

		if (EmptyChecker.isEmpty(args[0]) 
				|| EmptyChecker.isEmpty(args[1])
				|| EmptyChecker.isEmpty(args[2])
				|| EmptyChecker.isEmpty(args[3])
				|| EmptyChecker.isEmpty(args[4])
				|| EmptyChecker.isEmpty(args[5])
				|| EmptyChecker.isEmpty(args[6])
				|| EmptyChecker.isEmpty(args[7])
				|| EmptyChecker.isEmpty(args[8])
				|| EmptyChecker.isEmpty(args[9])
				|| EmptyChecker.isEmpty(args[10])
				|| EmptyChecker.isEmpty(args[11])
				|| EmptyChecker.isEmpty(args[12])
				|| EmptyChecker.isEmpty(args[13])
				|| EmptyChecker.isEmpty(args[14])
				|| EmptyChecker.isEmpty(args[15])
				|| EmptyChecker.isEmpty(args[17])
				|| EmptyChecker.isEmpty(args[16])
				|| EmptyChecker.isEmpty(args[18])
			    || EmptyChecker.isEmpty(args[19])
				) {

			Logger.error(this,String.format(
							"File is not in the correct format :[id: %s] [fileName : %s], line num  %s",
							file.getId(), file.getFileName(),num));
			return false;
		}

		return true;
    }
	
	
	public JijinRedeemAuditDTO readLine(String lineContent, long lineNum,JijinSyncFileDTO file){

		if(lineNum>0){

			lineContent = lineContent+"|x"; // append '|x' to let parse correctly
			String[] args = lineContent.split("\\|");
			
			if(!checkIntegrity(args,file,lineNum)) return null;
			
			JijinRedeemAuditDTO dto = new JijinRedeemAuditDTO();
			dto.setFileId(file.getId());
			dto.setCustomerId(args[0]);
			dto.setDistributorCode(args[1]);
			dto.setPayOrgId(args[2]);
			dto.setReceiveAcct(args[3]);
			dto.setReceiveAcctName(args[4]);
			dto.setRecBankName(args[5]);
			dto.setRecBankCode(args[6]);
			dto.setPayAcct(args[7]);
			dto.setPayAcctName(args[8]);
			dto.setPayBankName(args[9]);
			dto.setPayBankCode(args[10]);
			dto.setTxnDate(args[11]);
			dto.setTxnAmount(new BigDecimal(args[12]));
			dto.setCurrency(args[13]);
			dto.setTxnType(args[14]);
			dto.setFundDate(args[15]);
			dto.setFundTime(args[16]);
			dto.setFundSeqId(args[17]);
			dto.setFundType(args[18]);
			dto.setTxnMode(args[19]);

			return dto;
		
		}
		
		return null;
	}


}
