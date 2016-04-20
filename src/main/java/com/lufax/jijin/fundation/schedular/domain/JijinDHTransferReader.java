package com.lufax.jijin.fundation.schedular.domain;


import com.lufax.jijin.base.utils.EmptyChecker;
import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.fundation.dto.JijinSyncFileDTO;
import com.lufax.jijin.fundation.dto.JijinTransferDTO;

import java.math.BigDecimal;

public class JijinDHTransferReader {

    // 校验每条记录的完整性
    private boolean checkIntegrity(String[] args, JijinSyncFileDTO file, long num) {

        if (EmptyChecker.isEmpty(args[0])
                || EmptyChecker.isEmpty(args[1])
                || EmptyChecker.isEmpty(args[2])
                || EmptyChecker.isEmpty(args[3])
                || EmptyChecker.isEmpty(args[4])
                || EmptyChecker.isEmpty(args[5])
                || EmptyChecker.isEmpty(args[6])
                || EmptyChecker.isEmpty(args[7])
                || EmptyChecker.isEmpty(args[9])) {

            Logger.error(this, String.format(
                    "File is not in the correct format :[id: %s] [fileName : %s], line num  %s",
                    file.getId(), file.getFileName(), num));
            return false;
        }

        return true;
    }

    public JijinTransferDTO readLine(String lineContent, long lineNum, JijinSyncFileDTO file) {

        if (lineNum > 2) {

            lineContent = lineContent + "|x"; // append '|x' to let parse correctly
            String[] args = lineContent.split("\\|");

            if (!checkIntegrity(args, file, lineNum)) return null;
            JijinTransferDTO dto = new JijinTransferDTO();
            dto.setFileId(file.getId());
            dto.setInstId(args[0]);
            dto.setChannelId(args[1]);
            dto.setTransferNo(args[2]);
            dto.setAppSheetNo(args[3]);
            dto.setPafOrderNo(args[4]);
            dto.setAmount(new BigDecimal(args[5]));
            dto.setResultFlag(args[6]);
            dto.setTransferDate(args[7]);
            dto.setRemark(args[8]);
            dto.setFundCode(args[9]);
            dto.setStatus("NEW");
            return dto;
        }

        return null;// line 1, just return null
    }
}
