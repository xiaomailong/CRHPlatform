/**
 * ��ʾ���
 */
package com.crh2.view;


public class RailWay {
	private int i;//���
	private double x;// x����
	private double y;// y����
	private double speed = 0;// �ٶ�
	private Content content = null;// ����
	private boolean b = true;//�ж��Ƿ��˶�


	// ���캯��
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
		this.speed = speed;//�����ٶ�
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
