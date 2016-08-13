package com.crh.view.dialog;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import com.crh.service.RouteDataManagementDialogService;
import com.crh2.javabean.TrainRouteName;
import com.crh2.javabean.TrainRouteTrainnum;
import com.crh2.util.MyUtillity;

/**
 * ��������
 * @author huhui
 *
 */
public class RouteDataTrainNumDialog extends JDialog {
	
	private JPanel tablePanel;
	private JComboBox<String> routeNameComboBox;
	private String[] routeNameArray, trainNumComBoxArray;
	private JComboBox<String> trainNumComboBox;
	private String DEFAULTROUTENAME = "��ѡ��", DEFAULTTRAINNUM = "���г���";
	/**
	 * ��ѡ�����г��Ρ�ʱ��Ӧ�ĳ��κ�
	 */
	public static final int DEFAULTNUM = -1;
	
	public static JTable trainNumTable;
	private DefaultTableModel tableModel;
	private JScrollPane trainNumTableScrollPane;
	/**
	 * �������
	 */
	private Vector<String> columnNamesVector;
	private String trainNumIndex = DEFAULTTRAINNUM;
	
	public RouteDataTrainNumDialog(Dialog owner, String routeName) {
		super(owner);
		setTitle("��������");
		setBounds(100, 100, 916, 527);
		MyUtillity.setFrameOnCenter(this);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		panel.setBounds(10, 10, 880, 37);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel label = new JLabel("·�����ƣ�");
		label.setBounds(10, 10, 60, 15);
		panel.add(label);
		
		initCombBoxArray();//��ʼ��
		
		routeNameComboBox = new JComboBox<String>(new DefaultComboBoxModel<String>(routeNameArray));
		routeNameComboBox.setBounds(80, 7, 103, 21);
		panel.add(routeNameComboBox);
		routeNameComboBox.setSelectedItem(routeName);
		
		JLabel label_1 = new JLabel("���Σ�");
		label_1.setBounds(226, 10, 54, 15);
		panel.add(label_1);
		
		trainNumComboBox = new JComboBox<String>(new DefaultComboBoxModel<String>(trainNumComBoxArray));
		trainNumComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				trainNumIndex = (String) trainNumComboBox.getSelectedItem();
			}
		});
		trainNumComboBox.setBounds(275, 7, 103, 21);
		panel.add(trainNumComboBox);
		
		JButton button = new JButton("�鿴");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(isSelectRoute()){
					int index = trainNumComboBox.getSelectedIndex();
					String routeName = (String) routeNameComboBox.getSelectedItem();
					String trainNum = 0 + "";
					if(index == 0){
						trainNum = DEFAULTNUM + "";
					}else{
						trainNum = (String) trainNumComboBox.getSelectedItem();
					}
					displayTrainNum(routeName, trainNum);
				}else{
					JOptionPane.showMessageDialog(RouteDataTrainNumDialog.this, "��ѡ����·���ƣ�");
				}
			}
		});
		button.setBounds(426, 6, 93, 23);
		panel.add(button);
		
		JButton button_1 = new JButton("���");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(isSelectRoute()){
					appendOneLine();
				}else{
					JOptionPane.showMessageDialog(RouteDataTrainNumDialog.this, "��ѡ����·���ƣ�");
				}
				
			}
		});
		button_1.setBounds(540, 6, 93, 23);
		panel.add(button_1);
		
		JButton button_2 = new JButton("ɾ��");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(isSelectRoute()){
					removeOneLine();
				}else{
					JOptionPane.showMessageDialog(RouteDataTrainNumDialog.this, "��ѡ����·���ƣ�");
				}
			}
		});
		button_2.setBounds(656, 6, 93, 23);
		panel.add(button_2);
		
		JButton button_3 = new JButton("����");
		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(isSelectRoute()){
					int index = trainNumComboBox.getSelectedIndex();
					String routeName = (String) routeNameComboBox.getSelectedItem();
					String trainNum = 0 + "";
					if(index == 0){
						trainNum = DEFAULTNUM + "";
					}else{
						trainNum = (String) trainNumComboBox.getSelectedItem();
					}
					saveTrainNum(routeName, trainNum);
					updateTrainNum();
				}else{
					JOptionPane.showMessageDialog(RouteDataTrainNumDialog.this, "��ѡ����·���ƣ�");
				}
			}
		});
		button_3.setBounds(770, 6, 93, 23);
		panel.add(button_3);
		
		tablePanel = new JPanel();
		tablePanel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		tablePanel.setBounds(10, 57, 880, 422);
		getContentPane().add(tablePanel);
		tablePanel.setLayout(new GridLayout(1, 1, 0, 0));
		
		//��ʾ���
		createTrainNumTable();
		tablePanel.add(trainNumTableScrollPane);
		displayTrainNum(routeName, DEFAULTNUM+"");
		setModal(true);
		setVisible(true);
	}
	
	/**
	 * ��ʾ������Ϣ
	 * @param routeName
	 * @param trainNum
	 */
	public void displayTrainNum(String routeName, String trainNum){
		tableModel.setRowCount(0);
		ArrayList<TrainRouteTrainnum> beanList = RouteDataManagementDialogService.getTrainNum(routeName, trainNum);
		for(TrainRouteTrainnum bean : beanList){
			Vector<String> line = new Vector<String>();
			line.add(bean.getRouteName());
			line.add(bean.getSerialNum()+"");
			line.add(bean.getRouteNum()+"");
			line.add(bean.getDirection()+"");
			line.add(bean.getStationName());
			line.add(bean.getRunTime()+"");
			line.add("����");
			line.add(bean.getTrainNum());
			tableModel.addRow(line);
		}
	}
	
	/**
	 * ������е�����
	 */
	public void saveTrainNum(String routeName, String trainNum){
		ArrayList<TrainRouteTrainnum> beanList = new ArrayList<TrainRouteTrainnum>();
		for(int i=0; i < tableModel.getRowCount(); i++){
			TrainRouteTrainnum bean = new TrainRouteTrainnum();
			Vector line = (Vector) tableModel.getDataVector().elementAt(i);
			for(int j=0;j<line.size();j++){
				if(line.get(j) == null){
					JOptionPane.showMessageDialog(RouteDataTrainNumDialog.this, "ֵ����Ϊ�գ�");
					trainNumTable.setRowSelectionInterval(i, i);
					return;
				}
			}
			bean.setRouteName((String) line.get(0));
			bean.setSerialNum(Integer.parseInt(line.get(1).toString()));
			bean.setRouteNum(Integer.parseInt(line.get(2).toString()));
			bean.setDirection(Integer.parseInt(line.get(3).toString()));
			bean.setStationName(line.get(4).toString());
			bean.setRunTime(Double.parseDouble(line.get(5).toString()));
//			bean.setStopTime(Double.parseDouble(line.get(6).toString()));
			bean.setStopTime(1);//2014.10.19 ͣվʱ��������ã��˴�Ĭ�ϱ���1
			bean.setTrainNum(line.get(7).toString());
			beanList.add(bean);
		}
		RouteDataManagementDialogService.saveTrainNum(routeName, trainNum, beanList);
		JOptionPane.showMessageDialog(RouteDataTrainNumDialog.this, "����ɹ���");
	}
	
	/**
	 * ����һ��
	 */
	public void appendOneLine(){
		Vector<String> line = new Vector<String>();
		int index = routeNameComboBox.getSelectedIndex();
		if(index != 0){
			String str = (String) routeNameComboBox.getSelectedItem();
			line.add(0, str);
			int rowCount = 0;
			if((rowCount = trainNumTable.getRowCount()) != 0){
				String cellValue = (String) tableModel.getValueAt(rowCount-1, 1);
				if(cellValue != null && !cellValue.trim().equals("")){
					line.add(1, (Integer.parseInt(cellValue)+1)+"");
				}
			}else{
				line.add(1, 1+"");
			}
			for(int i=2;i<=5;i++){
				line.add(i, "");
			}
			line.add(6, "����");
		}
		tableModel.addRow(line);
	}
	
	/**
	 * ɾ��һ��
	 */
	public void removeOneLine(){
		int selectedIndex = trainNumTable.getSelectedRow();
		if(selectedIndex == -1){
			JOptionPane.showMessageDialog(RouteDataTrainNumDialog.this, "��ѡ��һ����ɾ����");
			return;
		}
		int option = JOptionPane.showConfirmDialog(RouteDataTrainNumDialog.this, "ȷ��ɾ����");
		if(option == 0){
			tableModel.removeRow(selectedIndex);
		}
	}
	
	/**
	 * ���ɱ��
	 */
	public void createTrainNumTable(){
		initColumnName();
		tableModel = new DefaultTableModel(columnNamesVector, 0){
			public boolean isCellEditable(int row, int column){
				if(column == 0){
					return false;
				}
				return true;
			}
		};
		trainNumTable = new JTable(tableModel);
		trainNumTable.putClientProperty("terminateEditOnFocusLost", true);
		trainNumTableScrollPane = new JScrollPane(trainNumTable);
		
		//��trainNumTable����ӡ�ͣվʱ�䡱��ť
		trainNumTable.getColumnModel().getColumn(6).setCellEditor(new MyButtonEditor());
		trainNumTable.getColumnModel().getColumn(6).setCellRenderer(new MyButtonRender());
	}
	
	/**
	 * ��ʼ����ͷ
	 */
	public void initColumnName(){
		columnNamesVector = new Vector<String>();
		columnNamesVector.add("��·����");
		columnNamesVector.add("���");
		columnNamesVector.add("��·���");
		columnNamesVector.add("������");
		columnNamesVector.add("վ��");
		columnNamesVector.add("����ʱ��(min)");
		columnNamesVector.add("ͣվʱ��(s)");
		columnNamesVector.add("����");
	}
	
	/**
	 * ����trainNumComboBox��ֵ
	 */
	public void updateTrainNum(){
		ArrayList<String> trainNumList = RouteDataManagementDialogService.getTrainNum();
		trainNumComBoxArray = new String[trainNumList.size() + 1];
		trainNumComBoxArray[0] = DEFAULTTRAINNUM;
		for(int j=0;j<trainNumList.size();j++){
			trainNumComBoxArray[j+1] = trainNumList.get(j);
		}
		trainNumComboBox.setModel(new DefaultComboBoxModel<String>(trainNumComBoxArray));
		trainNumComboBox.setSelectedItem(trainNumIndex);
	}
	
	/**
	 * ��ʼ��combBox����Ӧ������
	 */
	public void initCombBoxArray(){
		ArrayList<TrainRouteName> routeList = RouteDataManagementDialogService.getTrainRouteName();
		ArrayList<String> trainNumList = RouteDataManagementDialogService.getTrainNum();
		routeNameArray = new String[routeList.size() + 1];
		trainNumComBoxArray = new String[trainNumList.size() + 1];
		routeNameArray[0] = DEFAULTROUTENAME;
		trainNumComBoxArray[0] = DEFAULTTRAINNUM;
		for(int i=0;i<routeList.size();i++){
			routeNameArray[i+1] = routeList.get(i).getRouteName();
		}
		for(int j=0;j<trainNumList.size();j++){
			trainNumComBoxArray[j+1] = trainNumList.get(j);
		}
	}
	
	/**
	 * �����ťǰ���ж��Ƿ�ѡ������·��
	 */
	public boolean isSelectRoute(){
		int index = -1;
		index = routeNameComboBox.getSelectedIndex();
		if(index == 0){
			return false;
		}
		return true;
	}
	
}

