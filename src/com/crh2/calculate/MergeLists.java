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
 * �ϲ�����List�����ϲ�rundataBackBeansList��rundataBeansList
 * @author huhui
 *
 */
public class MergeLists {

    /**
     * ����List���min time
     */
    private ArrayList<ArrayList<Rundata>> mtForwardList        = null, mtReverseList = null;
    private ArrayList<TrainStopStation>   trainStopStationList = null;
    /**
     * �м�վ����ͣվ�ĳ�վ��Ϣ
     */
    private ArrayList<TrainStopStation>   stopStationList      = null;
    /**
     * ����List���fixed time
     */
    private ArrayList<Rundata>            ftForwardList        = null, ftReverseList = null;
    /**
     * ����List���specified time energy
     */
    private ArrayList<Rundata>            stEnergyForwardList  = null, stEnergyReverseList = null;
    /**
     * �ϲ�֮������ݴ���rundataMergedList��
     */
    public static ArrayList<Rundata>      mtRundataMergedList  = null, rtEnergyRundataMergedList = null, ftRundataMergedList = null;
    private SQLHelper                     sqlHelper            = new SQLHelper();

    /**
     * �ж��������Ƿ���ȵĳ���
     */
    private final double                  FACTOR               = 2.7;

    /**
     * ���캯������ʼ�����������fixed time��
     */
    public MergeLists(int routeId, ArrayList<Rundata> forwardList, ArrayList<Rundata> reverseList) {
        this.ftForwardList = forwardList;
        this.ftReverseList = reverseList;
        ftRundataMergedList = new ArrayList<Rundata>();
        fixedTimeListsMerger();
    }

    /**
     * ���캯������ʼ�����������specified time energy��
     */
    public MergeLists(ArrayList<Rundata> forwardList, ArrayList<Rundata> reverseList, int routeId) {
        this.stEnergyForwardList = forwardList;
        this.stEnergyReverseList = reverseList;
        rtEnergyRundataMergedList = new ArrayList<Rundata>();
        specifiedTimeListsMerger();
    }

    /**
     * ���캯������ʼ�����������min time��
     */
    public MergeLists(ArrayList<ArrayList<Rundata>> forwardList, ArrayList<ArrayList<Rundata>> reverseList, ArrayList<TrainStopStation> stationList, int routeId) {
        this.mtForwardList = forwardList;
        this.mtReverseList = reverseList;
        Collections.reverse(this.mtReverseList);// ��ת
        this.trainStopStationList = stationList;
        this.stopStationList = this.removeNoStopStation(stationList);
        mtRundataMergedList = new ArrayList<Rundata>(2000);
        minTimeListsMerger();
    }

