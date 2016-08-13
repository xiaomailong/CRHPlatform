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
 * 2014.11.30 ������������ͼ��
 * @author huhui
 *
 */
public class CreateChartService {
	
	/**
	 * ͼƬ��ʱ����·��
	 */
	public static final String PATH = "C:\\CRHPlatformTemp";
	
	//������������
	/**
	 * 1.10	�г�ǣ����������
	 */
	public static String  chart_1_10 = "chart_1_10";
	/**
	 * 1.11.1	�����ƶ���8���ƶ�
	 */
	public static String  chart_1_11_1 = "chart_1_11_1";
	/**
	 * 1.11.2	���ƶ�
	 */
	public static String  chart_1_11_2 = "chart_1_11_2";
	/**
	 * 2.1.2	������������
	 */
	public static String  chart_2_1_2 = "chart_2_1_2";
	/**
	 * 2.2.2	�ƶ�������ƣ�8���ƶ���
	 */
	public static String  chart_2_2_2 = "chart_2_2_2";
	/**
	 * 3.1	��·�¶�vs����
	 */
	public static String  chart_3_1 = "chart_3_1";
	/**
	 * 3.2	��·����vs����
	 */
	public static String  chart_3_2 = "chart_3_2";
	/**
	 * 3.3	�ٶ�vsʱ��
	 */
	public static String  chart_3_3 = "chart_3_3";
	/**
	 * 3.4	�ٶ�vs����
	 */
	public static String  chart_3_4 = "chart_3_4";
	/**
	 * 3.5	���ٶ�vsʱ��
	 */
	public static String  chart_3_5 = "chart_3_5";
	/**
	 * 3.6	���ٶ�vs����
	 */
	public static String  chart_3_6 = "chart_3_6";
	/**
	 * 3.7	ǣ�������ƶ���vs����
	 */
	public static String  chart_3_7 = "chart_3_7";
	/**
	 * 3.8	ǣ�������ƶ���vsʱ��
	 */
	public static String  chart_3_8 = "chart_3_8";
	/**
	 * 3.9	ǣ���ܺ�vs����
	 */
	public static String  chart_3_9 = "chart_3_9";
	/**
	 * 3.10	ǣ���ܺ�vsʱ��
	 */
	public static String  chart_3_10 = "chart_3_10";
	/**
	 * 3.11	�������ƶ�����vs����
	 */
	public static String  chart_3_11 = "chart_3_11";
	/**
	 * 3.12	�������ƶ�����vsʱ��
	 */
	public static String  chart_3_12 = "chart_3_12";
	/**
	 * 3.13	ʱ��vs����
	 */
	public static String  chart_3_13 = "chart_3_13";
	/**
	 * 3.14	����vs���� 3.14.1	ǣ��
	 */
	public static String  chart_3_14_1 = "chart_3_14_1";
	/**
	 * 3.14	����vs���� 3.14.2	�����ƶ�
	 */
	public static String  chart_3_14_2 = "chart_3_14_2";
	/**
	 * 3.15	����vsʱ��  3.15.1	ǣ��
	 */
	public static String  chart_3_15_1 = "chart_3_15_1";
	/**
	 * 3.15	����vsʱ��  3.15.2	�����ƶ�
	 */
	public static String  chart_3_15_2 = "chart_3_15_2";
	/**
	 * 2015.4.22����  ���ܺ�vs����
	 */
	public static String  chart_3_16 = "chart_3_16";
	
