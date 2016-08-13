package com.crh.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.crh.view.dialog.RouteDataTrainNumDialog;
import com.crh2.dao.SQLHelper;
import com.crh2.javabean.StationInfo;
import com.crh2.javabean.StationStoptimePair;
import com.crh2.javabean.TrainRouteName;
import com.crh2.javabean.TrainRouteTrainnum;
import com.crh2.javabean.TrainStopStation;
import com.crh2.service.DataService;

/**
 * “线路数据”的数据库增、删、改、查操作
 * @author huhui
 */
public class RouteDataManagementDialogService {

    private static SQLHelper sqlHelper = new SQLHelper();

    /**
     * 保存线路名称
     * @param routeName
     * @return 保存线路名称
     * @throws SQLException
     */
    public static boolean saveRouteName(String routeName) throws SQLException {
        boolean b = false;
        String sql = "SELECT * FROM train_route_name WHERE routeName = '" + routeName + "'";
        List list = sqlHelper.query(sql, null);
        if (list.size() == 1) {
            throw new SQLException("路线名重复，请重新输入");
        }
        String insertSQL = "insert into train_route_name values(null, '" + routeName + "')";
        b = sqlHelper.update(insertSQL, null);
        return b;
    }

    /**
     * 获取线路名称
     * @return 所有TrainRouteName
     */
    public static ArrayList<TrainRouteName> getTrainRouteName() {
        ArrayList<TrainRouteName> routeList = new ArrayList<TrainRouteName>();
        String sql = "SELECT * FROM train_route_name";
        List list = sqlHelper.query(sql, null);
        if (list.size() != 0) {
            for (int i = 0; i < list.size(); i++) {
                Object[] obj = (Object[]) list.get(i);
                TrainRouteName bean = new TrainRouteName(Integer.parseInt(obj[0].toString()), obj[1].toString());
                routeList.add(bean);
            }
        }
        return routeList;
    }

    /**
     * 根据线路名称和车号获取该线路对应的列车车号
     * @param routeName
     * @param trainNum
     * @return
     */
    public static ArrayList<TrainRouteTrainnum> getTrainNum(String routeName, String trainNum) {
        ArrayList<TrainRouteTrainnum> beanList = new ArrayList<TrainRouteTrainnum>();
        String num = trainNum;
        String sql = "SELECT * FROM train_route_trainnum WHERE routeName = '" + routeName + "'";
        if (!num.equals(RouteDataTrainNumDialog.DEFAULTNUM + "")) {
            sql += "AND trainNum = '" + trainNum + "'";
        }
        List list = sqlHelper.query(sql, null);
        for (int i = 0; i < list.size(); i++) {
            Object[] obj = (Object[]) list.get(i);
            TrainRouteTrainnum bean = new TrainRouteTrainnum();
            bean.setRouteName(obj[1].toString());
            bean.setSerialNum(Integer.parseInt(obj[2].toString()));
            bean.setRouteNum(Integer.parseInt(obj[3].toString()));
            bean.setDirection(Integer.parseInt(obj[4].toString()));
            bean.setStationName(obj[5].toString());
            bean.setRunTime(Double.parseDouble(obj[6].toString()));
            //			bean.setStopTime(Double.parseDouble(obj[7].toString()));
            bean.setTrainNum(obj[8].toString());
            beanList.add(bean);
        }
        return beanList;
    }

