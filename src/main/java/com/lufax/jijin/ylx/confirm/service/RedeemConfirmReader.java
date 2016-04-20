package com.lufax.jijin.ylx.confirm.service;

import com.google.common.base.Splitter;
import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.ylx.batch.dto.YLXBatchDTO;
import com.lufax.jijin.ylx.dto.YLXSellConfirmDetailDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

@Component
public class RedeemConfirmReader {

	private static final String SPRT = "\\|";
    public YLXSellConfirmDetailDTO readLine(String lineContent, long lineNum, YLXBatchDTO ylxBatchDTO) {

        if (lineNum > 3) {
            lineContent = lineContent + "|x"; // append '|x' to let parse correctly
            String[] args = lineContent.split(SPRT);
            YLXSellConfirmDetailDTO dto = new YLXSellConfirmDetailDTO();
            dto.setInternalTrxId(Long.valueOf(args[0]));
            dto.setBatchId(ylxBatchDTO.getId());
            dto.setResultCode(args[1]);
            dto.setResultInfo(args[2]);
            dto.setBankAccount(args[3]);
            dto.setThirdCustomerAccount(args[4]);
            dto.setThirdAccount(args[5]);
            dto.setThirdAccountType(args[6]);
            dto.setProdCode(args[7]);
            dto.setSellType(args[8]);
            try{
            	dto.setCommissionFee(new BigDecimal(args[9]));
            }catch(Exception e){
            	dto.setCommissionFee(BigDecimal.ZERO);
            }
          //全部金额args[10]
            try{
            	dto.setConfirmFundShare(new BigDecimal(args[11]));
            }catch(Exception e){
            	dto.setConfirmFundShare(BigDecimal.ZERO);
            }
            try{
            	dto.setAmount(new BigDecimal(args[12]));
            }catch(Exception e){
            	dto.setAmount(BigDecimal.ZERO);
            }
			try{
				dto.setConfirmUnitPrice(new BigDecimal(args[13]));
			}catch(Exception e){
				dto.setConfirmUnitPrice(BigDecimal.ZERO);
			}
            dto.setCurrency(args[14]);
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
                || !"卖出类型".equals(args[8])
                || !"手续费".equals(args[9])
                || !"全部金额".equals(args[10])
                || !"确认份额".equals(args[11])
                || !"到账金额".equals(args[12])
                || !"成交单位净值".equals(args[13])
                || !"币种".equals(args[14])) {
            return false;
        }

        return true;
    }

}


