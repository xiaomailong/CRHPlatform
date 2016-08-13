package com.crh2.javabean;

/**
 * ·ÖÏàsectionµÄjavabean
 * @author huhui
 *
 */
public class Section {
	private int id;
	private int section_id;
	private double start;
	private double end;
	private int electricity;
	private int routeId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSection_id() {
		return section_id;
	}

	public void setSection_id(int sectionId) {
		section_id = sectionId;
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

	public int getElectricity() {
		return electricity;
	}

	public void setElectricity(int electricity) {
		this.electricity = electricity;
	}

	public int getRouteId() {
		return routeId;
	}

	public void setRouteId(int routeId) {
		this.routeId = routeId;
	}

}
