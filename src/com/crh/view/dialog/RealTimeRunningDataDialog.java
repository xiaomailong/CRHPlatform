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
 * ��̬������չʾ������
 * @author huhui
 *
 */
public class RealTimeRunningDataDialog extends JDialog implements Runnable {

	/**
	 *  rowData������������ݣ�columnNames�����������
	 */
	private Vector<String> columnNamesVector;
	private JTable calResultTable;
	private JScrollPane jScrollPane,areaScrollPane;
	private DefaultTableModel tableModel;
	/**
	 * �ı�������
	 */
	private JTextArea consoleArea;
	private JSplitPane splitPane;

	/**
	 *  ���캯������������
	 */
	public RealTimeRunningDataDialog() {
		columnNamesVector = new Vector<String>();
		// ������������ʼ��JTable
		columnNamesVector.add("ʱ��(s)");
		columnNamesVector.add("�ٶ�(km/h)");
		columnNamesVector.add("���(km)");
		columnNamesVector.add("ǣ���ܺ�(KJ)");
		columnNamesVector.add("��������(KJ)");
		columnNamesVector.add("���ܺ�(KJ)");
		columnNamesVector.add("ǣ����(KN)");
		columnNamesVector.add("�ƶ���(KN)");
		columnNamesVector.add("��������(KN)");
		columnNamesVector.add("200:300ƽ�����ٶ�(m/s^2)");
		columnNamesVector.add("�Ƿ���");

		// ��ʼ��JTable
		tableModel = new DefaultTableModel(columnNamesVector,0);
		calResultTable = new JTable(tableModel);
		jScrollPane = new JScrollPane(calResultTable);
		
		//��ʼ��JTextArea
		consoleArea = new JTextArea();
		consoleArea.setEditable(false);
		areaScrollPane = new JScrollPane(consoleArea);
		//��ʼ���ָ���
		splitPane = new JSplitPane();
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setTopComponent(jScrollPane);
		splitPane.setBottomComponent(areaScrollPane);
		splitPane.setDividerSize(2);
		splitPane.setDividerLocation(600);
		
		// ����dialog
		this.setTitle("���ڼ���");
		this.add(splitPane);
		this.setSize(680, 720);
		MyUtillity.setFrameOnCenter(this);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setAlwaysOnTop(true);
		this.setVisible(true);
	}
	
	/**
	 * ����һ��������
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
	 * ����consoleArea����
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
