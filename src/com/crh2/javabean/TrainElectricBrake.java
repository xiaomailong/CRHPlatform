package com.crh2.javabean;

/**
 * train_electric_brakeµÄJavaBean
 * @author huhui
 *
 */
public class TrainElectricBrake {

	private int id;
	private double v1 = 5;
	private double v2 = 106.7;
	private double P00 = 500;
	private int trainCategoryId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public double getP00() {
		return P00;
	}

	public void setP00(double p00) {
		P00 = p00;
	}

	public int getTrainCategoryId() {
		return trainCategoryId;
	}

	public void setTrainCategoryId(int trainCategoryId) {
		this.trainCategoryId = trainCategoryId;
	}

}
