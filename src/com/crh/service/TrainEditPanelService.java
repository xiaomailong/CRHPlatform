package com.crh.service;

import java.util.ArrayList;
import java.util.List;

import com.crh2.dao.SQLHelper;
import com.crh2.javabean.TrainCategory;
import com.crh2.javabean.TrainFormation;
import com.crh2.javabean.TrainInfo;

/**
 * 完成TrainEditPanel的数据保存功能
 * @author huhui
 *
 */
public class TrainEditPanelService {

	private static SQLHelper sqlHelper = new SQLHelper();

	/**
	 * 保存列车种类数据train_category
	 * @param trainCategoryName
	 * @return
	 */
	public static int saveTrainCategory(String trainCategoryName) {
		int id = -1;
		String existSQL = "SELECT NAME FROM train_category WHERE NAME = '"+trainCategoryName+"'";
		List list = sqlHelper.query(existSQL, null);
		if(list.size() == 0){//如果不存在则插入
			String sql = "INSERT INTO train_category VALUES(NULL,'" + trainCategoryName + "')";
			id = sqlHelper.insertAndGetId(sql);
		}
		return id;
	}
	
	/**
	 * 获取列车种类数据train_category
	 * @return
	 */
	public static ArrayList<TrainCategory> getAllTrainCategory(){
		ArrayList<TrainCategory> trainCategoryList = new ArrayList<TrainCategory>();
		String sql = "SELECT * FROM train_category";
		List list = sqlHelper.query(sql, null);
		for(int i=0;i<list.size();i++){
			Object object[] = (Object[]) list.get(i);
			TrainCategory tc = new TrainCategory();
			tc.setId(Integer.parseInt(object[0].toString()));
			tc.setName(object[1].toString());
			trainCategoryList.add(tc);
		}
		return trainCategoryList;
	}
	
	/**
	 *  删除train_category表中的车型数据
	 * @param trainCategoryId
	 */
	public static void deleteTrainCategory(int trainCategoryId) {
		String sql = "DELETE FROM train_category WHERE id = " + trainCategoryId;
		sqlHelper.update(sql, null);
	}

	/**
	 *  保存列车配置数据train_info
	 * @param trainInfo
	 * @param trainCategoryId
	 */
	public static void saveTrainInfo(TrainInfo trainInfo,int trainCategoryId) {
		String deleteSQL = "DELETE FROM train_info WHERE train_category_id = "+trainCategoryId;
		sqlHelper.update(deleteSQL, null);
		String insertSQL = "INSERT INTO train_info VALUES(NULL,"
				+ trainInfo.getMaxV() + "," + trainInfo.getMaxEv() + ","
				+ trainInfo.getConV() + ",'" + trainInfo.getPowerConf() + "',"
				+ trainInfo.getM() + "," + trainInfo.getMzmax() + ","
				+ trainInfo.getA200() + "," + trainInfo.getMzmin() + ","
				+ trainInfo.getAr() + "," + trainInfo.getJ() + ","
				+ trainInfo.getDv() + "," + trainInfo.getSlope() + ","
				+ trainInfo.getLaunchf() + "," + trainInfo.getEm1() + ","
				+ trainInfo.getPowerFac() + "," + trainInfo.getGearm2() + ","
				+ trainInfo.getA() + "," + trainInfo.getB() + ","
				+ trainInfo.getC() + ",'" + trainInfo.getTu1() + "','"
				+ trainInfo.getTu2() + "','" + trainInfo.getBu1() + "','"
				+ trainInfo.getBu2() + "'," + trainCategoryId
				+ ")";
		sqlHelper.update(insertSQL, null);
	}
	
	/**
	 *  获取列车配置数据train_info
	 * @param trainCategoryId
	 * @return
	 */
	public static TrainInfo getTrainInfoByCategoryId(int trainCategoryId){
		String sql = "SELECT * FROM train_info WHERE train_category_id = "+trainCategoryId;
		List list = sqlHelper.query(sql, null);
		TrainInfo trainInfo = new TrainInfo();
		if(list.size() != 0){
			//组装JavaBean
			trainInfo.setId(Integer.parseInt(((Object[]) list.get(0))[0].toString()));
			trainInfo.setMaxV(Double.parseDouble(((Object[]) list.get(0))[1].toString()));
			trainInfo.setMaxEv(Double.parseDouble(((Object[]) list.get(0))[2].toString()));
			trainInfo.setConV(Double.parseDouble(((Object[]) list.get(0))[3].toString()));
			trainInfo.setPowerConf(((Object[]) list.get(0))[4].toString());
			trainInfo.setM(Double.parseDouble(((Object[]) list.get(0))[5].toString()));
			trainInfo.setMzmax(Double.parseDouble(((Object[]) list.get(0))[6].toString()));
			trainInfo.setA200(Double.parseDouble(((Object[]) list.get(0))[7].toString()));
			trainInfo.setMzmin(Double.parseDouble(((Object[]) list.get(0))[8].toString()));
			trainInfo.setAr(Double.parseDouble(((Object[]) list.get(0))[9].toString()));
			trainInfo.setJ(Double.parseDouble(((Object[]) list.get(0))[10].toString()));
			trainInfo.setDv(Double.parseDouble(((Object[]) list.get(0))[11].toString()));
			trainInfo.setSlope(Double.parseDouble(((Object[]) list.get(0))[12].toString()));
			trainInfo.setLaunchf(Double.parseDouble(((Object[]) list.get(0))[13].toString()));
			trainInfo.setEm1(Double.parseDouble(((Object[]) list.get(0))[14].toString()));
			trainInfo.setPowerFac(Double.parseDouble(((Object[]) list.get(0))[15].toString()));
			trainInfo.setGearm2(Double.parseDouble(((Object[]) list.get(0))[16].toString()));
			trainInfo.setA(Double.parseDouble(((Object[]) list.get(0))[17].toString()));
			trainInfo.setB(Double.parseDouble(((Object[]) list.get(0))[18].toString()));
			trainInfo.setC(Double.parseDouble(((Object[]) list.get(0))[19].toString()));
			trainInfo.setTu1(((Object[]) list.get(0))[20].toString());
			trainInfo.setTu2(((Object[]) list.get(0))[21].toString());
			trainInfo.setBu1(((Object[]) list.get(0))[22].toString());
			trainInfo.setBu2(((Object[]) list.get(0))[23].toString());
			trainInfo.setTrainCategoryId(Integer.parseInt(((Object[]) list.get(0))[24].toString()));
		}
		
		return trainInfo;
	}

