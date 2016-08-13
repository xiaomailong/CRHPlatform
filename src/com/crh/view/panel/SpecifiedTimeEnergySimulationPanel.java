package com.crh.view.panel;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.crh.calculation.mintime.RunDataThread;
import com.crh.calculation.specifiedtime.RunDataCalculatorSpecifiedTime;
import com.crh.doc.CreateDocThread;
import com.crh.service.MinTimeSimulationService;
import com.crh.view.dialog.CalculatingInvokeAndWaitDialog;
import com.crh.view.dialog.DisplayRunDataDialog;
import com.crh.view.dialog.DocInvokeAndWaitDialog;
import com.crh.view.dialog.RouteDataTrainNumDialog;
import com.crh.view.dialog.RundataChartDialog;
import com.crh2.calculate.MergeLists;
import com.crh2.javabean.TrainCategory;
import com.crh2.javabean.TrainRouteName;
import com.crh2.javabean.TrainRouteTrainnum;
import com.crh2.javabean.V0V1E;
import com.crh2.util.MyTools;
import com.crh2.util.MyUtillity;

/**
 * 固定时分节能控制运行仿真的界面
 * @author huhui
 *
 */
public class SpecifiedTimeEnergySimulationPanel extends JPanel {
	
	private JTextField maxSpeedText;
	private JTextField runTimeText;
	//定义所有JComboBox
	/**
	 * 线路选择
	 */
	private JComboBox<String> routeComboBox = new JComboBox<String>();
	/**
	 * 编组选择
	 */
	private JComboBox<String> trainNameComboBox = new JComboBox<String>();
	private JComboBox<Integer> trainIdComboBox = new JComboBox<Integer>();
	/**
	 * 牵引级位
	 */
	private JComboBox<String> tractionLevelComboBox = new JComboBox<String>();
	/**
	 * 制动级位
	 */
	private JComboBox<String> brakeLevelComboBox = new JComboBox<String>();
	/**
	 * 车次
	 */
	private JComboBox<String> trainNumComboBox = new JComboBox<String>();
	//定义所有JComboBox对应的数组
	private Integer [] trainIdArray;
	private String [] trainNameArray, routeArray, tractionLevelArray, brakeLevelArray, trainNumArray;
	private final String DEFAULT_COMBOBOX_ITEM = "请选择……";
	//定义仿真运行相关的控件
	/**
	 * 空白的面板
	 */
	private JPanel blankPanel = null; 
	/**
	 * 动画仿真
	 */
	private SpecifiedTimeEnergyAutoCRHPanel minTimeAutoCRHPanel = null;
	/**
	 * 快速运行仿真
	 */
	private SpecidiedTimeEnergyManualCRHPanel minTimeManualCRHPanel = null;
	/**
	 * 控制运行的按钮
	 */
	private JButton prePageButton, nextPageButton, startButton, pauseButton;
	/**
	 * 放置按钮
	 */
	private JPanel buttonPanel = null;
	private Thread autoRunThread = null;
	
	/**
	 * 翻页的大小
	 */
	private double RUN_LENGTH_FORWARD = 500;
	private double RUN_LENGTH_BACKWARD = -RUN_LENGTH_FORWARD;
	
	/**
	 * 0表示快速运行，1表示动画运行
	 */
	public static int MANUAL_MODEL = 0, AUTO_MODEL = 1;
	
