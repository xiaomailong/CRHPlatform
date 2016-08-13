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
 * ���ٶȺ�ʱ�����ߣ���������ģʽ��2014.10.16
 * @author huhui
 *
 */
public class DrawTwoLinesManual {
	/**
	 * �����������
	 */
	private int x = 370, y = 190;//�����������. 2014.10.15
	private int speedX [], speedY [];
	private int timeX [], timeY [];
	private double speed = 0;
	private ArrayList<Rundata> runDataList = null;
	/**
	 * �г��ܹ���ʻ��������
	 */
	private double sumLength = 116968.10649989508;
	private double factorX = 0;
	/**
	 * �̶�������ٶȺ����ʱ��
	 */
	private double maxSpeed = 400, maxTime = 35;
	
	/**
	 * ���캯������ʼ������
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
	 * �������꣬����������
	 * @param g
	 */
	public void drawSpeedAndTimeLines(Graphics g){
		Graphics2D g2d = (Graphics2D) g;
		g2d.setStroke(new BasicStroke(1));
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);//�����
		int size = speedX.length;
		//1.���ٶ�����
		g2d.setColor(Color.RED);
		g2d.drawPolyline(speedX, speedY, size);
		//2.��ʱ������
		g2d.setColor(Color.BLACK);
		g2d.drawPolyline(timeX, timeY, size);
	}
	
	/**
	 * ��������ٶȵ��ʱ��������ֵ
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
	 * ���ٶ�ֵת����y����ֵ 2014.10.16
	 * @param speed
	 * @return
	 */
	public double speedConvertor(double speed){
		return (DrawTable.HIGH * speed) / maxSpeed;
	}
	
	/**
	 * ��ʱ��ֵת����y����ֵ 2014.10.16
	 * @param time ��λ��s
	 * @return
	 */
	public double timeConvertor(double time){
		double t = MyTools.smallTime(time); //����ת�ɷ�
		return (DrawTable.HIGH * t) / maxTime;
	}
	
	/**
	 * ����speed
	 * @param speed
	 */
	public void setSpeed(double speed){
		this.speed = speed;
	}
	
	/**
	 * ʹ�������߶�����
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
