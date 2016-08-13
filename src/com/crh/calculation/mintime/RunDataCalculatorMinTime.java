package com.crh.calculation.mintime;

import java.util.ArrayList;

import com.crh.calculation.mintime.brakepoint.ReverseRunDataCalculatorMinTime;
import com.crh.service.MinTimeSimulationService;
import com.crh.service.RouteDataManagementDialogService;
import com.crh.service.TractionConfPanelService;
import com.crh2.calculate.MergeLists;
import com.crh2.calculate.TrainAttribute;
import com.crh2.javabean.Curve;
import com.crh2.javabean.Rundata;
import com.crh2.javabean.Section;
import com.crh2.javabean.Slope;
import com.crh2.javabean.SpecialSpeedLimitPoint;
import com.crh2.javabean.StationInfo;
import com.crh2.javabean.TrainElectricBrake;
import com.crh2.javabean.TrainStopStation;
import com.crh2.javabean.TrainTractionConf;
import com.crh2.service.DataService;
import com.crh2.util.MyTools;
import com.crh2.util.MyUtillity;

/**
 * ����С����ʱ�ַ��桱  �����������ݣ���randata
 * @author huhui
 *
 */
public class RunDataCalculatorMinTime {

	private OtherResistance otherResistance = null;
	private int routeId;
	/**
	 * �ṩ��·����
	 */
	private DataService dataService = new DataService();
	private ArrayList<Section> sectionList = null;
	private ArrayList<Slope> slopeList = null;
	private ArrayList<Curve> curveList = null;
	private ArrayList<StationInfo> stationList = null;
	private ArrayList<SpecialSpeedLimitPoint> specialSpeedLimitPointList = null;
	private int trainCategoryId;
	private double brakeLevel, maxSpeed, randomError;
	private String tractionLevel;
	/**
	 * �г�ͣվ��Ϣ
	 */
	private ArrayList<TrainStopStation> trainStopList = null;
	
	/**
	 * ����Rundata��javabean
	 */
	private ArrayList<Rundata> rundataBeansList = null;
	/**
	 * ����������ʱ��
	 */
	private int count = 0;
	
	/**
	 * �������
	 */
	private ReverseRunDataCalculatorMinTime brakePointCalculatorMinTime = null;
	
	/**
	 * �ϲ������������
	 */
	private MergeLists mergeLists = null;
	/**
	 * ��������
	 */
	private NetCurrent netCurrent = null;
	/**
	 *  ������
	 */
	private double globalTractionActualPower = 0;

	/**
	 * ���캯�������ڳ�ʼ��list
	 * @param routeName
	 * @param trainNum
	 * @param trainCategoryId
	 * @param tractionLevel
	 * @param brakeLevel
	 * @param maxSpeed
	 * @param randomError
	 */
	public RunDataCalculatorMinTime(String routeName, String trainNum,
			int trainCategoryId, String tractionLevel, double brakeLevel,
			double maxSpeed, double randomError) {
		routeId = MinTimeSimulationService.getRouteIdByName(routeName);
		// ��ʼ������list
		this.sectionList = dataService.getSectionData(routeId);
		this.slopeList = dataService.getSlopeData(routeId);
		this.curveList = dataService.getCurveData(routeId);
		this.stationList = dataService.getStationInfoData(routeId);
		this.specialSpeedLimitPointList = dataService.getSpeedLimitData(routeId);
		// ��ʼ��������������
		this.trainCategoryId = trainCategoryId;
		this.tractionLevel = tractionLevel;
		this.brakeLevel = brakeLevel;
		this.maxSpeed = maxSpeed - randomError;//��ȥ���
		TrainAttribute.CRH_MAX_SPEED = this.maxSpeed;
		TrainAttribute.CRH_DESTINATION_LOCATION = stationList.get(stationList.size()-1).getLocation();
		this.randomError = randomError;
		this.trainStopList = setStopStation(routeId, routeName, trainNum);
		netCurrent = new NetCurrent(trainCategoryId);//2014.12.1 ��������
		initFormulas();
		// ��ʼ����������
		otherResistance = new OtherResistance(slopeList, curveList);
		
		//�������
		brakePointCalculatorMinTime = new ReverseRunDataCalculatorMinTime(slopeList, curveList, stationList, netCurrent);
	}
	
	public static void main(String [] args){
		RunDataCalculatorMinTime runDataCalculatorMinTime = new RunDataCalculatorMinTime("�����ߣ����У�", "G99", 57, "", 9, 300, 0);
		runDataCalculatorMinTime.calculateFullData();
	}
	
	/**
	 * ��������վ֮�����������
	 */
	public void calculateFullData(){
		ArrayList<ArrayList<Rundata>> forwardList = new ArrayList<ArrayList<Rundata>>(), reverseList = new ArrayList<ArrayList<Rundata>>();//����ͷ�����������ݼ�����
		for(int i=0;i<trainStopList.size()-1;i++){
			ArrayList<Rundata> forwardRundata = calculateRunData(trainStopList.get(i), trainStopList.get(i+1));
			forwardList.add(forwardRundata);
		}
		
		for(int i = trainStopList.size()-1; i > 0; i--){
			ArrayList<Rundata> reverseRundata = brakePointCalculatorMinTime.calculatorRundataBack(trainStopList.get(i));
			reverseList.add(reverseRundata);
		}
		mergeLists = new MergeLists(forwardList, reverseList, trainStopList, routeId); 
	}
	
