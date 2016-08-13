package com.crh.view.panel;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.LineBorder;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.annotations.XYPointerAnnotation;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.TextAnchor;

import com.crh.service.TractionConfPanelService;
import com.crh.service.TrainEditPanelService;
import com.crh.view.dialog.TractionLevelEditDialog;
import com.crh2.calculate.TractionLevelForce;
import com.crh2.javabean.TractionLevelFormulaBean;
import com.crh2.javabean.TrainCategory;
import com.crh2.javabean.TrainTractionLevelConf;
import com.crh2.util.MyTools;

/**
 * 牵引特性配置（有级）
 * @author huhui
 *
 */
public class TractionLevelConfPanel extends JPanel {
	
	/**
	 * 全局train_category_id
	 */
	private int trainCategoryId = 0;
	
	/**
	 * 下拉菜单
	 */
	private static final JComboBox<Integer> trainIdComboBox = new JComboBox<Integer>();
	private static final JComboBox<String> trainNameComboBox = new JComboBox<String>();
	/**
	 * 所有“列车名称”
	 */
	private Integer [] trainCategoryIdArray;
	private String[] trainCategoryNameArray; 
	
	/**
	 * 图表Panel
	 */
	private ChartPanel tractionChartPanel = null;
	private ArrayList<TractionLevelFormulaBean> chartDataList = null;
	public static double V = 450;
	public static JButton showTractionLevelChartBtn;
	
