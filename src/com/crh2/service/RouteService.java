package com.crh2.service;

import java.util.ArrayList;
import java.util.List;

import com.crh2.dao.SQLHelper;
import com.crh2.javabean.RouteName;

/**
 * 提供关于线路方面的方法
 */
public class RouteService {

	private static SQLHelper sqlHelper = new SQLHelper();
	
	/**
	 * 根据route的id号，删除route
	 * @param routesIdList
	 */
	public static void deleteSelectedRoutes(ArrayList<Integer> routesIdList){
		//组装sql语句
		String sql = "DELETE FROM routename WHERE id IN (";
		for(int i=0;i<routesIdList.size();i++){
			sql += routesIdList.get(i) + ",";
		}
		//去除最后一个逗号，并加上另一半括号
		sql = sql.substring(0, sql.length() - 1) + ")";
		sqlHelper.update(sql, null);
	}

	/**
	 * 获取数据库中所有的线路名称
	 * @return
	 */
	public static List<RouteName> getAllRouteName() {
		String sql = "SELECT * FROM routename";
		List list = sqlHelper.query(sql, null);
		List<RouteName> routeList = new ArrayList<RouteName>();
		for (int i = 0; i < list.size(); i++) {
			Object[] obj = (Object[]) list.get(i);
			RouteName routeName = new RouteName();
			routeName.setId(Integer.parseInt(obj[0].toString()));
			routeName.setName(obj[1].toString());
			routeList.add(routeName);
		}
		return routeList;
	}

}
