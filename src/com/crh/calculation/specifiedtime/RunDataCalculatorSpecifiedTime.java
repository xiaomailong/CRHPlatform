package com.crh.calculation.specifiedtime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.crh.calculation.mintime.AirFrictionMinTime;
import com.crh.calculation.mintime.BrakeForceMinTime;
import com.crh.calculation.mintime.CpMinTime;
import com.crh.calculation.mintime.NetCurrent;
import com.crh.calculation.mintime.OtherResistance;
import com.crh.calculation.mintime.SnMinTime;
import com.crh.calculation.mintime.TractionForceMinTime;
import com.crh.calculation.mintime.TractionLevelForceMinTime;
import com.crh.calculation.mintime.VnMinTime;
import com.crh.calculation.mintime.brakepoint.ReverseOtherResistance;
import com.crh.service.MinTimeSimulationService;
import com.crh.service.TractionConfPanelService;
import com.crh2.calculate.MergeLists;
import com.crh2.calculate.TrainAttribute;
import com.crh2.javabean.Curve;
import com.crh2.javabean.Rundata;
import com.crh2.javabean.Slope;
import com.crh2.javabean.StationInfo;
import com.crh2.javabean.TrainElectricBrake;
import com.crh2.javabean.TrainTractionConf;
import com.crh2.javabean.V0V1E;
import com.crh2.service.DataService;
import com.crh2.util.MyUtillity;
import com.crh2.util.WriteIntoTxt;

/**
 * 2014.10.22 “固定时分仿真”  计算运行数据，即randata
 * @author huhui
 *
 */
public class RunDataCalculatorSpecifiedTime {

    private OtherResistance                       otherResistance      = null;
    private int                                   routeId;
    /**
     * 提供线路数据
     */
    private DataService                           dataService          = new DataService();
    private ArrayList<Slope>                      slopeList            = null;
    private ArrayList<Curve>                      curveList            = null;
    private ArrayList<StationInfo>                stationList          = null;
    private int                                   trainCategoryId;
    private double                                brakeLevel, maxSpeed;
    private String                                tractionLevel;
    /**
     * 给定初始运行时间1800s，再转成小时
     */
    private double                                T;
    /**
     * 给定总里程
     */
    private double                                S;

    /**
     * 保存Rundata的javabean
     */
    private ArrayList<Rundata>                    rundataBeansList     = null;
    /**
     * 2014.10.25 保存匀速时的Rundata
     */
    private ArrayList<Rundata>                    avgRundataBeansList  = null;
    /**
     * 2014.10.25 保存惰行时的Rundata
     */
    private ArrayList<Rundata>                    slowRundataBeansList = null;

    /**
     * 反向计算
     */
    private ReverseRunDataCalculatorSpecifiedTime brakePointCalculator = null;

    /**
     * 合并正反向计算结果
     */
    private MergeLists                            mergeLists           = null;
    /**
     * 计算网流
     */
    private NetCurrent                            netCurrent           = null;

    /**
     * 是否保存计算数据
     */
    public static final int                       CALCULATE_MODE       = 0, CAL_SAVE_MODE = 1;

    public static V0V1E                           resultBean           = null;

    /**
     * 构造函数，用于初始化list
     * @param routeName
     * @param trainNum
     * @param trainCategoryId
     * @param tractionLevel
     * @param brakeLevel
     * @param maxSpeed
     * @param runTime
     */
    public RunDataCalculatorSpecifiedTime(String routeName, String trainNum, int trainCategoryId, String tractionLevel, double brakeLevel, double maxSpeed,
                                          double runTime) {
        routeId = MinTimeSimulationService.getRouteIdByName(routeName);
        // 初始化各个list
        this.slopeList = dataService.getSlopeData(routeId);
        this.curveList = dataService.getCurveData(routeId);
        this.stationList = dataService.getStationInfoData(routeId);
        // 初始化其它几个参数
        this.trainCategoryId = trainCategoryId;
        this.tractionLevel = tractionLevel;
        this.brakeLevel = brakeLevel;
        this.maxSpeed = maxSpeed;
        this.T = runTime / 3600;//转成小时
        TrainAttribute.CRH_MAX_SPEED = this.maxSpeed;
        TrainAttribute.CRH_DESTINATION_LOCATION = stationList.get(stationList.size() - 1).getLocation();//终点站位置
        this.S = TrainAttribute.CRH_DESTINATION_LOCATION;
        netCurrent = new NetCurrent(trainCategoryId);//2014.12.1 计算网流
        initFormulas();
        // 初始化OtherResistance 即附加阻力
        otherResistance = new OtherResistance(slopeList, curveList);

        //反向计算
        brakePointCalculator = new ReverseRunDataCalculatorSpecifiedTime(slopeList, curveList, netCurrent);
    }