	public TractionLevelConfPanel() {
		setLayout(null);
		
		getAllTrainCategoryToArray();//初始化两个下拉框的数据
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		panel.setBounds(10, 10, 1326, 36);
		add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("\u5BF9\u5DF2\u5B58\u5728\u7684\u7F16\u7EC4\u8FDB\u884C\u8BBE\u8BA1\uFF1A");
		lblNewLabel.setBounds(10, 10, 144, 15);
		panel.add(lblNewLabel);
		
		trainNameComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 两个JComboBox级联
				int selectedIndex = trainNameComboBox.getSelectedIndex();
				trainIdComboBox.setSelectedIndex(selectedIndex);
			}
		});
		trainNameComboBox.setModel(new DefaultComboBoxModel<String>(trainCategoryNameArray));
		trainNameComboBox.setBounds(164, 7, 117, 21);
		panel.add(trainNameComboBox);
		
		JButton btnNewButton = new JButton("\u7275\u5F15\u7EA7\u4F4D\u914D\u7F6E");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int trainCategoryId = (Integer) trainIdComboBox.getSelectedItem();
				if(trainCategoryId == 0){
					JOptionPane.showMessageDialog(TractionLevelConfPanel.this, "请选择编组名称！");
					return;
				}
				int trainInfoCount = TrainEditPanelService.getTrainInfoCount(trainCategoryId);
				if(trainInfoCount == 0){
					JOptionPane.showMessageDialog(TractionLevelConfPanel.this, "该车“列车编组信息”数据未保存，请返回“列车数据”保存！");
					return;
				}
				String trainName = (String) trainNameComboBox.getSelectedItem();
				new TractionLevelEditDialog(trainName, trainCategoryId);
			}
		});
		btnNewButton.setBounds(428, 6, 105, 23);
		panel.add(btnNewButton);
		
		showTractionLevelChartBtn = new JButton("确定");
		showTractionLevelChartBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int categoryId = (Integer) trainIdComboBox.getSelectedItem();
				trainCategoryId = categoryId;
				if(categoryId == 0){
					JOptionPane.showMessageDialog(TractionLevelConfPanel.this, "请选择编组名称！");
				}else{
					assignParametersToTractionFormula(categoryId);//默认选中第一个设计
				}
			}
		});
		showTractionLevelChartBtn.setBounds(291, 6, 93, 23);
		panel.add(showTractionLevelChartBtn);
		
		trainIdComboBox.setBounds(571, 7, 76, 21);
		trainIdComboBox.setModel(new DefaultComboBoxModel<Integer>(trainCategoryIdArray));
		panel.add(trainIdComboBox);
		trainIdComboBox.setVisible(false);
		
		final JCheckBox checkBox = new JCheckBox("显示关键点");
		checkBox.setBounds(799, 6, 103, 23);
		panel.add(checkBox);
		checkBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(trainCategoryId == 0){
					JOptionPane.showMessageDialog(TractionLevelConfPanel.this, "请选择列车编组名称！");
					checkBox.setSelected(false);
					return;
				}
				if(checkBox.isSelected()){
					TractionLevelConfChart.displayCriticalPoints();
				}else{
					TractionLevelConfChart.cancelDisplayCriticalPoints();
				}
			}
		});
		
		//牵引特性曲线图
		tractionChartPanel = new ChartPanel(TractionLevelConfChart.createBlankChart("牵引特性设计"));
		tractionChartPanel.setPopupMenu(null);
		tractionChartPanel.setBounds(10, 56, 1326, 597);
		tractionChartPanel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		
		add(tractionChartPanel);
		
	}
	
	/**
	 *  从数据表train_category获得所有列车名称，并赋值
	 */
	public void getAllTrainCategoryToArray() {
		ArrayList<TrainCategory> tc = TrainEditPanelService.getAllTrainCategory();
		trainCategoryIdArray = new Integer[tc.size()+1];
		trainCategoryNameArray = new String[tc.size()+1];
		trainCategoryIdArray[0] = 0;
		trainCategoryNameArray[0] = "请选择编组名称";
		for (int i = 0; i < tc.size(); i++) {
			trainCategoryIdArray[i+1] = tc.get(i).getId();
			trainCategoryNameArray[i+1] = tc.get(i).getName();
		}
	}
	
	/**
	 * 获取不同牵引级别对应的牵引力（有级）
	 * @param level
	 * @param trainCategoryId
	 * @return
	 */
	public static ArrayList<TractionLevelFormulaBean> getTractionLevelData(int level, int trainCategoryId){
		ArrayList<TractionLevelFormulaBean> beanList = new ArrayList<TractionLevelFormulaBean>();
		TractionLevelForce.trainTractionLevelConf = TractionConfPanelService.getTrainTractionLevelConf(trainCategoryId);
		for(int v=0;v<=V;v+=5){
			TractionLevelFormulaBean bean = new TractionLevelFormulaBean();
			bean.setT1(TractionLevelForce.getTractionLevelForce(level, v));
			bean.setV(v);
			beanList.add(bean);
		}
		return beanList;
	}
	
	/**
	 * 通过train_category_id获取该车型对应的牵引参数，并给牵引公式赋值
	 * @param trainCategoryId
	 */
	public void assignParametersToTractionFormula(int trainCategoryId){
		//牵引力-速度曲线表
		final TractionLevelConfChart tractionLevelConfChart = new TractionLevelConfChart();
		if(chartDataList != null){
			chartDataList.clear();
		}
		// 保存由公式计算得到的结果
		chartDataList = new ArrayList<TractionLevelFormulaBean>();
		//给牵引公式赋值
		TractionLevelForce.trainTractionLevelConf = TractionConfPanelService.getTrainTractionLevelConf(trainCategoryId);
		for(int v=0;v<=V;v+=5){
			TractionLevelFormulaBean bean = new TractionLevelFormulaBean();
			bean.setT1(TractionLevelForce.getTractionLevelForce(1, v));
			bean.setT2(TractionLevelForce.getTractionLevelForce(2, v));
			bean.setT3(TractionLevelForce.getTractionLevelForce(3, v));
			bean.setT4(TractionLevelForce.getTractionLevelForce(4, v));
			bean.setT5(TractionLevelForce.getTractionLevelForce(5, v));
			bean.setT6(TractionLevelForce.getTractionLevelForce(6, v));
			bean.setT7(TractionLevelForce.getTractionLevelForce(7, v));
			bean.setT8(TractionLevelForce.getTractionLevelForce(8, v));
			bean.setT9(TractionLevelForce.getTractionLevelForce(9, v));
			bean.setT10(TractionLevelForce.getTractionLevelForce(10, v));
			bean.setT11(TractionLevelForce.getTractionLevelForce(11, v));
			bean.setT12(TractionLevelForce.getTractionLevelForce(12, v));
			bean.setT13(TractionLevelForce.getTractionLevelForce(13, v));
			bean.setT14(TractionLevelForce.getTractionLevelForce(14, v));
			bean.setT15(TractionLevelForce.getTractionLevelForce(15, v));
			bean.setV(v);
			chartDataList.add(bean);
		}
		
		if(tractionChartPanel != null){
			remove(tractionChartPanel);
		}
		tractionChartPanel = tractionLevelConfChart.addChartToPanel(chartDataList);
		//右键菜单
		JPopupMenu popupMenu = new JPopupMenu();
		addPopup(tractionChartPanel, popupMenu);
		//1N
		final JCheckBoxMenuItem checkBoxMenuItem_0 = new JCheckBoxMenuItem("1N");
		checkBoxMenuItem_0.setSelected(true);
		checkBoxMenuItem_0.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(checkBoxMenuItem_0.isSelected()){
					TractionLevelConfChart.lineStateChanger(0, true);
				}else{
					TractionLevelConfChart.lineStateChanger(0, false);
				}
			}
		});
		popupMenu.add(checkBoxMenuItem_0);
		//2N
		final JCheckBoxMenuItem checkBoxMenuItem_1 = new JCheckBoxMenuItem("2N");
		checkBoxMenuItem_1.setSelected(true);
		checkBoxMenuItem_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(checkBoxMenuItem_1.isSelected()){
					TractionLevelConfChart.lineStateChanger(1, true);
				}else{
					TractionLevelConfChart.lineStateChanger(1, false);
				}
			}
		});
		popupMenu.add(checkBoxMenuItem_1);
		//3N
		final JCheckBoxMenuItem checkBoxMenuItem_2 = new JCheckBoxMenuItem("3N");
		checkBoxMenuItem_2.setSelected(true);
		checkBoxMenuItem_2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(checkBoxMenuItem_2.isSelected()){
					TractionLevelConfChart.lineStateChanger(2, true);
				}else{
					TractionLevelConfChart.lineStateChanger(2, false);
				}
			}
		});
		popupMenu.add(checkBoxMenuItem_2);
		//4N
		final JCheckBoxMenuItem checkBoxMenuItem_3 = new JCheckBoxMenuItem("4N");
		checkBoxMenuItem_3.setSelected(true);
		checkBoxMenuItem_3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(checkBoxMenuItem_3.isSelected()){
					TractionLevelConfChart.lineStateChanger(3, true);
				}else{
					TractionLevelConfChart.lineStateChanger(3, false);
				}
			}
		});
		popupMenu.add(checkBoxMenuItem_3);
		//5N
		final JCheckBoxMenuItem checkBoxMenuItem_4 = new JCheckBoxMenuItem("5N");
		checkBoxMenuItem_4.setSelected(true);
		checkBoxMenuItem_4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(checkBoxMenuItem_4.isSelected()){
					TractionLevelConfChart.lineStateChanger(4, true);
				}else{
					TractionLevelConfChart.lineStateChanger(4, false);
				}
			}
		});
		popupMenu.add(checkBoxMenuItem_4);
		//6N
		final JCheckBoxMenuItem checkBoxMenuItem_5 = new JCheckBoxMenuItem("6N");
		checkBoxMenuItem_5.setSelected(true);
		checkBoxMenuItem_5.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(checkBoxMenuItem_5.isSelected()){
					TractionLevelConfChart.lineStateChanger(5, true);
				}else{
					TractionLevelConfChart.lineStateChanger(5, false);
				}
			}
		});
		popupMenu.add(checkBoxMenuItem_5);
		//7N
		final JCheckBoxMenuItem checkBoxMenuItem_6 = new JCheckBoxMenuItem("7N");
		checkBoxMenuItem_6.setSelected(true);
		checkBoxMenuItem_6.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(checkBoxMenuItem_6.isSelected()){
					TractionLevelConfChart.lineStateChanger(6, true);
				}else{
					TractionLevelConfChart.lineStateChanger(6, false);
				}
			}
		});
		popupMenu.add(checkBoxMenuItem_6);
		//8N
		final JCheckBoxMenuItem checkBoxMenuItem_7 = new JCheckBoxMenuItem("8N");
		checkBoxMenuItem_7.setSelected(true);
		checkBoxMenuItem_7.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(checkBoxMenuItem_7.isSelected()){
					TractionLevelConfChart.lineStateChanger(7, true);
				}else{
					TractionLevelConfChart.lineStateChanger(7, false);
				}
			}
		});
		popupMenu.add(checkBoxMenuItem_7);
		//9N
		final JCheckBoxMenuItem checkBoxMenuItem_8 = new JCheckBoxMenuItem("9N");
		checkBoxMenuItem_8.setSelected(true);
		checkBoxMenuItem_8.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(checkBoxMenuItem_8.isSelected()){
					TractionLevelConfChart.lineStateChanger(8, true);
				}else{
					TractionLevelConfChart.lineStateChanger(8, false);
				}
			}
		});
		popupMenu.add(checkBoxMenuItem_8);
		//10N
		final JCheckBoxMenuItem checkBoxMenuItem_9 = new JCheckBoxMenuItem("10N");
		checkBoxMenuItem_9.setSelected(true);
		checkBoxMenuItem_9.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(checkBoxMenuItem_9.isSelected()){
					TractionLevelConfChart.lineStateChanger(9, true);
				}else{
					TractionLevelConfChart.lineStateChanger(9, false);
				}
			}
		});
		popupMenu.add(checkBoxMenuItem_9);
		
		//11N
		final JCheckBoxMenuItem checkBoxMenuItem_10 = new JCheckBoxMenuItem("11N");
		checkBoxMenuItem_10.setSelected(true);
		checkBoxMenuItem_10.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(checkBoxMenuItem_10.isSelected()){
					TractionLevelConfChart.lineStateChanger(10, true);
				}else{
					TractionLevelConfChart.lineStateChanger(10, false);
				}
			}
		});
		popupMenu.add(checkBoxMenuItem_10);
		
		//12N
		final JCheckBoxMenuItem checkBoxMenuItem_11 = new JCheckBoxMenuItem("12N");
		checkBoxMenuItem_11.setSelected(true);
		checkBoxMenuItem_11.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(checkBoxMenuItem_11.isSelected()){
					TractionLevelConfChart.lineStateChanger(11, true);
				}else{
					TractionLevelConfChart.lineStateChanger(11, false);
				}
			}
		});
		popupMenu.add(checkBoxMenuItem_11);
		
		//13N
		final JCheckBoxMenuItem checkBoxMenuItem_12 = new JCheckBoxMenuItem("13N");
		checkBoxMenuItem_12.setSelected(true);
		checkBoxMenuItem_12.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(checkBoxMenuItem_12.isSelected()){
					TractionLevelConfChart.lineStateChanger(12, true);
				}else{
					TractionLevelConfChart.lineStateChanger(12, false);
				}
			}
		});
		popupMenu.add(checkBoxMenuItem_12);
		
		//14N
		final JCheckBoxMenuItem checkBoxMenuItem_13 = new JCheckBoxMenuItem("14N");
		checkBoxMenuItem_13.setSelected(true);
		checkBoxMenuItem_13.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(checkBoxMenuItem_13.isSelected()){
					TractionLevelConfChart.lineStateChanger(13, true);
				}else{
					TractionLevelConfChart.lineStateChanger(13, false);
				}
			}
		});
		popupMenu.add(checkBoxMenuItem_13);
		
		//15N
		final JCheckBoxMenuItem checkBoxMenuItem_14 = new JCheckBoxMenuItem("15N");
		checkBoxMenuItem_14.setSelected(true);
		checkBoxMenuItem_14.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(checkBoxMenuItem_14.isSelected()){
					TractionLevelConfChart.lineStateChanger(14, true);
				}else{
					TractionLevelConfChart.lineStateChanger(14, false);
				}
			}
		});
		popupMenu.add(checkBoxMenuItem_14);
		
		//刷新chartPanel
		repaint();
		add(tractionChartPanel);
		
	}
	
	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
}
	
