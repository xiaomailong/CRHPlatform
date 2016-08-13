/**
 * 计算分相区数据，并写入数据库
 */
package com.crh2.test;

import com.crh2.dao.SQLHelper;

public class CalSectionData {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CalSectionData csd = new CalSectionData();
		System.out.println("计算开始");
		csd.CalAndWrite();
		System.out.println("写入完成");
	}
	
	//计算分相区数据，并存入数据中
	public void CalAndWrite(){
		double start = 0,end = 0,length = 115219;
		String sql = "";
		SQLHelper sqlHelper = new SQLHelper();
		while(end<=length){
			start = end;
			end = start + 7000;//有电
			sql = "insert into sectiondata values(null,"+start+","+end+",1)";
			//插入数据库
			sqlHelper.update(sql, null);
			start = end;
			end = start + 190;//没电
			sql = "insert into sectiondata values(null,"+start+","+end+",0)";
			//插入数据库
			sqlHelper.update(sql, null);
		}
	}

}
