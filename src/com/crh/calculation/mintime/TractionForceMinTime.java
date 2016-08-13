package com.crh.calculation.mintime;

/**
 * 计算牵引力
 * @author huhui
 *
 */
public class TractionForceMinTime {
	/**
	 * 牵引力计算公式中的参数
	 */
	public static double Fst = 0;
	public static double F1 = 0;
	public static double F2 = 0;
	public static double v1 = 0;
	public static double v2 = 0;
	public static double P1 = 0;
	public static double mode = 0;
	
	/**
	 * 2015.1.18增加，0为无级(默认)，1为有级
	 */
	public static int tractionType = 0;
	/**
	 * 牵引级别
	 */
	public static int level = 0;

	/**
	 *  计算牵引力（牵引状态下）
	 * @param speed 单位km/h
	 * @return 牵引力，单位是N
	 */
	public static double getTractionForce(double speed) {//speed单位km/h
		double F = 0.0;
		double v = speed;
		if (tractionType == 0) {//无级(默认)
			if (v >= 0 && v <= v1) {
				F = 1000 * (Fst - (((Fst - F1) / v1) * v));
			} else if (v > v1 && v <= v2) {
				F = 1000 * ((3.6 * P1) / v);
			} else if (v > v2) {
				F = 1000 * ((F2 * v2 * v2) / (v * v));
			}
			return F * mode;// 单位是N
		}else if(tractionType == 1){//有级
			F = TractionLevelForceMinTime.getTractionLevelForce(level, v) * 1000;// 单位是N
			return F;
		}else{
			return 0;
		}
	}
}