    public static void main(String[] args) {
        RunDataCalculatorSpecifiedTime runDataCalculatorMinTime = new RunDataCalculatorSpecifiedTime("京津线（下行）", "G99", 57, "", 9, 300, 1800);
        runDataCalculatorMinTime.calculateFullData();
    }

    /**
     * 计算所有站之间的运行数据
     */
    public void calculateFullData() {
        V0V1E minBean = calculateV0AndV1();
        resultBean = minBean;
        //		System.out.println(minBean.getV0()+", "+minBean.getV1()+", "+minBean.getE());
        rundataBeansList = new ArrayList<Rundata>(); //初始化List用于保存所有正向运行数据
        avgRundataBeansList = new ArrayList<Rundata>();//保存匀速数据
        slowRundataBeansList = new ArrayList<Rundata>();//保存惰行数据

        //计算并保存所有正向运行数据
        calculateT0AndS0(minBean.getV0(), CAL_SAVE_MODE);
        calAirAndResistancePower(minBean.getS0End(), minBean.getS1End(), minBean.getV0(), CAL_SAVE_MODE);
        calculateT2AndS2(minBean.getV0(), minBean.getV1(), minBean.getS3(), CAL_SAVE_MODE);

        //合并前三段运行数据
        mergeForwardRundataList();

        ArrayList<Rundata> forwardRundata = rundataBeansList;

        brakePointCalculator.calculateT3AndS3(minBean.getV1(), CAL_SAVE_MODE);
        ArrayList<Rundata> reverseRundata = brakePointCalculator.getRundataBackBeansList();
        mergeLists = new MergeLists(forwardRundata, reverseRundata, routeId);
    }

    /**
     *计算第一个v0和v1 
     */
    public double calculateV0() {
        double v0 = S / T;//初始化v0等于平均速度(km/h)
        double s0, t0, s1, t1, s2 = 0, t2, s3, t3, v1;
        //第一个循环计算出第一个v1
        while (true) {
            //1.t0和s0
            double[] runPara0 = calculateT0AndS0(v0, CALCULATE_MODE);
            //2.t3和s3
            double[] runPara3 = brakePointCalculator.calculateT3AndS3(v0, CALCULATE_MODE);
            t0 = runPara0[0];//s
            s0 = runPara0[1];
            t3 = runPara3[0];//s
            s3 = runPara3[1];
            //3.t1
            t1 = calculateT1(s0, s2, s3, v0);//h
            //4.计算式
            double tempT = T - ((t0 + t3) / 3600 + t1);
            if (tempT >= 0) {
                v1 = v0;
                //				System.out.println("v1 = "+v1);
                return v0;//第一次v0 = v1;
            }
            v0++;
        }
    }

    /**
     * 返回最小的v0 v1 E对
     * @return
     */
    public V0V1E calculateV0AndV1() {
        double v0 = calculateV0();
        double v1 = v0;
        double s0, t0, E0, s1, t1, E1, s2 = 0, t2, s3, t3, E3, E;
        ArrayList<V0V1E> speedList = new ArrayList<V0V1E>();
        //循环计算出v0和v1组合
        while (v0 <= TrainAttribute.CRH_MAX_SPEED) {
            v1 = v0;
            while (true) {
                //1.t0和s0
                double[] runPara0 = calculateT0AndS0(v0, CALCULATE_MODE);
                //2.t3和s3
                --v1;
                double[] runPara3 = brakePointCalculator.calculateT3AndS3(v1, CALCULATE_MODE);
                t0 = runPara0[0];//s
                s0 = runPara0[1];
                E0 = runPara0[2];
                t3 = runPara3[0];//s
                s3 = runPara3[1];
                E3 = runPara3[2];

                //3.t2和s2
                double[] runPara2 = calculateT2AndS2(v0, v1, s3, CALCULATE_MODE);
                t2 = runPara2[0];//s
                s2 = runPara2[1];

                //4.t1
                t1 = calculateT1(s0, s2, s3, v0);//h
                //5.计算式
                double tempT = T - ((t0 + t2 + t3) / 3600 + t1);
                if (tempT <= 0) {
                    double s1EndLocation = S - s2 - s3;
                    E1 = calAirAndResistancePower(s0, s1EndLocation, v0, CALCULATE_MODE);
                    E = calculateE(E0, E1, E3);
                    //					System.out.println("E0 = "+E0+", E1 = "+E1+", E3 = "+E3+", E = "+E);
                    //					System.out.println("v0 = "+ v0 +", v1 = "+ v1 + ", E = " + E);
                    speedList.add(new V0V1E(v0, v1, E, s0, s1EndLocation, s3));
                    break;
                }
            }
            v0++;
        }
        return Collections.min(speedList, new Comparator<V0V1E>() {
            @Override
            public int compare(V0V1E o1, V0V1E o2) {
                if (o1.getE() > o2.getE()) {
                    return 1;
                } else if (o1.getE() < o2.getE()) {
                    return -1;
                }
                return 0;
            }
        });
    }

