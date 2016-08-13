package com.crh.calculation.mintime;

/**
 * 计算制动时候所产生的加速度Cp
 * @author huhui
 */
public class CpBrakeMinTime {
	
	/**
	 * 计算C
	 * @param speed
	 * @return
	 */
	public static double getC(double speed){//km/h
		double tractionForce = Math.abs(BrakeForceMinTime.getBrakingAcceleration(speed, 9)) * AirFrictionMinTime.m;
		double C = tractionForce;//KN
		return C;
	}
	
	/**
	 * 计算Cp
	 * @param speed
	 * @param cp
	 * @return
	 */
	public static double getCp(double speed, double cp){
		double Cp = getC(speed)*1000/(AirFrictionMinTime.m * 9.8) - cp;
		return Cp;
	}
	
}
