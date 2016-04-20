package com.lufax.jijin.ylx.batch.domain;

import com.lufax.jijin.ylx.batch.dto.YLXBatchDTO;
import com.lufax.jijin.ylx.dto.YLXBuyRequestDetailDTO;
import com.lufax.jijin.ylx.dto.YLXOpenRequestDetailDTO;
import com.lufax.jijin.ylx.dto.YLXSellRequestDetailDTO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BatchFileContentUtil {
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
    
    public static String createOpenRequestContent(YLXOpenRequestDetailDTO dto,YLXBatchDTO batch){
    	
    	StringBuilder sb = new StringBuilder();
		sb.append(dto.getId()).append(SEPERATOR);//交易流水号 pk id	
		
		String targetDate = dateFormat.format(batch.getTargetDate());
		String targetTime = timeFormat.format(new Date());
		targetTime = targetTime.replace(targetTime.substring(0, 8), targetDate);
		
		sb.append(targetTime).append(SEPERATOR);//交易时间yyyymmddhhmmss, 养老险校验规则规定，必须和文件名里的日期一致
		sb.append(targetDate).append(SEPERATOR);//交易日期yyyymmdd
		sb.append(dto.getVirtualBankAccount()).append(SEPERATOR);//银行虚拟帐号，与银行帐号一致，陆金所用户id
		sb.append(dto.getBankAccount()).append(SEPERATOR);//银行帐号，陆金所用户id
		sb.append(dto.getBankAccountType()).append(SEPERATOR);//银行帐号类型 01
		sb.append(dto.getName()).append(SEPERATOR);//姓名
		sb.append(dto.getSex()).append(SEPERATOR);//性别
		sb.append(dto.getIdType()).append(SEPERATOR);//证件类型
		sb.append(dto.getIdNo()).append(SEPERATOR);//证件号码
		sb.append("").append(SEPERATOR); //证件有效期占位
		sb.append(dto.getMobileNo()).append(SEPERATOR);//电话
		sb.append("").append(SEPERATOR);//邮箱占位
		sb.append("").append(SEPERATOR);//省占位
		sb.append("").append(SEPERATOR);//市占位
		sb.append("").append(SEPERATOR);//区占位
		sb.append(dto.getLufaxBankCode()).append(SEPERATOR);//投资人开户行代码，统一用陆金所开户行
		sb.append(dto.getLufaxBank()).append(SEPERATOR);//投资人开户行
		sb.append(dto.getRiskLevel()).append(SEPERATOR);//客户风险等级
		sb.append("").append(LINECHANGE);//第三方产品代码占位
		
		return sb.toString();

    }
    
    public static String createBuyRequestContent(YLXBuyRequestDetailDTO dto,YLXBatchDTO batch){
    	
    	StringBuilder sb = new StringBuilder();
		sb.append(dto.getId()).append(SEPERATOR);
		
		String targetDate = dateFormat.format(batch.getTargetDate());
		String targetTime = timeFormat.format(new Date());
		targetTime = targetTime.replace(targetTime.substring(0, 8), targetDate);
		
		sb.append(targetTime).append(SEPERATOR);
		sb.append(targetDate).append(SEPERATOR);
		sb.append(dto.getBankAccount()).append(SEPERATOR);
		sb.append("").append(SEPERATOR);//第三方客户号
		sb.append("").append(SEPERATOR);//第三方帐号
		sb.append("").append(SEPERATOR);//第三方帐号类型
		sb.append(dto.getProdCode()).append(SEPERATOR);
		sb.append(dto.getBuyType()).append(SEPERATOR);//买入类型
		sb.append("").append(SEPERATOR);//手续费类型占位
		sb.append("").append(SEPERATOR);//手续费因子占位
		sb.append("").append(SEPERATOR);//申请份额占位
		sb.append(String.format("%.2f", dto.getAmount())).append(SEPERATOR);
		sb.append(dto.getCurrency()).append(LINECHANGE);
		
		return sb.toString();
    }

    public static String createPurchaseRequestContent(YLXBuyRequestDetailDTO dto,YLXBatchDTO batch){

        StringBuilder sb = new StringBuilder();
        sb.append(dto.getId()).append(SEPERATOR);

        String targetDate = dateFormat.format(batch.getTargetDate());
        String targetTime = timeFormat.format(new Date());
        targetTime = targetTime.replace(targetTime.substring(0, 8), targetDate);

        sb.append(targetTime).append(SEPERATOR);
        sb.append(targetDate).append(SEPERATOR);
        sb.append(dto.getBankAccount()).append(SEPERATOR);
        sb.append("").append(SEPERATOR);//第三方客户号
        sb.append("").append(SEPERATOR);//第三方帐号
        sb.append("").append(SEPERATOR);//第三方帐号类型
        sb.append(dto.getProdCode()).append(SEPERATOR);
        sb.append(dto.getBuyType()).append(SEPERATOR);//买入类型
        sb.append("").append(SEPERATOR);//手续费类型占位
        sb.append("").append(SEPERATOR);//手续费因子占位
        sb.append("").append(SEPERATOR);//申请份额占位
        sb.append(String.format("%.2f", dto.getAmount())).append(SEPERATOR);
        sb.append(dto.getCurrency()).append(LINECHANGE);

        return sb.toString();
    }
    
    public static String createRedeemRequestContent(YLXSellRequestDetailDTO dto){
    	
    	StringBuilder sb = new StringBuilder();
		sb.append(dto.getId()).append(SEPERATOR);// use pk id as 交易流水号
		sb.append(timeFormat.format(dto.getTrxTime())).append(SEPERATOR);
		sb.append(dateFormat.format(dto.getTrxDate())).append(SEPERATOR);
		sb.append(dto.getBankAccount()).append(SEPERATOR);
		sb.append(dto.getThirdCustomerAccount()).append(SEPERATOR);//第三方客户号
		sb.append(dto.getThirdAccount()).append(SEPERATOR);//第三方帐号
		sb.append(dto.getThirdAccountType()).append(SEPERATOR);//第三方帐号类型
		sb.append(dto.getProdCode()).append(SEPERATOR);
		sb.append(dto.getSellType()).append(SEPERATOR);//卖出类型
		sb.append("").append(SEPERATOR);//手续费因子类型占位
		sb.append("").append(SEPERATOR);//手续费因子占位
		sb.append(String.format("%.2f", dto.getFundShare())).append(SEPERATOR);//申请份额
		sb.append(String.format("%.2f", dto.getPrincipal())).append(SEPERATOR);//金额=本金
		sb.append(dto.getCurrency()).append(LINECHANGE);
		
		return sb.toString();
    }
	
	public static String createColumnName(YLXBatchType type){
		
		StringBuilder sb = new StringBuilder();
		List<String> columnNames = new ArrayList<String>();
		if(YLXBatchType.YLX_SLP_OPEN_REQUEST==type){
			columnNames.add("交易流水号");
			columnNames.add("交易时间");
			columnNames.add("交易日期");
			columnNames.add("银行虚拟账号");
			columnNames.add("银行账号");
			columnNames.add("银行账号类型");
			columnNames.add("姓名");
			columnNames.add("性别");
			columnNames.add("证件类型");
			columnNames.add("证件号码");
			columnNames.add("证件有效期限");
			columnNames.add("电话");
			columnNames.add("邮箱");
			columnNames.add("省");
			columnNames.add("市");
			columnNames.add("区");
			columnNames.add("投资人开户行代码");
			columnNames.add("投资人开户行");
			columnNames.add("客户风险等级");
			columnNames.add("第三方产品代码");

		}else if(YLXBatchType.YLX_SLP_BUY_REQUEST==type){
			columnNames.add("交易流水号");
			columnNames.add("交易时间");
			columnNames.add("交易日期");
			columnNames.add("银行账号");
			columnNames.add("第三方客户号");
			columnNames.add("第三方账号");
			columnNames.add("第三方账号类型");
			columnNames.add("产品代码");
			columnNames.add("买入类型");
			columnNames.add("手续费类型");
			columnNames.add("手续费因子");
			columnNames.add("申请份额");
			columnNames.add("金额");
			columnNames.add("币种");
	
		}else if(YLXBatchType.YLX_SLP_PURCHASE_REQUEST==type){
            columnNames.add("交易流水号");
            columnNames.add("交易时间");
            columnNames.add("交易日期");
            columnNames.add("银行账号");
            columnNames.add("第三方客户号");
            columnNames.add("第三方账号");
            columnNames.add("第三方账号类型");
            columnNames.add("产品代码");
            columnNames.add("买入类型");
            columnNames.add("手续费类型");
            columnNames.add("手续费因子");
            columnNames.add("申请份额");
            columnNames.add("金额");
            columnNames.add("币种");

        }else if(YLXBatchType.YLX_SLP_REDEEM_REQUEST ==type){
			columnNames.add("交易流水号");
			columnNames.add("交易时间");
			columnNames.add("交易日期");
			columnNames.add("银行账号");
			columnNames.add("第三方客户号");
			columnNames.add("第三方账号");
			columnNames.add("第三方账号类型");
			columnNames.add("产品代码");
			columnNames.add("卖出类型");
			columnNames.add("手续费因子类型");
			columnNames.add("手续费因子");
			columnNames.add("申请份额");
			columnNames.add("金额");
			columnNames.add("币种");
		}else{
			sb.append("not support！");
		}
		
		for(String column:columnNames){
			sb.append(column).append(SEPERATOR);
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append(LINECHANGE);
		
		return sb.toString();
	}

}
