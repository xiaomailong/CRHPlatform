package com.crh2.javabean;

public class RundataBack {
	private int id;
	private double runtime;
	private double currentSpeed;
	private double lastSpeed;
	private double distance;
	private double forwardDistance;
	private double cp;
	private int slopeFlag;
	private int curveFlag;

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

	public double getCurrentSpeed() {
		return currentSpeed;
	}

	public void setCurrentSpeed(double currentSpeed) {
		this.currentSpeed = currentSpeed;
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

	public double getForwardDistance() {
		return forwardDistance;
	}

	public void setForwardDistance(double forwardDistance) {
		this.forwardDistance = forwardDistance;
	}

	public double getCp() {
		return cp;
	}

	public void setCp(double cp) {
		this.cp = cp;
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

}
