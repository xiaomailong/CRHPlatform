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
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.annotations.XYPointerAnnotation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.TextAnchor;

import com.crh.calculation.mintime.TrainRunParametersCal;
import com.crh.service.TractionConfPanelService;
import com.crh.service.TrainEditPanelService;
import com.crh.view.dialog.TractionParameterDialog;
import com.crh2.calculate.TractionForce;
import com.crh2.calculate.TractionFormulas;
import com.crh2.javabean.CurveFormulaBean;
import com.crh2.javabean.TrainCategory;
import com.crh2.javabean.TrainTractionConf;
import com.crh2.javabean.TrainTractionConfType;
import com.crh2.util.MyTools;

/**
 * ǣ���������ã��޼���
 * @author huhui
 *
 */
public class TractionConfPanel extends JPanel {
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	
	/**
	 * ȫ��train_category_id
	 */
	private int trainCategoryId = 0;
	
	/**
	 *  ���������˵�
	 */
	private static final JComboBox<Integer> trainIdComboBox = new JComboBox<Integer>();
	private static final JComboBox<String> trainNameComboBox = new JComboBox<String>();
	/**
	 * ���С��г����ơ�
	 */
	private Integer [] trainCategoryIdArray;
	private String[] trainCategoryNameArray; 
	
	/**
	 * ͼ��Panel
	 */
	private ChartPanel tractionChartPanel = null;
	private ChartPanel launchChartPanel = null;
	private ArrayList<CurveFormulaBean> chartDataList = null;
	private ArrayList<CurveFormulaBean> launchDataList = null;
	public static double V = 0;
	/**
	 * �µ�ǧ�ֱ�
	 */
	private double i = 0;
	
	private static JPanel tractionTypePanel;
	private static ButtonGroup typeButtonGroup;
	public static JButton refresh;
	private JTextField fw200TimeField;
	private JTextField fw200DistanceField;
	private JTextField fw200PowerField;
	private JTextField fw200AccField;
	private JTextField fw300TimeField;
	private JTextField fw300DistanceField;
	private JTextField fw300PowerField;
	private JTextField fw300AccField;
	
	/**
	 * ���ٲ���
	 */
	private double SPEED_200 = 200;
	private double SPEED_300 = 300;
	private TitledBorder panel2TitleBorder = null;
	
	/**
	 * ����ѡ��
	 */
	private JComboBox<Integer> tractionTypeComboBox = new JComboBox<Integer>();
	private Integer [] tractionTypeArray;
	private JPanel panel_2;
	private JTextField fw200RAccField;
	private JTextField fw300RAccField;
		
	public TractionConfPanel() {
		setLayout(null);
		
		getAllTrainCategoryToArray();//��ʼ�����������������
		
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
				// ����JComboBox����
				int selectedIndex = trainNameComboBox.getSelectedIndex();
				trainIdComboBox.setSelectedIndex(selectedIndex);
			}
		});
		trainNameComboBox.setModel(new DefaultComboBoxModel<String>(trainCategoryNameArray));
		trainNameComboBox.setBounds(164, 7, 117, 21);
		panel.add(trainNameComboBox);
		
		JButton btnNewButton = new JButton("\u7275\u5F15\u7279\u6027\u914D\u7F6E");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int categoryId = (Integer) trainIdComboBox.getSelectedItem();
				if(categoryId == 0){
					JOptionPane.showMessageDialog(TractionConfPanel.this, "��ѡ��������ƣ�");
					return;
				}
				int trainInfoCount = TrainEditPanelService.getTrainInfoCount(categoryId);
				if(trainInfoCount == 0){
					JOptionPane.showMessageDialog(TractionConfPanel.this, "�ó����г�������Ϣ������δ���棬�뷵�ء��г����ݡ����棡");
					return;
				}
				String trainCategory = (String) trainNameComboBox.getSelectedItem();
				int tractionId = 0;
				try {
					tractionId = getSelectedTractionId();
				} catch (NullPointerException e1) {
				}
				int selectedType = getSelectedButtonText();
				TractionParameterDialog tractionParameterDialog = new TractionParameterDialog(TractionConfPanel.this, categoryId, tractionId, trainCategory, selectedType);
			}
		});
		btnNewButton.setBounds(428, 6, 105, 23);
		panel.add(btnNewButton);
		
		final JToggleButton tractionButton = new JToggleButton("ǣ����������");
		final JToggleButton performanceButton = new JToggleButton("������������");
		tractionButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				tractionButton.setSelected(true);
				performanceButton.setSelected(false);
				remove(launchChartPanel);
				add(tractionChartPanel);
				updateUI();
			}
		});
		tractionButton.setBounds(10, 102, 135, 23);
		add(tractionButton);
		
		performanceButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(launchChartPanel == null){
					launchPerformanceCalculator();
				}
				performanceButton.setSelected(true);
				tractionButton.setSelected(false);
				remove(tractionChartPanel);
				add(launchChartPanel);
				updateUI();
			}
		});
		performanceButton.setBounds(155, 102, 135, 23);
		add(performanceButton);
		
		JButton button = new JButton("ȷ��");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int categoryId = (Integer) trainIdComboBox.getSelectedItem();
				trainCategoryId = categoryId;
				if(categoryId == 0){
					JOptionPane.showMessageDialog(TractionConfPanel.this, "��ѡ��������ƣ�");
				}else{
					int firstTractionId = TractionConfPanelService.getFirstTractionId(categoryId);
					assignParametersToTractionFormula(firstTractionId, categoryId);//Ĭ��ѡ�е�һ�����
					setTrainRunParameters(categoryId);
					launchChartPanel = null;
					tractionButton.setSelected(true);
					performanceButton.setSelected(false);
					refresh.doClick();//��ʾ�������
				}
			}
		});
		button.setBounds(291, 6, 93, 23);
		panel.add(button);
		
		trainIdComboBox.setBounds(571, 7, 76, 21);
		trainIdComboBox.setModel(new DefaultComboBoxModel<Integer>(trainCategoryIdArray));
		panel.add(trainIdComboBox);
		trainIdComboBox.setVisible(false);
		
		tractionTypeComboBox.setModel(new DefaultComboBoxModel<Integer>(tractionTypeArray));
		tractionTypeComboBox.setBounds(833, 7, 70, 21);
		panel.add(tractionTypeComboBox);
		
		JButton button_4 = new JButton("��������");
		button_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(tractionTypeComboBox.getItemCount() != 0){
					int tractionType = (Integer) tractionTypeComboBox.getSelectedItem();
					TractionConfPanelService.updateTrainTractionConfType(tractionType, trainCategoryId);
					JOptionPane.showMessageDialog(TractionConfPanel.this, "ǣ����������ɹ���");
				}
			}
		});
		button_4.setBounds(913, 6, 81, 23);
		panel.add(button_4);
