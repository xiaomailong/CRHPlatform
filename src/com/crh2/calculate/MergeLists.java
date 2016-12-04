package com.crh2.calculate;

import java.util.ArrayList;
import java.util.Collections;

import com.crh.calculation.mintime.AirFrictionMinTime;
import com.crh.calculation.mintime.BrakeForceMinTime;
import com.crh.calculation.mintime.TractionForceMinTime;
import com.crh2.dao.SQLHelper;
import com.crh2.javabean.Rundata;
import com.crh2.javabean.TrainStopStation;
import com.crh2.util.WriteIntoTxt;

/**
 * 合并两个List，即合并rundataBackBeansList和rundataBeansList
 * @author huhui
 *
 */
public class MergeLists {

    /**
     * 几个List针对min time
     */
    private ArrayList<ArrayList<Rundata>> mtForwardList        = null, mtReverseList = null;
    private ArrayList<TrainStopStation>   trainStopStationList = null;
    /**
     * 中间站所有停站的车站信息
     */
    private ArrayList<TrainStopStation>   stopStationList      = null;
    /**
     * 几个List针对fixed time
     */
    private ArrayList<Rundata>            ftForwardList        = null, ftReverseList = null;
    /**
     * 几个List针对specified time energy
     */
    private ArrayList<Rundata>            stEnergyForwardList  = null, stEnergyReverseList = null;
    /**
     * 合并之后的数据存于rundataMergedList中
     */
    public static ArrayList<Rundata>      mtRundataMergedList  = null, rtEnergyRundataMergedList = null, ftRundataMergedList = null;
    private SQLHelper                     sqlHelper            = new SQLHelper();

    /**
     * 判断两个数是否相等的常量
     */
    private final double                  FACTOR               = 2.7;

    /**
     * 构造函数，初始化变量（针对fixed time）
     */
    public MergeLists(int routeId, ArrayList<Rundata> forwardList, ArrayList<Rundata> reverseList) {
        this.ftForwardList = forwardList;
        this.ftReverseList = reverseList;
        ftRundataMergedList = new ArrayList<Rundata>();
        fixedTimeListsMerger();
    }

    /**
     * 构造函数，初始化变量（针对specified time energy）
     */
    public MergeLists(ArrayList<Rundata> forwardList, ArrayList<Rundata> reverseList, int routeId) {
        this.stEnergyForwardList = forwardList;
        this.stEnergyReverseList = reverseList;
        rtEnergyRundataMergedList = new ArrayList<Rundata>();
        specifiedTimeListsMerger();
    }

    /**
     * 构造函数，初始化变量（针对min time）
     */
    public MergeLists(ArrayList<ArrayList<Rundata>> forwardList, ArrayList<ArrayList<Rundata>> reverseList, ArrayList<TrainStopStation> stationList, int routeId) {
        this.mtForwardList = forwardList;
        this.mtReverseList = reverseList;
        Collections.reverse(this.mtReverseList);// 反转
        this.trainStopStationList = stationList;
        this.stopStationList = this.removeNoStopStation(stationList);
        mtRundataMergedList = new ArrayList<Rundata>(2000);
        minTimeListsMerger();
    }

    /**
     * fixed time 合并数据 2014.11.3
     */
    public void fixedTimeListsMerger() {
        //合并正向数据
        for (Rundata bean : ftForwardList) {
            bean.setTractionForce(bean.getTractionForce() / 1000.0);
            bean.setAirForce(AirFrictionMinTime.getAirFriction(bean.getSpeed()) / 1000.0);
            bean.setOtherForce(Math.abs(BrakeForceMinTime.getOtherForce(bean.getCp()) / 1000.0));
            ftRundataMergedList.add(bean);
        }

        double E = 0;
        //寻找正向数据，从后往前第一个power不为0的值
        for (int k = ftForwardList.size() - 1; k >= 0; k--) {
            double tractionPower = ftForwardList.get(k).getTractionPower();
            if (tractionPower != 0) {
                E = tractionPower;
                break;
            }
        }
        ArrayList<Double> tempComBrakePowerList = new ArrayList<Double>();
        ArrayList<Double> tempElecBrakePowerList = new ArrayList<Double>();
        for (Rundata bean : ftReverseList) {
            tempComBrakePowerList.add(bean.getElecBrakeForcePower());
            tempElecBrakePowerList.add(bean.getElecBrakeForcePower());
        }
        Collections.reverse(ftReverseList);
        for (int i = 0; i < ftReverseList.size(); i++) {
            Rundata bean = ftReverseList.get(i);
            bean.setAirForce(AirFrictionMinTime.getAirFriction(bean.getSpeed()) / 1000.0);
            bean.setBrakeForce(BrakeForceMinTime.getManulBrakingForce(bean.getSpeed()) / 1000.0);
            bean.setEleBrakeForce(BrakeForceMinTime.getElecBrakeForce(bean.getSpeed()) / 1000.0);
            bean.setOtherForce(Math.abs(BrakeForceMinTime.getOtherForce(bean.getCp()) / 1000.0));
            bean.setElecBrakeForcePower(tempElecBrakePowerList.get(i));
            double comBrakePower = tempComBrakePowerList.get(i);
            bean.setPower(E - 0.85 * comBrakePower);
            ftRundataMergedList.add(bean);
        }
        for (int time = 0; time < ftRundataMergedList.size(); time++) {
            ftRundataMergedList.get(time).setRuntime(time);
        }
    }

