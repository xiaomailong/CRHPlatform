package com.crh2.javabean;

/**
 * train_traction_conf_type
 * @author huhui
 *
 */
public class TrainTractionConfType {
	private int id;
	private int type;
	private int tractionId;
	private int trainCategoryId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getTractionId() {
		return tractionId;
	}

	public void setTractionId(int tractionId) {
		this.tractionId = tractionId;
	}

	public int getTrainCategoryId() {
		return trainCategoryId;
	}

	public void setTrainCategoryId(int trainCategoryId) {
		this.trainCategoryId = trainCategoryId;
	}

}