//		trainIdComboBox.setVisible(false);
		
		tractionTypePanel = new JPanel();
		tractionTypePanel.setLayout(new GridLayout(1, 15));
		tractionTypePanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		tractionTypePanel.setBounds(10, 56, 1114, 36);
		add(tractionTypePanel);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new LineBorder(Color.LIGHT_GRAY));
		panel_3.setBounds(1134, 56, 202, 597);
		add(panel_3);
		panel_3.setLayout(null);
		
		JPanel panel_5 = new JPanel();
		panel_5.setBorder(new TitledBorder(null, "\u7EB5\u5750\u6807\u67E5\u8BE2", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_5.setBounds(10, 5, 182, 72);
		panel_3.add(panel_5);
		panel_5.setLayout(null);
		
		JLabel label_1 = new JLabel("\u9009\u62E9\u66F2\u7EBF\uFF1A");
		label_1.setBounds(10, 22, 60, 15);
		panel_5.add(label_1);
		
		final JComboBox comboBox_1 = new JComboBox();
		comboBox_1.setFont(new Font("����", Font.PLAIN, 12));
		comboBox_1.setModel(new DefaultComboBoxModel(new String[] {"\u8BF7\u9009\u62E9\u66F2\u7EBF", "\u7275\u5F15\u7279\u6027\u66F2\u7EBF", "\u963B\u529B\u7279\u6027\u66F2\u7EBF", "\u7535\u6D41\u66F2\u7EBF", "\u7535\u538B\u66F2\u7EBF", "\u7275\u5F15\u7C98\u7740\u66F2\u7EBF(\u5E72\u8F68)", "\u7275\u5F15\u7C98\u7740\u66F2\u7EBF(\u6E7F\u8F68)"}));
		comboBox_1.setBounds(65, 19, 107, 21);
		panel_5.add(comboBox_1);
		
		JLabel label_2 = new JLabel("\u901F\u5EA6=");
		label_2.setBounds(10, 47, 54, 15);
		panel_5.add(label_2);
		
		textField = new JTextField();
		textField.setBounds(42, 44, 39, 21);
		panel_5.add(textField);
		textField.setColumns(10);
		
		JLabel lblKmh = new JLabel("km/h");
		lblKmh.setBounds(85, 47, 54, 15);
		panel_5.add(lblKmh);
		
		JButton btnNewButton_1 = new JButton("ȷ��");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(trainCategoryId == 0){
					JOptionPane.showMessageDialog(TractionConfPanel.this, "��ѡ���г��������ƣ�");
					return;
				}else{
					int index = comboBox_1.getSelectedIndex();
					if(index == 0){
						JOptionPane.showMessageDialog(TractionConfPanel.this, "��ѡ���������ƣ�");
						return;
					}
					double x = 0;
					try {
						x = Double.parseDouble(textField.getText().trim());
						JOptionPane.showMessageDialog(TractionConfPanel.this, "y = "+calculateY(index, x));
					} catch (NumberFormatException e) {
						JOptionPane.showMessageDialog(TractionConfPanel.this, "�����ٶ�ֵ���Ϸ������������룡");
						return;
					}
				}
			}
		});
		btnNewButton_1.setBounds(112, 43, 60, 23);
		panel_5.add(btnNewButton_1);
		
		JPanel panel_6 = new JPanel();
		panel_6.setBorder(new TitledBorder(null, "\u66F2\u7EBF\u8BBE\u7F6E", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_6.setBounds(10, 80, 182, 50);
		panel_3.add(panel_6);
		panel_6.setLayout(null);
		
		final JCheckBox checkBox = new JCheckBox("��ʾ�ؼ���");
		checkBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(trainCategoryId == 0){
					JOptionPane.showMessageDialog(TractionConfPanel.this, "��ѡ���г��������ƣ�");
					checkBox.setSelected(false);
					return;
				}
				if(checkBox.isSelected()){
					TractionConfChart.displayCriticalPoints();
				}else{
					TractionConfChart.cancelDisplayCriticalPoints();
				}
			}
		});
		checkBox.setBounds(17, 20, 103, 23);
		panel_6.add(checkBox);
		
		JPanel panel_7 = new JPanel();
		panel_7.setLayout(null);
		panel_7.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "\u5761\u9053\u66F2\u7EBF", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_7.setBounds(10, 128, 182, 81);
		panel_3.add(panel_7);
		
		JLabel label_3 = new JLabel("\u5761\u9053\u5343\u5206\u6BD4\uFF1A");
		label_3.setBounds(10, 22, 72, 15);
		panel_7.add(label_3);
		
		textField_1 = new JTextField();
		textField_1.setBounds(80, 19, 92, 21);
		panel_7.add(textField_1);
		textField_1.setColumns(10);
		
		JButton button_1 = new JButton("�µ�����");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (trainCategoryId == 0) {
					JOptionPane.showMessageDialog(TractionConfPanel.this,
							"��ѡ���г��������ƣ�");
					return;
				}
				try {
					i = Double.parseDouble(textField_1.getText().trim());
				} catch (NumberFormatException e1) {
					JOptionPane.showMessageDialog(TractionConfPanel.this,
							"�����µ�ǧ�ֱ���ֵ���Ϸ�");
					return;
				}
				if (TractionConfChart.xyseriescollection.getSeriesCount() == 7) {
					TractionConfChart.xyseriescollection.removeSeries(6);// �����ԭ���µ�����
				}
				// ��ʾ�µ�����
				calculateSlopeChartData();
				TractionConfChart.setSlopeDataset(chartDataList);
			}
		});
		button_1.setBounds(10, 47, 80, 23);
		panel_7.add(button_1);
		
		JButton button_2 = new JButton("ȡ����ʾ");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(trainCategoryId == 0){
					JOptionPane.showMessageDialog(TractionConfPanel.this, "��ѡ���г��������ƣ�");
					return;
				}
				if(TractionConfChart.xyseriescollection.getSeriesCount() == 7){
					TractionConfChart.xyseriescollection.removeSeries(6);//�����ԭ���µ�����
				}
			}
		});
		button_2.setBounds(92, 47, 80, 23);
		panel_7.add(button_2);
		
		JPanel panel_8 = new JPanel();
		panel_8.setLayout(null);
		panel_8.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "\u66F2\u7EBF\u4FDD\u5B58", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_8.setBounds(10, 208, 182, 47);
		panel_3.add(panel_8);
		
		JLabel label_4 = new JLabel("\u6B65\u957F\uFF1A");
		label_4.setBounds(10, 22, 72, 15);
		panel_8.add(label_4);
		
		textField_2 = new JTextField();
		textField_2.setColumns(10);
		textField_2.setBounds(44, 19, 39, 21);
		panel_8.add(textField_2);
		
		JButton button_3 = new JButton("����");
		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(trainCategoryId == 0){
					JOptionPane.showMessageDialog(TractionConfPanel.this, "��ѡ���г��������ƣ�");
					return;
				}
				int interval = 0;
				try {
					interval = Integer.parseInt(textField_2.getText().trim());
				} catch (NumberFormatException e1) {
					JOptionPane.showMessageDialog(TractionConfPanel.this, "���벽����Ч��");
					return;
				}
				try {
					JFileChooser excelFileChooser = new JFileChooser();
					FileNameExtensionFilter fileFilter = new FileNameExtensionFilter(".xls", "xls");
					excelFileChooser.setFileFilter(fileFilter);
					excelFileChooser.setFileSelectionMode(JFileChooser.SAVE_DIALOG);
					excelFileChooser.setVisible(true);//��ʾ
					int retval = excelFileChooser.showSaveDialog(TractionConfPanel.this);
					String filePath = "";
					if (retval == JFileChooser.APPROVE_OPTION) {
						// �õ��û�ѡ���ļ��ľ���·��
						filePath = excelFileChooser.getSelectedFile().getAbsolutePath() + ".xls";
						saveLinesDate(filePath, interval);
						JOptionPane.showMessageDialog(TractionConfPanel.this, "���߱���ɹ���");
					}
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(TractionConfPanel.this, "���߱���ʧ�ܣ�");
					return;
				}
			}
		});
		button_3.setBounds(117, 18, 60, 23);
		panel_8.add(button_3);
		
		JLabel label_5 = new JLabel("km/h");
		label_5.setBounds(88, 22, 54, 15);
		panel_8.add(label_5);
		
		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new TitledBorder(null, "\u5217\u8F66\u7275\u5F15\u7279\u6027\u8BA1\u7B97", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_4.setBounds(10, 258, 182, 333);
		panel_3.add(panel_4);
		panel_4.setLayout(null);
		
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
							TractionConfPanelService.saveSpeed200(SPEED_200, 1, trainCategoryId);
						} catch (NumberFormatException e) {
							JOptionPane.showMessageDialog(TractionConfPanel.this, "��������ȷ���ٶ�ֵ��");
							return;
						}
						panel1TitledBorder.setTitle("0-"+(int)SPEED_200);
						panel_1.updateUI();
					}
				}
			}
		});
		panel_1.setBounds(10, 15, 162, 156);
		panel_4.add(panel_1);
		panel_1.setLayout(null);
		panel_1.setBorder(panel1TitledBorder);
		
		JLabel label = new JLabel("\u65F6\u95F4(s)\uFF1A");
		label.setBounds(10, 23, 54, 15);
		panel_1.add(label);
		
		fw200TimeField = new JTextField();
		fw200TimeField.setEditable(false);
		fw200TimeField.setColumns(10);
		fw200TimeField.setBounds(66, 20, 86, 21);
		panel_1.add(fw200TimeField);
		
		JLabel label_6 = new JLabel("\u8DDD\u79BB(km)\uFF1A");
		label_6.setBounds(10, 51, 68, 15);
		panel_1.add(label_6);
		
		fw200DistanceField = new JTextField();
		fw200DistanceField.setEditable(false);
		fw200DistanceField.setColumns(10);
		fw200DistanceField.setBounds(76, 48, 76, 21);
		panel_1.add(fw200DistanceField);
		
		JLabel label_7 = new JLabel("\u80FD\u8017(KJ)\uFF1A");
		label_7.setBounds(10, 79, 68, 15);
		panel_1.add(label_7);
		
		fw200PowerField = new JTextField();
		fw200PowerField.setEditable(false);
		fw200PowerField.setColumns(10);
		fw200PowerField.setBounds(76, 76, 76, 21);
		panel_1.add(fw200PowerField);
		
		fw200AccField = new JTextField();
		fw200AccField.setEditable(false);
		fw200AccField.setColumns(10);
		fw200AccField.setBounds(120, 104, 32, 21);
		panel_1.add(fw200AccField);
		
		JLabel label_8 = new JLabel("\u5E73\u5747\u52A0\u901F\u5EA6(m/s^2)\uFF1A");
		label_8.setBounds(10, 107, 123, 15);
		panel_1.add(label_8);
		
		JLabel lblms = new JLabel("\u5269\u4F59\u52A0\u901F\u5EA6(m/s^2)\uFF1A");
		lblms.setBounds(10, 133, 123, 15);
		panel_1.add(lblms);
		
		fw200RAccField = new JTextField();
		fw200RAccField.setEditable(false);
		fw200RAccField.setColumns(10);
		fw200RAccField.setBounds(120, 130, 32, 21);
		panel_1.add(fw200RAccField);
		
		panel_2 = new JPanel();
		panel_2.setBounds(10, 170, 162, 156);
		panel_4.add(panel_2);
		panel_2.setLayout(null);
		panel2TitleBorder = new TitledBorder(UIManager.getBorder("TitledBorder.border"), "0-"+(int)SPEED_300, TitledBorder.LEADING, TitledBorder.TOP, null, null);
		panel_2.setBorder(panel2TitleBorder);
		
		JLabel label_9 = new JLabel("\u65F6\u95F4(s)\uFF1A");
		label_9.setBounds(10, 23, 54, 15);
		panel_2.add(label_9);
		
		fw300TimeField = new JTextField();
		fw300TimeField.setEditable(false);
		fw300TimeField.setColumns(10);
		fw300TimeField.setBounds(66, 20, 86, 21);
		panel_2.add(fw300TimeField);
		
		JLabel label_10 = new JLabel("\u8DDD\u79BB(km)\uFF1A");
		label_10.setBounds(10, 51, 68, 15);
		panel_2.add(label_10);
		
		fw300DistanceField = new JTextField();
		fw300DistanceField.setEditable(false);
		fw300DistanceField.setColumns(10);
		fw300DistanceField.setBounds(76, 48, 76, 21);
		panel_2.add(fw300DistanceField);
		
		JLabel label_11 = new JLabel("\u80FD\u8017(KJ)\uFF1A");
		label_11.setBounds(10, 79, 68, 15);
		panel_2.add(label_11);
		
		fw300PowerField = new JTextField();
		fw300PowerField.setEditable(false);
		fw300PowerField.setColumns(10);
		fw300PowerField.setBounds(76, 76, 76, 21);
		panel_2.add(fw300PowerField);
		
		fw300AccField = new JTextField();
		fw300AccField.setEditable(false);
		fw300AccField.setColumns(10);
		fw300AccField.setBounds(119, 104, 33, 21);
		panel_2.add(fw300AccField);
		
		JLabel label_12 = new JLabel("\u5E73\u5747\u52A0\u901F\u5EA6(m/s^2)\uFF1A");
		label_12.setBounds(10, 107, 123, 15);
		panel_2.add(label_12);
		
		JLabel label_13 = new JLabel("\u5269\u4F59\u52A0\u901F\u5EA6(m/s^2)\uFF1A");
		label_13.setBounds(10, 131, 123, 15);
		panel_2.add(label_13);
		
		fw300RAccField = new JTextField();
		fw300RAccField.setEditable(false);
		fw300RAccField.setColumns(10);
		fw300RAccField.setBounds(120, 128, 32, 21);
		panel_2.add(fw300RAccField);
		
		//ǣ����������ͼ
		tractionChartPanel = new ChartPanel(TractionConfChart.createBlankChart("ǣ���������"));
		tractionChartPanel.setPopupMenu(null);
		tractionChartPanel.setBounds(10, 135, 1114, 518);
		tractionChartPanel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		
		//������������ͼ
		launchChartPanel = new ChartPanel(TractionConfChart.createBlankChart("�����������"));
		launchChartPanel.setBounds(10, 135, 1114, 518);
		launchChartPanel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		
		add(tractionChartPanel);
		
		refresh = new JButton("ˢ��");
		refresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refreshTractionTypePanel(trainCategoryId);
			}
		});
		refresh.setBounds(431, 102, 93, 23);
		add(refresh);
		
	}
	
	/**
	 * �����г����в����������ı���
	 */
	public void setTrainRunParameters(int trainCategoryId){
		setSPEED_300(trainCategoryId);
		//����
		TrainRunParametersCal trainParametersCal = new TrainRunParametersCal(trainCategoryId);
		double [] fwPara200 = trainParametersCal.forwardParameters(SPEED_200);
		double [] fwPara300 = trainParametersCal.forwardParameters(SPEED_300);
		//��ֵ
		fw200TimeField.setText(MyTools.numFormat2(fwPara200[0])+"");
		fw200DistanceField.setText(MyTools.numFormat2(fwPara200[1])+"");
		fw200PowerField.setText(MyTools.numFormat2(fwPara200[2])+"");
		fw200AccField.setText(MyTools.numFormat2(fwPara200[3])+"");
		fw200RAccField.setText(MyTools.numFormat2(fwPara200[4])+"");
		
		fw300TimeField.setText(MyTools.numFormat2(fwPara300[0])+"");
		fw300DistanceField.setText(MyTools.numFormat2(fwPara300[1])+"");
		fw300PowerField.setText(MyTools.numFormat2(fwPara300[2])+"");
		fw300AccField.setText(MyTools.numFormat2(fwPara300[3])+"");
		fw300RAccField.setText(MyTools.numFormat2(fwPara300[4])+"");
	}
	
	/**
	 *  ���ݺ����꣬����������
	 * @param index
	 * @param x
	 * @return
	 */
	public double calculateY(int index, double x){
		double y = 0;
		if(x<0 || x>V){
			throw new NumberFormatException();
		}
		if(index == 1){//ǣ��������
			y = TractionForce.getTractionForce(x/3.6)/1000;//��λ km/h
		}else if(index == 2){//����������������
			y = TractionFormulas.airFriction(x);
		}else if(index == 3){//��������
			y = TractionFormulas.electricity(x);
		}else if(index == 4){//��ѹ����
			y = TractionFormulas.voltage(x);
		}else if(index == 5){//ǣ��ճ������(�ɹ�)
			y = TractionFormulas.dryRail(x);
		}else if(index == 6){//ǣ��ճ������(ʪ��)
			y = TractionFormulas.wetRail(x);
		}
		y = MyTools.numFormat2(y);//������λС��
		return y;
	}
	
	/**
	 * ���typeButtonGroup��ѡ�е�button
	 * @return
	 * @throws NullPointerException
	 */
	public int getSelectedTractionId() throws NullPointerException{
		int type = 0;
		if(typeButtonGroup.getButtonCount() != 0){
			type = Integer.parseInt(typeButtonGroup.getSelection().getActionCommand());
		}
		return type;
	}
	
	/**
	 * ��tractionTypePanel����ӵ�ѡ��
	 * @param trainCategoryId
	 */
	public void refreshTractionTypePanel(int trainCategoryId){
		tractionTypePanel.removeAll();
		typeButtonGroup = new ButtonGroup();
		ArrayList<TrainTractionConfType> typeList = TractionConfPanelService.getTrainTractionConfType(trainCategoryId);
		if(typeList.size() != 0){
			for(int i=0;i<typeList.size();i++){
				final TrainTractionConfType type = typeList.get(i);
				final JRadioButton newRadioButton = new JRadioButton("���"+type.getType());
				newRadioButton.setSelected(true);//Ĭ���������ѡ�С����1��
				newRadioButton.setActionCommand(type.getTractionId()+"");
				tractionTypePanel.add(newRadioButton);
				typeButtonGroup.add(newRadioButton);
				newRadioButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						if(newRadioButton.isSelected()){
							assignParametersToTractionFormula(type.getTractionId(), type.getTrainCategoryId());
						}
					}
				});
				tractionTypePanel.add(newRadioButton);
				tractionTypePanel.updateUI();
			}
		}
		refreshTrainTypeCombobox();
	}
	
	/**
	 * ��ȡѡ�еĵ�ѡ��������е����֣��������ǡ����1�����򷵻�1
	 * @return
	 */
	public int getSelectedButtonText() {
        for (Enumeration<AbstractButton> buttons = typeButtonGroup.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();
            if (button.isSelected()) {;
                return MyTools.getNumFromStr(button.getText());
            }
        }
        return 1;
    }
	
	/**
	 * ���л�trainCategoryId��ʱ��ˢ��ǣ������ѡ��
	 */
	public void refreshTrainTypeCombobox(){
		tractionTypeComboBox.removeAll();
		setTrainTypeArray();
		tractionTypeComboBox.setModel(new DefaultComboBoxModel<Integer>(tractionTypeArray));
	}
	
	/**
	 * ���÷��������������
	 */
	public void setTrainTypeArray(){
		ArrayList<TrainTractionConfType> typeList = TractionConfPanelService.getTrainTractionConfType(trainCategoryId);
		tractionTypeArray = new Integer[typeList.size()];
		for(int k=0; k < typeList.size(); k++){
			tractionTypeArray[k] = typeList.get(k).getType();
		}
	}
	
	/**
	 * �����ݱ�train_category��������г����ƣ�����ֵ
	 */
	public void getAllTrainCategoryToArray() {
		ArrayList<TrainCategory> tc = TrainEditPanelService.getAllTrainCategory();
		trainCategoryIdArray = new Integer[tc.size()+1];
		trainCategoryNameArray = new String[tc.size()+1];
		trainCategoryIdArray[0] = 0;
		trainCategoryNameArray[0] = "��ѡ���������";
		for (int i = 0; i < tc.size(); i++) {
			trainCategoryIdArray[i+1] = tc.get(i).getId();
			trainCategoryNameArray[i+1] = tc.get(i).getName();
		}
		
		setTrainTypeArray();
	}
	
	/**
	 * �����µ�ǧ�ֱȵ�ֵ
	 */
	public void calculateSlopeChartData(){
		for(int j=0;j<chartDataList.size();j++){
			double v = chartDataList.get(j).getV();
			chartDataList.get(j).setSlope(TractionFormulas.slope(i, v));
		}
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
	 * 2014.11.30 Ϊ����doc�����������ṩ����
	 * @return
	 */
	public static ArrayList<CurveFormulaBean> getPerformanceCalculator(){
		ArrayList<CurveFormulaBean> launchDataList = new ArrayList<CurveFormulaBean>();
		double a = 0;//m/s^2
		double V0=0;//km/h
		double S0 = 0;//km
		int t = 0;
		while(true){
			CurveFormulaBean bean = new CurveFormulaBean();
			a = TractionFormulas.acceleration(V0);
			double Vt = TractionFormulas.Vt(V0, a);
			double St = TractionFormulas.St(S0, Vt, V0, a);
			if(Vt == -1){
				break;
			}
			t++;//ʱ�����
			V0 = Vt;
			S0 = St;
			bean.setA(a);
			bean.setVt(Vt);
			bean.setSt(St);
			bean.setT(t);
			launchDataList.add(bean);
		}
		return launchDataList;
	}
	
	/**
	 * ������������
	 */
	public void launchPerformanceCalculator(){
		//�������ܱ�
		final LaunchPerformanceChart launchPerformanceChart = new LaunchPerformanceChart();
		if(launchDataList != null){
			launchDataList.clear();
		}
		//�������ܼ�����
		launchDataList = new ArrayList<CurveFormulaBean>();
		double a = 0;//m/s^2
		double V0=0;//km/h
		double S0 = 0;//km
		int t = 0;
		while(true){
			CurveFormulaBean bean = new CurveFormulaBean();
			a = TractionFormulas.acceleration(V0);
			double Vt = TractionFormulas.Vt(V0, a);
			double St = TractionFormulas.St(S0, Vt, V0, a);
			if(Vt == -1){
				break;
			}
			t++;//ʱ�����
			V0 = Vt;
			S0 = St;
			bean.setA(a);
			bean.setVt(Vt);
			bean.setSt(St);
			bean.setT(t);
			launchDataList.add(bean);
		}
		if(launchChartPanel != null){
			remove(launchChartPanel);
		}
		launchChartPanel = launchPerformanceChart.addChartToPanel(launchDataList);
		//�Ҽ��˵�
		JPopupMenu popupMenu = new JPopupMenu();
		addPopup(launchChartPanel, popupMenu);
		//0.λ���ٶ�����
		final JCheckBoxMenuItem checkBoxMenuItem_0 = new JCheckBoxMenuItem("λ���ٶ�����");
		final JCheckBoxMenuItem checkBoxMenuItem_1 = new JCheckBoxMenuItem("λ��ʱ������");
		final JCheckBoxMenuItem checkBoxMenuItem_2 = new JCheckBoxMenuItem("ƽ�����ٶ��ٶ�����");
		final JCheckBoxMenuItem checkBoxMenuItem_3 = new JCheckBoxMenuItem("�ٶ�ʱ������");
		final JCheckBoxMenuItem checkBoxMenuItem_4 = new JCheckBoxMenuItem("˲ʱ���ٶ�����");
		checkBoxMenuItem_0.setSelected(true);
		checkBoxMenuItem_0.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(checkBoxMenuItem_0.isSelected()){
					checkBoxMenuItem_0.setSelected(true);
					checkBoxMenuItem_1.setSelected(false);
					checkBoxMenuItem_2.setSelected(false);
					checkBoxMenuItem_3.setSelected(false);
					checkBoxMenuItem_4.setSelected(false);
					LaunchPerformanceChart.showStVt(launchDataList);
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
					checkBoxMenuItem_2.setSelected(false);
					checkBoxMenuItem_3.setSelected(false);
					checkBoxMenuItem_4.setSelected(false);
					LaunchPerformanceChart.showStT(launchDataList);
				}
			}
		});
		popupMenu.add(checkBoxMenuItem_1);
		//2.ƽ�����ٶ��ٶ�����
		checkBoxMenuItem_2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(checkBoxMenuItem_2.isSelected()){
					checkBoxMenuItem_0.setSelected(false);
					checkBoxMenuItem_1.setSelected(false);
					checkBoxMenuItem_2.setSelected(true);
					checkBoxMenuItem_3.setSelected(false);
					checkBoxMenuItem_4.setSelected(false);
					LaunchPerformanceChart.showAveAT(launchDataList);
				}
			}
		});
		popupMenu.add(checkBoxMenuItem_2);
		//3.�ٶ�ʱ������
		checkBoxMenuItem_3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(checkBoxMenuItem_3.isSelected()){
					checkBoxMenuItem_0.setSelected(false);
					checkBoxMenuItem_1.setSelected(false);
					checkBoxMenuItem_2.setSelected(false);
					checkBoxMenuItem_3.setSelected(true);
					checkBoxMenuItem_4.setSelected(false);
					LaunchPerformanceChart.showVtT(launchDataList);
				}
			}
		});
		popupMenu.add(checkBoxMenuItem_3);
		//4.˲ʱ���ٶ�����
		
		checkBoxMenuItem_4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(checkBoxMenuItem_4.isSelected()){
					checkBoxMenuItem_0.setSelected(false);
					checkBoxMenuItem_1.setSelected(false);
					checkBoxMenuItem_2.setSelected(false);
					checkBoxMenuItem_3.setSelected(false);
					checkBoxMenuItem_4.setSelected(true);
					LaunchPerformanceChart.showAT(launchDataList);
				}
			}
		});
		popupMenu.add(checkBoxMenuItem_4);
		
		repaint();
		add(launchChartPanel);
	}
	
	/**
	 * ����ǣ����ʽ��ȡǣ��������
	 * @param tractionId
	 * @param trainCategoryId
	 * @return
	 */
	public static ArrayList<CurveFormulaBean> getTractionData(int tractionId, int trainCategoryId){
		ArrayList<CurveFormulaBean> chartDataList = new ArrayList<CurveFormulaBean>();
		//1.��ǣ����ʽ��ֵ
		TrainTractionConf trainTractionConf = TractionConfPanelService.getTrainTractionConf(tractionId, trainCategoryId);
		TractionForce.Fst = trainTractionConf.getFst();
		TractionForce.F1 = trainTractionConf.getF1(); 
		TractionForce.F2 = trainTractionConf.getF2();
		TractionForce.v1 = trainTractionConf.getV1();
		TractionForce.v2 = trainTractionConf.getV2();
		TractionForce.P1 = trainTractionConf.getP1();
		
		for(int v=0;v<350;v+=5){
			CurveFormulaBean bean = new CurveFormulaBean();
			bean.setV(v);
			bean.setTractionForce(TractionForce.getTractionForce(v/3.6)/1000);
			bean.setAirFriction(TractionFormulas.airFriction(v));
			bean.setDryRail(TractionFormulas.dryRail(v));
			bean.setWetRail(TractionFormulas.wetRail(v));
//			System.out.println(v+"; "+TractionForce.getTractionForce(v/3.6)/1000);
			chartDataList.add(bean);
		}
		
		return chartDataList;
	}
	
	/**
	 * ͨ��train_category_id��ȡ�ó��Ͷ�Ӧ��ǣ������������ǣ����ʽ��ֵ
	 * @param tractionId
	 * @param trainCategoryId
	 */
	public void assignParametersToTractionFormula(int tractionId, int trainCategoryId){
		//ǣ����-�ٶ����߱�
		final TractionConfChart tractionConfChart = new TractionConfChart();
		if(chartDataList != null){
			chartDataList.clear();
		}
		// �����ɹ�ʽ����õ��Ľ��
		chartDataList = new ArrayList<CurveFormulaBean>();
		//1.��ǣ����ʽ��ֵ
		TrainTractionConf trainTractionConf = TractionConfPanelService.getTrainTractionConf(tractionId, trainCategoryId);
		TractionForce.Fst = trainTractionConf.getFst();
		TractionForce.F1 = trainTractionConf.getF1(); 
		TractionForce.F2 = trainTractionConf.getF2();
		TractionForce.v1 = trainTractionConf.getV1();
		TractionForce.v2 = trainTractionConf.getV2();
		TractionForce.P1 = trainTractionConf.getP1();
		
		//2.����������������ֵ
		double [] parameters = TractionConfPanelService.getSomeParametersFromTrainEdit(trainCategoryId);
		TractionFormulas.a = parameters[0];
		TractionFormulas.b = parameters[1];
		TractionFormulas.c = parameters[2];
		TractionFormulas.dv = parameters[3];//�����
		TractionFormulas.m = parameters[4];//����
		TractionFormulas.maxV = parameters[5];//��������ٶ�
		
		//3.������������ֵ
		TractionFormulas.vi1 = trainTractionConf.getVi1();
		TractionFormulas.I1 = trainTractionConf.getI1();
		TractionFormulas.vi2 = trainTractionConf.getVi2();
		TractionFormulas.I2 = trainTractionConf.getI2();
		TractionFormulas.vi3 = trainTractionConf.getVi3();
		TractionFormulas.I3 = trainTractionConf.getI3();
		
		//4.����ѹ������ֵ
		TractionFormulas.vu1 = trainTractionConf.getVu1();
		TractionFormulas.u1 = trainTractionConf.getU1();
		TractionFormulas.vu2 = trainTractionConf.getVu2();
		TractionFormulas.u2 = trainTractionConf.getU2();
		TractionFormulas.vu3 = trainTractionConf.getVu3();
		TractionFormulas.u3 = trainTractionConf.getU3();
		
		V = TractionFormulas.maxV;
		for(int v=0;v<=V;v+=5){
			CurveFormulaBean bean = new CurveFormulaBean();
			bean.setV(v);
			bean.setTractionForce(TractionForce.getTractionForce(v/3.6)/1000);
			bean.setAirFriction(TractionFormulas.airFriction(v));
			bean.setDryRail(TractionFormulas.dryRail(v));
			bean.setWetRail(TractionFormulas.wetRail(v));
//			System.out.println(v+"; "+TractionForce.getTractionForce(v/3.6)/1000);
			chartDataList.add(bean);
		}
		
		if(tractionChartPanel != null){
			remove(tractionChartPanel);
		}
		tractionChartPanel = tractionConfChart.addChartToPanel(chartDataList);
		//�Ҽ��˵�
		JPopupMenu popupMenu = new JPopupMenu();
		addPopup(tractionChartPanel, popupMenu);
		//0.ǣ����������
		final JCheckBoxMenuItem checkBoxMenuItem_0 = new JCheckBoxMenuItem("ǣ����������");
		checkBoxMenuItem_0.setSelected(true);
		checkBoxMenuItem_0.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(checkBoxMenuItem_0.isSelected()){
					TractionConfChart.lineStateChanger(0, true);
				}else{
					TractionConfChart.lineStateChanger(0, false);
				}
			}
		});
		popupMenu.add(checkBoxMenuItem_0);
		//1.������������
		final JCheckBoxMenuItem checkBoxMenuItem_1 = new JCheckBoxMenuItem("������������");
		checkBoxMenuItem_1.setSelected(true);
		checkBoxMenuItem_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(checkBoxMenuItem_1.isSelected()){
					TractionConfChart.lineStateChanger(1, true);
				}else{
					TractionConfChart.lineStateChanger(1, false);
				}
			}
		});
		popupMenu.add(checkBoxMenuItem_1);
		//2.��������
		final JCheckBoxMenuItem checkBoxMenuItem_2 = new JCheckBoxMenuItem("��������");
		checkBoxMenuItem_2.setSelected(true);
		checkBoxMenuItem_2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(checkBoxMenuItem_2.isSelected()){
					TractionConfChart.lineStateChanger(2, true);
				}else{
					TractionConfChart.lineStateChanger(2, false);
				}
			}
		});
		popupMenu.add(checkBoxMenuItem_2);
		//3.��ѹ����
		final JCheckBoxMenuItem checkBoxMenuItem_3 = new JCheckBoxMenuItem("��ѹ����");
		checkBoxMenuItem_3.setSelected(true);
		checkBoxMenuItem_3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(checkBoxMenuItem_3.isSelected()){
					TractionConfChart.lineStateChanger(3, true);
				}else{
					TractionConfChart.lineStateChanger(3, false);
				}
			}
		});
		popupMenu.add(checkBoxMenuItem_3);
		//4.ǣ��ճ����������(�ɹ�)
		final JCheckBoxMenuItem checkBoxMenuItem_4 = new JCheckBoxMenuItem("ǣ��ճ����������(�ɹ�)");
		checkBoxMenuItem_4.setSelected(false);
		checkBoxMenuItem_4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(checkBoxMenuItem_4.isSelected()){
					TractionConfChart.lineStateChanger(4, true);
				}else{
					TractionConfChart.lineStateChanger(4, false);
				}
			}
		});
		popupMenu.add(checkBoxMenuItem_4);
		//5.ǣ��ճ����������(ʪ��)
		final JCheckBoxMenuItem checkBoxMenuItem_5 = new JCheckBoxMenuItem("ǣ��ճ����������(ʪ��)");
		checkBoxMenuItem_5.setSelected(false);
		checkBoxMenuItem_5.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(checkBoxMenuItem_5.isSelected()){
					TractionConfChart.lineStateChanger(5, true);
				}else{
					TractionConfChart.lineStateChanger(5, false);
				}
			}
		});
		popupMenu.add(checkBoxMenuItem_5);
		//ˢ��chartPanel
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
	
	/**
	 * �������ߣ�д��excel�ļ�
	 * @param filePath
	 * @param interval
	 * @throws Exception
	 */
	public void saveLinesDate(String filePath, int interval) throws Exception{
		File file = new File(filePath);
		WritableWorkbook wb = Workbook.createWorkbook(file);
		WritableSheet sheet = wb.createSheet("ǣ������", 0);
		//������ʽ
		WritableFont titleFont = new WritableFont(WritableFont.ARIAL, 11, WritableFont.BOLD);
		WritableCellFormat titleFormat = new WritableCellFormat(titleFont);
		
		//д����
		sheet.addCell(new Label(0, 0, "�ٶ�(km/h)", titleFormat));
		sheet.addCell(new Label(2, 0, "ǣ����(KN)", titleFormat));
		sheet.addCell(new Label(3, 0, "��������(KN)", titleFormat));
		sheet.addCell(new Label(4, 0, "����(A)", titleFormat));
		sheet.addCell(new Label(5, 0, "��ѹ(v)", titleFormat));
		sheet.addCell(new Label(6, 0, "�ɹ�ǣ��ճ������(KN)", titleFormat));
		sheet.addCell(new Label(7, 0, "ʪ��ǣ��ճ������(KN)", titleFormat));
		sheet.addCell(new Label(8, 0, "�µ�����(KN)", titleFormat));
		
		//д�ٶ�����
		for(int v=0,j=1;v<V;v+=interval,j++){//interval�ǲ���   j�ǵ�j��
			Number vNum = new Number(0, j, v);
			sheet.addCell(vNum);
			Number tractionNum = new Number(2, j, MyTools.numFormat2(TractionForce.getTractionForce(v/3.6)/1000));
			sheet.addCell(tractionNum);
			Number airNum = new Number(3, j, MyTools.numFormat2(TractionFormulas.airFriction(v)));
			sheet.addCell(airNum);
			Number eleNum = new Number(4, j, MyTools.numFormat2(TractionFormulas.electricity(v)));
			sheet.addCell(eleNum);
			Number volNum = new Number(5, j, MyTools.numFormat2(TractionFormulas.voltage(v)*10));
			sheet.addCell(volNum);
			Number dryNum = new Number(6, j, MyTools.numFormat2(TractionFormulas.dryRail(v)));
			sheet.addCell(dryNum);
			Number wetNum = new Number(7, j, MyTools.numFormat2(TractionFormulas.wetRail(v)));
			sheet.addCell(wetNum);
			//�ж��Ƿ�����µ�����
			if(i!=0){
				Number slopeNum = new Number(8, j, MyTools.numFormat2(TractionFormulas.slope(interval, v)));
				sheet.addCell(slopeNum);
			}
		}
		
		wb.write();
		wb.close();
	}
}

