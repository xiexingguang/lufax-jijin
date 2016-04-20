package com.lufax.jijin.fundation.util;

import com.lufax.jijin.base.utils.DateUtils;
import com.lufax.jijin.base.utils.NumberUtils;
import com.lufax.jijin.fundation.constant.TradeRecordType;
import com.lufax.jijin.fundation.dto.JijinTradeRecordDTO;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class PurchaseFileContentUtil {
    public static final String SEPERATOR = "|";
    public static final String LINECHANGE = "\n";


    public static String getStrSeq(int input) {

        String num = String.valueOf(input);
        int loop = 3 - num.length();
        for (int i = 0; i < loop; i++)
            num = "0" + num;
        return num;
    }

    public static int getIntSeq(String fileName) {

        int po = fileName.indexOf(".");
        String seq = fileName.substring(po - 3, po);

        return Integer.valueOf(seq);
    }

    public static String createPurchaseFileHeader(int totalCount) {
        StringBuilder sb = new StringBuilder();
        sb.append(totalCount).append(LINECHANGE);
        ;
        return sb.toString();

    }

    public static String createPurchaseFileContent(JijinTradeRecordDTO dto) {

        StringBuilder sb = new StringBuilder();
        sb.append(dto.getInstId()).append(SEPERATOR);//机构标识
        sb.append(dto.getContractNo()).append(SEPERATOR);//基金公司用户交易账号
        sb.append(dto.getAppNo()).append(SEPERATOR);//lufax申请流水号
        sb.append(StringUtils.isBlank(dto.getAppSheetNo()) ? "" : dto.getAppSheetNo()).append(SEPERATOR);//基金公司流水号
        sb.append(dto.getType().equals(TradeRecordType.PURCHASE) ? "022" : "020").append(SEPERATOR);//业务代码
        sb.append(dto.getFundCode()).append(SEPERATOR);//基金代码
        sb.append(NumberUtils.stringFormat(dto.getReqAmount())).append(SEPERATOR);//申请金额
        sb.append(dto.getType().name()).append(SEPERATOR);//类型
        sb.append(StringUtils.isBlank(dto.getTrxDate()) ? "" : dto.getTrxDate()).append(SEPERATOR);//所属交易日
        sb.append(DateUtils.formatDateAsCmsDrawSequence(dto.getCreatedAt())).append(SEPERATOR);//申请时间
        sb.append(dto.getStatus()).append(SEPERATOR); //状态
        sb.append(StringUtils.isBlank(dto.getErrorCode()) ? "" : dto.getErrorCode()).append(SEPERATOR);//返回错误码
        sb.append(dto.getErrorMsg() == null ? "" : dto.getErrorMsg()).append(LINECHANGE);//返回错误信息

        return sb.toString();

    }

    public static String createPurchaseFileColumnName() {

        StringBuilder sb = new StringBuilder();
        List<String> columnNames = new ArrayList<String>();
        columnNames.add("机构标识");
        columnNames.add("基金公司用户交易账号");
        columnNames.add("Lufax申请流水号");
        columnNames.add("基金公司流水号");
        columnNames.add("业务代码");
        columnNames.add("基金代码");
        columnNames.add("申请金额");
        columnNames.add("类型");
        columnNames.add("所属交易日");
        columnNames.add("申请时间");
        columnNames.add("状态");
        columnNames.add("返回错误码");
        columnNames.add("返回错误信息");

        for (String column : columnNames) {
            sb.append(column).append(SEPERATOR);
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(LINECHANGE);

        return sb.toString();
    }

}
