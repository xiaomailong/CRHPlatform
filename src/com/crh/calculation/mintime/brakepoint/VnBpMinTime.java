package com.crh.calculation.mintime.brakepoint;

import com.crh.calculation.mintime.CpBrakeMinTime;

/**
 * ���򣺼����г��ٶ�Vn
 * @author huhui
 *
 */
public class VnBpMinTime {
	/**
	 * �൱���ٶȼ��㹫ʽ�е�Vn+1
	 */
	private static double Vn=0;
	/**
	 * �����ٶȹ�ʽ����ʵʱ�ٶ�
	 * @param speed ��һʱ���ٶ�
	 * @param time ����ʱ����
	 * @param cp ���ٶ�
	 * @return ��һʱ���ٶ�
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
