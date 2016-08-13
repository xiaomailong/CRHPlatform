package com.crh.calculation.mintime;

import com.crh2.calculate.TrainAttribute;

/**
 * ����·��Sn
 * @author huhui
 */
public class SnMinTime {
	/**
	 * �൱��λ�ƹ�ʽ�е�Sn
	 */
	private static double Sn = 0;
	/**
	 * ����λ��Sn����λ��km
	 * @param Vn
	 * @param V1
	 * @param Si
	 * @return
	 */
	public static double calShift(double Vn,double V1,double Si){
		if(Vn != V1){//�����������
			Sn = Si + ((Vn + V1)*(TrainAttribute.CRH_CAL_TIME_DIVISION/3600))/2;
		}else{//�����˶�
			Sn = Si + Vn*(TrainAttribute.CRH_CAL_TIME_DIVISION/3600);//����ת����Сʱ�������
		}
		return Sn;
	}
	
}