	/**
	 * ��������߶�Ӧ������
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
	 * ��ȡ���ݣ����������е�ͼ��������
	 */
	public void createCharts(){
		createData();//����ͼ������
		createAndSaveSingleLineChart(chart_1_10_array, "ǣ����������", "ǣ����", "�ٶ�(km/h)", "ǣ����(KN)", chart_1_10);
		createAndSaveDoubleLineChart(chart_1_11_1_array, "�ƶ���������", "8���ƶ�", "�����ƶ�", "�ٶ�(km/h)", "�ƶ���(KN)", chart_1_11_1);
		createAndSaveSingleLineChart(chart_1_11_2_array, "���ƶ���������", "���ƶ���", "�ٶ�(km/h)", "���ƶ���(KN)", chart_1_11_2);
		createAndSaveSingleLineChart(chart_2_1_2_array, "������������", "λ��-�ٶ�", "�ٶ�(km/h)", "λ��(km)", chart_2_1_2);
		createAndSaveSingleLineChart(chart_2_2_2_array, "�ƶ���������", "�ٶ�-λ��", "λ��(km)", "�ٶ�(km/h)", chart_2_2_2);
		createAndSaveSingleLineChart(chart_3_1_array, "��·�¶�-����", "�¶�", "����(km)", "���(m)", chart_3_1);
		createAndSaveSingleLineChart(chart_3_2_array, "��·����-����", "����", "����(km)", "����(km/h)", chart_3_2);
		createAndSaveSingleLineChart(chart_3_3_array, "�ٶ�-ʱ��", "�ٶ�", "ʱ��(s)", "�ٶ�(km/h)", chart_3_3);
		createAndSaveSingleLineChart(chart_3_4_array, "�ٶ�-����", "�ٶ�", "����(km)", "�ٶ�(km/h)", chart_3_4);
		createAndSaveSingleLineChart(chart_3_5_array, "���ٶ�-ʱ��", "���ٶ�", "ʱ��(s)", "���ٶ�(m/s^2)", chart_3_5);
		createAndSaveSingleLineChart(chart_3_6_array, "���ٶ�-����", "���ٶ�", "����(km)", "���ٶ�(m/s^2)", chart_3_6);
		createAndSaveDoubleLineChart(chart_3_7_array, "ǣ�������ƶ���-����", "ǣ����", "�ƶ���", "����(km)", "KN", chart_3_7);
		createAndSaveDoubleLineChart(chart_3_8_array, "ǣ�������ƶ���-ʱ��", "ǣ����", "�ƶ���", "ʱ��(s)", "KN", chart_3_8);
		createAndSaveSingleLineChart(chart_3_9_array, "ǣ���ܺ�-����", "ǣ���ܺ�", "����(km)", "ǣ���ܺ�(KJ)", chart_3_9);
		createAndSaveSingleLineChart(chart_3_10_array, "ǣ���ܺ�-ʱ��", "ǣ���ܺ�", "ʱ��(s)", "ǣ���ܺ�(KJ)", chart_3_10);
		createAndSaveSingleLineChart(chart_3_11_array, "�������ƶ�����-����", "�������ƶ�����", "����(km)", "�������ƶ�����(KJ)", chart_3_11);
		createAndSaveSingleLineChart(chart_3_12_array, "�������ƶ�����-ʱ��", "�������ƶ�����", "ʱ��(s)", "�������ƶ�����(KJ)", chart_3_12);
		createAndSaveSingleLineChart(chart_3_13_array, "ʱ��-����", "����", "ʱ��(s)", "����(km)", chart_3_13);
		createAndSaveSingleLineChart(chart_3_14_1_array, "����-����", "����", "����(km)", "����(A)", chart_3_14_1);
//		createAndSaveSingleLineChart(chart_3_14_2_array, "����-���루�����ƶ���", "����", "����(km)", "����(A)", chart_3_14_2);
		createAndSaveSingleLineChart(chart_3_15_1_array, "����-ʱ��", "����", "ʱ��(s)", "����(A)", chart_3_15_1);
//		createAndSaveSingleLineChart(chart_3_15_2_array, "����-ʱ�䣨�����ƶ���", "����", "ʱ��(s)", "����(A)", chart_3_15_2);
		createAndSaveSingleLineChart(chart_3_16_array, "���ܺ�-����", "���ܺ�", "����(km)", "���ܺ�(KJ)", chart_3_16);
	}
	
