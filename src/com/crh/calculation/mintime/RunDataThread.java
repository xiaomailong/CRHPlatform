package com.crh.calculation.mintime;

import com.crh.calculation.fixedtime.RunDataCalculatorFixedTime;
import com.crh.calculation.specifiedtime.RunDataCalculatorSpecifiedTime;

/**
 * ���������߳�
 * @author huhui
 *
 */
public class RunDataThread implements Runnable {
	
	/**
	 *  ����ǰ�����
	 */
	protected volatile int current = 0;
	/**
	 *  ��������
	 */
	protected int amount = 100;
	
	/**
	 * �������
	 */
	private String routeName, trainNum, tractionLevel;
	private int trainCategoryId, mode = 0;
	private double maxSpeed, randomError, initV0;
	/**
	 * �������ַ����ʶ
	 */
	public static final int MT = 1, RT = 2, STE = 3, FT = 4;
	
	public RunDataThread(String routeName, String trainNum,
			int trainCategoryId, double maxSpeed, double randomError, int mode, double initV0, String tractionLevel) {//mode:�������ͣ�1������Сʱ�֣�2����̬���� ��3����̶�ʱ�ֽ��ܿ������У�4����̶�ʱ������
		this.routeName = routeName;
		this.trainNum = trainNum;
		this.trainCategoryId = trainCategoryId;
		this.maxSpeed = maxSpeed;
		this.randomError = randomError;//MT��randomError��STE��FT��runTime
		this.mode = mode;
		this.initV0 = initV0;
		this.tractionLevel = tractionLevel;
	}

	/**
	 * ������������
	 * @return
	 */
	public int getAmount(){
		return amount;
	}
	
	/**
	 * ���ص�ǰ������
	 * @return
	 */
	public int getCurrent(){
		return current;
	}

	@Override
	public void run() {
		if(mode == MT){
			RunDataCalculatorMinTime runDataCalculatorMinTime = new RunDataCalculatorMinTime(routeName, trainNum, trainCategoryId, tractionLevel, 9, maxSpeed, randomError);
			runDataCalculatorMinTime.calculateFullData();//����Rundata
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
