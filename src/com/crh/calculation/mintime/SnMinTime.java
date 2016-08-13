package com.crh.calculation.mintime;

import com.crh2.calculate.TrainAttribute;

/**
 * 计算路程Sn
 * @author huhui
 */
public class SnMinTime {
	/**
	 * 相当于位移公式中的Sn
	 */
	private static double Sn = 0;
	/**
	 * 计算位移Sn，单位是km
	 * @param Vn
	 * @param V1
	 * @param Si
	 * @return
	 */
	public static double calShift(double Vn,double V1,double Si){
		if(Vn != V1){//如果不是匀速
			Sn = Si + ((Vn + V1)*(TrainAttribute.CRH_CAL_TIME_DIVISION/3600))/2;
		}else{//匀速运动
			Sn = Si + Vn*(TrainAttribute.CRH_CAL_TIME_DIVISION/3600);//将秒转化成小时再求里程
		}
		return Sn;
	}
	
}