	/**
	 * ������ͼ���Ӧ��array
	 */
	public void createData(){
		initFormula();
		
		//1.10	�г�ǣ����������
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
		
		//1.11.1	�����ƶ���8���ƶ�
		double parameters [] = TractionConfPanelService.getSomeParametersFromTrainEdit(trainCategoryId);
		double M = (parameters[4]) * 1000;//tת��kg
		double maxV = parameters[5]; //MaxV
		ArrayList<TrainBrakeFactor> brakeFactorList = BrakeConfPanelService.getBrakeFactor(trainCategoryId);
		int brakeFactorSize = brakeFactorList.size();
		//1.11.2	���ƶ�
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
		
		//2.1.2	������������
		ArrayList<CurveFormulaBean> launchDataList = TractionConfPanel.getPerformanceCalculator();
		int launchDataSize = launchDataList.size();
		chart_2_1_2_array = new double[2][launchDataSize];
		for(int i=0;i<launchDataSize;i++){
			CurveFormulaBean bean = launchDataList.get(i);
			chart_2_1_2_array[0][i] = bean.getVt();
			chart_2_1_2_array[1][i] = bean.getSt();
		}
		
		//2.2.2	�ƶ�������ƣ�8���ƶ���
		ArrayList<CurveFormulaBean> brakePerformanceDataList = BrakeConfPanel.getBrakePerformanceData(9, trainCategoryId, maxV);
		int brakePerformanceDataSize = brakePerformanceDataList.size();
		chart_2_2_2_array = new double[2][brakePerformanceDataSize];
		for(int i=0;i<brakePerformanceDataSize;i++){
			CurveFormulaBean bean = brakePerformanceDataList.get(i);
			chart_2_2_2_array[0][i] = bean.getSt();
			chart_2_2_2_array[1][i] = bean.getVt();
		}
		
		//3.1	��·�¶�vs����
		DataService dataService = new DataService();
		int routeId = RouteDataManagementDialogService.getRouteIdByName(routeName);
		ArrayList<Slope> slopeList = dataService.getSlopeData(routeId);
		int slopeSize = slopeList.size();
		chart_3_1_array = new double[2][slopeSize + 1];//��һ����ʼ����(0, 0)
		chart_3_1_array[0][0] = 0;
		chart_3_1_array[1][0] = 0;
		for(int i=0;i<slopeSize;i++){
			Slope bean = slopeList.get(i);
			chart_3_1_array[0][i+1] = bean.getEnd();
			chart_3_1_array[1][i+1] = bean.getHeight();
		}
		
		//3.2	��·����vs����
		ArrayList<Curve> curveList = dataService.getCurveData(routeId);
		int curveSize = curveList.size();
		chart_3_2_array = new double[2][2*curveSize + 1];////��һ����ʼ����(0, 0)
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
		
		//��������
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
		//��rundataList��ȡ�����������������
		for(int i=0;i<rundataSize;i++){
			Rundata bean = rundataList.get(i);
			//3.3 �ٶ�vsʱ��
			chart_3_3_array[0][i] = bean.getRuntime();
			chart_3_3_array[1][i] = bean.getSpeed();
			//3.4 �ٶ�vs����
			chart_3_4_array[0][i] = bean.getLocation();
			chart_3_4_array[1][i] = bean.getSpeed();
			//3.5 ���ٶ�vsʱ��
			chart_3_5_array[0][i] = bean.getRuntime();
			chart_3_5_array[1][i] = bean.getAcceleration();
			//3.6 ���ٶ�vs����
			chart_3_6_array[0][i] = bean.getLocation();
			chart_3_6_array[1][i] = bean.getAcceleration();
			//3.7 ǣ�������ƶ���vs����
			chart_3_7_array[0][i] = bean.getLocation();
			chart_3_7_array[1][i] = bean.getTractionForce();
			chart_3_7_array[2][i] = bean.getBrakeForce();
			//3.8 ǣ�������ƶ���vsʱ��
			chart_3_8_array[0][i] = bean.getRuntime();
			chart_3_8_array[1][i] = bean.getTractionForce();
			chart_3_8_array[2][i] = bean.getBrakeForce();
			//3.9 ǣ���ܺ�vs����
			chart_3_9_array[0][i] = bean.getLocation();
			chart_3_9_array[1][i] = bean.getTractionPower();
			//3.10 ǣ���ܺ�vsʱ��
			chart_3_10_array[0][i] = bean.getRuntime();
			chart_3_10_array[1][i] = bean.getTractionPower();
			//3.11 �������ƶ�����vs����
			chart_3_11_array[0][i] = bean.getLocation();
			chart_3_11_array[1][i] = bean.getElecBrakeForcePower();
			//3.12 �������ƶ�����vsʱ��
			chart_3_12_array[0][i] = bean.getRuntime();
			chart_3_12_array[1][i] = bean.getElecBrakeForcePower();
			//3.13 ʱ��vs����
			chart_3_13_array[0][i] = bean.getRuntime();
			chart_3_13_array[1][i] = bean.getLocation();
			//3.14 ����vs���� 3.14.1	ǣ��
			chart_3_14_1_array[0][i] = bean.getLocation();
			chart_3_14_1_array[1][i] = bean.getTractionGridCurrent();
			//3.14����vs���� 3.14.2	�����ƶ�
			chart_3_14_2_array[0][i] = bean.getLocation();
			chart_3_14_2_array[1][i] = bean.getComBrakeGridCurrent();
			//3.15 ����vsʱ�� 3.15.1	ǣ��
			chart_3_15_1_array[0][i] = bean.getRuntime();
			chart_3_15_1_array[1][i] = bean.getTractionGridCurrent();
			//3.15 ����vsʱ�� 3.15.2	�����ƶ�
			chart_3_15_2_array[0][i] = bean.getRuntime();
			chart_3_15_2_array[1][i] = bean.getComBrakeGridCurrent();
			//3.16 ���ܺ�vs����
			chart_3_16_array[0][i] = bean.getLocation();
			chart_3_16_array[1][i] = bean.getTotalPower();

		}
		
	}
	