/**
 * 生成 速度-牵引力 图表
 * @author huhui
 *
 */
class TractionLevelConfChart {
	
	private static XYLineAndShapeRenderer xySplineRenderer;
	public static XYSeriesCollection xyseriescollection = null;
	
	/**
	 * 将chartPanel添加到TractionConfChartPanel中
	 * @param dataList
	 * @return
	 */
	public ChartPanel addChartToPanel(ArrayList<TractionLevelFormulaBean> dataList){
		XYDataset dataset = createDataset(dataList);
		JFreeChart chart = createChart(dataset);
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setBounds(10, 56, 1326, 597);
		chartPanel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		chartPanel.setPopupMenu(null);
		chartPanel.setMouseWheelEnabled(true);//滚轮放大
		return chartPanel;
	}

	/**
	 *  设置数据集
	 * @param chartDataList
	 * @return
	 */
	public XYDataset createDataset(ArrayList<TractionLevelFormulaBean> chartDataList) {
		xyseriescollection = new XYSeriesCollection();
		XYSeries _1NSeries = new XYSeries("1N");
		XYSeries _2NSeries = new XYSeries("2N");
		XYSeries _3NSeries = new XYSeries("3N");
		XYSeries _4NSeries = new XYSeries("4N");
		XYSeries _5NSeries = new XYSeries("5N");
		XYSeries _6NSeries = new XYSeries("6N");
		XYSeries _7NSeries = new XYSeries("7N");
		XYSeries _8NSeries = new XYSeries("8N");
		XYSeries _9NSeries = new XYSeries("9N");
		XYSeries _10NSeries = new XYSeries("10N");
		XYSeries _11NSeries = new XYSeries("11N");
		XYSeries _12NSeries = new XYSeries("12N");
		XYSeries _13NSeries = new XYSeries("13N");
		XYSeries _14NSeries = new XYSeries("14N");
		XYSeries _15NSeries = new XYSeries("15N");
		
		for (TractionLevelFormulaBean bean : chartDataList) {
			_1NSeries.add(bean.getV(), bean.getT1());
			_2NSeries.add(bean.getV(), bean.getT2());
			_3NSeries.add(bean.getV(), bean.getT3());
			_4NSeries.add(bean.getV(), bean.getT4());
			_5NSeries.add(bean.getV(), bean.getT5());
			_6NSeries.add(bean.getV(), bean.getT6());
			_7NSeries.add(bean.getV(), bean.getT7());
			_8NSeries.add(bean.getV(), bean.getT8());
			_9NSeries.add(bean.getV(), bean.getT9());
			_10NSeries.add(bean.getV(), bean.getT10());
			_11NSeries.add(bean.getV(), bean.getT11());
			_12NSeries.add(bean.getV(), bean.getT12());
			_13NSeries.add(bean.getV(), bean.getT13());
			_14NSeries.add(bean.getV(), bean.getT14());
			_15NSeries.add(bean.getV(), bean.getT15());
			/*System.out.println("T1="+bean.getT1()+",T2="+bean.getT2()+",T3="+bean.getT3()+",T4="+bean.getT4()+",T5="+bean.getT5()+",T6="+bean.getT6()+",T7=" +
					""+bean.getT7()+",T8="+bean.getT8()+",T9="+bean.getT9()+",T10="+bean.getT10());*/
		}
	
		xyseriescollection.addSeries(_1NSeries);
		xyseriescollection.addSeries(_2NSeries);
		xyseriescollection.addSeries(_3NSeries);
		xyseriescollection.addSeries(_4NSeries);
		xyseriescollection.addSeries(_5NSeries);
		xyseriescollection.addSeries(_6NSeries);
		xyseriescollection.addSeries(_7NSeries);
		xyseriescollection.addSeries(_8NSeries);
		xyseriescollection.addSeries(_9NSeries);
		xyseriescollection.addSeries(_10NSeries);
		xyseriescollection.addSeries(_11NSeries);
		xyseriescollection.addSeries(_12NSeries);
		xyseriescollection.addSeries(_13NSeries);
		xyseriescollection.addSeries(_14NSeries);
		xyseriescollection.addSeries(_15NSeries);
		return xyseriescollection;
	}
	
