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
import com.crh2.frame.items.DrawTwoLinesManual;
import com.crh2.javabean.Rundata;
import com.crh2.service.DataService;
import com.crh2.util.MyTools;
import com.crh2.view.RailWay;
import com.crh2.view.Train;

/**
 * 2014.10.15 常态仿真的“快速运行”
 * @author huhui
 *
 */
public class RealTimeManualCRHPanel extends JPanel {
	
	/**
	 * 定义一个Train
	 */
	private Train train = null;
	/**
	 * 定义Train的宽度
	 */
	private int TRAIN_WIDTH = 70;
	/**
	 * 定义Train的高度
	 */
	private int TRAIN_HEIGHT = 12;
	//定义一个RailWay
	/**
	 * 定义RailWay的宽度
	 */
	private double RAILWAY_WIDTH = 5;
	/**
	 * 定义RailWay的高度
	 */
	private double RAILWAY_HEIGHT = 50;
	/**
	 * RailWya的纵坐标
	 */
	private double RAILWAY_WAY_Y = 367;
	
	/**
	 * 定义Train的初始坐标
	 */
	private int TRAIN_X = 300;
	private int TRAIN_Y = 442; //2014.10.15 565->442
	
	/**
	 * 定义列车右边顶点的横坐标
	 */
	private double startPoint = TRAIN_X + TRAIN_WIDTH;
	
	/**
	 * 定义一个ArrayList，里面放RailWay
	 */
	private ArrayList<RailWay> railWayList = null;
	/**
	 * 定义一个ArrayList，里面存放所有运行时的数据
	 */
	private ArrayList<Rundata> runDataList = null;
	/**
	 * 画表格
	 */
	private DrawTable drawTable = null;
	/**
	 * 画速度曲线和时间曲线 2014.10.16(快速运行模式)
	 */
	private DrawTwoLinesManual drawTwoLinesManual = null;
	/**
	 * 画弯道
	 */
	private DrawCurve drawCurve = null;
	/**
	 * 画公里标
	 */
	private DrawLengthIndex drawLengthIndex = null;
	/**
	 * 画坡道
	 */
	private DrawSlope drawSlope = null;
	/**
	 * 定义动车组图片
	 */
	private Image crhImage = null;
	/**
	 * 画纵断面
	 */
	private DrawSection drawSection = null;
	/**
	 * 线路id
	 */
	private int routeId;
	/**
	 * run函数中的t
	 */
	private int t = 0;
	private DataService dataService = null;
	
	
	/**
	 * 构造函数，初始化Train和RailWay
	 * @param routeId
	 * @param runDataList
	 */
	public RealTimeManualCRHPanel(int routeId, ArrayList<Rundata> runDataList){
		this.routeId = routeId;
		this.runDataList = new ArrayList<Rundata>(runDataList);//拷贝运行数据
		this.resetAllData(routeId);
	}
	
	/**
	 * 当用户更换线路时，初始化所有参数
	 * @param routeId
	 */
	public void resetAllData(int routeId){
		this.routeId = routeId;
		drawTwoLinesManual = new DrawTwoLinesManual(runDataList);
		drawCurve = new DrawCurve(routeId);
		drawSlope = new DrawSlope(routeId);
		drawSection = new DrawSection(routeId);
		dataService = new DataService();
		//初始化railWayList
		railWayList = new ArrayList<RailWay>();
		//初始化DrawTable
		drawTable = new DrawTable();
		//初始化DrawLengthIndex
		drawLengthIndex = new DrawLengthIndex();
		//设置CRHPanel为白色
		this.setBackground(Color.WHITE);
		//初始化train
		this.initTrain();
		//初始化railWay
		this.initRailway(routeId);
		//初始化动车组图片
		crhImage = Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/train.jpg"));
		t = 0;
		this.repaint();
	}
	
	/**
	 * 重写paint方法
	 */
	@Override
	public void paint(Graphics g){
		super.paint(g);
		//设置背景
		g.setColor(new Color(238,238,238));
		g.fillRect(0, 190, 3200, 162);//2014.10.15 252->162
		//画列车运行背景，即天空的颜色
		drawSlope.drawSkyBackGround(g);
		//画动车组
		this.drawTrain(train.getX(), train.getY(), g);
		//画轨道
//		this.drawRailWay(g);
		//画速度线和时间线
		drawTwoLinesManual.drawSpeedAndTimeLines(g);
		//画纵断面
		drawSection.drawSlope(g);
		//画弯道
		drawCurve.drawCurve(g);
		//画公里标
		drawLengthIndex.drawLengthIndex(g);
		//画坡道（轨道）
		drawSlope.drawSlope(g);
		//画表格，并且将多余的弯道、坡道、公里标遮住
		drawTable.drawTable(g, MinTimeSimulationPanel.MANUAL_MODEL);
		
	}
	
