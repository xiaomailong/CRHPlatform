package com.crh2.frame.items;

import java.awt.Color;
import java.awt.Graphics;

import com.crh2.util.MyTools;

/**
 * ������꣬ÿ��һ���ﻭһ�������
 * @author huhui
 *
 */
public class DrawLengthIndex {
	
	/**
	 * ����ϵ��(��λͳһ����)
	 */
	private double para = MyTools.lengthIndex;
	private int x = 370, y = 330;//��ʼ������. 2014.10.15 450->330
	private int pointX [] = new int[116];//һ����116�������
	private int pointY [] = new int[116];
	private double speed = 0;
	
	/**
	 * ���캯������ʼ������
	 */
	public DrawLengthIndex(){
		this.initData();
	}
	
	/**
	 * �������꣬�������������
	 * @param g
	 */
	public void drawLengthIndex(Graphics g){
		for(int i=0;i<pointX.length;i++){
			g.setColor(Color.GREEN);
			g.fillOval(pointX[i], pointY[i], 12, 12);
			g.setColor(Color.BLACK);
			g.drawString(i+"����", pointX[i], pointY[i]);
		}
	}
	
	/**
	 * �����������������ֵ
	 */
	public void initData(){
		//һ��Ҫ����116�������
		//�Ȱ�������
		pointX[0] = x;
		pointY[0] = y;
		for(int i=1;i<116;i++){
			pointX[i] = (int)(pointX[i-1] + 1006*para);
//			pointX[i] = (int)(pointX[i-1] + 1000);
			pointY[i] = y;
		}
	}
	
	/**
	 * ����speed
	 * @param speed
	 */
	public void setSpeed(double speed){
		this.speed = speed;
	}
	
	/**
	 * ʹ����궯����
	 */
	public void run(){
		for(int i=0;i<pointX.length;i++){
			pointX [i] -= speed;
		}
	}
	
}