    /**
     * 给牵引力、空气阻力、制动力公式赋值
     */
    public void initFormulas() {
        this.initTractionForce();
        this.initAirFriction();
        this.initBrakeForce();
    }

    /**
     * 判断当前在分相区内是否有电
     * @param Si
     */
    public void hasElectricityNow(double Si) {
        CpMinTime.hasElectricity = true;//2014.10.22 始终有电 即不需要分相区
    }

    /**
     * 保存Rundata的javabean
     * @param runtime
     * @param speed
     * @param location
     * @param tractionPower
     * @param power
     * @param cp
     * @param tractionGridCurrent
     * @param comBrakeGridCurrent
     * @param acceleration
     * @param tractionForce
     */
    public void saveRundataBeans(int runtime, double speed, double location, double tractionPower, double power, double cp, double tractionGridCurrent,
                                 double comBrakeGridCurrent, double acceleration, double tractionForce) {
        Rundata rundata = new Rundata();
        rundata.setRuntime(runtime);
        rundata.setSpeed(speed);
        rundata.setLocation(location);
        rundata.setCp(cp);
        rundata.setTractionPower(tractionPower);
        rundata.setPower(power);
        rundata.setTractionGridCurrent(tractionGridCurrent);
        rundata.setComBrakeGridCurrent(comBrakeGridCurrent);
        rundata.setAcceleration(acceleration);
        rundata.setTractionForce(tractionForce);
        rundataBeansList.add(rundata);
    }

    /**
     * 保存匀速行Rundata的javabean
     * @param runtime
     * @param speed
     * @param location
     * @param tractionPower
     * @param cp
     * @param tractionGridCurrent
     * @param comBrakeGridCurrent
     * @param acceleration
     * @param tractionForce
     */
    public void saveAvgRundataBeans(int runtime, double speed, double location, double tractionPower, double cp, double tractionGridCurrent,
                                    double comBrakeGridCurrent, double acceleration, double tractionForce) {
        Rundata rundata = new Rundata();
        rundata.setRuntime(runtime);
        rundata.setSpeed(speed);
        rundata.setLocation(location);
        rundata.setCp(cp);
        rundata.setTractionPower(tractionPower);
        rundata.setTractionGridCurrent(tractionGridCurrent);
        rundata.setComBrakeGridCurrent(comBrakeGridCurrent);
        rundata.setAcceleration(acceleration);
        rundata.setTractionForce(tractionForce);
        avgRundataBeansList.add(rundata);
    }

    /**
     * 保存惰行Rundata的javabean
     * @param runtime
     * @param speed
     * @param location
     * @param power
     * @param cp
     * @param tractionGridCurrent
     * @param comBrakeGridCurrent
     * @param acceleration
     * @param tractionForce
     */
    public void saveSlowRundataBeans(int runtime, double speed, double location, double power, double cp, double tractionGridCurrent,
                                     double comBrakeGridCurrent, double acceleration, double tractionForce) {
        Rundata rundata = new Rundata();
        rundata.setRuntime(runtime);
        rundata.setSpeed(speed);
        rundata.setLocation(location);
        rundata.setCp(cp);
        rundata.setPower(power);
        rundata.setTractionGridCurrent(tractionGridCurrent);
        rundata.setComBrakeGridCurrent(comBrakeGridCurrent);
        rundata.setAcceleration(acceleration);
        rundata.setTractionForce(tractionForce);
        slowRundataBeansList.add(rundata);
    }

