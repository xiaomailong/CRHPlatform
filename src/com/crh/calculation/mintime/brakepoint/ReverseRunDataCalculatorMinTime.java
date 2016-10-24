package com.crh.calculation.mintime.brakepoint;

import java.util.ArrayList;

import com.crh.calculation.mintime.AirFrictionMinTime;
import com.crh.calculation.mintime.BrakeForceMinTime;
import com.crh.calculation.mintime.NetCurrent;
import com.crh2.calculate.TrainAttribute;
import com.crh2.javabean.Curve;
import com.crh2.javabean.Rundata;
import com.crh2.javabean.Slope;
import com.crh2.javabean.StationInfo;
import com.crh2.javabean.TrainStopStation;
import com.crh2.util.Arith;

/**
 * 2014.8.10 计算从各个车站反向计算得到刹车点
 * @author huhui
 */
public class ReverseRunDataCalculatorMinTime {

    private ReverseOtherResistance reverseOtherResistance          = null;
    /**
     * 用于保存RundataBack的javabean
     */
    private ArrayList<Rundata>     rundataBackBeansList            = null;
    /**
     * 车站信息
     */
    private ArrayList<StationInfo> stationList;
    private int                    count                           = 0;
    private NetCurrent             netCurrent                      = null;
    private double                 globalActualElecBrakeForcePower = 0;

    /**
     * 构造函数，初始化数据
     * @param slopeList
     * @param curveList
     * @param stationList
     * @param netCurrent
     */
    public ReverseRunDataCalculatorMinTime(ArrayList<Slope> slopeList, ArrayList<Curve> curveList, ArrayList<StationInfo> stationList, NetCurrent netCurrent) {
        reverseOtherResistance = new ReverseOtherResistance(slopeList, curveList);
        this.stationList = stationList;
        this.netCurrent = netCurrent;
    }

    /**
     * 保存RundataBack的javabean
     */
    public void saveRundataBackBeans(int runtime, double speed, double distance, double location, double comBrakePower, double elecBrakePower, double cp,
                                     double tractionGridCurrent, double comBrakeGridCurrent, double acceleration, double actualElecBrakeForcePower) {
        Rundata rundata = new Rundata();
        rundata.setRuntime(runtime);
        rundata.setSpeed(speed);
        rundata.setDistance(distance);
        rundata.setLocation(location);
        rundata.setCp(cp);
        rundata.setComBrakeForcePower(comBrakePower);
        rundata.setElecBrakeForcePower(elecBrakePower);
        rundata.setTractionGridCurrent(tractionGridCurrent);
        rundata.setComBrakeGridCurrent(comBrakeGridCurrent);
        rundata.setAcceleration(acceleration);
        rundata.setActualElecBrakeForcePower(actualElecBrakeForcePower);
        rundataBackBeansList.add(rundata);
    }

    /**
     * 用于得到rundataBackBeansList最后一个数据的位置，提供给正向计算使用
     * @return
     */
    public double getStopPoint() {
        return Arith.sub(TrainAttribute.CRH_DESTINATION_LOCATION, rundataBackBeansList.get(rundataBackBeansList.size() - 1).getDistance());
    }

    /**
     * 判断是否匀速，如果是，则Cp=0，否则正常计算Cp
     * @param Cp
     * @param cp
     * @param currentSpeed
     * @param lastSpeed
     * @return
     */
    public double constantSpeedCalCp(double Cp, double cp, double currentSpeed, double lastSpeed) {
        double printCp = 0;
        if (currentSpeed != lastSpeed) {//如果不是匀速
            return Cp - cp;
        } else {//如果是匀速，则Cp=0
            return printCp;
        }
    }

    /**
     * 反向计算
     * @param station
     * @return
     */
    public ArrayList<Rundata> calculatorRundataBack(TrainStopStation station) {
        int i = 0;
        double currentSpeed = 0;// 相当于Vi+1
        double lastSpeed = 0;// 相当于Vi
        double Si = 0, S = 0;// 相当于Si+1
        String str = "";// 写入txt中的内容
        double cp = 0;// 因为阻力需要修改的cp值
        double slopeCp = 0, curveCp = 0;// 分别用于记录因为坡道和弯道所修正的Cp值
        ArrayList<String> strList = new ArrayList<String>();//用于保存需要打印的数据
        double comBrakePower = 0;//综合阻力做功
        double elecBrakePower = 0;//电制动力
        double comBrakeForce = 0, location = 0, tractionGridCurrent = 0, comBrakeGridCurrent = 0, acceleration = 0, actualElecBrakeForcePower = 0, g = 9.8;
        rundataBackBeansList = new ArrayList<Rundata>();
        int isSpeedLimited = 0;
        double elecBrakeForce = 0;//电制动力
        while (true) {
            location = station.getLocation() - Si;
            str = i + count + "		" + currentSpeed + "		" + Si + "		" + location + "		" + comBrakePower;
            this.saveRundataBackBeans(i + count, currentSpeed, Si, location, comBrakePower, elecBrakePower, cp, tractionGridCurrent, comBrakeGridCurrent,
                acceleration, actualElecBrakeForcePower);
            currentSpeed = VnBpMinTime.calSpeed(currentSpeed, TrainAttribute.CRH_CAL_TIME_DIVISION, cp);
            slopeCp = ReverseOtherResistance.bySlope(Si);
            double[] paras = ReverseOtherResistance.byCurve(currentSpeed, Si);
            currentSpeed = paras[0];
            curveCp = paras[1];
            isSpeedLimited = (int) paras[2];
            cp = slopeCp + curveCp;
            Si = SnBpMinTime.calShift(currentSpeed, lastSpeed, Si);

            if (isSpeedLimited == 0) {//正常情况
                elecBrakeForce = BrakeForceMinTime.getElecBrakeForce(currentSpeed);
            } else if (isSpeedLimited == 1) {//限速情况
                elecBrakeForce = cp * AirFrictionMinTime.m * g / 1000000 + AirFrictionMinTime.getAirFriction(currentSpeed);
            }

            strList.add(str);

            i++;
            acceleration = BrakeForceMinTime.getBrakingAcceleration(currentSpeed, 9);//8级制动
            comBrakeForce = BrakeForceMinTime.getComBrakingForce(currentSpeed);
            comBrakeGridCurrent = netCurrent.getComBrakeForceNetCurrent(comBrakeForce, currentSpeed);
            tractionGridCurrent = comBrakeGridCurrent;
            comBrakePower += comBrakeForce * (Si - S);//计算阻力做功
            elecBrakePower += elecBrakeForce * (Si - S) * 0.85;
            System.out.println("Si=" + Si + ", isSpeedLimited=" + isSpeedLimited + ", location=" + location + ",currentSpeed=" + currentSpeed
                               + ", elecBrakeForce=" + elecBrakeForce + ", elecBrakePower=" + elecBrakePower);
            actualElecBrakeForcePower = globalActualElecBrakeForcePower + elecBrakePower;
            lastSpeed = currentSpeed;
            S = Si;
            if (lastSpeed >= TrainAttribute.CRH_MAX_SPEED) {
                count = i;
                globalActualElecBrakeForcePower = elecBrakePower + globalActualElecBrakeForcePower;
                break;
            }
        }
        //		WriteIntoTxt.writeIntoTxt(strList, "CRH_10.21_反向计算.txt");
        return rundataBackBeansList;
    }
}
