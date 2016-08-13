package com.crh2.javabean;

/**
 * 六条曲线公式的JavaBean
 * @author huhui
 *
 */
public class CurveFormulaBean {
	// 牵引特性参数
	private double v;
	private double tractionForce;
	private double airFriction;
	private double slope;
	private double dryRail;
	private double wetRail;
	// 启动性能参数
	private double a;// 加速度
	private double Vt;// 速度
	private double St;// 位移
	private int t;// 时间

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