    /**
     * 2014.10.25 合并前三段运行数据
     */
    public void mergeForwardRundataList() {
        double e0 = rundataBeansList.get(rundataBeansList.size() - 1).getTractionPower();
        //合并匀速运行时的数据
        for (Rundata bean : avgRundataBeansList) {
            double power = bean.getTractionPower() + e0;
            bean.setPower(power);
            bean.setTractionPower(power);
            rundataBeansList.add(bean);
        }
        //合并惰行时的数据
        Collections.reverse(slowRundataBeansList);
        for (Rundata bean : slowRundataBeansList) {
            rundataBeansList.add(bean);
        }
    }

    /**
     * 设置牵引力
     */
    public void initTractionForce() {
        if (tractionLevel.equals("最大牵引(无级)")) {
            TrainTractionConf trainTractionConf = MinTimeSimulationService.getTrainTractionConf(trainCategoryId);
            TractionForceMinTime.Fst = trainTractionConf.getFst();
            TractionForceMinTime.F1 = trainTractionConf.getF1();
            TractionForceMinTime.F2 = trainTractionConf.getF2();
            TractionForceMinTime.v1 = trainTractionConf.getV1();
            TractionForceMinTime.v2 = trainTractionConf.getV2();
            TractionForceMinTime.P1 = trainTractionConf.getP1();
            TractionForceMinTime.mode = 1;
            TractionForceMinTime.tractionType = 0;
        } else {
            TractionLevelForceMinTime.trainTractionLevelConf = TractionConfPanelService.getTrainTractionLevelConf(trainCategoryId);
            TractionForceMinTime.tractionType = 1;
            TractionForceMinTime.level = MyUtillity.getNumFromString(tractionLevel);
        }
    }

    /**
     * 设置空气阻力
     */
    public void initAirFriction() {
        double[] abc = MinTimeSimulationService.getAirFrictionParameters(trainCategoryId);
        AirFrictionMinTime.a = abc[0];
        AirFrictionMinTime.b = abc[1];
        AirFrictionMinTime.c = abc[2];
        AirFrictionMinTime.m = abc[4];
    }

    /**
     * 设置制动力
     */
    public void initBrakeForce() {
        BrakeForceMinTime.setBrakeList(MinTimeSimulationService.getTrainBrakeFactor(trainCategoryId));//制动力系数
        TrainElectricBrake trainElectricBrake = MinTimeSimulationService.getTrainElectricBrake(trainCategoryId);
        double[] para = MinTimeSimulationService.getSixParameters(trainCategoryId);//k1,k2,D,N,T,F2
        double k1 = para[0];
        double k2 = para[1];
        double D = para[2];
        double N = para[3];
        double T = para[4];
        BrakeForceMinTime.v1 = trainElectricBrake.getV1();
        BrakeForceMinTime.v2 = trainElectricBrake.getV2();
        BrakeForceMinTime.F2 = (N * trainElectricBrake.getP00() * 3.6) / BrakeForceMinTime.v2;
        BrakeForceMinTime.Fst = ((2 * N * k1 * k2 * T) / 1000) / D;
    }

    /**
     * 负责计算匀速阶段，空气阻力和附加阻力做功 = 牵引力做功
     * @param s1StartLocation 匀速开始坐标
     * @param s1EndLocation 匀速结束坐标
     * @param v 匀速时速度值
     * @return 空气阻力和附加阻力做工
     */
    public double calAirAndResistancePower(double s1StartLocation, double s1EndLocation, double v, int mode) {
        int i = 0;
        double currentSpeed = v;// 相当于Vi+1
        double lastSpeed = v;// 相当于Vi
        double Si = 0, S = 0;// 里程
        String str = "";// 写入txt中的内容
        double cp = 0;// 因为阻力需要修改的cp值
        double slopeCp = 0, curveCp = 0;// 分别用于记录因为坡道和弯道所修正的Cp值
        ArrayList<String> strList = new ArrayList<String>();//用于保存需要打印的数据
        double tractionForce = 0, tractionPower = 0, tractionGridCurrent = 0, comBrakeGridCurrent = 0, acceleration = 0;
        double location = 0;
        while (true) {
            location = s1StartLocation + Si;
            str = i + "	" + currentSpeed + "	" + Si + "		" + tractionPower;
            if (mode == CAL_SAVE_MODE) {
                this.saveAvgRundataBeans(i, currentSpeed, location, tractionPower, cp, tractionGridCurrent, comBrakeGridCurrent, acceleration, tractionForce);
            }
            this.hasElectricityNow(Si);
            slopeCp = OtherResistance.bySlope(location);// 坡道附加阻力
            double[] paras = OtherResistance.byCurve(currentSpeed, location);// 弯道附加
            curveCp = paras[1];
            cp = slopeCp + curveCp;
            Si = SnMinTime.calShift(currentSpeed, lastSpeed, Si);
            strList.add(str);

            i++;
            //            tractionForce = AirFrictionMinTime.getAirFriction(currentSpeed) + 9.8 * AirFrictionMinTime.m * cp;
            tractionForce = TractionForceMinTime.getTractionForce(lastSpeed);
            //            System.out.println("currentSpeed=" + currentSpeed + ", tractionForce=" + tractionForce);
            tractionGridCurrent = netCurrent.getTractionNetCurrent(tractionForce, currentSpeed);
            comBrakeGridCurrent = netCurrent.getComBrakeForceNetCurrent(0, currentSpeed);
            tractionPower += tractionForce * (Si - S);//计算做功
            lastSpeed = currentSpeed;
            S = Si;
            if (Si >= (s1EndLocation - s1StartLocation)) {
                break;
            }
        }
        return Math.abs(tractionPower);
    }

