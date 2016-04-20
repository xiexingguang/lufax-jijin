package com.lufax.jijin.ylx.confirm.service;

import com.lufax.jijin.ylx.batch.dto.YLXBatchDTO;
import com.lufax.jijin.ylx.dto.OpenConfirmDetailDTO;

public class OpenConfirmReader {

	private static final String SPRT = "\\|";

    public OpenConfirmDetailDTO readLine(String lineContent, long lineNum, YLXBatchDTO batch) {

        if (lineNum > 3) {
        	String[] arr = (lineContent + "|abc").split(SPRT);
    		OpenConfirmDetailDTO dto = new OpenConfirmDetailDTO();
    		dto.setInternalTrxId(Long.valueOf(arr[0]));
    		dto.setBatchId(batch.getId());
    		dto.setResultCode(arr[1]);
    		dto.setResultInfo(arr[2]);
    		dto.setBankAccount(arr[3]);
    		dto.setThirdCustomerAccount(arr[4]);
    		dto.setThirdAccount(arr[5]);
    		dto.setThirdAccountType(arr[6]);
    		dto.setName(arr[7]);
    		dto.setIdentityType(arr[8]);
    		dto.setIdentityNum(arr[9]);
            return dto;
        }

        return null;// line 1-3, just return null
    }
    
    public boolean validateFileHeaderContent(String lineContent){

        lineContent = lineContent+"|x"; // append '|x' to let parse correctly
        String[] args = lineContent.split(SPRT);
        if(!"交易流水号".equals(args[0])
                ||!"处理结果编码".equals(args[1])
                ||!"处理结果信息".equals(args[2])
                ||!"银行账号".equals(args[3])
                ||!"第三方客户号".equals(args[4])
                ||!"第三方账号".equals(args[5])
                ||!"第三方账号类型".equals(args[6])
                ||!"姓名".equals(args[7])
                ||!"证件类型".equals(args[8])
                ||!"证件号码".equals(args[9])){
            return false;
        }

        return true;
    }
}
