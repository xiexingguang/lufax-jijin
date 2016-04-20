package com.lufax.jijin.daixiao.schedular.domain;

import com.lufax.jijin.base.utils.EmptyChecker;
import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.base.utils.StringUtils;
import com.lufax.jijin.daixiao.constant.JijinExValidEnum;
import com.lufax.jijin.daixiao.dto.JijinExDividendDTO;
import com.lufax.jijin.fundation.dto.JijinSyncFileDTO;

import java.math.BigDecimal;

/**
 * 历史分红
 */
public class JijinExDividendReader {


    /*
    {信息提示}| {总笔数}|{数据起始时间}|{数据截止时间}
    序号|{字段1}|{字段2} |OPDATE
    1|xxxx|xxxx|2015-07-22 18:18:18
    1   基金代码
    2   公告日期
    3   权益登记日
    4   除息日
    5   场外除息日
    6   净值除权日
    7   分红发放日
    8   货币代码
    9   每份分红(元)
    */

    // 校验每条记录的完整性
    private boolean checkIntegrity(String[] args, JijinSyncFileDTO file, long num) {

        if (EmptyChecker.isEmpty(args[1])) {

            Logger.error(this, String.format(
                    "JijinExDividend File check empty failed :[id: %s] [fileName : %s], line num  %s",
                    file.getId(), file.getFileName(), num));
            return false;
        }

        if (args.length != 12) {
            Logger.error(this, String.format(
                    "JijinExDividend File length not correct :[id: %s] [fileName : %s], line num  %s",
                    file.getId(), file.getFileName(), num));
            return false;
        }
        return true;
    }

    public JijinExDividendDTO readLine(String lineContent, long lineNum, JijinSyncFileDTO file) {

        try {
            if (lineNum > 2) {

                lineContent = lineContent + "|x"; // append '|x' to let parse correctly
                String[] args = lineContent.split("\\|");

                if (!checkIntegrity(args, file, lineNum)) return null;

                JijinExDividendDTO dto = new JijinExDividendDTO();
                dto.setFundCode(args[1]);
                dto.setAnnDate(args[2]);
                dto.setRecordDate(args[3]);
                dto.setExDate(args[4]);
                dto.setDivEdexDate(args[5]);
                dto.setDivDate(args[6]);
                dto.setDividendDate(args[7]);
                dto.setCurrencyCode(args[8]);
                dto.setIsValid(JijinExValidEnum.IS_VALID.getCode());
                if (StringUtils.isNotBlank(args[9])) {
                    dto.setPerDividend(new BigDecimal(args[9]));
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
