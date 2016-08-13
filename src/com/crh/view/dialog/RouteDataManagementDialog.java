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
 * 线路管理的Dialog
 * @author huhui
 *
 */
public class RouteDataManagementDialog extends JDialog {

	public RouteDataManagementDialog(Frame owner) {
		super(owner, true);
		setTitle("线路管理");
		setBounds(100, 100, 426, 397);
		getContentPane().setLayout(null);

		JLabel label = new JLabel("线路数据管理");
		label.setFont(new Font("宋体", Font.PLAIN, 16));
		label.setBounds(160, 10, 100, 16);
		getContentPane().add(label);

		JLabel label_1 = new JLabel("线路名称");
		label_1.setBounds(10, 45, 54, 15);
		getContentPane().add(label_1);

		RouteNameListPanel routeNameListPanel = new RouteNameListPanel();
		getContentPane().add(routeNameListPanel);

		JButton button = new JButton("增加线路");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//增加线路
				RouteDataManagementInputRouteNameDialog rdmirnd = new RouteDataManagementInputRouteNameDialog(RouteDataManagementDialog.this);
			}
		});
		button.setBounds(295, 95, 93, 23);
		getContentPane().add(button);

		JButton button_1 = new JButton("删除线路");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//删除线路
				if(isSelectedElement()){
					int option = JOptionPane.showConfirmDialog(RouteDataManagementDialog.this, "确认删除？");
					if(option == 0){
						String routeName = RouteNameListPanel.getSelectedElement();
						if(RouteDataManagementDialogService.deleteRoute(routeName)){
							RouteNameListPanel.removeListElement(RouteNameListPanel.getSelectedElement());
							JOptionPane.showMessageDialog(RouteDataManagementDialog.this, "删除线路成功！");
						}
					}
				}
			}
		});
		button_1.setBounds(295, 138, 93, 23);
		getContentPane().add(button_1);

		JButton button_2 = new JButton("编辑线路");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (isSelectedElement()) {
					String routeName = RouteNameListPanel.getSelectedElement();
					if(RouteDataManagementDialogService.hasImported(routeName)){//已导入
						RouteDataEditDialog routeDataEditDialog = new RouteDataEditDialog(RouteDataManagementDialog.this, routeName);
					}else{//需要重新导入
						int option = JOptionPane.showConfirmDialog(RouteDataManagementDialog.this, "当前线路未导入数据，是否导入？");
						if(option == 0){
							ImportRouteDataFromExcelDialog importRouteDataFromExcelDialog = new ImportRouteDataFromExcelDialog(routeName, RouteDataManagementDialog.this);
						}
					}
					
				}
			}
		});
		button_2.setBounds(295, 181, 93, 23);
		getContentPane().add(button_2);

		JButton button_3 = new JButton("车次设置");
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
	 * 判断是否选择了JList中的内容
	 * @return
	 */
	public boolean isSelectedElement(){
		String selectedString = RouteNameListPanel.getSelectedElement();
		if(selectedString == null){
			JOptionPane.showMessageDialog(RouteDataManagementDialog.this, "请选择线路名！");
			return false;
		}else{
			return true;
		}
	}
	
}

/**
 *  线路名称的list的panel
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
	 *  初始化JList
	 */
	public void initRouteNameList() {
		//初始化routeNameList
		routeNameList = new JList<String>(new DefaultListModel<String>());
		routeNameList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);//设置单选
		routeNameList.setFont(new Font("宋体", Font.PLAIN, 12));
		listScrollPane = new JScrollPane(routeNameList);
		ArrayList<TrainRouteName> routeList = RouteDataManagementDialogService.getTrainRouteName();
		if(routeList.size() != 0){
			for(TrainRouteName route : routeList){
				addListElement(route.getRouteName());
			}
		}
	}
	
	/**
	 * 向JList中添加内容
	 * @param routeName
	 */
	public static void addListElement(String routeName){
		DefaultListModel<String> listModel = (DefaultListModel<String>) routeNameList.getModel();
		listModel.addElement(routeName);
		routeNameList.setModel(listModel);
	}
	
	/**
	 * 删除JList中的内容
	 * @param routeName
	 */
	public static void removeListElement(String routeName){
		DefaultListModel<String> listModel = (DefaultListModel<String>)routeNameList.getModel();
		listModel.removeElement(routeName);
		routeNameList.setModel(listModel);
	}
	
	/**
	 * 获得当前JList被选中的内容
	 * @return
	 */
	public static String getSelectedElement(){
		return routeNameList.getSelectedValue();
	}

}
