package com.crh2.frame.items;

import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import com.crh2.javabean.Slope;
import com.crh2.service.DataService;
import com.crh2.util.MyTools;

/**
 * �����ݶ���
 * @author huhui
 *
 */
public class DrawSection {
	/**
	 * ��ʼ������
	 */
	private int x = 370, y = 90;//��ʼ������.y����Ϊ��һ������ڶ�����֮��  2014.10.15   166->90 ��������76
	private double speed = 0;
	private DataService ds = null;
	/**
	 * ��������¶���Ϣ
	 */
	private List<Slope> slopeList = null;
	/**
	 * �������е��x,y���꼯��
	 */
	private ArrayList<Integer> pointsX, pointsY;
	/**
	 * ����ɼ���ת������
	 */
	private int a[],b[];
	/**
	 * ����ϵ��(��λͳһ����)
	 */
	private double para = MyTools.lengthIndex;
	/**
	 * ������Ҫд�����Ϣ��x,y���꼯��
	 */
	private ArrayList<Integer> infX, infY;
	private ArrayList<String> slopesInfo;
	/**
	 * �µ���Ϣ��������
	 */
	private int rx[],ry[];
	/**
	 * �����ߵ�x��������
	 */
	private int lineX[];
	
	
	/**
	 * ���캯������ʼ������
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
		this.setLineX();//�������ߵĺ�����
	}
	
	/**
	 * ���µ���Ϣ��ת��Ϊһ��һ�������
	 */
	public void infoToPoints(){
		//�ȷ����������
		pointsX.add(x);
		pointsY.add(y+120);
		double infx = x,infy = y;//��¼��ǰһ��������꣬Ϊ��¼�µ���Ϣ��׼��
		for(int i=0;i<slopeList.size();i++){
			int t = i + 1;//̽����һ���µ���ʲô
			double px=0,py=0;//��Ҫ����������
			Slope slopes = slopeList.get(i);
			double end = slopes.getEnd()*1000;//�µ�ĩ��
			double slope = slopes.getSlope();
			if(slope == 0){//˵����ƽ��
				px = x + end * para;
//				px = x + end;
				py = y ;
				this.insertIntoList(px, py);
				this.checkNextSlope(t, px, py);
			}else if(slope > 0){//����
				px = x + end * para;
//				px = x + end;
				py = y - 16;
				this.insertIntoList(px, py);
				this.checkNextSlope(t, px, py);
			}else if(slope < 0){//����
				px = x + end * para;
//				px = x + end;
				py = y + 16;
				this.insertIntoList(px, py);
				this.checkNextSlope(t, px, py);
			}
			
			//�����µ�����
			this.insertIntoInfoList((px+infx)/2, (py+infy)/2);
//			System.out.println("px="+px+"		infx="+infx+"		(px+infx)/2="+(px+infx)/2+"		(py+infy)/2="+(py+infy)/2);
			//���¸�infx��infy��ֵ,ʹ֮ÿ�ζ�����ǰһ������
			infx = px;
			infy = py;
			//�����µ���Ϣ
			slopesInfo.add(slopes.getSlope()+", "+slopes.getLength());
		}
		//ת��Ϊ����
		this.toArray();
	}
	
	/**
	 * ̽����һ���µ������»������»���ֱ��
	 * @param index
	 * @param x
	 * @param y
	 */
	public void checkNextSlope(int index,double x,double y){
		if(index >= slopeList.size()){
			this.insertIntoList(x, 166);
		}else{
			double slope = slopeList.get(index).getSlope();
			if(slope == 0){//ֱ��
				this.insertIntoList(x, 90); //2014.10.15  166->90
			}else if(slope > 0){//����
				this.insertIntoList(x, 108); //2014.10.15  182->108
			}else if(slope < 0){//����
				this.insertIntoList(x, 73); //2014.10.15  150->72
			}
		}
	}
	
	/**
	 * �����µ���Ϣ����
	 * @param x
	 * @param y
	 */
	public void insertIntoInfoList(double x,double y){
		infX.add((int)x);
		infY.add((int)y+120);
	}
	
	/**
	 * �����µ�����
	 * @param x
	 * @param y
	 */
	public void insertIntoList(double x,double y){
		pointsX.add((int)x);
		pointsY.add((int)y+120);
	}
	
	/**
	 * ��Listת��Ϊ����
	 */
	public void toArray(){
		//��
		a = this.listToArray(pointsX);
		b = this.listToArray(pointsY);
		//����
		rx = this.listToArray(infX);
		ry = this.listToArray(infY);
	}
	
	/**
	 * ��Integer����ת��Ϊint����
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
	 * �����ٶ�
	 * @param speed
	 */
	public void setSpeed(double speed){
		this.speed = speed;
	}
	
	/**
	 * ͨ��Slopes���ݣ����������µ���Ϣ
	 * @param g
	 */
	public void drawSlope(Graphics g){
		g.drawPolyline(a, b, a.length);
		this.drawSlopeInfo(g);
		this.drawLine(g);
	}
	
	/**
	 * ������
	 * @param g
	 */
	public void drawLine(Graphics g){
		for(int i=0;i<lineX.length;i++){
			g.drawLine(lineX[i], 190, lineX[i], 229); //2014.10.15  252->190  305->229
		}
	}
	
	/**
	 * ���µ���Ϣ����
	 * @param g
	 */
	public void drawSlopeInfo(Graphics g){
		Font font = new Font("����", Font.BOLD, 14);
		g.setFont(font);
		for(int i=0;i<slopesInfo.size();i++){
			g.drawString(slopesInfo.get(i), rx[i], ry[i]);
		}
	}
	
	/**
	 * ʹ�µ�������
	 */
	public void run(){
		this.setPoints(a, speed);
		this.setPoints(rx, speed);
		this.setPoints(lineX, speed);
	}
	
	/**
	 * ���û����ߵĺ�����
	 */
	public void setLineX(){
		lineX = (int [])a.clone();
	}
	
	/**
	 * ���ø������x����ֵ
	 * @param array
	 * @param speed
	 */
	public void setPoints(int [] array,double speed){//indexΪ��־λ,0Ϊ����¼,1Ϊ��¼
		for(int i=0;i<array.length;i++){
			array[i] -= (int)speed;
		}
	}
	
}
