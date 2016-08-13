/**
 * չʾ���ݼ�����
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
	// rowData������������ݣ�columnNames�����������
	Vector<String> columnNamesVector;
	Vector<Vector<String>> rowDataVector;
	JTable calResultTable;
	JScrollPane jScrollPane;

	// ���캯��
	public CalculateResultFrame(Frame owner, boolean modal, String routeName,int routeId) {
		super(owner, modal);// ���ø��๹�캯��
		columnNamesVector = new Vector<String>();
		// ��������
		columnNamesVector.add("ʱ��");
		columnNamesVector.add("�ٶ�");
		columnNamesVector.add("���");
		columnNamesVector.add("���ٶ�");
		columnNamesVector.add("��������");

		// ��ʼ����������
		rowDataVector = this.getAllCalculateData(routeId);

		// ��ʼ��JTable
		calResultTable = new JTable(rowDataVector, columnNamesVector);
		jScrollPane = new JScrollPane(calResultTable);

		// ����dialog
		this.setTitle(routeName + "��������");
		this.add(jScrollPane);
		this.setSize(680, 750);
		MyUtillity.setFrameOnCenter(this);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}

	// �õ����м�������
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
				null, false, "�����ߣ����У�",1);
	}

}
