/**
 * 列车仿真的主界面
 */
package com.crh2.frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.UIManager;

import com.crh2.chart.SpeedChart;
import com.crh2.frame.items.CRHPanel;
import com.crh2.javabean.RouteName;
import com.crh2.service.RouteService;
import com.crh2.socket.SocketConnection;
import com.crh2.util.MyTools;
import com.crh2.util.MyUtillity;

public class CRHSimulation extends JFrame implements ActionListener {

	// 菜单栏
	JMenuBar jMenuBar = null;
	// 菜单
	JMenu userManageMenu,routeChooseMenu,checkResultMenu, helpMenu;
	// 用户管理中的子菜单
	JMenuItem userManageMenu_modifyPassword,userManageMenu_filePath,userManageMenu_exit;
	// 线路选择菜单中的子菜单
	JRadioButtonMenuItem routeChooseMenu_jjdown;
	JMenuItem routeChooseMenu_newRoute;//新建线路
	JMenuItem routeChooseMenu_deleteRoute;//删除线路
	//查看结果菜单中的子菜单
	JMenuItem resultMenu_charts,resultMenu_calResult;
	// 帮助菜单中的子菜单
	JMenuItem helpMenu_guide, helpMenu_about;
	// 创建一个ButtonGroup，用于JRadioButtonMenuItem间的互斥
	ButtonGroup routeChooseRadioButtonGroup;
	
