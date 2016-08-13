package com.crh.view.dialog;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.Timer;

import com.crh.doc.CreateDocThread;
import com.crh2.util.MyUtillity;

/**
 * 2014.11.30 �ĵ����ɵĵȴ�������
 * @author huhui
 *
 */
public class DocInvokeAndWaitDialog extends JDialog {
	
	/**
	 *  ����һ����ֱ������
	 */
	private JProgressBar bar = new JProgressBar();
	private Timer timer = null;

	public DocInvokeAndWaitDialog(CreateDocThread createDocThread) {
		setLayout(new FlowLayout());
		// �ѽ�������ӵ�JFrame������
		add(bar);
		bar.setString("�ĵ������У����Ե�...");
		bar.setStringPainted(true);
		bar.setIndeterminate(true);
		final CreateDocThread target = createDocThread;
		
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
