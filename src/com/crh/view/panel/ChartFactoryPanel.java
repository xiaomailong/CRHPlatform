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
 * 负责生成曲线图的panel
 * @author huhui
 *
 */
public class ChartFactoryPanel {
	
	/**
	 * 创建只有一条曲线的图表
	 * 
	 * @param xyData 二维数组[2][n] xyData[0]表示x轴数据 xyData[1]表示y轴数据
	 * @param chartName 表名，例如“时间-里程”
	 * @param lineName 曲线名称
	 * @param xLabel x轴名，例如“时间(s)”
	 * @param yLabel y轴名，例如“位移(km)”
	 * @return ChartPanel
	 */
	public static ChartPanel createSingleLineChartPanel(double[][] xyData, String chartName, String lineName, String xLabel, String yLabel) {
		XYDataset dataset = createSingleDataset(xyData, lineName);
		JFreeChart chart = createChart(dataset, chartName, xLabel, yLabel);
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		chartPanel.setMouseWheelEnabled(true);// 滚轮放大
		return chartPanel;
	}

	/**
	 * 创建含有两条曲线的图表
	 * 
	 * @param xyData 二维数组[3][n] xyData[0]表示x轴数据 xyData[1]表示y轴数据 xyData[2]表示y轴数据
	 * @param chartName 表名，例如“时间-里程”
	 * @param xLabel x轴名，例如“时间(s)”
	 * @param yLabel y轴名，例如“位移(km)”
	 * @return ChartPanel
	 */
	public static ChartPanel createDoubleLineChartPanel(double[][] xyData, String chartName, String lineName1, String lineName2, String xLabel, String yLabel) {
		XYDataset dataset = createDoubleDataset(xyData, lineName1, lineName2);
		JFreeChart chart = createChart(dataset, chartName, xLabel, yLabel);
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		chartPanel.setMouseWheelEnabled(true);// 滚轮放大
		return chartPanel;
	}

	/**
	 *  设置数据集
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
	 *  设置数据集
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
		xylinerenderer.setSeriesStroke(0, new BasicStroke(1.5F));//设置曲线粗细
		xylinerenderer.setSeriesStroke(1, new BasicStroke(1.5F));
        return jfreechart;  
	}
	
	/**
	 * 设置曲线图表的样式
	 */
	public static void setChartTheme(){
		//创建主题样式  
        StandardChartTheme mChartTheme = new StandardChartTheme("CN");
        //设置标题字体  
        mChartTheme.setExtraLargeFont(new Font("黑体", Font.BOLD, 14));
        //设置轴向字体 
        mChartTheme.setLargeFont(new Font("宋体", Font.CENTER_BASELINE, 12));
        //设置图例字体  
        mChartTheme.setRegularFont(new Font("宋体", Font.CENTER_BASELINE, 12));
        //应用主题样式  
        ChartFactory.setChartTheme(mChartTheme);
	}

	/*public static JFreeChart createChart(XYDataset dataset, String chartName,
			String xLabel, String yLabel) {
		NumberAxis xNum = new NumberAxis(xLabel);// X轴
		xNum.setAutoRangeStickyZero(true);
		NumberAxis yNum = new NumberAxis(yLabel);// Y轴
		yNum.setAutoRangeStickyZero(true);
		XYSplineRenderer xySplineRenderer = new XYSplineRenderer();
		xySplineRenderer.setSeriesStroke(0, new BasicStroke(1.8F));// 设置曲线粗细
		xySplineRenderer.setSeriesStroke(1, new BasicStroke(1.8F));// 设置曲线粗细
		xySplineRenderer.setBaseShapesVisible(false);// 隐藏曲线上的小点
		XYPlot xyPlot = new XYPlot(dataset, xNum, yNum, xySplineRenderer);
		xyPlot.setBackgroundPaint(Color.white);
		// 设置虚线颜色
		xyPlot.setDomainGridlinePaint(Color.black);
		xyPlot.setRangeGridlinePaint(Color.black);
		xyPlot.setAxisOffset(new RectangleInsets(4D, 4D, 4D, 4D));
		JFreeChart jFreeChart = new JFreeChart(chartName,
				JFreeChart.DEFAULT_TITLE_FONT, xyPlot, true);
		return jFreeChart;
	}*/

	/**
	 *  创建一张空表
	 * @param title
	 * @return
	 */
	public static JFreeChart createBlankChart(String title) {
		StandardChartTheme standardChartTheme = new StandardChartTheme("CN");
		standardChartTheme.setExtraLargeFont(new Font("宋体", Font.BOLD, 16));
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
