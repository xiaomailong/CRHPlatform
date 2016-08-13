package com.crh.view.panel;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
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

/**
 * ��������Panel��ֱ����
 * @author huhui
 *
 */
public class AuxiliaryPowerSupplyDCPanel extends JPanel {
	
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
	 * ֱ�������������
	 */
	private final String TABLE = "train_power_supply_dc";
	
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
	private JLabel label1,label2,label3,label4,label5,label6,label7,label8,label9;

	public AuxiliaryPowerSupplyDCPanel() {
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
					JOptionPane.showMessageDialog(AuxiliaryPowerSupplyDCPanel.this,"��ѡ��������ƣ�");
					return;
				}
				int trainInfoCount = TrainEditPanelService.getTrainInfoCount(categoryId);
				if(trainInfoCount == 0){
					JOptionPane.showMessageDialog(AuxiliaryPowerSupplyDCPanel.this, "�ó����г�������Ϣ������δ���棬�뷵�ء��г����ݡ����棡");
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
					JOptionPane.showMessageDialog(AuxiliaryPowerSupplyDCPanel.this,"��ѡ��������ƣ�");
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
		panel_4.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "��Ϣ����", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_4.setBounds(1113, 56, 225, 464);
		add(panel_4);
		panel_4.setLayout(null);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "\u51AC\u5B63", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_3.setBounds(10, 21, 205, 110);
		panel_4.add(panel_3);
		panel_3.setLayout(null);
		
		JLabel label_3 = new JLabel("ȫ��ֱ���������ع��ʣ�");
		label_3.setBounds(10, 20, 140, 15);
		panel_3.add(label_3);
		
		label1 = new JLabel("");
		label1.setBounds(141, 20, 64, 15);
		panel_3.add(label1);
		
		JLabel label_4 = new JLabel("ȫ�����г��");
		label_4.setBounds(10, 45, 92, 15);
		panel_3.add(label_4);
		
		JLabel label_5 = new JLabel("������֮�ͣ�");
		label_5.setBounds(10, 58, 84, 15);
		panel_3.add(label_5);
		
		label2 = new JLabel("");
		label2.setBounds(104, 52, 74, 15);
		panel_3.add(label2);
		
		JLabel label_7 = new JLabel("ȫ������������");
		label_7.setBounds(10, 83, 120, 15);
		panel_3.add(label_7);
		
		label3 = new JLabel("");
		label3.setBounds(115, 83, 74, 15);
		panel_3.add(label3);
		
		JPanel panel_5 = new JPanel();
		panel_5.setLayout(null);
		panel_5.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "\u590F\u5B63", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_5.setBounds(10, 150, 205, 110);
		panel_4.add(panel_5);
		
		JLabel label_6 = new JLabel("ȫ��ֱ���������ع��ʣ�");
		label_6.setBounds(10, 20, 140, 15);
		panel_5.add(label_6);
		
		label4 = new JLabel("");
		label4.setBounds(141, 20, 64, 15);
		panel_5.add(label4);
		
		JLabel label_9 = new JLabel("ȫ�����г��");
		label_9.setBounds(10, 45, 92, 15);
		panel_5.add(label_9);
		
		JLabel label_10 = new JLabel("������֮�ͣ�");
		label_10.setBounds(10, 58, 84, 15);
		panel_5.add(label_10);
		
		label5 = new JLabel("");
		label5.setBounds(104, 52, 74, 15);
		panel_5.add(label5);
		
		JLabel label_12 = new JLabel("ȫ������������");
		label_12.setBounds(10, 83, 120, 15);
		panel_5.add(label_12);
		
		label6 = new JLabel("");
		label6.setBounds(115, 83, 74, 15);
		panel_5.add(label6);
		
		JPanel panel_6 = new JPanel();
		panel_6.setBorder(new TitledBorder(null, "\u5176\u5B83", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_6.setBounds(10, 270, 205, 184);
		panel_4.add(panel_6);
		panel_6.setLayout(null);
		
		JLabel label_8 = new JLabel("\u5145\u7535\u673A\u4F4D\u7F6E\uFF1A");
		label_8.setBounds(10, 20, 96, 15);
		panel_6.add(label_8);
		
		JLabel label_11 = new JLabel("\u4F18\u5148\u63A8\u8350\u5B89\u88C5\u5728\u6CA1\u6709\u53D8\u538B\u5668\u7684\u62D6\u8F66\u4E0A");
		label_11.setBounds(9, 42, 205, 15);
		panel_6.add(label_11);
		
		JLabel label_13 = new JLabel("\u5168\u8F66\u84C4\u7535\u6C60\u603B\u5BB9\u91CF\uFF1A");
		label_13.setBounds(10, 68, 114, 15);
		panel_6.add(label_13);
		
		JLabel label_14 = new JLabel("\u5168\u8F66\u84C4\u7535\u6C60\u4E2A\u6570\uFF1A");
		label_14.setBounds(10, 91, 114, 15);
		panel_6.add(label_14);
		
		JLabel label_15 = new JLabel("\u6BCF\u4E2A\u84C4\u7535\u6C60\u7EC4\u7684\u5BB9\u91CF\uFF1A");
		label_15.setBounds(10, 117, 128, 15);
		panel_6.add(label_15);
		
		JLabel label_16 = new JLabel("\u84C4\u7535\u6C60\u7684\u4F4D\u7F6E\uFF1A");
		label_16.setBounds(10, 142, 114, 15);
		panel_6.add(label_16);
		
		label7 = new JLabel("");
		label7.setBounds(121, 68, 74, 15);
		panel_6.add(label7);
		
		label8 = new JLabel("");
		label8.setBounds(108, 91, 74, 15);
		panel_6.add(label8);
		
		label9 = new JLabel("");
		label9.setBounds(129, 117, 74, 15);
		panel_6.add(label9);
		
		JLabel label10 = new JLabel("");
		label10.setBounds(96, 142, 74, 15);
		panel_6.add(label10);
		
		JLabel label_21 = new JLabel("\u4F18\u5148\u63A8\u8350\u5B89\u88C5\u5728\u6CA1\u6709\u53D8\u538B\u5668\u7684\u62D6\u8F66\u4E0A");
		label_21.setBounds(10, 159, 205, 15);
		panel_6.add(label_21);
		
		JButton btnNewButton = new JButton("���");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				appendOneLine();
			}
		});
		btnNewButton.setBounds(1120, 620, 93, 23);
		add(btnNewButton);
		