	//动车运行界面
	public CRHPanel crhPanel = null;
	//动车运行界面的线程
	private Thread trainThread = null;
	//速度仪表盘
	private SpeedChart speedChart = null;
	//7.14晚修改
	private int routeId;
	//记录将要增加的新路线在routeChooseMenu中的位置
	private int routeMenuIndex = 0;
	//创建Socket监听
	private SocketConnection socketClient = null;

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		CRHSimulation crhSimulation = new CRHSimulation(1);
	}

	// 构造函数，初始化各个组件
	public CRHSimulation(int routeId) {
		this.routeId = routeId;
		//设置frame背景为白色
		this.setBackground(Color.WHITE);
		// 初始化菜单栏
		jMenuBar = new JMenuBar();
		// 初始化“用户管理”菜单
		this.initUserManageMenu();
		// 初始化“ButtonGroup”
		this.initButtonGroup();
		// 初始化“路线选择”菜单
		this.initRouteChooseMenu();
		//初始化“查看结果”菜单
		this.initCheckResultMenu();
		// 初始化“帮助”菜单
		this.initHelpMenu();
		// 将菜单加入到菜单栏中
		this.addToMenuBar();
		
		//初始化界面
		this.initInterface();

		this.setJMenuBar(jMenuBar);
		this.add(crhPanel,BorderLayout.CENTER);
		this.add(speedChart,BorderLayout.SOUTH);
		this.setSize(1173, 1160);
		MyUtillity.setFrameOnCenter(this);
		this.setTitle("列车运行仿真");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		// 最大化显示窗体
//		this.setExtendedState(JFrame.MAXIMIZED_BOTH);

		//train的线程
		trainThread = new Thread(crhPanel);
		trainThread.start();
		//开始监听服务器端发来的信号
		socketClient = new SocketConnection();
		//TCP监听线程
		Thread tcpThread = new Thread(socketClient);
		tcpThread.start();
	}

	// 初始化“用户管理”菜单
	public void initUserManageMenu() {
		userManageMenu = new JMenu("用户管理");
		//初始化用户管理中的子菜单
		userManageMenu_modifyPassword = new JMenuItem("修改密码");
		userManageMenu_filePath = new JMenuItem("路径修改");
		userManageMenu_exit = new JMenuItem("退出系统");
		//将子菜单加入菜单中
		userManageMenu.add(userManageMenu_modifyPassword);
		userManageMenu.add(userManageMenu_filePath);
		userManageMenu.addSeparator();//分割线
		userManageMenu.add(userManageMenu_exit);
		//注册监听
		userManageMenu_modifyPassword.addActionListener(this);
		userManageMenu_modifyPassword.setActionCommand("ModifyPassword");
		userManageMenu_filePath.addActionListener(this);
		userManageMenu_filePath.setActionCommand("ModifyFilePath");
		userManageMenu_exit.addActionListener(this);
		userManageMenu_exit.setActionCommand("Exit");
	}

	// 初始化“线路选择”菜单
	public void initRouteChooseMenu() {
		routeChooseMenu = new JMenu("线路选择");
		//得到所有的路线信息
		final List<RouteName> routeNameList = RouteService.getAllRouteName();
		for(int i=0;i<routeNameList.size();i++){
			//对每个路线生成JRadioButtonMenuItem
			final JRadioButtonMenuItem item = new JRadioButtonMenuItem(routeNameList.get(i).getName(), true);
			//actioncommand为Route+id
			item.setActionCommand("Route"+routeNameList.get(i).getId());
			item.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
//					//关闭当前的线路
//					dispose();
					//打开新的线路
//					CRHSimulation crhSimulation = new CRHSimulation(MyUtillity.getNumFromString(item.getActionCommand()));
					resetCRHSimulation(MyUtillity.getNumFromString(item.getActionCommand()));
					//给新的线路设置路线的单选按钮
//					crhSimulation.setJRadioButtonMenuItemSelected(item.getActionCommand());
				}
			});
			routeChooseMenu.add(item,i);
			this.addToButtonGroup(item);// 将radio加入到ButtonGroup
		}
		//“新建线路”
		routeChooseMenu_newRoute = new JMenuItem("新建线路");
		//注册监听
		routeChooseMenu_newRoute.addActionListener(this);
		routeChooseMenu_newRoute.setActionCommand("OtherRoute");
		//删除线路
		routeChooseMenu_deleteRoute = new JMenuItem("删除线路");
		routeChooseMenu_deleteRoute.addActionListener(this);
		routeChooseMenu_deleteRoute.setActionCommand("deleteRoute");
		// 加入到菜单中
		routeChooseMenu.addSeparator();
		routeChooseMenu.add(routeChooseMenu_newRoute);
		routeChooseMenu.add(routeChooseMenu_deleteRoute);
	}
	
	//当用户增加一条新的线路的时候，下拉表单要增加新的线路名称
	public void addNewLineToRouteMenu(RouteName newRoute){
		final JRadioButtonMenuItem item = new JRadioButtonMenuItem(newRoute.getName(), true);
		item.setActionCommand("Route"+newRoute.getId());
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resetCRHSimulation(MyUtillity.getNumFromString(item.getActionCommand()));
			}
		});
		routeChooseMenu.add(item,routeMenuIndex);
		this.addToButtonGroup(item);// 将radio加入到ButtonGroup
	}
	
	//当用户删除一条线路的时候，下拉表单需要更新
	public void deleteRouteItemFromMenu(ArrayList<Integer> routeIdList){
		Enumeration<AbstractButton> items = routeChooseRadioButtonGroup.getElements();
		while (items.hasMoreElements()) {
			JRadioButtonMenuItem item = (JRadioButtonMenuItem) items.nextElement();
			if (routeIdList.contains(MyUtillity.getNumFromString(item.getActionCommand()))) {
				routeChooseMenu.remove(item);
			}
		}
	}
	
	//重置CRHSimulation
	public void resetCRHSimulation(int routeId){
		crhPanel.resetAllData(routeId);
	}
	
	//初始化“查看结果”菜单
	public void initCheckResultMenu(){
		checkResultMenu = new JMenu("查看结果");
		resultMenu_charts = new JMenuItem("曲线生成");
		resultMenu_calResult = new JMenuItem("数据结果");
		//注册监听
		resultMenu_charts.addActionListener(this);
		resultMenu_charts.setActionCommand("Charts");
		resultMenu_calResult.addActionListener(this);
		resultMenu_calResult.setActionCommand("CalResult");
		// 将子菜单加入到菜单中
		checkResultMenu.add(resultMenu_charts);
		checkResultMenu.add(resultMenu_calResult);
	}

	// 初始化“帮助”菜单
	public void initHelpMenu() {
		helpMenu = new JMenu("帮助");
		helpMenu_about = new JMenuItem("关于本软件");
		helpMenu_guide = new JMenuItem("操作指南");
		// 注册监听
		helpMenu_about.addActionListener(this);
		helpMenu_about.setActionCommand("About");
		helpMenu_guide.addActionListener(this);
		helpMenu_guide.setActionCommand("Guide");
		helpMenu.add(helpMenu_guide);
		helpMenu.add(helpMenu_about);
	}

	// 初始化ButtonGroup
	public void initButtonGroup() {
		routeChooseRadioButtonGroup = new ButtonGroup();
	}
	
	//初始化界面
	public void initInterface(){
		crhPanel = new CRHPanel(routeId);//应置于中间
		speedChart = new SpeedChart();//应置于下方
		crhPanel.setSize(crhPanel.getPreferredSize());
		speedChart.setSize(speedChart.getPreferredSize());
	}

	// 将routeChooseMenu加入到ButtonGroup中，实现互斥
	public void addToButtonGroup(JRadioButtonMenuItem item) {
		routeMenuIndex++;
		routeChooseRadioButtonGroup.add(item);
	}

	// 将所有菜单加入到菜单栏中
	public void addToMenuBar() {
		jMenuBar.add(userManageMenu);
		jMenuBar.add(routeChooseMenu);
		jMenuBar.add(checkResultMenu);
		jMenuBar.add(helpMenu);
	}
	
	//得到ButtonGroup中被选中的JRadioButtonMenuItem
	public JRadioButtonMenuItem getSelectedJRadioButtonMenuItem() {
		Enumeration<AbstractButton> items = routeChooseRadioButtonGroup.getElements();
		while (items.hasMoreElements()) {
			JRadioButtonMenuItem item = (JRadioButtonMenuItem) items.nextElement();
			if (item.isSelected()) {
				return item;
			}
		}
		return null;
	}
	
	//设置JRadioButtonMenuItem被选中
	public void setJRadioButtonMenuItemSelected(String actionCommand){
		Enumeration<AbstractButton> items = routeChooseRadioButtonGroup.getElements();
		while (items.hasMoreElements()) {
			JRadioButtonMenuItem item = (JRadioButtonMenuItem) items.nextElement();
			if (item.getActionCommand().equals(actionCommand) || item.getText().equals(actionCommand)) {
				item.setSelected(true);
			}
		}
	}

	//获得目前选中线路的ActionCommand
	public String getSelectedRadioButtonActionCommand(){
		return this.getSelectedJRadioButtonMenuItem().getActionCommand();
	}
	
	//获得线路的id
	public int getRouteId(){
		String actionCommandStr = this.getSelectedRadioButtonActionCommand();//为了得到路线的id
		int routeId = MyTools.getNumFromStr(actionCommandStr);
		return routeId;
	}
	
	//获得路线的name
	public String getRouteName(){
		return this.getSelectedJRadioButtonMenuItem().getText();
	}
	
	public void actionPerformed(ActionEvent e) {
		// 判断是哪个按钮被选中
		if (e.getActionCommand().equals("ModifyPassword")) {//修改密码
			ModifyPasswordFrame modifyPasswordFrame = new ModifyPasswordFrame();
		}else if(e.getActionCommand().equals("ModifyFilePath")){
			ModifyFilePathFrame modifyFilePathFrame = new ModifyFilePathFrame();
		}else if(e.getActionCommand().equals("Exit")) {//退出系统
			System.exit(0);
		}else if(e.getActionCommand().equals("About")){//关于
			JOptionPane.showMessageDialog(this, "关于《列车仿真运行》软件声明如下：\n" +
					"本软件为北京交通大学电气工程学院电力电子研究所实验室开发研究的，\n用于列车牵引计算仿真的研究学习。" +
					"如有问题或者更好的建议，欢迎提出，\n我们将积极改进。谢谢！");
		}else if(e.getActionCommand().equals("Guide")){
			JOptionPane.showMessageDialog(this, "关于“列车运行仿真”软件的操作说明：\n功能一：选择已有线路仿真运行\n    点击菜单-线路选择-京津线（下行），然后可以点击菜单项中“查看结果”，就可以选择你想看的“曲线生成”或者“数据结果”。\n功能二：新建/删除线路仿真运行\n    点击菜单-线路选择-新建线路，将出现一个对话框，填上“线路名称”、“车辆设置”、“限速设置”以及初始的数据文件，然后点击“开始计算”，计算完毕后，点“完成”。\n然后在线路选择中就可以看到刚才新建的线路了。\n    删除线路也一样，点击菜单-线路选择-删除线路，在弹出的对话框中，勾选准备删除的线路，然后点击“删除”，就可以了。\n功能三：仿真运行操作\n    在菜单中-线路选择中，任意选择一条线路，然后点击主界面中的“开始”，就可以看到主界面上的动画演示了，在运行任何过程中，你可以随时选择“暂停”，以及动画演示\n的快慢，“x1”、“x2”、“x3”动画速度从慢到快。最后动画演示完毕后，点击“退出”。\n功能四：用户管理\n    软件初始账户为“admin”，密码为空，你可以选择菜单中的“用户管理”，进行用户密码修改。");
		}else if(e.getActionCommand().equals("OtherRoute")){//新建路线
			NewRouteFrame newRouteFrame = new NewRouteFrame(this,true);
		}else if(e.getActionCommand().equals("CalResult")){//查看计算结果数据
			CalculateResultFrame calculateResultFrame = new CalculateResultFrame(this, true, this.getRouteName(), this.getRouteId());
		}else if(e.getActionCommand().equals("Charts")){
			int routeId = this.getRouteId();
			FourChartsFrame fourChartsFrame = new FourChartsFrame(null,false,routeId);
		}else if(e.getActionCommand().equals("deleteRoute")){
			int routeId = this.getRouteId();
			DeleteRouteFrame deleteRouteFrame = new DeleteRouteFrame(this,true,routeId);
		}
	}

}
