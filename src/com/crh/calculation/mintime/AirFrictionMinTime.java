package com.crh.calculation.mintime;

/**
 * ���������������
 * @author huhui
 */
public class AirFrictionMinTime {
	/**
	 * ����������ʽϵ��
	 */
	public static double a = 0, b = 0, c = 0, m = 0;
	/**
	 * ���ݹ�ʽ�������������С����λN
	 * @param speed ��ǰ�ٶ�
	 * @return ��������
	 */
	public static double getAirFriction(double speed){
		double f = 0;
		f = (a + b * speed + c * speed * speed) * 1000;
		return f;//��λN
	}
}
