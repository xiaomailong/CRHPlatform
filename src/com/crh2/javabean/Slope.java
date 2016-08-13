package com.crh2.javabean;

/**
 * slope±íµÄjavabean
 */
public class Slope {
	private int id;
	private int slopeId;
	private double slope;
	private double length;
	private double end;
	private double height;
	private int routeId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSlopeId() {
		return slopeId;
	}

	public void setSlopeId(int slopeId) {
		this.slopeId = slopeId;
	}

	public double getSlope() {
		return slope;
	}

	public void setSlope(double slope) {
		this.slope = slope;
	}

	public double getLength() {
		return length;
	}

	public void setLength(double length) {
		this.length = length;
	}

	public double getEnd() {
		return end;
	}

	public void setEnd(double end) {
		this.end = end;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public int getRouteId() {
		return routeId;
	}

	public void setRouteId(int routeId) {
		this.routeId = routeId;
	}

}