    /**
     * fixed time �ϲ����� 2014.11.3
     */
    public void fixedTimeListsMerger() {
        //�ϲ���������
        for (Rundata bean : ftForwardList) {
            bean.setTractionForce(bean.getTractionForce() / 1000.0);
            bean.setAirForce(AirFrictionMinTime.getAirFriction(bean.getSpeed()) / 1000.0);
            bean.setOtherForce(Math.abs(BrakeForceMinTime.getOtherForce(bean.getCp()) / 1000.0));
            ftRundataMergedList.add(bean);
        }

        double E = 0;
        //Ѱ���������ݣ��Ӻ���ǰ��һ��power��Ϊ0��ֵ
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
     * specified time energy �ϲ����� 2014.10.25
     */
    public void specifiedTimeListsMerger() {
        //�ϲ���������
        for (Rundata bean : stEnergyForwardList) {
            //            bean.setTractionForce(TractionForceMinTime.getTractionForce(bean.getSpeed()) / 1000.0);
            bean.setAirForce(AirFrictionMinTime.getAirFriction(bean.getSpeed()) / 1000.0);
            bean.setOtherForce(Math.abs(BrakeForceMinTime.getOtherForce(bean.getCp()) / 1000.0));
            rtEnergyRundataMergedList.add(bean);
        }

        double E = 0;
        //Ѱ���������ݣ��Ӻ���ǰ��һ��power��Ϊ0��ֵ
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
     * �ϲ�ForwardList��ReverseList
     */
    public void listsMerger() {
        int[] flag = findIndex();
        double power = stEnergyForwardList.get(flag[0]).getPower();
        if (flag[0] != 0 && flag[1] != 0) {
            for (int m = 0; m <= flag[0]; m++) {// ��������
                Rundata bean = stEnergyForwardList.get(m);
                bean.setTractionForce(TractionForceMinTime.getTractionForce(bean.getSpeed()) / 1000.0);
                bean.setAirForce(AirFrictionMinTime.getAirFriction(bean.getSpeed()) / 1000.0);
                bean.setOtherForce(Math.abs(BrakeForceMinTime.getOtherForce(bean.getCp()) / 1000.0));
                mtRundataMergedList.add(bean);
            }
            for (int n = flag[1]; n >= 0; n--) {// ��������
                Rundata bean = stEnergyReverseList.get(n);
                power -= bean.getPower();// ��������
                bean.setPower(power);
                bean.setAirForce(AirFrictionMinTime.getAirFriction(bean.getSpeed()) / 1000.0);
                bean.setBrakeForce(BrakeForceMinTime.getManulBrakingForce(bean.getSpeed()) / 1000.0);
                bean.setEleBrakeForce(BrakeForceMinTime.getElecBrakeForce(bean.getSpeed()) / 1000.0);
                bean.setOtherForce(Math.abs(BrakeForceMinTime.getOtherForce(bean.getCp()) / 1000.0));
                mtRundataMergedList.add(bean);
            }
        } else {
            System.out.println("�ϲ�forwardList��reverseListʧ��");
        }
        for (int time = 0; time < mtRundataMergedList.size(); time++) {
            mtRundataMergedList.get(time).setRuntime(time);
        }
    }

    /**
     * Ѱ�Һϲ����ݵĵ�
     */
    public int[] findIndex() {
        ArrayList<Rundata> forwardBeans = stEnergyForwardList;
        ArrayList<Rundata> reverseBeans = stEnergyReverseList;
        int[] flag = new int[2];
        double S = TrainAttribute.CRH_DESTINATION_LOCATION;//ȫ�̾���
        for (int m = 0; m < forwardBeans.size(); m++) {
            Rundata fBean = forwardBeans.get(m);
            for (int n = 0; n < reverseBeans.size(); n++) {
                Rundata rBean = reverseBeans.get(n);
                if ((Math.abs(fBean.getSpeed() - rBean.getSpeed()) <= FACTOR) && (Math.abs(S - fBean.getLocation() - rBean.getDistance()) <= (FACTOR / 60))) {
                    flag[0] = m;// ����
                    flag[1] = n;// ����
                    // System.out.println(index+"�ϲ��ɹ�  p-->"+m+", q-->"+n);
                    // System.out.println("fSpeed()="+fBean.getSpeed()+",rSpeed()="+rBean.getSpeed()+", S="+S+", fDistance()="+fBean.getDistance()+", rDistance()="+rBean.getDistance());
                    // break;
                    return flag;
                }
            }
        }
        return flag;
    }

    /**
     * min time �ϲ�forwardList��reverseList
     */
    public void minTimeListsMerger() {
        for (int i = 0; i < mtForwardList.size(); i++) {
            int[] flag = minTimeFindIndex(i);
            double power = mtForwardList.get(i).get(flag[0]).getTractionPower();

            if (flag[0] != 0 && flag[1] != 0) {
                for (int m = 0; m <= flag[0]; m++) {// ��������
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
                for (int n = flag[1]; n >= 0; n--) {// ��������
                    Rundata bean = mtReverseList.get(i).get(n);
                    Rundata rBean = mtReverseList.get(i).get(flag[1] - n);
                    reversePower = power - rBean.getComBrakeForcePower();// ��������
                    bean.setPower(reversePower);
                    bean.setElecBrakeForcePower(reverseElecBrakePower[flag[1] - n]);
                    bean.setAirForce(AirFrictionMinTime.getAirFriction(bean.getSpeed()) / 1000.0);
                    bean.setBrakeForce(BrakeForceMinTime.getManulBrakingForce(bean.getSpeed()) / 1000.0);
                    bean.setEleBrakeForce(BrakeForceMinTime.getElecBrakeForce(bean.getSpeed()) / 1000.0);
                    bean.setOtherForce(Math.abs(BrakeForceMinTime.getOtherForce(bean.getCp()) / 1000.0));
                    mtRundataMergedList.add(bean);
                }
                if (i != mtForwardList.size() - 1) {// ���һ��վ����Ҫͣվ������վ����Ҫͣ
                    double location = mtReverseList.get(i).get(0).getLocation();
                    for (int k = 0; k < stopStationList.get(i).getStopTIme() - 1; k++) {
                        Rundata bean = new Rundata();
                        bean.setLocation(location);
                        bean.setPower(reversePower);
                        mtRundataMergedList.add(bean);// ��վͣ��
                    }
                }
            } else {
                System.out.println("�ϲ�forwardList��reverseListʧ��");
            }
        }
        for (int time = 0; time < mtRundataMergedList.size(); time++) {
            mtRundataMergedList.get(time).setRuntime(time);
        }
    }

    /**
     * min time Ѱ�Һϲ����ݵĵ�
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
                    flag[0] = m;// ����
                    flag[1] = n;// ����
                    // System.out.println(index+"�ϲ��ɹ�  p-->"+m+", q-->"+n);
                    // System.out.println("fSpeed()="+fBean.getSpeed()+",rSpeed()="+rBean.getSpeed()+", S="+S+", fDistance()="+fBean.getDistance()+", rDistance()="+rBean.getDistance());
                    // break;
                    return flag;
                }
            }
        }
        return flag;
    }

    /**
     *  ��������ϲ�֮���list
     * @param routeId
     * @param rundataList
     */
    public void saveMergedData(int routeId, ArrayList<Rundata> rundataList) {
        // �ϲ�����
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
        // ��������
        sqlHelper.batchInsert(sqlList);
    }

    /**
     * 2014.10.21 �Ƴ����в�ͣվ�ĳ�վ�����ڻ�ȡstopTime��
     */
    public ArrayList<TrainStopStation> removeNoStopStation(ArrayList<TrainStopStation> stationList) {
        ArrayList<TrainStopStation> stopStationList = new ArrayList<TrainStopStation>(stationList);
        for (int i = 0; i < stopStationList.size(); i++) {
            TrainStopStation bean = stopStationList.get(i);
            if (bean.getStopTIme() == 0) { //��վ��ͣ
                stopStationList.remove(i);
            }
        }
        return stopStationList;
    }

    /**
     *  ��ӡ�ϲ�֮�������
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
        WriteIntoTxt.writeIntoTxt(strList, "CRH_11.3_�ϲ�����.txt");
        System.out.println("�ϲ����");
    }

}
