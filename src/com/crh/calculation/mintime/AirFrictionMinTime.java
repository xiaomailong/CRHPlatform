package com.crh.calculation.mintime;

/**
 * 计算基本空气阻力
 * @author huhui
 */
public class AirFrictionMinTime {
	/**
	 * 空气阻力公式系数
	 */
	public static double a = 0, b = 0, c = 0, m = 0;
	/**
	 * 根据公式计算空气阻力大小，单位N
	 * @param speed 当前速度
	 * @return 空气阻力
	 */
	public static double getAirFriction(double speed){
		double f = 0;
		f = (a + b * speed + c * speed * speed) * 1000;
		return f;//单位N
	}
}
