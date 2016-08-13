package com.crh.view.panel;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleInsets;

import com.crh.calculation.mintime.TrainRunParametersCal;
import com.crh.service.BrakeConfPanelService;
import com.crh.service.TractionConfPanelService;
import com.crh.service.TrainEditPanelService;
import com.crh.view.dialog.BrakeParameterDialog;
import com.crh2.calculate.TractionFormulas;
import com.crh2.javabean.CurveFormulaBean;
import com.crh2.javabean.TrainBrakeFactor;
import com.crh2.javabean.TrainCategory;
import com.crh2.javabean.TrainElectricBrake;
import com.crh2.util.MyTools;

/**
 * �ƶ���������
 * @author huhui
 *
 */
public class BrakeConfPanel extends JPanel {

	/**
	 * �����˵�
	 */
	private static final JComboBox<Integer> trainIdComboBox = new JComboBox<Integer>();
	private static final JComboBox<String> trainNameComboBox = new JComboBox<String>();
	/**
	 *  ���С��г����ơ�
	 */
	private Integer[] trainCategoryIdArray;
	private String[] trainCategoryNameArray;

	private JPanel brakeLevelPanel = null;

	/**
	 *  ͼ��panel��0������ʾbrakeCharacterPanel��1������ʾbrakePerformancePanel
	 */
	private int chartFlag = 0;
	private ChartPanel brakeCharacterPanel;
	private ChartPanel brakePerformancePanel;
	private static ArrayList<TrainBrakeFactor> brakeFactorList = null;
	private ArrayList<CurveFormulaBean> brakePerformanceDataList = null;
	
	private double M = 0, V= 0;
	private int trainCategoryId = 0;

	/**
	 *  ���桰�����ƶ����͡������ƶ����ļ�����
	 */
	public static ArrayList<Double> electricBrakeList = new ArrayList<Double>();
	
	private ButtonGroup brakeLevelButtonGroup;
	private final JCheckBox emergencyBrakeCheckBox;
	private JTextField re200TimeField;
	private JTextField re200DistanceField;
	private JTextField re200PowerField;
	private JTextField re200AccField;
	private JTextField re300TimeField;
	private JTextField re300DistanceField;
	private JTextField re300PowerField;
	private JTextField re300AccField;
	
	/**
	 * ���ٲ���
	 */
	private double SPEED_200 = 200;
	private double SPEED_300 = 300;
	private TitledBorder panel2TitleBorder = null;
	private JPanel panel_2;
	
	public static boolean [] brakeLevelBoolean;
	public int brakeLevelCount;

