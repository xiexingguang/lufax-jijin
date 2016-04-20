package com.lufax.jijin.daixiao.schedular.domain;


import com.lufax.jijin.base.utils.EmptyChecker;
import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.daixiao.constant.RecordStatus;
import com.lufax.jijin.daixiao.dto.JijinExBuyLimitDTO;
import com.lufax.jijin.fundation.dto.JijinSyncFileDTO;
import com.lufax.jijin.fundation.dto.JijinTradeSyncDTO;

import java.math.BigDecimal;

public class JijinExBuyLimitSyncReader {

    /*认申购起点限额
    {信息提示}| {总笔数}|{数据起始时间}|{数据截止时间}
    序号|{字段1}|{字段2} |OPDATE
    1|xxxx|xxxx|2015-07-22 18:18:18
    1   基金代码
    2   业务代码
    3   首次最低限额
    4   首次最高限额
    5   追加最低限额
    6   追加最高限额
    7   单日累计最高限额
    8   单笔最低限额
    9   单笔最高限额
    10  级差
    */


    // 校验每条记录的完整性
    private boolean checkIntegrity(String[] args, JijinSyncFileDTO file, long num) {

        if (EmptyChecker.isEmpty(args[0]) || EmptyChecker.isEmpty(args[1])
                || EmptyChecker.isEmpty(args[2])
                || EmptyChecker.isEmpty(args[3])
                || EmptyChecker.isEmpty(args[4])
                || EmptyChecker.isEmpty(args[5])
                || EmptyChecker.isEmpty(args[6])
                || EmptyChecker.isEmpty(args[7])
                || EmptyChecker.isEmpty(args[8])
                || EmptyChecker.isEmpty(args[9])
                || EmptyChecker.isEmpty(args[10])) {

            Logger.error(this, String.format(
                    "File is not in the correct format :[id: %s] [fileName : %s], line num  %s",
                    file.getId(), file.getFileName(), num));
            return false;
        }

        if (args.length != 13) {
            Logger.error(this, String.format(
                    "JijinExBuyLimit File length not correct :[id: %s] [fileName : %s], line num  %s",
                    file.getId(), file.getFileName(), num));
            return false;
        }

        return true;
    }


    public JijinExBuyLimitDTO readLine(String lineContent, long lineNum, JijinSyncFileDTO file) {

        try {
            if (lineNum > 2) {

                lineContent = lineContent + "|x"; // append '|x' to let parse correctly
                String[] args = lineContent.split("\\|");

                if (!checkIntegrity(args, file, lineNum)) return null;

                JijinExBuyLimitDTO dto = new JijinExBuyLimitDTO();
                dto.setFundCode(args[1]);
                dto.setBizCode(args[2]);
                dto.setFirstInvestMinAmount(new BigDecimal(args[3]));
                dto.setFirstInvestMaxAmount(new BigDecimal(args[4]));
                dto.setAddInvestMinAmount(new BigDecimal(args[5]));
                dto.setAddInvestMaxAmount(new BigDecimal(args[6]));
                dto.setInvestDailyLimit(new BigDecimal(args[7]));
                dto.setSingleInvestMinAmount(new BigDecimal(args[8]));
                dto.setSingleInvestMaxAmount(new BigDecimal(args[9]));
                dto.setRaisedAmount(new BigDecimal(args[10]));
                dto.setBatchId(file.getId());
                dto.setStatus(RecordStatus.NEW.name());//无需状态转换
                return dto;
            }
        } catch (Exception e) {
            return null;
        }
        return null;// line 1, just return null
    }


}