/**
 * ʵ�ֵ��Զ������Ⱦ��
 * @author huhui
 */
class MyButtonRender implements TableCellRenderer {
	private JPanel panel;

	private JButton button;

	public MyButtonRender() {
		this.initButton();
		this.initPanel();
		// ��Ӱ�ť��
		this.panel.add(this.button);
	}

	private void initButton() {
		this.button = new JButton();
		// ���ð�ť�Ĵ�С��λ�á�
		this.button.setBounds(15, 0, 80, 15);
	}

	private void initPanel() {
		this.panel = new JPanel();
		// panelʹ�þ��Զ�λ������button�Ͳ������������Ԫ��
		this.panel.setLayout(null);
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		// ֻΪ��ť��ֵ���ɡ�Ҳ������������������汳���ȡ�
		this.button.setText(value == null ? "" : String.valueOf(value));
		return this.panel;
	}

}

/**
 * ����¼�
 * @author huhui
 *
 */
class MyButtonEditor extends DefaultCellEditor {

	private JPanel panel;
	private JButton button;

	public MyButtonEditor() {
		// DefautlCellEditor�д˹���������Ҫ����һ�������������ʹ�õ���ֱ��newһ�����ɡ�
		super(new JTextField());
		// ���õ�����μ���༭��
		this.setClickCountToStart(1);
		this.initButton();
		this.initPanel();
		// ��Ӱ�ť��
		this.panel.add(this.button);
	}