	public BrakeConfPanel() {
		setLayout(null);

		getAllTrainCategoryToArray();// ��ʼ�����������������

		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		panel.setBounds(10, 10, 1326, 36);
		add(panel);
		panel.setLayout(null);

		JLabel lblNewLabel = new JLabel(
				"\u5BF9\u5DF2\u5B58\u5728\u7684\u7F16\u7EC4\u8FDB\u884C\u8BBE\u8BA1\uFF1A");
		lblNewLabel.setBounds(10, 10, 144, 15);
		panel.add(lblNewLabel);

		trainNameComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// ����JComboBox����
				int selectedIndex = trainNameComboBox.getSelectedIndex();
				trainIdComboBox.setSelectedIndex(selectedIndex);
			}
		});
		trainNameComboBox.setModel(new DefaultComboBoxModel<String>(
				trainCategoryNameArray));
		trainNameComboBox.setBounds(164, 7, 117, 21);
		panel.add(trainNameComboBox);

		JButton btnNewButton = new JButton("�ƶ���������");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int categoryId = (Integer) trainIdComboBox.getSelectedItem();
				if (categoryId == 0) {
					JOptionPane.showMessageDialog(BrakeConfPanel.this, "��ѡ��������ƣ�");
					return;
				}
				String trainCategory = (String) trainNameComboBox.getSelectedItem();
				BrakeParameterDialog brakeParameterDialog = new BrakeParameterDialog(categoryId, trainCategory);
			}
		});
		btnNewButton.setBounds(428, 6, 105, 23);
		panel.add(btnNewButton);
		
		final JToggleButton tractionButton = new JToggleButton("�ƶ���������", true);
		final JToggleButton performanceButton = new JToggleButton("�ƶ���������");
		tractionButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				tractionButton.setSelected(true);
				performanceButton.setSelected(false);
				remove(brakePerformancePanel);
				add(brakeCharacterPanel);
				refreshBrakeLevelPanelByCheckBox();
				updateUI();
				chartFlag = 0;
			}
		});
		tractionButton.setBounds(10, 56, 135, 23);
		add(tractionButton);

		performanceButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				performanceButton.setSelected(true);
				tractionButton.setSelected(false);
				remove(brakeCharacterPanel);
				add(brakePerformancePanel);
				refreshBrakeLevelPanelByRadioButton();
				drwaBrakePerformanceChart(1);
				updateUI();
				chartFlag = 1;
			}
		});
		performanceButton.setBounds(155, 56, 135, 23);
		add(performanceButton);

		JButton button = new JButton("ȷ��");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int categoryId = (Integer) trainIdComboBox.getSelectedItem();
				trainCategoryId = categoryId;
				if (categoryId == 0) {
					JOptionPane.showMessageDialog(BrakeConfPanel.this,
							"��ѡ��������ƣ�");
				} else {
					electricBrakeCreater(categoryId);
//					emergencyBrakeCreater();
					drawBrakeCharacterChart(categoryId);
					setTrainRunParameters(categoryId);
					refreshBrakeLevelPanelByCheckBox();
					tractionButton.setSelected(true);
					performanceButton.setSelected(false);
					emergencyBrakeCheckBox.setSelected(true);
				}
			}
		});
		button.setBounds(291, 6, 93, 23);
		panel.add(button);

		trainIdComboBox.setBounds(571, 7, 76, 21);
		trainIdComboBox.setModel(new DefaultComboBoxModel<Integer>(
				trainCategoryIdArray));
		panel.add(trainIdComboBox);
		trainIdComboBox.setVisible(false);

		brakeCharacterPanel = new ChartPanel(
				BrakeCharacterChart.createBlankChart("�ƶ���������"));
		brakeCharacterPanel.setPopupMenu(null);
		brakeCharacterPanel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		brakeCharacterPanel.setBounds(10, 114, 1114, 539);
		add(brakeCharacterPanel);

		brakePerformancePanel = new ChartPanel(
				BrakeCharacterChart.createBlankChart("�ƶ���������"));
		brakePerformancePanel.setPopupMenu(null);
		brakePerformancePanel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		brakePerformancePanel.setBounds(10, 114, 1114, 539);

		emergencyBrakeCheckBox = new JCheckBox("�����ƶ�");
		emergencyBrakeCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				BrakeCharacterChart.lineStateChanger((brakeLevelCount-1)+"", emergencyBrakeCheckBox.isSelected());
				//�������ʾbrakePerformancePanel
				if(chartFlag == 1 && emergencyBrakeCheckBox.isSelected()){
					drwaBrakePerformanceChart(10);
					brakeLevelButtonGroup.clearSelection();
				}
			}
		});
		emergencyBrakeCheckBox.setBounds(10, 85, 73, 23);
		add(emergencyBrakeCheckBox);

		brakeLevelPanel = new JPanel();
		brakeLevelPanel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		brakeLevelPanel.setBounds(128, 83, 1208, 27);
		add(brakeLevelPanel);
		brakeLevelPanel.setLayout(new GridLayout(1, 14));
		
		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "\u5217\u8F66\u5236\u52A8\u7279\u6027\u8BA1\u7B97", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_3.setBounds(1134, 114, 201, 324);
		add(panel_3);
		panel_3.setLayout(null);
		
		final JPanel panel_1 = new JPanel();
		final TitledBorder panel1TitledBorder = new TitledBorder(null, "0-200", TitledBorder.LEADING, TitledBorder.TOP, null, null);
		panel_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				if(event.getClickCount() == 2){ //˫��ʱ�����Ի����޸��ٶ�
					String speedValue = JOptionPane.showInputDialog("�������ٶ�ֵ");
					if(speedValue != null && !"".equals(speedValue.trim())){
						try {
							SPEED_200 = Double.parseDouble(speedValue);
							TractionConfPanelService.saveSpeed200(SPEED_200, 0, trainCategoryId);
						} catch (NumberFormatException e) {
							JOptionPane.showMessageDialog(BrakeConfPanel.this, "��������ȷ���ٶ�ֵ��");
							return;
						}
						panel1TitledBorder.setTitle("0-"+(int)SPEED_200);
						panel_1.updateUI();
					}
				}
			}
		});
		panel_1.setBounds(10, 20, 181, 142);
		panel_3.add(panel_1);
		panel_1.setLayout(null);
		panel_1.setBorder(panel1TitledBorder);
		
		JLabel label = new JLabel("\u65F6\u95F4(s)\uFF1A");
		label.setBounds(10, 23, 54, 15);
		panel_1.add(label);
		
		re200TimeField = new JTextField();
		re200TimeField.setEditable(false);
		re200TimeField.setColumns(10);
		re200TimeField.setBounds(66, 20, 105, 21);
		panel_1.add(re200TimeField);
		
		JLabel label_1 = new JLabel("\u8DDD\u79BB(km)\uFF1A");
		label_1.setBounds(10, 51, 68, 15);
		panel_1.add(label_1);
		
		re200DistanceField = new JTextField();
		re200DistanceField.setEditable(false);
		re200DistanceField.setColumns(10);
		re200DistanceField.setBounds(76, 48, 95, 21);
		panel_1.add(re200DistanceField);
		
		JLabel label_2 = new JLabel("\u80FD\u8017(KJ)\uFF1A");
		label_2.setBounds(10, 79, 68, 15);
		panel_1.add(label_2);
		
		re200PowerField = new JTextField();
		re200PowerField.setEditable(false);
		re200PowerField.setColumns(10);
		re200PowerField.setBounds(76, 76, 95, 21);
		panel_1.add(re200PowerField);
		
		re200AccField = new JTextField();
		re200AccField.setEditable(false);
		re200AccField.setColumns(10);
		re200AccField.setBounds(127, 104, 44, 21);
		panel_1.add(re200AccField);
		
		JLabel label_3 = new JLabel("\u5E73\u5747\u52A0\u901F\u5EA6(m/s^2)\uFF1A");
		label_3.setBounds(10, 107, 123, 15);
		panel_1.add(label_3);
		
		panel_2 = new JPanel();
		panel_2.setBounds(10, 172, 181, 142);
		panel_3.add(panel_2);
		panel_2.setLayout(null);
		panel2TitleBorder = new TitledBorder(UIManager.getBorder("TitledBorder.border"), "0-"+(int)SPEED_300, TitledBorder.LEADING, TitledBorder.TOP, null, null);
		panel_2.setBorder(panel2TitleBorder);
		
		JLabel label_4 = new JLabel("\u65F6\u95F4(s)\uFF1A");
		label_4.setBounds(10, 23, 54, 15);
		panel_2.add(label_4);
		
		re300TimeField = new JTextField();
		re300TimeField.setEditable(false);
		re300TimeField.setColumns(10);
		re300TimeField.setBounds(66, 20, 105, 21);
		panel_2.add(re300TimeField);
		
		JLabel label_5 = new JLabel("\u8DDD\u79BB(km)\uFF1A");
		label_5.setBounds(10, 51, 68, 15);
		panel_2.add(label_5);
		
		re300DistanceField = new JTextField();
		re300DistanceField.setEditable(false);
		re300DistanceField.setColumns(10);
		re300DistanceField.setBounds(76, 48, 95, 21);
		panel_2.add(re300DistanceField);
		
		JLabel label_6 = new JLabel("\u80FD\u8017(KJ)\uFF1A");
		label_6.setBounds(10, 79, 68, 15);
		panel_2.add(label_6);
		
		re300PowerField = new JTextField();
		re300PowerField.setEditable(false);
		re300PowerField.setColumns(10);
		re300PowerField.setBounds(76, 76, 95, 21);
		panel_2.add(re300PowerField);
		
		re300AccField = new JTextField();
		re300AccField.setEditable(false);
		re300AccField.setColumns(10);
		re300AccField.setBounds(127, 104, 44, 21);
		panel_2.add(re300AccField);
		
		JLabel label_7 = new JLabel("\u5E73\u5747\u52A0\u901F\u5EA6(m/s^2)\uFF1A");
		label_7.setBounds(10, 107, 123, 15);
		panel_2.add(label_7);

	}
	
	/**
	 * �����г����в����������ı���
	 */
	public void setTrainRunParameters(int trainCategoryId){
		setSPEED_300(trainCategoryId);
		//����
		TrainRunParametersCal trainParametersCal = new TrainRunParametersCal(trainCategoryId);
		double [] rePara200 = trainParametersCal.reverseParameters(SPEED_200);
		double [] rePara300 = trainParametersCal.reverseParameters(SPEED_300);
		//��ֵ
		re200TimeField.setText(MyTools.numFormat2(rePara200[0])+"");
		re200DistanceField.setText(MyTools.numFormat2(rePara200[1])+"");
		re200PowerField.setText(MyTools.numFormat2(rePara200[2])+"");
		re200AccField.setText(MyTools.numFormat2(rePara200[3])+"");
		
		re300TimeField.setText(MyTools.numFormat2(rePara300[0])+"");
		re300DistanceField.setText(MyTools.numFormat2(rePara300[1])+"");
		re300PowerField.setText(MyTools.numFormat2(rePara300[2])+"");
		re300AccField.setText(MyTools.numFormat2(rePara300[3])+"");
	}
	
	/**
	 * ����ǣ����������ٶ�
	 * @param trainCategoryId
	 */
	public void setSPEED_300(int trainCategoryId){
		SPEED_300 = TrainEditPanelService.getTrainInfoByCategoryId(trainCategoryId).getMaxV();//����ٶ�
		panel2TitleBorder.setTitle("0-"+(int)SPEED_300);
		panel_2.updateUI();
	}
	
	/**
	 * 2014.11.30 ��ȡ�ƶ��������ݣ�Ϊ�����ĵ�׼������
	 * @param mode �ƶ���λ
	 * @return
	 */
	public static ArrayList<CurveFormulaBean> getBrakePerformanceData(int mode, int trainCategoryId, double maxV){
		brakeFactorList = BrakeConfPanelService.getBrakeFactor(trainCategoryId);
		ArrayList<CurveFormulaBean> brakePerformanceDataList = new ArrayList<CurveFormulaBean>();
		double a = 0;//m/s^2
		double V0=maxV;//km/h
		double S0 = 0;//km
		int t = 0;
		//��brakePerformanceDataList�������ʼ��
		CurveFormulaBean startBean = new CurveFormulaBean();
		startBean.setA(a);
		startBean.setVt(V0);
		startBean.setSt(S0);
		startBean.setT(t);
		brakePerformanceDataList.add(startBean);
		while(true){
			CurveFormulaBean bean = new CurveFormulaBean();
			// �����ƶ����ٶ�
			a = getAcceleration(mode, V0);
			double Vt = TractionFormulas.Vt(V0, a);
			double St = TractionFormulas.St(S0, Vt, V0, a);
			if(Vt <= 0 ){
				break;
			}
			t++;//ʱ�����
			V0 = Vt;
			S0 = St;
			bean.setA(a);
			bean.setVt(Vt);
			bean.setSt(St);
			bean.setT(t);
			brakePerformanceDataList.add(bean);
		}
		return brakePerformanceDataList;
	}
	
	/**
	 * �����ƶ�����
	 * @param mode
	 */
	public void drwaBrakePerformanceChart(int mode){
		//�������ܱ�
		final BrakePerformanceChart brakePerformanceChart = new BrakePerformanceChart();
		if(brakePerformanceDataList != null){
			brakePerformanceDataList.clear();
		}
		//�������ܼ�����
		brakePerformanceDataList = new ArrayList<CurveFormulaBean>();
		double a = 0;//m/s^2
		double V0=V;//km/h
		double S0 = 0;//km
		int t = 0;
		//��brakePerformanceDataList�������ʼ��
		CurveFormulaBean startBean = new CurveFormulaBean();
		startBean.setA(a);
		startBean.setVt(V0);
		startBean.setSt(S0);
		startBean.setT(t);
		brakePerformanceDataList.add(startBean);
		while(true){
			CurveFormulaBean bean = new CurveFormulaBean();
			// �����ƶ����ٶ�
			a = getAcceleration(mode, V0);
			double Vt = TractionFormulas.Vt(V0, a);
			double St = TractionFormulas.St(S0, Vt, V0, a);
			if(Vt <= 0 ){
				break;
			}
			t++;//ʱ�����
			V0 = Vt;
			S0 = St;
			bean.setA(a);
			bean.setVt(Vt);
			bean.setSt(St);
			bean.setT(t);
			brakePerformanceDataList.add(bean);
		}
		if(brakePerformancePanel != null){
			remove(brakePerformancePanel);
		}
		brakePerformancePanel = brakePerformanceChart.addChartToPanel(brakePerformanceDataList);
		//�Ҽ��˵�
		JPopupMenu popupMenu = new JPopupMenu();
		addPopup(brakePerformancePanel, popupMenu);
		//0.λ���ٶ�����
		final JCheckBoxMenuItem checkBoxMenuItem_0 = new JCheckBoxMenuItem("λ���ٶ�����");
		final JCheckBoxMenuItem checkBoxMenuItem_1 = new JCheckBoxMenuItem("λ��ʱ������");
		final JCheckBoxMenuItem checkBoxMenuItem_3 = new JCheckBoxMenuItem("�ٶ�ʱ������");
		checkBoxMenuItem_0.setSelected(true);
		checkBoxMenuItem_0.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(checkBoxMenuItem_0.isSelected()){
					checkBoxMenuItem_0.setSelected(true);
					checkBoxMenuItem_1.setSelected(false);
					checkBoxMenuItem_3.setSelected(false);
					BrakePerformanceChart.showStVt(brakePerformanceDataList);
				}
			}
		});
		popupMenu.add(checkBoxMenuItem_0);
		//1.λ��ʱ������
		checkBoxMenuItem_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(checkBoxMenuItem_1.isSelected()){
					checkBoxMenuItem_0.setSelected(false);
					checkBoxMenuItem_1.setSelected(true);
					checkBoxMenuItem_3.setSelected(false);
					BrakePerformanceChart.showStT(brakePerformanceDataList);
				}
			}
		});
		popupMenu.add(checkBoxMenuItem_1);
		
		//3.�ٶ�ʱ������
		checkBoxMenuItem_3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(checkBoxMenuItem_3.isSelected()){
					checkBoxMenuItem_0.setSelected(false);
					checkBoxMenuItem_1.setSelected(false);
					checkBoxMenuItem_3.setSelected(true);
					BrakePerformanceChart.showVtT(brakePerformanceDataList);
				}
			}
		});
		popupMenu.add(checkBoxMenuItem_3);
		
		repaint();
		add(brakePerformancePanel);
	}
	
	/**
	 * ��ȡ�ٶȺ��ƶ������ƶ����ٶ�
	 * @param mode
	 * @param v
	 * @return
	 */
	private static double getAcceleration(int mode, double v) {
		int index = findNearestIndex(v);
		double a = 0;
		if (mode == 1) {// 1A
			a = brakeFactorList.get(index).get_1A();
		} else if (mode == 2) {// 1B
			a = brakeFactorList.get(index).get_1B();
		} else if (mode == 3) {// 2
			a = brakeFactorList.get(index).get_2();
		} else if (mode == 4) {
			a = brakeFactorList.get(index).get_3();
		} else if (mode == 5) {
			a = brakeFactorList.get(index).get_4();
		} else if (mode == 6) {
			a = brakeFactorList.get(index).get_5();
		} else if (mode == 7) {
			a = brakeFactorList.get(index).get_6();
		} else if (mode == 8) {
			a = brakeFactorList.get(index).get_7();
		} else if (mode == 9) {
			a = brakeFactorList.get(index).get_8();
		}else if (mode == 10) {
			a = brakeFactorList.get(index).get_9();
		}else if (mode == 11) {
			a = brakeFactorList.get(index).get_10();
		}else if (mode == 12) {
			a = brakeFactorList.get(index).get_11();
		}else if (mode == 13) {
			a = brakeFactorList.get(index).get_12();
		}else if (mode == 14) {
			a = brakeFactorList.get(index).get_13();
		} else if (mode == 15) {
			a = brakeFactorList.get(index).getEb();
		}
		return -a;
	}
	
	/**
	 * �����ٶȼ�������ƶ����ٶ�
	 * @param v
	 * @return
	 */
	private double getEmergencyBrakeAcceleration(double v) {
		double f = 0;//�ƶ���  N
		double a = 0;//�����ƶ����ٶ�
		if(v >=0 && v <= 200){
			f = 600000.0;
		}else if(v > 200 && v <= 204){
			f = 600000.0 - 37500.0 * (v - 200.0);
		}else if(v > 204){
			f = 450000.0;
		}
		a = f/M;
		return a;
	}

	/**
	 *  Ѱ���ٶ���ӽ���brake���±�
	 * @param v
	 * @return
	 */
	private static int findNearestIndex(double v) {
		int index = 0;
		double diff = Integer.MAX_VALUE - v;
		for (int i = 0; i < brakeFactorList.size(); i++) {
			TrainBrakeFactor factor = brakeFactorList.get(i);
			double temp = Math.abs(factor.getSpeed() - v);
			if (temp < diff) {
				index = i;// ��¼i
				diff = temp;
			}
		}
		return index;
	}
	
	/**
	 * ��brakeLevelPanel������ƶ�����ѡ��
	 */
	public void refreshBrakeLevelPanelByRadioButton(){
		brakeLevelPanel.removeAll();
		brakeLevelButtonGroup = new ButtonGroup();
		// 1A
		JRadioButton _1AButton = new JRadioButton("1A");
		_1AButton.setSelected(true);
		_1AButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				emergencyBrakeCheckBox.setSelected(false);
				drwaBrakePerformanceChart(1);
			}
		});
		if(brakeLevelBoolean[0] == true){
			brakeLevelPanel.add(_1AButton);
		}
		brakeLevelButtonGroup.add(_1AButton);
		
		// 1B
		JRadioButton _1BButton = new JRadioButton("1B");
		_1BButton.setSelected(true);
		_1BButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				emergencyBrakeCheckBox.setSelected(false);
				drwaBrakePerformanceChart(2);
			}
		});
		if(brakeLevelBoolean[1] == true){
			brakeLevelPanel.add(_1BButton);
		}
		brakeLevelButtonGroup.add(_1BButton);
		
		// ѭ������������ѡ��ť
		for (int i = 2; i <= 13; i++) {
			final JRadioButton radioButton = new JRadioButton(i + "");
			radioButton.setSelected(true);
			radioButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					emergencyBrakeCheckBox.setSelected(false);
					drwaBrakePerformanceChart(Integer.parseInt(radioButton.getText()) + 1);
				}
			});
			if(brakeLevelBoolean[i] == true){
				brakeLevelPanel.add(radioButton);
			}
			brakeLevelButtonGroup.add(radioButton);
		}

		brakeLevelPanel.updateUI();
	}

	/**
	 *  ��brakeLevelPanel������ƶ�����ѡ��
	 */
	public void refreshBrakeLevelPanelByCheckBox() {
		brakeLevelPanel.removeAll();
		// 1A
		final JCheckBox _1ACheckBox = new JCheckBox("1A");
		_1ACheckBox.setSelected(true);
		_1ACheckBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				BrakeCharacterChart.lineStateChanger("0",
						_1ACheckBox.isSelected());
			}
		});
		if(brakeLevelBoolean[0] == true){
			brakeLevelPanel.add(_1ACheckBox);
		}
		// 1B
		final JCheckBox _1BCheckBox = new JCheckBox("1B");
		_1BCheckBox.setSelected(true);
		_1BCheckBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				BrakeCharacterChart.lineStateChanger("1",
						_1BCheckBox.isSelected());
			}
		});
		if(brakeLevelBoolean[1] == true){
			brakeLevelPanel.add(_1BCheckBox);
		}
		// ѭ������������ѡ��ť
		for (int i = 2; i <= 13; i++) {
			final JCheckBox checkBox = new JCheckBox(i + "");
			checkBox.setSelected(true);
			checkBox.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					BrakeCharacterChart.lineStateChanger(checkBox.getText(),
							checkBox.isSelected());
				}
			});
			if(brakeLevelBoolean[i] == true){
				brakeLevelPanel.add(checkBox);
			}
		}
		brakeLevelPanel.updateUI();
	}

	/**
	 *  �����ݱ�train_category��������г����ƣ�����ֵ
	 */
	public void getAllTrainCategoryToArray() {
		ArrayList<TrainCategory> tc = TrainEditPanelService
				.getAllTrainCategory();
		trainCategoryIdArray = new Integer[tc.size() + 1];
		trainCategoryNameArray = new String[tc.size() + 1];
		trainCategoryIdArray[0] = 0;
		trainCategoryNameArray[0] = "��ѡ���������";
		for (int i = 0; i < tc.size(); i++) {
			trainCategoryIdArray[i + 1] = tc.get(i).getId();
			trainCategoryNameArray[i + 1] = tc.get(i).getName();
		}
	}

	/**
	 *  ͨ��trainCategoryId��ȡ���ݣ��������ƶ�����
	 * @param trainCategoryId
	 */
	public void drawBrakeCharacterChart(int trainCategoryId) {
		final BrakeCharacterChart brakeCharacterChart = new BrakeCharacterChart();
		if (brakeFactorList != null) {
			brakeFactorList.clear();
		}
		brakeFactorList = BrakeConfPanelService.getBrakeFactor(trainCategoryId);
		brakeLevelBoolean = BrakeConfPanelService.getBrakeLevelBoolean(brakeFactorList);
		brakeLevelCount = brakeLevelInFact();
		if (brakeCharacterPanel != null) {
			remove(brakeCharacterPanel);
		}
		double parameters [] = TractionConfPanelService.getSomeParametersFromTrainEdit(trainCategoryId);
		//��������
		M = (parameters[4]) * 1000;//tת��kg
		V = parameters[5]; //MaxV
		brakeCharacterPanel = brakeCharacterChart.addChartToPanel(brakeFactorList, electricBrakeList, M);
		// �Ҽ��˵�
		JPopupMenu popupMenu = new JPopupMenu();
		addPopup(brakeCharacterPanel, popupMenu);

		// 0.�г��ṩ�ƶ�������
		final JCheckBoxMenuItem checkBoxMenuItem_0 = new JCheckBoxMenuItem(
				"�г��ƶ�������");
		checkBoxMenuItem_0.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				for(int i=0;i<brakeLevelCount;i++){
					BrakeCharacterChart.lineStateChanger(i+"", checkBoxMenuItem_0.isSelected());
				}
			}
		});
		checkBoxMenuItem_0.setSelected(true);
		popupMenu.add(checkBoxMenuItem_0);
		// 1.�����ƶ�������
		final JCheckBoxMenuItem checkBoxMenuItem_1 = new JCheckBoxMenuItem(
				"�����ƶ�����");
		checkBoxMenuItem_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				BrakeCharacterChart.lineStateChanger(brakeLevelCount+"", checkBoxMenuItem_1.isSelected());
			}
		});
		checkBoxMenuItem_1.setSelected(true);
		popupMenu.add(checkBoxMenuItem_1);

		// ˢ��chartPanel
		repaint();
		add(brakeCharacterPanel);

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

	/**
	 *  �������ƶ������㹫ʽ
	 * @param trainCategoryId
	 */
	public static void electricBrakeCreater(int trainCategoryId) {
		electricBrakeList.clear();
		String[] parameters = BrakeConfPanelService
				.getFiveParameters(trainCategoryId);// ���N,k1,k2,T,D,F2
		double Fst = 2 * Double.parseDouble(parameters[3])
				* Double.parseDouble(parameters[0])
				* Double.parseDouble(parameters[1])
				* Double.parseDouble(parameters[4]) / 1000
				/ Double.parseDouble(parameters[2]);// KN
		TrainElectricBrake trainElectricBrake = BrakeConfPanelService.getTrainElectricBrake(trainCategoryId);
		double v1 = trainElectricBrake.getV1();
		double v2 = trainElectricBrake.getV2();
		//2014.12.14������ƶ������޸�
		int N = TractionConfPanelService.getSumDynamicAxleNum(trainCategoryId);
		double P00 = trainElectricBrake.getP00();
		double P11 = N * P00;
		double F11 = (P11 * 3.6)/v2;
		double B = 0;//��λ��KN
		for(int v=0;v<=300;v+=2){
			if(v>=0 && v<=v1){
				B = (-Fst/v1)*v;
			}else if(v>v1 && v<=v2){
				B = -Fst+((Fst-F11)/(v2-v1))*(v-v1);
			}else if(v>v2){
				B = (-(Fst-( Fst-F11))*v2)/v;
			}
			electricBrakeList.add(Math.abs(B));
		}

	}
	
	/**
	 * ͳ��ʵ���ж��ٸ��ƶ���λ
	 * @return
	 */
	public int brakeLevelInFact(){
		int sum = 0;
		for(int i=0;i<brakeLevelBoolean.length;i++){
			if(brakeLevelBoolean[i] == true){
				sum += 1;
			}
		}
		return sum;
	}

}