    /**
     * 负责计算t2和s2，即惰行阶段由v0->v1
     * 实际计算时，采用逆向计算，即v1加速到v0
     */
    public double[] calculateT2AndS2(double v0, double v1, double s3EndLocation, int mode) {

        int i = 0;
        double currentSpeed = v1;// 相当于Vi+1
        double lastSpeed = v1;// 相当于Vi
        double Si = 0;// 里程
        String str = "";// 写入txt中的内容
        double cp = 0;// 因为阻力需要修改的cp值
        double slopeCp = 0, curveCp = 0;// 分别用于记录因为坡道和弯道所修正的Cp值
        ArrayList<String> strList = new ArrayList<String>();//用于保存需要打印的数据
        double[] paraArray = new double[2];//0表示时间t，1表示里程s
        double tractionForce = 0, tractionPower = 0, power = 0, tractionGridCurrent = 0, comBrakeGridCurrent = 0, acceleration = 0;
        double location = 0;
        double s = 0;
        while (true) {
            s = Si + s3EndLocation;
            location = TrainAttribute.CRH_DESTINATION_LOCATION - s;
            if (mode == CAL_SAVE_MODE) {
                this.saveSlowRundataBeans(i, currentSpeed, location, 0, cp, tractionGridCurrent, comBrakeGridCurrent, acceleration, tractionForce);
            }
            str = i + "	" + currentSpeed + "	" + location;
            acceleration = CpMinTime.getAcceleretionSlow(currentSpeed);
            currentSpeed = VnMinTime.calSpeedSlow(currentSpeed, TrainAttribute.CRH_CAL_TIME_DIVISION, cp);//速度
            slopeCp = ReverseOtherResistance.bySlope(s);// 坡道附加阻力
            double[] paras = ReverseOtherResistance.byCurve(currentSpeed, s);// 弯道附加
            currentSpeed = paras[0];
            curveCp = paras[1];
            cp = slopeCp + curveCp;
            Si = SnMinTime.calShift(currentSpeed, lastSpeed, Si);
            strList.add(str);
            tractionGridCurrent = netCurrent.getTractionNetCurrent(tractionForce, currentSpeed);
            comBrakeGridCurrent = netCurrent.getComBrakeForceNetCurrent(0, currentSpeed);
            i++;
            lastSpeed = currentSpeed;
            if (currentSpeed >= v0) {
                paraArray[0] = i;
                paraArray[1] = Si;
                break;
            }
        }
        //		WriteIntoTxt.writeIntoTxt(strList, "10.23_t2_s2 _计算.txt");
        return paraArray;
    }

