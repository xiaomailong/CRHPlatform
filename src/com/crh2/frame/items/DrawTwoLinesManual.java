package com.crh2.frame.items;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;

import com.crh2.javabean.Rundata;
import com.crh2.util.MyTools;

/**
 * 画速度和时间曲线（快速运行模式）2014.10.16
 * @author huhui
 *
 */
public class DrawTwoLinesManual {
	/**
	 * 曲线起点坐标
	 */
	private int x = 370, y = 190;//曲线起点坐标. 2014.10.15
	private int speedX [], speedY [];
	private int timeX [], timeY [];
	private double speed = 0;
	private ArrayList<Rundata> runDataList = null;
	/**
	 * 列车总共行驶的像素数
	 */
	private double sumLength = 116968.10649989508;
	private double factorX = 0;
	/**
	 * 刻度上最大速度和最大时间
	 */
	private double maxSpeed = 400, maxTime = 35;
	
	/**
	 * 构造函数，初始化数据
	 * @param runDataList
	 */
	public DrawTwoLinesManual(ArrayList<Rundata> runDataList){
		int size = runDataList.size();
		this.factorX = sumLength/size;
		this.runDataList = runDataList;
		speedX = new int [size];
		speedY = new int [size];
		timeX = new int [size];
		timeY = new int [size];
		this.initData();
	}
	
	/**
	 * 根据坐标，画出各个点
	 * @param g
	 */
	public void drawSpeedAndTimeLines(Graphics g){
		Graphics2D g2d = (Graphics2D) g;
		g2d.setStroke(new BasicStroke(1));
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);//抗锯齿
		int size = speedX.length;
		//1.画速度曲线
		g2d.setColor(Color.RED);
		g2d.drawPolyline(speedX, speedY, size);
		//2.画时间曲线
		g2d.setColor(Color.BLACK);
		g2d.drawPolyline(timeX, timeY, size);
	}
	
	/**
	 * 计算各个速度点和时间点的坐标值
	 */
	public void initData(){
		for(int i=0; i<runDataList.size(); i++){
			Rundata bean = runDataList.get(i);
			speedX[i] = (int) (x + i * factorX);
			speedY[i] = (int) (y - speedConvertor(bean.getSpeed()));
			timeX[i] = (int) (x + i * factorX);
			timeY[i] = (int) (y - timeConvertor(bean.getRuntime()));
		}
	}
	
	/**
	 * 将速度值转换成y坐标值 2014.10.16
	 * @param speed
	 * @return
	 */
	public double speedConvertor(double speed){
		return (DrawTable.HIGH * speed) / maxSpeed;
	}
	
	/**
	 * 将时间值转换成y坐标值 2014.10.16
	 * @param time 单位是s
	 * @return
	 */
	public double timeConvertor(double time){
		double t = MyTools.smallTime(time); //将秒转成分
		return (DrawTable.HIGH * t) / maxTime;
	}
	
	/**
	 * 设置speed
	 * @param speed
	 */
	public void setSpeed(double speed){
		this.speed = speed;
	}
	
	/**
	 * 使两个曲线动起来
	 */
	public void run(){
		for(int i=0;i<speedX.length;i++){
			speedX [i] -= speed;
		}
		for(int j=0;j<timeX.length;j++){
			timeX [j] -= speed;
		}
	}
	
}
