package com.crh2.javabean;

/**
 * �������߹�ʽ��JavaBean
 * @author huhui
 *
 */
public class CurveFormulaBean {
	// ǣ�����Բ���
	private double v;
	private double tractionForce;
	private double airFriction;
	private double slope;
	private double dryRail;
	private double wetRail;
	// �������ܲ���
	private double a;// ���ٶ�
	private double Vt;// �ٶ�
	private double St;// λ��
	private int t;// ʱ��

	public double getV() {
		return v;
	}

	public void setV(double v) {
		this.v = v;
	}

	public double getTractionForce() {
		return tractionForce;
	}

	public void setTractionForce(double tractionForce) {
		this.tractionForce = tractionForce;
	}

	public double getAirFriction() {
		return airFriction;
	}

	public void setAirFriction(double airFriction) {
		this.airFriction = airFriction;
	}

	public double getSlope() {
		return slope;
	}

	public void setSlope(double slope) {
		this.slope = slope;
	}

	public double getDryRail() {
		return dryRail;
	}

	public void setDryRail(double dryRail) {
		this.dryRail = dryRail;
	}

	public double getWetRail() {
		return wetRail;
	}

	public void setWetRail(double wetRail) {
		this.wetRail = wetRail;
	}

	public double getA() {
		return a;
	}

	public void setA(double a) {
		this.a = a;
	}

	public double getVt() {
		return Vt;
	}

	public void setVt(double vt) {
		Vt = vt;
	}

	public double getSt() {
		return St;
	}

	public void setSt(double st) {
		St = st;
	}

	public int getT() {
		return t;
	}

	public void setT(int t) {
		this.t = t;
	}

}