	/**
	 * 显示关键点
	 */
	public static void displayCriticalPoints(){
		TrainTractionLevelConf trainTractionLevelConf = TractionLevelForce.trainTractionLevelConf;
		//1N
		if (xySplineRenderer.getSeriesVisible(0)) {
			double x1 = trainTractionLevelConf.get_1_1();
			double y1 = MyTools.numFormat2(TractionLevelForce.getTractionLevelForce(1, x1));
			XYPointerAnnotation annotation0 = new XYPointerAnnotation("(" + x1 + "km/h," + y1 + "KN)", x1, y1, -0.7853981633974483D);
			annotation0.setTextAnchor(TextAnchor.BOTTOM_LEFT);
			annotation0.setPaint(Color.red);
			annotation0.setArrowPaint(Color.red);
			xySplineRenderer.addAnnotation(annotation0);
		}
		//2N
		if (xySplineRenderer.getSeriesVisible(1)) {
			double x2 = trainTractionLevelConf.get_2_1();
			double y2 = MyTools.numFormat2(TractionLevelForce.getTractionLevelForce(2, x2));
			XYPointerAnnotation annotation0 = new XYPointerAnnotation("(" + x2 + "km/h," + y2 + "KN)", x2, y2, -0.7853981633974483D);
			annotation0.setTextAnchor(TextAnchor.BOTTOM_LEFT);
			annotation0.setPaint(Color.blue);
			annotation0.setArrowPaint(Color.blue);
			xySplineRenderer.addAnnotation(annotation0);
		}
		//3N
		if (xySplineRenderer.getSeriesVisible(2)) {
			double x3 = trainTractionLevelConf.get_3_1();
			double y3 = MyTools.numFormat2(TractionLevelForce.getTractionLevelForce(3, x3));
			XYPointerAnnotation annotation0 = new XYPointerAnnotation("(" + x3 + "km/h," + y3 + "KN)", x3, y3, -0.7853981633974483D);
			annotation0.setTextAnchor(TextAnchor.BOTTOM_LEFT);
			annotation0.setPaint(new Color(85, 255, 85));
			annotation0.setArrowPaint(new Color(85, 255, 85));
			xySplineRenderer.addAnnotation(annotation0);
		}
		//4N
		if (xySplineRenderer.getSeriesVisible(3)) {
			double x4 = trainTractionLevelConf.get_4_2();
			double y4 = MyTools.numFormat2(TractionLevelForce.getTractionLevelForce(4, x4));
			XYPointerAnnotation annotation0 = new XYPointerAnnotation("(" + x4 + "km/h," + y4 + "KN)", x4, y4, -0.7853981633974483D);
			annotation0.setTextAnchor(TextAnchor.BOTTOM_LEFT);
			annotation0.setPaint(Color.yellow);
			annotation0.setArrowPaint(Color.yellow);
			xySplineRenderer.addAnnotation(annotation0);
		}
		//5N
		if (xySplineRenderer.getSeriesVisible(4)) {
			double x5 = trainTractionLevelConf.get_5_2();
			double y5 = MyTools.numFormat2(TractionLevelForce.getTractionLevelForce(5, x5));
			XYPointerAnnotation annotation0 = new XYPointerAnnotation("(" + x5 + "km/h," + y5 + "KN)", x5, y5, -0.7853981633974483D);
			annotation0.setTextAnchor(TextAnchor.BOTTOM_LEFT);
			annotation0.setPaint(new Color(255, 85, 255));
			annotation0.setArrowPaint(new Color(255, 85, 255));
			xySplineRenderer.addAnnotation(annotation0);
		}
		//6N
		if (xySplineRenderer.getSeriesVisible(5)) {
			double x6 = trainTractionLevelConf.get_6_2();
			double y6 = MyTools.numFormat2(TractionLevelForce.getTractionLevelForce(6, x6));
			XYPointerAnnotation annotation0 = new XYPointerAnnotation("(" + x6 + "km/h," + y6 + "KN)", x6, y6, -0.7853981633974483D);
			annotation0.setTextAnchor(TextAnchor.BOTTOM_LEFT);
			annotation0.setPaint(Color.cyan);
			annotation0.setArrowPaint(Color.cyan);
			xySplineRenderer.addAnnotation(annotation0);
		}
		//7N
		if (xySplineRenderer.getSeriesVisible(6)) {
			double x7 = trainTractionLevelConf.get_7_2();
			double y7 = MyTools.numFormat2(TractionLevelForce.getTractionLevelForce(7, x7));
			XYPointerAnnotation annotation0 = new XYPointerAnnotation("(" + x7 + "km/h," + y7 + "KN)", x7, y7, -0.7853981633974483D);
			annotation0.setTextAnchor(TextAnchor.BOTTOM_LEFT);
			annotation0.setPaint(new Color(255, 175, 175));
			annotation0.setArrowPaint(new Color(255, 175, 175));
			xySplineRenderer.addAnnotation(annotation0);
		}
		//8N
		if (xySplineRenderer.getSeriesVisible(7)) {
			double x8 = trainTractionLevelConf.get_8_2();
			double y8 = MyTools.numFormat2(TractionLevelForce.getTractionLevelForce(8, x8));
			XYPointerAnnotation annotation0 = new XYPointerAnnotation("(" + x8 + "km/h," + y8 + "KN)", x8, y8, -0.7853981633974483D);
			annotation0.setTextAnchor(TextAnchor.BOTTOM_LEFT);
			annotation0.setPaint(new Color(128, 128, 128));
			annotation0.setArrowPaint(new Color(128, 128, 128));
			xySplineRenderer.addAnnotation(annotation0);
		}
		//9N
		if (xySplineRenderer.getSeriesVisible(8)) {
			double x9 = trainTractionLevelConf.get_9_2();
			double y9 = MyTools.numFormat2(TractionLevelForce.getTractionLevelForce(9, x9));
			XYPointerAnnotation annotation0 = new XYPointerAnnotation("(" + x9 + "km/h," + y9 + "KN)", x9, y9, -0.7853981633974483D);
			annotation0.setTextAnchor(TextAnchor.BOTTOM_LEFT);
			annotation0.setPaint(new Color(154, 16, 14));
			annotation0.setArrowPaint(new Color(154, 16, 14));
			xySplineRenderer.addAnnotation(annotation0);
		}
		//10N
		if (xySplineRenderer.getSeriesVisible(9)) {
			double x10_1 = trainTractionLevelConf.get_10_2();
			double y10_1 = MyTools.numFormat2(TractionLevelForce.getTractionLevelForce(10, x10_1));
			XYPointerAnnotation annotation0 = new XYPointerAnnotation("(" + x10_1 + "km/h," + y10_1 + "KN)", x10_1, y10_1, -0.7853981633974483D);
			annotation0.setTextAnchor(TextAnchor.BOTTOM_LEFT);
			annotation0.setPaint(new Color(13, 7, 143));
			annotation0.setArrowPaint(new Color(13, 7, 143));
			xySplineRenderer.addAnnotation(annotation0);
			double x10_2 = trainTractionLevelConf.get_10_4();
			double y10_2 = MyTools.numFormat2(TractionLevelForce.getTractionLevelForce(10, x10_2));
			XYPointerAnnotation annotation1 = new XYPointerAnnotation("(" + x10_2 + "km/h," + y10_2 + "KN)", x10_2, y10_2, -0.7853981633974483D);
			annotation1.setTextAnchor(TextAnchor.BOTTOM_LEFT);
			annotation1.setPaint(new Color(13, 7, 143));
			annotation1.setArrowPaint(new Color(13, 7, 143));
			xySplineRenderer.addAnnotation(annotation1);
		}
		//11N
		if (xySplineRenderer.getSeriesVisible(10)) {
			double x11_1 = trainTractionLevelConf.get_11_2();
			double y11_1 = MyTools.numFormat2(TractionLevelForce.getTractionLevelForce(11, x11_1));
			XYPointerAnnotation annotation0 = new XYPointerAnnotation("(" + x11_1 + "km/h," + y11_1 + "KN)", x11_1, y11_1, -0.7853981633974483D);
			annotation0.setTextAnchor(TextAnchor.BOTTOM_LEFT);
			annotation0.setPaint(new Color(36, 167, 35));
			annotation0.setArrowPaint(new Color(36, 167, 35));
			xySplineRenderer.addAnnotation(annotation0);
			double x11_2 = trainTractionLevelConf.get_11_4();
			double y11_2 = MyTools.numFormat2(TractionLevelForce.getTractionLevelForce(11, x11_2));
			XYPointerAnnotation annotation1 = new XYPointerAnnotation("(" + x11_2 + "km/h," + y11_2 + "KN)", x11_2, y11_2, -0.7853981633974483D);
			annotation1.setTextAnchor(TextAnchor.BOTTOM_LEFT);
			annotation1.setPaint(new Color(36, 167, 35));
			annotation1.setArrowPaint(new Color(36, 167, 35));
			xySplineRenderer.addAnnotation(annotation1);
		}
		//12N
		if (xySplineRenderer.getSeriesVisible(11)) {
			double x12_1 = trainTractionLevelConf.get_12_2();
			double y12_1 = MyTools.numFormat2(TractionLevelForce.getTractionLevelForce(12, x12_1));
			XYPointerAnnotation annotation0 = new XYPointerAnnotation("(" + x12_1 + "km/h," + y12_1 + "KN)", x12_1, y12_1, -0.7853981633974483D);
			annotation0.setTextAnchor(TextAnchor.BOTTOM_LEFT);
			annotation0.setPaint(new Color(176, 187, 48));
			annotation0.setArrowPaint(new Color(176, 187, 48));
			xySplineRenderer.addAnnotation(annotation0);
			double x12_2 = trainTractionLevelConf.get_12_4();
			double y12_2 = MyTools.numFormat2(TractionLevelForce.getTractionLevelForce(12, x12_2));
			XYPointerAnnotation annotation1 = new XYPointerAnnotation("(" + x12_2 + "km/h," + y12_2 + "KN)", x12_2, y12_2, -0.7853981633974483D);
			annotation1.setTextAnchor(TextAnchor.BOTTOM_LEFT);
			annotation1.setPaint(new Color(176, 187, 48));
			annotation1.setArrowPaint(new Color(176, 187, 48));
			xySplineRenderer.addAnnotation(annotation1);
		}
		//13N
		if (xySplineRenderer.getSeriesVisible(12)) {
			double x13_1 = trainTractionLevelConf.get_13_2();
			double y13_1 = MyTools.numFormat2(TractionLevelForce.getTractionLevelForce(13, x13_1));
			XYPointerAnnotation annotation0 = new XYPointerAnnotation("(" + x13_1 + "km/h," + y13_1 + "KN)", x13_1, y13_1, -0.7853981633974483D);
			annotation0.setTextAnchor(TextAnchor.BOTTOM_LEFT);
			annotation0.setPaint(new Color(158, 23, 161));
			annotation0.setArrowPaint(new Color(158, 23, 161));
			xySplineRenderer.addAnnotation(annotation0);
			double x13_2 = trainTractionLevelConf.get_13_4();
			double y13_2 = MyTools.numFormat2(TractionLevelForce.getTractionLevelForce(13, x13_2));
			XYPointerAnnotation annotation1 = new XYPointerAnnotation("(" + x13_2 + "km/h," + y13_2 + "KN)", x13_2, y13_2, -0.7853981633974483D);
			annotation1.setTextAnchor(TextAnchor.BOTTOM_LEFT);
			annotation1.setPaint(new Color(158, 23, 161));
			annotation1.setArrowPaint(new Color(158, 23, 161));
			xySplineRenderer.addAnnotation(annotation1);
		}
		//14N
		if (xySplineRenderer.getSeriesVisible(13)) {
			double x14_1 = trainTractionLevelConf.get_14_2();
			double y14_1 = MyTools.numFormat2(TractionLevelForce.getTractionLevelForce(14, x14_1));
			XYPointerAnnotation annotation0 = new XYPointerAnnotation("(" + x14_1 + "km/h," + y14_1 + "KN)", x14_1, y14_1, -0.7853981633974483D);
			annotation0.setTextAnchor(TextAnchor.BOTTOM_LEFT);
			annotation0.setPaint(new Color(43, 173, 171));
			annotation0.setArrowPaint(new Color(43, 173, 171));
			xySplineRenderer.addAnnotation(annotation0);
			double x14_2 = trainTractionLevelConf.get_14_4();
			double y14_2 = MyTools.numFormat2(TractionLevelForce.getTractionLevelForce(14, x14_2));
			XYPointerAnnotation annotation1 = new XYPointerAnnotation("(" + x14_2 + "km/h," + y14_2 + "KN)", x14_2, y14_2, -0.7853981633974483D);
			annotation1.setTextAnchor(TextAnchor.BOTTOM_LEFT);
			annotation1.setPaint(new Color(43, 173, 171));
			annotation1.setArrowPaint(new Color(43, 173, 171));
			xySplineRenderer.addAnnotation(annotation1);
		}
		//15N
		if (xySplineRenderer.getSeriesVisible(14)) {
			double x15 = trainTractionLevelConf.get_15_2();
			double y15 = MyTools.numFormat2(TractionLevelForce.getTractionLevelForce(15, x15));
			XYPointerAnnotation annotation0 = new XYPointerAnnotation("(" + x15 + "km/h," + y15 + "KN)", x15, y15, -0.7853981633974483D);
			annotation0.setTextAnchor(TextAnchor.BOTTOM_LEFT);
			annotation0.setPaint(new Color(64, 64, 64));
			annotation0.setArrowPaint(new Color(64, 64, 64));
			xySplineRenderer.addAnnotation(annotation0);
		}
		
		
	}
	