		JButton button = new JButton("ɾ��");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeOneLine();
			}
		});
		button.setBounds(1223, 620, 93, 23);
		add(button);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "\u76F4\u6D41\u8F85\u52A9\u8D1F\u8F7D\u53C2\u6570\u8BBE\u7F6E", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_2.setBounds(1113, 530, 225, 76);
		add(panel_2);
		panel_2.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("����������Ч�ʣ�");
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
		
		JLabel label_2 = new JLabel("��������������ȣ�");
		label_2.setBounds(10, 46, 108, 15);
		panel_2.add(label_2);
		
		para2 = new JTextField();
		para2.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				showInfo();
			}
		});
		para2.setText("1.5");
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
			JOptionPane.showMessageDialog(AuxiliaryPowerSupplyDCPanel.this, "��ѡ��һ����ɾ����");
			return;
		}
		int option = JOptionPane.showConfirmDialog(AuxiliaryPowerSupplyDCPanel.this, "ȷ��ɾ����");
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
			JOptionPane.showMessageDialog(AuxiliaryPowerSupplyDCPanel.this, "��������ȷ�ĸ��ز�����");
			return;
		}
		double val1=0,val2=0,val3=0,val4=0,val5=0,val6=0,val7=0,val8=0,val9=0;
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
		val3 = Math.floor(carCount/2) + 1;
		val6 = Math.floor(carCount/2) + 1;
		
		val7 = carCount * 40;
		val8 = val3;
		val9 = MyTools.numFormat2(val7/val8);
		
		//��ʾͳ����Ϣ
		label1.setText(val1+"");
		label2.setText(val2+"");
		label3.setText(val3+"");
		label4.setText(val4+"");
		label5.setText(val5+"");
		label6.setText(val6+"");
		label7.setText(val7+" Ah");
		label8.setText(val8+"");
		label9.setText(val9+"");
		
	}
	
	/**
	 *  ��������Table��ֵ
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
					JOptionPane.showMessageDialog(AuxiliaryPowerSupplyDCPanel.this, "\"����\"���Ʋ���Ϊ�գ�");
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
		AuxiliaryPowerSupplyPanelService.saveTrainPowerSupply(trainCategoryId, dataList, TABLE);
		
		//���潻���������ز���
		String para0Str = para1.getText().trim();
		String para1Str = para2.getText().trim();
		if(para0Str == null || "".equals(para0Str) || para1Str == null || "".equals(para1Str)){
			JOptionPane.showMessageDialog(AuxiliaryPowerSupplyDCPanel.this,"�������ز�������ʧ�ܣ�");
			return;
		}
		double para0Val = Double.parseDouble(para0Str);
		double para1Val = Double.parseDouble(para1Str);
		AuxiliaryPowerSupplyPanelService.saveTrainPowerPara(trainCategoryId, para0Val, para1Val, 1);
		
		JOptionPane.showMessageDialog(AuxiliaryPowerSupplyDCPanel.this,"����ɹ���");
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
			beans = TrainPowerSupplyDCArrayFactory.getTrainPowerSupplyArray(carNo, trainCategoryId);
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
							JOptionPane.showMessageDialog(AuxiliaryPowerSupplyDCPanel.this, "��������ȷ�����֣�");
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
class TrainPowerSupplyDCArrayFactory{
	
	/**
	 * ����TrainPowerSupply����
	 * @param carNo
	 * @param trainCategoryId
	 * @return
	 */
	public static TrainPowerSupply [] getTrainPowerSupplyArray(int carNo, int trainCategoryId){
		TrainPowerSupply [] beans;
		if(carNo == 1){//01��
			beans = new TrainPowerSupply[27];
			for(int i=0;i<beans.length;i++){
				beans[i] = new TrainPowerSupply(carNo, trainCategoryId);
			}
			beans[0].setParameters("������Ƶ�ԪCCU 1", 0, 0);
			beans[1].setParameters("�ܵ繭����", 0, 0);
			beans[2].setParameters("���Ͽ���", 0, 0);
			beans[3].setParameters("���򿪹ؿ���", 0, 0);
			beans[4].setParameters("�����ضϻ�·", 0, 0);
			beans[5].setParameters("��ȫ��·", 0, 0);
			beans[6].setParameters("��𱨾�	", 0, 0);
			beans[7].setParameters("ǣ������������TCU", 0, 0);
			beans[8].setParameters("�ƶ�����ϵͳ2BCU2", 0, 0);
			beans[9].setParameters("ATP", 0, 0);
			beans[10].setParameters("Compact I/O", 0, 0);
			beans[11].setParameters("���ſ���ϵͳ", 0, 0);
			beans[12].setParameters("�յ�����ϵͳ", 0, 0);
			beans[13].setParameters("�����豸��ˮϵͳ����", 0, 0);
			beans[14].setParameters("�ÿ���ϢϵͳPIS", 0, 0);
			beans[15].setParameters("MVB�м���A·", 0, 0);
			beans[16].setParameters("������", 0, 0);
			beans[17].setParameters("���ݼ�¼��", 0, 0);
			beans[18].setParameters("�г�����ϵͳETCS", 0, 0);
			beans[19].setParameters("������Ƶ�ԪCCU2", 0, 0);
			beans[20].setParameters("MVB�м���B·", 0, 0);
			beans[21].setParameters("DC110/AC230V�����", 0, 0);
			beans[22].setParameters("�ƶ����Ƶ�Ԫ1BCU1", 0, 0);
			beans[23].setParameters("�ƶ�������", 0, 0);
			beans[24].setParameters("����β���ź�", 0, 0);
			beans[25].setParameters("��ؿ���", 0, 0);
			double[] sum = loadSum(beans);
			beans[26].setParameters("�ܼ�", sum[0], sum[1]);
		}else if(carNo == 2){
			beans = new TrainPowerSupply[17];
			for(int i=0;i<beans.length;i++){
				beans[i] = new TrainPowerSupply(carNo, trainCategoryId);
			}
			beans[0].setParameters("�ܵ繭����", 0, 0);
			beans[1].setParameters("���Ͽ���", 0, 0);
			beans[2].setParameters("���򿪹ؿ���", 0, 0);
			beans[3].setParameters("�����ضϻ�·", 0, 0);
			beans[4].setParameters("��ȫ��·", 0, 0);
			beans[5].setParameters("��𱨾�", 0, 0);
			beans[6].setParameters("������������ACU", 0, 0);
			beans[7].setParameters("BCU", 0, 0);
			beans[8].setParameters("����ϵͳ", 0, 0);
			beans[9].setParameters("�յ�����ϵͳ", 0, 0);
			beans[10].setParameters("Compact I/O", 0, 0);
			beans[11].setParameters("MVB�м���B·", 0, 0);
			beans[12].setParameters("DC110/AC230V�����", 0, 0);
			beans[13].setParameters("DNRA", 0, 0);
			beans[14].setParameters("BEB", 0, 0);
			beans[15].setParameters("�ƶ����Ƶ�ԪBCU", 0, 0);
			double[] sum = loadSum(beans);
			beans[16].setParameters("�ܼ�", sum[0], sum[1]);
		}else if(carNo == 3){
			beans = new TrainPowerSupply[11];
			for(int i=0;i<beans.length;i++){
				beans[i] = new TrainPowerSupply(carNo, trainCategoryId);
			}
			beans[0].setParameters("�����ضϻ�·", 0, 0);
			beans[1].setParameters("��ȫ��·", 0, 0);
			beans[2].setParameters("ǣ������������TCU", 0, 0);
			beans[3].setParameters("����ϵͳ", 0, 0);
			beans[4].setParameters("�յ�����ϵͳ", 0, 0);
			beans[5].setParameters("Compact I/O", 0, 0);
			beans[6].setParameters("BEB", 0, 0);
			beans[7].setParameters("MVB�м���B·", 0, 0);
			beans[8].setParameters("DC110/AC230V�����", 0, 0);
			beans[9].setParameters("�ƶ����Ƶ�ԪBCU", 0, 0);
			double[] sum = loadSum(beans);
			beans[10].setParameters("�ܼ�", sum[0], sum[1]);
		}else if(carNo == 4){
			beans = new TrainPowerSupply[12];
			for(int i=0;i<beans.length;i++){
				beans[i] = new TrainPowerSupply(carNo, trainCategoryId);
			}
			beans[0].setParameters("�����ضϻ�·", 0, 0);
			beans[1].setParameters("��ȫ��·", 0, 0);
			beans[2].setParameters("������������ACU", 0, 0);
			beans[3].setParameters("����ϵͳ", 0, 0);
			beans[4].setParameters("�յ�����ϵͳ", 0, 0);
			beans[5].setParameters("Compact I/O", 0, 0);
			beans[6].setParameters("BC", 0, 0);
			beans[7].setParameters("MVB�м���B·", 0, 0);
			beans[8].setParameters("DC110/AC230V�����", 0, 0);
			beans[9].setParameters("�ƶ����Ƶ�ԪBCU", 0, 0);
			beans[10].setParameters("BEB", 0, 0);
			double[] sum = loadSum(beans);
			beans[11].setParameters("�ܼ�", sum[0], sum[1]);
		}else if(carNo == 5){
			beans = new TrainPowerSupply[12];
			for(int i=0;i<beans.length;i++){
				beans[i] = new TrainPowerSupply(carNo, trainCategoryId);
			}
			beans[0].setParameters("�����ضϻ�·", 0, 0);
			beans[1].setParameters("��ȫ��·", 0, 0);
			beans[2].setParameters("������������ACU", 0, 0);
			beans[3].setParameters("����ϵͳ", 0, 0);
			beans[4].setParameters("�յ�����ϵͳ", 0, 0);
			beans[5].setParameters("Compact I/O", 0, 0);
			beans[6].setParameters("BC", 0, 0);
			beans[7].setParameters("MVB�м���B·", 0, 0);
			beans[8].setParameters("DC110/AC230V�����", 0, 0);
			beans[9].setParameters("�ƶ����Ƶ�ԪBCU", 0, 0);
			beans[10].setParameters("BEB", 0, 0);
			double[] sum = loadSum(beans);
			beans[11].setParameters("�ܼ�", sum[0], sum[1]);
		}else if(carNo == 6){
			beans = new TrainPowerSupply[11];
			for(int i=0;i<beans.length;i++){
				beans[i] = new TrainPowerSupply(carNo, trainCategoryId);
			}
			beans[0].setParameters("�����ضϻ�·", 0, 0);
			beans[1].setParameters("��ȫ��·", 0, 0);
			beans[2].setParameters("ǣ������������TCU", 0, 0);
			beans[3].setParameters("����ϵͳ", 0, 0);
			beans[4].setParameters("�յ�����ϵͳ", 0, 0);
			beans[5].setParameters("Compact I/O", 0, 0);
			beans[6].setParameters("BEB", 0, 0);
			beans[7].setParameters("MVB�м���B·", 0, 0);
			beans[8].setParameters("DC110/AC230V�����", 0, 0);
			beans[9].setParameters("�ƶ����Ƶ�ԪBCU", 0, 0);
			double[] sum = loadSum(beans);
			beans[10].setParameters("�ܼ�", sum[0], sum[1]);
		}else if(carNo == 7){
			beans = new TrainPowerSupply[17];
			for(int i=0;i<beans.length;i++){
				beans[i] = new TrainPowerSupply(carNo, trainCategoryId);
			}
			beans[0].setParameters("�ܵ繭����", 0, 0);
			beans[1].setParameters("���Ͽ���", 0, 0);
			beans[2].setParameters("���򿪹ؿ���", 0, 0);
			beans[3].setParameters("�����ضϻ�·", 0, 0);
			beans[4].setParameters("��ȫ��·", 0, 0);
			beans[5].setParameters("��𱨾�", 0, 0);
			beans[6].setParameters("������������ACU", 0, 0);
			beans[7].setParameters("BCU", 0, 0);
			beans[8].setParameters("����ϵͳ", 0, 0);
			beans[9].setParameters("�յ�����ϵͳ", 0, 0);
			beans[10].setParameters("Compact I/O", 0, 0);
			beans[11].setParameters("MVB�м���B·", 0, 0);
			beans[12].setParameters("DC110/AC230V�����", 0, 0);
			beans[13].setParameters("DNRA", 0, 0);
			beans[14].setParameters("BEB", 0, 0);
			beans[15].setParameters("�ƶ����Ƶ�ԪBCU", 0, 0);
			double[] sum = loadSum(beans);
			beans[16].setParameters("�ܼ�", sum[0], sum[1]);
		}else if(carNo == 8){
			beans = new TrainPowerSupply[27];
			for(int i=0;i<beans.length;i++){
				beans[i] = new TrainPowerSupply(carNo, trainCategoryId);
			}
			beans[0].setParameters("������Ƶ�ԪCCU 1", 0, 0);
			beans[1].setParameters("�ܵ繭����", 0, 0);
			beans[2].setParameters("���Ͽ���", 0, 0);
			beans[3].setParameters("���򿪹ؿ���", 0, 0);
			beans[4].setParameters("�����ضϻ�·", 0, 0);
			beans[5].setParameters("��ȫ��·", 0, 0);
			beans[6].setParameters("��𱨾�	", 0, 0);
			beans[7].setParameters("ǣ������������TCU", 0, 0);
			beans[8].setParameters("�ƶ�����ϵͳ2BCU2", 0, 0);
			beans[9].setParameters("ATP", 0, 0);
			beans[10].setParameters("Compact I/O", 0, 0);
			beans[11].setParameters("���ſ���ϵͳ", 0, 0);
			beans[12].setParameters("�յ�����ϵͳ", 0, 0);
			beans[13].setParameters("�����豸��ˮϵͳ����", 0, 0);
			beans[14].setParameters("�ÿ���ϢϵͳPIS", 0, 0);
			beans[15].setParameters("MVB�м���A·", 0, 0);
			beans[16].setParameters("������", 0, 0);
			beans[17].setParameters("���ݼ�¼��", 0, 0);
			beans[18].setParameters("�г�����ϵͳETCS", 0, 0);
			beans[19].setParameters("������Ƶ�ԪCCU2", 0, 0);
			beans[20].setParameters("MVB�м���B·", 0, 0);
			beans[21].setParameters("DC110/AC230V�����", 0, 0);
			beans[22].setParameters("�ƶ����Ƶ�Ԫ1BCU1", 0, 0);
			beans[23].setParameters("�ƶ�������", 0, 0);
			beans[24].setParameters("����β���ź�", 0, 0);
			beans[25].setParameters("��ؿ���", 0, 0);
			double[] sum = loadSum(beans);
			beans[26].setParameters("�ܼ�", sum[0], sum[1]);
		}else{
			beans = new TrainPowerSupply[47];
			for(int i=0;i<beans.length;i++){
				beans[i] = new TrainPowerSupply(carNo, trainCategoryId);
			}
			beans[0].setParameters("������Ƶ�ԪCCU 1", 0, 0);
			beans[1].setParameters("KLIPվ (����1)", 0, 0);
			beans[2].setParameters("MVBת�����C��Դ�� A", 0, 0);
			beans[3].setParameters("ǣ������������1", 0, 0);
			beans[4].setParameters("�յ�����ϵͳ", 0, 0);
			beans[5].setParameters("���ƹ�ͨ�������", 0, 0);
			beans[6].setParameters("ǰ�絲���ȿ���", 0, 0);
			beans[7].setParameters("������", 0, 0);
			beans[8].setParameters("�Ķ���", 0, 0);
			beans[9].setParameters("Ӧ����������1��", 0, 0);
			beans[10].setParameters("˾���Ҷ���", 0, 0);
			beans[11].setParameters("������������ACU", 0, 0);
			beans[12].setParameters("��������", 0, 0);
			beans[13].setParameters("���ſ���ϵͳ", 0, 0);
			beans[14].setParameters("�ÿ���Ϣϵͳ��ʾ��", 0, 0);
			beans[15].setParameters("�����豸��ˮϵͳ����", 0, 0);
			beans[16].setParameters("��ɳ�ܼ���������", 0, 0);
			beans[17].setParameters("��������ѹ��������", 0, 0);
			beans[18].setParameters("��Ե��", 0, 0);
			beans[19].setParameters("�ƶ���", 0, 0);
			beans[20].setParameters("�ƶ����Ƶ�ԪBCU", 0, 0);
			beans[21].setParameters("�г�����ϵͳ", 0, 0);
			beans[22].setParameters("��𱨾�", 0, 0);
			beans[23].setParameters("��ʻ�ҵ���ʾ��", 0, 0);
			beans[24].setParameters("�г��㲥", 0, 0);
			beans[25].setParameters("�ⲿ����", 0, 0);
			beans[26].setParameters("��ȫ��·", 0, 0);
			beans[27].setParameters("��������", 0, 0);
			beans[28].setParameters("�ܵ繭����", 0, 0);
			beans[29].setParameters("���Ͽ���", 0, 0);
			beans[30].setParameters("���򿪹ؿ���", 0, 0);
			beans[31].setParameters("���ƹ�ͨ������ң�", 0, 0);
			beans[32].setParameters("Ӧ������", 0, 0);
			beans[33].setParameters("�ÿ���ϢϵͳPIS", 0, 0);
			beans[34].setParameters("��ʻԱ/����ԱMMI", 0, 0);
			beans[35].setParameters("����β���ź�", 0, 0);
			beans[36].setParameters("�ƶ���������", 0, 0);
			beans[37].setParameters("�г�����ͨѶ", 0, 0);
			beans[38].setParameters("�Զ�����ϵͳATP", 0, 0);
			beans[39].setParameters("���ݼ�¼��", 0, 0);
			beans[40].setParameters("DC110/AC230�����", 0, 0);
			beans[41].setParameters("��ؿ���", 0, 0);
			beans[42].setParameters("BEB", 0, 0);
			beans[43].setParameters("DNRA", 0, 0);
			beans[44].setParameters("�����ضϻ�·", 0, 0);
			beans[45].setParameters("BC", 0, 0);
			
			double[] sum = loadSum(beans);
			beans[46].setParameters("�ܼ�", sum[0], sum[1]);
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
