package com.crh2.view;

public class Content {
	private double x;
	private double y;
	private String str;
	
	public Content(double x,double y,String str){
		this.x = x;
		this.y = y;
		this.str = str;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}
	
}
