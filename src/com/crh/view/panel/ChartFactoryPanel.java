package com.crh.view.panel;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;

import javax.swing.border.LineBorder;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * ������������ͼ��panel
 * @author huhui
 *
 */
public class ChartFactoryPanel {
	
	/**
	 * ����ֻ��һ�����ߵ�ͼ��
	 * 
	 * @param xyData ��ά����[2][n] xyData[0]��ʾx������ xyData[1]��ʾy������
	 * @param chartName ���������硰ʱ��-��̡�
	 * @param lineName ��������
	 * @param xLabel x���������硰ʱ��(s)��
	 * @param yLabel y���������硰λ��(km)��
	 * @return ChartPanel
	 */
	public static ChartPanel createSingleLineChartPanel(double[][] xyData, String chartName, String lineName, String xLabel, String yLabel) {
		XYDataset dataset = createSingleDataset(xyData, lineName);
		JFreeChart chart = createChart(dataset, chartName, xLabel, yLabel);
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		chartPanel.setMouseWheelEnabled(true);// ���ַŴ�
		return chartPanel;
	}

	/**
	 * ���������������ߵ�ͼ��
	 * 
	 * @param xyData ��ά����[3][n] xyData[0]��ʾx������ xyData[1]��ʾy������ xyData[2]��ʾy������
	 * @param chartName ���������硰ʱ��-��̡�
	 * @param xLabel x���������硰ʱ��(s)��
	 * @param yLabel y���������硰λ��(km)��
	 * @return ChartPanel
	 */
	public static ChartPanel createDoubleLineChartPanel(double[][] xyData, String chartName, String lineName1, String lineName2, String xLabel, String yLabel) {
		XYDataset dataset = createDoubleDataset(xyData, lineName1, lineName2);
		JFreeChart chart = createChart(dataset, chartName, xLabel, yLabel);
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		chartPanel.setMouseWheelEnabled(true);// ���ַŴ�
		return chartPanel;
	}

	/**
	 *  �������ݼ�
	 * @param xyData
	 * @param lineName
	 * @return
	 */
	public static XYDataset createSingleDataset(double[][] xyData,
			String lineName) {
		XYSeriesCollection xyseriescollection = new XYSeriesCollection();
		XYSeries xySeries = new XYSeries(lineName);
		int size = xyData[0].length;
		for (int i = 0; i < size; i++) {
			xySeries.add(xyData[0][i], xyData[1][i]);
		}
		xyseriescollection.addSeries(xySeries);
		return xyseriescollection;
	}

	/**
	 *  �������ݼ�
	 * @param xyData
	 * @param lineName1
	 * @param lineName2
	 * @return
	 */
	public static XYDataset createDoubleDataset(double[][] xyData,
			String lineName1, String lineName2) {
		XYSeriesCollection xyseriescollection = new XYSeriesCollection();
		XYSeries xySeries1 = new XYSeries(lineName1);
		XYSeries xySeries2 = new XYSeries(lineName2);
		int size = xyData[0].length;
		for (int i = 0; i < size; i++) {
			xySeries1.add(xyData[0][i], xyData[1][i]);
			xySeries2.add(xyData[0][i], xyData[2][i]);
		}
		xyseriescollection.addSeries(xySeries1);
		xyseriescollection.addSeries(xySeries2);
		return xyseriescollection;
	}
	
	public static JFreeChart createChart(XYDataset dataset, String chartName,
			String xLabel, String yLabel) {
		setChartTheme();
		JFreeChart jfreechart = ChartFactory.createXYLineChart(chartName, xLabel, yLabel, dataset, PlotOrientation.VERTICAL, true, true, false); 
		XYPlot plot = jfreechart.getXYPlot();
		plot.setDomainGridlinePaint(Color.BLACK);
		plot.setRangeGridlinePaint(Color.BLACK);
		plot.setBackgroundPaint(Color.WHITE);
		XYLineAndShapeRenderer xylinerenderer=(XYLineAndShapeRenderer)plot.getRenderer();
		xylinerenderer.setSeriesStroke(0, new BasicStroke(1.5F));//�������ߴ�ϸ
		xylinerenderer.setSeriesStroke(1, new BasicStroke(1.5F));
        return jfreechart;  
	}
	
	/**
	 * ��������ͼ�����ʽ
	 */
	public static void setChartTheme(){
		//����������ʽ  
        StandardChartTheme mChartTheme = new StandardChartTheme("CN");
        //���ñ�������  
        mChartTheme.setExtraLargeFont(new Font("����", Font.BOLD, 14));
        //������������ 
        mChartTheme.setLargeFont(new Font("����", Font.CENTER_BASELINE, 12));
        //����ͼ������  
        mChartTheme.setRegularFont(new Font("����", Font.CENTER_BASELINE, 12));
        //Ӧ��������ʽ  
        ChartFactory.setChartTheme(mChartTheme);
	}

	/*public static JFreeChart createChart(XYDataset dataset, String chartName,
			String xLabel, String yLabel) {
		NumberAxis xNum = new NumberAxis(xLabel);// X��
		xNum.setAutoRangeStickyZero(true);
		NumberAxis yNum = new NumberAxis(yLabel);// Y��
		yNum.setAutoRangeStickyZero(true);
		XYSplineRenderer xySplineRenderer = new XYSplineRenderer();
		xySplineRenderer.setSeriesStroke(0, new BasicStroke(1.8F));// �������ߴ�ϸ
		xySplineRenderer.setSeriesStroke(1, new BasicStroke(1.8F));// �������ߴ�ϸ
		xySplineRenderer.setBaseShapesVisible(false);// ���������ϵ�С��
		XYPlot xyPlot = new XYPlot(dataset, xNum, yNum, xySplineRenderer);
		xyPlot.setBackgroundPaint(Color.white);
		// ����������ɫ
		xyPlot.setDomainGridlinePaint(Color.black);
		xyPlot.setRangeGridlinePaint(Color.black);
		xyPlot.setAxisOffset(new RectangleInsets(4D, 4D, 4D, 4D));
		JFreeChart jFreeChart = new JFreeChart(chartName,
				JFreeChart.DEFAULT_TITLE_FONT, xyPlot, true);
		return jFreeChart;
	}*/

	/**
	 *  ����һ�ſձ�
	 * @param title
	 * @return
	 */
	public static JFreeChart createBlankChart(String title) {
		StandardChartTheme standardChartTheme = new StandardChartTheme("CN");
		standardChartTheme.setExtraLargeFont(new Font("����", Font.BOLD, 16));
		ChartFactory.setChartTheme(standardChartTheme);
		JFreeChart blankChart = ChartFactory.createXYLineChart(title, "", "",
				null, PlotOrientation.VERTICAL, true, true, false);
		XYPlot plot = blankChart.getXYPlot();
		plot.setBackgroundPaint(Color.WHITE);
		plot.setDomainGridlinePaint(Color.black);
		plot.setRangeGridlinePaint(Color.black);
		return blankChart;
	}
	
}
