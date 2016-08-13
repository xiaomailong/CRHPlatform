package com.crh.calculation.mintime;

import com.crh2.calculate.TrainAttribute;

/**
 * �����г��ٶ�Vn
 * @author huhui
 */
public class VnMinTime {
	
	/**
	 * ʵʱ���е�Vn
	 */
	public static double VnRT = 0;
	
	/**
	 * �����ٶ�
	 * @param speed
	 * @param time
	 * @param cp
	 * @return
	 */
	public static double calSpeed(double speed,double time,double cp){
		double Vn = 0;
		if(speed == 0){
			Vn = ((CpMinTime.getCp(speed)-cp)/30) * time;
		}else if(0 < speed && speed < TrainAttribute.CRH_MAX_SPEED ){//����ٶ����£����������
			Vn = speed + ((CpMinTime.getCp(speed)-cp)/30) * time;
		}else if(speed >= TrainAttribute.CRH_MAX_SPEED){//������ڻ��ߵ�������ٶȣ���������ٶ���������
			if(CpMinTime.hasElectricity == true){//����������е磬����
				Vn = TrainAttribute.CRH_MAX_SPEED;
				//���ö�����״̬Ϊ����
				TrainAttribute.CRH_IS_CONSTANT_SPEED= true;
			}else{//���û�磬������������
				Vn = speed + ((CpMinTime.getCp(speed)-cp)/30) * time;
				//���ö�����״̬Ϊ������
				TrainAttribute.CRH_IS_CONSTANT_SPEED= false;
			}
		}
		return Vn;
	}
	
	/**
	 *  2014.10.25 �������ٶ�
	 * @param speed
	 * @param time
	 * @param cp
	 * @return
	 */
	public static double calSpeedSlow(double speed,double time,double cp){
		double Vn = 0; //�൱��Vn+1
		if(0 < speed && speed < TrainAttribute.CRH_MAX_SPEED ){//����ٶ����£����������
			Vn = speed + ((CpMinTime.getCpSlow(speed)-cp)/30) * time;
		}
		return Vn;
	}
	
	/**
	 * 2014.10.29 ��̬�������ٶ�
	 * @param speed
	 * @param time
	 * @param cp
	 * @param brakeLevel
	 * @return
	 */
	public static double calSpeedRealTime(double speed,double time,double cp, int brakeLevel){
		if(speed == 0){
			VnRT = ((CpMinTime.getCpRealTime(speed, brakeLevel)-cp)/30) * time;
		}else if(0 < speed && speed < TrainAttribute.CRH_MAX_SPEED ){//����ٶ����£����������
			double tempV = ((CpMinTime.getCpRealTime(speed, brakeLevel)-cp)/30) * time;
			if(TrainAttribute.CRH_IS_BRAKE){
				VnRT = speed - tempV;
			}else{
				VnRT = speed + tempV;
			}
		}else if(speed >= TrainAttribute.CRH_MAX_SPEED){//������ڻ��ߵ�������ٶȣ���������ٶ���������
			VnRT = TrainAttribute.CRH_MAX_SPEED;
			if(TrainAttribute.CRH_IS_BRAKE){
				VnRT -= 0.001;
			}
		}
		return VnRT < 0 ? 0 : VnRT;
	}
	
}
