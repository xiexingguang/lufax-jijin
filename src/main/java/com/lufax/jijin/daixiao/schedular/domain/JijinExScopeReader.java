package com.lufax.jijin.daixiao.schedular.domain;

import com.lufax.jijin.base.utils.EmptyChecker;
import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.base.utils.StringUtils;
import com.lufax.jijin.daixiao.constant.JijinExValidEnum;
import com.lufax.jijin.daixiao.constant.RecordStatus;
import com.lufax.jijin.daixiao.dto.JijinExScopeDTO;
import com.lufax.jijin.fundation.dto.JijinSyncFileDTO;

import java.math.BigDecimal;

/**
 * 规模
 */
public class JijinExScopeReader {


    /*
    {信息提示}| {总笔数}|{数据起始时间}|{数据截止时间}
    序号|{字段1}|{字段2} |OPDATE
    1|xxxx|xxxx|2015-07-22 18:18:18
    1   基金代码
    2   开始日期
    3   报告日期
    4   基金份额
    5   期间申购份额
    6   期间赎回份额
    */

    // 校验每条记录的完整性
    private boolean checkIntegrity(String[] args, JijinSyncFileDTO file, long num) {

        if (EmptyChecker.isEmpty(args[1])
                || EmptyChecker.isEmpty(args[3])
                || EmptyChecker.isEmpty(args[4])) {

            Logger.error(this, String.format(
                    "JijinExScope File check empty failed :[id: %s] [fileName : %s], line num  %s",
                    file.getId(), file.getFileName(), num));
            return false;
        }

        if (args.length != 9) {
            Logger.error(this, String.format(
                    "JijinExScope File length not correct :[id: %s] [fileName : %s], line num  %s",
                    file.getId(), file.getFileName(), num));
            return false;
        }

        return true;
    }

    public JijinExScopeDTO readLine(String lineContent, long lineNum, JijinSyncFileDTO file) {

        try {
            if (lineNum > 2) {

                lineContent = lineContent + "|x"; // append '|x' to let parse correctly
                String[] args = lineContent.split("\\|");

                if (!checkIntegrity(args, file, lineNum)) return null;

                JijinExScopeDTO dto = new JijinExScopeDTO();
                dto.setFundCode(args[1]);
                dto.setStartDate(args[2]);
                dto.setReportDate(args[3]);
                dto.setFundShare(new BigDecimal(args[4]));
                dto.setIsValid(JijinExValidEnum.IS_NOT_VALID.getCode());
                if (StringUtils.isNotBlank(args[5])) {
                    dto.setPurchaseShare(new BigDecimal(args[5]));
                }
                if (StringUtils.isNotBlank(args[6])) {
                    dto.setRedeemShare(new BigDecimal(args[6]));
                }
                dto.setStatus(RecordStatus.NEW.name());
                dto.setBatchId(file.getId());
                return dto;
            }
        } catch (Exception e) {
            return null;
        }
        return null;// line 1, just return null
    }
}
