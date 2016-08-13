package com.crh.calculation.specifiedtime;

import com.crh.calculation.mintime.CpMinTime;
import com.crh2.calculate.TrainAttribute;

/**
 * �����г��ٶ�Vn
 * @author huhui
 */
public class VnSpecifiedTime {
	/**
	 * /**
	 * �൱���ٶȼ��㹫ʽ�е�Vn+1
	 */
	private static double Vn=0;
	 /**
	  * �����ٶȹ�ʽ����ʵʱ�ٶ�
	  * @param speed ��һʱ���ٶ�
	  * @param time ����ʱ����
	  * @param cp ���ٶ�
	  * @param maxSpeed �������
	  * @return ��һʱ���ٶ�
	  */
	public static double calSpeed(double speed, double time, double cp, double maxSpeed){
		if(speed == 0){
			Vn = ((CpMinTime.getCp(speed)-cp)/30) * time;
		}else if(0 < speed && speed <= maxSpeed ){//����ٶ����£����������
			Vn = speed + ((CpMinTime.getCp(speed)-cp)/30) * time;
		}else if(speed > maxSpeed){//������ڻ��ߵ�������ٶȣ���������ٶ���������
			if(CpMinTime.hasElectricity == true){//����������е磬����
				Vn = maxSpeed;
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
	
}
