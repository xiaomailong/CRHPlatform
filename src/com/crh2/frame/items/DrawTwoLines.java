package com.crh2.frame.items;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;

import com.crh2.util.MyTools;

/**
 * ��̬�����ٶȺ�ʱ����
 * @author huhui
 *
 */
public class DrawTwoLines {
	/**
	 * �ٶ�
	 */
	public static double speed = 0;
	/**
	 * ʱ��
	 */
	public static double time = 0;
	
	/**
	 * ��̬�����ٶ��ߺ�ʱ����
	 * @param g
	 */
	public static void drawTwoLine(Graphics g){
		Graphics2D g2d = (Graphics2D) g;
		g2d.setStroke(new BasicStroke(2));
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);//�����
		g.setColor(Color.RED);//�ٶ���Ϊ��ɫ
		int x = 170, y = 190;//�����ʼ����  2014.10.15�޸� 250->190
		int length = 759;
		//�ٶȵ��������ת��
		Line2D speedLine = new Line2D.Double(x, y-(DrawTable.HIGH*speed/400), x+length, y-(DrawTable.HIGH*speed/400)); //DrawTable.HIGH��������߶�
		g2d.draw(speedLine);
		//ʱ�䵽�������ת��
		Line2D timeLine = new Line2D.Double(x, y-(DrawTable.HIGH*time/10), x+length, y-(DrawTable.HIGH*time/10));
		g2d.setColor(Color.BLACK);//ʱ����Ϊ��ɫ
		g2d.draw(timeLine);
	}
	
	/**
	 * ����speed��time
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
