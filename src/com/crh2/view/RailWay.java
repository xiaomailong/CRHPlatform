/**
 * 表示轨道
 */
package com.crh2.view;


public class RailWay {
	private int i;//序号
	private double x;// x坐标
	private double y;// y坐标
	private double speed = 0;// 速度
	private Content content = null;// 内容
	private boolean b = true;//判断是否运动


	// 构造函数
	public RailWay(double x, double y,String str) {
		this.x = x;
		this.y = y;
		this.content = new Content(x, y, str);
	}
	
	public Content getContent(){
		return this.content;
	}
	
	public void setContentStr(String str){
		this.content.setStr(str);
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

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;//设置速度
	}

	public void run() {
		// TODO Auto-generated method stub
		if(b){
			double contentX = content.getX();
			x -= speed;
			contentX -= speed;
			if (x <= 0) {
				b = false;
			}
		}
	}

}
