package com.crh.view.dialog;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import com.crh.service.RouteDataManagementDialogService;
import com.crh2.javabean.TrainStopStation;
import com.crh2.util.MyUtillity;

/**
 * 2014.10.19 设置列车停站时间
 * @author huhui
 *
 */
public class RouteStationStopTimeDialog extends JDialog {
	
	private JPanel tablePanel;
	
	private JTable stopTimeTable;
	private DefaultTableModel tableModel;
	private JScrollPane stopTimeTableScrollPane;
	/**
	 * 列名
	 */
	private Vector<String> columnNamesVector;
	
	private JLabel routeNameLabel = null, trainNumLabel = null;
	private int routeId;

	public RouteStationStopTimeDialog(final String routeName, final String trainNum) {
		setTitle("停站时间设置");
		setBounds(100, 100, 600, 400);
		MyUtillity.setFrameOnCenter(this);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);
		
		this.routeId = RouteDataManagementDialogService.getRouteIdByName(routeName);
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		panel.setBounds(10, 10, 564, 37);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel label = new JLabel("路线名称：");
		label.setBounds(10, 10, 60, 15);
		panel.add(label);
		
		routeNameLabel = new JLabel(routeName);
		routeNameLabel.setBounds(80, 7, 103, 21);
		panel.add(routeNameLabel);
		
		JLabel label_1 = new JLabel("车次：");
		label_1.setBounds(226, 10, 54, 15);
		panel.add(label_1);
		
		trainNumLabel = new JLabel(trainNum);
		trainNumLabel.setBounds(275, 7, 103, 21);
		panel.add(trainNumLabel);
		
		JButton button_3 = new JButton("保存");
		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				saveTrainNum(routeId, routeName, trainNum);
			}
		});
		button_3.setBounds(410, 6, 93, 23);
		panel.add(button_3);
		
		tablePanel = new JPanel();
		tablePanel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		tablePanel.setBounds(10, 57, 564, 295);
		getContentPane().add(tablePanel);
		tablePanel.setLayout(new GridLayout(1, 1, 0, 0));
		
		//显示表格
		createTrainNumTable();
		tablePanel.add(stopTimeTableScrollPane);
		showStopTime(routeId, routeName, trainNum);
		setModal(true);
		setVisible(true);
	}
	
	/**
	 * 显示车站信息
	 * @param routeName
	 * @param trainNum
	 */
	public void showStopTime(int routeId,String routeName, String trainNum){
		tableModel.setRowCount(0);
		ArrayList<TrainStopStation> beanList = RouteDataManagementDialogService.getStationStopTime(routeId, routeName, trainNum);
		if(beanList.size() == 0){
			JOptionPane.showMessageDialog(RouteStationStopTimeDialog.this, "该线路尚未导入数据！");
		}else{
			for(TrainStopStation bean : beanList){
				Vector<String> line = new Vector<String>();
				line.add(bean.getStationId() + "");
				line.add(bean.getStationName());
				line.add(bean.getStopTIme() + "");
				tableModel.addRow(line);
			}
		}
	}
	
	/**
	 * 保存表中的内容
	 */
	public void saveTrainNum(int trainRouteId, String routeName, String trainNum){
		ArrayList<TrainStopStation> beanList = new ArrayList<TrainStopStation>();
		for(int i=0; i < tableModel.getRowCount(); i++){
			TrainStopStation bean = new TrainStopStation();
			Vector line = (Vector) tableModel.getDataVector().elementAt(i);
			for(int j=0;j<line.size();j++){
				if(line.get(j) == null){
					JOptionPane.showMessageDialog(RouteStationStopTimeDialog.this, "值不能为空！");
					stopTimeTable.setRowSelectionInterval(i, i);
					return;
				}
			}
			bean.setTrainRouteId(trainRouteId);
			bean.setRouteName(routeName);
			bean.setStationId(Integer.parseInt((String) line.get(0)));
			bean.setStationName((String) line.get(1));
			bean.setTrainNum(trainNum);
			bean.setStopTIme(Double.parseDouble((String) line.get(2)));
			beanList.add(bean);
		}
		RouteDataManagementDialogService.saveStationStopTime(trainRouteId, trainNum, beanList);
		JOptionPane.showMessageDialog(RouteStationStopTimeDialog.this, "保存成功！");
	}
	
	/**
	 * 生成表格
	 */
	public void createTrainNumTable(){
		initColumnName();
		tableModel = new DefaultTableModel(columnNamesVector, 0){
			public boolean isCellEditable(int row, int column){
				if(column == 0 || column == 1 || (row == 0 && column == 2) || (row == tableModel.getRowCount()-1 && column == 2)){//设置首站和终点站不可编辑
					return false;
				}
				return true;
			}
		};
		stopTimeTable = new JTable(tableModel);
		stopTimeTable.putClientProperty("terminateEditOnFocusLost", true);
		stopTimeTableScrollPane = new JScrollPane(stopTimeTable);
	}
	
	/**
	 * 初始化表头
	 */
	public void initColumnName(){
		columnNamesVector = new Vector<String>();
		columnNamesVector.add("序号");
		columnNamesVector.add("站名");
		columnNamesVector.add("停站时间(s)");
	}
	
}

