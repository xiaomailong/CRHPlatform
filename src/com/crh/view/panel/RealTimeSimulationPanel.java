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
 * ��̬���з���Ľ���
 * @author huhui
 *
 */
public class RealTimeSimulationPanel extends JPanel {
	//��������JComboBox
	/**
	 * ��·ѡ��
	 */
	private JComboBox<String> routeComboBox = new JComboBox<String>();
	/**
	 * ����ѡ��
	 */
	private JComboBox<String> trainNameComboBox = new JComboBox<String>();
	private JComboBox<Integer> trainIdComboBox = new JComboBox<Integer>();
	/**
	 * ����
	 */
	private JComboBox<String> trainNumComboBox = new JComboBox<String>();
	/**
	 * ��������JComboBox��Ӧ������
	 */
	private Integer [] trainIdArray;
	private String [] trainNameArray, routeArray, tractionLevelArray, brakeLevelArray, trainNumArray;
	private final String DEFAULT_COMBOBOX_ITEM = "��ѡ�񡭡�";
	//�������������صĿؼ�
	/**
	 * �հ׵����
	 */
	private JPanel blankPanel = null;
	/**
	 * ��������
	 */
	private RealTimeAutoCRHPanel realTimeAutoCRHPanel = null;
	/**
	 * �������з���
	 */
	private RealTimeManualCRHPanel realTimeManualCRHPanel = null;
	/**
	 * �������еİ�ť
	 */
	private JButton prePageButton, nextPageButton, startButton, pauseButton;
	private JPanel buttonPanel = null; //���ð�ť
	private Thread autoRunThread = null;
	
	/**
	 * ��ҳ�Ĵ�С
	 */
	private double RUN_LENGTH_FORWARD = 500;
	private double RUN_LENGTH_BACKWARD = -RUN_LENGTH_FORWARD;
	
	/**
	 * 0��ʾ�������У�1��ʾ��������
	 */
	public static int MANUAL_MODEL = 0, AUTO_MODEL = 1;
	