/**
 * ���� �ٶ�-ǣ���� ͼ��
 * @author huhui
 *
 */
class TractionConfChart {
	
	private static XYSplineRenderer xySplineRenderer = null;
	private XYPlot xyPlot = null;
	public static XYSeriesCollection xyseriescollection = null;
	
	/**
	 * ��chartPanel��ӵ�TractionConfChartPanel��
	 * @param dataList
	 * @return
	 */
	public ChartPanel addChartToPanel(ArrayList<CurveFormulaBean> dataList){
		XYDataset dataset = createDataset(dataList);
		JFreeChart chart = createChart(dataset);
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setBounds(10, 135, 1114, 518);
		chartPanel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		chartPanel.setPopupMenu(null);
		chartPanel.setMouseWheelEnabled(true);//���ַŴ�
		return chartPanel;
	}

	/**
	 *  �������ݼ�
	 * @param chartDataList
	 * @return
	 */
	public XYDataset createDataset(ArrayList<CurveFormulaBean> chartDataList) {
		xyseriescollection = new XYSeriesCollection();
		XYSeries vTractionSeries = new XYSeries("ǣ����");
		XYSeries vAirFrictionSeries = new XYSeries("��������");
		XYSeries vElecSeries = new XYSeries("����");
		XYSeries vVoltageSeries = new XYSeries("��ѹ");
		XYSeries vDrySeries = new XYSeries("�ɹ�ճ������");
		XYSeries vWetSeries = new XYSeries("ʪ��ճ������");
		
		for (CurveFormulaBean bean : chartDataList) {
			vTractionSeries.add(bean.getV(), bean.getTractionForce());
			vAirFrictionSeries.add(bean.getV(), bean.getAirFriction());
			vDrySeries.add(bean.getV(), bean.getDryRail());
			vWetSeries.add(bean.getV(), bean.getWetRail());
		}
		//��������ֵ
		vElecSeries.add(TractionFormulas.vi1, TractionFormulas.I1);
		vElecSeries.add(TractionFormulas.vi2, TractionFormulas.I2);
		vElecSeries.add(TractionFormulas.vi3, TractionFormulas.I3);
		vElecSeries.add(TractionConfPanel.V, TractionFormulas.I3);
		//����ѹ��ֵ
		vVoltageSeries.add(TractionFormulas.vu1, TractionFormulas.u1);
		vVoltageSeries.add(TractionFormulas.vu2, TractionFormulas.u2);
		vVoltageSeries.add(TractionFormulas.vu3, TractionFormulas.u3);
		vVoltageSeries.add(TractionConfPanel.V, TractionFormulas.u3);
		xyseriescollection.addSeries(vTractionSeries);
		xyseriescollection.addSeries(vAirFrictionSeries);
		xyseriescollection.addSeries(vElecSeries);
		xyseriescollection.addSeries(vVoltageSeries);
		xyseriescollection.addSeries(vDrySeries);
		xyseriescollection.addSeries(vWetSeries);
		return xyseriescollection;
	}
	
