package com.crh.view.dialog;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JPanel;

import com.crh.view.panel.ChartFactoryPanel;
import com.crh2.javabean.Rundata;

/**
 * 显示仿真以后运行数据的对话框
 * @author huhui
 *
 */
public class RundataChartDialog extends JDialog {
	
	private ArrayList<Rundata> dataList = null;
	/**
	 * Array[2][n]
	 */
	private double [][] timeDistanceArray, timeSpeedArray, distanceSpeedArray;
	/**
	 * Array[3][n]  一次是时间、牵引力能量、电制动力能量
	 */
	private double [][] timeTractionElecPowerArray;

	public RundataChartDialog(String title, ArrayList<Rundata> dataList) {
		
		this.dataList = dataList;
		initChartData();
		getContentPane().setLayout(new GridLayout(2, 2, 0, 0));
		
		JPanel timeDisChartPanel = ChartFactoryPanel.createSingleLineChartPanel(timeDistanceArray, "位移-时间", "s-t", "时间(s)", "位移(km)");
		JPanel timeSpeedChartPanel = ChartFactoryPanel.createSingleLineChartPanel(timeSpeedArray, "速度-时间", "v-t", "时间(s)", "速度(km/h)");
		JPanel distanceSpeedChartPanel = ChartFactoryPanel.createSingleLineChartPanel(distanceSpeedArray, "位移-速度", "s-t", "位移(km)", "速度(km/h)");
		JPanel timeTractionElecPowerPanel = ChartFactoryPanel.createDoubleLineChartPanel(timeTractionElecPowerArray, "牵引力做功-再生能量", "牵引力做功", "再生能量", "时间(s)", "能量(KJ)");
		add(timeDisChartPanel);
		add(timeSpeedChartPanel);
		add(distanceSpeedChartPanel);
		add(timeTractionElecPowerPanel);
		
		Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		int screenHeight = (int) screenSize.getHeight() - 40;
		int screenWidth = (int) screenSize.getWidth();
		setBounds(0, 0, screenWidth, screenHeight);
		
		setTitle(title);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setModal(true);
		setVisible(true);
	}
	
	/**
	 * 初始化图表数据
	 * @return
	 */
	public void initChartData(){
		int size = dataList.size();
		timeDistanceArray = new double[2][size];
		timeSpeedArray = new double[2][size];
		distanceSpeedArray = new double[2][size];
		timeTractionElecPowerArray = new double[3][size];
		for(int i=0;i<size;i++){
			Rundata bean = dataList.get(i);
			timeDistanceArray[0][i] = bean.getRuntime();//路程-时间
			timeDistanceArray[1][i] = bean.getLocation();
			
			timeSpeedArray[0][i] = bean.getRuntime();//速度-时间
			timeSpeedArray[1][i] = bean.getSpeed();
			
			distanceSpeedArray[0][i] = bean.getLocation();//速度-路程
			distanceSpeedArray[1][i] = bean.getSpeed();
			
			//牵引力能量-时间
			timeTractionElecPowerArray[0][i] = bean.getRuntime();//时间
			timeTractionElecPowerArray[1][i] = bean.getTractionPower();
			timeTractionElecPowerArray[2][i] = bean.getElecBrakeForcePower();
		}
	}

}