	/**
	 * UDP�����߳�
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
		
		//��ʼ��������������ֵ
		initComboBoxes();
		
		routeComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//����route��trainnum����
				refreshTrainNumComboBox();
			}
		});
		routeComboBox.setBounds(69, 21, 95, 21);
		routeComboBox.setModel(new DefaultComboBoxModel<String>(routeArray));
		panel.add(routeComboBox);
		
		trainNameComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//����name��id����
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
					JOptionPane.showMessageDialog(RealTimeSimulationPanel.this, "��ѡ��·�ߣ�");
				}else{
					RouteDataTrainNumDialog routeDataTrainNumDialog = new RouteDataTrainNumDialog(null, routeName);
					//ˢ�³���
					refreshTrainNumComboBox();
				}
			}
		});
		btnNewButton.setBounds(521, 20, 25, 23);
		panel.add(btnNewButton);
		
		JButton button = new JButton("��������");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(RunDataCalculatorRealTime.rundataBeansList != null){
					//��ʾ���н���
					int routeId = getRouteId();
//					int routeId = MinTimeSimulationService.getRouteIdByName("�����ߣ����У�"); //*����
					realTimeManualCRHPanel = new RealTimeManualCRHPanel(routeId, RunDataCalculatorRealTime.rundataBeansList);
					realTimeManualCRHPanel.setBounds(10, 84, 1350, 544);
					displayUI(RealTimeSimulationPanel.MANUAL_MODEL);//��ʾ�������е�panel����ť
					updateUI();
				}else{
					JOptionPane.showMessageDialog(RealTimeSimulationPanel.this, "���ȼ����������ݣ�");
				}
			}
		});
		button.setBounds(1037, 6, 105, 23);
		panel.add(button);
		
		JButton autoRunButton = new JButton("��������");
		autoRunButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(RunDataCalculatorRealTime.rundataBeansList != null){
					int routeId = getRouteId();
//					int routeId = MinTimeSimulationService.getRouteIdByName("�����ߣ����У�"); //*����
					realTimeAutoCRHPanel = new RealTimeAutoCRHPanel(routeId, RunDataCalculatorRealTime.rundataBeansList);
					realTimeAutoCRHPanel.setBounds(10, 84, 1350, 544);
					autoRunThread = new Thread(realTimeAutoCRHPanel);
					autoRunThread.start();
					displayUI(RealTimeSimulationPanel.AUTO_MODEL);//��ʾ�������е�panel����ť
					updateUI();
				}else{
					JOptionPane.showMessageDialog(RealTimeSimulationPanel.this, "���ȼ����������ݣ�");
				}
			}
		});
		autoRunButton.setBounds(1037, 34, 105, 23);
		panel.add(autoRunButton);
		
		JButton calButton = new JButton("����");
		calButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//�������
				if(checkParameters()){//��������
					String routeStr = (String) routeComboBox.getSelectedItem();
					String trainNumStr = (String) trainNumComboBox.getSelectedItem();
					int trainId = (Integer) trainIdComboBox.getSelectedItem();
					launchUDPListeningThread();//����UDP�����߳�
					RunDataCalculatorRealTime runDataCalculatorRealTime = new RunDataCalculatorRealTime(routeStr, trainNumStr, trainId);
//					RunDataCalculatorRealTime runDataCalculatorRealTime = new RunDataCalculatorRealTime("�����ߣ����У�", "G99", 57);//*������
					new Thread(runDataCalculatorRealTime).start();
				}else{
					JOptionPane.showMessageDialog(RealTimeSimulationPanel.this, "������������������ѡ��");
				}
			}
		});
		calButton.setBounds(800, 6, 105, 23);
		panel.add(calButton);
		
		trainIdComboBox.setBounds(600, 21, 95, 21);
		trainIdComboBox.setModel(new DefaultComboBoxModel<Integer>(trainIdArray));
		panel.add(trainIdComboBox);
		trainIdComboBox.setVisible(false);
		
		JButton button_5 = new JButton("������");
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
		
		JButton btnNewButton_1 = new JButton("ͼ��");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(RunDataCalculatorRealTime.rundataBeansList != null){
					new RundataChartDialog("��̬������������", RunDataCalculatorRealTime.rundataBeansList);
				}
			}
		});
		btnNewButton_1.setBounds(925, 6, 93, 23);
		panel.add(btnNewButton_1);
		
		JButton button_1 = new JButton("���ɱ���");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(checkParameters()){//��������
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
							docFileChooser.setVisible(true);//��ʾ
							int retval = docFileChooser.showSaveDialog(RealTimeSimulationPanel.this);
							String savePath = "";
							if (retval == JFileChooser.APPROVE_OPTION) {
								// �õ��û�ѡ���ļ��ľ���·��
								savePath = docFileChooser.getSelectedFile().getAbsolutePath() + ".doc";
								CreateDocThread createDocThread = new CreateDocThread(routeStr, trainNumStr, trainId, trainCategory, RunDataCalculatorRealTime.rundataBeansList, savePath);
								new DocInvokeAndWaitDialog(createDocThread);
							}
						} catch (Exception e1) {
							JOptionPane.showMessageDialog(RealTimeSimulationPanel.this, "�ĵ�����ʧ�ܣ�");
							return;
						}
					}
				}else{
					JOptionPane.showMessageDialog(RealTimeSimulationPanel.this, "������������������ѡ��");
				}
			}
		});
		button_1.setBounds(925, 34, 93, 23);
		panel.add(button_1);
		
		JButton button_2 = new JButton("��������");
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
		
		//�������ж�Ӧ�İ�ť
		prePageButton = new JButton("��һҳ");
		prePageButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				realTimeManualCRHPanel.run(RUN_LENGTH_BACKWARD);
			}
		});
		prePageButton.setBounds(564, 5, 93, 23);
		
		nextPageButton = new JButton("��һҳ");
		nextPageButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				realTimeManualCRHPanel.run(RUN_LENGTH_FORWARD);
			}
		});
		nextPageButton.setBounds(690, 5, 93, 23);
		
		//�������ж�Ӧ��İ�ť
		startButton = new JButton("��ʼ");
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RealTimeAutoCRHPanel.isPause = false;
			}
		});
		startButton.setBounds(564, 5, 93, 23);
		
		pauseButton = new JButton("��ͣ");
		pauseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RealTimeAutoCRHPanel.isPause = true;
			}
		});
		pauseButton.setBounds(690, 5, 93, 23);

	}
	
	/**
	 * ��ʼ������������
	 */
	public void initComboBoxes(){
		//��·ѡ��
		ArrayList<TrainRouteName> routeList = MinTimeSimulationService.getTrainRouteName();
		routeArray = new String[routeList.size() + 1];
		routeArray[0] = DEFAULT_COMBOBOX_ITEM;
		for(int i=0;i<routeList.size();i++){
			routeArray[i+1] = routeList.get(i).getRouteName();
		}
		
		//����ѡ��
		ArrayList<TrainCategory> trainCategoryList = MinTimeSimulationService.getTrainCategory();
		trainIdArray = new Integer[trainCategoryList.size() + 1];
		trainNameArray = new String[trainCategoryList.size() + 1];
		trainIdArray[0] = 0;
		trainNameArray[0] = DEFAULT_COMBOBOX_ITEM;
		for(int i=0;i<trainCategoryList.size();i++){
			trainIdArray[i+1] = trainCategoryList.get(i).getId();
			trainNameArray[i+1] = trainCategoryList.get(i).getName();
		}
		
		//ǣ����λ
		tractionLevelArray = new String[2];
		tractionLevelArray[0] = DEFAULT_COMBOBOX_ITEM;
		tractionLevelArray[1] = "���ǣ��";
		
		//�ƶ���λ
		brakeLevelArray = new String[2];
		brakeLevelArray[0] = DEFAULT_COMBOBOX_ITEM;
		brakeLevelArray[1] = "8";
		
		//����
		trainNumArray = new String[1];
		trainNumArray[0]= DEFAULT_COMBOBOX_ITEM;
		
	}
	
