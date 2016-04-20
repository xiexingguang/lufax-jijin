package com.lufax.jijin.daixiao.schedular.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.lufax.jijin.base.utils.EmptyChecker;
import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.daixiao.constant.JijinExValidEnum;
import com.lufax.jijin.daixiao.constant.RecordStatus;
import com.lufax.jijin.daixiao.dto.JijinExMfPerformDTO;
import com.lufax.jijin.fundation.dto.JijinSyncFileDTO;

public class JijinExMfPerformSyncReader {

	/*
	 * 
	 * 
	 * 基金代码|日期｜收益率（当天）｜
	 * 收益率（一周）｜收益率（一个月）｜收益率（三个月）｜收益率（六个月）｜收益率（一年）｜收益率（两年）｜收益率（三年）｜收益率（本年以来）｜收益率（成立以来）｜
	 * 最近一周同类基金收益率｜最近一月同类基金收益率｜最近三月同类基金收益率｜最近6月同类基金收益率｜最近一年同类基金收益率｜最近两年同类基金收益率｜最近三年同类基金收益率｜今年以来同类基金收益率｜成立以来同类基金收益率｜
	 * 最近一周同类排名｜最近一月同类排名｜最近三月同类排名｜最近六月同类排名｜最近一年同类排名｜最近两年同类排名｜最近三年同类排名｜今年以来同类排名｜成立以来同类排名
	 * 
	 */
	public boolean checkIntegrity(String[] args,long num,JijinSyncFileDTO file){
		if(EmptyChecker.isEmpty(args[0])||EmptyChecker.isEmpty(args[1])){
			//TODO 可能有列值为空
			Logger.error(this, String.format(
	                "File is not in the correct format :[id: %s] [fileName : %s], line num  %s",file.getId(), file.getFileName(), num));
			return false;
		}

        if (args.length != 33) {
            Logger.error(this, String.format(
                    "JijinExMfPerform File length not correct :[id: %s] [fileName : %s], line num  %s",
                    file.getId(), file.getFileName(), num));
            return false;
        }

		return true;
		
	}

	public JijinExMfPerformDTO readLine(String lineContent, long lineNum,JijinSyncFileDTO file,String switchParam) {
		try {
			if (lineNum > 2) {
				lineContent = lineContent + "|x"; // append '|x' to let parse
													// correctly
				String[] args = lineContent.split("\\|");
				if (!checkIntegrity(args,lineNum,file)){
					return null;
				}
				JijinExMfPerformDTO dto = new JijinExMfPerformDTO();
				dto.setBatchId(file.getId());
				dto.setCreatedAt(new Date());
				dto.setCreatedBy("SYS");
				dto.setUpdatedAt(new Date());
				dto.setUpdatedBy("SYS");
				dto.setFundCode(args[1]);
				dto.setPerformanceDay(args[2]);
				dto.setBenefitDaily(EmptyChecker.isEmpty(args[3])?null :new BigDecimal(args[3]));
				dto.setBenefitOneWeek(EmptyChecker.isEmpty(args[4])?null :new BigDecimal(args[4]));
				dto.setBenefitOneMonth(EmptyChecker.isEmpty(args[5])?null :new BigDecimal(args[5]));
				dto.setBenefitThreeMonth(EmptyChecker.isEmpty(args[6])?null :new BigDecimal(args[6]));
				dto.setBenefitSixMonth(EmptyChecker.isEmpty(args[7])?null :new BigDecimal(args[7]));
				dto.setBenefitOneYear(EmptyChecker.isEmpty(args[8])?null :new BigDecimal(args[8]));
				dto.setBenefitTwoYear(EmptyChecker.isEmpty(args[9])?null :new BigDecimal(args[9]));
				dto.setBenefitThreeYear(EmptyChecker.isEmpty(args[10])?null :new BigDecimal(args[10]));
				dto.setBenefitThisYear(EmptyChecker.isEmpty(args[11])?null :new BigDecimal(args[11]));
				dto.setBenefitTotal(EmptyChecker.isEmpty(args[12])?null :new BigDecimal(args[12]));
				
				dto.setKinBenefitOneWeek(EmptyChecker.isEmpty(args[13])?null :new BigDecimal(args[13]));
				dto.setKinBenefitOneMonth(EmptyChecker.isEmpty(args[14])?null :new BigDecimal(args[14]));
				dto.setKinBenefitThreeMonth(EmptyChecker.isEmpty(args[15])?null :new BigDecimal(args[15]));
				dto.setKinBenefitSixMonth(EmptyChecker.isEmpty(args[16])?null :new BigDecimal(args[16]));
				dto.setKinBenefitOneYear(EmptyChecker.isEmpty(args[17])?null :new BigDecimal(args[17]));
				dto.setKinBenefitTwoYear(EmptyChecker.isEmpty(args[18])?null :new BigDecimal(args[18]));
				dto.setKinBenefitThreeYear(EmptyChecker.isEmpty(args[19])?null :new BigDecimal(args[19]));
				dto.setKinBenefitThisYear(EmptyChecker.isEmpty(args[20])?null :new BigDecimal(args[20]));
				dto.setKinBenefitTotal(EmptyChecker.isEmpty(args[21])?null :new BigDecimal(args[21]));
				
				dto.setOrderOneWeek(EmptyChecker.isEmpty(args[22])?null :args[22]); 
				dto.setOrderOneMonth(EmptyChecker.isEmpty(args[23])?null :args[23]);
				dto.setOrderThreeMonth(EmptyChecker.isEmpty(args[24])?null :args[24]);
				dto.setOrderSixMonth(EmptyChecker.isEmpty(args[25])?null :args[25]);
				dto.setOrderOneYear(EmptyChecker.isEmpty(args[26])?null :args[26]);
				dto.setOrderTwoYear(EmptyChecker.isEmpty(args[27])?null :args[27]);
				dto.setOrderThreeYear(EmptyChecker.isEmpty(args[28])?null :args[28]);
				dto.setOrderThisYear(EmptyChecker.isEmpty(args[29])?null :args[29]);
				dto.setOrderTotal(EmptyChecker.isEmpty(args[30])?null :args[30]);
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