	/**
	 * ��ʾ�ؼ��� 
	 */
	public static void displayCriticalPoints(){
		if (xySplineRenderer.getSeriesVisible(0)) {
			//0.ǣ���������߹ؼ���
			double x0_1 = TractionForce.v1;
			double y0_1 = MyTools.numFormat2(TractionForce
					.getTractionForce(x0_1 / 3.6) / 1000);
			double x0_2 = TractionForce.v2;
			double y0_2 = MyTools.numFormat2(TractionForce
					.getTractionForce(x0_2 / 3.6) / 1000);
			XYPointerAnnotation annotation0 = new XYPointerAnnotation("("
					+ x0_1 + "km/h," + y0_1 + "KN)", x0_1, y0_1,
					-0.7853981633974483D);
			annotation0.setTextAnchor(TextAnchor.BOTTOM_LEFT);
			annotation0.setPaint(Color.red);
			annotation0.setArrowPaint(Color.red);
			xySplineRenderer.addAnnotation(annotation0);
			XYPointerAnnotation annotation1 = new XYPointerAnnotation("("
					+ x0_2 + "km/h," + y0_2 + "KN)", x0_2, y0_2,
					-0.7853981633974483D);
			annotation1.setTextAnchor(TextAnchor.BOTTOM_LEFT);
			annotation1.setPaint(Color.red);
			annotation1.setArrowPaint(Color.red);
			xySplineRenderer.addAnnotation(annotation1);
		}
		
		
		//1.�����������߹ؼ���
		
		
		//2.�������߹ؼ���
		if (xySplineRenderer.getSeriesVisible(2)) {
			double x2_1 = TractionFormulas.vi2;
			double y2_1 = TractionFormulas.I2;
			double x2_2 = TractionFormulas.vi3;
			double y2_2 = TractionFormulas.I3;
			XYPointerAnnotation annotation2 = new XYPointerAnnotation("("
					+ x2_1 + "km/h," + y2_1 + "A)", x2_1, y2_1,
					-0.7853981633974483D);
			annotation2.setTextAnchor(TextAnchor.BOTTOM_LEFT);
			annotation2.setPaint(Color.green);
			annotation2.setArrowPaint(Color.green);
			xySplineRenderer.addAnnotation(annotation2);
			XYPointerAnnotation annotation3 = new XYPointerAnnotation("("
					+ x2_2 + "km/h," + y2_2 + "A)", x2_2, y2_2,
					-0.7853981633974483D);
			annotation3.setTextAnchor(TextAnchor.BOTTOM_LEFT);
			annotation3.setPaint(Color.green);
			annotation3.setArrowPaint(Color.green);
			xySplineRenderer.addAnnotation(annotation3);
		}
		
		//3.��ѹ���߹ؼ���
		if (xySplineRenderer.getSeriesVisible(3)) {
			double x3_1 = TractionFormulas.vu2;
			double y3_1 = TractionFormulas.u2;
			double x3_2 = TractionFormulas.vu3;
			double y3_2 = TractionFormulas.u3;
			XYPointerAnnotation annotation4 = new XYPointerAnnotation("("
					+ x3_1 + "km/h," + y3_1 + "*10v)", x3_1, y3_1,
					-0.7853981633974483D);
			annotation4.setTextAnchor(TextAnchor.BOTTOM_LEFT);
			annotation4.setPaint(Color.yellow);
			annotation4.setArrowPaint(Color.yellow);
			xySplineRenderer.addAnnotation(annotation4);
			XYPointerAnnotation annotation5 = new XYPointerAnnotation("("
					+ x3_2 + "km/h," + y3_2 + "*10v)", x3_2, y3_2,
					-0.7853981633974483D);
			annotation5.setTextAnchor(TextAnchor.BOTTOM_LEFT);
			annotation5.setPaint(Color.yellow);
			annotation5.setArrowPaint(Color.yellow);
			xySplineRenderer.addAnnotation(annotation5);
		}
		
		//4.�ɹ�ճ���������߹ؼ���
		if (xySplineRenderer.getSeriesVisible(4)) {
			double x4_1 = 100.0;
			double y4_1 = MyTools.numFormat2(TractionFormulas
					.dryRail(x4_1));
			XYPointerAnnotation annotation6 = new XYPointerAnnotation("("
					+ x4_1 + "km/h," + y4_1 + "KN)", x4_1, y4_1,
					1.570796326794897D);
			annotation6.setTextAnchor(TextAnchor.TOP_CENTER);
			annotation6.setPaint(Color.pink);
			annotation6.setArrowPaint(Color.pink);
			xySplineRenderer.addAnnotation(annotation6);
		}
		
		//5.ʪ��ճ���������߹ؼ���
		if (xySplineRenderer.getSeriesVisible(5)) {
			double x5_1 = 100.0;
			double y5_1 = MyTools.numFormat2(TractionFormulas
					.wetRail(x5_1));
			XYPointerAnnotation annotation7 = new XYPointerAnnotation("("
					+ x5_1 + "km/h," + y5_1 + "KN)", x5_1, y5_1,
					1.570796326794897D);
			annotation7.setTextAnchor(TextAnchor.TOP_CENTER);
			annotation7.setPaint(Color.cyan);
			annotation7.setArrowPaint(Color.cyan);
			xySplineRenderer.addAnnotation(annotation7);
		}
		
	}
	
