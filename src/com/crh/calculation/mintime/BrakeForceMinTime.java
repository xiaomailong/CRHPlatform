package com.crh.calculation.mintime;

import java.util.ArrayList;

import com.crh.service.BrakeConfPanelService;
import com.crh2.javabean.TrainBrakeFactor;

/**
 * �ƶ����ļ���
 * @author huhui
 *
 */
public class BrakeForceMinTime {

	/**
	 * �����ƶ������б�
	 */
	public static ArrayList<TrainBrakeFactor> brakeList = null;
	/**
	 * ����ƶ���������Ӧ�ı�ʶλ
	 */
	private static int maxBrakeLevelIndex = 9;
	/**
	 * �ƶ���
	 */
	public static double brakeForce = 0;
	/**
	 * ���ƶ���
	 */
	public static double elecBrakeForce = 0;
	
	/**
	 * ���ƶ�������ز���
	 */
	public static double v1 = 0;
	public static double v2 = 0;
	public static double F2 = 0;
	public static double Fst = 0;

	/**
	 * �����ۺ��ƶ���
	 * @param speed ��λkm/h
	 * @return
	 */
	public static double getComBrakingForce(double speed) {// �����ٶ�
		double fa = getBrakingAcceleration(speed, maxBrakeLevelIndex);// �ƶ�ʱ��ļ��ٶ�a����Сʱ�������ǵ�9��ɲ��
		brakeForce = Math.abs(fa * AirFrictionMinTime.m * 1000);
		elecBrakeForce = getElecBrakeForce(speed);//������ƶ���
		if(elecBrakeForce > brakeForce){
			elecBrakeForce = brakeForce;
		}
		return elecBrakeForce;
	}
	
	/**
	 * 2014.10.14�����ݼ��ٶ�a���ƶ���,����ȡ��ͨ�ƶ���
	 * @param speed
	 * @return
	 */
	public static double getManulBrakingForce(double speed){ //km/h
		double fa = getBrakingAcceleration(speed, maxBrakeLevelIndex);
//		System.out.println("maxBrakeLevelIndex------>"+maxBrakeLevelIndex);
		return Math.abs(fa * AirFrictionMinTime.m * 1000);//N
	}
	
	/**
	 * 2014.10.14 �󸽼�����
	 * @param cp
	 * @return
	 */
	public static double getOtherForce(double cp) {
		return AirFrictionMinTime.m * 9.8 * cp;// N
	}
	
	/**
	 * �����ƶ�ʱ�ļ��ٶ�
	 * @param speed
	 * @param mode
	 * @return
	 */
	public static double getBrakingAcceleration(double speed, int mode){
		double fa = 0;// �ƶ�ʱ��ļ��ٶ�a
		int index = findNearestIndex(speed);
		fa = getAcceleration(index, mode, speed);
		return fa;
	}
	
	/**
	 * ͨ��modeѡ���ƶ����ٶ�
	 * @param index
	 * @param mode
	 * @param v
	 * @return
	 */
	private static double getAcceleration(int index, int mode, double v) {
		double a = 0;
		if (mode == 1) {// 1A
			a = brakeList.get(index).get_1A();
		} else if (mode == 2) {// 1B
			a = brakeList.get(index).get_1B();
		} else if (mode == 3) {// 2
			a = brakeList.get(index).get_2();
		} else if (mode == 4) {
			a = brakeList.get(index).get_3();
		} else if (mode == 5) {
			a = brakeList.get(index).get_4();
		} else if (mode == 6) {
			a = brakeList.get(index).get_5();
		} else if (mode == 7) {
			a = brakeList.get(index).get_6();
		} else if (mode == 8) {
			a = brakeList.get(index).get_7();
		} else if (mode == 9) {
			a = brakeList.get(index).get_8();
		} else if (mode == 10) {
			a = brakeList.get(index).get_9();
		}else if (mode == 11) {
			a = brakeList.get(index).get_10();
		}else if (mode == 12) {
			a = brakeList.get(index).get_11();
		}else if (mode == 13) {
			a = brakeList.get(index).get_12();
		}else if (mode == 14) {
			a = brakeList.get(index).get_13();
		} else if (mode == 15) {
			a = brakeList.get(index).getEb();
		}
		return -a;
	}

	/**
	 *  Ѱ���ٶ���ӽ���brake���±�
	 * @param v
	 * @return
	 */
	private static int findNearestIndex(double v) {
		int index = 0;
		double diff = Integer.MAX_VALUE - v;
		for (int i = 0; i < brakeList.size(); i++) {
			TrainBrakeFactor brake = brakeList.get(i);
			double temp = Math.abs(brake.getSpeed() - v);
			if (temp < diff) {
				index = i;// ��¼i
				diff = temp;
			}
		}
		return index;
	}
	
	/**
	 * ���ݵ�ǰ�ٶȣ�������ƶ���   v:km/h
	 * @param v
	 * @return
	 */
	public static double getElecBrakeForce(double v){
		double B = 0;//��λ��KN
		if(v>=0 && v<=v1){
			B = (-Fst/v1)*v;
		}else if(v>v1 && v<=v2){
			B = -Fst+((Fst-F2)/(v2-v1))*(v-v1);
		}else if(v>v2){
			B = (-(Fst-( Fst-F2))*v2)/v;
		}
		return Math.abs(B * 1000);//KNת����N
	}
	
	/**
	 * ����brakeList�����ҳ�ĳ���͵ĳ������ƶ�����λ
	 * @param brakeList
	 */
	public static void setBrakeList(ArrayList<TrainBrakeFactor> brakeList){
		BrakeForceMinTime.brakeList = brakeList;
		boolean [] brakeLevelboolean = BrakeConfPanelService.getBrakeLevelBoolean(brakeList);
		for(int i=brakeLevelboolean.length-2;i>=0;i--){ //ȥ���˽����ƶ�
			if(brakeLevelboolean[i] == true){
				maxBrakeLevelIndex = i + 1;
				break;
			}
		}
	}
	
	/**
	 *  ������λ
	 */
	public static void resetBrakeForce() {
		brakeForce = 0;
		elecBrakeForce = 0;
	}

}
