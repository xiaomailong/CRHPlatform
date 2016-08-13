/**
 * ���ܣ��û���¼����
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

	// ������
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
		//��ʼ��topPanel
		topPanel = new JPanel(new GridLayout(2, 2, 9, 13));
		userNameLabel = new JLabel("     �û�����");
		userNameField = new JTextField("admin");
		passwordLabel = new JLabel("     ��  �룺");
		passwordField = new JPasswordField();
		//���ؼ�����panel
		topPanel.add(userNameLabel);
		topPanel.add(userNameField);
		topPanel.add(passwordLabel);
		topPanel.add(passwordField);
		// ����������ü�����������ENTERʱ��¼
		passwordField.addKeyListener(this);
		
		//��ʼ��tailPanel
		tailPanel = new JPanel();
		okButton = new JButton("��¼");
		okButton.addActionListener(this);
		exitButton = new JButton("ȡ��");
		exitButton.addActionListener(this);
		
		tailPanel.add(okButton);
		tailPanel.add(exitButton);

		this.add(topPanel,BorderLayout.NORTH);
		this.add(tailPanel,BorderLayout.SOUTH);
		
		this.setSize(300, 130);
		// ��JFrame������ʾ
		MyUtillity.setFrameOnCenter(this);
		this.setTitle("ϵͳ��¼");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		passwordField.requestFocus();//������ý��㣬����show()����setVisible()�������Ч
		this.setResizable(false);
	}

	// ��������¼�
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("��¼")) {
			Users user = new Users();
			user.setName(userNameField.getText().trim());
			user.setPassword(new String(passwordField.getPassword()));
			if (userService.checkUsers(user)) {
				this.dispose();
				//��ʼ�����棬Ĭ��ѡ��routeidΪ1����·���������ߣ����У�
				CRHSimulation crhSimulation = new CRHSimulation(TrainAttribute.CRH_DEFAULT_ROUTE_ID);
			} else {
				JOptionPane.showMessageDialog(this, "��¼ʧ�ܣ��û������������");
			}
		} else if (e.getActionCommand().equals("ȡ��")) {
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