	public SpecifiedTimeEnergySimulationPanel() {
		setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		panel.setBounds(10, 10, 1350, 64);
		add(panel);
		panel.setLayout(null);
		
		JLabel label = new JLabel("\u8DEF\u7EBF\u9009\u62E9\uFF1A");
		label.setBounds(10, 10, 60, 15);
		panel.add(label);
		
		//初始化所有下拉框数值
		initComboBoxes();
		
		routeComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//设置route和trainnum级联
				refreshTrainNumComboBox();
			}
		});
		routeComboBox.setBounds(69, 7, 95, 21);
		routeComboBox.setModel(new DefaultComboBoxModel<String>(routeArray));
		panel.add(routeComboBox);
		trainIdComboBox.setVisible(false);
		trainNameComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//设置name和id级联
				int selectedIndex = trainNameComboBox.getSelectedIndex();
				trainIdComboBox.setSelectedIndex(selectedIndex);
			}
		});
		trainNameComboBox.setBounds(256, 7, 95, 21);
		trainNameComboBox.setModel(new DefaultComboBoxModel<String>(trainNameArray));
		panel.add(trainNameComboBox);
		
		JLabel label_1 = new JLabel("\u7F16\u7EC4\u9009\u62E9\uFF1A");
		label_1.setBounds(197, 10, 60, 15);
		panel.add(label_1);
		
		tractionLevelComboBox.setBounds(449, 7, 95, 21);
		tractionLevelComboBox.setModel(new DefaultComboBoxModel<String>(tractionLevelArray));
		panel.add(tractionLevelComboBox);
		
		JLabel label_2 = new JLabel("\u7275\u5F15\u7EA7\u4F4D\uFF1A");
		label_2.setBounds(390, 10, 60, 15);
		panel.add(label_2);
		
		brakeLevelComboBox.setBounds(647, 7, 95, 21);
		brakeLevelComboBox.setModel(new DefaultComboBoxModel<String>(brakeLevelArray));
		panel.add(brakeLevelComboBox);
		
		JLabel label_3 = new JLabel("\u5236\u52A8\u7EA7\u4F4D\uFF1A");
		label_3.setBounds(588, 10, 60, 15);
		panel.add(label_3);
		
		trainNumComboBox.setBounds(44, 35, 95, 21);
		trainNumComboBox.setModel(new DefaultComboBoxModel<String>(trainNumArray));
		panel.add(trainNumComboBox);
		
		JLabel label_4 = new JLabel("\u8F66\u6B21\uFF1A");
		label_4.setBounds(10, 38, 60, 15);
		panel.add(label_4);
		
		JLabel label_5 = new JLabel("\u6700\u9AD8\u901F\u5EA6\uFF1A");
		label_5.setBounds(197, 38, 60, 15);
		panel.add(label_5);
		
		maxSpeedText = new JTextField();
		maxSpeedText.setText("300");
		maxSpeedText.setBounds(256, 35, 95, 21);
		panel.add(maxSpeedText);
		maxSpeedText.setColumns(10);
		
		JButton btnNewButton = new JButton("...");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String routeName = (String) routeComboBox.getSelectedItem();
				if(DEFAULT_COMBOBOX_ITEM.equals(routeName)){
					JOptionPane.showMessageDialog(SpecifiedTimeEnergySimulationPanel.this, "请选择路线！");
				}else{
					RouteDataTrainNumDialog routeDataTrainNumDialog = new RouteDataTrainNumDialog(null, routeName);
					//刷新车次
					refreshTrainNumComboBox();
				}
			}
		});
		btnNewButton.setBounds(141, 34, 25, 23);
		panel.add(btnNewButton);
		
		JLabel lblKm = new JLabel("km/h");
		lblKm.setBounds(358, 38, 54, 15);
		panel.add(lblKm);
		
		JButton button = new JButton("快速运行");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(MergeLists.rtEnergyRundataMergedList != null){
					//显示运行界面
					int routeId = getRouteId();
//					int routeId = MinTimeSimulationService.getRouteIdByName("京津线（下行）"); //*调试
					minTimeManualCRHPanel = new SpecidiedTimeEnergyManualCRHPanel(routeId, MergeLists.rtEnergyRundataMergedList);
					minTimeManualCRHPanel.setBounds(10, 84, 1350, 544);
					displayUI(SpecifiedTimeEnergySimulationPanel.MANUAL_MODEL);//显示快速运行的panel及按钮
					updateUI();
				}else{
					JOptionPane.showMessageDialog(SpecifiedTimeEnergySimulationPanel.this, "请先计算运行数据！");
				}
			}
		});
		button.setBounds(1037, 6, 105, 23);
		panel.add(button);
		
		JButton autoRunButton = new JButton("动画运行");
		autoRunButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(MergeLists.rtEnergyRundataMergedList != null){
					int routeId = getRouteId();
					minTimeAutoCRHPanel = new SpecifiedTimeEnergyAutoCRHPanel(routeId, MergeLists.rtEnergyRundataMergedList);
					minTimeAutoCRHPanel.setBounds(10, 84, 1350, 544);
					autoRunThread = new Thread(minTimeAutoCRHPanel);
					autoRunThread.start();
					displayUI(SpecifiedTimeEnergySimulationPanel.AUTO_MODEL);//显示动画运行的panel及按钮
					updateUI();
				}else{
					JOptionPane.showMessageDialog(SpecifiedTimeEnergySimulationPanel.this, "请先计算运行数据！");
				}
			}
		});
		autoRunButton.setBounds(1037, 34, 105, 23);
		panel.add(autoRunButton);
		
		JButton calButton = new JButton("计算");
		calButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//参数检查
				if(checkParameters()){//参数正常
					String routeStr = (String) routeComboBox.getSelectedItem();
					String trainNumStr = (String) trainNumComboBox.getSelectedItem();
					int trainId = (Integer) trainIdComboBox.getSelectedItem();
					double maxSpeed = Double.parseDouble(maxSpeedText.getText());
					double randomSpeed = Double.parseDouble(runTimeText.getText());
					String tractionLevel = (String) tractionLevelComboBox.getSelectedItem();
					RunDataThread minTimeRunDataThread = new RunDataThread(routeStr, trainNumStr, trainId, maxSpeed, randomSpeed, RunDataThread.STE, 0, tractionLevel);
					new CalculatingInvokeAndWaitDialog(minTimeRunDataThread);
					V0V1E bean = RunDataCalculatorSpecifiedTime.resultBean;
					JOptionPane.showMessageDialog(SpecifiedTimeEnergySimulationPanel.this, "v0 = "+MyTools.numFormat2(bean.getV0())+", v1 = "+MyTools.numFormat2(bean.getV1())+", E = "+MyTools.numFormat2(bean.getE()));
					MyUtillity.checkTractionPower(MergeLists.rtEnergyRundataMergedList);
				}else{
					JOptionPane.showMessageDialog(SpecifiedTimeEnergySimulationPanel.this, "参数不完整，请重新选择！");
				}
			}
		});
		calButton.setBounds(769, 6, 105, 23);
		panel.add(calButton);
		
		JLabel label_6 = new JLabel("运行时间：");
		label_6.setBounds(390, 38, 60, 15);
		panel.add(label_6);
		
		runTimeText = new JTextField();
		runTimeText.setText("1800");
		runTimeText.setColumns(10);
		runTimeText.setBounds(449, 35, 95, 21);
		panel.add(runTimeText);
		
		JLabel lblKmh = new JLabel("s");
		lblKmh.setBounds(551, 38, 54, 15);
		panel.add(lblKmh);
		
		trainIdComboBox.setBounds(647, 35, 95, 21);
		trainIdComboBox.setModel(new DefaultComboBoxModel<Integer>(trainIdArray));
		panel.add(trainIdComboBox);
		
		JButton button_5 = new JButton("计算结果");
		button_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(MergeLists.rtEnergyRundataMergedList != null){
					String routeNameStr = (String) routeComboBox.getSelectedItem();
					new DisplayRunDataDialog(routeNameStr, MergeLists.rtEnergyRundataMergedList);
				}
			}
		});
		button_5.setBounds(769, 34, 105, 23);
		panel.add(button_5);
		
		JButton btnNewButton_1 = new JButton("图表");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(MergeLists.rtEnergyRundataMergedList != null){
					new RundataChartDialog("固定时分节能控制运行数据曲线", MergeLists.rtEnergyRundataMergedList);
				}
			}
		});
		btnNewButton_1.setBounds(891, 6, 105, 23);
		panel.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("生成报告");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(checkParameters()){//参数正常
					String routeStr = (String) routeComboBox.getSelectedItem();
					String trainNumStr = (String) trainNumComboBox.getSelectedItem();
					int trainId = (Integer) trainIdComboBox.getSelectedItem();
					String trainCategory = (String) trainNameComboBox.getSelectedItem();
					if(MergeLists.rtEnergyRundataMergedList.size() != 0){
						try {
							JFileChooser docFileChooser = new JFileChooser();
							FileNameExtensionFilter fileFilter = new FileNameExtensionFilter(".doc", "doc");
							docFileChooser.setFileFilter(fileFilter);
							docFileChooser.setFileSelectionMode(JFileChooser.SAVE_DIALOG);
							docFileChooser.setVisible(true);//显示
							int retval = docFileChooser.showSaveDialog(SpecifiedTimeEnergySimulationPanel.this);
							String savePath = "";
							if (retval == JFileChooser.APPROVE_OPTION) {
								// 得到用户选择文件的绝对路径
								savePath = docFileChooser.getSelectedFile().getAbsolutePath() + ".doc";
								CreateDocThread createDocThread = new CreateDocThread(routeStr, trainNumStr, trainId, trainCategory, MergeLists.rtEnergyRundataMergedList, savePath);
								new DocInvokeAndWaitDialog(createDocThread);
							}
						} catch (Exception e1) {
							JOptionPane.showMessageDialog(SpecifiedTimeEnergySimulationPanel.this, "文档保存失败！");
							return;
						}
					}
				}else{
					JOptionPane.showMessageDialog(SpecifiedTimeEnergySimulationPanel.this, "参数不完整，请重新选择！");
				}
			}
		});
		btnNewButton_2.setBounds(891, 34, 105, 23);
		panel.add(btnNewButton_2);
		
		blankPanel = new JPanel();
		blankPanel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		blankPanel.setBounds(10, 84, 1350, 544);
		add(blankPanel);
		
		buttonPanel = new JPanel();
		buttonPanel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		buttonPanel.setBounds(10, 632, 1350, 33);
		add(buttonPanel);
		buttonPanel.setLayout(null);
		
		//快速运行对应的按钮
		prePageButton = new JButton("上一页");
		prePageButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				minTimeManualCRHPanel.run(RUN_LENGTH_BACKWARD);
			}
		});
		prePageButton.setBounds(564, 5, 93, 23);
		
		nextPageButton = new JButton("下一页");
		nextPageButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				minTimeManualCRHPanel.run(RUN_LENGTH_FORWARD);
			}
		});
		nextPageButton.setBounds(690, 5, 93, 23);
		
		//动画运行对应你的按钮
		startButton = new JButton("开始");
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SpecifiedTimeEnergyAutoCRHPanel.isPause = false;
			}
		});
		startButton.setBounds(564, 5, 93, 23);
		
		pauseButton = new JButton("暂停");
		pauseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SpecifiedTimeEnergyAutoCRHPanel.isPause = true;
			}
		});
		pauseButton.setBounds(690, 5, 93, 23);

	}
	
	/**
	 * 初始化所有下拉框
	 */
	public void initComboBoxes(){
		//线路选择
		ArrayList<TrainRouteName> routeList = MinTimeSimulationService.getTrainRouteName();
		routeArray = new String[routeList.size() + 1];
		routeArray[0] = DEFAULT_COMBOBOX_ITEM;
		for(int i=0;i<routeList.size();i++){
			routeArray[i+1] = routeList.get(i).getRouteName();
		}
		
		//编组选择
		ArrayList<TrainCategory> trainCategoryList = MinTimeSimulationService.getTrainCategory();
		trainIdArray = new Integer[trainCategoryList.size() + 1];
		trainNameArray = new String[trainCategoryList.size() + 1];
		trainIdArray[0] = 0;
		trainNameArray[0] = DEFAULT_COMBOBOX_ITEM;
		for(int i=0;i<trainCategoryList.size();i++){
			trainIdArray[i+1] = trainCategoryList.get(i).getId();
			trainNameArray[i+1] = trainCategoryList.get(i).getName();
		}
		
		//牵引级位
		tractionLevelArray = new String[17];
		tractionLevelArray[0] = DEFAULT_COMBOBOX_ITEM;
		tractionLevelArray[1] = "最大牵引(无级)";
		tractionLevelArray[2] = "1级";
		tractionLevelArray[3] = "2级";
		tractionLevelArray[4] = "3级";
		tractionLevelArray[5] = "4级";
		tractionLevelArray[6] = "5级";
		tractionLevelArray[7] = "6级";
		tractionLevelArray[8] = "7级";
		tractionLevelArray[9] = "8级";
		tractionLevelArray[10] = "9级";
		tractionLevelArray[11] = "10级";
		tractionLevelArray[12] = "11级";
		tractionLevelArray[13] = "12级";
		tractionLevelArray[14] = "13级";
		tractionLevelArray[15] = "14级";
		tractionLevelArray[16] = "15级";
		
		//制动级位
		brakeLevelArray = new String[2];
		brakeLevelArray[0] = DEFAULT_COMBOBOX_ITEM;
		brakeLevelArray[1] = "最大制动";
		
		//车次
		trainNumArray = new String[1];
		trainNumArray[0]= DEFAULT_COMBOBOX_ITEM;
		
	}
	
	/**
	 * 给车次下拉框重新赋值
	 */
	public void refreshTrainNumComboBox(){
		String routeName = (String) routeComboBox.getSelectedItem();
		ArrayList<TrainRouteTrainnum> trainNumList = MinTimeSimulationService.getTrainNum(routeName);
		trainNumArray = new String[trainNumList.size() + 1];
		trainNumArray[0] = DEFAULT_COMBOBOX_ITEM;
		for(int i=0;i<trainNumList.size();i++){
			trainNumArray[i+1] = trainNumList.get(i).getTrainNum();
		}
		trainNumComboBox.removeAll();
		trainNumComboBox.setModel(new DefaultComboBoxModel<String>(trainNumArray));
	}
	
	/**
	 * 参数检查
	 */
	public boolean checkParameters(){
		boolean b = false;
		String maxSpeedStr = maxSpeedText.getText().trim();
		String randomSpeedStr = runTimeText.getText().trim();
		if (routeComboBox.getSelectedIndex() != 0
				&& trainNameComboBox.getSelectedIndex() != 0
				&& tractionLevelComboBox.getSelectedIndex() != 0
				&& brakeLevelComboBox.getSelectedIndex() != 0
				&& trainNumComboBox.getSelectedIndex() != 0
				&& !"".equals(maxSpeedStr) && !"".equals(randomSpeedStr)) {
			b = true;
		}
//		return true;//*调试用
		return b;
	}
	
	/**
	 * 控制buttonPanel显示不同的button
	 * type: 0表示快速运行，1表示动画运行
	 */
	public void displayButton(int type){
		buttonPanel.removeAll();
		if(type == SpecifiedTimeEnergySimulationPanel.MANUAL_MODEL){
			buttonPanel.add(prePageButton);
			buttonPanel.add(nextPageButton);
		}else if(type == SpecifiedTimeEnergySimulationPanel.AUTO_MODEL){
			buttonPanel.add(startButton);
			buttonPanel.add(pauseButton);
		}
	}
	
	/**
	 * 显示响应的仿真panel，并删除其它panel
	 * type: 0表示快速运行，1表示动画运行
	 */
	public void displayUI(int type){
		remove(blankPanel);
		if(type == SpecifiedTimeEnergySimulationPanel.MANUAL_MODEL){
			if(minTimeAutoCRHPanel != null){
				remove(minTimeAutoCRHPanel);
			}
			add(minTimeManualCRHPanel);//显示快速运行的panel
			displayButton(type);
		}else if(type == SpecifiedTimeEnergySimulationPanel.AUTO_MODEL){
			if(minTimeManualCRHPanel != null){
				remove(minTimeManualCRHPanel);
			}
			add(minTimeAutoCRHPanel);//显示动画运行的panel
			displayButton(type);
		}
	}
	
	
	/**
	 * 通过routeName获取routeId
	 */
	public int getRouteId(){
		String routeNameStr = (String) routeComboBox.getSelectedItem();
		return MinTimeSimulationService.getRouteIdByName(routeNameStr);
	}
}