/**
 *  ���� �ƶ����� ͼ��
 * @author huhui
 *
 */
class BrakeCharacterChart {

	private static XYSplineRenderer xySplineRenderer = null;
	private XYPlot xyPlot = null;
	public static XYSeriesCollection xyseriescollection = null;

	/**
	 *  ��chartPanel��ӵ�TractionConfChartPanel��
	 * @param dataList
	 * @param electricBrakeList
	 * @param M
	 * @return
	 */
	public ChartPanel addChartToPanel(ArrayList<TrainBrakeFactor> dataList, ArrayList<Double> electricBrakeList, double M) {
		XYDataset dataset = createDataset(dataList, electricBrakeList, M);
		JFreeChart chart = createChart(dataset);
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setBounds(10, 114, 1114, 539);
		chartPanel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		chartPanel.setPopupMenu(null);
		chartPanel.setMouseWheelEnabled(true);// ���ַŴ�
		return chartPanel;
	}

	/**
	 * �������ݼ�
	 * @param chartDataList
	 * @param electricBrakeList
	 * @param M
	 * @return
	 */
	public XYDataset createDataset(ArrayList<TrainBrakeFactor> chartDataList, ArrayList<Double> electricBrakeList, double M) {
		xyseriescollection = new XYSeriesCollection();
		XYSeries _1ASeries = new XYSeries("1A");
		XYSeries _1BSeries = new XYSeries("1B");
		XYSeries _2Series = new XYSeries("2");
		XYSeries _3Series = new XYSeries("3");
		XYSeries _4Series = new XYSeries("4");
		XYSeries _5Series = new XYSeries("5");
		XYSeries _6Series = new XYSeries("6");
		XYSeries _7Series = new XYSeries("7");
		XYSeries _8Series = new XYSeries("8");
		XYSeries _9Series = new XYSeries("9");
		XYSeries _10Series = new XYSeries("10");
		XYSeries _11Series = new XYSeries("11");
		XYSeries _12Series = new XYSeries("12");
		XYSeries _13Series = new XYSeries("13");
		XYSeries electricBrakeSeries = new XYSeries("�����ƶ�");
		XYSeries emergencyBrakeSeries = new XYSeries("�����ƶ�");

		for(int i=0;i<chartDataList.size();i++){
			TrainBrakeFactor factor = chartDataList.get(i);
			_1ASeries.add(factor.getSpeed(), (factor.get_1A() * M)/1000);
			_1BSeries.add(factor.getSpeed(), (factor.get_1B() * M)/1000);
			_2Series.add(factor.getSpeed(), (factor.get_2() * M)/1000);
			_3Series.add(factor.getSpeed(), (factor.get_3() * M)/1000);
			_4Series.add(factor.getSpeed(), (factor.get_4() * M)/1000);
			_5Series.add(factor.getSpeed(), (factor.get_5() * M)/1000);
			_6Series.add(factor.getSpeed(), (factor.get_6() * M)/1000);
			_7Series.add(factor.getSpeed(), (factor.get_7() * M)/1000);
			_8Series.add(factor.getSpeed(), (factor.get_8() * M)/1000);
			_9Series.add(factor.getSpeed(), (factor.get_9() * M)/1000);
			_10Series.add(factor.getSpeed(), (factor.get_10() * M)/1000);
			_11Series.add(factor.getSpeed(), (factor.get_11() * M)/1000);
			_12Series.add(factor.getSpeed(), (factor.get_12() * M)/1000);
			_13Series.add(factor.getSpeed(), (factor.get_13() * M)/1000);
			electricBrakeSeries.add(factor.getSpeed(), electricBrakeList.get(i));
			emergencyBrakeSeries.add(factor.getSpeed(), (factor.getEb() * M)/1000);
		}

		boolean [] brakeLevelBoolean = BrakeConfPanel.brakeLevelBoolean;
		if(brakeLevelBoolean[0] == true){
			xyseriescollection.addSeries(_1ASeries);
		}
		if(brakeLevelBoolean[1] == true){
			xyseriescollection.addSeries(_1BSeries);
		}
		if(brakeLevelBoolean[2] == true){
			xyseriescollection.addSeries(_2Series);
		}
		if(brakeLevelBoolean[3] == true){
			xyseriescollection.addSeries(_3Series);
		}
		if(brakeLevelBoolean[4] == true){
			xyseriescollection.addSeries(_4Series);
		}
		if(brakeLevelBoolean[5] == true){
			xyseriescollection.addSeries(_5Series);
		}
		if(brakeLevelBoolean[6] == true){
			xyseriescollection.addSeries(_6Series);
		}
		if(brakeLevelBoolean[7] == true){
			xyseriescollection.addSeries(_7Series);
		}
		if(brakeLevelBoolean[8] == true){
			xyseriescollection.addSeries(_8Series);
		}
		if(brakeLevelBoolean[9] == true){
			xyseriescollection.addSeries(_9Series);
		}
		if(brakeLevelBoolean[10] == true){
			xyseriescollection.addSeries(_10Series);
		}
		if(brakeLevelBoolean[11] == true){
			xyseriescollection.addSeries(_11Series);
		}
		if(brakeLevelBoolean[12] == true){
			xyseriescollection.addSeries(_12Series);
		}
		if(brakeLevelBoolean[13] == true){
			xyseriescollection.addSeries(_13Series);
		}
		if(brakeLevelBoolean[14] == true){
			xyseriescollection.addSeries(emergencyBrakeSeries);
		}
		
		xyseriescollection.addSeries(electricBrakeSeries);

		return xyseriescollection;
	}

