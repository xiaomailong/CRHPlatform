/**
 * ���������ĸ�����ͼ
 */
package com.crh2.frame;

import java.awt.Frame;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JFrame;

import com.crh2.chart.BrakeCharacteristicCurveChart;
import com.crh2.chart.SpeedDistanceChart;
import com.crh2.chart.TimeDistanceChart;
import com.crh2.chart.TractiveCharacteristicCurveChart;
import com.crh2.dao.SQLHelper;
import com.crh2.javabean.Rundata;
import com.crh2.util.MyUtillity;

public class FourChartsFrame extends JDialog {

	/**
	 * @param args
	 */
	private ArrayList<Rundata> vsDataList,tsDataList;

	// ���캯��
	public FourChartsFrame(Frame owner, boolean modal,int routeId) {
		super(owner, modal);
		//��ʼ��vs��ts�������
		this.initRundataForVS(routeId);
		// ���ò���
		this.setLayout(new GridLayout(2, 2,5,1));
		//��ͼ����뵽JDialog
		SpeedDistanceChart speedDistanceChart = new SpeedDistanceChart(vsDataList);//�ٶ�-�������
		TimeDistanceChart timeDistanceChart = new TimeDistanceChart(tsDataList);//ʱ��-�������
		TractiveCharacteristicCurveChart tractiveCharacteristicCurveChart = new TractiveCharacteristicCurveChart(this.getTrainIdbyRouteName(routeId));//ǣ������������
		BrakeCharacteristicCurveChart brakeCharacteristicCurveChart = new BrakeCharacteristicCurveChart(this.getTrainIdbyRouteName(routeId));//�ƶ���������
		this.add(tractiveCharacteristicCurveChart);
		this.add(brakeCharacteristicCurveChart);
		this.add(speedDistanceChart);
		this.add(timeDistanceChart);
		this.setTitle("����ͼ������");
		this.setSize(1000, 800);
		MyUtillity.setFrameOnCenter(this);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}
	
	//��ȡV-S������������
	public void initRundataForVS(int routeId){
		//��ʼ��list
		vsDataList = new ArrayList<Rundata>();
		tsDataList = new ArrayList<Rundata>();
		//�����ݿ�ȡ����
		String sql = "SELECT rd.power,rd.speed,rd.distance FROM rundata rd WHERE rd.routeid="+routeId+" AND rd.id-(rd.id/3)*3=0";
		SQLHelper sqlHelper = new SQLHelper();
		List list = sqlHelper.query(sql, null);
		for (int i = 0; i < list.size(); i++) {
			Object[] obj = (Object[]) list.get(i);
			Rundata vsRundata = new Rundata();
			Rundata tsRundata = new Rundata();
			//��vs��׼������
			vsRundata.setSpeed(Double.parseDouble(obj[1].toString()));
			vsRundata.setDistance(Double.parseDouble(obj[2].toString()));
			//��ts��׼������
			tsRundata.setRuntime(((Double)obj[0])*1.23/3600000);
			tsRundata.setDistance(Double.parseDouble(obj[2].toString()));
			//����Ӧ��list��ֵ
			vsDataList.add(vsRundata);
			tsDataList.add(tsRundata);
		}
	}
	
	//ͨ����·id���õ�����·�ĳ����ͺŵ�id
	public int getTrainIdbyRouteName(int routeid){
		SQLHelper sqlHelper = new SQLHelper();
		String sql = "SELECT traintypeid FROM routename WHERE id = "+routeid+"";
		List list = sqlHelper.query(sql, null);
		return Integer.parseInt(((Object [])list.get(0))[0].toString());
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FourChartsFrame fourChartsFrame = new FourChartsFrame(null,false,1);
	}

}