	/**
	 * ���������������¸�ֵ
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
	 * �������
	 */
	public boolean checkParameters(){
		boolean b = false;
		if (routeComboBox.getSelectedIndex() != 0
				&& trainNameComboBox.getSelectedIndex() != 0
				&& trainNumComboBox.getSelectedIndex() != 0) {
			b = true;
		}
//		return true;//*������
		return b;
	}
	
	/**
	 * ����buttonPanel��ʾ��ͬ��button
	 * type: 0��ʾ�������У�1��ʾ��������
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
	 * ��ʾ��Ӧ�ķ���panel����ɾ������panel
	 * type: 0��ʾ�������У�1��ʾ��������
	 */
	public void displayUI(int type){
		remove(blankPanel);
		if(type == RealTimeSimulationPanel.MANUAL_MODEL){
			if(realTimeAutoCRHPanel != null){
				remove(realTimeAutoCRHPanel);
			}
			add(realTimeManualCRHPanel);//��ʾ�������е�panel
			displayButton(type);
		}else if(type == RealTimeSimulationPanel.AUTO_MODEL){
			if(realTimeManualCRHPanel != null){
				remove(realTimeManualCRHPanel);
			}
			add(realTimeAutoCRHPanel);//��ʾ�������е�panel
			displayButton(type);
		}
	}
	
	/**
	 * ����UDP�����߳�
	 */
	public void launchUDPListeningThread(){
		if(socketClient == null){
			socketClient = new SocketConnection();
			Thread UDPThread = new Thread(socketClient);
			UDPThread.start();
		}
	}
	
	/**
	 * ͨ��routeName��ȡrouteId
	 */
	public int getRouteId(){
		String routeNameStr = (String) routeComboBox.getSelectedItem();
		return MinTimeSimulationService.getRouteIdByName(routeNameStr);
	}
}
