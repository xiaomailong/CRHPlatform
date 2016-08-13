package com.crh2.javabean;

/**
 * Slopes entity. @author MyEclipse Persistence Tools
 */

public class Slopes {

	// Fields

	private Integer id;
	private Double slope;
	private Double length;
	private Double end;
	private Double high;

	// Constructors

	/** default constructor */
	public Slopes() {
	}

	/** minimal constructor */
	public Slopes(Integer id) {
		this.id = id;
	}

	/** full constructor */
	public Slopes(Integer id, Double slope, Double length, Double end,
			Double high) {
		this.id = id;
		this.slope = slope;
		this.length = length;
		this.end = end;
		this.high = high;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double getSlope() {
		return this.slope;
	}

	public void setSlope(Double slope) {
		this.slope = slope;
	}

	public Double getLength() {
		return this.length;
	}

	public void setLength(Double length) {
		this.length = length;
	}

	public Double getEnd() {
		return this.end;
	}

	public void setEnd(Double end) {
		this.end = end;
	}

	public Double getHigh() {
		return this.high;
	}

	public void setHigh(Double high) {
		this.high = high;
	}

}