/**
 * �г������������
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

	// �˵���
	JMenuBar jMenuBar = null;
	// �˵�
	JMenu userManageMenu,routeChooseMenu,checkResultMenu, helpMenu;
	// �û������е��Ӳ˵�
	JMenuItem userManageMenu_modifyPassword,userManageMenu_filePath,userManageMenu_exit;
	// ��·ѡ��˵��е��Ӳ˵�
	JRadioButtonMenuItem routeChooseMenu_jjdown;
	JMenuItem routeChooseMenu_newRoute;//�½���·
	JMenuItem routeChooseMenu_deleteRoute;//ɾ����·
	//�鿴����˵��е��Ӳ˵�
	JMenuItem resultMenu_charts,resultMenu_calResult;
	// �����˵��е��Ӳ˵�
	JMenuItem helpMenu_guide, helpMenu_about;
	// ����һ��ButtonGroup������JRadioButtonMenuItem��Ļ���
	ButtonGroup routeChooseRadioButtonGroup;
	
	//�������н���
	public CRHPanel crhPanel = null;
	//�������н�����߳�
	private Thread trainThread = null;
	//�ٶ��Ǳ���
	private SpeedChart speedChart = null;
	//7.14���޸�
	private int routeId;
	//��¼��Ҫ���ӵ���·����routeChooseMenu�е�λ��
	private int routeMenuIndex = 0;
	//����Socket����
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

	// ���캯������ʼ���������
	public CRHSimulation(int routeId) {
		this.routeId = routeId;
		//����frame����Ϊ��ɫ
		this.setBackground(Color.WHITE);
		// ��ʼ���˵���
		jMenuBar = new JMenuBar();
		// ��ʼ�����û������˵�
		this.initUserManageMenu();
		// ��ʼ����ButtonGroup��
		this.initButtonGroup();
		// ��ʼ����·��ѡ�񡱲˵�
		this.initRouteChooseMenu();
		//��ʼ�����鿴������˵�
		this.initCheckResultMenu();
		// ��ʼ�����������˵�
		this.initHelpMenu();
		// ���˵����뵽�˵�����
		this.addToMenuBar();
		
		//��ʼ������
		this.initInterface();

		this.setJMenuBar(jMenuBar);
		this.add(crhPanel,BorderLayout.CENTER);
		this.add(speedChart,BorderLayout.SOUTH);
		this.setSize(1173, 1160);
		MyUtillity.setFrameOnCenter(this);
		this.setTitle("�г����з���");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		// �����ʾ����
//		this.setExtendedState(JFrame.MAXIMIZED_BOTH);

		//train���߳�
		trainThread = new Thread(crhPanel);
		trainThread.start();
		//��ʼ�����������˷������ź�
		socketClient = new SocketConnection();
		//TCP�����߳�
		Thread tcpThread = new Thread(socketClient);
		tcpThread.start();
	}

	// ��ʼ�����û������˵�
	public void initUserManageMenu() {
		userManageMenu = new JMenu("�û�����");
		//��ʼ���û������е��Ӳ˵�
		userManageMenu_modifyPassword = new JMenuItem("�޸�����");
		userManageMenu_filePath = new JMenuItem("·���޸�");
		userManageMenu_exit = new JMenuItem("�˳�ϵͳ");
		//���Ӳ˵�����˵���
		userManageMenu.add(userManageMenu_modifyPassword);
		userManageMenu.add(userManageMenu_filePath);
		userManageMenu.addSeparator();//�ָ���
		userManageMenu.add(userManageMenu_exit);
		//ע�����
		userManageMenu_modifyPassword.addActionListener(this);
		userManageMenu_modifyPassword.setActionCommand("ModifyPassword");
		userManageMenu_filePath.addActionListener(this);
		userManageMenu_filePath.setActionCommand("ModifyFilePath");
		userManageMenu_exit.addActionListener(this);
		userManageMenu_exit.setActionCommand("Exit");
	}

	// ��ʼ������·ѡ�񡱲˵�
	public void initRouteChooseMenu() {
		routeChooseMenu = new JMenu("��·ѡ��");
		//�õ����е�·����Ϣ
		final List<RouteName> routeNameList = RouteService.getAllRouteName();
		for(int i=0;i<routeNameList.size();i++){
			//��ÿ��·������JRadioButtonMenuItem
			final JRadioButtonMenuItem item = new JRadioButtonMenuItem(routeNameList.get(i).getName(), true);
			//actioncommandΪRoute+id
			item.setActionCommand("Route"+routeNameList.get(i).getId());
			item.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
//					//�رյ�ǰ����·
//					dispose();
					//���µ���·
//					CRHSimulation crhSimulation = new CRHSimulation(MyUtillity.getNumFromString(item.getActionCommand()));
					resetCRHSimulation(MyUtillity.getNumFromString(item.getActionCommand()));
					//���µ���·����·�ߵĵ�ѡ��ť
//					crhSimulation.setJRadioButtonMenuItemSelected(item.getActionCommand());
				}
			});
			routeChooseMenu.add(item,i);
			this.addToButtonGroup(item);// ��radio���뵽ButtonGroup
		}
		//���½���·��
		routeChooseMenu_newRoute = new JMenuItem("�½���·");
		//ע�����
		routeChooseMenu_newRoute.addActionListener(this);
		routeChooseMenu_newRoute.setActionCommand("OtherRoute");
		//ɾ����·
		routeChooseMenu_deleteRoute = new JMenuItem("ɾ����·");
		routeChooseMenu_deleteRoute.addActionListener(this);
		routeChooseMenu_deleteRoute.setActionCommand("deleteRoute");
		// ���뵽�˵���
		routeChooseMenu.addSeparator();
		routeChooseMenu.add(routeChooseMenu_newRoute);
		routeChooseMenu.add(routeChooseMenu_deleteRoute);
	}
	
	//���û�����һ���µ���·��ʱ��������Ҫ�����µ���·����
	public void addNewLineToRouteMenu(RouteName newRoute){
		final JRadioButtonMenuItem item = new JRadioButtonMenuItem(newRoute.getName(), true);
		item.setActionCommand("Route"+newRoute.getId());
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resetCRHSimulation(MyUtillity.getNumFromString(item.getActionCommand()));
			}
		});
		routeChooseMenu.add(item,routeMenuIndex);
		this.addToButtonGroup(item);// ��radio���뵽ButtonGroup
	}
	
	//���û�ɾ��һ����·��ʱ����������Ҫ����
	public void deleteRouteItemFromMenu(ArrayList<Integer> routeIdList){
		Enumeration<AbstractButton> items = routeChooseRadioButtonGroup.getElements();
		while (items.hasMoreElements()) {
			JRadioButtonMenuItem item = (JRadioButtonMenuItem) items.nextElement();
			if (routeIdList.contains(MyUtillity.getNumFromString(item.getActionCommand()))) {
				routeChooseMenu.remove(item);
			}
		}
	}
	
	//����CRHSimulation
	public void resetCRHSimulation(int routeId){
		crhPanel.resetAllData(routeId);
	}
	
	//��ʼ�����鿴������˵�
	public void initCheckResultMenu(){
		checkResultMenu = new JMenu("�鿴���");
		resultMenu_charts = new JMenuItem("��������");
		resultMenu_calResult = new JMenuItem("���ݽ��");
		//ע�����
		resultMenu_charts.addActionListener(this);
		resultMenu_charts.setActionCommand("Charts");
		resultMenu_calResult.addActionListener(this);
		resultMenu_calResult.setActionCommand("CalResult");
		// ���Ӳ˵����뵽�˵���
		checkResultMenu.add(resultMenu_charts);
		checkResultMenu.add(resultMenu_calResult);
	}

	// ��ʼ�����������˵�
	public void initHelpMenu() {
		helpMenu = new JMenu("����");
		helpMenu_about = new JMenuItem("���ڱ����");
		helpMenu_guide = new JMenuItem("����ָ��");
		// ע�����
		helpMenu_about.addActionListener(this);
		helpMenu_about.setActionCommand("About");
		helpMenu_guide.addActionListener(this);
		helpMenu_guide.setActionCommand("Guide");
		helpMenu.add(helpMenu_guide);
		helpMenu.add(helpMenu_about);
	}

	// ��ʼ��ButtonGroup
	public void initButtonGroup() {
		routeChooseRadioButtonGroup = new ButtonGroup();
	}
	
	//��ʼ������
	public void initInterface(){
		crhPanel = new CRHPanel(routeId);//Ӧ�����м�
		speedChart = new SpeedChart();//Ӧ�����·�
		crhPanel.setSize(crhPanel.getPreferredSize());
		speedChart.setSize(speedChart.getPreferredSize());
	}

	// ��routeChooseMenu���뵽ButtonGroup�У�ʵ�ֻ���
	public void addToButtonGroup(JRadioButtonMenuItem item) {
		routeMenuIndex++;
		routeChooseRadioButtonGroup.add(item);
	}

	// �����в˵����뵽�˵�����
	public void addToMenuBar() {
		jMenuBar.add(userManageMenu);
		jMenuBar.add(routeChooseMenu);
		jMenuBar.add(checkResultMenu);
		jMenuBar.add(helpMenu);
	}
	
	//�õ�ButtonGroup�б�ѡ�е�JRadioButtonMenuItem
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
	
	//����JRadioButtonMenuItem��ѡ��
	public void setJRadioButtonMenuItemSelected(String actionCommand){
		Enumeration<AbstractButton> items = routeChooseRadioButtonGroup.getElements();
		while (items.hasMoreElements()) {
			JRadioButtonMenuItem item = (JRadioButtonMenuItem) items.nextElement();
			if (item.getActionCommand().equals(actionCommand) || item.getText().equals(actionCommand)) {
				item.setSelected(true);
			}
		}
	}

	//���Ŀǰѡ����·��ActionCommand
	public String getSelectedRadioButtonActionCommand(){
		return this.getSelectedJRadioButtonMenuItem().getActionCommand();
	}
	
	//�����·��id
	public int getRouteId(){
		String actionCommandStr = this.getSelectedRadioButtonActionCommand();//Ϊ�˵õ�·�ߵ�id
		int routeId = MyTools.getNumFromStr(actionCommandStr);
		return routeId;
	}
	
	//���·�ߵ�name
	public String getRouteName(){
		return this.getSelectedJRadioButtonMenuItem().getText();
	}
	
	public void actionPerformed(ActionEvent e) {
		// �ж����ĸ���ť��ѡ��
		if (e.getActionCommand().equals("ModifyPassword")) {//�޸�����
			ModifyPasswordFrame modifyPasswordFrame = new ModifyPasswordFrame();
		}else if(e.getActionCommand().equals("ModifyFilePath")){
			ModifyFilePathFrame modifyFilePathFrame = new ModifyFilePathFrame();
		}else if(e.getActionCommand().equals("Exit")) {//�˳�ϵͳ
			System.exit(0);
		}else if(e.getActionCommand().equals("About")){//����
			JOptionPane.showMessageDialog(this, "���ڡ��г��������С�����������£�\n" +
					"�����Ϊ������ͨ��ѧ��������ѧԺ���������о���ʵ���ҿ����о��ģ�\n�����г�ǣ�����������о�ѧϰ��" +
					"����������߸��õĽ��飬��ӭ�����\n���ǽ������Ľ���лл��");
		}else if(e.getActionCommand().equals("Guide")){
			JOptionPane.showMessageDialog(this, "���ڡ��г����з��桱����Ĳ���˵����\n����һ��ѡ��������·��������\n    ����˵�-��·ѡ��-�����ߣ����У���Ȼ����Ե���˵����С��鿴��������Ϳ���ѡ�����뿴�ġ��������ɡ����ߡ����ݽ������\n���ܶ����½�/ɾ����·��������\n    ����˵�-��·ѡ��-�½���·��������һ���Ի������ϡ���·���ơ������������á������������á��Լ���ʼ�������ļ���Ȼ��������ʼ���㡱��������Ϻ󣬵㡰��ɡ���\nȻ������·ѡ���оͿ��Կ����ղ��½�����·�ˡ�\n    ɾ����·Ҳһ��������˵�-��·ѡ��-ɾ����·���ڵ����ĶԻ����У���ѡ׼��ɾ������·��Ȼ������ɾ�������Ϳ����ˡ�\n���������������в���\n    �ڲ˵���-��·ѡ���У�����ѡ��һ����·��Ȼ�����������еġ���ʼ�����Ϳ��Կ����������ϵĶ�����ʾ�ˣ��������κι����У��������ʱѡ����ͣ�����Լ�������ʾ\n�Ŀ�������x1������x2������x3�������ٶȴ������졣��󶯻���ʾ��Ϻ󣬵�����˳�����\n�����ģ��û�����\n    �����ʼ�˻�Ϊ��admin��������Ϊ�գ������ѡ��˵��еġ��û������������û������޸ġ�");
		}else if(e.getActionCommand().equals("OtherRoute")){//�½�·��
			NewRouteFrame newRouteFrame = new NewRouteFrame(this,true);
		}else if(e.getActionCommand().equals("CalResult")){//�鿴����������
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
