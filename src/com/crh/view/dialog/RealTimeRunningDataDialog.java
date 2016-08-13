package com.crh.view.dialog;

import java.awt.Rectangle;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

import com.crh2.util.MyTools;
import com.crh2.util.MyUtillity;

/**
 * 常态仿真中展示计算结果
 * @author huhui
 *
 */
public class RealTimeRunningDataDialog extends JDialog implements Runnable {

	/**
	 *  rowData用来存放行数据，columnNames用来存放列名
	 */
	private Vector<String> columnNamesVector;
	private JTable calResultTable;
	private JScrollPane jScrollPane,areaScrollPane;
	private DefaultTableModel tableModel;
	/**
	 * 文本框区域
	 */
	private JTextArea consoleArea;
	private JSplitPane splitPane;

	/**
	 *  构造函数，设置列名
	 */
	public RealTimeRunningDataDialog() {
		columnNamesVector = new Vector<String>();
		// 设置列名，初始化JTable
		columnNamesVector.add("时间(s)");
		columnNamesVector.add("速度(km/h)");
		columnNamesVector.add("里程(km)");
		columnNamesVector.add("牵引能耗(KJ)");
		columnNamesVector.add("再生能量(KJ)");
		columnNamesVector.add("总能耗(KJ)");
		columnNamesVector.add("牵引力(KN)");
		columnNamesVector.add("制动力(KN)");
		columnNamesVector.add("空气阻力(KN)");
		columnNamesVector.add("200:300平均加速度(m/s^2)");
		columnNamesVector.add("是否超速");

		// 初始化JTable
		tableModel = new DefaultTableModel(columnNamesVector,0);
		calResultTable = new JTable(tableModel);
		jScrollPane = new JScrollPane(calResultTable);
		
		//初始化JTextArea
		consoleArea = new JTextArea();
		consoleArea.setEditable(false);
		areaScrollPane = new JScrollPane(consoleArea);
		//初始化分隔栏
		splitPane = new JSplitPane();
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setTopComponent(jScrollPane);
		splitPane.setBottomComponent(areaScrollPane);
		splitPane.setDividerSize(2);
		splitPane.setDividerLocation(600);
		
		// 设置dialog
		this.setTitle("正在计算");
		this.add(splitPane);
		this.setSize(680, 720);
		MyUtillity.setFrameOnCenter(this);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setAlwaysOnTop(true);
		this.setVisible(true);
	}
	
	/**
	 * 增加一条表数据
	 * @param time
	 * @param speed
	 * @param location
	 * @param tractionPower
	 * @param elecPower
	 * @param power
	 * @param tractionForce
	 * @param brakeForce
	 * @param airFriction
	 * @param aveA
	 * @param isOverSpeed
	 */
	public void appendTableData(double time, double speed, double location,
			double tractionPower, double elecPower, double power,
			double tractionForce, double brakeForce, double airFriction,
			double aveA, String isOverSpeed) {
		Vector rundata = new Vector();
		rundata.add(time);
		rundata.add(MyTools.numFormat2(speed));
		rundata.add(MyTools.numFormat2(location));
		rundata.add(MyTools.numFormat2(tractionPower));
		rundata.add(MyTools.numFormat2(elecPower));
		rundata.add(MyTools.numFormat2(power));
		rundata.add(MyTools.numFormat2(tractionForce));
		rundata.add(MyTools.numFormat2(brakeForce));
		rundata.add(MyTools.numFormat2(airFriction));
		rundata.add(MyTools.numFormat2(aveA));
		rundata.add(isOverSpeed);
		rundata.add(MyTools.numFormat2(power));
		rundata.add(MyTools.numFormat2(elecPower));
		tableModel.addRow(rundata);
		int rowCount = calResultTable.getRowCount();  
		Rectangle rect = calResultTable.getCellRect(rowCount - 1, 0, true); 
		calResultTable.scrollRectToVisible(rect);
	}
	
	/**
	 * 增加consoleArea内容
	 * @param content
	 */
	public void appendConsoleArea(String content){
		if(content != null && !content.equals("")){
			consoleArea.append(content+"\r\n");
			consoleArea.selectAll();
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}