	/**
	 * 取消显示关键点
	 */
	public static void cancelDisplayCriticalPoints(){
		xySplineRenderer.removeAnnotations();
	}
	
	public static JFreeChart createChart(XYDataset dataset) {
		setChartTheme();
		JFreeChart jFreeChart = ChartFactory.createXYLineChart("牵引级位设计", "速度(km/h)", "牵引力(KN)", dataset, PlotOrientation.VERTICAL, true, true, false); 
		XYPlot plot = jFreeChart.getXYPlot();
		plot.setDomainGridlinePaint(Color.BLACK);
		plot.setRangeGridlinePaint(Color.BLACK);
		plot.setBackgroundPaint(Color.WHITE);
		xySplineRenderer=(XYLineAndShapeRenderer)plot.getRenderer();
		xySplineRenderer.setSeriesStroke(0, new BasicStroke(1.8F));//设置曲线粗细
		xySplineRenderer.setSeriesStroke(1, new BasicStroke(1.8F));
		xySplineRenderer.setSeriesStroke(2, new BasicStroke(1.8F));
		xySplineRenderer.setSeriesStroke(3, new BasicStroke(1.8F));
		xySplineRenderer.setSeriesStroke(4, new BasicStroke(1.8F));
		xySplineRenderer.setSeriesStroke(5, new BasicStroke(1.8F));
		xySplineRenderer.setSeriesStroke(6, new BasicStroke(1.8F));
		xySplineRenderer.setSeriesStroke(7, new BasicStroke(1.8F));
		xySplineRenderer.setSeriesStroke(8, new BasicStroke(1.8F));
		xySplineRenderer.setSeriesStroke(9, new BasicStroke(1.8F));
		xySplineRenderer.setSeriesStroke(10, new BasicStroke(1.8F));
		xySplineRenderer.setSeriesStroke(11, new BasicStroke(1.8F));
		xySplineRenderer.setSeriesStroke(12, new BasicStroke(1.8F));
		xySplineRenderer.setSeriesStroke(13, new BasicStroke(1.8F));
		xySplineRenderer.setSeriesStroke(14, new BasicStroke(1.8F));
		setLinesVisible();
		
		return jFreeChart;
	}
	
