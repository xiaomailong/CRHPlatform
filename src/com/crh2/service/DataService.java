package com.crh2.service;

import java.util.ArrayList;
import java.util.List;

import com.crh2.dao.SQLHelper;
import com.crh2.javabean.Curve;
import com.crh2.javabean.Rundata;
import com.crh2.javabean.Section;
import com.crh2.javabean.Serverinfo;
import com.crh2.javabean.Slope;
import com.crh2.javabean.SpecialSpeedLimitPoint;
import com.crh2.javabean.StationInfo;

/**
 * 获取数据库中的数据
 * @author huhui
 *
 */
public class DataService {
	
	private SQLHelper sqlHelper = new SQLHelper();

	/**
	 *  获取坡度数据
	 * @param routeId
	 * @return
	 */
	public ArrayList<Slope> getSlopeData(int routeId) {
		String sql = "select * from train_route_slope where train_route_id = "+routeId;
		List list = sqlHelper.query(sql, null);
		ArrayList<Slope> slopeList = new ArrayList<Slope>();
		for (int i = 0; i < list.size(); i++) {
			Object object[] = (Object[]) list.get(i);
			Slope slope = new Slope();
			slope.setId(Integer.parseInt(object[0].toString()));
			slope.setSlopeId(Integer.parseInt(object[1].toString()));
			slope.setSlope(Double.parseDouble(object[2].toString()));
			slope.setLength(Double.parseDouble(object[3].toString()));
			slope.setEnd(Double.parseDouble(object[4].toString()));
			slope.setHeight(Double.parseDouble(object[5].toString()));
			slopeList.add(slope);
		}
		return slopeList;
	}

	/**
	 *  获取弯道数据
	 * @param routeId
	 * @return
	 */
	public ArrayList<Curve> getCurveData(int routeId) {
		String sql = "select * from train_route_curve where train_route_id = "+routeId;
		List list = sqlHelper.query(sql, null);
		ArrayList<Curve> curveList = new ArrayList<Curve>();
		for (int i = 0; i < list.size(); i++) {
			Object object[] = (Object[]) list.get(i);
			Curve curve = new Curve();
			curve.setCurveId(Integer.parseInt(object[1].toString()));
			curve.setRadius(Double.parseDouble(object[2].toString()));
			curve.setStart(Double.parseDouble(object[3].toString()));
			curve.setLength(Double.parseDouble(object[4].toString()));
			curve.setSpeedLimit(Double.parseDouble(object[5].toString()));
			curveList.add(curve);
		}
		return curveList;
	}
	
	/**
	 * 获取stationinfo数据
	 * @param routeId
	 * @return
	 */
	public ArrayList<StationInfo> getStationInfoData(int routeId){
		String sql = "SELECT * FROM train_route_station WHERE train_route_id="+routeId;
		List list = sqlHelper.query(sql, null);
		ArrayList<StationInfo> stationInfoList = new ArrayList<StationInfo>();
		for(int i=0;i<list.size();i++){
			Object object[] = (Object[]) list.get(i);
			StationInfo stationInfo = new StationInfo();
			stationInfo.setId(Integer.parseInt(object[0].toString()));
			stationInfo.setStationId(Integer.parseInt(object[1].toString()));
			stationInfo.setStationName(object[2].toString());
			stationInfo.setLocation(Double.parseDouble(object[3].toString()));
			stationInfo.setRouteId(Integer.parseInt(object[4].toString()));
			stationInfoList.add(stationInfo);
		}
		return stationInfoList;
	}
	
	/**
	 * 获取section数据
	 * @param routeId
	 * @return
	 */
	public ArrayList<Section> getSectionData(int routeId){
		String sql = "SELECT * FROM train_route_section WHERE train_route_id="+routeId;
		List list = sqlHelper.query(sql, null);
		ArrayList<Section> sectionList = new ArrayList<Section>();
		for(int i=0;i<list.size();i++){
			Object obj[] = (Object[]) list.get(i);
			Section section = new Section();
			section.setSection_id(Integer.parseInt(obj[1].toString()));
			section.setStart(Double.parseDouble(obj[2].toString()));
			section.setEnd(Double.parseDouble(obj[3].toString()));
			section.setElectricity(Integer.parseInt(obj[4].toString()));
			sectionList.add(section);
		}
		return sectionList;
	}
	
