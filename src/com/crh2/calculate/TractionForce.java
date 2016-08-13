package com.crh2.calculate;

import com.crh.calculation.mintime.TractionForceMinTime;
import com.crh.calculation.mintime.TractionLevelForceMinTime;

/**
 * 计算牵引力
 */
public class TractionForce {
	
	public static double Fst = 0;
	public static double F1 = 0;
	public static double F2 = 0;
	public static double v1 = 0;
	public static double v2 = 0;
	public static double P1 = 0;

	/**
	 * 计算牵引力（牵引状态下）
	 * @param speed 单位 m/s
	 * @return
	 */
	public static double getTractionForce(double speed) {
		double F = 0.0;
		double v = speed * 3.6;
		if (TractionForceMinTime.tractionType == 0) {
			if (v >= 0 && v <= v1) {
				F = 1000 * (Fst - (((Fst - F1) / v1) * v));
			} else if (v > v1 && v <= v2) {
				F = 1000 * ((3.6 * P1) / v);
			} else if (v > v2) {
				F = 1000 * ((F2 * v2 * v2) / (v * v));
			}
			return F;// 单位是N
		}else if(TractionForceMinTime.tractionType == 1){
			F = TractionLevelForceMinTime.getTractionLevelForce(TractionForceMinTime.level, v) * 1000;// 单位是N
			return F;
		}else{
			return F;
		}
	}
	
}
