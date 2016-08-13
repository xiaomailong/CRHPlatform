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
 * “最小运行时分仿真”  计算运行数据，即randata
 * @author huhui
 *
 */
public class RunDataCalculatorMinTime {

	private OtherResistance otherResistance = null;
	private int routeId;
	/**
	 * 提供线路数据
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
	 * 列车停站信息
	 */
	private ArrayList<TrainStopStation> trainStopList = null;
	
	/**
	 * 保存Rundata的javabean
	 */
	private ArrayList<Rundata> rundataBeansList = null;
	/**
	 * 计数，代表时间
	 */
	private int count = 0;
	
	/**
	 * 反向计算
	 */
	private ReverseRunDataCalculatorMinTime brakePointCalculatorMinTime = null;
	
	/**
	 * 合并正反向计算结果
	 */
	private MergeLists mergeLists = null;
	/**
	 * 计算网流
	 */
	private NetCurrent netCurrent = null;
	/**
	 *  总能量
	 */
	private double globalTractionActualPower = 0;

	/**
	 * 构造函数，用于初始化list
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
		// 初始化各个list
		this.sectionList = dataService.getSectionData(routeId);
		this.slopeList = dataService.getSlopeData(routeId);
		this.curveList = dataService.getCurveData(routeId);
		this.stationList = dataService.getStationInfoData(routeId);
		this.specialSpeedLimitPointList = dataService.getSpeedLimitData(routeId);
		// 初始化其它几个参数
		this.trainCategoryId = trainCategoryId;
		this.tractionLevel = tractionLevel;
		this.brakeLevel = brakeLevel;
		this.maxSpeed = maxSpeed - randomError;//减去误差
		TrainAttribute.CRH_MAX_SPEED = this.maxSpeed;
		TrainAttribute.CRH_DESTINATION_LOCATION = stationList.get(stationList.size()-1).getLocation();
		this.randomError = randomError;
		this.trainStopList = setStopStation(routeId, routeName, trainNum);
		netCurrent = new NetCurrent(trainCategoryId);//2014.12.1 计算网流
		initFormulas();
		// 初始化附加阻力
		otherResistance = new OtherResistance(slopeList, curveList);
		
		//反向计算
		brakePointCalculatorMinTime = new ReverseRunDataCalculatorMinTime(slopeList, curveList, stationList, netCurrent);
	}
	
	public static void main(String [] args){
		RunDataCalculatorMinTime runDataCalculatorMinTime = new RunDataCalculatorMinTime("京津线（下行）", "G99", 57, "", 9, 300, 0);
		runDataCalculatorMinTime.calculateFullData();
	}
	
	/**
	 * 计算所有站之间的运行数据
	 */
	public void calculateFullData(){
		ArrayList<ArrayList<Rundata>> forwardList = new ArrayList<ArrayList<Rundata>>(), reverseList = new ArrayList<ArrayList<Rundata>>();//正向和反向的运行数据计算结果
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
	 * 2014.10.21 将StationInfo中的站的位置信息写入到TrainStopStation中
	 */
	public ArrayList<TrainStopStation> setStopStation(int routeId,String routeName, String trainNum){
		ArrayList<TrainStopStation> stopStation = new ArrayList<TrainStopStation>();
		ArrayList<TrainStopStation> beanList = RouteDataManagementDialogService.getStationStopTime(routeId, routeName, trainNum);
		if(beanList.size() == stationList.size()){
			for(int i=0;i<beanList.size();i++){
				beanList.get(i).setLocation(stationList.get(i).getLocation());
			}
			//移除不停车的站
			stopStation.add(beanList.get(0));//首站
			for(int k=1;k<beanList.size()-1;k++){
				TrainStopStation bean = beanList.get(k);
				if(bean.getStopTIme() != 0){ //此站停
					stopStation.add(bean);
				}
			}
			stopStation.add(beanList.get(beanList.size()-1));//尾站
		}
		return stopStation;
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
	 * 判断当前在分相区内是否有电
	 * @param Si
	 */
	public void hasElectricityNow(double Si){
		for(int i=0;i<sectionList.size();i++){
			Section section = sectionList.get(i);
			if(sectionList.get(i).getStart() <= Si*1000 && Si*1000 < sectionList.get(i).getEnd()){
				if(section.getElectricity() == 1){//代表有电
					CpMinTime.hasElectricity = true;
					break;
				}else if(section.getElectricity() == 0){//代表没电
					CpMinTime.hasElectricity = false;
					break;
				}
			}
		}
	}
	
	/**
	 * 保存Rundata的javabean
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
	 * 设置牵引力
	 */
	public void initTractionForce(){
		if (tractionLevel.equals("最大牵引(无级)")) {
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
	 * 运行数据计算
	 * @param station
	 * @param nextStation
	 * @return
	 */
	public ArrayList<Rundata> calculateRunData(TrainStopStation station, TrainStopStation nextStation) {
		int i = 0;
		double currentSpeed = 0;// 相当于Vi+1
		double lastSpeed = 0;// 相当于Vi
		double Si = 0, S=0;// 里程
		String str = "";// 写入txt中的内容
		double cp = 0;// 因为阻力需要修改的cp值
		double slopeCp = 0, curveCp = 0;// 分别用于记录因为坡道和弯道所修正的Cp值
		ArrayList<String> strList = new ArrayList<String>();//用于保存需要打印的数据
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
			tractionPower += tractionForce * (Si-S);//计算做功
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
//		WriteIntoTxt.writeIntoTxt(strList, "CRH_10.23_正向计算.txt");
		return rundataBeansList;
	}

}
