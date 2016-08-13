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
 * 负责画表格,这些属于静态的
 * @author huhui
 *
 */
public class DrawTable extends JPanel {
	
	/**
	 * 定义一个固定的x坐标
	 */
	private int X = 35;
	/**
	 * 定义一个固定的y坐标
	 */
	private int Y = 10;
	/**
	 * 定义速度线和时间线的刻度总高度
	 */
	public static int HIGH = 180;
	
	public DrawTable(){
		this.setBackground(Color.WHITE);
	}
	
	/**
	 * 画一些静态的表格
	 * @param g
	 * @param type 0表示快速运行，1表示动画运行
	 */
	public void drawTable(Graphics g, int type){
		Graphics2D g2d = (Graphics2D) g;
		g.setColor(new Color(238,238,238));
		g.fillRect(X-35, Y+180, 97, 162); //2014.10.15 240->180   225->162
		g2d.setFont(new Font("TimesRoman", Font.BOLD, 16));
		this.drawSpeedRuling(g2d);//速度刻度
		this.drawTimeRuling(g2d, type);//时间刻度
		this.drawTableRows(g2d);
	}
	
	/**
	 * 画四行表格
	 * @param g2d
	 */
	public void drawTableRows(Graphics2D g2d){
		int x = X-35, y = Y+180;//定义起始位置。2014.10.15修改 240->180，即减小60
		g2d.setColor(Color.BLACK);
		g2d.setStroke(new BasicStroke(2));//加粗
		int m = 40; // 行间距。2014.10.15修改 56->40，即减小16
		int n = 25; // 文字纵坐标偏移    20->15
		//画第一根线
		g2d.drawLine(x, y, x+3200, y);
		//画"纵断面"
		g2d.drawString("纵断面", x+15, y+n);
		//画第二根线
		g2d.drawLine(x, y+m, x+3200, y+m);
		//画"曲线"
		g2d.drawString("曲    线", x+15, y+n+m);
		//画第三根线
		g2d.drawLine(x, y+2*m, x+3200, y+2*m);
		//画"道桥隧"
		g2d.drawString("道桥隧", x+15, y+n+2*m);
		//画"道桥隧"表格中的直线
		Line2D bridgeLine = new Line2D.Double(x+35+59, y+2.5*m, x+3200, y+2.5*m);
		g2d.setStroke(new BasicStroke(1));
		g2d.draw(bridgeLine);
		g2d.setStroke(new BasicStroke(2));
		//画第四根线
		g2d.drawLine(x, y+3*m, x+3200, y+3*m);
		//画"里程"
		g2d.drawString("里    程", x+15, y+n+3*m);
		//画第五根线
		g2d.drawLine(x, y+4*m, x+3200, y+4*m);
		//画竖线
		g2d.drawLine(X+60, y, X+60, y+4*m);
		
	}
	
	/**
	 * 画时间刻度线
	 * @param g2d
	 * @param type
	 */
	public void drawTimeRuling(Graphics2D g2d, int type){
		int x = X+130, y = Y;//定义起始位置
		g2d.setStroke(new BasicStroke(2));//加粗
		g2d.setColor(Color.BLACK);//刻度线为黑色
		g2d.drawString("时间(分)", x+15, y+5);
		int lineX = x+2;
		g2d.drawLine(lineX, y, lineX, y+HIGH); //2014.10.15修改 240->180，即减小60
		//画刻度
		int n = 3;
		g2d.drawLine(lineX-n, y, lineX, y);
		g2d.drawLine(lineX-n, y+36, lineX, y+36);
		g2d.drawLine(lineX-n, y+72, lineX, y+72);
		g2d.drawLine(lineX-n, y+108, lineX, y+108);
		g2d.drawLine(lineX-n, y+144, lineX, y+144);
		g2d.drawLine(lineX-n, y+180, lineX, y+180);
		//画数字
		int m = 13;
		int p = 4;
		if(type == SpecifiedTimeEnergySimulationPanel.MANUAL_MODEL){
			int factor = 2; //2014.10.16 两位数，故将偏移调大
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
	 * 画速度刻度线
	 * @param g2d
	 */
	public void drawSpeedRuling(Graphics2D g2d){
		int lineX = X + 20;
		g2d.setStroke(new BasicStroke(2));//加粗
		g2d.setColor(Color.RED);//刻度线为红色
		g2d.drawLine(lineX, Y, lineX, Y+HIGH);//2014.10.15修改 240->180，即减小60
		g2d.drawString("速度(km/h)", lineX, Y+5);
		//画刻度
		int n = 5;
		g2d.drawLine(lineX-n, Y, lineX, Y);
		g2d.drawLine(lineX-n, Y+45, lineX, Y+45);
		g2d.drawLine(lineX-n, Y+90, lineX, Y+90);
		g2d.drawLine(lineX-n, Y+135, lineX, Y+135);
		g2d.drawLine(lineX-n, Y+180, lineX, Y+180);
		//画数字
		int m = 20;//偏移
		int p = 4+5;//11.13修改   数字下移5个像素
		g2d.drawString("400", X-m, Y+p);
		g2d.drawString("300", X-m, Y+45+p);
		g2d.drawString("200", X-m, Y+90+p);
		g2d.drawString("100", X-m, Y+135+p);
	}
	
}
