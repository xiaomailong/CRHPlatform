package com.crh.calculation.fixedtime;

import java.util.ArrayList;
import java.util.Collections;

import com.crh.calculation.mintime.AirFrictionMinTime;
import com.crh.calculation.mintime.BrakeForceMinTime;
import com.crh.calculation.mintime.CpMinTime;
import com.crh.calculation.mintime.NetCurrent;
import com.crh.calculation.mintime.OtherResistance;
import com.crh.calculation.mintime.SnMinTime;
import com.crh.calculation.mintime.TractionForceMinTime;
import com.crh.calculation.mintime.TractionLevelForceMinTime;
import com.crh.calculation.mintime.VnMinTime;
import com.crh.calculation.mintime.brakepoint.ReverseOtherResistance;
import com.crh.service.MinTimeSimulationService;
import com.crh.service.TractionConfPanelService;
import com.crh2.calculate.MergeLists;
import com.crh2.calculate.TrainAttribute;
import com.crh2.javabean.Curve;
import com.crh2.javabean.Rundata;
import com.crh2.javabean.Slope;
import com.crh2.javabean.StationInfo;
import com.crh2.javabean.TrainElectricBrake;
import com.crh2.javabean.TrainTractionConf;
import com.crh2.javabean.V0V1E;
import com.crh2.service.DataService;
import com.crh2.util.MyUtillity;
import com.crh2.util.WriteIntoTxt;

/**
 * 2014.11.3 ���̶�ʱ�ַ��桱  �����������ݣ���rundata
 * @author huhui
 */
public class RunDataCalculatorFixedTime {

	/**
	 * ������������
	 */
	private OtherResistance otherResistance = null;
	/**
	 * ��·���
	 */
	private int routeId;
	/**
	 * �ṩ��·����
	 */
	private DataService dataService = new DataService();
	/**
	 * �µ�����
	 */
	private ArrayList<Slope> slopeList = null;
	/**
	 * �������
	 */
	private ArrayList<Curve> curveList = null;
	/**
	 * ��վ��Ϣ����
	 */
	private ArrayList<StationInfo> stationList = null;
	/**
	 * ����������
	 */
	private int trainCategoryId;
	/**
	 * �ƶ��ȼ�
	 */
	private double brakeLevel;
	/**
	 * ����ٶ�
	 */
	private double maxSpeed;
	/**
	 * ǣ����λ
	 */
	private String tractionLevel;
	/**
	 * ������ʼ����ʱ��1800s����ת��Сʱ
	 */
	private double T;
	/**
	 * ���������
	 */
	private double S;
	/**
	 * ������ʼv0
	 */
	private double initV0;
	
	/**
	 * ����Rundata��javabean
	 */
	private ArrayList<Rundata> rundataBeansList = null;
	/**
	 * 2014.10.25 ��������ʱ��Rundata
	 */
	private ArrayList<Rundata> avgRundataBeansList = null;
	/**
	 * 2014.10.25 �������ʱ��Rundata
	 */
	private ArrayList<Rundata> slowRundataBeansList = null;
	
	/**
	 * �������
	 */
	private ReverseRunDataCalculatorFixedTime brakePointCalculator = null;
	
	/**
	 * �ϲ������������
	 */
	private MergeLists mergeLists = null;
	/**
	 * ��������
	 */
	private NetCurrent netCurrent = null;
	/**
	 * �Ƿ񱣴��������
	 */
	public static final int CALCULATE_MODE = 0, CAL_SAVE_MODE = 1;
	
	public static V0V1E resultBean = null;
	
