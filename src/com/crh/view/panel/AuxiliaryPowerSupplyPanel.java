package com.crh.view.panel;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import com.crh.service.AuxiliaryPowerSupplyPanelService;
import com.crh.service.TrainEditPanelService;
import com.crh2.javabean.TrainCategory;
import com.crh2.javabean.TrainPowerSupply;
import com.crh2.util.MyTools;
import javax.swing.ListSelectionModel;
import javax.swing.JTextField;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * ��������Panel��������
 * @author huhui
 *
 */
public class AuxiliaryPowerSupplyPanel extends JPanel {
	
	/**
	 * �����˵�
	 */
	private static final JComboBox<Integer> trainIdComboBox = new JComboBox<Integer>();
	private static final JComboBox<String> trainNameComboBox = new JComboBox<String>();
	/**
	 * ���С��г����ơ�
	 */
	private Integer [] trainCategoryIdArray;
	private String[] trainCategoryNameArray;
	
	/**
	 * ���������������
	 */
	private final String TABLE = "train_power_supply";
	
	private JTabbedPane tabbedPane;
	/**
	 * ����JTabbedPane�е�tabҳ
	 */
	private JPanel [] panelTab;
	private JTable [] loadTable;
	private JScrollPane [] loadTableScrollPane;
	private Vector<String> columnNamesVector;// LoadTable������
	private JTable table_1;
	private JTextField para1;
	private JTextField para2;
	/**
	 * ������Ŀ
	 */
	private int carCount = 0;
	/**
	 * ����ͳ����Ϣ
	 */
	private JLabel label1,label2,label3,label4,label5,label6;