    /**
     * specified time energy 合并数据 2014.10.25
     */
    public void specifiedTimeListsMerger() {
        //合并正向数据
        for (Rundata bean : stEnergyForwardList) {
            //            bean.setTractionForce(TractionForceMinTime.getTractionForce(bean.getSpeed()) / 1000.0);
            bean.setAirForce(AirFrictionMinTime.getAirFriction(bean.getSpeed()) / 1000.0);
            bean.setOtherForce(Math.abs(BrakeForceMinTime.getOtherForce(bean.getCp()) / 1000.0));
            rtEnergyRundataMergedList.add(bean);
        }

        double E = 0;
        //寻找正向数据，从后往前第一个power不为0的值
        for (int k = stEnergyForwardList.size() - 1; k >= 0; k--) {
            double tractionPower = stEnergyForwardList.get(k).getTractionPower();
            if (tractionPower != 0) {
                E = tractionPower;
                break;
            }
        }
        ArrayList<Double> tempComBrakePowerList = new ArrayList<Double>();
        ArrayList<Double> tempElecBrakePowerList = new ArrayList<Double>();
        for (Rundata bean : stEnergyReverseList) {
            tempComBrakePowerList.add(bean.getComBrakeForcePower());
            tempElecBrakePowerList.add(bean.getElecBrakeForcePower());
        }
        Collections.reverse(stEnergyReverseList);
        for (int i = 0; i < stEnergyReverseList.size(); i++) {
            Rundata bean = stEnergyReverseList.get(i);
            bean.setAirForce(AirFrictionMinTime.getAirFriction(bean.getSpeed()) / 1000.0);
            bean.setBrakeForce(BrakeForceMinTime.getManulBrakingForce(bean.getSpeed()) / 1000.0);
            bean.setEleBrakeForce(BrakeForceMinTime.getElecBrakeForce(bean.getSpeed()) / 1000.0);
            bean.setOtherForce(Math.abs(BrakeForceMinTime.getOtherForce(bean.getCp()) / 1000.0));
            bean.setElecBrakeForcePower(tempElecBrakePowerList.get(i));
            double comBrakePower = tempComBrakePowerList.get(i);
            bean.setPower(E - 0.85 * comBrakePower);
            rtEnergyRundataMergedList.add(bean);
        }
        for (int time = 0; time < rtEnergyRundataMergedList.size(); time++) {
            rtEnergyRundataMergedList.get(time).setRuntime(time);
        }
    }

    /**
     * 合并ForwardList和ReverseList
     */
    public void listsMerger() {
        int[] flag = findIndex();
        double power = stEnergyForwardList.get(flag[0]).getPower();
        if (flag[0] != 0 && flag[1] != 0) {
            for (int m = 0; m <= flag[0]; m++) {// 正向数据
                Rundata bean = stEnergyForwardList.get(m);
                bean.setTractionForce(TractionForceMinTime.getTractionForce(bean.getSpeed()) / 1000.0);
                bean.setAirForce(AirFrictionMinTime.getAirFriction(bean.getSpeed()) / 1000.0);
                bean.setOtherForce(Math.abs(BrakeForceMinTime.getOtherForce(bean.getCp()) / 1000.0));
                mtRundataMergedList.add(bean);
            }
            for (int n = flag[1]; n >= 0; n--) {// 反向数据
                Rundata bean = stEnergyReverseList.get(n);
                power -= bean.getPower();// 计算能量
                bean.setPower(power);
                bean.setAirForce(AirFrictionMinTime.getAirFriction(bean.getSpeed()) / 1000.0);
                bean.setBrakeForce(BrakeForceMinTime.getManulBrakingForce(bean.getSpeed()) / 1000.0);
                bean.setEleBrakeForce(BrakeForceMinTime.getElecBrakeForce(bean.getSpeed()) / 1000.0);
                bean.setOtherForce(Math.abs(BrakeForceMinTime.getOtherForce(bean.getCp()) / 1000.0));
                mtRundataMergedList.add(bean);
            }
        } else {
            System.out.println("合并forwardList和reverseList失败");
        }
        for (int time = 0; time < mtRundataMergedList.size(); time++) {
            mtRundataMergedList.get(time).setRuntime(time);
        }
    }

