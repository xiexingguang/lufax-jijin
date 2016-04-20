package com.lufax.jijin.fundation.util;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.lufax.jijin.fundation.dto.JijinTradeRecordDTO;

public class ReconFileContentUtil {
    public static final String SEPERATOR = "|";
    public static final String LINECHANGE = "\n";
    private static SimpleDateFormat timeFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");


	public static String getStrSeq(int input){
		
		String num = String.valueOf(input);
		int loop = 3-num.length();
		for(int i=0;i<loop;i++)
			num="0"+num;
		return num;
	}
    
	public static int getIntSeq(String fileName){
		
		int po = fileName.indexOf(".");
		String seq = fileName.substring(po-3,po);
			
		return Integer.valueOf(seq);
	}
	
	public static String createReconFileHeader(int totalCount,BigDecimal totalAmount){
		
		StringBuilder sb = new StringBuilder();
		sb.append("0").append(SEPERATOR).append(SEPERATOR)
		.append(totalCount).append(SEPERATOR)
		.append(totalAmount).append(SEPERATOR)
		.append(totalCount).append(SEPERATOR)
		.append(totalAmount).append(LINECHANGE);;
		
		return sb.toString();
		
	}
    
    public static String createReconFileContent(JijinTradeRecordDTO dto,String payNo){
    	
    	StringBuilder sb = new StringBuilder();
		sb.append(dto.getAppSheetNo()).append(SEPERATOR);//基金公司申购订单号	
		sb.append(dto.getAppNo()).append(SEPERATOR);//陆金所订单号
		sb.append(payNo).append(SEPERATOR);//平安付签约协议号

		sb.append(dto.getContractNo()).append(SEPERATOR);//
		sb.append(dto.getInstId()).append(SEPERATOR);//
		sb.append(dto.getFundCode()).append(SEPERATOR);//
		sb.append("0").append(SEPERATOR);//申购结果 0 表示成功
		sb.append(dto.getErrorMsg()==null?"":dto.getErrorMsg()).append(SEPERATOR);//返回信息
		sb.append("0").append(SEPERATOR);//订单状态
		sb.append(dto.getReqAmount()).append(SEPERATOR);//订单金额
		sb.append("1").append(SEPERATOR); //1 RMB
		sb.append(dto.getTrxTime()).append(SEPERATOR).append(LINECHANGE);// 交易时间
		
		return sb.toString();

    }

	public static String createReconFileColumnName(){
		
		StringBuilder sb = new StringBuilder();
		List<String> columnNames = new ArrayList<String>();
			columnNames.add("基金公司申购订单号");
			columnNames.add("陆金所订单号");
			columnNames.add("陆金所基金交易账号");
			columnNames.add("用户在基金公司的账号");
			columnNames.add("基金公司号");
			columnNames.add("基金代码");
			columnNames.add("申购结果");
			columnNames.add("返回信息");
			columnNames.add("订单状态");
			columnNames.add("订单金额");
			columnNames.add("币种");
			columnNames.add("交易时间");
			columnNames.add("备注");
		
		for(String column:columnNames){
			sb.append(column).append(SEPERATOR);
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append(LINECHANGE);
		
		return sb.toString();
	}

}
