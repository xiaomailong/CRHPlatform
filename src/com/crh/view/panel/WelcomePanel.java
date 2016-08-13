package com.crh.view.panel;

import java.awt.GridLayout;
import java.awt.Panel;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * 程序启动欢迎界面
 * @author huhui
 *
 */
public class WelcomePanel extends JPanel {
	
	
	public WelcomePanel() {
		setLayout(new GridLayout(1, 1, 0, 0));
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(Panel.class.getResource("/images/welcome.jpg")));
		add(lblNewLabel);
	}

}
