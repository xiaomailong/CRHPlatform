package com.crh2.frame.items;

import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import com.crh2.javabean.Slope;
import com.crh2.service.DataService;
import com.crh2.util.MyTools;

/**
 * 绘制纵断面
 * @author huhui
 *
 */
public class DrawSection {
	/**
	 * 初始化坐标
	 */
	private int x = 370, y = 90;//初始化坐标.y坐标为第一条线与第二条线之间  2014.10.15   166->90 即减少了76
	private double speed = 0;
	private DataService ds = null;
	/**
	 * 存放所有坡度信息
	 */
	private List<Slope> slopeList = null;
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
	/**
	 * 坡道信息坐标数组
	 */
	private int rx[],ry[];
	/**
	 * 画竖线的x坐标数组
	 */
	private int lineX[];
	
	
	/**
	 * 构造函数，初始化数据
	 * @param routeId
	 */
	public DrawSection(int routeId){
		ds = new DataService();
		slopeList = ds.getSlopeData(routeId);
		pointsX = new ArrayList<Integer>();
		pointsY = new ArrayList<Integer>();
		infX = new ArrayList<Integer>();
		infY = new ArrayList<Integer>();
		slopesInfo = new ArrayList<String>();
		this.infoToPoints();
		this.setLineX();//设置竖线的横坐标
	}
	
	/**
	 * 将坡道信息，转化为一个一个坐标点
	 */
	public void infoToPoints(){
		//先放入起点坐标
		pointsX.add(x);
		pointsY.add(y+120);
		double infx = x,infy = y;//记录先前一个点的坐标，为记录坡道信息做准备
		for(int i=0;i<slopeList.size();i++){
			int t = i + 1;//探测下一个坡道是什么
			double px=0,py=0;//将要加入点的坐标
			Slope slopes = slopeList.get(i);
			double end = slopes.getEnd()*1000;//坡道末端
			double slope = slopes.getSlope();
			if(slope == 0){//说明是平地
				px = x + end * para;
//				px = x + end;
				py = y ;
				this.insertIntoList(px, py);
				this.checkNextSlope(t, px, py);
			}else if(slope > 0){//上坡
				px = x + end * para;
//				px = x + end;
				py = y - 16;
				this.insertIntoList(px, py);
				this.checkNextSlope(t, px, py);
			}else if(slope < 0){//下坡
				px = x + end * para;
//				px = x + end;
				py = y + 16;
				this.insertIntoList(px, py);
				this.checkNextSlope(t, px, py);
			}
			
			//设置坡道坐标
			this.insertIntoInfoList((px+infx)/2, (py+infy)/2);
//			System.out.println("px="+px+"		infx="+infx+"		(px+infx)/2="+(px+infx)/2+"		(py+infy)/2="+(py+infy)/2);
			//重新给infx和infy赋值,使之每次都等于前一个坐标
			infx = px;
			infy = py;
			//放入坡道信息
			slopesInfo.add(slopes.getSlope()+", "+slopes.getLength());
		}
		//转化为数组
		this.toArray();
	}
	
	/**
	 * 探测下一个坡道是上坡还是下坡还是直线
	 * @param index
	 * @param x
	 * @param y
	 */
	public void checkNextSlope(int index,double x,double y){
		if(index >= slopeList.size()){
			this.insertIntoList(x, 166);
		}else{
			double slope = slopeList.get(index).getSlope();
			if(slope == 0){//直线
				this.insertIntoList(x, 90); //2014.10.15  166->90
			}else if(slope > 0){//上坡
				this.insertIntoList(x, 108); //2014.10.15  182->108
			}else if(slope < 0){//下坡
				this.insertIntoList(x, 73); //2014.10.15  150->72
			}
		}
	}
	
	/**
	 * 放入坡道信息坐标
	 * @param x
	 * @param y
	 */
	public void insertIntoInfoList(double x,double y){
		infX.add((int)x);
		infY.add((int)y+120);
	}
	
	/**
	 * 放入坡道坐标
	 * @param x
	 * @param y
	 */
	public void insertIntoList(double x,double y){
		pointsX.add((int)x);
		pointsY.add((int)y+120);
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
		g.drawPolyline(a, b, a.length);
		this.drawSlopeInfo(g);
		this.drawLine(g);
	}
	
	/**
	 * 画竖线
	 * @param g
	 */
	public void drawLine(Graphics g){
		for(int i=0;i<lineX.length;i++){
			g.drawLine(lineX[i], 190, lineX[i], 229); //2014.10.15  252->190  305->229
		}
	}
	
	/**
	 * 画坡道信息数据
	 * @param g
	 */
	public void drawSlopeInfo(Graphics g){
		Font font = new Font("宋体", Font.BOLD, 14);
		g.setFont(font);
		for(int i=0;i<slopesInfo.size();i++){
			g.drawString(slopesInfo.get(i), rx[i], ry[i]);
		}
	}
	
	/**
	 * 使坡道动起来
	 */
	public void run(){
		this.setPoints(a, speed);
		this.setPoints(rx, speed);
		this.setPoints(lineX, speed);
	}
	
	/**
	 * 设置画竖线的横坐标
	 */
	public void setLineX(){
		lineX = (int [])a.clone();
	}
	
	/**
	 * 设置各个点的x坐标值
	 * @param array
	 * @param speed
	 */
	public void setPoints(int [] array,double speed){//index为标志位,0为不记录,1为记录
		for(int i=0;i<array.length;i++){
			array[i] -= (int)speed;
		}
	}
	
}
