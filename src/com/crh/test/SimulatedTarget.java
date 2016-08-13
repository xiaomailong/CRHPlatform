// 模拟一个耗时的任务
package com.crh.test;

public class SimulatedTarget implements Runnable {
	// 任务的当前完成量
	protected volatile int current = 0;
	// 总任务量
	protected int amount = 100;

	public int getAmount() {
		return amount;
	}

	public int getCurrent() {
		return current;
	}

	// run方法代表不断完成任务的过程
	public void run() {
		long sum = 0;
		for (int i = 0; i < Integer.MAX_VALUE / 1000; i++) {
			sum += i;
			System.out.println(sum);
		}
		current = amount;
	}
}
