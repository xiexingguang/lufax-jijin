package com.lufax.jijin.daixiao.schedular.domain;


import com.lufax.jijin.base.utils.EmptyChecker;
import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.base.utils.StringUtils;
import com.lufax.jijin.daixiao.constant.JijinExValidEnum;
import com.lufax.jijin.daixiao.constant.RecordStatus;
import com.lufax.jijin.daixiao.dto.JijinExNetValueDTO;
import com.lufax.jijin.daixiao.dto.JijinExYieldRateDTO;
import com.lufax.jijin.fundation.dto.JijinSyncFileDTO;

import java.math.BigDecimal;

public class JijinExYieldRateReader {

    /*净值
    {信息提示}| {总笔数}|{数据起始时间}|{数据截止时间}
    序号|{字段1}|{字段2} |OPDATE
    1|xxxx|xxxx|2015-07-22 18:18:18
    1   序号
    2   基金代码
    3   起始日期
    4   截止日期
    5   公告日期
    6   每万份基金单位收益
    7   最近七日收益所折算的年资产收益率
    */


    // 校验每条记录的完整性
    private boolean checkIntegrity(String[] args, JijinSyncFileDTO file, long num) {

        if (EmptyChecker.isEmpty(args[1]) || EmptyChecker.isEmpty(args[3])
                || EmptyChecker.isEmpty(args[5]) || EmptyChecker.isEmpty(args[6])) {

            Logger.error(this, String.format(
                    "JijinExYieldRate File check empty failed :[id: %s] [fileName : %s], line num  %s",
                    file.getId(), file.getFileName(), num));
            return false;
        }
        /**
         * 每行后都加了|x，所以是8列
         */
        if (args.length != 9) {
            Logger.error(this, String.format(
                    "JijinExYieldRate File length not correct :[id: %s] [fileName : %s], line num  %s",
                    file.getId(), file.getFileName(), num));
            return false;
        }

        return true;
    }


    public JijinExYieldRateDTO readLine(String lineContent, long lineNum, JijinSyncFileDTO file) {

        try {
            if (lineNum > 2) {

                lineContent = lineContent + "|x"; // append '|x' to let parse correctly
                String[] args = lineContent.split("\\|");

                if (!checkIntegrity(args, file, lineNum)) return null;

                JijinExYieldRateDTO dto = new JijinExYieldRateDTO();
                dto.setFundCode(args[1]);
                dto.setStartDate(args[2]);
                dto.setEndDate(args[3]);
                dto.setNoticeDate(args[4]);
                if(!EmptyChecker.isEmpty(args[5])){
                	dto.setBenefitPerTenthousand(new BigDecimal(args[5]));
                }
                if(!EmptyChecker.isEmpty(args[6])){
                	dto.setInterestratePerSevenday(new BigDecimal(args[6]));
                }
                dto.setBatchId(file.getId());
                dto.setStatus(RecordStatus.NEW.name());
                dto.setIsValid(0);
                return dto;
            }
        } catch (Exception e) {
            return null;
        }
        return null;// line 1, just return null
    }
}