	public JFreeChart createChart(XYDataset dataset) {
		NumberAxis xNum = new NumberAxis("�ٶ�(km/h)");// X��
		xNum.setAutoRangeStickyZero(true);
		NumberAxis yNum = new NumberAxis("�ƶ���(KN)");// Y��
		yNum.setAutoRangeStickyZero(true);
		xySplineRenderer = new XYSplineRenderer();
		xySplineRenderer.setSeriesStroke(0, new BasicStroke(1.8F));// �������ߴ�ϸ
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
		xySplineRenderer.setSeriesStroke(15, new BasicStroke(1.8F));
		xySplineRenderer.setBaseShapesVisible(false);// ���������ϵ�С��
		xyPlot = new XYPlot(dataset, xNum, yNum, xySplineRenderer);
		xyPlot.setBackgroundPaint(Color.white);
		// ����������ɫ
		xyPlot.setDomainGridlinePaint(Color.black);
		xyPlot.setRangeGridlinePaint(Color.black);
		xyPlot.setAxisOffset(new RectangleInsets(4D, 4D, 4D, 4D));

		JFreeChart jFreeChart = new JFreeChart("�ƶ�����",
				JFreeChart.DEFAULT_TITLE_FONT, xyPlot, true);
		return jFreeChart;
	}

