package com.lufax.jijin.fundation.constant;

import java.util.HashSet;
import java.util.Set;
/**
 * 基金状态
 * @author chenqunhui
 *
 */
public class JijinStatus {

	/**
	 * 申购状态未设置
	 */
	public static final String BUY_STATUS_NEW="NEW";
	/**
	 * 开放认购
	 */
	public static final String BUY_STATUS_SUB_OPEN="SUB_OPEN";
	/**
	 * 暂停认购
	 */
	public static final String BUY_STATUS_SUB_PAUSE="SUB_PAUSE";
	/**
	 * 认购失败
	 */
	public static final String BUY_STATUS_SUB_FAILED="SUB_FAILED";
	/**
	 * 申购封闭期
	 */
	public static final String BUY_STATUS_CLOSE="CLOSE";
	/**
	 * 开放申购
	 */
	public static final String BUY_STATUS_PUR_OPEN="PUR_OPEN";
	/**
	 * 暂停申购
	 */
	public static final String BUY_STATUS_PUR_CLOSE="PUR_PAUSE";
	
	/**
	 * 赎回封闭期
	 */
	public static final String REDEMPTION_STATU_CLOSE="CLOSE";
	/**
	 * 开放赎回
	 */
	public static final String REDEMPTION_STATU_RED_OPEN="RED_OPEN";
	/**
	 * 暂停赎回
	 */
	public static final String REDEMPTION_STATU_RED_PAUSE="RED_PAUSE";
	
	public static Set<String> buyStatusSet = new HashSet<String>();
	
	public static Set<String> redStatusSet = new HashSet<String>();
	
	static{
		buyStatusSet.add(BUY_STATUS_CLOSE);
		buyStatusSet.add(BUY_STATUS_NEW);
		buyStatusSet.add(BUY_STATUS_PUR_CLOSE);
		buyStatusSet.add(BUY_STATUS_PUR_OPEN);
		buyStatusSet.add(BUY_STATUS_SUB_FAILED);
		buyStatusSet.add(BUY_STATUS_SUB_OPEN);
		buyStatusSet.add(BUY_STATUS_SUB_PAUSE);
		redStatusSet.add(REDEMPTION_STATU_CLOSE);
		redStatusSet.add(REDEMPTION_STATU_RED_OPEN);
		redStatusSet.add(REDEMPTION_STATU_RED_PAUSE);
	}
	
	public static boolean isBuyStatus(String stauts){
		return buyStatusSet.contains(stauts);
	}
	
	public static boolean isRedStatus(String status){
		return redStatusSet.contains(status);
	}
}
