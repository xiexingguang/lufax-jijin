package com.lufax.jijin.daixiao.schedular.domain;


import com.lufax.jijin.base.utils.EmptyChecker;
import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.daixiao.constant.JijinExFundTypeEnum;
import com.lufax.jijin.daixiao.constant.RecordStatus;
import com.lufax.jijin.daixiao.dto.JijinExFundTypeDTO;
import com.lufax.jijin.fundation.dto.JijinSyncFileDTO;

import java.util.Date;

public class JijinExFundTypeSyncReader {

    /*
     *
     *
     * 基金代码｜基金类型
     */
    public boolean checkIntegrity(String[] args, long num, JijinSyncFileDTO file) {
        if (EmptyChecker.isEmpty(args[1]) || EmptyChecker.isEmpty(args[2])) {
            Logger.error(this, String.format(
                    "File is not in the correct format :[id: %s] [fileName : %s], line num  %s", file.getId(), file.getFileName(), num));
            return false;
        }

        if (args.length != 5) {
            Logger.error(this, String.format(
                    "JijinExFund File length not correct :[id: %s] [fileName : %s], line num  %s",
                    file.getId(), file.getFileName(), num));
            return false;
        }
        return true;

    }


    public JijinExFundTypeDTO readLine(String lineContent, long lineNum, JijinSyncFileDTO file) {
        try {
            if (lineNum > 2) {
                lineContent = lineContent + "|x"; // append '|x' to let parse
                // correctly
                String[] args = lineContent.split("\\|");
                if (!checkIntegrity(args, lineNum, file)) {
                    return null;
                }
                JijinExFundTypeDTO dto = new JijinExFundTypeDTO();
                dto.setBatchId(file.getId());
                dto.setCreatedAt(new Date());
                dto.setCreatedBy("SYS");
                dto.setFundCode(args[1]);
                JijinExFundTypeEnum eu =  JijinExFundTypeEnum.getByLfexCode(args[2]); 
                if(null == eu){
                	eu = JijinExFundTypeEnum.getByFundTypeCnName(args[2]);
                }
                if(null == eu){
                	Logger.error(this, String.format("file id [%s],file name [%]. Read [BUS_JIJIN_EX_FUND_TYPE] file error at line[%s],fundType is [%s] in file",file.getId(),file.getFileName(),lineNum,args[2]));
                	return null;
                }else{
                	dto.setFundType(eu.getTypeCode());
                }
                
                dto.setStatus(RecordStatus.NEW.name());
                dto.setUpdatedAt(new Date());
                dto.setUpdatedBy("SYS");
                return dto;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }
}
