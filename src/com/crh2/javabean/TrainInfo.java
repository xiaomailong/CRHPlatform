package com.crh2.javabean;

/**
 * train_infoµÄJavaBean
 * @author huhui
 *
 */
public class TrainInfo {

	private int id;
	private double maxV = 330;
	private double maxEv = 350;
	private double conV = 300;
	private String powerConf = "4¶¯4ÍÏ";
	private double m = 475.8;
	private double mzmax = 16.98;
	private double a200 = 25;//ÍøÑ¹
	private double mzmin = 13.88;
	private double ar = 0.05;
	private double j = 0.05;
	private double dv = 0;
	private double slope = 0;
	private double launchf = 1315.503;
	private double em1 = 0.947;
	private double powerFac = 0.89;
	private double gearm2 = 0.975;
	private double a = 1.315503;
	private double b = 0.0155847;
	private double c = 0.000608;
	private String tu1 = "0.25.*(v<100)+(0.325-0.15/200*v) .*(v>100)";
	private String tu2 = "0.22.*(v<100)+(0.295-0.15/200*v) .*(v>100)";
	private String bu1 = "0.15.*(v<200)+(0.22-0.035/100*v) .*(v>200)";
	private String bu2 = "0.12.*(v<200)+(0.19-0.035/100*v) .*(v>200)";
	private int trainCategoryId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getMaxV() {
		return maxV;
	}

	public void setMaxV(double maxV) {
		this.maxV = maxV;
	}

	public double getMaxEv() {
		return maxEv;
	}

	public void setMaxEv(double maxEv) {
		this.maxEv = maxEv;
	}

	public double getConV() {
		return conV;
	}

	public void setConV(double conV) {
		this.conV = conV;
	}

	public String getPowerConf() {
		return powerConf;
	}

	public void setPowerConf(String powerConf) {
		this.powerConf = powerConf;
	}

	public double getM() {
		return m;
	}

	public void setM(double m) {
		this.m = m;
	}

	public double getMzmax() {
		return mzmax;
	}

	public void setMzmax(double mzmax) {
		this.mzmax = mzmax;
	}

	public double getA200() {
		return a200;
	}

	public void setA200(double a200) {
		this.a200 = a200;
	}

	public double getMzmin() {
		return mzmin;
	}

	public void setMzmin(double mzmin) {
		this.mzmin = mzmin;
	}

	public double getAr() {
		return ar;
	}

	public void setAr(double ar) {
		this.ar = ar;
	}

	public double getJ() {
		return j;
	}

	public void setJ(double j) {
		this.j = j;
	}

	public double getDv() {
		return dv;
	}

	public void setDv(double dv) {
		this.dv = dv;
	}

	public double getSlope() {
		return slope;
	}

	public void setSlope(double slope) {
		this.slope = slope;
	}

	public double getLaunchf() {
		return launchf;
	}

	public void setLaunchf(double launchf) {
		this.launchf = launchf;
	}

	public double getEm1() {
		return em1;
	}

	public void setEm1(double em1) {
		this.em1 = em1;
	}

	public double getPowerFac() {
		return powerFac;
	}

	public void setPowerFac(double powerFac) {
		this.powerFac = powerFac;
	}

	public double getGearm2() {
		return gearm2;
	}

	public void setGearm2(double gearm2) {
		this.gearm2 = gearm2;
	}

	public double getA() {
		return a;
	}

	public void setA(double a) {
		this.a = a;
	}

	public double getB() {
		return b;
	}

	public void setB(double b) {
		this.b = b;
	}

	public double getC() {
		return c;
	}

	public void setC(double c) {
		this.c = c;
	}

	public String getTu1() {
		return tu1;
	}

	public void setTu1(String tu1) {
		this.tu1 = tu1;
	}

	public String getTu2() {
		return tu2;
	}

	public void setTu2(String tu2) {
		this.tu2 = tu2;
	}

	public String getBu1() {
		return bu1;
	}

	public void setBu1(String bu1) {
		this.bu1 = bu1;
	}

	public String getBu2() {
		return bu2;
	}

	public void setBu2(String bu2) {
		this.bu2 = bu2;
	}

	public int getTrainCategoryId() {
		return trainCategoryId;
	}

	public void setTrainCategoryId(int trainCategoryId) {
		this.trainCategoryId = trainCategoryId;
	}

}