package com.crh2.frame.items;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.JPanel;

import com.crh.view.panel.SpecifiedTimeEnergySimulationPanel;
import com.crh2.chart.SpeedChart;
import com.crh2.javabean.Rundata;
import com.crh2.service.DataService;
import com.crh2.service.TrainParameter;
import com.crh2.util.MyTools;
import com.crh2.view.RailWay;
import com.crh2.view.Train;

public class CRHPanel extends JPanel implements Runnable {
	
	//����һ��Train
	private Train train = null;
	private int TRAIN_WIDTH = 70;//����Train�Ŀ��
	private int TRAIN_HEIGHT = 12;//����Train�ĸ߶�
	//����һ��RailWay
	private double RAILWAY_WIDTH = 5;//����RailWay�Ŀ��
	private double RAILWAY_HEIGHT = 50;//����RailWay�ĸ߶�
	private double RAILWAY_WAY_Y = 367;//RailWya��������
	
	//����Train�ĳ�ʼ����
	private int TRAIN_X = 300;
	private int TRAIN_Y = 565;
	
	//�����г��ұ߶���ĺ�����
	private double startPoint = TRAIN_X + TRAIN_WIDTH;
	
	//����һ��ArrayList�������RailWay
	private ArrayList<RailWay> railWayList = null;
	//����һ��ArrayList����������������ʱ������
	private ArrayList<Rundata> runDataList = null;
	//����һ��int�������������ô�ӡ���
	private int lengthIndex = 1;
	//�����
	private DrawTable drawTable = null;
	//�����
	private DrawCurve drawCurve = null;
	//�������
	private DrawLengthIndex drawLengthIndex = null;
	//���µ�
	private DrawSlope drawSlope = null;
	//���嶯����ͼƬ
	private Image crhImage = null;
	//�ж��Ƿ���ͣ
	public static boolean isPause = true;
	//����sleep��ʱ��
	public static int sleepTime = 10;
	//���ݶ���
	private DrawSection drawSection = null;
	//��·id
	private int routeId;
	//run�����е�t
	private int t = 0;
	private DataService dataService = null;
	
	
	//���캯������ʼ��Train��RailWay
	public CRHPanel(int routeId){
		//7.14���޸ģ������ʼ����ʽ
		this.routeId = routeId;
		this.resetAllData(routeId);
	}
	
	//���û�������·ʱ����ʼ�����в���
	public void resetAllData(int routeId){
		this.routeId = routeId;
		drawCurve = new DrawCurve(routeId);
		drawSlope = new DrawSlope(routeId);
		drawSection = new DrawSection(routeId);
		dataService = new DataService();
		//��ʼ��railWayList
		railWayList = new ArrayList<RailWay>();
		//��ʼ��DrawTable
		drawTable = new DrawTable();
		//��ʼ��DrawLengthIndex
		drawLengthIndex = new DrawLengthIndex();
		//����isPause
		isPause = true;
		//��ʼ��runDataList
		this.initRunData();
		//����CRHPanelΪ��ɫ
		this.setBackground(Color.WHITE);
		//��ʼ��train
		this.initTrain();
		//��ʼ��railWay
		this.initRailway(routeId);
		//��ʼ��������ͼƬ
		crhImage = Toolkit.getDefaultToolkit().getImage(
				Panel.class.getResource("/train.jpg"));
		t = 0;
		this.repaint();
	}
	
	//��дpaint����
	@Override
	public void paint(Graphics g){
		super.paint(g);
		//���ñ���
		g.setColor(new Color(238,238,238));
		g.fillRect(0, 252, 3200, 224);
		//���г����б���������յ���ɫ
		drawSlope.drawSkyBackGround(g);
		//��������
		this.drawTrain(train.getX(), train.getY(), g);
		//�����
//		this.drawRailWay(g);
		//���ٶ��ߺ�ʱ����
		DrawTwoLines.drawTwoLine(g);
		//���ݶ���
		drawSection.drawSlope(g);
		//�����
		drawCurve.drawCurve(g);
		//�������
		drawLengthIndex.drawLengthIndex(g);
		//���µ�
		drawSlope.drawSlope(g);
		//����񣬲��ҽ������������µ����������ס
		drawTable.drawTable(g, SpecifiedTimeEnergySimulationPanel.AUTO_MODEL);
		
	}
	
	//��������
	public void drawTrain(double x,double y,Graphics g){
		Graphics2D g2d = (Graphics2D)g;
		g.drawImage(crhImage, (int)x, (int)y, TRAIN_WIDTH, TRAIN_HEIGHT, this);
		this.drawSpeed((float)(x+TRAIN_WIDTH), (float)y, TrainParameter.speed, g2d);
	}
	
	//���ٶ�
	public void drawSpeed(float x,float y,double speed,Graphics2D g2d){
		String str = MyTools.numFormat2(speed) + "km/h";
		if(!str.equals("")){
			g2d.setColor(Color.BLACK);//�����ַ���ɫ
			Font font = new Font("����", Font.BOLD, 16);
			g2d.setFont(font);
			g2d.drawString(str, x, y);
			g2d.setColor(Color.BLUE);
		}
	}
	
