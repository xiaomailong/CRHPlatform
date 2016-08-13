package com.crh.calculation.mintime;

import java.util.List;

import com.crh2.javabean.Curve;
import com.crh2.javabean.Slope;

/**
 * 通过获取数据库中坡道和弯道的数据，修改Cp的数值
 * @author huhui
 */
public class UpdateCpMinTime {
	/**
	 * 存放坡道数据
	 */
	private List<Slope> slopeList = null;
	/**
	 * 存放弯道数据
	 */
	private List<Curve> curveList = null;
	/**
	 * 用于记录坡道下标
	 */
	private int slopeFlag = 0;
	/**
	 * 用于记录弯道下标
	 */
	private int curveFlag = 0;
	/**
	 * 用于记录坡度值
	 */
	private double slope = 0;
	/**
	 * 用于记录弯道值
	 */
	private double curve = 0;

	/**
	 *  构造函数，初始化List
	 * @param slopeList
	 * @param curveList
	 */
	public UpdateCpMinTime(List<Slope> slopeList,List<Curve> curveList) {
		// 计算牵引时候的情况
		this.slopeList = slopeList;
		this.curveList = curveList;

	}

	/**
	 *  弯道修改Cp
	 * @param Cp
	 * @param Sn
	 * @param index
	 * @return
	 */
	public double byCurve(double Cp, double Sn, int index) {
		if(index != 0){
			index = index - 1;
		}
		double newCp = Cp;
		double curveStart = 0;// 记录每段弯道的起点
		for (int i = index; i < curveList.size(); i++) {
			curveStart = curveList.get(i).getStart();
			if (Sn >= curveStart) {// 进入弯道
				curveFlag = i;
				curve = 600 / Math.abs(curveList.get(i).getRadius());
				newCp = newCp - curve;
//				break;
			}
		}
		return newCp;
	}

	/**
	 *  判断是否到达下一个弯道，如果到达，则调用byCurve
	 * @param Sn
	 * @param index
	 * @return
	 */
	public boolean isGetNextCurve(double Sn, int index) {
		if (index >= curveList.size()) {
			return false;
		}
		if (Sn >= curveList.get(index).getStart()) {
			return true;// 到达
		} else {
			return false;// 未到达
		}
	}
	
	/**
	 * 判断是否超过下一个弯道的速度，如果超过，则已该弯道最高速度运行，否则，继续加速
	 */
	public double overSpeedNextCurve(double speed, int index) {
		if (index >= curveList.size()) {
			return speed;
		}
		double speedLimit = curveList.get(index).getSpeedLimit();
		if (speed >= speedLimit) {
			if(index == 21 || index == 22){//防止速度从300->250
				return speed;
			}else{
				return speedLimit;// 超速
			}
		} else {
			return speed;// 未超速    speed和speedLimit都是km/h
		}
	}

	/**
	 *  判断是在弯道内还是弯道外
	 * @param Sn
	 * @param index
	 * @return
	 */
	public boolean isInCurve(double Sn, int index) {
		//判断是不是最后一个弯道
		if(index == curveList.size()){
			--index;
		}
		if (Sn > curveList.get(index).getStart() + curveList.get(index).getLength() / 1000) {
			return false;// 在弯道外
		} else {
			return true;// 在弯道内
		}
	}

	/**
	 *  坡道修改Cp
	 * @param Cp
	 * @param Sn
	 * @param index
	 * @return
	 */
	public double bySlope(double Cp, double Sn, int index) {
		if(index != 0){
			index = index - 1;
		}
		double newCp = Cp;
		double slopeStart = 0;// 记录每段坡道的起点
		for (int i = index; i < slopeList.size(); i++) {
			slopeStart = slopeList.get(i).getEnd()- (slopeList.get(i).getLength() / 1000);
			if (Sn >= slopeStart) {
				slopeFlag = i;
				slope = slopeList.get(i).getSlope();
				newCp = newCp - slope;
//				break;
			}
		}
		return newCp;
	}

	/**
	 *  判断是否到达下一个坡道，如果到达，则调用bySlope
	 * @param Sn
	 * @param index
	 * @return
	 */
	public boolean isGetNextSlope(double Sn, int index) {
		if (index >= slopeList.size()) {
			return false;
		}
		double startPoint = slopeList.get(index).getEnd() - (slopeList.get(index).getLength() / 1000);
		if (Sn >= startPoint) {
//			System.out.println("到达第 "+index+" 个坡道，Sn= "+Sn);
			return true;// 达到
		} else {
			return false;// 未到达
		}
	}

	public double getCurve() {
		return curve;
	}

	public double getSlope() {
		return slope;
	}

	public int getSlopeFlag() {
		return slopeFlag;
	}

	public int getCurveFlag() {
		return curveFlag;
	}
}
