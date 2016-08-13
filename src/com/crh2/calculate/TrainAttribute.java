package com.crh2.calculate;

/**
 * 动车组的属性
 * @author huhui
 *
 */
public class TrainAttribute {
	/**
	 *  动车组质量
	 */
	public static final double CRH_WEIGHT = 475000;// 质量为475t
	/**
	 *  动车组参数计算时间分度
	 */
	public static double CRH_CAL_TIME_DIVISION = 1;
	/**
	 *  动车组是否进入匀速运行状态，初始化为false，即非匀速
	 */
	public static boolean CRH_IS_CONSTANT_SPEED = false;
	/**
	 *  动车组终点站的里程
	 */
	public static double CRH_DESTINATION_LOCATION = 0;
	/**
	 *  初始化界面时，默认的routeid
	 */
	public static final int CRH_DEFAULT_ROUTE_ID = 1;
	/**
	 * 计算是否开始，0表示没有开始计算，1表示已经开始
	 */
	public static int CRH_IS_RUN = 0;
	
	// 以下数据是从UDP接收
	/**
	 * 牵引是否开始，false表示没有开始，true表示已经开始
	 */
	public static boolean CRH_IS_START = false;
	/**
	 *  是否制动
	 */
	public static boolean CRH_IS_BRAKE = false;
	/**
	 *  制动级别
	 */
	public static int CRH_BRAKE_LEVEL = 0;
	/**
	 *  动车组最高速度
	 */
	public static double CRH_MAX_SPEED = 0;
	/**
	 *  是否重置， 1表示重置，0表示正常
	 */
	public static int CRH_IS_RESET = 0;
	/**
	 *  临时变量
	 */
	public static int CRH_TEMP = 1;
	
}
