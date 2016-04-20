package com.lufax.jijin.ylx.confirm.service;

import java.math.BigDecimal;
import java.util.Date;

import com.lufax.jijin.base.utils.EmptyChecker;
import com.lufax.jijin.ylx.batch.dto.YLXBatchDTO;
import com.lufax.jijin.ylx.dto.BuyConfirmDetailDTO;

public class BuyConfirmReader {

	private static final String SPRT = "\\|";
    public BuyConfirmDetailDTO readLine(String lineContent, long lineNum, YLXBatchDTO ylxBatchDTO) {

        if (lineNum > 3) {
            lineContent = lineContent + "|x"; // append '|x' to let parse correctly
            String[] args = lineContent.split(SPRT);

            BuyConfirmDetailDTO dto = new BuyConfirmDetailDTO();
            dto.setInternalTrxId(Long.valueOf(args[0]));
            dto.setBatchId(ylxBatchDTO.getId());
            dto.setResultCode(args[1]);
            dto.setResultInfo(args[2]);
            dto.setBankAccount(args[3]);
            dto.setThirdCustomerAccount(args[4]);
            dto.setThirdAccount(args[5]);
            dto.setThirdAccountType(args[6]);
            dto.setProdCode(args[7]);
            dto.setBuyType(args[8]);
            dto.setPurchaseFee(EmptyChecker.isEmpty(args[9])?new BigDecimal(0):new BigDecimal(args[9]));
            dto.setConfirmFundShare(new BigDecimal(args[10]));
            dto.setAmount(new BigDecimal(args[11]));
            dto.setConfirmUnitPrice(EmptyChecker.isEmpty(args[12])?new BigDecimal(1):new BigDecimal(args[12]));
            dto.setCurrency(args[13]);
            return dto;

        }

        return null;// line 1-3, just return null
    }

    public boolean validateFileHeaderContent(String lineContent) {

        lineContent = lineContent + "|x"; // append '|x' to let parse correctly
        String[] args = lineContent.split(SPRT);
        if (!"交易流水号".equals(args[0])
                || !"处理结果编码".equals(args[1])
                || !"处理结果信息".equals(args[2])
                || !"银行账号".equals(args[3])
                || !"第三方客户号".equals(args[4])
                || !"第三方账号".equals(args[5])
                || !"第三方账号类型".equals(args[6])
                || !"产品代码".equals(args[7])
                || !"买入类型".equals(args[8])
                || !"手续费".equals(args[9])
                || !"确认份额".equals(args[10])
                || !"金额".equals(args[11])
                || !"成交单位净值".equals(args[12])
                || !"币种".equals(args[13])) {
            return false;
        }


        return true;
    }

}