	/**
	 *  保存列车编组数据train_formation
	 * @param trainFormationList
	 * @param trainCategoryId
	 */
	public static void saveTrainFormation(ArrayList<TrainFormation> trainFormationList,int trainCategoryId) {
		String deleteSQL = "DELETE FROM train_formation WHERE train_category_id = "+trainCategoryId;
		sqlHelper.update(deleteSQL, null);
		//批量插入
		ArrayList<String> sqlList = new ArrayList<String>();
		for(TrainFormation trainFormation : trainFormationList){
			String insertSQL = "INSERT INTO train_formation VALUES(NULL,"
					+ trainFormation.getUnitNo() + ","
					+ trainFormation.getCarriageNo() + ",'"
					+ trainFormation.getCarriageType() + "','"
					+ trainFormation.getCarriageCategory() + "',"
					+ trainFormation.getLength() + "," + trainFormation.getWidth()
					+ "," + trainFormation.getHeight() + ","
					+ trainFormation.getAxleLength() + ","
					+ trainFormation.getAxleNum() + "," +trainFormation.getDynamicAxleNum()+","
					+ trainFormation.getAxleWeight() + ","
					+ trainFormation.getCarriageDis() + ","
					+ trainFormation.getPassenger() + ","
					+ trainFormation.getCarriageWeight() + "," + trainFormation.getSumDynamicAxle() + ","
					+ trainCategoryId + ")";
			sqlList.add(insertSQL);
		}
		sqlHelper.batchInsert(sqlList);
	}
	
	/**
	 *  获取列车编组数据train_formation
	 * @param trainCategoryId
	 * @return
	 */
	public static ArrayList<TrainFormation> getTrainFormationByTrainCategoryId(int trainCategoryId){
		String sql = "SELECT * FROM train_formation WHERE train_category_id = "+trainCategoryId;
		ArrayList<TrainFormation> trainFormationList = new ArrayList<TrainFormation>();
		List list = sqlHelper.query(sql, null);
		for(int i=0;i<list.size();i++){
			Object [] obj = (Object[]) list.get(i);
			TrainFormation trainFormation = new TrainFormation();
			trainFormation.setId(Integer.parseInt(obj[0].toString()));
			trainFormation.setUnitNo(Integer.parseInt(obj[1].toString()));
			trainFormation.setCarriageNo(Integer.parseInt(obj[2].toString()));
			trainFormation.setCarriageType(obj[3].toString());
			trainFormation.setCarriageCategory(obj[4].toString());
			trainFormation.setLength(Double.parseDouble(obj[5].toString()));
			trainFormation.setWidth(Double.parseDouble(obj[6].toString()));
			trainFormation.setHeight(Double.parseDouble(obj[7].toString()));
			trainFormation.setAxleLength(Double.parseDouble(obj[8].toString()));
			trainFormation.setAxleNum(Integer.parseInt(obj[9].toString()));
			trainFormation.setDynamicAxleNum(Integer.parseInt(obj[10].toString()));
			trainFormation.setAxleWeight(Double.parseDouble(obj[11].toString()));
			trainFormation.setCarriageDis(Double.parseDouble(obj[12].toString()));
			trainFormation.setPassenger(Integer.parseInt(obj[13].toString()));
			trainFormation.setCarriageWeight(Double.parseDouble(obj[14].toString()));
			trainFormation.setTrainCategoryId(Integer.parseInt(obj[16].toString()));
			
			trainFormationList.add(trainFormation);
		}
		return trainFormationList;
	}
	
	/**
	 * 返回列车质量相关数据，分别是“列车长度”、“空车质量”、“总质量”、“定员数”
	 * @param trainCategoryId
	 * @return
	 */
	public static double [] getWeightInfo(int trainCategoryId){
		double [] para = new double[4];
		String sql = "SELECT SUM(LENGTH), SUM(carriageWeight), SUM(carriageWeight) + SUM(passenger) * 0.08, SUM(passenger) FROM train_formation WHERE train_category_id = "+trainCategoryId;
		List list = sqlHelper.query(sql, null);
		if(list.size() != 0){
			Object [] obj = (Object[]) list.get(0);
			para[0] = Double.parseDouble(obj[0].toString());
			para[1] = Double.parseDouble(obj[1].toString());
			para[2] = Double.parseDouble(obj[2].toString());
			para[3] = Double.parseDouble(obj[3].toString());
		}
		return para;
	}
	
	/**
	 * 判断是否保存了车辆信息
	 * @param trainCategoryId
	 * @return
	 */
	public static int getTrainInfoCount(int trainCategoryId){
		int count = 0;
		String sql = "SELECT COUNT(*) FROM train_info WHERE train_category_id = "+trainCategoryId;
		List list = sqlHelper.query(sql, null);
		if(list.size() != 0){
			count = Integer.parseInt(((Object [])list.get(0))[0].toString());
		}
		return count;
	}
	
}