	public AuxiliaryPowerSupplyPanel() {
		setLayout(null);
		
		getAllTrainCategoryToArray();//��ʼ�����������������
		
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		panel.setBounds(10, 10, 1328, 36);
		add(panel);
		
		JLabel label = new JLabel("\u7F16\u7EC4\u7F16\u53F7\uFF1A");
		label.setBounds(10, 10, 144, 15);
		panel.add(label);
		
		trainNameComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// ����JComboBox����
				int selectedIndex = trainNameComboBox.getSelectedIndex();
				trainIdComboBox.setSelectedIndex(selectedIndex);
			}
		});
		trainNameComboBox.setModel(new DefaultComboBoxModel<String>(trainCategoryNameArray));
		trainNameComboBox.setBounds(90, 7, 117, 21);
		panel.add(trainNameComboBox);
		
		JButton button_1 = new JButton("ȷ��");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int categoryId = (Integer) trainIdComboBox.getSelectedItem();
				if (categoryId == 0) {
					JOptionPane.showMessageDialog(AuxiliaryPowerSupplyPanel.this,"��ѡ��������ƣ�");
					return;
				}
				int trainInfoCount = TrainEditPanelService.getTrainInfoCount(categoryId);
				if(trainInfoCount == 0){
					JOptionPane.showMessageDialog(AuxiliaryPowerSupplyPanel.this, "�ó����г�������Ϣ������δ���棬�뷵�ء��г����ݡ����棡");
					return;
				}
				generatePanelTab(categoryId);
				showInfo();
			}
		});
		button_1.setBounds(245, 6, 93, 23);
		panel.add(button_1);
		
		trainIdComboBox.setBounds(711, 7, 76, 21);
		trainIdComboBox.setModel(new DefaultComboBoxModel<Integer>(trainCategoryIdArray));
		panel.add(trainIdComboBox);
		trainIdComboBox.setVisible(false);
		
		JButton button_2 = new JButton("����");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int categoryId = (Integer) trainIdComboBox.getSelectedItem();
				if (categoryId == 0) {
					JOptionPane.showMessageDialog(AuxiliaryPowerSupplyPanel.this,"��ѡ��������ƣ�");
					return;
				}
				savePowerSupply(categoryId);
			}
		});
		button_2.setBounds(444, 6, 93, 23);
		panel.add(button_2);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBorder(new TitledBorder(null, "\u8F66\u8F86\u8F85\u52A9\u8D1F\u8F7D\u6C47\u603B", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		tabbedPane.setBounds(10, 56, 1100, 637);
		add(tabbedPane);
		
		JPanel panel_1 = new JPanel();
		panel_1.setLayout(new GridLayout(1, 1));
		
		initColumnName();
		
		DefaultTableModel defaultTableModel = new DefaultTableModel(columnNamesVector, 15);
		table_1 = new JTable(defaultTableModel);
		table_1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		table_1.getColumnModel().getColumn(0).setPreferredWidth(184);
		table_1.getColumnModel().getColumn(1).setPreferredWidth(263);
		table_1.getColumnModel().getColumn(2).setPreferredWidth(165);
		table_1.setBounds(10, 10, 1063, 564);
		panel_1.add(table_1);
		tabbedPane.addTab("���ر�", null, panel_1, null);
		
		
		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "\u4EA4\u6D41\u8D1F\u8F7D\u7EDF\u8BA1\u4FE1\u606F", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_4.setBounds(1113, 56, 225, 328);
		add(panel_4);
		panel_4.setLayout(null);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "\u51AC\u5B63", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_3.setBounds(10, 21, 205, 110);
		panel_4.add(panel_3);
		panel_3.setLayout(null);
		
		JLabel label_3 = new JLabel("\u5168\u8F66\u4EA4\u6D41\u8F85\u52A9\u8D1F\u8F7D\u529F\u7387\uFF1A");
		label_3.setBounds(10, 20, 140, 15);
		panel_3.add(label_3);
		
		label1 = new JLabel("");
		label1.setBounds(141, 20, 64, 15);
		panel_3.add(label1);
		
		JLabel label_4 = new JLabel("\u5168\u8F66\u6240\u6709\u8F85\u52A9\u53D8");
		label_4.setBounds(10, 45, 92, 15);
		panel_3.add(label_4);
		
		JLabel label_5 = new JLabel("\u6D41\u5668\u5BB9\u91CF\u4E4B\u548C\uFF1A");
		label_5.setBounds(10, 58, 84, 15);
		panel_3.add(label_5);
		
		label2 = new JLabel("");
		label2.setBounds(104, 52, 74, 15);
		panel_3.add(label2);
		
		JLabel label_7 = new JLabel("\u5168\u8F66\u8F85\u52A9\u53D8\u6D41\u5668\u6570\u91CF\uFF1A");
		label_7.setBounds(10, 83, 120, 15);
		panel_3.add(label_7);
		
		label3 = new JLabel("");
		label3.setBounds(131, 83, 74, 15);
		panel_3.add(label3);
		
		JPanel panel_5 = new JPanel();
		panel_5.setLayout(null);
		panel_5.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "\u590F\u5B63", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_5.setBounds(10, 150, 205, 110);
		panel_4.add(panel_5);
		
		JLabel label_6 = new JLabel("\u5168\u8F66\u4EA4\u6D41\u8F85\u52A9\u8D1F\u8F7D\u529F\u7387\uFF1A");
		label_6.setBounds(10, 20, 140, 15);
		panel_5.add(label_6);
		
		label4 = new JLabel("");
		label4.setBounds(141, 20, 64, 15);
		panel_5.add(label4);
		
		JLabel label_9 = new JLabel("\u5168\u8F66\u6240\u6709\u8F85\u52A9\u53D8");
		label_9.setBounds(10, 45, 92, 15);
		panel_5.add(label_9);
		
		JLabel label_10 = new JLabel("\u6D41\u5668\u5BB9\u91CF\u4E4B\u548C\uFF1A");
		label_10.setBounds(10, 58, 84, 15);
		panel_5.add(label_10);
		
		label5 = new JLabel("");
		label5.setBounds(104, 52, 74, 15);
		panel_5.add(label5);
		
		JLabel label_12 = new JLabel("\u5168\u8F66\u8F85\u52A9\u53D8\u6D41\u5668\u6570\u91CF\uFF1A");
		label_12.setBounds(10, 83, 120, 15);
		panel_5.add(label_12);
		
		label6 = new JLabel("");
		label6.setBounds(131, 83, 74, 15);
		panel_5.add(label6);
		
		JLabel label_8 = new JLabel("\u8F85\u52A9\u53D8\u6D41\u5668\u4F4D\u7F6E\uFF1A");
		label_8.setBounds(20, 268, 96, 15);
		panel_4.add(label_8);
		
		JLabel label7 = new JLabel("\u4F18\u5148\u63A8\u8350\u5B89\u88C5\u5728\u62D6\u8F66\u4E0A");
		label7.setBounds(54, 285, 126, 15);
		panel_4.add(label7);
		
		JButton btnNewButton = new JButton("���");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				appendOneLine();
			}
		});
		btnNewButton.setBounds(1120, 600, 93, 23);
		add(btnNewButton);
		
		JButton button = new JButton("ɾ��");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeOneLine();
			}
		});
		button.setBounds(1223, 600, 93, 23);
		add(button);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(null, "\u4EA4\u6D41\u8F85\u52A9\u8D1F\u8F7D\u53C2\u6570\u8BBE\u7F6E", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_2.setBounds(1113, 400, 225, 76);
		add(panel_2);
		panel_2.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("\u8F85\u52A9\u53D8\u6D41\u5668\u6548\u7387\uFF1A");
		lblNewLabel.setBounds(10, 22, 101, 15);
		panel_2.add(lblNewLabel);
		
		para1 = new JTextField();
		para1.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				showInfo();
			}
		});
		para1.setText("90");
		para1.setBounds(116, 19, 75, 21);
		panel_2.add(para1);
		para1.setColumns(10);
		
		JLabel label_1 = new JLabel("%");
		label_1.setBounds(198, 22, 54, 15);
		panel_2.add(label_1);
		
		JLabel label_2 = new JLabel("\u8F85\u52A9\u53D8\u6D41\u5668\u5197\u4F59\u5EA6\uFF1A");
		label_2.setBounds(10, 46, 108, 15);
		panel_2.add(label_2);
		
		para2 = new JTextField();
		para2.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				showInfo();
			}
		});
		para2.setText("1.4");
		para2.setColumns(10);
		para2.setBounds(116, 43, 75, 21);
		panel_2.add(para2);

	}
	
	/**
	 * ����һ��
	 */
	public void appendOneLine(){
		int tabIndex = tabbedPane.getSelectedIndex();
		if(tabIndex == -1){
			return;
		}
		JTable table = loadTable[tabIndex];
		DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
		tableModel.insertRow(tableModel.getRowCount()-1, new Vector<String>());
	}
	
	/**
	 * ɾ��һ��
	 */
	public void removeOneLine(){
		int tabIndex = tabbedPane.getSelectedIndex();
		if(tabIndex == -1){
			return;
		}
		JTable table = loadTable[tabIndex];
		DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
		int selectedIndex = table.getSelectedRow();
		if(selectedIndex == -1){
			JOptionPane.showMessageDialog(AuxiliaryPowerSupplyPanel.this, "��ѡ��һ����ɾ����");
			return;
		}
		int option = JOptionPane.showConfirmDialog(AuxiliaryPowerSupplyPanel.this, "ȷ��ɾ����");
		if(option == 0){
			tableModel.removeRow(selectedIndex);
		}
		tableModel.fireTableDataChanged();
	}
	
	/**
	 * ��������ͳ����Ϣ
	 */
	public void showInfo(){
		double para1Val, para2Val;
		try {
			para1Val = Double.parseDouble(para1.getText().trim());
			para2Val  = Double.parseDouble(para2.getText().trim());
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(AuxiliaryPowerSupplyPanel.this, "��������ȷ�ĸ��ز�����");
			return;
		}
		double val1=0,val2=0,val3=0,val4=0,val5=0,val6=0;
		for(int i=0; i<loadTable.length; i++){
			JTable table = loadTable[i];
			DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
			int lastRow = tableModel.getRowCount() - 1;
			Vector lastLine = (Vector) tableModel.getDataVector().elementAt(lastRow);
			//���㶬��
			val1 += Double.parseDouble((String) lastLine.get(1));
			//�����ļ�
			val4 += Double.parseDouble((String) lastLine.get(2));
		}
		val1 = MyTools.numFormat2(val1);
		val4 = MyTools.numFormat2(val4);
		val2 = MyTools.numFormat2((val1 * para2Val)/(para1Val/100));
		val5 = MyTools.numFormat2((val4 * para2Val)/(para1Val/100));
		val3 = Math.floor(carCount/2);
		val6 = Math.floor(carCount/2);
		
		//��ʾͳ����Ϣ
		label1.setText(val1+"");
		label2.setText(val2+"");
		label3.setText(val3+"");
		label4.setText(val4+"");
		label5.setText(val5+"");
		label6.setText(val6+"");
	}
	
	
	
	/**
	 * ��������loadTable��ֵ
	 * @param trainCategoryId
	 */
	public void savePowerSupply(int trainCategoryId){
		ArrayList<TrainPowerSupply> dataList = new ArrayList<TrainPowerSupply>();
		for (int i = 0; i < loadTable.length; i++) {
			int carNo = i+1;
			JTable table = loadTable[i];
			DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
			for (int j = 0; j < tableModel.getRowCount(); j++) {
				TrainPowerSupply bean = new TrainPowerSupply();
				Vector line = (Vector) tableModel.getDataVector().elementAt(j);
				String loadStr = (String)line.get(0);
				if(loadStr == null || loadStr.equals("")){
					JOptionPane.showMessageDialog(AuxiliaryPowerSupplyPanel.this, "\"����\"���Ʋ���Ϊ�գ�");
					tabbedPane.setSelectedIndex(i);
					table.setRowSelectionInterval(j, j);
					return;
				}
				bean.setLoad(loadStr);
				String winterStr = (String)line.get(1);
				bean.setWinter(Double.parseDouble(winterStr == null ? 0+"" : winterStr));
				String summerStr = (String)line.get(2);
				bean.setSummer(Double.parseDouble(summerStr == null ? 0+"" : summerStr));
				bean.setCarNo(carNo);
				bean.setTrainCategoryId(trainCategoryId);
				dataList.add(bean);
			}
		}
		//����table������
		AuxiliaryPowerSupplyPanelService.saveTrainPowerSupply(trainCategoryId, dataList, TABLE);
		//���潻���������ز���
		String para0Str = para1.getText().trim();
		String para1Str = para2.getText().trim();
		if(para0Str == null || "".equals(para0Str) || para1Str == null || "".equals(para1Str)){
			JOptionPane.showMessageDialog(AuxiliaryPowerSupplyPanel.this,"�������ز�������ʧ�ܣ�");
			return;
		}
		double para0Val = Double.parseDouble(para0Str);
		double para1Val = Double.parseDouble(para1Str);
		AuxiliaryPowerSupplyPanelService.saveTrainPowerPara(trainCategoryId, para0Val, para1Val, 0);
		
		JOptionPane.showMessageDialog(AuxiliaryPowerSupplyPanel.this,"����ɹ���");
	}
	
	/**
	 * ����PanelTab
	 * @param trainCategoryId
	 */
	public void generatePanelTab(int trainCategoryId){
		tabbedPane.removeAll();
		int unitNum = AuxiliaryPowerSupplyPanelService.getTrainUnitNum(trainCategoryId);//������Ŀ
		carCount = unitNum;
		panelTab = new JPanel[unitNum];
		loadTable = new JTable[unitNum];
		loadTableScrollPane = new JScrollPane[unitNum];
		for(int i=0;i<unitNum;i++){
			panelTab[i] = new JPanel(new GridLayout(1, 1));
			panelTab[i].setSize(1063, 564);
			tabbedPane.addTab((i+1)+"�����ر�", panelTab[i]);
			loadTable[i] = createLoadTable(i+1, trainCategoryId);
			loadTableScrollPane[i] = new JScrollPane(loadTable[i]);
			panelTab[i].add(loadTableScrollPane[i]);
		}
	}
	
	/**
	 * ����table
	 * @param carNo �����
	 * @param trainCategoryId ����������
	 * @return LoadTable
	 */
	public JTable createLoadTable(int carNo, int trainCategoryId){
		TrainPowerSupply [] beans;
		beans = AuxiliaryPowerSupplyPanelService.getTrainPowerSupply(carNo, trainCategoryId, TABLE);
		if(beans.length == 0){
			beans = TrainPowerSupplyArrayFactory.getTrainPowerSupplyArray(carNo, trainCategoryId);
		}
		//����JTable
		final DefaultTableModel loadTableModel = new DefaultTableModel(columnNamesVector, 0);
		JTable loadTable = new JTable(loadTableModel);
		//��loadTable����¼�����ֵ���
		loadTable.getModel().addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				if(e.getType() == TableModelEvent.UPDATE){
					int rowCount = loadTableModel.getRowCount();
					double winterSum = 0, summerSum = 0;
					for(int i=0;i<rowCount-1;i++){
						String winterStr = (String)loadTableModel.getValueAt(i, 1);
						String summerStr = (String)loadTableModel.getValueAt(i, 2);
						if("".equals(winterStr) || winterStr == null){
							winterStr = "0";
						}
						if("".equals(summerStr) || summerStr == null){
							summerStr = "0";
						}
						double winterItem = 0, summerItem = 0;
						try {
							winterItem = Double.parseDouble(winterStr);
							summerItem = Double.parseDouble(summerStr);
						} catch (NumberFormatException e1) {
							JOptionPane.showMessageDialog(AuxiliaryPowerSupplyPanel.this, "��������ȷ�����֣�");
							return;
						}
						winterSum += winterItem;
						summerSum += summerItem;
					}
					//����table�����һ��
					loadTableModel.removeRow(rowCount-1);
					Vector<String> line = new Vector<String>();
					line.add("�ܼ�");
					line.add(MyTools.numFormat2(winterSum) + "");
					line.add(MyTools.numFormat2(summerSum) + "");
					loadTableModel.addRow(line);
					showInfo();
				}
			}
		});
		
		loadTable.putClientProperty("terminateEditOnFocusLost", true);
		//���
		for(TrainPowerSupply bean : beans){
			Vector<String> line = new Vector<String>();
			line.add(bean.getLoad());
			line.add(bean.getWinter() + "");
			line.add(bean.getSummer() + "");
			loadTableModel.addRow(line);
		}
		return loadTable;
	}
	
	/**
	 * ��ʼ��LoadTable������
	 */
	public void initColumnName(){
		columnNamesVector = new Vector<String>();
		columnNamesVector.add("����");
		columnNamesVector.add("����(KW)");
		columnNamesVector.add("�ļ�(KW)");
	}
	
	/**
	 *  �����ݱ�train_category��������г����ƣ�����ֵ
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
	}
}

/**
 * ���ݲ�ͬ�������ɲ�ͬ��TrainPowerSupply����
 * @author huhui
 *
 */
