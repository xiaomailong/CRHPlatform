// ģ��һ����ʱ������
package com.crh.test;

public class SimulatedTarget implements Runnable {
	// ����ĵ�ǰ�����
	protected volatile int current = 0;
	// ��������
	protected int amount = 100;

	public int getAmount() {
		return amount;
	}

	public int getCurrent() {
		return current;
	}

	// run�����������������Ĺ���
	public void run() {
		long sum = 0;
		for (int i = 0; i < Integer.MAX_VALUE / 1000; i++) {
			sum += i;
			System.out.println(sum);
		}
		current = amount;
	}
}
