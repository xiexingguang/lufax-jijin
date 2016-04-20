package com.lufax.jijin.daixiao.schedular.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.lufax.jijin.base.utils.EmptyChecker;
import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.daixiao.constant.JijinExValidEnum;
import com.lufax.jijin.daixiao.constant.RecordStatus;
import com.lufax.jijin.daixiao.dto.JijinExFxPerformDTO;
import com.lufax.jijin.fundation.dto.JijinSyncFileDTO;

/**
 * 指数 沪深300
 * @author chenqunhui
 *
 */
public class JijinExFxPerformSyncReader {

	/*
	 * 
	 * 
	 * 序号｜指数代码|最近1周涨跌幅｜最近1月涨跌幅｜最近3月涨跌幅｜最近6月涨跌幅｜最近1年涨跌幅｜最近2年涨跌幅｜最近3年涨跌幅｜本年来涨跌幅
	 */
	public boolean checkIntegrity(String[] args,long num,JijinSyncFileDTO file){
		if(EmptyChecker.isEmpty(args[1])){
			Logger.error(this, String.format(
	                "File is not in the correct format :[id: %s] [fileName : %s], line num  %s",file.getId(), file.getFileName(), num));
			return false;
		}

        if (args.length != 13) {
            Logger.error(this, String.format(
                    "JijinExFxPerform File length not correct :[id: %s] [fileName : %s], line num  %s",
                    file.getId(), file.getFileName(), num));
            return false;
        }
		return true;
		
	}
	
	
	public JijinExFxPerformDTO readLine(String lineContent, long lineNum,JijinSyncFileDTO file ,String switchParam) {
		try {
			if (lineNum > 2) {
				lineContent = lineContent + "|x"; // append '|x' to let parse
													// correctly
				String[] args = lineContent.split("\\|");
				if (!checkIntegrity(args,lineNum,file)){
					return null;
				}
				JijinExFxPerformDTO dto = new JijinExFxPerformDTO();
				dto.setBatchId(file.getId());
				dto.setCreatedAt(new Date());
				dto.setCreatedBy("SYS");
				
				dto.setUpdatedAt(new Date());
				dto.setUpdatedBy("SYS");
				dto.setFindexCode(args[1]);
				dto.setPerformanceDay(args[2]);
				dto.setRiseRateOneWeek(EmptyChecker.isEmpty(args[3])?null :new BigDecimal(args[3]));
				dto.setRiseRateOneMonth(EmptyChecker.isEmpty(args[4])?null :new BigDecimal(args[4]));
				dto.setRiseRateThreeMonth(EmptyChecker.isEmpty(args[5])?null :new BigDecimal(args[5]));
				dto.setRiseRateSixMonth(EmptyChecker.isEmpty(args[6])?null :new BigDecimal(args[6]));
				dto.setRiseRateOneYear(EmptyChecker.isEmpty(args[7])?null :new BigDecimal(args[7]));
				dto.setRiseRateTwoYear(EmptyChecker.isEmpty(args[8])?null :new BigDecimal(args[8]));
				dto.setRiseRateThreeYear(EmptyChecker.isEmpty(args[9])?null :new BigDecimal(args[9]));
				dto.setRiseRateThisYear(EmptyChecker.isEmpty(args[10])?null :new BigDecimal(args[10]));
				if(EmptyChecker.isEmpty(switchParam) || !switchParam.equals("on")){
					dto.setIsValid(JijinExValidEnum.IS_NOT_VALID.getCode());
					dto.setStatus(RecordStatus.NEW.name());
				}else{
					dto.setIsValid(JijinExValidEnum.IS_VALID.getCode());
					dto.setStatus(RecordStatus.NO_USED.name());
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
