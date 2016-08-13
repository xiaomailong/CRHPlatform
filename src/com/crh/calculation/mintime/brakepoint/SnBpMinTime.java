package com.crh.calculation.mintime.brakepoint;

import com.crh2.calculate.TrainAttribute;

/**
 * 计算路程Sn
 * @author huhui
 */
public class SnBpMinTime {
	/**
	 * 相当于计算公式中的Sn
	 */
	private static double Sn = 0;
	
	/**
	 * 计算交点的时候计算位移Sn
	 * @param Vn
	 * @param V1
	 * @param Si
	 * @return
	 */
	public static double calShift(double Vn,double V1,double Si){
		Sn = Si + ((Vn + V1)/2) * (TrainAttribute.CRH_CAL_TIME_DIVISION/3600);
		return Sn;
	}
	
}
