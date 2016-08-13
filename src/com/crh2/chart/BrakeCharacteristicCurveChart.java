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
 * ��������ƶ���������ͼ
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
	 * �������ݼ�
	 * @param trainTypeId
	 * @return
	 */
	private XYSeriesCollection createDataset(int trainTypeId) {
		XYSeriesCollection result = new XYSeriesCollection();
		try {
			XYSeries s3 = new XYSeries("", true, false);
			//��s3��ֵ
			this.getCalculateResult(s3,trainTypeId);
			result.addSeries(s3);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;

	}
	
	/**
	 * ���㲢��ֵ
	 * @param sySeries
	 * @param trainTypeId
	 */
	public void getCalculateResult(XYSeries sySeries,int trainTypeId){
		int M = 0;
		if(trainTypeId == 1){//4��4��
			M = 4;
		}else if(trainTypeId == 2){//1��7��
			M = 1;
		}else if(trainTypeId == 3){//2��6��
			M = 2;
		}else if(trainTypeId == 4){//3��5��
			M = 3;
		}else if(trainTypeId == 5){//5��3��
			M = 5;
		}else if(trainTypeId == 6){//6��2��
			M = 6;
		}else if(trainTypeId == 7){//7��1��
			M = 7;
		}else if(trainTypeId == 8){//8��0��
			M = 8;
		}
		
		if(trainTypeId != 9){
			double f = 0;// �ƶ���f
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
		}else{// 250km/h��
			double f = 0;// �ƶ���f
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
		TextTitle t1 = new TextTitle("�ƶ�����������", new Font("SansSerif", Font.BOLD, 18));
		chart.addSubtitle(t1);
		XYPlot plot = chart.getXYPlot();
		//���ÿ̶�����
		Font tickFont = new Font("����",Font.BOLD,17);
		plot.getDomainAxis().setTickLabelFont(tickFont);
		plot.getRangeAxis().setTickLabelFont(tickFont);
		//����������ɫ
		plot.setDomainGridlinePaint(Color.RED);
		plot.setRangeGridlinePaint(Color.RED);
		XYLineAndShapeRenderer xylinerenderer=(XYLineAndShapeRenderer)plot.getRenderer();
		xylinerenderer.setSeriesPaint(0, new Color(0,0,0));//����������ɫ
		xylinerenderer.setSeriesStroke(0, new BasicStroke(1.8F));//�������ߴ�ϸ
		NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
		domainAxis.setUpperMargin(0.12);
		domainAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setAutoRangeIncludesZero(false);
		plot.setBackgroundPaint(Color.WHITE);
		return chart;
	}

}