    /**
     * 寻找合并数据的点
     */
    public int[] findIndex() {
        ArrayList<Rundata> forwardBeans = stEnergyForwardList;
        ArrayList<Rundata> reverseBeans = stEnergyReverseList;
        int[] flag = new int[2];
        double S = TrainAttribute.CRH_DESTINATION_LOCATION;//全程距离
        for (int m = 0; m < forwardBeans.size(); m++) {
            Rundata fBean = forwardBeans.get(m);
            for (int n = 0; n < reverseBeans.size(); n++) {
                Rundata rBean = reverseBeans.get(n);
                if ((Math.abs(fBean.getSpeed() - rBean.getSpeed()) <= FACTOR) && (Math.abs(S - fBean.getLocation() - rBean.getDistance()) <= (FACTOR / 60))) {
                    flag[0] = m;// 正向
                    flag[1] = n;// 反向
                    // System.out.println(index+"合并成功  p-->"+m+", q-->"+n);
                    // System.out.println("fSpeed()="+fBean.getSpeed()+",rSpeed()="+rBean.getSpeed()+", S="+S+", fDistance()="+fBean.getDistance()+", rDistance()="+rBean.getDistance());
                    // break;
                    return flag;
                }
            }
        }
        return flag;
    }

    /**
     * min time 合并forwardList和reverseList
     */
    public void minTimeListsMerger() {
        for (int i = 0; i < mtForwardList.size(); i++) {
            int[] flag = minTimeFindIndex(i);
            double power = mtForwardList.get(i).get(flag[0]).getTractionPower();

            if (flag[0] != 0 && flag[1] != 0) {
                for (int m = 0; m <= flag[0]; m++) {// 正向数据
                    Rundata bean = mtForwardList.get(i).get(m);
                    bean.setTractionForce(TractionForceMinTime.getTractionForce(bean.getSpeed()) / 1000.0);
                    bean.setAirForce(AirFrictionMinTime.getAirFriction(bean.getSpeed()) / 1000.0);
                    bean.setOtherForce(Math.abs(BrakeForceMinTime.getOtherForce(bean.getCp()) / 1000.0));
                    mtRundataMergedList.add(bean);
                }
                double[] reverseElecBrakePower = new double[flag[1] + 1];
                for (int k = 0; k <= flag[1]; k++) {
                    Rundata bean = mtReverseList.get(i).get(k);
                    reverseElecBrakePower[k] = bean.getElecBrakeForcePower();
                }
                double reversePower = 0;
                for (int n = flag[1]; n >= 0; n--) {// 反向数据
                    Rundata bean = mtReverseList.get(i).get(n);
                    Rundata rBean = mtReverseList.get(i).get(flag[1] - n);
                    reversePower = power - rBean.getComBrakeForcePower();// 计算能量
                    bean.setPower(reversePower);
                    bean.setElecBrakeForcePower(reverseElecBrakePower[flag[1] - n]);
                    bean.setAirForce(AirFrictionMinTime.getAirFriction(bean.getSpeed()) / 1000.0);
                    bean.setBrakeForce(BrakeForceMinTime.getManulBrakingForce(bean.getSpeed()) / 1000.0);
                    bean.setEleBrakeForce(BrakeForceMinTime.getElecBrakeForce(bean.getSpeed()) / 1000.0);
                    bean.setOtherForce(Math.abs(BrakeForceMinTime.getOtherForce(bean.getCp()) / 1000.0));
                    mtRundataMergedList.add(bean);
                }
                if (i != mtForwardList.size() - 1) {// 最后一个站不需要停站，其他站都需要停
                    double location = mtReverseList.get(i).get(0).getLocation();
                    for (int k = 0; k < stopStationList.get(i).getStopTIme() - 1; k++) {
                        Rundata bean = new Rundata();
                        bean.setLocation(location);
                        bean.setPower(reversePower);
                        mtRundataMergedList.add(bean);// 车站停车
                    }
                }
            } else {
                System.out.println("合并forwardList和reverseList失败");
            }
        }
        for (int time = 0; time < mtRundataMergedList.size(); time++) {
            mtRundataMergedList.get(time).setRuntime(time);
        }
    }

