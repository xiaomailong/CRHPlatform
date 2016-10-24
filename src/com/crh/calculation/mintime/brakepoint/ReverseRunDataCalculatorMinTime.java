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
 * 2014.8.10 ����Ӹ�����վ�������õ�ɲ����
 * @author huhui
 */
public class ReverseRunDataCalculatorMinTime {

    private ReverseOtherResistance reverseOtherResistance          = null;
    /**
     * ���ڱ���RundataBack��javabean
     */
    private ArrayList<Rundata>     rundataBackBeansList            = null;
    /**
     * ��վ��Ϣ
     */
    private ArrayList<StationInfo> stationList;
    private int                    count                           = 0;
    private NetCurrent             netCurrent                      = null;
    private double                 globalActualElecBrakeForcePower = 0;

    /**
     * ���캯������ʼ������
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
     * ����RundataBack��javabean
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
     * ���ڵõ�rundataBackBeansList���һ�����ݵ�λ�ã��ṩ���������ʹ��
     * @return
     */
    public double getStopPoint() {
        return Arith.sub(TrainAttribute.CRH_DESTINATION_LOCATION, rundataBackBeansList.get(rundataBackBeansList.size() - 1).getDistance());
    }

    /**
     * �ж��Ƿ����٣�����ǣ���Cp=0��������������Cp
     * @param Cp
     * @param cp
     * @param currentSpeed
     * @param lastSpeed
     * @return
     */
    public double constantSpeedCalCp(double Cp, double cp, double currentSpeed, double lastSpeed) {
        double printCp = 0;
        if (currentSpeed != lastSpeed) {//�����������
            return Cp - cp;
        } else {//��������٣���Cp=0
            return printCp;
        }
    }

    /**
     * �������
     * @param station
     * @return
     */
    public ArrayList<Rundata> calculatorRundataBack(TrainStopStation station) {
        int i = 0;
        double currentSpeed = 0;// �൱��Vi+1
        double lastSpeed = 0;// �൱��Vi
        double Si = 0, S = 0;// �൱��Si+1
        String str = "";// д��txt�е�����
        double cp = 0;// ��Ϊ������Ҫ�޸ĵ�cpֵ
        double slopeCp = 0, curveCp = 0;// �ֱ����ڼ�¼��Ϊ�µ��������������Cpֵ
        ArrayList<String> strList = new ArrayList<String>();//���ڱ�����Ҫ��ӡ������
        double comBrakePower = 0;//�ۺ���������
        double elecBrakePower = 0;//���ƶ���
        double comBrakeForce = 0, location = 0, tractionGridCurrent = 0, comBrakeGridCurrent = 0, acceleration = 0, actualElecBrakeForcePower = 0, g = 9.8;
        rundataBackBeansList = new ArrayList<Rundata>();
        int isSpeedLimited = 0;
        double elecBrakeForce = 0;//���ƶ���
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

            if (isSpeedLimited == 0) {//�������
                elecBrakeForce = BrakeForceMinTime.getElecBrakeForce(currentSpeed);
            } else if (isSpeedLimited == 1) {//�������
                elecBrakeForce = cp * AirFrictionMinTime.m * g / 1000000 + AirFrictionMinTime.getAirFriction(currentSpeed);
            }

            strList.add(str);

            i++;
            acceleration = BrakeForceMinTime.getBrakingAcceleration(currentSpeed, 9);//8���ƶ�
            comBrakeForce = BrakeForceMinTime.getComBrakingForce(currentSpeed);
            comBrakeGridCurrent = netCurrent.getComBrakeForceNetCurrent(comBrakeForce, currentSpeed);
            tractionGridCurrent = comBrakeGridCurrent;
            comBrakePower += comBrakeForce * (Si - S);//������������
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
        //		WriteIntoTxt.writeIntoTxt(strList, "CRH_10.21_�������.txt");
        return rundataBackBeansList;
    }
}
