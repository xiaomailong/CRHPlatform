package com.crh.view.dialog;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.Timer;

import com.crh.calculation.mintime.RunDataThread;
import com.crh2.util.MyUtillity;

/**
 * 等待进度条
 * @author huhui
 *
 */
public class CalculatingInvokeAndWaitDialog extends JDialog {
	
	/**
	 *  创建一条垂直进度条
	 */
	private JProgressBar bar = new JProgressBar();
	/**
	 * 定时器
	 */
	private Timer timer = null;

	/**
	 * 创建进度条，并开始执行任务，显示任务执行进度
	 * @param runDataThread
	 */
	public CalculatingInvokeAndWaitDialog(RunDataThread runDataThread) {
		setLayout(new FlowLayout());
		// 把进度条添加到JFrame窗口中
		add(bar);
		bar.setString("计算中，请稍等...");
		bar.setStringPainted(true);
		bar.setIndeterminate(true);
		final RunDataThread target = runDataThread;
		
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
					dispose();
					timer.stop();
				}
			}
		});

		timer.start();
		setAlwaysOnTop(true);
		setModal(true);
		setUndecorated(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		pack();
		MyUtillity.setFrameOnCenter(this);
		setVisible(true);
	}

}
