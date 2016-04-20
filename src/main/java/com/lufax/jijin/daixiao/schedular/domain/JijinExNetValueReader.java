package com.lufax.jijin.daixiao.schedular.domain;


import com.lufax.jijin.base.utils.EmptyChecker;
import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.base.utils.StringUtils;
import com.lufax.jijin.daixiao.constant.JijinExValidEnum;
import com.lufax.jijin.daixiao.constant.RecordStatus;
import com.lufax.jijin.daixiao.dto.JijinExNetValueDTO;
import com.lufax.jijin.fundation.dto.JijinSyncFileDTO;

import java.math.BigDecimal;

public class JijinExNetValueReader {

    /*净值
    {信息提示}| {总笔数}|{数据起始时间}|{数据截止时间}
    序号|{字段1}|{字段2} |OPDATE
    1|xxxx|xxxx|2015-07-22 18:18:18
    1   基金代码
    2   截止日期
    3   单位净值
    4   累计净值
    5   复权单位净值
    */


    // 校验每条记录的完整性
    private boolean checkIntegrity(String[] args, JijinSyncFileDTO file, long num) {

        if (EmptyChecker.isEmpty(args[1]) || EmptyChecker.isEmpty(args[2])
                || EmptyChecker.isEmpty(args[3])) {

            Logger.error(this, String.format(
                    "JijinExNetValue File check empty failed :[id: %s] [fileName : %s], line num  %s",
                    file.getId(), file.getFileName(), num));
            return false;
        }

        return true;
    }


    public JijinExNetValueDTO readLine(String lineContent, long lineNum, JijinSyncFileDTO file, String initSwitch) {

        try {
            if (lineNum > 2) {

                lineContent = lineContent + "|x"; // append '|x' to let parse correctly
                String[] args = lineContent.split("\\|");

                if (!checkIntegrity(args, file, lineNum)) return null;

                JijinExNetValueDTO dto = new JijinExNetValueDTO();
                dto.setFundCode(args[1]);
                dto.setEndDate(args[2]);
                dto.setNetValue(new BigDecimal(args[3]));
                if("on".equals(initSwitch)){
                    dto.setIsValid(JijinExValidEnum.IS_VALID.getCode());
                    dto.setStatus(RecordStatus.NO_USED.name());
                }else{
                    dto.setIsValid(JijinExValidEnum.IS_NOT_VALID.getCode());
                    dto.setStatus(RecordStatus.NEW.name());
                }

                if (StringUtils.isNotBlank(args[4])) {
                    dto.setTotalNetValue(new BigDecimal(args[4]));
                }
                
                if(args.length>7){ //兼容复权单位净值
                	 if (StringUtils.isNotBlank(args[5])) {
                         dto.setAdjNetValue(new BigDecimal(args[5]));
                     }
                }
       
                dto.setBatchId(file.getId());

                return dto;
            }
        } catch (Exception e) {
            return null;
        }
        return null;// line 1, just return null
    }
}
