package com.crh.service;

import java.util.ArrayList;
import java.util.List;

import com.crh2.dao.SQLHelper;
import com.crh2.javabean.TrainPowerSupply;
import com.crh2.util.MyTools;

/**
 * 为交流辅助负载提供增、删、改、查操作
 * @author huhui
 *
 */
public class AuxiliaryPowerSupplyPanelService {

	/**
	 * 提供统一的数据库增、删、改、查操作
	 */
	private static SQLHelper sqlHelper = new SQLHelper();
	
	/**
	 * @param trainCategoryId
	 * @return 列车编组总数
	 */
	public static int getTrainUnitNum(int trainCategoryId){
		int unitNum = 0;
		String sql = "SELECT COUNT(*) FROM train_formation WHERE train_category_id = "+trainCategoryId;
		List list = sqlHelper.query(sql, null);
		if(list.size() != 0){
			unitNum = Integer.parseInt(((Object [])list.get(0))[0].toString());
		}
		return unitNum;
	}
	
	/**
	 * 
	 * @param carNo
	 * @param trainCategoryId
	 * @return 通过车厢号和编组编号获取TrainPowerSupply
	 */
	public static TrainPowerSupply [] getTrainPowerSupply(int carNo, int trainCategoryId, String table){
		String sql = "SELECT * FROM "+table+" WHERE carNo = "+carNo+" AND train_category_id = "+trainCategoryId;
		List list = sqlHelper.query(sql, null);
		TrainPowerSupply [] beans = new TrainPowerSupply[list.size()];
		for(int i=0;i<list.size();i++){
			Object [] obj = (Object [])list.get(i);
			beans[i] = new TrainPowerSupply();
			beans[i].setLoad(obj[1].toString());
			beans[i].setWinter(Double.parseDouble(obj[2].toString()));
			beans[i].setSummer(Double.parseDouble(obj[3].toString()));
			beans[i].setCarNo(Integer.parseInt(obj[4].toString()));
			beans[i].setTrainCategoryId(Integer.parseInt(obj[5].toString()));
		}
		return beans;
	}
	
	/**
	 * @param trainCategoryId
	 * @param dataList
	 */
	public static void saveTrainPowerSupply(int trainCategoryId, ArrayList<TrainPowerSupply> dataList, String table){
		String deleteSQL = "delete from "+table+" where train_category_id = " + trainCategoryId;
		sqlHelper.update(deleteSQL, null);
		//批量插入
		ArrayList<String> sqlList = new ArrayList<String>();
		for(TrainPowerSupply bean : dataList){
			String insertSQL = "insert into "+table+" values(null,'"+bean.getLoad()+"',"+bean.getWinter()+","+bean.getSummer()+","+bean.getCarNo()+","+trainCategoryId+")";
			sqlList.add(insertSQL);
		}
		sqlHelper.batchInsert(sqlList);
	}
	
	/**
	 * 保存交流辅助负载参数
	 * @param trainCategoryId
	 * @param para0
	 * @param para1
	 * @param type 0为交流，1为直流
	 */
	public static void saveTrainPowerPara(int trainCategoryId, double para0, double para1, int type){
		String delSQL = "DELETE FROM train_power_para WHERE train_category_id = "+trainCategoryId+" AND type = "+type;
		sqlHelper.update(delSQL, null);
		String sql = "INSERT INTO train_power_para VALUES(NULL,"+para0+","+para1+","+type+","+trainCategoryId+")";
		sqlHelper.update(sql, null);
	}
	
	/**
	 * 获取交流辅助负载参数
	 * @param trainCategoryId
	 * @param type 0代表交流，1代表直流
	 * @return
	 */
	public static double [] getTrainPowerPara(int trainCategoryId, int type){
		double [] paras = new double[2];
		String sql = "SELECT para0,para1 FROM train_power_para WHERE train_category_id = "+trainCategoryId+" AND TYPE = "+type;
		List list = sqlHelper.query(sql, null);
		if(list.size() != 0){
			Object [] obj = (Object[]) list.get(0);
			paras[0] = Double.parseDouble(obj[0] == null?"1":obj[0].toString());
			paras[1] = Double.parseDouble(obj[1] == null?"1":obj[1].toString());
		}else{
			paras[0] = 1;
			paras[1] = 1;
		}
		return paras;
	}
	
