package com.crh.calculation.mintime.brakepoint;

import java.util.ArrayList;
import java.util.List;

import com.crh2.calculate.TrainAttribute;
import com.crh2.javabean.Curve;
import com.crh2.javabean.Curveback;
import com.crh2.javabean.Slope;
import com.crh2.javabean.Slopeback;
import com.crh2.service.DataService;
import com.crh2.util.Arith;

/**
 * 反向运行，用于计算最后一个制动点
 * 通过获取数据库中坡道和弯道的数据，修改Cp的数值
 * @author huhui
 *
 */
public class UpdateCpReverseMinTime {
	/**
	 * 存放坡道数据
	 */
	private List<Slopeback> slopeListReverse = null;
	/**
	 * 存放弯道数据
	 */
	private List<Curveback> curveListReverse = null;
	/**
	 *  记录坡道下标
	 */
	private int slopeFlag = 0;
	/**
	 *  记录弯道下标
	 */
	private int curveFlag = 0;
	/**
	 * 记录坡度值
	 */
	private double slope = 0;
	/**
	 * 记录弯道值
	 */
	private double curve = 0;

	/**
	 *  构造函数，初始化List
	 * @param slopeList
	 * @param curveList
	 */
	public UpdateCpReverseMinTime(List<Slope> slopeList,List<Curve> curveList) {
		DataService ds = new DataService();
		// 计算牵引时候的情况
		this.slopeListReverse = this.slopeListConvertor(slopeList);
		this.curveListReverse = this.curveListConvertor(curveList);
	}
	
	/**
	 * 用于将slopeList转化为slopeListReverse，即反转数据
	 * @param slopeList
	 * @return
	 */
	public List<Slopeback> slopeListConvertor(List<Slope> slopeList){
		List<Slopeback> slopeListReverse = new ArrayList<Slopeback>();
		//先给第0条记录赋值
		int t = 0;
		Slopeback slopeback = new Slopeback();
		slopeback.setId(0);
		slopeback.setSlope(0);
		slopeback.setStart(0);
		slopeListReverse.add(slopeback);
		
		double slopeValue = 0;
		double sumLength = 0;
		for(int i=slopeList.size()-1;i>0;i--){
			t++;
			slopeback = new Slopeback();
			Slope slope = slopeList.get(i);
			sumLength += slope.getLength();
			slopeValue = slopeList.get(i-1).getSlope();
			slopeback.setId(t);
			slopeback.setSlope(Arith.negate(slopeValue));
			slopeback.setStart(sumLength/1000);
			slopeListReverse.add(slopeback);
		}
		return slopeListReverse;
	}
	
	/**
	 * 用于将curveList转化为curveListReverse
	 * @param curveList
	 * @return
	 */
	public List<Curveback> curveListConvertor(List<Curve> curveList){
		List<Curveback> curveListReverse = new ArrayList<Curveback>();
		int t = 0;
		for(int i=curveList.size()-1;i>=0;i--){
			Curveback curveback = new Curveback();
			Curve curve = curveList.get(i);
			curveback.setId(t);
			curveback.setRadius(curve.getRadius());
			curveback.setLength(curve.getLength());
			curveback.setSpeedLimit(curve.getSpeedLimit());
			curveback.setStart(TrainAttribute.CRH_DESTINATION_LOCATION - curve.getStart() - curve.getLength()/1000);
			curveListReverse.add(curveback);
			t++;
		}
		return curveListReverse;
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
		for (int i = index; i < curveListReverse.size(); i++) {
			curveStart = curveListReverse.get(i).getStart();
			if (Sn >= curveStart) {// 进入弯道
				curveFlag = i;
				curve = 600 / Math.abs(curveListReverse.get(i).getRadius());
				newCp = newCp - curve;
//				break;
			}
		}
		return newCp;
	}

	/**
	 * 判断是否到达下一个弯道，如果到达，则调用byCurve
	 * @param Sn
	 * @param index
	 * @return
	 */
	public boolean isGetNextCurve(double Sn, int index) {
		if (index >= curveListReverse.size()) {
			return false;
		}
		if (Sn >= curveListReverse.get(index).getStart()) {
//			System.out.println("到达第 "+index+" 个弯道，Sn= "+Sn);
			return true;// 到达
		} else {
			return false;// 未到达
		}
	}
	
	/**
	 * 判断是否超过下一个弯道的速度，如果超过，则已该弯道最高速度运行，否则，继续加速
	 */
	public double overSpeedNextCurve(double speed, int index) {
		if (index >= curveListReverse.size()) {
			return speed;
		}
		double speedLimit = curveListReverse.get(index).getSpeedLimit();
		if (speed >= speedLimit) {
			return speedLimit;// 超速
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
		if(index == curveListReverse.size()){
			--index;
		}
		if (Sn > curveListReverse.get(index).getStart() + curveListReverse.get(index).getLength() / 1000) {
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
		for (int i = index; i < slopeListReverse.size(); i++) {
			slopeStart = slopeListReverse.get(i).getStart();
			if (Sn >= slopeStart) {
				slopeFlag = i;
				slope = slopeListReverse.get(i).getSlope();
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
		if (index >= slopeListReverse.size()) {
			return false;
		}
		double startPoint = slopeListReverse.get(index).getStart();
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
