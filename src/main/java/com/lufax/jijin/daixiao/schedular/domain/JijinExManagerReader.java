package com.lufax.jijin.daixiao.schedular.domain;


import java.math.BigDecimal;

import com.lufax.jijin.base.utils.EmptyChecker;
import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.base.utils.StringUtils;
import com.lufax.jijin.daixiao.constant.JijinExValidEnum;
import com.lufax.jijin.daixiao.constant.RecordStatus;
import com.lufax.jijin.daixiao.dto.JijinExManagerDTO;
import com.lufax.jijin.fundation.dto.JijinSyncFileDTO;

/**
 * 基金经理
 */
public class JijinExManagerReader {

    /*基金经理
    {信息提示}| {总笔数}|{数据起始时间}|{数据截止时间}
    序号|{字段1}|{字段2} |OPDATE
    1|xxxx|xxxx|2015-07-22 18:18:18
    1   基金代码
    2   基金经理
    3   基金经理简介
    4   基金经理ID
    5  公告日期
    6  性别
    7  学历
    8  任职日期
    */


    // 校验每条记录的完整性
    private boolean checkIntegrity(String[] args, JijinSyncFileDTO file, long num) {

        if (EmptyChecker.isEmpty(args[1]) || EmptyChecker.isEmpty(args[2])) {

            Logger.error(this, String.format(
                    "JijinExManager File check empty failed :[id: %s] [fileName : %s], line num  %s",
                    file.getId(), file.getFileName(), num));
            return false;
        }

        return true;
    }

    public JijinExManagerDTO readLine(String lineContent, long lineNum, JijinSyncFileDTO file) {

        try {
            if (lineNum > 2) {

                lineContent = lineContent + "|x"; // append '|x' to let parse correctly
                String[] args = lineContent.split("\\|");

                if (!checkIntegrity(args, file, lineNum)) return null;

                JijinExManagerDTO dto = new JijinExManagerDTO();
                if(args.length>10){
                	//兼容数据
                	dto.setFundCode(args[1]);
                	dto.setManagerId(args[2]);
                    dto.setResume(args[3]);
                    dto.setPubDate(args[4]);
                    dto.setBatchId(file.getId());
                    dto.setStatus(RecordStatus.NEW.name());
                    dto.setIsValid(JijinExValidEnum.IS_VALID.getCode());
                    dto.setManager(args[5]);
                    dto.setGender(args[6]);
                    dto.setEducation(args[7]);
                    dto.setAppointDate(args[8]);
                }else{
                	dto.setFundCode(args[1]);
                    dto.setManager(args[2]);
                    dto.setResume(args[3]);
                    dto.setBatchId(file.getId());
                    dto.setStatus(RecordStatus.NEW.name());
                    dto.setIsValid(JijinExValidEnum.IS_VALID.getCode());
                }

             
                
                
                return dto;
            }
        } catch (Exception e) {
            return null;
        }
        return null;// line 1, just return null
    }
}
