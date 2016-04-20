package com.lufax.jijin.fundation.schedular.domain;

import java.math.BigDecimal;
import java.util.List;

import com.lufax.jersey.utils.Logger;
import com.lufax.jijin.base.utils.EmptyChecker;
import com.lufax.jijin.fundation.dto.JijinSyncFileDTO;
import com.lufax.jijin.fundation.dto.JijinUserBalanceAuditDTO;

/**
 * 基金用户持仓文件
 * @author xuneng
 *
 */
public class JijinUserBalanceAuditReader {
	
	// 校验每条记录的完整性
	/*
		基金代码
		收费方式
		销售人代码
		基金公司交易账号
		投资人平台用户号
		基金总份额
		基金冻结总份额
		基金可用总份额
		货币基金未付收益金额 (no check)
		每日收益 (no check)
		累计收益	
		默认分红方式
		持仓日期yyyyMMdd
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
				|| EmptyChecker.isEmpty(args[11])
				) {

			Logger.error(this,String.format(
							"File is not in the correct format :[id: %s] [fileName : %s], line num  %s",
							file.getId(), file.getFileName(),num));
			return false;
		}

		return true;
    }
	
	
	public JijinUserBalanceAuditDTO readLine(String lineContent, long lineNum,JijinSyncFileDTO file, String bizDate, List<String> currencyFundCodes){

		if(lineNum>2){

			lineContent = lineContent+"|x"; // append '|x' to let parse correctly
			String[] args = lineContent.split("\\|");
			
			if(!checkIntegrity(args,file,lineNum)) return null;
			
			JijinUserBalanceAuditDTO dto = new JijinUserBalanceAuditDTO();
			dto.setFileId(file.getId());
			dto.setFundCode(args[0]);
			dto.setFeeType(args[1]);
			dto.setInstId(args[2]);
			dto.setContractNo(args[3]);
			dto.setUserId(Long.valueOf(args[4]));
			dto.setTotalShare(new BigDecimal(args[5]));
			dto.setFrozenShare(new BigDecimal(args[6]));
			dto.setAvailableShare(new BigDecimal(args[7]));
			if(!EmptyChecker.isEmpty(args[8]))
			dto.setUnpaiedIncome(new BigDecimal(args[8]));
			if(!EmptyChecker.isEmpty(args[9]))
			dto.setDailyIncome(new BigDecimal(args[9]));
			if(!EmptyChecker.isEmpty(args[10]))
			dto.setTotalIncome(new BigDecimal(args[10]));
            dto.setDividendType(args[11]);
            dto.setBizDate(bizDate);
            if(currencyFundCodes.contains(args[0]))
            	dto.setFundType("1"); //货基
            dto.setStatus("NEW");
            dto.setVersion(0l);
			return dto;
		
		}
		
		return null;
	}


}