	/**
	 * �����µ�ǧ�ֱ����ݼ�
	 * @param chartDataList
	 */
	public static void setSlopeDataset(ArrayList<CurveFormulaBean> chartDataList){
		XYSeries slopeSeries = new XYSeries("�µ�");
		for (CurveFormulaBean bean : chartDataList) {
			slopeSeries.add(bean.getV(), bean.getSlope());
		}
		xyseriescollection.addSeries(slopeSeries);
	}
	
	/**
	 * ȡ����ʾ�ؼ���
	 */
	public static void cancelDisplayCriticalPoints(){
		xySplineRenderer.removeAnnotations();
	}
	
	public JFreeChart createChart(XYDataset dataset) {
		NumberAxis xNum = new NumberAxis("�ٶ�(km/h)");//X��
		xNum.setAutoRangeStickyZero(true);
		NumberAxis yNum = new NumberAxis("");//Y��
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
		setLinesVisible();
		xyPlot = new XYPlot(dataset, xNum, yNum, xySplineRenderer);
		xyPlot.setBackgroundPaint(Color.white);
		//����������ɫ
		xyPlot.setDomainGridlinePaint(Color.black);
		xyPlot.setRangeGridlinePaint(Color.black);
		xyPlot.setAxisOffset(new RectangleInsets(4D, 4D, 4D, 4D));
		
		JFreeChart jFreeChart = new JFreeChart("ǣ���������", JFreeChart.DEFAULT_TITLE_FONT, xyPlot, true);
		return jFreeChart;
	}
	
