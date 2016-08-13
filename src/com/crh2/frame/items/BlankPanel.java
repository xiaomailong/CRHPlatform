/**
 * 除了占满EAST方向的空间外，还要提供三个按钮:开始，暂停和停止
 */
package com.crh2.frame.items;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class BlankPanel extends JPanel implements ActionListener {
	
	private JLabel labels [] = new JLabel[50];//用于占空间
	private JButton start,pause,stop;
	private JRadioButton one,two,three;//分别代表100ms，40ms和10ms
	private ButtonGroup speedButtonGroup;
	
	public BlankPanel(){
		this.setLayout(new GridLayout(6, 13));
		Font font = new Font("宋体", Font.BOLD, 18);
		//初始化按钮
		start = new JButton("开始");
		pause = new JButton("暂停");
		stop = new JButton("退出");
		start.setFont(font);
		pause.setFont(font);
		stop.setFont(font);
		
		//占位
		for(int i=0;i<3;i++){
			labels[i] = new JLabel("1111");
			this.add(labels[i]);
			labels[i].setVisible(false);
		}
		
		this.add(start);
		this.add(pause);
		this.add(stop);
		
		//占位
		for(int i=0;i<3;i++){
			labels[i] = new JLabel("1111");
			this.add(labels[i]);
			labels[i].setVisible(false);
		}
		
		//注册监听
		start.addActionListener(this);
		start.setActionCommand("start");
		pause.addActionListener(this);
		pause.setActionCommand("pause");
		stop.addActionListener(this);
		stop.setActionCommand("stop");
		
		//初始化单选框
		one = new JRadioButton("×1");
		two = new JRadioButton("×2");
		two.setSelected(true);
		three = new JRadioButton("×3");
		one.setFont(font);
		two.setFont(font);
		three.setFont(font);
		//注册监听
		one.addActionListener(this);
		one.setActionCommand("one");
		two.addActionListener(this);
		two.setActionCommand("two");
		three.addActionListener(this);
		three.setActionCommand("three");
		
		//占位
		for(int i=0;i<3;i++){
			labels[i] = new JLabel("1111");
			this.add(labels[i]);
			labels[i].setVisible(false);
		}
		this.add(one);
		this.add(two);
		this.add(three);
		
		//创建ButtonGroup
		speedButtonGroup = new ButtonGroup();
		speedButtonGroup.add(one);
		speedButtonGroup.add(two);
		speedButtonGroup.add(three);
		
		for(int i=13;i<labels.length;i++){
			labels[i] = new JLabel("1111");
			this.add(labels[i]);
			labels[i].setVisible(false);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		//判断是哪个按钮被选中
		if(e.getActionCommand().equals("start")){
			CRHPanel.isPause = false;
		}else if(e.getActionCommand().equals("pause")){
			CRHPanel.isPause = true;
		}else if(e.getActionCommand().equals("stop")){
			System.exit(0);
		}else if(e.getActionCommand().equals("one")){
			CRHPanel.sleepTime = 20;
		}else if(e.getActionCommand().equals("two")){
			CRHPanel.sleepTime = 10;
		}else if(e.getActionCommand().equals("three")){
			CRHPanel.sleepTime = 2;
		}
	}
}
