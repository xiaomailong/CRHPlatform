package com.crh2.test;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import com.crh2.util.MyUtillity;

public class ListenerTest extends JFrame {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		ListenerTest listenerTest = new ListenerTest();
		MyUtillity.getNumFromString("reoute123");
	}
	
	public void getActionCommand(JButton button){
		System.out.println(button.getText());
	}

	public ListenerTest() {
		this.setLayout(new GridLayout(2, 2));
		for (int i = 0; i < 4; i++) {
			final JButton button = new JButton("°´Å¥" + i);
			button.setActionCommand("button"+i);
			this.add(button);
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					getActionCommand(button);
					System.out.println("±»µã»÷");
				}
			});
		}
		this.setSize(430, 300);
		MyUtillity.setFrameOnCenter(this);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setVisible(true);
	}

}
