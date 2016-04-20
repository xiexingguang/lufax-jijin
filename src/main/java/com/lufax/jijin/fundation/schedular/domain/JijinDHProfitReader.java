package com.lufax.jijin.fundation.schedular.domain;


import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lufax.jijin.base.utils.EmptyChecker;
import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.fundation.dto.JijinAccountDTO;
import com.lufax.jijin.fundation.dto.JijinSyncFileDTO;
import com.lufax.jijin.fundation.dto.JijinUserBalanceAuditDTO;
import com.lufax.jijin.fundation.repository.JijinAccountRepository;

/**
 * 大华T+0 货基收益
 * 
 * @author xuneng
 *
 */
@Component
public class JijinDHProfitReader {
	
	@Autowired
	private JijinAccountRepository jijinAccountRepository;
    // 校验每条记录的完整性
	/*
	 * 0陆金所用户id
	 * 1用户在基金公司的帐号
	 * 2基金代码
	 * 3总份额
	 * 4T-1日用户收益 100.00
	 * 5T-1日归属垫资方收益 100.00
	 * 6未产生收益的份额
	 * 7历史累计收益
	 * 8所属日期 yyyymmdd
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
                || EmptyChecker.isEmpty(args[8])) {

            Logger.error(this, String.format(
                    "File is not in the correct format :[id: %s] [fileName : %s], line num  %s",
                    file.getId(), file.getFileName(), num));
            return false;
        }

        return true;
    }


    public JijinUserBalanceAuditDTO readLine(String lineContent, long lineNum, JijinSyncFileDTO file,List<String> currencyFundCodes,String instId) {

        if (lineNum > 2) {

        	lineContent = lineContent+"|x"; // append '|x' to let parse correctly
			String[] args = lineContent.split("\\|");
			
			if(!checkIntegrity(args,file,lineNum)) return null;
			
			JijinUserBalanceAuditDTO dto = new JijinUserBalanceAuditDTO();
			dto.setFileId(file.getId());
			dto.setFundCode(args[2]);
			dto.setFeeType("N/A");
			dto.setInstId(instId);
			dto.setContractNo(args[1]);
			
			if("dh103".equals(instId)){	
				JijinAccountDTO accountDto =jijinAccountRepository.findBusJijinAccountByPayNoAndInstId(args[0],instId);
				if(null==accountDto){
					dto.setUserId(Long.valueOf(args[1]));//04999000000000001
					dto.setStatus("ERROR");// can not find account
				}else{
					dto.setUserId(accountDto.getUserId());
				}
			}else{
				dto.setUserId(Long.valueOf(args[0]));
			}
			
			
			dto.setTotalShare(new BigDecimal(args[3]));
			dto.setFrozenShare(new BigDecimal("0.00"));
			dto.setAvailableShare(new BigDecimal(args[3]));
			dto.setDailyIncome(new BigDecimal(args[4]));
			dto.setTotalIncome(new BigDecimal(args[7]));
			dto.setDividendType("0");
            dto.setBizDate(args[8]);
            if(currencyFundCodes.contains(args[2]))
            	dto.setFundType("1"); //货基
            dto.setStatus("NEW");
            dto.setVersion(0l);
			return dto;
        }

        return null;// line 1, just return null
    }


}