	/**
	 * 2014.10.21 ��StationInfo�е�վ��λ����Ϣд�뵽TrainStopStation��
	 */
	public ArrayList<TrainStopStation> setStopStation(int routeId,String routeName, String trainNum){
		ArrayList<TrainStopStation> stopStation = new ArrayList<TrainStopStation>();
		ArrayList<TrainStopStation> beanList = RouteDataManagementDialogService.getStationStopTime(routeId, routeName, trainNum);
		if(beanList.size() == stationList.size()){
			for(int i=0;i<beanList.size();i++){
				beanList.get(i).setLocation(stationList.get(i).getLocation());
			}
			//�Ƴ���ͣ����վ
			stopStation.add(beanList.get(0));//��վ
			for(int k=1;k<beanList.size()-1;k++){
				TrainStopStation bean = beanList.get(k);
				if(bean.getStopTIme() != 0){ //��վͣ
					stopStation.add(bean);
				}
			}
			stopStation.add(beanList.get(beanList.size()-1));//βվ
		}
		return stopStation;
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
	 * �жϵ�ǰ�ڷ��������Ƿ��е�
	 * @param Si
	 */
	public void hasElectricityNow(double Si){
		for(int i=0;i<sectionList.size();i++){
			Section section = sectionList.get(i);
			if(sectionList.get(i).getStart() <= Si*1000 && Si*1000 < sectionList.get(i).getEnd()){
				if(section.getElectricity() == 1){//�����е�
					CpMinTime.hasElectricity = true;
					break;
				}else if(section.getElectricity() == 0){//����û��
					CpMinTime.hasElectricity = false;
					break;
				}
			}
		}
	}
	
	/**
	 * ����Rundata��javabean
	 * @param runtime
	 * @param speed
	 * @param distance
	 * @param location
	 * @param tractionPower
	 * @param power
	 * @param cp
	 * @param tractionGridCurrent
	 * @param comBrakeGridCurrent
	 * @param acceleration
	 * @param tractionForce
	 * @param actualTractionPower
	 */
	public void saveRundataBeans(int runtime,double speed,double distance, double location, double tractionPower, double power, double cp, double tractionGridCurrent, double comBrakeGridCurrent, double acceleration, double tractionForce, double actualTractionPower){
		Rundata rundata = new Rundata();
		rundata.setRuntime(runtime);
		rundata.setSpeed(speed);
		rundata.setDistance(distance);
		rundata.setLocation(location);
		rundata.setCp(cp);
		rundata.setPower(power);
		rundata.setTractionPower(tractionPower);
		rundata.setTractionGridCurrent(tractionGridCurrent);
		rundata.setComBrakeGridCurrent(comBrakeGridCurrent);
		rundata.setAcceleration(acceleration);
		rundata.setTractionForce(tractionForce);
		rundata.setActualTractionPower(actualTractionPower);
		rundataBeansList.add(rundata);
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
	 * �������ݼ���
	 * @param station
	 * @param nextStation
	 * @return
	 */
	public ArrayList<Rundata> calculateRunData(TrainStopStation station, TrainStopStation nextStation) {
		int i = 0;
		double currentSpeed = 0;// �൱��Vi+1
		double lastSpeed = 0;// �൱��Vi
		double Si = 0, S=0;// ���
		String str = "";// д��txt�е�����
		double cp = 0;// ��Ϊ������Ҫ�޸ĵ�cpֵ
		double slopeCp = 0, curveCp = 0;// �ֱ����ڼ�¼��Ϊ�µ��������������Cpֵ
		ArrayList<String> strList = new ArrayList<String>();//���ڱ�����Ҫ��ӡ������
		double tractionPower = 0, power = 0, actualTractionPower = 0;;
		double tractionForce = 0, location = 0, tractionGridCurrent = 0, comBrakeGridCurrent = 0, acceleration = 0;
		rundataBeansList = new ArrayList<Rundata>();
		while (true) {
			location = Si + station.getLocation();
			str = (i+count) + "	" + currentSpeed + "	"+ Si + "	" + location + "		"+tractionPower;
			this.saveRundataBeans((i+count), currentSpeed, Si, location, tractionPower, power, cp, tractionGridCurrent, comBrakeGridCurrent, acceleration, tractionForce, actualTractionPower);
			this.hasElectricityNow(Si);
			currentSpeed = VnMinTime.calSpeed(currentSpeed, TrainAttribute.CRH_CAL_TIME_DIVISION, cp);
			slopeCp = OtherResistance.bySlope(location);
			double [] paras = OtherResistance.byCurve(currentSpeed, location);
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
			actualTractionPower = globalTractionActualPower + tractionPower;
//			System.out.println("s="+MyTools.numFormat2(location)+"----actualTractionPower="+MyTools.numFormat2(actualTractionPower)+"----globalTractionActualPower="+MyTools.numFormat2(globalTractionActualPower)+"----tractionPower="+MyTools.numFormat2(tractionPower));
			lastSpeed = currentSpeed;
			S = Si;
			if ((Si + station.getLocation()) >= nextStation.getLocation()) {
				count = i;
				globalTractionActualPower = tractionPower + globalTractionActualPower;
				break;
			}
		}
//		WriteIntoTxt.writeIntoTxt(strList, "CRH_10.23_�������.txt");
		return rundataBeansList;
	}

}
