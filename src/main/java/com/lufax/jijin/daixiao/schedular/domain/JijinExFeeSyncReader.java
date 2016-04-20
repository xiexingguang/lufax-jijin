package com.lufax.jijin.daixiao.schedular.domain;


import com.lufax.jijin.base.utils.EmptyChecker;
import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.base.utils.NumberUtils;
import com.lufax.jijin.daixiao.constant.HoldPeriodUnitEnum;
import com.lufax.jijin.daixiao.constant.JijinExFeeTypeEnum;
import com.lufax.jijin.daixiao.dto.JijinExFeeDTO;
import com.lufax.jijin.fundation.dto.JijinSyncFileDTO;
import com.site.lookup.util.StringUtils;

import java.math.BigDecimal;

public class JijinExFeeSyncReader {

    /*费率
    {信息提示}| {总笔数}|{数据起始时间}|{数据截止时间}
    序号|{字段1}|{字段2} |OPDATE
    1|xxxx|xxxx|2015-07-22 18:18:18
    1   基金代码
    2   费率类型代码
    3   收费类型
    4   金额上限  单位为万元，故落地时直接乘10000
    5   金额下限  单位为万元，故落地时直接乘10000
    6   持有年限上限
    7   持有年限下限
    8   费率
    9   变动日期
    10  费率补充说明
    11  持有期限单位
    12 　固定值
    13  OPDATE
    */


    // 校验每条记录的完整性
    private boolean checkIntegrity(String[] args, JijinSyncFileDTO file, long num) {

        if (EmptyChecker.isEmpty(args[0]) || EmptyChecker.isEmpty(args[1])
                || EmptyChecker.isEmpty(args[2])) {

            Logger.error(this, String.format(
                    "File is not in the correct format :[id: %s] [fileName : %s], line num  %s",
                    file.getId(), file.getFileName(), num));
            return false;
        }

        //兼容
        if (args.length != 15) {
            Logger.error(this, String.format(
                    "JijinExFee File length not correct :[id: %s] [fileName : %s], line num  %s",
                    file.getId(), file.getFileName(), num));
            return false;
        }

        return true;
    }


    public JijinExFeeDTO readLine(String lineContent, long lineNum, JijinSyncFileDTO file) {

        try {
            if (lineNum > 2) {

                lineContent = lineContent + "|x"; // append '|x' to let parse correctly
                String[] args = lineContent.split("\\|");

                if (!checkIntegrity(args, file, lineNum)) return null;

                JijinExFeeDTO dto = new JijinExFeeDTO();
                dto.setFundCode(args[1]);
                dto.setFeeType(JijinExFeeTypeEnum.getFeeTypeCodeByName(args[2]));//将中文转换为code  认购费率20 申购费率22 赎回费率24
                if (args[3] == null || "".equals(args[3].trim())) {
                    dto.setChargeType("前端");
                } else {
                    dto.setChargeType(args[3]);
                }

                BigDecimal upperLimitAmount = NumberUtils.string2BigDecimal(args[4]);
                BigDecimal lowerLimitAmount = NumberUtils.string2BigDecimal(args[5]);
                if (upperLimitAmount != null) {
                    upperLimitAmount = upperLimitAmount.multiply(new BigDecimal(10000));
                }
                if (lowerLimitAmount != null) {
                    lowerLimitAmount = lowerLimitAmount.multiply(new BigDecimal(10000));
                }

                dto.setUpperLimitAmount(upperLimitAmount);
                dto.setLowerLimitAmount(lowerLimitAmount);
                dto.setUpperLimitHoldYear(NumberUtils.string2BigDecimal(args[6]));
                dto.setLowerLimitHoldYear(NumberUtils.string2BigDecimal(args[7]));
                dto.setFee(NumberUtils.string2BigDecimal(args[8]));
                dto.setChangeDate(args[9]);
                dto.setFeeMemo(args[10]);
                //天("1"),周("2"),月("3"),年("4");
                dto.setHoldPeriodUnit(HoldPeriodUnitEnum.getUnitValueByName(args[11]));
                if(StringUtils.isNotEmpty(args[12])){
                	try{
                		dto.setFixValue(new BigDecimal(args[12]));
                	}catch(Exception e){
                		Logger.error(this,String.format("read jijinExFeeSyncFile error because the value of fixValue is not numeric,file id=[%s] line num=[%s]",file.getId(),lineNum));
                	}
                }
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
