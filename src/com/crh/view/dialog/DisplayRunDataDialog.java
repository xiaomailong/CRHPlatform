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
 * չʾ���ݼ�����
 * @author huhui
 *
 */
public class DisplayRunDataDialog extends JDialog {

	/**
	 *  columnNames�����������
	 */
	private Vector<String> columnNamesVector;
	/**
	 * rowData�������������
	 */
	private Vector<Vector<String>> rowDataVector;
	/**
	 * չʾ�������ı��
	 */
	private JTable calResultTable;
	/**
	 * ���Ļ�����
	 */
	private JScrollPane jScrollPane;

	/**
	 * ���캯��������������ʼ����������
	 * @param routeName
	 * @param rundataList
	 */
	public DisplayRunDataDialog(String routeName, ArrayList<Rundata> rundataList) {
		columnNamesVector = new Vector<String>();
		// ��������
		columnNamesVector.add("ʱ��");
		columnNamesVector.add("�ٶ�");
		columnNamesVector.add("���");
		columnNamesVector.add("����");
		columnNamesVector.add("ǣ����");
		columnNamesVector.add("�ƶ���");
		columnNamesVector.add("���ƶ���");
		columnNamesVector.add("��������");

		// ��ʼ����������
		rowDataVector = this.getAllCalculateData(rundataList);

		// ��ʼ��JTable
		calResultTable = new JTable(rowDataVector, columnNamesVector);
		jScrollPane = new JScrollPane(calResultTable);

		// ����dialog
		this.setTitle(routeName + "��������");
		this.setModal(true);
		this.add(jScrollPane);
		this.setSize(980, 740);
		MyUtillity.setFrameOnCenter(this);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}

	/**
	 *  �õ����м�������
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
