package com.crh2.javabean;

/**
 * train_formation��JavaBean
 * @author huhui
 *
 */
public class TrainFormation {

	private int id;
	private int unitNo;
	private int carriageNo;
	private String carriageType;
	private String carriageCategory;
	private double length;
	private double width;
	private double height;
	private double axleLength;
	private int axleNum;
	private int dynamicAxleNum;
	private double axleWeight;
	private double carriageDis;
	private int passenger;
	private double carriageWeight;
	private int sumDynamicAxle;
	private int trainCategoryId;
	/**
	 * �������ӣ����ݿ����޴��ֶΣ��������������ࣺ0Ϊ��ͷ��1Ϊһ�ȳ���2Ϊ���ȳ���3Ϊ�ͳ�
	 */
	private int carType;
	
	public TrainFormation(){
		
	}
	
	public TrainFormation(int unitNo, int carriageNo, int carType) {
		this.unitNo = unitNo;
		this.carriageNo = carriageNo;
		this.carType = carType;
		if(carType == 0){//��ͷ
			this.carriageType = "����";
			this.carriageCategory = "VIPͷ��";
			this.length = 25.535;
			this.axleWeight = 16.66;
			this.passenger = 73;
			this.carriageWeight = 60.12;
		}else if(carType == 1){//һ�ȳ�
			this.carriageType = "����";
			this.carriageCategory = "˫������ʽVIP��";
			this.length = 24.175;
			this.axleWeight = 15.36;
			this.passenger = 56;
			this.carriageWeight = 55.74;
		}else if(carType == 2){//���ȳ�
			this.carriageType = "�ϳ�";
			this.carriageCategory = "˫����ȳ�";
			this.length = 24.175;
			this.axleWeight = 16.81;
			this.passenger = 87;
			this.carriageWeight = 60.18;
		}else if(carType == 3){//�ͳ�
			this.carriageType = "�ϳ�";
			this.carriageCategory = "�๦�ܲͳ�/���������쳵";
			this.length = 24.175;
			this.axleWeight = 15.75;
			this.passenger = 50;
			this.carriageWeight = 57.94;
		}
		this.width = 3.265;
		this.height = 3.89;
		this.axleLength = 2.5;
		this.axleNum = 4;
		this.dynamicAxleNum = 4;
		this.carriageDis = 17.375;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUnitNo() {
		return unitNo;
	}

	public void setUnitNo(int unitNo) {
		this.unitNo = unitNo;
	}

	public int getCarriageNo() {
		return carriageNo;
	}

	public void setCarriageNo(int carriageNo) {
		this.carriageNo = carriageNo;
	}

	public String getCarriageType() {
		return carriageType;
	}

	public void setCarriageType(String carriageType) {
		this.carriageType = carriageType;
	}

	public String getCarriageCategory() {
		return carriageCategory;
	}

	public void setCarriageCategory(String carriageCategory) {
		this.carriageCategory = carriageCategory;
	}

	public double getLength() {
		return length;
	}

	public void setLength(double length) {
		this.length = length;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public double getAxleLength() {
		return axleLength;
	}

	public void setAxleLength(double axleLength) {
		this.axleLength = axleLength;
	}

	public int getAxleNum() {
		return axleNum;
	}

	public void setAxleNum(int axleNum) {
		this.axleNum = axleNum;
	}

	public int getDynamicAxleNum() {
		return dynamicAxleNum;
	}

	public void setDynamicAxleNum(int dynamicAxleNum) {
		this.dynamicAxleNum = dynamicAxleNum;
	}

	public double getAxleWeight() {
		return axleWeight;
	}

	public void setAxleWeight(double axleWeight) {
		this.axleWeight = axleWeight;
	}

	public double getCarriageDis() {
		return carriageDis;
	}

	public void setCarriageDis(double carriageDis) {
		this.carriageDis = carriageDis;
	}

	public int getPassenger() {
		return passenger;
	}

	public void setPassenger(int passenger) {
		this.passenger = passenger;
	}

	public double getCarriageWeight() {
		return carriageWeight;
	}

	public void setCarriageWeight(double carriageWeight) {
		this.carriageWeight = carriageWeight;
	}

	public int getTrainCategoryId() {
		return trainCategoryId;
	}

	public int getSumDynamicAxle() {
		return sumDynamicAxle;
	}

	public void setSumDynamicAxle(int sumDynamicAxle) {
		this.sumDynamicAxle = sumDynamicAxle;
	}

	public void setTrainCategoryId(int trainCategoryId) {
		this.trainCategoryId = trainCategoryId;
	}

	public int getCarType() {
		return carType;
	}

	public void setCarType(int carType) {
		this.carType = carType;
	}

}