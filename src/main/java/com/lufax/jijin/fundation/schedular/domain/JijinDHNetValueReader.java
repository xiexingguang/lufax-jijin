package com.lufax.jijin.fundation.schedular.domain;

import java.math.BigDecimal;

import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.daixiao.constant.RecordStatus;
import com.lufax.jijin.fundation.dto.JijinNetValueDTO;
import com.lufax.jijin.fundation.dto.JijinSyncFileDTO;
import com.site.lookup.util.StringUtils;
/**
 * 大华货基净值文件reader
 * @author chenqunhui
 *
 */
public class JijinDHNetValueReader {

	 private boolean checkIntegrity(String[] args, JijinSyncFileDTO file, long num) {
		 if(StringUtils.isEmpty(args[0])
				 || StringUtils.isEmpty(args[2])
				 ||	StringUtils.isEmpty(args[3])
				 || StringUtils.isEmpty(args[4])
				 ||	StringUtils.isEmpty(args[5])){
			 Logger.error(this, String.format(
	                    "File is not in the correct format :[id: %s] [fileName : %s], line num  %s",
	                    file.getId(), file.getFileName(), num));
			 return false;
		 }
	 
		 return true;
	 }
	
	public JijinNetValueDTO readLine(String lineContent, long lineNum,JijinSyncFileDTO file){
		if(lineNum>2){
			lineContent = lineContent+"|x"; // append '|x' to let parse correctly
			String[] args = lineContent.split("\\|");
			
			if(!checkIntegrity(args,file,lineNum)) return null;
			
			JijinNetValueDTO dto = new JijinNetValueDTO();
			dto.setFundCode(args[0]);
			dto.setFundStatus("0");
			dto.setNetValueDate(args[2]);
			if(StringUtils.isNotEmpty(args[3])){
				dto.setNetValue(new BigDecimal(args[3]));
				dto.setTotalNetValue(new BigDecimal(args[3]));
			}
			if(StringUtils.isNotEmpty(args[4])){
				dto.setInterestratePerSevenday(new BigDecimal(args[4]));
			}
			if(StringUtils.isNotEmpty(args[5])){
				dto.setBenefitPerTenthousand(new BigDecimal(args[5]));
			}
			dto.setStatus(RecordStatus.DISPACHED.name()); // 默认读为已处理状态
			return dto;
			
		}
		return null;
	}
}