	/**
	 * ���캯�������ڳ�ʼ��list
	 * @param routeName
	 * @param trainNum
	 * @param trainCategoryId
	 * @param tractionLevel
	 * @param brakeLevel
	 * @param maxSpeed
	 * @param runTime
	 * @param initV0
	 */
	public RunDataCalculatorFixedTime(String routeName, String trainNum,
			int trainCategoryId, String tractionLevel, double brakeLevel,
			double maxSpeed, double runTime, double initV0) {
		routeId = MinTimeSimulationService.getRouteIdByName(routeName);
		// ��ʼ������list
		this.slopeList = dataService.getSlopeData(routeId);
		this.curveList = dataService.getCurveData(routeId);
		this.stationList = dataService.getStationInfoData(routeId);
		// ��ʼ��������������
		this.trainCategoryId = trainCategoryId;
		this.tractionLevel = tractionLevel;
		this.brakeLevel = brakeLevel;
		this.maxSpeed = maxSpeed;
		this.T = runTime/3600;//ת��Сʱ
		TrainAttribute.CRH_MAX_SPEED = initV0;
		TrainAttribute.CRH_DESTINATION_LOCATION = stationList.get(stationList.size()-1).getLocation();//�յ�վλ��
		this.S = TrainAttribute.CRH_DESTINATION_LOCATION;
		this.initV0 = initV0;
		netCurrent = new NetCurrent(trainCategoryId);//2014.12.1 ��������
		initFormulas();
		// ��ʼ��OtherResistance ����������
		otherResistance = new OtherResistance(slopeList, curveList);
		
		//�������
		brakePointCalculator = new ReverseRunDataCalculatorFixedTime(slopeList, curveList, netCurrent);
	}
	
	public static void main(String [] args){
		RunDataCalculatorFixedTime runDataCalculatorMinTime = new RunDataCalculatorFixedTime("�����ߣ����У�", "G99", 57, "", 9, 300, 1800, 280.46);//275.46
		runDataCalculatorMinTime.calculateFullData();
	}
	
	/**
	 * ��������վ֮�����������
	 */
	public void calculateFullData(){
		V0V1E minBean = calculateV0AndV1();
		resultBean = minBean;
//		System.out.println(minBean.getV0()+", "+minBean.getV1()+", "+minBean.getE());
		rundataBeansList = new ArrayList<Rundata>(); //��ʼ��List���ڱ�������������������
		avgRundataBeansList = new ArrayList<Rundata>();//������������
		slowRundataBeansList = new ArrayList<Rundata>();//�����������
		
		//���㲢��������������������
		calculateT0AndS0(minBean.getV0(), CAL_SAVE_MODE);
		calAirAndResistancePower(minBean.getS0End(), minBean.getS1End(), minBean.getV0(), CAL_SAVE_MODE);
		calculateT2AndS2(minBean.getV0(), minBean.getV1(), minBean.getS3(), CAL_SAVE_MODE);
		
		//�ϲ�ǰ������������
		mergeForwardRundataList();
		
		ArrayList<Rundata> forwardRundata = rundataBeansList;
		
		brakePointCalculator.calculateT3AndS3(minBean.getV1(), CAL_SAVE_MODE);
		ArrayList<Rundata> reverseRundata = brakePointCalculator.getRundataBackBeansList();
		mergeLists = new MergeLists(routeId, forwardRundata, reverseRundata);
	}
	