	/**
	 *  ���������Ƿ���ʾ
	 * @param checkBoxText
	 * @param visible
	 */
	public static void lineStateChanger(String checkBoxText, boolean visible) {
		xySplineRenderer.setSeriesVisible(Integer.parseInt(checkBoxText),
				visible);
	}

	/**
	 *  ����һ�ſձ�
	 * @param title
	 * @return
	 */
	public static JFreeChart createBlankChart(String title) {
		StandardChartTheme standardChartTheme = new StandardChartTheme("CN");
		standardChartTheme.setExtraLargeFont(new Font("����", Font.BOLD, 16));
		ChartFactory.setChartTheme(standardChartTheme);
		JFreeChart blankChart = ChartFactory.createXYLineChart(title,
				"Speed(km/h)", "", null, PlotOrientation.VERTICAL, true, true,
				false);
		XYPlot plot = blankChart.getXYPlot();
		plot.setBackgroundPaint(Color.WHITE);
		plot.setDomainGridlinePaint(Color.black);
		plot.setRangeGridlinePaint(Color.black);
		return blankChart;
	}

}

/**
 * ���� �ƶ��������� ͼ��
 * @author huhui
 *
 */
class BrakePerformanceChart {
	
	private static XYSplineRenderer xySplineRenderer = null;
	private XYPlot xyPlot = null;
	public static XYSeriesCollection xyseriescollection = null;
	private static NumberAxis xNum;
	private static NumberAxis yNum;
	
