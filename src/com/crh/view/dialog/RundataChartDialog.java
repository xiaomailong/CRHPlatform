package com.crh.view.dialog;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JPanel;

import com.crh.view.panel.ChartFactoryPanel;
import com.crh2.javabean.Rundata;

/**
 * ��ʾ�����Ժ��������ݵĶԻ���
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
	 * Array[3][n]  һ����ʱ�䡢ǣ�������������ƶ�������
	 */
	private double [][] timeTractionElecPowerArray;

	public RundataChartDialog(String title, ArrayList<Rundata> dataList) {
		
		this.dataList = dataList;
		initChartData();
		getContentPane().setLayout(new GridLayout(2, 2, 0, 0));
		
		JPanel timeDisChartPanel = ChartFactoryPanel.createSingleLineChartPanel(timeDistanceArray, "λ��-ʱ��", "s-t", "ʱ��(s)", "λ��(km)");
		JPanel timeSpeedChartPanel = ChartFactoryPanel.createSingleLineChartPanel(timeSpeedArray, "�ٶ�-ʱ��", "v-t", "ʱ��(s)", "�ٶ�(km/h)");
		JPanel distanceSpeedChartPanel = ChartFactoryPanel.createSingleLineChartPanel(distanceSpeedArray, "λ��-�ٶ�", "s-t", "λ��(km)", "�ٶ�(km/h)");
		JPanel timeTractionElecPowerPanel = ChartFactoryPanel.createDoubleLineChartPanel(timeTractionElecPowerArray, "ǣ��������-��������", "ǣ��������", "��������", "ʱ��(s)", "����(KJ)");
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
	 * ��ʼ��ͼ������
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
			timeDistanceArray[0][i] = bean.getRuntime();//·��-ʱ��
			timeDistanceArray[1][i] = bean.getLocation();
			
			timeSpeedArray[0][i] = bean.getRuntime();//�ٶ�-ʱ��
			timeSpeedArray[1][i] = bean.getSpeed();
			
			distanceSpeedArray[0][i] = bean.getLocation();//�ٶ�-·��
			distanceSpeedArray[1][i] = bean.getSpeed();
			
			//ǣ��������-ʱ��
			timeTractionElecPowerArray[0][i] = bean.getRuntime();//ʱ��
			timeTractionElecPowerArray[1][i] = bean.getTractionPower();
			timeTractionElecPowerArray[2][i] = bean.getElecBrakeForcePower();
		}
	}

}
