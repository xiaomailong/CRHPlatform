/**
 * 用于生成四个曲线图
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

	// 构造函数
	public FourChartsFrame(Frame owner, boolean modal,int routeId) {
		super(owner, modal);
		//初始化vs，ts表的数据
		this.initRundataForVS(routeId);
		// 设置布局
		this.setLayout(new GridLayout(2, 2,5,1));
		//将图表加入到JDialog
		SpeedDistanceChart speedDistanceChart = new SpeedDistanceChart(vsDataList);//速度-里程曲线
		TimeDistanceChart timeDistanceChart = new TimeDistanceChart(tsDataList);//时间-里程曲线
		TractiveCharacteristicCurveChart tractiveCharacteristicCurveChart = new TractiveCharacteristicCurveChart(this.getTrainIdbyRouteName(routeId));//牵引力特性曲线
		BrakeCharacteristicCurveChart brakeCharacteristicCurveChart = new BrakeCharacteristicCurveChart(this.getTrainIdbyRouteName(routeId));//制动特性曲线
		this.add(tractiveCharacteristicCurveChart);
		this.add(brakeCharacteristicCurveChart);
		this.add(speedDistanceChart);
		this.add(timeDistanceChart);
		this.setTitle("曲线图表生成");
		this.setSize(1000, 800);
		MyUtillity.setFrameOnCenter(this);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}
	
	//获取V-S曲线所需数据
	public void initRundataForVS(int routeId){
		//初始化list
		vsDataList = new ArrayList<Rundata>();
		tsDataList = new ArrayList<Rundata>();
		//从数据库取数据
		String sql = "SELECT rd.power,rd.speed,rd.distance FROM rundata rd WHERE rd.routeid="+routeId+" AND rd.id-(rd.id/3)*3=0";
		SQLHelper sqlHelper = new SQLHelper();
		List list = sqlHelper.query(sql, null);
		for (int i = 0; i < list.size(); i++) {
			Object[] obj = (Object[]) list.get(i);
			Rundata vsRundata = new Rundata();
			Rundata tsRundata = new Rundata();
			//给vs表准备数据
			vsRundata.setSpeed(Double.parseDouble(obj[1].toString()));
			vsRundata.setDistance(Double.parseDouble(obj[2].toString()));
			//给ts表准备数据
			tsRundata.setRuntime(((Double)obj[0])*1.23/3600000);
			tsRundata.setDistance(Double.parseDouble(obj[2].toString()));
			//给响应的list赋值
			vsDataList.add(vsRundata);
			tsDataList.add(tsRundata);
		}
	}
	
	//通过线路id，得到该线路的车的型号的id
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
