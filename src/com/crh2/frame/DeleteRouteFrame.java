/**
 * 删除路线的frame
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
	
	//定义变量
	private JPanel delRoutePanel,routesPanel,buttonPanel;
	private JCheckBox [] routesCheckBox = null;//路线多选框
	private JButton deleteButon,finishButton;
	private List<RouteName> routeNameList = null;
	private int currentRouteId = 0;//目前正在显示的路线
	ArrayList<Integer> routeIdList = null;
	
	//构造函数
	public DeleteRouteFrame(Frame owner,boolean modal,int currentRouteId){
		super(owner, modal);
		//初始化数据
		routeNameList = RouteService.getAllRouteName();
		this.currentRouteId = currentRouteId;
		//初始化多选框panel
		this.initRoutesCheckBox();
		//初始化“完成”panel
		this.initFinishButton();
		
		//设置JDialog
		this.setTitle("线路删除");
		this.add(delRoutePanel,BorderLayout.CENTER);
		this.add(buttonPanel,BorderLayout.SOUTH);
		this.setSize(330, 400);
		MyUtillity.setFrameOnCenter(this);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setResizable(false);
		this.setVisible(true);
	}
	
	//初始化按钮panel
	public void initFinishButton(){
		//初始化“删除”按钮
		deleteButon = new JButton("删除");
		deleteButon.addActionListener(this);
		deleteButon.setActionCommand("delete");
		//初始化“完成”按钮
		finishButton = new JButton("完成");
		finishButton.addActionListener(this);
		finishButton.setActionCommand("finish");
		buttonPanel = new JPanel();
		buttonPanel.add(deleteButon);
		buttonPanel.add(finishButton);
	}
	
	//初始化routesCheckBox
	public void initRoutesCheckBox(){
		delRoutePanel = new JPanel();
		routesPanel = new JPanel(new GridLayout(routeNameList.size(), 1));//初始化routespanel
		routesCheckBox = new JCheckBox[routeNameList.size()];//初始化复选框
		for(int i=0;i<routesCheckBox.length;i++){
			RouteName route = routeNameList.get(i);
			routesCheckBox[i] = new JCheckBox(route.getId()+"-"+route.getName());
			//如果是当前显示的route id，则不能删除
			if(route.getId() == currentRouteId){
				routesCheckBox[i].setEnabled(false);
			}
			routesPanel.add(routesCheckBox[i]);
		}
		delRoutePanel.add(routesPanel);
		delRoutePanel.setBorder(BorderFactory.createTitledBorder("线路删除"));
	}
	
	//响应删除按钮
	public void deleteRoutes(){
		//路线id的集合
		routeIdList = new ArrayList<Integer>();
		//首先得到所有被选中的路线
		for(int i=0;i<routesCheckBox.length;i++){
			if(routesCheckBox[i].isSelected()){//如果被选中
				int id = Integer.parseInt((routesCheckBox[i].getText().split("-"))[0]);
				routeIdList.add(id);
			}
		}
		if(routeIdList.size() == 0){
			JOptionPane.showMessageDialog(this, "请选择要删除的线路");
		}
		//删除路线
		RouteService.deleteSelectedRoutes(routeIdList);
		JOptionPane.showMessageDialog(this, "所选路线已删除");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getActionCommand().equals("delete")){
			this.deleteRoutes();
		}else if(e.getActionCommand().equals("finish")){
			//点击完成，则将当前窗口关闭
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
