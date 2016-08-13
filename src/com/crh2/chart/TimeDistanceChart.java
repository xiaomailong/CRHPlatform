package com.crh2.chart;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
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
 * �������ʱ��-�������ͼ
 * @author huhui
 *
 */
public class TimeDistanceChart extends JPanel {

	public TimeDistanceChart(ArrayList<Rundata> tsDataList) {
		XYSeriesCollection dataset = createDataset(tsDataList);
		JFreeChart chart = createChart(dataset);
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 400));
		add(chartPanel);
	}

	/**
	 * �������ݼ�
	 * @param tsDataList
	 * @return
	 */
	private XYSeriesCollection createDataset(ArrayList<Rundata> tsDataList) {
		XYSeriesCollection result = new XYSeriesCollection();
		try {
			XYSeries s3 = new XYSeries("", true, false);
			//��s3��ֵ
			for(Rundata rundata : tsDataList){
				s3.add(rundata.getDistance(),rundata.getRuntime());
			}
			result.addSeries(s3);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;

	}

	private JFreeChart createChart(XYDataset dataset) {
		JFreeChart chart = ChartFactory.createXYLineChart(null,
				"Distance (km)", "Energy (kw*h)", dataset, PlotOrientation.VERTICAL, true,
				true, false);
		TextTitle t1 = new TextTitle("E-Sͼ", new Font(
				"SansSerif", Font.BOLD, 18));
		TextTitle t2 = new TextTitle("����-�������ͼ",new Font("SansSerif", Font.BOLD, 14));
		chart.addSubtitle(t1);
		chart.addSubtitle(t2);
		XYPlot plot = chart.getXYPlot();
		//���ÿ̶�����
		Font tickFont = new Font("����",Font.BOLD,17);
		plot.getDomainAxis().setTickLabelFont(tickFont);
		plot.getRangeAxis().setTickLabelFont(tickFont);
		//����������ɫ
		plot.setDomainGridlinePaint(Color.RED);
		plot.setRangeGridlinePaint(Color.RED);
		XYLineAndShapeRenderer xylinerenderer=(XYLineAndShapeRenderer)plot.getRenderer();
		xylinerenderer.setSeriesPaint(0, new Color(0, 0, 0));//����������ɫ
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