	private void initButton() {
		this.button = new JButton();
		// ���ð�ť�Ĵ�С��λ�á�
		this.button.setBounds(15, 0, 80, 15);
		// Ϊ��ť����¼�������ֻ�����ActionListner�¼���Mouse�¼���Ч��
		this.button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int rowIndex = RouteDataTrainNumDialog.trainNumTable.getSelectedRow();
				String routeName = (String) RouteDataTrainNumDialog.trainNumTable.getValueAt(rowIndex, 0);
				String trainNum = (String) RouteDataTrainNumDialog.trainNumTable.getValueAt(rowIndex, 7);
				new RouteStationStopTimeDialog(routeName, trainNum);
			}
		});
	}

	private void initPanel() {
		this.panel = new JPanel();
		// panelʹ�þ��Զ�λ������button�Ͳ������������Ԫ��
		this.panel.setLayout(null);
	}

	/**
	 * ������д����ı༭����������һ��JPanel���󼴿ɣ�Ҳ����ֱ�ӷ���һ��Button���󣬵��������������������Ԫ��
	 */
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		// ֻΪ��ť��ֵ���ɡ�Ҳ����������������
		this.button.setText(value == null ? "" : String.valueOf(value));
		return this.panel;
	}

	/**
	 * ��д�༭��Ԫ��ʱ��ȡ��ֵ���������д��������ܻ�Ϊ��ť���ô����ֵ��
	 */
	@Override
	public Object getCellEditorValue() {
		return this.button.getText();
	}

}