	/**
	 * 计算交流和直流负载表的和
	 * @param trainCategoryId
	 * @param table
	 * @return
	 */
	public static double [] getSumTrainPowerPara(int trainCategoryId, String table){
		double [] paras = new double[2];
		String sql = "SELECT SUM(winter), SUM(summer) FROM "+table+" t WHERE train_category_id = "+trainCategoryId+" AND t.load != '总计'";
		List list = sqlHelper.query(sql, null);
		if(list.size() != 0){
			Object [] obj = (Object[]) list.get(0);
			paras[0] = Double.parseDouble(obj[0] == null?"1":obj[0].toString());
			paras[1] = Double.parseDouble(obj[1] == null?"1":obj[1].toString());
		}
		return paras;
	}
	
	/**
	 * 获取交流部分生成文档时需要的8个参数
	 * @param trainCategoryId
	 * @return
	 */
	public static double [] getDocPara(int trainCategoryId){
		double [] paras = new double[8];
		double val1=0,val2=0,val3=0,val4=0,val5=0,val6=0;
		int carCount = AuxiliaryPowerSupplyPanelService.getTrainUnitNum(trainCategoryId);//编组数目
		double [] sumPara = getSumTrainPowerPara(trainCategoryId, "train_power_supply");
		double [] powerPara = getTrainPowerPara(trainCategoryId, 0);
		val1 = sumPara[0];
		val4 = sumPara[1];
		val2 = (val1 * powerPara[1])/(powerPara[0]/100);
		val5 = (val4 * powerPara[1])/(powerPara[0]/100);
		val3 = Math.floor(carCount/2);
		val6 = Math.floor(carCount/2);
		paras[0] = MyTools.numFormat2(val1);
		paras[1] = MyTools.numFormat2(val2);
		paras[2] = MyTools.numFormat2(val3);
		paras[3] = MyTools.numFormat2(val4);
		paras[4] = MyTools.numFormat2(val5);
		paras[5] = MyTools.numFormat2(val6);
		paras[6] = MyTools.numFormat2(powerPara[0]);
		paras[7] = MyTools.numFormat2(powerPara[1]);
		return paras;
	}
	
	/**
	 * 获取直流部分生成文档时需要的11个参数
	 * @param trainCategoryId
	 * @return
	 */
	public static double [] getDocParaDC(int trainCategoryId){
		double [] paras = new double[11];
		double val1=0,val2=0,val3=0,val4=0,val5=0,val6=0,val7=0,val8=0,val9=0;
		int carCount = AuxiliaryPowerSupplyPanelService.getTrainUnitNum(trainCategoryId);//编组数目
		double [] sumPara = getSumTrainPowerPara(trainCategoryId, "train_power_supply_dc");
		double [] powerPara = getTrainPowerPara(trainCategoryId, 1);
		val1 = sumPara[0];
		val4 = sumPara[1];
		val2 = MyTools.numFormat2((val1 * powerPara[1])/(powerPara[0]/100));
		val5 = MyTools.numFormat2((val4 * powerPara[1])/(powerPara[0]/100));
		val3 = Math.floor(carCount/2) + 1;
		val6 = Math.floor(carCount/2) + 1;
		
		val7 = carCount * 40;
		val8 = val3;
		val9 = val7/val8;
		paras[0] = MyTools.numFormat2(val1);
		paras[1] = MyTools.numFormat2(val2);
		paras[2] = MyTools.numFormat2(val3);
		paras[3] = MyTools.numFormat2(val4);
		paras[4] = MyTools.numFormat2(val5);
		paras[5] = MyTools.numFormat2(val6);
		paras[6] = MyTools.numFormat2(val7);
		paras[7] = MyTools.numFormat2(val8);
		paras[8] = MyTools.numFormat2(val9);
		paras[9] = MyTools.numFormat2(powerPara[0]);
		paras[10] = MyTools.numFormat2(powerPara[1]);
		
		return paras;
	}

}