    /**
     * 保存“车次设置”
     * @param sqlList
     */
    public static void saveTrainNum(String routeName, String trainNum, ArrayList<TrainRouteTrainnum> beanList) {
        String num = trainNum;
        //删除数据
        String deleteSQL = "DELETE FROM train_route_trainnum WHERE routeName = '" + routeName + "' ";
        if (!num.equals(RouteDataTrainNumDialog.DEFAULTNUM + "")) {//所有车次
            deleteSQL += "AND trainNum = '" + trainNum + "'";
        }
        sqlHelper.update(deleteSQL, null);
        //插入新的数据
        ArrayList<String> sqlList = new ArrayList<String>();
        for (TrainRouteTrainnum bean : beanList) {
            String sql = "insert into train_route_trainnum values(null,'" + bean.getRouteName() + "'," + bean.getSerialNum() + "," + bean.getRouteNum() + ","
                         + bean.getDirection() + "," + bean.getStationName() + "," + bean.getRunTime() + "," + bean.getStopTime() + ",'" + bean.getTrainNum()
                         + "')";
            sqlList.add(sql);
        }
        sqlHelper.batchInsert(sqlList);
    }

    /**
     * 从train_route_trainnum获取不重复的车次trainnum
     * @return
     */
    public static ArrayList<String> getTrainNum() {
        ArrayList<String> trainNumList = new ArrayList<String>();
        String sql = "SELECT DISTINCT trainNum FROM train_route_trainnum";
        List list = sqlHelper.query(sql, null);
        if (list.size() != 0) {
            for (int i = 0; i < list.size(); i++) {
                Object[] obj = (Object[]) list.get(i);
                String str = obj[0].toString();
                trainNumList.add(str);
            }
        }
        return trainNumList;
    }

    /**
     * 保存线路条件数据，包括车站信息、分相区、坡道数据、弯道数据、限速数据
     * @param routeId
     * @param sqlList
     */
    public static void saveRouteData(int routeId, ArrayList<String> sqlList) {
        String deleteStation = "DELETE FROM train_route_station WHERE train_route_id = " + routeId;
        sqlHelper.update(deleteStation, null);
        String deleteSection = "DELETE FROM train_route_section WHERE train_route_id = " + routeId;
        sqlHelper.update(deleteSection, null);
        String deleteSlope = "DELETE FROM train_route_slope WHERE train_route_id = " + routeId;
        sqlHelper.update(deleteSlope, null);
        String deleteCurve = "DELETE FROM train_route_curve WHERE train_route_id = " + routeId;
        sqlHelper.update(deleteCurve, null);
        String deleteSpeedLimit = "DELETE FROM train_route_speed_limit WHERE train_route_id = " + routeId;
        sqlHelper.update(deleteSpeedLimit, null);
        sqlHelper.batchInsert(sqlList);
    }

    /**
     * 通过线路名删除线路
     * @param routeName
     */
    public static boolean deleteRoute(String routeName) {
        boolean b = false;
        String sql = "DELETE FROM train_route_name WHERE routeName = '" + routeName + "'";
        b = sqlHelper.update(sql, null);
        return b;
    }

    /**
     * 通过线路名判断该线路对应的弯道数据是否已经导入
     * @param routeName
     * @return true代表已经导入了，不再需要重新导入
     */
    public static boolean hasImported(String routeName) {
        boolean b = false;
        int id = getRouteIdByName(routeName);
        String sql = "SELECT COUNT(*) FROM train_route_curve WHERE train_route_id = " + id;//通过train_route_curve表判断是否需要导入数据
        List list = sqlHelper.query(sql, null);
        int count = Integer.parseInt(((Object[]) sqlHelper.query(sql, null).get(0))[0].toString());
        if (count != 0) {
            b = true;
        }
        return b;
    }

    /**
     * 根据route name获取route id
     * @param routeName
     * @return
     */
    public static int getRouteIdByName(String routeName) {
        int id = 0;
        String sql = "select id from train_route_name where routeName = '" + routeName + "'";
        id = Integer.parseInt(((Object[]) sqlHelper.query(sql, null).get(0))[0].toString());
        return id;
    }

