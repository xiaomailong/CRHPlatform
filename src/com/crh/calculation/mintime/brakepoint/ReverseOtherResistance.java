package com.crh.calculation.mintime.brakepoint;

import java.util.List;

import com.crh2.calculate.TrainAttribute;
import com.crh2.javabean.Curve;
import com.crh2.javabean.Slope;

/**
 * 2014.10.23 从终点站出发，反向计算附加阻力
 * @author huhui
 */
public class ReverseOtherResistance {
	/**
	 * 存放坡道数据
	 */
	private static List<Slope> slopeList = null;
	/**
	 * 存放弯道数据
	 */
	private static List<Curve> curveList = null;

	/**
	 * 构造函数，初始化List
	 * @param slopeList
	 * @param curveList
	 */
	public ReverseOtherResistance(List<Slope> slopeList,List<Curve> curveList) {
		// 计算牵引时候的情况
		ReverseOtherResistance.slopeList = slopeList;
		ReverseOtherResistance.curveList = curveList;
	}
	
	/**
	 * 弯道附加阻力Cp
	 * @param v 当前速度
	 * @param s 当前里程
	 * @return 数组 array[0]->速度, array[1]->弯道附加阻力 
	 */
	public static double [] byCurve(double v, double s) {
		double location = TrainAttribute.CRH_DESTINATION_LOCATION - s;
		double [] paras = new double[2];
		double curveCp = 0;
		int size = curveList.size();
		for (int i = size - 1; i >= 0; i--) {
			Curve bean = curveList.get(i);
			double curveStart = bean.getStart(); //弯道的起点
			double curveEnd = bean.getStart() + bean.getLength() / 1000.0; //弯道终点
			if(location < curveStart){
				continue;
			}else{
				double speedLimit = bean.getSpeedLimit();
				if(v > speedLimit){
					v = speedLimit;
				}
				if(location >= curveEnd && location <= curveStart){
					curveCp = 600 / Math.abs(bean.getRadius());
				}
				break;
			}
		}
		paras[0] = v;
		paras[1] = curveCp;
		return paras;
	}

	/**
	 *  坡道修改Cp
	 * @param s
	 * @return
	 */
	public static double bySlope(double s) {
		double location = TrainAttribute.CRH_DESTINATION_LOCATION - s;
		double slopeCp = 0;
		int size = slopeList.size();
		for (int i = size - 1; i >= 0; i--) {
			Slope bean = slopeList.get(i);
			double slopeStart = bean.getEnd() - bean.getLength() / 1000.0;
			if(location < slopeStart){
				continue;
			}else{
				slopeCp = bean.getSlope();
				break;
			}
		}
		return slopeCp;
	}

}