	/**
	 * 获取train_route_speed_limit数据
	 * @param routeId
	 * @return
	 */
	public ArrayList<SpecialSpeedLimitPoint> getSpeedLimitData(int routeId){
		String sql = "SELECT * FROM train_route_speed_limit WHERE train_route_id = "+routeId;
		List list = sqlHelper.query(sql, null);
		ArrayList<SpecialSpeedLimitPoint> speedLimitList = new ArrayList<SpecialSpeedLimitPoint>();
		for(int i=0;i<list.size();i++){
			Object obj[] = (Object[]) list.get(i);
			SpecialSpeedLimitPoint point = new SpecialSpeedLimitPoint();
			point.setPointId(Integer.parseInt(obj[1].toString()));
			point.setStart(Double.parseDouble(obj[2].toString()));
			point.setEnd(Double.parseDouble(obj[3].toString()));
			point.setSpeedLimit(Double.parseDouble(obj[4].toString()));
			speedLimitList.add(point);
		}
		return speedLimitList;
	}
	
	/**
	 * 获取第一个站名
	 * @param routeId
	 * @return
	 */
	public String getFirstStationName(int routeId){
		String sql = "SELECT t.station_name FROM train_route_station t WHERE t.train_route_id = "+routeId;
		List list = sqlHelper.query(sql, null);
		return ((Object[]) list.get(0))[0].toString();
	}
	
	/**
	 * 获取车站的停站时间
	 */
	public double getStopTime(String routeName, String trainNum){
		String sql = "SELECT stopTime FROM train_route_trainnum WHERE	routeName = '"+routeName+"' AND trainNum = '"+trainNum+"'";
		List list = sqlHelper.query(sql, null);
		return Double.parseDouble(((Object[]) list.get(0))[0].toString());
	}
	
	/**
	 * 获取serverinfo
	 * @return
	 */
	public Serverinfo getServerinfo(){
		String sql = "SELECT * FROM serverinfo LIMIT 0,1";
		List list = sqlHelper.query(sql, null);
		Object object[] = (Object[]) list.get(0);
		Serverinfo serverinfo = new Serverinfo();
		serverinfo.setPort(object[1].toString());
		serverinfo.setSendToPort(object[2].toString());
		serverinfo.setSendToIp(object[3].toString());
		serverinfo.setIndex1(object[4].toString());
		serverinfo.setIndex2(object[5].toString());
		serverinfo.setIndex3(object[6].toString());
		serverinfo.setIndex4(object[7].toString());
		serverinfo.setIndex5(object[8].toString());
		serverinfo.setIndex6(object[9].toString());
		serverinfo.setIndex7(object[10].toString());
		serverinfo.setIndex8(object[11].toString());
		serverinfo.setPrefix1(object[12].toString());
		serverinfo.setPrefix2(object[13].toString());
		serverinfo.setPrefix3(object[14].toString());
		serverinfo.setPrefix4(object[15].toString());
		serverinfo.setPrefix5(object[16].toString());
		serverinfo.setPrefix6(object[17].toString());
		serverinfo.setPrefix7(object[18].toString());
		serverinfo.setPrefix8(object[19].toString());
		return serverinfo;
	}
	
	/**
	 * 2015.3.15 保存serverinfo
	 * @param bean
	 */
	public void saveServerinfo(Serverinfo bean){
		String delSQL = "DELETE FROM serverinfo";
		sqlHelper.update(delSQL, null);
		String sql = "INSERT INTO serverinfo VALUES(NULL, '" + bean.getPort()
				+ "', '" + bean.getSendToPort() + "', '" + bean.getSendToIp()
				+ "', '" + bean.getIndex1() + "', '" + bean.getIndex2()
				+ "', '" + bean.getIndex3() + "', '" + bean.getIndex4()
				+ "', '" + bean.getIndex5() + "','" + bean.getIndex6() + "', '"
				+ bean.getIndex7() + "', '" + bean.getIndex8() + "', '"
				+ bean.getPrefix1() + "', '" + bean.getPrefix2() + "', '"
				+ bean.getPrefix3() + "', '" + bean.getPrefix4() + "', '"
				+ bean.getPrefix5() + "', '" + bean.getPrefix6() + "', '"
				+ bean.getPrefix7() + "', '" + bean.getPrefix8() + "')";
		sqlHelper.update(sql, null);
	}

	/**
	 * 获取rundata
	 * @param routeId
	 * @return
	 */
	public List<Rundata> getRundata(int routeId) {
		String sql = "select * from rundata where routeid="+routeId+"";
		List list = sqlHelper.query(sql, null);
		List<Rundata> rundataList = new ArrayList<Rundata>();
		for (int i = 0; i < list.size(); i++) {
			Object object[] = (Object[]) list.get(i);
			Rundata rundata = new Rundata();
			rundata.setId(Integer.parseInt(object[0].toString()));
			rundata.setRuntime(Double.parseDouble(object[1].toString()));
			rundata.setSpeed(Double.parseDouble(object[2].toString()));
			rundata.setDistance(Double.parseDouble(object[3].toString()));
			rundata.setCp(Double.parseDouble(object[4].toString()));
			rundataList.add(rundata);
		}
		return rundataList;
	}

}
