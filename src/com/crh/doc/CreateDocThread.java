package com.crh.doc;

import java.util.ArrayList;

import com.crh2.javabean.Rundata;

/**
 * 生成文档的线程
 * @author huhui
 *
 */
public class CreateDocThread implements Runnable {
	
	/**
	 * 任务当前完成量
	 */
	protected volatile int current = 0;
	/**
	 * 总任务量
	 */
	protected int amount = 100;
	
	/**
	 * 业务相关参数
	 */
	private String routeName, trainNum, trainCategory;
	private int trainCategoryId;
	private ArrayList<Rundata> rundataList;
	private String savePath = "";
	
	public CreateDocThread(String routeName, String trainNum, int trainCategoryId, String trainCategory, ArrayList<Rundata> rundataList, String savePath) {
		this.routeName = routeName;
		this.trainNum = trainNum;
		this.trainCategoryId = trainCategoryId;
		this.trainCategory = trainCategory;
		this.rundataList = rundataList;
		this.savePath = savePath;
	}

	/**
	 * 返回任务总量
	 * @return
	 */
	public int getAmount(){
		return amount;
	}
	
	/**
	 * 返回当前任务量
	 * @return
	 */
	public int getCurrent(){
		return current;
	}

	@Override
	public void run() {
		CreateDocService createDocService = new CreateDocService(routeName, trainNum, trainCategoryId, trainCategory, rundataList, savePath);
		createDocService.createDoc();
		current = amount;
	}

}