	//�����
	public void drawRailWay(Graphics g){
		g.setColor(Color.GREEN);//���ù����ɫ
		Graphics2D g2d = (Graphics2D)g;
		for(int i=0;i<railWayList.size();i++){
			double x = railWayList.get(i).getX();
			if(x>=0 && x<=1400){
				Rectangle2D railWayRect=new Rectangle2D.Double(railWayList.get(i).getX(),railWayList.get(i).getY(),RAILWAY_WIDTH,RAILWAY_HEIGHT);
				g2d.draw(railWayRect);
				g2d.fill(railWayRect);
				//���ַ�
				this.drawContent(railWayList.get(i), g2d);
				this.createRailWay();
			}else if(x < 0){
				railWayList.remove(i);
			}
		}
	}
	
	//�����µ�RailWay
	public void createRailWay(){
		//�ж��Ƿ���Ҫ�����µ�
		int size = railWayList.size();
		if(railWayList.get(size-5).getX()<1200){//���������5��RailWay��x����С��1200������Ҫ�����µ�
			for(int i=1;i<5000;i++){
				RailWay railWay = new RailWay(railWayList.get(size-1).getX()+RAILWAY_WIDTH*i, RAILWAY_WAY_Y,"");
				railWayList.add(railWay);
			}
		}
	}
	
	//���ַ�
	public void drawContent(RailWay railWay,Graphics2D g2d){
		String str = railWay.getContent().getStr();
		if(!str.equals("")){
			g2d.setColor(Color.RED);//�����ַ���ɫ
			g2d.drawString(str, (float)railWay.getX(), (float)railWay.getY());
			g2d.setColor(Color.GREEN);
		}
	}
	
	//��ʼ��Train
	public void initTrain(){
		train = new Train(TRAIN_X, TRAIN_Y);
	}
	
	//��ʼ��Railway
	public void initRailway(int routeId){
		for(int i=0;i<5000;i++){
			RailWay railWay = new RailWay(RAILWAY_WIDTH*i, RAILWAY_WAY_Y,"");
			if(MyTools.isMostRight(railWay.getX())){
				//��ȡ��һ����վ����
				railWay.setContentStr(dataService.getFirstStationName(routeId));
			}
			railWayList.add(railWay);
		}
	}
	
	//���ô�С
	public Dimension getPreferredSize() {
        return new Dimension(300, 150); //Set your own size.
    }
	
	//�õ���������ʱ����
	public void initRunData(){
		runDataList = (ArrayList<Rundata>) dataService.getRundata(routeId);
		//��runDataList������������Ϊ0��data
		Rundata extraData = runDataList.get(runDataList.size()-1);
		runDataList.add(extraData);
		runDataList.add(extraData);
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		int id = 0;
		double realSpeed = 0;
		double length = 0;
		double Cp = 0;
		double speed = 0;
		double time = 0;
		while (true) {
			while(isPause){
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			try {
				// ��runDataList��ȡ���ٶȲ���
				realSpeed = runDataList.get(t).getSpeed();
				length = runDataList.get(t).getDistance();
				Cp = runDataList.get(t).getCp();
				speed = realSpeed / 12;//���ٶ���С
				time = runDataList.get(t).getRuntime();

				TrainParameter.speed = realSpeed;
				TrainParameter.length = length;
				//�õ�����id
				id = runDataList.get(t).getId();
				//�����Ǳ���
				SpeedChart.setValue(realSpeed);
				//����ParaPanel
				ParaPanel.setPanelValue(id,length, Cp, realSpeed, time,routeId);
				// ��RailWay������
				for (int i = 0; i < railWayList.size(); i++) {
					railWayList.get(i).setSpeed(speed);
					railWayList.get(i).run();
				}
				
				//���ٶȺ�ʱ���߸�ֵ
				DrawTwoLines.setValues(realSpeed, time);
				
				//ͳ�������ٶ�ֵ������������
//				MyTools.countSpeed(speed);
				
				//���ݶ���
				drawSection.setSpeed(speed);
				drawSection.run();
				
				//�����
				drawCurve.setSpeed(speed);
				drawCurve.run();
				
				//���µ�
				drawSlope.setSpeed(speed);
				drawSlope.run();
				
				//�������
				drawLengthIndex.setSpeed(speed);
				drawLengthIndex.run();
				
				//����Train��y����
				train.setY(drawSlope.getLastY());
				
				//�ػ�
				this.repaint();
				t = t + 3;
				if(runDataList.get(t).getSpeed() == 0){//ͣ��
//					System.out.println(MyTools.countlength);
//					break;
					ParaPanel.setPanelValue(id,length, 0, 0, time,routeId);
					TrainParameter.speed = 0;
					SpeedChart.setValue(0);
					isPause = true;
				}
				Thread.sleep(sleepTime);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}

}
