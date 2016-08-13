package com.crh.calculation.mintime;

import java.util.ArrayList;

import com.crh.service.BrakeConfPanelService;
import com.crh2.javabean.TrainBrakeFactor;

/**
 * 制动力的计算
 * @author huhui
 *
 */
public class BrakeForceMinTime {

	/**
	 * 保存制动因子列表
	 */
	public static ArrayList<TrainBrakeFactor> brakeList = null;
	/**
	 * 最大制动级别所对应的标识位
	 */
	private static int maxBrakeLevelIndex = 9;
	/**
	 * 制动力
	 */
	public static double brakeForce = 0;
	/**
	 * 电制动力
	 */
	public static double elecBrakeForce = 0;
	
	/**
	 * 电制动特性相关参数
	 */
	public static double v1 = 0;
	public static double v2 = 0;
	public static double F2 = 0;
	public static double Fst = 0;

	/**
	 * 计算综合制动力
	 * @param speed 单位km/h
	 * @return
	 */
	public static double getComBrakingForce(double speed) {// 传入速度
		double fa = getBrakingAcceleration(speed, maxBrakeLevelIndex);// 制动时候的加速度a；最小时分运行是第9档刹车
		brakeForce = Math.abs(fa * AirFrictionMinTime.m * 1000);
		elecBrakeForce = getElecBrakeForce(speed);//计算电制动力
		if(elecBrakeForce > brakeForce){
			elecBrakeForce = brakeForce;
		}
		return elecBrakeForce;
	}
	
	/**
	 * 2014.10.14，根据加速度a求制动力,即获取普通制动力
	 * @param speed
	 * @return
	 */
	public static double getManulBrakingForce(double speed){ //km/h
		double fa = getBrakingAcceleration(speed, maxBrakeLevelIndex);
//		System.out.println("maxBrakeLevelIndex------>"+maxBrakeLevelIndex);
		return Math.abs(fa * AirFrictionMinTime.m * 1000);//N
	}
	
	/**
	 * 2014.10.14 求附加阻力
	 * @param cp
	 * @return
	 */
	public static double getOtherForce(double cp) {
		return AirFrictionMinTime.m * 9.8 * cp;// N
	}
	
	/**
	 * 计算制动时的加速度
	 * @param speed
	 * @param mode
	 * @return
	 */
	public static double getBrakingAcceleration(double speed, int mode){
		double fa = 0;// 制动时候的加速度a
		int index = findNearestIndex(speed);
		fa = getAcceleration(index, mode, speed);
		return fa;
	}
	
	/**
	 * 通过mode选择制动加速度
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
	 *  寻找速度最接近的brake的下标
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
				index = i;// 记录i
				diff = temp;
			}
		}
		return index;
	}
	
	/**
	 * 根据当前速度，计算电制动力   v:km/h
	 * @param v
	 * @return
	 */
	public static double getElecBrakeForce(double v){
		double B = 0;//单位：KN
		if(v>=0 && v<=v1){
			B = (-Fst/v1)*v;
		}else if(v>v1 && v<=v2){
			B = -Fst+((Fst-F2)/(v2-v1))*(v-v1);
		}else if(v>v2){
			B = (-(Fst-( Fst-F2))*v2)/v;
		}
		return Math.abs(B * 1000);//KN转换成N
	}
	
	/**
	 * 设置brakeList，查找出某类型的车最大的制动力级位
	 * @param brakeList
	 */
	public static void setBrakeList(ArrayList<TrainBrakeFactor> brakeList){
		BrakeForceMinTime.brakeList = brakeList;
		boolean [] brakeLevelboolean = BrakeConfPanelService.getBrakeLevelBoolean(brakeList);
		for(int i=brakeLevelboolean.length-2;i>=0;i--){ //去除了紧急制动
			if(brakeLevelboolean[i] == true){
				maxBrakeLevelIndex = i + 1;
				break;
			}
		}
	}
	
	/**
	 *  参数复位
	 */
	public static void resetBrakeForce() {
		brakeForce = 0;
		elecBrakeForce = 0;
	}

}
