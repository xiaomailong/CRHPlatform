package com.crh2.frame.items;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;

import com.crh2.util.MyTools;

/**
 * 动态画出速度和时间线
 * @author huhui
 *
 */
public class DrawTwoLines {
	/**
	 * 速度
	 */
	public static double speed = 0;
	/**
	 * 时间
	 */
	public static double time = 0;
	
	/**
	 * 动态画出速度线和时间线
	 * @param g
	 */
	public static void drawTwoLine(Graphics g){
		Graphics2D g2d = (Graphics2D) g;
		g2d.setStroke(new BasicStroke(2));
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);//抗锯齿
		g.setColor(Color.RED);//速度线为红色
		int x = 170, y = 190;//定义初始坐标  2014.10.15修改 250->190
		int length = 759;
		//速度到纵坐标的转化
		Line2D speedLine = new Line2D.Double(x, y-(DrawTable.HIGH*speed/400), x+length, y-(DrawTable.HIGH*speed/400)); //DrawTable.HIGH是纵坐标高度
		g2d.draw(speedLine);
		//时间到纵坐标的转化
		Line2D timeLine = new Line2D.Double(x, y-(DrawTable.HIGH*time/10), x+length, y-(DrawTable.HIGH*time/10));
		g2d.setColor(Color.BLACK);//时间线为黑色
		g2d.draw(timeLine);
	}
	
	/**
	 * 设置speed和time
	 * @param s
	 * @param t
	 */
	public static void setValues(double s, double t){
		speed = s;
		double i = MyTools.smallTime(t);
		if(i<=10){
			time = i;
		}else if(i>10 && i<=20){
			time = i - 10;
		}else if(i>20 && i<=30){
			time = i - 20;
		}else if(i>30){
			time = i - 30;
		}
		
	}
	
}
