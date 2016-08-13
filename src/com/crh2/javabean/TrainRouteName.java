package com.crh2.javabean;

/**
 * train_route_nameµÄJavaBean
 * @author huhui
 *
 */
public class TrainRouteName {

	private int id;
	private String routeName;

	public TrainRouteName() {

	}

	public TrainRouteName(int id, String routeName) {
		this.id = id;
		this.routeName = routeName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRouteName() {
		return routeName;
	}

	public void setRouteName(String routeName) {
		this.routeName = routeName;
	}

}
