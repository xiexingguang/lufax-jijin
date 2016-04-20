package com.lufax.jijin.daixiao.schedular.domain;


import com.lufax.jijin.base.utils.EmptyChecker;
import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.daixiao.constant.JijinExValidEnum;
import com.lufax.jijin.daixiao.constant.RatingGagencyEnum;
import com.lufax.jijin.daixiao.constant.RecordStatus;
import com.lufax.jijin.daixiao.dto.JijinExGradeDTO;
import com.lufax.jijin.fundation.dto.JijinSyncFileDTO;

public class JijinExGradeReader {

    /*机构评级
    {信息提示}| {总笔数}|{数据起始时间}|{数据截止时间}
    序号|{字段1}|{字段2} |OPDATE
    1|xxxx|xxxx|2015-07-22 18:18:18
    1   基金代码
    2   评级日期
    3   星级别
    4   基金类型
    5   评级区间
    6   评级机构
    */


    // 校验每条记录的完整性
    private boolean checkIntegrity(String[] args, JijinSyncFileDTO file, long num) {

        if (EmptyChecker.isEmpty(args[1]) || EmptyChecker.isEmpty(args[2])
                || EmptyChecker.isEmpty(args[6])
                || EmptyChecker.isEmpty(args[5])) {

            Logger.error(this, String.format(
                    "JijinExGrade File check empty failed :[id: %s] [fileName : %s], line num  %s",
                    file.getId(), file.getFileName(), num));
            return false;
        }

        if (args.length != 9) {
            Logger.error(this, String.format(
                    "JijinExGrade File length not correct :[id: %s] [fileName : %s], line num  %s",
                    file.getId(), file.getFileName(), num));
            return false;
        }

        return true;
    }


    public JijinExGradeDTO readLine(String lineContent, long lineNum, JijinSyncFileDTO file) {

        try {
            if (lineNum > 2) {

                lineContent = lineContent + "|x"; // append '|x' to let parse correctly
                String[] args = lineContent.split("\\|");

                if (!checkIntegrity(args, file, lineNum)) return null;

                JijinExGradeDTO dto = new JijinExGradeDTO();
                dto.setFundCode(args[1]);
                dto.setRateDate(args[2]);
                dto.setStarLevel(args[3]);
                dto.setFundType(args[4]);
                if (args[5].equals("1年")) {
                    dto.setRatingInterval("1");
                } else if (args[5].equals("2年")) {
                    dto.setRatingInterval("2");
                } else if (args[5].equals("3年")) {
                    dto.setRatingInterval("3");
                } else if (args[5].equals("4年")) {
                    dto.setRatingInterval("4");
                } else if (args[5].equals("5年")) {
                    dto.setRatingInterval("5");
                }
                if (dto.getRatingInterval().equals("3")) {
                    dto.setStatus(RecordStatus.NEW.name());
                } else {
                    dto.setStatus(RecordStatus.NO_USED.name());
                }
                dto.setIsValid(JijinExValidEnum.IS_NOT_VALID.getCode());
                dto.setRatingGagency(RatingGagencyEnum.getGagenCyCodeByName(args[6]));
                dto.setBatchId(file.getId());

                return dto;
            }
        } catch (Exception e) {
            return null;
        }
        return null;// line 1, just return null
    }
}
