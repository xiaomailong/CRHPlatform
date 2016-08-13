package com.crh.service;

import java.util.ArrayList;
import java.util.List;

import com.crh2.dao.SQLHelper;
import com.crh2.javabean.TrainCategory;
import com.crh2.javabean.TrainTopTarget;
import com.crh2.util.MyTools;

/**
 * 为TopTargetPanel提供数据服务
 * @author huhui
 *
 */
public class TopTargetService {
	
	private static SQLHelper sqlHelper = new SQLHelper();
	
	/**
	 * 保存train_top_target数据
	 */
	public static void saveTrainTopTarget(TrainTopTarget trainTopTarget, int trainCategoryId){
		String delSQL = "DELETE FROM train_top_target WHERE train_category_id = " + trainCategoryId;
		sqlHelper.update(delSQL, null);
		String sql = "insert into train_top_target values(null,"
				+ trainTopTarget.getSpeed0() + "," + trainTopTarget.getSpeed1()
				+ "," + trainTopTarget.getSpeed2() + ","
				+ trainTopTarget.getSpeed3() + "," + trainTopTarget.getSpeed4()
				+ "," + trainTopTarget.getComfort0() + ","
				+ trainTopTarget.getComfort1() + ","
				+ trainTopTarget.getComfort2() + ","
				+ trainTopTarget.getComfort3() + ","
				+ trainTopTarget.getComfort4() + ","
				+ trainTopTarget.getComfort5() + ","
				+ trainTopTarget.getComfort6() + ","
				+ trainTopTarget.getSafty0() + "," + trainTopTarget.getSafty1()
				+ "," + trainTopTarget.getSafty2() + ","
				+ trainTopTarget.getSafty3() + "," + trainTopTarget.getSafty4()
				+ "," + trainCategoryId + ")";
		sqlHelper.update(sql, null);
	}
	
	/**
	 * 获取所有train_top_target数据
	 */
	public static TrainTopTarget getTrainTopTarget(int trainCategoryId){
		TrainTopTarget bean = new TrainTopTarget();
		String sql = "SELECT * FROM train_top_target WHERE train_category_id = " + trainCategoryId;
		List list = sqlHelper.query(sql, null);
		if(list.size() != 0){
			Object [] obj = (Object[]) list.get(0);//每个trainCategorId只对应一条记录
			bean.setId(Integer.parseInt(obj[0].toString()));
			bean.setSpeed0(Double.parseDouble(obj[1].toString()));
			bean.setSpeed1(Double.parseDouble(obj[2].toString()));
			bean.setSpeed2(Double.parseDouble(obj[3].toString()));
			bean.setSpeed3(Double.parseDouble(obj[4].toString()));
			bean.setSpeed4(Double.parseDouble(obj[5].toString()));
			bean.setComfort0(Double.parseDouble(obj[6].toString()));
			bean.setComfort1(Double.parseDouble(obj[7].toString()));
			bean.setComfort2(Double.parseDouble(obj[8].toString()));
			bean.setComfort3(Double.parseDouble(obj[9].toString()));
			bean.setComfort4(Double.parseDouble(obj[10].toString()));
			bean.setComfort5(Double.parseDouble(obj[11].toString()));
			bean.setComfort6(Double.parseDouble(obj[12].toString()));
			bean.setSafty0(Double.parseDouble(obj[13].toString()));
			bean.setSafty1(Double.parseDouble(obj[14].toString()));
			bean.setSafty2(Double.parseDouble(obj[15].toString()));
			bean.setSafty3(Double.parseDouble(obj[16].toString()));
			bean.setSafty4(Double.parseDouble(obj[17].toString()));
			bean.setTrainCategoryId(Integer.parseInt(obj[18].toString()));
		}
		double [] paras = getABCParameters(trainCategoryId);
		bean.setEnergy0(paras[0]); //a
		bean.setEnergy1(paras[1]); //b
		bean.setEnergy2(paras[2]); //c
		bean.setEnergy3(getAxleWeight(trainCategoryId));
		return bean;
	}
	
	/**
	 * 获取编组数据：SUM(carriageWeight), SUM(passenger), SUM(axleNum)
	 * @param trainCategoryId
	 * @return
	 */
	public static double getAxleWeight(int trainCategoryId){
		String sql = "SELECT SUM(carriageWeight), SUM(passenger), SUM(axleNum) FROM train_formation WHERE train_category_id = "+trainCategoryId;
		List list = sqlHelper.query(sql, null);
		double axleWeight = 0;
		double sumTrainWeight = 0, sumEmptyTrainWeight = 0, sumPassage = 0, sumAxle = 0;
		if(list.size() != 0){
			Object [] obj = (Object[]) list.get(0);
			sumEmptyTrainWeight = Double.parseDouble(obj[0].toString());
			sumPassage = Double.parseDouble(obj[1].toString());
			sumAxle = Double.parseDouble(obj[2].toString());
			sumTrainWeight = sumEmptyTrainWeight + sumPassage * 0.08;
			axleWeight = sumTrainWeight / sumAxle;
		}
		return MyTools.numFormat2(axleWeight);
	}
	
	/**
	 * 获取制动系数a, b, c
	 * @return 0,1,2分别为a,b,c
	 */
	public static double[] getABCParameters(int trainCategoryId){
		return TractionConfPanelService.getSomeParametersFromTrainEdit(trainCategoryId);
	}
	
	/**
	 * 获取所有编组
	 */
	public static ArrayList<TrainCategory> getTrainCategory(){
		return TrainEditPanelService.getAllTrainCategory();
	}

}
