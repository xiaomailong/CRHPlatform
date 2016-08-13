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
 * �̶�ʱ�ֽ��ܿ������з���Ľ���
 * @author huhui
 *
 */
public class SpecifiedTimeEnergySimulationPanel extends JPanel {
	
	private JTextField maxSpeedText;
	private JTextField runTimeText;
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
	 * ǣ����λ
	 */
	private JComboBox<String> tractionLevelComboBox = new JComboBox<String>();
	/**
	 * �ƶ���λ
	 */
	private JComboBox<String> brakeLevelComboBox = new JComboBox<String>();
	/**
	 * ����
	 */
	private JComboBox<String> trainNumComboBox = new JComboBox<String>();
	//��������JComboBox��Ӧ������
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
	private SpecifiedTimeEnergyAutoCRHPanel minTimeAutoCRHPanel = null;
	/**
	 * �������з���
	 */
	private SpecidiedTimeEnergyManualCRHPanel minTimeManualCRHPanel = null;
	/**
	 * �������еİ�ť
	 */
	private JButton prePageButton, nextPageButton, startButton, pauseButton;
	/**
	 * ���ð�ť
	 */
	private JPanel buttonPanel = null;
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
		
		//��ʼ��������������ֵ
		initComboBoxes();
		
		routeComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//����route��trainnum����
				refreshTrainNumComboBox();
			}
		});
		routeComboBox.setBounds(69, 7, 95, 21);
		routeComboBox.setModel(new DefaultComboBoxModel<String>(routeArray));
		panel.add(routeComboBox);
		trainIdComboBox.setVisible(false);
		trainNameComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//����name��id����
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
					JOptionPane.showMessageDialog(SpecifiedTimeEnergySimulationPanel.this, "��ѡ��·�ߣ�");
				}else{
					RouteDataTrainNumDialog routeDataTrainNumDialog = new RouteDataTrainNumDialog(null, routeName);
					//ˢ�³���
					refreshTrainNumComboBox();
				}
			}
		});
		btnNewButton.setBounds(141, 34, 25, 23);
		panel.add(btnNewButton);
		
		JLabel lblKm = new JLabel("km/h");
		lblKm.setBounds(358, 38, 54, 15);
		panel.add(lblKm);
		
		JButton button = new JButton("��������");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(MergeLists.rtEnergyRundataMergedList != null){
					//��ʾ���н���
					int routeId = getRouteId();
//					int routeId = MinTimeSimulationService.getRouteIdByName("�����ߣ����У�"); //*����
					minTimeManualCRHPanel = new SpecidiedTimeEnergyManualCRHPanel(routeId, MergeLists.rtEnergyRundataMergedList);
					minTimeManualCRHPanel.setBounds(10, 84, 1350, 544);
					displayUI(SpecifiedTimeEnergySimulationPanel.MANUAL_MODEL);//��ʾ�������е�panel����ť
					updateUI();
				}else{
					JOptionPane.showMessageDialog(SpecifiedTimeEnergySimulationPanel.this, "���ȼ����������ݣ�");
				}
			}
		});
		button.setBounds(1037, 6, 105, 23);
		panel.add(button);
		
		JButton autoRunButton = new JButton("��������");
		autoRunButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(MergeLists.rtEnergyRundataMergedList != null){
					int routeId = getRouteId();
					minTimeAutoCRHPanel = new SpecifiedTimeEnergyAutoCRHPanel(routeId, MergeLists.rtEnergyRundataMergedList);
					minTimeAutoCRHPanel.setBounds(10, 84, 1350, 544);
					autoRunThread = new Thread(minTimeAutoCRHPanel);
					autoRunThread.start();
					displayUI(SpecifiedTimeEnergySimulationPanel.AUTO_MODEL);//��ʾ�������е�panel����ť
					updateUI();
				}else{
					JOptionPane.showMessageDialog(SpecifiedTimeEnergySimulationPanel.this, "���ȼ����������ݣ�");
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
					double maxSpeed = Double.parseDouble(maxSpeedText.getText());
					double randomSpeed = Double.parseDouble(runTimeText.getText());
					String tractionLevel = (String) tractionLevelComboBox.getSelectedItem();
					RunDataThread minTimeRunDataThread = new RunDataThread(routeStr, trainNumStr, trainId, maxSpeed, randomSpeed, RunDataThread.STE, 0, tractionLevel);
					new CalculatingInvokeAndWaitDialog(minTimeRunDataThread);
					V0V1E bean = RunDataCalculatorSpecifiedTime.resultBean;
					JOptionPane.showMessageDialog(SpecifiedTimeEnergySimulationPanel.this, "v0 = "+MyTools.numFormat2(bean.getV0())+", v1 = "+MyTools.numFormat2(bean.getV1())+", E = "+MyTools.numFormat2(bean.getE()));
					MyUtillity.checkTractionPower(MergeLists.rtEnergyRundataMergedList);
				}else{
					JOptionPane.showMessageDialog(SpecifiedTimeEnergySimulationPanel.this, "������������������ѡ��");
				}
			}
		});
		calButton.setBounds(769, 6, 105, 23);
		panel.add(calButton);
		
		JLabel label_6 = new JLabel("����ʱ�䣺");
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
		
		JButton button_5 = new JButton("������");
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
		
		JButton btnNewButton_1 = new JButton("ͼ��");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(MergeLists.rtEnergyRundataMergedList != null){
					new RundataChartDialog("�̶�ʱ�ֽ��ܿ���������������", MergeLists.rtEnergyRundataMergedList);
				}
			}
		});
		btnNewButton_1.setBounds(891, 6, 105, 23);
		panel.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("���ɱ���");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(checkParameters()){//��������
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
							docFileChooser.setVisible(true);//��ʾ
							int retval = docFileChooser.showSaveDialog(SpecifiedTimeEnergySimulationPanel.this);
							String savePath = "";
							if (retval == JFileChooser.APPROVE_OPTION) {
								// �õ��û�ѡ���ļ��ľ���·��
								savePath = docFileChooser.getSelectedFile().getAbsolutePath() + ".doc";
								CreateDocThread createDocThread = new CreateDocThread(routeStr, trainNumStr, trainId, trainCategory, MergeLists.rtEnergyRundataMergedList, savePath);
								new DocInvokeAndWaitDialog(createDocThread);
							}
						} catch (Exception e1) {
							JOptionPane.showMessageDialog(SpecifiedTimeEnergySimulationPanel.this, "�ĵ�����ʧ�ܣ�");
							return;
						}
					}
				}else{
					JOptionPane.showMessageDialog(SpecifiedTimeEnergySimulationPanel.this, "������������������ѡ��");
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
		
		//�������ж�Ӧ�İ�ť
		prePageButton = new JButton("��һҳ");
		prePageButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				minTimeManualCRHPanel.run(RUN_LENGTH_BACKWARD);
			}
		});
		prePageButton.setBounds(564, 5, 93, 23);
		
		nextPageButton = new JButton("��һҳ");
		nextPageButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				minTimeManualCRHPanel.run(RUN_LENGTH_FORWARD);
			}
		});
		nextPageButton.setBounds(690, 5, 93, 23);
		
		//�������ж�Ӧ��İ�ť
		startButton = new JButton("��ʼ");
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SpecifiedTimeEnergyAutoCRHPanel.isPause = false;
			}
		});
		startButton.setBounds(564, 5, 93, 23);
		
		pauseButton = new JButton("��ͣ");
		pauseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SpecifiedTimeEnergyAutoCRHPanel.isPause = true;
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
		tractionLevelArray = new String[17];
		tractionLevelArray[0] = DEFAULT_COMBOBOX_ITEM;
		tractionLevelArray[1] = "���ǣ��(�޼�)";
		tractionLevelArray[2] = "1��";
		tractionLevelArray[3] = "2��";
		tractionLevelArray[4] = "3��";
		tractionLevelArray[5] = "4��";
		tractionLevelArray[6] = "5��";
		tractionLevelArray[7] = "6��";
		tractionLevelArray[8] = "7��";
		tractionLevelArray[9] = "8��";
		tractionLevelArray[10] = "9��";
		tractionLevelArray[11] = "10��";
		tractionLevelArray[12] = "11��";
		tractionLevelArray[13] = "12��";
		tractionLevelArray[14] = "13��";
		tractionLevelArray[15] = "14��";
		tractionLevelArray[16] = "15��";
		
		//�ƶ���λ
		brakeLevelArray = new String[2];
		brakeLevelArray[0] = DEFAULT_COMBOBOX_ITEM;
		brakeLevelArray[1] = "����ƶ�";
		
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
//		return true;//*������
		return b;
	}
	
	/**
	 * ����buttonPanel��ʾ��ͬ��button
	 * type: 0��ʾ�������У�1��ʾ��������
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
	 * ��ʾ��Ӧ�ķ���panel����ɾ������panel
	 * type: 0��ʾ�������У�1��ʾ��������
	 */
	public void displayUI(int type){
		remove(blankPanel);
		if(type == SpecifiedTimeEnergySimulationPanel.MANUAL_MODEL){
			if(minTimeAutoCRHPanel != null){
				remove(minTimeAutoCRHPanel);
			}
			add(minTimeManualCRHPanel);//��ʾ�������е�panel
			displayButton(type);
		}else if(type == SpecifiedTimeEnergySimulationPanel.AUTO_MODEL){
			if(minTimeManualCRHPanel != null){
				remove(minTimeManualCRHPanel);
			}
			add(minTimeAutoCRHPanel);//��ʾ�������е�panel
			displayButton(type);
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
