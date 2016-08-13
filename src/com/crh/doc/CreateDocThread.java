package com.crh.doc;

import java.util.ArrayList;

import com.crh2.javabean.Rundata;

/**
 * �����ĵ����߳�
 * @author huhui
 *
 */
public class CreateDocThread implements Runnable {
	
	/**
	 * ����ǰ�����
	 */
	protected volatile int current = 0;
	/**
	 * ��������
	 */
	protected int amount = 100;
	
	/**
	 * ҵ����ز���
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
	 * ������������
	 * @return
	 */
	public int getAmount(){
		return amount;
	}
	
	/**
	 * ���ص�ǰ������
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