	/**
	 * 设置所有曲线显示    因为要使用xySplineRenderer.getSeriesVisible(4)方法，这个方法依赖于setSeriesVisible方法
	 */
	public static void setLinesVisible(){
		xySplineRenderer.setSeriesVisible(0, true);
		xySplineRenderer.setSeriesVisible(1, true);
		xySplineRenderer.setSeriesVisible(2, true);
		xySplineRenderer.setSeriesVisible(3, true);
		xySplineRenderer.setSeriesVisible(4, true);
		xySplineRenderer.setSeriesVisible(5, true);
		xySplineRenderer.setSeriesVisible(6, true);
		xySplineRenderer.setSeriesVisible(7, true);
		xySplineRenderer.setSeriesVisible(8, true);
		xySplineRenderer.setSeriesVisible(9, true);
		xySplineRenderer.setSeriesVisible(10, true);
		xySplineRenderer.setSeriesVisible(11, true);
		xySplineRenderer.setSeriesVisible(12, true);
		xySplineRenderer.setSeriesVisible(13, true);
		xySplineRenderer.setSeriesVisible(14, true);
	}
	
	/**
	 * 显示/隐藏曲线
	 * @param lineIndex
	 * @param visible
	 */
	public static void lineStateChanger(int lineIndex, boolean visible){
		if(lineIndex == 0){
			xySplineRenderer.setSeriesVisible(0, visible);
		}else if(lineIndex == 1){
			xySplineRenderer.setSeriesVisible(1, visible);
		}else if(lineIndex == 2){
			xySplineRenderer.setSeriesVisible(2, visible);
		}else if(lineIndex == 3){
			xySplineRenderer.setSeriesVisible(3, visible);
		}else if(lineIndex == 4){
			xySplineRenderer.setSeriesVisible(4, visible);
		}else if(lineIndex == 5){
			xySplineRenderer.setSeriesVisible(5, visible);
		}else if(lineIndex == 6){
			xySplineRenderer.setSeriesVisible(6, visible);
		}else if(lineIndex == 7){
			xySplineRenderer.setSeriesVisible(7, visible);
		}else if(lineIndex == 8){
			xySplineRenderer.setSeriesVisible(8, visible);
		}else if(lineIndex == 9){
			xySplineRenderer.setSeriesVisible(9, visible);
		}else if(lineIndex == 10){
			xySplineRenderer.setSeriesVisible(10, visible);
		}else if(lineIndex == 11){
			xySplineRenderer.setSeriesVisible(11, visible);
		}else if(lineIndex == 12){
			xySplineRenderer.setSeriesVisible(12, visible);
		}else if(lineIndex == 13){
			xySplineRenderer.setSeriesVisible(13, visible);
		}else if(lineIndex == 14){
			xySplineRenderer.setSeriesVisible(14, visible);
		}
	}
	
	/**
	 * 创建一张空表
	 * @param title
	 * @return
	 */
	public static JFreeChart createBlankChart(String title){
		StandardChartTheme standardChartTheme = new StandardChartTheme("CN");
		standardChartTheme.setExtraLargeFont(new Font("宋体", Font.BOLD, 16));
		ChartFactory.setChartTheme(standardChartTheme);
		JFreeChart blankChart = ChartFactory.createXYLineChart(title,"Speed(km/h)", "", null, PlotOrientation.VERTICAL, true,true, false);
		XYPlot plot = blankChart.getXYPlot();
		plot.setBackgroundPaint(Color.WHITE);
		plot.setDomainGridlinePaint(Color.black);
		plot.setRangeGridlinePaint(Color.black);
		return blankChart;
	}
	
	/**
	 * 设置图表样式
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
	
}
