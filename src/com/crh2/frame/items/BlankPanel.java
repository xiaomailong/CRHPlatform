/**
 * ����ռ��EAST����Ŀռ��⣬��Ҫ�ṩ������ť:��ʼ����ͣ��ֹͣ
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
	
	private JLabel labels [] = new JLabel[50];//����ռ�ռ�
	private JButton start,pause,stop;
	private JRadioButton one,two,three;//�ֱ����100ms��40ms��10ms
	private ButtonGroup speedButtonGroup;
	
	public BlankPanel(){
		this.setLayout(new GridLayout(6, 13));
		Font font = new Font("����", Font.BOLD, 18);
		//��ʼ����ť
		start = new JButton("��ʼ");
		pause = new JButton("��ͣ");
		stop = new JButton("�˳�");
		start.setFont(font);
		pause.setFont(font);
		stop.setFont(font);
		
		//ռλ
		for(int i=0;i<3;i++){
			labels[i] = new JLabel("1111");
			this.add(labels[i]);
			labels[i].setVisible(false);
		}
		
		this.add(start);
		this.add(pause);
		this.add(stop);
		
		//ռλ
		for(int i=0;i<3;i++){
			labels[i] = new JLabel("1111");
			this.add(labels[i]);
			labels[i].setVisible(false);
		}
		
		//ע�����
		start.addActionListener(this);
		start.setActionCommand("start");
		pause.addActionListener(this);
		pause.setActionCommand("pause");
		stop.addActionListener(this);
		stop.setActionCommand("stop");
		
		//��ʼ����ѡ��
		one = new JRadioButton("��1");
		two = new JRadioButton("��2");
		two.setSelected(true);
		three = new JRadioButton("��3");
		one.setFont(font);
		two.setFont(font);
		three.setFont(font);
		//ע�����
		one.addActionListener(this);
		one.setActionCommand("one");
		two.addActionListener(this);
		two.setActionCommand("two");
		three.addActionListener(this);
		three.setActionCommand("three");
		
		//ռλ
		for(int i=0;i<3;i++){
			labels[i] = new JLabel("1111");
			this.add(labels[i]);
			labels[i].setVisible(false);
		}
		this.add(one);
		this.add(two);
		this.add(three);
		
		//����ButtonGroup
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
		//�ж����ĸ���ť��ѡ��
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
