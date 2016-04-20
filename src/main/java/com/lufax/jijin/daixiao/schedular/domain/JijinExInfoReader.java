package com.lufax.jijin.daixiao.schedular.domain;


import com.lufax.jijin.base.utils.EmptyChecker;
import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.daixiao.constant.InvestTypeEnum;
import com.lufax.jijin.daixiao.constant.JijinExValidEnum;
import com.lufax.jijin.daixiao.constant.RecordStatus;
import com.lufax.jijin.daixiao.dto.JijinExInfoDTO;
import com.lufax.jijin.fundation.dto.JijinSyncFileDTO;
import com.site.lookup.util.StringUtils;

import java.math.BigDecimal;

public class JijinExInfoReader {

    /*基金基本要素
    {信息提示}| {总笔数}|{数据起始时间}|{数据截止时间}
    序号|{字段1}|{字段2} |OPDATE
    1|xxxx|xxxx|2015-07-22 18:18:18
    1   基金代码
    2   简称
    3   名称
    4   成立日期
    5   投资类型
    6   管理人
    7   托管人
    8   业绩比较基准
    9   管理费
    10  托管费
    */


    // 校验每条记录的完整性
    private boolean checkIntegrity(String[] args, JijinSyncFileDTO file, long num) {

        if (EmptyChecker.isEmpty(args[1])
                || EmptyChecker.isEmpty(args[2])
                || EmptyChecker.isEmpty(args[3])
                || EmptyChecker.isEmpty(args[5])
                || EmptyChecker.isEmpty(args[6])) {

            Logger.error(this, String.format(
                    "JijinExInfo File check empty failed :[id: %s] [fileName : %s], line num  %s",
                    file.getId(), file.getFileName(), num));
            return false;
        }

        if (args.length != 13) {
            Logger.error(this, String.format(
                    "JijinExInfo File length not correct :[id: %s] [fileName : %s], line num  %s",
                    file.getId(), file.getFileName(), num));
            return false;
        }

        return true;
    }

    public JijinExInfoDTO readLine(String lineContent, long lineNum, JijinSyncFileDTO file) {

        try {
            if (lineNum > 2) {

                lineContent = lineContent + "|x"; // append '|x' to let parse correctly
                String[] args = lineContent.split("\\|");

                if (!checkIntegrity(args, file, lineNum)) return null;

                JijinExInfoDTO dto = new JijinExInfoDTO();
                dto.setFundCode(args[1]);
                dto.setName(args[2]);
                dto.setFullName(args[3]);
                dto.setSetupDate(args[4]);
                dto.setInvestType(InvestTypeEnum.getInvestTypeCodeByName(args[5]));
                dto.setManagementComp(args[6]);
                dto.setCustodianBank(args[7]);
                dto.setBenchMark(args[8]);
                if(StringUtils.isNotEmpty(args[9])){
                	dto.setManagementFee(new BigDecimal(args[9]));
                }
                if(StringUtils.isNotEmpty(args[10])){
                	dto.setTrusteeFee(new BigDecimal(args[10]));
                }
                dto.setIsValid(JijinExValidEnum.IS_VALID.getCode());
                dto.setBatchId(file.getId());

                dto.setStatus(RecordStatus.NEW.name());
                return dto;
            }
        } catch (Exception e) {
        	Logger.error(this, String.format(
                    "JijinExInfo read error :[id: %s] [fileName : %s], line num  %s",
                    file.getId(), file.getFileName(), lineNum),e);
            return null;
        }
        return null;// line 1, just return null
    }

    /*public static void main(String[] args){
    	JijinExInfoReader r = new JijinExInfoReader();
    	r.readLine("68|968006|行健宏扬中国基金RMBHDG|行健宏扬中国基金|20140530|股票型|行健资产管理有限公司|||||2015-12-18 16:43:46", 20, new JijinSyncFileDTO());
    }*/
}
