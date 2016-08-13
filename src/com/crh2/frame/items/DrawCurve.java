package com.crh2.frame.items;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import com.crh2.javabean.Curve;
import com.crh2.service.DataService;
import com.crh2.util.MyTools;

/**
 * 画弯道，属于动态。根据数据库中的弯道信息，等比例画出列车经过的弯道
 * @author huhui
 *
 */
public class DrawCurve extends JPanel {
	/**
	 * 初始化坐标.y坐标为第二条线与第三条线之间
	 */
	private int x = 370, y = 198;
	private double speed = 0;
	private DataService ds = null;
	/**
	 * 存放弯道信息的list
	 */
	private List<Curve> curveList = null;
	/**
	 * 定义所有点的x,y坐标集合
	 */
	private ArrayList<Integer> pointsX, pointsY;
	/**
	 * 存放由集合转化数组
	 */
	private int a[],b[];
	/**
	 * 比例系数(单位统一用米)
	 */
	private double para = MyTools.lengthIndex;
	/**
	 * 定义需要写弯道信息的x,y坐标集合
	 */
	private ArrayList<Integer> infX, infY;
	private ArrayList<String> curveInfo;
	private int rx[],ry[];
	
	/**
	 * 构造函数，初始化数据
	 * @param routeId
	 */
	public DrawCurve(int routeId){
		ds = new DataService();
		curveList = ds.getCurveData(routeId);//拿到Curve所有数据
		pointsX = new ArrayList<Integer>();
		pointsY = new ArrayList<Integer>();
		infX = new ArrayList<Integer>();
		infY = new ArrayList<Integer>();
		curveInfo = new ArrayList<String>();
		this.infoToPoints();//信息转化为坐标点
	}
	
	/**
	 * 将弯道信息，转化为一个一个坐标点
	 */
	public void infoToPoints(){
		//先放入起点坐标
		pointsX.add(x);
		pointsY.add(y+53); //2014.10.15  135->53
		for(int i=0;i<curveList.size();i++){
			Curve curve = curveList.get(i);
			//第一个点
			double px1 = x + curve.getStart()*1000*para;
//			double px1 = x + curve.getStart()*1000;
			double py1 = pointsY.get(pointsX.size()-1);
			//加入坐标集合
			pointsX.add((int)px1);
			pointsY.add((int)py1);
			
			//第二个点
			double px2 = 0,py2 = 0;
			if(curve.getRadius()>0){//如果大于0，则向上
				px2 = px1;
				py2 = py1 - curve.getRadius()/1000;
			}else if(curve.getRadius()<0){//如果小于0，则向下
				px2 = px1;
				py2 = py1 + curve.getRadius()/1000;
			}
			//加入坐标集合
			pointsX.add((int)px2);
			pointsY.add((int)py2);
			
			//第三个点
			double px3 = px2 + curve.getLength()*para;
//			double px3 = px2 + curve.getLength();
			double py3 = py2;
			//加入坐标集合
			pointsX.add((int)px3);
			pointsY.add((int)py3);
			
			//第二个点和第三个点之间，需要写出曲线半径
			infX.add((int)((px2 + px3)/2));
			infY.add((int)py2);
			curveInfo.add(curve.getRadius()+"");
			
			//第四个点
			double px4 = 0, py4 = 0;
			if(curve.getRadius()>0){//如果大于0，则向下
				px4 = px3;
				py4 = py3 + curve.getRadius()/1000;
			}else if(curve.getRadius()<0){//如果小于0，则向上
				px4 = px3;
				py4 = py3 - curve.getRadius()/1000;
			}
			//加入坐标集合
			pointsX.add((int)px4);
			pointsY.add((int)py4);
			
		}
		//最后一个点
		double endX = x + 115.73071*1000*para;
//		double endX = x + 115.73071*1000;
		double endY = 251; //2014.10.15 333->251
		pointsX.add((int)endX);
		pointsY.add((int)endY);
		
		//是否半径信息删除最后一个点
		
		this.toArray();//转化为数组
	}
	
	/**
	 * 将List转化为数组
	 */
	public void toArray(){
		//点
		a = this.listToArray(pointsX);
		b = this.listToArray(pointsY);
		//半径
		rx = this.listToArray(infX);
		ry = this.listToArray(infY);
	}
	
	/**
	 * 将Integer数组转化为int数组
	 * @param array
	 * @return
	 */
	public int [] listToArray(ArrayList<Integer> array){
		int [] a = new int[array.size()];
		for(int i=0;i<array.size();i++){
			a[i] = array.get(i);
		}
		return a;
	}
	
	/**
	 * 设置速度
	 * @param speed
	 */
	public void setSpeed(double speed){
		this.speed = speed;
	}
	
	/**
	 * 通过Curve数据，画出所有弯道信息
	 * @param g
	 */
	public void drawCurve(Graphics g){
		g.drawPolyline(a, b, a.length);
		this.drawRadius(g);
	}
	
	/**
	 * 画出半径数据
	 * @param g
	 */
	public void drawRadius(Graphics g){
		for(int i=0;i<curveInfo.size();i++){
			g.drawString(curveInfo.get(i), rx[i], ry[i]);
		}
	}
	
	/**
	 * 使弯道动起来
	 */
	public void run() {
		setPoints(a, speed);
		setPoints(rx, speed);
	}
	
	/**
	 * 设置各个点的坐标值
	 * @param array
	 * @param speed
	 */
	public void setPoints(int [] array,double speed){
		for(int i=0;i<array.length;i++){
			array[i] -= (int)speed;
		}
	}
	
	
}
