package com.crh.calculation.mintime;

/**
 * �����ƶ�ʱ���������ļ��ٶ�Cp
 * @author huhui
 */
public class CpBrakeMinTime {
	
	/**
	 * ����C
	 * @param speed
	 * @return
	 */
	public static double getC(double speed){//km/h
		double tractionForce = Math.abs(BrakeForceMinTime.getBrakingAcceleration(speed, 9)) * AirFrictionMinTime.m;
		double C = tractionForce;//KN
		return C;
	}
	
	/**
	 * ����Cp
	 * @param speed
	 * @param cp
	 * @return
	 */
	public static double getCp(double speed, double cp){
		double Cp = getC(speed)*1000/(AirFrictionMinTime.m * 9.8) - cp;
		return Cp;
	}
	
}
