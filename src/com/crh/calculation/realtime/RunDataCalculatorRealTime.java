package com.crh.calculation.realtime;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import com.crh.calculation.mintime.AirFrictionMinTime;
import com.crh.calculation.mintime.BrakeForceMinTime;
import com.crh.calculation.mintime.CpMinTime;
import com.crh.calculation.mintime.NetCurrent;
import com.crh.calculation.mintime.OtherResistance;
import com.crh.calculation.mintime.SnMinTime;
import com.crh.calculation.mintime.TractionForceMinTime;
import com.crh.calculation.mintime.VnMinTime;
import com.crh.service.MinTimeSimulationService;
import com.crh.view.dialog.RealTimeRunningDataDialog;
import com.crh2.calculate.TrainAttribute;
import com.crh2.javabean.Curve;
import com.crh2.javabean.Rundata;
import com.crh2.javabean.Slope;
import com.crh2.javabean.StationInfo;
import com.crh2.javabean.TrainElectricBrake;
import com.crh2.javabean.TrainTractionConf;
import com.crh2.service.DataService;

/**
 * 实时运行仿真，计算运行数据，即randata
 * @author huhui
 */
public class RunDataCalculatorRealTime implements Runnable {

	private OtherResistance otherResistance = null;
	/**
	 * 提供线路数据
	 */
	private DataService dataService = new DataService();
	private ArrayList<Slope> slopeList = null;
	private ArrayList<Curve> curveList = null;
	private ArrayList<StationInfo> stationList = null;
	private int trainCategoryId;
	/**
	 *  2013.7.11增加，保存Rundata的javabean
	 */
	public static ArrayList<Rundata> rundataBeansList = null;
	/**
	 * 实时显示计算过程
	 */
	private RealTimeRunningDataDialog runnintDataFrame = null;
	private int routeId;
	
	/**
	 * 平均加速度
	 */
	private double aveAcceleration = 0;
	private int isFirst1 = 0,isFirst2 = 0;
	/**
	 * 计算网流
	 */
	private NetCurrent netCurrent = null;
	

	/**
	 *  构造函数，用于初始化list
	 * @param routeName
	 * @param trainNum
	 * @param trainCategoryId
	 */
	public RunDataCalculatorRealTime(String routeName, String trainNum, int trainCategoryId) {
		this.routeId = MinTimeSimulationService.getRouteIdByName(routeName);
		// 初始化各个list
		this.slopeList = dataService.getSlopeData(routeId);
		this.curveList = dataService.getCurveData(routeId);
		this.stationList = dataService.getStationInfoData(routeId);
		// 初始化其它几个参数
		this.trainCategoryId = trainCategoryId;
		TrainAttribute.CRH_DESTINATION_LOCATION = stationList.get(stationList.size()-1).getLocation();//终点站位置
		netCurrent = new NetCurrent(trainCategoryId);
		initFormulas();
		// 初始化OtherResistance 即附加阻力
		otherResistance = new OtherResistance(slopeList, curveList);
		rundataBeansList = new ArrayList<Rundata>();
	}
	
	/**
	 * 计算常态运行的数据
	 */
	public void calculateFullData() {
		calculateRunData();
		//每次计算完将列车置为非匀速状态
		TrainAttribute.CRH_IS_CONSTANT_SPEED = false;
	}
	
	/**
	 * 给牵引力、空气阻力、制动力公式赋值
	 */
	public void initFormulas(){
		this.initTractionForce();
		this.initAirFriction();
		this.initBrakeForce();
	}
	
	/**
	 * 设置牵引力
	 */
	public void initTractionForce(){
		TrainTractionConf trainTractionConf = MinTimeSimulationService.getTrainTractionConf(trainCategoryId);
		TractionForceMinTime.Fst = trainTractionConf.getFst();
		TractionForceMinTime.F1 = trainTractionConf.getF1();
		TractionForceMinTime.F2 = trainTractionConf.getF2();
		TractionForceMinTime.v1 = trainTractionConf.getV1();
		TractionForceMinTime.v2 = trainTractionConf.getV2();
		TractionForceMinTime.P1 = trainTractionConf.getP1();
//		TractionForceMinTime.mode = tractionLevel;
	}
	
	/**
	 * 设置空气阻力
	 */
	public void initAirFriction(){
		double [] abc = MinTimeSimulationService.getAirFrictionParameters(trainCategoryId);
		AirFrictionMinTime.a = abc[0];
		AirFrictionMinTime.b = abc[1];
		AirFrictionMinTime.c = abc[2];
		AirFrictionMinTime.m = abc[4];
	}
	
