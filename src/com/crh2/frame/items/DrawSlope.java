package com.crh2.frame.items;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import com.crh2.javabean.Slope;
import com.crh2.javabean.StationInfo;
import com.crh2.service.DataService;
import com.crh2.util.MyTools;

/**
 * 绘制坡度,只绘制列车实际运行的坡度，上下坡
 * @author huhui
 *
 */
public class DrawSlope {
	/**
	 * 初始化坐标
	 */
	private int x = 370, y = 266;//初始化坐标.y坐标为第一条线与第二条线之间    2014.10.15 389->266
	private double speed = 0;
	private DataService ds = null;
	/**
	 * 存放所有坡度信息
	 */
	private List<Slope> slopeList = null;
	/**
	 * 存放所有车站信息
	 */
	private List<StationInfo> stationInfoList = null;
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
	private ArrayList<String> slopesInfo;
	private int rx[],ry[];
	private int pa[],pb[];
	/**
	 * 定义一个ArrayList,里面存放x坐标小于370的坡道点的y坐标
	 */
	private ArrayList<Integer> yList = new ArrayList<Integer>();
	/**
	 * 用于存放站名的坐标
	 */
	private int stationX[],stationY[];
	/**
	 * 精确化列车的上下坡
	 */
	private ArrayList<Integer> upDownX, upDownY;
	private int newX[],newY[];
	/**
	 * 填充整个轨道
	 */
	private int newPa[],newPb[];
	/**
	 * 保存轨道前面的填充轨道的坐标
	 */
	private int rwx[] = new int[1],rwy[] = new int[1];
	
	/**
	 * 构造函数，初始化数据
	 * @param routeId
	 */
	public DrawSlope(int routeId){
		ds = new DataService();
		slopeList = ds.getSlopeData(routeId);
		stationInfoList = ds.getStationInfoData(routeId);
		pointsX = new ArrayList<Integer>();
		pointsY = new ArrayList<Integer>();
		infX = new ArrayList<Integer>();
		infY = new ArrayList<Integer>();
		slopesInfo = new ArrayList<String>();
		upDownX = new ArrayList<Integer>();
		upDownY = new ArrayList<Integer>();
		rwx[0] = 0;
		rwy[0] = 354;
		this.infoToPoints();
		this.arrayCopy();
		this.initStationLocation();
	}
	
	/**
	 * 将坡道信息，转化为一个一个坐标点
	 */
	public void infoToPoints(){
		//先放入起点坐标
		pointsX.add(x);
		pointsY.add(y);
		upDownX.add(x);
		upDownY.add(y);
		double infx = x,infy = y;
		for(int i=0;i<slopeList.size();i++){
			double px=0,py=0;//将要加入点的坐标
			Slope slopes = slopeList.get(i);
			double end = slopes.getEnd()*1000;//坡道末端
			double slope = slopes.getSlope();
			if(slope == 0){//说明是平地
				px = x + end * para;
//				px = x + end;
				py = pointsY.get(pointsY.size()-1);
			}else if(slope > 0){//上坡
				px = x + end * para;
//				px = x + end;
				py = pointsY.get(pointsY.size()-1) - Math.abs(slopes.getHeight()/3);
			}else if(slope < 0){//下坡
				px = x + end * para;
//				px = x + end;
				py = pointsY.get(pointsY.size()-1) + Math.abs(slopes.getHeight()/3);
			}
			//放入坐标
			pointsX.add((int)px);
			pointsY.add((int)py);
			infX.add((int)(px+infx)/2);
			infY.add((int)(py+infy)/2);
			this.insertIntoList((((px+infx)/2 + infx)/2 + infx)/2, (((py+infy)/2 + infy)/2 + infy)/2);
			this.insertIntoList(((px+infx)/2 + infx)/2, ((py+infy)/2 + infy)/2);
			this.insertIntoList((px+infx)/2, (py+infy)/2);
			this.insertIntoList(((px+infx)/2 + px)/2, ((py+infy)/2 + py)/2);
			this.insertIntoList((((px+infx)/2 + px)/2 + px)/2, (((py+infy)/2 + py)/2 + py)/2);
			this.insertIntoList(px, py);
			infx = px;
			infy = py;
			slopesInfo.add(slopes.getSlope()+","+slopes.getLength());
		}
		//转化为数组
		this.toArray();
	}
	
	/**
	 * 添加坐标
	 * @param x
	 * @param y
	 */
	public void insertIntoList(double x,double y){
		upDownX.add((int)x);
		upDownY.add((int)y);
	}
	