class TrainPowerSupplyArrayFactory{
	
	/**
	 * ���ɲ�ͬ��TrainPowerSupply����
	 * @param carNo
	 * @param trainCategoryId
	 * @return
	 */
	public static TrainPowerSupply [] getTrainPowerSupplyArray(int carNo, int trainCategoryId){
		TrainPowerSupply [] beans;
		if(carNo == 1){//01��
			beans = new TrainPowerSupply[20];
			for(int i=0;i<beans.length;i++){
				beans[i] = new TrainPowerSupply(carNo, trainCategoryId);
			}
			beans[0].setParameters("�������Ƚ׶�", 35.00, 0);
			beans[1].setParameters("���/ͨ��̨���ȼ�����", 11.40, 0);
			beans[2].setParameters("��Ϣ���ټ���", 0.75, 0);
			beans[3].setParameters("�յ�ѹ����", 0, 32.00);
			beans[4].setParameters("����������", 0, 1.60);
			beans[5].setParameters("����������/������Ԫ", 6.30, 6.30);
			beans[6].setParameters("�������ȱ�ѹ��", 2.00, 0);
			beans[7].setParameters("˾���ҿ���������", 5.20, 0);
			beans[8].setParameters("˾���ҿ�ѹ��", 0, 5.00);
			beans[9].setParameters("˾������������", 0, 0.90);
			beans[10].setParameters("˾������������", 2.00, 2.00);
			beans[11].setParameters("˾�����·����", 0.08, 0.08);
			beans[12].setParameters("�絲��������", 1.10, 0);
			beans[13].setParameters("ǣ����������ȴ��Ԫ", 26.00, 26.00);
			beans[14].setParameters("ǣ�������ȴ��Ԫ", 12.40, 12.40);
			beans[15].setParameters("��ˮ��", 1.00, 1.00);
			beans[16].setParameters("UPS��Դ", 8.60, 8.60);
			beans[17].setParameters("����ѹ��", 18.10, 18.10);
			beans[18].setParameters("ͨ�����2̨��", 11.60, 11.60);
			double[] sum = loadSum(beans);
			beans[19].setParameters("�ܼ�", sum[0], sum[1]);
		}else if(carNo == 2){
			beans = new TrainPowerSupply[14];
			for(int i=0;i<beans.length;i++){
				beans[i] = new TrainPowerSupply(carNo, trainCategoryId);
			}
			beans[0].setParameters("�������Ƚ׶�", 35.00, 0);
			beans[1].setParameters("���/ͨ��̨���ȼ�����", 11.40, 0);
			beans[2].setParameters("��Ϣ���ټ���", 0.75, 0);
			beans[3].setParameters("�յ�ѹ����", 0, 32.00);
			beans[4].setParameters("����������", 0, 1.60);
			beans[5].setParameters("����������/������Ԫ", 6.30, 6.30);
			beans[6].setParameters("�������ȱ�ѹ��", 2.00, 0);
			beans[7].setParameters("ǣ����������ȴ��Ԫ", 26.00, 26.00);
			beans[8].setParameters("ǣ�������ȴ��Ԫ", 12.40, 12.40);
			beans[9].setParameters("��ˮ��", 1.00, 1.00);
			beans[10].setParameters("UPS��Դ", 8.60, 8.60);
			beans[11].setParameters("ͨ�����2̨��", 11.60, 11.60);
			beans[12].setParameters("����ѹ����ȴ��Ԫ", 38.40, 38.40);
			double[] sum = loadSum(beans);
			beans[13].setParameters("�ܼ�", sum[0], sum[1]);
		}else if(carNo == 3){
			beans = new TrainPowerSupply[14];
			for(int i=0;i<beans.length;i++){
				beans[i] = new TrainPowerSupply(carNo, trainCategoryId);
			}
			beans[0].setParameters("�������Ƚ׶�", 35.00, 0);
			beans[1].setParameters("���/ͨ��̨���ȼ�����", 11.40, 0);
			beans[2].setParameters("�յ�ѹ����", 0, 32.00);
			beans[3].setParameters("����������", 0, 1.60);
			beans[4].setParameters("����������/������Ԫ", 6.30, 6.30);
			beans[5].setParameters("�������ȱ�ѹ��", 2.00, 0);
			beans[6].setParameters("ǣ����������ȴ��Ԫ", 26.00, 26.00);
			beans[7].setParameters("ǣ�������ȴ��Ԫ", 12.40, 12.40);
			beans[8].setParameters("UPS��Դ", 8.60, 8.60);
			beans[9].setParameters("ͨ�����2̨��", 11.60, 11.60);
			beans[10].setParameters("������ȴ", 1.00, 1.00);
			beans[11].setParameters("BLG", 25.00, 25.00);
			beans[12].setParameters("˫����ȴ��Ԫ", 8.00, 8.00);
			double[] sum = loadSum(beans);
			beans[13].setParameters("�ܼ�", sum[0], sum[1]);
		}else if(carNo == 4){
			beans = new TrainPowerSupply[14];
			for(int i=0;i<beans.length;i++){
				beans[i] = new TrainPowerSupply(carNo, trainCategoryId);
			}
			beans[0].setParameters("�������Ƚ׶�", 35.00, 0);
			beans[1].setParameters("���/ͨ��̨���ȼ�����", 11.40, 0);
			beans[2].setParameters("�յ�ѹ����", 0, 32.00);
			beans[3].setParameters("����������", 0, 1.60);
			beans[4].setParameters("����������/������Ԫ", 6.30, 6.30);
			beans[5].setParameters("�������ȱ�ѹ��", 2.00, 0);
			beans[6].setParameters("ǣ����������ȴ��Ԫ", 26.00, 26.00);
			beans[7].setParameters("ǣ�������ȴ��Ԫ", 12.40, 12.40);
			beans[8].setParameters("UPS��Դ", 8.60, 8.60);
			beans[9].setParameters("ͨ�����2̨��", 11.60, 11.60);
			beans[10].setParameters("����ѹ����ȴ��Ԫ", 38.40, 38.40);
			beans[11].setParameters("������ȴ", 1.00, 1.00);
			beans[12].setParameters("BLG", 25.00, 25.00);
			double[] sum = loadSum(beans);
			beans[13].setParameters("�ܼ�", sum[0], sum[1]);
		}else if(carNo == 5){
			beans = new TrainPowerSupply[12];
			for(int i=0;i<beans.length;i++){
				beans[i] = new TrainPowerSupply(carNo, trainCategoryId);
			}
			beans[0].setParameters("�������Ƚ׶�", 35.00, 0);
			beans[1].setParameters("���/ͨ��̨���ȼ�����", 11.40, 0);
			beans[2].setParameters("�յ�ѹ����", 0, 32.00);
			beans[3].setParameters("����������", 0, 1.60);
			beans[4].setParameters("����������/������Ԫ", 6.30, 6.30);
			beans[5].setParameters("�������ȱ�ѹ��", 2.00, 0);
			beans[6].setParameters("ǣ����������ȴ��Ԫ", 26.00, 26.00);
			beans[7].setParameters("ǣ�������ȴ��Ԫ", 12.40, 12.40);
			beans[8].setParameters("ͨ�����2̨��", 11.60, 11.60);
			beans[9].setParameters("����ѹ����ȴ��Ԫ", 38.40, 38.40);
			beans[10].setParameters("3��������ȴ��Ԫ", 12.00, 12.00);
			double[] sum = loadSum(beans);
			beans[11].setParameters("�ܼ�", sum[0], sum[1]);
		}else if(carNo == 6){
			beans = new TrainPowerSupply[13];
			for(int i=0;i<beans.length;i++){
				beans[i] = new TrainPowerSupply(carNo, trainCategoryId);
			}
			beans[0].setParameters("�������Ƚ׶�", 35.00, 0);
			beans[1].setParameters("���/ͨ��̨���ȼ�����", 11.40, 0);
			beans[2].setParameters("�յ�ѹ����", 0, 32.00);
			beans[3].setParameters("����������", 0, 1.60);
			beans[4].setParameters("����������/������Ԫ", 6.30, 6.30);
			beans[5].setParameters("�������ȱ�ѹ��", 2.00, 0);
			beans[6].setParameters("ǣ����������ȴ��Ԫ", 26.00, 26.00);
			beans[7].setParameters("ǣ�������ȴ��Ԫ", 12.40, 12.40);
			beans[8].setParameters("ͨ�����2̨��", 11.60, 11.60);
			beans[9].setParameters("������ȴ", 1.00, 1.00);
			beans[10].setParameters("BLG", 25.00, 25.00);
			beans[11].setParameters("˫����ȴ��Ԫ", 8.00, 8.00);
			double[] sum = loadSum(beans);
			beans[12].setParameters("�ܼ�", sum[0], sum[1]);
		}else if(carNo == 7){
			beans = new TrainPowerSupply[12];
			for(int i=0;i<beans.length;i++){
				beans[i] = new TrainPowerSupply(carNo, trainCategoryId);
			}
			beans[0].setParameters("�������Ƚ׶�", 35.00, 0);
			beans[1].setParameters("���/ͨ��̨���ȼ�����", 11.40, 0);
			beans[2].setParameters("�յ�ѹ����", 0, 32.00);
			beans[3].setParameters("����������", 0, 1.60);
			beans[4].setParameters("����������/������Ԫ", 6.30, 6.30);
			beans[5].setParameters("�������ȱ�ѹ��", 2.00, 0);
			beans[6].setParameters("ǣ����������ȴ��Ԫ", 26.00, 26.00);
			beans[7].setParameters("ǣ�������ȴ��Ԫ", 12.40, 12.40);
			beans[8].setParameters("UPS��Դ", 8.60, 8.60);
			beans[9].setParameters("ͨ�����2̨��", 11.60, 11.60);
			beans[10].setParameters("����ѹ����ȴ��Ԫ", 38.40, 38.40);
			double[] sum = loadSum(beans);
			beans[11].setParameters("�ܼ�", sum[0], sum[1]);
		}else if(carNo == 8){
			beans = new TrainPowerSupply[20];
			for(int i=0;i<beans.length;i++){
				beans[i] = new TrainPowerSupply(carNo, trainCategoryId);
			}
			beans[0].setParameters("�������Ƚ׶�", 35.00, 0);
			beans[1].setParameters("���/ͨ��̨���ȼ�����", 11.40, 0);
			beans[2].setParameters("��Ϣ���ټ���", 0.75, 0);
			beans[3].setParameters("�յ�ѹ����", 0, 32.00);
			beans[4].setParameters("����������", 0, 1.60);
			beans[5].setParameters("����������/������Ԫ", 6.30, 6.30);
			beans[6].setParameters("�������ȱ�ѹ��", 2.00, 0);
			beans[7].setParameters("˾���ҿ���������", 5.20, 0);
			beans[8].setParameters("˾���ҿ�ѹ��", 0, 5.00);
			beans[9].setParameters("˾������������", 0, 0.90);
			beans[10].setParameters("˾������������", 2.00, 2.00);
			beans[11].setParameters("˾�����·����", 0.08, 0.08);
			beans[12].setParameters("�絲��������", 1.10, 0);
			beans[13].setParameters("ǣ����������ȴ��Ԫ", 26.00, 26.00);
			beans[14].setParameters("ǣ�������ȴ��Ԫ", 12.40, 12.40);
			beans[15].setParameters("��ˮ��", 1.00, 1.00);
			beans[16].setParameters("UPS��Դ", 8.60, 8.60);
			beans[17].setParameters("����ѹ��", 18.10, 18.10);
			beans[18].setParameters("ͨ�����2̨��", 11.60, 11.60);
			double[] sum = loadSum(beans);
			beans[19].setParameters("�ܼ�", sum[0], sum[1]);
		}else{
			beans = new TrainPowerSupply[25];
			for(int i=0;i<beans.length;i++){
				beans[i] = new TrainPowerSupply(carNo, trainCategoryId);
			}
			beans[0].setParameters("�������Ƚ׶�", 35.00, 0);
			beans[1].setParameters("���/ͨ��̨���ȼ�����", 11.40, 0);
			beans[2].setParameters("��Ϣ���ټ���", 0.75, 0);
			beans[3].setParameters("�յ�ѹ����", 0, 32.00);
			beans[4].setParameters("����������", 0, 1.60);
			beans[5].setParameters("����������/������Ԫ", 6.30, 6.30);
			beans[6].setParameters("�������ȱ�ѹ��", 2.00, 0);
			beans[7].setParameters("˾���ҿ���������", 5.20, 0);
			beans[8].setParameters("˾���ҿ�ѹ��", 0, 5.00);
			beans[9].setParameters("˾������������", 0, 0.90);
			beans[10].setParameters("˾������������", 2.00, 2.00);
			beans[11].setParameters("˾�����·����", 0.08, 0.08);
			beans[12].setParameters("�絲��������", 1.10, 0);
			beans[13].setParameters("ǣ����������ȴ��Ԫ", 26.00, 26.00);
			beans[14].setParameters("ǣ�������ȴ��Ԫ", 12.40, 12.40);
			beans[15].setParameters("��ˮ��", 1.00, 1.00);
			beans[16].setParameters("UPS��Դ", 8.60, 8.60);
			beans[17].setParameters("����ѹ��", 18.10, 18.10);
			beans[18].setParameters("ͨ�����2̨��", 11.60, 11.60);
			beans[19].setParameters("����ѹ����ȴ��Ԫ", 38.40, 38.40);
			beans[20].setParameters("������ȴ", 1.00, 1.00);
			beans[21].setParameters("BLG", 25.00, 25.00);
			beans[22].setParameters("˫����ȴ��Ԫ", 8.00, 8.00);
			beans[23].setParameters("3��������ȴ��Ԫ", 12.00, 12.00);
			double[] sum = loadSum(beans);
			beans[24].setParameters("�ܼ�", sum[0], sum[1]);
		}
		
		return beans;
	}
	
	/**
	 * ���㸺������
	 * @param beans
	 * @return ��������
	 */
	public static double [] loadSum(TrainPowerSupply [] beans){
		double [] sum = new double[2];
		for(TrainPowerSupply bean : beans){
			sum[0] += bean.getWinter();
			sum[1] += bean.getSummer();
		}
		sum[0] = MyTools.numFormat2(sum[0]);
		sum[1] = MyTools.numFormat2(sum[1]);
		return sum;
	}
	
}
