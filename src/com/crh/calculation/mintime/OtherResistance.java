package com.crh.calculation.mintime;

import java.util.List;

import com.crh2.javabean.Curve;
import com.crh2.javabean.Slope;

/**
 * 通过获取数据库中坡道和弯道的数据，修改Cp的数值，即附加阻力
 * @author huhui
 * @date 2014.10.23
 */
public class OtherResistance {
	/**
	 *  存放坡道数据
	 */
	private static List<Slope> slopeList = null;
	/**
	 * 存放弯道数据
	 */
	private static List<Curve> curveList = null;

	/**
	 *  构造函数，初始化List
	 * @param slopeList
	 * @param curveList
	 */
	public OtherResistance(List<Slope> slopeList,List<Curve> curveList) {
		OtherResistance.slopeList = slopeList;
		OtherResistance.curveList = curveList;
	}

	/**
	 * 给定里程s，得到弯道附加阻力Cp
	 * @param v 当前速度
	 * @param s 当前里程
	 * @return 数组 array[0]->速度, array[1]->弯道附加阻力 
	 */
	public static double [] byCurve(double v, double s) {
		double [] paras = new double[2];
		double curveCp = 0;
		for (Curve bean : curveList) {
			double curveStart = bean.getStart(); //弯道的起点
			double curveEnd = curveStart + bean.getLength() / 1000.0; //弯道终点
			if(s > curveEnd){
				continue; //寻找列车将要到达的弯道
			}else{
				double speedLimit = bean.getSpeedLimit(); //弯道限速
				if(v > speedLimit){
					v = speedLimit;
				}
				if(s >= curveStart && s <= curveEnd){
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
	 * 给定里程s，得到坡道附加阻力Cp
	 * @param s 当前里程
	 * @return 坡道附加阻力 
	 */
	public static double bySlope(double s) {
		double slopeCp = 0;
		for (Slope bean : slopeList) {
			double slopeEnd = bean.getEnd();
			if(s > slopeEnd){
				continue;
			}else{
				slopeCp = bean.getSlope();
				break;
			}
		}
		return slopeCp;
	}

}
