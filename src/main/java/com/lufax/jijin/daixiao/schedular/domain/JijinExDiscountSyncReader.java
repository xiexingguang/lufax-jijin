package com.lufax.jijin.daixiao.schedular.domain;


import com.lufax.jijin.base.utils.EmptyChecker;
import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.base.utils.NumberUtils;
import com.lufax.jijin.daixiao.dto.JijinExDiscountDTO;
import com.lufax.jijin.fundation.dto.JijinSyncFileDTO;

import java.math.BigDecimal;

public class JijinExDiscountSyncReader {

    /*费率
    {信息提示}| {总笔数}|{数据起始时间}|{数据截止时间}
    序号|{字段1}|{字段2} |OPDATE
    1|xxxx|xxxx|2015-07-22 18:18:18
    1   基金代码
    2   业务代码
    3   生效日期
    4   折扣模式
    5   计费策略
    6   固定比例
    7   金额下限
    8   金额上限
    9   设置类型
    10  设定值
    */


    // 校验每条记录的完整性
    private boolean checkIntegrity(String[] args, JijinSyncFileDTO file, long num) {

        if (EmptyChecker.isEmpty(args[0]) || EmptyChecker.isEmpty(args[1])
                || EmptyChecker.isEmpty(args[2])
                || EmptyChecker.isEmpty(args[3])
                || EmptyChecker.isEmpty(args[4])
                || EmptyChecker.isEmpty(args[5])
                || EmptyChecker.isEmpty(args[9])) {

            Logger.error(this, String.format(
                    "File is not in the correct format :[id: %s] [fileName : %s], line num  %s",
                    file.getId(), file.getFileName(), num));
            return false;
        }

        if (args.length != 13) {
            Logger.error(this, String.format(
                    "JijinExDiscount File length not correct :[id: %s] [fileName : %s], line num  %s",
                    file.getId(), file.getFileName(), num));
            return false;
        }

        return true;
    }


    public JijinExDiscountDTO readLine(String lineContent, long lineNum, JijinSyncFileDTO file) {

        try {
            if (lineNum > 2) {

                lineContent = lineContent + "|x"; // append '|x' to let parse correctly
                String[] args = lineContent.split("\\|");

                if (!checkIntegrity(args, file, lineNum)) return null;

                JijinExDiscountDTO dto = new JijinExDiscountDTO();
                dto.setFundCode(args[1]);
                dto.setBizCode(args[2]);
                dto.setValidDate(args[3]);
                dto.setDiscountMode(args[4]);
                dto.setFeePloy(args[5]);
                dto.setFixedRate(NumberUtils.string2BigDecimal(args[6]));
                dto.setFixedMinAmount(NumberUtils.string2BigDecimal(args[7]));
                dto.setFixedMaxAmount(NumberUtils.string2BigDecimal(args[8]));
                dto.setSetType(args[9]);
                dto.setSetValue(NumberUtils.string2BigDecimal(args[10]));
                dto.setBatchId(file.getId());
                dto.setStatus("NEW");
                return dto;
            }
        } catch (Exception e) {
            return null;
        }
        return null;// line 1, just return null
    }


}
