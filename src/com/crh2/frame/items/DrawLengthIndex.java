package com.crh2.frame.items;

import java.awt.Color;
import java.awt.Graphics;

import com.crh2.util.MyTools;

/**
 * 画公里标，每隔一公里画一个公里标
 * @author huhui
 *
 */
public class DrawLengthIndex {
	
	/**
	 * 比例系数(单位统一用米)
	 */
	private double para = MyTools.lengthIndex;
	private int x = 370, y = 330;//初始化坐标. 2014.10.15 450->330
	private int pointX [] = new int[116];//一共有116个公里标
	private int pointY [] = new int[116];
	private double speed = 0;
	
	/**
	 * 构造函数，初始化数据
	 */
	public DrawLengthIndex(){
		this.initData();
	}
	
	/**
	 * 根据坐标，画出各个公里标
	 * @param g
	 */
	public void drawLengthIndex(Graphics g){
		for(int i=0;i<pointX.length;i++){
			g.setColor(Color.GREEN);
			g.fillOval(pointX[i], pointY[i], 12, 12);
			g.setColor(Color.BLACK);
			g.drawString(i+"公里", pointX[i], pointY[i]);
		}
	}
	
	/**
	 * 计算各个公里标的坐标值
	 */
	public void initData(){
		//一共要设置116个公里标
		//先把起点放入
		pointX[0] = x;
		pointY[0] = y;
		for(int i=1;i<116;i++){
			pointX[i] = (int)(pointX[i-1] + 1006*para);
//			pointX[i] = (int)(pointX[i-1] + 1000);
			pointY[i] = y;
		}
	}
	
	/**
	 * 设置speed
	 * @param speed
	 */
	public void setSpeed(double speed){
		this.speed = speed;
	}
	
	/**
	 * 使公里标动起来
	 */
	public void run(){
		for(int i=0;i<pointX.length;i++){
			pointX [i] -= speed;
		}
	}
	
}
