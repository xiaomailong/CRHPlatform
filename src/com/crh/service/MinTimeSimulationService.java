package com.crh.service;

import java.util.ArrayList;

import com.crh.view.dialog.RouteDataTrainNumDialog;
import com.crh2.javabean.TrainBrakeFactor;
import com.crh2.javabean.TrainCategory;
import com.crh2.javabean.TrainElectricBrake;
import com.crh2.javabean.TrainRouteName;
import com.crh2.javabean.TrainRouteTrainnum;
import com.crh2.javabean.TrainTractionConf;

/**
 * ΪMinTimeSimulationPanel�ṩ���ݵ�����ɾ���ġ������
 * @author huhui
 */
public class MinTimeSimulationService {
	
	/**
	 * ��ȡ����·��
	 */
	public static ArrayList<TrainRouteName> getTrainRouteName(){
		return RouteDataManagementDialogService.getTrainRouteName();
	}
	
	/**
	 * ͨ��·�߻�ȡ��·�߶�Ӧ�����г���
	 */
	public static ArrayList<TrainRouteTrainnum> getTrainNum(String routeName){
		return RouteDataManagementDialogService.getTrainNum(routeName, RouteDataTrainNumDialog.DEFAULTNUM + "");
	}
	
	/**
	 * ͨ����·���ͳ��λ�ȡTrainRouteTrainnum
	 */
	public static TrainRouteTrainnum getTrainnum(String routeName, String trainNum){
		return RouteDataManagementDialogService.getTrainNum(routeName, trainNum).get(0);
	}
	
	/**
	 * ͨ����·����ȡ��·���
	 */
	public static int getRouteIdByName(String routeName){
		return RouteDataManagementDialogService.getRouteIdByName(routeName);
	}
	
	/**
	 * ͨ��train_category_id��ȡtrain_traction_conf����ǣ�������ò���
	 */
	public static TrainTractionConf getTrainTractionConf(int trainCategoryId){
		return TractionConfPanelService.getTrainTractionConf(trainCategoryId);
	}
	
	/**
	 * ͨ��train_category_id��ȡa,b,c�����������������������ò���
	 */
	public static double [] getAirFrictionParameters(int trainCategoryId){
		return TractionConfPanelService.getSomeParametersFromTrainEdit(trainCategoryId);
	}
	
	/**
	 * ͨ��train_category_id��ȡ���ƶ�ϵ�������ƶ������ò���
	 */
	public static ArrayList<TrainBrakeFactor> getTrainBrakeFactor(int trainCategoryId){
		return BrakeConfPanelService.getBrakeFactor(trainCategoryId);
	}
	
	/**
	 * ͨ��train_category_id��ȡtrain_electric_brake�������õ��ƶ�������
	 */
	public static TrainElectricBrake getTrainElectricBrake(int trainCategoryId){
		return BrakeConfPanelService.getTrainElectricBrake(trainCategoryId);
	}
	
	/**
	 * ��ȡ6��������Ϊ������ƶ���
	 */
	public static double [] getSixParameters(int trainCategoryId){
		String [] paraStr = BrakeConfPanelService.getFiveParameters(trainCategoryId);
		double [] para = new double[paraStr.length];
		for(int i=0;i<paraStr.length;i++){
			para[i] = Double.parseDouble(paraStr[i]);
		}
		return para;
	}
	
	/**
	 * ��ȡ���б���
	 */
	public static ArrayList<TrainCategory> getTrainCategory(){
		return TrainEditPanelService.getAllTrainCategory();
	}
	
}
