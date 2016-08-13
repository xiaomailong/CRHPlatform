/**
 * 这个类表示动车组
 */
package com.crh2.view;

public class Train {
	private double x;// x坐标
	private double y;// y坐标

	public Train(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

}
