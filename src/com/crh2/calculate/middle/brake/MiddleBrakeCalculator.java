package com.crh2.calculate.middle.brake;

import java.util.ArrayList;
import java.util.HashMap;

import com.crh2.calculate.TrainAttribute;
import com.crh2.javabean.Curve;
import com.crh2.javabean.Rundata;
import com.crh2.util.Arith;

/**
 * 中间限速制动计算类
 * @author huhui
 *
 */
public class MiddleBrakeCalculator {
	/**
	 * 用于保存所有的弯道信息
	 */
	private ArrayList<Curve> curveList = null;
	/**
	 * 用于设置匀速运动时的index
	 */
	private HashMap<Integer,Integer> curveFlagMap = null;
	
	//构造函数
	public MiddleBrakeCalculator(ArrayList<Curve> curveList){
		this.curveList = curveList;
		curveFlagMap = new HashMap<Integer, Integer>();
	}
	
	/**
	 * 当到达一个新的弯道的起点的时候，开始检查速度
	 * @param currentSpeed
	 * @param curveFlag
	 * @return
	 */
	public boolean isBeyondLimitSpeed(double currentSpeed,int curveFlag) {
//		System.out.println("curveFlag="+curveFlag+",size="+curveList.size());
		Curve curve = curveList.get(curveFlag);
		if(currentSpeed > curve.getSpeedLimit()){//超速
			System.out.println("进入"+curveFlag+"号弯道超速，限速是"+curve.getSpeedLimit()+"，实际速度是"+currentSpeed);
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 如果真超过了限制的速度，那么就回溯
	 * @param currentSpeed
	 * @param curveFlag
	 * @param rundataBeansList
	 * @return
	 */
	public Rundata rollBackRundata(double currentSpeed,int curveFlag,ArrayList<Rundata> rundataBeansList){
		//拿到当前限速点的坡道信息
		Curve curve = curveList.get(curveFlag);
		//计算平均速度    averageSpeed = (currentSpeed + curve.getSpeedLimit())/2
		double averageSpeed = Arith.div(Arith.add(currentSpeed, curve.getSpeedLimit()), 2);
//		System.out.println("currentSpeed = "+currentSpeed);
		//得到与averageSpeed对应的Si的最近的点
		Rundata rundata = this.nearestRundataBeanCalculator(curveFlag,averageSpeed, rundataBeansList);
		return rundata;
	}
	
	/**
	 * 计算与averageSpeed对应的Si的最近的点
	 * @param curveFlag
	 * @param averageSpeed
	 * @param rundataBeansList
	 * @return
	 */
	public Rundata nearestRundataBeanCalculator(int curveFlag,double averageSpeed,ArrayList<Rundata> rundataBeansList){
		Rundata rundata = null;
		double min = Integer.MAX_VALUE;//令min为一个很大的数
		int index = 0;//用于记录找到的Rundata的下标
		if(TrainAttribute.CRH_IS_CONSTANT_SPEED == false){//如果不是匀速状态
			//遍历rundataBeansList，寻找最近的点
			for(int i=0;i<rundataBeansList.size();i++){
				//rundataBeansList.get(i).getSpeed()-averageSpeed
				double temp = Math.abs(Arith.sub(rundataBeansList.get(i).getSpeed(), averageSpeed));
				if(temp < min){
					min = temp;
					index = i;
					
				}
			}
		}else{//如果是匀速状态
			addIntoCurveFlagMap(curveFlag);//先加入hashmap
			index = rundataBeansList.size() - 50*curveFlagMap.get(curveFlag);
		}
		//得到最近的点
		rundata = rundataBeansList.get(index);
		//删除index之后的所有rundata
		this.removeRundata(index, rundataBeansList);
		return rundata;
	}
	
	/**
	 * 移除index之后的所有数据
	 * @param index
	 * @param rundataBeansList
	 */
	public void removeRundata(int index,ArrayList<Rundata> rundataBeansList){
		for(int i=rundataBeansList.size()-1;i>=index;i--){
			rundataBeansList.remove(i);
		}
	}
	
	/**
	 * 向curveFlagMap中增加元素
	 * @param curveFlag
	 */
	public void addIntoCurveFlagMap(int curveFlag){
		if(curveFlagMap.containsKey(curveFlag)){//如果包含了该curveFlag，则加1
			int newValue = curveFlagMap.get(curveFlag) + 1;
			curveFlagMap.put(curveFlag, newValue);
		}else{//如果不包括，则新加入
			curveFlagMap.put(curveFlag, 1);
		}
	}
	
}
