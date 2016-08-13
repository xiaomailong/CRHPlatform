package com.crh.test;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.Timer;
import javax.swing.UIManager;

import com.crh2.util.MyUtillity;

public class InvokeAndWait {
	
	private JFrame frame = new JFrame();
	// ����һ����ֱ������
	private JProgressBar bar = new JProgressBar();
	private Timer timer = null;

	public InvokeAndWait(SimulatedTarget simulatedTarget) {
		frame.setLayout(new FlowLayout());
		// �ѽ�������ӵ�JFrame������
		frame.add(bar);
		bar.setString("�����У����Ե�...");
		bar.setStringPainted(true);
		bar.setIndeterminate(true);
		final SimulatedTarget target = simulatedTarget;
		
		// ������һ���̵߳ķ�ʽ��ִ��һ����ʱ������
		Thread thread = new Thread(target);
		thread.start();
		// ���ý����������ֵ����Сֵ,
		bar.setMinimum(0);
		// ������������Ϊ�����������ֵ
		bar.setMaximum(target.getAmount());
		timer = new Timer(100, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// ������ĵ�ǰ��������ý�������value
				bar.setValue(target.getCurrent());
				if (bar.getValue() == target.getAmount()) {
					System.out.println("���");
					frame.dispose();
					timer.stop();
				}
			}
		});

		timer.start();
		frame.setAlwaysOnTop(true);
		frame.setUndecorated(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		MyUtillity.setFrameOnCenter(frame);
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		SimulatedTarget target = new SimulatedTarget();
		new InvokeAndWait(target);
	}
}
