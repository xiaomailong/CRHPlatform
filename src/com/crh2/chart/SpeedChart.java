package com.crh2.chart;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Point;

import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.dial.DialBackground;
import org.jfree.chart.plot.dial.DialCap;
import org.jfree.chart.plot.dial.DialPlot;
import org.jfree.chart.plot.dial.DialTextAnnotation;
import org.jfree.chart.plot.dial.DialValueIndicator;
import org.jfree.chart.plot.dial.StandardDialFrame;
import org.jfree.chart.plot.dial.StandardDialRange;
import org.jfree.chart.plot.dial.StandardDialScale;
import org.jfree.data.general.DefaultValueDataset;
import org.jfree.data.general.ValueDataset;
import org.jfree.ui.GradientPaintTransformType;
import org.jfree.ui.StandardGradientPaintTransformer;

import com.crh2.frame.items.BlankPanel;
import com.crh2.frame.items.ParaPanel;

/**
 * �ٶ��Ǳ��̵�ͼ��
 * @author huhui
 *
 */
public class SpeedChart extends JPanel {

	private static DefaultValueDataset dataset;
	private ParaPanel paraPanel = new ParaPanel();
	private BlankPanel blankPanel = new BlankPanel();

	public static JFreeChart createStandardDialChart(String s, String s1,
			ValueDataset valuedataset, double d, double d1, double d2, int i) {
		DialPlot dialplot = new DialPlot();
		dialplot.setDataset(valuedataset);
		dialplot.setDialFrame(new StandardDialFrame());
		dialplot.setBackground(new DialBackground());
		DialTextAnnotation dialtextannotation = new DialTextAnnotation(s1);
		dialtextannotation.setFont(new Font("Dialog", 1, 14));
		dialtextannotation.setRadius(0.69999999999999996D);
		dialplot.addLayer(dialtextannotation);
		DialValueIndicator dialvalueindicator = new DialValueIndicator(0);
		dialplot.addLayer(dialvalueindicator);
		StandardDialScale standarddialscale = new StandardDialScale(d, d1,
				-120D, -300D, 10D, 4);
		standarddialscale.setMajorTickIncrement(d2);
		standarddialscale.setMinorTickCount(i);
		standarddialscale.setTickRadius(0.88D);
		standarddialscale.setTickLabelOffset(0.14999999999999999D);
		standarddialscale.setTickLabelFont(new Font("Dialog", 0, 14));
		dialplot.addScale(0, standarddialscale);
		dialplot.addPointer(new org.jfree.chart.plot.dial.DialPointer.Pin());
		DialCap dialcap = new DialCap();
		dialplot.setCap(dialcap);
		return new JFreeChart(s, dialplot);
	}

	public static void setValue(double speed) {
		// ������ֵ
		dataset.setValue(speed);
	}

	public SpeedChart() {
		super(new BorderLayout());
		dataset = new DefaultValueDataset(0D);
		JFreeChart jfreechart = createStandardDialChart("",
				"Speed (km/h)", dataset, 0D, 390D, 30D, 4);
		DialPlot dialplot = (DialPlot) jfreechart.getPlot();
		StandardDialRange standarddialrange = new StandardDialRange(260D,
				390D, Color.red);
		standarddialrange.setInnerRadius(0.52000000000000002D);
		standarddialrange.setOuterRadius(0.55000000000000004D);
		dialplot.addLayer(standarddialrange);
		StandardDialRange standarddialrange1 = new StandardDialRange(130D,
				260D, Color.orange);
		standarddialrange1.setInnerRadius(0.52000000000000002D);
		standarddialrange1.setOuterRadius(0.55000000000000004D);
		dialplot.addLayer(standarddialrange1);
		StandardDialRange standarddialrange2 = new StandardDialRange(0D,
				130D, Color.green);
		standarddialrange2.setInnerRadius(0.52000000000000002D);
		standarddialrange2.setOuterRadius(0.55000000000000004D);
		dialplot.addLayer(standarddialrange2);
		GradientPaint gradientpaint = new GradientPaint(new Point(),
				new Color(255, 255, 255), new Point(), new Color(170, 170,
						220));
		DialBackground dialbackground = new DialBackground(gradientpaint);
		dialbackground
				.setGradientPaintTransformer(new StandardGradientPaintTransformer(
						GradientPaintTransformType.VERTICAL));
		dialplot.setBackground(dialbackground);
		dialplot.removePointer(0);
		org.jfree.chart.plot.dial.DialPointer.Pointer pointer = new org.jfree.chart.plot.dial.DialPointer.Pointer();
		pointer.setFillPaint(Color.yellow);
		dialplot.addPointer(pointer);
		ChartPanel chartpanel = new ChartPanel(jfreechart);
		chartpanel.setPreferredSize(new Dimension(300, 300));
		add(chartpanel,BorderLayout.WEST);
		add(paraPanel,BorderLayout.CENTER);
		add(blankPanel,BorderLayout.EAST);
	}
	
	//����panel�Ĵ�С
	public Dimension getPreferredSize() {
        return new Dimension(318, 306); //Set your own size.
    }
}
