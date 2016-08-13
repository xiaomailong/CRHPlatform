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
 * ʵʱ���з��棬�����������ݣ���randata
 * @author huhui
 */
public class RunDataCalculatorRealTime implements Runnable {

	private OtherResistance otherResistance = null;
	/**
	 * �ṩ��·����
	 */
	private DataService dataService = new DataService();
	private ArrayList<Slope> slopeList = null;
	private ArrayList<Curve> curveList = null;
	private ArrayList<StationInfo> stationList = null;
	private int trainCategoryId;
	/**
	 *  2013.7.11���ӣ�����Rundata��javabean
	 */
	public static ArrayList<Rundata> rundataBeansList = null;
	/**
	 * ʵʱ��ʾ�������
	 */
	private RealTimeRunningDataDialog runnintDataFrame = null;
	private int routeId;
	
	/**
	 * ƽ�����ٶ�
	 */
	private double aveAcceleration = 0;
	private int isFirst1 = 0,isFirst2 = 0;
	/**
	 * ��������
	 */
	private NetCurrent netCurrent = null;
	

	/**
	 *  ���캯�������ڳ�ʼ��list
	 * @param routeName
	 * @param trainNum
	 * @param trainCategoryId
	 */
	public RunDataCalculatorRealTime(String routeName, String trainNum, int trainCategoryId) {
		this.routeId = MinTimeSimulationService.getRouteIdByName(routeName);
		// ��ʼ������list
		this.slopeList = dataService.getSlopeData(routeId);
		this.curveList = dataService.getCurveData(routeId);
		this.stationList = dataService.getStationInfoData(routeId);
		// ��ʼ��������������
		this.trainCategoryId = trainCategoryId;
		TrainAttribute.CRH_DESTINATION_LOCATION = stationList.get(stationList.size()-1).getLocation();//�յ�վλ��
		netCurrent = new NetCurrent(trainCategoryId);
		initFormulas();
		// ��ʼ��OtherResistance ����������
		otherResistance = new OtherResistance(slopeList, curveList);
		rundataBeansList = new ArrayList<Rundata>();
	}
	
	/**
	 * ���㳣̬���е�����
	 */
	public void calculateFullData() {
		calculateRunData();
		//ÿ�μ����꽫�г���Ϊ������״̬
		TrainAttribute.CRH_IS_CONSTANT_SPEED = false;
	}
	
	/**
	 * ��ǣ�����������������ƶ�����ʽ��ֵ
	 */
	public void initFormulas(){
		this.initTractionForce();
		this.initAirFriction();
		this.initBrakeForce();
	}
	
	/**
	 * ����ǣ����
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
	 * ���ÿ�������
	 */
	public void initAirFriction(){
		double [] abc = MinTimeSimulationService.getAirFrictionParameters(trainCategoryId);
		AirFrictionMinTime.a = abc[0];
		AirFrictionMinTime.b = abc[1];
		AirFrictionMinTime.c = abc[2];
		AirFrictionMinTime.m = abc[4];
	}
	
	/**
	 * �����ƶ���
	 */
	public void initBrakeForce(){
		BrakeForceMinTime.setBrakeList(MinTimeSimulationService.getTrainBrakeFactor(trainCategoryId));//�ƶ���ϵ��
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
	 *  ����Rundata��javabean
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
	 *  ���ݼ���
	 * @return
	 */
	public ArrayList<Rundata> calculateRunData() {
		int i = 0;
		double currentSpeed = 0, lastSpeed = 0;
		double Si = 0, S = 0;
		double tractionForce = 0, airFriction = 0, elecBrake = 0, brakeForce = 0;
		double averageAcc = 0;
		double curveCp = 0, slopeCp = 0, cp = 0;
		String isOverSpeed = "";//�Ƿ���
		double tractionPower = 0, brakePower = 0, airPower = 0, elecPower = 0, power = 0;
		double tractionGridCurrent = 0, comBrakeGridCurrent = 0, acceleration = 0;
		//2015.4.13 ��ճ�̬��������
		System.err.println("size = "+RunDataCalculatorRealTime.rundataBeansList.size());
		if(RunDataCalculatorRealTime.rundataBeansList != null){
			RunDataCalculatorRealTime.rundataBeansList.clear();
		}
		//��ǿ�ʼ����
		TrainAttribute.CRH_IS_RUN = 1;
		//ʵʱ��ʾ�������
		runnintDataFrame = new RealTimeRunningDataDialog();
		Thread displayThread = new Thread(runnintDataFrame);
		displayThread.start();
		runnintDataFrame.appendConsoleArea("׼������");
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
				JOptionPane.showMessageDialog(runnintDataFrame, "�������");
				runnintDataFrame.dispose();
				break;
			}
			this.saveRundataBeans(i, currentSpeed, Si, tractionPower, elecPower, power, tractionForce, brakeForce, airFriction, averageAcc, isOverSpeed, acceleration, tractionGridCurrent, comBrakeGridCurrent);
			
			slopeCp = OtherResistance.bySlope(Si);
			double [] paras = OtherResistance.byCurve(currentSpeed, Si);
			curveCp = paras[1];
			cp = slopeCp + curveCp;
			isOverSpeed = currentSpeed > paras[0] ? "��" : "��"; //�Ƿ���
			if(isOverSpeed.equals("��")){
				runnintDataFrame.appendConsoleArea("�������");
			}
			tractionForce = TractionForceMinTime.getTractionForce(currentSpeed); // ����ǣ����
			airFriction = AirFrictionMinTime.getAirFriction(currentSpeed); // �����������
			elecBrake = calculateElecBrake(currentSpeed); //���ƶ���
			brakeForce =  getManulBrakeForce(currentSpeed); //�ƶ���
			currentSpeed = VnMinTime.calSpeedRealTime(currentSpeed, TrainAttribute.CRH_CAL_TIME_DIVISION, cp, TrainAttribute.CRH_BRAKE_LEVEL); //�ٶ�
			averageAcc = averageAcceleration(currentSpeed, i);
			Si = SnMinTime.calShift(currentSpeed, lastSpeed, Si); //���
			
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
			if (currentSpeed <= 0) { //����ٶȼ�Ϊ0�������н���
//				this.saveRundataBeans(i, 0, Si, cp, airPower, elecBrakeForcePower);
				
				runnintDataFrame.appendConsoleArea("�ٶȼ��� 0km/h");
				//��Ǽ���
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
	 * ����ƽ�����ٶ�
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
	 * 2014.10.28 ������ƶ���
	 */
	public double calculateElecBrake(double speed){ //km/h
		double elecBrake = 0;
		if(TrainAttribute.CRH_IS_BRAKE){ //���ɲ��
			elecBrake = BrakeForceMinTime.getElecBrakeForce(speed); //N
		}
		return elecBrake;
	}
	
	/**
	 * 2014.11.1 �����ƶ���
	 */
	public double getManulBrakeForce(double speed){ //km/h
		double brakeForce = 0;
		if(TrainAttribute.CRH_IS_BRAKE){ //���ɲ��
			brakeForce = BrakeForceMinTime.getManulBrakingForce(speed);
		}
		return brakeForce;
	}
	
	/**
	 * 2014.11.1 �����ܵ�����
	 */
	public double calculatePower(double tractionPower, double brakePower, double airPower){
		return tractionPower - airPower - 0.85 * brakePower;
	}
	
	/**
	 * 2014.11.1 ������������
	 */
	public double getComBrakePower(double brakeForce, double elecBrake, double s){ //km/h
		double power = 0;
		if(TrainAttribute.CRH_IS_BRAKE){ //���ɲ��
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
