package com.lufax.jijin.fundation.schedular.domain;


import com.lufax.jijin.base.utils.EmptyChecker;
import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.fundation.dto.JijinSyncFileDTO;
import com.lufax.jijin.fundation.dto.JijinTradeConfirmDTO;

import java.math.BigDecimal;

/**
 * 确认转入文件读取
 */
public class JijinDHPayInReader {


    // 校验每条记录的完整性
    private boolean checkIntegrity(String[] args, JijinSyncFileDTO file, long num) {

        if (EmptyChecker.isEmpty(args[1])
                || EmptyChecker.isEmpty(args[2])
                || EmptyChecker.isEmpty(args[3])
                || EmptyChecker.isEmpty(args[4])
                || EmptyChecker.isEmpty(args[5])
                || EmptyChecker.isEmpty(args[6])
                || EmptyChecker.isEmpty(args[7])
                || EmptyChecker.isEmpty(args[8])
                || EmptyChecker.isEmpty(args[9])
                || EmptyChecker.isEmpty(args[10])
                || EmptyChecker.isEmpty(args[11])) {

            Logger.error(this, String.format(
                    "File is not in the correct format :[id: %s] [fileName : %s], line num  %s",
                    file.getId(), file.getFileName(), num));
            return false;
        }

        return true;
    }


    public JijinTradeConfirmDTO readLine(String lineContent, long lineNum, JijinSyncFileDTO file) {

        if (lineNum > 2) {

            lineContent = lineContent + "|x"; // append '|x' to let parse correctly
            String[] args = lineContent.split("\\|");

            if (!checkIntegrity(args, file, lineNum)) return null;
            JijinTradeConfirmDTO dto = new JijinTradeConfirmDTO();
            dto.setFileId(file.getId());
            dto.setPayNo(args[0]);
            dto.setContractNo(args[1]);
            dto.setApplyDate(args[2]);
            dto.setLufaxRequestNo(args[3]);
            dto.setAppSheetNo(args[4]);
            dto.setBizType(args[5]);
            dto.setConfirmDate(args[6]);
            dto.setCompanyConfirmNo(args[7]);
            dto.setFundCode(args[8]);
            dto.setAmount(new BigDecimal(args[9]));
            dto.setConfirmShare(new BigDecimal(args[10]));
            dto.setTradeResCode(args[11]);
            dto.setTradeResMemo(args[12]);
            dto.setStatus("NEW");
            return dto;
        }

        return null;// line 1, just return null
    }


}
