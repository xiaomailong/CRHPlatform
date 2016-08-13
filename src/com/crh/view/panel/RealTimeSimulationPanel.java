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
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.crh.calculation.realtime.RunDataCalculatorRealTime;
import com.crh.doc.CreateDocThread;
import com.crh.service.MinTimeSimulationService;
import com.crh.view.dialog.DisplayRunDataDialog;
import com.crh.view.dialog.DocInvokeAndWaitDialog;
import com.crh.view.dialog.NetworkConfDialog;
import com.crh.view.dialog.RouteDataTrainNumDialog;
import com.crh.view.dialog.RundataChartDialog;
import com.crh2.javabean.TrainCategory;
import com.crh2.javabean.TrainRouteName;
import com.crh2.javabean.TrainRouteTrainnum;
import com.crh2.socket.SocketConnection;

/**
 * 常态运行仿真的界面
 * @author huhui
 *
 */
public class RealTimeSimulationPanel extends JPanel {
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
	 * 车次
	 */
	private JComboBox<String> trainNumComboBox = new JComboBox<String>();
	/**
	 * 定义所有JComboBox对应的数组
	 */
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
	private RealTimeAutoCRHPanel realTimeAutoCRHPanel = null;
	/**
	 * 快速运行仿真
	 */
	private RealTimeManualCRHPanel realTimeManualCRHPanel = null;
	/**
	 * 控制运行的按钮
	 */
	private JButton prePageButton, nextPageButton, startButton, pauseButton;
	private JPanel buttonPanel = null; //放置按钮
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
	
	/**
	 * UDP监听线程
	 */
	private SocketConnection socketClient = null;
	
