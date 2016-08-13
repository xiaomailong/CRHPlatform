package com.crh.doc;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;

import com.crh.calculation.mintime.TractionForceMinTime;
import com.crh.service.BrakeConfPanelService;
import com.crh.service.RouteDataManagementDialogService;
import com.crh.service.TractionConfPanelService;
import com.crh.view.panel.BrakeConfPanel;
import com.crh.view.panel.ChartFactoryPanel;
import com.crh.view.panel.TractionConfPanel;
import com.crh.view.panel.TractionLevelConfPanel;
import com.crh2.calculate.TractionFormulas;
import com.crh2.javabean.Curve;
import com.crh2.javabean.CurveFormulaBean;
import com.crh2.javabean.Rundata;
import com.crh2.javabean.Slope;
import com.crh2.javabean.TractionLevelFormulaBean;
import com.crh2.javabean.TrainBrakeFactor;
import com.crh2.service.DataService;

/**
 * 2014.11.30 负责生成曲线图表
 * @author huhui
 *
 */
public class CreateChartService {
	
	/**
	 * 图片临时保存路径
	 */
	public static final String PATH = "C:\\CRHPlatformTemp";
	
	//定义曲线名称
	/**
	 * 1.10	列车牵引特性曲线
	 */
	public static String  chart_1_10 = "chart_1_10";
	/**
	 * 1.11.1	紧急制动、8级制动
	 */
	public static String  chart_1_11_1 = "chart_1_11_1";
	/**
	 * 1.11.2	电制动
	 */
	public static String  chart_1_11_2 = "chart_1_11_2";
	/**
	 * 2.1.2	启动性能曲线
	 */
	public static String  chart_2_1_2 = "chart_2_1_2";
	/**
	 * 2.2.2	制动性能设计（8级制动）
	 */
	public static String  chart_2_2_2 = "chart_2_2_2";
	/**
	 * 3.1	线路坡度vs距离
	 */
	public static String  chart_3_1 = "chart_3_1";
	/**
	 * 3.2	线路限速vs距离
	 */
	public static String  chart_3_2 = "chart_3_2";
	/**
	 * 3.3	速度vs时间
	 */
	public static String  chart_3_3 = "chart_3_3";
	/**
	 * 3.4	速度vs距离
	 */
	public static String  chart_3_4 = "chart_3_4";
	/**
	 * 3.5	加速度vs时间
	 */
	public static String  chart_3_5 = "chart_3_5";
	/**
	 * 3.6	加速度vs距离
	 */
	public static String  chart_3_6 = "chart_3_6";
	/**
	 * 3.7	牵引力、制动力vs距离
	 */
	public static String  chart_3_7 = "chart_3_7";
	/**
	 * 3.8	牵引力、制动力vs时间
	 */
	public static String  chart_3_8 = "chart_3_8";
	/**
	 * 3.9	牵引能耗vs距离
	 */
	public static String  chart_3_9 = "chart_3_9";
	/**
	 * 3.10	牵引能耗vs时间
	 */
	public static String  chart_3_10 = "chart_3_10";
	/**
	 * 3.11	再生电制动能量vs距离
	 */
	public static String  chart_3_11 = "chart_3_11";
	/**
	 * 3.12	再生电制动能量vs时间
	 */
	public static String  chart_3_12 = "chart_3_12";
	/**
	 * 3.13	时间vs距离
	 */
	public static String  chart_3_13 = "chart_3_13";
	/**
	 * 3.14	网流vs距离 3.14.1	牵引
	 */
	public static String  chart_3_14_1 = "chart_3_14_1";
	/**
	 * 3.14	网流vs距离 3.14.2	再生制动
	 */
	public static String  chart_3_14_2 = "chart_3_14_2";
	/**
	 * 3.15	网流vs时间  3.15.1	牵引
	 */
	public static String  chart_3_15_1 = "chart_3_15_1";
	/**
	 * 3.15	网流vs时间  3.15.2	再生制动
	 */
	public static String  chart_3_15_2 = "chart_3_15_2";
	/**
	 * 2015.4.22增加  总能耗vs距离
	 */
	public static String  chart_3_16 = "chart_3_16";
	
