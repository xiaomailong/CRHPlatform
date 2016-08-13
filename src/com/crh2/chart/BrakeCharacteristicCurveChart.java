package com.crh2.chart;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;

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

import com.crh2.calculate.TrainAttribute;

/**
 * 负责绘制制动特性曲线图
 * @author huhui
 *
 */
public class BrakeCharacteristicCurveChart extends JPanel {

	public BrakeCharacteristicCurveChart(int trainTypeId) {
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
			this.getCalculateResult(s3,trainTypeId);
			result.addSeries(s3);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;

	}
	
	/**
	 * 计算并赋值
	 * @param sySeries
	 * @param trainTypeId
	 */
	public void getCalculateResult(XYSeries sySeries,int trainTypeId){
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
			double f = 0;// 制动力f
			double Fst = (65.35854 * M)/0.875;
			double F1 = 67.4789 * M;
			for(int v=0;v<=300;v++){
				if (v >= 0 && v <= 5) {
					f = (65.35854 * M)/0.875;
				} else if (v > 5 && v <= 106.7) {
					f = Fst - ((Fst - F1)/101.7)*(v - 5);
				} else if (v > 106.7) {
					f = (67.4789 * M * 106.7)/v;
				}
				sySeries.add(v,f);
			}
		}else{// 250km/h车
			double f = 0;// 制动力f
			for(int v=0;v<=250;v++){
				if (v >= 0 && v < 75) {
					f = 290;
				} else if (v >= 75 && v <= 250) {
					f = (3.6 * 6041.6)/v;
				}
				sySeries.add(v,f);
			}
		}
		
		
	}

	private static JFreeChart createChart(XYDataset dataset) {
		JFreeChart chart = ChartFactory.createXYLineChart(null,"Speed (km/h)", "Drag force (KN)", dataset, PlotOrientation.VERTICAL, true,true, false);
		TextTitle t1 = new TextTitle("制动力特性曲线", new Font("SansSerif", Font.BOLD, 18));
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