	public RealTimeSimulationPanel() {
		setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		panel.setBounds(10, 10, 1350, 64);
		add(panel);
		panel.setLayout(null);
		
		JLabel label = new JLabel("\u8DEF\u7EBF\u9009\u62E9\uFF1A");
		label.setBounds(10, 24, 60, 15);
		panel.add(label);
		
		//初始化所有下拉框数值
		initComboBoxes();
		
		routeComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//设置route和trainnum级联
				refreshTrainNumComboBox();
			}
		});
		routeComboBox.setBounds(69, 21, 95, 21);
		routeComboBox.setModel(new DefaultComboBoxModel<String>(routeArray));
		panel.add(routeComboBox);
		
		trainNameComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//设置name和id级联
				int selectedIndex = trainNameComboBox.getSelectedIndex();
				trainIdComboBox.setSelectedIndex(selectedIndex);
			}
		});
		trainNameComboBox.setBounds(256, 21, 95, 21);
		trainNameComboBox.setModel(new DefaultComboBoxModel<String>(trainNameArray));
		panel.add(trainNameComboBox);
		
		JLabel label_1 = new JLabel("\u7F16\u7EC4\u9009\u62E9\uFF1A");
		label_1.setBounds(197, 24, 60, 15);
		panel.add(label_1);
		
		trainNumComboBox.setBounds(424, 21, 95, 21);
		trainNumComboBox.setModel(new DefaultComboBoxModel<String>(trainNumArray));
		panel.add(trainNumComboBox);
		
		JLabel label_4 = new JLabel("\u8F66\u6B21\uFF1A");
		label_4.setBounds(390, 24, 60, 15);
		panel.add(label_4);
		
		JButton btnNewButton = new JButton("...");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String routeName = (String) routeComboBox.getSelectedItem();
				if(DEFAULT_COMBOBOX_ITEM.equals(routeName)){
					JOptionPane.showMessageDialog(RealTimeSimulationPanel.this, "请选择路线！");
				}else{
					RouteDataTrainNumDialog routeDataTrainNumDialog = new RouteDataTrainNumDialog(null, routeName);
					//刷新车次
					refreshTrainNumComboBox();
				}
			}
		});
		btnNewButton.setBounds(521, 20, 25, 23);
		panel.add(btnNewButton);
		
		JButton button = new JButton("快速运行");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(RunDataCalculatorRealTime.rundataBeansList != null){
					//显示运行界面
					int routeId = getRouteId();
//					int routeId = MinTimeSimulationService.getRouteIdByName("京津线（下行）"); //*调试
					realTimeManualCRHPanel = new RealTimeManualCRHPanel(routeId, RunDataCalculatorRealTime.rundataBeansList);
					realTimeManualCRHPanel.setBounds(10, 84, 1350, 544);
					displayUI(RealTimeSimulationPanel.MANUAL_MODEL);//显示快速运行的panel及按钮
					updateUI();
				}else{
					JOptionPane.showMessageDialog(RealTimeSimulationPanel.this, "请先计算运行数据！");
				}
			}
		});
		button.setBounds(1037, 6, 105, 23);
		panel.add(button);
		
		JButton autoRunButton = new JButton("动画运行");
		autoRunButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(RunDataCalculatorRealTime.rundataBeansList != null){
					int routeId = getRouteId();
//					int routeId = MinTimeSimulationService.getRouteIdByName("京津线（下行）"); //*调试
					realTimeAutoCRHPanel = new RealTimeAutoCRHPanel(routeId, RunDataCalculatorRealTime.rundataBeansList);
					realTimeAutoCRHPanel.setBounds(10, 84, 1350, 544);
					autoRunThread = new Thread(realTimeAutoCRHPanel);
					autoRunThread.start();
					displayUI(RealTimeSimulationPanel.AUTO_MODEL);//显示动画运行的panel及按钮
					updateUI();
				}else{
					JOptionPane.showMessageDialog(RealTimeSimulationPanel.this, "请先计算运行数据！");
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
					launchUDPListeningThread();//启动UDP监听线程
					RunDataCalculatorRealTime runDataCalculatorRealTime = new RunDataCalculatorRealTime(routeStr, trainNumStr, trainId);
//					RunDataCalculatorRealTime runDataCalculatorRealTime = new RunDataCalculatorRealTime("京津线（下行）", "G99", 57);//*调试用
					new Thread(runDataCalculatorRealTime).start();
				}else{
					JOptionPane.showMessageDialog(RealTimeSimulationPanel.this, "参数不完整，请重新选择！");
				}
			}
		});
		calButton.setBounds(800, 6, 105, 23);
		panel.add(calButton);
		
		trainIdComboBox.setBounds(600, 21, 95, 21);
		trainIdComboBox.setModel(new DefaultComboBoxModel<Integer>(trainIdArray));
		panel.add(trainIdComboBox);
		trainIdComboBox.setVisible(false);
		
		JButton button_5 = new JButton("计算结果");
		button_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(RunDataCalculatorRealTime.rundataBeansList != null){
					String routeNameStr = (String) routeComboBox.getSelectedItem();
					new DisplayRunDataDialog(routeNameStr, RunDataCalculatorRealTime.rundataBeansList);
				}
			}
		});
		button_5.setBounds(800, 34, 105, 23);
		panel.add(button_5);
		
		JButton btnNewButton_1 = new JButton("图表");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(RunDataCalculatorRealTime.rundataBeansList != null){
					new RundataChartDialog("常态运行数据曲线", RunDataCalculatorRealTime.rundataBeansList);
				}
			}
		});
		btnNewButton_1.setBounds(925, 6, 93, 23);
		panel.add(btnNewButton_1);
		
		JButton button_1 = new JButton("生成报告");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(checkParameters()){//参数正常
					String routeStr = (String) routeComboBox.getSelectedItem();
					String trainNumStr = (String) trainNumComboBox.getSelectedItem();
					int trainId = (Integer) trainIdComboBox.getSelectedItem();
					String trainCategory = (String) trainNameComboBox.getSelectedItem();
					if(RunDataCalculatorRealTime.rundataBeansList.size() != 0){
						try {
							JFileChooser docFileChooser = new JFileChooser();
							FileNameExtensionFilter fileFilter = new FileNameExtensionFilter(".doc", "doc");
							docFileChooser.setFileFilter(fileFilter);
							docFileChooser.setFileSelectionMode(JFileChooser.SAVE_DIALOG);
							docFileChooser.setVisible(true);//显示
							int retval = docFileChooser.showSaveDialog(RealTimeSimulationPanel.this);
							String savePath = "";
							if (retval == JFileChooser.APPROVE_OPTION) {
								// 得到用户选择文件的绝对路径
								savePath = docFileChooser.getSelectedFile().getAbsolutePath() + ".doc";
								CreateDocThread createDocThread = new CreateDocThread(routeStr, trainNumStr, trainId, trainCategory, RunDataCalculatorRealTime.rundataBeansList, savePath);
								new DocInvokeAndWaitDialog(createDocThread);
							}
						} catch (Exception e1) {
							JOptionPane.showMessageDialog(RealTimeSimulationPanel.this, "文档保存失败！");
							return;
						}
					}
				}else{
					JOptionPane.showMessageDialog(RealTimeSimulationPanel.this, "参数不完整，请重新选择！");
				}
			}
		});
		button_1.setBounds(925, 34, 93, 23);
		panel.add(button_1);
		
		JButton button_2 = new JButton("网络设置");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new NetworkConfDialog();
			}
		});
		button_2.setBounds(1166, 6, 105, 23);
		panel.add(button_2);
		
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
				realTimeManualCRHPanel.run(RUN_LENGTH_BACKWARD);
			}
		});
		prePageButton.setBounds(564, 5, 93, 23);
		
		nextPageButton = new JButton("下一页");
		nextPageButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				realTimeManualCRHPanel.run(RUN_LENGTH_FORWARD);
			}
		});
		nextPageButton.setBounds(690, 5, 93, 23);
		
		//动画运行对应你的按钮
		startButton = new JButton("开始");
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RealTimeAutoCRHPanel.isPause = false;
			}
		});
		startButton.setBounds(564, 5, 93, 23);
		
		pauseButton = new JButton("暂停");
		pauseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RealTimeAutoCRHPanel.isPause = true;
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
		tractionLevelArray = new String[2];
		tractionLevelArray[0] = DEFAULT_COMBOBOX_ITEM;
		tractionLevelArray[1] = "最大牵引";
		
		//制动级位
		brakeLevelArray = new String[2];
		brakeLevelArray[0] = DEFAULT_COMBOBOX_ITEM;
		brakeLevelArray[1] = "8";
		
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
		if (routeComboBox.getSelectedIndex() != 0
				&& trainNameComboBox.getSelectedIndex() != 0
				&& trainNumComboBox.getSelectedIndex() != 0) {
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
		if(type == RealTimeSimulationPanel.MANUAL_MODEL){
			buttonPanel.add(prePageButton);
			buttonPanel.add(nextPageButton);
		}else if(type == RealTimeSimulationPanel.AUTO_MODEL){
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
		if(type == RealTimeSimulationPanel.MANUAL_MODEL){
			if(realTimeAutoCRHPanel != null){
				remove(realTimeAutoCRHPanel);
			}
			add(realTimeManualCRHPanel);//显示快速运行的panel
			displayButton(type);
		}else if(type == RealTimeSimulationPanel.AUTO_MODEL){
			if(realTimeManualCRHPanel != null){
				remove(realTimeManualCRHPanel);
			}
			add(realTimeAutoCRHPanel);//显示动画运行的panel
			displayButton(type);
		}
	}
	
	/**
	 * 启动UDP监听线程
	 */
	public void launchUDPListeningThread(){
		if(socketClient == null){
			socketClient = new SocketConnection();
			Thread UDPThread = new Thread(socketClient);
			UDPThread.start();
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