	/**
	 * 定义各曲线对应的数组
	 */
	private double [][] chart_1_10_array;
	private double [][] chart_1_11_1_array;
	private double [][] chart_1_11_2_array;
	private double [][] chart_2_1_2_array;
	private double [][] chart_2_2_2_array;
	private double [][] chart_3_1_array;
	private double [][] chart_3_2_array;
	private double [][] chart_3_3_array;
	private double [][] chart_3_4_array;
	private double [][] chart_3_5_array;
	private double [][] chart_3_6_array;
	private double [][] chart_3_7_array;
	private double [][] chart_3_8_array;
	private double [][] chart_3_9_array;
	private double [][] chart_3_10_array;
	private double [][] chart_3_11_array;
	private double [][] chart_3_12_array;
	private double [][] chart_3_13_array;
	private double [][] chart_3_14_1_array;
	private double [][] chart_3_14_2_array;
	private double [][] chart_3_15_1_array;
	private double [][] chart_3_15_2_array;
	private double [][] chart_3_16_array;
	
	
	private String routeName;
	private String trainNum;
	private int trainCategoryId;
	private String trainCategory;
	private ArrayList<Rundata> rundataList;
	
	public CreateChartService(String routeName, String trainNum, int trainCategoryId, String trainCategory, ArrayList<Rundata> rundataList) {
		this.routeName = routeName;
		this.trainNum = trainNum;
		this.trainCategoryId = trainCategoryId;
		this.trainCategory = trainCategory;
		this.rundataList = calTotalPower(rundataList);
	}

	/**
	 * 获取数据，并生成所有的图表，并保存
	 */
	public void createCharts(){
		createData();//生成图表数据
		createAndSaveSingleLineChart(chart_1_10_array, "牵引特性曲线", "牵引力", "速度(km/h)", "牵引力(KN)", chart_1_10);
		createAndSaveDoubleLineChart(chart_1_11_1_array, "制动特性曲线", "8级制动", "紧急制动", "速度(km/h)", "制动力(KN)", chart_1_11_1);
		createAndSaveSingleLineChart(chart_1_11_2_array, "电制动特性曲线", "电制动力", "速度(km/h)", "电制动力(KN)", chart_1_11_2);
		createAndSaveSingleLineChart(chart_2_1_2_array, "启动性能曲线", "位移-速度", "速度(km/h)", "位移(km)", chart_2_1_2);
		createAndSaveSingleLineChart(chart_2_2_2_array, "制动性能曲线", "速度-位移", "位移(km)", "速度(km/h)", chart_2_2_2);
		createAndSaveSingleLineChart(chart_3_1_array, "线路坡度-距离", "坡度", "距离(km)", "标高(m)", chart_3_1);
		createAndSaveSingleLineChart(chart_3_2_array, "线路限速-距离", "限速", "距离(km)", "限速(km/h)", chart_3_2);
		createAndSaveSingleLineChart(chart_3_3_array, "速度-时间", "速度", "时间(s)", "速度(km/h)", chart_3_3);
		createAndSaveSingleLineChart(chart_3_4_array, "速度-距离", "速度", "距离(km)", "速度(km/h)", chart_3_4);
		createAndSaveSingleLineChart(chart_3_5_array, "加速度-时间", "加速度", "时间(s)", "加速度(m/s^2)", chart_3_5);
		createAndSaveSingleLineChart(chart_3_6_array, "加速度-距离", "加速度", "距离(km)", "加速度(m/s^2)", chart_3_6);
		createAndSaveDoubleLineChart(chart_3_7_array, "牵引力、制动力-距离", "牵引力", "制动力", "距离(km)", "KN", chart_3_7);
		createAndSaveDoubleLineChart(chart_3_8_array, "牵引力、制动力-时间", "牵引力", "制动力", "时间(s)", "KN", chart_3_8);
		createAndSaveSingleLineChart(chart_3_9_array, "牵引能耗-距离", "牵引能耗", "距离(km)", "牵引能耗(KJ)", chart_3_9);
		createAndSaveSingleLineChart(chart_3_10_array, "牵引能耗-时间", "牵引能耗", "时间(s)", "牵引能耗(KJ)", chart_3_10);
		createAndSaveSingleLineChart(chart_3_11_array, "再生电制动能量-距离", "再生电制动能量", "距离(km)", "再生电制动能量(KJ)", chart_3_11);
		createAndSaveSingleLineChart(chart_3_12_array, "再生电制动能量-时间", "再生电制动能量", "时间(s)", "再生电制动能量(KJ)", chart_3_12);
		createAndSaveSingleLineChart(chart_3_13_array, "时间-距离", "距离", "时间(s)", "距离(km)", chart_3_13);
		createAndSaveSingleLineChart(chart_3_14_1_array, "网流-距离", "网流", "距离(km)", "网流(A)", chart_3_14_1);
//		createAndSaveSingleLineChart(chart_3_14_2_array, "网流-距离（再生制动）", "网流", "距离(km)", "网流(A)", chart_3_14_2);
		createAndSaveSingleLineChart(chart_3_15_1_array, "网流-时间", "网流", "时间(s)", "网流(A)", chart_3_15_1);
//		createAndSaveSingleLineChart(chart_3_15_2_array, "网流-时间（再生制动）", "网流", "时间(s)", "网流(A)", chart_3_15_2);
		createAndSaveSingleLineChart(chart_3_16_array, "总能耗-距离", "总能耗", "距离(km)", "总能耗(KJ)", chart_3_16);
	}
	
