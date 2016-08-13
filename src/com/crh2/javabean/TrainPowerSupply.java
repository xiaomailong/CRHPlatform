package com.crh2.javabean;

/**
 * train_power_supplyµÄJavaBean
 * @author huhui
 *
 */
public class TrainPowerSupply {

	private int id;
	private String load;
	private double winter;
	private double summer;
	private int carNo;
	private int trainCategoryId;
	
	public TrainPowerSupply(){
		
	}

	public TrainPowerSupply(int carNo, int trainCategoryId) {
		super();
		this.carNo = carNo;
		this.trainCategoryId = trainCategoryId;
	}
	
	public void setParameters(String load, double winter, double summer) {
		this.load = load;
		this.winter = winter;
		this.summer = summer;
	}



	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLoad() {
		return load;
	}

	public void setLoad(String load) {
		this.load = load;
	}

	public double getWinter() {
		return winter;
	}

	public void setWinter(double winter) {
		this.winter = winter;
	}

	public double getSummer() {
		return summer;
	}

	public void setSummer(double summer) {
		this.summer = summer;
	}

	public int getCarNo() {
		return carNo;
	}

	public void setCarNo(int carNo) {
		this.carNo = carNo;
	}

	public int getTrainCategoryId() {
		return trainCategoryId;
	}

	public void setTrainCategoryId(int trainCategoryId) {
		this.trainCategoryId = trainCategoryId;
	}

}
