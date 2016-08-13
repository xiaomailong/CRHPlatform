package com.crh2.frame.items;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import com.crh2.javabean.Curve;
import com.crh2.service.DataService;
import com.crh2.util.MyTools;

/**
 * ����������ڶ�̬���������ݿ��е������Ϣ���ȱ��������г����������
 * @author huhui
 *
 */
public class DrawCurve extends JPanel {
	/**
	 * ��ʼ������.y����Ϊ�ڶ��������������֮��
	 */
	private int x = 370, y = 198;
	private double speed = 0;
	private DataService ds = null;
	/**
	 * ��������Ϣ��list
	 */
	private List<Curve> curveList = null;
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
	private ArrayList<String> curveInfo;
	private int rx[],ry[];
	
	/**
	 * ���캯������ʼ������
	 * @param routeId
	 */
	public DrawCurve(int routeId){
		ds = new DataService();
		curveList = ds.getCurveData(routeId);//�õ�Curve��������
		pointsX = new ArrayList<Integer>();
		pointsY = new ArrayList<Integer>();
		infX = new ArrayList<Integer>();
		infY = new ArrayList<Integer>();
		curveInfo = new ArrayList<String>();
		this.infoToPoints();//��Ϣת��Ϊ�����
	}
	
	/**
	 * �������Ϣ��ת��Ϊһ��һ�������
	 */
	public void infoToPoints(){
		//�ȷ����������
		pointsX.add(x);
		pointsY.add(y+53); //2014.10.15  135->53
		for(int i=0;i<curveList.size();i++){
			Curve curve = curveList.get(i);
			//��һ����
			double px1 = x + curve.getStart()*1000*para;
//			double px1 = x + curve.getStart()*1000;
			double py1 = pointsY.get(pointsX.size()-1);
			//�������꼯��
			pointsX.add((int)px1);
			pointsY.add((int)py1);
			
			//�ڶ�����
			double px2 = 0,py2 = 0;
			if(curve.getRadius()>0){//�������0��������
				px2 = px1;
				py2 = py1 - curve.getRadius()/1000;
			}else if(curve.getRadius()<0){//���С��0��������
				px2 = px1;
				py2 = py1 + curve.getRadius()/1000;
			}
			//�������꼯��
			pointsX.add((int)px2);
			pointsY.add((int)py2);
			
			//��������
			double px3 = px2 + curve.getLength()*para;
//			double px3 = px2 + curve.getLength();
			double py3 = py2;
			//�������꼯��
			pointsX.add((int)px3);
			pointsY.add((int)py3);
			
			//�ڶ�����͵�������֮�䣬��Ҫд�����߰뾶
			infX.add((int)((px2 + px3)/2));
			infY.add((int)py2);
			curveInfo.add(curve.getRadius()+"");
			
			//���ĸ���
			double px4 = 0, py4 = 0;
			if(curve.getRadius()>0){//�������0��������
				px4 = px3;
				py4 = py3 + curve.getRadius()/1000;
			}else if(curve.getRadius()<0){//���С��0��������
				px4 = px3;
				py4 = py3 - curve.getRadius()/1000;
			}
			//�������꼯��
			pointsX.add((int)px4);
			pointsY.add((int)py4);
			
		}
		//���һ����
		double endX = x + 115.73071*1000*para;
//		double endX = x + 115.73071*1000;
		double endY = 251; //2014.10.15 333->251
		pointsX.add((int)endX);
		pointsY.add((int)endY);
		
		//�Ƿ�뾶��Ϣɾ�����һ����
		
		this.toArray();//ת��Ϊ����
	}
	
	/**
	 * ��Listת��Ϊ����
	 */
	public void toArray(){
		//��
		a = this.listToArray(pointsX);
		b = this.listToArray(pointsY);
		//�뾶
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
	 * ͨ��Curve���ݣ��������������Ϣ
	 * @param g
	 */
	public void drawCurve(Graphics g){
		g.drawPolyline(a, b, a.length);
		this.drawRadius(g);
	}
	
	/**
	 * �����뾶����
	 * @param g
	 */
	public void drawRadius(Graphics g){
		for(int i=0;i<curveInfo.size();i++){
			g.drawString(curveInfo.get(i), rx[i], ry[i]);
		}
	}
	
	/**
	 * ʹ���������
	 */
	public void run() {
		setPoints(a, speed);
		setPoints(rx, speed);
	}
	
	/**
	 * ���ø����������ֵ
	 * @param array
	 * @param speed
	 */
	public void setPoints(int [] array,double speed){
		for(int i=0;i<array.length;i++){
			array[i] -= (int)speed;
		}
	}
	
	
}