	/**
	 * 填充各个图表对应的array
	 */
	public void createData(){
		initFormula();
		
		//1.10	列车牵引特性曲线
		if (TractionForceMinTime.tractionType == 0) {
			ArrayList<CurveFormulaBean> chartDataList = TractionConfPanel.getTractionData(1, trainCategoryId);
			int chartDataSize = chartDataList.size();
			chart_1_10_array = new double[2][chartDataSize];
			for (int i = 0; i < chartDataSize; i++) {
				CurveFormulaBean bean = chartDataList.get(i);
				chart_1_10_array[0][i] = bean.getV();
				chart_1_10_array[1][i] = bean.getTractionForce();
			}
		}else if(TractionForceMinTime.tractionType == 1){
			ArrayList<TractionLevelFormulaBean> chartDataList = TractionLevelConfPanel.getTractionLevelData(TractionForceMinTime.level, trainCategoryId);
			int chartDataSize = chartDataList.size();
			chart_1_10_array = new double[2][chartDataSize];
			for (int i = 0; i < chartDataSize; i++) {
				TractionLevelFormulaBean bean = chartDataList.get(i);
				chart_1_10_array[0][i] = bean.getV();
				chart_1_10_array[1][i] = bean.getT1();
			}
		}
		
		//1.11.1	紧急制动、8级制动
		double parameters [] = TractionConfPanelService.getSomeParametersFromTrainEdit(trainCategoryId);
		double M = (parameters[4]) * 1000;//t转成kg
		double maxV = parameters[5]; //MaxV
		ArrayList<TrainBrakeFactor> brakeFactorList = BrakeConfPanelService.getBrakeFactor(trainCategoryId);
		int brakeFactorSize = brakeFactorList.size();
		//1.11.2	电制动
		BrakeConfPanel.electricBrakeCreater(trainCategoryId);
		ArrayList<Double> electricBrakeList = BrakeConfPanel.electricBrakeList;
		chart_1_11_1_array = new double[3][brakeFactorSize];
		chart_1_11_2_array = new double[2][brakeFactorSize];
		for(int i=0;i<brakeFactorSize;i++){
			TrainBrakeFactor bean = brakeFactorList.get(i);
			chart_1_11_1_array[0][i] = bean.getSpeed();
			chart_1_11_1_array[1][i] = (bean.get_8() * M)/1000.0;
			chart_1_11_1_array[2][i] = (bean.getEb() * M)/1000.0;
			chart_1_11_2_array[0][i] = bean.getSpeed();
			chart_1_11_2_array[1][i] = electricBrakeList.get(i);
		}
		
		//2.1.2	启动性能曲线
		ArrayList<CurveFormulaBean> launchDataList = TractionConfPanel.getPerformanceCalculator();
		int launchDataSize = launchDataList.size();
		chart_2_1_2_array = new double[2][launchDataSize];
		for(int i=0;i<launchDataSize;i++){
			CurveFormulaBean bean = launchDataList.get(i);
			chart_2_1_2_array[0][i] = bean.getVt();
			chart_2_1_2_array[1][i] = bean.getSt();
		}
		
		//2.2.2	制动性能设计（8级制动）
		ArrayList<CurveFormulaBean> brakePerformanceDataList = BrakeConfPanel.getBrakePerformanceData(9, trainCategoryId, maxV);
		int brakePerformanceDataSize = brakePerformanceDataList.size();
		chart_2_2_2_array = new double[2][brakePerformanceDataSize];
		for(int i=0;i<brakePerformanceDataSize;i++){
			CurveFormulaBean bean = brakePerformanceDataList.get(i);
			chart_2_2_2_array[0][i] = bean.getSt();
			chart_2_2_2_array[1][i] = bean.getVt();
		}
		
		//3.1	线路坡度vs距离
		DataService dataService = new DataService();
		int routeId = RouteDataManagementDialogService.getRouteIdByName(routeName);
		ArrayList<Slope> slopeList = dataService.getSlopeData(routeId);
		int slopeSize = slopeList.size();
		chart_3_1_array = new double[2][slopeSize + 1];//多一个起始坐标(0, 0)
		chart_3_1_array[0][0] = 0;
		chart_3_1_array[1][0] = 0;
		for(int i=0;i<slopeSize;i++){
			Slope bean = slopeList.get(i);
			chart_3_1_array[0][i+1] = bean.getEnd();
			chart_3_1_array[1][i+1] = bean.getHeight();
		}
		
		//3.2	线路限速vs距离
		ArrayList<Curve> curveList = dataService.getCurveData(routeId);
		int curveSize = curveList.size();
		chart_3_2_array = new double[2][2*curveSize + 1];////多一个起始坐标(0, 0)
		chart_3_2_array[0][0] = 0;
		chart_3_2_array[1][0] = 0;
		int index = 1;
		for(Curve bean : curveList){
			chart_3_2_array[0][index] = bean.getStart();
			chart_3_2_array[1][index] = bean.getSpeedLimit();
			++index;
			chart_3_2_array[0][index] = bean.getStart() + bean.getLength()/1000.0;
			chart_3_2_array[1][index] = bean.getSpeedLimit();
			++index;
		}
		
		//运行数据
		int rundataSize = rundataList.size();
		chart_3_3_array = new double[2][rundataSize];
		chart_3_4_array = new double[2][rundataSize];
		chart_3_5_array = new double[2][rundataSize];
		chart_3_6_array = new double[2][rundataSize];
		chart_3_7_array = new double[3][rundataSize];
		chart_3_8_array = new double[3][rundataSize];
		chart_3_9_array = new double[2][rundataSize];
		chart_3_10_array = new double[2][rundataSize];
		chart_3_11_array = new double[2][rundataSize];
		chart_3_12_array = new double[2][rundataSize];
		chart_3_13_array = new double[2][rundataSize];
		chart_3_14_1_array = new double[2][rundataSize];
		chart_3_14_2_array = new double[2][rundataSize];
		chart_3_15_1_array = new double[2][rundataSize];
		chart_3_15_2_array = new double[2][rundataSize];
		chart_3_16_array = new double[2][rundataSize];
		//从rundataList中取出运行数据填充数组
		for(int i=0;i<rundataSize;i++){
			Rundata bean = rundataList.get(i);
			//3.3 速度vs时间
			chart_3_3_array[0][i] = bean.getRuntime();
			chart_3_3_array[1][i] = bean.getSpeed();
			//3.4 速度vs距离
			chart_3_4_array[0][i] = bean.getLocation();
			chart_3_4_array[1][i] = bean.getSpeed();
			//3.5 加速度vs时间
			chart_3_5_array[0][i] = bean.getRuntime();
			chart_3_5_array[1][i] = bean.getAcceleration();
			//3.6 加速度vs距离
			chart_3_6_array[0][i] = bean.getLocation();
			chart_3_6_array[1][i] = bean.getAcceleration();
			//3.7 牵引力、制动力vs距离
			chart_3_7_array[0][i] = bean.getLocation();
			chart_3_7_array[1][i] = bean.getTractionForce();
			chart_3_7_array[2][i] = bean.getBrakeForce();
			//3.8 牵引力、制动力vs时间
			chart_3_8_array[0][i] = bean.getRuntime();
			chart_3_8_array[1][i] = bean.getTractionForce();
			chart_3_8_array[2][i] = bean.getBrakeForce();
			//3.9 牵引能耗vs距离
			chart_3_9_array[0][i] = bean.getLocation();
			chart_3_9_array[1][i] = bean.getTractionPower();
			//3.10 牵引能耗vs时间
			chart_3_10_array[0][i] = bean.getRuntime();
			chart_3_10_array[1][i] = bean.getTractionPower();
			//3.11 再生电制动能量vs距离
			chart_3_11_array[0][i] = bean.getLocation();
			chart_3_11_array[1][i] = bean.getElecBrakeForcePower();
			//3.12 再生电制动能量vs时间
			chart_3_12_array[0][i] = bean.getRuntime();
			chart_3_12_array[1][i] = bean.getElecBrakeForcePower();
			//3.13 时间vs距离
			chart_3_13_array[0][i] = bean.getRuntime();
			chart_3_13_array[1][i] = bean.getLocation();
			//3.14 网流vs距离 3.14.1	牵引
			chart_3_14_1_array[0][i] = bean.getLocation();
			chart_3_14_1_array[1][i] = bean.getTractionGridCurrent();
			//3.14网流vs距离 3.14.2	再生制动
			chart_3_14_2_array[0][i] = bean.getLocation();
			chart_3_14_2_array[1][i] = bean.getComBrakeGridCurrent();
			//3.15 网流vs时间 3.15.1	牵引
			chart_3_15_1_array[0][i] = bean.getRuntime();
			chart_3_15_1_array[1][i] = bean.getTractionGridCurrent();
			//3.15 网流vs时间 3.15.2	再生制动
			chart_3_15_2_array[0][i] = bean.getRuntime();
			chart_3_15_2_array[1][i] = bean.getComBrakeGridCurrent();
			//3.16 总能耗vs距离
			chart_3_16_array[0][i] = bean.getLocation();
			chart_3_16_array[1][i] = bean.getTotalPower();

		}
		
	}
	
