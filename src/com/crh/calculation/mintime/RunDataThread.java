package com.crh.calculation.mintime;

import com.crh.calculation.fixedtime.RunDataCalculatorFixedTime;
import com.crh.calculation.specifiedtime.RunDataCalculatorSpecifiedTime;

/**
 * 仿真计算的线程
 * @author huhui
 *
 */
public class RunDataThread implements Runnable {
	
	/**
	 *  任务当前完成量
	 */
	protected volatile int current = 0;
	/**
	 *  总任务量
	 */
	protected int amount = 100;
	
	/**
	 * 计算参数
	 */
	private String routeName, trainNum, tractionLevel;
	private int trainCategoryId, mode = 0;
	private double maxSpeed, randomError, initV0;
	/**
	 * 定义四种仿真标识
	 */
	public static final int MT = 1, RT = 2, STE = 3, FT = 4;
	
	public RunDataThread(String routeName, String trainNum,
			int trainCategoryId, double maxSpeed, double randomError, int mode, double initV0, String tractionLevel) {//mode:仿真类型，1代表最小时分；2代表常态运行 ；3代表固定时分节能控制运行；4代表固定时分运行
		this.routeName = routeName;
		this.trainNum = trainNum;
		this.trainCategoryId = trainCategoryId;
		this.maxSpeed = maxSpeed;
		this.randomError = randomError;//MT是randomError，STE和FT是runTime
		this.mode = mode;
		this.initV0 = initV0;
		this.tractionLevel = tractionLevel;
	}

	/**
	 * 返回总任务量
	 * @return
	 */
	public int getAmount(){
		return amount;
	}
	
	/**
	 * 返回当前任务量
	 * @return
	 */
	public int getCurrent(){
		return current;
	}

	@Override
	public void run() {
		if(mode == MT){
			RunDataCalculatorMinTime runDataCalculatorMinTime = new RunDataCalculatorMinTime(routeName, trainNum, trainCategoryId, tractionLevel, 9, maxSpeed, randomError);
			runDataCalculatorMinTime.calculateFullData();//计算Rundata
		}else if(mode == RT){
			
		}else if(mode == STE){
			RunDataCalculatorSpecifiedTime runDataCalculatorSpecifiedTime = new RunDataCalculatorSpecifiedTime(routeName, trainNum, trainCategoryId, tractionLevel, 9, maxSpeed, randomError);
			runDataCalculatorSpecifiedTime.calculateFullData();
		}else if(mode == FT){
			RunDataCalculatorFixedTime runDataCalculatorFixedTime = new RunDataCalculatorFixedTime(routeName, trainNum, trainCategoryId, tractionLevel, 9, maxSpeed, randomError, initV0);
			runDataCalculatorFixedTime.calculateFullData();
		}
		current = amount;
	}

}
