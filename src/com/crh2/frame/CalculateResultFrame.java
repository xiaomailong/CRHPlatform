/**
 * 展示数据计算结果
 */
package com.crh2.frame;

import java.awt.Frame;
import java.util.List;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.crh2.dao.SQLHelper;
import com.crh2.util.MyUtillity;

public class CalculateResultFrame extends JDialog {

	/**
	 * @param args
	 */
	// rowData用来存放行数据，columnNames用来存放列名
	Vector<String> columnNamesVector;
	Vector<Vector<String>> rowDataVector;
	JTable calResultTable;
	JScrollPane jScrollPane;

	// 构造函数
	public CalculateResultFrame(Frame owner, boolean modal, String routeName,int routeId) {
		super(owner, modal);// 调用父类构造函数
		columnNamesVector = new Vector<String>();
		// 设置列名
		columnNamesVector.add("时间");
		columnNamesVector.add("速度");
		columnNamesVector.add("里程");
		columnNamesVector.add("加速度");
		columnNamesVector.add("阻力做功");

		// 初始化计算数据
		rowDataVector = this.getAllCalculateData(routeId);

		// 初始化JTable
		calResultTable = new JTable(rowDataVector, columnNamesVector);
		jScrollPane = new JScrollPane(calResultTable);

		// 设置dialog
		this.setTitle(routeName + "计算数据");
		this.add(jScrollPane);
		this.setSize(680, 750);
		MyUtillity.setFrameOnCenter(this);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}

	// 得到所有计算数据
	public Vector<Vector<String>> getAllCalculateData(int routeId) {
		String sql = "SELECT rd.runtime,rd.speed,rd.distance,rd.cp,rd.power FROM rundata rd WHERE rd.routeid="+routeId+"";
		SQLHelper sqlHelper = new SQLHelper();
		List list = sqlHelper.query(sql, null);
		Vector<Vector<String>> rundataVector = new Vector<Vector<String>>();
		for (int i = 0; i < list.size(); i++) {
			Object[] obj = (Object[]) list.get(i);
			Vector<String> rundata = new Vector<String>();
			rundata.add(obj[0].toString());
			rundata.add(obj[1].toString());
			rundata.add(obj[2].toString());
			rundata.add(obj[3].toString());
			rundata.add(obj[4].toString());
			rundataVector.add(rundata);
		}
		return rundataVector;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CalculateResultFrame calculateResultFrame = new CalculateResultFrame(
				null, false, "京津线（上行）",1);
	}

}