	/**
	 * 初始化计算公式
	 */
	public void initFormula(){
		//2.给基本空气阻力赋值
		double [] parameters = TractionConfPanelService.getSomeParametersFromTrainEdit(trainCategoryId);
		TractionFormulas.a = parameters[0];
		TractionFormulas.b = parameters[1];
		TractionFormulas.c = parameters[2];
		TractionFormulas.dv = parameters[3];//逆风速
		TractionFormulas.m = parameters[4];//质量
		TractionFormulas.maxV = parameters[5];//最高运行速度
	}
	
	
	/**
	 * 只保存曲线图，不显示
	 * @param xyData 二维数组[2][n] xyData[0]表示x轴数据 xyData[1]表示y轴数据
	 * @param chartName 表名，例如“时间-里程”
	 * @param xLabel x轴名，例如“时间(s)”
	 * @param yLabel y轴名，例如“位移(km)”
	 * @param picName 图片保存名称
	 */
	public void createAndSaveSingleLineChart(double[][] xyData, String chartName, String lineName, String xLabel, String yLabel, String picName){
		XYDataset dataset = ChartFactoryPanel.createSingleDataset(xyData, lineName);
		JFreeChart chart = ChartFactoryPanel.createChart(dataset, chartName, xLabel, yLabel);
		saveChartAsJPEG(PATH, picName, chart);
	}
	
