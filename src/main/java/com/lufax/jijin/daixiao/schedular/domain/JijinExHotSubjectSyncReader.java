package com.lufax.jijin.daixiao.schedular.domain;

import java.util.Date;

import com.lufax.jijin.base.utils.EmptyChecker;
import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.daixiao.constant.RecordStatus;
import com.lufax.jijin.daixiao.dto.JijinExHotSubjectDTO;
import com.lufax.jijin.fundation.dto.JijinSyncFileDTO;

public class JijinExHotSubjectSyncReader {

	/*
	 * 
	 * 
	 * 序号|基金代码｜人气方案｜方案序号             
	 */
	public boolean checkIntegrity(String[] args,long num,JijinSyncFileDTO file){
		if(EmptyChecker.isEmpty(args[1])
				||EmptyChecker.isEmpty(args[2])
				||EmptyChecker.isEmpty(args[3])){
			Logger.error(this, String.format(
	                "File is not in the correct format :[id: %s] [fileName : %s], line num  %s",file.getId(), file.getFileName(), num));
			return false;
		}

		return true;
		
	}
	
	
	public JijinExHotSubjectDTO readLine(String lineContent, long lineNum,JijinSyncFileDTO file) {
		try {
			if (lineNum > 2) {
				lineContent = lineContent + "|x"; // append '|x' to let parse
													// correctly
				String[] args = lineContent.split("\\|");
				if (!checkIntegrity(args,lineNum,file)){
					return null;
				}
				JijinExHotSubjectDTO dto = new JijinExHotSubjectDTO();
				dto.setBatchId(file.getId());
				dto.setCreatedAt(new Date());
				dto.setCreatedBy("SYS");
				dto.setFundCode(args[1]);
				dto.setSubjectName(args[2]);
				dto.setSubjectIndex(EmptyChecker.isEmpty(args[3])? null :Long.parseLong(args[3]));
				dto.setStatus(RecordStatus.NEW.name());
				dto.setUpdatedAt(new Date());
				dto.setUpdatedBy("SYS");
				if(args.length>6){
					dto.setMemo(args[4]);
				}
				
				return dto;
			}else{
				return null;
			}
		} catch (Exception e) {
			return null;
		}
	}
}
