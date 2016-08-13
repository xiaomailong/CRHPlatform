package com.crh.view.panel;

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

import com.crh2.frame.items.DrawCurve;
import com.crh2.frame.items.DrawLengthIndex;
import com.crh2.frame.items.DrawSection;
import com.crh2.frame.items.DrawSlope;
import com.crh2.frame.items.DrawTable;
import com.crh2.frame.items.DrawTwoLines;
import com.crh2.javabean.Rundata;
import com.crh2.service.DataService;
import com.crh2.service.TrainParameter;
import com.crh2.util.MyTools;
import com.crh2.view.RailWay;
import com.crh2.view.Train;

/**
 * 2014.10.15�̶�ʱ�ֽ��ܿ������з���ġ��������С�
 * @author huhui
 */
public class SpecifiedTimeEnergyAutoCRHPanel extends JPanel implements Runnable {
	
	/**
	 * ����һ��Train
	 */
	private Train train = null;
	/**
	 * ����Train�Ŀ���
	 */
	private int TRAIN_WIDTH = 70;
	/**
	 * ����Train�ĸ߶�
	 */
	private int TRAIN_HEIGHT = 12;
	//����һ��RailWay
	/**
	 * ����RailWay�Ŀ���
	 */
	private double RAILWAY_WIDTH = 5;
	/**
	 * ����RailWay�ĸ߶�
	 */
	private double RAILWAY_HEIGHT = 50;
	/**
	 * RailWya��������
	 */
	private double RAILWAY_WAY_Y = 367;
	
	/**
	 * ����Train�ĳ�ʼ����
	 */
	private int TRAIN_X = 300;
	private int TRAIN_Y = 442; //2014.10.15 565->442
	
	/**
	 * �����г��ұ߶���ĺ�����
	 */
	private double startPoint = TRAIN_X + TRAIN_WIDTH;
	
	/**
	 * ����һ��ArrayList�������RailWay
	 */
	private ArrayList<RailWay> railWayList = null;
	/**
	 * ����һ��ArrayList����������������ʱ������
	 */
	private ArrayList<Rundata> runDataList = null;
	/**
	 * ����һ��int�������������ô�ӡ���
	 */
	private int lengthIndex = 1;
	/**
	 * ������
	 */
	private DrawTable drawTable = null;
	/**
	 * �����
	 */
	private DrawCurve drawCurve = null;
	/**
	 * �������
	 */
	private DrawLengthIndex drawLengthIndex = null;
	/**
	 * ���µ�
	 */
	private DrawSlope drawSlope = null;
	/**
	 * ���嶯����ͼƬ
	 */
	private Image crhImage = null;
	/**
	 * �ж��Ƿ���ͣ
	 */
	public static boolean isPause = true;
	/**
	 * ����sleep��ʱ��
	 */
	public static int sleepTime = 10;
	/**
	 * ���ݶ���
	 */
	private DrawSection drawSection = null;
	/**
	 * ��·id
	 */
	private int routeId;
	/**
	 * run�����е�t
	 */
	private int t = 0;
	private DataService dataService = null;
	
	
	/**
	 * ���캯������ʼ��Train��RailWay
	 * @param routeId
	 * @param runDataList
	 */
	public SpecifiedTimeEnergyAutoCRHPanel(int routeId, ArrayList<Rundata> runDataList){
		this.routeId = routeId;
		this.runDataList = new ArrayList<Rundata>(runDataList);//������������
		this.resetAllData(routeId);
	}
	
	/**
	 * ���û�������·ʱ����ʼ�����в���
	 * @param routeId
	 */
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
		crhImage = Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/train.jpg"));
		t = 0;
		this.repaint();
	}
	
	/**
	 * ��дpaint����
	 */
	@Override
	public void paint(Graphics g){
		super.paint(g);
		//���ñ���
		g.setColor(new Color(238,238,238));
		g.fillRect(0, 190, 3200, 162);//2014.10.15 252->162
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
		//���µ��������
		drawSlope.drawSlope(g);
		//�����񣬲��ҽ������������µ����������ס
		drawTable.drawTable(g, SpecifiedTimeEnergySimulationPanel.AUTO_MODEL);
		
	}
	
	/**
	 * ��������
	 * @param x
	 * @param y
	 * @param g
	 */
	public void drawTrain(double x,double y,Graphics g){
		Graphics2D g2d = (Graphics2D)g;
		g.drawImage(crhImage, (int)x, (int)y, TRAIN_WIDTH, TRAIN_HEIGHT, this);
		this.drawSpeed((float)(x+TRAIN_WIDTH), (float)y, TrainParameter.speed, g2d);
	}
	
	/**
	 * ���ٶ�
	 * @param x
	 * @param y
	 * @param speed
	 * @param g2d
	 */
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
	
	/**
	 * �����
	 * @param g
	 */
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
	
	/**
	 * �����µ�RailWay
	 */
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
	
	/**
	 * ���ַ�
	 * @param railWay
	 * @param g2d
	 */
	public void drawContent(RailWay railWay,Graphics2D g2d){
		String str = railWay.getContent().getStr();
		if(!str.equals("")){
			g2d.setColor(Color.RED);//�����ַ���ɫ
			g2d.drawString(str, (float)railWay.getX(), (float)railWay.getY());
			g2d.setColor(Color.GREEN);
		}
	}
	
	/**
	 * ��ʼ��Train
	 */
	public void initTrain(){
		train = new Train(TRAIN_X, TRAIN_Y);
	}
	
	/**
	 * ��ʼ��Railway
	 * @param routeId
	 */
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
	
	/**
	 * ���ô�С
	 */
	public Dimension getPreferredSize() {
        return new Dimension(300, 150); //Set your own size.
    }
	
	/**
	 * �õ���������ʱ����
	 */
	public void initRunData(){
		//��runDataList������������Ϊ0��data
		Rundata extraData = runDataList.get(runDataList.size()-1);
		runDataList.add(extraData);
		runDataList.add(extraData);
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		double realSpeed = 0;
		double length = 0;
		double speed = 0;
		double time = 0;
		while (t<runDataList.size()) {
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
				speed = realSpeed/3.5715;//���ٶ���С
				time = runDataList.get(t).getRuntime();

				TrainParameter.speed = realSpeed;
				TrainParameter.length = length;
				// ��RailWay������
				for (int i = 0; i < railWayList.size(); i++) {
					railWayList.get(i).setSpeed(speed);
					railWayList.get(i).run();
				}
				
				//���ٶȺ�ʱ���߸�ֵ
				DrawTwoLines.setValues(realSpeed, time);
				
				//ͳ�������ٶ�ֵ������������
				MyTools.countSpeed(speed);
				
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
				t = t + 1;
				/*if(runDataList.get(t).getSpeed() == 0){//��վͣ��
					System.out.println(MyTools.countlength);
//					break;
					TrainParameter.speed = 0;
//					isPause = true;
				}*/
				Thread.sleep(sleepTime);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}

}