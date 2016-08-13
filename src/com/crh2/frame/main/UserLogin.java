/**
 * 功能：用户登录界面
 * @author HuHui
 * @create time 2013.6.12
 */
package com.crh2.frame.main;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;

import com.crh2.calculate.TrainAttribute;
import com.crh2.frame.CRHSimulation;
import com.crh2.javabean.Users;
import com.crh2.service.UserService;
import com.crh2.util.MyUtillity;

public class UserLogin extends JFrame implements ActionListener, KeyListener {
	private JPanel topPanel,tailPanel;
	private JLabel userNameLabel,passwordLabel;
	private JTextField userNameField;
	private JPasswordField passwordField;
	private JButton okButton,exitButton;
	
	// service
	UserService userService = new UserService();

	// 主函数
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		new UserLogin();
	}

	public UserLogin() {
		//初始化topPanel
		topPanel = new JPanel(new GridLayout(2, 2, 9, 13));
		userNameLabel = new JLabel("     用户名：");
		userNameField = new JTextField("admin");
		passwordLabel = new JLabel("     密  码：");
		passwordField = new JPasswordField();
		//将控件放入panel
		topPanel.add(userNameLabel);
		topPanel.add(userNameField);
		topPanel.add(passwordLabel);
		topPanel.add(passwordField);
		// 给密码框设置监听，当按下ENTER时登录
		passwordField.addKeyListener(this);
		
		//初始化tailPanel
		tailPanel = new JPanel();
		okButton = new JButton("登录");
		okButton.addActionListener(this);
		exitButton = new JButton("取消");
		exitButton.addActionListener(this);
		
		tailPanel.add(okButton);
		tailPanel.add(exitButton);

		this.add(topPanel,BorderLayout.NORTH);
		this.add(tailPanel,BorderLayout.SOUTH);
		
		this.setSize(300, 130);
		// 将JFrame居中显示
		MyUtillity.setFrameOnCenter(this);
		this.setTitle("系统登录");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		passwordField.requestFocus();//密码框获得焦点，放在show()或者setVisible()后面才有效
		this.setResizable(false);
	}

	// 处理鼠标事件
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("登录")) {
			Users user = new Users();
			user.setName(userNameField.getText().trim());
			user.setPassword(new String(passwordField.getPassword()));
			if (userService.checkUsers(user)) {
				this.dispose();
				//初始化界面，默认选择routeid为1的线路，即京津线（下行）
				CRHSimulation crhSimulation = new CRHSimulation(TrainAttribute.CRH_DEFAULT_ROUTE_ID);
			} else {
				JOptionPane.showMessageDialog(this, "登录失败！用户名或密码错误");
			}
		} else if (e.getActionCommand().equals("取消")) {
			System.exit(0);
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		int keyCode = e.getKeyCode();
		if (keyCode == KeyEvent.VK_ENTER) {
			okButton.doClick();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}
}