	/**
	 * ��chartPanel��ӵ�LaunchPerformanceConfChart��
	 * @param dataList
	 * @return
	 */
	public ChartPanel addChartToPanel(ArrayList<CurveFormulaBean> dataList){
		XYDataset dataset = createDataset(dataList);
		JFreeChart chart = createChart(dataset);
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setBounds(10, 114, 1114, 539);
		chartPanel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		chartPanel.setPopupMenu(null);
		chartPanel.setMouseWheelEnabled(true);//���ַŴ�
		return chartPanel;
	}

	/**
	 * �������ݼ�
	 * @param chartDataList
	 * @return
	 */
	public XYDataset createDataset(ArrayList<CurveFormulaBean> chartDataList) {
		xyseriescollection = new XYSeriesCollection();
		XYSeries VtStSeries = new XYSeries("λ��-�ٶ�");
		for (int i=0;i<chartDataList.size();i=i+2) {
			CurveFormulaBean bean = chartDataList.get(i);
			VtStSeries.add(bean.getSt(), bean.getVt());
		}
		xyseriescollection.addSeries(VtStSeries);
		return xyseriescollection;
	}
	
	/**
	 * ���á�λ��-�ٶȡ��������ݼ�
	 * @param chartDataList
	 */
	public static void showStVt(ArrayList<CurveFormulaBean> chartDataList){
		xNum.setLabel("λ��(km)");
		yNum.setLabel("�ٶ�(km/h)");
		xyseriescollection.removeAllSeries();
		XYSeries VtStSeries = new XYSeries("λ��-�ٶ�");
		for (int i=0;i<chartDataList.size();i=i+2) {
			CurveFormulaBean bean = chartDataList.get(i);
			VtStSeries.add(bean.getSt(), bean.getVt());
		}
		xyseriescollection.addSeries(VtStSeries);
	}
	
