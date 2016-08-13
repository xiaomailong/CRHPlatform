package com.crh.test;


public class TargetTest extends SimulatedTarget {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TargetTest target = new TargetTest();
		new InvokeAndWait(target);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		long sum = 0;
		for (int i = 0; i < Integer.MAX_VALUE / 100000; i++) {
			sum += i;
			System.out.println("sum="+sum);
		}
		current = amount;
	}
	
}