	/**
	 * 创建含有两条曲线的图表
	 * @param xyData 二维数组[3][n] xyData[0]表示x轴数据 xyData[1]表示y轴数据 xyData[2]表示y轴数据
	 * @param chartName 表名，例如“时间-里程”
	 * @param xLabel x轴名，例如“时间(s)”
	 * @param yLabel y轴名，例如“位移(km)”
	 * @param picName 图片保存名称
	 */
	public void createAndSaveDoubleLineChart(double[][] xyData, String chartName, String lineName1, String lineName2, String xLabel, String yLabel, String picName) {
		XYDataset dataset = ChartFactoryPanel.createDoubleDataset(xyData, lineName1, lineName2);
		JFreeChart chart = ChartFactoryPanel.createChart(dataset, chartName, xLabel, yLabel);
		saveChartAsJPEG(PATH, picName, chart);
	}
	
	/**
	 * 将图表保存成图片
	 * @param path 保存图片的路径
	 * @param picName 图片文件名，如SpeedTime
	 * @param chart 要保存的图表
	 */
	public void saveChartAsJPEG(String path, String picName, JFreeChart chart){
		FileOutputStream fosJpg = null;
		File picFile = new File(path);
		if(!picFile.exists()){
			picFile.mkdir();
		}
		try {
			fosJpg = new FileOutputStream(picFile.getAbsolutePath()+"//"+picName+".jpg");
			ChartUtilities.writeChartAsJPEG(fosJpg, 1.0f, chart, 800, 600);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				fosJpg.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		
	}
	
	/**
	 * 2015.4.24修改，增加总能量
	 */
	public ArrayList<Rundata> calTotalPower(ArrayList<Rundata> rundataList){
		double maxTractionPower = getMaxTractionPower(rundataList);
		for(int i=0;i<rundataList.size();i++){
			Rundata bean = rundataList.get(i);
			if(bean.getTractionPower() != 0){
				bean.setTotalPower(bean.getTractionPower() - bean.getElecBrakeForcePower());
			}else{
				if(i != 0){
					//最大牵引能量减去电制动能量
					bean.setTotalPower(maxTractionPower - bean.getElecBrakeForcePower());
				}
			}
		}
		return rundataList;
	}
	
	/**
	 * 寻找最大的牵引能量
	 * @param rundataList
	 * @return
	 */
	private double getMaxTractionPower(ArrayList<Rundata> rundataList){
		double temp = 0;
		for(int i=0;i<rundataList.size();i++){
			double power = rundataList.get(i).getTractionPower();
			if(power > temp){
				temp = power;
			}
		}
		return temp;
	}
	
}