	/**
	 * ���á�λ��-ʱ�䡱�������ݼ�
	 * @param chartDataList
	 */
	public static void showStT(ArrayList<CurveFormulaBean> chartDataList){
		xNum.setLabel("ʱ��(s)");
		yNum.setLabel("λ��(km)");
		xyseriescollection.removeAllSeries();
		XYSeries StTSeries = new XYSeries("λ��-ʱ��");
		for (int i=0;i<chartDataList.size();i=i+2) {
			CurveFormulaBean bean = chartDataList.get(i);
			StTSeries.add(bean.getT(), bean.getSt());
		}
		xyseriescollection.addSeries(StTSeries);
	}
	
	/**
	 * ���á��ٶ�-ʱ�䡱�������ݼ�
	 * @param chartDataList
	 */
	public static void showVtT(ArrayList<CurveFormulaBean> chartDataList){
		xNum.setLabel("ʱ��(s)");
		yNum.setLabel("�ٶ�(km/h)");
		xyseriescollection.removeAllSeries();
		XYSeries VtTSeries = new XYSeries("�ٶ�-ʱ��");
		for (int i=0;i<chartDataList.size();i=i+2) {
			CurveFormulaBean bean = chartDataList.get(i);
			VtTSeries.add(bean.getT(), bean.getVt());
		}
		xyseriescollection.addSeries(VtTSeries);
	}
	
