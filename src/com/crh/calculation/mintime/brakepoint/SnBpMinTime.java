package com.crh.calculation.mintime.brakepoint;

import com.crh2.calculate.TrainAttribute;

/**
 * ����·��Sn
 * @author huhui
 */
public class SnBpMinTime {
	/**
	 * �൱�ڼ��㹫ʽ�е�Sn
	 */
	private static double Sn = 0;
	
	/**
	 * ���㽻���ʱ�����λ��Sn
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
