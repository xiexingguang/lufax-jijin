package com.lufax.jijin.fundation.schedular.domain;


import com.lufax.jijin.base.utils.EmptyChecker;
import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.fundation.dto.JijinSyncFileDTO;
import com.lufax.jijin.fundation.dto.JijinTradeSyncDTO;

import java.math.BigDecimal;

public class JijinTradeSyncReader {


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
                || EmptyChecker.isEmpty(args[10])
                || EmptyChecker.isEmpty(args[11])
                || EmptyChecker.isEmpty(args[12])
                || EmptyChecker.isEmpty(args[13])
                || EmptyChecker.isEmpty(args[14])
                || EmptyChecker.isEmpty(args[15])
                || EmptyChecker.isEmpty(args[16])
                || EmptyChecker.isEmpty(args[17])
                || EmptyChecker.isEmpty(args[19])
                || EmptyChecker.isEmpty(args[20])
                || EmptyChecker.isEmpty(args[21])) {

            Logger.error(this, String.format(
                    "File is not in the correct format :[id: %s] [fileName : %s], line num  %s",
                    file.getId(), file.getFileName(), num));
            return false;
        }

        return true;
    }


    public JijinTradeSyncDTO readLine(String lineContent, long lineNum, JijinSyncFileDTO file) {

        if (lineNum > 2) {

            lineContent = lineContent + "|x"; // append '|x' to let parse correctly
            String[] args = lineContent.split("\\|");

            if (!checkIntegrity(args, file, lineNum)) return null;

            JijinTradeSyncDTO dto = new JijinTradeSyncDTO();
            dto.setFileId(file.getId());
            dto.setContractNo(args[0]);
            dto.setAppSheetNo(args[1]);
            dto.setTaNo(args[2]);
            dto.setLufaxRequestNo(args[3]);
            dto.setFundCompanyCode(args[4]);
            dto.setTrxDate(args[5]);
            dto.setTrxTime(args[6]);
            dto.setTrxConfirmDate(args[7]);
            dto.setBusinessCode(args[8]);
            dto.setFundCode(args[9]);
            dto.setChargeType(args[10]);
            dto.setNetValue(new BigDecimal(args[11]));
            dto.setPurchaseAmount(new BigDecimal(args[12]));
            dto.setRedeemShare(new BigDecimal(args[13]));
            dto.setConfirmShare(new BigDecimal(args[14]));
            dto.setConfirmAmount(new BigDecimal(args[15]));
            dto.setDividentType(args[16]);
            dto.setTradeResCode(args[17]);
            dto.setTradeResMemo(args[18]);
            dto.setTaConfirmSign(args[19]);
            dto.setBusinessFinishFlag(args[20]);
            dto.setPurchaseFee(new BigDecimal(args[21]));
            dto.setStatus("NEW");
            return dto;

        }

        return null;// line 1, just return null
    }


}
