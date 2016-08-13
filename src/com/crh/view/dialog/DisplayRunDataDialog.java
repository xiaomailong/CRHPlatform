package com.crh.view.dialog;

import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.crh2.javabean.Rundata;
import com.crh2.util.MyTools;
import com.crh2.util.MyUtillity;

/**
 * 展示数据计算结果
 * @author huhui
 *
 */
public class DisplayRunDataDialog extends JDialog {

	/**
	 *  columnNames用来存放列名
	 */
	private Vector<String> columnNamesVector;
	/**
	 * rowData用来存放行数据
	 */
	private Vector<Vector<String>> rowDataVector;
	/**
	 * 展示计算结果的表格
	 */
	private JTable calResultTable;
	/**
	 * 表格的滑动条
	 */
	private JScrollPane jScrollPane;

	/**
	 * 构造函数设置列名、初始化计算数据
	 * @param routeName
	 * @param rundataList
	 */
	public DisplayRunDataDialog(String routeName, ArrayList<Rundata> rundataList) {
		columnNamesVector = new Vector<String>();
		// 设置列名
		columnNamesVector.add("时间");
		columnNamesVector.add("速度");
		columnNamesVector.add("里程");
		columnNamesVector.add("能量");
		columnNamesVector.add("牵引力");
		columnNamesVector.add("制动力");
		columnNamesVector.add("电制动力");
		columnNamesVector.add("附加阻力");

		// 初始化计算数据
		rowDataVector = this.getAllCalculateData(rundataList);

		// 初始化JTable
		calResultTable = new JTable(rowDataVector, columnNamesVector);
		jScrollPane = new JScrollPane(calResultTable);

		// 设置dialog
		this.setTitle(routeName + "计算数据");
		this.setModal(true);
		this.add(jScrollPane);
		this.setSize(980, 740);
		MyUtillity.setFrameOnCenter(this);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}

	/**
	 *  得到所有计算数据
	 * @param rundataList
	 * @return
	 */
	public Vector<Vector<String>> getAllCalculateData(ArrayList<Rundata> rundataList) {
		Vector<Vector<String>> rundataVector = new Vector<Vector<String>>();
		for (Rundata bean : rundataList) {
			Vector<String> rundata = new Vector<String>();
			rundata.add(MyTools.numFormat2(bean.getRuntime()) + "");
			rundata.add(MyTools.numFormat2(bean.getSpeed()) +"");
			rundata.add(MyTools.numFormat2(bean.getLocation()) +"");
			rundata.add(MyTools.numFormat2(bean.getPower()) +"");
			rundata.add(MyTools.numFormat2(bean.getTractionForce()) +"");
			rundata.add(MyTools.numFormat2(bean.getBrakeForce()) +"");
			rundata.add(MyTools.numFormat2(bean.getEleBrakeForce()) +"");
			rundata.add(MyTools.numFormat2(bean.getOtherForce()) +"");
			rundataVector.add(rundata);
		}
		return rundataVector;
	}

}