	public JFreeChart createChart(XYDataset dataset) {
		xNum = new NumberAxis("λ��(km)");//X��
		xNum.setAutoRangeStickyZero(true);
		yNum = new NumberAxis("�ٶ�(km/h)");//Y��
		yNum.setAutoRangeStickyZero(true);
		xySplineRenderer = new XYSplineRenderer();
		xySplineRenderer.setSeriesStroke(0, new BasicStroke(1.8F));//�������ߴ�ϸ
		xySplineRenderer.setSeriesStroke(1, new BasicStroke(1.8F));
		xySplineRenderer.setSeriesStroke(2, new BasicStroke(1.8F));
		xySplineRenderer.setSeriesStroke(3, new BasicStroke(1.8F));
		xySplineRenderer.setSeriesStroke(4, new BasicStroke(1.8F));
		xySplineRenderer.setSeriesStroke(5, new BasicStroke(1.8F));
		xySplineRenderer.setSeriesStroke(6, new BasicStroke(1.8F));
		xySplineRenderer.setBaseShapesVisible(false);//���������ϵ�С��
		xyPlot = new XYPlot(dataset, xNum, yNum, xySplineRenderer);
		xyPlot.setBackgroundPaint(Color.white);
		//����������ɫ
		xyPlot.setDomainGridlinePaint(Color.black);
		xyPlot.setRangeGridlinePaint(Color.black);
		xyPlot.setAxisOffset(new RectangleInsets(4D, 4D, 4D, 4D));
		
		JFreeChart jFreeChart = new JFreeChart("�ƶ��������", JFreeChart.DEFAULT_TITLE_FONT, xyPlot, true);
		return jFreeChart;
	}
	
}
