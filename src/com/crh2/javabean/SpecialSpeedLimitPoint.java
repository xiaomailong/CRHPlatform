package com.crh2.javabean;

/**
 * specialspeedlimitpointµÄjavabean
 * @author huhui
 *
 */
public class SpecialSpeedLimitPoint {
	private int id;
	private int pointId;
	private double start;
	private double end;
	private double speedLimit;
	private int routeId;

	public int getId() {
		return id;
	}

	public int getPointId() {
		return pointId;
	}

	public void setPointId(int pointId) {
		this.pointId = pointId;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getStart() {
		return start;
	}

	public void setStart(double start) {
		this.start = start;
	}

	public double getEnd() {
		return end;
	}

	public void setEnd(double end) {
		this.end = end;
	}

	public double getSpeedLimit() {
		return speedLimit;
	}

	public void setSpeedLimit(double speedLimit) {
		this.speedLimit = speedLimit;
	}

	public int getRouteId() {
		return routeId;
	}

	public void setRouteId(int routeId) {
		this.routeId = routeId;
	}

}