	/**
	 * 设置制动力
	 */
	public void initBrakeForce(){
		BrakeForceMinTime.setBrakeList(MinTimeSimulationService.getTrainBrakeFactor(trainCategoryId));//制动力系数
		TrainElectricBrake trainElectricBrake = MinTimeSimulationService.getTrainElectricBrake(trainCategoryId);
		double [] para = MinTimeSimulationService.getSixParameters(trainCategoryId);//k1,k2,D,N,T,F2
		double k1 = para[0];
		double k2 = para[1];
		double D = para[2];
		double N = para[3];
		double T = para[4];
		BrakeForceMinTime.v1 = trainElectricBrake.getV1();
		BrakeForceMinTime.v2 = trainElectricBrake.getV2();
		BrakeForceMinTime.F2 = (N * trainElectricBrake.getP00() * 3.6)/BrakeForceMinTime.v2;
		BrakeForceMinTime.Fst = ((2*N*k1*k2*T)/1000)/D;
	}

	/**
	 *  保存Rundata的javabean
	 * @param time
	 * @param speed
	 * @param location
	 * @param tractionPower
	 * @param elecPower
	 * @param power
	 * @param tractionForce
	 * @param brakeForce
	 * @param airFriction
	 * @param averageAcc
	 * @param isOverSpeed
	 * @param acceleration
	 * @param tractionGridCurrent
	 * @param comBrakeGridCurrent
	 */
	public void saveRundataBeans(double time, double speed, double location,
			double tractionPower, double elecPower, double power,
			double tractionForce, double brakeForce, double airFriction,
			double averageAcc, String isOverSpeed, double acceleration, double tractionGridCurrent, double comBrakeGridCurrent) {
		runnintDataFrame.appendTableData(time, speed, location, tractionPower,
				elecPower, power, tractionForce, brakeForce, airFriction, averageAcc,
				isOverSpeed);
		Rundata rundata = new Rundata();
		rundata.setRuntime(time);
		rundata.setSpeed(speed);
		rundata.setLocation(location);
		rundata.setTractionPower(tractionPower);
		rundata.setElecBrakeForcePower(elecPower);
		rundata.setPower(power);
		rundata.setTractionForce(tractionForce);
		rundata.setBrakeForce(brakeForce);
		rundata.setAirForce(airFriction);
		rundata.setAverageAcc(averageAcc);
		rundata.setAcceleration(acceleration);
		rundata.setTractionGridCurrent(tractionGridCurrent);
		rundata.setComBrakeGridCurrent(comBrakeGridCurrent);
		rundataBeansList.add(rundata);
	}

