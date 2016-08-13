package com.crh.calculation.mintime;

import com.crh2.calculate.TrainAttribute;

/**
 * 计算参数Cp
 * @author huhui
 */
public class CpMinTime {
	/**
	 *  定义是否有电的标识
	 */
	public static boolean hasElectricity = true;

	/**
	 * 求出C
	 * @param speed
	 * @return
	 */
	public static double getC(double speed) {
		double C = 0;
		// 用于标定当前牵引的状态
		double tractionForce = 0;
		if (hasElectricity) {// 如果在分相区有电
			tractionForce = TractionForceMinTime.getTractionForce(speed);
		}else{
			tractionForce = 0;
		}
		// 求C
		C = tractionForce - AirFrictionMinTime.getAirFriction(speed);
		return C; //N
	}

	/**
	 * 求出Cp
	 * @param speed
	 * @return
	 */
	public static double getCp(double speed) {
		double Cp = 0;
		Cp = (1000 * getC(speed)) / (AirFrictionMinTime.m * 1000 * 9.8);
		return Cp; // N/KN
	}
	
	/**
	 * 2014.10.25 惰行求Cp
	 */
	public static double getCpSlow(double speed){
		double Cp = 0;
		Cp = (1000 * AirFrictionMinTime.getAirFriction(speed)) / (AirFrictionMinTime.m * 1000 * 9.8);
		return Cp; // N/KN
	}
	
	/**
	 * 2014.10.29 常态运行求Cp
	 */
	public static double getCpRealTime(double speed, int brakeLevel){
		double Cp = 0;
		if(TrainAttribute.CRH_IS_BRAKE){ //如果是刹车
			Cp = (Math.abs(BrakeForceMinTime.getBrakingAcceleration(speed, brakeLevel)) * AirFrictionMinTime.m * 1000) / (AirFrictionMinTime.m * 9.8);
		}else{ //如果是加速
			double C = TractionForceMinTime.getTractionForce(speed) - AirFrictionMinTime.getAirFriction(speed);
			Cp = (1000 * C) / (AirFrictionMinTime.m * 1000 * 9.8);
		}
		return Cp;
	}
	
	/**
	 * 2014.12.4 求加速度
	 * @param speed
	 * @return
	 */
	public static double getAcceleration(double speed){
		double a = 0;
		if(speed != TrainAttribute.CRH_MAX_SPEED){
			double c = getC(speed);
			a = c / (AirFrictionMinTime.m * 1000);
		}
		return a;
	}
	
	/**
	 * 惰行时求加速度
	 * @param speed
	 * @return
	 */
	public static double getAcceleretionSlow(double speed){
		double  a = 0;
		double airF = AirFrictionMinTime.getAirFriction(speed);
		a = airF / (AirFrictionMinTime.m * 1000);
		return -a;
	}
	
}
