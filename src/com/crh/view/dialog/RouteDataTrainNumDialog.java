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
 * 车次设置
 * @author huhui
 *
 */
public class RouteDataTrainNumDialog extends JDialog {
	
	private JPanel tablePanel;
	private JComboBox<String> routeNameComboBox;
	private String[] routeNameArray, trainNumComBoxArray;
	private JComboBox<String> trainNumComboBox;
	private String DEFAULTROUTENAME = "请选择", DEFAULTTRAINNUM = "所有车次";
	/**
	 * 当选择“所有车次”时对应的车次号
	 */
	public static final int DEFAULTNUM = -1;
	
	public static JTable trainNumTable;
	private DefaultTableModel tableModel;
	private JScrollPane trainNumTableScrollPane;
	/**
	 * 表的列名
	 */
	private Vector<String> columnNamesVector;
	private String trainNumIndex = DEFAULTTRAINNUM;
	
	public RouteDataTrainNumDialog(Dialog owner, String routeName) {
		super(owner);
		setTitle("车次设置");
		setBounds(100, 100, 916, 527);
		MyUtillity.setFrameOnCenter(this);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		panel.setBounds(10, 10, 880, 37);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel label = new JLabel("路线名称：");
		label.setBounds(10, 10, 60, 15);
		panel.add(label);
		
		initCombBoxArray();//初始化
		
		routeNameComboBox = new JComboBox<String>(new DefaultComboBoxModel<String>(routeNameArray));
		routeNameComboBox.setBounds(80, 7, 103, 21);
		panel.add(routeNameComboBox);
		routeNameComboBox.setSelectedItem(routeName);
		
		JLabel label_1 = new JLabel("车次：");
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
		
		JButton button = new JButton("查看");
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
					JOptionPane.showMessageDialog(RouteDataTrainNumDialog.this, "请选择线路名称！");
				}
			}
		});
		button.setBounds(426, 6, 93, 23);
		panel.add(button);
		
		JButton button_1 = new JButton("添加");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(isSelectRoute()){
					appendOneLine();
				}else{
					JOptionPane.showMessageDialog(RouteDataTrainNumDialog.this, "请选择线路名称！");
				}
				
			}
		});
		button_1.setBounds(540, 6, 93, 23);
		panel.add(button_1);
		
		JButton button_2 = new JButton("删除");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(isSelectRoute()){
					removeOneLine();
				}else{
					JOptionPane.showMessageDialog(RouteDataTrainNumDialog.this, "请选择线路名称！");
				}
			}
		});
		button_2.setBounds(656, 6, 93, 23);
		panel.add(button_2);
		
		JButton button_3 = new JButton("保存");
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
					JOptionPane.showMessageDialog(RouteDataTrainNumDialog.this, "请选择线路名称！");
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
		
		//显示表格
		createTrainNumTable();
		tablePanel.add(trainNumTableScrollPane);
		displayTrainNum(routeName, DEFAULTNUM+"");
		setModal(true);
		setVisible(true);
	}
	
	/**
	 * 显示车次信息
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
			line.add("设置");
			line.add(bean.getTrainNum());
			tableModel.addRow(line);
		}
	}
	
	/**
	 * 保存表中的内容
	 */
	public void saveTrainNum(String routeName, String trainNum){
		ArrayList<TrainRouteTrainnum> beanList = new ArrayList<TrainRouteTrainnum>();
		for(int i=0; i < tableModel.getRowCount(); i++){
			TrainRouteTrainnum bean = new TrainRouteTrainnum();
			Vector line = (Vector) tableModel.getDataVector().elementAt(i);
			for(int j=0;j<line.size();j++){
				if(line.get(j) == null){
					JOptionPane.showMessageDialog(RouteDataTrainNumDialog.this, "值不能为空！");
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
			bean.setStopTime(1);//2014.10.19 停站时间独立设置，此处默认保存1
			bean.setTrainNum(line.get(7).toString());
			beanList.add(bean);
		}
		RouteDataManagementDialogService.saveTrainNum(routeName, trainNum, beanList);
		JOptionPane.showMessageDialog(RouteDataTrainNumDialog.this, "保存成功！");
	}
	
	/**
	 * 增加一行
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
			line.add(6, "设置");
		}
		tableModel.addRow(line);
	}
	
	/**
	 * 删除一行
	 */
	public void removeOneLine(){
		int selectedIndex = trainNumTable.getSelectedRow();
		if(selectedIndex == -1){
			JOptionPane.showMessageDialog(RouteDataTrainNumDialog.this, "请选择一行再删除！");
			return;
		}
		int option = JOptionPane.showConfirmDialog(RouteDataTrainNumDialog.this, "确认删除？");
		if(option == 0){
			tableModel.removeRow(selectedIndex);
		}
	}
	
	/**
	 * 生成表格
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
		
		//在trainNumTable中添加“停站时间”按钮
		trainNumTable.getColumnModel().getColumn(6).setCellEditor(new MyButtonEditor());
		trainNumTable.getColumnModel().getColumn(6).setCellRenderer(new MyButtonRender());
	}
	
	/**
	 * 初始化表头
	 */
	public void initColumnName(){
		columnNamesVector = new Vector<String>();
		columnNamesVector.add("线路名称");
		columnNamesVector.add("序号");
		columnNamesVector.add("线路编号");
		columnNamesVector.add("上下行");
		columnNamesVector.add("站名");
		columnNamesVector.add("运行时间(min)");
		columnNamesVector.add("停站时间(s)");
		columnNamesVector.add("车次");
	}
	
	/**
	 * 更新trainNumComboBox的值
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
	 * 初始化combBox所对应的数组
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
	 * 点击按钮前，判断是否选择了线路名
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
 * 实现的自定义的渲染器
 * @author huhui
 */
class MyButtonRender implements TableCellRenderer {
	private JPanel panel;

	private JButton button;

	public MyButtonRender() {
		this.initButton();
		this.initPanel();
		// 添加按钮。
		this.panel.add(this.button);
	}

	private void initButton() {
		this.button = new JButton();
		// 设置按钮的大小及位置。
		this.button.setBounds(15, 0, 80, 15);
	}

	private void initPanel() {
		this.panel = new JPanel();
		// panel使用绝对定位，这样button就不会充满整个单元格。
		this.panel.setLayout(null);
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		// 只为按钮赋值即可。也可以作其它操作，如绘背景等。
		this.button.setText(value == null ? "" : String.valueOf(value));
		return this.panel;
	}

}

/**
 * 添加事件
 * @author huhui
 *
 */
class MyButtonEditor extends DefaultCellEditor {

	private JPanel panel;
	private JButton button;

	public MyButtonEditor() {
		// DefautlCellEditor有此构造器，需要传入一个，但这个不会使用到，直接new一个即可。
		super(new JTextField());
		// 设置点击几次激活编辑。
		this.setClickCountToStart(1);
		this.initButton();
		this.initPanel();
		// 添加按钮。
		this.panel.add(this.button);
	}

	private void initButton() {
		this.button = new JButton();
		// 设置按钮的大小及位置。
		this.button.setBounds(15, 0, 80, 15);
		// 为按钮添加事件。这里只能添加ActionListner事件，Mouse事件无效。
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
		// panel使用绝对定位，这样button就不会充满整个单元格。
		this.panel.setLayout(null);
	}

	/**
	 * 这里重写父类的编辑方法，返回一个JPanel对象即可（也可以直接返回一个Button对象，但是那样会填充满整个单元格）
	 */
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		// 只为按钮赋值即可。也可以作其它操作。
		this.button.setText(value == null ? "" : String.valueOf(value));
		return this.panel;
	}

	/**
	 * 重写编辑单元格时获取的值。如果不重写，这里可能会为按钮设置错误的值。
	 */
	@Override
	public Object getCellEditorValue() {
		return this.button.getText();
	}

}