	/**
	 *  数据计算
	 * @return
	 */
	public ArrayList<Rundata> calculateRunData() {
		int i = 0;
		double currentSpeed = 0, lastSpeed = 0;
		double Si = 0, S = 0;
		double tractionForce = 0, airFriction = 0, elecBrake = 0, brakeForce = 0;
		double averageAcc = 0;
		double curveCp = 0, slopeCp = 0, cp = 0;
		String isOverSpeed = "";//是否超速
		double tractionPower = 0, brakePower = 0, airPower = 0, elecPower = 0, power = 0;
		double tractionGridCurrent = 0, comBrakeGridCurrent = 0, acceleration = 0;
		//2015.4.13 清空常态运行数据
		System.err.println("size = "+RunDataCalculatorRealTime.rundataBeansList.size());
		if(RunDataCalculatorRealTime.rundataBeansList != null){
			RunDataCalculatorRealTime.rundataBeansList.clear();
		}
		//标记开始计算
		TrainAttribute.CRH_IS_RUN = 1;
		//实时显示计算过程
		runnintDataFrame = new RealTimeRunningDataDialog();
		Thread displayThread = new Thread(runnintDataFrame);
		displayThread.start();
		runnintDataFrame.appendConsoleArea("准备计算");
		while (TrainAttribute.CRH_IS_RESET == 0) {
			
			while(!TrainAttribute.CRH_IS_START){
				if(TrainAttribute.CRH_IS_RESET == 1){
					break;
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if(TrainAttribute.CRH_IS_RESET == 1){
				TrainAttribute.CRH_IS_RESET = 0;
				TrainAttribute.CRH_IS_RUN = 0;
				TrainAttribute.CRH_BRAKE_LEVEL = 0;
				TrainAttribute.CRH_IS_BRAKE = false;
				TrainAttribute.CRH_TEMP = 1;
				TrainAttribute.CRH_IS_START = false;
				JOptionPane.showMessageDialog(runnintDataFrame, "计算结束");
				runnintDataFrame.dispose();
				break;
			}
			this.saveRundataBeans(i, currentSpeed, Si, tractionPower, elecPower, power, tractionForce, brakeForce, airFriction, averageAcc, isOverSpeed, acceleration, tractionGridCurrent, comBrakeGridCurrent);
			
			slopeCp = OtherResistance.bySlope(Si);
			double [] paras = OtherResistance.byCurve(currentSpeed, Si);
			curveCp = paras[1];
			cp = slopeCp + curveCp;
			isOverSpeed = currentSpeed > paras[0] ? "是" : "否"; //是否超速
			if(isOverSpeed.equals("是")){
				runnintDataFrame.appendConsoleArea("弯道超速");
			}
			tractionForce = TractionForceMinTime.getTractionForce(currentSpeed); // 计算牵引力
			airFriction = AirFrictionMinTime.getAirFriction(currentSpeed); // 计算空气阻力
			elecBrake = calculateElecBrake(currentSpeed); //电制动力
			brakeForce =  getManulBrakeForce(currentSpeed); //制动力
			currentSpeed = VnMinTime.calSpeedRealTime(currentSpeed, TrainAttribute.CRH_CAL_TIME_DIVISION, cp, TrainAttribute.CRH_BRAKE_LEVEL); //速度
			averageAcc = averageAcceleration(currentSpeed, i);
			Si = SnMinTime.calShift(currentSpeed, lastSpeed, Si); //里程
			
			i++;
			acceleration = CpMinTime.getAcceleration(currentSpeed);
			tractionPower += tractionForce * (Si-S);
			airPower += airFriction * (Si-S);
			elecPower += elecBrake * (Si-S);
			tractionGridCurrent = netCurrent.getTractionNetCurrent(elecBrake == 0 ? tractionForce : elecBrake, currentSpeed);
			brakePower += getComBrakePower(brakeForce, elecBrake, Si-S);
			power += calculatePower(tractionPower, brakePower, airPower);
			
			lastSpeed = currentSpeed;
			S = Si;
			
			TrainAttribute.CRH_IS_RUN = 1;
			if (currentSpeed <= 0) { //如果速度减为0，则运行结束
//				this.saveRundataBeans(i, 0, Si, cp, airPower, elecBrakeForcePower);
				
				runnintDataFrame.appendConsoleArea("速度减至 0km/h");
				//标记计算
				TrainAttribute.CRH_IS_RUN = 0;
				TrainAttribute.CRH_IS_START = false;
				TrainAttribute.CRH_TEMP = 0;
			}
			
			BrakeForceMinTime.resetBrakeForce();
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return rundataBeansList;
	}
	
	/**
	 * 计算平均加速度
	 * @param currentSpeed
	 * @param time
	 * @return
	 */
	public double averageAcceleration(double currentSpeed,double time){
		double speed = currentSpeed;
		if(speed >= 200 && speed < 300 && isFirst1 == 0){
			aveAcceleration = currentSpeed / time;
			isFirst1 = 1;
		}else if(speed >= 300 && isFirst2 == 0){
			aveAcceleration = currentSpeed / time;
			isFirst2 = 1;
		}
		return aveAcceleration;
	}
	
	/**
	 * 2014.10.28 计算电制动力
	 */
	public double calculateElecBrake(double speed){ //km/h
		double elecBrake = 0;
		if(TrainAttribute.CRH_IS_BRAKE){ //如果刹车
			elecBrake = BrakeForceMinTime.getElecBrakeForce(speed); //N
		}
		return elecBrake;
	}
	
	/**
	 * 2014.11.1 计算制动力
	 */
	public double getManulBrakeForce(double speed){ //km/h
		double brakeForce = 0;
		if(TrainAttribute.CRH_IS_BRAKE){ //如果刹车
			brakeForce = BrakeForceMinTime.getManulBrakingForce(speed);
		}
		return brakeForce;
	}
	
	/**
	 * 2014.11.1 计算总的做功
	 */
	public double calculatePower(double tractionPower, double brakePower, double airPower){
		return tractionPower - airPower - 0.85 * brakePower;
	}
	
	/**
	 * 2014.11.1 计算阻力做功
	 */
	public double getComBrakePower(double brakeForce, double elecBrake, double s){ //km/h
		double power = 0;
		if(TrainAttribute.CRH_IS_BRAKE){ //如果刹车
			if(elecBrake > brakeForce){
				elecBrake = brakeForce;
			}
			power = elecBrake * s;
		}
		return power;
	}

	@Override
	public void run() {
		calculateFullData();
	}
	
}
