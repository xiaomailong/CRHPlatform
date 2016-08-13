package com.crh2.chart;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.crh2.javabean.Rundata;

/**
 * 负责绘制牵引曲线图
 * @author huhui
 *
 */
public class TractiveCharacteristicCurveChart extends JPanel {

	public TractiveCharacteristicCurveChart(int trainTypeId) {
		XYSeriesCollection dataset = createDataset(trainTypeId);
		JFreeChart chart = createChart(dataset);
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 400));
		add(chartPanel);
	}

	/**
	 * 设置数据集
	 * @param trainTypeId
	 * @return
	 */
	private XYSeriesCollection createDataset(int trainTypeId) {
		XYSeriesCollection result = new XYSeriesCollection();
		try {
			XYSeries s3 = new XYSeries("", true, false);
			//给s3赋值
			this.getCalculateResultByTrainTypeId(s3, trainTypeId);
			result.addSeries(s3);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;

	}
	
	/**
	 * 根据trainTypeId选择不同的牵引力公式，并计算
	 * @param sySeries
	 * @param trainTypeId
	 */
	public void getCalculateResultByTrainTypeId(XYSeries sySeries,int trainTypeId){
		int M = 0;
		if(trainTypeId == 1){//4动4拖
			M = 4;
		}else if(trainTypeId == 2){//1动7拖
			M = 1;
		}else if(trainTypeId == 3){//2动6拖
			M = 2;
		}else if(trainTypeId == 4){//3动5拖
			M = 3;
		}else if(trainTypeId == 5){//5动3拖
			M = 5;
		}else if(trainTypeId == 6){//6动2拖
			M = 6;
		}else if(trainTypeId == 7){//7动1拖
			M = 7;
		}else if(trainTypeId == 8){//8动0拖
			M = 8;
		}
		
		if(trainTypeId != 9){
			//根据M值的不同，计算各种类型的动车组的牵引力
			double Fst = (65.35854 * M)/0.875;
			double F1 = 66.49874 * M;
			double F = 0;
			for(int v=0;v<400;v++){
				if(v >= 0 && v < 119.1){
					F = Fst - ((Fst - F1)/119.1) * v;
				}else if(v >=119.1 && v <= 310){
					F = (7920 * M) / v;
				}else if(v > 310){
					F = (7920 * M * 310) / (v * v);
				}
				sySeries.add(v,F);
			}
		}else{// 250km/h车
			double F = 0;
			for(int v=0;v<=250;v++){
				if(v >= 0 && v < 67.4){
					F = 290 - 0.178 * v;
				}else if(v >=67.4 && v <= 250){
					F = (3.6 * 5200)/v;
				}
				sySeries.add(v,F);
			}
		}
		
}

	private static JFreeChart createChart(XYDataset dataset) {
		JFreeChart chart = ChartFactory.createXYLineChart(null,"Speed (km/h)", "Traction (KN)", dataset, PlotOrientation.VERTICAL, true,true, false);
		TextTitle t1 = new TextTitle("牵引力特性曲线", new Font("SansSerif", Font.BOLD, 18));
		chart.addSubtitle(t1);
		XYPlot plot = chart.getXYPlot();
		//设置刻度字体
		Font tickFont = new Font("宋体",Font.BOLD,17);
		plot.getDomainAxis().setTickLabelFont(tickFont);
		plot.getRangeAxis().setTickLabelFont(tickFont);
		//设置虚线颜色
		plot.setDomainGridlinePaint(Color.RED);
		plot.setRangeGridlinePaint(Color.RED);
		XYLineAndShapeRenderer xylinerenderer=(XYLineAndShapeRenderer)plot.getRenderer();
		xylinerenderer.setSeriesPaint(0, new Color(0,0,0));//设置曲线颜色
		xylinerenderer.setSeriesStroke(0, new BasicStroke(1.8F));//设置曲线粗细
		NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
		domainAxis.setUpperMargin(0.12);
		domainAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setAutoRangeIncludesZero(false);
		plot.setBackgroundPaint(Color.WHITE);
		return chart;
	}

}
