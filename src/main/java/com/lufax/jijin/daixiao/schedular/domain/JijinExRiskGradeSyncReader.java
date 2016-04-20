package com.lufax.jijin.daixiao.schedular.domain;

import java.util.Date;

import com.lufax.jijin.base.utils.EmptyChecker;
import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.daixiao.constant.RecordStatus;
import com.lufax.jijin.daixiao.dto.JijinExHotSubjectDTO;
import com.lufax.jijin.daixiao.dto.JijinExRiskGradeDTO;
import com.lufax.jijin.fundation.dto.JijinSyncFileDTO;

public class JijinExRiskGradeSyncReader {

	/*
	 * 
	 * 
	 * 序号|基金代码｜风险等级            
	 */
	public boolean checkIntegrity(String[] args,long num,JijinSyncFileDTO file){
		if(EmptyChecker.isEmpty(args[1])
				||EmptyChecker.isEmpty(args[2])){
			Logger.error(this, String.format(
	                "File is not in the correct format :[id: %s] [fileName : %s], line num  %s",file.getId(), file.getFileName(), num));
			return false;
		}

        if (args.length != 5) {
            Logger.error(this, String.format(
                    "JijinExRiskGrade File length not correct :[id: %s] [fileName : %s], line num  %s",
                    file.getId(), file.getFileName(), num));
            return false;
        }

        return true;
		
	}
	
	
	public JijinExRiskGradeDTO readLine(String lineContent, long lineNum,JijinSyncFileDTO file) {
		try {
			if (lineNum > 2) {
				lineContent = lineContent + "|x"; // append '|x' to let parse
													// correctly
				String[] args = lineContent.split("\\|");
				if (!checkIntegrity(args,lineNum,file)){
					return null;
				}
				JijinExRiskGradeDTO dto = new JijinExRiskGradeDTO();
				dto.setBatchId(file.getId());
				dto.setCreatedAt(new Date());
				dto.setCreatedBy("SYS");
				dto.setFundCode(args[1]);
				dto.setRiskGrade(args[2]);
				dto.setStatus(RecordStatus.NEW.name());
				dto.setUpdatedAt(new Date());
				dto.setUpdatedBy("SYS");
				return dto;
			}else{
				return null;
			}
		} catch (Exception e) {
			return null;
		}
	}
}
