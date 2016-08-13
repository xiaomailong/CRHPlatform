package com.crh.calculation.mintime;

import com.crh.service.AuxiliaryPowerSupplyPanelService;
import com.crh.service.TrainEditPanelService;

/**
 * 2014.12.1 计算网流
 * @author huhui
 */
public class NetCurrent {
	
	/**
	 * 网压
	 */
	private double u = 0;
	/**
	 * 夏季辅助功率
	 */
	private double summerPower = 0;
	/**
	 * 冬季辅助功率
	 */
	private double winterPower = 0;
	
	public NetCurrent(int trainCategoryId){
		u = TrainEditPanelService.getTrainInfoByCategoryId(trainCategoryId).getA200() * 1000;//获取网压，如27.5kv
		double [] para = AuxiliaryPowerSupplyPanelService.getDocPara(trainCategoryId);//用的是交流功率
		summerPower = para[0] * 1000;
		winterPower = para[3] * 1000;
	}
	
	/**
	 * 计算牵引状态下的网流
	 * @param tractionForce
	 * @param v 速度
	 * @return
	 */
	public double getTractionNetCurrent(double tractionForce, double v){
		tractionForce = tractionForce * 1000;// N
		v = v * 3.6;// m/s
		return (tractionForce * v + summerPower)/u;
	}
	
	/**
	 * 计算制动状态下的网流(负值)
	 * @param comBrakeForce 比较后的制动力
	 * @param v
	 * @return
	 */
	public double getComBrakeForceNetCurrent(double comBrakeForce, double v){
		comBrakeForce = comBrakeForce * 1000;// N
		v = v * 3.6;// m/s
		return -((comBrakeForce * v + winterPower)/u);
	}

}