	/**
	 * ��������������ʾ    ��ΪҪʹ��xySplineRenderer.getSeriesVisible(4)�������������������setSeriesVisible����
	 */
	public void setLinesVisible(){
		xySplineRenderer.setSeriesVisible(0, true);
		xySplineRenderer.setSeriesVisible(1, true);
		xySplineRenderer.setSeriesVisible(2, true);
		xySplineRenderer.setSeriesVisible(3, true);
		xySplineRenderer.setSeriesVisible(4, false);
		xySplineRenderer.setSeriesVisible(5, false);
	}
	
	/**
	 *  ��ʾ/��������
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
		}
	}
	
	/**
	 * ����һ�ſձ�
	 * @param title
	 * @return
	 */
	public static JFreeChart createBlankChart(String title){
		StandardChartTheme standardChartTheme = new StandardChartTheme("CN");
		standardChartTheme.setExtraLargeFont(new Font("����", Font.BOLD, 16));
		ChartFactory.setChartTheme(standardChartTheme);
		JFreeChart blankChart = ChartFactory.createXYLineChart(title,"Speed(km/h)", "", null, PlotOrientation.VERTICAL, true,true, false);
		XYPlot plot = blankChart.getXYPlot();
		plot.setBackgroundPaint(Color.WHITE);
		plot.setDomainGridlinePaint(Color.black);
		plot.setRangeGridlinePaint(Color.black);
		return blankChart;
	}
	
}