    /**
     * 负责计算t0和s0，即加速阶段
     * @param mode 0代表不保存计算数据，1代表保存计算数据
     */
    public double[] calculateT0AndS0(double v0, int mode) {
        int i = 0;
        double currentSpeed = 0;// 相当于Vi+1
        double lastSpeed = 0;// 相当于Vi
        double Si = 0, S = 0;// 里程
        String str = "";// 写入txt中的内容
        double cp = 0;// 因为阻力需要修改的cp值
        double slopeCp = 0, curveCp = 0;// 分别用于记录因为坡道和弯道所修正的Cp值
        ArrayList<String> strList = new ArrayList<String>();//用于保存需要打印的数据
        double tractionForce = 0, tractionPower = 0, power = 0, tractionGridCurrent = 0, comBrakeGridCurrent = 0, acceleration = 0;
        double[] paraArray = new double[3];//0表示时间t，1表示里程s，2表示能量E
        while (true) {
            str = i + "	" + currentSpeed + "	" + Si + "		" + tractionPower;
            if (mode == CAL_SAVE_MODE) {
                this.saveRundataBeans(i, currentSpeed, Si, tractionPower, power, cp, tractionGridCurrent, comBrakeGridCurrent, acceleration, tractionForce);
            }
            this.hasElectricityNow(Si);
            currentSpeed = VnMinTime.calSpeed(currentSpeed, TrainAttribute.CRH_CAL_TIME_DIVISION, cp);//速度
            slopeCp = OtherResistance.bySlope(Si);// 坡道附加阻力
            double[] paras = OtherResistance.byCurve(currentSpeed, Si);// 弯道附加
            currentSpeed = paras[0];
            curveCp = paras[1];
            cp = slopeCp + curveCp;
            Si = SnMinTime.calShift(currentSpeed, lastSpeed, Si);
            strList.add(str);

            i++;
            acceleration = CpMinTime.getAcceleration(currentSpeed);
            tractionForce = TractionForceMinTime.getTractionForce(currentSpeed);
            tractionGridCurrent = netCurrent.getTractionNetCurrent(tractionForce, currentSpeed);
            comBrakeGridCurrent = netCurrent.getComBrakeForceNetCurrent(0, currentSpeed);
            tractionPower += tractionForce * (Si - S);//计算做功
            power = tractionPower;
            lastSpeed = currentSpeed;
            S = Si;
            if (currentSpeed >= v0) {
                paraArray[0] = i;
                paraArray[1] = Si;
                paraArray[2] = tractionPower;
                break;
            }
        }
        //		WriteIntoTxt.writeIntoTxt(strList, "10.23_t0_s0 _计算.txt");
        return paraArray;
    }

    public double calculateE(double E0, double E1, double E3) {
        return E0 + E1 - 0.85 * E3;
    }

    /**
     *负责计算t1，即匀速阶段时间 
     */
    public double calculateT1(double s0, double s2, double s3, double v0) {
        return (S - s0 - s2 - s3) / v0;
    }

    /**
     *负责计算惰行的起始点坐标location，用于求限速 
     */
    public double calculateS1End(double s3) {
        return S - s3;
    }

    /**
     * 运行数据计算
     * @return
     */
    public ArrayList<Rundata> calculateRunData() {
        int i = 0;
        double currentSpeed = 0;// 相当于Vi+1
        double lastSpeed = 0;// 相当于Vi
        double Si = 0, S = 0;// 里程
        String str = "";// 写入txt中的内容
        double cp = 0;// 附加阻力和
        double slopeCp = 0, curveCp = 0;// 坡道和弯道附加阻力
        ArrayList<String> strList = new ArrayList<String>();//用于保存需要打印的数据
        double tractionPower = 0;//能量
        rundataBeansList = new ArrayList<Rundata>();
        while (true) {
            str = i + "	" + currentSpeed + "	" + Si + "		" + tractionPower;
            //			this.saveRundataBeans(i, currentSpeed, Si, tractionPower , tractionPower, cp);
            this.hasElectricityNow(Si);//分相区计算部分
            currentSpeed = VnMinTime.calSpeed(currentSpeed, TrainAttribute.CRH_CAL_TIME_DIVISION, cp);//速度
            slopeCp = OtherResistance.bySlope(Si);// 坡道附加阻力
            double[] paras = OtherResistance.byCurve(currentSpeed, Si);// 弯道附加
            currentSpeed = paras[0];
            curveCp = paras[1];
            cp = slopeCp + curveCp;//坡道和弯道的附加阻力
            Si = SnMinTime.calShift(currentSpeed, lastSpeed, Si);//里程

            strList.add(str);

            i++;
            tractionPower += TractionForceMinTime.getTractionForce(currentSpeed) * (Si - S);//计算做功
            lastSpeed = currentSpeed;
            S = Si;
            if (Si >= TrainAttribute.CRH_DESTINATION_LOCATION) {
                break;
            }
        }
        System.out.println("固定时分_正向计算_完成");
        WriteIntoTxt.writeIntoTxt(strList, "CRH_10.23_固定时分_正向计算.txt");
        return rundataBeansList;
    }

}
