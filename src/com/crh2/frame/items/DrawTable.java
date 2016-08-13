package com.crh2.frame.items;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

import javax.swing.JPanel;

import com.crh.view.panel.SpecifiedTimeEnergySimulationPanel;

/**
 * ���𻭱��,��Щ���ھ�̬��
 * @author huhui
 *
 */
public class DrawTable extends JPanel {
	
	/**
	 * ����һ���̶���x����
	 */
	private int X = 35;
	/**
	 * ����һ���̶���y����
	 */
	private int Y = 10;
	/**
	 * �����ٶ��ߺ�ʱ���ߵĿ̶��ܸ߶�
	 */
	public static int HIGH = 180;
	
	public DrawTable(){
		this.setBackground(Color.WHITE);
	}
	
	/**
	 * ��һЩ��̬�ı��
	 * @param g
	 * @param type 0��ʾ�������У�1��ʾ��������
	 */
	public void drawTable(Graphics g, int type){
		Graphics2D g2d = (Graphics2D) g;
		g.setColor(new Color(238,238,238));
		g.fillRect(X-35, Y+180, 97, 162); //2014.10.15 240->180   225->162
		g2d.setFont(new Font("TimesRoman", Font.BOLD, 16));
		this.drawSpeedRuling(g2d);//�ٶȿ̶�
		this.drawTimeRuling(g2d, type);//ʱ��̶�
		this.drawTableRows(g2d);
	}
	
	/**
	 * �����б��
	 * @param g2d
	 */
	public void drawTableRows(Graphics2D g2d){
		int x = X-35, y = Y+180;//������ʼλ�á�2014.10.15�޸� 240->180������С60
		g2d.setColor(Color.BLACK);
		g2d.setStroke(new BasicStroke(2));//�Ӵ�
		int m = 40; // �м�ࡣ2014.10.15�޸� 56->40������С16
		int n = 25; // ����������ƫ��    20->15
		//����һ����
		g2d.drawLine(x, y, x+3200, y);
		//��"�ݶ���"
		g2d.drawString("�ݶ���", x+15, y+n);
		//���ڶ�����
		g2d.drawLine(x, y+m, x+3200, y+m);
		//��"����"
		g2d.drawString("��    ��", x+15, y+n+m);
		//����������
		g2d.drawLine(x, y+2*m, x+3200, y+2*m);
		//��"������"
		g2d.drawString("������", x+15, y+n+2*m);
		//��"������"����е�ֱ��
		Line2D bridgeLine = new Line2D.Double(x+35+59, y+2.5*m, x+3200, y+2.5*m);
		g2d.setStroke(new BasicStroke(1));
		g2d.draw(bridgeLine);
		g2d.setStroke(new BasicStroke(2));
		//�����ĸ���
		g2d.drawLine(x, y+3*m, x+3200, y+3*m);
		//��"���"
		g2d.drawString("��    ��", x+15, y+n+3*m);
		//���������
		g2d.drawLine(x, y+4*m, x+3200, y+4*m);
		//������
		g2d.drawLine(X+60, y, X+60, y+4*m);
		
	}
	
	/**
	 * ��ʱ��̶���
	 * @param g2d
	 * @param type
	 */
	public void drawTimeRuling(Graphics2D g2d, int type){
		int x = X+130, y = Y;//������ʼλ��
		g2d.setStroke(new BasicStroke(2));//�Ӵ�
		g2d.setColor(Color.BLACK);//�̶���Ϊ��ɫ
		g2d.drawString("ʱ��(��)", x+15, y+5);
		int lineX = x+2;
		g2d.drawLine(lineX, y, lineX, y+HIGH); //2014.10.15�޸� 240->180������С60
		//���̶�
		int n = 3;
		g2d.drawLine(lineX-n, y, lineX, y);
		g2d.drawLine(lineX-n, y+36, lineX, y+36);
		g2d.drawLine(lineX-n, y+72, lineX, y+72);
		g2d.drawLine(lineX-n, y+108, lineX, y+108);
		g2d.drawLine(lineX-n, y+144, lineX, y+144);
		g2d.drawLine(lineX-n, y+180, lineX, y+180);
		//������
		int m = 13;
		int p = 4;
		if(type == SpecifiedTimeEnergySimulationPanel.MANUAL_MODEL){
			int factor = 2; //2014.10.16 ��λ�����ʽ�ƫ�Ƶ���
			g2d.drawString("35", lineX-m*factor, y+p);
			g2d.drawString("28", lineX-m*factor, y+36+p);
			g2d.drawString("21", lineX-m*factor, y+72+p);
			g2d.drawString("14", lineX-m*factor, y+108+p);
			g2d.drawString("7", lineX-m*factor, y+144+p);
		}else if(type == SpecifiedTimeEnergySimulationPanel.AUTO_MODEL){
			g2d.drawString("10", lineX-m, y+p);
			g2d.drawString("8", lineX-m, y+36+p);
			g2d.drawString("6", lineX-m, y+72+p);
			g2d.drawString("4", lineX-m, y+108+p);
			g2d.drawString("2", lineX-m, y+144+p);
		}
	}
	
	
	/**
	 * ���ٶȿ̶���
	 * @param g2d
	 */
	public void drawSpeedRuling(Graphics2D g2d){
		int lineX = X + 20;
		g2d.setStroke(new BasicStroke(2));//�Ӵ�
		g2d.setColor(Color.RED);//�̶���Ϊ��ɫ
		g2d.drawLine(lineX, Y, lineX, Y+HIGH);//2014.10.15�޸� 240->180������С60
		g2d.drawString("�ٶ�(km/h)", lineX, Y+5);
		//���̶�
		int n = 5;
		g2d.drawLine(lineX-n, Y, lineX, Y);
		g2d.drawLine(lineX-n, Y+45, lineX, Y+45);
		g2d.drawLine(lineX-n, Y+90, lineX, Y+90);
		g2d.drawLine(lineX-n, Y+135, lineX, Y+135);
		g2d.drawLine(lineX-n, Y+180, lineX, Y+180);
		//������
		int m = 20;//ƫ��
		int p = 4+5;//11.13�޸�   ��������5������
		g2d.drawString("400", X-m, Y+p);
		g2d.drawString("300", X-m, Y+45+p);
		g2d.drawString("200", X-m, Y+90+p);
		g2d.drawString("100", X-m, Y+135+p);
	}
	
}
