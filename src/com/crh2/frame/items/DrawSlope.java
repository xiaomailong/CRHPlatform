package com.crh2.frame.items;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import com.crh2.javabean.Slope;
import com.crh2.javabean.StationInfo;
import com.crh2.service.DataService;
import com.crh2.util.MyTools;

/**
 * �����¶�,ֻ�����г�ʵ�����е��¶ȣ�������
 * @author huhui
 *
 */
public class DrawSlope {
	/**
	 * ��ʼ������
	 */
	private int x = 370, y = 266;//��ʼ������.y����Ϊ��һ������ڶ�����֮��    2014.10.15 389->266
	private double speed = 0;
	private DataService ds = null;
	/**
	 * ��������¶���Ϣ
	 */
	private List<Slope> slopeList = null;
	/**
	 * ������г�վ��Ϣ
	 */
	private List<StationInfo> stationInfoList = null;
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
	private int rx[],ry[];
	private int pa[],pb[];
	/**
	 * ����һ��ArrayList,������x����С��370���µ����y����
	 */
	private ArrayList<Integer> yList = new ArrayList<Integer>();
	/**
	 * ���ڴ��վ��������
	 */
	private int stationX[],stationY[];
	/**
	 * ��ȷ���г���������
	 */
	private ArrayList<Integer> upDownX, upDownY;
	private int newX[],newY[];
	/**
	 * ����������
	 */
	private int newPa[],newPb[];
	/**
	 * ������ǰ��������������
	 */
	private int rwx[] = new int[1],rwy[] = new int[1];
	
	/**
	 * ���캯������ʼ������
	 * @param routeId
	 */
	public DrawSlope(int routeId){
		ds = new DataService();
		slopeList = ds.getSlopeData(routeId);
		stationInfoList = ds.getStationInfoData(routeId);
		pointsX = new ArrayList<Integer>();
		pointsY = new ArrayList<Integer>();
		infX = new ArrayList<Integer>();
		infY = new ArrayList<Integer>();
		slopesInfo = new ArrayList<String>();
		upDownX = new ArrayList<Integer>();
		upDownY = new ArrayList<Integer>();
		rwx[0] = 0;
		rwy[0] = 354;
		this.infoToPoints();
		this.arrayCopy();
		this.initStationLocation();
	}
	
	/**
	 * ���µ���Ϣ��ת��Ϊһ��һ�������
	 */
	public void infoToPoints(){
		//�ȷ����������
		pointsX.add(x);
		pointsY.add(y);
		upDownX.add(x);
		upDownY.add(y);
		double infx = x,infy = y;
		for(int i=0;i<slopeList.size();i++){
			double px=0,py=0;//��Ҫ����������
			Slope slopes = slopeList.get(i);
			double end = slopes.getEnd()*1000;//�µ�ĩ��
			double slope = slopes.getSlope();
			if(slope == 0){//˵����ƽ��
				px = x + end * para;
//				px = x + end;
				py = pointsY.get(pointsY.size()-1);
			}else if(slope > 0){//����
				px = x + end * para;
//				px = x + end;
				py = pointsY.get(pointsY.size()-1) - Math.abs(slopes.getHeight()/3);
			}else if(slope < 0){//����
				px = x + end * para;
//				px = x + end;
				py = pointsY.get(pointsY.size()-1) + Math.abs(slopes.getHeight()/3);
			}
			//��������
			pointsX.add((int)px);
			pointsY.add((int)py);
			infX.add((int)(px+infx)/2);
			infY.add((int)(py+infy)/2);
			this.insertIntoList((((px+infx)/2 + infx)/2 + infx)/2, (((py+infy)/2 + infy)/2 + infy)/2);
			this.insertIntoList(((px+infx)/2 + infx)/2, ((py+infy)/2 + infy)/2);
			this.insertIntoList((px+infx)/2, (py+infy)/2);
			this.insertIntoList(((px+infx)/2 + px)/2, ((py+infy)/2 + py)/2);
			this.insertIntoList((((px+infx)/2 + px)/2 + px)/2, (((py+infy)/2 + py)/2 + py)/2);
			this.insertIntoList(px, py);
			infx = px;
			infy = py;
			slopesInfo.add(slopes.getSlope()+","+slopes.getLength());
		}
		//ת��Ϊ����
		this.toArray();
	}
	
