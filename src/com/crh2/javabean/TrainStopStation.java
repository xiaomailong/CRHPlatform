package com.crh2.javabean;

/**
 * train_stop_stationµÄJavaBean
 * @author huhui
 *
 */
public class TrainStopStation {
	private int id;
	private int trainRouteId;
	private String routeName;
	private int stationId;
	private String stationName;
	private String trainNum;
	private double stopTIme = 0;
	private double location;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTrainRouteId() {
		return trainRouteId;
	}

	public void setTrainRouteId(int trainRouteId) {
		this.trainRouteId = trainRouteId;
	}

	public String getRouteName() {
		return routeName;
	}

	public int getStationId() {
		return stationId;
	}

	public void setStationId(int stationId) {
		this.stationId = stationId;
	}

	public void setRouteName(String routeName) {
		this.routeName = routeName;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public String getTrainNum() {
		return trainNum;
	}

	public void setTrainNum(String trainNum) {
		this.trainNum = trainNum;
	}

	public double getStopTIme() {
		return stopTIme;
	}

	public void setStopTIme(double stopTIme) {
		this.stopTIme = stopTIme;
	}

	public double getLocation() {
		return location;
	}

	public void setLocation(double location) {
		this.location = location;
	}

}