	/**
	 * 画动车组
	 * @param x
	 * @param y
	 * @param g
	 */
	public void drawTrain(double x,double y,Graphics g){
		Graphics2D g2d = (Graphics2D)g;
		g.drawImage(crhImage, (int)x, (int)y, TRAIN_WIDTH, TRAIN_HEIGHT, this);
//		this.drawSpeed((float)(x+TRAIN_WIDTH), (float)y, TrainParameter.speed, g2d); //2014.10.18 由于手动翻页，故不需要显示速度
	}
	
	/**
	 * 画速度
	 * @param x
	 * @param y
	 * @param speed
	 * @param g2d
	 */
	public void drawSpeed(float x,float y,double speed,Graphics2D g2d){
		String str = MyTools.numFormat2(speed) + "km/h";
		if(!str.equals("")){
			g2d.setColor(Color.BLACK);//设置字符颜色
			Font font = new Font("宋体", Font.BOLD, 16);
			g2d.setFont(font);
			g2d.drawString(str, x, y);
			g2d.setColor(Color.BLUE);
		}
	}
	
	/**
	 * 画轨道
	 * @param g
	 */
	public void drawRailWay(Graphics g){
		g.setColor(Color.GREEN);//设置轨道颜色
		Graphics2D g2d = (Graphics2D)g;
		for(int i=0;i<railWayList.size();i++){
			double x = railWayList.get(i).getX();
			if(x>=0 && x<=1400){
				Rectangle2D railWayRect=new Rectangle2D.Double(railWayList.get(i).getX(),railWayList.get(i).getY(),RAILWAY_WIDTH,RAILWAY_HEIGHT);
				g2d.draw(railWayRect);
				g2d.fill(railWayRect);
				//画字符
				this.drawContent(railWayList.get(i), g2d);
				this.createRailWay();
			}else if(x < 0){
				railWayList.remove(i);
			}
		}
	}
	
	/**
	 * 生成新的RailWay
	 */
	public void createRailWay(){
		//判断是否需要生成新的
		int size = railWayList.size();
		if(railWayList.get(size-5).getX()<1200){//如果倒数第5个RailWay的x坐标小于1200，则需要生成新的
			for(int i=1;i<5000;i++){
				RailWay railWay = new RailWay(railWayList.get(size-1).getX()+RAILWAY_WIDTH*i, RAILWAY_WAY_Y,"");
				railWayList.add(railWay);
			}
		}
	}
	
	/**
	 * 画字符
	 * @param railWay
	 * @param g2d
	 */
	public void drawContent(RailWay railWay,Graphics2D g2d){
		String str = railWay.getContent().getStr();
		if(!str.equals("")){
			g2d.setColor(Color.RED);//设置字符颜色
			g2d.drawString(str, (float)railWay.getX(), (float)railWay.getY());
			g2d.setColor(Color.GREEN);
		}
	}
	
	/**
	 * 初始化Train
	 */
	public void initTrain(){
		train = new Train(TRAIN_X, TRAIN_Y);
	}
	
	/**
	 * 初始化Railway
	 * @param routeId
	 */
	public void initRailway(int routeId){
		for(int i=0;i<5000;i++){
			RailWay railWay = new RailWay(RAILWAY_WIDTH*i, RAILWAY_WAY_Y,"");
			if(MyTools.isMostRight(railWay.getX())){
				//获取第一个车站名字
				railWay.setContentStr(dataService.getFirstStationName(routeId));
			}
			railWayList.add(railWay);
		}
	}
	
	/**
	 * 设置大小
	 */
	public Dimension getPreferredSize() {
        return new Dimension(300, 150); //Set your own size.
    }
	
	public void run(double speed) {
		double time = 0;
		
		// 让RailWay动起来
		for (int i = 0; i < railWayList.size(); i++) {
			railWayList.get(i).setSpeed(speed);
			railWayList.get(i).run();
		}

		// 给速度和时间线赋值
//		DrawTwoLines.setValues(realSpeed, time);

		// 统计所有速度值，计算总像素
		MyTools.countSpeed(speed);
		
		// 画速度和时间
		drawTwoLinesManual.setSpeed(speed);
		drawTwoLinesManual.run();

		// 画纵断面
		drawSection.setSpeed(speed);
		drawSection.run();

		// 画弯道
		drawCurve.setSpeed(speed);
		drawCurve.run();

		// 画坡道
		drawSlope.setSpeed(speed);
		drawSlope.run();

		// 画公里标
		drawLengthIndex.setSpeed(speed);
		drawLengthIndex.run();

		// 设置Train的y坐标
		train.setY(drawSlope.getLastY());

		// 重绘
		this.repaint();
	}

}