    /**
     * min time 寻找合并数据的点
     * @param index
     * @return
     */
    public int[] minTimeFindIndex(int index) {
        ArrayList<Rundata> forwardBeans = mtForwardList.get(index);
        ArrayList<Rundata> reverseBeans = mtReverseList.get(index);
        int[] flag = new int[2];
        double S = trainStopStationList.get(index + 1).getLocation() - trainStopStationList.get(index).getLocation();
        for (int m = 0; m < forwardBeans.size(); m++) {
            Rundata fBean = forwardBeans.get(m);
            for (int n = 0; n < reverseBeans.size(); n++) {
                Rundata rBean = reverseBeans.get(n);
                if ((Math.abs(fBean.getSpeed() - rBean.getSpeed()) <= FACTOR) && (Math.abs(S - fBean.getDistance() - rBean.getDistance()) <= (FACTOR / 60))) {
                    flag[0] = m;// 正向
                    flag[1] = n;// 反向
                    // System.out.println(index+"合并成功  p-->"+m+", q-->"+n);
                    // System.out.println("fSpeed()="+fBean.getSpeed()+",rSpeed()="+rBean.getSpeed()+", S="+S+", fDistance()="+fBean.getDistance()+", rDistance()="+rBean.getDistance());
                    // break;
                    return flag;
                }
            }
        }
        return flag;
    }

    /**
     *  批量保存合并之后的list
     * @param routeId
     * @param rundataList
     */
    public void saveMergedData(int routeId, ArrayList<Rundata> rundataList) {
        // 合并数据
        // this.listsMerger();
        ArrayList<String> sqlList = new ArrayList<String>();
        String sql = "";
        for (Rundata rundata : rundataList) {
            sql = "INSERT INTO train_rundata(id, runtime, speed, location, POWER, tractionForce, brakeForce, eleBrakeForce, airForce, otherForce, routeid) VALUES(NULL, "
                  + rundata.getRuntime()
                  + ", "
                  + rundata.getSpeed()
                  + ", "
                  + rundata.getLocation()
                  + ", "
                  + rundata.getPower()
                  + ", "
                  + rundata.getTractionForce()
                  + ", "
                  + rundata.getBrakeForce()
                  + ", "
                  + rundata.getEleBrakeForce()
                  + ", "
                  + rundata.getAirForce()
                  + ", "
                  + rundata.getOtherForce() + ", " + routeId + ");";
            sqlList.add(sql);
        }
        // 批量保存
        sqlHelper.batchInsert(sqlList);
    }

    /**
     * 2014.10.21 移除所有不停站的车站（用于获取stopTime）
     */
    public ArrayList<TrainStopStation> removeNoStopStation(ArrayList<TrainStopStation> stationList) {
        ArrayList<TrainStopStation> stopStationList = new ArrayList<TrainStopStation>(stationList);
        for (int i = 0; i < stopStationList.size(); i++) {
            TrainStopStation bean = stopStationList.get(i);
            if (bean.getStopTIme() == 0) { //此站不停
                stopStationList.remove(i);
            }
        }
        return stopStationList;
    }

    /**
     *  打印合并之后的数据
     * @param rundataList
     */
    public void displayMergedList(ArrayList<Rundata> rundataList) {
        String str = "";
        ArrayList<String> strList = new ArrayList<String>();
        for (int j = 0; j < rundataList.size(); j++) {
            Rundata rundata = rundataList.get(j);
            str = rundata.getRuntime() + "		" + rundata.getSpeed() + "		" + rundata.getDistance() + "		" + rundata.getLocation() + "		" + rundata.getPower();
            strList.add(str);
        }
        WriteIntoTxt.writeIntoTxt(strList, "CRH_11.3_合并数据.txt");
        System.out.println("合并完成");
    }

}
