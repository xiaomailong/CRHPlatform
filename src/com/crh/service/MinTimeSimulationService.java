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
 * 为MinTimeSimulationPanel提供数据的增、删、改、查服务
 * @author huhui
 */
public class MinTimeSimulationService {
	
	/**
	 * 获取所有路线
	 */
	public static ArrayList<TrainRouteName> getTrainRouteName(){
		return RouteDataManagementDialogService.getTrainRouteName();
	}
	
	/**
	 * 通过路线获取该路线对应的所有车次
	 */
	public static ArrayList<TrainRouteTrainnum> getTrainNum(String routeName){
		return RouteDataManagementDialogService.getTrainNum(routeName, RouteDataTrainNumDialog.DEFAULTNUM + "");
	}
	
	/**
	 * 通过线路名和车次获取TrainRouteTrainnum
	 */
	public static TrainRouteTrainnum getTrainnum(String routeName, String trainNum){
		return RouteDataManagementDialogService.getTrainNum(routeName, trainNum).get(0);
	}
	
	/**
	 * 通过线路名获取线路编号
	 */
	public static int getRouteIdByName(String routeName){
		return RouteDataManagementDialogService.getRouteIdByName(routeName);
	}
	
	/**
	 * 通过train_category_id获取train_traction_conf，即牵引力配置参数
	 */
	public static TrainTractionConf getTrainTractionConf(int trainCategoryId){
		return TractionConfPanelService.getTrainTractionConf(trainCategoryId);
	}
	
	/**
	 * 通过train_category_id获取a,b,c三个参数，即空气阻力配置参数
	 */
	public static double [] getAirFrictionParameters(int trainCategoryId){
		return TractionConfPanelService.getSomeParametersFromTrainEdit(trainCategoryId);
	}
	
	/**
	 * 通过train_category_id获取电制动系数，即制动力配置参数
	 */
	public static ArrayList<TrainBrakeFactor> getTrainBrakeFactor(int trainCategoryId){
		return BrakeConfPanelService.getBrakeFactor(trainCategoryId);
	}
	
	/**
	 * 通过train_category_id获取train_electric_brake，即配置电制动力参数
	 */
	public static TrainElectricBrake getTrainElectricBrake(int trainCategoryId){
		return BrakeConfPanelService.getTrainElectricBrake(trainCategoryId);
	}
	
	/**
	 * 获取6个参数，为计算电制动力
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
	 * 获取所有编组
	 */
	public static ArrayList<TrainCategory> getTrainCategory(){
		return TrainEditPanelService.getAllTrainCategory();
	}
	
}
