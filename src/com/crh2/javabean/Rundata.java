package com.crh2.javabean;

/**
 * train_rundata的JavaBean
 * @author huhui
 *
 */
public class Rundata {

	private int id;
	private double runtime = 0;
	private double speed = 0;
	private double lastSpeed = 0;
	private double distance = 0;
	private double location = 0;
	private double cp = 0;
	private double printCp = 0;
	private int routeId = 0;
	private int slopeFlag = 0;
	private int curveFlag = 0;
	private boolean isConstantSpeed;
	private double power = 0;//总能量
	private double acceleration = 0;
	private double tractionForce = 0;
	private double brakeForce = 0;
	private double eleBrakeForce = 0;
	private double airForce = 0;
	private double otherForce = 0;
	private double tractionPower = 0;
	private double averageAcc = 0;
	private double manulBrakeForcePower = 0;//普通制动力
	private double elecBrakeForcePower = 0;//电制动力
	private double comBrakeForcePower = 0;//综合制动力
	private double tractionGridCurrent = 0;//牵引力网流
	private double comBrakeGridCurrent = 0;//综合制动力网流
	//总能量 = actualTractionPower - actualElecBrakeForcePower   2015.4.24修改
	private double actualTractionPower = 0; //连续的牵引能量
	private double actualElecBrakeForcePower = 0; //连续的电制动能量
	private double totalPower;

	public double getTotalPower() {
		return totalPower;
	}

	public void setTotalPower(double totalPower) {
		this.totalPower = totalPower;
	}

	public double getActualTractionPower() {
		return actualTractionPower;
	}

	public void setActualTractionPower(double actualTractionPower) {
		this.actualTractionPower = actualTractionPower;
	}

	public double getActualElecBrakeForcePower() {
		return actualElecBrakeForcePower;
	}

	public void setActualElecBrakeForcePower(double actualElecBrakeForcePower) {
		this.actualElecBrakeForcePower = actualElecBrakeForcePower;
	}

	public double getTractionGridCurrent() {
		return tractionGridCurrent;
	}

	public void setTractionGridCurrent(double tractionGridCurrent) {
		this.tractionGridCurrent = tractionGridCurrent;
	}

	public double getComBrakeGridCurrent() {
		return comBrakeGridCurrent;
	}

	public void setComBrakeGridCurrent(double comBrakeGridCurrent) {
		this.comBrakeGridCurrent = comBrakeGridCurrent;
	}

	public double getManulBrakeForcePower() {
		return manulBrakeForcePower;
	}

	public void setManulBrakeForcePower(double manulBrakeForcePower) {
		this.manulBrakeForcePower = manulBrakeForcePower;
	}

	public double getComBrakeForcePower() {
		return comBrakeForcePower;
	}

	public void setComBrakeForcePower(double comBrakeForcePower) {
		this.comBrakeForcePower = comBrakeForcePower;
	}

	public double getTractionPower() {
		return tractionPower;
	}

	public void setTractionPower(double tractionPower) {
		this.tractionPower = tractionPower;
	}

	public double getAverageAcc() {
		return averageAcc;
	}

	public void setAverageAcc(double averageAcc) {
		this.averageAcc = averageAcc;
	}

	public double getLocation() {
		return location;
	}

	public void setLocation(double location) {
		this.location = location;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getRuntime() {
		return runtime;
	}

	public void setRuntime(double runtime) {
		this.runtime = runtime;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public double getLastSpeed() {
		return lastSpeed;
	}

	public void setLastSpeed(double lastSpeed) {
		this.lastSpeed = lastSpeed;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public double getCp() {
		return cp;
	}

	public void setCp(double cp) {
		this.cp = cp;
	}

	public double getPrintCp() {
		return printCp;
	}

	public void setPrintCp(double printCp) {
		this.printCp = printCp;
	}

	public int getRouteId() {
		return routeId;
	}

	public void setRouteId(int routeId) {
		this.routeId = routeId;
	}

	public int getSlopeFlag() {
		return slopeFlag;
	}

	public void setSlopeFlag(int slopeFlag) {
		this.slopeFlag = slopeFlag;
	}

	public int getCurveFlag() {
		return curveFlag;
	}

	public void setCurveFlag(int curveFlag) {
		this.curveFlag = curveFlag;
	}

	public boolean isConstantSpeed() {
		return isConstantSpeed;
	}

	public void setConstantSpeed(boolean isConstantSpeed) {
		this.isConstantSpeed = isConstantSpeed;
	}

	public double getPower() {
		return power;
	}

	public void setPower(double power) {
		this.power = power;
	}

	public double getAcceleration() {
		return acceleration;
	}

	public void setAcceleration(double acceleration) {
		this.acceleration = acceleration;
	}

	public double getElecBrakeForcePower() {
		return elecBrakeForcePower;
	}

	public void setElecBrakeForcePower(double elecBrakeForcePower) {
		this.elecBrakeForcePower = elecBrakeForcePower;
	}

	public double getTractionForce() {
		return tractionForce;
	}

	public void setTractionForce(double tractionForce) {
		this.tractionForce = tractionForce;
	}

	public double getBrakeForce() {
		return brakeForce;
	}

	public void setBrakeForce(double brakeForce) {
		this.brakeForce = brakeForce;
	}

	public double getEleBrakeForce() {
		return eleBrakeForce;
	}

	public void setEleBrakeForce(double eleBrakeForce) {
		this.eleBrakeForce = eleBrakeForce;
	}

	public double getAirForce() {
		return airForce;
	}

	public void setAirForce(double airForce) {
		this.airForce = airForce;
	}

	public double getOtherForce() {
		return otherForce;
	}

	public void setOtherForce(double otherForce) {
		this.otherForce = otherForce;
	}
	
}