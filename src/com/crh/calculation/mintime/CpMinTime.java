package com.crh.calculation.mintime;

import com.crh2.calculate.TrainAttribute;

/**
 * �������Cp
 * @author huhui
 */
public class CpMinTime {
	/**
	 *  �����Ƿ��е�ı�ʶ
	 */
	public static boolean hasElectricity = true;

	/**
	 * ���C
	 * @param speed
	 * @return
	 */
	public static double getC(double speed) {
		double C = 0;
		// ���ڱ궨��ǰǣ����״̬
		double tractionForce = 0;
		if (hasElectricity) {// ����ڷ������е�
			tractionForce = TractionForceMinTime.getTractionForce(speed);
		}else{
			tractionForce = 0;
		}
		// ��C
		C = tractionForce - AirFrictionMinTime.getAirFriction(speed);
		return C; //N
	}

	/**
	 * ���Cp
	 * @param speed
	 * @return
	 */
	public static double getCp(double speed) {
		double Cp = 0;
		Cp = (1000 * getC(speed)) / (AirFrictionMinTime.m * 1000 * 9.8);
		return Cp; // N/KN
	}
	
	/**
	 * 2014.10.25 ������Cp
	 */
	public static double getCpSlow(double speed){
		double Cp = 0;
		Cp = (1000 * AirFrictionMinTime.getAirFriction(speed)) / (AirFrictionMinTime.m * 1000 * 9.8);
		return Cp; // N/KN
	}
	
	/**
	 * 2014.10.29 ��̬������Cp
	 */
	public static double getCpRealTime(double speed, int brakeLevel){
		double Cp = 0;
		if(TrainAttribute.CRH_IS_BRAKE){ //�����ɲ��
			Cp = (Math.abs(BrakeForceMinTime.getBrakingAcceleration(speed, brakeLevel)) * AirFrictionMinTime.m * 1000) / (AirFrictionMinTime.m * 9.8);
		}else{ //����Ǽ���
			double C = TractionForceMinTime.getTractionForce(speed) - AirFrictionMinTime.getAirFriction(speed);
			Cp = (1000 * C) / (AirFrictionMinTime.m * 1000 * 9.8);
		}
		return Cp;
	}
	
	/**
	 * 2014.12.4 ����ٶ�
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
	 * ����ʱ����ٶ�
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
