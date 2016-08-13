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
import com.crh.doc.CreateDocThread;
import com.crh.service.MinTimeSimulationService;
import com.crh.view.dialog.CalculatingInvokeAndWaitDialog;
import com.crh.view.dialog.DisplayRunDataDialog;
import com.crh.view.dialog.DocInvokeAndWaitDialog;
import com.crh.view.dialog.RouteDataTrainNumDialog;
import com.crh.view.dialog.RundataChartDialog;
import com.crh2.calculate.MergeLists;
import com.crh2.javabean.Rundata;
import com.crh2.javabean.TrainCategory;
import com.crh2.javabean.TrainRouteName;
import com.crh2.javabean.TrainRouteTrainnum;

/**
 * ��Сʱ�����з���Ľ���
 * @author huhui
 *
 */
public class MinTimeSimulationPanel extends JPanel {
	
	private JTextField maxSpeedText;
	private JTextField randomSpeedText;
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
	private MinTimeAutoCRHPanel minTimeAutoCRHPanel = null;
	/**
	 * �������з���
	 */
	private MinTimeManualCRHPanel minTimeManualCRHPanel = null;
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
	
	public MinTimeSimulationPanel() {
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
		
		tractionLevelComboBox.setBounds(449, 7, 105, 21);
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
					JOptionPane.showMessageDialog(MinTimeSimulationPanel.this, "��ѡ��·�ߣ�");
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
				if(MergeLists.mtRundataMergedList != null){
					//��ʾ���н���
					int routeId = getRouteId();
//					int routeId = MinTimeSimulationService.getRouteIdByName("�����ߣ����У�"); //*����
					minTimeManualCRHPanel = new MinTimeManualCRHPanel(routeId, MergeLists.mtRundataMergedList);
					minTimeManualCRHPanel.setBounds(10, 84, 1350, 544);
					displayUI(MinTimeSimulationPanel.MANUAL_MODEL);//��ʾ�������е�panel����ť
					updateUI();
				}else{
					JOptionPane.showMessageDialog(MinTimeSimulationPanel.this, "���ȼ����������ݣ�");
				}
			}
		});
		button.setBounds(1037, 6, 105, 23);
		panel.add(button);
		
		JButton autoRunButton = new JButton("��������");
		autoRunButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(MergeLists.mtRundataMergedList != null){
					int routeId = getRouteId();
//					int routeId = MinTimeSimulationService.getRouteIdByName("�����ߣ����У�"); //*����
					minTimeAutoCRHPanel = new MinTimeAutoCRHPanel(routeId, MergeLists.mtRundataMergedList);
					minTimeAutoCRHPanel.setBounds(10, 84, 1350, 544);
					autoRunThread = new Thread(minTimeAutoCRHPanel);
					autoRunThread.start();
					displayUI(MinTimeSimulationPanel.AUTO_MODEL);//��ʾ�������е�panel����ť
					updateUI();
				}else{
					JOptionPane.showMessageDialog(MinTimeSimulationPanel.this, "���ȼ����������ݣ�");
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
					double randomSpeed = Double.parseDouble(randomSpeedText.getText());
					String tractionLevel = (String) tractionLevelComboBox.getSelectedItem();
					RunDataThread minTimeRunDataThread = new RunDataThread(routeStr, trainNumStr, trainId, maxSpeed, randomSpeed, RunDataThread.MT, 0, tractionLevel);
//					MinTimeRunDataThread minTimeRunDataThread = new MinTimeRunDataThread("�����ߣ����У�", "G99", 57, 300.0, 0.0); //*������
					new CalculatingInvokeAndWaitDialog(minTimeRunDataThread);
					checkMTRundata(MergeLists.mtRundataMergedList);
				}else{
					JOptionPane.showMessageDialog(MinTimeSimulationPanel.this, "������������������ѡ��");
				}
			}
		});
		calButton.setBounds(762, 6, 105, 23);
		panel.add(calButton);
		
		JLabel label_6 = new JLabel("\u9650\u901F\u8BEF\u5DEE\uFF1A");
		label_6.setBounds(390, 38, 60, 15);
		panel.add(label_6);
		
		randomSpeedText = new JTextField();
		randomSpeedText.setText("0");
		randomSpeedText.setColumns(10);
		randomSpeedText.setBounds(449, 35, 95, 21);
		panel.add(randomSpeedText);
		
		JLabel lblKmh = new JLabel("km/h");
		lblKmh.setBounds(551, 38, 54, 15);
		panel.add(lblKmh);
		
		trainIdComboBox.setBounds(647, 35, 95, 21);
		trainIdComboBox.setModel(new DefaultComboBoxModel<Integer>(trainIdArray));
		panel.add(trainIdComboBox);
		
		JButton button_5 = new JButton("������");
		button_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(MergeLists.mtRundataMergedList != null){
					String routeNameStr = (String) routeComboBox.getSelectedItem();
					new DisplayRunDataDialog(routeNameStr, MergeLists.mtRundataMergedList);
				}
			}
		});
		button_5.setBounds(762, 34, 105, 23);
		panel.add(button_5);
		
		JButton btnNewButton_1 = new JButton("ͼ��");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(MergeLists.mtRundataMergedList != null){
					new RundataChartDialog("��Сʱ����������", MergeLists.mtRundataMergedList);
				}
			}
		});
		btnNewButton_1.setBounds(891, 6, 105, 23);
		panel.add(btnNewButton_1);
		
		JButton createDocBtn = new JButton("���ɱ���");
		createDocBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(checkParameters()){//��������
					String routeStr = (String) routeComboBox.getSelectedItem();
					String trainNumStr = (String) trainNumComboBox.getSelectedItem();
					int trainId = (Integer) trainIdComboBox.getSelectedItem();
					String trainCategory = (String) trainNameComboBox.getSelectedItem();
					if(MergeLists.mtRundataMergedList.size() != 0){
						try {
							JFileChooser docFileChooser = new JFileChooser();
							FileNameExtensionFilter fileFilter = new FileNameExtensionFilter(".doc", "doc");
							docFileChooser.setFileFilter(fileFilter);
							docFileChooser.setFileSelectionMode(JFileChooser.SAVE_DIALOG);
							docFileChooser.setVisible(true);//��ʾ
							int retval = docFileChooser.showSaveDialog(MinTimeSimulationPanel.this);
							String savePath = "";
							if (retval == JFileChooser.APPROVE_OPTION) {
								// �õ��û�ѡ���ļ��ľ���·��
								savePath = docFileChooser.getSelectedFile().getAbsolutePath() + ".doc";
								CreateDocThread createDocThread = new CreateDocThread(routeStr, trainNumStr, trainId, trainCategory, MergeLists.mtRundataMergedList, savePath);
								new DocInvokeAndWaitDialog(createDocThread);
							}
						} catch (Exception e1) {
							e1.printStackTrace();
							JOptionPane.showMessageDialog(MinTimeSimulationPanel.this, "�ĵ�����ʧ�ܣ�");
							return;
						}
					}
				}else{
					JOptionPane.showMessageDialog(MinTimeSimulationPanel.this, "������������������ѡ��");
				}
			}
		});
		createDocBtn.setBounds(891, 34, 105, 23);
		panel.add(createDocBtn);
		
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
				MinTimeAutoCRHPanel.isPause = false;
			}
		});
		startButton.setBounds(564, 5, 93, 23);
		
		pauseButton = new JButton("��ͣ");
		pauseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MinTimeAutoCRHPanel.isPause = true;
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
		String randomSpeedStr = randomSpeedText.getText().trim();
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
		if(type == MinTimeSimulationPanel.MANUAL_MODEL){
			buttonPanel.add(prePageButton);
			buttonPanel.add(nextPageButton);
		}else if(type == MinTimeSimulationPanel.AUTO_MODEL){
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
		if(type == MinTimeSimulationPanel.MANUAL_MODEL){
			if(minTimeAutoCRHPanel != null){
				remove(minTimeAutoCRHPanel);
			}
			add(minTimeManualCRHPanel);//��ʾ�������е�panel
			displayButton(type);
		}else if(type == MinTimeSimulationPanel.AUTO_MODEL){
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
	
	/**
	 * 2.15.4.25��У��@MergeLists.mtRundataMergedList�е�����
	 */
	public ArrayList<Rundata> checkMTRundata(ArrayList<Rundata> mtRundataMergedList) {
		ArrayList<Rundata> mtRundataList = mtRundataMergedList;
		double tempElecBrakeForcePower = 0, tempValue = 0;
		int size = mtRundataList.size();
		for (int i = 10; i < size; i++) {// i��10��ʼ���޳��׸�Ϊ0��ֵ
			Rundata bean = mtRundataList.get(i);
			//У��TractionPower()
			if (bean.getActualTractionPower() == 0) { // ˵�������ƶ��׶Σ�ǣ����Ϊ0
				Rundata preBean = mtRundataList.get(i - 1);
				bean.setActualTractionPower(preBean.getActualTractionPower());// ��ǣ����������Ϊ0
				if (i < size - 1) {
					Rundata postBean = mtRundataList.get(i + 1);// �����ǣ����������ȥ��ֵ
					if (postBean.getActualTractionPower() > bean.getActualTractionPower()) {
						double diff = postBean.getActualTractionPower()- bean.getActualTractionPower();// ��ֵ
						for (int k = i + 1; k < size; k++) {
							Rundata tempBean = mtRundataList.get(k);
							if (tempBean.getActualTractionPower() > 0) {
								double power = tempBean.getActualTractionPower();
								tempBean.setActualTractionPower(power - diff);
							}
						}
					}
				}
			}
			
			tempElecBrakeForcePower = bean.getElecBrakeForcePower();
			//У��ElecBrakeForcePower
			if(bean.getElecBrakeForcePower() != 0 && i <size-1){
				Rundata postBean = mtRundataList.get(i + 1);
				if(postBean.getElecBrakeForcePower() == 0){
					postBean.setElecBrakeForcePower(bean.getElecBrakeForcePower());
				}else{
					if(postBean.getElecBrakeForcePower() > tempElecBrakeForcePower){//Ѱ��ÿ���׶εķ�ֵ
						tempElecBrakeForcePower = postBean.getElecBrakeForcePower();
					}else{
						for(int k=i+1;k<size;k++){
							Rundata tempBean = mtRundataList.get(k);
							if(tempBean.getElecBrakeForcePower() > 0){
								double power = tempBean.getElecBrakeForcePower();
								tempBean.setElecBrakeForcePower(power + tempElecBrakeForcePower - tempValue); //��ֹ�ظ�����
							}
						}
						tempValue = tempElecBrakeForcePower;
					}
				}
			}
		}
		
		for (int j = 0; j < size; j++) {
			Rundata bean = mtRundataList.get(j);
			bean.setTractionPower(bean.getActualTractionPower());
		}
		return mtRundataList;
	}
	
}