	/**
	 * ��ʼ�����㹫ʽ
	 */
	public void initFormula(){
		//2.����������������ֵ
		double [] parameters = TractionConfPanelService.getSomeParametersFromTrainEdit(trainCategoryId);
		TractionFormulas.a = parameters[0];
		TractionFormulas.b = parameters[1];
		TractionFormulas.c = parameters[2];
		TractionFormulas.dv = parameters[3];//�����
		TractionFormulas.m = parameters[4];//����
		TractionFormulas.maxV = parameters[5];//��������ٶ�
	}
	
	
	/**
	 * ֻ��������ͼ������ʾ
	 * @param xyData ��ά����[2][n] xyData[0]��ʾx������ xyData[1]��ʾy������
	 * @param chartName ���������硰ʱ��-��̡�
	 * @param xLabel x���������硰ʱ��(s)��
	 * @param yLabel y���������硰λ��(km)��
	 * @param picName ͼƬ��������
	 */
	public void createAndSaveSingleLineChart(double[][] xyData, String chartName, String lineName, String xLabel, String yLabel, String picName){
		XYDataset dataset = ChartFactoryPanel.createSingleDataset(xyData, lineName);
		JFreeChart chart = ChartFactoryPanel.createChart(dataset, chartName, xLabel, yLabel);
		saveChartAsJPEG(PATH, picName, chart);
	}
	
	/**
	 * ���������������ߵ�ͼ��
	 * @param xyData ��ά����[3][n] xyData[0]��ʾx������ xyData[1]��ʾy������ xyData[2]��ʾy������
	 * @param chartName ���������硰ʱ��-��̡�
	 * @param xLabel x���������硰ʱ��(s)��
	 * @param yLabel y���������硰λ��(km)��
	 * @param picName ͼƬ��������
	 */
	public void createAndSaveDoubleLineChart(double[][] xyData, String chartName, String lineName1, String lineName2, String xLabel, String yLabel, String picName) {
		XYDataset dataset = ChartFactoryPanel.createDoubleDataset(xyData, lineName1, lineName2);
		JFreeChart chart = ChartFactoryPanel.createChart(dataset, chartName, xLabel, yLabel);
		saveChartAsJPEG(PATH, picName, chart);
	}
	
	/**
	 * ��ͼ�����ͼƬ
	 * @param path ����ͼƬ��·��
	 * @param picName ͼƬ�ļ�������SpeedTime
	 * @param chart Ҫ�����ͼ��
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
	 * 2015.4.24�޸ģ�����������
	 */
	public ArrayList<Rundata> calTotalPower(ArrayList<Rundata> rundataList){
		double maxTractionPower = getMaxTractionPower(rundataList);
		for(int i=0;i<rundataList.size();i++){
			Rundata bean = rundataList.get(i);
			if(bean.getTractionPower() != 0){
				bean.setTotalPower(bean.getTractionPower() - bean.getElecBrakeForcePower());
			}else{
				if(i != 0){
					//���ǣ��������ȥ���ƶ�����
					bean.setTotalPower(maxTractionPower - bean.getElecBrakeForcePower());
				}
			}
		}
		return rundataList;
	}
	
	/**
	 * Ѱ������ǣ������
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
