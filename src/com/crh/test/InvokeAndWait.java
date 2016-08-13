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
	// 创建一条垂直进度条
	private JProgressBar bar = new JProgressBar();
	private Timer timer = null;

	public InvokeAndWait(SimulatedTarget simulatedTarget) {
		frame.setLayout(new FlowLayout());
		// 把进度条添加到JFrame窗口中
		frame.add(bar);
		bar.setString("操作中，请稍等...");
		bar.setStringPainted(true);
		bar.setIndeterminate(true);
		final SimulatedTarget target = simulatedTarget;
		
		// 以启动一条线程的方式来执行一个耗时的任务
		Thread thread = new Thread(target);
		thread.start();
		// 设置进度条的最大值和最小值,
		bar.setMinimum(0);
		// 以总任务量作为进度条的最大值
		bar.setMaximum(target.getAmount());
		timer = new Timer(100, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 以任务的当前完成量设置进度条的value
				bar.setValue(target.getCurrent());
				if (bar.getValue() == target.getAmount()) {
					System.out.println("完成");
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
