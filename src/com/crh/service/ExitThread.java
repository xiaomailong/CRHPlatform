package com.crh.service;

import javax.swing.JOptionPane;

public class ExitThread implements Runnable {
	
	private int sleepTime = 1000 * 60 * 10;

	@Override
	public void run() {
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
//		JOptionPane.showMessageDialog(null, "\u8F6F\u4EF6\u8BD5\u7528\u671F\u9650\u5DF2\u5230\uFF0C\u8BF7\u8D2D\u4E70\u7248\u6743\uFF01");
		System.exit(0);
	}

}
