/**
 * 修改用户密码的界面
 */
package com.crh2.frame;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.crh2.dao.SQLHelper;
import com.crh2.util.MyUtillity;

public class ModifyPasswordFrame extends JFrame implements ActionListener {

	/**
	 * @param args
	 */
	// 定义label
	private JLabel passwordLabel, passwordConfirmLabel;
	private JPasswordField passwordText, passwordConfirmText;
	private JButton confirmButton, cancelButton;

	// 构造函数，初始化各个控件及窗口
	public ModifyPasswordFrame() {
		// 初始化label
		passwordLabel = new JLabel("新 密 码");
		passwordConfirmLabel = new JLabel("密码确认");
		// 初始化textfield
		passwordText = new JPasswordField();
		passwordConfirmText = new JPasswordField();
		// 初始化button
		confirmButton = new JButton("确定");
		cancelButton = new JButton("取消");
		// 为按钮添加事件响应
		confirmButton.addActionListener(this);
		confirmButton.setActionCommand("confirm");
		cancelButton.addActionListener(this);
		cancelButton.setActionCommand("cancel");

		// 设置布局
		this.setLayout(new GridLayout(3, 2));
		// 设置frame为top
		this.setAlwaysOnTop(true);
		// 将控件加入到frame中
		this.add(passwordLabel);
		this.add(passwordText);
		this.add(passwordConfirmLabel);
		this.add(passwordConfirmText);
		this.add(confirmButton);
		this.add(cancelButton);
		this.setSize(240, 120);
		this.setTitle("修改密码");
		// 设置frame居中
		MyUtillity.setFrameOnCenter(this);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);
		this.setResizable(false);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ModifyPasswordFrame modifyPasswordFrame = new ModifyPasswordFrame();
	}

	// 修改数据库中的admin密码
	public boolean modifyPassword(String newPassword) {
		SQLHelper sqlHelper = new SQLHelper();
		// 组织sql语句
		String sql = "UPDATE users SET PASSWORD = '" + newPassword
				+ "' WHERE NAME = 'admin';";
		return sqlHelper.update(sql, null);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getActionCommand().equals("confirm")) {
			String passwordStr = new String(passwordText.getPassword());
			String passwordConfirmStr = new String(passwordConfirmText
					.getPassword());
			// 如果密码相同则修改
			if (passwordStr.equals(passwordConfirmStr)) {
				if (this.modifyPassword(passwordStr)) {
					JOptionPane.showMessageDialog(this, "密码修改成功");
					this.dispose();
				}
			} else {
				JOptionPane.showMessageDialog(this, "密码输入不一致，请重新输入");
				// 清空密码框
				passwordText.setText("");
				passwordConfirmText.setText("");
			}
		} else if (e.getActionCommand().equals("cancel")) {
			this.dispose();
		}
	}

}
