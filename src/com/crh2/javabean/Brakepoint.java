package com.crh2.javabean;

/**
 * 制动的DTO对象
 * @author huhui
 *
 */
public class Brakepoint{

	// Fields

	private Integer id;
	private Double start;
	private Double end;
	private Double limitSpeed;

	// Constructors

	/** default constructor */
	public Brakepoint() {
	}

	/** minimal constructor */
	public Brakepoint(Integer id) {
		this.id = id;
	}

	/** full constructor */
	public Brakepoint(Integer id, Double start, Double end, Double limitSpeed) {
		this.id = id;
		this.start = start;
		this.end = end;
		this.limitSpeed = limitSpeed;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double getStart() {
		return this.start;
	}

	public void setStart(Double start) {
		this.start = start;
	}

	public Double getEnd() {
		return this.end;
	}

	public void setEnd(Double end) {
		this.end = end;
	}

	public Double getLimitSpeed() {
		return this.limitSpeed;
	}

	public void setLimitSpeed(Double limitSpeed) {
		this.limitSpeed = limitSpeed;
	}

}