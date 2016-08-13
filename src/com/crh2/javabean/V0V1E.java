package com.crh2.javabean;

/**
 * v0 v1 Eศýิชื้
 * @author huhui
 *
 */
public class V0V1E {
	private double v0;
	private double v1;
	private double E;
	private double s0End;
	private double s1End;
	private double s3;

	public V0V1E(double v0, double v1, double e, double s0End, double s1End, double s3) {
		super();
		this.v0 = v0;
		this.v1 = v1;
		E = e;
		this.s0End = s0End;
		this.s1End = s1End;
		this.s3 = s3;
	}

	public double getS3() {
		return s3;
	}

	public void setS3(double s3) {
		this.s3 = s3;
	}

	public double getS0End() {
		return s0End;
	}

	public void setS0End(double s0End) {
		this.s0End = s0End;
	}

	public double getS1End() {
		return s1End;
	}

	public void setS1End(double s1End) {
		this.s1End = s1End;
	}

	public double getV0() {
		return v0;
	}

	public void setV0(double v0) {
		this.v0 = v0;
	}

	public double getV1() {
		return v1;
	}

	public void setV1(double v1) {
		this.v1 = v1;
	}

	public double getE() {
		return E;
	}

	public void setE(double e) {
		E = e;
	}

}
