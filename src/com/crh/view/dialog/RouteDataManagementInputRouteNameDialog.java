package com.crh.view.dialog;

import java.awt.Dialog;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import com.crh.service.RouteDataManagementDialogService;
import com.crh2.util.MyUtillity;

/**
 * “增加线路名称”对话框
 * @author huhui
 *
 */
public class RouteDataManagementInputRouteNameDialog extends JDialog {
	/**
	 * “线路名称”的文本框
	 */
	private JTextField textField;

	public RouteDataManagementInputRouteNameDialog(Dialog owner) {
		super(owner,true);
		setSize(450, 300);
		MyUtillity.setFrameOnCenter(this);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(new GridLayout(1, 1, 0, 0));
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "增加线路", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel label = new JLabel("线路名称");
		label.setBounds(67, 79, 69, 15);
		panel.add(label);
		
		textField = new JTextField();
		textField.setBounds(145, 76, 205, 21);
		panel.add(textField);
		textField.setColumns(10);
		
		JButton button = new JButton("确定");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(isBlank()){//内容为空
					JOptionPane.showMessageDialog(RouteDataManagementInputRouteNameDialog.this, "路线名不能为空！");
				}else{
					String newRouteName = textField.getText().trim();
					//更新数据库
					try {
						if(RouteDataManagementDialogService.saveRouteName(newRouteName)){
							//更新JList
							RouteNameListPanel.addListElement(newRouteName);
							JOptionPane.showMessageDialog(RouteDataManagementInputRouteNameDialog.this, "添加线路成功！");
						}else{
							JOptionPane.showMessageDialog(RouteDataManagementInputRouteNameDialog.this, "添加线路失败！");
						}
						//关闭Dialog
						dispose();
					} catch (SQLException e) {
						JOptionPane.showMessageDialog(RouteDataManagementInputRouteNameDialog.this, e.getMessage());
					}
				}
			}
		});
		button.setBounds(82, 147, 93, 23);
		panel.add(button);
		
		JButton button_1 = new JButton("取消");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		button_1.setBounds(243, 147, 93, 23);
		panel.add(button_1);
		setVisible(true);
	}
	
	/**
	 * 判断线路名称是否为空
	 * @return
	 */
	public boolean isBlank(){
		if(textField.getText().trim().equals("")){
			return true;//内容为空
		}else{
			return false;//内容不为空
		}
	}
}