	/**
	 * �������
	 * @param x
	 * @param y
	 */
	public void insertIntoList(double x,double y){
		upDownX.add((int)x);
		upDownY.add((int)y);
	}
	
	/**
	 * ��ʼ��վ��������
	 */
	public void initStationLocation(){
		stationX = new int[stationInfoList.size()];
		stationY = new int[stationInfoList.size()];
		int staY = y + 166;
		//����һ����վ��������ֵ
		stationX[0] = x;
		stationY[0] = staY;
		//��ʣ�µĳ�վ��������ֵ
		for(int i=1;i<stationInfoList.size();i++){
			stationX[i] = (int)(x + stationInfoList.get(i).getLocation() * 1000 * para);
			stationY[i] = staY;
		}
	}
	
	/**
	 * ����վ��
	 * @param g
	 */
	public void drawStationName(Graphics g){
		g.setColor(Color.BLACK);
		for(int i=0;i<stationInfoList.size();i++){
			StationInfo station = stationInfoList.get(i);
			g.drawString(station.getStationName(), stationX[i], stationY[i]);
		}
		/*//������
		g.drawString("������", stationX[0], stationY[0]);
		//��ׯ
		g.drawString("��ׯ", stationX[1], stationY[1]);
		//����
		g.drawString("����", stationX[2], stationY[2]);
		//����
		g.drawString("����", stationX[3], stationY[3]);
		//���
		g.drawString("���", stationX[4], stationY[4]);*/
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
		//��ȷ��
		newX = this.listToArray(upDownX);
		newY = this.listToArray(upDownY);
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
		this.fillSlope(g);
		this.drawStationName(g);
	}
	
	/**
	 * ���µ�����RailWay�ϣ��������
	 * @param g
	 */
	public void fillSlope(Graphics g){
		g.setColor(new Color(125,126,69));
		g.fillPolygon(newPa, newPb, newPa.length);
		//�����ǰ��Ĺ��
		g.fillRect(rwx[0], rwy[0]+100, 400, 5553); //2014.10.15 223->100
	}
	
	/**
	 * ���鸴��
	 */
	public void arrayCopy(){
		pa = (int [])a.clone();//������ĸ��ƿ���clone
		int t = 188;
		pb = new int[b.length];
		for(int i=0;i<b.length;i++){
			pb[i] = b[i] + t;
		}
		newPa = new int[a.length+3];
		newPb = new int[b.length+3];
		newPa[0] = pa[0];
		newPb[0] = pb[0] + 430;
		for(int i=0;i<pa.length;i++){
			newPa[i+1] = pa[i];
			newPb[i+1] = pb[i];
		}
		newPa[a.length+1] = newPa[a.length] + 18000;
		newPb[b.length+1] = newPb[b.length];
		
		newPa[a.length+2] = newPa[a.length +1];
		newPb[b.length+2] = newPb[b.length] + 50000;
	}
	
	/**
	 * ���µ���Ϣ����
	 * @param g
	 */
	public void drawSlopeInfo(Graphics g){
		for(int i=0;i<slopesInfo.size();i++){
			g.drawString(slopesInfo.get(i), rx[i], ry[i]);
		}
	}
	
	/**
	 * �����г����еı���������յ���ɫ
	 * @param g
	 */
	public void drawSkyBackGround(Graphics g){
		g.setColor(new Color(125,229,228));
		g.fillRect(0, 350, 3200, 800); //2014.10.15 475->350
	}
	
	/**
	 * ʹ�µ�������
	 */
	public void run(){
		this.setPoints(stationX, speed, 0);
		this.setPoints(newPa, speed,0);
		this.setPoints(newX, speed, 1);
		this.setPoints(rwx, speed, 0);
	}
	
	/**
	 * ����yList�����һ��ֵ��ΪTrain��������
	 * @return
	 */
	public int getLastY(){
		return yList.get(yList.size()-1) - 12;//��ȥTrain�ĸ߶�
	}
	
	/**
	 * ���ø������x����ֵ
	 * @param array
	 * @param speed
	 * @param index indexΪ��־λ,0Ϊ����¼,1Ϊ��¼
	 */
	public void setPoints(int [] array,double speed,int index){
		for(int i=0;i<array.length;i++){
			array[i] -= (int)speed;
		}
		if(index == 1){
			for(int t=array.length-1;t>=0;t--){
				if(array[t] <= 370){
					yList.add(newY[t]+188);
					break;
				}
			}
		}
	}
	
}
