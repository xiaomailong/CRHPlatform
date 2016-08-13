/**
 * ɾ��·�ߵ�frame
 */
package com.crh2.frame;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

import com.crh2.calculate.TrainAttribute;
import com.crh2.javabean.RouteName;
import com.crh2.service.RouteService;
import com.crh2.util.MyUtillity;

public class DeleteRouteFrame extends JDialog implements ActionListener {
	
	//�������
	private JPanel delRoutePanel,routesPanel,buttonPanel;
	private JCheckBox [] routesCheckBox = null;//·�߶�ѡ��
	private JButton deleteButon,finishButton;
	private List<RouteName> routeNameList = null;
	private int currentRouteId = 0;//Ŀǰ������ʾ��·��
	ArrayList<Integer> routeIdList = null;
	
	//���캯��
	public DeleteRouteFrame(Frame owner,boolean modal,int currentRouteId){
		super(owner, modal);
		//��ʼ������
		routeNameList = RouteService.getAllRouteName();
		this.currentRouteId = currentRouteId;
		//��ʼ����ѡ��panel
		this.initRoutesCheckBox();
		//��ʼ������ɡ�panel
		this.initFinishButton();
		
		//����JDialog
		this.setTitle("��·ɾ��");
		this.add(delRoutePanel,BorderLayout.CENTER);
		this.add(buttonPanel,BorderLayout.SOUTH);
		this.setSize(330, 400);
		MyUtillity.setFrameOnCenter(this);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setResizable(false);
		this.setVisible(true);
	}
	
	//��ʼ����ťpanel
	public void initFinishButton(){
		//��ʼ����ɾ������ť
		deleteButon = new JButton("ɾ��");
		deleteButon.addActionListener(this);
		deleteButon.setActionCommand("delete");
		//��ʼ������ɡ���ť
		finishButton = new JButton("���");
		finishButton.addActionListener(this);
		finishButton.setActionCommand("finish");
		buttonPanel = new JPanel();
		buttonPanel.add(deleteButon);
		buttonPanel.add(finishButton);
	}
	
	//��ʼ��routesCheckBox
	public void initRoutesCheckBox(){
		delRoutePanel = new JPanel();
		routesPanel = new JPanel(new GridLayout(routeNameList.size(), 1));//��ʼ��routespanel
		routesCheckBox = new JCheckBox[routeNameList.size()];//��ʼ����ѡ��
		for(int i=0;i<routesCheckBox.length;i++){
			RouteName route = routeNameList.get(i);
			routesCheckBox[i] = new JCheckBox(route.getId()+"-"+route.getName());
			//����ǵ�ǰ��ʾ��route id������ɾ��
			if(route.getId() == currentRouteId){
				routesCheckBox[i].setEnabled(false);
			}
			routesPanel.add(routesCheckBox[i]);
		}
		delRoutePanel.add(routesPanel);
		delRoutePanel.setBorder(BorderFactory.createTitledBorder("��·ɾ��"));
	}
	
	//��Ӧɾ����ť
	public void deleteRoutes(){
		//·��id�ļ���
		routeIdList = new ArrayList<Integer>();
		//���ȵõ����б�ѡ�е�·��
		for(int i=0;i<routesCheckBox.length;i++){
			if(routesCheckBox[i].isSelected()){//�����ѡ��
				int id = Integer.parseInt((routesCheckBox[i].getText().split("-"))[0]);
				routeIdList.add(id);
			}
		}
		if(routeIdList.size() == 0){
			JOptionPane.showMessageDialog(this, "��ѡ��Ҫɾ������·");
		}
		//ɾ��·��
		RouteService.deleteSelectedRoutes(routeIdList);
		JOptionPane.showMessageDialog(this, "��ѡ·����ɾ��");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getActionCommand().equals("delete")){
			this.deleteRoutes();
		}else if(e.getActionCommand().equals("finish")){
			//�����ɣ��򽫵�ǰ���ڹر�
			this.dispose();
			CRHSimulation owner = (CRHSimulation) this.getOwner();
			owner.deleteRouteItemFromMenu(routeIdList);
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		DeleteRouteFrame deleteRouteFrame = new DeleteRouteFrame(null,false,3);
	}

}
