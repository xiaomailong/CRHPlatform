package com.crh2.javabean;

/**
 * train_traction_confµÄJavaBean
 * @author huhui
 *
 */
public class TrainTractionConf {

	private int id;
	private double k1 = 0.975;
	private double k2 = 2.7931;
	private double D = 0.875;
	private int N0 = 4;
	private int N2 = 4;
	private int N = 16;
	private double T = 3000;
	private double P0 = 550;
	private double P1 = 8800;
	private double v1 = 119.1;
	private double v2 = 310;
	private double F1 = 265.99;
	private double F2 = 102.19;
	private double Fst = 298.78;
	private double vi1 = 11.8;
	private double I1 = 216;
	private double vi2 = 118;
	private double I2 = 198;
	private double vi3 = 212;
	private double I3 = 136;
	private double vu1 = 27.6;
	private double u1 = 10;
	private double vu2 = 112;
	private double u2 = 195;
	private double vu3 = 207;
	private double u3 = 280;
	private int trainCategoryId;
	private double D0 = 0.92;
	private double D1 = 0.83;

	public double getD0() {
		return D0;
	}

	public void setD0(double d0) {
		D0 = d0;
	}

	public double getD1() {
		return D1;
	}

	public void setD1(double d1) {
		D1 = d1;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getK1() {
		return k1;
	}

	public void setK1(double k1) {
		this.k1 = k1;
	}

	public double getK2() {
		return k2;
	}

	public void setK2(double k2) {
		this.k2 = k2;
	}

	public double getD() {
		return D;
	}

	public void setD(double d) {
		D = d;
	}

	public int getN0() {
		return N0;
	}

	public void setN0(int n0) {
		N0 = n0;
	}

	public int getN2() {
		return N2;
	}

	public void setN2(int n2) {
		N2 = n2;
	}

	public int getN() {
		return N;
	}

	public void setN(int n) {
		N = n;
	}

	public double getT() {
		return T;
	}

	public void setT(double t) {
		T = t;
	}

	public double getP0() {
		return P0;
	}

	public void setP0(double p0) {
		P0 = p0;
	}

	public double getP1() {
		return P1;
	}

	public void setP1(double p1) {
		P1 = p1;
	}

	public double getV1() {
		return v1;
	}

	public void setV1(double v1) {
		this.v1 = v1;
	}

	public double getV2() {
		return v2;
	}

	public void setV2(double v2) {
		this.v2 = v2;
	}

	public double getF1() {
		return F1;
	}

	public void setF1(double f1) {
		F1 = f1;
	}

	public double getF2() {
		return F2;
	}

	public void setF2(double f2) {
		F2 = f2;
	}

	public double getFst() {
		return Fst;
	}

	public void setFst(double fst) {
		Fst = fst;
	}

	public int getTrainCategoryId() {
		return trainCategoryId;
	}

	public double getVi1() {
		return vi1;
	}

	public void setVi1(double vi1) {
		this.vi1 = vi1;
	}

	public double getI1() {
		return I1;
	}

	public void setI1(double i1) {
		I1 = i1;
	}

	public double getVi2() {
		return vi2;
	}

	public void setVi2(double vi2) {
		this.vi2 = vi2;
	}

	public double getI2() {
		return I2;
	}

	public void setI2(double i2) {
		I2 = i2;
	}

	public double getVi3() {
		return vi3;
	}

	public void setVi3(double vi3) {
		this.vi3 = vi3;
	}

	public double getI3() {
		return I3;
	}

	public void setI3(double i3) {
		I3 = i3;
	}

	public double getVu1() {
		return vu1;
	}

	public void setVu1(double vu1) {
		this.vu1 = vu1;
	}

	public double getU1() {
		return u1;
	}

	public void setU1(double u1) {
		this.u1 = u1;
	}

	public double getVu2() {
		return vu2;
	}

	public void setVu2(double vu2) {
		this.vu2 = vu2;
	}

	public double getU2() {
		return u2;
	}

	public void setU2(double u2) {
		this.u2 = u2;
	}

	public double getVu3() {
		return vu3;
	}

	public void setVu3(double vu3) {
		this.vu3 = vu3;
	}

	public double getU3() {
		return u3;
	}

	public void setU3(double u3) {
		this.u3 = u3;
	}

	public void setTrainCategoryId(int trainCategoryId) {
		this.trainCategoryId = trainCategoryId;
	}

}