/**
 * ���� ������������ ͼ��
 * @author huhui
 *
 */
class LaunchPerformanceChart {
	
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
		chartPanel.setBounds(10, 135, 1114, 518);
		chartPanel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		chartPanel.setPopupMenu(null);
		chartPanel.setMouseWheelEnabled(true);//���ַŴ�
		return chartPanel;
	}

	/**
	 *  �������ݼ�
	 * @param chartDataList
	 * @return
	 */
	public XYDataset createDataset(ArrayList<CurveFormulaBean> chartDataList) {
		xyseriescollection = new XYSeriesCollection();
		XYSeries VtStSeries = new XYSeries("λ��-�ٶ�");
		XYSeries StTSeries = new XYSeries("λ��-ʱ��");
		for (int i=0;i<chartDataList.size();i=i+2) {
			CurveFormulaBean bean = chartDataList.get(i);
			VtStSeries.add(bean.getVt(), bean.getSt());
			StTSeries.add(bean.getT(), bean.getSt());
		}
		xyseriescollection.addSeries(VtStSeries);
		return xyseriescollection;
	}
	
	/**
	 * ���á�λ��-�ٶȡ��������ݼ�
	 * @param chartDataList
	 */
	public static void showStVt(ArrayList<CurveFormulaBean> chartDataList){
		xNum.setLabel("�ٶ�(km/h)");
		yNum.setLabel("λ��(km)");
		xyseriescollection.removeAllSeries();
		XYSeries VtStSeries = new XYSeries("λ��-�ٶ�");
		for (int i=0;i<chartDataList.size();i=i+2) {
			CurveFormulaBean bean = chartDataList.get(i);
			VtStSeries.add(bean.getVt(), bean.getSt());
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
	 * ���á�ƽ�����ٶ�-ʱ�䡱�������ݼ�
	 * @param chartDataList
	 */
	public static void showAveAT(ArrayList<CurveFormulaBean> chartDataList){
		xNum.setLabel("ʱ��(s)");
		yNum.setLabel("ƽ�����ٶ�(m/s^2)");
		xyseriescollection.removeAllSeries();
		XYSeries series = new XYSeries("ƽ�����ٶ�-ʱ��");
		double sumA = 0;
		for (int i=0, j=1;i<chartDataList.size();i=i+2, j++) {
			CurveFormulaBean bean = chartDataList.get(i);
			sumA += bean.getA();
			series.add(bean.getT(), sumA/j);
		}
		xyseriescollection.addSeries(series);
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
	
	/**
	 * ���á�˲ʱ���ٶ�-ʱ�䡱�������ݼ�
	 * @param chartDataList
	 */
	public static void showAT(ArrayList<CurveFormulaBean> chartDataList){
		xNum.setLabel("ʱ��(s)");
		yNum.setLabel("���ٶ�(m/s^2)");
		xyseriescollection.removeAllSeries();
		XYSeries aTSeries = new XYSeries("���ٶ�-ʱ��");
		for (int i=0;i<chartDataList.size();i=i+2) {
			CurveFormulaBean bean = chartDataList.get(i);
			aTSeries.add(bean.getT(), bean.getA());
		}
		xyseriescollection.addSeries(aTSeries);
	}
	
	public JFreeChart createChart(XYDataset dataset) {
		xNum = new NumberAxis("");//X��
		xNum.setAutoRangeStickyZero(true);
		yNum = new NumberAxis("");//Y��
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
		
		JFreeChart jFreeChart = new JFreeChart("�����������", JFreeChart.DEFAULT_TITLE_FONT, xyPlot, true);
		return jFreeChart;
	}
	
}