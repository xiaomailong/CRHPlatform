package com.crh.calculation.mintime.brakepoint;

import com.crh.calculation.mintime.CpBrakeMinTime;

/**
 * 反向：计算列车速度Vn
 * @author huhui
 *
 */
public class VnBpMinTime {
	/**
	 * 相当于速度计算公式中的Vn+1
	 */
	private static double Vn=0;
	/**
	 * 根据速度公式计算实时速度
	 * @param speed 上一时刻速度
	 * @param time 计算时间间隔
	 * @param cp 加速度
	 * @return 下一时刻速度
	 */
	public static double calSpeed(double speed,double time,double cp){
		if(speed == 0){
			Vn = (CpBrakeMinTime.getCp(speed, cp) * 9.8 * time * 3.6)/1000.0;
		}else{
			Vn = speed + (CpBrakeMinTime.getCp(speed, cp) * 9.8 * time * 3.6)/1000.0;
		}
		return Math.abs(Vn);
	}
	
}