	/**
	 * 初始化站名的坐标
	 */
	public void initStationLocation(){
		stationX = new int[stationInfoList.size()];
		stationY = new int[stationInfoList.size()];
		int staY = y + 166;
		//给第一个车站放入坐标值
		stationX[0] = x;
		stationY[0] = staY;
		//给剩下的车站放入坐标值
		for(int i=1;i<stationInfoList.size();i++){
			stationX[i] = (int)(x + stationInfoList.get(i).getLocation() * 1000 * para);
			stationY[i] = staY;
		}
	}
	
	/**
	 * 画出站名
	 * @param g
	 */
	public void drawStationName(Graphics g){
		g.setColor(Color.BLACK);
		for(int i=0;i<stationInfoList.size();i++){
			StationInfo station = stationInfoList.get(i);
			g.drawString(station.getStationName(), stationX[i], stationY[i]);
		}
		/*//北京南
		g.drawString("北京南", stationX[0], stationY[0]);
		//亦庄
		g.drawString("亦庄", stationX[1], stationY[1]);
		//永乐
		g.drawString("永乐", stationX[2], stationY[2]);
		//武清
		g.drawString("武清", stationX[3], stationY[3]);
		//天津
		g.drawString("天津", stationX[4], stationY[4]);*/
	}
	
	/**
	 * 将List转化为数组
	 */
	public void toArray(){
		//点
		a = this.listToArray(pointsX);
		b = this.listToArray(pointsY);
		//数据
		rx = this.listToArray(infX);
		ry = this.listToArray(infY);
		//精确点
		newX = this.listToArray(upDownX);
		newY = this.listToArray(upDownY);
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
	 * 通过Slopes数据，画出所有坡道信息
	 * @param g
	 */
	public void drawSlope(Graphics g){
		this.fillSlope(g);
		this.drawStationName(g);
	}
	
	/**
	 * 将坡道画到RailWay上，并且填充
	 * @param g
	 */
	public void fillSlope(Graphics g){
		g.setColor(new Color(125,126,69));
		g.fillPolygon(newPa, newPb, newPa.length);
		//画轨道前面的轨道
		g.fillRect(rwx[0], rwy[0]+100, 400, 5553); //2014.10.15 223->100
	}
	
	/**
	 * 数组复制
	 */
	public void arrayCopy(){
		pa = (int [])a.clone();//横坐标的复制可以clone
		int t = 188;
		pb = new int[b.length];
		for(int i=0;i<b.length;i++){
			pb[i] = b[i] + t;
		}
		newPa = new int[a.length+3];
		newPb = new int[b.length+3];
		newPa[0] = pa[0];
		newPb[0] = pb[0] + 430;
		for(int i=0;i<pa.length;i++){
			newPa[i+1] = pa[i];
			newPb[i+1] = pb[i];
		}
		newPa[a.length+1] = newPa[a.length] + 18000;
		newPb[b.length+1] = newPb[b.length];
		
		newPa[a.length+2] = newPa[a.length +1];
		newPb[b.length+2] = newPb[b.length] + 50000;
	}
	
	/**
	 * 画坡道信息数据
	 * @param g
	 */
	public void drawSlopeInfo(Graphics g){
		for(int i=0;i<slopesInfo.size();i++){
			g.drawString(slopesInfo.get(i), rx[i], ry[i]);
		}
	}
	
	/**
	 * 设置列车运行的背景，即天空的颜色
	 * @param g
	 */
	public void drawSkyBackGround(Graphics g){
		g.setColor(new Color(125,229,228));
		g.fillRect(0, 350, 3200, 800); //2014.10.15 475->350
	}
	
	/**
	 * 使坡道动起来
	 */
	public void run(){
		this.setPoints(stationX, speed, 0);
		this.setPoints(newPa, speed,0);
		this.setPoints(newX, speed, 1);
		this.setPoints(rwx, speed, 0);
	}
	
	/**
	 * 返回yList的最后一个值作为Train的纵坐标
	 * @return
	 */
	public int getLastY(){
		return yList.get(yList.size()-1) - 12;//减去Train的高度
	}
	
	/**
	 * 设置各个点的x坐标值
	 * @param array
	 * @param speed
	 * @param index index为标志位,0为不记录,1为记录
	 */
	public void setPoints(int [] array,double speed,int index){
		for(int i=0;i<array.length;i++){
			array[i] -= (int)speed;
		}
		if(index == 1){
			for(int t=array.length-1;t>=0;t--){
				if(array[t] <= 370){
					yList.add(newY[t]+188);
					break;
				}
			}
		}
	}
	
}
