package com.crh.test;

import java.awt.EventQueue;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.JFreeChart;

import com.crh.service.TractionConfPanelService;
import com.crh.view.panel.ChartFactoryPanel;
import com.crh2.calculate.TractionForce;
import com.crh2.javabean.TrainTractionConf;

public class ChartTest extends JFrame {
	
	private static JFreeChart chart = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TrainTractionConf trainTractionConf = TractionConfPanelService.getTrainTractionConf(1, 57);
					TractionForce.Fst = trainTractionConf.getFst();
					TractionForce.F1 = trainTractionConf.getF1(); 
					TractionForce.F2 = trainTractionConf.getF2();
					TractionForce.v1 = trainTractionConf.getV1();
					TractionForce.v2 = trainTractionConf.getV2();
					TractionForce.P1 = trainTractionConf.getP1();
					
					double [][] chart_1_10_array = new double[2][350];
					
					for(int v=0;v<350;v+=5){
						chart_1_10_array[0][v] = v;
						chart_1_10_array[1][v] = TractionForce.getTractionForce(v/3.6)/1000;
//						chart_1_10_array[1][v] = v;
						System.out.println(chart_1_10_array[1][v]);
					}
					JPanel panel = ChartFactoryPanel.createSingleLineChartPanel(chart_1_10_array, "牵引特性", "牵引力", "速度(km/h)", "牵引力");
					ChartTest dialog = new ChartTest();
					dialog.add(panel);
					dialog.setSize(800, 600);
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
