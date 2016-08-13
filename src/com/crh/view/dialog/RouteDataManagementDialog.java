package com.crh.view.dialog;

import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import com.crh.service.RouteDataManagementDialogService;
import com.crh2.javabean.TrainRouteName;
import com.crh2.util.MyUtillity;

/**
 * ��·�����Dialog
 * @author huhui
 *
 */
public class RouteDataManagementDialog extends JDialog {

	public RouteDataManagementDialog(Frame owner) {
		super(owner, true);
		setTitle("��·����");
		setBounds(100, 100, 426, 397);
		getContentPane().setLayout(null);

		JLabel label = new JLabel("��·���ݹ���");
		label.setFont(new Font("����", Font.PLAIN, 16));
		label.setBounds(160, 10, 100, 16);
		getContentPane().add(label);

		JLabel label_1 = new JLabel("��·����");
		label_1.setBounds(10, 45, 54, 15);
		getContentPane().add(label_1);

		RouteNameListPanel routeNameListPanel = new RouteNameListPanel();
		getContentPane().add(routeNameListPanel);

		JButton button = new JButton("������·");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//������·
				RouteDataManagementInputRouteNameDialog rdmirnd = new RouteDataManagementInputRouteNameDialog(RouteDataManagementDialog.this);
			}
		});
		button.setBounds(295, 95, 93, 23);
		getContentPane().add(button);

		JButton button_1 = new JButton("ɾ����·");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//ɾ����·
				if(isSelectedElement()){
					int option = JOptionPane.showConfirmDialog(RouteDataManagementDialog.this, "ȷ��ɾ����");
					if(option == 0){
						String routeName = RouteNameListPanel.getSelectedElement();
						if(RouteDataManagementDialogService.deleteRoute(routeName)){
							RouteNameListPanel.removeListElement(RouteNameListPanel.getSelectedElement());
							JOptionPane.showMessageDialog(RouteDataManagementDialog.this, "ɾ����·�ɹ���");
						}
					}
				}
			}
		});
		button_1.setBounds(295, 138, 93, 23);
		getContentPane().add(button_1);

		JButton button_2 = new JButton("�༭��·");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (isSelectedElement()) {
					String routeName = RouteNameListPanel.getSelectedElement();
					if(RouteDataManagementDialogService.hasImported(routeName)){//�ѵ���
						RouteDataEditDialog routeDataEditDialog = new RouteDataEditDialog(RouteDataManagementDialog.this, routeName);
					}else{//��Ҫ���µ���
						int option = JOptionPane.showConfirmDialog(RouteDataManagementDialog.this, "��ǰ��·δ�������ݣ��Ƿ��룿");
						if(option == 0){
							ImportRouteDataFromExcelDialog importRouteDataFromExcelDialog = new ImportRouteDataFromExcelDialog(routeName, RouteDataManagementDialog.this);
						}
					}
					
				}
			}
		});
		button_2.setBounds(295, 181, 93, 23);
		getContentPane().add(button_2);

		JButton button_3 = new JButton("��������");
		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (isSelectedElement()) {
					String routeName = RouteNameListPanel.getSelectedElement();
					RouteDataTrainNumDialog routeDataTrainNumDialog = new RouteDataTrainNumDialog(RouteDataManagementDialog.this, routeName);
				}
			}
		});
		button_3.setBounds(295, 224, 93, 23);
		getContentPane().add(button_3);
		
		MyUtillity.setFrameOnCenter(this);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setVisible(true);

	}
	
	/**
	 * �ж��Ƿ�ѡ����JList�е�����
	 * @return
	 */
	public boolean isSelectedElement(){
		String selectedString = RouteNameListPanel.getSelectedElement();
		if(selectedString == null){
			JOptionPane.showMessageDialog(RouteDataManagementDialog.this, "��ѡ����·����");
			return false;
		}else{
			return true;
		}
	}
	
}

/**
 *  ��·���Ƶ�list��panel
 * @author huhui
 *
 */
class RouteNameListPanel extends JPanel {
	private JScrollPane listScrollPane;
	private static JList<String> routeNameList;

	public RouteNameListPanel() {
		this.setLayout(new GridLayout(1, 1, 0, 0));
		this.initRouteNameList();
		this.add(listScrollPane);
		this.setBounds(10, 66, 263, 283);
		this.setVisible(true);
	}

	/**
	 *  ��ʼ��JList
	 */
	public void initRouteNameList() {
		//��ʼ��routeNameList
		routeNameList = new JList<String>(new DefaultListModel<String>());
		routeNameList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);//���õ�ѡ
		routeNameList.setFont(new Font("����", Font.PLAIN, 12));
		listScrollPane = new JScrollPane(routeNameList);
		ArrayList<TrainRouteName> routeList = RouteDataManagementDialogService.getTrainRouteName();
		if(routeList.size() != 0){
			for(TrainRouteName route : routeList){
				addListElement(route.getRouteName());
			}
		}
	}
	
	/**
	 * ��JList���������
	 * @param routeName
	 */
	public static void addListElement(String routeName){
		DefaultListModel<String> listModel = (DefaultListModel<String>) routeNameList.getModel();
		listModel.addElement(routeName);
		routeNameList.setModel(listModel);
	}
	
	/**
	 * ɾ��JList�е�����
	 * @param routeName
	 */
	public static void removeListElement(String routeName){
		DefaultListModel<String> listModel = (DefaultListModel<String>)routeNameList.getModel();
		listModel.removeElement(routeName);
		routeNameList.setModel(listModel);
	}
	
	/**
	 * ��õ�ǰJList��ѡ�е�����
	 * @return
	 */
	public static String getSelectedElement(){
		return routeNameList.getSelectedValue();
	}

}