	/**
	 * initV0��Ӧ�� v1 E
	 * @return
	 */
	public V0V1E calculateV0AndV1(){
		double v0 = initV0;
		double v1 = v0;
		double s0, t0, E0, s1, t1 , E1, s2=0 ,t2, s3, t3, E3, E;
		V0V1E bean = null;
		//ѭ������
		while(true){
			//1.t0��s0
			double [] runPara0 = calculateT0AndS0(v0, CALCULATE_MODE);
			//2.t3��s3
			--v1;
			double [] runPara3 = brakePointCalculator.calculateT3AndS3(v1, CALCULATE_MODE);
			t0 = runPara0[0];//s
			s0 = runPara0[1];
			E0 = runPara0[2];
			t3 = runPara3[0];//s
			s3 = runPara3[1];
			E3 = runPara3[2];
			
			//3.t2��s2
			double [] runPara2 = calculateT2AndS2(v0, v1, s3, CALCULATE_MODE);
			t2 = runPara2[0];//s
			s2 = runPara2[1];
			
			//4.t1
			t1 = calculateT1(s0, s2, s3, v0);//h
			//5.����ʽ
			double tempT = T - ((t0 + t2 + t3)/3600 + t1);
			if(tempT <= 0){
				double s1EndLocation = S - s2 - s3;
				E1 = calAirAndResistancePower(s0, s1EndLocation, v0, CALCULATE_MODE);
				E = calculateE(E0, E1, E3);
//					System.out.println("v0 = "+ v0 +", v1 = "+ v1 + ", E = " + E);
				bean = new V0V1E(v0, v1, E, s0, s1EndLocation, s3);
				break;
			}
		}
		return bean;
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
	 * �жϵ�ǰ�ڷ��������Ƿ��е磬 ʼ���е� ������Ҫ������
	 * @param Si ��ǰ���
	 */
	public void hasElectricityNow(double Si){
		CpMinTime.hasElectricity = true;//2014.10.22
	}
	
	/**
	 * ����Rundata��javabean
	 * @param runtime
	 * @param speed
	 * @param location
	 * @param tractionPower
	 * @param power
	 * @param cp
	 * @param tractionGridCurrent
	 * @param comBrakeGridCurrent
	 * @param acceleration
	 * @param tractionForce
	 */
	public void saveRundataBeans(int runtime,double speed, double location, double tractionPower, double power, double cp, double tractionGridCurrent, double comBrakeGridCurrent, double acceleration, double tractionForce){
		Rundata rundata = new Rundata();
		rundata.setRuntime(runtime);
		rundata.setSpeed(speed);
		rundata.setLocation(location);
		rundata.setCp(cp);
		rundata.setTractionPower(tractionPower);
		rundata.setPower(power);
		rundata.setTractionGridCurrent(tractionGridCurrent);
		rundata.setComBrakeGridCurrent(comBrakeGridCurrent);
		rundata.setAcceleration(acceleration);
		rundata.setTractionForce(tractionForce);
		rundataBeansList.add(rundata);
	}
	
	/**
	 * ����������Rundata��javabean
	 * @param runtime
	 * @param speed
	 * @param location
	 * @param tractionPower
	 * @param cp
	 * @param tractionGridCurrent
	 * @param comBrakeGridCurrent
	 * @param acceleration
	 * @param tractionForce
	 */
	public void saveAvgRundataBeans(int runtime,double speed, double location, double tractionPower, double cp, double tractionGridCurrent, double comBrakeGridCurrent, double acceleration, double tractionForce){
		Rundata rundata = new Rundata();
		rundata.setRuntime(runtime);
		rundata.setSpeed(speed);
		rundata.setLocation(location);
		rundata.setCp(cp);
		rundata.setTractionPower(tractionPower);
		rundata.setTractionGridCurrent(tractionGridCurrent);
		rundata.setComBrakeGridCurrent(comBrakeGridCurrent);
		rundata.setAcceleration(acceleration);
		rundata.setTractionForce(tractionForce);
		avgRundataBeansList.add(rundata);
	}
	
	/**
	 * �������Rundata��javabean
	 * @param runtime
	 * @param speed
	 * @param location
	 * @param power
	 * @param cp
	 * @param tractionGridCurrent
	 * @param comBrakeGridCurrent
	 * @param acceleration
	 * @param tractionForce
	 */
	public void saveSlowRundataBeans(int runtime,double speed, double location, double power, double cp, double tractionGridCurrent, double comBrakeGridCurrent, double acceleration, double tractionForce){
		Rundata rundata = new Rundata();
		rundata.setRuntime(runtime);
		rundata.setSpeed(speed);
		rundata.setLocation(location);
		rundata.setCp(cp);
		rundata.setPower(power);
		rundata.setTractionGridCurrent(tractionGridCurrent);
		rundata.setComBrakeGridCurrent(comBrakeGridCurrent);
		rundata.setAcceleration(acceleration);
		rundata.setTractionForce(tractionForce);
		slowRundataBeansList.add(rundata);
	}
	
	/**
	 * 2014.10.25 �ϲ�ǰ������������
	 */
	public void mergeForwardRundataList(){
		double e0 = rundataBeansList.get(rundataBeansList.size() - 1).getTractionPower();
		//�ϲ���������ʱ������
		for(Rundata bean : avgRundataBeansList){
			double power = bean.getTractionPower() + e0;
			bean.setTractionPower(power);
			rundataBeansList.add(bean);
		}
		//�ϲ�����ʱ������
		Collections.reverse(slowRundataBeansList);
		for(Rundata bean : slowRundataBeansList){
			rundataBeansList.add(bean);
		}
	}
	
	/**
	 * ����ǣ����
	 */
	public void initTractionForce(){
		if (tractionLevel.equals("���ǣ��(�޼�)")) {
			TrainTractionConf trainTractionConf = MinTimeSimulationService.getTrainTractionConf(trainCategoryId);
			TractionForceMinTime.Fst = trainTractionConf.getFst();
			TractionForceMinTime.F1 = trainTractionConf.getF1();
			TractionForceMinTime.F2 = trainTractionConf.getF2();
			TractionForceMinTime.v1 = trainTractionConf.getV1();
			TractionForceMinTime.v2 = trainTractionConf.getV2();
			TractionForceMinTime.P1 = trainTractionConf.getP1();
			TractionForceMinTime.mode = 1;
			TractionForceMinTime.tractionType = 0;
		}else{
			TractionLevelForceMinTime.trainTractionLevelConf = TractionConfPanelService.getTrainTractionLevelConf(trainCategoryId);
			TractionForceMinTime.tractionType = 1;
			TractionForceMinTime.level = MyUtillity.getNumFromString(tractionLevel);
		}
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
	
	/*
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
	 * ����������ٽ׶Σ����������͸����������� = ǣ��������
	 * @param s1StartLocation ���ٿ�ʼ����
	 * @param s1EndLocation ���ٽ�������
	 * @param v ����ʱ�ٶ�ֵ
	 * @return ���������͸�����������
	 */
	public double calAirAndResistancePower(double s1StartLocation, double s1EndLocation, double v, int mode) {
		int i = 0;
		double currentSpeed = v;// �൱��Vi+1
		double lastSpeed = v;// �൱��Vi
		double Si = 0, S = 0;// ���
		String str = "";// д��txt�е�����
		double cp = 0;// ��Ϊ������Ҫ�޸ĵ�cpֵ
		double slopeCp = 0, curveCp = 0;// �ֱ����ڼ�¼��Ϊ�µ��������������Cpֵ
		ArrayList<String> strList = new ArrayList<String>();//���ڱ�����Ҫ��ӡ������
		double tractionForce = 0, tractionPower = 0, tractionGridCurrent = 0, comBrakeGridCurrent = 0, acceleration = 0;
		double location = 0;
		while (true) {
			location = s1StartLocation + Si;
			str = i + "	" + currentSpeed + "	"+ Si + "		" + tractionPower;
			if(mode == CAL_SAVE_MODE){
				this.saveAvgRundataBeans(i, currentSpeed, location, tractionPower, cp, tractionGridCurrent, comBrakeGridCurrent, acceleration, tractionForce);
			}
			this.hasElectricityNow(Si);
			slopeCp = OtherResistance.bySlope(location);// �µ���������
			double [] paras = OtherResistance.byCurve(currentSpeed, location);// �������
			curveCp = paras[1];
			cp = slopeCp + curveCp;
			Si = SnMinTime.calShift(currentSpeed, lastSpeed, Si);
			strList.add(str);
			
			i++;
			tractionForce = AirFrictionMinTime.getAirFriction(currentSpeed) + 9.8 * AirFrictionMinTime.m * cp;
			tractionGridCurrent = netCurrent.getTractionNetCurrent(tractionForce, currentSpeed);
			comBrakeGridCurrent = netCurrent.getComBrakeForceNetCurrent(0, currentSpeed);
			tractionPower += tractionForce * (Si-S);//��������
			lastSpeed = currentSpeed;
			S = Si;
			if (Si >= (s1EndLocation - s1StartLocation)) {
				break;
			}
		}
		return Math.abs(tractionPower);
	}
	
	/**
	 * �������t2��s2�������н׶���v0->v1
	 * ʵ�ʼ���ʱ������������㣬��v1���ٵ�v0
	 */
	public double [] calculateT2AndS2(double v0, double v1, double s3EndLocation, int mode){
		
		int i = 0;
		double currentSpeed = v1;// �൱��Vi+1
		double lastSpeed = v1;// �൱��Vi
		double Si = 0;// ���
		String str = "";// д��txt�е�����
		double cp = 0;// ��Ϊ������Ҫ�޸ĵ�cpֵ
		double slopeCp = 0, curveCp = 0;// �ֱ����ڼ�¼��Ϊ�µ��������������Cpֵ
		ArrayList<String> strList = new ArrayList<String>();//���ڱ�����Ҫ��ӡ������
		double [] paraArray = new double [2];//0��ʾʱ��t��1��ʾ���s
		double tractionForce = 0, tractionPower = 0, power = 0, tractionGridCurrent = 0, comBrakeGridCurrent = 0, acceleration = 0;
		double location = 0;
		double s = 0;
		while (true) {
			s = Si + s3EndLocation;
			location = TrainAttribute.CRH_DESTINATION_LOCATION - s;
			if(mode == CAL_SAVE_MODE){
				this.saveSlowRundataBeans(i, currentSpeed, location, 0, cp, tractionGridCurrent, comBrakeGridCurrent, acceleration, tractionForce);
			}
			str = i + "	" + currentSpeed + "	"+ location;
			currentSpeed = VnMinTime.calSpeedSlow(currentSpeed, TrainAttribute.CRH_CAL_TIME_DIVISION, cp);//�ٶ�
			slopeCp = ReverseOtherResistance.bySlope(s);// �µ���������
			double [] paras = ReverseOtherResistance.byCurve(currentSpeed, s);// �������
			currentSpeed = paras[0];
			curveCp = paras[1];
			cp = slopeCp + curveCp;
			Si = SnMinTime.calShift(currentSpeed, lastSpeed, Si);
			strList.add(str);
			acceleration = CpMinTime.getAcceleretionSlow(currentSpeed);//���н׶�����ٶ�
			tractionGridCurrent = netCurrent.getTractionNetCurrent(tractionForce, currentSpeed);
			comBrakeGridCurrent = netCurrent.getComBrakeForceNetCurrent(0, currentSpeed);
			i++;
			lastSpeed = currentSpeed;
			if (currentSpeed >= v0) {
				paraArray[0] = i;
				paraArray[1] = Si;
				break;
			}
		}
//		WriteIntoTxt.writeIntoTxt(strList, "10.23_t2_s2 _����.txt");
		return paraArray;
	}
	
	/**
	 * �������t0��s0�������ٽ׶�
	 * @param mode 0��������������ݣ�1�������������
	 */
	public double [] calculateT0AndS0(double v0, int mode) {
		int i = 0;
		double currentSpeed = 0;// �൱��Vi+1
		double lastSpeed = 0;// �൱��Vi
		double Si = 0, S = 0;// ���
		String str = "";// д��txt�е�����
		double cp = 0;// ��Ϊ������Ҫ�޸ĵ�cpֵ
		double slopeCp = 0, curveCp = 0;// �ֱ����ڼ�¼��Ϊ�µ��������������Cpֵ
		ArrayList<String> strList = new ArrayList<String>();//���ڱ�����Ҫ��ӡ������
		double tractionForce = 0, tractionPower = 0, power = 0, tractionGridCurrent = 0, comBrakeGridCurrent = 0, acceleration = 0;
		double [] paraArray = new double [3];//0��ʾʱ��t��1��ʾ���s��2��ʾ����E
		while (true) {
			str = i + "	" + currentSpeed + "	"+ Si + "		" + tractionPower;
			if(mode == CAL_SAVE_MODE){
				this.saveRundataBeans(i, currentSpeed, Si, tractionPower, power, cp, tractionGridCurrent, comBrakeGridCurrent, acceleration, tractionForce);
			}
			this.hasElectricityNow(Si);
			currentSpeed = VnMinTime.calSpeed(currentSpeed, TrainAttribute.CRH_CAL_TIME_DIVISION, cp);//�ٶ�
			slopeCp = OtherResistance.bySlope(Si);// �µ���������
			double [] paras = OtherResistance.byCurve(currentSpeed, Si);// �������
			currentSpeed = paras[0];
			curveCp = paras[1];
			cp = slopeCp + curveCp;
			Si = SnMinTime.calShift(currentSpeed, lastSpeed, Si);
			strList.add(str);
			
			i++;
			acceleration = CpMinTime.getAcceleration(currentSpeed);
			tractionForce = TractionForceMinTime.getTractionForce(currentSpeed);
			tractionGridCurrent = netCurrent.getTractionNetCurrent(tractionForce, currentSpeed);
			comBrakeGridCurrent = netCurrent.getComBrakeForceNetCurrent(0, currentSpeed);
			tractionPower += tractionForce * (Si-S);//��������
			power = tractionPower;
			lastSpeed = currentSpeed;
			S = Si;
			if (currentSpeed >= v0) {
				paraArray[0] = i;
				paraArray[1] = Si;
				paraArray[2] = tractionPower;
				break;
			}
		}
//		WriteIntoTxt.writeIntoTxt(strList, "10.23_t0_s0 _����.txt");
		return paraArray;
	}
	
	/**
	 * ����������
	 * @param E0
	 * @param E1
	 * @param E3
	 * @return
	 */
	public double calculateE(double E0, double E1, double E3){
		return E0 + E1 - 0.85 * E3;
	}
	
	/**
	 *�������t1�������ٽ׶�ʱ�� 
	 */
	public double calculateT1(double s0, double s2, double s3, double v0){
		return (S - s0 - s2 - s3) / v0;
	}
	
	/**
	 *���������е���ʼ������location������������ 
	 */
	public double calculateS1End(double s3){
		return S - s3;
	}
	
	/**
	 * ���ݼ���
	 * @return
	 */
	public ArrayList<Rundata> calculateRunData() {
		int i = 0;
		double currentSpeed = 0;// �൱��Vi+1
		double lastSpeed = 0;// �൱��Vi
		double Si = 0, S=0;// ���
		String str = "";// д��txt�е�����
		double cp = 0;// ����������
		double slopeCp = 0, curveCp = 0;// �µ��������������
		ArrayList<String> strList = new ArrayList<String>();//���ڱ�����Ҫ��ӡ������
		double power = 0;//����
		rundataBeansList = new ArrayList<Rundata>();
		while (true) {
			str = i + "	" + currentSpeed + "	"+ Si + "		"+power;
//			this.saveRundataBeans(i, currentSpeed, Si, power, power, cp);
			this.hasElectricityNow(Si);//���������㲿��
			currentSpeed = VnMinTime.calSpeed(currentSpeed, TrainAttribute.CRH_CAL_TIME_DIVISION, cp);//�ٶ�
			slopeCp = OtherResistance.bySlope(Si);// �µ���������
			double [] paras = OtherResistance.byCurve(currentSpeed, Si);// �������
			currentSpeed = paras[0];
			curveCp = paras[1];
			cp = slopeCp + curveCp;//�µ�������ĸ�������
			Si = SnMinTime.calShift(currentSpeed, lastSpeed, Si);//���
			
			strList.add(str);
			
			i++;
			power += TractionForceMinTime.getTractionForce(currentSpeed)*(Si-S);//��������
			lastSpeed = currentSpeed;
			S = Si;
			if (Si >= TrainAttribute.CRH_DESTINATION_LOCATION) {
				break;
			}
		}
		System.out.println("�̶�ʱ��_�������_���");
		WriteIntoTxt.writeIntoTxt(strList, "CRH_10.23_�̶�ʱ��_�������.txt");
		return rundataBeansList;
	}

}