    /**
     * 获取停站信息
     * @param trainRouteId
     * @param trainNum
     * @return
     */
    public static ArrayList<TrainStopStation> getStationStopTime(int trainRouteId, String routeName, String trainNum) {
        ArrayList<TrainStopStation> stopList = new ArrayList<TrainStopStation>();
        String sql = "SELECT * FROM train_stop_station WHERE train_route_id = " + trainRouteId + " AND trainNum = '" + trainNum + "'";
        List list = sqlHelper.query(sql, null);
        if (list.size() == 0) {//尚未设置停站时间
            DataService ds = new DataService();
            ArrayList<StationInfo> stationList = ds.getStationInfoData(trainRouteId);
            for (StationInfo bean : stationList) {
                TrainStopStation stopBean = new TrainStopStation();
                stopBean.setTrainRouteId(trainRouteId);
                stopBean.setRouteName(routeName);
                stopBean.setStationId(bean.getStationId());
                stopBean.setStationName(bean.getStationName());
                stopBean.setTrainNum(trainNum);
                stopList.add(stopBean);
            }
        } else {//已设置停站时间，只需读取出来
            for (int i = 0; i < list.size(); i++) {
                Object[] obj = (Object[]) list.get(i);
                TrainStopStation stopBean = new TrainStopStation();
                stopBean.setTrainRouteId(Integer.parseInt(obj[1].toString()));
                stopBean.setRouteName((String) obj[2]);
                stopBean.setStationId(Integer.parseInt(obj[3].toString()));
                stopBean.setStationName((String) obj[4]);
                stopBean.setTrainNum((String) obj[5]);
                stopBean.setStopTIme(Double.parseDouble(obj[6].toString()));
                stopList.add(stopBean);
            }
        }
        return stopList;
    }

    /**
     * 保存车站停站数据信息
     * @param routeId
     * @param trainNum
     * @param stopList
     */
    public static void saveStationStopTime(int routeId, String trainNum, ArrayList<TrainStopStation> stopList) {
        //删除原有数据
        String delSQL = "DELETE FROM train_stop_station WHERE train_route_id = " + routeId + " AND trainNum = '" + trainNum + "'";
        sqlHelper.update(delSQL, null);
        //保存新数据
        ArrayList<String> sqlList = new ArrayList<String>();
        for (TrainStopStation bean : stopList) {
            String sql = "INSERT INTO train_stop_station VALUES(NULL," + bean.getTrainRouteId() + ",'" + bean.getRouteName() + "'," + bean.getStationId()
                         + ",'" + bean.getStationName() + "','" + bean.getTrainNum() + "'," + bean.getStopTIme() + ")";
            sqlList.add(sql);
        }
        sqlHelper.batchInsert(sqlList);
    }

    /**
    * 获取车次设置中的运行时间和停站时间，为生成文档做准备
    * @param routeName
    * @param trainNum
    * @return
    */
    public static double[] getRuntime(String routeName, String trainNum) {
        double[] para = new double[1];
        String sql = "SELECT runTime FROM train_route_trainnum WHERE routeName = '" + routeName + "' AND trainNum = '" + trainNum + "'";
        List list1 = sqlHelper.query(sql, null);
        if (list1.size() != 0) {
            para[0] = Double.parseDouble(((Object[]) list1.get(0))[0].toString());
        }
        return para;
    }

    public static StationStoptimePair[] getStationStoptimePairArray(String routeName, String trainNum) {
        StationStoptimePair[] sspArray = new StationStoptimePair[14];
        for (int i = 0; i < sspArray.length; i++) {
            sspArray[i] = new StationStoptimePair();
        }
        String sql = "SELECT stationName,stopTime FROM train_stop_station t WHERE routeName='" + routeName + "' AND trainNum='" + trainNum
                     + "' ORDER BY stationId";
        List list = sqlHelper.query(sql, null);
        if (list.size() != 0) {
            for (int i = 0; i < list.size(); i++) {
                StationStoptimePair pair = sspArray[i];
                Object[] obj = (Object[]) list.get(i);
                pair.setStationName((String) obj[0]);
                pair.setStoptime(obj[1] + "");
            }

        }
        return sspArray;
    }

}
